<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/wap/reslist.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=basePath %>js/wap/wapPager.js"></script>
<script type="text/javascript" src="<%=basePath %>js/wap/reslist.js"></script>

<p class="clear" id="cntline" />
<ul class="w_tab">
	<li>
		<a href="javascript:void(0);" class="active" id="1"><fmt:message key='repeat.list.mode'/></a>
		<a href="javascript:void(0);" id="2"><fmt:message key='repeat.table.mode'/></a>
	</li>
</ul>

<div class="w_listbox">
	<div id="box">
		<ul id="res1"></ul>
		<ul id="res2"></ul>
	</div>
</div>
<div id="w_pages"></div>

<input type="hidden" id="_rmstId" value="${numId }">
<input type="hidden" id="_rltyId" value="${rltyId }">
<input type="hidden" id="_currentPage" value="${currentPage }">
<%@ include file="footer.jsp" %>
