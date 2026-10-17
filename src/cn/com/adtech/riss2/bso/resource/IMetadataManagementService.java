package cn.com.adtech.riss2.bso.resource;

import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResStoreLiter;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;

import com.jbetter.framework.session.Pagination;

/**
 * 元数据管理
 * 
 * @author MaChao
 * @date 2010-8-31
 */
@SuppressWarnings("unchecked")
public interface IMetadataManagementService extends ICommonService {
	/**
	 * 获取特色库数据
	 * 
	 * @return
	 * @author MaChao
	 * @date 2010-8-31
	 */
	List getResMetaStoreList(Pagination pagination, Map<String, Object> conditions, int rmpeType, long userId);

	/**
	 * 获取具有管理权限的特色库列表
	 * 
	 * @param pagination
	 * @param conditions
	 * @return
	 * @author MaChao
	 * @date 2010-9-19
	 */
	List getResMetaStoreListByRmpeType(Pagination pagination, Map<String, Object> conditions);

	/**
	 * 获取文献类型列表
	 * 
	 * @return
	 * @author MaChao
	 * @date 2010-8-31
	 */
	List getResLiterTypeObejctList();

	List getRltyIdsList(Long rmstId);

	/**
	 * 获取管理员列表
	 * 
	 * @return
	 * @author MaChao
	 * @date 2010-8-31
	 */
	List getSysManagerObjectList();

	List getSysManIdsList(Long rmstId);

	/**
	 * 判断特色库名称是否存在
	 * 
	 * @param arg
	 * @return
	 * @author MaChao
	 * @date 2010-9-1
	 */
	ResMetaStore isResMetaStoreRmstNameExit(String arg);

	/**
	 * 获取特色库资源
	 * 
	 * @param rmstId
	 * @return
	 * @author MaChao
	 * @date 2010-9-2
	 */
	List getResStoreLiterList(Long rmstId);

	/**
	 * 获取特色库管理员
	 * 
	 * @param rmstId
	 * @param rmpeType
	 * @param adminId
	 * @return
	 * @author MaChao
	 * @date 2010-9-2
	 */
	List getResMetaPermList(Long rmstId, Long rmpeType, Long adminId, Long type);

	/**
	 * 删除特色库
	 * 
	 * @param rmstId
	 * @param rltyId
	 * @author MaChao
	 * @date 2010-9-3
	 */
	ResStoreLiter getResStoreLiter(Long rmstId, Long rltyId);

	/**
	 * 获取资源对应的元素池
	 * 
	 * @param rltyId
	 * @return
	 * @author MaChao
	 * @date 2010-9-3
	 */
	List getResElementPoolList(Long rltyId);

	/**
	 * 删除特色库
	 * 
	 * @param rmstId
	 * @author MaChao
	 * @date 2010-9-6
	 */
	boolean deleteResMetaStore(String basePath, Long rmstId);

	/**
	 * 获取特色库分类
	 * 
	 * @param rdtyId
	 * @param rmstId
	 * @param rdtyPid
	 * @param rdtyName
	 * @param rdtyGrade
	 * @return
	 * @author MaChao
	 * @date 2010-9-6
	 */
	List getResDynamicTypeList(Long rdtyId, Long rmstId, Long rdtyPid, String rdtyName, Long rdtyGrade);

	/**
	 * 批量删除元素
	 * 
	 * @param rmstId
	 * @param rltyId
	 * @author MaChao
	 * @date 2010-9-16
	 */
	void deleteResTempletMetaByBatch(Long rmstId, Long rltyId);

	/**
	 * 获取特色库权限列表
	 * 
	 * @param pagination
	 * @param conditions
	 * @return
	 * @author MaChao
	 * @date 2010-9-19
	 */
	List getResMetaPermList(Pagination pagination, Map<String, Object> conditions);

	/**
	 * 获取管理员列表
	 * 
	 * @param rmstId
	 * @param rmpeType
	 * @param smanId
	 * @return
	 * @author MaChao
	 * @date 2010-9-20
	 */
	List getSysManagerList(Long rmstId, Long rmpeType, Long smanId, String type);

	/**
	 * 获取语言分类列表
	 * 
	 * @param sort
	 * @param dir
	 * @return
	 * @author MaChao
	 * @date 2010-9-21
	 */
	List getResLanguageTypeList(String rltyName, String sort, String dir);

	/**
	 * 获取到所有的学科分类
	 * 
	 * @return
	 * @author MaChao
	 * @date 2010-9-24
	 */
	List getResSubjectTypeList(Long rstyPid, String rstyName);

	/**
	 * 批量删除分类
	 * 
	 * @param rstyId
	 * @return
	 * @author MaChao
	 * @date 2010-9-24
	 */
	boolean deleteBatchResSubjectType(Long rstyId);
	
	/**
	 * 获得学科分类列表
	 * @param rmstyId
	 * @param rstyPid
	 * @param rstyName
	 * @param rstyGrade
	 * @return
	 * @author MaChao
	 * @date 2010-9-28
	 */
	List getResSubjectTypeList(Long rmstyId,Long rstyPid,String rstyName,Long rstyGrade);
	List getResSubjectTypeList(Long pid, Long rstyId, String rstyName,Long rstyGrade , Integer rstyIndex, String type);
	
	List getResSubjectTypeList();
	
	/**
	 * 获取学科分类信息
	 * @param pid
	 * @param rstyIndex
	 * @return
	 * @author MaChao
	 * @date 2010-10-19
	 */
	ResSubjectType getResSubjectTypeIndex(Long pid,String rstyName , Integer rstyIndex,String type);
	
	/**
	 * 获取自定义分类信息
	 * @param pId
	 * @param rmstId
	 * @param rdtyName
	 * @param rdtyIndex
	 * @param type
	 * @return
	 * @author MaChao
	 * @date 2010-10-21
	 */
	List getResDynamicTypeList(Long pid, Long rmstId, String rdtyName,Long rdtyGrade , Integer rdtyIndex, String type, int rdtyType);
	
	/**
	 * 修改自定义分类排序
	 * @param index
	 * @author MaChao
	 * @date 2010-10-21
	 */
	void updateResDynamicType(Long rdtyId , Integer index);
	
	/**
	 * 获取特色库下评论开关开启的资源
	 * 
	 * @param rmstId
	 * @return
	 * @author MaChao
	 * @date 2010-10-30
	 */
	List getResSourceData(Long rmstId,Long rsdaCmtstus);
	
	/**
	 * 批量修改资源评论状态
	 * @param ids
	 * @author MaChao
	 * @date 2010-10-30
	 */
	Boolean updateResSourceDataCmtStusByBatch(String ids,Long rsdaCmtstus);
	
	/**
	 * 获取某个特色库下面的所有资源
	 * @param metastoreId
	 * @author MaChao
	 * @return
	 */
	List<ResSourceData> getAllRessourcesByMetastoreId(Long metaStoreId, Pagination pagination);
	
	/**
	 * 增加特色库,主要是为了增加特色库的同时添加到会员中的RSS订阅中
	 * @author MaChao
	 */
	void addMetaStore(String basePath, ResMetaStore metastore);
	
	/**
	 * 获得特色库IP配置列表数据
	 * @author MaChao
	 * @return List
	 */
	public List getResStoreIpDeployList(Pagination pagination, Map<String, Object> conditions, Long metaStoreId);
	/**
	 * 获取特色库用来主页展示
	 * @param page
	 * @return
	 */
	public List getResmMetaStoreList();
	
}
