package cn.com.unionlib.parser.core.interfaces;

import java.util.List;

import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.Query;

import cn.com.unionlib.parser.core.bean.Serach;

/**
 * 短语查询
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-12
 */
public interface TermQuery{
	/**
	 * 单个短语
	 * @author MaChao
	 * @date 2017-4-12
	 */
	public Query bulidTermQuery(String coulmn,String keyWord, Occur occur);
	/**
	 * 多个短语
	 * @author MaChao
	 * @date 2017-4-12
	 */
	public Query bulidTermQuery(List<Serach> serachList);
	
}
