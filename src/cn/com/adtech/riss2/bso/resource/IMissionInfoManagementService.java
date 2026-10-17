package cn.com.adtech.riss2.bso.resource;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;

import com.jbetter.framework.session.Pagination;

/**
 * 作业管理
 * 
 * @author MaChao
 * @date 2010-9-8
 */
@SuppressWarnings("unchecked")
public interface IMissionInfoManagementService extends ICommonService {
	/**
	 * 获取作业列表
	 * 
	 * @param pagination
	 * @param conditions
	 * @return
	 * @author MaChao
	 * @date 2010-9-8
	 */
	List getResMissionInfoList(Pagination pagination, Map<String, Object> conditions);

	/**
	 * 查看作业名称是否存在
	 * 
	 * @param rminRmstId
	 * @param rminMissName
	 * @return
	 * @author MaChao
	 * @date 2010-9-9
	 */
	ResMissionInfo getResMissionInfo(Long rminRmstId, Long rminRltyId, String rminMissName);


	/**
	 * 获取管理员列表
	 * 
	 * @param rmpeType 
	 * @param rmstId
	 * @return
	 */
	List getSysManagerList(Long rmpeType, Long rmstId , Long smanId);
	
	/**
	 * 根据作业ID获取作业对象
	 * @param rminId
	 * @return
	 */
	public ResMissionInfo getResMissionInfoById(long rminId);
	
	
	/**
	 * 作业提交审核
	 * @param missionId
	 * @param statu
	 * @return
	 */
	public boolean updateMissionStatus(long missionId, long statu);
	
	public String getMissionNameById(long rminId);
	
	/**
	 * 获取作业下对应的资源
	 * 
	 * @param rminId
	 * @return
	 * @author MaChao
	 * @date 2010-10-13
	 */
	public List getResSourceDataList(Long rminId,Long rsdaStatus);
	
	/**
	 * 获取作业下所有资源
	 * 
	 * @param rminId
	 * @return
	 * @author MaChao
	 * @date 2010-10-13
	 */
	public List getResSourceDataByMission(Long rminId);
	
	/**
	 * 获取作业下对应的资源
	 * 
	 * @param rminId
	 * @return
	 * @author MaChao
	 * @date 2010-10-13
	 */
	public List getOtherLanguageResSourceDataList(Long rminId);
	
	/**
	 * 查询其它语录入的所有作业
	 * @param store
	 * @param sourceType
	 * @param statu
	 * @param userId
	 * @param permType
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getInterpretMissionListByJson(long store, long sourceType, int statu, long userId, int start, int limit, String orderColumn, String orderType);
	
	/**
	 * 设置作业操作人员
	 * @param userType 操作类型，2：其它语录入，3：第一语校验，4：其它语校验
	 * @param userid
	 * @return
	 */
	public boolean setMissionUser(int userType, long userid, long missionId, long languageId);
	
	/**
	 * 获取作业校验列表
	 * @param store 特色库ID
	 * @param sourceType  
	 * @param userId
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getCheckedMissionListByJson(long store, long sourceType, long userId, int start, int limit, String orderColumn, String orderType);
	
	/**
	 * 其它语校验作业列表
	 * @param store
	 * @param sourceType
	 * @param userId
	 * @param start
	 * @param limit
	 * @return
	 */
	public String getOtherLanguageCheckedMissionListByJson(long store, long sourceType, long userId, int start, int limit, String orderColumn, String orderType);
	
	
	/**
	 * 获取作业的资源总数
	 * @param missionId
	 * @return
	 */
	public long getMissionCountSource(long missionId);
	
	/**
	 * 保存资源 (第一语种资源)
	 * @param resource  资源对象
	 * @param languageId  语种（即作业第一语种）
	 * @param userId 登录用户ID
	 * @param userName 登录用户名
	 */
	public long saveResource(ResSourceData resource, List<ResElementPool> elementList, HttpServletRequest request);
	
	/**
	 * 保存资源的附件 (第一语种资源)
	 * @param resFile 附件对象
	 * @return
	 */
	public long saveAttachmentFile(ResSourceFile resFile);
	
	/**
	 * 查询资源的附件 (第一语种资源)
	 * @param resFile 附件对象
	 * @return
	 */
	public List<ResSourceFile> getResSourceFileToResSourceId(Long resId);
	
	/**
	 * 删除资源的附件 (第一语种资源)
	 * @param resFile 附件对象
	 * @return
	 */
	public boolean deleteResSourceFile(Long resId);
	
	/**
	 *修改资源的附件 (第一语种资源)
	 * @param resFile 附件对象
	 * @return
	 */
	public boolean updateResSourceFileIndex(Long resFId , int index);
	/**
	 *找到新增INDEX (第一语种资源)
	 * @param resFile 附件对象
	 * @return
	 */
	public int getFileIndex(Long resSourceId);
	/**
	 * 第一语审核
	 * @param missionInfo
	 * @return
	 */
	public boolean checkFirstLanguage(ResMissionInfo missionInfo, Long rminId);

	public ResMissionInfo saveMissionInfo(ResMissionInfo mission);
	
	public String getSourceViewHtml(List<ResElementPool> elementList, HttpServletRequest request);
	
	/**
	 * 获取系统默认的作业名称
	 * @return
	 */
	public String getMissionDefaultName();
	
	
	/**
	 * 根据作业id 删除主题
	 * @param rminId
	 */
	public void deleteSubjectByRminId(long rminId);
	
}
