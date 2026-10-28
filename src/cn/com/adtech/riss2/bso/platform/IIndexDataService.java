package cn.com.adtech.riss2.bso.platform;

import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.bean.ResourceForTypeBean;
import cn.com.adtech.riss2.bso.ICommonService;

/**
 * 
 * 前台首页相关数据查询
 *
 * @author MaChao
 *
 * @date：Jan 8, 2018
 */
public interface IIndexDataService extends ICommonService{
	/** Published documents and recorded site visits across all languages and all years. */
	public Map<String, Object> getHomeStatistics();
	/** Live public catalogue totals and monthly access counts, using console report definitions. */
	public Map<String, Object> getDatabaseStatistics(int year);
	
	/**
	 * 根据资源类型获取推荐资源
	 * @return
	 */
	public List<ResourceForTypeBean> getRcmdResource();

	/**
	 * 获取首页热门资源与最新资源
	 * @param sort
	 * @param num
	 * @return
	 */
	public List<Object[]> getIndexResource(String sort, int num);
	
	/**
	 * 获取热门检索词
	 *
	 * @author MaChao
	 * @date Oct 31, 2015
	 *
	 * @param num
	 * @return
	 */
	public List<String> getHotSearchKey(int num);
	
	/**
	 * 获取首页信息资讯
	 * @param num
	 * @return
	 */
	public List<Object[]> getInfoList(int num);
	
}
