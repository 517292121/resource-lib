package cn.com.adtech.riss2.bso.resource;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

public interface IMemberResourceService extends ICommonService{

	/**
	 * 获取用户资源列表
	 */
	public List<ResSourceData> getMemberResourceList(long memberId, String keyWord, int sourceType,Pagination pagination);
	
	/**
	 * 获取用户可上传资源的特色库列表
	 * @return
	 */
	public List<ResMetaStore> getStoreList();
	
	/**
	 * 根据特色库获取对应的资源类型列表
	 * @param rmstId
	 * @return
	 */
	public List<ResLiterType> getLiterListByStore(long rmstId);
	
	/**
	 * 组装特色库对应的资源类型下拉选择字符串
	 * @param rmstId
	 * @return
	 */
	public String getListOptionByStore(long rmstId, HttpServletRequest request);
	
	/**
	 * 获取会员上传的资源列表,主要用于后台审核
	 * @param pagination
	 * @param title
	 * @param statu
	 * @param orderColumn
	 * @param orderType
	 * @return
	 */
	public List<Object> getMemberResourceList(Pagination pagination, String title, int statu, String orderColumn, String orderType);
}
