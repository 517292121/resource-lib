<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<script type="text/javascript" language="javascript" src="<c:url value='/viewsjs/resource/setting/sysSetting.js' />"></script>
	<script type="text/javascript">
		var basePath = '<%=basePath%>';
		var _RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE='${resource_upload_photofile_maxsize }'; //资源封面大小
		var _RESOURCE_UPLOAD_VIEWFILE_MAXSIZE = '${resource_upload_viewfile_maxsize }'; //资源预览文件大小
		var _RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE = '${resource_upload_sourcefile_maxsize}'; //资源源文件大小
		var _SOURCE_LOCATION_OPTIONS = '${source_location_options }'; //馆藏位置
	</script>
  </head>
  <body>
    
  </body>
</html>
