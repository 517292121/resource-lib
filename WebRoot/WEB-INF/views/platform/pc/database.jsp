<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<% String basePath = request.getContextPath() + "/"; %>
<!doctype html>
<html lang="zh-CN">
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库 · 敦煌文献智慧墙") %>">
    <title><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "敦煌文献智慧墙 - 西藏大学敦煌文献智能数据库") %></title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="stylesheet" href="<%=basePath%>css/platform/database.css?v=screen-20261024-1">
    <script src="<%=basePath%>js/platform/database.js?v=screen-20261024-1&amp;ui=zh-zw-20261025-1" defer></script>
</head>
<body class="dh-database-page">
    <div class="db-screen">
        <header class="db-topbar">
            <div class="db-toolbar">
                <a class="db-back" href="<%=basePath%>index"><span aria-hidden="true">‹</span> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "返回") %></a>
                <button type="button" class="db-refresh" id="db-refresh"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "刷新") %> <svg viewBox="0 0 24 24" aria-hidden="true"><path d="M20 9a8 8 0 0 0-14-3L3 9m0-6v6h6M4 15a8 8 0 0 0 14 3l3-3m0 6v-6h-6"/></svg></button>
                <span class="db-update-state" id="db-update-state" role="status"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "数据实时更新") %></span>
            </div>
            <h1 class="db-screen-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库") %><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "敦煌文献智慧墙") %></span></h1>
            <div class="db-clock" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "当前时间") %>"><time id="db-time">--:--:--</time><time id="db-date">---- -- --</time><span id="db-weekday"></span></div>
        </header>
        <main class="db-dashboard" data-base="<%=basePath%>" aria-busy="true">
            <section class="db-panel db-metrics-panel" aria-labelledby="db-metrics-title">
                <div class="db-panel-heading"><h2 id="db-metrics-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "核心指标统计") %></h2><span class="db-heading-mark" aria-hidden="true"></span></div>
                <div class="db-metrics">
                    <article class="db-metric db-metric-resources">
                        <h3><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献总量") %></h3><p class="db-metric-value"><strong id="db-resource-total">—</strong><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "本") %></span></p>
                        <p class="db-metric-change" id="db-resource-change"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在读取…") %></p>
                    </article>
                    <article class="db-metric db-metric-pages">
                        <h3><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "总页码量") %></h3><p class="db-metric-value"><strong id="db-page-total">—</strong><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "页") %></span></p>
                        <p class="db-metric-change" id="db-page-change"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "实时统计") %></p>
                    </article>
                    <article class="db-metric db-metric-recent">
                        <h3><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "近30天新增") %></h3><p class="db-metric-value"><strong id="db-recent-total">—</strong><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "本") %></span></p>
                        <p class="db-metric-change" id="db-recent-change"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在读取…") %></p>
                    </article>
                    <article class="db-metric db-metric-access" title="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "本月访问量") %>">
                        <h3><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "访问数据") %></h3><p class="db-metric-value"><strong id="db-access-month">—</strong><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "次") %></span></p>
                        <p class="db-metric-change" id="db-access-change"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在读取…") %></p>
                    </article>
                </div>
            </section>
            <section class="db-panel db-hot-panel" aria-labelledby="db-hot-title">
                <div class="db-panel-heading"><h2 id="db-hot-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "热词追踪") %></h2><span class="db-heading-mark" aria-hidden="true"></span></div>
                <div class="db-word-orbit" id="db-hotwords" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "热门检索词") %>"><p class="db-data-message"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在读取热门检索词…") %></p></div>
            </section>
            <section class="db-panel db-trend-panel" aria-labelledby="db-trend-title">
                <div class="db-panel-heading"><h2 id="db-trend-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献增长趋势") %></h2><span class="db-heading-mark" aria-hidden="true"></span></div>
                <div class="db-trend-chart" id="db-trend" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "按入库年份统计的已发布文献数量") %>"><p class="db-data-message"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在读取文献数据…") %></p></div>
            </section>
            <section class="db-panel db-access-panel" aria-labelledby="db-access-title">
                <div class="db-panel-heading"><h2 id="db-access-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "访问数据分析") %></h2><span class="db-heading-mark" aria-hidden="true"></span><button class="db-export-button" type="button" id="db-export" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "导出数据") %></button></div>
                <p class="db-sr-only" id="db-access-source"></p>
                <div class="db-bar-chart" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "月度访问量") %>"><div class="db-y-axis" id="db-access-axis"></div><div class="db-bars" id="db-bars"></div><div class="db-chart-tooltip" id="db-bar-tooltip" role="status"></div></div>
            </section>
            <section class="db-panel db-news-panel" aria-labelledby="db-news-title">
                <div class="db-panel-heading"><h2 id="db-news-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "最新动态") %></h2><span class="db-heading-mark" aria-hidden="true"></span></div>
                <ol class="db-news-list" id="db-news"><li class="db-data-message"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在读取最新动态…") %></li></ol>
            </section>
            <p class="db-sr-only" id="db-statistics-status" role="status"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "正在读取实时统计…") %></p>
        </main>
    </div>
</body>
</html>
