<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<c:set var="memberSection" value="favorites" />
<%@ include file="memberCenterStart.jspf" %>
<main class="annotation-records-layout">
    <%@ include file="memberCenterNav.jspf" %>
    <section class="annotation-records-main">
        <div class="annotation-records-heading"><h1><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "我的收藏") %></h1><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "共") %> ${pagination.totalResults} <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "条收藏") %></span></div>
        <form class="annotation-record-search" action="<%=basePath%>user/favorite.htm" method="get">
            <input type="hidden" name="action" value="initScFavoriteView" /><input type="hidden" name="resourceType" value="${resourceType}" />
            <label for="member-favorite-search"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检索收藏") %><input id="member-favorite-search" name="search" type="search" maxlength="100" value="<c:out value='${search}'/>" placeholder="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "资源题名") %>" /></label><button type="submit"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "检索") %></button>
        </form>
        <div class="annotation-list" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "收藏记录") %>">
            <c:choose><c:when test="${not empty favoriteList}"><c:forEach var="fav" items="${favoriteList}">
                <c:url value="/res/${fav[1]}" var="favoriteResourceUrl" />
                <article class="annotation-record">
                    <div class="member-favorite-title"><h2><a href="<c:out value='${favoriteResourceUrl}'/>"><c:out value="${fav[2]}" /></a></h2><c:if test="${not empty fav[4]}"><span class="member-resource-type"><c:out value="${fav[4]}" /></span></c:if></div>
                    <p class="annotation-meta"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "收藏时间：") %><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${fav[3]}" /></p>
                    <div class="member-favorite-actions"><a class="annotation-view" href="<c:out value='${favoriteResourceUrl}'/>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "查看资源") %></a>
                        <form action="<%=basePath%>user/favorite.htm?action=delScFavorite" method="post" data-confirm="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "确定取消收藏该资源吗？") %>" onsubmit="return confirm(this.dataset.confirm);"><input type="hidden" name="scfId" value="${fav[0]}" /><button class="member-text-button" type="submit"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "取消收藏") %></button></form>
                    </div>
                </article>
            </c:forEach></c:when><c:otherwise><p class="annotation-empty">${empty search ? frontendText['暂无收藏记录'] : frontendText['没有找到相关收藏']}</p></c:otherwise></c:choose>
        </div>
        <c:url value="/user/favorite.htm" var="favoritePrevious"><c:param name="action" value="initScFavoriteView" /><c:param name="resourceType" value="${resourceType}" /><c:param name="search" value="${search}" /><c:param name="currentPage" value="${pagination.currentPage-1}" /></c:url>
        <c:url value="/user/favorite.htm" var="favoriteNext"><c:param name="action" value="initScFavoriteView" /><c:param name="resourceType" value="${resourceType}" /><c:param name="search" value="${search}" /><c:param name="currentPage" value="${pagination.currentPage+1}" /></c:url>
        <nav class="annotation-record-pagination" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "收藏记录分页") %>">
            <c:choose><c:when test="${pagination.currentPage gt 0}"><a href="<c:out value='${favoritePrevious}'/>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "上一页") %></a></c:when><c:otherwise><button type="button" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "上一页") %></button></c:otherwise></c:choose>
            <span>${pagination.totalPages gt 0 ? pagination.currentPage+1 : 1} / ${pagination.totalPages gt 0 ? pagination.totalPages : 1}</span>
            <c:choose><c:when test="${pagination.currentPage+1 lt pagination.totalPages}"><a href="<c:out value='${favoriteNext}'/>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "下一页") %></a></c:when><c:otherwise><button type="button" disabled><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "下一页") %></button></c:otherwise></c:choose>
        </nav>
    </section>
</main>
<%@ include file="memberCenterEnd.jspf" %>
