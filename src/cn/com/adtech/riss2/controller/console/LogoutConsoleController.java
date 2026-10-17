package cn.com.adtech.riss2.controller.console;

import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.com.adtech.riss2.core.CounselorOnline;
import cn.com.adtech.riss2.core.CurrentUserSession;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;

public class LogoutConsoleController extends AbstractController{

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		// 用户注销,清空session
		CurrentUserSession userSession = CurrentUserSession
				.findUserSession(request);
		
		//删除当前咨询员
		SysManager userinfo = userSession.getCurrentUser();
		if(userinfo != null){
			Map<Long, SysManager> map = CounselorOnline.getInstance().getOnlineCounselors();
			if(map != null){
				if((SysManager)map.get(userinfo.getSmanId()) != null){
					map.remove(userinfo.getSmanId());
				}
				CounselorOnline.getInstance().getInstance().setOnlineCounselors(map);
			}
		}
		
		if (userSession != null) {
			userSession.setCurrentUser(null);
		}
		request.getSession().setAttribute("userSession", null);
		
		String cookiePath = request.getContextPath()+"/mylib/";
		
		Cookie cookie = new Cookie("activeUserID","");
    	cookie.setMaxAge(0);
    	cookie.setPath(cookiePath);
    	response.addCookie(cookie);
    	
    	cookie = new Cookie("activeSessionID","");
    	cookie.setPath(cookiePath);
    	cookie.setMaxAge(0);
    	response.addCookie(cookie);
    	
		//删除Cookie
		ServiceUtil.deleteCookie("userName", "/", response);
		ServiceUtil.deleteCookie("userId", "/", response);    
		
		response.sendRedirect(request.getContextPath()+"/console/index.htm");
		
		return null;
	}

}
