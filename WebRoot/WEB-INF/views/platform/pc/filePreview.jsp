<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <title>${title }文件预览—敦煌研究数据库</title>

	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta name=renderer content=webkit> <!-- 默认采用极速模式 -->
  	<link rel="stylesheet" href="<%=basePath %>css/platform/common.css" type="text/css"></link>
  	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
  	<style type="text/css">
  		html,body,.resview{width: 100%; height: 100%; text-align: center;}
  		.multimediaview{width: 850px; margin: 50px auto;}
  		.video{height: 560px;}
  		.nobrowse{margin: 50px auto; color: #808080; font-size: 16px; text-align: center;}
  		.title{margin: 20px auto; text-align: center; font-size: 18px;}
  	</style>
  </head>

  <body>
  	<c:choose>
  		<c:when test="${resType eq 'video' || resType eq 'audio'}">
  			<div class="multimediaview">
  				<p class="title">${title }</p>
  				<%@ include file="resBrowse.jsp" %>
  			</div>
  		</c:when>
  		<c:otherwise>
  			<%@ include file="resBrowse.jsp" %>
  		</c:otherwise>
  	</c:choose>

  </body>
</html>
