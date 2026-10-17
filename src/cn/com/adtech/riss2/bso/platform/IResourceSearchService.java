package cn.com.adtech.riss2.bso.platform;

import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;

import com.jbetter.framework.session.Pagination;

/**
 * 
 * 前端资源检索处理
 *
 * @author MaChao
 *
 * @date：Jan 8, 2018
 */
public interface IResourceSearchService extends ICommonService{

	/**
	 * 获取资源列表，根据资源库与资源类型检索
	 * @param p
	 * @param rmstId
	 * @param rltyId
	 * @return
	 */
	public List<Object[]> getResourceList(Pagination p, Map<String, Object> map);
	public long getIdentifierHitCount(Map<String, Object> query);
	
	/**
	 * 根据特色库ID获取对应的资源类型
	 * @param rmstId
	 * @return
	 */
	public List<Object[]> getResType(Long rmstId);

	/** Distinct resource types configured on enabled libraries; zero means all libraries. */
	public List<Object[]> getLiteratureTypes(Long rmstId);
	
	/**
	 * 获取特色库的所有自定义分类
	 * @param rmstId
	 * @return
	 */
	public List<ResDynamicType> getGroupList(Long rmstId);
	
	/**
	 * 获取所有学科分类
	 * @return
	 */
	public List<ResSubjectType> getSubjectClassList();
	
	/**
	 * 根据资源类型获取资源库
	 * @param rltyId
	 * @return
	 */
	public List<Object[]> getStoreList(Long rltyId);
	
	/**
	 * 获取资源元数据信息
	 * @param rsdaId
	 * @return
	 */
	public List<Object[]> getMetaData(Long rsdaId);
	
	/**
	 * 根据特色库ID获取访问IP地址
	 * @param rmstId
	 * @return
	 */
	public List<String> getStoreIpList(Long rmstId);
	
	/**
	 * 获取资源同一主题下其它资源
	 * @param rssuId 主题ID
	 * @return
	 */
	public List<Object[]> getResRelated(Long rssuId, Long rsdaId);

	/** Published research linked through the resource's existing subject. */
	public List<Object[]> getRelatedResearch(Pagination pagination, Long rsdaId);
	
	/**
	 * 获取资源附件
	 * @param rsdaId
	 * @return
	 */
	public List<Object[]> getAttachFile(Long rsdaId);
	
	/**
	 * 按资源库与资源类型获取相关资源
	 * @param _rsdaId
	 * @param rmstId
	 * @param rltyId
	 * @return
	 */
	public List<Object[]> getSimilarRes(Long _rsdaId, Long rmstId, Long rltyId);
	
	/**
	 * 保存检索记录
	 * @param searchVal
	 */
	public void saveSearchKey(String searchVal);
	
	/**
	 * 资源高级检索
	 * @param p
	 * @param params
	 * @param sort
	 * @return
	 */
	public List<Object[]> getResAdvSearch(Pagination p, Map<String, Object> params, String rmstIds, String rltyIds, String sort);
	
	/**
	 * 获取热门资源
	 * @param num 获取条数
	 * @return
	 */
	public List<Object[]> getHotReslist(int num);
	/**
	 * 通过特色库id获取资源类型
	 * @author MaChao
	 * @date 2019-11-13
	 * @param rmstId
	 * @return
	 */
	public List<Object[]> getRdtyList(Long rmstId);
	/**
	 * 根据作家文本库的作家名获取作品资源
	 * @author MaChao
	 * @date 2019-11-14
	 * @param autor
	 * @return
	 */
	public List<Object[]> getWorksByAutor(String autor);

}
