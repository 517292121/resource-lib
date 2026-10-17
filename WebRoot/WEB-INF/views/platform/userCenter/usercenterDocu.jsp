<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<!-- 会员中心首页头部  -->
	<%@ include file="../pc/common/header.jsp"%>
	<link rel="stylesheet" href="<%=path %>/css/platform/docu.css" type="text/css"></link>
	<script type="text/javascript">
		function submit(val){
			var form1 = document.getElementById(val);
			if(form1)
				form1.submit();
		}
		function checkDel(Id){
			var info_1 = "<fmt:message key='docu.determine.delete.the.service'/>";
			var rel = confirm(info_1);
			if(rel){
				location.href = '<%=request.getContextPath() %>/usercenter/docudelivery.htm?action=delDocu&sddeId='+Id;
			}
		}
		$(document).ready(function(){
	     	/**鼠标移动效果*/
			$(".style1").hover(function(){
				$(this).addClass('hover');
			},function(){
				$(this).removeClass('hover');
			});
		});
	</script>
		<!-- 会员中心中间 ============== 开始 ============== -->
		<div class="p-cnt ucdiv shadow" >
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b><fmt:message key='edit.current.location'/>：
		  			<a href="<%=path %>/index" class="navigation"><fmt:message key='home.page'/></a> >> 
			  		<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation">
			  		<fmt:message key='addPage.membership.space'/></a> >> <fmt:message key='left.document.delivery'/>
		  			</b>
		  			<div class="tbtns">
						<a class="tbtn" style="color:#fff;" href="<%=path %>/usercenter/docudelivery.htm?action=initDocuTable"><fmt:message key='docu.fill.in.form'/></a>
					</div> 
	  			</div>
				
				
				<div class="zx_wx_list">
				
					<div class="zwListDiv" style="width:100%;">
				           <c:forEach var="v" items="${noResoleList}">
				           		<div class="zwListDiv_o style1">
						           	<div class="zwld_2">
						            	<a class="atitle" href="<%=path %>/usercenter/docudelivery.htm?action=initDocuDetail&sddeId=${v.sddeId }" title="${v.sddeDocuTitle }">${v.sddeDocuTitle }</a><br/>
						            	<span class="span_minor">
						            		<span class="sorange">${v.sddeDocuType }</span> &nbsp;|&nbsp;
						            		<fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${v.sddeCreationTime }"/>
					            		</span>
					            	</div>
						            <div class="zwld_3">
						          		<c:if test="${v.sddeStatus == 1 }"><span class="sred"><fmt:message key='consul.unanswered'/></span></c:if>
							            <c:if test="${v.sddeStatus != 1 }"><span class="sgreen"><fmt:message key='consul.replies'/></span></c:if>
								        <br/>
						            	<span class="span_minor"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${v.sddeReplyTime }"/></span>
					            	</div>
					            	<b class="closestyle1" title="<fmt:message key='edit.delete'/>" onclick="checkDel(${v.sddeId});">
							  			<img src="<%=path %>/images/usercenter/close1.png"/>
						  			</b>
						         </div>
				  			</c:forEach>
						  	 <c:if test="${pagination.totalPages>0 }">
						  			<div class="pages" style="float:left;">
								    	<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
								    </div>
		 					 </c:if>
					         <c:if test="${pagination.totalPages == 0 }">
					         	<fmt:message key='docu.no.relevant.data'/>
		                      </c:if>
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