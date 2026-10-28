<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<% String basePath = request.getContextPath() + "/"; %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <title><c:out value="${title}" /> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "- 在线阅读") %></title>
    <link rel="icon" href="<%=basePath%>favicon.ico" />
    <link rel="stylesheet" href="<%=basePath%>css/platform/page-header.css?v=logout-20261024-1" />
    <link rel="stylesheet" href="<%=basePath%>css/platform/resource-text-reader.css?v=preview-hit-20261025-8" />
    <link rel="stylesheet" href="<%=basePath%>css/platform/text-annotations.css?v=admin-actions-20261024-6" />
    <script src="<%=basePath%>js/platform/page-header.js?v=frontend-member-dialog-20261025-2&amp;ui=zh-zw-20261025-1" defer></script>
    <script src="<%=basePath%>js/platform/text-annotations.js?v=preview-hit-20261025-8&amp;ui=zh-zw-20261025-1" defer></script>
    <script src="<%=basePath%>js/platform/resource-text-reader.js?v=preview-hit-20261025-9&amp;ui=zh-zw-20261025-1" defer></script>
</head>
<body class="dh-text-reader">
<a class="reader-skip" href="#reader-book"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "跳至正文") %></a>
<header class="reader-header platform-page-header">
    <a class="reader-brand" href="<%=basePath%>index"><img src="<%=basePath%>images/platform/psb/home/brand-logo.png" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库") %>" width="537" height="74" /></a>
    <nav class="reader-main-nav" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "主导航") %>"><a href="<%=basePath%>index"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "首页") %></a><a class="is-active" href="<%=basePath%>literature"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献库") %></a><a href="<%=basePath%>database"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "数据库") %></a></nav>
    <%@ include file="/WEB-INF/views/platform/pc/common/pageHeaderTools.jsp" %>
</header>
<main class="reader-scene">
    <div class="reader-layout">
        <section class="reader-book" id="reader-book" aria-labelledby="reader-title" data-resource-code="<c:out value='${idcode}'/>" data-base="<%=basePath%>" data-resource-title="<c:out value='${title}'/>" data-initial-chapter="${currentChapter}" data-preview-only="${consolePreview == true}">
            <h1 id="reader-title" class="reader-sr-only"><c:out value="${title}" /> · <c:out value="${chapterTitle}" /></h1>
            <div id="reader-source" hidden><c:forEach items="${textChapters}" var="chapter"><div class="reader-chapter-source" data-path="<c:out value='${chapter.path}'/>" data-hash="<c:out value='${chapter.hash}'/>" data-title="<c:out value='${chapter.title}'/>"><c:out value="${chapter.text}" /></div></c:forEach></div>
            <div class="reader-leaf reader-leaf-left reader-text" id="reader-left" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "左页") %>"></div>
            <div class="reader-leaf reader-leaf-right reader-text" id="reader-right" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "右页") %>"></div>
            <nav class="reader-toolbar" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "阅读工具") %>">
                <c:choose><c:when test="${consolePreview}"><button class="reader-return" type="button" onclick="window.close()"><span aria-hidden="true">≪</span> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "返 回") %></button></c:when><c:otherwise><a class="reader-return" href="<%=basePath%>res/${idcode}"><span aria-hidden="true">≪</span> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "返 回") %></a></c:otherwise></c:choose>
                <button type="button" id="reader-open-chapters" aria-haspopup="dialog"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "目录") %></button>
                <div class="reader-page-controls">
                    <button type="button" id="reader-prev" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "上一页") %></button>
                    <label class="reader-page-jump"><span class="reader-sr-only"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "跳转阅读页") %></span><input id="reader-page-number" type="number" min="1" value="1" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "当前阅读页") %>" /><span id="reader-page-total">/ 1</span></label>
                    <button type="button" id="reader-next" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "下一页") %></button>
                </div>
                <label class="reader-font-control"><span class="reader-sr-only"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正文字号") %></span><select id="reader-font-size" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正文字号") %>"><option value="14">14 <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "像素") %></option><option value="16">16 <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "像素") %></option><option value="18" selected>18 <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "像素") %></option><option value="20">20 <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "像素") %></option><option value="22">22 <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "像素") %></option><option value="24">24 <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "像素") %></option></select></label>
                <button class="reader-zoom" id="reader-zoom-in" type="button" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "放大字号") %>"><svg viewBox="0 0 36 36" aria-hidden="true"><circle cx="14" cy="14" r="10"/><path d="M21 21l10 10M9 14h10M14 9v10"/></svg></button>
                <button class="reader-zoom" id="reader-zoom-out" type="button" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "缩小字号") %>"><svg viewBox="0 0 36 36" aria-hidden="true"><circle cx="14" cy="14" r="10"/><path d="M21 21l10 10M9 14h10"/></svg></button>
            </nav>
            <p class="reader-sr-only" id="reader-status" role="status" aria-live="polite"></p>
        </section>
        <aside class="reader-catalog" aria-labelledby="reader-catalog-title">
            <h2 id="reader-catalog-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献批注") %></h2>
            <div class="reader-annotations-panel">
                <div class="annotation-toolbar" id="annotation-tools" hidden><button type="button" id="annotation-selection" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "选文批注") %></button><button type="button" id="annotation-page"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "整页批注") %></button><a href="<%=basePath%>member/annotations"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "我的批注") %></a></div>
                <p class="annotation-hint" id="annotation-hint"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "批注加载中…") %></p>
                <div id="annotation-list" class="annotation-list" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "当前文献全部批注") %>"></div>
                <p id="annotation-status" class="annotation-status" role="status" aria-live="polite"></p>
            </div>
        </aside>
    </div>
    <dialog class="annotation-dialog reader-chapter-dialog" id="reader-chapter-dialog" aria-labelledby="reader-chapter-dialog-title">
        <div class="annotation-dialog-heading"><h2 id="reader-chapter-dialog-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "章节目录") %></h2><button type="button" data-close-chapters aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "关闭章节目录") %>">×</button></div>
        <nav class="reader-chapters"><c:forEach items="${textChapters}" var="chapter" varStatus="status">
                <c:url value="/res/browse/${idcode}" var="chapterUrl"><c:param name="p" value="${chapter.path}" /></c:url>
                <a href="<c:out value='${chapterUrl}'/>" data-chapter-index="${status.index}" class="${status.index == currentChapter ? 'current' : ''}" <c:if test="${status.index == currentChapter}">aria-current="page"</c:if>><span class="reader-chapter-number" aria-hidden="true">${status.index + 1}</span><span><c:out value="${chapter.title}" /></span></a>
        </c:forEach></nav>
    </dialog>
    <%@ include file="/WEB-INF/views/platform/pc/common/annotationDialogs.jspf" %>
    <noscript><article class="reader-no-script"><h2><c:out value="${chapterTitle}" /></h2><pre><c:out value="${chapterText}" /></pre></article></noscript>
</main>
<footer class="reader-footer"><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "地址：拉萨市藏大东路10号") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "网站举报电话：0891-6405074") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "网站举报邮箱：zdwljb@xzu.edu.cn") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "Copyright © 西藏大学文学院版权所有") %></span></footer>
</body>
</html>
