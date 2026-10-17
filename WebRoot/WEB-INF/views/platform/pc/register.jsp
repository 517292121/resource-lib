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

<html>
<head>
	<title>用户注册</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<meta name=renderer content=webkit> <!-- 默认采用极速模式 -->
  	<link rel="stylesheet" href="<%=basePath %>css/platform/common.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=basePath %>css/platform/regLogin.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=basePath %>css/platform/skin-<%=ConstantsObj.getSkinCode() %>.css" type="text/css"></link>
  	<script type="text/javascript">var basePath = "<%=basePath %>"; var skin = "<%=ConstantsObj.getSkinCode() %>";</script>
  	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
  	<script src="<%=path %>/viewsjs/common/sha1.js" type="text/javascript"></script> 
	<script type="text/javascript" src="<%=basePath %>js/platform/register.js"></script>
	
	<!-- JS语言包 -->
  	<script src="<%=basePath %>js/platform/lankey/<%=language %>.js"></script>

	<style>
		.container{width:400px;	margin:10px auto;}
		.container .nav{width:310px;border-bottom:1px solid #ccc; margin: 0 auto;}
		.container .nav span{display: block; width:120px; border:0px solid #ccc; text-align:center; line-height:40px; color:#666; float:left; cursor:pointer;}
		.container .nav span:first-child{border:0px}
		.content{ margin-top:20px;position:relative; overflow:hidden;height:400px;} /*定位一般设置为relative，overflow设置为hidden来隐藏外面的内容*/
		.content .box{position:absolute; height:500px;} /*这个定位设置为absolute, 因为要操作这个的位置来切换内容*/
		.content ul{float:left; width:400px;}
  		@media screen and (max-width:1200px){
			body{min-width:100%;}
		}
	</style>

</head>
<body>
<div class="login_subject zc"><fmt:message key="pc.registration.platform"/></div>

<div class="container">
	<div class="nav">
        <span class="active"><fmt:message key="pc.mailbox.registration"/></span>
        <span><fmt:message key="pc.phone.registration"/></span>
        <div class="clear"></div>
    </div>
    <div class="content">
        <div class="box">
        	<!-- 邮箱注册 -->
            <ul>
                <div class="login_panelbox" style="width: 310px; margin: 0px auto;">
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
					    <td class="td2" id="reg_mobileTelephone_td">
					    	<input class="zc_input yzm" type="text" id="e_imgCode" name="e_imgCode" value="${imgCode }" placeholder="<fmt:message key='pc.picture.code'/>" maxlength="4">
					    	<div class="codebox"><img src="<%=basePath %>yzm.jsp" id="imgcode1" width="100" height="38" align="absmiddle" title="<fmt:message key='pc.refresh.code'/>" onclick="refcode();"></div>
					    </td>
					</tr>
					<tr style="height: 22px">
						<td>

							<input type="checkbox" name="regServer" id="regServer" checked="checked"><label for="regServer"><fmt:message key='pc.read.and.agree'/>
							<a href="javascript:openserver();">“<fmt:message key='pc.service.law.statement'/>”</a></label>
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
            
            <!-- 手机注册 -->
            <ul>
               <div class="login_panelbox" style="width: 310px; margin: 0px auto;">
				<table cellpadding="0" cellspacing="0">
					<tr>
						<td>
							<input class="zc_input" type="text" name="mobileTel" id="mobileTel" maxlength="11" value="${loginName }" placeholder="<fmt:message key='pc.correct.phone'/>">
						</td>
					</tr>
					<tr>
						<td>
							<input class="zc_input" type="password" id="loginPwd" name="loginPwd" value="${password }" placeholder="<fmt:message key='pc.login.password.characters'/>"></input>
						</td>
					</tr>
					<tr>
					    <td class="td2" id="reg_mobileTelephone_td">
					    	<input class="zc_input yzm" type="text" id="imgCode" name="imgCode" value="${imgCode }" placeholder="<fmt:message key='pc.picture.code'/>" maxlength="4">
					    	<div class="codebox"><img src="<%=basePath %>yzm.jsp" id="imgcode2" width="100" height="38" align="absmiddle" title="<fmt:message key='pc.refresh.code'/>" onclick="refcode();"></div>
					    </td>
					</tr>
					<tr>
					    <td class="td2" id="reg_mobileTelephone_td">
					    	<input class="zc_input yzm" type="text" id="smsCode" name="smsCode" value="${smsCode }" placeholder="<fmt:message key='pc.sms.code'/>" maxlength="6">
					    	<input type="button" class="codebox smsbtn" id="smsbtn" onclick="getMobileCode();" value="<fmt:message key='pc.obtain.sms.code'/>">
					    </td>
					</tr>
					<tr style="height: 22px">
						<td>
							<input type="checkbox" name="e_regServer" id="e_regServer" checked="checked"><label for="regServer"><fmt:message key='pc.read.and.agree'/>
							<a href="javascript:openserver();">“<fmt:message key='pc.service.law.statement'/>”</a></label>
						</td>
					</tr>
					<tr>
						<td style="padding-top: 20px;">
							<button class="reg_btn bcm" onclick="mobileRegSubmit();"> <fmt:message key='pc.free.registration'/> </button>
						</td>
					</tr>
					<tr>
						<td>
							<fmt:message key='pc.i.have.registered'/><a href="javascript:inLogin();" style="color: #2f82ff;"><fmt:message key='pc.login'/></a>
						</td>
					</tr>
				</table>
		    </div>
            </ul>
        </div>
    </div>  	
</div>
</body>
</html>
