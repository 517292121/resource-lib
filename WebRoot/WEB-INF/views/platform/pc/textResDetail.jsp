<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/platform/textres.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>js/platform/textResDetail.js?ui=zh-zw-20261025-1"></script>
<div class="main">
	<div class="pleft">
		<!-- 元数据信息 -->
		<div class="metadata">
		<table border="0">
			<tr>
				<td colspan="2" id="title">
					<c:if test="${!empty resource.resLibraryClassify}"><span class="bcm">${resource.resLibraryClassify.rlclName }</span></c:if> 
					<font id="rsdaTitle">${resource.rtreTitle }</font>
				</td>
			</tr>
			<tr>
				<td colspan="2" id="base">

					<fmt:message key="repeat.resource.library"/><c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose><a href="<%=basePath %>text/lib/${resource.rtreRtliId }">${resource.resTextLibrary.rtliName }</a> &nbsp;
					<fmt:message key="repeat.amount.of.access"/><c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose>${resource.rtreAccess } &nbsp; 
					<fmt:message key="repeat.entry.time"/><c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose><fmt:formatDate value="${resource.rtreDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>

					<fmt:message key='repeat.resource.library'/><c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose><a href="<%=basePath %>text/lib/${resource.rtreRtliId }">${resource.resTextLibrary.rtliName }</a> &nbsp;
					<fmt:message key='info.read'/><c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose>${resource.rtreAccess } &nbsp; 
					<fmt:message key='repeat.entry.time'/><c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose><fmt:formatDate value="${resource.rtreDate}" type="date" pattern="yyyy-MM-dd HH:mm"/>

				</td>
			</tr>
			<c:if test="${!empty textLib.rtliCol1Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol1Label) }">${textLib.rtliCol1Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol1 }</td>
			</tr>
			</c:if>
			
			<c:if test="${!empty textLib.rtliCol2Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol2Label) }">${textLib.rtliCol2Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol2 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol3Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol3Label) }">${textLib.rtliCol3Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol3 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol4Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol4Label) }">${textLib.rtliCol4Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol4 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol5Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol5Label) }">${textLib.rtliCol5Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol5 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol6Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol6Label) }">${textLib.rtliCol6Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol6 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol7Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol7Label) }">${textLib.rtliCol7Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol7 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol8Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol8Label) }">${textLib.rtliCol8Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol8 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol9Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol9Label) }">${textLib.rtliCol9Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol9 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol10Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol10Label) }">${textLib.rtliCol10Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol10 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol11Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol11Label) }">${textLib.rtliCol11Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol11 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol12Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol12Label) }">${textLib.rtliCol12Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol12 }</td>
			</tr>
			</c:if>
			
			<c:if test="${!empty textLib.rtliCol13Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol13Label) }">${textLib.rtliCol13Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol13 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol14Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol14Label) }">${textLib.rtliCol14Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol14 }</td>
			</tr>
			</c:if>
			<c:if test="${!empty textLib.rtliCol15Label}">
			<tr>
				<td class="lab w${fn:length(textLib.rtliCol15Label) }">${textLib.rtliCol15Label }<c:choose>
						<c:when test="${empty language || 'zw' eq language}">&nbsp;&nbsp;</c:when>
						<c:otherwise>：</c:otherwise>
					</c:choose></td>
				<td>${resource.rtreCol15 }</td>
			</tr>
			</c:if>
		</table>
		</div>
	</div>
	
	
	<!-- 页面右侧 -->
	<div class="pright">
		<div class="r-panel">
			<h2 class="bcm"><fmt:message key="repeat.hot.resources"/></h2>
			<ul id="resHot"></ul>
		</div>
		<div class="r-panel">
			<h2 class="bcm"><fmt:message key="repeat.latest.resources"/></h2>
			<ul id="resNew"></ul>
		</div>
	</div>
	<p class="clear">
</div>
<%@ include file="common/footer.jsp" %>
