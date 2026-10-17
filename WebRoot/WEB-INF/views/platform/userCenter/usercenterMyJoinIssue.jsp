<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../pc/common/header.jsp"%>
	<script type="text/javascript">
		function submit(val){
			var form1 = document.getElementById(val);
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
		  			<a href="<%=path %>/index" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='user.conter'/></a> >> 
		  			<a href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView" class="navigation"><fmt:message key='left.literature.seek.help'/></a> >> 
		  			<fmt:message key='create.my.am.involved'/>
		  			</b>
	  			</div>
				
				<div class="ckzxss">
		                <p>
		                	<a href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView"><fmt:message key='create.search.help'/></a> &nbsp;|&nbsp;
		                	<a href="<%=path %>/sc/createScIssue.htm"><fmt:message key='create.my.help'/></a> &nbsp;|&nbsp;
		                	<a href="<%=path %>/platform/scIssue.htm?action=initPlatformMyIssueView"><fmt:message key='create.my.ask.for.help'/></a> &nbsp;|&nbsp;
		                	<a class="selected" href="<%=path %>/platform/scIssue.htm?action=initPlatformMyJoinIssueView"><fmt:message key='create.my.am.involved'/></a>
	                	</p>
	                	<div class="uc_search_wrap">
		  					<form action="<%=path %>/platform/scIssue.htm?action=initPlatformMyJoinIssueView" method="post" id="form1">
			  					<input class="uc_search_input" name="search" value="${search }" type="text"/>
			  					<input class="uc_search_btn" type="submit" title="<fmt:message key='comt.search'/>"/>
				            </form>
	  					</div>
	            </div>
				
				
		  		<div class="zx_wx_list">
				
					<div class="zwListDiv" style="width:100%;">
				           <c:forEach var="scIssue" items="${myJoinIssueList}">
				           		<div class="zwListDiv_o style1">
				           			<div class="zwld_1"> 
						                <span class="numbtn" style="width:50px">${scIssue.scisReplyNumber }</span>
						            </div>
						           	<div class="zwld_2">
						           		
						            	<a class="atitle" href="<%=path %>/platform/scIssue.htm?action=initScIssueDetailView&scisId=${scIssue.scisId }">${scIssue.subScisTitle }</a><br/>
						            	<span class="span_minor">
						            		<span class="sorange">${scIssue.scIssueType.subScitName }</span> &nbsp;|&nbsp;
						            		<fmt:message key='issue.ask.for.help'/>&nbsp;
						            		<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${scIssue.scisCreationTime }"/>
					            		</span>
					            	</div>
						            <div class="zwld_3">
						           		<c:if test="${scIssue.scisState == 1 }"><span class="sred"><fmt:message key='issue.to.be.solved'/></span></c:if>
								        <c:if test="${scIssue.scisState == 2 }"><span class="sgreen"><fmt:message key='issue.resolved'/><span></c:if>
								        <c:if test="${scIssue.scisState == 3 }"><span class="sgray"><fmt:message key='issue.closed'/></span></c:if>
								        <br/>
						            	<span class="span_minor"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${scIssue.scisLastReplyTime }"/></span>
					            	</div>
					            	<b class="closestyle1" title="<fmt:message key='edit.delete'/>" onclick="checkDel(${scIssue.scisId },${scIssue.scisReplyNumber });">
							  			<img src="<%=path %>/images/usercenter/close1.png"/>
						  			</b>
						         </div>
				  			</c:forEach>
						  	 <c:if test="${pagination.totalPages>0 }">
						  			<div class="pages" style="float:left;">
								    	<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
								    </div>
		 					 </c:if>
					         <c:if test="${pagination.totalPages == 0 }"><fmt:message key='docu.no.relevant.data'/></c:if>
				    </div>
				    
			</div> 
			</div> 
			<div class="clear"></div>
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->
		
		


 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>