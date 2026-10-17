<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>
<style>
<!--
body{background: #FFF;}
-->
</style>
<link rel="stylesheet" href="<%=basePath %>css/platform/info.css" type="text/css"></link>

<!-- jQuery滚动执行动画插件FadeThis -->
<script type="text/javascript" src="<%=path %>/js/jquery.fadethis.js"></script>

<div class="class-top">
	<ul class="main">
	<li><a href="<%=basePath %>info/list" <c:if test="${empty classId }">class='curr'</c:if>><fmt:message key="repeat.all"/></a></li>
	<c:forEach items="${classList }" var="v">
     <li><a href="<%=basePath %>info/list?cid=${v[0] }" <c:if test="${classId eq v[0] }">class='curr'</c:if>>${v[1] }</a></li>
    </c:forEach>
    </ul>
</div>
<div class="main">
	<div class="list-full">
		<c:forEach items="${dataList }" var="v">
		<ul class="slide-bottom">
			<li class="col1"><a href="<%=basePath %>info/detail/${v[0] }">
			<c:if test="${empty v[4] }">
				<dt><span class="date"><fmt:formatDate value="${v[3] }" pattern="dd"/></span>
	    			<span><fmt:formatDate value="${v[3] }" pattern="yyyy-MM"/></span>
	    		</dt>
			</c:if>
			<c:if test="${!empty v[4] }"><img src="<%=SysConfigInfo.FILE_URL_PATH %>${v[4] }"></c:if></a>
			</li>
			
			<li class="col2">
				<p class="tm"><a href="<%=basePath %>info/detail/${v[0] }">${v[1] }</a></p>
				<p class="ms">
					<c:if test="${fn:length(v[2]) > 150 }">${fn:substring(v[2], 0, 150) }...</c:if>
					<c:if test="${fn:length(v[2]) <= 150 }">${v[2] }</c:if>
				</p>
				<p class="rq">
					<fmt:formatDate value="${v[3] }" pattern="yyyy-MM-dd HH:mm"/> &nbsp; &nbsp; &nbsp; &nbsp; 
					${v[5] }
				</p>
			</li>
			
			<li class="slide-right col3">
				<a href="<%=basePath %>info/detail/${v[0] }"><fmt:message key="info.view.the.details"/></a>
			</li>
			<p class="clear">
		</ul>
		</c:forEach>	
		
		<c:if test="${empty dataList}"><div class="nodata"><fmt:message key="info.unrelated.information"/>...</div></c:if>
		<div style="padding: 15px;"><%@ include file="common/turnPage.jsp" %></div>
	</div>
</div>

<%@ include file="common/footer.jsp" %>
