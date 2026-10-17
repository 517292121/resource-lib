<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>
<script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
<script type="text/javascript" src="<%=basePath %>js/platform/advsearchResult.js"></script>
<link rel="stylesheet" href="<%=basePath %>css/platform/advsearch.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>/wtree/dtree.js"></script>
<!-- jQuery滚动执行动画插件FadeThis -->
<script type="text/javascript" src="<%=path %>/js/jquery.fadethis.js"></script>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>

<p id="cntline"></p>
<div class="main">
	<!-- 页面左侧 -->
	<div class="pleft">
		<ul class="toolbar nav">
			<li id="results"><fmt:message key="repeat.common"/> <span id="resultTotal">${pagination.totalResults}</span> <fmt:message key="repeat.bar.record"/> <span id="currnums">${begin}-${end}</span></li>
			<li id="l">
				<a href="javascript:void(0);" id="1" class="active"><fmt:message key="repeat.list.mode"/></a>
				<a href="javascript:void(0);" id="2"><fmt:message key="repeat.detailed.pattern"/></a>
				<a href="javascript:void(0);" id="3"><fmt:message key="repeat.table.mode"/></a>
			</li>
			<p class="clear">
		</ul>
		<div class="listbox">
			<div id="box">
						<!-- 表格模式 -->
						<ul id="res1" >
							<c:if test="${ empty dataList }">
									<fmt:message key="repeat.no.data"/>
							</c:if>
							<c:if test="${ !empty dataList }">
							<table  cellpadding='0' cellspacing='0' border='0'>
								<tr id='tou'>
									<th class='tm'><fmt:message key="repeat.title"/></th>
									<th class='zrz'><fmt:message key="repeat.responsible.person"/></th>
									<th class='rq'><fmt:message key="repeat.creation.date"/></th>
								<c:forEach items="${dataList}" var="v">
									<tr>
										<td class='tm' id='tm'><a href="<%=basePath %>res/${v[0]}">${v[1]}</a></td>
										<td class='zrz' id='zrz'>
											<c:forEach items="${fn:split(v[2],' ')}" var="zrz">
												<a href="<%=basePath %>resAdvs?COL2=rsdaCreator&KEY2=${zrz}">${zrz}</a>
											</c:forEach>
										</td>
										<td class='rq'><fmt:formatDate value="${v[8]}" pattern="yyyy-MM-dd" /></td>
									</tr>
								</c:forEach>
							</table>
							</c:if>
						</ul> 
						
						
						<!-- 详细模式 -->
						<ul id= "res2" >
							<c:if test="${ empty dataList }">
								<fmt:message key="repeat.no.data"/>
							</c:if>
							<c:if test="${ !empty dataList }">
							<c:forEach items="${dataList}" var="v">
								<div class='item'>
									<div class='fm'>
										<c:if test="${empty v[7]}">
												<a href="<%=basePath %>res/${v[0]}"><img src="<%=basePath %>images/platform/doc-cover.jpg"></a>
										  	</c:if>
											<c:if test="${!empty v[7]}">
												<a href="<%=basePath %>res/${v[0]}"><img src="${v[7]}"></a>
										</c:if>
									</div>
									<div class='xx'>
										<p id='tm'><a href="<%=basePath %>res/${v[0]}">${v[1]}</a></p>
										<li id='zrz'><b><fmt:message key="repeat.responsible.person"/>：</b>
											<c:forEach items="${fn:split(v[2],' ')}" var="zrz">
												<a href="<%=basePath %>resAdvs?COL2=rsdaCreator&KEY2=${zrz}">${zrz}</a>
											</c:forEach>
											&nbsp;<fmt:message key="repeat.publisher"/>：${v[3]}
										</li>
										<li id='ms'><b><fmt:message key="repeat.description"/>：</b>${v[5]} </li>
										<li id='gjc'><b><fmt:message key="repeat.detail.keyword"/>： </b>
											<c:forEach items="${fn:split(v[6],' ')}" var="gjc">
											 	<a href="<%=basePath %>resAdvs?COL4=rsdaKeyword&KEY4=${gjc}">${gjc}</a>
											</c:forEach>
										</li>
										<dl><b><fmt:message key="repeat.date"/>： </b><fmt:formatDate value="${v[8]}" pattern="yyyy-MM-dd" />  &nbsp; <fmt:message key="repeat.amount.of.access"/>：${v[9]}</dl>
									</div>
								</div>
							</c:forEach>
							</c:if>
						</ul>
						
						<!-- 列表模式 -->
						<ul id="res3">
							<c:if test="${ empty dataList }">
								<fmt:message key="repeat.no.data"/>
							</c:if>
							<c:if test="${ !empty dataList }">
							<c:forEach items="${dataList}" var="v">
								<div class='item'>
									<p>
										<c:if test="${empty v[7]}">
												<a href="<%=basePath %>res/${v[0]}"><img src="<%=basePath %>images/platform/doc-cover.jpg"></a>
										  	</c:if>
											<c:if test="${!empty v[7]}">
												<a href="<%=basePath %>res/${v[0]}"><img src="${v[7]}"></a>
										</c:if>
									</p>
									<li id="tm"><a href="<%=basePath %>res/${v[0]}">${v[1]}</a></li>
								</div>
							</c:forEach>
							</c:if>
						 </ul>
				</div>
			</div>
				 <%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
		</div>
		<!-- 页面右侧 -->
			<div class="pright">
				<div id="keys" class="r-panel">
						<h2 class="bcm"><fmt:message key="repeat.hot.key.words"/></h2>
						<ul></ul>
				</div>
				<div id="attachfile"></div>
				<div id="similarbox"></div>
			</div>
		<p class='clear' />
	</div>
	<input type="hidden" id="key1" value="${key1 }"/>
	<input type="hidden" id="key2" value="${key2 }"/>
	<input type="hidden" id="key3" value="${key3 }"/>
	<input type="hidden" id="key4" value="${key4 }"/>
	<input type="hidden" id="col1" value="${col1 }"/>
	<input type="hidden" id="col2" value="${col2 }"/>
	<input type="hidden" id="col3" value="${col3 }"/>
	<input type="hidden" id="col4" value="${col4 }"/>
<%@ include file="common/footer.jsp" %>
