<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.Locale" %>
<%@ page import="org.springframework.web.servlet.i18n.SessionLocaleResolver" %>
<%
    String basePath = request.getContextPath() + "/";
    Object databaseLocale = session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
    String databaseLanguage = databaseLocale instanceof Locale ? ((Locale) databaseLocale).getLanguage() : "zh";
%>
<!doctype html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="西藏大学敦煌文献智能数据库数据分析">
    <title>数据库数据分析 - 西藏大学敦煌文献智能数据库</title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="stylesheet" href="<%=basePath%>css/platform/database.css?v=psb-database-2">
</head>
<body class="dh-database-page">
    <header class="db-header">
        <a class="db-brand" href="<%=basePath%>index" aria-label="返回敦煌文献智能数据库首页">
            <img src="<%=basePath%>images/platform/psb/home/brand-logo.png" alt="西藏大学敦煌文献智能数据库" width="537" height="74">
        </a>
        <nav class="db-nav" aria-label="主导航">
            <a href="<%=basePath%>index">首页</a>
            <a href="<%=basePath%>literature">文献库</a>
            <a class="is-active" aria-current="page" href="<%=basePath%>database">数据库</a>
        </nav>
    </header>

    <main class="db-dashboard">
        <section class="db-top-row" aria-label="文献趋势与热门追踪">
            <article class="db-panel db-trend-panel">
                <div class="db-panel-heading"><h1>文献增长趋势（年）</h1><span class="db-heading-mark" aria-hidden="true"></span></div>
                <div class="db-trend-chart" role="img" aria-label="2016年至2026年文献增长趋势示意图">
                    <svg viewBox="0 0 780 280" preserveAspectRatio="none" aria-hidden="true">
                        <defs><linearGradient id="trendFill" x1="0" x2="0" y1="0" y2="1"><stop offset="0" stop-color="#91ad83" stop-opacity=".78"/><stop offset="1" stop-color="#91ad83" stop-opacity=".58"/></linearGradient></defs>
                        <g class="db-grid-lines"><path d="M54 28H770M54 74H770M54 120H770M54 166H770M54 212H770M54 258H770"/></g>
                        <g class="db-axis-labels"><text x="18" y="262">0</text><text x="7" y="216">100</text><text x="7" y="170">200</text><text x="7" y="124">300</text><text x="7" y="78">400</text><text x="7" y="32">500</text></g>
                        <path class="db-area" d="M54 228 C82 187 100 166 126 169 S166 152 188 127 S226 69 257 72 S300 69 326 57 S372 34 405 22 S456 34 483 73 S526 134 557 154 S594 177 623 172 S670 166 700 169 S747 154 770 136 L770 258 L54 258Z"/>
                        <path class="db-line" d="M54 228 C82 187 100 166 126 169 S166 152 188 127 S226 69 257 72 S300 69 326 57 S372 34 405 22 S456 34 483 73 S526 134 557 154 S594 177 623 172 S670 166 700 169 S747 154 770 136"/>
                        <g class="db-year-labels"><text x="54" y="278">2016年</text><text x="126" y="278">2017年</text><text x="198" y="278">2018年</text><text x="270" y="278">2019年</text><text x="342" y="278">2020年</text><text x="414" y="278">2021年</text><text x="486" y="278">2022年</text><text x="558" y="278">2023年</text><text x="630" y="278">2024年</text><text x="702" y="278">2025年</text><text x="770" y="278" text-anchor="end">2026年</text></g>
                    </svg>
                    <div class="db-chart-tooltip" aria-live="polite"></div>
                </div>
            </article>

            <article class="db-panel db-hot-panel">
                <div class="db-panel-heading"><h2>热词追踪</h2><span class="db-heading-mark" aria-hidden="true"></span></div>
                <div class="db-word-orbit" role="img" aria-label="敦煌古籍研究相关热门词语">
                    <div class="db-orbit db-orbit-outer"></div><div class="db-orbit db-orbit-inner"></div>
                    <span class="db-word db-word-center">敦煌古籍</span>
                    <span class="db-word db-word-1">藏经</span><span class="db-word db-word-2">文献库</span>
                    <span class="db-word db-word-3">环境保护</span><span class="db-word db-word-4">壁画</span>
                    <span class="db-word db-word-5">论文</span><span class="db-word db-word-6">艺术</span>
                    <span class="db-word db-word-7">天文历法</span><span class="db-word db-word-8">敦煌文学</span>
                    <span class="db-word db-word-9">写本</span><span class="db-word db-word-10">古藏文</span>
                    <span class="db-word db-word-11">保护研究</span><span class="db-word db-word-12">丝路文化</span>
                </div>
            </article>
        </section>

        <section class="db-panel db-access-panel" aria-labelledby="db-access-title">
            <div class="db-panel-heading db-access-heading">
                <h2 id="db-access-title">数据访问分析</h2>
                <button class="db-export-button" type="button" id="db-export">导出数据</button>
            </div>
            <div class="db-bar-chart" role="img" aria-label="2026年月度数据访问量示意图">
                <div class="db-y-axis"><span>600</span><span>500</span><span>400</span><span>300</span><span>200</span><span>100</span><span>0</span></div>
                <div class="db-bars" id="db-bars">
                    <div class="db-bar-item" data-month="2026年1月" data-count="190"><i style="--bar:32%"></i><span>2026年1月</span></div>
                    <div class="db-bar-item" data-month="2026年2月" data-count="360"><i style="--bar:60%"></i><span>2026年2月</span></div>
                    <div class="db-bar-item" data-month="2026年3月" data-count="360"><i style="--bar:60%"></i><span>2026年3月</span></div>
                    <div class="db-bar-item" data-month="2026年4月" data-count="360"><i style="--bar:60%"></i><span>2026年4月</span></div>
                    <div class="db-bar-item" data-month="2026年5月" data-count="510"><i style="--bar:85%"></i><span>2026年5月</span></div>
                    <div class="db-bar-item" data-month="2026年6月" data-count="300"><i style="--bar:50%"></i><span>2026年6月</span></div>
                    <div class="db-bar-item" data-month="2026年7月" data-count="390"><i style="--bar:65%"></i><span>2026年7月</span></div>
                    <div class="db-bar-item" data-month="2026年8月" data-count="410"><i style="--bar:68%"></i><span>2026年8月</span></div>
                    <div class="db-bar-item" data-month="2026年9月" data-count="410"><i style="--bar:68%"></i><span>2026年9月</span></div>
                    <div class="db-bar-item" data-month="2026年10月" data-count="200"><i style="--bar:33%"></i><span>2026年10月</span></div>
                    <div class="db-bar-item" data-month="2026年11月" data-count="200"><i style="--bar:33%"></i><span>2026年11月</span></div>
                    <div class="db-bar-item" data-month="2026年12月" data-count="340"><i style="--bar:57%"></i><span>2026年12月</span></div>
                </div>
                <div class="db-chart-tooltip" id="db-bar-tooltip" aria-live="polite"></div>
            </div>
        </section>

        <section class="db-panel db-metrics-panel" aria-labelledby="db-metrics-title">
            <div class="db-panel-heading"><h2 id="db-metrics-title">核心指标统计</h2><span class="db-heading-mark" aria-hidden="true"></span></div>
            <div class="db-metrics">
                <article class="db-metric" style="--gauge-color:#9d625e;--gauge-value:78%"><div class="db-gauge"><span>75</span></div><p>文献总量（本）</p></article>
                <article class="db-metric" style="--gauge-color:#6895a0;--gauge-value:82%"><div class="db-gauge"><span>136</span></div><p>总页码量（页）</p></article>
                <article class="db-metric" style="--gauge-color:#a8a15e;--gauge-value:68%"><div class="db-gauge"><span>80</span></div><p>近30天新增（页）</p></article>
                <article class="db-metric" style="--gauge-color:#71917b;--gauge-value:88%"><div class="db-gauge"><span>158</span></div><p>数据访问（月）</p></article>
            </div>
        </section>
    </main>

    <footer class="db-footer">
        <span>地址：拉萨市藏大南路10号</span><i></i><span>网站学管电话：0891 6405074</span><i></i>
        <span>网站学管邮箱：ZDWLJB@XZU.EDU.CN</span><i></i><span>Copyright © 西藏大学文学院版权所有</span>
    </footer>
    <script src="<%=basePath%>js/platform/database.js?v=psb-database-1" defer></script>
</body>
</html>
