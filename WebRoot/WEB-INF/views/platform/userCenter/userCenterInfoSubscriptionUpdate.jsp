<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp"%>
<%@ include file="../pc/common/header.jsp"%>
<%
String path1 = request.getContextPath();
String basePath1 = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path1+"/";
%>
<script src="<%=path %>/viewsjs/search/searchCommon.js" type=text/javascript></script>
<script src="<%=path %>/viewsjs/sc/scFavorites.js" type=text/javascript></script>
<script type="text/javascript">
	riss2.subscription = {
		FAVORITE_HREF: "<%=path %>/user/favorite.htm?action=addFavorite",
		addFavorite: function(id) {
			if(id) {
				riss2.favorites.addFavorite(this.FAVORITE_HREF, '${sysUserinfo.suinId}', id);
			}
		},
		addManyFavorites: function() {
			var that = riss2.subscription;
			var infoIds = document.getElementsByName("infoId");
			var checkedIds = [];
			for(var i = 0; i < infoIds.length; i++) {
				if(infoIds[i].checked) {
					checkedIds.push(infoIds[i].value);
				}
			}
			if(checkedIds.length == 0) {
				alert("<fmt:message key='user.choose.a.delete.options'/>");
			} else {
				for(var i = 0; i < checkedIds.length; i++) {
					riss2.favorites.addFavorite(that.FAVORITE_HREF, '${sysUserinfo.suinId}', checkedIds[i], function() {
						void(0);
					});
				}
			}
		},
		ignoreResources: function() {
			var infoIds = document.getElementsByName("infoId");
			var checkedIds = [];
			for(var i = 0; i < infoIds.length; i++) {
				if(infoIds[i].checked) {
					checkedIds.push(infoIds[i].value);
				}
			}
			if(checkedIds.length == 0) {
				alert("<fmt:message key='user.ignore.resources'/>");
			} else {
				if(window.confirm("<fmt:message key='user.determine.lgnore.it'/>")) {
					document.getElementById("ignoreIds").value = checkedIds.toString();
					document.getElementById("formn").submit();
				}
			}
		},
		ignoreResource: function(id) {
			if(window.confirm("<fmt:message key='user.determine.lgnore.it'/>")) {
				document.getElementById("ignoreIds").value = id + ",";
				document.getElementById("formn").submit();
			}
		},
		checkAll: function() {
			var check = document.getElementById("checkAll");
			var infoIds = document.getElementsByName("infoId");
			for(var i = 0; i < infoIds.length; i++) {
				infoIds[i].checked = check.checked; 
			}
		}
	}
	
	function getKeyWordsInfoList(suinId){
		sysUserPlatformFacade.getKeyWordsInfoList(suinId,function(data){
			var msgs = eval("("+data+")");
			if (msgs.isError) {
				alert(msgs.errorMessage);
				return;
			}
			var keyWordsContent = document.getElementById("keyWordsContent");
			if(msgs.result){
				keyWordsContent.innerHTML = msgs.result;
			}else{
				keyWordsContent.innerHTML = '';
			}
			if(msgs.hasData == 'false'){
				document.getElementById('keyWordsContent').innerHTML = '<li class="nothing"><fmt:message key='user.no.subscribe.item'/></li>';
			}
		});
	}
	
		function delSelfTag(id){

		    if(${empty sysUserinfo}){
				alert("<fmt:message key='user.log.in.first'/>");
				return;
			}
			
			var ids = '';
			if(id != '0'){
				ids = id+",";
			}else{
				$("#keyWordsContent li.color1").each(function(){
					ids += $(this).attr('ref')+",";
				});
			}  
			
			if(ids.trim().length < 1){
				alert("<fmt:message key='user.log.in.first'/>");
				return;
			}
			var con = confirm("<fmt:message key='user.determine.deleting.selected.record'/>");
			if(!con){
				return;
			}
			sysUserPlatformFacade.delKeyWordsInfo(ids,function(data){
				var msgs = eval("("+data+")");
				if (msgs.isError) {
					alert(msgs.errorMessage);
					return;
				}
				getKeyWordsInfoList(${sysUserinfo.suinId});
			});
		}
		
		function addSelfTag(){
			var infokeywords =document.getElementById('infoSubscribtionkeyWords');
			if(infokeywords.value.trim() == '' || infokeywords.value.trim()== "<fmt:message key='user.key.word.separate'/>"){
				alert("<fmt:message key='user.key.word.separate'/>"); 
				infokeywords.focus();
				return ;
			} else { 
			    if(${empty sysUserinfo}){
					alert("<fmt:message key='user.log.in.first'/>");
					return;
				}
				
				var b = valinfoSubName();
				if(b == '0'){
					return;
				}
			
				var keyWords = infokeywords.value.trim();
				sysUserPlatformFacade.addKeyWordsInfo(keyWords,function(data){
					var msgs = eval("("+data+")");
					if (msgs.isError) {
						alert(msgs.errorMessage);
						return;
					}
					getKeyWordsInfoList(${sysUserinfo.suinId});
					infokeywords.value = "<fmt:message key='user.key.word.separate'/>";
				});
			}
		}
		
		function valinfoSubName(){
			var size = $("#keyWordsContent").children("li").length;
			if(size > 14){
				alert("<fmt:message key='user.subacribe.max.items'/>");
				return '0';
			}else{
				return '1';
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
		  			<a href="<%=path %>/index" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='user.conter'/></a> >> 
		  			<fmt:message key='left.resource.subscription'/>
		  			</b>
	  			</div>
		  
				<form action="<%=path %>/usercenter/infoSubscription.htm?action=ignoreResource" id="formn" method="post">
					<input type="hidden" name="currentPage" value="${pagination.currentPage }"/>
					<input type="hidden" name="ignoreIds" value="" id="ignoreIds"/>
					<input type="hidden" name="display" value="block"/>
				</form>

				   	<div class="znDiv">
					     <div class="znDiv-left">
					     	<h2><fmt:message key='user.subscribe'/></h2>
					     	<ul id="keyWordsContent"></ul>
					     </div>
					     <div class="znDiv-right">
					     	<h2><fmt:message key='user.subscribe.item'/><span><fmt:message key='user.most.subscribe'/></span></h2>
							<form action="<%=path %>/platform/web/student/znUserData.htm" id="formn" method="post" onsubmit="return saveForm();">
						     	<input type="hidden" name="action" value="saveZnData"/>
						     	<table cellpadding="0" cellspacing="10" border="0" class="znTable">
						     		<tr>
						     			<td>
						     				<input type="text" name="keyWords" id='infoSubscribtionkeyWords'  class="znName" value="<fmt:message key='user.key.word.separate'/>"onfocus="javascript: if(this.value.trim() == <fmt:message key='user.key.word.separate'/>) {this.value='';}"onblur="javascript: if(this.value.trim() == '') {this.value=<fmt:message key='user.key.word.separate'/>;}"/>
						     			</td>
						     		</tr>
						     		<tr>
						     			<td>
						     				<input type="button" class="tbtn znBtn" value="<fmt:message key='user.book'/>&nbsp;<fmt:message key='user.read'/>" onclick="javascript:addSelfTag();"/>
						     				<input type="button" class="tbtn znBtn right" value="<fmt:message key='user.heavy'/>&nbsp;<fmt:message key='user.the'/>" 
						     				onclick="javascript: document.getElementById('infoSubscribtionkeyWords').value=<fmt:message key='user.key.word.separate'/>;"/>
						     			</td>
						     		</tr>
						     	</table>
							</form>
					     </div>
					     <img src="<%=path %>/images/usercenter/infoSub/b_shadow.png" width="100%"/>
				   </div>
		   
		   		 <div class="znList">
					<ul>
		     		<li>
		     			<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navbtn selected"><fmt:message key='user.subscribe.resources'/></a> &nbsp;|&nbsp;
                    	<a href="<%=path %>/usercenter/infoSubscription.htm?action=initIgnoredResource&display=block" class="navbtn"><fmt:message key='user.lgnoring.resource.management'/></a>
                   		<!-- 
                    	<a href="javascript:riss2.subscription.ignoreResources();" class="znBtn">
                    			<b>忽略选中</b>
                   		</a>
                   		 -->
		     		</li>
					<c:if test="${empty result}">
						<li style="border: none;font-size: 15px;color:#888;text-align: center;"><fmt:message key='user.re.subscribe'/></li>
					</c:if>
					<c:forEach items="${result}" var="v">
				     		<li>
				     			<table cellpadding="0" cellspacing="0" border="0" class="resultTable">
				     				<tr>
				     					<td width="500">
							     			<h3><a href="<%=basePath %>res/${v.rsdaId }" target="_blank">${v.rsdaTitle }</a></h3>
				     					</td>
				     					<td rowspan="2">
											<a onclick="riss2.subscription.ignoreResource(${v.rsdaId });" class="isbtn right"><fmt:message key='user.ignore'/></a>
							            	<a onclick="riss2.subscription.addFavorite(${v.rsdaId })" class="isbtn right mgr"><fmt:message key='user.collection'/></a>
				     					</td>
				     				</tr>
				     				<tr>
				     					<td>
											<div style="font-size:12px;color:#84878C">
												<span class="ts"><fmt:message key='create.author'/>：</span>${v.rsdaCreator }&nbsp;&nbsp;
												<span class="ts"><fmt:message key='repeat.date'/>：</span><fmt:formatDate pattern="yyyy-MM-dd" value="${v.rsdaBuilddate}"/>&nbsp;&nbsp;
											</div> 
				     					</td>
				     				</tr>
				     			
				     			</table>
			     			</li>
				     </c:forEach>
				     	<c:if test="${pagination.totalPages>0}">
						    <%@include file="../pc/common/turnPage.jsp" %>
					    </c:if>
					</ul>
				</div> 
		   		
		   
			</div> 
			<div class="clear"></div>
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
			<div class="clear"></div>
		</div>
		<script type="text/javascript">
			if(${!empty sysUserinfo}){
				getKeyWordsInfoList(${sysUserinfo.suinId});// 初始化我的标签
			}
		</script>
		<%@ include file="../pc/common/footer.jsp"%>
		
