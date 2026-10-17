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
				document.getElementById('scisTitle').value='';
				document.getElementById('scisContent').value='';
				document.getElementById('scisScitId').value='-1';
				var t_scisTitle = document.getElementById('t_scisTitle');
				var t_scisContent = document.getElementById('t_scisContent');
				var t_scisScitId = document.getElementById('t_scisScitId');
				if(t_scisTitle)
					t_scisTitle.innerHTML='';
				if(t_scisContent)
					t_scisContent.innerHTML='';
				if(t_scisScitId)
					t_scisScitId.innerHTML='';
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
		  			<b><fmt:message key='edit.current.location'/>：
		  			<a href="<%=path %>/index.htm" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='addPage.membership.space'/></a> >> 
		  			<a href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView" class="navigation"><fmt:message key='left.literature.seek.help'/></a> >> 
		  			<fmt:message key='create.my.help'/>
		  			</b>
	  			</div>
				
				<div class="ckzxss">
		                <p>
		                	<a href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView"><fmt:message key='create.search.help'/></a> &nbsp;|&nbsp;
		                	<a class="selected" href="<%=path %>/sc/createScIssue.htm"><fmt:message key='create.my.help'/></a> &nbsp;|&nbsp;
		                	<a href="<%=path %>/platform/scIssue.htm?action=initPlatformMyIssueView"><fmt:message key='create.my.ask.for.help'/></a> &nbsp;|&nbsp;
		                	<a href="<%=path %>/platform/scIssue.htm?action=initPlatformMyJoinIssueView"><fmt:message key='create.my.am.involved'/></a>
	                	</p>
	                	<div class="uc_search_wrap">
		  					<form action="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView" method="post" id="form1">
			  					<input class="uc_search_input" name="search" value="${search }" type="text"/>
			  					<input class="uc_search_btn" type="submit" title="<fmt:message key='comt.search'/>"/>
				            </form>
	  					</div>
	            </div>
				
		        <form action="<c:url value='/sc/createScIssue.htm'/>" method="post" id="form2">
				<div class="fbxx">
                    <table width="100%" cellpadding="0" cellspacing="10" border="0">
                      <tr>
                        <td width="150" align="right">&nbsp;</td>
                        <td width="550" align="left"><h3><fmt:message key='create.release.help.information'/></h3></td>
                      </tr>
                      <tr>
                        <td width="150" align="right"><fmt:message key='search.title'/>：</td>
                        <td width="550" align="left">
				              <input id="scisTitle" name="scisTitle" class="tanInput left" type="text" value="${scIssue.scisTitle}"/>
	                        	<strong style="float: left;color:red;">*</strong>
	                        	<spring:bind path="scIssue.scisTitle">
					                <c:if test="${status.error}">
					                  <span style="color: red;" id="t_scisTitle"><c:forEach items="${status.errorMessages}" var="error">
					                    <c:out value="${error}"/>
					                  </c:forEach></span>
					                </c:if>
				              </spring:bind>
                        </td>
                      </tr>
                      <tr>
                        <td align="right"><fmt:message key='consul.classification'/>：</td>
                        <td align="left">
                          
                          <select class="fl_xiala" id="scisScitId" name="scisScitId">
				                <option value="-1"> <fmt:message key='create.choose.help.classification'/></option>
				                <c:forEach items="${scIssueTypeList}" var="scIssueType">
					                  <option value="<c:out value="${scIssueType.scitId}"/>">${scIssueType.scitName}</option>
				                </c:forEach>
				              </select><strong style="float: left;color:red">*</strong>
				              <spring:bind path="scIssue.scisScitId">
				                <c:if test="${status.error}">
				                    <span style="color: red;" id="t_scisScitId"><c:forEach items="${status.errorMessages}" var="error">
				                    <c:out value="${error}"/>
				                  </c:forEach></span>
				                </c:if>
				              </spring:bind>
						</td>
                      </tr>
                      <tr>
                        <td  align="right"><fmt:message key='create.supplementary.content'/>：</td>
                        	<td>
                          <textarea  class="ms_ssk2" id="scisContent" name="scisContent" cols="" rows="">${scIssue.scisContent }</textarea>
                          <spring:bind path="scIssue.scisContent">
			                <c:if test="${status.error}">
			                  <br/>
			                  <font style="color: red;" id="t_scisContent"><fmt:message key='repeat.error'/>：<c:forEach items="${status.errorMessages}" var="error">
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
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>
	</body>
</html>