<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@ page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp"%>
<% String basePath = request.getContextPath() + "/"; %>
<c:set var="detailDefaultCover" value="${pageContext.request.contextPath}/images/platform/psb/literature/default-cover.webp"/>
<c:set var="detailCanRead" value="${userRight >= 4 and (isPdfViewer or not empty browseUrl or (resource.rsdaSourceType > 0 and not empty resource.rsdaFileUrl))}"/>
<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><c:out value="${resource.rsdaTitle}"/> - 西藏大学敦煌文献智能数据库</title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="stylesheet" href="<%=basePath%>css/platform/resource-detail.css?v=psb3-20261017-4">
    <link rel="preload" as="image" href="<%=basePath%>images/platform/psb/resource-detail/header-background.webp">
</head>
<body class="dh-resource-detail" data-base-path="<%=basePath%>" data-resource-id="<c:out value='${resource.rsdaId}'/>" data-subject-id="<c:out value='${resource.rsdaRssuId}'/>" data-member-id="<%=MyRequestUtil.getMemberId(request)%>">
    <a class="rd-skip-link" href="#rd-title">跳至资源详情</a>
    <div class="rd-masthead">
        <header class="rd-header">
            <a class="rd-brand" href="<%=basePath%>index" aria-label="返回首页"><img src="<%=basePath%>images/platform/psb/home/brand-logo.png" alt="西藏大学敦煌文献智能数据库" width="537" height="74"></a>
            <nav class="rd-nav" aria-label="主导航"><a href="<%=basePath%>index">首页</a><a class="is-active" href="<%=basePath%>literature">文献库</a><a href="<%=basePath%>database">数据库</a></nav>
        </header>
        <form class="rd-search" action="<%=basePath%>search" method="get" role="search" aria-label="文献检索">
            <div class="rd-search-entry">
                <label class="rd-search-scope"><span class="rd-sr-only">检索字段</span><select name="attr">
                    <option value="">全 部</option>
                    <option value="rsdaTitle" <c:if test="${param.attr eq 'rsdaTitle' or param.attr eq 'title'}">selected</c:if>>卷 名</option>
                    <option value="identifier" <c:if test="${param.attr eq 'identifier'}">selected</c:if>>卷 号</option>
                    <option value="rsdaCreator" <c:if test="${param.attr eq 'rsdaCreator' or param.attr eq 'creator'}">selected</c:if>>责任者</option>
                    <option value="rsdaKeyword" <c:if test="${param.attr eq 'rsdaKeyword' or param.attr eq 'keyword'}">selected</c:if>>关键词</option>
                    <option value="rsdaPublisher" <c:if test="${param.attr eq 'rsdaPublisher' or param.attr eq 'publisher'}">selected</c:if>>出版者</option>
                </select></label>
                <label class="rd-sr-only" for="rd-search-key">请输入检索内容</label><input type="search" id="rd-search-key" name="key" value="<c:out value='${param.key}'/>" placeholder="请输入您的内容…">
            </div>
            <button class="rd-search-submit" type="submit">检 索</button><a class="rd-search-advanced" href="<%=basePath%>resAdv" data-advanced-search aria-haspopup="dialog">高级检索</a>
        </form>
    </div>
    <main>
        <section class="rd-summary" aria-labelledby="rd-title">
            <div class="rd-record">
                <aside class="rd-cover-panel" aria-label="资源封面和操作">
                    <c:choose><c:when test="${empty resource.rsdaBigimgUrl or resource.rsdaBigimgUrl eq 'null'}"><c:set var="detailCover" value="${detailDefaultCover}"/></c:when><c:otherwise><c:set var="detailCover" value="${resource.rsdaBigimgUrl}"/></c:otherwise></c:choose>
                    <img id="rd-cover" class="rd-cover" src="<c:out value='${detailCover}'/>" data-default-cover="<c:out value='${detailDefaultCover}'/>" onerror="this.onerror=null;this.src=this.getAttribute('data-default-cover');" alt="<c:out value='${resource.rsdaTitle}'/>封面" width="277" height="375">
                    <div class="rd-actions">
                        <c:choose><c:when test="${detailCanRead}"><a class="rd-action rd-read" href="<%=basePath%>res/browse/<c:out value='${idcode}'/>" target="_blank" rel="noopener">在线阅读</a></c:when><c:otherwise><button class="rd-action rd-read" type="button" disabled title="${userRight < 4 ? '当前账号暂无阅读权限' : '该资源暂无可阅读文件'}">在线阅读</button></c:otherwise></c:choose>
                        <%-- 跳转字段预留：待确认后台字段后，将对应值填入各按钮的 data-url；留空时不启用。 --%>
                        <a class="rd-action rd-annotation" id="rd-annotation-link" role="link" aria-disabled="true" title="知识标注入口暂未开放" data-url="" target="_blank" rel="noopener noreferrer">知识标注</a>
                        <a class="rd-action rd-image" id="rd-image-link" role="link" aria-disabled="true" title="图片链接入口暂未开放" data-url="" target="_blank" rel="noopener noreferrer">图片链接</a>
                    </div>
                </aside>
                <article class="rd-metadata" id="baseinfo">
                    <h1 id="rd-title"><c:out value="${resource.rsdaTitle}"/></h1>
                    <dl id="rd-metadata-list" class="rd-metadata-list">
                        <c:if test="${not empty resource.rsdaLanguage}"><div><dt>语 种</dt><dd><c:out value="${resource.rsdaLanguage}"/></dd></div></c:if>
                        <c:if test="${not empty resource.rsdaIdentifier}"><div><dt>卷 号</dt><dd><c:out value="${resource.rsdaIdentifier}"/></dd></div></c:if>
                        <c:if test="${not empty resource.resSubjectType}"><div><dt>学 科</dt><dd><c:out value="${resource.resSubjectType.rstyName}"/></dd></div></c:if>
                        <c:if test="${not empty resource.rsdaDescription}"><div><dt>内 容</dt><dd><c:out value="${resource.rsdaDescription}"/></dd></div></c:if>
                        <c:if test="${not empty resource.rsdaCreator}"><div><dt>责任者</dt><dd><c:out value="${resource.rsdaCreator}"/></dd></div></c:if>
                        <c:if test="${not empty resource.rsdaPublisher}"><div><dt>出版者</dt><dd><c:out value="${resource.rsdaPublisher}"/></dd></div></c:if>
                        <c:if test="${not empty resource.rsdaDate}"><div><dt>日 期</dt><dd><c:out value="${resource.rsdaDate}"/></dd></div></c:if>
                        <div><dt>资源库</dt><dd><c:out value="${resource.resMetaStore.rmstName}"/></dd></div>
                    </dl>
                    <div class="rd-secondary-actions">
                        <c:if test="${not empty downloadUrl}"><a href="<%=SysConfigInfo.FILE_URL_PATH%><c:out value='${downloadUrl}'/>">下载资源</a></c:if>
                        <c:if test="${not empty pdfpath and userRight >= 4}"><c:url var="detailPdfUrl" value="/res/browse/pdf"><c:param name="file" value="${pdfpath}"/><c:param name="title" value="${resource.rsdaTitle}"/></c:url><a href="<c:out value='${detailPdfUrl}'/>" target="_blank" rel="noopener">PDF阅读</a></c:if>
                        <button id="rd-favorite" type="button">加入收藏</button><span class="rd-feedback" id="rd-feedback" role="status"></span>
                    </div>
                </article>
            </div>
        </section>
        <section class="rd-research" aria-labelledby="rd-research-title">
            <header class="rd-research-header">
                <div class="rd-research-heading"><button type="button" class="rd-arrow" data-research-step="-1" aria-label="上一页相关研究" disabled><svg viewBox="0 0 42 42" aria-hidden="true"><path d="M1 21 21 1 41 21 21 41Z M12 21H29 M12 21l5-4 M12 21l5 4"/></svg></button><h2 id="rd-research-title">相关研究</h2><button type="button" class="rd-arrow" data-research-step="1" aria-label="下一页相关研究" disabled><svg viewBox="0 0 42 42" aria-hidden="true"><path d="M1 21 21 1 41 21 21 41Z M12 21H29 M29 21l-5-4 M29 21l-5 4"/></svg></button></div>
            </header>
            <div class="rd-research-body"><div class="rd-research-list" id="rd-research-list" role="region" aria-labelledby="rd-research-title" aria-live="polite" aria-busy="true"><p class="rd-empty">正在读取相关研究…</p></div></div>
            <div class="rd-research-footer"><button class="rd-more" id="rd-research-more" type="button" hidden>更 多</button><span class="rd-page-status" id="rd-page-status" role="status"></span></div>
        </section>
    </main>
    <footer class="rd-footer"><span>地址：拉萨市藏大东路10号</span><i></i><span>网站举报电话：0891-6405074</span><i></i><span>网站举报邮箱：zdwljb@xzu.edu.cn</span><i></i><span>Copyright © 西藏大学文学院版权所有</span></footer>
    <script src="<%=basePath%>js/platform/resource-detail.js?v=search-context-20261017-1" defer></script>
    <%@ include file="/WEB-INF/views/platform/pc/common/advancedSearchInclude.jsp" %>
</body>
</html>
