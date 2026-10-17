package cn.com.adtech.riss2.bso.resource;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bean.ResourceRepeatBean;
import cn.com.adtech.riss2.bso.ICommonService;

/**
 * 系统级资源查重
 * 
 * @author MaChao
 * 
 * @date 2011-08-25
 *
 */
public interface ISystemCheckRepeatService extends ICommonService {

	/**
	 * 系统资源查重入口函数
	 * @param paramList
	 */
	public void saveResourceRepeat(List<String> paramList);
	
	/**
	 * 获取查重结果
	 * @return
	 */
	public List getRepeatResultList(Pagination pagination, String orderColumn, String orderType);
	
	
	/**
	 * 根据资源ID和查重条件删除重复记录
	 * @param rsdaId
	 * @param repeatBean
	 * @return
	 */
	public boolean deleteRepeatByRsdaId(Long rsdaId, ResourceRepeatBean repeatBean);
	
	/**
	 * 获取所有重复记录的资源ID
	 * @return
	 */
	public List<Long> getAllRepeatRsdaId();
}
