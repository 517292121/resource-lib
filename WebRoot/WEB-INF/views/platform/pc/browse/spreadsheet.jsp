<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<% String basePath=request.getContextPath()+"/"; %>
<!doctype html>
<html lang="zh-CN">
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <meta charset="UTF-8"><meta name="viewport" content="width=device-width,initial-scale=1">
    <title><c:out value="${title}"/> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "- Excel 在线预览") %></title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="stylesheet" href="<%=basePath%>css/platform/page-header.css?v=logout-20261024-1">
    <link rel="stylesheet" href="<%=basePath%>css/platform/spreadsheet-preview.css?v=20261024-1">
    <script src="<%=basePath%>js/platform/page-header.js?v=frontend-member-dialog-20261025-2&amp;ui=zh-zw-20261025-1" defer></script>
</head>
<body class="excel-preview">
<header class="excel-header platform-page-header">
    <a href="<%=basePath%>index"><img src="<%=basePath%>images/platform/psb/home/brand-logo.png" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库") %>" width="537" height="74"></a>
    <nav aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "主导航") %>"><a href="<%=basePath%>index"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "首页") %></a><a class="is-active" href="<%=basePath%>literature"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献库") %></a><a href="<%=basePath%>database"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "数据库") %></a></nav>
    <%@ include file="/WEB-INF/views/platform/pc/common/pageHeaderTools.jsp" %>
</header>
<main class="excel-main">
    <div class="excel-title"><div><span class="excel-icon" aria-hidden="true">▦</span><h1><c:out value="${title}"/></h1><span class="excel-readonly"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "只读预览") %></span></div><c:choose><c:when test="${consolePreview}"><button class="excel-back" type="button" style="border:0;background:transparent;font:inherit;cursor:pointer" onclick="window.close()"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "关闭") %></button></c:when><c:otherwise><a class="excel-back" href="<%=basePath%>res/<c:out value='${idcode}'/>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "返回资源详情") %></a></c:otherwise></c:choose></div>
    <c:choose><c:when test="${not empty excelError}"><p class="excel-error" role="alert"><c:out value="${excelError}"/></p></c:when><c:otherwise>
        <c:choose><c:when test="${consolePreview}">
            <c:set var="previewUrl" value="/console/data/resource/resourceManager.htm?action=sourceView&sourceId=${consoleSourceId}&type=3"/>
            <c:url var="previewFormUrl" value="/console/data/resource/resourceManager.htm"/>
        </c:when><c:otherwise><c:set var="previewUrl" value="/res/browse/${idcode}"/><c:url var="previewFormUrl" value="${previewUrl}"/></c:otherwise></c:choose>
        <div class="excel-panel">
            <div class="excel-toolbar">
                <div><strong><c:out value="${excel.sheetName}"/></strong><span class="excel-dimensions"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "共") %> ${excel.rowCount} <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "行 ·") %> ${excel.columnCount} <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "列") %></span></div>
                <form action="<c:out value='${previewFormUrl}'/>" method="get" class="excel-paging" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "表格分页") %>">
                    <c:if test="${consolePreview}"><input type="hidden" name="action" value="sourceView"><input type="hidden" name="sourceId" value="${consoleSourceId}"><input type="hidden" name="type" value="3"></c:if>
                    <input type="hidden" name="sheet" value="${excel.sheet}"><input type="hidden" name="columns" value="${excel.columnPage}">
                    <c:choose><c:when test="${excel.page>1}"><c:url var="previousUrl" value="${previewUrl}"><c:param name="sheet" value="${excel.sheet}"/><c:param name="page" value="${excel.page-1}"/><c:param name="columns" value="${excel.columnPage}"/></c:url><a href="<c:out value='${previousUrl}'/>" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "上一页") %>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "上一页") %></a></c:when><c:otherwise><span class="is-disabled"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "上一页") %></span></c:otherwise></c:choose>
                    <label><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "第") %> <input type="number" name="page" min="1" max="${excel.pages}" value="${excel.page}" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "当前表格页码") %>"> / ${excel.pages} <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "页") %></label><button type="submit"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "跳转") %></button>
                    <c:choose><c:when test="${excel.page<excel.pages}"><c:url var="nextUrl" value="${previewUrl}"><c:param name="sheet" value="${excel.sheet}"/><c:param name="page" value="${excel.page+1}"/><c:param name="columns" value="${excel.columnPage}"/></c:url><a href="<c:out value='${nextUrl}'/>" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "下一页") %>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "下一页") %></a></c:when><c:otherwise><span class="is-disabled"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "下一页") %></span></c:otherwise></c:choose>
                </form>
            </div>
            <div class="excel-grid" tabindex="0" role="region" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "Excel 表格，可横向和纵向滚动") %>">
                <c:choose><c:when test="${excel['empty']}"><p class="excel-empty"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "此工作表为空。") %></p></c:when><c:otherwise>
                <table aria-label="${fn:escapeXml(excel.sheetName)}">
                    <colgroup><col style="width:56px"><c:forEach items="${excel.columns}" var="col"><col style="width:${col.width}px"></c:forEach></colgroup>
                    <thead><tr><th class="excel-corner" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "行号") %>"></th><c:forEach items="${excel.columns}" var="col"><th scope="col"><c:out value="${col.name}"/></th></c:forEach></tr></thead>
                    <tbody><c:forEach items="${excel.rows}" var="row"><tr style="height:${row.height}px"><th scope="row">${row.number}</th><c:forEach items="${row.cells}" var="cell"><td rowspan="${cell.rowspan}" colspan="${cell.colspan}" style="<c:out value='${cell.style}'/>"><c:out value="${cell.text}"/></td></c:forEach></tr></c:forEach></tbody>
                </table>
                </c:otherwise></c:choose>
            </div>
            <nav class="excel-sheets" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "工作表切换") %>"><c:forEach items="${excel.sheets}" var="sheet"><c:url var="sheetUrl" value="${previewUrl}"><c:param name="sheet" value="${sheet.index}"/></c:url><a href="<c:out value='${sheetUrl}'/>" <c:if test="${sheet.index==excel.sheet}">aria-current="page"</c:if>><c:out value="${sheet.name}"/></a></c:forEach></nav>
            <div class="excel-status"><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "显示第") %> ${excel.startRow} – ${excel.endRow} <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "行，每页最多 100 行") %><span class="excel-status-divider"> · </span><c:out value="${excel.columnRange}"/> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "列") %></span>
                <c:if test="${excel.columnPages>1}"><div class="excel-column-pages"><c:if test="${excel.columnPage>1}"><c:url var="prevColumns" value="${previewUrl}"><c:param name="sheet" value="${excel.sheet}"/><c:param name="page" value="${excel.page}"/><c:param name="columns" value="${excel.columnPage-1}"/></c:url><a href="<c:out value='${prevColumns}'/>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "前一组列") %></a></c:if><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "列组") %> ${excel.columnPage} / ${excel.columnPages}</span><c:if test="${excel.columnPage<excel.columnPages}"><c:url var="nextColumns" value="${previewUrl}"><c:param name="sheet" value="${excel.sheet}"/><c:param name="page" value="${excel.page}"/><c:param name="columns" value="${excel.columnPage+1}"/></c:url><a href="<c:out value='${nextColumns}'/>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "后一组列") %></a></c:if></div></c:if>
            </div>
        </div>
    </c:otherwise></c:choose>
</main>
<footer class="excel-footer"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "Copyright © 西藏大学文学院版权所有") %></footer>
</body>
</html>
