<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/platform/reslist.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/myPager.js"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/textResList.js"></script>
<link rel="stylesheet" href="<%=basePath %>/wtree/dtree.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>/wtree/dtree.js"></script>

<p id="cntline"></p>
<div class="main booklist">
	<div class="pleft">
		<div class="resclass">
			<p class="slide-left">${textLib.rtliName }</p>
			<ul>
			     <script type="text/javascript">
			           var d0 = new dTree("d0");
				       d0.add(0,-1,"全部分类","javascript:classQuery(0);","","");
			           <c:forEach items="${classList }" var="v" varStatus="s">
			           		d0.add("${v.rlclId }","${empty v.rlclParentId ? 0 : v.rlclParentId }","${v.rlclName }","javascript:classQuery(${v.rlclId });","","");
			           </c:forEach>
			           document.write(d0);
			     </script>
			</ul>
		</div>
	</div>
	
	<div class="pright">
		<ul class="toolbar nav txtbar">
			<li id="l">
				<a href="javascript:void(0);" class="active" id="1"><fmt:message key="repeat.detailed.pattern"/></a>
				<a href="javascript:void(0);" id="3"><fmt:message key="repeat.table.mode"/></a>
			</li>
			<li id="r">
				<span><fmt:message key="res.sort.order"/>：</span>
				<a href="javascript:void(0);" onclick="sortQuery('rtreDate');" class="active"><fmt:message key="repeat.entry.time"/></a>
				<a href="javascript:void(0);" onclick="sortQuery('rtreAccess');" style="margin-right: 0px;"><fmt:message key="res.visit.frequency"/></a>
			</li>
			<p class="clear">
		</ul>
		
		<div class="listbox">
			<div id="box">
				<ul id="res1">
					<div class="jz"><img src="<%=basePath %>/images/platform/loading.gif"></div>
				</ul>
				<ul id="res3"></ul>
			</div>
		</div>
	</div>
	<p class="clear">
</div>
<input type="hidden" id="_rtliId" value="${numId }">
<input type="hidden" id="_rlclId" value="${rlclId }">
<input type="hidden" id="_sort" value="${sort }">
<input type="hidden" id="_currentPage" value="${currentPage }">
<%@ include file="common/footer.jsp" %>
