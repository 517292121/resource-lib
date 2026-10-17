package cn.com.adtech.riss2.bso.resource;

import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;

import com.jbetter.framework.session.Pagination;
/**
 * 作业校验
 * @author MaChao
 * @date 2010-9-13
 */
@SuppressWarnings("unchecked")
public interface IResMissionInfoCheckService extends ICommonService {
	
	/**
	 * 获取资源列表
	 * @param rminId
	 * @return
	 * @author MaChao
	 * @date 2010-9-14
	 */
	List getResSourceDataList(Pagination pagination, Map<String, Object> conditions);
	
	/**
	 * 获取资源详情信息
	 * @param rsdaId
	 * @param rmstId
	 * @param rltyId
	 * @return
	 * @author MaChao
	 * @date 2010-9-17
	 */
	List getResSourceDataDetailInfo(Long rsdaId,Long rmstId,Long rltyId,String type, Long rltyId2);
	
	/**
	 * 修改资源语种状态
	 * @param rsdaId
	 * @param isFirst
	 * @author MaChao
	 * @date 2010-10-14
	 */
	public void updateResSourceLanguageStatus(Long rsdaId,int isFirst,Long status);
	
	/**
	 * 获取语种ID
	 * 
	 * @param rsdaId
	 * @param isFirst
	 * @return
	 */
	public List getRSDFirstLanguage(Long rsdaId,Boolean isFirst,String type,Long rslaRltyId);
	
	/**
	 * 获取作业操作者
	 * @param rminId 
	 * @param userType
	 * @return
	 */
	public long getUserId(long rminId, String userType);
	
	/**
	 * 获取下一条未审核的资源
	 * 
	 * @param rsdaId
	 * @param authorType 资源来源类型
	 * @return
	 * @author MaChao
	 * @date 2010-10-22
	 */
	public List getNextRSD(Long rminId, int authorType,String searchtitle);
	
	/**
	 * 获取下一条其它语种资源信息
	 * 
	 * @param rsdaId
	 * @return
	 * @author MaChao
	 * @date 2010-10-22
	 */
	public List getNextOtherLanguageRSD(Long rsdaId , Long rminId, Long rltyId);
	
	/**
	 * 获取资源信息
	 * @param pagination
	 * @param rminId
	 * @param rltyId
	 * @return
	 * @author MaChao
	 * @date 2010-10-27
	 */
	public List getResSourceDataList(Pagination pagination,String sort , String dir , Long rminId,Long rltyId,Long status, String title);
	
	/**
	 * 会员资源审核
	 * @param resource
	 * @param statu
	 * @param languageId
	 * @return
	 */
	public boolean updateResourceChecked(ResSourceData resource, long statu);
	
	/**
	 * 获取资源语种记录
	 * @param rsdaId
	 * @param isFirst
	 */
	public ResSourceLanguage getResSourceLanguage(Long rsdaId, int isFirst);
	
	/**
	 * 批量审核资源
	 * 
	 * @param rsdaIds
	 * @param rsdaStatus
	 * @param sysManager
	 */
	public void updateBatchCheckResSourceData(String rsdaIds, Long rsdaStatus, SysManager sysManager);
	
	/**
	 * 根据作业ID批量审核作业下所有资源
	 * @param rminId
	 * @param rsdaStatus
	 */
	public void updateCheckAllByMission(long rminId, long rsdaStatus, SysManager sysManager);
}
