<%@ page language="java" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>密码修改</title>
		<meta http-equiv="content-type" content="text/html; charset=UTF-8">
		<link type="text/css"
			href="<c:url value='/ext/resources/css/ext-all.css'/>"
			rel="stylesheet" />
		<link type="text/css" href="<c:url value='/css/button.css'/>"
			rel="stylesheet" />
		<script type="text/javascript"
			src="<c:url value='/ext/ext-base.js'/>"></script>
		<script type="text/javascript" src="<c:url value='/ext/ext-all.js'/>"></script>
		<script type="text/javascript"
			src="<c:url value='/ext/ext-lang-zh_CN.js'/>"></script>
		<script type="text/javascript"
			src="<c:url value='/viewsjs/common/sha1.js'/>"></script>
		<script type="text/javascript"
			src="<c:url value='/pages/js/changepwd.js'/>"></script>
		<script type="text/javascript"
			src="<c:url value='/ext/ux/ProgressBarPager.js'/>"></script>
		<script type="text/javascript">
			Ext.basePath = Ext.emptyFn;
			Ext.apply(Ext.basePath.prototype,{
				path:''
			})
			Ext.basePath.path = '<%=basePath%>';
		</script>
	</head>

	<body>
	</body>
</html>