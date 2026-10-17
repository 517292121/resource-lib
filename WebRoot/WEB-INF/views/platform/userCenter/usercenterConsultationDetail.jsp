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
		  			<fmt:message key='left.reference.consultation'/>
		  			</b>
		  		</div>
				
				<div class="ckzxss" >
					<p>
	                	<a href="<%=path %>/platform/scConsultation.htm?action=initConsultationView"><fmt:message key='consul.consulting.search'/></a> &nbsp;|&nbsp;
	                	<a href="<%=path %>/sc/createScConsultation.htm"><fmt:message key='consul.my.consult'/></a> &nbsp;|&nbsp;
	                	<a class="selected"  href="<%=path %>/platform/scConsultation.htm?action=myConsultationView"><fmt:message key='consul.my.advice'/></a>
                	</p>
                	<div class="uc_search_wrap">
	  					<form action="<%=path %>/platform/scConsultation.htm?action=initConsultationView" method="post" id="form1">
		  					<input class="uc_search_input" name="search" value="${search }" type="text"/>
		  					<input class="uc_search_btn" type="submit" title="<fmt:message key='comt.search'/>"/>
			            </form>
  					</div>	
	            </div>
				
				 <div class="comList">
				 	<div class="ucdetail-div">
				 		<div class="ucdd-div head">
				 			<h2>${sc.sccoTitle }</h2>
				 			<c:if test="${sc.sccoState == 1 }"><span class="sred"><fmt:message key='consul.unanswered'/></span></c:if>
							<c:if test="${sc.sccoState != 1 }"><span class="sgreen"><fmt:message key='consul.replies'/></span></c:if>
				 		</div>
				 		<div class="ucdd-div">
				 			<p>${sc.handlerSccoContent }</p>
				 			<div class="citime">
				 				<span class="sorange">${sc.scConsultationType.sctyName }</span> &nbsp;|&nbsp;
				 				<fmt:message key='detail.consulting.time'/> &nbsp;|&nbsp;<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${sc.sccoCreationTime }"/>
				 			</div>
				 			<c:if test="${sc.sccoState != 1 }">
				 			<div class="replycon">
				 				<p><span class="sgray"><fmt:message key='detail.reply.content'/>：${sc.handlerSccoReplyContent }</span></p>
				 				<div class="citime">
					 				<fmt:message key='detail.recovery.time'/> &nbsp;|&nbsp;<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${sc.sccoReplyTime }"/>
					 			</div>
				 			</div>
				 			</c:if>
				 		</div>
				 	</div>
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