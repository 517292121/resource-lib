<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
	<%@ include file="../pc/common/header.jsp"%>
	
	<script type="text/javascript">
	    function getLiter(id){
	    	var rmstid = id;
	    	if(rmstid.length < 1){
	    	    return;
	    	}
	    	memberResource.getListOptionByStore(rmstid,function(data){
	    		document.getElementById("rltyId_Area").innerHTML = "<select name='rltyId' id='rltyId' class='fl_xiala' style='width: 200px;'>" + data + "</select>";
	    	});
	    }
	    
	    function onNext(){
	    	if(document.getElementById("rmstId").value.length < 1){
	    		alert("<fmt:message key='addType.choose.library'/>");
	    		return;
	    	} 
	    	/***
	    	if(document.getElementById("rltyId").value.length < 1){
	    		alert("请选择资源类型！");
	    		return;
	    	}*/
	    	document.getElementById("typeform").submit();
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

		
		<!-- 会员中心首页头部  -->
		
		<script type='text/javascript' src="<c:url value='/dwr/engine.js'/>"> </script>
		<script type='text/javascript' src="<c:url value='/dwr/util.js'/>"> </script>
		<script type='text/javascript' src="<c:url value='/dwr/interface/memberResource.js'/>"></script>
		
		<!-- 会员中心中间 ============== 开始 ============== -->
		<div class="p-cnt ucdiv shadow">
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  		  <b>
		  		  	<fmt:message key="edit.current.location"/>
		  		  	<a href="<%=path %>/index.htm" class="navigation"><fmt:message key="home.page"/></a> >> 
		  		  	<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key="addPage.membership.space"/></a> >> 
		  		  	<a href="<%=path %>/resource.htm?action=list" class="navigation"><fmt:message key="addPage.personal.resources"/></a> >> 
		  		  	<a href="<%=path %>/resource.htm?action=add" class="navigation"><fmt:message key="addType.new.resources"/></a> >> 
		  		  	<fmt:message key="addType.select.library"/>
		  		  </b>
		  		</div>
		  
				
				<div class="resourceRemark" >
				  &nbsp; &nbsp;  &nbsp; <fmt:message key="edit.permanent.preservation"/>
	            </div>
				
					<form action="<%=path %>/resource.htm" name="typeform" id="typeform">
						<input type="hidden" name="action" value="initAdd">
						<div class="fbxx">
							<table width="100%" cellpadding="0" cellspacing="10" border="0">
								<tr>
			                        <td width="150" align="right">&nbsp;</td>
			                        <td width="550" align="left"><h3><fmt:message key="addType.select.library"/></h3></td>
			                    </tr>
								<tr>
									<td width="150" align="right"><fmt:message key='repeat.characteristic.library'/>：</td>
									<td width="550" align="left">
									   <select name="rmstId" id="rmstId" onchange="getLiter(this.value);" class="fl_xiala" style="width: 200px;">
									      <option value=""><fmt:message key='edit.please.choose'/></option>
									      <c:forEach var="v" items="${requestScope.storeList}">
									      	 <option value="${v.rmstId }">${v.rmstName }</option>
									      </c:forEach>
									   </select>
									</td>
								</tr>
								
								<tr>
									<td align="right"><fmt:message key='repeat.resource.type'/>：</td>
									<td id="rltyId_Area">
									   <select name="rltyId" id="rltyId" class="fl_xiala" style="width: 200px;">
									      <option value=""><fmt:message key='edit.please.choose'/></option> 
									   </select>
									</td>
								</tr>
								
								<tr>
									<td>&nbsp; </td>
									<td id="rltyId_Area">
									   <a href="javascript:void(0);" class="tbtn cabtn mgr" onclick="onNext();"><b><fmt:message key='addType.next.step'/></b></a>
									   <a href="<%=path %>/resource.htm?action=list" class="tbtn cabtn"><b><fmt:message key='edit.return'/></b></a>
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