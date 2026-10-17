package cn.com.adtech.riss2.bso;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.Wrapper;

public interface ISysUserinfoService extends ICommonService {
	/**
	 * 根据用户名或者姓名查找用户 如果为空的话就查询出全部用户
	 * 
	 * @param conditon
	 * @param page
	 * @return
	 */
	Wrapper getAllUsersBySearchCondition(String condition, Boolean suinIsStop, PageExtNative page);

	/**
	 * 删除用户
	 * 
	 * @param users
	 */
	void deleteUsers(Long[] userIds);
	
	/**
	 * 判断用户是否参与文献求助,资源评论
	 * @param userIds
	 * @return
	 */
	Boolean getIssu(Long[] userIds);
	/**
	 * 重置密码
	 * 
	 * @param users
	 */
	void updateUsersPassword(Long[] userIds, String newPassword);

	/**
	 * 启用，禁用用户
	 * 
	 * @param userIds
	 */
	void updateUsersState(Long[] userIds);

	/**
	 * 根据用户名或者姓名查找未审核会员 如果为空的话就查询出全部未审核会员
	 * 
	 * @param conditon
	 * @param page
	 * @return
	 */
	Wrapper getAllNotAuditedUsersBySearchCondition(String condition, String selectUserState, PageExtNative page);
	
	/**
	 * 修改用户类型
	 * @param userIds
	 * @param userTypeId
	 * @return
	 */
	void updateUsersTypes(Long[] userIds, Long userTypeId);
}
