package cn.com.unionlib.lucene.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.lucene.queryparser.classic.ParseException;

import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.bean.ResPageBean;

/**
 * 
 * 索引检索处理
 *
 * @author MaChao
 *
 * @date：Jan 29, 2018
 */
public interface ILuceneSearchService {
	
	/**
	 * lucene一键检索
	 * @param page
	 * @param params
	 * @return
	 */
	public List<Object[]> luceneSearch(PageBean page, Map<String, String> params) throws Exception;
	//lucene一键检索，同时检索出资源子页面记录
	public List<ResMainBean> luceneSearchForChildPage(PageBean page, Map<String, String> params) throws Exception;
	/** Matching books and keyword occurrence frequency in the selected fields. */
	public long[] getSearchResultCounts(Map<String, String> params) throws Exception;
	
	
	/**
	 * 统计聚类
	 * @param params
	 * @return
	 */
	public Map<String, List<Object[]>> groupSearch(Map<String, String> params) throws ParseException;
	
	/**
	 * 检索资源子页内容
	 * @param page
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<ResPageBean> pageSearch(PageBean pageBean, Map<String, String> params) throws Exception;
	
	/**
	 * 获取资源详细信息对象
	 * @param resId
	 * @return
	 */
	public ResMainBean getResDetail(Long resId) throws IOException;
	
}
