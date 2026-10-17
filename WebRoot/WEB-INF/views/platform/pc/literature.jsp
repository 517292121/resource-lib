<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    String basePath = request.getContextPath() + "/";
%>
<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="西藏大学敦煌文献智能数据库文献典藏">
    <title>文献库 - 西藏大学敦煌文献智能数据库</title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="preload" href="<%=basePath%>images/platform/psb/literature/page-background.webp" as="image">
    <link rel="stylesheet" href="<%=basePath%>css/platform/literature.css?v=psb-literature-20261017-2">
</head>
<body class="dh-literature-page">
    <a class="lit-skip-link" href="#lit-types-title">跳至文献资源</a>
    <div class="lit-shell">
        <header class="lit-header">
            <a class="lit-brand" href="<%=basePath%>index" aria-label="返回首页">
                <img src="<%=basePath%>images/platform/psb/literature/brand-logo.webp" alt="西藏大学敦煌文献智能数据库" width="537" height="74">
            </a>
            <nav class="lit-nav" aria-label="主导航">
                <a href="<%=basePath%>index">首页</a>
                <a class="is-active" href="<%=basePath%>literature" aria-current="page">文献库</a>
                <a href="<%=basePath%>database">数据库</a>
            </nav>
        </header>

        <main class="lit-main">
            <form class="lit-search" id="lit-search-form" role="search" aria-label="文献检索">
                <div class="lit-search-entry">
                    <label class="lit-search-scope">
                        <span class="lit-sr-only">检索字段</span>
                        <select id="lit-search-field" name="field">
                            <option value="all">全 部</option>
                            <option value="title">卷 名</option>
                            <option value="identifier">卷 号</option>
                            <option value="creator">责任者</option>
                        </select>
                    </label>
                    <label class="lit-sr-only" for="lit-search-key">请输入检索内容</label>
                    <input id="lit-search-key" name="key" type="search" placeholder="请输入您的内容…" autocomplete="off">
                </div>
                <button class="lit-search-submit" type="submit">检 索</button>
                <a class="lit-search-advanced" href="<%=basePath%>resAdv" data-advanced-search aria-haspopup="dialog">高级检索</a>
            </form>

            <section class="lit-catalog" aria-labelledby="lit-types-title">
                <header class="lit-catalog-heading">
                    <h1 id="lit-types-title">文献类型</h1>
                    <div class="lit-view-controls" aria-label="资源显示方式">
                        <button type="button" class="lit-view-toggle is-active" data-view-mode="grid" aria-label="网格视图" aria-pressed="true">
                            <svg viewBox="0 0 28 28" aria-hidden="true"><rect x="2" y="2" width="9" height="9" rx="1"/><rect x="17" y="2" width="9" height="9" rx="1"/><rect x="2" y="17" width="9" height="9" rx="1"/><rect x="17" y="17" width="9" height="9" rx="1"/></svg>
                        </button>
                        <button type="button" class="lit-view-toggle" data-view-mode="list" aria-label="列表视图" aria-pressed="false">
                            <svg viewBox="0 0 30 28" aria-hidden="true"><path d="M2 4h3M11 4h17M2 14h3M11 14h17M2 24h3M11 24h17"/></svg>
                        </button>
                    </div>
                </header>

                <nav class="lit-type-bar" aria-label="文献类型筛选">
                    <div class="lit-type-items" id="lit-type-items" aria-busy="true">
                        <button class="is-selected" type="button" data-type-filter="all" aria-pressed="true">全部文献</button>
                    </div>
                </nav>

                <div class="lit-book-grid" id="lit-book-grid" aria-live="polite" aria-busy="true">
                    <p class="lit-empty">正在读取文献资源…</p>
                </div>

                <nav class="lit-pagination" aria-label="资源分页">
                    <button type="button" data-page="first" disabled>首页</button>
                    <button type="button" data-page="prev" disabled>上一页</button>
                    <span id="lit-page-numbers" class="lit-page-numbers"></span>
                    <button type="button" data-page="next" disabled>下一页</button>
                    <button type="button" data-page="last" disabled>末页</button>
                    <span class="lit-result-count" role="status">正在加载资源…</span>
                </nav>
            </section>
        </main>
    </div>

    <footer class="lit-footer">
        <span>地址：拉萨市藏大东路10号</span><i></i><span>网站举报电话：0891-6405074</span><i></i>
        <span>网站举报邮箱：zdwljb@xzu.edu.cn</span><i></i><span>Copyright © 西藏大学文学院版权所有</span>
    </footer>
    <script>window.literatureBasePath = "<%=basePath%>";</script>
    <script src="<%=basePath%>js/platform/literature.js?v=search-context-20261017-1" defer></script>
    <%@ include file="/WEB-INF/views/platform/pc/common/advancedSearchInclude.jsp" %>
</body>
</html>
