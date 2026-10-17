package cn.com.adtech.riss2.bso.resource;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.domain.resource.ResMissDevolve;

/**
 * 任务转交相关操作
 * @author MaChao
 *
 */
public interface IMissionDeliverService {
	
	/**
	 * 获取用户的转交记录
	 * @param userId 用户
	 * @param type 转交任务类型
	 * @return
	 */
	public List<ResMissDevolve> getDevolveList(long userId, int type, int missionType, Pagination pageination, String missiontTypes);
	
	/**
	 * 处理转交申请
	 * @param deliverId 转交记录ID
	 * @param statu 状态	
	 * @return
	 */
	public boolean revertDeliver(long deliverId, int statu, String remark);
	
	public int getDevolveListCount(long userId, int type, int missionType, String missiontTypes);
	
	public ResMissDevolve getMissDevolveById(long devolveId);
	
	/**
	 * 删除转交记录
	 * @param id
	 * @return
	 */
	public boolean deleteDevolveById(long id);
	
	
	/**
	 * 撤销转交
	 * @param id
	 * @return
	 */
	public boolean chexiao(long id);
	
	
}
