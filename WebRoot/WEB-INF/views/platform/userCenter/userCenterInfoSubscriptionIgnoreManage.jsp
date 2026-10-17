<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../pc/common/header.jsp"%>
<link rel="stylesheet" href="<%=path %>/css/platform4.0/infoSub.css" type="text/css"></link>
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
				alert("<fmt:message key='user.choose.a.collection'/>");
			} else {
				for(var i = 0; i < checkedIds.length; i++) {
					//riss2.favorites.addFavorite(that.FAVORITE_HREF, '${sysUserinfo.suinId}', checkedIds[i]);
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
				alert("<fmt:message key='user.choose.a.recoveryn'/>");
			} else {
				document.getElementById("ignoreIds").value = checkedIds.toString();
				document.getElementById("formn").submit();
			}
		},
		ignoreResource: function(id) {
			document.getElementById("ignoreIds").value = id + ",";
			document.getElementById("formn").submit();
		},
		checkAll: function() {
			var check = document.getElementById("checkAll");
			var infoIds = document.getElementsByName("infoId");
			for(var i = 0; i < infoIds.length; i++) {
				infoIds[i].checked = check.checked; 
			}
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
		  			<fmt:message key='user.lgnoring.resource.management'/>
			  		</b></div>
		  
				<form action="<%=path %>/usercenter/infoSubscription.htm?action=restoreIgnore" id="formn" method="post">
					<input type="hidden" name="currentPage" value="${pagination.currentPage }"/>
					<input type="hidden" name="ignoreIds" value="" id="ignoreIds"/>
					<input type="hidden" name="display" value="block"/>
				</form>
				
				
				<div class="znList">
					<ul>
		     		<li>
                   		<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navbtn"><fmt:message key='user.subscribe.resources'/></a> &nbsp;|&nbsp;
                    	<a href="<%=path %>/usercenter/infoSubscription.htm?action=initIgnoredResource&display=block" class="navbtn selected"><fmt:message key='user.lgnoring.resource.management'/></a>
                   		
                   		<!-- 
                    	<a href="javascript:riss2.subscription.ignoreResources();" class="znBtn">
                    			<b>忽略选中</b>
                   		</a>
                   		 -->
		     		</li>
					<c:if test="${empty result}">
						<li style="border: none;font-size: 15px;color:#888;text-align: center;"><fmt:message key='user.no.lgnore.resources'/></li>
					</c:if>
					<c:forEach items="${result}" var="v">
				     		<li>
				     			<table cellpadding="0" cellspacing="0" border="0" class="resultTable">
				     				<tr>
				     					<td width="500">
							     			<h3><a href="<%=basePath %>res/${v.isInRsSourceId }" target="_blank">${v.isInRsSourceData.rsdaTitle }</a></h3>
				     					</td>
				     					<td rowspan="2">
											<a onclick="riss2.subscription.ignoreResource(${v.isInRsId });" class="isbtn right"><fmt:message key='user.recovery'/></a>
							            	<a onclick="riss2.subscription.addFavorite(${v.isInRsSourceId })" class="isbtn right mgr"><fmt:message key='user.collection'/></a>
				     					</td>
				     				</tr>
				     				<tr>
				     					<td>
											<div style="font-size:12px;color:#84878C">
												<span class="ts"><fmt:message key='create.author'/>：</span>${v.isInRsSourceData.rsdaCreator }&nbsp;&nbsp;
												<span class="ts"><fmt:message key='repeat.date'/>：</span><fmt:formatDate pattern="yyyy-MM-dd" value="${v.isInRsDate}"/>&nbsp;&nbsp;
											</div> 
				     					</td>
				     				</tr>
				     			
				     			</table>
			     			</li>
				     </c:forEach>
				     	<c:if test="${pagination.totalPages>0}">
						    	<li>
						    		<%@include file="/WEB-INF/views/platform/pc/common/turnPage.jsp" %>
						    	</li>
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
		<%@ include file="../pc/common/footer.jsp"%>
