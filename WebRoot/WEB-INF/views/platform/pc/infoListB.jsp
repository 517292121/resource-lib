<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/platform/info.css" type="text/css"></link>

<div class="main" style="padding-bottom: 40px;">
	<div class="pleft">
		<div class="infoclass">
			<p class="slide-left"><fmt:message key="info.classified.navigation"/></p>
			<ul>
			     <c:forEach items="${classList }" var="v">
			     <li <c:if test="${classId eq v[0] }">class='curr'</c:if>><a href="<%=basePath %>info/list?cid=${v[0] }">${v[1] }</a></li>
			     </c:forEach>
			</ul>
		</div>
	</div>
	
	<div class="pright">
		<div class="listbox box-shadow">
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
						<c:if test="${fn:length(v[2]) > 110 }">${fn:substring(v[2], 0, 110) }...</c:if>
						<c:if test="${fn:length(v[2]) <= 110 }">${v[2] }</c:if>
					</p>
					<p class="rq">
						<fmt:formatDate value="${v[3] }" pattern="yyyy-MM-dd HH:mm"/> &nbsp; &nbsp; &nbsp; &nbsp; 
						${v[5] }
					</p>
				</li>
				<p class="clear">
			</ul>
			</c:forEach>	
			
			<c:if test="${empty dataList}"><div class="nodata"><fmt:message key="info.unrelated.information"/>...</div></c:if>
			<div style="padding: 10px 0px;"><%@ include file="common/turnPage.jsp" %></div>
		</div>
	</div>
	<p class="clear">
</div>

<%@ include file="common/footer.jsp" %>
