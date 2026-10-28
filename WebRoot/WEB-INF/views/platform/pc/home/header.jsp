<%@ page import="java.util.Locale" pageEncoding="UTF-8" %>
<%@ page import="org.springframework.web.servlet.i18n.SessionLocaleResolver" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
    Object homeLocale = session.getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
    String language = homeLocale instanceof Locale ? ((Locale) homeLocale).getLanguage() : "zh";
%>
<!doctype html>
<html lang="<%= "zw".equals(language) ? "bo" : "zh-CN" %>">
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库，典藏敦煌古藏文文献，呈现汉藏交融的千年往事。") %>">
    <title><c:out value="${title}" /></title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="preload" href="<%=basePath%>images/platform/psb/home/home-scene-clean.webp" as="image">
    <link rel="stylesheet" href="<%=basePath%>css/platform/common.css">
    <link rel="stylesheet" href="<%=basePath%>css/platform/index.css?v=logout-20261024-1">
    <link rel="stylesheet" href="<%=basePath%>css/platform/member-login.css?v=frontend-login-20261025-1">
    <script>var basePath = "<%=basePath%>"; var language = "<%=language%>";</script>
    <script src="<%=basePath%>js/platform/lankey/<%=language%>.js" defer></script>
    <script src="<%=basePath%>js/jquery-1.7.2.min.js" defer></script>
    <script src="<%=basePath%>layer/layer.js" defer></script>
    <script src="<%=basePath%>js/platform/common.js?v=frontend-member-dialog-20261025-2&amp;ui=zh-zw-20261025-1" defer></script>
    <%@ include file="/WEB-INF/views/platform/pc/common/advancedSearchInclude.jsp" %>
    <script src="<%=basePath%>js/switchLang.js" defer></script>
    <script src="<%=basePath%>js/platform/index.js?v=home-direct-database-20261017&amp;ui=zh-zw-20261025-1" defer></script>
    <script src="<%=basePath%>js/platform/account-menu.js?v=logout-20261024-1&amp;ui=zh-zw-20261025-1" defer></script>
</head>
<body class="dunhuang-home">
    <a class="dh-skip-link" href="#home-content"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "跳至主要内容") %></a>
    <div class="dh-page">
        <header class="dh-header">
            <a class="dh-brand" href="<%=basePath%>index" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库首页") %>">
                <img class="dh-brand-image" src="<%=basePath%>images/platform/psb/home/brand-logo.png" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库：西藏大学与敦煌研究机构徽标、中藏英文字标") %>" width="537" height="74">
            </a>
            <nav class="dh-nav" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "主导航") %>">
                <a class="dh-nav-link is-current" href="<%=basePath%>index" aria-current="page"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "首 页") %></a>
                <a class="dh-nav-link" href="<%=basePath%>literature"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献库") %></a>
                <a class="dh-nav-link" href="<%=basePath%>database"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "数据库") %></a>
            </nav>
            <div class="dh-header-tools">
                <label class="dh-language"><span class="dh-sr-only"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "切换语言") %></span><select id="dh-language"><option value="zh_CN" lang="zh-CN" translate="no" <%= "zh".equals(language) ? "selected" : "" %>><%= "汉文" %></option><option value="zw_XZ" lang="bo" translate="no" <%= "zw".equals(language) ? "selected" : "" %>>&#3926;&#3964;&#3921;&#3851;&#3937;&#3954;&#3906;</option></select></label>
                <c:choose>
                    <c:when test="${not empty sessionScope.sysUserinfo}">
                        <div class="platform-account-menu" data-account-menu data-account-base="<%=basePath%>">
                            <a class="dh-account" href="<%=basePath%>member/center" title="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "进入个人中心") %>" data-account-trigger aria-haspopup="menu" aria-expanded="false" aria-controls="home-account-dropdown"><img src="<%=basePath%>images/platform/psb/home/account-icon.png" alt=""><span><c:out value="${sessionScope.sysUserinfo.suinLoginName}" /></span></a>
                            <div class="platform-account-dropdown" id="home-account-dropdown" data-account-dropdown role="menu" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "用户操作") %>" hidden>
                                <button type="button" role="menuitem" data-account-logout><fmt:message key="pc.exit"/></button>
                                <p class="platform-account-error" data-account-error role="status" hidden></p>
                            </div>
                        </div>
                    </c:when>
                    <c:otherwise><button class="dh-account" type="button" data-home-login><img src="<%=basePath%>images/platform/psb/home/account-icon.png" alt=""><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "登 录") %></span></button></c:otherwise>
                </c:choose>
            </div>
        </header>
