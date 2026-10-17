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
	<%if(language.equals("zw")){ %>
  		<style type="text/css">*{font-family: fm; font-size: 16px;}</style>
  	<%} %>
  	<!-- JS语言包 -->
  	<script src="<%=basePath %>js/platform/lankey/<%=language %>.js"></script>
	<script type="text/javascript">var basePath = "<%=basePath %>"; var skin = "<%=ConstantsObj.getSkinCode() %>";</script>
  	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
  	<script src="<%=path %>/viewsjs/common/sha1.js" type="text/javascript"></script> 
  	
	<script type="text/javascript" src="<%=basePath %>js/platform/register.js"></script>
	
	<style>
		.container{margin:10px; overflow: hidden;}
		.container .nav{border-bottom:1px solid #ccc; margin: 10 auto;}
		.container .nav span{display: block; width:120px; border:0px solid #ccc; text-align:center; line-height:40px; color:#666; float:left; cursor:pointer;}
		.container .nav span:first-child{border:0px}
		.content{ margin-top:20px; width:400px;position:relative; overflow:hidden;height:400px;} /*定位一般设置为relative，overflow设置为hidden来隐藏外面的内容*/
		.content .box{position:absolute;width:400px;height:500px;} /*这个定位设置为absolute, 因为要操作这个的位置来切换内容*/
		.content ul{float:left;}
	</style>

</head>
<body>
<div class="w_header bcm">
	<h3><fmt:message key='register.title'/></h3>
</div>

<div class="container">
	<div class="nav">
        <span class="active"><fmt:message key='pc.mailbox.registration'/></span>
        <span><fmt:message key='pc.phone.registration'/></span>
        <div class="clear"></div>
    </div>
    <div class="content">
        <div class="box">
        	<!-- 邮箱注册 -->
            <ul>
                <div class="login_panelbox" style="margin: 0px auto;">
				<table cellpadding="0" cellspacing="0" style="width: 100%">
					<tr>
						<td>
							<input class="zc_input" type="text" name="e_loginName" id="e_loginName" maxlength="64" placeholder="<fmt:message key='pc.mailbox.as.a.name'/>">
						</td>
					</tr>
					<tr>
						<td>
							<input class="zc_input" type="password" id="e_loginPwd" name="e_loginPwd" placeholder="<fmt:message key='register.passwd.number'/>"></input>
						</td>
					</tr>
					<tr>
					    <td class="td2" id="reg_mobileTelephone_td">
					    	<input class="zc_input" type="text" id="e_email" name="e_email" placeholder="<fmt:message key='pc.correct.mailbox'/>" maxlength="64">
					    </td>
					</tr>
					<tr>
					    <td class="td2 yzmtd" id="reg_mobileTelephone_td">
					    	<input class="zc_input yzm" type="text" id="e_imgCode" name="e_imgCode" value="${imgCode }" placeholder="<fmt:message key='pc.picture.code'/>" maxlength="4">
					    	<div class="codebox"><img src="<%=basePath %>yzm.jsp" id="imgcode1" width="100" height="46" align="absmiddle" title="<fmt:message key='pc.refresh.code'/>" onclick="refcode();"></div>
					    </td>
					</tr>
					<tr style="height: 22px">
						<td>
							<input type="checkbox" name="e_regServer" id="e_regServer" checked="checked"><label for="regServer"><fmt:message key='pc.read.and.agree'/>
							<a href="<%=basePath %>reg/server" style="color: #2f82ff;">“<fmt:message key='pc.service.law.statement'/>”</a></label>
						</td>
					</tr>
					<tr>
						<td style="padding-top: 20px;">
							<button class="reg_btn bcm" onclick="emailReg();"> <fmt:message key='pc.free.registration'/> </button>
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key='pc.i.have.registered'/> <a href="<%=basePath %>wap/login" style="color: #2f82ff;"><fmt:message key='pc.login'/></a>
						</td>
					</tr>
				</table>
		    	</div>
            </ul>
            
            <!-- 手机注册 -->
            <ul>
               <div class="login_panelbox" style="margin: 0px auto;">
				<table cellpadding="0" cellspacing="0" style="width: 100%">
					<tr>
						<td>
							<input class="zc_input" type="text" name="mobileTel" id="mobileTel" maxlength="11" value="${loginName }" placeholder="<fmt:message key='pc.correct.phone'/>">
						</td>
					</tr>
					<tr>
						<td>
							<input class="zc_input" type="password" id="loginPwd" name="loginPwd" value="${password }" placeholder="<fmt:message key='register.passwd.number'/>"></input>
						</td>
					</tr>
					<tr>
					    <td class="td2 yzmtd" id="reg_mobileTelephone_td">
					    	<input class="zc_input yzm" type="text" id="imgCode" name="imgCode" value="${imgCode }" placeholder="<fmt:message key='pc.picture.code'/>" maxlength="4">
					    	<div class="codebox"><img src="<%=basePath %>yzm.jsp" id="imgcode2" width="100" height="46" align="absmiddle" title="<fmt:message key='pc.refresh.code'/>" onclick="refcode();"></div>
					    </td>
					</tr>
					<tr>
					    <td class="td2 yzmtd" id="reg_mobileTelephone_td">
					    	<input class="zc_input yzm" type="text" id="smsCode" name="smsCode" value="${smsCode }" placeholder="<fmt:message key='pc.sms.code'/>" maxlength="6">
					    	<input type="button" class="codebox smsbtn" id="smsbtn" onclick="getMobileCode();" value="<fmt:message key='pc.obtain.sms.code'/>">
					    </td>
					</tr>
					<tr style="height: 22px">
						<td>
							<input type="checkbox" name="regServer" id="regServer" checked="checked"><label for="regServer"><fmt:message key='pc.read.and.agree'/>
							<a href="<%=basePath %>reg/server" style="color: #2f82ff;">“<fmt:message key='pc.service.law.statement'/>”</a></label>
						</td>
					</tr>
					<tr>
						<td style="padding-top: 20px;">
							<button class="reg_btn bcm" onclick="mobileRegSubmit();"> <fmt:message key='pc.free.registration'/> </button>
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key='pc.i.have.registered'/> <a href="<%=basePath %>wap/login" style="color: #2f82ff;"><fmt:message key='pc.login'/></a>
						</td>
					</tr>
				</table>
		    </div>
            </ul>
         

        </div>
    </div>  	
</div>

<%@ include file="footer.jsp" %>
