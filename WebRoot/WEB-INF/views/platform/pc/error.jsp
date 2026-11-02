<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
    String basePath = request.getContextPath() + "/";
    Object resourceError = request.getAttribute("errormsg");
    String resourceErrorMessage = resourceError == null ? "" : resourceError.toString().trim();
    if (resourceErrorMessage.isEmpty()) resourceErrorMessage = "资源不存在或已下架。";
    // 控制器的旧提示文案复用配置中已有的下架译文，中文提示保持原样。
    if (cn.com.adtech.riss2.core.i18n.FrontendMessages.tibetan(request)
            && ("对不起，未找到该资源信息。该资源可能已下架！".equals(resourceErrorMessage)
                || "对不起，未找到该资源信息！".equals(resourceErrorMessage))) {
        resourceErrorMessage = "资源不存在或已下架。";
    }
%>
<!doctype html>
<html lang="zh-CN">
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "提示") %> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "- 西藏大学敦煌文献智能数据库") %></title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="stylesheet" href="<%=basePath%>css/platform/resource-detail.css?v=research-tabs-20261025-3">
    <link rel="stylesheet" href="<%=basePath%>css/platform/resource-error.css?v=20261031-1">
    <link rel="stylesheet" href="<%=basePath%>css/platform/page-header.css?v=logout-20261024-1">
    <script src="<%=basePath%>js/platform/page-header.js?v=frontend-member-dialog-20261025-2&amp;ui=zh-zw-20261025-1" defer></script>
</head>
<body class="dh-resource-detail dh-resource-error">
    <div class="rd-masthead">
        <header class="rd-header platform-page-header">
            <a class="rd-brand" href="<%=basePath%>index" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "返回首页") %>"><img src="<%=basePath%>images/platform/psb/home/brand-logo.png" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库") %>" width="537" height="74"></a>
            <nav class="rd-nav" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "主导航") %>"><a href="<%=basePath%>index"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "首页") %></a><a class="is-active" href="<%=basePath%>literature"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献库") %></a><a href="<%=basePath%>database"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "数据库") %></a></nav>
            <%@ include file="/WEB-INF/views/platform/pc/common/pageHeaderTools.jsp" %>
        </header>
        <form class="rd-search" action="<%=basePath%>search" method="get" role="search" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献检索") %>">
            <div class="rd-search-entry">
                <label class="rd-search-scope"><span class="rd-sr-only"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检索字段") %></span><select name="attr">
                    <option value=""><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "全 部") %></option>
                    <option value="rsdaTitle"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "卷 名") %></option>
                    <option value="identifier"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "卷 号") %></option>
                    <option value="rsdaCreator"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "责任者") %></option>
                    <option value="rsdaKeyword"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "关键词") %></option>
                    <option value="rsdaPublisher"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "出版者") %></option>
                </select></label>
                <label class="rd-sr-only" for="rd-error-search-key"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "请输入检索内容") %></label><input type="search" id="rd-error-search-key" name="key" value="<c:out value='${param.key}'/>" placeholder="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "请输入您的内容…") %>">
            </div>
            <button class="rd-search-submit" type="submit"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检 索") %></button><a class="rd-search-advanced" href="<%=basePath%>resAdv" data-advanced-search aria-haspopup="dialog"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "高级检索") %></a>
        </form>
    </div>
    <main class="rd-error-main">
        <section class="rd-error-card" aria-labelledby="rd-error-title" aria-describedby="rd-error-message">
            <svg class="rd-error-icon" viewBox="0 0 72 72" fill="none" aria-hidden="true"><path d="M36 19c-8-5-17-6-27-3v38c10-3 19-2 27 3 8-5 17-6 27-3V16c-10-3-19-2-27 3Z M36 19v38 M16 25c4-1 8 0 12 2 M16 33c4-1 8 0 12 2 M44 27c4-2 8-3 12-2 M44 35c4-2 8-3 12-2" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"/></svg>
            <h1 id="rd-error-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "提示") %></h1>
            <p id="rd-error-message" role="status"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, resourceErrorMessage) %></p>
            <div class="rd-error-actions">
                <a class="rd-error-library" href="<%=basePath%>literature"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献库") %></a>
                <a class="rd-error-home" href="<%=basePath%>index"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "返回首页") %></a>
            </div>
        </section>
    </main>
    <footer class="rd-footer"><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "地址：拉萨市藏大东路10号") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "网站举报电话：0891-6405074") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "网站举报邮箱：zdwljb@xzu.edu.cn") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "Copyright © 西藏大学文学院版权所有") %></span></footer>
    <%@ include file="/WEB-INF/views/platform/pc/common/advancedSearchInclude.jsp" %>
</body>
</html>
