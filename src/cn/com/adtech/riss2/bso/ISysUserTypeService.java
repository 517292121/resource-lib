package cn.com.adtech.riss2.bso;

import java.util.List;

import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysUserType;

@SuppressWarnings("unchecked")
public interface ISysUserTypeService extends ICommonService {
	/**
	 * 分页查询出所有的会员类型
	 * 
	 * @param condition
	 * @param page
	 * @return
	 */
	public Wrapper getAllUserTypesBySearchCondition(String condition, PageExtNative page);

	/**
	 * 编辑会员类型
	 * 
	 * @param userType
	 * @param sutyId
	 */
	public void updateUserType(SysUserType userType, Long sutyId);

	/**
	 * 根据id查找对应的会员类型
	 * 
	 * @param id
	 * @return
	 */
	public SysUserType getSysUserTypeById(Long id);

	/**
	 * 添加用户类型
	 * 
	 * @param sut
	 * @return
	 */
	public SysUserType saveSysUserType(SysUserType sut);

	/**
	 * 删除用户类型
	 * 
	 * @param id
	 */
	public void deleteSysUserType(Long id);

	/**
	 * 获得用户类型
	 * 
	 * @author MaChao
	 * @date 日期
	 * @return
	 */
	public List getUserTypeList();
}
