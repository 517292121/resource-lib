package cn.com.adtech.riss2.bso.ib;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.ib.IndexBanner;

import com.jbetter.framework.session.Pagination;

public interface IBannerInfoService  extends ICommonService {
	
	/**
	 * 友情链接列表
	 */
	public String getLinkInfoToJson(Pagination pagination,
			String orderColumn, String orderType);
	
	/**
	 * 添加友情链接
	 */
	public boolean saveLinkInfo(IndexBanner ib);
	
	/**
	 * 修改友情链接
	 */
	public boolean updateLinkInfo(IndexBanner ib);
	
	/**
	 * 删除友情链接
	 */
	public boolean deleteLinkInfo(IndexBanner ib);
	
	/**
	 * 返回排序号
	 */
	public int getSlinIndex();
}
