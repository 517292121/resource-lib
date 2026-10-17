<%@page import="cn.com.unionlib.lucene.bean.ResMainBean"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>索引资源详细信息</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<link rel="stylesheet" href="<%=basePath %>css/console/common.css" type="text/css"></link>
	<style type="text/css">
		body{background: #F3F3F3;}
		.box-shadow{-webkit-box-shadow: 0 0.1rem 0.4rem rgba(0, 0, 0, 0.1); box-shadow: 0 0.1rem 0.4rem rgba(0, 0, 0, 0.1);}
		.res-body{margin: 15px 20px;border: 1px solid #ddd; background: #FFF; }
		.res-body .title{font-weight: bold;border-bottom: 1px solid #dad8d8;padding: 20px 0px 12px 30px;}
		.res-body ul {margin: 10px 0px; padding: 0px;}
		.res-body ul li{padding: 12px 0px; line-height: 22px; margin: 0px 18px; border-bottom: 1px dashed #ddd;}
		.res-body ul li:last-child{border: 0;}
		.res-body ul li div{color: #666; float: left;}
		.res-body ul li div:first-child{width: 120px; text-align: right;}
		.res-body ul li:after{content: ''; clear: both;visibility: hidden; display: block;}
		.src-txt{margin: 15px 30px; color: #666; line-height: 26px;}
		.src-file{padding: 8px 10px; color: #666; margin: 20px; background: #eee;}
		.childs ul li{margin: 0px;}
		.childs ul li p{line-height: 28px;}
		.childs ul li p:first-child{margin-bottom: 12px; color: #333;}
		.nodata{padding: 20px 0px 40px 30px; color: #999;}
		.back{padding: 20px 0px 100px 20px; text-align: left;}
		.back a{color: #FFF; padding: 10px 30px;}
		.msg{margin: 25px 40px;}
	</style>
	<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>plugin/layer/layer.js"></script>
	<script type="text/javascript">
		function back(){
			window.history.back();
		}
	</script>
  </head>

  <body>
  	 <div class="pagenav">
     	当前位置：<a href="<%=basePath %>console/indexManager.htm">索引管理</a> >
     	 <a href="javascript:void();" onclick="back();">资源管理</a> > 资源详情 
     </div>
     <c:if test="${!empty res }">
     <div class="res-body box-shadow">
	     <p class="title box-shadow">基本信息</p>
     	<ul>
     		<li>
     			<div>数据ID：</div>
     			<div>${res.id }</div>
     		</li>
     		<li>
     			<div>题名：</div>
     			<div>${res.title }</div>
     		</li>
     		<li>
     			<div>责任者：</div>
     			<div>${res.creator }</div>
     		</li>
     		<li>
     			<div>出版者：</div>
     			<div>${res.publisher }</div>
     		</li>
     		<li>
     			<div>关键词：</div>
     			<div>${res.keyword }</div>
     		</li>
     		
     		<li>
     			<div>学科分类：</div>
     			<div>${res.subjectClass }</div>
     		</li>
     		<li>
     			<div>数据类型：</div>
     			<div>${res.dataType }</div>
     		</li>
     		<li>
     			<div>
     				<c:choose>
     					<c:when test="${res.dataType eq '资源' }">资源库：</c:when>
     					<c:when test="${res.dataType eq '文本' }">文本库：</c:when>
     					<c:when test="${res.dataType eq '资讯' }">所属分类：</c:when>
     					<c:otherwise>${res.dataType }：</c:otherwise>
     				</c:choose>
     			</div>
     			<div>${res.resLibrary }</div>
     		</li>
     		<li>
     			<div>资源类型：</div>
     			<div>${res.resType }</div>
     		</li>
     	</ul>
     </div>
     
     <div class="res-body box-shadow">
	     <p class="title box-shadow">全文内容</p>
   	 	 <c:if test="${empty res.content }"><div class="nodata">该资源没有全文内容...</div></c:if>
     	 <c:if test="${!empty res.content }"><div class="src-txt">${res.content }</div></c:if>
     	 <div class="src-file">全文路径：${res.textPath }</div>
     </div>	
     
     <div class="res-body box-shadow">
	     <p class="title box-shadow">子页目录</p>
     	 <div class="src-txt childs">
     	 	<ul>
     	 	<c:forEach items="${res.pageList }" var="v" varStatus="s">
     	 	<li>
     	 		<p>
	     	 		${v.pageNum }、
	     	 		<c:choose>
     					<c:when test="${v.pageType == 1 }">[PDF]</c:when>
     					<c:when test="${v.pageType == 2 }">[图文]</c:when>
     					<c:otherwise>[${v.pageType }]</c:otherwise>
     				</c:choose>
	     	 		${v.title }
     	 		</p>
     	 		<p>${v.content }</p>
     	 	</li>
     	 	</c:forEach>
     	 	</ul>
     	 </div>
     	 <c:if test="${empty res.pageList }">
     		 <div class="nodata">没有相关数据...</div>
     	 </c:if>
     </div>	
     </c:if>
     
     <c:if test="${!empty message }">
     	<div class="msg">${message }</div>
     </c:if>
     
     
     <div class="back">
     	<a href="javascript:void();" onclick="back();" class="p-btn">返回</a>
     </div>
  </body>
</html>
