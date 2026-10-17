<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<html>
<head>
<title>图形插件测试</title>

<link href="<%=path %>/plugin/charts/css/jquery.circliful.css" rel="stylesheet" type="text/css" />

<script src="<%=path %>/js/jquery.min.js"></script>
<script src="<%=path %>/plugin/charts/js/jquery.circliful.min.js"></script>

<style>
body {
    font-family: arial,verdana, sans-serif;
    font-size: 12px;
}


</style>
</head>
<body>
<div id="myStat" 
	data-dimension="180" 
	data-text="35%" 
	data-info="资源占比" 
	data-width="16" 
	data-fontsize="36"
	data-percent="35" 
	data-fgcolor="#339999" 
	data-bgcolor="#ff8000" 
	data-fill="#F5F5F5"></div>
<script>
	$( document ).ready(function() {
		$('#myStat').circliful();
    });
</script>
</body>
</html>