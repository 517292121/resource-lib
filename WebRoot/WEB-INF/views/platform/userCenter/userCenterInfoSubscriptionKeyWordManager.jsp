<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp"%>
<%@ include file="../pc/common/header.jsp"%>

	<script type="text/javascript">
		function checkAll() {
			var check = $("checkAll");
			var infoIds = document.getElementsByName("infoId");
			for(var i = 0; i < infoIds.length; i++) {
				infoIds[i].checked = check.checked; 
			}
		}
		function deleteThis(obj, id) {
			if(window.confirm("<fmt:message key='user.exactly.delete'/>")) {
				$("ids").value = id + ",";
				$("formn").submit();
			}
		}
		function deleteMany() {
			var infoIds = document.getElementsByName("infoId");
			var checkedIds = [];
			for(var i = 0; i < infoIds.length; i++) {
				if(infoIds[i].checked) {
					checkedIds[i] = infoIds[i].value;
				}
			}
			if(checkedIds.length == 0) {
				alert("<fmt:message key='user.choose.a.delete.options'/>");
			} else {
				if(window.confirm("<fmt:message key='user.exactly.delete'/>")) {
					$("ids").value = checkedIds.toString();
					$("formn").submit();
				}
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

		<div style="margin:0px auto; text-align: center;">
		<!-- 会员中心首页头部  -->
		
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
		  			<fmt:message key='user.keyword.management'/>
		  			</b></div>
		  
				
				<div class="ckzxss" >
					<form action="<%=path %>/usercenter/infoSubscription.htm?action=addKeyWordManage" method="post" id="form1">
		            	<input class="ckzx_ssk inputgray"  name="keyWords" id='infoSubscribtionkeyWords' value="<fmt:message key='user.key.word.separate'/>" onfocus="javascript: if(this.value.trim() == <fmt:message key='user.key.word.separate'/>) {this.value=''; this.className='ckzx_ssk inputblack'}" onblur="javascript: if(this.value.trim() == '') {this.className = 'ckzx_ssk inputgray';this.value="<fmt:message key='user.key.word.separate'/>";}" type="text" />
		                <a href="javascript:void(0);" class="button3 b" onclick="javascript: if($('infoSubscribtionkeyWords').value.trim() == '' || $('infoSubscribtionkeyWords').value.trim()=="<fmt:message key='user.key.word.separate'/>") {alert("<fmt:message key='user.key.word.separate'/>"); $('infoSubscribtionkeyWords').focus(); return ;} else { $('form1').submit(); }"><b><fmt:message key='user.key.word.separate'/></b></a>
		                <a href="javascript:void()" class="button3 b" onclick="javascript: $('infoSubscribtionkeyWords').value="<fmt:message key='user.key.word.separate'/>"; $('infoSubscribtionkeyWords').className = 'ckzx_ssk inputgray';" style="text-align: center;"><b><fmt:message key='consul.reset'/></b></a>
		               	<input type="hidden" name="currentPage" value="${pagination.currentPage }"/>
		               	<input type="hidden" name="display" value="block"/>
		               <!--  <p><a href="<%=path %>/platform/scConsultation.htm?action=myConsultationView">添加</a>丨<a href="<%=path %>/platform/scConsultation.htm?action=initConsultationView">查询</a></p> -->
					</form>				
	            </div>
				
				<form action="<%=path %>/usercenter/infoSubscription.htm?action=deleteKeyWords" id="formn" method="post">
					<input type="hidden" name="currentPage" value="${pagination.currentPage }"/>
					<input type="hidden" name="ids" value="" id="ids"/>
					<input type="hidden" name="display" value="block"/>
				</form>
				
				<div style="float: left; width:100%; text-align: left;background: #fff">
					<table border="0" class="zxlbk" >
					  <tr>
					    <td> <table width="100%" border="0" align="center" cellpadding="3" cellspacing="1" bgcolor="#CCCCCC" >
					
					          <tr id="tr1" class="zxlb_1p" >
					          	<td align="center" background="<%=imgBasePath %>ckzx_bt_bj.png" style="background-repeat:repeat-x; " width="30"><input type="checkbox" onclick="checkAll();" id="checkAll"></input></td>
					            <td align="center" background="<%=imgBasePath %>ckzx_bt_bj.png" style="background-repeat:repeat-x; "><b style="text-align: center; width: 100%;"><fmt:message key='user.keyword'/></b></td>
					            <td align="center" width="120" background="<%=imgBasePath %>ckzx_bt_bj.png" style="background-repeat:repeat-x; "><b  style="text-align: center; width: 100%;"><fmt:message key='user.add.time'/></b></td>
					            <td align="center" width="60" background="<%=imgBasePath %>ckzx_bt_bj.png" style="background-repeat:repeat-x;"><b style="text-align: center; width: 100%;"><fmt:message key='user.operation'/></b></td>
					            <!-- <td width="45" background="<%=imgBasePath %>ckzx_bt_bj.png" style="background-repeat:repeat-x;"><b class="bt_3">状态</b></td>
					            <td width="70" background="<%=imgBasePath %>ckzx_bt_bj.png" style="background-repeat:repeat-x;"><b class="bt_4">回复时间</b> </td> -->
					          </tr>
					          
					          <c:choose>
					          	<c:when test="${!empty result}">
					          		<c:forEach var="is" items="${result}">
							          	  <tr bgcolor="#FFFFFF" class="lb_bottom" style="height:30px;">
								            <td align="center"><input type="checkbox" name="infoId" value="${is.issId }"></td>
								            <td align="left" class="whf">${is.issKeyWord }</td>
								            <td align="center"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${is.issDate}"/></td>
								            <td align="center">
								            	<!-- <a style="text-decoration: none;" onmousemove="this.style.color='red';" onmouseout="this.style.color='black';">修改</a> -->
								            	<a style="text-decoration: none; cursor: pointer;" onmousemove="this.style.color='red';" onmouseout="this.style.color='#4f4f4f';" onclick="deleteThis(this, ${is.issId })" title="<fmt:message key='edit.delete'/>"><fmt:message key='edit.delete'/></a></td>
								            	
								          </tr>
				                      </c:forEach>
				                      <tr bgcolor="#FFFFFF" class="lb_bottom" style="height:30px;">
				                      	<td colspan="4" align="right" style="maring-right: 25px;">
				                      		<a style="text-decoration: none; cursor: pointer;" onmousemove="this.style.color='red';" onmouseout="this.style.color='#4f4f4f';" onclick="deleteMany();"><fmt:message key='user.deleting'/></a>
				                      	</td>
				                      </tr>
					          </c:when>
					          <c:otherwise>
					          		<tr id="tr2"  bgcolor="#FFFFFF" class="lb_bottom" style="height:30px;">
							            <td align="center" colspan="6"><fmt:message key='docu.no.relevant.data'/></td>
							        </tr>
		                      </c:otherwise>
				          </c:choose>
					    </table></td>
					  </tr>
					  
					  <tr>
					     <td>
								<!-- 分页 -->
								<div class="pages">
							       <c:if test="${pagination.totalPages>0}">
							    	<div style="float:right;padding-top: 10px;">
							    		<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
							    	</div>
								   </c:if>
							    </div>
					     
					     </td>
					  </tr>
					</table>
				</div>
			</div>
			<div class="clear"></div> 
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
		</div>
		<%@ include file="../pc/common/footer.jsp"%>