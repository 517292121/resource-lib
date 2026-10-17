package cn.com.adtech.riss2.controller.platform.member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.domain.SysForgetPassword;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.upload.IUploadService;

/**
 * 会员登出
 * 
 */
public class CheckEmailUrlController extends AbstractController {
	private ISysUserPlatformService sysUserPlatformService;
	
	private IUploadService localUploadService;

	public IUploadService getLocalUploadService() {
		return localUploadService;
	}

	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}

	public ISysUserPlatformService getSysUserPlatformService() {
		return sysUserPlatformService;
	}

	public void setSysUserPlatformService(ISysUserPlatformService sysUserPlatformService) {
		this.sysUserPlatformService = sysUserPlatformService;
	}
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		try {
			String uid = ServletRequestUtils.getStringParameter(request, "uid","");
			String ukey = ServletRequestUtils.getStringParameter(request, "key","");
			if(CommonUtil.isNotNull(uid) && CommonUtil.isNotNull(ukey)){
				// 当suinId或key为空时，跳转到错误提示页面；
				Long suinId = Long.parseLong(this.localUploadService.decode(uid));
				Long key = Long.parseLong(this.localUploadService.decode(ukey));
				SysForgetPassword sysForgetPassword = this.sysUserPlatformService.getSysForgetPassword(suinId);
				ResourceBundle rb = CommonUtil.getResourceBundle("messages",request);
				String info = rb.getString("forgetpassword.checkurl.info1")+ConstantsObj.FORGET_PASSWORD_MAIL_DAYS+rb.getString("forgetpassword.checkurl.info2")+"<a href='"+request.getContextPath()+"/forgetPassword.htm?action=initForgetPasswordView' style='color:#FF0000'>"+rb.getString("forgetpassword.checkurl.info3")+"</a>。";
				if(sysForgetPassword != null){
					if(sysForgetPassword.getSfpaKey().equals(key)){
						if(compareDate(new Date(),sysForgetPassword.getSfpaDate()) > 0)
							throw new ErrorMessageException(info);
					}else
						throw new ErrorMessageException(info);
				}else
					throw new ErrorMessageException(info);
				
				// 删除密码找回记录
				this.sysUserPlatformService.delete(sysForgetPassword);
				
				// 保存用户相关信息
				map.put("suinId", suinId);
				// 保存找回密码标识，防止恶意操作
				map.put("key", this.localUploadService.encode(suinId.toString()));
			}
		}catch(Exception e) {
			map.put("msg", e.getMessage());
			return new ModelAndView("platform/userGetPasswordTips",map);
		}
		return new ModelAndView("platform/userResetPassword",map);
	}
	
	/**
	 * 比较两个日期大小
	 * @param start
	 * @param end
	 * @return
	 */
	private int compareDate(Date start,Date end){
		int result = 0;
		Long time1 = start.getTime();
		Long time2 = end.getTime();
		if(time1 > time2){
			result = 1;
		}else if(time1 < time2){
			result = -1;
		}
		return result;
	}
}
