package cn.com.adtech.riss2.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ICommonServiceCommonlyService;
import cn.com.adtech.riss2.core.CurrentUserSession;
import cn.com.adtech.riss2.domain.SysManager;

/**
 * 登录验证
 * 
 * @author MaChao
 */
@SuppressWarnings("unchecked")
public class LoginController extends MultiActionController {
	// private ICommonService commonService;
	private ICommonServiceCommonlyService commonlyService;

	public void setCommonlyService(ICommonServiceCommonlyService commonlyService) {
		this.commonlyService = commonlyService;
	}

	/**
	 * 管理员登录
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView consoleLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		CurrentUserSession userSession = (CurrentUserSession)CurrentUserSession.findUserSession(request);
		userSession.setCurrentUser(null);
		String loginName = ServletRequestUtils.getStringParameter(request, "userName");
		String pwd = ServletRequestUtils.getStringParameter(request, "pwd");
		request.setAttribute("loginName", loginName);
		SysManager sysManager = commonlyService.managerLogin(loginName, pwd, request.getRemoteHost());
		if(sysManager == null){
			request.setAttribute("error", "用户名或密码错误");
			return new ModelAndView("../../pages/login");
		}
		logger.debug("requestURL= " + request.getRequestURI());
		Enumeration<String> paramNamesEnum = request.getParameterNames();
		String paramName = null;
		String paramValue = null;
		int i = 1;
		while(paramNamesEnum.hasMoreElements()) {
			paramName = paramNamesEnum.nextElement();
			paramValue = request.getParameter(paramName);
			logger.debug(i + "paramName=" + paramName + " paramValue=" + paramValue);
			i++;
		}
		userSession.setCurrentUser(sysManager);
		request.getSession().setAttribute("userSession", userSession);
		userSession.refreshRoleFunctions(commonlyService);
		if(sysManager == null) {
			request.setAttribute("error", "登录失败！");
			return new ModelAndView("../../pages/login");
		}else {
			response.sendRedirect("console/index.htm");
			return null;
		}
	}

	public ModelAndView userLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setCharacterEncoding("utf-8");
		return null;
	}
}
