<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<!-- 会员中心首页头部  -->
	<%@ include file="../pc/common/header.jsp"%>
	
	<script type="text/javascript">
		function submit(){
			var form1 = document.getElementById('form1');
			if(form1)
				form1.submit();
		}
	</script>
		<!-- 会员中心中间 ============== 开始 ============== -->
		<div class="p-cnt ucdiv shadow">
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b><fmt:message key='edit.current.location'/>：
		  			<a href="<%=path %>/index.htm" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='addPage.membership.space'/></a> >> 
		  			<a href="<%=path %>/platform/scConsultation.htm?action=initConsultationView" class="navigation"><fmt:message key='left.reference.consultation'/></a> >> 
		  			${sct.subSctyName }</b></div>
		  			
				
				<div class="ckzxss" >
					<p>
	                	<a class="selected" href="<%=path %>/platform/scConsultation.htm?action=initConsultationView"><fmt:message key='consul.consulting.search'/></a> &nbsp;|&nbsp;
	                	<a href="<%=path %>/sc/createScConsultation.htm"><fmt:message key='consul.my.consult'/></a> &nbsp;|&nbsp;
	                	<a href="<%=path %>/platform/scConsultation.htm?action=myConsultationView"><fmt:message key='consul.my.advice'/></a>
                	</p>
                	<div class="uc_search_wrap">
	  					<form action="<%=path %>/platform/scConsultation.htm?action=initConsultationTypeView" method="post" id="form1">
		  					<input class="uc_search_input" name="search" value="${search }" type="text"/>
		  					<input class="uc_search_btn" type="submit" title="<fmt:message key='comt.search'/>"/>
		  					<input type="hidden" value="${sctyId}" name="sctyId"/>
			            </form>
  					</div>			
	            </div>
				
					<div class="zx_wx_list">
					<div class="zwListDiv">
				           <c:forEach var="myC" items="${consultationList}">
				           		<div class="zwListDiv_o">
						           	<div class="zwld_2">
						            	<a class="atitle" href="<%=path %>/platform/scConsultation.htm?action=initCosultationDetail&sccoId=${myC.sccoId }">${myC.subSccoTitle2 }</a><br/>
						            	<span class="span_minor">
						            		<span class="sorange">${myC.scConsultationType.subSctyName }</span> &nbsp;|&nbsp;
						            		<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${myC.sccoCreationTime }"/>
					            		</span>
					            	</div>
						            <div class="zwld_3">
						            	<c:if test="${myC.sccoState == 1 }"><span class="sred"><fmt:message key='consul.unanswered'/></span></c:if>
							            <c:if test="${myC.sccoState != 1 }"><span class="sgreen"><fmt:message key='consul.replies'/></span></c:if>
								        <br/>
						            	<span class="span_minor"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${myC.sccoReplyTime }"/></span>
					            	</div>
						         </div>
				  			</c:forEach>
						  	 <c:if test="${pagination.totalPages>0 }">
						  			<div class="pages" style="float:left;">
								    	<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
								    </div>
		 					 </c:if>
					         <c:if test="${pagination.totalPages == 0 }">
					         	<fmt:message key='consul.not.found.records'/>
		                      </c:if>
				    </div>
				
					<%@ include file="usercenterConsultationGroup.jsp" %>
		  		
				<div class="clear"></div>
			</div>
					
			</div> 
			<div class="clear"></div>
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>
	</body>
</html>