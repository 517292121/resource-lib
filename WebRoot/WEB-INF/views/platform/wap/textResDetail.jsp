<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/wap/textres.css" type="text/css"></link>
<!-- 元数据信息 -->
<div class="metadata">
<table border="0">
	<tr>
		<td id="title">
			<font id="rsdaTitle">${resource.rtreTitle }</font>
		</td>
	</tr>
	<tr>
		<td><fmt:message key='repeat.resource.library'/>：${resource.resTextLibrary.rtliName }</td>
	</tr>
	<c:if test="${!empty resource.resLibraryClassify}">
	<tr>
		<td><fmt:message key='list.type'/>：${resource.resLibraryClassify.rlclName }</td>
	</tr>
	</c:if>
	<tr>
		<td><fmt:message key='repeat.amount.of.access'/>：${resource.rtreAccess }</td>
	</tr>
	<c:if test="${!empty textLib.rtliCol1Label}">
	<tr>
		<td>${textLib.rtliCol1Label }：${resource.rtreCol1 }</td>
	</tr>
	</c:if>
	
	<c:if test="${!empty textLib.rtliCol2Label}">
	<tr>
		<td>${textLib.rtliCol2Label }：${resource.rtreCol2 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol3Label}">
	<tr>
		<td>${textLib.rtliCol3Label }：${resource.rtreCol3 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol4Label}">
	<tr>
		<td>${textLib.rtliCol4Label }：${resource.rtreCol4 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol5Label}">
	<tr>
		<td>${textLib.rtliCol5Label }：${resource.rtreCol5 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol6Label}">
	<tr>
		<td>${textLib.rtliCol6Label }：${resource.rtreCol6 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol7Label}">
	<tr>
		<td>${textLib.rtliCol7Label }：${resource.rtreCol7 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol8Label}">
	<tr>
		<td>${textLib.rtliCol8Label }：${resource.rtreCol8 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol9Label}">
	<tr>
		<td>${textLib.rtliCol9Label }：${resource.rtreCol9 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol10Label}">
	<tr>
		<td>${textLib.rtliCol10Label }：${resource.rtreCol10 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol11Label}">
	<tr>
		<td>${textLib.rtliCol11Label }：${resource.rtreCol11 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol12Label}">
	<tr>
		<td>${textLib.rtliCol12Label }：${resource.rtreCol12 }</td>
	</tr>
	</c:if>
	
	<c:if test="${!empty textLib.rtliCol13Label}">
	<tr>
		<td>${textLib.rtliCol13Label }：${resource.rtreCol13 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol14Label}">
	<tr>
		<td>${textLib.rtliCol14Label }：${resource.rtreCol14 }</td>
	</tr>
	</c:if>
	<c:if test="${!empty textLib.rtliCol15Label}">
	<tr>
		<td>${textLib.rtliCol15Label }：${resource.rtreCol15 }</td>
	</tr>
	</c:if>
</table>
</div>
<%@ include file="footer.jsp" %>
