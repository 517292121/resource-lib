<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/wap/info.css" type="text/css"></link>
	<div class="infodetail">
		<p id="title">${info.idsiTitle }</p>
		<li id="base">
			<span id="time">
				<c:if test="${!empty info.idsInformationType }">
				<a href="<%=basePath %>info/list?cid=${info.idsiIdstId }">${info.idsInformationType.idstName }</a> &nbsp; 
				</c:if>
				<fmt:formatDate value="${info.idsiDate }" pattern="yyyy-MM-dd HH:mm"/> &nbsp; 
				<fmt:message key='info.read'/>：${info.idsiHitCount }
			</span>
		</li>
		<div id="content">${info.idsiContent }</div>
		
		<div id="round">
			<li id="pre">
				<c:if test="${!empty infoPre }"><a href="<%=basePath %>wap/info/detail/${infoPre[0] }"><fmt:message key='info.last.one'/>：${infoPre[1] }</a></c:if>
			</li>
			<li id="next">
				<c:if test="${!empty infoNext }"><a href="<%=basePath %>wap/info/detail/${infoNext[0] }"><fmt:message key='info.next'/>：${infoNext[1] }</a></c:if>
			</li>
		</div>
	</div>
<%@ include file="footer.jsp" %>
