<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@ include file="header.jsp" %>
<%@ include file="resBrowse.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/wap/resdetail.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=basePath %>js/wap/browseHis.js"></script>
<script type="text/javascript" src="<%=basePath %>js/wap/resdetail.js"></script>


<div class="w_split"></div>
<div class="w_base">
	<c:if test="${empty resource.rsdaBigimgUrl }">
		<div class="cover"><img src="<%=basePath %>/images/platform/doc-cover.jpg"></div>
	</c:if>
	<c:if test="${!empty resource.rsdaBigimgUrl }">
		<div class="cover"><img src="${resource.rsdaBigimgUrl }" onerror="this.src='<%=basePath %>/images/platform/doc-cover.jpg'"></div>
	</c:if>
	<div class="base">
		<p id="title"><span class="bcm">${resource.resLiterType.rltyName }</span> <font id="rsdaTitle">${resource.rsdaTitle }</font></p>
		
		<li><b><fmt:message key='repeat.resource.library'/>：</b> <p><a href="<%=basePath %>wap/res/db/${resource.resMetaStore.rmstId }">${resource.resMetaStore.rmstName }</a></p></li>
		
		<c:if test="${!empty resource.resSubjectType }">
		<li><b><fmt:message key='det.discipline'/>：</b> <p>${resource.resSubjectType.rstyName }</p></li>
		</c:if>
		
		<c:if test="${!empty resource.rsdaFileType }">
				<li>
					<b><fmt:message key="edit.format" /><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b> <p>${resource.rsdaFileType }</p></li>	
				</li>
		</c:if>
		<c:if test="${!empty fileSize }">
				<li>
					<b><fmt:message key="file.size" /><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b> <p>${fileSize }</p></li>	
				</li>
		</c:if>
				
		<li><b><fmt:message key='repeat.entry.time'/><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b> <p><fmt:formatDate value="${resource.rsdaBuilddate }" pattern="yyyy-MM-dd HH:mm" /></p></li>
		<li><b><fmt:message key='repeat.amount.of.access'/><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b> <p>${resource.rsdaLooknum }</p></li>
	</div>
</div>
<dl class="fav">
		<c:if test="${userRight >= 4 }">
					<c:if test="${isPdfViewer or (resource.rsdaSourceType > 0 and !empty resource.rsdaFileUrl)}">
					<a href="<%=basePath %>wap/res/browse/${resource.rsdaId }" id="yulan" class="bcm"><fmt:message key="file.online.preview"/></a>
					</c:if>
		</c:if>
					
	
	<c:if test="${!empty downloadUrl}">
	<a href="<%=SysConfigInfo.FILE_URL_PATH %>${downloadUrl }" id="xiazai" class="bcm"><fmt:message key='det.download.resource'/></a>
	</c:if>
	<a href="javascript:void(0);" onclick="saveFav();" id="shouchang" class="bcm"><fmt:message key='det.join.the.collection'/></a>
</dl>
<div class="w_line"></div>
<div class="w_metadata">
	<ul id="metalist"></ul>
</div>

<div id="attachfile"></div>
<div id="similarbox"></div>

<div class="hisbox">
	<h2><fmt:message key='repeat.browse'/></h2>
	<ul id="resBrowseHis"></ul>
</div>

<input type="hidden" id="_rsdaId" value="${resource.rsdaId }">
<input type="hidden" id="_rmstId" value="${resource.rsdaRmstId }">
<input type="hidden" id="_rltyId" value="${resource.rsdaRltyId }">
<input type="hidden" id="_rssuId" value="${resource.rsdaRssuId }">
<input type="hidden" id="_isCmt" value="${resource.rsdaCmtstus }">
<input type="hidden" id="_userId" value="<%=MyRequestUtil.getMemberId(request) %>">
<%@ include file="footer.jsp" %>
