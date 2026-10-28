<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/platform/reslist.css" type="text/css"></link>
<link rel="stylesheet" href="<%=basePath %>css/platform/luceneList.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/myPager.js?ui=zh-zw-20261025-1"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/pagenumFormat.js?ui=zh-zw-20261025-1"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/luceneSearch.js?ui=zh-zw-20261025-1"></script>

<p id="cntline"></p>
<div class="main booklist">
	<div class="pleft" id="groupbox">
		<div class="resclass">
			<p><fmt:message key="repeat.data.type"/></p>
			<ul id="ku"><img src="<%=basePath %>images/platform/loading.gif"></img></ul>
		</div>
		<div class="resclass">
			<p><fmt:message key="repeat.characteristic.library"/></p>
			<ul id="ku"><img src="<%=basePath %>images/platform/loading.gif"></img></ul>
		</div>
		<div class="resclass">
			<p><fmt:message key="repeat.literature.type"/></p>
			<ul id="ku"><img src="<%=basePath %>images/platform/loading.gif"></img></ul>
		</div>
	</div>
	
	
	<div class="pright">
		<div class="tiaojian" id="searchcon">
			<i><fmt:message key="repeat.retrieval.condition"/>：</i>
		</div>
		<div class="tools nav">
			<li id="results"><fmt:message key="repeat.common"/> <span id="resultTotal">-</span> <fmt:message key="repeat.bar.record"/> <span id="currnums">1-10</span></li>
			<li id="models">
				<a href="javascript:void(0);" class="active" id="2"><fmt:message key="repeat.detailed.pattern"/></a>
				<a href="javascript:void(0);" id="3"><fmt:message key="repeat.table.mode"/></a>
			</li>
		</div>
		<div class="listbox">
			<div id="box">
				<ul id="res2"><img src="<%=basePath %>images/platform/loading.gif"></ul>
				<ul id="res3"></ul>
			</div>
		</div>
	</div>
	<p class="clear" />
</div>
<input type="hidden" name="s_params" id="s_params" attr="${attr }" key="${key }" datatype="${datatype }" reslib="" restype="" xueke="" creator="" publisher="" keyword="" date="" pn="1" match="${match }" >
<%@ include file="common/footer.jsp" %>
