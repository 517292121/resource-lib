<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>敦煌研究数据库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
  </head>
  	<script type="text/javascript">
		var limitLength = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>;
		var pagingDisplayMsg = '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>';
		function getPath() {
			return '<%=path%>/';
		}
		var rminId = ${rminId};
		var userId = ${userId};
		var rminStatu = ${rminStatu};
		var loginUser = <%=MyRequestUtil.getLoginUserId(request) %>
	</script>
	<script type="text/javascript" language="javascript" src="<c:url value='/viewsjs/resource/checkout/resSourceDataBatchCheck.js' />"></script>
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
  <body>
  	<!-- 资源保存成功的图片提示 -->
    <div id="SuccessMessageBox" class="SuccessMessageBox"></div>
    ${result}
  </body>
</html>
