<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<%@ taglib prefix="fn" uri="/WEB-INF/taglibs/fn.tld" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="referrer" content="origin" />
    <meta http-equiv="Cache-Control" content="no-transform" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>数据接口API说明文档</title>
	<link rel="stylesheet" href="<%=basePath %>css/apidoc.css" type="text/css"></link>
</head>
<body>
	<div class="webhead">
		<div id="logoname">
			数据接口API说明文档
		</div>
	</div>
	<c:if test="${counts <= 0 }">
		<div class="blank">该项目暂未创建数据接口信息！</div>
	</c:if>
		
	<c:if test="${counts > 0 }">
	<div class="mainframe">
		<div class="catalog">
			<c:if test="${!empty ty_list }">
			<p>产品通用接口</p>
			<ul>
				<c:forEach items="${ty_list }" var="v">
					<li><a href="#api_${v.sadaId }">${v.sadaName }</a></li>
				</c:forEach>
			</ul>
			</c:if>
		
			<c:if test="${!empty dz_list }">
			<p>项目定制接口</p>
			<ul>
				<c:forEach items="${dz_list }" var="v">
					<li><a href="#api_${v.sadaId }">${v.sadaName }</a></li>
				</c:forEach>
			</ul>
			</c:if>
			
			<c:if test="${!empty qt_list }">
			<p>项目定制接口</p>
			<ul>
				<c:forEach items="${qt_list }" var="v">
					<li><a href="#api_${v.sadaId }">${v.sadaName }</a></li>
				</c:forEach>
			</ul>
			</c:if>
		</div>
		
		
		<c:set var="basepath" value="<%=basePath %>"></c:set>
		<div class="docinfo">
			<div class="contentInner">
				<c:forEach items="${apiList }" var="v" varStatus="s">
					<div class="apidetail" id="api_${v.sadaId }">
						<p class="apiname">${s.index+1 }、${v.sadaName }—${v.sadaType }</p>
						<table cellPadding="0" cellSpacing="1">
							<tr>
								<td class="lab">接口地址：</td>
								<td>${fn:replace(v.sadaReqUrl, "<%=basePath%>", basepath)}</td>
							</tr>
							<tr>
								<td class="lab">接口示例：</td>
								<td><a href="${fn:replace(v.sadaDemoUrl,"<%=basePath%>", basepath)}" target="_blank">${fn:replace(v.sadaDemoUrl, "<%=basePath%>", basepath)}</a></td>
							</tr>
							<tr>
								<td class="lab">请求方式：</td>
								<td>${v.sadaReqType }</td>
							</tr>
							<tr>
								<td class="lab">请求参数：</td>
								<td>${v.sadaReqParams }</td>
							</tr>
							<tr>
								<td class="lab">请求结果：</td>
								<td>${v.sadaResult }</td>
							</tr>
							<tr>
								<td class="lab">研发备注：</td>
								<td>${v.sadaRemark }</td>
							</tr>
						</table>
					</div>
				</c:forEach>
				<div class="endline">
					---------------我已到底线了---------------
				</div>
			</div>
		</div>
		
	</div>
	</c:if>	
</body>
</html>