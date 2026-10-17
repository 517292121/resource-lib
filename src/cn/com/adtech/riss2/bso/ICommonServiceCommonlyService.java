package cn.com.adtech.riss2.bso;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.domain.MmSetting;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysUserinfo;

public interface ICommonServiceCommonlyService extends ICommonService {

	/**
	 * ConstantsObj中的getSysProperty方法调用
	 * 
	 * @author MaChao
	 * @date 2007-8-28
	 * @param name 
	 * @return
	 */
	public String getSysProperty(String name);
	
	/**
	 * 根据用户,获得用户的角色集合
	 * 
	 * @param currentUser
	 * @return
	 */
	public List<Object> getUserRoles(SysManager currentUser);
	
	/**
	 * 根据角色集合，获得角色集中的角色有权限操作的系统功能树
	 * 
	 * @param roles
	 *            角色对象集合
	 * @see cn.com.adtech.riss2.domain.SysCatalog
	 * @return 用户有权限访问的顶级SysCatalog对象集合（按SCAT_CODE排序）， 每个对象中包括了用户有权限访问的子SysCatalog对象集合（按SCAT_CODE排序）和SysFunction集合（按SFUN_INDEX排序）
	 */
	public List<Object> getAccessableSysCatalogs(List roles);
	
	/**
	 * 根据功能点倒推出catalog
	 * 
	 * @param sysFunctionList
	 *            功能点
	 * @return 一个构造好的树
	 */
	public List getRootCategories(List sysFunctionList);
	
	/**
	 * 用户登录验证（跟据用户ID识别用户类型）<br>
	 * 用户通过用户名密码登录系，并获得用户相应信息,修改用户最后登录时间,记录登录日志
	 * 
	 * @param remoteHost
	 *            客户端IP
	 * @param UserName
	 *            登录名
	 * @param Password
	 *            登录密码
	 */
	public SysUserinfo sysuserLogin(String userName, String password, String remoteHost);
	
	/**
	 * 管理员登录
	 * 
	 * @param remoteHost
	 *            客户端IP
	 * @param UserName
	 *            登录名
	 * @param Password
	 *            登录密码
	 */
	public SysManager managerLogin(String userName, String password, String remoteHost);
	
	/**
	 * 根据参数名称和类型,取出对应参数对象
	 * 
	 * @param name
	 * @param type
	 * @return
	 */
	public MmSetting getSettingParam(String name, Short type);
	
	/**
	 * 保存到系统设置表中
	 * 
	 * @param setting
	 */
	public void saveMmSettingParam(MmSetting setting);	
	
	/**
	 * 用户登录验证（跟据用户ID识别用户类型）<br>
	 * 用户通过用户名密码登录系，并获得用户相应信息,修改用户最后登录时间,记录登录日志
	 * 
	 * @param remoteHost
	 *            客户端IP
	 * @param UserName
	 *            登录名
	 * @param Password
	 *            登录密码
	 * @modify hetao 2008-11-14 取消事务,因为用户表经常死锁.
	 */
	public SysManager userLogin(String userName, String password, String remoteHost);
	
	/**
	 * 保存系统参数设置
	 * 
	 * @author MaChao
	 * @date 2010-10-12
	 * @param request
	 */
//	public void saveAllMmSettings(HttpServletRequest request);
	
	/**
	 * 更新系统参数设置
	 * 
	 * @param conditions
	 * @author MaChao
	 * @date 2010-12-15
	 */
	public void saveMmSetting(Map conditions);
	
}
