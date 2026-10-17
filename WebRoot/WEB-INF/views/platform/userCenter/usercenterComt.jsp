<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<!-- 会员中心首页头部  -->
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
		  			<a href="<%=path %>/index.htm" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='addPage.membership.space'/></a> >> 
		  			<fmt:message key='comt.my.comments'/>
		  			</b>
		  			<div class="uc_search_wrap" style="margin: 8px 0px 0px 0px;">
	  					<form action="<%=path %>/user/favorite.htm?action=initMyComtView" method="post" id="form1">
		  					<input class="uc_search_input" name="search" value="${search }" type="text"/>
		  					<input class="uc_search_btn" type="submit" title="<fmt:message key='comt.search'/>"/>
			            </form>
  					</div>
	  			</div>
	  			
				
				<div class="my_sp">
         			
         				<c:choose>
		       			<c:when test="${!empty comtList}"> 
		       				<div class="comList">
			       				<c:forEach var="comt" items="${comtList}" varStatus="state">
					  			
					  				<!-- 构造下载路径 -->
				        			<c:choose>
					        			<c:when test="${!empty comt.resSourceData.resMetaStore.rmstRfilId}"> 
					        				<c:set var="newDownLoadPath" value="${comt.resSourceData.resMetaStore.resFilesystem.rfilAddress}" />
					        			</c:when>
					        			<c:otherwise> 
					        				<c:set var="newDownLoadPath" value="${downLoadPath}" />
					        			</c:otherwise>
				        			</c:choose>
					  			
								 	<div class="ucdetail-div">
								 		<div class="ucdd-div head">
								 			<h2><a target="_blank" href='<%=basePath %>res/${comt.resSourceData.rsdaId }' title='${comt.resSourceData.rsdaTitle }'>${comt.resSourceData.subRsdaTitle }</a></h2>
								 		</div>
								 		<div class="ucdd-div">
								 			<p><span class="sgray"><fmt:message key='comt.my'/>：${comt.replaceRscoContent }<span></p>
								 			<div class="citime">
								 				<c:if test="${empty comt.rscoStatus}">
			               							<span class="sred"><fmt:message key='comt.to.be.audited'/></span>
			               						</c:if>
			               						<c:if test="${comt.rscoStatus == 0}">
			               							<span class="sred"><fmt:message key='comt.shielded'/></span>
			               						</c:if>
			               						<c:if test="${comt.rscoStatus == 1}">
			               							<span class="sred" ><fmt:message key='comt.normal'/></span>
			               						</c:if>
		               							&nbsp;|&nbsp;
								 				<fmt:message key='comt.review.time'/> <fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${comt.rscoDate }"/>
								 			</div>
								 		</div>
								 	</div>
						    		
					  			</c:forEach>
				  			</div>
				  			
				  			<!-- 分页 -->
					       <c:if test="${pagination.totalPages>0}">
								<div class="pages">
					    		<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
							    </div>
						   </c:if>
		       			</c:when>
		       			<c:otherwise> 
		       				<span class="noList">
			    			<span><fmt:message key='comt.comment.null'/></span>
		    			</span>
		       			</c:otherwise>
		       			</c:choose>
         		</div>  
				
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