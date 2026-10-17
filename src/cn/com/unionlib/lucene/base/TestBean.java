package cn.com.unionlib.lucene.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.util.Version;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.lucene.factory.AnalyzerFactory;
import cn.com.unionlib.lucene.factory.MainConnFactory;

public class TestBean {

	public static void main(String[] args) throws Exception{
//		SysConfigInfo.initConfig();
//		TestBean tb = new TestBean();
		
		Analyzer a = AnalyzerFactory.instance().getSmartAnalyzer();
		List<String> words = SegmentWord.displayToken("首都国际机场", a);
		for(String s : words){
			System.out.println(s);
		}
	}
	
	
	
	/**
	 * 通过多个关键词进行检索；即先将用户输入的关键词分词后再检索，因此实用性更大
	 *
	 * @author MaChao
	 * @date Jun 30, 2015
	 *
	 * @param pageBean
	 * @param keyArr
	 * @return
	 * @throws Exception
	 */
	public List<ResMainBean> getDatabean(PageBean pageBean, Map<String, String> params)
			throws Exception {
		String key = params.get("key"); 
		if(CommonUtil.isNotNull(key) == false){
			return getIndexList(pageBean);
		}
		int beginRows = (pageBean.getCurrentPage() - 1) * pageBean.getPageSize();  //起始记录数
		int endRows = beginRows + pageBean.getPageSize(); //结束记录数
		
		IndexSearcher indexSearcher = MainConnFactory.instance().getSearcher();
		
		Analyzer analyzer = AnalyzerFactory.instance().getSmartAnalyzer();
		//2：通过BooleanQuery查询，将各个属性分别封装成Query添加到BooleanQuery；
		//   如果条件较多逻辑较复杂的情况使用该方式更为灵活
		BooleanQuery query = new BooleanQuery();
		if(params.containsKey("key")){
			Query titleQuery = new QueryParser(Version.LUCENE_46, "content", analyzer).parse(key);
			query.add(titleQuery, Occur.SHOULD);
		}
		if(params.containsKey("resLib")){
			String resLib = params.get("resLib");
			Query resLibQuery = new QueryParser(Version.LUCENE_46, "resLibrary", analyzer).parse(resLib);
			query.add(resLibQuery, Occur.MUST);
		}
		if(params.containsKey("resType")){
			String resType = params.get("resType");
			Query resTypeQuery = new QueryParser(Version.LUCENE_46, "resType", analyzer).parse(resType);
			query.add(resTypeQuery, Occur.MUST);
		}
		TopDocs topDocs = indexSearcher.search(query, endRows);
		
		int count = topDocs.totalHits; //结果总数
		pageBean.setCountRows(count);
		pageBean.setCountPage((count-1) / pageBean.getPageSize() + 1);
		
//		int resultNum = count;
//		System.out.println(key + " 共找到：" + resultNum + " 条数据");
		
//		if(resultNum < 1){
//			return null;
//		}
		
		
		List<ResMainBean> dataList = new ArrayList<ResMainBean>();
		/**
		 * 默认为每页12条，当超过20页，即结束条数为240的时候，就采用searchAfter查询
		 * 不过目前我并没看懂它的优秀，反而是多进行了一次查询而已。
		 */
		ResMainBean data = null;
		ScoreDoc[] docs = null;
		if(endRows > 240){
			ScoreDoc afterDoc = topDocs.scoreDocs[beginRows-1];
			topDocs = indexSearcher.searchAfter(afterDoc, query, pageBean.getPageSize());
			
			for(ScoreDoc doc : topDocs.scoreDocs){
				Document document = indexSearcher.doc(doc.doc);
				data = ObjectConvert.documentToData(document, query);
				if(data != null){
					dataList.add(data);
				}
			}
		}
		else{
			docs = topDocs.scoreDocs;
			ScoreDoc doc = null;
			for(int i=beginRows, len=docs.length; i<len; i++){
				doc = docs[i];
				Document document = indexSearcher.doc(doc.doc);
				data = ObjectConvert.documentToData(document, query);
				if(data != null){
					dataList.add(data);
				}
			}
		}
		return dataList;
	}
	
	/**
	 * 根据分页对象获取所有索引中的资源(即不带任何检索条件)
	 *
	 * @author MaChao
	 * @date Jun 6, 2016
	 *
	 * @param page
	 * @return
	 * @throws IOException 
	 */
	public List<ResMainBean> getIndexList(PageBean page) throws IOException{
		int beginRows = (page.getCurrentPage() - 1) * page.getPageSize();  //起始记录数
		int endRows = beginRows + page.getPageSize(); //结束记录数
		
		IndexSearcher indexSearcher = MainConnFactory.instance().getSearcher();
		
//		SortField sortField = new SortField("title", SortField.Type.STRING, true);
		SortField sortField = new SortField("lastDate", SortField.Type.LONG, true);
		Sort sort = new Sort(sortField);
		
		Query query = new MatchAllDocsQuery();
		TopDocs topDocs = indexSearcher.search(query, null, endRows, sort);
		
		int count = topDocs.totalHits; //结果总数
		page.setCountRows(count);
		page.setCountPage((count-1) / page.getPageSize() + 1);
		
		int s = topDocs.scoreDocs.length;
		
		if(s < 1){
			return null;
		}
		
		List<ResMainBean> dataList = new ArrayList<ResMainBean>();
		
		/**
		 * 默认为每页12条，当超过20页，即结束条数为240的时候，就采用searchAfter查询
		 * 不过目前我并没看懂它的优秀，反而是多进行了一次查询而已。
		 */
		if(endRows > 240){
			int num = beginRows - 1;
			ScoreDoc afterDoc = topDocs.scoreDocs[num];
			topDocs = indexSearcher.searchAfter(afterDoc, query, page.getPageSize(), sort);
			for(ScoreDoc doc : topDocs.scoreDocs){
				Document document = indexSearcher.doc(doc.doc);
				dataList.add(ObjectConvert.documentToData(document, query));
			}
		}else{
			//当页数小于20页时依然采用之前的分页方式
			ScoreDoc[] scoreDoc = topDocs.scoreDocs;
			for(int i=beginRows; i<s; i++){
				Document document = indexSearcher.doc(scoreDoc[i].doc);
				dataList.add(ObjectConvert.documentToData(document, query));
			}
		}
		return dataList;
	}
}
