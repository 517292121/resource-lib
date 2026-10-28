<%@ page language="java" pageEncoding="UTF-8"%>
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
<html lang="<%= "zw".equals(language) ? "bo" : ("en".equals(language) ? "en" : "zh-CN") %>">
  <head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <title><fmt:message key="pc.login"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
	<link rel="shortcut icon" href="<%=basePath%>favicon.ico" type="image/x-icon" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<meta name=renderer content=webkit> <!-- 默认采用极速模式 -->
    <link rel="stylesheet" href="<%=basePath%>css/platform/member-login.css?v=frontend-login-20261025-1">
    <script>if (window.parent !== window) document.documentElement.classList.add('is-embedded');</script>
  	<script type="text/javascript">var basePath = "<%=basePath %>";</script>
  	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
  	<script src="<%=path %>/viewsjs/common/sha1.js" type="text/javascript"></script> 
  	<!-- 语言切换 -->
	<script src="<%=basePath
		
	%>js/switchLang.js"></script>

  	<script type="text/javascript">

  		function onLogin(){
  			$("#msg").html("");
			var loginName = $("#loginname").val();
			if(!loginName || loginName.trim().length < 1) {
				layer.msg("<fmt:message key='pc.please.enter.login.name'/>");
				return false;
			}
			var password = $("#loginpwd").val();
			if(!password || password.trim().length < 1) {
				layer.msg("<fmt:message key='pc.please.enter.login.passwd'/>");
				return false;
			}
			//密码加密
			password = hex_sha1(password);
			
			$.ajax({
			    type: 'GET',
			    url: '<%=basePath %>member/memberLogin.htm?t=' + new Date().getTime(),
			    data : {
			    	name : loginName,
			    	pwd : password
			    },
			    cache:false,
			    success: function(data){
				   	try{
				    	var val = eval("("+data+")");
			    		if(val.isSuccess == true || val.isSuccess == "true"){
							//登录成功，现在的操作是刷新当前父页面，如有需求也可以直接跳转到用户中心
			  				window.parent.location.reload();
			    		}else{
			    			layer.msg(val.msg);
			    		}
				   	}catch(e){
				   		alert(e.message);
				   		layer.msg("<fmt:message key='repeat.error'/>" + e.message, {icon:2});
				   	}
			    }
			});
  		}
  		
  		$(function(){
            $("#loginform").on("submit", function(e){
                e.preventDefault();
                onLogin();
            });
  		})
  		function reg(){
            if (typeof window.parent.openReg === "function") window.parent.openReg();
            else window.location.assign(basePath + "register");
  		}
  	</script>
  </head>
  
  <body class="member-login-page">
    <main class="member-login-card" aria-labelledby="member-login-title">
        <header class="member-login-heading">
            <img class="member-login-brand" src="<%=basePath%>images/platform/psb/home/brand-logo.png" width="537" height="74" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库") %>">
            <h1 id="member-login-title"><fmt:message key="pc.login"/></h1>
        </header>
        <form name="loginform" id="loginform" class="member-login-form" method="post">
            <div class="member-login-field">
                <label for="loginname"><fmt:message key="pc.login.name"/></label>
                <div class="member-login-input">
                    <svg viewBox="0 0 24 24" aria-hidden="true" focusable="false"><circle cx="12" cy="8" r="4"/><path d="M4 21v-3a8 8 0 0 1 16 0v3"/></svg>
                    <input type="text" id="loginname" name="username" maxlength="32" autocomplete="username" autocapitalize="none" spellcheck="false">
                </div>
            </div>
            <div class="member-login-field">
                <label for="loginpwd"><fmt:message key="pc.login.passwd"/></label>
                <div class="member-login-input">
                    <svg viewBox="0 0 24 24" aria-hidden="true" focusable="false"><rect x="5" y="10" width="14" height="11" rx="1"/><path d="M8 10V6a4 4 0 0 1 8 0v4M12 14v3"/></svg>
                    <input type="password" id="loginpwd" name="password" maxlength="32" autocomplete="current-password">
                </div>
            </div>
            <button type="submit" id="anniu" class="member-login-submit"><fmt:message key="pc.login"/></button>
            <p class="member-login-register"><fmt:message key="pc.no.account.number"/> <a href="<%=basePath%>register" onclick="reg(); return false;"><fmt:message key="pc.immediaate.registration"/><span aria-hidden="true"> →</span></a></p>
            <div id="msg" role="status" aria-live="polite"></div>
        </form>
    </main>
  </body>
</html>
