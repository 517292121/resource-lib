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
  </head>
  	<script type="text/javascript">
		var limitLength = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>;
		var start = ${start};
		var pagingDisplayMsg = '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>';
		var idsTitle_Condition ='${idsTitle_Condition}';
		var idstIds = '${groupId}';
		function getPath() {
			return '<%=path%>/';
		}
	</script>

	<script type="text/javascript" language="javascript" src="<c:url value='/tinymce/tiny_mce.js' />"></script>
	<script type="text/javascript" language="javascript" src="<c:url value='/tinymce/Ext.ux.TinyMCE.min.js' />"></script>
	<script type="text/javascript" language="javascript" src="<c:url value='/viewsjs/ids/idsInformation.js' />"></script>
  <body>
  </body>
</html>
