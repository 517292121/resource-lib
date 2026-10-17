<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="cn.com.adtech.riss2.controller.platform.local.LocalecUtil" %>
<%
	LocalecUtil localecUtil=new LocalecUtil();
	String keyWord = localecUtil.get("list.please.enter.keyword");
	if(request.getParameter("keyWord") != null && request.getParameter("keyWord").length() > 0){
		keyWord = request.getParameter("keyWord") ;
	}
 %>
<%@ include file="../pc/common/header.jsp"%>
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
	<script type="text/javascript">
		function deleteResource(id){
			if(window.confirm("<fmt:message key='list.determine.delete.resource'/>")){
				window.location.href = "<%=path %>/resource.htm?action=deleteResource&id=" + id;
			}
		}
	</script>
	<style type="text/css">
		.inputgray {
			color: gray;
		}
		.inputblack {
			color: black;
		}
	</style>
		
		<!-- 会员中心中间 ============== 开始 ============== --> 
		<div class="p-cnt ucdiv shadow">
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b>
		  				<fmt:message key='edit.current.location'/>：
		  				<a href="<%=path %>/index.htm" class="navigation"><fmt:message key='home.page'/></a> >> 
			  			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='user.conter'/></a> >> 
			  			<a href="<%=path %>/resource.htm?action=list" class="navigation"><fmt:message key='left.resource.management'/></a>
		  			</b>
		  		</div>
		  
				<script type="text/javascript">
					function search(){
						if(document.getElementById("keyWord").value == "<%=keyWord %>"){
							document.getElementById("keyWord").value = "";
						}
						//document.all.form1.submit();
						document.getElementById('form1').submit();
					}
				</script>
				<div class="ckzxss">
					<p>
	                	<a class="selected" href="<%=path %>/resource.htm?action=list"><fmt:message key='list.resource'/></a> &nbsp;|&nbsp;
	                	<a href="<%=path %>/resource.htm?action=add"><fmt:message key='list.new.resources'/></a>
                	</p>
                	<div class="uc_search_wrap">
	  					<form action="<%=path %>/resource.htm" method="post" id="form1">
			               	<input type="hidden" name="action" value="list"/>
		  					<input class="uc_search_input" name="keyWord" id='keyWord' value="<%=keyWord %>" type="text"/>
		  					<input class="uc_search_btn" type="button" title="<fmt:message key='comt.search'/>" onclick="search();"/>
		  					<input type="hidden" name="currentPage" value="${pagination.currentPage }"/> 
			               	<input type="hidden" name="display" value="block"/>
			            </form>
  					</div>	
	            </div>
				
				<form action="<%=path %>/usercenter/infoSubscription.htm?action=deleteKeyWords" id="formn" method="post">
					<input type="hidden" name="currentPage" value="${pagination.currentPage }"/>
					<input type="hidden" name="ids" value="" id="ids"/>
					<input type="hidden" name="display" value="block"/>
				</form>
				
				
				<div class="znList">
					<ul>
					<c:if test="${empty sourceList}">
						<li style="border: none;font-size: 15px;color:#888;text-align: center;"><fmt:message key='list.resources.not'/>！</li>
					</c:if>
					<c:forEach items="${sourceList}" var="v">
				     		<li>
				     			<table cellpadding="0" cellspacing="0" border="0" class="resultTable">
				     				<tr>
				     					<td width="500">
							     			<h3><a href="<%=basePath %>res/${v.rsdaId }" target="_blank">${v.rsdaTitle }</a></h3>
				     					</td>
				     					<td rowspan="2">
							            	<c:if test="${v.rsdaStatus != 3 }">
								            	<a href="javascript: deleteResource('${v.rsdaId }');" class="isbtn right"><fmt:message key='edit.delete'/></a>
								            	<a href="<%=path %>/sourceinput.htm?action=doResourceEdit&sid=${v.rsdaId }&reqType=5" class="isbtn right mgr"><fmt:message key='list.modify'/></a>
							                </c:if>
							                <c:if test="${v.rsdaStatus == 3 }">
								            	<a href="<%=basePath %>res/${v.rsdaId }" target="_blank"><fmt:message key='list.see'/></a>
							                </c:if>
				     					</td>
				     				</tr>
				     				<tr>
				     					<td>
											<div style="font-size:12px;color:#84878C">
												<fmt:message key='list.type'/>：${v.resLiterType.rltyName } &nbsp;|&nbsp;
												<fmt:message key='repeat.characteristic.library'/>：${v.resMetaStore.rmstName } &nbsp;|&nbsp;
												<fmt:message key='search.time'/>：<fmt:formatDate pattern="yyyy-MM-dd" value="${v.rsdaBuilddate}"/> &nbsp;|&nbsp;
												<c:choose>
					          						<c:when test="${v.rsdaStatus == 2 }"><fmt:message key='list.audit'/></c:when>
					          						<c:when test="${v.rsdaStatus == 3 }">
									            		<span class="sgreen"><fmt:message key='list.examine.adopt'/></span>
					          						</c:when>
					          						<c:when test="${v.rsdaStatus == 4 }">
									            		<span class="sred"><fmt:message key='list.examine.not.through'/></span>
					          						</c:when>
								                </c:choose>
											</div> 
				     					</td>
				     				</tr>
				     			
				     			</table>
			     			</li>
				     </c:forEach>
				     	<c:if test="${pagination.totalPages>0}">
						    <div class="pages" style="float:left;">
						    		<%@include file="../pc/common/turnPage.jsp" %>
						    </div>
					    </c:if>
					</ul>
				</div> 
				
			</div> 
							<div class="clear"></div>
		</div>
		
		<!-- 保存结果提示区域 -->
		<div id='SuccessMessageBox' class='SuccessMessageBox' style="position: fixed;"></div>
		${requestScope.result }
		<c:if test="${param.result == 'success'}">
		    <script>showMsg("<fmt:message key='list.save.success'/>");</script>
		</c:if>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>