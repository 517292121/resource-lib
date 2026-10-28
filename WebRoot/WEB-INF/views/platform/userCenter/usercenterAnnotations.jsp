<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<% String basePath = request.getContextPath() + "/"; %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %><meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
    <title>${annotationAdmin ? frontendText['批注管理'] : frontendText['我的批注']} <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "- 敦煌文献智能数据库") %></title>
    <c:if test="${annotationAdmin}"><link rel="stylesheet" href="<%=basePath%>ext/resources/css/ext-all.css"></c:if>
    <c:if test="${not annotationAdmin}"><link rel="stylesheet" href="<%=basePath%>css/platform/page-header.css?v=logout-20261024-1"></c:if>
    <link rel="stylesheet" href="<%=basePath%>css/platform/text-annotations.css?v=admin-actions-20261024-6">
    <c:choose><c:when test="${annotationAdmin}">
    <link rel="stylesheet" href="<%=basePath%>css/console/annotations.css?v=console-theme-20261024-1">
    </c:when><c:otherwise>
    <link rel="stylesheet" href="<%=basePath%>css/platform/member-center.css?v=footer-home-20261024-3">
    <script src="<%=basePath%>js/platform/page-header.js?v=frontend-member-dialog-20261025-2&amp;ui=zh-zw-20261025-1" defer></script>
    </c:otherwise></c:choose>
    <script src="<%=basePath%>js/platform/text-annotations.js?v=admin-actions-20261024-6&amp;ui=zh-zw-20261025-1" defer></script>
</head>
<body class="annotation-records-page ${annotationAdmin ? 'annotation-console' : 'member-center-page'}">
<c:if test="${not annotationAdmin}">
    <%@ include file="memberCenterHeader.jspf" %>
</c:if>
<main class="annotation-records-layout">
    <c:if test="${not annotationAdmin}"><c:set var="memberSection" value="annotations" /><%@ include file="memberCenterNav.jspf" %></c:if>
    <section class="annotation-records-main" id="annotation-records" data-base="<%=basePath%>" data-admin="${annotationAdmin}" data-endpoint="<%=basePath%>${annotationAdmin ? 'console' : 'member'}/annotations">
        <div class="annotation-records-heading"><h1>${annotationAdmin ? frontendText['批注管理'] : frontendText['我的批注']}</h1><span id="annotation-record-count"></span></div>
        <form id="annotation-search-form" class="annotation-record-search annotation-review-search">
            <label for="annotation-record-search"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检索批注") %><input id="annotation-record-search" type="search" maxlength="100" placeholder="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "资源题名、章节或批注内容") %>"></label>
            <label for="annotation-audit-filter"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "审核状态") %><select id="annotation-audit-filter"><option value=""><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "全部状态") %></option><option value="PENDING" <c:if test="${annotationAdmin}">selected</c:if>><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "待审核") %></option><option value="APPROVED"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "已通过") %></option><option value="REJECTED"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "未通过") %></option></select></label>
            <button type="submit"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检索") %></button>
        </form>
        <p class="annotation-review-hint">${annotationAdmin ? frontendText['批注审核通过后才会在文献页显示；修改批注内容后需要重新审核。'] : frontendText['新批注及修改后的批注默认待审核，审核通过后会显示在文献页。']}</p>
        <p class="annotation-status" id="annotation-status" role="status" aria-live="polite"></p>
        <div id="annotation-list" class="annotation-list" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "批注记录") %>"></div>
        <nav class="annotation-record-pagination" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "批注记录分页") %>"><button type="button" id="annotation-record-prev" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "上一页") %></button><span id="annotation-record-page">1 / 1</span><button type="button" id="annotation-record-next" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "下一页") %></button></nav>
    </section>
</main>
<%@ include file="/WEB-INF/views/platform/pc/common/annotationDialogs.jspf" %>
<c:if test="${annotationAdmin}">
<dialog id="annotation-review" class="annotation-dialog" aria-labelledby="annotation-review-title">
    <form id="annotation-review-form">
        <div class="annotation-dialog-heading"><h2 id="annotation-review-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "审核批注") %></h2><button type="button" data-close-review aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "关闭审核弹窗") %>">×</button></div>
        <p id="annotation-review-summary"></p>
        <label for="annotation-review-remark"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "审核说明（选填）") %></label><textarea id="annotation-review-remark" rows="3" maxlength="1000" placeholder="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "审核说明会在用户的“我的批注”中显示") %>"></textarea>
        <p id="annotation-review-error" class="annotation-error" role="alert"></p>
        <div class="annotation-dialog-actions"><button type="button" data-close-review><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "取消") %></button><button type="submit" id="annotation-review-submit"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "确认审核") %></button></div>
    </form>
</dialog>
</c:if>
<c:if test="${not annotationAdmin}"><footer class="annotation-records-footer"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "Copyright © 西藏大学文学院版权所有") %></footer></c:if>
</body></html>
