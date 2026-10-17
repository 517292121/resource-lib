package cn.com.adtech.riss2.bso.resource;

import java.util.List;

import cn.com.adtech.riss2.bso.ICommonService;

import com.jbetter.framework.session.Pagination;

/**
 * 抓取数据审核处理
 * 
 * @author MaChao
 * 
 * @date
 *
 */
public interface IGatherResourceService extends ICommonService{

	/**
	 * 获取抓取数据资源列表
	 * @param pagination
	 * @param title
	 * @param statu
	 * @param orderColumn
	 * @param orderType
	 * @return
	 */
	public List getGatherResourceList(Pagination pagination,
			String title, int statu, String orderColumn, String orderType);
	
	
	/**
	 * 设置抓取资源的审核状态
	 * @param rsdaIds 以逗号分割的资源id字符串。如果一个都没有，则设置所有的未审核的资源
	 * @param auditStatu 审核结果状态
	 */
	public void updateGatherResourceStatus(String rsdaIds, int auditStatu);
}
