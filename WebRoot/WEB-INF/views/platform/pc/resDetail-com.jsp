<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@ include file="common/header.jsp" %>
<script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/browseHis-font.js?ui=zh-zw-20261025-1"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/resdetail-com.js?ui=zh-zw-20261025-1"></script>
<link rel="stylesheet" href="<%=basePath %>css/platform/resdetail-com.css" type="text/css"></link>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<div class="main">
	<div class="pleft">
		<!-- 资源预览 -->
		<c:if test="${resource.rsdaSourceType == 0 && isPdfViewer==false}">
		<%@ include file="resBrowse.jsp" %>
		</c:if>
		
		<!-- 元数据信息 -->
		<div class="metadata">
			<div id="cover">
				<c:if test="${empty resource.rsdaBigimgUrl }"><img src="<%=basePath %>/images/platform/doc-cover.jpg"></c:if>
				<c:if test="${!empty resource.rsdaBigimgUrl }"><img src="${resource.rsdaBigimgUrl }" onerror="this.src='<%=basePath %>/images/platform/doc-cover.jpg'"></c:if>
				<dl>
					<c:if test="${userRight >= 4 }">
					<c:if test="${isPdfViewer or (resource.rsdaSourceType > 0 and !empty resource.rsdaFileUrl)}">
					<a href="<%=basePath %>res/browse/${idcode }" id="yulan" target="_blank"><fmt:message key="file.online.preview"/></a>
						<c:if test="${!empty resource.rsdaFileSize and resource.rsdaFileSize > (1024*1024*20)}">
							<div class="bigfile"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "源文较大，建议下载预览") %></div>
						</c:if>
					</c:if>
					</c:if>
					<c:if test="${!empty downloadUrl}">
					<a href="<%=SysConfigInfo.FILE_URL_PATH %>${downloadUrl }" id="xiazai"><fmt:message key="det.download.resource"/></a>
					</c:if>
					<a href="javascript:void(0);" onclick="saveFav();" id="shouchang"><fmt:message key="det.join.the.collection"/></a>
				</dl>
			</div>
			<div id="baseinfo">
				<p id="title"><span class="bcm">${resource.resLiterType.rltyName }</span> <font id="rsdaTitle">${resource.rsdaTitle }</font></p>
				<li><b><fmt:message key='repeat.resource.library'/><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b></b> <p>${resource.resMetaStore.rmstName }</p></li>
				<c:if test="${!empty resource.resSubjectType }">
				<li><b><fmt:message key='det.discipline'/><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b></b> <p>${resource.resSubjectType.rstyName }</p></li>
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
							</c:choose></b></b> <p><fmt:formatDate value="${resource.rsdaBuilddate }" pattern="yyyy-MM-dd HH:mm:ss" /></p></li>
				<li><b><fmt:message key='repeat.amount.of.access'/><c:choose>
								<c:when test="${'zw' eq language}">&nbsp;&nbsp;</c:when>
								<c:otherwise>：</c:otherwise>
							</c:choose></b></b> <p>${resource.rsdaLooknum }</p></li>

			</div>
			<p class="clear">
		</div>
		
		<!-- 该资源所属主题下其它资源 -->
		<div id="related-panel"></div>
		
		<c:if test="${resource.rsdaCmtstus==1 }">
		<!-- 资源评论 -->
		<div class="comments">
			<li id="bt"><b><fmt:message key="det.i.commented"/></b><span> · <fmt:message key="det.agreement"/></span></li>
			<div id="savecom">
				<p><textarea name="comContent" id="comContent" onkeydown="remainWords(event);" onkeypress="remainWords(event);" onkeyup="remainWords(event);"></textarea></p>
				<p id="baochun">
					<input type="button" id="savebtn" value="<fmt:message key="det.comment"/>" onclick="saveComments()">
					<span>&nbsp; (<fmt:message key="det.still.left"/> <font id="fonts">300</font> <fmt:message key="det.word"/>)</span>
				</p>
			</div>
			<p id="title">
				<b><fmt:message key="det.all.comments"/></b>
				<span><fmt:message key="repeat.common"/> <i id="comtotals">0</i> <fmt:message key="det.strip.comments"/></span>
			</p>
			<div id="comlist"></div>
			<div id="morecomd">
				<a href="javascript:void(0)" onclick="getResComments();"><fmt:message key="det.more.comments"/></a>
			</div>
		</div>
		</c:if>
	</div>
	
	
	<!-- 页面右侧 -->
	<div class="pright">
		<div id="attachfile"></div>
		<div id="similarbox"></div>
		<div class="r-panel">
			<h2 class="bcm"><fmt:message key="repeat.browse"/></h2>
			<ul id="resBrowseHis"></ul>
		</div>
	</div>
	<p class="clear">
</div>

<input type="hidden" id="_rsdaId" value="${resource.rsdaId }">
<input type="hidden" id="_rmstId" value="${resource.rsdaRmstId }">
<input type="hidden" id="_rltyId" value="${resource.rsdaRltyId }">
<input type="hidden" id="_rssuId" value="${resource.rsdaRssuId }">
<input type="hidden" id="_userId" value="<%=MyRequestUtil.getMemberId(request) %>">
<input type="hidden" id="_isCmt" value="${resource.rsdaCmtstus }">
<%@ include file="common/footer.jsp" %>
