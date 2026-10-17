<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<!-- 会员中心首页头部  -->
	<%@ include file="../pc/common/header.jsp"%>
	<script type="text/javascript">
		function submit(val){
			var form1 = document.getElementById(val);
			if(form1)
				form1.submit();
		}
		function literTypeLink(id) {
			document.getElementById("rltyId").value = id;
			document.getElementById("rltyLinkaaaSearchForm").submit();
		}
	</script>
	<form method="post" id="rltyLinkaaaSearchForm" target="_blank" action="<%=path %>/search.htm">
		<input id="sortField" name="sortField" type="hidden" value="title">
		<input id="reverse" name="reverse" type="hidden" value="false">
		<input id="showModel" name="showModel" type="hidden" value="1">
		<input id="searchType" name="searchType" type="hidden" value="simple">
		<input id="rltyId" name="rltyId" type="hidden" >
		<input id="rltyLink" name="rltyLink" type="hidden" value="rltyLink" >
	</form>		
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
		  			<fmt:message key='left.my.collection'/>
		  			</b>
		  			<div class="uc_search_wrap" style="margin: 8px 0px 0px 0px;">
	  					<form action="<%=path %>/user/favorite.htm?action=initScFavoriteView" method="post" id="form1">
		  					<input class="uc_search_input" name="search" value="${search }" type="text"/>
		  					<input class="uc_search_btn" type="submit" title="<fmt:message key='comt.search'/>"/>
			            </form>
  					</div>
	  			</div>
				
				<div class="my_sp">
         			<ul class="sp_all">
				  	<c:choose>
	       			<c:when test="${!empty favoriteList}"> 
	       				<c:forEach var="fav" items="${favoriteList}" varStatus="state">
			  				  <!--开始-->
							  <li class="jsxq_lb">
						   		  <ul class="js_content">
						         	<li>
						         		<b class="js_book">
						         			<span class="rltyname">${fav[4] }</span>
					         			</b>
					         			<a style="float:none" class="title" target="_blank" href='<%=basePath %>res/${fav[1] }'>${fav[2] }</a>
			         				</li>
						            <li  style="padding-top: 5px">
						            	<b class="js_of">
						            		<fmt:message key='faq.collection.time'/>：
					            		</b>
					            		<b class="js_time"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${fav[3] }"/></b>
				            		</li>
						            <li></li>
						   		 </ul>
					             <p class="del">
					             	<a class="isbtn right" href="<%=path %>/user/favorite.htm?action=delScFavorite&scfId=${fav[0]}" onclick="return confirm(<fmt:message key='faq.determine.delete.collection'/>);">
					             	<fmt:message key='faq.deleting.the.collection'/>
					             	</a>
					             </p>
				    		</li>
				    		<!--结束-->
			  			</c:forEach>
			  			</ul>
			  			<!-- 分页 -->
						<div class="pages">
					       <c:if test="${pagination.totalPages>0}">
					    	<div style="float:right;padding-top: 20px;">
					    		<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
					    	</div>
						   </c:if>
					    </div>
	       			</c:when>
	       			<c:otherwise> 
	       				<span class="noList">
			    			<span><fmt:message key='faq.no.related.collection'/></span>
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
