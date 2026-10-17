<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
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
<html lang="en">
  <head>
    <title><fmt:message key="pc.riss.user.login"/></title>
	<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<meta name=renderer content=webkit> <!-- 默认采用极速模式 -->
  	<link rel="stylesheet" href="<%=basePath %>css/platform/common.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=basePath %>css/platform/regLogin.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=basePath %>css/platform/skin-<%=ConstantsObj.getSkinCode() %>.css" type="text/css"></link>
  	<style type="text/css">
  		@media screen and (max-width:1200px){
			body{min-width:100%;}
		}
  	</style>
  	<script type="text/javascript">var basePath = "<%=basePath %>";</script>
  	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
  	<script src="<%=path %>/viewsjs/common/sha1.js" type="text/javascript"></script> 
  	<!-- 语言切换 -->
	<script src="<%=basePath
		
	%>js/switchLang.js"></script>

  	<script src="<%=path %>/js/platform/login.js" type="text/javascript"></script> 
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
  			$("#loginform").keydown(function(e){
				var e = e || event,
				keycode = e.which || e.keyCode;
				if (keycode==13) {
					$("#anniu").trigger("click");
				}
			});
  		})
  		function reg(){
  			window.parent.openReg();
  		}
  	</script>
  </head>
  
  <body>
    <div class="login_subject"><fmt:message key="pc.riss.user.login"/></div>
    <div class="login_panelbox">
    <form name="loginform" id="loginform">
    	<p><fmt:message key="pc.login.name"/>：</p>
    	<ul class="k-panel">
    		<li id="usericon"></li>
    		<li><input type="text" id="loginname" class="k" maxlength="32"></li>
    	</ul>
    	<p><fmt:message key="pc.login.passwd"/>：</p>
    	<ul class="k-panel">
    		<li id="pwdicon"></li>
    		<li><input type="password" id="loginpwd" class="k" maxlength="32"></li>
    	</ul>
    	<ul>
    		<li>
    			<input type="button" value="<fmt:message key='pc.login'/>" id="anniu" onclick="onLogin();">
    			<span id="zc">
    					<fmt:message key='pc.no.account.number'/>
    				<a href="javascript:reg();">
    					<fmt:message key="pc.immediaate.registration"/>
    				<font style="font-family: 宋体;">>> 
    					</font></a></span>	
    		</li>
    	</ul>
    	<ul id="msg"></ul>
    </form>	
    </div>
  </body>
</html>
