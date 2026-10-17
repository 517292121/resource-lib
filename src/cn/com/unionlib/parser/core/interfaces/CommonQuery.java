package cn.com.unionlib.parser.core.interfaces;

import org.apache.lucene.search.Query;

/**
 * 公用构建方法 * @Description: TODO
 * @author MaChao
 * @date 2017-4-13
 */
public interface CommonQuery {
	public Query bulidQuery(Object object);
}
