package cn.com.adtech.riss2.bso;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.domain.IdsInformation;
import cn.com.adtech.riss2.domain.IdsInformationType;
import cn.com.adtech.riss2.domain.SysEmailActivation;
import cn.com.adtech.riss2.domain.SysForgetPassword;
import cn.com.adtech.riss2.domain.SysUserType;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.ib.IndexBanner;
import cn.com.adtech.riss2.domain.infosubscription.InfoSubscription;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceComt;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;
import cn.com.adtech.riss2.domain.resource.ResTagInfo;
import cn.com.adtech.riss2.domain.sc.SysLink;
import cn.com.adtech.riss2.domain.text.ResLibraryClassify;
import cn.com.adtech.riss2.domain.text.ResTextResource;

import com.jbetter.framework.session.Pagination;

/**
 * 前台检索及会员中心服务接口
 * 
 * @author MaChao
 * @date 2010-9-13
 */
public interface ISysUserPlatformService extends ICommonServiceCommonlyService {
	/**
	 * 资源数量统计
	 * 
	 * @author MaChao
	 * @date 2010-9-13
	 * @return
	 */
	public Map<String, Object> getResourceNum(Map<String, Object> map);
	/**
	 * 获得学科分类
	 * 
	 * @author MaChao
	 * @date 2010-9-13
	 * @return
	 */
	public List<ResSubjectType> getSubjectOptions();

	/**
	 * 获得特色库列表
	 * 
	 * @author MaChao
	 * @date 2010-9-13
	 * @return
	 */
	public List<ResMetaStore> getMetaStoreList();

	/**
	 * 获得通知公告列表
	 * 
	 * @author MaChao
	 * @date 2010-9-13
	 * @return
	 */
	public List<IdsInformation> getInformationList();

	/**
	 * 获得资源基本信息列表_首页
	 * 
	 * @author MaChao
	 * @date 2010-9-13
	 * @param pagination
	 * @param type[1.图片；2.文档；3.视频；4.音频；5.高清；]
	 * @param sortParam排序
	 * @param selectType[1.推荐；2.热门]
	 * @param sortType[发布：1.升序；2.降序]
	 * @param more是否是显示更多
	 * @return
	 */
	public List<ResSourceData> getSourceDataListForIndex(Pagination pagination, int type, int sortParam, int selectType, int sortType,
			boolean more);

	/**
	 * 获得热门Tag列表
	 * 
	 * @author MaChao
	 * @date 2010-9-13
	 * @param suinId
	 * @param rsdaId
	 * @return
	 */
	public List<ResTagInfo> getTagInfoList(Long suinId, Long rsdaId);

	/**
	 * 获得热门Tag列表（用于首页）
	 * 
	 * @author MaChao
	 * @date 2010-12-20
	 * @return
	 */
	public List getTagInfoListForIndex();

	/**
	 * 获得资源基本信息列表_简单查询
	 * 
	 * @author MaChao
	 * @date 2010-9-13
	 * @param pagination
	 * @param sortParam
	 * @param threeBigType
	 *            三大类
	 * @param keyWord
	 *            检索词
	 * @param rmstId
	 *            特色库id
	 * @param rstyId
	 *            学科分类id
	 * @return
	 */
	public List<ResSourceData> getSourceDataListForSimpleSearch(Pagination pagination, int sortParam, Long threeBigType, String keyWord,
			Long rmstId, Long rstyId);

	/**
	 * 获得资源详细
	 * 
	 * @author MaChao
	 * @date 2010-9-20
	 * @param basePath
	 * @param rsdaId
	 *            资源id
	 * @param rsdaLanguageId
	 *            语种id
	 * @param request
	 * @return
	 */
	public Map<String, Object> getSourceDataForDetail(String basePath, Long rsdaId, Long rsdaLanguageId, HttpServletRequest request, SysUserType userType);

	/**
	 * 通过登录名获得用户
	 * 
	 * @author MaChao
	 * @data 2010-10-11
	 * @param loginName
	 * @return
	 */
	public SysUserinfo getUserinfoByLoginName(String loginName);

	/**
	 * 获得单个资源的评论列表
	 * 
	 * @author MaChao
	 * @date 2010-10-27
	 * @param rsdaId
	 *            资源id
	 * @param pagination
	 * @param sysUserinfo
	 * @return
	 */
	public List<ResSourceComt> getResSourceComtByRsdaId(Long rsdaId, Pagination pagination, SysUserinfo sysUserinfo);
	
	/**
	 * 获取信息上下篇
	 */
	public IdsInformation getInfoPre(IdsInformation info);
	public IdsInformation getInfoNext(IdsInformation info);


	/**
	 * 获取信息列表
	 */
	public List<IdsInformation> getIdsInformationList(Pagination pagination, Long idstId,String title);

	/**
	 * 获取信息分类列表
	 */
	public List<IdsInformationType> getIdsInformationTypeList();

	/**
	 * 获得资源详细
	 * 
	 * @author MaChao
	 * @date 2010-11-2
	 * @param basePath
	 * @param rsdaId
	 *            资源id
	 * @param rltyId
	 *            语种id
	 * @return
	 */
	public String getSourceDataForDetailByLanguageId(String basePath, Long rsdaId, Long rltyId, HttpServletRequest request);

	/**
	 * 保存资源评论并更新资源评分
	 * 
	 * @author MaChao
	 * @date 2010-11-3
	 * @param resSourceComt
	 */
	public void saveResSourceReview(ResSourceComt resSourceComt);

	/**
	 * 获取浏览历史记录
	 * 
	 * @param suinId
	 * @param rsdaId
	 * @param dir
	 * @return
	 * @author MaChao
	 * @date 2010-12-6
	 */
	public List getSourceBrowseHistory(Long suinId, String dir, Long rsdaId);

	/**
	 * 获得资源基本信息列表_首页(包括评论数)
	 * 
	 * @author MaChao
	 * @date 2010-9-13
	 * @param pagination
	 * @param type[1.图片；2.文档；3.视频；4.音频；]
	 * @param selectType[1.推荐；2.热门]
	 * @param sortType[发布：1.升序；2.降序]
	 * @return
	 */
	public List getSourceDataListForIndexMainList(Pagination pagination, int type, int selectType, int sortType);

	/**
	 * 资源详细页面，添加个性标签
	 * 
	 * @author MaChao
	 * @date 2010-12-21
	 * @param rsdaId
	 * @param suinId
	 * @param rtinTitle
	 * @return
	 */
	public List<ResTagInfo> saveSelfTag(Long rsdaId, Long suinId, String rtinTitle);

	/**
	 * 首页获得热门标签
	 * 
	 * @author MaChao
	 * @date 2010-12-21
	 * @return
	 */
//	public List getHotTagList();
	public List<Object[]> getHotTagList();

	/**
	 * 获取找回密码记录信息
	 * 
	 * @param suinId
	 * @return
	 * @author MaChao
	 * @date 2010-12-21
	 */
	public SysForgetPassword getSysForgetPassword(Long suinId);

	/**
	 * 删除找回密码表中的过期信息
	 */
	public void deleteForgetPasswordRecord();

	/**
	 * 点击查询时更新相应的标签
	 * 
	 * @author MaChao
	 * @date 2010-12-22
	 * @param request
	 */
	public void updateTagInfo(HttpServletRequest request);

	/**
	 * 按特色库和资源类型获取当前资源的上一个或下一个资源
	 * 
	 * @param sourceId
	 * @param type
	 *            1: 上一个 2:下一个
	 * @return
	 */
	public ResSourceData getPreOrNextResource(ResSourceData resource, String type);

	/**
	 * 激活用户
	 * 
	 * @param suinId
	 * @param key
	 * @return
	 */
	public SysEmailActivation getSysEmailActivation(Long suinId, Long key);

	/**
	 * 根据邮箱获取相关用户信息
	 * 
	 * @param suinEmail
	 * @return
	 * @author MaChao
	 * @date 2010-12-27
	 */
	public SysUserinfo getUserinfoBySuinEmail(String suinEmail);

	/**
	 * 获得系统默认用户类型
	 * 
	 * @author MaChao
	 * @date 2010-12-28
	 * @return
	 */
	public SysUserType getDefaultSysUserType();

	/**
	 * 定时任务：更新游客访问记录信息
	 */
	public void updateSysTouristBroseCache();


	/**
	 * 事件监听，站点访问量统计
	 * 
	 * @author MaChao
	 * @date 2011-1-12
	 * @param ip
	 */
	public void saveAccessCounterListener(String ip);
	
	/**
	 * 批量删除
	 * @param rtinIds
	 * @author MaChao
	 * @date 2011-1-14
	 */
	public void deleteTagInfo(String rtinIds);
	
	/**
	 * 相关文档
	 * 
	 * @author MaChao
	 * @date 2011-1-14
	 * @param rsdaId
	 *            资源id
	 * @param rsdaRltyId
	 *            文献类型id
	 * @param rsdaRmstId
	 *            特色库分类id
	 * @return
	 */
	public List getCorrelationSourceDataList(Long rsdaId, Long rsdaRltyId, Long rsdaRmstId);

	/**
	 * 本学科热门资源
	 * 
	 * @author MaChao
	 * @date 2011-1-14
	 * @param rsdaId
	 *            资源id
	 * @param rsdaRstyId
	 *            学科分类id
	 * @return
	 */
	public List getSubjectHotSourceDataList(Long rsdaId, Long rsdaRstyId);
	
	/**
	 * 获取某资源所有有效评分信息
	 * 
	 * @return List<ResSourceComt>
	 * @author MaChao
	 * @date 2011-03-14
	 */
	public Object getResSourceForGrade(Long rscId); 
	
	/**
	 * 获取某登录用户对某资源的评分信息
	 * @param userId 用户ID
	 * @param rscId 资源ID
	 * @return true 该用户已经有过评分
	 * 		   false 该用户没有过评分
	 * @author MaChao
	 * @date 2011-03-14
	 */
	public boolean findRssSourceGradeOfUser(Long userId , Long rscId); 
	
	/**
	 * 获得用户定义关键字信息列表
	 * 
	 * @param userId
	 * @return 
	 * @author MaChao
	 * @date 2011-03-15
	 */
	public List<InfoSubscription> getKeyWordsInfoList(Long userId);
	
	/**
	 * 根据资源 id 获取该资源的附件
	 * @param rsdaId
	 * @return
	 */
	public List<ResSourceFile> getResRourceFile(Long rsdaId, Long selfId);
	
	/**
	 * 根据资源附件 id 获取附件实体
	 * @param rsfiId
	 * @return
	 */
	public ResSourceFile getResRourceFileViewUrl(Long rsfiId);
	
	/**
	 * 根据资源 id 获取资源相关主题的其他资源列表
	 * @param rsdaId
	 * @return
	 */
	public List<ResSourceData> getOtherSubjectList(long rsdaId);
	/**
	 * 根据特色库来获取相关主的其他资源列表
	 * @param rsdaId
	 * @return
	 */
	public List<ResSourceData> getOtherRmstList(long rmstId);
	
	/**
	 * 根据资源id 获取与他不在同一主题下的后一资源
	 * @param rsdaId
	 * @param type 
	 * @return
	 */
	public long getNexRsda(long rsdaId,String type,String sort);
	
	/**
	 * 根据资源id 获取与他不在同一主题下的前一资源
	 * @param rsdaId
	 * @param type  
	 * @return
	 */
	public long getPreRsda(long rsdaId,String type,String sort);
	
	/**
	 * 友情链接
	 * @return
	 */
	public List<SysLink> getSysLink();
	
	/**
	 * banner图片
	 */
	public List<IndexBanner> getBannerList();
	
	/**
	 * 获取资源基本信息
	 *
	 * @author MaChao
	 * @date Oct 28, 2016
	 * @time 4:01:51 PM
	 */
	public Map<String, Object> getDataDetailInfo(ResSourceData sourceData);
	
	/**
	 * 获取文档资源库资源列表
	 * 
	 * @athor lichangkun
	 * 2017-10-20
	 */
	public List<ResTextResource> getTextResList(Pagination pagination, Long rtliId, Long rlclId, String rtreTitle);
	
	/**
	 * 获取资源分类列表
	 * 
	 * @athor lichangkun
	 * 2017-10-20
	 */
	public List<ResLibraryClassify> getResClassfiyList(Long rtliId);
	
}
