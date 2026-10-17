package cn.com.adtech.riss2.bso;

import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysRole;

/**
 * 用户管理
 * 	会员管理和管理员管理
 * @author MaChao
 *
 */
public interface ISysManagerService extends ICommonService {
	
	/**
	 * 根据条件得到所有的管理员，并分页
	 * @return
	 */
	Wrapper getAllManagersBySearchCondition(String condition, PageExtNative page);

	/**
	 * 得到所有管理员类型
	 * @return
	 */
	Wrapper getAllRoles(PageExtNative page);

	/**
	 * 取得所有的管理员类型，返回list
	 * @return
	 */
	List getAllRoles();

	/**
	 * 添加管理员
	 * @return
	 */
	SysManager saveManager(SysManager manager, SysRole[] roles);
	
	/**
	 * 根据id获取管理员
	 * @param id
	 * @return
	 */
	Map getManagerInfoById(long id);
	
	/**
	 * 更新管理员基本信息
	 * @param manager
	 */
	void updateManagerInfo(SysManager manager, SysRole[] roles);
	
	/**
	 * 根据登录账号查找管理员 
	 * @param loginName
	 * @return
	 */
	SysManager getSysManagerByLoginName(String loginName);
	
	/**
	 * 删除管理员
	 * @param manager
	 */
	void deleteSysManager(SysManager manager);
	
	/**
	 * 根据管理员获取管理员的角色
	 * @param id
	 * @return
	 */
	List getRolesByManagerId(long id);
	
	/**
	 * 根据管理员获取系统角色
	 * @author MaChao
	 * @param id
	 * @return
	 */
	List getSysRoleByManagerId(long id);
	
	/**
	 * 修改管理员状态
	 * @param managerId
	 */
	void updateSysManagerStatus(Long managerId);
	
	/**
	 * 判断该用户建的数据库 是否还有其他管理人员 
	 * @param managerId
	 * @return 是（true）   否(false)
	 */
	boolean isStop(Long managerId);
}
