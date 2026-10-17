package cn.com.adtech.riss2.bso;

import java.util.List;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.Wrapper;

import com.jbetter.framework.session.Pagination;

/**
 * 服务管理
 * 
 * @author MaChao
 * @date 2010-10-30
 */
public interface ISysServiceService extends ICommonService {
	/**
	 * 获得资源评论列表
	 * 
	 * @author MaChao
	 * @date 2010-10-30
	 * @param name
	 * @param beginDate
	 * @param endDate
	 * @param page
	 * @return
	 */
	public Wrapper getResSourceComtList(String name, String beginDate, String endDate, String state, PageExtNative page);
	
	

	/**
	 * 更新资源评论状态
	 * 
	 * @author MaChao
	 * @date 2010-10-30
	 * @param rscoId
	 * @param rscoStatus
	 */
	public void updateResSourceComt(Long rscoId, Short rscoStatus);

	/**
	 * 删除资源评论
	 * 
	 * @author MaChao
	 * @date 2010-10-30
	 * @param ids
	 * @return
	 */
	public int deleteResSourceComt(String ids);
}
