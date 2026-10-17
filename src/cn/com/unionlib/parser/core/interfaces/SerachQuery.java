package cn.com.unionlib.parser.core.interfaces;

import java.util.List;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.Sort;

import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.parser.util.DocumentToBean;

/**
 * 基础查询
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-13
 */
public interface SerachQuery {
	
	/**
	 * 查询数据
	 * 无过滤条件
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public List<?> getDocList(Query query,Sort sort,PageBean pageBean,Class clas);
	
	/**
	 * 查询数据
	 * 有过滤条件
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public List<?> getDocList(Query query,Filter filter,Sort sort,PageBean pageBean,Class clas);

	/**
	 * 设置解析字段工具
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public void setDocToBean(DocumentToBean docToBean);
	
	/**
	 * 设置索引查询器
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public void setIndexSearcher(IndexSearcher indexSearcher);
}
