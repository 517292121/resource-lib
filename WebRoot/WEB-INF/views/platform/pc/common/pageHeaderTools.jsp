<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld" %>
<%@ taglib prefix="fmt" uri="/WEB-INF/taglibs/fmt.tld" %>
<%
    Object toolsLocale = session.getAttribute(org.springframework.web.servlet.i18n.SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
    String toolsLanguage = toolsLocale instanceof java.util.Locale ? ((java.util.Locale) toolsLocale).getLanguage() : "zh";
%>
<div class="platform-header-tools" data-platform-base="<%=basePath%>" data-language="<%=toolsLanguage%>">
    <label class="platform-language"><span class="platform-sr-only"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "切换语言") %></span>
        <select aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "切换语言") %>" data-platform-language>
            <option value="zh_CN" lang="zh-CN" translate="no" <%= "zh".equals(toolsLanguage) ? "selected" : "" %>><%= "汉文" %></option>
            <option value="zw_XZ" lang="bo" translate="no" <%= "zw".equals(toolsLanguage) ? "selected" : "" %>>བོད་ཡིག</option>
        </select>
    </label>
    <c:choose><c:when test="${not empty sessionScope.sysUserinfo}">
        <div class="platform-account-menu" data-account-menu data-account-base="<%=basePath%>">
            <a class="platform-account" href="<%=basePath%>member/center" title="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "进入个人中心") %>" data-account-trigger aria-haspopup="menu" aria-expanded="false" aria-controls="platform-account-dropdown"><img src="<%=basePath%>images/platform/psb/home/account-icon.png" alt=""><span><c:out value="${sessionScope.sysUserinfo.suinLoginName}"/></span></a>
            <div class="platform-account-dropdown" id="platform-account-dropdown" data-account-dropdown role="menu" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "用户操作") %>" hidden>
                <button type="button" role="menuitem" data-account-logout><fmt:message key="pc.exit"/></button>
                <p class="platform-account-error" data-account-error role="status" hidden></p>
            </div>
        </div>
    </c:when><c:otherwise>
        <button class="platform-account" type="button" data-platform-login aria-haspopup="dialog"><img src="<%=basePath%>images/platform/psb/home/account-icon.png" alt=""><span><fmt:message key="pc.login"/></span></button>
    </c:otherwise></c:choose>
    <span class="platform-header-status platform-sr-only" role="status"></span>
</div>
<script src="<%=basePath%>js/platform/account-menu.js?v=logout-20261024-1&amp;ui=zh-zw-20261025-1" defer></script>
