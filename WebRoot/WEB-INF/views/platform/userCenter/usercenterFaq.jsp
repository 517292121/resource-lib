<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
		<!-- 会员中心首页头部  -->
		<%@ include file="../pc/common/header.jsp"%>
		<!-- 会员中心中间 ============== 开始 ============== -->
		<div class="p-cnt ucdiv shadow">
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b><fmt:message key='edit.current.location'/>:
		  			<a href="<%=path %>/index.htm" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='addPage.membership.space'/></a> >> 
		  			<fmt:message key='left.knowledge.base'/>
		  			</b></div>
				
           		<c:if test="${!empty faqList}">
           			<%int count=0;String type="1"; %>
		  			<c:forEach var="faq" items="${faqList}" varStatus="state">
		  				<%count++; 
		  				  if(count%2 == 0)
		  				  	type = "2";
		  				  else
		  				  	type = "1";
		  				%>
		  				<div class="zsk_<%=type %>">
				     		<div  class="zsk_nr">
			         			<span class="zsk_ask"><b><Img src="<%=path %>/images/usercenter/faq/faq_q.gif"/></b><p>${faq.handlerScflQuestion }</p></span>
				        		<span class="zsk_da"><b><Img src="<%=path %>/images/usercenter/faq/faq_a.gif"/></b><p>${faq.handlerScflAnswer }</p></span>
			            	</div>
				   	  	</div>
		  			</c:forEach>
		  			
		  			<!-- 分页 -->
					<div class="pages">
				       <c:if test="${pagination.totalPages>0}">
				    	<div style="float:right;padding-top: 20px;">
				    		<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
				    	</div>
					   </c:if>
				    </div>
		  		</c:if>
		  			<c:if test="${empty faqList}">
		  				<span class="noList">
			    			<span><fmt:message key='faq.no.related.knowledge'/></span>
		    			</span>
		  			</c:if>
		  		
			</div> 
			<div class="clear"></div>
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->
		<!-- 会员中心首页脚部 -->

 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>
	</body>
</html>