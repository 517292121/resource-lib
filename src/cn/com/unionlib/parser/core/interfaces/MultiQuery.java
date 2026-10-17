package cn.com.unionlib.parser.core.interfaces;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.BooleanClause.Occur;

/**
 * 模糊查询
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-12
 */
public interface MultiQuery{
	
	/**
	 * 模糊查询单字段
	 * @author MaChao
	 * @date 2017-4-11
	 */
	public Query bulidMultiFieldQuery(String coulmn, String keyWord, Occur occur);
	/**
	 * 模糊查询
	 * 单关键词
	 * 多字段
	 * 单或非关系
	 * @author MaChao
	 * @date 2017-4-11
	 */
	public Query bulidMultiFieldQuery(String[] coulmns,String keyWord,Occur occur);
	/**
	 * 模糊查询
	 * 单关键词
	 * 多字段
	 * 多或非关系
	 * @author MaChao
	 * @date 2017-4-11
	 */
	public Query bulidMultiFieldQuery(String[] coulmns,String keyWord, Occur[] occurs) ;
	/**
	 * 模糊查询
	 * 多关键词
	 * 多字段
	 * 多或非关系
	 * @author MaChao
	 * @date 2017-4-11
	 */
	public Query bulidMultiFieldQuery(String[] coulmns,String[] keyWords, Occur[] occurs);
	
	/**
	 * 设置分词器
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public void setAnalyzer(Analyzer analyzer);
}
