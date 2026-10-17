<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
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
    <title>数据接口API管理</title>
	<link rel="stylesheet" href="<%=basePath %>css/apidoc.css" type="text/css"></link>
	<script type="text/javascript">
		function del(id){
			if(window.confirm("你确定要删除该接口吗？")){
				window.location.href = "<%=basePath %>dataapi/del/" + id;
			}
		}
	</script>
</head>
<body>
	<div class="webhead">
		<div id="logoname">
			数据接口API管理
		</div>
	</div>
	<div class="mainframe">
		<div class="toolbar">
			<a href="<%=basePath %>dataapi/add">新增接口</a>
			<a href="<%=basePath %>dataapi/doc">文档预览</a>
			<p class="clearfix" />
		</div>
		<c:if test="${!empty ty_list }">
			<div class="apitable">
			<p class="apitype">产品通用接口</p>
			<table cellPadding="0" cellSpacing="1">
				<tr>
					<th style="width: 30px"></th>
					<th class="apiname">接口名称</th>
					<th>请求地址</th>
					<th class="edit">操作</th>
				</tr>
				<c:forEach items="${ty_list }" var="v" varStatus="s">
				<tr>
					<td>${s.index+1 }</td>
					<td class="apiname">${v.sadaName }</td>
					<td>${v.sadaReqUrl }</td>
					<td class="edit">
						<a href="<%=basePath %>dataapi/edit/${v.sadaId}">编辑</a>
						<a href="javascript:del(${v.sadaId})">删除</a>
					</td>
				</tr>
				</c:forEach>
			</table>
			</div>
		</c:if>
		
		
		<c:if test="${!empty dz_list }">
			<div class="apitable">
			<p class="apitype">项目定制接口</p>
			<table cellPadding="0" cellSpacing="1">
				<tr>
					<th style="width: 30px"></th>
					<th class="apiname">接口名称</th>
					<th>请求地址</th>
					<th class="edit">操作</th>
				</tr>
				<c:forEach items="${dz_list }" var="v" varStatus="s">
				<tr>
					<td>${s.index+1 }</td>
					<td class="apiname">${v.sadaName }</td>
					<td>${v.sadaReqUrl }</td>
					<td class="edit">
						<a href="<%=basePath %>dataapi/edit/${v.sadaId}">编辑</a>
						<a href="javascript:del(${v.sadaId})">删除</a>
					</td>
				</tr>
				</c:forEach>
			</table>
			</div>
		</c:if>
		
		<c:if test="${!empty qt_list }">
			<div class="apitable">
			<p class="apitype">其它接口</p>
			<table cellPadding="0" cellSpacing="1">
				<tr>
					<th style="width: 30px"></th>
					<th class="apiname">接口名称</th>
					<th>请求地址</th>
					<th class="edit">操作</th>
				</tr>
				<c:forEach items="${qt_list }" var="v" varStatus="s">
				<tr>
					<td>${s.index+1 }</td>
					<td class="apiname">${v.sadaName }</td>
					<td>${v.sadaReqUrl }</td>
					<td class="edit">
						<a href="<%=basePath %>dataapi/edit/${v.sadaId}">编辑</a>
						<a href="javascript:del(${v.sadaId})">删除</a>
					</td>
				</tr>
				</c:forEach>
			</table>
			</div>
		</c:if>
		
		<c:if test="${counts < 1 }">
			<div style="margin: 30px; color: #808080; font-size: 16px;">该项目没有创建接口信息...</div>
		</c:if>
	</div>
	
	${message }
</body>
</html>