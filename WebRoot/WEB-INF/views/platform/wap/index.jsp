<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="header.jsp" %>
<link rel="stylesheet" href="<%=basePath %>css/wap/index.css" type="text/css"></link>
<script type="text/javascript" src="<%=basePath %>js/wap/index.js"></script>
<div class="w-center">
		<ul class="choose">
			<c:forEach items="<%=PlatformCacheFactory.instance().getResForTypeList() %>" var="v" varStatus="s">
    		<li <c:if test="${s.index==0 }">class="active"</c:if> data-id="d${v.rltyId }"><a href="">${v.rltyName }</a></li>
    		</c:forEach>
		</ul>
		<div class="content-img">
			<div class="img-all">
				<ul class="img-list"></ul>
			</div>
		</div>
	
		<ul class="choose-news">
			<li class="active" data-type="1"><a href=""><fmt:message key='repeat.latest.resources'/></a></li>
			<li data-type="2"><a href="" ><fmt:message key='index.hot.spot.resources'/></a></li>
		</ul>
		<ul class="news-li"></ul>
	</div>
	
	<script type="text/javascript">
    	$(function(){
    		var w_width = $(window).width();
    		$('.long-nav .nav-content .nav li').width((w_width-20)/5.5);
    		$('.long-nav .nav-content').width((w_width-20)/5.5*$('.long-nav .nav-content .nav li').length);
    		$('.content-img .img-list li a').width((w_width-30)/3.5);
    		$('.content-img .img-list').width((w_width)/3.5*$('.content-img .img-list li').length);
    	})
    </script>
<%@ include file="footer.jsp" %>
