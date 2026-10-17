package cn.com.unionlib.lucene.dao;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;

import cn.com.adtech.riss2.bso.platform.PlatformBasicService;
import cn.com.adtech.riss2.controller.console.IndexManagerController;
import cn.com.adtech.riss2.controller.platform.ProgressController;
import cn.com.adtech.riss2.controller.platform.ResCommonController;
import cn.com.adtech.riss2.domain.IdsInformation;
import cn.com.adtech.riss2.domain.text.ResTextResource;
import cn.com.unionlib.lucene.base.ObjectConvert;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.bean.ResPageBean;
import cn.com.unionlib.lucene.factory.GroupConnFactory;
import cn.com.unionlib.lucene.factory.PageConnFactory;
import cn.com.unionlib.lucene.factory.MainConnFactory;

import com.jbetter.framework.session.Pagination;

public class AllDataIndexServiceImp extends IndexCommonService implements IAllDataIndexService{

	@Override
	public void saveResToIndex(HttpServletRequest request) {
		String commonCon = PlatformBasicService.getSearchCommonConditionByHql("rsda", false);
		//如果只需要创建有文档源文件的资源，则打开以下几个条件即可
//		commonCon += " and rsda.rsdaFileLocalName is not null";
//		commonCon += " and rsda.rsdaFileType is not null";
//		commonCon += " and (rsda.rsdaFileType='doc' or rsda.rsdaFileType='docx' or rsda.rsdaFileType='pdf' or rsda.rsdaFileType='xls' or rsda.rsdaFileType='xlsx' or rsda.rsdaFileType='txt')";
		String countHql = "select count(rsda.rsdaId) from ResSourceData rsda left join rsda.resSubjectType rsty where " + commonCon;
		
		int countNum = 0;
		try {
			String countStr = getSingleData(countHql);
			countNum = Integer.parseInt(countStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(countNum < 1){
			System.out.println("没有可创建索引的资源——————————");
			return;
		}
		System.out.println("一共有：" + countNum + " 条资源准备创建索引--------------------------------");
		
		//为了提高效果，只查询出了资源需要创建成索引的字段，如果需要添加字段根据情况扩展即可；
		String columns = getSearchColumn();
		String hql = "select " + columns + " from ResSourceData rsda left join rsda.resSubjectType rsty where " + commonCon;
		
		//考虑到库里面的资源可能会较多，因此不要一次性查出来进行操作；需采用按批次操作，如同分页
		int pageNum = (countNum - 1) / BATCH_SIZE + 1; //一共多少批次
		Pagination p = null;
		int No = 0;
		
		//主索引
		IndexWriter mainIndexWriter = null;
		//分页索引
		IndexWriter pageIndexWriter = null;
		//聚类索引
		DirectoryTaxonomyWriter taxoWriter = null;
		
		ResMainBean dataBean = null;
		Document mainDoc = null;
		Document pageDoc = null;
		try {
			//先把所有写权限释放掉
			Object[] resData = null;	
			
			//一键索引时 重建写入器
			mainIndexWriter = MainConnFactory.instance().getWriter();
			pageIndexWriter = PageConnFactory.instance().getWriter();
			taxoWriter = GroupConnFactory.instance().getTaxonomyWriter();
			
			//删除所有资源索引文件
			mainIndexWriter.deleteDocuments(new Term("dataType", ResMainBean.DATA_TYPE_RES));
			mainIndexWriter.commit();
			
			//删除子页索引
			pageIndexWriter.deleteAll();
			pageIndexWriter.commit();
			
			//循环资源重新创建索引
			for(int i=0; i<pageNum; i++){
				p = new Pagination();
				p.setCurrentPage(i);
				//每次添加索引的条数
				p.setPageResults(BATCH_SIZE);
				
				List<Object[]> resList = getPageHibernateTemplate().find(p, hql);
				
				//封面按照新的模式进行重新解析
				resList = ResCommonController.parseCoverList(request, resList, 9, 11, 12);
				
				for(int resi=0,ress=resList.size(); resi<ress; resi++){
					No++;
					resData = resList.get(resi);
					System.out.println("RESOURCE INDEX STATUS   BATCH：" + (i+1) + "/" + pageNum + "    No：" + No);
					// 更新进度条
					ProgressController.updateProgress(IndexManagerController.INDEX_CREATE_PROGRESS_KEY, No, countNum, "文件资源:【"+resData[1]+"】处理中");
					//将所有元数据信息追加到全文字段
					dataBean = resourceToDatabean(resData);
					
					mainDoc = ObjectConvert.dataToDocument(dataBean);
					mainIndexWriter.addDocument(mainDoc);
					
					//添加子页索引
					List<ResPageBean> pageList = dataBean.getPageList();
					if(pageList != null && pageList.size() > 0){
						for(ResPageBean page : pageList){
							pageDoc = ObjectConvert.dataToPageDoc(page);
							pageIndexWriter.addDocument(pageDoc);
						}
					}
					
				}
				mainIndexWriter.commit();
				pageIndexWriter.commit();
				taxoWriter.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ProgressController.sendError(IndexManagerController.INDEX_CREATE_PROGRESS_KEY, "文件资源索引创建失败!");
			MainConnFactory.instance().unlockWriter();
		}
	}

	@Override
	public void saveTextResToIndex() {
		int countNum = 0;
		String countHql = "select count(rtre.rtreId) from ResTextResource rtre where rtre.rtreUsed=1";
		try {
			String countStr = getSingleData(countHql);
			countNum = Integer.parseInt(countStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(countNum < 1){
			System.out.println("没有可创建索引的文本资源——————————");
			return;
		}
		System.out.println("一共有：" + countNum + " 条文本资源准备创建索引--------------------------------");
		
		String hql = "from ResTextResource rtre where rtre.rtreUsed=1 order by rtre.rtreId ASC";
		
		//考虑到库里面的资源可能会较多，因此不要一次性查出来进行操作；需采用按批次操作，如同分页
		int pageNum = (countNum - 1) / BATCH_SIZE + 1; //一共多少批次
		Pagination p = null;
		int No = 0;
		
		IndexWriter indexWriter = null;
		//获取聚类写索引
		DirectoryTaxonomyWriter taxoWriter = null;
		ResMainBean dataBean = null;
		Document doc = null;
		try {
			//先把所有写权限释放掉
			ResTextResource resData = null;	
			
			//一键索引时 重建写入器
			indexWriter = MainConnFactory.instance().getWriter();
			taxoWriter = GroupConnFactory.instance().getTaxonomyWriter();
			
			//删除所有文本资源索引文件
			indexWriter.deleteDocuments(new Term("dataType", ResMainBean.DATA_TYPE_TEXT));
			indexWriter.commit();
			for(int i=0; i<pageNum; i++){
				p = new Pagination();
				p.setCurrentPage(i);
				//每次添加索引的条数
				p.setPageResults(BATCH_SIZE);
				
				List<ResTextResource> resList = getPageHibernateTemplate().find(p, hql);
				for(int resi=0,ress=resList.size(); resi<ress; resi++){
					No++;
					resData = resList.get(resi);
					System.out.println("TEXT INDEX STATUS   BATCH：" + (i+1) + "/" + pageNum + "    No：" + No);
					// 更新进度条
					ProgressController.updateProgress(IndexManagerController.INDEX_CREATE_PROGRESS_KEY, No, countNum, "文本资源:【"+resData.getRtreTitle()+"】处理中");
					
					//将所有元数据信息追加到全文字段
					dataBean = textResToDatabean(resData);
					
					doc = ObjectConvert.dataToDocument(dataBean);
					indexWriter.addDocument(doc);
				}
				indexWriter.commit();
				taxoWriter.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ProgressController.sendError(IndexManagerController.INDEX_CREATE_PROGRESS_KEY, "文本资源索引创建失败!");
			MainConnFactory.instance().unlockWriter();
		}
	}
	
	@Override
	public void saveInfoToIndex() {
		int countNum = 0;
		String countHql = "select count(idsi.idsiId) from IdsInformation idsi";
		try {
			String countStr = getSingleData(countHql);
			countNum = Integer.parseInt(countStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(countNum < 1){
			System.out.println("没有可创建索引的资讯信息——————————");
			return;
		}
		System.out.println("一共有：" + countNum + " 条资讯信息准备创建索引--------------------------------");
		
		String hql = "from IdsInformation idsi order by idsi.idsiId ASC";
		
		//考虑到库里面的资源可能会较多，因此不要一次性查出来进行操作；需采用按批次操作，如同分页
		int pageNum = (countNum - 1) / BATCH_SIZE + 1; //一共多少批次
		Pagination p = null;
		int No = 0;
		
		IndexWriter indexWriter = null;
		//获取聚类写索引
		DirectoryTaxonomyWriter taxoWriter = null;
		ResMainBean dataBean = null;
		Document doc = null;
		try {
			//先把所有写权限释放掉
			IdsInformation info = null;	
			
			//一键索引时 重建写入器
			indexWriter = MainConnFactory.instance().getWriter();
			taxoWriter = GroupConnFactory.instance().getTaxonomyWriter();
			
			//删除所有文本资源索引文件
			indexWriter.deleteDocuments(new Term("dataType", ResMainBean.DATA_TYPE_INFO));
			indexWriter.commit();
			for(int i=0; i<pageNum; i++){
				p = new Pagination();
				p.setCurrentPage(i);
				//每次添加索引的条数
				p.setPageResults(BATCH_SIZE);
				
				List<IdsInformation> infoList = getPageHibernateTemplate().find(p, hql);
				for(int idsi=0,idss=infoList.size(); idsi<idss; idsi++){
					No++;
					info = infoList.get(idsi);
					System.out.println("INFO INDEX STATUS   BATCH：" + (i+1) + "/" + pageNum + "    No：" + No);
					// 更新进度条
					ProgressController.updateProgress(IndexManagerController.INDEX_CREATE_PROGRESS_KEY, No, countNum, "资讯信息:【"+info.getIdsiTitle()+"】处理中");
					
					dataBean = infoToDatabean(info);
					doc = ObjectConvert.dataToDocument(dataBean);
					indexWriter.addDocument(doc);
				}
				indexWriter.commit();
				taxoWriter.commit();
			}
		} catch (Exception e) {
			e.printStackTrace();
			ProgressController.sendError(IndexManagerController.INDEX_CREATE_PROGRESS_KEY, "资讯信息索引创建失败!");
			MainConnFactory.instance().unlockWriter();
		}
	}

	@Override
	public void saveAlldataToIndex(HttpServletRequest request) {
		saveResToIndex(request);
		saveTextResToIndex();
		saveInfoToIndex();
	}

	
	/**
	 * 获取资源总数
	 * @return 资源,文本资源,资讯 以“,”分割的字符串返回
	 * @throws IOException 
	 */
	@Override
	public String getResCounts() throws IOException {
		IndexSearcher searcher = MainConnFactory.instance().getSearcher();
		Query query = new TermQuery(new Term("dataType", ResMainBean.DATA_TYPE_RES));
		TopDocs docs = searcher.search(query, 1);
		int resCount = docs.totalHits;
		
		query = new TermQuery(new Term("dataType", ResMainBean.DATA_TYPE_TEXT));
		docs = searcher.search(query, 1);
		int textCount = docs.totalHits;
		
		query = new TermQuery(new Term("dataType", ResMainBean.DATA_TYPE_INFO));
		docs = searcher.search(query, 1);
		int infoCount = docs.totalHits;
		
		IndexSearcher pageSearcher = PageConnFactory.instance().getSearcher();
		Query q = new MatchAllDocsQuery();
		docs = pageSearcher.search(q, 1);
		int pageCount = docs.totalHits;
		
		return resCount + "," + textCount + "," + infoCount + "," + pageCount;
	}
	
	@Override
	public int deleteInvalidIndex() throws IOException {
		IndexSearcher indexSearcher = MainConnFactory.instance().getSearcher();

		// 获取索引资源总条数
		int dbSize = indexSearcher.getIndexReader().numDocs();
		ProgressController.updateProgress(IndexManagerController.INDEX_CREATE_PROGRESS_KEY, 0, dbSize, "数据库信息加载中...");
		if(dbSize < 1) {
			return 0;
		}
		// 索引不大的清空下直接查询全部索引
		TopDocs topDocs = indexSearcher.search(new MatchAllDocsQuery(), dbSize);
		
		String id = null;
		ScoreDoc doc = null;
		Document document = null;
		ScoreDoc[] docs = topDocs.scoreDocs;
		Set<Long> idSet = findDatabaseIds();	// 获取数据库的id
		List<String> idList = new ArrayList<String>(dbSize);
		for(int i=0, len = docs.length; i<len; i++){
			doc = docs[i];
			if(doc == null) {
				continue;
			}
			document = indexSearcher.doc(doc.doc);
			id = document.get("id");
			// 更新进度条
			ProgressController.updateProgress(IndexManagerController.INDEX_CREATE_PROGRESS_KEY, i, len, document.get("title"));
			if(StringUtils.isBlank(id)) {
				continue;
			}
			if(idSet.contains(Long.parseLong(id))) {
				continue;
			}
			idList.add(id);
		}
		int updateNum = idList.size();
		new LuceneDaoServiceImp().deleteBatchData(idList);
		return updateNum;
	}
	
	public Set<Long> findDatabaseIds(){
		Set<Long> set = new HashSet<Long>();
		// 资源库资源ID
		set.addAll(this.getHibernateTemplate().find("select rsda.rsdaId from ResSourceData rsda where "+PlatformBasicService.getSearchCommonConditionByHql("rsda", false)));
		// 文本库资源ID
		set.addAll(this.getHibernateTemplate().find("select rtre.rtreId from ResTextResource rtre where rtre.rtreUsed=1"));
		// 咨询信息ID
		set.addAll(this.getHibernateTemplate().find("select count(idsi.idsiId) from IdsInformation idsi"));
		return set;
	}
}
