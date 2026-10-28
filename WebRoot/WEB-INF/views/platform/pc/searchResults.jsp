<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    String basePath = request.getContextPath() + "/";
    Object initialKey = request.getAttribute("key");
    String searchKey = initialKey == null ? "" : String.valueOf(initialKey);
%>
<!doctype html>
<html lang="zh-CN">
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检索结果 - 西藏大学敦煌文献智能数据库") %></title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="preload" href="<%=basePath%>images/platform/psb/search-results/page-background.webp" as="image">
    <link rel="stylesheet" href="<%=basePath%>css/platform/search-results.css?v=preview-hit-20261025-8">
    <link rel="stylesheet" href="<%=basePath%>css/platform/page-header.css?v=logout-20261024-1">
    <script src="<%=basePath%>js/platform/page-header.js?v=frontend-member-dialog-20261025-2&amp;ui=zh-zw-20261025-1" defer></script>
</head>
<body class="dh-search-results" data-base="<%=basePath%>" data-key="<%=org.springframework.web.util.HtmlUtils.htmlEscape(searchKey)%>" data-search-mode="${advancedSearch ? 'advanced' : 'simple'}">
    <a class="sr-skip" href="#sr-result-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "跳至检索结果") %></a>
    <div class="sr-shell">
        <header class="sr-header platform-page-header">
            <a class="sr-brand" href="<%=basePath%>index" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "返回首页") %>">
                <img src="<%=basePath%>images/platform/psb/literature/brand-logo.webp" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库") %>" width="537" height="74">
            </a>
            <nav class="sr-nav" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "主导航") %>">
                <a href="<%=basePath%>index"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "首页") %></a>
                <a href="<%=basePath%>literature" class="is-active"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献库") %></a>
                <a href="<%=basePath%>database"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "数据库") %></a>
            </nav>
            <%@ include file="/WEB-INF/views/platform/pc/common/pageHeaderTools.jsp" %>
        </header>
        <main class="sr-main">
            <form class="sr-search" id="sr-search-form" action="<%=basePath%>search" role="search">
                <div class="sr-search-entry">
                    <label class="sr-search-field"><span class="sr-only"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检索字段") %></span>
                        <select id="sr-field" name="attr">
                            <option value=""><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "全 部") %></option>
                            <option value="rsdaTitle"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "题 名") %></option>
                            <option value="identifier"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "卷 号") %></option>
                            <option value="rsdaCreator"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "责任者") %></option>
                            <option value="rsdaKeyword"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "关键词") %></option>
                            <option value="rsdaPublisher"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "出版者") %></option>
                        </select>
                    </label>
                    <label class="sr-only" for="sr-key"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "请输入检索内容") %></label>
                    <input id="sr-key" type="search" name="key" placeholder="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "请输入您的内容…") %>" autocomplete="off">
                </div>
                <button type="submit" class="sr-submit"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检 索") %></button>
                <a href="<%=basePath%>resAdv" class="sr-advanced" data-advanced-search aria-haspopup="dialog"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "高级检索") %></a>
            </form>

            <section class="sr-scroll" aria-labelledby="sr-result-title">
                <h1 id="sr-result-title" class="sr-only"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献检索结果") %></h1>
                <div class="sr-statistics" aria-live="polite">
                    <div class="sr-stat sr-stat-books"><span data-ui-template="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "在 {count} 本文献中出现") %>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "在") %> <strong id="sr-book-count" data-ui-slot="count">—</strong> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "本文献中出现") %></span></div>
                    <div class="sr-stat sr-stat-records" title="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "按当前检索字段和匹配方式累计检索词的实际命中次数，不按文献数或子页数计数") %>"><span data-ui-template="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "共计有 {count} 条记录") %>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "共计有") %> <strong id="sr-record-count" data-ui-slot="count">—</strong> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "条记录") %></span></div>
                </div>
                <div class="sr-tools">
                    <span class="sr-filter-note" id="sr-filter-note"></span>
                </div>
                <div id="sr-results" class="sr-results" aria-live="polite" aria-busy="true">
                    <p class="sr-message" role="status"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在检索文献…") %></p>
                </div>
                <nav class="sr-pagination" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检索结果分页") %>">
                    <button type="button" data-page="first" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "首页") %></button>
                    <button type="button" data-page="prev" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "上一页") %></button>
                    <span id="sr-page-numbers" class="sr-page-numbers"></span>
                    <button type="button" data-page="next" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "下一页") %></button>
                    <button type="button" data-page="last" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "末页") %></button>
                    <span id="sr-page-status" role="status"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在加载…") %></span>
                </nav>
            </section>
        </main>
    </div>
    <footer class="sr-footer">
        <span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "地址：拉萨市藏大东路10号") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "网站举报电话：0891-6405074") %></span><i></i>
        <span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "网站举报邮箱：zdwljb@xzu.edu.cn") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "Copyright © 西藏大学文学院版权所有") %></span>
    </footer>
    <script src="<%=basePath%>js/platform/search-results.js?v=preview-hit-20261025-8&amp;ui=zh-zw-20261025-1" defer></script>
    <%@ include file="/WEB-INF/views/platform/pc/common/advancedSearchInclude.jsp" %>
</body>
</html>
