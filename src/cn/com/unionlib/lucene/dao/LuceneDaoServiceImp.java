package cn.com.unionlib.lucene.dao;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import cn.com.unionlib.lucene.base.ObjectConvert;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.bean.ResPageBean;
import cn.com.unionlib.lucene.factory.GroupConnFactory;
import cn.com.unionlib.lucene.factory.MainConnFactory;
import cn.com.unionlib.lucene.factory.PageConnFactory;

/**
 * 
 * @description lucene数据操作对象
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Jun 23, 201510:35:24 AM
 *
 */
public class LuceneDaoServiceImp implements ILuceneDaoService{


	/**
	 * 新增一个数据对象到索引 
	 *
	 * @author MaChao
	 * @date Jul 1, 2015
	 *
	 * @param dataBean ：已经封装好的数据对象
	 * @return
	 */
	@Override
	public boolean addDatabean(ResMainBean dataBean) {
		IndexWriter indexWriter = MainConnFactory.instance().getWriter();
		if(indexWriter == null){
			System.out.println("索引器创建失败！");
			return false;
		}
		try {
			//子页索引
			IndexWriter pageIndexWriter = PageConnFactory.instance().getWriter();
			
			//获取聚类写索引
			DirectoryTaxonomyWriter taxoWriter = GroupConnFactory.instance().getTaxonomyWriter();
			Document d = ObjectConvert.dataToDocument(dataBean);
			indexWriter.addDocument(d);
			
			Document pageDoc = null;
			//添加子页索引
			List<ResPageBean> pageList = dataBean.getPageList();
			if(pageList != null && pageList.size() > 0){
				for(ResPageBean page : pageList){
					pageDoc = ObjectConvert.dataToPageDoc(page);
					pageIndexWriter.addDocument(pageDoc);
				}
			}
			
			//indexWriter.optimize();
			indexWriter.commit();
			taxoWriter.commit();
			System.out.println("索引添加成功！");
			return true;
		} catch (Exception e) {
			System.out.println("索引添加失败：" + e.getMessage());
			e.printStackTrace();
			MainConnFactory.instance().unlockWriter();
			return false;
		}
	}


	/**
	 * 新增多个数据对象到索引 
	 *
	 * @author MaChao
	 * @date Jul 1, 2015
	 *
	 * @param beanList : 已经封装好的数据对象集合
	 * @return
	 */
	@Override
	public boolean addMultiDatabean(List<ResMainBean> beanList) {
		IndexWriter mainIndexWriter = MainConnFactory.instance().getWriter();
		if(mainIndexWriter == null){
			System.out.println("索引器创建失败！");
			return false;
		}
		try {
			Document mainDoc = null;
			Document pageDoc = null;
			//子页索引
			IndexWriter pageIndexWriter = PageConnFactory.instance().getWriter();
			//获取聚类写索引
			DirectoryTaxonomyWriter taxoWriter = GroupConnFactory.instance().getTaxonomyWriter();
			ResMainBean mainBean = null;
			for(int i=0,s=beanList.size(); i<s; i++){
				mainBean = beanList.get(i);
				mainDoc = ObjectConvert.dataToDocument(mainBean);
				mainIndexWriter.addDocument(mainDoc);
				
				//添加子页索引
				List<ResPageBean> pageList = mainBean.getPageList();
				if(pageList != null && pageList.size() > 0){
					for(ResPageBean page : pageList){
						pageDoc = ObjectConvert.dataToPageDoc(page);
						pageIndexWriter.addDocument(pageDoc);
					}
				}
			}
			
			//indexWriter.optimize();
			mainIndexWriter.commit();
			taxoWriter.commit();
			pageIndexWriter.commit();
			System.out.println("索引添加成功！");
			return true;
		} catch (Exception e) {
			System.out.println("索引添加失败：" + e.getMessage());
			e.printStackTrace();
			MainConnFactory.instance().unlockWriter();
			return false;
		}
	}
	
	/**
	 * 修改数据对象
	 *
	 * @author MaChao
	 * @date Jul 1, 2015
	 *
	 * @param dataBean
	 * @return
	 */
	@Override
	public boolean updateDatabean(ResMainBean dataBean) {
		//修改前关闭读取
		IndexWriter indexWriter = MainConnFactory.instance().getWriter();
		if(indexWriter == null){
			System.out.println("索引器创建失败！");
			return false;
		}
			Term t = new Term("id", dataBean.getId().toString());
		try {
			//获取聚类写索引
			DirectoryTaxonomyWriter taxoWriter = GroupConnFactory.instance().getTaxonomyWriter();
			Document doc = ObjectConvert.dataToDocument(dataBean);
			indexWriter.updateDocument(t, doc);
			taxoWriter.commit();
			indexWriter.commit();
			
			//-----以下为子页索引处理------------/
			IndexWriter pageIndexWriter = PageConnFactory.instance().getWriter();
			pageIndexWriter.deleteDocuments(new Term("resid", dataBean.getId().toString()));
			//重新添加子页索引
			Document pageDoc = null;
			List<ResPageBean> pageList = dataBean.getPageList();
			if(pageList != null && pageList.size() > 0){
				for(ResPageBean page : pageList){
					pageDoc = ObjectConvert.dataToPageDoc(page);
					pageIndexWriter.addDocument(pageDoc);
				}
			}
			pageIndexWriter.commit();			
			//-----子页索引处理结束------------/
			
			System.out.println("索引修改成功");
			return true;
		} catch (Exception e) {
			System.out.println("索引修改错误：" + e.getMessage());
			e.printStackTrace();
			MainConnFactory.instance().unlockWriter();
			return false;
		}
	}

	/**
	 * 根据数据ID删除数据对象
	 *
	 * @author MaChao
	 * @date Jul 1, 2015
	 *
	 * @param id
	 * @return
	 */
	@Override
	public boolean deleteDatabean(String id) {
		IndexWriter indexWriter = MainConnFactory.instance().getWriter();
		if(indexWriter == null ){
			System.out.println("索引器创建失败！");
			return false;
		}
		Term t = new Term("id", id);
		try {
			//删除资源主索引
			indexWriter.deleteDocuments(t);	
			indexWriter.commit();		
			
			//删除资源子页索引
			IndexWriter pageIndexWriter = PageConnFactory.instance().getWriter();
			pageIndexWriter.deleteDocuments(new Term("resid", id));
			pageIndexWriter.commit();
			
			System.out.println("删除索引成功");
			return true;
		} catch (Exception e) {
			System.out.println("删除索引错误：" + e.getMessage());
			e.printStackTrace();
			MainConnFactory.instance().unlockWriter();
			return false;
		}
	}


	/**
	 * 批量删除索引
	 * @param idlist
	 * @return
	 * @throws IOException 
	 */
	@Override
	public void deleteBatchData(List<String> idlist) throws IOException {
		if(idlist == null || idlist.size() < 1){
			return;
		}
		IndexWriter indexWriter = MainConnFactory.instance().getWriter();
		if(indexWriter == null ){
			System.out.println("索引器创建失败！");
			return;
		}
		
		IndexWriter pageIndexWriter = PageConnFactory.instance().getWriter();
		for(int i=0,s=idlist.size(); i<s; i++){
			Term t = new Term("id", idlist.get(i));
			indexWriter.deleteDocuments(t);
			
			//删除资源子页索引
			pageIndexWriter.deleteDocuments(new Term("resid", idlist.get(i)));
		}
		pageIndexWriter.commit();
		indexWriter.commit();		
	}	
}
