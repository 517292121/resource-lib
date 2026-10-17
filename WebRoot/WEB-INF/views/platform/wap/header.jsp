<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@page import="cn.com.adtech.riss2.cache.PlatformCacheFactory"%>
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
    <title><c:if test="${!empty title }">${title }—</c:if><fmt:message key='footer.wap.title'/></title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
	<meta http-equiv="content-type" content="text/html;charset=utf-8">
	<meta content="always" name="referrer">
	<meta name="theme-color" content="#2932e1">
	<link rel="stylesheet" href="<%=path %>/css/wap/common.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=basePath %>css/wap/skin-<%=ConstantsObj.getSkinCode() %>.css" type="text/css"></link>
	<%if(language.equals("zw")){ %>
  	<style type="text/css">*{font-family: fm; font-size: 16px;}</style>
  	<%} %>
  	<!-- JS语言包 -->
  	<script src="<%=basePath %>js/platform/lankey/<%=language %>.js"></script>
	<script type="text/javascript">var basePath = "<%=basePath %>"; var fileUrl = "<%=SysConfigInfo.FILE_URL_PATH %>";</script>
	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/wap/common.js"></script>
  </head>
  <body>
  	<div class="w-top">
		<div class="user">
			<c:choose>
   				<c:when test="${!empty sessionScope.sysUserinfo}">
   						<a href="javascript:void(0);" onclick="userWin();"><img src="<%=basePath %>images/platform/face.jpg"/></a>
   					
   					<div class="w_userinfo">
   						<ul id="xx">
   							<li id="lname">${sysUserinfo.suinLoginName }</li>
   							<li id="usertype">${sysUserinfo.sysUserType.sutyName }</li>
   						</ul>
   						<ul id="menu">
   							<li id="yhzx">
   								<a href="javascript:void(0);" onclick="inUsercenter();"><fmt:message key='user.conter'/></a>
   							</li>
   							<li id="tc">
   								<a href="javascript:void(0);" onclick="exitLogin();"><fmt:message key='pc.exit'/></a>
   							</li>	
   						</ul>
   					</div>
   				</c:when>
   				<c:otherwise>
					<a href="javascript:void(0);" onclick="inLogin();"><img src="<%=basePath %>images/platform/wap/icon-user.png"/></a>
   				</c:otherwise>
   			</c:choose>
		</div>
		<div class="search">
			<div class="logo-img" onclick="window.location.href='<%=basePath %>mobile'"></div>
			<div class="search-group">
				<input type="text" name="searchkey" id="searchkey" value="${key }" class="key" />
				<button class="search-icon" onclick="simSearch();"><img src="<%=basePath %>images/platform/wap/icon-search.png"/></button>
			</div>
			<div class='matchbox'>
				<input type="checkbox" value="1" name="match" id="match" <c:if test="${match==1 }">checked="checked"</c:if>><label for="match">精确匹配</label>
			</div>
		</div>
		<div class="long-nav">
			<div class="nav-content">
				<ul class="nav">
					<c:forEach items="<%=PlatformCacheFactory.instance().getResCountList() %>" var="v" varStatus="s">
						<li>
						<a href="<%=basePath %>wap/res/type/${v.id }">
							<c:if test="${rltyId != v.id}">
							<img src="<%=basePath %>images/platform/wap/restype/${v.id }.png" onerror="this.src='<%=basePath %>images/platform/wap/restype/def.png'"/>
							<span>${v.groupName }</span>
							</c:if>
							<c:if test="${rltyId == v.id}">
							<img src="<%=basePath %>images/platform/wap/<%=ConstantsObj.getSkinCode() %>/${v.id }.png" onerror="this.src='<%=basePath %>images/platform/wap/<%=ConstantsObj.getSkinCode() %>/def.png'"/>
							<span class="active">${v.groupName }</span>
							</c:if>
						</a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
