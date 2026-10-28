<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
  <head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <title><fmt:message key="file.enclosure.details"/></title>
	<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<meta name=renderer content=webkit> <!-- 默认采用极速模式 -->
  	<link rel="stylesheet" href="<%=basePath %>css/platform/common.css" type="text/css"></link>
  	<style type="text/css">
  		body{background: #FFF;min-width: 200px;}
  		.subject{width: 100%; height: 60px; line-height: 60px; font-size: 24px; text-align: center; border-bottom: 1px dashed #CCC;}
  		.fileinfo{width: 90%; margin: 15px 5%;}
  		.fileinfo table{width: 100%;}
  		.fileinfo table td{padding: 10px 5px; font-size: 15px;}
  		.fileinfo table .txt{width: 100px; text-align: right;}
  		.fileinfo a{display: block; width: 120px; text-align:center; float: left; margin-right: 10px; margin-bottom: 6px; height: 40px; line-height: 40px; background: #E8E8E8;}
  		.fileinfo a:hover{background: #C0C0C0;}
  	</style>
  	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
  	<script type="text/javascript">
  		$(function(){
	  		var resTitle = $('#rsdaTitle', window.parent.document).html();
  			$("#resname").html(resTitle);
  		})
  		
  	</script>
  	
  </head>
  
  <body>
    <div class="subject"><fmt:message key="file.resourec.enclosure.details"/></div>
    <div class="fileinfo">
    	<table cellpadding="0" cellspacing="0" border="0">
    		<tr>
    			<td class="txt" valign="top"><fmt:message key="file.resource.name"/><c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
    			<td id="resname" valign="top"></td>
    		</tr>
    		<tr>
    			<td class="txt" valign="top"><fmt:message key="file.resource.name"/><c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
    			<td valign="top">${resFile.resFileName }</td>
    		</tr>
    		<tr>
    			<td class="txt" valign="top"><fmt:message key="file.file.description"/> <c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
    			<td valign="top">${resFile.resFileRemark }</td>
    		</tr>
    		<tr>
    			<td class="txt">&nbsp;</td>
    			<td style="padding-top: 10px;">
    				<c:choose>
	    				<c:when test="${!empty resFile.resFilePath }">
	    					<c:if test="${srcExist}">
	    						<a href="<%=SysConfigInfo.FILE_URL_PATH %>${resFile.resFilePath }"><fmt:message key="file.source.text.download"/></a>
	    					</c:if>
	    					<c:if test="${!srcExist}">
	    						<span style="color: red; line-height: 40px;"><fmt:message key="file.resource.be.deleted"/></span>
	    					</c:if>
	    				</c:when>
    					<c:otherwise>
    						<fmt:message key="file.no.source.text"/>
    					</c:otherwise>
    				</c:choose>
    				
    				<c:if test="${!empty resFile.resViewPath}">
    					<a href="<%=basePath %>res/attview/${resFile.rsfiId }" target="_blank"><fmt:message key="file.online.preview"/></a>
    				</c:if>
    			</td>
    		</tr>
    	</table>
    </div>
  </body>
</html>
