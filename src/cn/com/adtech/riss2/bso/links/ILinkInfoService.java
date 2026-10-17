package cn.com.adtech.riss2.bso.links;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.sc.SysLink;;

public interface ILinkInfoService  extends ICommonService {
	
	/**
	 * 友情链接列表
	 */
	public String getLinkInfoToJson(Pagination pagination,
			String orderColumn, String orderType);
	
	/**
	 * 添加友情链接
	 */
	public boolean saveLinkInfo(SysLink sysLink);
	
	/**
	 * 修改友情链接
	 */
	public boolean updateLinkInfo(SysLink sysLink);
	
	/**
	 * 删除友情链接
	 */
	public boolean deleteLinkInfo(Long slinId);
	
	/**
	 * 返回排序号
	 */
	public int getSlinIndex();
}
