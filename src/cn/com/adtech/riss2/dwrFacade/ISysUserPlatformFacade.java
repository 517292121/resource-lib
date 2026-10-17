package cn.com.adtech.riss2.dwrFacade;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.adtech.riss2.domain.resource.ResSourceComt;

/**
 * 前台检索及会员中心服务Dwr接口
 * 
 * @author MaChao
 * @date 2010-10-18
 */
public interface ISysUserPlatformFacade {
	/**
	 * 获得评论列表（有分页）
	 * 
	 * @author MaChao
	 * @date 2010-10-18
	 * @param rsdaId
	 *            资源id
	 * @param pagination
	 * @param request
	 * @return
	 */
	public String getResSourceComtList(Long rsdaId, int currentPage, HttpServletRequest request);

	/**
	 * 评论资源
	 * 
	 * @author MaChao
	 * @date 2010-10-18
	 * @param rsdaId
	 *            资源id
	 * @param reviewContent
	 *            评论内容
	 * @param grade
	 *            评分
	 * @param request
	 * @return
	 */
	public String saveResSourceReview(Long rsdaId, String reviewContent, Short grade, HttpServletRequest request);

	/**
	 * 回复评论
	 * 
	 * @author MaChao
	 * @date 2010-10-18
	 * @param rsdaId
	 *            资源id
	 * @param rscoId
	 *            评论id
	 * @param replyContent
	 *            回复内容
	 * @param request
	 * @return
	 */
	public String saveResSourceReviewReply(Long rsdaId, Long rscoId, String replyContent, HttpServletRequest request);

	/**
	 * 获得资源回复列表
	 * 
	 * @author MaChao
	 * @date 2010-10-27
	 * @param rscoId
	 *            评论id
	 * @param request
	 * @return
	 */
	public String getResSourceReviewReplyList(Long rscoId, HttpServletRequest request);

	/**
	 * 语种切换
	 * 
	 * @author MaChao
	 * @date 2010-11-2
	 * @param rsdaId
	 *            资源id
	 * @param rltyId
	 *            语种id
	 * @param request
	 * @return
	 */
	public String cutoverLanguageForResourcesById(Long rsdaId, Long rltyId, HttpServletRequest request);

	/**
	 * 获得资源基本信息列表_首页
	 * 
	 * @author MaChao
	 * @date 2010-11-23
	 * @param selectType
	 *            [1.推荐；2.热门]
	 * @param formatType
	 *            [0.全部；1.图片；2.文档；3.视频；4.音频；]
	 * @param sortType
	 *            [发布：1.升序；2.降序]
	 * @param currentPage
	 * @param request
	 * @return
	 */
	public String getSourceDataListForIndex(int selectType, int formatType, int sortType, int currentPage, HttpServletRequest request);

	/**
	 * 获取资源浏览历史
	 * 
	 * @param suinId
	 * @param rsdaId
	 * @return
	 * @author MaChao
	 * @date 2010-12-6
	 */
	public String getSourceBrowseHistory(String cookieIds, HttpServletRequest request);

	/**
	 * 检测用户名是否已经被使用
	 * 
	 * @param loginName
	 * @return
	 * @author MaChao
	 * @date 2010-12-9
	 */
	public String checkUserNameIsExit(String loginName);

	/**
	 * 资源详细页面，添加个性标签
	 * 
	 * @author MaChao
	 * @date 2010-12-21
	 * @param rsdaId
	 *            资源id
	 * @param rtinTitle
	 *            标签标题
	 * @param request
	 * @return
	 */
	public String saveSelfTag(Long rsdaId, String rtinTitle, HttpServletRequest request);

	/**
	 * 检测邮箱是否使用
	 * 
	 * @param suinEmail
	 * @return
	 * @author MaChao
	 * @date 2010-12-27
	 */
	public String checkEmailIsUse(String suinEmail);

	/**
	 * 保存用户样式
	 * 
	 * @param type
	 * @param request
	 */
	public String saveSysUserStyle(String type, HttpServletRequest request, HttpServletResponse response);

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
	 * @param request
	 * @return
	 */
	public String getCorrelationSourceDataList(Long rsdaId, Long rsdaRltyId, Long rsdaRmstId, HttpServletRequest request);

	/**
	 * 本学科热门资源
	 * 
	 * @author MaChao
	 * @date 2011-1-14
	 * @param rsdaId
	 *            资源id
	 * @param rsdaRstyId
	 *            学科分类id
	 * @param request
	 * @return
	 */
	public String getSubjectHotSourceDataList(Long rsdaId, Long rsdaRstyId, HttpServletRequest request);

	/**
	 * 批量删除标签
	 * 
	 * @param ids
	 * @return
	 * @author MaChao
	 * @date 2011-1-14
	 */
	public String delTagInfo(String ids, HttpServletRequest request);

	/**
	 * 获取资源标签
	 * 
	 * @param suinId
	 * @param rsdaId
	 * @return
	 * @author MaChao
	 * @date 2011-1-14
	 */
	public String getResourceTagInfoList(Long suinId, Long rsdaId, HttpServletRequest request);
	
	/**
	 * 保存资源评分
	 * 
	 * @param grade
	 * @param rsdaId
	 * @return
	 * @author MaChao
	 * @date 2011-03-10
	 */
	public String saveResSourceGrade(Long rsdaId, Short grade, HttpServletRequest request, HttpServletResponse response); 
	
	/**
	 * 获得用户定义关键字信息列表
	 * 
	 * @param userId
	 * @return 
	 * @author MaChao
	 * @date 2011-03-15
	 */
	public String getKeyWordsInfoList(Long userId,HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 删除用户定义关键字信息
	 * 
	 * @param ids
	 * @return 
	 * @author MaChao
	 * @date 2011-03-16
	 */
	public String delKeyWordsInfo(String ids,HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 添加用户定义关键字信息
	 * 
	 * @param keywords
	 * @return 
	 * @author MaChao
	 * @date 2011-03-16
	 */
	public String addKeyWordsInfo(String keywords,HttpServletRequest request, HttpServletResponse response);
	
}
