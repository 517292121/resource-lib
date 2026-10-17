package cn.com.adtech.riss2.controller;

/**
 * 密码修改控制器
 * @author MaChao
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.core.CurrentUserSession;
import cn.com.adtech.riss2.core.PasswordAuthenticator;
import cn.com.adtech.riss2.domain.SysManager;

public class ChangePasswordController extends MultiActionController{

	private ICommonService commonService;

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}
	
	// 进入页面
	public ModelAndView initializePage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return new ModelAndView("../../pages/pages/changepwd"); 
	}
	
	public ModelAndView changepwd(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		response.setContentType("text/html; charset=UTF-8");
		String pwdStart = ServletRequestUtils.getStringParameter(request, "pwdStart", "");
        String pwdNew = ServletRequestUtils.getStringParameter(request, "pwdNew", "");
        
        CurrentUserSession userSession = (CurrentUserSession) CurrentUserSession.findUserSession(request);
        SysManager sysUser = userSession.getCurrentUser();
        try {
			if(!PasswordAuthenticator.authenticatePassword(pwdStart, sysUser.getSmanPwd())) {
				sysUser = null;
			}
		}catch(Exception e) {
			response.getWriter().write("{failure:true,errors:'密码验证失败'}");
			response.getWriter().flush();
			sysUser = null;
			return null;
		}
		if(sysUser==null){
			response.getWriter().write("{failure:true,errors:'密码验证失败'}");
			response.getWriter().flush();
			return null;
		}
		sysUser.setSmanPwd(PasswordAuthenticator.createPassword(pwdNew));
		commonService.save(sysUser);
		response.getWriter().write("{success:true,msg:'密码修改成功'}");
		response.getWriter().flush();
		return null;
	}
	
}
