package cn.com.adtech.riss2.bso.resource;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.DbmsBakHistory;

public interface IDataBakService extends ICommonService{
	
	/**
	 * 返回备份列表的json字符串
	 * @param pagination 分页控件
	 * @param orderColumn 排序字段
	 * @param orderType 排序方式
	 * @return String
	 */
	String getDataBakByJson(Pagination pagination, String orderColumn, String orderType);
	
	/**
	 * 根据备份文件 id 删除备份文件
	 * @param dbmsId
	 * @return
	 */
	boolean deleteDataBakByDbmsId(Long dbmsId);
	
	/**
	 * 备份数据
	 * @return
	 */
	boolean dataBak(String exec, DbmsBakHistory dbms);
}
