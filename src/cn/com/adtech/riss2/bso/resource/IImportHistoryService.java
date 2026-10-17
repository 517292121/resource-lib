package cn.com.adtech.riss2.bso.resource;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResImportHistory;

public interface IImportHistoryService extends ICommonService{

	/**
	 * 获取导入记录列表
	 * @param rminId 作业ID
	 * @return
	 */
	public List<ResImportHistory> getImportHistoryList(long rminId, Pagination pagination, String orderType, String orderColumn);
	
	/**
	 * 获取导入记录下的所有资源
	 * @param rihiId
	 * @param start
	 * @param limit
	 * @param orderColumn
	 * @param orderType
	 * @return
	 */
	public String getResourceByImportHistory(long rihiId, int start, int limit, String orderColumn, String orderType);
	
	
	
	/**
	 * 删除导入记录
	 * @param rmhiId
	 * @return
	 */
	public void deleteImportHistory(Long rmhiId);
	
	/**
	 * 删除导入记录以及对应的资源
	 * @param rmhiId
	 * @return
	 */
	public void deleteImportHistoryAndResource(Long rmhiId);
}
