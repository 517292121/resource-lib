<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<!-- 会员中心首页头部  -->
	<%@ include file="../pc/common/header.jsp"%>
	<script type="text/javascript">
		function submit(val){
			var form1 = document.getElementById(val);
			if(form1)
				form1.submit();
		}
		function reset(val){
			var form1 = document.getElementById(val);
			if(form1){
				document.getElementById('sccoTitle').value='';
				document.getElementById('sccoContent').value='';
				document.getElementById('sctyId').value='-1';
			}
		}
	</script>
		<!-- 会员中心中间 ============== 开始 ============== -->
		<div class="p-cnt ucdiv shadow">
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b><fmt:message key="edit.current.location"/>：
		  			<a href="<%=path %>/index.htm" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='addPage.membership.space'/></a> >> 
		  			<a href="<%=path %>/platform/scConsultation.htm?action=initConsultationView" class="navigation"><fmt:message key='left.reference.consultation'/></a> >> 
		  			<fmt:message key='consul.my.consult'/></b></div>
				
				<div class="ckzxss" >
					<p>
	                	<a href="<%=path %>/platform/scConsultation.htm?action=initConsultationView"><fmt:message key='consul.consulting.search'/></a> &nbsp;|&nbsp;
	                	<a class="selected" href="<%=path %>/sc/createScConsultation.htm"><fmt:message key='consul.my.consult'/></a> &nbsp;|&nbsp;
	                	<a href="<%=path %>/platform/scConsultation.htm?action=myConsultationView"><fmt:message key='consul.my.advice'/></a>
                	</p>
                	<div class="uc_search_wrap">
	  					<form action="<%=path %>/platform/scConsultation.htm?action=initConsultationView" method="post" id="form1">
		  					<input class="uc_search_input" name="search" value="${search }" type="text"/>
		  					<input class="uc_search_btn" type="submit" title="<fmt:message key='comt.search'/>"/>
			            </form>
  					</div>					
	            </div>
				
			    <form action="<c:url value='/sc/createScConsultation.htm'/>" method="post" id="form2">
					<div class="fbxx">
	                    <table width="100%" cellpadding="0" cellspacing="10" border="0">
	                      <tr>
	                        <td width="150" align="right">&nbsp;</td>
	                        <td width="550" align="left"><h3><fmt:message key='consul.my.consult'/></h3></td>
	                      </tr>
	                      <tr>
	                        <td width="150" align="right"><fmt:message key='search.title'/>：</td>
	                        <td width="550" align="left">
	                        	<input id="sccoTitle" name="sccoTitle" class="tanInput left" type="text" value="${scConsultation.sccoTitle}"/>
	                        	<strong style="float: left;color:red;">*</strong>
	                        	<spring:bind path="scConsultation.sccoTitle">
					                <c:if test="${status.error}">
					                  <span style="color: red;"><c:forEach items="${status.errorMessages}" var="error">
					                    <c:out value="${error}"/>
					                  </c:forEach></span>
					                </c:if>
				              </spring:bind>
	                        </td>
	                      </tr>
	                      <tr>
	                        <td align="right"><fmt:message key='consul.classification'/>：</td>
	                        <td align="left">
	                          <select class="fl_xiala" id="sctyId" name="sctyId">
				                <option value="-1"><fmt:message key='consul.choose.consultation.classification'/></option> 
				                <c:forEach items="${scConsultationTypeList}" var="scConsultationType">
				                  <c:if test="${scConsultationType.sctyId == scConsultation.sctyId}">
				                  	<option value="<c:out value="${scConsultationType.sctyId}"/>" selected="selected">${scConsultationType.sctyName}</option>
				                  </c:if>
				                  <c:if test="${scConsultationType.sctyId != scConsultation.sctyId}">
					                  <option value="<c:out value="${scConsultationType.sctyId}"/>">${scConsultationType.sctyName}</option>
				                  </c:if>
				                </c:forEach>
				              </select><strong style="float: left;color:red">*</strong>
				              <spring:bind path="scConsultation.sctyId">
				                <c:if test="${status.error}">
				                    <span style="color: red;"><c:forEach items="${status.errorMessages}" var="error">
				                    <c:out value="${error}"/>
				                  </c:forEach></span>
				                </c:if>
				              </spring:bind>
				              
							</td>
	                      </tr>
	                      <tr>
	                        <td align="right"><fmt:message key='consul.mioas'/>：</td>
	                        <td align="left">
	                          <textarea  class="ms_ssk2" id="sccoContent" name="sccoContent">${scConsultation.sccoContent}</textarea>
	                          <spring:bind path="scConsultation.sccoContent">
				                <c:if test="${status.error}">
				                  <br/>
				                  <font style="color: red;"><fmt:message key='repeat.error'/>：<c:forEach items="${status.errorMessages}" var="error">
				                    <c:out value="${error}"/>
				                  </c:forEach></font>
				                </c:if>
				              </spring:bind>
							</td>						
	                      </tr>
	                      <tr>
	                        <td>&nbsp;</td>
	                        <td>                    
		                         <a href="javascript:submit('form2');" class="tbtn cabtn mgr"><fmt:message key='consul.submit'/></a>
		                	 	 <a href="javascript:reset('form2');" class="tbtn cabtn"><fmt:message key='consul.reset'/></a>
							</td>
	                      </tr>
	                    </table>
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