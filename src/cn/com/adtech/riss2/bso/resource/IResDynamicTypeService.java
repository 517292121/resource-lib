package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;

/**
 * 自定义分类操作，主要针对党校提出无限级分类的ajax调用操作
 * 
 * @author MaChao
 *
 */
public interface IResDynamicTypeService extends ICommonService {

	/**
	 * 保存自定义分类
	 * @param rmstId
	 * @param rdtyPid
	 * @param rdtyName
	 * @param rdtyGrade
	 * @param rdtyIndex
	 * @param rdtyType
	 * @return
	 */
	public boolean saveDynamicType(Long rdtyId, Long rmstId, Long rdtyPid, String rdtyName, long rdtyGrade, int rdtyIndex, int rdtyType, String rdtyCode);
	
	/**
	 * 保存自定义分类
	 * @param rmstId
	 * @param rdtyPid
	 * @param rdtyName
	 * @param rdtyZwName
	 * @param rdtyEnName
	 * @param rdtyGrade
	 * @param rdtyIndex
	 * @param rdtyType
	 * @return
	 */
	public boolean saveDynamicType(Long rdtyId, Long rmstId, Long rdtyPid, String rdtyName, String rdtyZwName, String rdtyEnName, long rdtyGrade, int rdtyIndex, int rdtyType, String rdtyCode);
	
	/**
	 * 删除自定义分类
	 * @param rdtyId
	 * @return
	 */
	public String deleteDynamicType(Long rdtyId);
	

	/**
	 * 根据特色库ID和自定义分类类型获取所有的自定义分类
	 * @param rmstId  特色库ID
	 * @param rdtyType 自定义分类类型
	 * @return
	 */
	public List<ResDynamicType> getDynamicTypeList(Long rmstId, int rdtyType);
	
	
	/**
	 * 根据特色库ID以无限级数的方式获取所有Option
	 * @param rmstId 
	 * @param selectid
	 * @param rdtyType 自定义分类类型
	 * @return
	 */
	public String getDynamicOptions(Long rmstId, Long selectid, int rdtyType);
	
	
	
	/**
	 * 添加对分类号的唯一性验证
	 * @param rmstId
	 * @param rdtyType
	 * @param rdtyCode
	 * @return
	 */
	public boolean checkGroupCode(Long rmstId, int rdtyType, String rdtyCode, Long selfId);
	/******************************以下是自定义分类导入导出相关************************/
	
	public List<Object[]> getDynamicType(Long rmstId, int rdtyType);
	
	/**
	 * Excel文件有效性验证
	 * @param xlsFile
	 * @return
	 */
	public String checkGroupExcel(File xlsFile);
	
	/**
	 * Excle文件中的数据校验
	 * @param xlsFile
	 * @return
	 */
	public String checkGroupData(File xlsFile, int beginRow, Long rmstId, int rdtyType);
	
	/**
	 * 批量导入分类
	 * @param xlsFile 上传的Excel文件
	 * @param rmstId 特色库ID
	 * @param rdtyType 分类类型
	 * @param coverType 分类覆盖类型
	 * @return
	 */
	public String saveImportExcel(File xlsFile, HttpServletRequest request);
	
	/**
	 * 更新自定义分类的资源统计数
	 */
	public void updateDynamicCount();
	
	
}
