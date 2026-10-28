<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<link rel="stylesheet" href="<%=basePath%>css/platform/text-annotations.css?v=admin-actions-20261024-6" />
<c:set var="memberSection" value="${fn:contains(pageContext.request.requestURI, '/favorite.htm') ? 'favorites' : (fn:contains(pageContext.request.requestURI, '/annotations') ? 'annotations' : 'settings')}" />
<%@ include file="memberCenterNav.jspf" %>
<%-- 以下个人中心入口暂时停用，保留原代码供后续恢复。
<!-- 消息订阅 -->
                <li>
                	<a class="uclia" href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block">
                		<fmt:message key="left.resource.subscription"/><i></i>
                	</a>
                </li>
           		<!-- RSS订阅 -->
                <li>
                	<a class="uclia" href="<%=path %>/usercenter/rssUserData.htm?action=initUserRssData">
                		<fmt:message key="left.subscribe"/><i></i>
                	</a>
               	</li>
               	<!-- 文献传递 -->
            	<li>
            		<a class="uclia" href="<%=path %>/usercenter/docudelivery.htm?action=initDocuView">
	            		<fmt:message key="left.document.delivery"/><i></i>
	            	</a>
	            </li>
               	<!-- 文献求助 -->
            	<li>
            		<a class="uclia" href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView">
	            		<fmt:message key="left.literature.seek.help"/><i></i>
	            	</a>
	            </li>
               	<!-- 参考咨询 -->
                <li>
                	<a class="uclia" href="<%=path %>/platform/scConsultation.htm?action=initConsultationView">
              	  		<fmt:message key="left.reference.consultation"/><i></i>
              	  	</a>
             	</li>
                <!-- 分割线 -->
                <!-- 资源管理 -->
                <%if(ConstantsObj.getSysProperty(MmSettingConstant.getUserUploadResourceIsOpen()).equals("1")){ %> 
                 <li>
                 	<a class="uclia" href="<%=path %>/resource.htm?action=list">
                 		<fmt:message key="left.resource.management"/><i></i>
                 	</a>
                </li>
                 <%} %>
               	<!-- FAQ知识库 -->
                <li>
                	<a class="uclia" href="<%=path %>/platform/faq.htm?action=initPlatformFaqView">
                		<fmt:message key="left.knowledge.base"/><i></i>
                	</a>
               	</li>
               	
<!-- 我的评论 -->
<li><a href="<%=basePath%>user/favorite.htm?action=initMyComtView">我的评论</a></li>
--%>
