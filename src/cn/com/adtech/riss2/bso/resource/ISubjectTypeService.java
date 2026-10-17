package cn.com.adtech.riss2.bso.resource;

import java.util.List;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;

/**
 * 学科分类管理
 * @author MaChao
 *
 */
public interface ISubjectTypeService extends ICommonService{

	/**
	 * 获取所有学科
	 * @return
	 */
	public List<ResSubjectType> getSubjectList();
	
	/**
	 * 根据ID获取学科对象
	 * @param rstyId
	 * @return
	 */
	public ResSubjectType getSubjectById(Long rstyId);
	
	
	/**
	 * 获取学科分类的下拉选项
	 * @param selectid 选择的选项ID
	 * @return
	 */
	public String getSubjectOptions(Long selectid);
	
	/**
	 * 保存学科分类
	 * @param rstyId
	 * @param rstyPid
	 * @param rstyName
	 * @param rstyIndex
	 * @param rstyCode
	 * @return
	 */
	public boolean saveSubject(Long rstyId, Long rstyPid, String rstyName, Integer rstyIndex, String rstyCode);
	
	/**
	 * 删除学科分类
	 * @param rstyId
	 * @return
	 */
	public boolean deleteSubject(Long rstyId);
	
	/**
	 * 检验学科编号是否存在
	 * @param rstyCode
	 * @return
	 */
	public boolean checkSubjectCode(String rstyCode, Long selfId);
}
