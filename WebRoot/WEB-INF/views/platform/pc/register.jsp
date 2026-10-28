<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@page import="java.util.Locale"%>
<%@ page import="org.springframework.web.servlet.i18n.SessionLocaleResolver"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	//切换语种
	String language = "zh";
	Object lang = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
	Locale locale ;
	if(lang != null){
	 	locale= (Locale)lang;
	  	language = locale.getLanguage();
	}
%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
	<title><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "用户注册") %></title>
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<meta name=renderer content=webkit> <!-- 默认采用极速模式 -->
	<link rel="stylesheet" href="<%=basePath%>css/platform/member-login.css?v=member-terms-20261025-5">
  	<script type="text/javascript">var basePath = "<%=basePath %>"; var skin = "<%=ConstantsObj.getSkinCode() %>";</script>
  	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
  	<script src="<%=path %>/viewsjs/common/sha1.js" type="text/javascript"></script> 
	<script type="text/javascript" src="<%=basePath %>js/platform/register.js?v=member-terms-20261025-5&amp;ui=zh-zw-20261025-1"></script>
	
	<!-- JS语言包 -->
  	<script src="<%=basePath %>js/platform/lankey/<%=language %>.js"></script>

</head>
<body class="member-register-page">
<main class="member-login-card member-register-card" aria-labelledby="member-register-title">
	<header class="member-login-heading member-register-heading">
		<img class="member-login-brand" src="<%=basePath%>images/platform/psb/home/brand-logo.png" width="537" height="74" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库") %>">
		<h1 id="member-register-title"><fmt:message key="pc.registration.platform"/></h1>
	</header>

	<div class="container member-register-container">
	<div class="nav member-register-tabs">
        <span class="active"><fmt:message key="pc.mailbox.registration"/></span>
        <div class="clear"></div>
    </div>
    <div class="content member-register-content">
        <div class="box">
        	<!-- 邮箱注册 -->
            <ul class="is-active">
				<div class="login_panelbox member-register-panel">
				<table cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<input class="zc_input" type="text" name="e_loginName" id="e_loginName" maxlength="64" placeholder="<fmt:message key='pc.mailbox.as.a.name'/>">
						</td>
					</tr>
					<tr>
						<td>
							<input class="zc_input" type="password" id="e_loginPwd" name="e_loginPwd" placeholder="<fmt:message key='pc.login.password.characters'/>"></input>
						</td>
					</tr>
					<tr>
					    <td class="td2" id="reg_mobileTelephone_td">
					    	<input class="zc_input" type="text" id="e_email" name="e_email" placeholder="<fmt:message key='pc.correct.mailbox'/>" maxlength="64">
					    </td>
					</tr>
					<tr>
					    <td class="td2" id="reg_mobileTelephone_td"><div class="member-register-code-row">
					    	<input class="zc_input yzm" type="text" id="e_imgCode" name="e_imgCode" value="${imgCode }" placeholder="<fmt:message key='pc.picture.code'/>" maxlength="4">
					    	<div class="codebox"><img src="<%=basePath %>yzm.jsp" id="imgcode1" width="100" height="38" align="absmiddle" title="<fmt:message key='pc.refresh.code'/>" onclick="refcode();"></div>
					    </div></td>
					</tr>
					<tr style="height: 22px">
						<td>

							<input type="checkbox" name="regServer" id="regServer" checked="checked"><label for="regServer"><fmt:message key='pc.read.and.agree'/>
							<a href="<%=basePath%>reg/server" target="_blank" rel="noopener" onclick="return openserver();">“<fmt:message key='pc.service.law.statement'/>”</a></label>
						</td>
					</tr>
					<tr>
						<td style="padding-top: 20px;">
							<button class="reg_btn bcm" onclick="emailReg();"> <fmt:message key='pc.free.registration'/></button>
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key='pc.i.have.registered'/> <a href="javascript:inLogin();" style="color: #2f82ff;"><fmt:message key='pc.login'/></a>
						</td>
					</tr>
				</table>
		    	</div>
            </ul>
            
        </div>
    </div>  	
</div>
</main>
</body>
</html>
