package cn.com.adtech.riss2.controller;


import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysUserinfo;

/**
 * httpRequest基本操作类型
 * 
 * @author MaChao
 */
public class MyRequestUtil {
	/**
	 * 获取Integer型参数。如果没有则返回0
	 */
	final public static int getIntParameter(ServletRequest request, String paramname) {
		int result = 0;
		if(request.getParameter(paramname) != null && request.getParameter(paramname).length() > 0) {
			try {
				result = Integer.parseInt(request.getParameter(paramname));
			} catch (NumberFormatException e) {
				return 0;
			}
		}
		return result;
	}

	/**
	 * 获取Long型参数。如果没有则返回0
	 */
	final public static long getLongParameter(ServletRequest request, String paramname) {
		long result = 0;
		if(request.getParameter(paramname) != null && request.getParameter(paramname).length() > 0) {
			try {
				result = Long.parseLong(request.getParameter(paramname));
			} catch (Exception e) {
				return 0;
			}
		}
		return result;
	}

	/**
	 * 获取整型参数。如果没有则返回0
	 */
	final public static String getStringParameter(ServletRequest request, String paramname) {
		String result = "";
		if(request.getParameter(paramname) != null && request.getParameter(paramname).length() > 0) {
			result = request.getParameter(paramname);
		}
		return result;
	}

	final public static int getIntAttribute(ServletRequest request, String attName) {
		try {
			int result = Integer.parseInt(request.getAttribute(attName).toString());
			return result;
		}catch(Exception e) {
			return 0;
		}
	}

	final public static long getLongAttribute(ServletRequest request, String attName) {
		try {
			long result = Long.parseLong(request.getAttribute(attName).toString());
			return result;
		}catch(Exception e) {
			return 0;
		}
	}

	final public static String getStringAttribute(ServletRequest request, String attName) {
		try {
			String result = request.getAttribute(attName).toString();
			return result;
		}catch(Exception e) {
			return "";
		}
	}

	/**
	 * 获取当前登录用户ID
	 */
	final public static long getLoginUserId(HttpServletRequest request) {
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) { // 如果用户为空
			return 0;
		}
		return sysManager.getSmanId();
	}

	/**
	 * 获取当前登录用户姓名
	 */
	final public static String getLoginUserName(HttpServletRequest request) {
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) { // 如果用户为空
			return "";
		}
		return sysManager.getSmanLoginName();
	}
	
	/**
	 * 获取当前登录用户姓名
	 */
	final public static String getSmanName(HttpServletRequest request) {
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) { // 如果用户为空
			return "";
		}
		return sysManager.getSmanName();
	}
	
	/**
	 * 获取当前登录的会员ID，如果未登录则返回0
	 * @param request
	 * @return
	 */
	final public static long getMemberId(HttpServletRequest request){
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			return 0;
		} else {
			return user.getSuinId();
		}
	}
	
}
