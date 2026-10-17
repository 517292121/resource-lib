<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<!-- 会员中心首页头部  -->
	<%@ include file="../pc/common/header.jsp"%>
	<link rel="stylesheet" href="<%=path %>/css/platform/docu.css" type="text/css"></link>
		<div class="p-cnt ucdiv shadow" >
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b><fmt:message key='edit.current.location'/>：
		  			<a href="<%=path %>/index" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/docudelivery.htm?action=initDocuView" class="navigation">
		  			<fmt:message key='left.document.delivery'/></a>>><fmt:message key='detail.document.application.information'/>
		  			</b>
		  			<div class="tbtns">
						<a class="tbtn" style="color:#fff;" href="<%=path %>/usercenter/docudelivery.htm?action=initDocuView"><fmt:message key='edit.return'/></a>
					</div> 	
		  		</div>
				
             <form action="<c:url value='/platform/xkdt.htm'/>" method="post" id="form2">
		        <div class="fbxx">
		        	<div class="docuDiv" style="display: none">
	                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="listTableInfo" > 
	                      <tr class="thead">
		                   	 	<td>&nbsp;</td>
		                        <td><fmt:message key='create.application.information'/></td>
		                      </tr> 
	                   	 <tr>
	                        <td align="right" class="td150"><fmt:message key='create.surname'/>&nbsp;&nbsp;<fmt:message key='create.name'/>：</td> 
	                        <td align="left" class="td500">${userinfo.suinName }</td> 
	                      </tr>
	                   	 <tr>
	                        <td align="right" class="td150"><fmt:message key='create.post'/>&nbsp;&nbsp;<fmt:message key='create.box'/>：</td> 
	                        <td align="left" class="td500">${userinfo.suinEmail }</td> 
	                      </tr>
	                   	 <tr>
	                        <td align="right" class="td150"><fmt:message key='create.electric'/>&nbsp;&nbsp;<fmt:message key='create.word'/>：</td> 
	                        <td align="left" class="td500">${userinfo.suinMobileTelephone }</td> 
	                      </tr>
	                     </table>
                   </div>
                     
                    <div class="docuDiv mgt"> 
	                    <table width="100%" border="0" cellpadding="0" cellspacing="0" class="listTableInfo">  
	                   	 <tr class="thead">
	                   	 	<td>&nbsp;</td>
	                        <td><fmt:message key='create.literature.information'/></td>
	                      </tr>
	                       <tr>
	                        <td align="right" class="td150"><fmt:message key='detail.time.of.submission'/>：</td> 
	                        <td align="left" class="td500"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${sdde.sddeCreationTime }"/> </td> 
	                      </tr>
	                      <tr>
	                        <td align="right" class="td150"><fmt:message key='repeat.literature.type'/>：</td> 
	                        <td align="left" class="td500"> ${sdde.sddeDocuType } </td> 
	                     </tr>
	                      <tr>
	                        <td align="right" class="td150"><fmt:message key='search.title'/>：</td> 
	                        <td align="left" class="td500"> ${sdde.sddeDocuTitle } </td> 
	                     </tr>
	                     <c:if test="${!empty sdde.sddeDocuCreator }">
	                      <tr>
	                        <td align="right" class="td150"><fmt:message key='create.author'/>：</td> 
	                        <td align="left" class="td500"> ${sdde.sddeDocuCreator } </td> 
	                     </tr>
	                     </c:if>
	                     <c:if test="${!empty sdde.sddeDocuPublisher }">
	                      <tr>
	                        <td align="right" class="td150"><fmt:message key='repeat.publisher'/>：</td> 
	                        <td align="left" class="td500"> ${sdde.sddeDocuPublisher } </td> 
	                     </tr>
	                     </c:if>
	                     <c:if test="${!empty sdde.sddeDocuKeyword }">
	                      <tr>
	                        <td align="right" class="td150"><fmt:message key='repeat.detail.keyword'/>：</td> 
	                        <td align="left" class="td500"> ${sdde.sddeDocuKeyword } </td> 
	                     </tr>
	                     </c:if>
	                     <c:if test="${!empty sdde.sddeDocuDate }">
	                      <tr>
	                        <td align="right" class="td150"><fmt:message key='create.time.selection'/>：</td> 
	                        <td align="left" class="td500"> ${sdde.sddeDocuDate } </td> 
	                     </tr>
	                     </c:if>
	                     <c:if test="${!empty sdde.sddeDocuPage }">
	                      <tr>
	                        <td align="right" class="td150"><fmt:message key='create.page.selection'/>：</td> 
	                        <td align="left" class="td500"> ${sdde.sddeDocuPage } </td> 
	                     </tr>
	                     </c:if>
	                     <c:if test="${!empty sdde.sddeDocuIsbn }">
	                      <tr>
	                        <td align="right" class="td150">ISSN/ISBN：</td> 
	                        <td align="left" class="td500"> ${sdde.sddeDocuIsbn } </td> 
	                     </tr>
	                     </c:if>
	                     <c:if test="${!empty sdde.sddeContent }">
	                   	 <tr>
	                        <td align="right" class="td150"><fmt:message key='create.requirement.description'/>：</td> 
	                        <td align="left" class="td500">${sdde.sddeContent}</td> 
	                      </tr>
	                      </c:if>
	                     </table>
                     </div>
                     
                     <div class="docuDiv"> 
                      <table width="100%" border="0" cellpadding="0" cellspacing="0" class="listTableInfo"> 
		                   <tr class="thead">
		                   	<td>&nbsp;</td>
	                        <td class="t1"><fmt:message key='detail.reply.information'/></td>
	                      </tr>
	                   		<c:if test="${sdde.sddeStatus == 2}">
	                   		  	<tr>
		                        <td align="right" class="td150"><fmt:message key='detail.reply.content'/>：</td> 
		                        <td align="left" class="td500">${sdde.sddeReplyContent}</td> 
		                      </tr>
		                      <c:if test="${!empty sdde.sddeDocuUrl}">
			                   	 <tr>
			                        <td align="right" class="td150"><fmt:message key='detail.text.link'/>：</td> 
			                        <td align="left" class="td500"><a href="<%=path %>/${sdde.sddeDocuUrl }" style="color:#007bb7" target="_blank"><fmt:message key='detail.click.downloads'/></a></td> 
			                      </tr>
		                      </c:if>
		                      <tr>
		                        <td align="right" class="td150"><fmt:message key='detail.reply.librarian'/>：</td> 
		                        <td align="left" class="td500">${sdde.sysManager.smanName }</td>
		                      </tr>
		                   	 <tr>
		                        <td align="right" class="td150"><fmt:message key='detail.recovery.time'/>：</td> 
		                        <td align="left" class="td500"><fmt:formatDate pattern="yyyy-MM-dd HH:mm" value="${sdde.sddeReplyTime }"/></td>
		                      </tr>
	                      </c:if>
	                      <c:if test="${sdde.sddeStatus != 2}">
	                   		  <tr>
	                   		  	<td>&nbsp;</td>
		                        <td align="left"><fmt:message key='detail.not.timely.reply'/></td> 
		                      </tr>
	                      </c:if>
                     </table>
                     </div>
                     
                </div>
                 </form>
			</div> 
			<div class="clear"></div>
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>
	</body>
</html>