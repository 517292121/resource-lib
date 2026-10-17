<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%
    String basePath = request.getContextPath() + "/";
    Object initialKey = request.getAttribute("key");
    String searchKey = initialKey == null ? "" : String.valueOf(initialKey);
%>
<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>检索结果 - 西藏大学敦煌文献智能数据库</title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="preload" href="<%=basePath%>images/platform/psb/search-results/page-background.webp" as="image">
    <link rel="stylesheet" href="<%=basePath%>css/platform/search-results.css?v=psb5-20261017-4">
</head>
<body class="dh-search-results" data-base="<%=basePath%>" data-key="<%=org.springframework.web.util.HtmlUtils.htmlEscape(searchKey)%>">
    <a class="sr-skip" href="#sr-result-title">跳至检索结果</a>
    <div class="sr-shell">
        <header class="sr-header">
            <a class="sr-brand" href="<%=basePath%>index" aria-label="返回首页">
                <img src="<%=basePath%>images/platform/psb/literature/brand-logo.webp" alt="西藏大学敦煌文献智能数据库" width="537" height="74">
            </a>
            <nav class="sr-nav" aria-label="主导航">
                <a href="<%=basePath%>index">首页</a>
                <a href="<%=basePath%>literature" class="is-active">文献库</a>
                <a href="<%=basePath%>database">数据库</a>
            </nav>
        </header>
        <main class="sr-main">
            <form class="sr-search" id="sr-search-form" action="<%=basePath%>search" role="search">
                <div class="sr-search-entry">
                    <label class="sr-search-field"><span class="sr-only">检索字段</span>
                        <select id="sr-field" name="attr">
                            <option value="">全 部</option>
                            <option value="rsdaTitle">题 名</option>
                            <option value="identifier">卷 号</option>
                            <option value="rsdaCreator">责任者</option>
                            <option value="rsdaKeyword">关键词</option>
                            <option value="rsdaPublisher">出版者</option>
                        </select>
                    </label>
                    <label class="sr-only" for="sr-key">请输入检索内容</label>
                    <input id="sr-key" type="search" name="key" placeholder="请输入您的内容…" autocomplete="off">
                </div>
                <button type="submit" class="sr-submit">检 索</button>
                <a href="<%=basePath%>resAdv" class="sr-advanced" data-advanced-search aria-haspopup="dialog">高级检索</a>
            </form>

            <section class="sr-scroll" aria-labelledby="sr-result-title">
                <h1 id="sr-result-title" class="sr-only">文献检索结果</h1>
                <div class="sr-statistics" aria-live="polite">
                    <div class="sr-stat sr-stat-books"><span>在 <strong id="sr-book-count">—</strong> 本文献中出现</span></div>
                    <div class="sr-stat sr-stat-records" title="按当前检索字段和匹配方式累计检索词的实际命中次数，不按文献数或子页数计数"><span>共计有 <strong id="sr-record-count">—</strong> 条记录</span></div>
                </div>
                <div class="sr-tools">
                    <span class="sr-filter-note" id="sr-filter-note"></span>
                </div>
                <div id="sr-results" class="sr-results" aria-live="polite" aria-busy="true">
                    <p class="sr-message" role="status">正在检索文献…</p>
                </div>
                <nav class="sr-pagination" aria-label="检索结果分页">
                    <button type="button" data-page="first" disabled>首页</button>
                    <button type="button" data-page="prev" disabled>上一页</button>
                    <span id="sr-page-numbers" class="sr-page-numbers"></span>
                    <button type="button" data-page="next" disabled>下一页</button>
                    <button type="button" data-page="last" disabled>末页</button>
                    <span id="sr-page-status" role="status">正在加载…</span>
                </nav>
            </section>
        </main>
    </div>
    <footer class="sr-footer">
        <span>地址：拉萨市藏大东路10号</span><i></i><span>网站举报电话：0891-6405074</span><i></i>
        <span>网站举报邮箱：zdwljb@xzu.edu.cn</span><i></i><span>Copyright © 西藏大学文学院版权所有</span>
    </footer>
    <script src="<%=basePath%>js/platform/search-results.js?v=search-context-20261017-1" defer></script>
    <%@ include file="/WEB-INF/views/platform/pc/common/advancedSearchInclude.jsp" %>
</body>
</html>
