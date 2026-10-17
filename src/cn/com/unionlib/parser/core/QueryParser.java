package cn.com.unionlib.parser.core;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;

import cn.com.unionlib.parser.core.interfaces.FacetQuery;
import cn.com.unionlib.parser.core.interfaces.MultiQuery;
import cn.com.unionlib.parser.core.interfaces.ParseValue;
import cn.com.unionlib.parser.core.interfaces.PhrasesQuery;
import cn.com.unionlib.parser.core.interfaces.SerachGroup;
import cn.com.unionlib.parser.core.interfaces.SerachQuery;
import cn.com.unionlib.parser.core.interfaces.SerachQueryResult;
import cn.com.unionlib.parser.core.interfaces.TermQuery;
import cn.com.unionlib.parser.core.interfaces.realize.FacetFiledQuery;
import cn.com.unionlib.parser.core.interfaces.realize.MultiFiledQuery;
import cn.com.unionlib.parser.core.interfaces.realize.PhraseFiledQuery;
import cn.com.unionlib.parser.core.interfaces.realize.SerachGroupQuery;
import cn.com.unionlib.parser.core.interfaces.realize.TermFiledQuery;
import cn.com.unionlib.parser.util.DocumentToBean;

/**
 * 解析查询
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-12
 */
public class QueryParser {
	
	
	/**
	 * 构造函数
	 */
	public QueryParser(IndexSearcher searcher) {
		this.searcher = searcher;
	}
	
	
	
	/**
	 * 过滤条件设置
	 * @author MaChao
	 * @date 2017-4-12
	 */
	public Filter bulidFilter(Query query){
		if(query==null){
			throw new NullPointerException("过滤查询不能为空!");
		}
		return new QueryWrapperFilter(query);
	}

	/**
	 * 合并查询
	 * @author MaChao
	 * @date 2017-4-12
	 */
	public Query mergerQuery(Query a,Query b,Occur...occurs){
		BooleanQuery query = new BooleanQuery();
		int length = occurs.length;
		if(length==1){
			query.add(a, occurs[0]);
			query.add(b, occurs[0]);
		}else{
			query.add(a, occurs[0]);
			query.add(b, occurs[1]);
		}
		
		return query;
	}	
	
	
	/**
	 * 模糊查询接口
	 */
	protected MultiQuery multiQuery;
	/**
	 * 完全匹配查询接口
	 */
	protected TermQuery termQuery ;
	
	/**
	 * 短语查询接口
	 */
	protected PhrasesQuery phraseQuery;

	/**
	 * 分词器
	 */
	protected Analyzer analyzer;

	/**
	 * 字段值转接口
	 */
	protected ParseValue parseValue;
	
	/**
	 * 字段转换
	 */
	protected DocumentToBean docToBean;
	
	/**
	 * 聚类查询
	 */
	protected SerachGroup serachgroup;
	
	/**
	 * 切面查询
	 */
	protected FacetQuery facetQuery;
	
	/**
	 * 查询
	 */
	protected SerachQuery serachQuery;
	
	/**
	 * 索引查询器
	 */
	protected  IndexSearcher searcher;
	
	
	
	/**
	 * 模糊查询实例
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public MultiQuery getMultiQuery() {
		if(multiQuery==null){
			multiQuery = new MultiFiledQuery();
		}
		if(analyzer==null){
			throw new NullPointerException("分词器不能为空!");
		}
		multiQuery.setAnalyzer(analyzer);
		return multiQuery;
	}

	/**
	 * 完全匹配查询实例
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public TermQuery getTermQuery() {
		if(termQuery==null){
			termQuery = new TermFiledQuery();
		}
		return termQuery;
	}
	
	/**
	 * 短语查询实例
	 * @author MaChao
	 * @date 2017-7-21
	 */
	public PhrasesQuery getPhrasesQuery(){
		if(phraseQuery==null){
			phraseQuery = new PhraseFiledQuery();
		}
		return phraseQuery;
	}
	
	/**
	 * 自定义类型转换接口设置
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public void setParseValue(ParseValue parseValue) {
		this.parseValue = parseValue;
	}
	
	/**
	 * 自定义解析转换
	 * @author MaChao
	 * @date 2017-4-13
	 */
	protected DocumentToBean getDocToBean() {
		if(docToBean==null ){
			docToBean = new DocumentToBean(parseValue);
		}
		return docToBean;
	}
	
	/**
	 * 聚类查询
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public SerachGroup getSerachGroup() {
		if(serachgroup==null){
			serachgroup = new SerachGroupQuery();
		}
		if(searcher==null)throw new NullPointerException("索引查询器不能为空");
		//设置索引查询器
		serachgroup.setIndexSearcher(searcher);
		return serachgroup;
	}
	
	/**
	 * 基本查询
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public SerachQuery getSerachQuery() {
		if(serachQuery==null){
			serachQuery = new SerachQueryResult();
		}
		//设置doc转bean
		serachQuery.setDocToBean(getDocToBean());
		//设置索引查询器
		if(searcher==null)throw new NullPointerException("索引查询器不能为空");
		serachQuery.setIndexSearcher(searcher);
		return serachQuery;
	}
	
	/**
	 * 切面查询
	 * @author MaChao
	 * @date 2017-9-4
	 */
	public FacetQuery getFacetQuery() {
		if(facetQuery==null){
			facetQuery = new FacetFiledQuery();
		}
		if(searcher==null)throw new NullPointerException("索引查询器不能为空");
		facetQuery.setIndexSearcher(searcher);
		return facetQuery;
	}


	//设置分词器
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
	//设置模糊查询接口
	public void setMultiQuery(MultiQuery multiQuery) {
		this.multiQuery = multiQuery;
	}
	//设置短语查询接口
	public void setTermQuery(TermQuery termQuery) {
		this.termQuery = termQuery;
	}
	//设置聚类查询接口
	public void setSerachGroup(SerachGroup serachgroup) {
		this.serachgroup = serachgroup;
	}
	//设置查询处理接口
	public void setSerachQuery(SerachQuery serachQuery) {
		this.serachQuery = serachQuery;
	}
	//设置切面查询
	public void setFacetQuery(FacetQuery facetQuery) {
		this.facetQuery = facetQuery;
	}


	
}
