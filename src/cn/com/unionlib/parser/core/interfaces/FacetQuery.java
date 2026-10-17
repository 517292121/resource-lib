package cn.com.unionlib.parser.core.interfaces;

import java.util.Map;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

/**
 * 聚类查询
 * @author MaChao
 * @date 2017-9-4
 */
public interface FacetQuery {

	/**
	 * 获取聚类结果
	 * @author MaChao
	 * @date 2017-9-4
	 */
	public Map<String, Map<String, Integer>> group(Query query,Filter filter,
			String[] groupField,int docsLimit); 
	
	/**
	 * 设置所要查询器
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public void setIndexSearcher(IndexSearcher indexSearcher);
}
