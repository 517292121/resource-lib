<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
  <head>
    <title>资源章节目录</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  	<link rel="stylesheet" href="<%=basePath %>css/platform/page-catalog.css" type="text/css"></link>
  	<script type="text/javascript" src="<%=basePath %>js/jquery-1.11.0.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
  	<script type="text/javascript">var basePath = "<%=basePath %>";</script>
  	<script>
		$(function(){
			$("#gb").click(function(){
				window.parent.window.closeCatalog();
			})
		})
		
		function inpage(fname, title){
			var xmlpath = "${imgDir}" + fname + ".xml";
			window.parent.catalog(title,xmlpath,"");
		}
	</script>
  </head>
  
  <body>
    <div class="c-body">
    	<p class="header">
    		<span id="js">章节目录</span>
    		<span id="gb"><a href="">×</a></span>
    	</p>
    	<div class="list-div">
    		<ul id="treelist">
    		
	    	<c:if test="${!empty catalogList}">
		    	<c:forEach items="${catalogList}" var="v">
		    	<li><a href="javascript:void(0);" onclick="inpage('${v.pageNameCode }', '${v.pageName }');" title="${v.title }">${v.title }</a></li>
		    	</c:forEach>
	    	</c:if>
	    	<c:if test="${empty catalogList}">
	    		<p class="no-search"><fmt:message key="resource.preview.not.directory"/></p>
	    	</c:if>
    		</ul>
    	</div>
    </div>
  </body>
</html>
