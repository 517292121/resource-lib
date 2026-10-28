<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<c:set var="title" scope="request" value='<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.text(request, "西藏大学敦煌文献智能数据库") %>' />
<c:set var="homeLibraryUrl" value="${pageContext.request.contextPath}/literature" />
<%-- Keep zero as a real count; missing statistics are unavailable, never design placeholders. --%>
<c:set var="documentTotal" value="${empty homeDocumentCount ? '—' : homeDocumentCount}" />
<c:set var="pageTotal" value="${empty homePageCount ? '—' : homePageCount}" />
<c:set var="visitTotal" value="${empty homeVisitCount ? '—' : homeVisitCount}" />
<c:set var="annotationTotal" value="${empty homeAnnotationCount ? '—' : homeAnnotationCount}" />
<%@ include file="home/header.jsp" %>
        <main class="dh-stage" id="home-content" aria-labelledby="dh-collection-title">
            <div class="dh-collection-plaque"><h1 id="dh-collection-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "敦煌文献典藏") %></h1></div>
            <div class="dh-manuscript">
                <img class="dh-manuscript-image" src="<%=basePath%>images/platform/psb/home/manuscript-table.png" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "木质展台上铺展的敦煌古藏文卷轴") %>" width="976" height="478" fetchpriority="high">
                <a class="dh-library-entry" href="${homeLibraryUrl}"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "进入文献库") %></a>
            </div>
            <section class="dh-statistics" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "资源数据概览") %>">
                <article class="dh-stat-card" data-stat-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "总文献量") %>" data-stat-description="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "敦煌文献资源总量") %>" data-stat-unit="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "篇") %>">
                    <h2><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "总文献量") %></h2><img class="dh-stat-icon" src="<%=basePath%>images/platform/psb/home/stat-documents.png" alt="">
                    <div class="dh-stat-value"><strong><c:out value="${documentTotal}" /></strong><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "篇") %></span></div><a class="dh-stat-view" href="${homeLibraryUrl}" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "查看总文献量，进入文献库") %>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "查 看") %></a>
                </article>
                <article class="dh-stat-card" data-stat-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "总页码量") %>" data-stat-description="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献数字化页面总量") %>" data-stat-unit="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "页") %>">
                    <h2><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "总页码量") %></h2><img class="dh-stat-icon" src="<%=basePath%>images/platform/psb/home/stat-pages.png" alt="">
                    <div class="dh-stat-value"><strong><c:out value="${pageTotal}" /></strong><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "页") %></span></div><a class="dh-stat-view" href="${homeLibraryUrl}" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "查看总页码量，进入文献库") %>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "查 看") %></a>
                </article>
                <article class="dh-stat-card" data-stat-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "总访问量") %>" data-stat-description="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "数据库累计访问量") %>" data-stat-unit="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "次") %>">
                    <h2><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "总访问量") %></h2><img class="dh-stat-icon" src="<%=basePath%>images/platform/psb/home/stat-visits.png" alt="">
                    <div class="dh-stat-value"><strong><c:out value="${visitTotal}" /></strong><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "次") %></span></div><a class="dh-stat-view" href="${homeLibraryUrl}" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "查看总访问量，进入文献库") %>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "查 看") %></a>
                </article>
                <article class="dh-stat-card" data-stat-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "知识标注") %>" data-stat-description="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献知识标注总量") %>" data-stat-unit="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "条") %>">
                    <h2><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "知识标注") %></h2><img class="dh-stat-icon" src="<%=basePath%>images/platform/psb/home/stat-annotations.png" alt="">
                    <div class="dh-stat-value"><strong><c:out value="${annotationTotal}" /></strong><span><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "条") %></span></div><a class="dh-stat-view" href="${homeLibraryUrl}" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "查看知识标注，进入文献库") %>"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "查 看") %></a>
                </article>
            </section>
            <section class="dh-introduction" aria-labelledby="dh-introduction-title">
                <h2 id="dh-introduction-title"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "来自沙海的藏语长歌") %> <span class="dh-title-divider" aria-hidden="true"></span> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "敦煌古藏文文献") %></h2>
                <p><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "纸本墨书，古藏文字随笔尖舒展。纸虽脆，字犹存，斑驳残卷之上，一笔一画，承载雪域文明，诉说汉藏") %><br class="dh-desktop-break"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "交融的千年往事，让青藏高原与中原的对话，重见人间！") %></p>
            </section>
        </main>
<%@ include file="home/footer.jsp" %>
    </div>
</body>
</html>
