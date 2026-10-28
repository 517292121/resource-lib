<%@ page language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<% String path=request.getContextPath(); %>
<!DOCTYPE html><html lang="zh-CN"><head><meta charset="UTF-8" /><title>新增资源</title>
<link rel="stylesheet" href="<%=path%>/css/form.css" />
<link rel="stylesheet" href="<%=path%>/css/console/resource-association.css?v=20261025-1" />
<script defer="defer" src="<%=path%>/viewsjs/resource/build/resource-association.js?v=20261025-1"></script>
</head><body>
<div class="title">资源维护 — 新增资源</div>
<form class="maintenance-create" action="<%=path%>/console/data/inputSingle.htm" method="get">
<input type="hidden" name="action" value="initData" /><input type="hidden" name="maintenance" value="1" />
<table width="100%" bgcolor="#ECECEC" cellspacing="1"><tbody>
<tr><td width="110">资源类型：</td><td><select id="maintenance-type" name="stype" required="required"><option value="">请选择资源类型</option>
<c:forEach items="${creationTypes}" var="type"><option value="${type.rltyId}"><c:out value="${type.rltyName}" /></option></c:forEach>
</select></td></tr>
<tr><td>所属作业：</td><td>
<div id="maintenance-mission" class="resource-selector" data-resource-selector="true" data-option="mission" data-url="<%=path%>/console/data/inputSingle.htm?action=maintenanceOptions">
<input type="search" required="required" autocomplete="off" placeholder="选择现有作业，可输入作业名称或备注搜索" aria-label="所属作业" />
<button type="button" class="resource-selector-toggle" aria-label="展开所属作业">▼</button><input type="hidden" name="rmin_id" />
<div class="resource-selector-popup" hidden="hidden"></div>
</div><p class="resource-selector-hint">可选择当前有权限的同类型作业，不限制审核状态；沿用该作业的资源库、模板和语种。</p>
</td></tr></tbody></table>
<p class="resource-selector-hint">从资源维护新增的资源，保存后默认审核通过。</p>
<div class="maintenance-actions"><button type="submit">下一步：录入资源</button> <button type="button" onclick="location.href='<%=path%>/console/data/resource/SourceManager.htm?action=managerResourceList'">返回资源维护</button></div>
</form>
<script>document.querySelector('form').addEventListener('submit',function(event){if(!this.elements.rmin_id.value){event.preventDefault();alert('请从下拉列表中选择所属作业');}});</script>
</body></html>
