<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>

<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,minimum-scale=1,maximum-scale=1,user-scalable=no" />
    <title>${resource.rsdaTitle }<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "在线预览") %></title>
    <style type="text/css">
    	@font-face {
			font-style: normal;
			font-weight: normal;
			font-family: hm;
			src: url(<%=basePath %>font/hm.eot);
			src: local('A-Himalaya'), local('A-Himalaya'),
				url(<%=basePath %>font/A-Himalaya.ttf) format('truetype');
		}
		* {font-family: hm; font-size: 24px; color: #a6b8cc;}
    	body{background: #1a314a;}
    	.a{width: 600px; height: 220px; text-align: center; color: #FFF;}
    	.a{position: absolute; left: 50%; top: 30%; margin: -110px 0px 0px -300px;}
    </style>
</head>

<body>
	<div class="a">
		<p><img src="<%=basePath %>images/platform/error.png" height="128px"></p>
		<p>${message }</p>
	</div>    
</body>
</html>
