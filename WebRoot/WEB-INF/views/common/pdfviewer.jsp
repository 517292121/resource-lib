<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=640, initial-scale=0.5, user-scalable=no" />
    <title><c:if test="${!empty title }">${title }-</c:if>在线预览</title>
    <style type="text/css">
		hmtl,body{margin: 0px; padding: 0px;}
        .pop,iframe{width: 100%; height: 100%; position: absolute;}
    </style>
</head>

<body>
  	 <iframe src="<%=basePath %>plugin/pdfjs/web/viewer.html?file=<%=basePath %>file/pdf/access/${fileUrl}&key=${key}&page=${page}&bright=${bright}&v=<%=Math.random()%>" frameborder="0" id="pdfContainer" name="pdfContainer"></iframe>
</body>
</html>