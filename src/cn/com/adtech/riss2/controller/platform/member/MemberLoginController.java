package cn.com.adtech.riss2.controller.platform.member;

import java.io.PrintWriter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.controller.platform.local.LocalecUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.count.SysAccessCounter;

/**
 * 会员登录
 * 
 * @author MaChao
 * @date 2010-9-19
 */
public class MemberLoginController extends AbstractController {
	
	
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		String result = "";
		//获取国际化
		LocalecUtil localecUtil=new LocalecUtil();
		String loginName = ServletRequestUtils.getStringParameter(request, "name", "");
		String password = ServletRequestUtils.getStringParameter(request, "pwd", "");
		
//		int usertype = ServletRequestUtils.getIntParameter(request, "utype", 1);//用户登录类型
		SysUserinfo sysUserinfo = null;
		if(ServiceUtil.isNotNull(loginName) && ServiceUtil.isNotNull(password)) {
			sysUserinfo = sysUserPlatformService.sysuserLogin(loginName, password, request.getRemoteHost());
		}
		if(sysUserinfo != null) {
			if(sysUserinfo.getSuinIsStop()) {// 该用户已被停用！
				result = "{isSuccess:false,msg:\""+localecUtil.get("memb.sorry.stop")+"\"}";
			}else {
				if(sysUserinfo.getSuinState().equals(SysUserinfo.STATE_PAST)) {
					request.getSession().setAttribute("sysUserinfo", sysUserinfo);
					result = "{isSuccess:true,msg:\""+localecUtil.get("memb.login.success")+"\"}";
					//——————日志记录——————//
					sysUserPlatformService.saveAccessLog(SysAccessCounter.TYPE_LOGIN, request.getRemoteAddr(), sysUserinfo.getSuinId(),null);
					//——————日志记录——————//
				}else if(sysUserinfo.getSuinState().equals(SysUserinfo.STATE_NOT_AUDIT)) {// 该用户还未审核,请耐心等待！
					result = "{isSuccess:false,msg:\""+localecUtil.get("memb.unaudited.wait")+"\"}";
				}else if(sysUserinfo.getSuinState().equals(SysUserinfo.STATE_NOT_PAST)) {// 该用户审核未通过！
					result = "{isSuccess:false,msg:\""+localecUtil.get("memb.not.through")+"\"}";
				}
			}
		}else {// 用户名或密码错误！
			result = "{isSuccess:false,msg:\""+localecUtil.get("memb.login.wrong")+"\"}";
		}
		out.print(result);
		out.flush();
		out.close();
		return null;
	}

	private ISysUserPlatformService sysUserPlatformService;

	public ISysUserPlatformService getSysUserPlatformService() {
		return sysUserPlatformService;
	}

	public void setSysUserPlatformService(ISysUserPlatformService sysUserPlatformService) {
		this.sysUserPlatformService = sysUserPlatformService;
	}
}
