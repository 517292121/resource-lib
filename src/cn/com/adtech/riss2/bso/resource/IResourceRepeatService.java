package cn.com.adtech.riss2.bso.resource;

import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bean.ResourceRepeatBean;
import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceRepeat;

/**
 * 资源查重数据库操作接口
 * 
 * @author MaChao
 * 
 * @date 2011-08-04
 *
 */
public interface IResourceRepeatService extends ICommonService {

	/**
	 * 资源查重入口函数
	 * @param resourceRepeatBean 封面查重条件和参数的bean对象
	 */
	public void saveResourceRepeat(ResourceRepeatBean resourceRepeatBean);

	/**
	 * 根据以逗号分类的ID字符串查询对应的资源集合
	 * @param ids 以逗号分类的ID字符串
	 * @return
	 */
	public List<ResSourceData> getResourceDataByIdArr(String ids);

	/**
	 * 获取资源已查重的百分比
	 * @return
	 */
	public int getProgress();

	/**
	 * 根据作业获取查重结果集合
	 * @return
	 */
	public List<ResSourceRepeat> getRepeatByMission(Pagination pagination,
			Long rminId, String orderColumn, String orderType, String rsdaTitle,boolean showall);

	/**
	 * 根据资源id获取查重结果集合
	 * @return
	 */
	public List<ResSourceRepeat> getRepeatByResources(String rsdaIds,
			String orderColumn, String orderType, String rsdaTitle,boolean showall);

	/**
	 * 根据查重的某个元素获取所重复的值
	 * 
	 * @param pagination
	 *            分页对象
	 * @param selfRsdaId
	 *            资源本身ID
	 * @param eleColumn
	 *            所匹配的元素名称
	 * @param rsdaState
	 *            查重范围(资源状态)
	 * @param orderColumn
	 *            排序列
	 * @param orderType
	 *            排序类型
	 * @return
	 */
	public List<Object[]> getRepeatResourceByElement(Pagination pagination,
			Long selfRsdaId, String eleColumn,
			String orderColumn, String orderType);

	/**
	 * 查询全部匹配的资源
	 * 
	 * @param pagination
	 *            分页对象
	 * @param repeatBean
	 *            查重条件bean
	 * @param selfRsdaId
	 *            资源本身ID
	 * @param orderColumn
	 *            排序列
	 * @param orderType
	 *            排序类型
	 * @return
	 */
	public List<Object[]> getRepeatResourceByAll(Pagination pagination,
			ResourceRepeatBean repeatBean, Long selfRsdaId, String orderColumn,
			String orderType, int haveSelf);
	
	/**
	 * 获取自定义分类名称，主要用于查看资源详细信息时
	 * @param rdtyId
	 * @return
	 */
	public String getDynamicName(Long rdtyId);
	
	/**
	 * 清空所有查重数据
	 */
	public void deleteAllRepeat();
	
	/**
	 * 设置查重局部开关，主要用于结束查重
	 * @param repeatOpen
	 */
	public void setRepeatOpen(boolean repeatOpen);
	
	/**
	 * 删除重复的资源，本身
	 * @param rminId
	 * @param rsdaIdArr
	 * @return
	 */
	public boolean deleteRepeatResource(Long rminId, Long[] rsdaIdArr, IResourceDataService resourceService);
}
