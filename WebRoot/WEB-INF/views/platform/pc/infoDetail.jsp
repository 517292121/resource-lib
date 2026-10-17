<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/platform/info.css" type="text/css"></link>

<div class="main page box-shadow">
	<div class="infodetail">
		<p id="title">${info.idsiTitle }</p>
		<li id="base">
			<span id="time">
				<c:if test="${!empty info.idsInformationType }">
				<a href="<%=basePath %>info/list?cid=${info.idsiIdstId }">${info.idsInformationType.idstName }</a> &nbsp; 
				</c:if>
				<fmt:formatDate value="${info.idsiDate }" pattern="yyyy-MM-dd HH:mm"/> &nbsp; 
				<fmt:message key="info.read"/><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b>${info.idsiHitCount }
			</span>
			<span id="author"><fmt:message key="info.publisher"/><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b>${info.sysManager.smanName }</span>
		</li>
		<div id="content">${info.idsiContent }</div>
		
		<div id="round">
			<li id="pre">
				<c:if test="${!empty infoPre }"><a href="<%=basePath %>info/detail/${infoPre[0] }"><fmt:message key="info.last.one"/><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b>${infoPre[1] }</a></c:if>
			</li>
			<li id="next">
				<c:if test="${!empty infoNext }"><a href="<%=basePath %>info/detail/${infoNext[0] }"><fmt:message key="info.next"/><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b>${infoNext[1] }</a></c:if>
			</li>
		</div>
	</div>
</div>

<%@ include file="common/footer.jsp" %>
