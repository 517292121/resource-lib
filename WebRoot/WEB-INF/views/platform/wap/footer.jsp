<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@page import="cn.com.adtech.riss2.core.MmSettingConstant"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="java.util.Locale"%>
<%@page import="org.springframework.web.servlet.i18n.SessionLocaleResolver"%>
<!--底部开始-->
<% 
	Locale locale = (Locale)request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
	if(locale == null){
		locale = request.getLocale();
	}
	String key = MmSettingConstant.getWebSiteFooter4zh();
	if(locale != null){
		String currentLanguage = locale.getLanguage();
		if("zh".equals(currentLanguage)){ // 简体中文
			key = MmSettingConstant.getWebSiteFooter4zh();
		}else if("en".equals(currentLanguage)){ // 英文
			key = MmSettingConstant.getWebSiteFooter4en();
		}else if("zw".equals(currentLanguage)){// 藏文
			key = MmSettingConstant.getWebSiteFooter4zw();
		}
	}
%>

<div class="banquan">
	
	<%=ConstantsObj.getSysProperty(key)%><br />
	<a href="<%=basePath %>index"><fmt:message key='footer.computer.edition'/></a>
</div>
  </body>
</html>
