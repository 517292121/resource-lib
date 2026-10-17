<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>  
<html>  
<head>  
     <title>jQuery实现图片切换</title>    
     <script src="<%=basePath %>js/jquery.min.js" type="text/javascript"></script>
     <script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/platform/browseHis.js"></script>
	<script type="text/javascript">
		$(function(){
			var resCookie = $.cookie(BROWSE_COOKIE_NAME);
			$("#cookieval").html(resCookie);
		})
	</script>
</head>  
<body>  
      <div id="cookieval">  
           
      </div>  
</body>  
</html> 

