package cn.com.unionlib.lucene.dao;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.facet.params.FacetSearchParams;
import org.apache.lucene.facet.search.CountFacetRequest;
import org.apache.lucene.facet.search.FacetResult;
import org.apache.lucene.facet.search.FacetResultNode;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.ConstantScoreQuery;
import org.apache.lucene.queries.TermsFilter;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.Collector;
import org.apache.lucene.search.Scorer;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.util.Version;
import org.apache.lucene.util.FixedBitSet;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.unionlib.lucene.base.AnalyzerUtil;
import cn.com.unionlib.lucene.base.IoCommonUtil;
import cn.com.unionlib.lucene.base.ObjectConvert;
import cn.com.unionlib.lucene.base.SearchOccurrenceCounter;
import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.bean.ResPageBean;
import cn.com.unionlib.lucene.factory.AnalyzerFactory;
import cn.com.unionlib.lucene.factory.GroupConnFactory;
import cn.com.unionlib.lucene.factory.MainConnFactory;
import cn.com.unionlib.lucene.factory.PageConnFactory;

public class LuceneSearchServiceImp implements ILuceneSearchService{
	
	//需要做聚类的字符
//	private static String[] groupArr = {"dataType", "resLibrary", "resType", "subjectClass", "creator", "keyword", "publisher", "date"};
	
	//此处省略了数据分类、出版者、日期的聚类，只留下了最重要的几个聚类，如果有需要直接打开上面数组即可
	private static String[] groupArr = {"dataType", "resLibrary", "resType", "creator", "keyword"};
	
	
	public static void main(String[] args) throws IOException{
		LuceneSearchServiceImp lss = new LuceneSearchServiceImp();
		lss.testSearch();
	}
	
	public void testSearch() throws IOException{
		SysConfigInfo.initConfig();
		
		IndexSearcher indexSearcher = MainConnFactory.instance().getSearcher();
		TopDocs topDocs = indexSearcher.search(new MatchAllDocsQuery(), 100000);
		
		int i = 0;
		for(ScoreDoc doc : topDocs.scoreDocs){
			Document document = indexSearcher.doc(doc.doc);
			
			i++;
			System.out.println(i + "[" + document.get("dataType") + "]  lan：" + document.get("language") + "   ：" + document.get("title"));
		}
	}
	
	/**
	 * lucene一键检索
	 * @param page
	 * @param params
	 * @return
	 * @throws ParseException 
	 * @throws IOException 
	 */
	@Override
	public List<Object[]> luceneSearch(PageBean pageBean,
			Map<String, String> params) throws Exception {
		List<ResMainBean> dataList = searchData(pageBean, params);
		
		//结果转换
		List<Object[]> resultList = dataParser(dataList);
		
//		SearchResultBean resultBean = new SearchResultBean();
//		resultBean.setDataList(resultList);
//		resultBean.setGroupMap(groupMap);
		return resultList;
	}
	
	@Override
	public List<ResMainBean> luceneSearchForChildPage(PageBean pageBean,
			Map<String, String> params) throws Exception {
		List<ResMainBean> dataList = searchData(pageBean, params);
		if(dataList == null || dataList.size() < 1){
			return dataList;
		}
		//没有检索词则不进行子页检索
		if(!params.containsKey("key")){
			return dataList;
		}
		String key = params.get("key");
		
		//根据检索词检索每个资源具体出现的子页
		PageBean p = new PageBean(0, 10);
		Map<String, String> paramMap = null;
		for(ResMainBean res : dataList){
			paramMap = new LinkedHashMap<String, String>();
			paramMap.put("resId", res.getId().toString());
			paramMap.put("key", key);
			paramMap.put("match", params.get("match"));
			List<ResPageBean> plist = pageSearch(p, paramMap);
			if(plist != null && plist.size() > 0){
				for(ResPageBean page : plist){
					page.setTitleEncode(URLEncoder.encode(page.getTitle(), "utf-8"));
				}
				res.setPageList(plist);
			}
		}
		return dataList;
	}
	
	@Override
	public long[] getSearchResultCounts(Map<String, String> params) throws Exception {
		IndexSearcher main = MainConnFactory.instance().getSearcher();
		BooleanQuery parsed = parserQuery(params);
		Query query = parsed.clauses().isEmpty() ? new MatchAllDocsQuery() : parsed;
		final Set<String> resourceIds = new HashSet<String>();
		final FixedBitSet acceptedDocs = new FixedBitSet(main.getIndexReader().maxDoc());
		main.search(query, new Collector() {
			private AtomicReaderContext context;
			public void setScorer(Scorer scorer) { }
			public void setNextReader(AtomicReaderContext context) { this.context = context; }
			public boolean acceptsDocsOutOfOrder() { return true; }
			public void collect(int doc) throws IOException {
				String id = context.reader().document(doc).get("id");
				if (id != null && resourceIds.add(id)) acceptedDocs.set(context.docBase + doc);
			}
		});
		if (resourceIds.isEmpty()) return new long[] {0L, 0L};
		String key = params.get("key");
		if (!CommonUtil.isNotNull(key)) return new long[] {resourceIds.size(), 0L};
		// Apply all original filters to the accepted document set, but only count
		// positive KEY clauses. Library/type/date filters are not keyword occurrences.
		Map<String, String> keywordParams = new HashMap<String, String>();
		keywordParams.put("key", key);
		keywordParams.put("match", params.get("match"));
		if (params.containsKey("attr")) keywordParams.put("attr", params.get("attr"));
		long occurrences = SearchOccurrenceCounter.count(main, parserQuery(keywordParams), acceptedDocs);
		return new long[] {resourceIds.size(), occurrences};
	}

	private List<ResMainBean> searchData(PageBean pageBean,
			Map<String, String> params) throws Exception {
		int beginRows = (pageBean.getCurrentPage() - 1) * pageBean.getPageSize();  //起始记录数
		int endRows = beginRows + pageBean.getPageSize(); //结束记录数
		
		IndexSearcher indexSearcher = MainConnFactory.instance().getSearcher();
		
		BooleanQuery query = parserQuery(params);
		
		TopDocs topDocs = null;
		int cs = query.clauses().size();
		if(cs < 1){
			//如果一个检索条件都没有则检索所有
			topDocs = indexSearcher.search(new MatchAllDocsQuery(), endRows);
		}else{
			topDocs = indexSearcher.search(query, endRows);
		}
		
		
		int count = topDocs.totalHits; //结果总数
		pageBean.setCountRows(count);
		pageBean.setCountPage((count-1) / pageBean.getPageSize() + 1);
		
		List<ResMainBean> dataList = new ArrayList<ResMainBean>();
		
		/**
		 * 默认为每页12条，当超过20页，即结束条数为240的时候，就采用searchAfter查询
		 * 不过目前我并没看懂它的优秀，反而是多进行了一次查询而已。
		 */
		ResMainBean data = null;
		ScoreDoc[] docs = null;
		
		BooleanQuery redQuery = null; 
		if(params.containsKey("attr") && !params.get("attr").equals("title")&&params.get("key")!=null){
			//如果检索字段不为全文与题名，则需要将高亮query重置为title和content的query，
			//因此列表页面只展示title和content，没有展示其它字段，因此只能将这两个字段用来高亮
			Analyzer analyzer = AnalyzerFactory.instance().getSmartAnalyzer();
			redQuery = new BooleanQuery();
			String key = params.get("key");
			Query contentQuery = new QueryParser(Version.LUCENE_46, "content", analyzer).parse(key);
			
			redQuery.add(contentQuery, Occur.MUST);
		}else{
			redQuery = query;
		}
		
		
		if(endRows > 240){
			ScoreDoc afterDoc = topDocs.scoreDocs[beginRows-1];
			topDocs = indexSearcher.searchAfter(afterDoc, query, pageBean.getPageSize());
			for(ScoreDoc doc : topDocs.scoreDocs){
				Document document = indexSearcher.doc(doc.doc);
				data = ObjectConvert.documentToData(document, redQuery);
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
				data = ObjectConvert.documentToData(document, redQuery);
				if(data != null){
					dataList.add(data);
				}
			}
		}
		return dataList;
	}
	
	
	private List<Object[]> dataParser(List<ResMainBean> dataList){
		if(dataList == null || dataList.size() < 1){
			return null;
		}
		List<Object[]> resultList = new ArrayList<Object[]>();
		Object[] objArr = null;
		ResMainBean data = null;
		for(int i=0,s=dataList.size(); i<s; i++){
			data = dataList.get(i);
			
			objArr = new Object[13];
			objArr[0] = data.getId();
			objArr[1] = data.getTitle();
			objArr[2] = CommonUtil.nullToBlank(data.getContent());
			objArr[3] = data.getDataType();
			objArr[4] = data.getResType();
			objArr[5] = data.getCreator();
			objArr[6] = data.getPublisher();
			objArr[7] = data.getKeyword();
			objArr[8] = data.getDate();
			objArr[9] = data.getCoverUrl();
			objArr[10] = data.getResLibrary();
			objArr[11] = data.getSubjectClass();
			
			objArr[12] = data.getLanguage();
			resultList.add(objArr);
		}
		return resultList;
	}
	
	private BooleanQuery parserQuery(Map<String, String> params) throws ParseException{
		Analyzer analyzer = AnalyzerFactory.instance().getSmartAnalyzer();	//只按当前语种进行检索
//		Analyzer analyzer = AnalyzerFactory.instance().getSmartSameAnalyzer(); //开启同义词(跨语种)检索
		
		
		
		//2：通过BooleanQuery查询，将各个属性分别封装成Query添加到BooleanQuery；
		//   如果条件较多逻辑较复杂的情况使用该方式更为灵活
		BooleanQuery query = new BooleanQuery();
		// Server-resolved category membership applies to both result rows and occurrence counts.
		// TermsFilter supports large libraries without BooleanQuery's 1024-clause limit.
		if (params.containsKey("resourceIds")) {
			List<Term> ids = new ArrayList<Term>();
			for (String id : params.get("resourceIds").split(",")) if (!id.isEmpty()) ids.add(new Term("id", id));
			if (ids.isEmpty()) ids.add(new Term("id", "__no_category_resources__"));
			query.add(new ConstantScoreQuery(new TermsFilter(ids)), Occur.MUST);
		}
		if(params.containsKey("key")){
			String key = params.get("key"); 
			int match = Integer.parseInt(params.get("match"));
			String attr = null;
			
			if(params.containsKey("attr")){
				attr = params.get("attr");
				attr = parseAttr(attr);
			}
			if(CommonUtil.isNotNull(attr)){
				//分词存储字段，采用分词检索
				if(attr.equals("title")){
					if(match == 1){	//精确检索
						PhraseQuery titleQuery = new PhraseQuery();
						titleQuery.setSlop(key.length());
						List<String> keyList = AnalyzerUtil.displayToken(key);
						for (int i = 0, s = keyList.size(); i < s; i++) {
							titleQuery.add(new Term("title", keyList.get(i)));
						}
							
						query.add(titleQuery, Occur.MUST);	
					}else{
						QueryParser parser = new QueryParser(Version.LUCENE_46, attr, analyzer);
						
						if(match == 2){
							//半精确，采用AND:文章中必须包含所有检索词，但位置可在不同处
							parser.setDefaultOperator(Operator.AND);
						}else{
							//模糊，采用OR：包含其中一个词即可
							parser.setDefaultOperator(Operator.OR);
						}
						
						Query keyQuery = parser.parse(key);
						query.add(keyQuery, Occur.MUST);
					}
				}else{
					
					//未分词存储字段，采用term模式匹配
					Query keyQuery = new TermQuery(new Term(attr, key));
					query.add(keyQuery, Occur.MUST);
				}
				
				
			}else{
				if(match == 1){	//精确检索
					//短语查询，以下模式可基本实现精确检索
					BooleanQuery txtQuery = new BooleanQuery();
			        PhraseQuery titleQuery = new PhraseQuery();
			        titleQuery.setSlop(key.length());	//设定有几跳，表示中间存在一个单词
			        PhraseQuery contentQuery = new PhraseQuery();
			        contentQuery.setSlop(key.length());
			        
					List<String> keyList = AnalyzerUtil.displayToken(key);
					for(int i=0,s=keyList.size(); i<s; i++){
						titleQuery.add(new Term("title",keyList.get(i)));
						contentQuery.add(new Term("content",keyList.get(i)));
					}
					
					txtQuery.add(titleQuery, Occur.SHOULD);
					txtQuery.add(contentQuery, Occur.SHOULD);
					query.add(txtQuery, Occur.MUST);
				}else{
					//模糊检索
					//title与content采用两个BooleanQuery组合查询
//					BooleanQuery txtQuery = new BooleanQuery();
//					Query titleQuery = new QueryParser(Version.LUCENE_46, "title", analyzer).parse(key);
//					txtQuery.add(titleQuery, Occur.SHOULD);
//					Query contentQuery = new QueryParser(Version.LUCENE_46, "content", analyzer).parse(key);
//					txtQuery.add(contentQuery, Occur.SHOULD);
//					query.add(txtQuery, Occur.MUST);
					
					//title与content采用MultiFieldQueryParser多字段查询对象方式查询。与上面BooleanQuery效果相同
					String[] sCols = {"title", "content"};
					MultiFieldQueryParser  multiQueryParser = new MultiFieldQueryParser(Version.LUCENE_46, sCols, analyzer);
					if(match == 2){
						//半精确，采用AND:文章中必须包含所有检索词，但位置可在不同处
						multiQueryParser.setDefaultOperator(Operator.AND);
					}else{
						//模糊，采用OR：包含其中一个词即可
						multiQueryParser.setDefaultOperator(Operator.OR);
					}
					multiQueryParser.setPhraseSlop(0);
					Query multiQuery = multiQueryParser.parse(key);
					query.add(multiQuery, Occur.MUST);
				}
			}
			
		}
		if(params.containsKey("dataType")){
			String dataType = params.get("dataType");
			Query dataTypeQuery = new TermQuery(new Term("dataType", dataType));
			query.add(dataTypeQuery, Occur.MUST);
		}
		if(params.containsKey("resLib")){
			String resLib = params.get("resLib");
			Query resLibQuery = new TermQuery(new Term("resLibrary", resLib));
			query.add(resLibQuery, Occur.MUST);
		}
		if(params.containsKey("resType")){
			String resType = params.get("resType");
			Query resTypeQuery = new TermQuery(new Term("resType", resType));
			query.add(resTypeQuery, Occur.MUST);
		}
		if(params.containsKey("subjectClass")){
			String subjectClass = params.get("subjectClass");
			Query subjectClassQuery = new TermQuery(new Term("subjectClass", subjectClass));
			query.add(subjectClassQuery, Occur.MUST);
		}
		if(params.containsKey("creator")){
			String creator = params.get("creator");
			Query creatorQuery = new TermQuery(new Term("creator", creator));
			query.add(creatorQuery, Occur.MUST);
		}
		if(params.containsKey("keyword")){
			String keyword = params.get("keyword");
			Query keywordQuery = new TermQuery(new Term("keyword", keyword));
			query.add(keywordQuery, Occur.MUST);
		}
		if(params.containsKey("date")){
			String date = params.get("date");
			Query dateQuery = new TermQuery(new Term("date", date));
			query.add(dateQuery, Occur.MUST);
		}
		
		//只检索当前语种信息
//		String lan = "zh";
//		if(CommonUtil.isNotNull(lan)){
//			Query lanQuery = new TermQuery(new Term("language", lan));
//			query.add(lanQuery, Occur.MUST);
//		}
		
		//语种过滤只在调用方明确传入时启用。
		//前台“全部”检索不会传 language；旧逻辑会默认加 language=zh，
		//而 MySQL 中 RSDA_LANGUAGE 保存的是语种 ID（如 103），会导致检索结果被全部过滤。
		String lan = params.containsKey("language") ? params.get("language") : "";
		
		if(CommonUtil.isNotNull(lan)){
			BooleanQuery lanQuery = new BooleanQuery();
			
			Query q1 = new TermQuery(new Term("language", lan));
//			Query q2 = new TermQuery(new Term("language", "none"));
			
			lanQuery.add(q1, Occur.SHOULD);
//			lanQuery.add(q2, Occur.SHOULD);
			
			query.add(lanQuery, Occur.MUST);
		}
		
		
		return query;
	}
	private String parseAttr(String searchAtt){
		if(searchAtt.equals("rsdaTitle")){
			return "title";
		} else if(searchAtt.equals("rsdaCreator")){
			return "creator";
		} else if(searchAtt.equals("rsdaPublisher")){
			return "publisher";
		} else if(searchAtt.equals("rsdaKeyword")){
			return "keyword";
		}else{
			return null;
		}
	}
	
	
	
	@Override
	public Map<String, List<Object[]>> groupSearch(Map<String, String> params) throws ParseException {
		IndexSearcher indexSearcher = MainConnFactory.instance().getSearcher();
		BooleanQuery query = parserQuery(params);
		
		Map<String, List<Object[]>> groupMap = null;
		int cs = query.clauses().size();
		if(cs < 1){
			//如果一个检索条件都没有则检索所有
			groupMap = group(new MatchAllDocsQuery(), null, groupArr, 10, indexSearcher);
		}else{
			groupMap = group(query, null, groupArr, 10, indexSearcher);
		}
		return groupMap;
	}
	
	/**
	 * 获取聚类结果
	 * @author MaChao
	 * @date 2017-9-4
	 */
	public Map<String, List<Object[]>> group(Query query, Filter filter,
			String[] groupField, int docsLimit, IndexSearcher searcher) {
		//保证返回不为null
		Map<String, List<Object[]>> facetMap = new HashMap<String, List<Object[]>>();
		TaxonomyReader reader = GroupConnFactory.instance().getTaxonomyReader();
		if(docsLimit==0){
			docsLimit = 100;
		}
		//生成聚类字段和返回数
		CountFacetRequest[] facetRequests = new CountFacetRequest[groupField.length];
		for(int i = 0,size = groupField.length;i< size;i++){
			facetRequests[i] = new CountFacetRequest(new CategoryPath(groupField[i]), docsLimit);		
		}
		FacetSearchParams facetSearchParams = new FacetSearchParams(facetRequests);
		
		//开始搜集
		FacetsCollector facetsCollector = FacetsCollector.create(facetSearchParams, searcher.getIndexReader(), reader);
		
		//搜集完毕开始查询
		try{
			if(filter!=null){
				searcher.search(query, filter,facetsCollector);
			}else{
				searcher.search(query,facetsCollector);
			}
			
			List<FacetResult> res = facetsCollector.getFacetResults();
			String filedName = null;
		    List<Object[]> grouplist = null;
			if(res!=null && res.size()>0){
				for (FacetResult r : res) {
					if(r.getFacetResultNode().subResults.size()>0){
						filedName = r.getFacetResultNode().label.toString();						
						if(facetMap.containsKey(filedName)){
							grouplist = facetMap.get(filedName);
						}else{
							grouplist = new ArrayList<Object[]>();
							facetMap.put(filedName, grouplist);
						}			
						for (FacetResultNode n : r.getFacetResultNode().subResults) {
							grouplist.add(new Object[]{n.label.components[1], (int)n.value});
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return facetMap;
	}
	
	
	@Override
	public List<ResPageBean> pageSearch(PageBean pageBean,
			Map<String, String> params) throws Exception {
		int beginRows = pageBean.getCurrentPage() * pageBean.getPageSize();  //起始记录数
		int endRows = beginRows + pageBean.getPageSize(); //结束记录数
		
		IndexSearcher pageSearcher = PageConnFactory.instance().getSearcher();
		
		BooleanQuery query = new BooleanQuery();
		String resId = params.get("resId"); 
		
		Term t1 = new Term("resid", resId);
		Query q1 = new TermQuery(t1);
		query.add(q1, Occur.MUST);
		
		if(params.containsKey("key")){
			int match = Integer.parseInt(params.get("match"));
			String key = params.get("key");
			
			if(match == 1){
				//精确检索
				PhraseQuery contentQuery = new PhraseQuery();
				contentQuery.setSlop(key.length());
				List<String> keyList = AnalyzerUtil.displayToken(key);
				for (int i = 0, s = keyList.size(); i < s; i++) {
					contentQuery.add(new Term("content", keyList.get(i)));
				}
					
				query.add(contentQuery, Occur.MUST);
			}else{
				
				//模糊检索
				Analyzer analyzer = AnalyzerFactory.instance().getSmartAnalyzer();
				
				QueryParser parser = new QueryParser(Version.LUCENE_46, "content", analyzer);
				
				if(match == 2){
					//半精确，采用AND:文章中必须包含所有检索词，但位置可在不同处
					parser.setDefaultOperator(Operator.AND);
				}else{
					//模糊，采用OR：包含其中一个词即可
					parser.setDefaultOperator(Operator.OR);
				}
				
				Query contentQuery = parser.parse(key);
				query.add(contentQuery, Occur.MUST);
			}
		}
		
		/**
		 * 自定义查询排序
		 * 
		 * 第一个参数为排序字段，可以有多个；
		 * 第二个参数TYPE的取值为
		 * type对应的值分别为： 
			SortField. SCORE 按积分排序 
			SortField. DOC 按文档排序 
			SortField. AUTO 域的值为int、long、float都有效 
			SortField.STRING 域按STRING排序 
			SortField.CUSTOM 通过比较器排序 
			
			一般时间采用:SortField.STRING
			字符串采用：SortField.DOC
			第三个参数为排序方式，true:倒序  false:正序；默认为false正序
		 */
		SortField sortField = new SortField("pageNum", SortField.Type.INT, false);
		Sort sort = new Sort(sortField);
		TopDocs topDocs = pageSearcher.search(query, endRows, sort);
		
		
		int count = topDocs.totalHits; //结果总数
		pageBean.setCountRows(count);
		pageBean.setCountPage((count-1) / pageBean.getPageSize() + 1);
		
		List<ResPageBean> dataList = new ArrayList<ResPageBean>();
		
		/**
		 * 默认为每页12条，当超过20页，即结束条数为240的时候，就采用searchAfter查询
		 * 不过目前我并没看懂它的优秀，反而是多进行了一次查询而已。
		 */
		ResPageBean data = null;
		ScoreDoc[] docs = topDocs.scoreDocs;
		ScoreDoc doc = null;
		for(int i=beginRows, len=docs.length; i<len; i++){
			doc = docs[i];
			Document document = pageSearcher.doc(doc.doc);
			data = ObjectConvert.docToPagedata(document, query, true);
			if(data != null){
				dataList.add(data);
			}
		}
		return dataList;
	}
	
	@Override
	public ResMainBean getResDetail(Long resId) throws IOException {
		IndexSearcher indexSearcher = MainConnFactory.instance().getSearcher();
		
		Term t = new Term("id", resId.toString());
		TermQuery termQuery = new TermQuery(t);
		
		TopDocs topDocs = indexSearcher.search(termQuery, 1);
		
		ResMainBean data = null;
		ScoreDoc[] docs = topDocs.scoreDocs;
		ScoreDoc doc = docs[0];
		Document document = indexSearcher.doc(doc.doc);
		
		data = ObjectConvert.documentToData(document, termQuery);
		
		//通过documentToData转换数据对象时对原文做了高亮及字符截取
		//但在本应用中无须截取，因此重新获取全文内容
		String txt = "";
		String textPath = data.getTextPath();
		if(CommonUtil.isNotNull(textPath)){
			txt = IoCommonUtil.readTxt(textPath);
			data.setContent(txt);
		}
		
		List<ResPageBean> childs = getAllChildPage(resId.toString());
		data.setPageList(childs);
		return data;
	}
	public List<ResPageBean> getAllChildPage(String resId) throws IOException {
		
		IndexSearcher pageSearcher = PageConnFactory.instance().getSearcher();
		
		Term t = new Term("resid", resId);
		Query query = new TermQuery(t);
		
		SortField sortField = new SortField("pageNum", SortField.Type.INT, false);
		Sort sort = new Sort(sortField);
		TopDocs topDocs = pageSearcher.search(query, 1000, sort);
		
		List<ResPageBean> dataList = new ArrayList<ResPageBean>();
		ResPageBean data = null;
		ScoreDoc[] docs = topDocs.scoreDocs;
		ScoreDoc doc = null;
		for(int i=0, len=docs.length; i<len; i++){
			doc = docs[i];
			Document document = pageSearcher.doc(doc.doc);
			try {
				data = ObjectConvert.docToPagedata(document, query, false);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(data != null){
				dataList.add(data);
			}
		}
		return dataList;
	}
}















