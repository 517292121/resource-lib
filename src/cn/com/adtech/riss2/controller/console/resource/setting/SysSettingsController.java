package cn.com.adtech.riss2.controller.console.resource.setting;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ICommonServiceCommonlyService;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.TempletConfig;
import cn.com.adtech.riss2.exception.ErrorMessageException;

/**
 * 参数设置
 * 
 * @author MaChao
 * @date 2010-10-12
 */
@SuppressWarnings("unchecked")
public class SysSettingsController extends MultiActionController {
	/**
	 * 初始化页面
	 * 
	 * @author MaChao
	 * @date 2010-10-12
	 * @param request
	 * @param response
	 * @return
	 */
//	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) {
//		Map<String, String> result = new HashMap<String, String>();
//		// 资源填封面大小
//		result.put(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE, ConstantsObj
//				.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE));
//		// 资源预览文件大小
//		result.put(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_VIEWFILE_MAXSIZE, ConstantsObj
//				.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_VIEWFILE_MAXSIZE));
//		// 资源源文件大小
//		result.put(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE, ConstantsObj
//				.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE));
//		// 馆藏位置
//		result.put(ConstantsObj.SysSettingKey.SOURCE_LOCATION_OPTIONS, ConstantsObj
//				.getSysProperty(ConstantsObj.SysSettingKey.SOURCE_LOCATION_OPTIONS));
//		return new ModelAndView("resource/setting/sysSetting", result);
//	}

	/**
	 * 保存参数
	 * 
	 * @author MaChao
	 * @date 2010-10-12
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
//	public ModelAndView updateSetting(HttpServletRequest request, HttpServletResponse response) throws IOException {
//		response.setContentType("text/html; charset=UTF-8");
//		String msg = "";
//		try {
//			commonlyService.saveAllMmSettings(request);
//			msg = "{success: true, msg: '保存成功！'}";
//		}catch(ErrorMessageException e) {
//			msg = "{success: false, msg:'" + e.getMessage() + "'}";
//		}
//		response.getWriter().write(msg);
//		response.getWriter().flush();
//		return null;
//	}
	
	/**
	 * 初始化系统参数设置
	 * 
	 * @param request
	 * @param response  
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-15
	 */
	public ModelAndView initMmSettingView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("setting/mmSetting");
	}
	
	/**
	 * 网站参数设置
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-4
	 */
	public ModelAndView initWwebsiteSettingView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("setting/websiteSetting");
	}
	
	/**
	 * 系统积分设置
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-4
	 */
	public ModelAndView initAssPointSettingView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("setting/AssPointSetting");
	}
	
	
	/**
	 * 获取系统参数信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-15
	 */
	public void getMmSettingInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			String userUploadResourceIsOpen = ConstantsObj.getSysProperty(MmSettingConstant.getUserUploadResourceIsOpen());
			
			String userRegisetrCheckType = ConstantsObj.getSysProperty(MmSettingConstant.getUserRegisterCheckType());
			String sendMailHost = ConstantsObj.getSysProperty(MmSettingConstant.getSendMailHost());
			String sendMailFromAddress = ConstantsObj.getSysProperty(MmSettingConstant.getSendMailFromAddress());
			String sendMailFromUsername = ConstantsObj.getSysProperty(MmSettingConstant.getSendMailFromUsername());
			String sendMailFromPassword = ConstantsObj.getSysProperty(MmSettingConstant.getSendMailFromPassword());
			//String sensitiveWords = ConstantsObj.getSysProperty(MmSettingConstant.getsensitiveWords());
			String resouceUPloadSourceFileMaxsize = ConstantsObj.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE);
			String resourceUploadViewFileMaxsize = ConstantsObj.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_VIEWFILE_MAXSIZE);
			String resourceUploadPhotoFileMaxsize = ConstantsObj.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE);
			String resouceDownMaxsize = ConstantsObj.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_DOWN_MAXSIZE);
			//String resourceLoctionOptions = ConstantsObj.getSysProperty(ConstantsObj.SysSettingKey.SOURCE_LOCATION_OPTIONS);
			String rsdaRdtyOne = ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE);
			String rsdaRdtyTwo = ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO);
			String rsdaRdtyThree = ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE);
			String resourceRootDir = ConstantsObj.getSysProperty(TempletConfig.RESOURCE_ROOT_DIR);
			
			
			result.put("success", true);
			result.put("userUploadResourceIsOpen", userUploadResourceIsOpen);
			result.put("userRegisetrCheckType", userRegisetrCheckType);
			result.put("sendMailHost", sendMailHost);
			result.put("sendMailFromAddress", sendMailFromAddress);
			result.put("sendMailFromUsername", sendMailFromUsername);
			result.put("sendMailFromPassword", sendMailFromPassword);
			//result.put("sensitiveWords", sensitiveWords);
			result.put("resouceUPloadSourceFileMaxsize", resouceUPloadSourceFileMaxsize);
			result.put("resourceUploadViewFileMaxsize", resourceUploadViewFileMaxsize);
			result.put("resourceUploadPhotoFileMaxsize", resourceUploadPhotoFileMaxsize);
			result.put("resouceDownMaxsize", resouceDownMaxsize);
			//result.put("resourceLoctionOptions", resourceLoctionOptions);
			result.put("rsdaRdtyOneName", rsdaRdtyOne);
			result.put("rsdaRdtyTwoName", rsdaRdtyTwo);
			result.put("rsdaRdtyOneThreeName", rsdaRdtyThree);
			result.put("resourceRootDir", resourceRootDir);

			
			JSONObject jsonObject = JSONObject.fromObject(result);
			out.write(jsonObject.toString());
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'初始化参数失败！'}");
		}
		out.flush();
	}
	
	/**
	 * 获取网站参数信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-4
	 */
	public void getWebsiteSettingInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Map<String, Object> result = new HashMap<String, Object>();
			String defaultPageSize = ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize());
			String defaultIssueAttarchementSize = ConstantsObj.getSysProperty(MmSettingConstant.getDefaultIssueAttachementSize());
			
			String riss2Website = ConstantsObj.getSysProperty(MmSettingConstant.getRiss2Website());
			String webSiteFooter4zh = ConstantsObj.getSysProperty(MmSettingConstant.getWebSiteFooter4zh());
			String webSiteFooter4en = ConstantsObj.getSysProperty(MmSettingConstant.getWebSiteFooter4en());
			String webSiteFooter4zw = ConstantsObj.getSysProperty(MmSettingConstant.getWebSiteFooter4zw());
			String riss2IndexHotResource = ConstantsObj.getSysProperty(MmSettingConstant.getRiss2IndexHotResource());
			String unloginUserCanBrowseVideoNumber = ConstantsObj.getSysProperty(MmSettingConstant.getUnLoginUserCanBrowseVideoNumber());
			String allowUserUploadFileType_Picture = ConstantsObj.getSysProperty(MmSettingConstant.getAllowUserUploadFileTypePicture());
			String allowUserUploadFileType_Document = ConstantsObj.getSysProperty(MmSettingConstant.getAllowUserUploadFileTypeDocument());
			String allowUserUploadFileType_Video = ConstantsObj.getSysProperty(MmSettingConstant.getAllowUserUploadFileTypeVideo());
			String allowUserUploadFileType_Audieo = ConstantsObj.getSysProperty(MmSettingConstant.getAllowUserUploadFileTypeAudieo());
			String riss2DefaultSkin = ConstantsObj.getSysProperty(MmSettingConstant.getRiss2DefaultSkin());
			
			
			
			
			
			
			result.put("success", true);
			result.put("defaultPageSize", defaultPageSize);
			result.put("defaultIssueAttarchementSize", defaultIssueAttarchementSize);
			result.put("riss2Website", riss2Website);
			result.put("webSiteFooter4zh", webSiteFooter4zh);
			result.put("webSiteFooter4en", webSiteFooter4en);
			result.put("webSiteFooter4zw", webSiteFooter4zw);
			result.put("riss2IndexHotResource", riss2IndexHotResource);
			result.put("unloginUserCanBrowseVideoNumber", unloginUserCanBrowseVideoNumber);
			result.put("allowUserUploadFileType_Picture", allowUserUploadFileType_Picture);
			result.put("allowUserUploadFileType_Document", allowUserUploadFileType_Document);
			result.put("allowUserUploadFileType_Video", allowUserUploadFileType_Video);
			result.put("allowUserUploadFileType_Audieo", allowUserUploadFileType_Audieo);
			result.put("riss2DefaultSkin", riss2DefaultSkin);
			
			
			JSONObject jsonObject = JSONObject.fromObject(result);
			out.write(jsonObject.toString());
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'初始化网站参数失败！'}");
		}
		out.flush();
	}
	
	/**
	 * 获取系统积分信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-4
	 */
	public void getAssPointsettingInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Map<String, Object> result = new HashMap<String, Object>(); 
			String loginpoint = ConstantsObj.getSysProperty(MmSettingConstant.getAsspoint_Login());
			String machiningres = ConstantsObj.getSysProperty(MmSettingConstant.getAsspoint_machining_Res());
			String machiningmessage = ConstantsObj.getSysProperty(MmSettingConstant.getAsspoint_machining_message());
			 
			
			result.put("success", true); 
			result.put("loginpoint", loginpoint); 
			result.put("machiningres", machiningres); 
			result.put("machiningmessage", machiningmessage);
			
			
			JSONObject jsonObject = JSONObject.fromObject(result);
			out.write(jsonObject.toString());
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'初始化积分信息失败！'}");
		}
		out.flush();
	}
	
	/**
	 * 更新系统参数设置
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-15
	 */
	public void saveMmSetting(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			// 获取参数值
			String userUploadResourceIsOpen = ServletRequestUtils.getStringParameter(request, "user_upload_resource_is_open","1");// 是否允许上传资源
			String userRegisetrCheckType = ServletRequestUtils.getStringParameter(request, "user_register_check_type","");// 会员审核方式
			String sendMailHost = ServletRequestUtils.getStringParameter(request, "sendmail_host","");// 邮件服务器SMTP
			String sendMailFromAddress = ServletRequestUtils.getStringParameter(request, "sendmail_from_address","");// 发送邮件名
			String sendMailFromUsername = ServletRequestUtils.getStringParameter(request, "sendmail_from_username","");// 发送邮件用户名
			String sendMailFromPassword = ServletRequestUtils.getStringParameter(request, "sendmail_from_password","");// 发送邮件密码
			String sensitiveWords = ServletRequestUtils.getStringParameter(request, "sensitive_words","");// 敏感词
			
			
			String resouceUPloadSourceFileMaxsize = ServletRequestUtils.getStringParameter(request, "RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE","");// 资源文件
			String resourceUploadViewFileMaxsize = ServletRequestUtils.getStringParameter(request, "RESOURCE_UPLOAD_VIEWFILE_MAXSIZE","");// 预览文件
			String resourceUploadPhotoFileMaxsize = ServletRequestUtils.getStringParameter(request, "RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE","");// 图片
			String resourceLoctionOptions = ServletRequestUtils.getStringParameter(request, "SOURCE_LOCATION_OPTIONS","");// 馆藏地
			String resouceDownMaxsize = ServletRequestUtils.getStringParameter(request, "RESOURCE_DOWN_MAXSIZE","");//允许用户每天下载资源的次数
			
			String rsdaRdtyOne = ServletRequestUtils.getStringParameter(request, "rsda_rdty_one_name","");
			String rsdaRdtyTwo = ServletRequestUtils.getStringParameter(request, "rsda_rdty_two_name","");
			String rsdaRdtyThree = ServletRequestUtils.getStringParameter(request, "rsda_rdty_three_name","");
			String resourceRootDir = ServletRequestUtils.getStringParameter(request, TempletConfig.RESOURCE_ROOT_DIR, "").replaceAll("\\\\", "/");

			
			// 将获取出来的参数值根据key键，一一放入map中；
			Map conditions = new HashMap();
			conditions.put(MmSettingConstant.getUserUploadResourceIsOpen(), userUploadResourceIsOpen);
			
			conditions.put(MmSettingConstant.getUserRegisterCheckType(), userRegisetrCheckType);
			conditions.put(MmSettingConstant.getSendMailHost(), sendMailHost);
			conditions.put(MmSettingConstant.getSendMailFromAddress(), sendMailFromAddress);
			conditions.put(MmSettingConstant.getSendMailFromUsername(), sendMailFromUsername);
			conditions.put(MmSettingConstant.getSendMailFromPassword(), sendMailFromPassword);
			conditions.put(MmSettingConstant.getsensitiveWords(), sensitiveWords);
			
			conditions.put(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE, resouceUPloadSourceFileMaxsize);
			conditions.put(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_VIEWFILE_MAXSIZE, resourceUploadViewFileMaxsize);
			conditions.put(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE, resourceUploadPhotoFileMaxsize);
			conditions.put(ConstantsObj.SysSettingKey.SOURCE_LOCATION_OPTIONS, resourceLoctionOptions);
			conditions.put(ConstantsObj.SysSettingKey.RESOURCE_DOWN_MAXSIZE, resouceDownMaxsize);
			
			conditions.put(TempletConfig.RSDA_RDTY_ONE, rsdaRdtyOne);
			conditions.put(TempletConfig.RSDA_RDTY_TWO, rsdaRdtyTwo);
			conditions.put(TempletConfig.RSDA_RDTY_THREE, rsdaRdtyThree);
			conditions.put(TempletConfig.RESOURCE_ROOT_DIR, resourceRootDir);

			
			
			this.commonlyService.saveMmSetting(conditions);
			out.write("{success:true,msg:'更新系统参数成功！'}");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}
	
	/**
	 * 保存网站参数设置
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-4
	 */
	public void saveWebsiteSetting(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			// 获取参数值
			String defaultPageSize = ServletRequestUtils.getStringParameter(request, "default_page_size","20");// 默认列表显示条数
			String defaultIssueAttarchementSize = ServletRequestUtils.getStringParameter(request, "default_issue_attachement_size","10");// 求助回复附件大小
			String riss2Website = ServletRequestUtils.getStringParameter(request, "riss2_website","");// 域名
			String webSiteFooter4zh = ServletRequestUtils.getStringParameter(request, "web_site_footer_zh","");// 版权
			String webSiteFooter4en = ServletRequestUtils.getStringParameter(request, "web_site_footer_en","");// 版权
			String webSiteFooter4zw = ServletRequestUtils.getStringParameter(request, "web_site_footer_zw","");// 版权
			String riss2IndexHotResource = ServletRequestUtils.getStringParameter(request, "riss2_index_hotresource","1");// 热门资源设置
			String unloginUserCanBrowseVideoNumber = ServletRequestUtils.getStringParameter(request, "unlogin_user_can_browse_video_number","");// 游客每天允许访问的视频数
			String allowUserUploadFileType_Picture = ServletRequestUtils.getStringParameter(request, "allow_user_upload_file_type_picture","");// 允许会员上传的资源类型
			String allowUserUploadFileType_Document = ServletRequestUtils.getStringParameter(request, "allow_user_upload_file_type_document","");// 允许会员上传的资源类型
			String allowUserUploadFileType_Audieo = ServletRequestUtils.getStringParameter(request, "allow_user_upload_file_type_audieo","");// 允许会员上传的资源类型
			String allowUserUploadFileType_Video = ServletRequestUtils.getStringParameter(request, "allow_user_upload_file_type_video","");// 允许会员上传的资源类型
			Long riss2DefaultSkin = ServletRequestUtils.getLongParameter(request, "riss2_default_skin",2l);
			
			
			
			
			// 将获取出来的参数值根据key键，一一放入map中；
			Map conditions = new HashMap();
			conditions.put(MmSettingConstant.getDefaultPageSize(), defaultPageSize);
			conditions.put(MmSettingConstant.getDefaultIssueAttachementSize(), defaultIssueAttarchementSize);
			conditions.put(MmSettingConstant.getRiss2Website(), riss2Website);
			conditions.put(MmSettingConstant.getWebSiteFooter4zh(), webSiteFooter4zh);
			conditions.put(MmSettingConstant.getWebSiteFooter4en(), webSiteFooter4en);
			conditions.put(MmSettingConstant.getWebSiteFooter4zw(), webSiteFooter4zw);
			conditions.put(MmSettingConstant.getRiss2IndexHotResource(), riss2IndexHotResource);
			conditions.put(MmSettingConstant.getUnLoginUserCanBrowseVideoNumber(), unloginUserCanBrowseVideoNumber);
			conditions.put(MmSettingConstant.getAllowUserUploadFileTypeAudieo(), allowUserUploadFileType_Audieo);
			conditions.put(MmSettingConstant.getAllowUserUploadFileTypeDocument(), allowUserUploadFileType_Document);
			conditions.put(MmSettingConstant.getAllowUserUploadFileTypePicture(), allowUserUploadFileType_Picture);
			conditions.put(MmSettingConstant.getAllowUserUploadFileTypeVideo(), allowUserUploadFileType_Video);
			conditions.put(MmSettingConstant.getRiss2DefaultSkin(), riss2DefaultSkin+"");
			
			
			this.commonlyService.saveMmSetting(conditions);
			out.write("{success:true,msg:'更新网站参数成功！'}");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}
	
	/**
	 * 保存系统积分设置
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-4
	 */
	public void saveAssPointSetting(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			// 获取参数值
			String asspoint_login = ServletRequestUtils.getStringParameter(request, "asspoint_login","0");// 默认列表显示条数
			String asspoint_machining_res = ServletRequestUtils.getStringParameter(request, "asspoint_machining_res","0");// 求助回复附件大小
			String asspoint_machining_message = ServletRequestUtils.getStringParameter(request, "asspoint_machining_message","0");// 域名 
			
			// 将获取出来的参数值根据key键，一一放入map中；
			Map conditions = new HashMap();
			conditions.put(MmSettingConstant.getAsspoint_Login(),asspoint_login);
			conditions.put(MmSettingConstant.getAsspoint_machining_Res(),asspoint_machining_res);
			conditions.put(MmSettingConstant.getAsspoint_machining_message(), asspoint_machining_message); 
			
			
			this.commonlyService.saveMmSetting(conditions);
			out.write("{success:true,msg:'更新系统积分成功！'}");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}

	private ICommonServiceCommonlyService commonlyService = null;

	public void setCommonlyService(ICommonServiceCommonlyService commonlyService) {
		this.commonlyService = commonlyService;
	}
}
