package cn.com.unionlib.parser.core.interfaces.realize;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.PhraseQuery;

import cn.com.unionlib.parser.core.interfaces.PhrasesQuery;
import cn.com.unionlib.parser.util.IKDicWord;

/**
 * 短语查询
 * @author MaChao
 * @date 2017-7-21
 */
public class PhraseFiledQuery implements PhrasesQuery {

	/**
	 * 短语查询
	 * @author MaChao
	 * @date 2017-7-21
	 */
	@Override
	public PhraseQuery getPhraseQuery(String coulmn,String value, int slop) {
		PhraseQuery query = new PhraseQuery();
		query.setSlop(slop);
		String[] values = IKDicWord.getDicWords(value);
		for(String s : values){
			query.add(new Term(coulmn,s));
		}
		return query;
	}

}
