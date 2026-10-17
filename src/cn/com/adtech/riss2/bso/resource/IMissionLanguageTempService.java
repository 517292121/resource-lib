package cn.com.adtech.riss2.bso.resource;

import java.io.Serializable;
import java.util.List;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResMissDevolve;
import cn.com.adtech.riss2.domain.resource.ResMissionLanguage;

public interface IMissionLanguageTempService extends ICommonService{
	
	final public static String rmlaStatus = "rmlaStatus";
	final public static String mlaProcessor = "rmlaProcessor";
	final public static String rmlaAssessor = "rmlaAssessor";
	final public static String rmlaProctime = "rmlaProctime";
	final public static String rmlaAssetime = "rmlaAssetime";

	/**
	 * 保存作业和语种的记录
	 * @param missionLanguage
	 * @return
	 */
	public Serializable saveMissionLanguage(ResMissionLanguage missionLanguage);
	
	public boolean updateSingleColumn(long missionId, long languageId, String columnName, Object dataValue); 
	
	public boolean updateSingleColumn(long missLanId, String columnName, Object dataValue); 
	
	public void updateMissionFirstStatu(long missionId, int statu);
	
	public boolean updateMissionLanguageStatu(long rmla_id, int statu);
	/**
	 * 获取作业语种记录
	 * @param missionId 作业ID
	 * @param statu  状态， 匹配ResMissionLanguage对象中的STATU_常量
	 * @return
	 */
	public String getMissionLanguageList(long missionId, String userColumn, int start, int limit);
	
	public boolean saveMissionLanguageAuthor(long rminId, long lanId, int statu, long processor);
	
	/**
	 * 根据作业语种ID删除记录
	 * @param rmla_id
	 */
	public void deleteById(long rmla_id);
	
	/**
	 * 根据作业ID删除记录
	 * @param rminId
	 */
	public void deleteByMissionId(long rminId);
	
	/**
	 * 根据作业ID获取作业第一语种的任务ID
	 * @param rminId
	 * @param type 1：第一语加工， 2：第一语校验
	 * @return
	 */
	public long getMissionFirstId(long rminId);
	
	/**
	 * 获取作业已拥有的语种
	 * @param rminId
	 * @param statu
	 * @return
	 */
	public List<Object> getMissionLanguageByJson(Pagination pagination, long rminId, short statu);
	
	/**
	 * 作业校验
	 * @param missLanId 作业语种ID
	 * @param missId 作业ID
	 * @param isfirst 是否为第一语种
	 * @return
	 */
	public boolean missionChecked(long missLanId, long missId, boolean isfirst, short statu);
	

	/**
	 * 作业转交
	 * @param deliver
	 * @param isfirst
	 * @return
	 */
	public boolean missionDeliver(ResMissDevolve deliver, boolean isfirst);
	
	/**
	 * 其它语任务审核
	 * @param missLanId 作业语种ID
	 */
	public boolean missionChecked(long missLanId, long statu);
	
	/**
	 * 获取作业语种中未审核的资源数
	 */
	public int getUnCheckNumber(long missLanId);
	
	/**
	 * 获取资源语种表中所有记录
	 * @return
	 * @author MaChao
	 * @date 2011-1-25
	 */
	public List getResSourceLanguageList();
	
	/**
	 * 通过作业ID和语种ID获取语种记录
	 * @param rminId 作业ID
	 * @param rltyId 语种ID
	 * @return
	 * @author MaChao
	 * @date 2011-1-25
	 */
	public ResMissionLanguage getResMissionLanguge(Long rminId,Long rltyId);
	
	
	
	
}
