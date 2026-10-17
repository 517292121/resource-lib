package cn.com.adtech.riss2.controller.platform.member;

import java.util.Calendar;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.bso.sc.IScService;
import cn.com.adtech.riss2.controller.common.AbsFormController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ImageScaleTool;
import cn.com.adtech.riss2.core.PasswordAuthenticator;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.upload.IUploadService;

/**
 * 基本信息更新
 * 
 * @author MaChao
 * @date 2010-12-17
 */
public class UpdateUserinfoController extends AbsFormController {
	private IUploadService uploadService;
	
	private ISysUserPlatformService sysUserPlatformService;

	public ISysUserPlatformService getSysUserPlatformService() {
		return sysUserPlatformService;
	}

	public void setSysUserPlatformService(ISysUserPlatformService sysUserPlatformService) {
		this.sysUserPlatformService = sysUserPlatformService;
	}
	
	public IUploadService getUploadService() {
		return uploadService;
	}

	public void setUploadService(IUploadService uploadService) {
		this.uploadService = uploadService;
	}

	@SuppressWarnings("unchecked")
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		SysUserinfo sysUserinfo = (SysUserinfo)makeObject("suinId", SysUserinfo.class, request);
		return sysUserinfo;
	}

	@Override
	protected void validate(HttpServletRequest request, Object obj, Errors errors) {
		/**
		 * 实现国际化验证,Spring会根据session中的locale对象，自动调用属性文件
		 */
		ResourceBundle rb = CommonUtil.getResourceBundle("messages",request);
		SysUserinfo sysUserinfo = (SysUserinfo)obj;
		// 验证密码是否为空
		String old_password = ServletRequestUtils.getStringParameter(request, "old_password","");
		String update_password = ServletRequestUtils.getStringParameter(request, "update_password","");
		if(old_password.trim().length() > 0 && update_password.trim().length() > 0){
			SysUserinfo userinfo = sysUserPlatformService.sysuserLogin(sysUserinfo.getSuinLoginName(), old_password, request.getRemoteHost());
			if(userinfo == null){
				request.setAttribute("error", rb.getString("usercenter.updatepwd.info_6"));
				errors.rejectValue("suinPassword", null, rb.getString("usercenter.updatepwd.info_6"));
				return;
			}else{
				try {
					PasswordAuthenticator.createPassword(update_password);
				}catch(Exception e) {
					request.setAttribute("error", rb.getString("usercenter.updatepwd.info_7"));
					errors.rejectValue("suinPassword", null, rb.getString("usercenter.updatepwd.info_7"));
					return;
				}
			}
		}
		
		
		// 验证基本信息
		String suinName = sysUserinfo.getSuinName();
		if(!CommonUtil.isNotNull(suinName) || suinName.trim().length() == 0){
			request.setAttribute("suinNameError", rb.getString("usercenter.userinfo.suinName.null"));
			errors.rejectValue("suinName", null, rb.getString("usercenter.userinfo.suinName.null"));
			return;
		}else if(suinName.trim().length() > 26){
			request.setAttribute("suinNameError", rb.getString("usercenter.userinfo.suinName.large"));
			errors.rejectValue("suinName", null, rb.getString("usercenter.userinfo.suinName.large"));
			return;
		}
//		if(sysUserinfo.getSuinQuestion().equals("0")){
//			request.setAttribute("suinQuestionError", rb.getString("usercenter.userinfo.suinAnswer.null"));
//			errors.rejectValue("suinQuestion", null, rb.getString("usercenter.userinfo.suinAnswer.null"));
//			return;
//		}
//		String suinAnswer = sysUserinfo.getSuinAnswer();
//		if(!CommonUtil.isNotNull(suinAnswer) || suinAnswer.trim().length() == 0){
//			request.setAttribute("suinAnswerError", rb.getString("usercenter.userinfo.suinAnswer.null"));
//			errors.rejectValue("suinAnswer", null, rb.getString("usercenter.userinfo.suinAnswer.null"));
//			return;
//		}else if(suinAnswer.trim().length()>30){
//			request.setAttribute("suinAnswerError",rb.getString("usercenter.userinfo.suinAnswer.erro"));
//			errors.rejectValue("suinAnswer", null, rb.getString("usercenter.userinfo.suinAnswer.erro"));
//			return;
//		}
//		if(!CommonUtil.isNotNull(sysUserinfo.getSuinEmail()) || sysUserinfo.getSuinEmail().trim().length() == 0){
//			request.setAttribute("suinEmailError", rb.getString("usercenter.userinfo.suinEmail.null"));
//			errors.rejectValue("suinEmail", null, rb.getString("usercenter.userinfo.suinEmail.null"));
//			return;
//		}
		try {
			Short.valueOf(sysUserinfo.getSuinSex());
		}catch(Exception e) {
			request.setAttribute("suinSexError", rb.getString("usercenter.userinfo.suinsex.erro"));
			errors.rejectValue("suinSex", null,  rb.getString("usercenter.userinfo.suinsex.erro"));
			return;
		}
		if(sysUserinfo.getSuinMobileTelephone().trim().length() > 16){
			request.setAttribute("suinMobileTelephoneError", rb.getString("usercenter.userinfo.suinMobileTelephone.erro"));
			errors.rejectValue("suinSex", null,rb.getString("usercenter.userinfo.suinMobileTelephone.erro"));
			return;
		}
		
		// 验证头像
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile("suinImage");
		String suinImage = file.getOriginalFilename();
		if(suinImage != null && suinImage.trim().length() > 1) {
			// 检测图片类型
			if(suinImage.indexOf(".") == -1) {
				request.setAttribute("suinImageLocalNameError", rb.getString("usercenter.userinfo.suinImage.error"));
				errors.rejectValue("suinImageLocalName", null, rb.getString("usercenter.userinfo.suinImage.error"));
				return;
			}
			String[] types = ConstantsObj.SUIN_IMAGE_TYPE.split(",");
			String extName = getExtName(suinImage);
			int i = 0;
			for(String type : types) {
				if(type.equalsIgnoreCase(extName)){
					i++;
					break;
				}
			}
			if(i == 0){
				request.setAttribute("suinImageLocalNameError", rb.getString("usercenter.userinfo.suinImage.error"));
				errors.rejectValue("suinImageLocalName", null, rb.getString("usercenter.userinfo.suinImage.error"));
				return;
			}
			
			
			// 检测图片大小
			if(file.getSize() == 0) {
				request.setAttribute("suinImageLocalNameError", rb.getString("usercenter.userinfo.suinImage.error.filenull"));
				errors.rejectValue("suinImageLocalName", null,  rb.getString("usercenter.userinfo.suinImage.error.filenull"));
				return;
			}
			int SUIN_IMAGE_SIZE = ConstantsObj.SUIN_IMAGE_SIZE;
			Long SIZE = Long.parseLong(1024 * 1024 * SUIN_IMAGE_SIZE+"");
			if(file.getSize() > SIZE) {
				request.setAttribute("suinImageLocalNameError", rb.getString("usercenter.userinfo.suinImage.error.filelarge")+SUIN_IMAGE_SIZE+"M！");
				errors.rejectValue("suinImageLocalName", null,rb.getString("usercenter.userinfo.suinImage.error.filelarge")+SUIN_IMAGE_SIZE+"M！");
				return;
			}
		}
		
		
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj, BindException errors)
			throws Exception {
		// 获取登录用户ID
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		// 保存信息
		SysUserinfo sysUserinfo = (SysUserinfo)obj;
		
		// 更新密码
		String old_password = ServletRequestUtils.getStringParameter(request, "old_password","");
		String update_password = ServletRequestUtils.getStringParameter(request, "update_password","");
		if(old_password.trim().length() > 0 && update_password.trim().length() > 0){
			try {
				sysUserinfo.setSuinPassword(PasswordAuthenticator.createPassword(update_password));
			}catch(Exception e) {
			}
		}
		
		
		/**
		 * 当用户已经更新过头像时，需要将之前的头像删除再上传头像；
		 */
		// 上传头像:以每一个月为单位；
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);//获取年份
	    int month=ca.get(Calendar.MONTH);//获取月份
		String path = ConstantsObj.SUIN_IMAGE + "/" + year + "-" + month;
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile("suinImage");
		
		String suinImage = file.getOriginalFilename();
		if(suinImage != null && suinImage.trim().length() > 1) {
			// 删除头像
			String suinImageLocalName = sysUserinfo.getSuinImageLocalName();
			if(CommonUtil.isNotNull(suinImageLocalName) && suinImageLocalName.trim().length() > 1){
				this.uploadService.deleteFile(path, suinImageLocalName);
			}
			// 上传新的头像
			byte[] img = ImageScaleTool.createThumbPicByAppointWidthAndHeight(file.getBytes(), 250, 250);
			String extName = getExtName(suinImage);
			// 生成实际保存的文件名
			Long pre = sysUserinfo.getSuinId();
			// 原图物理文件名
			String saveFileName = pre + "." + extName;
			String url = "/FileDownload?downloadPath=" + uploadService.encode(path + "/" + saveFileName) + "&fileName="
					+ uploadService.encode(suinImage)+"&isOnLine=true";
			// 上传文件
			this.uploadService.saveFile(path, saveFileName, img);
			sysUserinfo.setSuinImageUrl(url);
			sysUserinfo.setSuinImageLocalName(saveFileName);
		}
		((IScService)getCommomService()).save(sysUserinfo);
		
		request.getSession().setAttribute("sysUserinfo", sysUserinfo);
		
		
		
		response.sendRedirect(request.getContextPath() + "/member/center");
		return null;
	}

	@Override
	protected void supportData(HttpServletRequest request) {}
	
	/**
	 * 返回文件后缀名，以"."开始 。例 文件名"pic.jpg",返回的后缀名"jpg"。
	 * 
	 * @param fileName
	 * @return
	 */
	public String getExtName(String fileName) {
		String extName = "";
		if(fileName != null) {
			extName = fileName.substring(fileName.lastIndexOf('.') + 1);
		}
		return extName;
	}

	/**
	 * 返回文件前缀，如pic.jpg,返回pic
	 * 
	 * @param fileName
	 * @return
	 */
	public String getPreName(String fileName) {
		String preName = "";
		if(fileName != null && !"".equals(fileName)) {
			// preName = fileName.split("\\.")[0];
			preName = fileName.substring(0, fileName.lastIndexOf('.'));
		}
		return preName;
	}
}
