<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>预览</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<style type="text/css">
		body{text-align: center;}
	</style>
  </head>
  
  <body>
	<img src="<%=SysConfigInfo.FILE_URL_PATH %>${url }" style="margin: 0px auto;" />
  </body>
</html>
