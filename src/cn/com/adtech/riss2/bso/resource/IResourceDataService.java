package cn.com.adtech.riss2.bso.resource;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.calisSchema.MetaDataEntity;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceSubject;

public interface IResourceDataService extends ICommonService{

	
	public void setParams(String params);
	
	public String getDynamicTypeOptions(long store_id, long selectid, int rdtyType);

	/**
	 * 获取资源列表的JSON数据
	 * 
	 * @param userid
	 *            用户ID
	 * @param store_id
	 *            特色库ID
	 * @param sourceType
	 *            资源类型ID
	 * @param rmin_id
	 *            作业ID
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getResourceListJson(long userid, long store_id,
			long sourceType, long rmin_id, int start, int limit);

	/**
	 * 获取指定作业下资源列表的JSON数据
	 * 
	 * @param userid
	 *            用户ID
	 * @param store_id
	 *            特色库ID
	 * @param sourceType
	 *            资源类型ID
	 * @param rmin_id
	 *            作业ID
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getResourceListJsonByMission(long rmin_id, int start,
			int limit, String orderColumn, String orderType);

	public String getResourceListJsonByMission(long rmin_id, long languageId,
			int start, int limit, String sort, String dir, String title,
			int statu);

	/**
	 * 获取所有资源列表的JSON数据
	 * 
	 * @param userid
	 *            用户ID
	 * @param store_id
	 *            特色库ID
	 * @param sourceType
	 *            资源类型ID
	 * @param rmin_id
	 *            作业ID
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getAllResourceListJson(long storeId, long sourceType,
			int start, int limit, String orderColumn, String orderType, String rsdaTitle);
	/**
	 * 后加查询时间
	 */
	public String getAllResourceListJson(long storeId, long sourceType,
			int start, int limit, String orderColumn, String orderType, String rsdaTitle, String beginDay, String endDay);

	/**
	 * 获取资源所有数据以及元素信息
	 * 
	 * @param store
	 *            特色库ID
	 * @param sourceType
	 *            资源类型ID
	 * @param languageId
	 *            语言ID
	 * @param resourceId
	 *            资源ID
	 * @return
	 */
	public List<ResElementPool> getResourceAllData(long store, long sourceType,
			long languageId, long resourceId);

	/**
	 * 获取资源基本信息
	 * 
	 * @param sourceId
	 * @return
	 */
	public ResSourceData getResourceMetaInfo(long sourceId);

	/**
	 * 修改资源
	 * 
	 * @param resource
	 * @return
	 */
	public boolean updateResource(ResSourceData resource,List<ResElementPool> elementList, HttpServletRequest request );

	/**
	 * 删除资源
	 * 
	 * @param resourceId
	 *            资源ID
	 * @param languageId
	 *            语种ID 如果该资源只有一种语种，则删除基本信息和其它相应信息，如果不只一种语种的信息，则不删除资源基本信息
	 * @return
	 */
	public boolean deleteResource(long resourceId, long languageId,
			HttpServletRequest request);

	/**
	 * 删除资源下的具体某种语种的数据
	 * 
	 * @param sourceId
	 * @param languageId
	 * @return
	 */
	public boolean deleteResourceByLanguage(long sourceId, long languageId);

	/**
	 * 删除整个资源
	 * 
	 * @param resourceId
	 * @param request
	 * @return
	 */
	public boolean deleteResource(long resourceId, HttpServletRequest request);

	/**
	 * 设置资源评论状态
	 * 
	 * @param idlist
	 * @param statu
	 * @return
	 */
	public boolean updateCommentStatus(String idlist);

	/**
	 * 设置资源显示状态
	 * 
	 * @param idlist
	 * @param statu
	 * @return
	 */
	public boolean updateUseStatus(String idlist);

	/**
	 * 设置推荐资源状态
	 * 
	 * @param idlist
	 * @param statu
	 * @return
	 */
	public boolean updateRcmdStatus(String idlist);

	/**
	 * 根据特色库ID获取该特色库文件存放盘符
	 * 
	 * @return
	 */
	public String getStoreFolder(long storeId);

	/**
	 * 根据资源ID获取所属作业ID
	 * 
	 * @return
	 */
	public long getSourceRminId(long sourceId);

	public String getSubjectOptions(final long selected, final long rootId,
			final String type);
	/**
	 * 首页rss连接获取最新的资源
	 * @author MaChao
	 * @return
	 */
	public List<ResSourceData> getFeedResSourceDatas(int resultCount);
	
	/**
	 * 会员中心中消息订阅->查看更新
	 * @author MaChao
	 * @return
	 */
	public List<ResSourceData> getResSourceDatasByKeyWords(Long userId, String[] keyWords, Pagination pagination);
	
	/**
	 * 新增或修改主题
	 * @param rssu
	 * @return
	 */
	public boolean saveOrUpdateSubject(ResSourceSubject rssu);
	
	/**
	 * 获取主题json
	 * @param pagination
	 * @param peinErrorData
	 * @param orderColumn
	 * @param orderType
	 * @return
	 */
	public String getResourceSubjectJson( int start, int limit,
			long rssuRminId, String orderColumn, String orderType);
	
	/**
	 * 删除主题
	 * @param rssuID
	 * @return
	 */
	public boolean deleteSubject(long rssuId);
	
	/**
	 * 获取主题下拉列表
	 * @param rminId
	 * @param selectId
	 * @return
	 */
	public String getSourceSubjectOptions(long rminId,long selectId);
	
	/**
	 * 获取主题下拉列表
	 * @param rminId
	 * @param selectId
	 * @return
	 */
	public String getSourceRightsOptions(long selectId);
	
	/**
	 * 根据资源ID查询资源下所有的元数据信息
	 */
	public List<MetaDataEntity> getSourceToList(Long rsdaId);
	
	/**
	 * 按匹配条件查询通过审核的资源的ID
	 */
	public String[] getResIdToAudit(Long storeId , Long sourceType , String rsdaTitle , String beginDay , String endDay);
	
	/**
	 * 修改馆号
	 */
	public Boolean saveLibrary(String library );
	
	/**
	 * 判断该资源是否可被搜索到
	 * @param rsdaId
	 * @return
	 */
	public Boolean getResAllStatusPass(Long rsdaId);
	
	/**
	 * 获取导出excel的资源元数据信息
	 *
	 * @author MaChao
	 * @date 2013-12-5
	 *
	 * @param params
	 * @return
	 */
	public List<Object[]> getExportResource(Map<String, Object> params);
	
	
	/**
	 * 根据资源ID删除相关目录
	 * @param rsdaId
	 * @return
	 */
	public void deleteResSourceCatalogue(Long rsdaId);
	
	/**
	 * 根据资源ID删除相关图片
	 * @param rsdaId
	 * @return
	 */
	public void deleteResSourcePic(Long rsdaId);
	
	/**
	 * 根据资源id删除相关评论
	 * 
	 * @param rsdaId
	 * @author MaChao
	 * @date 2018-4-26
	 */
	public void deleteComt(Long rsdaId);
	
}
