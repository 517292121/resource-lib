package cn.com.adtech.riss2.controller.platform.member;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.SysEmailActivation;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.upload.IUploadService;

/**
 * 激活用户
 * 
 */
public class CheckRegisterEmailUrlController extends AbstractController {
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
				Long suinId = 0L;
				Long key = 0L;
				try {
					 suinId = Long.parseLong(this.localUploadService.decode(uid));
					 key = Long.parseLong(this.localUploadService.decode(ukey));
					
				}catch(Exception e) {
					throw new ErrorMessageException("非法的用户请求！");
				}
				SysEmailActivation sysEmailActivation = this.sysUserPlatformService.getSysEmailActivation(suinId, key);
				if(sysEmailActivation == null)
					throw new ErrorMessageException("非法的用户请求！");
				
				// 修改用户状态
				SysUserinfo sysUserinfo = sysEmailActivation.getSysUserinfo();
				sysUserinfo.setSuinState(SysUserinfo.STATE_PAST);
				this.sysUserPlatformService.save(sysUserinfo);
				
				// 删除激活请求记录
				this.sysUserPlatformService.delete(sysEmailActivation);
				
				response.sendRedirect(request.getContextPath()+"/forgetPassword.htm?action=showMsg&type="+localUploadService.encode("8"));
				return null;
			}
		}catch(Exception e) {
			map.put("msg", e.getMessage());
			return new ModelAndView("platform/pc/userGetPasswordTips",map);
		}
		return null;
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
