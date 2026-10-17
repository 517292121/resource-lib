package cn.com.unionlib.parser.core.interfaces.realize;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;
import org.apache.lucene.util.Version;

import cn.com.unionlib.parser.core.exception.ParserException;
import cn.com.unionlib.parser.core.interfaces.MultiQuery;

/**
 * 模糊查询
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-12
 */
public class MultiFiledQuery implements MultiQuery {
	

	/**
	 * 模糊查询单字段
	 * @author MaChao
	 * @date 2017-4-11
	 */
	public Query bulidMultiFieldQuery( String coulmn,String keyWord,Occur occur) {
		return bulidMultiFieldQuery(getArrays(coulmn),getArrays(keyWord), getOccurs(occur));
	}

	/**
	 * 模糊查询
	 * 单关键词
	 * 多字段
	 * 单或非关系
	 * @author MaChao
	 * @date 2017-4-11
	 */
	public Query bulidMultiFieldQuery(String[] coulmns, String keyWord, Occur occur) {
		String[] keyWords = new String[coulmns.length];
		Occur [] occurs = new Occur[coulmns.length];
		for(int i=0,x=coulmns.length;i<x;i++){
			keyWords[i] = keyWord;
			occurs[i] = occur;
		}
		return bulidMultiFieldQuery(coulmns,keyWords , occurs);
	}
	
	/**
	 * 模糊查询
	 * 单关键词
	 * 多字段
	 * 多或非关系
	 * @author MaChao
	 * @date 2017-4-11
	 */
	public Query bulidMultiFieldQuery(String[] coulmns,String keyWord,Occur[] occurs) {
		String[] keyWords = new String[coulmns.length];
		for(int i=0,x=coulmns.length;i<x;i++){
			keyWords[i] = keyWord;
		}
		return bulidMultiFieldQuery(keyWords, coulmns, occurs);
	}

	/**
	 * 模糊查询
	 * 多关键词
	 * 多字段
	 * 多或非关系
	 * @author MaChao
	 * @date 2017-4-11
	 */
	public Query bulidMultiFieldQuery(String[] coulmns,String[] keyWords, Occur[] occurs){
		Query query = null;
		if(keyWords.length!=coulmns.length || keyWords.length!=occurs.length){
				throw new ParserException("解析失败,请检查参数!");
		}
		try{
			query = MultiFieldQueryParser.parse(Version.LUCENE_46, keyWords, coulmns,occurs, getAnalyzer());
		}catch (ParseException e) {
			System.out.println("解析失败,请检查参数!");
			e.printStackTrace();
		}
		return query;
	}

	/**
	 * 或非
	 * @author MaChao
	 * @date 2017-4-11
	 */
	protected static Occur[] getOccurs(Occur...occurs){
		return occurs;
	}
	
	/**
	 * 字段与关键词
	 * @author MaChao
	 * @date 2017-4-11
	 */
	protected static String[] getArrays(String... arryas){
		return arryas;
	}

	/**
	 * 分词器
	 */
	private Analyzer analyzer;

	public Analyzer getAnalyzer() {
		return analyzer;
	}
	public void setAnalyzer(Analyzer analyzer) {
		this.analyzer = analyzer;
	}
}
