package cn.com.unionlib.parser.core.interfaces;

import java.util.Map;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;

/**
 * 聚类查询
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-13
 */
public interface SerachGroup {
	/**
	 * 聚类查询接口
	 * @Description: TODO
	 * @param @param query			查询条件
	 * @param @param filter			过滤条件
	 * @param @param groupField		聚类字段
	 * @param @param sortFields		排序(暂无使用,如需要请自行实现GroupSerach)
	 * @param @param docsLimit		一个字段返回多少条记录
	 * @param @param groupOffset	起始位置(类似分页 一般设为 0 )
	 * @param @param groupLimit     结束位置(一页返回多少条)
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public Map<String, Map<String, Integer>> group(Query query,Filter filter,
			String[] groupField,SortField[] sortFields,int docsLimit,int groupOffset,int groupLimit); 
	
	/**
	 * 设置所要查询器
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public void setIndexSearcher(IndexSearcher indexSearcher);
}
