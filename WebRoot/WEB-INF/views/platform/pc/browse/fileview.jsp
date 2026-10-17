<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta charset="UTF-8">
    <title>${resource.rsdaTitle }-在线预览</title>
    <style type="text/css">
        *{padding: 0;margin: 0;}
        iframe{position: fixed;width: 100%;height: 100%;left: 0;top: 0;border: 0;}
    </style>
  </head>
  
  <body>
    <%@ include file="../resBrowse.jsp" %>
  </body>
</html>
