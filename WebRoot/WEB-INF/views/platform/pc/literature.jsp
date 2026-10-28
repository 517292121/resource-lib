<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    String basePath = request.getContextPath() + "/";
%>
<!doctype html>
<html lang="zh-CN">
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库文献典藏") %>">
    <title><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献库 - 西藏大学敦煌文献智能数据库") %></title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="preload" href="<%=basePath%>images/platform/psb/literature/page-background.webp" as="image">
    <link rel="stylesheet" href="<%=basePath%>css/platform/literature.css?v=type-spacing-20261028-1">
    <link rel="stylesheet" href="<%=basePath%>css/platform/page-header.css?v=logout-20261024-1">
    <script src="<%=basePath%>js/platform/page-header.js?v=frontend-member-dialog-20261025-2&amp;ui=zh-zw-20261025-1" defer></script>
</head>
<body class="dh-literature-page">
    <a class="lit-skip-link" href="#lit-types-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "跳至文献资源") %></a>
    <div class="lit-shell">
        <header class="lit-header platform-page-header">
            <a class="lit-brand" href="<%=basePath%>index" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "返回首页") %>">
                <img src="<%=basePath%>images/platform/psb/literature/brand-logo.webp" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库") %>" width="537" height="74">
            </a>
            <nav class="lit-nav" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "主导航") %>">
                <a href="<%=basePath%>index"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "首页") %></a>
                <a class="is-active" href="<%=basePath%>literature" aria-current="page"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献库") %></a>
                <a href="<%=basePath%>database"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "数据库") %></a>
            </nav>
            <%@ include file="/WEB-INF/views/platform/pc/common/pageHeaderTools.jsp" %>
        </header>

        <main class="lit-main">
            <form class="lit-search" id="lit-search-form" role="search" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献检索") %>">
                <div class="lit-search-entry">
                    <label class="lit-search-scope">
                        <span class="lit-sr-only"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检索字段") %></span>
                        <select id="lit-search-field" name="field">
                            <option value="all"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "全 部") %></option>
                            <option value="title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "卷 名") %></option>
                            <option value="identifier"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "卷 号") %></option>
                            <option value="creator"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "责任者") %></option>
                        </select>
                    </label>
                    <label class="lit-sr-only" for="lit-search-key"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "请输入检索内容") %></label>
                    <input id="lit-search-key" name="key" type="search" placeholder="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "请输入您的内容…") %>" autocomplete="off">
                </div>
                <button class="lit-search-submit" type="submit"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检 索") %></button>
                <a class="lit-search-advanced" href="<%=basePath%>resAdv" data-advanced-search aria-haspopup="dialog"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "高级检索") %></a>
            </form>

            <section class="lit-catalog" aria-labelledby="lit-types-title">
                <header class="lit-catalog-heading">
                    <h1 id="lit-types-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献类型") %></h1>
                    <div class="lit-view-controls" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "资源显示方式") %>">
                        <button type="button" class="lit-view-toggle is-active" data-view-mode="grid" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "网格视图") %>" aria-pressed="true">
                            <svg viewBox="0 0 28 28" aria-hidden="true"><rect x="2" y="2" width="9" height="9" rx="1"/><rect x="17" y="2" width="9" height="9" rx="1"/><rect x="2" y="17" width="9" height="9" rx="1"/><rect x="17" y="17" width="9" height="9" rx="1"/></svg>
                        </button>
                        <button type="button" class="lit-view-toggle" data-view-mode="list" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "列表视图") %>" aria-pressed="false">
                            <svg viewBox="0 0 30 28" aria-hidden="true"><path d="M2 4h3M11 4h17M2 14h3M11 14h17M2 24h3M11 24h17"/></svg>
                        </button>
                    </div>
                </header>

                <nav class="lit-type-bar" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献类型筛选") %>">
                    <div class="lit-type-items" id="lit-type-items" aria-busy="true">
                        <button class="is-selected" type="button" data-type-filter="all" aria-pressed="true"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "全部文献") %></button>
                    </div>
                </nav>

                <div class="lit-book-grid" id="lit-book-grid" aria-live="polite" aria-busy="true">
                    <p class="lit-empty"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在读取文献资源…") %></p>
                </div>

                <nav class="lit-pagination" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "资源分页") %>">
                    <button type="button" data-page="first" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "首页") %></button>
                    <button type="button" data-page="prev" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "上一页") %></button>
                    <span id="lit-page-numbers" class="lit-page-numbers"></span>
                    <button type="button" data-page="next" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "下一页") %></button>
                    <button type="button" data-page="last" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "末页") %></button>
                    <span class="lit-result-count" role="status"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在加载资源…") %></span>
                </nav>
            </section>
        </main>
    </div>

    <footer class="lit-footer">
        <span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "地址：拉萨市藏大东路10号") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "网站举报电话：0891-6405074") %></span><i></i>
        <span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "网站举报邮箱：zdwljb@xzu.edu.cn") %></span><i></i><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "Copyright © 西藏大学文学院版权所有") %></span>
    </footer>
    <script>window.literatureBasePath = "<%=basePath%>";</script>
    <script src="<%=basePath%>js/platform/literature.js?v=type-spacing-20261028-1&amp;ui=zh-zw-20261025-1" defer></script>
    <%@ include file="/WEB-INF/views/platform/pc/common/advancedSearchInclude.jsp" %>
</body>
</html>
