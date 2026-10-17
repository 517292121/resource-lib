package cn.com.adtech.riss2.bso;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysRole;

public interface ISysRoleService extends ICommonService {
	/**
	 * 根据角色类型、角色名查找角色 如果为空的话就查询出全部角色
	 * 
	 * @param conditon
	 * @param page
	 * @return
	 */
	public Wrapper getAllRolesBySearchCondition(String condition, PageExtNative page);

	/**
	 * 新增角色
	 * 
	 * @return
	 */
	public SysRole saveRole(SysRole role);

	/**
	 * 删除角色
	 * 
	 * @param role
	 */
	public void deleteRole(SysRole role);

	/**
	 * 更新角色
	 * 
	 * @param role
	 */
	public void updateRole(SysRole role);

	/**
	 * 根据ID查找角色对象
	 * 
	 * @param id
	 * @return
	 */
	public SysRole getRoleById(Long id);

	/**
	 * 跟据catalogid获取所有的function
	 * 
	 * @param id
	 * @return
	 */
	public Wrapper getAllFunctionsByCataLogId(Long id, PageExtNative page);

	/**
	 * 跟据roleid获取所有的function
	 * 
	 * @param id
	 * @return
	 */
	public Wrapper getAllFunctionsByRoleId(Long id, PageExtNative page);

	/**
	 * 给一个角色添加权限
	 * 
	 * @param funcs
	 * @param role
	 */
	public void saveFunctionsToRole(Long[] funcsId, Long roleId);

	/**
	 * 给角色删除权限
	 * 
	 * @param funcs
	 * @param role
	 */
	public void deleteFunctionsFromRole(Long[] funcsId, Long roleId);
}