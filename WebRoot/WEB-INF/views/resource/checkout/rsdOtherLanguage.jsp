<%@ page language="java" pageEncoding="UTF-8"%>
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
	<style type="text/css">
		.my_status{font-size:12px;font-weight: bold;padding-left: 3px;}
	</style>
  </head>
  	<script type="text/javascript">
		var limitLength = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>;
		var pagingDisplayMsg = '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>';
		function getPath() {
			return '<%=path%>/';
		}
		var rsdaId = '${rsdaId}';
		var rminId = '${rminId}';
		var userId = '${param.userid}';
	</script>
	<script type="text/javascript" language="javascript" src="<c:url value='/viewsjs/resource/checkout/rsdOtherLanguage.js' />"></script>
  <body>
  </body>
</html>
