<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@page import="org.springframework.web.servlet.i18n.SessionLocaleResolver"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//切换语种
String language = "zh";
Object lang = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
Locale locale1 ;
if(lang != null){
	locale1= (Locale)lang;
	language = locale1.getLanguage();
}
%>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="Access-Control-Allow-Origin" content="*">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <title><fmt:message key='login.user.login'/></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta content="always" name="referrer">
	<meta name="theme-color" content="#2932e1">
	<link rel="stylesheet" href="<%=path %>/css/wap/common.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=basePath %>css/wap/skin-<%=ConstantsObj.getSkinCode() %>.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/wap/regLogin.css" type="text/css"></link>
	<style type="text/css">
		.banquan{position: fixed; bottom: 0px;}
	</style>
	<%if(language.equals("zw")){ %>
 	 	<style type="text/css">*{font-family: fm; font-size: 16px;}</style>
  	<%} %>
  	<!-- JS语言包 -->
  	<script src="<%=basePath %>js/platform/lankey/<%=language %>.js"></script>
	<script type="text/javascript">var basePath = "<%=basePath %>"; var srcfrom = "${from}"; </script>
	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
	<script src="<%=path %>/viewsjs/common/sha1.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath %>js/platform/login.js"></script>
  </head>
  <body>
  	<!--页头-->
	<header class="w_header bcm">
		<a href="${from }"></a>
		<h3>RISS2<fmt:message key='pc.login'/></h3>
	</header>	
	
	<div class="w_wrapper">
	<form name="loginform" id="loginform">
    	<ul class="input-wrapper">
    		<li id="usericon"></li>
    		<li class="kbox"><input type="text" id="loginname" class="k" maxlength="32"></li>
    	</ul>
    	<ul class="input-wrapper">
    		<li id="pwdicon"></li>
    		<li class="kbox"><input type="password" id="loginpwd" class="k" maxlength="32"></li>
    	</ul>
    	<ul>
    		<li>
    			<input type="button" value="<fmt:message key='pc.login'/>" id="anniu" onclick="onLogin('wap');">
    			<span id="zc"><fmt:message key='pc.no.account.number'/><a href="<%=basePath %>wap/register"><fmt:message key='pc.immediaate.registration'/><font style="font-family: 宋体;">>></font></a></span>	
    		</li>
    	</ul>
    	<ul id="msg"></ul>
	</form>
	</div>
<%@ include file="footer.jsp" %>
