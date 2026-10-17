package cn.com.unionlib.parser.core.interfaces;

import org.apache.lucene.search.PhraseQuery;

/**
 * 短语查询
 * @author MaChao
 * @date 2017-7-21
 */
public interface PhrasesQuery {
	/**
	 * 短语查询
	 * @author MaChao
	 * @date 2017-7-21
	 */
	public PhraseQuery getPhraseQuery(String coulmn,String value,int slop);
}
