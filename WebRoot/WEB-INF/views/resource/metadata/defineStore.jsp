<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.TempletConfig"%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>敦煌研究数据库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link type="text/css" href="<c:url value='/css/defineStore.css'/>" rel="stylesheet"/>
  </head>
  	<script type="text/javascript">
		var limitLength = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>;
		var pagingDisplayMsg = '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>';
		function getPath() {
			return '<%=path%>/';
		}

		var rdtyOne = '<%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE) %>';
		var rdtyTwo = '<%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO) %>';
		var rdtyThree = '<%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE) %>';
	</script>
	<script type="text/javascript" language="javascript" src="<c:url value='/viewsjs/resource/metadata/defineStore.js' />"></script>
	<script type="text/javascript" src="<c:url value='/ext/ux/RowEditor.js'/>"></script>
  <body>
  </body>
</html>
