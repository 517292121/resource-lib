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
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="西藏大学敦煌文献智能数据库，典藏敦煌古藏文文献，呈现汉藏交融的千年往事。">
    <title><c:out value="${title}" /></title>
    <link rel="icon" href="<%=basePath%>favicon.ico">
    <link rel="preload" href="<%=basePath%>images/platform/psb/home/home-scene-clean.webp" as="image">
    <link rel="stylesheet" href="<%=basePath%>css/platform/common.css">
    <link rel="stylesheet" href="<%=basePath%>css/platform/index.css?v=psb-home-3">
    <script>var basePath = "<%=basePath%>"; var language = "<%=language%>";</script>
    <script src="<%=basePath%>js/platform/lankey/<%=language%>.js" defer></script>
    <script src="<%=basePath%>js/jquery-1.7.2.min.js" defer></script>
    <script src="<%=basePath%>layer/layer.js" defer></script>
    <script src="<%=basePath%>js/platform/common.js" defer></script>
    <%@ include file="/WEB-INF/views/platform/pc/common/advancedSearchInclude.jsp" %>
    <script src="<%=basePath%>js/switchLang.js" defer></script>
    <script src="<%=basePath%>js/platform/index.js?v=home-direct-database-20261017" defer></script>
</head>
<body class="dunhuang-home">
    <a class="dh-skip-link" href="#home-content">跳至主要内容</a>
    <div class="dh-page">
        <header class="dh-header">
            <a class="dh-brand" href="<%=basePath%>index" aria-label="&#35199;&#34255;&#22823;&#23398;&#25958;&#29004;&#25991;&#29486;&#26234;&#33021;&#25968;&#25454;&#24211;&#39318;&#39029;">
                <img class="dh-brand-image" src="<%=basePath%>images/platform/psb/home/brand-logo.png" alt="&#35199;&#34255;&#22823;&#23398;&#25958;&#29004;&#25991;&#29486;&#26234;&#33021;&#25968;&#25454;&#24211;&#65306;&#35199;&#34255;&#22823;&#23398;&#19982;&#25958;&#29004;&#30740;&#31350;&#26426;&#26500;&#24509;&#26631;&#12289;&#20013;&#34255;&#33521;&#25991;&#23383;&#26631;" width="537" height="74">
            </a>
            <nav class="dh-nav" aria-label="&#20027;&#23548;&#33322;">
                <a class="dh-nav-link is-current" href="<%=basePath%>index" aria-current="page">&#39318; &#39029;</a>
                <a class="dh-nav-link" href="<%=basePath%>literature">&#25991;&#29486;&#24211;</a>
                <a class="dh-nav-link" href="<%=basePath%>database">&#25968;&#25454;&#24211;</a>
            </nav>
            <div class="dh-header-tools">
                <label class="dh-language"><span class="dh-sr-only">&#20999;&#25442;&#35821;&#35328;</span><select id="dh-language"><option value="zh_CN" <%= "zh".equals(language) ? "selected" : "" %>>&#27721; &#35821;</option><option value="zw_XZ" <%= "zw".equals(language) ? "selected" : "" %>>&#3926;&#3964;&#3921;&#3851;&#3937;&#3954;&#3906;</option></select></label>
                <c:choose>
                    <c:when test="${not empty sessionScope.sysUserinfo}"><a class="dh-account" href="<%=basePath%>member/center" title="&#36827;&#20837;&#20010;&#20154;&#20013;&#24515;"><img src="<%=basePath%>images/platform/psb/home/account-icon.png" alt=""><span><c:out value="${sessionScope.sysUserinfo.suinLoginName}" /></span></a></c:when>
                    <c:otherwise><button class="dh-account" type="button" data-home-login><img src="<%=basePath%>images/platform/psb/home/account-icon.png" alt=""><span>&#30331; &#24405;</span></button></c:otherwise>
                </c:choose>
            </div>
        </header>
