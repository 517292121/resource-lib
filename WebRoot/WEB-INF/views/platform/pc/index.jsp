<%@ page language="java" pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<c:set var="title" scope="request" value="西藏大学敦煌文献智能数据库" />
<c:set var="homeLibraryUrl" value="${pageContext.request.contextPath}/literature" />
<%-- Presentation values from 1首页.psb; optional controller attributes override them. --%>
<c:set var="documentTotal" value="${empty homeDocumentCount ? 1920 : homeDocumentCount}" />
<c:set var="pageTotal" value="${empty homePageCount ? 5218 : homePageCount}" />
<c:set var="visitTotal" value="${empty homeVisitCount ? 1259 : homeVisitCount}" />
<c:set var="annotationTotal" value="${empty homeAnnotationCount ? 51289 : homeAnnotationCount}" />
<%@ include file="home/header.jsp" %>
        <main class="dh-stage" id="home-content" aria-labelledby="dh-collection-title">
            <div class="dh-collection-plaque"><h1 id="dh-collection-title">敦煌文献典藏</h1></div>
            <div class="dh-manuscript">
                <img class="dh-manuscript-image" src="<%=basePath%>images/platform/psb/home/manuscript-table.png" alt="木质展台上铺展的敦煌古藏文卷轴" width="976" height="478" fetchpriority="high">
                <a class="dh-library-entry" href="${homeLibraryUrl}">进入文献库</a>
            </div>
            <section class="dh-statistics" aria-label="资源数据概览">
                <article class="dh-stat-card" data-stat-label="总文献量" data-stat-description="敦煌文献资源总量" data-stat-unit="篇">
                    <h2>总文献量</h2><img class="dh-stat-icon" src="<%=basePath%>images/platform/psb/home/stat-documents.png" alt="">
                    <div class="dh-stat-value"><strong><c:out value="${documentTotal}" /></strong><span>篇</span></div><a class="dh-stat-view" href="${homeLibraryUrl}" aria-label="查看总文献量，进入文献库">查 看</a>
                </article>
                <article class="dh-stat-card" data-stat-label="总页码量" data-stat-description="文献数字化页面总量" data-stat-unit="页">
                    <h2>总页码量</h2><img class="dh-stat-icon" src="<%=basePath%>images/platform/psb/home/stat-pages.png" alt="">
                    <div class="dh-stat-value"><strong><c:out value="${pageTotal}" /></strong><span>页</span></div><a class="dh-stat-view" href="${homeLibraryUrl}" aria-label="查看总页码量，进入文献库">查 看</a>
                </article>
                <article class="dh-stat-card" data-stat-label="总访问量" data-stat-description="数据库累计访问量" data-stat-unit="次">
                    <h2>总访问量</h2><img class="dh-stat-icon" src="<%=basePath%>images/platform/psb/home/stat-visits.png" alt="">
                    <div class="dh-stat-value"><strong><c:out value="${visitTotal}" /></strong><span>次</span></div><a class="dh-stat-view" href="${homeLibraryUrl}" aria-label="查看总访问量，进入文献库">查 看</a>
                </article>
                <article class="dh-stat-card" data-stat-label="知识标注" data-stat-description="文献知识标注总量" data-stat-unit="条">
                    <h2>知识标注</h2><img class="dh-stat-icon" src="<%=basePath%>images/platform/psb/home/stat-annotations.png" alt="">
                    <div class="dh-stat-value"><strong><c:out value="${annotationTotal}" /></strong><span>条</span></div><a class="dh-stat-view" href="${homeLibraryUrl}" aria-label="查看知识标注，进入文献库">查 看</a>
                </article>
            </section>
            <section class="dh-introduction" aria-labelledby="dh-introduction-title">
                <h2 id="dh-introduction-title">来自沙海的藏语长歌 <span class="dh-title-divider" aria-hidden="true"></span> 敦煌古藏文文献</h2>
                <p>纸本墨书，古藏文字随笔尖舒展。纸虽脆，字犹存，斑驳残卷之上，一笔一画，承载雪域文明，诉说汉藏<br class="dh-desktop-break">交融的千年往事，让青藏高原与中原的对话，重见人间！</p>
            </section>
        </main>
<%@ include file="home/footer.jsp" %>
    </div>
</body>
</html>
