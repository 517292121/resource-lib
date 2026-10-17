<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/wap/luceneSearch.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>js/wap/luceneSearch.js"></script>
<script type="text/javascript" src="<%=basePath %>js/wap/wapPager.js"></script>

<div class="reslist">
	<!-- <img src="<%=basePath %>images/platform/loading.gif" id="loading"></img> -->
	
	<ul class='luc-res'>
		<img src="<%=basePath %>images/platform/loading.gif" id="loading"></img>	
	</ul>
</div>
<div id="w_pages"></div>

<input type="hidden" name="s_params" id="s_params" key="${key }" datatype="${datatype }" reslib="" restype="" xueke="" creator="" publisher="" keyword="" date="" pn="1" match="${match }">
<%@ include file="footer.jsp" %>
