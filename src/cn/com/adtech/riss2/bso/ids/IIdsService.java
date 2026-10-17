package cn.com.adtech.riss2.bso.ids;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.IdsInformation;
import cn.com.adtech.riss2.domain.IdsInformationType;
import cn.com.unionlib.lucene.bean.ResMainBean;

/**
 * 信息发布系统
 * 
 * @author MaChao
 * @date 2010-10-25
 */
@SuppressWarnings("unchecked")
public interface IIdsService extends ICommonService {
	/**
	 * 获取信息分类列表
	 * 
	 * @param idstName
	 * @param idstIndex
	 * @param type
	 * @return
	 * @author MaChao
	 * @date 2010-11-23
	 */
	List getIdsInformationType(String idstName, Long idstIndex, String type);
	
	/**
	 * 获取信息分类列表
	 * 
	 * @param lan
	 * @return
	 * 
	 * 
	 * @author MaChao
	 * @date 	2020.04.14
	 * @version	1.0
	 */
	List<Map<String, Object>> getIdsInformationType(String lan);

	/**
	 * 获取信息分类列表
	 * 
	 * @param idstName
	 * @param sort
	 * @param dir
	 * @return
	 * @author MaChao
	 * @date 2010-10-26
	 */
	List<IdsInformationType> getIdsInformationTypeList(String idstName, String sort, String dir);
	
	/**
	 * 获取信息分类下拉选项
	 * @param selectid
	 * @return
	 */
	String getIdsInformationOptions(long selectid);

	/**
	 * 获取信息列表
	 * 
	 * @param idsiTitle
	 * @param sort
	 * @param dir
	 * @return
	 * @author MaChao
	 * @date 2010-10-26
	 */
	List getIdsInformation(String idsiTitle);

	/**
	 * 获取所有信息列表
	 * 
	 * @param pagination
	 * @param conditions
	 * @return
	 * @author MaChao
	 * @date 2010-10-26
	 */
	List getIdsInformationList(Pagination pagination, Map<String, Object> conditions);
	
	/**
	 * 根据id获取信息实体
	 * @param idsId
	 * @return
	 */
	IdsInformation getIdsInfomarion(Long idsId);
	
	/**
	 * Excel文件有效性验证
	 */
	public String checkExcel(File xlsFile);
	
	/**
	 * Excle文件中的数据校验
	 */
	public String checkExcelData(File xlsFile, int beginRow);
	
	/**
	 * 批量导入分类
	 */
	public String saveImportExcel(File xlsFile, HttpServletRequest request);
	
	/**
	 * 获取错误信息
	 */
	public String getMessage();
	
	/**
	 * 获取导入数量
	 */
	public int getTotalNumber();
	
	/**
	 * 获取导入数量-成功
	 */
	public int getSuccessNumber();
	
	/**
	 * 获取导入数量-失败
	 */
	public int getFailNumber();
	
	
	/**
	 * 前端信息列表查询
	 * @param p
	 * @param classId
	 * @return
	 */
	public List<Object[]> getInfoList(Pagination p, Long classId);
	
	/**
	 * 获取上一个资源
	 * @param idsiId
	 * @return
	 */
	public Object[] getInfoPre(Long idsiId);
	
	/**
	 * 获取下一个资源
	 * @param idsiId
	 * @return
	 */
	public Object[] getInfoNext(Long idsiId);
	
	/**
	 * 更新分类缓存
	 */
	public void updateInfoClassCache();
	
	public ResMainBean infoToDatabean(IdsInformation info);
	
	/**
	 *  	根据名字查询数量，以判断name是否存在
	 * @param name	分类名
	 * @param id	当前分类id
	 * @return
	 * 
	 * @author MaChao
	 * @date	2020.04.15
	 * @version	1.0
	 */
	public Long getCountByName(String name, Long id);
	
	/**
	 *  	根据分类id，获取关联的信息数量
	 * @param id	分类id
	 * @return
	 * 
	 * @author MaChao
	 * @date	2020.04.15
	 * @version	1.0
	 */
	public Long getInfoCountById(Long id);
}
