<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.core.MmSettingConstant"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp"%>
<link rel="stylesheet" href="<%=path %>/css/platform/usercenter.css" type="text/css"></link>
<script type='text/javascript' src="<c:url value='/dwr/engine.js'/>"> </script>
<script type='text/javascript' src="<c:url value='/dwr/util.js'/>"> </script>
<script type='text/javascript' src="<c:url value='/dwr/interface/sysUserPlatformFacade.js'/>"></script>
<script type="text/javascript">

     $(document).ready(function(){
     
     	/**鼠标移动效果*/
		$(".uclia").hover(function(){
			$(this).addClass('hover');
		},function(){
			$(this).removeClass('hover');
		});
		
		/**高亮选中菜单*/
		valNavLeft();
	});
	
	function valNavLeft(){
		var nowHref = window.location.href; 
		var action = nowHref.substring(nowHref.lastIndexOf("/")+1,nowHref.indexOf(".htm"));
		
		//左侧菜单-特殊的单独处理
		if(action == 'createScIssue'){
			action = 'scIssue';
		}else if(action == 'createScConsultation'){
			action = 'scConsultation'; 
		}else if(action == 'favorite'){
		 	action = nowHref.substring(nowHref.lastIndexOf("action=")+7);
		}
		
		$(".uclia").each(function(){ 
			 var childhref = $(this).attr('href');
			 var childaction = childhref.substring(childhref.lastIndexOf("/")+1,childhref.indexOf(".htm"));
			 if(childaction == 'favorite'){
			 	childaction = childhref.substring(childhref.lastIndexOf("action=")+7);
			 }
			 if(childaction == action){
			 	$(this).addClass('selected');
			 }
		}); 
		
	}
</script>
<div class="nav_left box-shadow">
		<div id="tou1">
			<table cellpadding="0" cellspacing="15" border="0">
				<tr>
					<td>
						<img src="<%=SysConfigInfo.FILE_URL_PATH %>${sysUserinfo.suinImageUrl}" onerror="this.src='<%=path %>/images/platform/face.jpg'"/>
					</td>
				</tr>
				<tr><td><p>${sysUserinfo.suinLoginName }</p></td></tr>
			</table>
		</div>
	
		<div class="nav_left_list" >
			<ul>
               	<!-- 消息订阅 -->
                <li>
                	<a class="uclia" href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block">
                		<fmt:message key="left.resource.subscription"/><i></i>
                	</a>
                </li>
           		<!-- RSS订阅 -->
                <li>
                	<a class="uclia" href="<%=path %>/usercenter/rssUserData.htm?action=initUserRssData">
                		<fmt:message key="left.subscribe"/><i></i>
                	</a>
               	</li>
               	<!-- 文献传递 -->
            	<li>
            		<a class="uclia" href="<%=path %>/usercenter/docudelivery.htm?action=initDocuView">
	            		<fmt:message key="left.document.delivery"/><i></i>
	            	</a>
	            </li>
               	<!-- 文献求助 -->
            	<li>
            		<a class="uclia" href="<%=path %>/platform/scIssue.htm?action=initPlatformScIssueView">
	            		<fmt:message key="left.literature.seek.help"/><i></i>
	            	</a>
	            </li>
               	<!-- 参考咨询 -->
                <li>
                	<a class="uclia" href="<%=path %>/platform/scConsultation.htm?action=initConsultationView">
              	  		<fmt:message key="left.reference.consultation"/><i></i>
              	  	</a>
             	</li>
                <!-- 我的收藏 -->
                <li>
                	<a class="uclia" href="<%=path %>/user/favorite.htm?action=initScFavoriteView">
                		<fmt:message key="left.my.collection"/><i></i>
                	</a>
               	</li>
                <!-- 我的评论 -->
                <%--  <li>
                	<a class="uclia" href="<%=path %>/user/favorite.htm?action=initMyComtView">
                		<fmt:message key="left.my.comments"/><i></i>
                	</a>
               	</li>--%>
                <!-- 分割线 -->
                <!-- 资源管理 -->
                <%if(ConstantsObj.getSysProperty(MmSettingConstant.getUserUploadResourceIsOpen()).equals("1")){ %> 
                 <li>
                 	<a class="uclia" href="<%=path %>/resource.htm?action=list">
                 		<fmt:message key="left.resource.management"/><i></i>
                 	</a>
                </li>
                 <%} %>
               	<!-- FAQ知识库 -->
                <li>
                	<a class="uclia" href="<%=path %>/platform/faq.htm?action=initPlatformFaqView">
                		<fmt:message key="left.knowledge.base"/><i></i>
                	</a>
               	</li>
               	<!--个人设置 -->
                <li>
                	<a class="uclia" href="<%=path %>/member/center">
                		<fmt:message key="left.personal.settings"/><i></i>
                	</a>
               	</li>                         
            </ul>
		</div>
</div>
