<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.BasicDataUtil"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.metaparadigm.jsonrpc.JSONRPCBridge"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.core.TempletConfig"%>
<%@page import="cn.com.adtech.riss2.domain.SysUserType"%>
<%@page import="cn.com.adtech.riss2.jdbcms.ToolUnit"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	List<ResElementPool> EleDataList = (List<ResElementPool>)request.getAttribute("EleDataList");
	ResSourceData resource = (ResSourceData)request.getAttribute("resource");
	
	IResourceRpc resourceRpc = (IResourceRpc)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("resourceRpc");
	
	JSONRPCBridge.registerObject("resourceRpc",resourceRpc);
	
	int reqType = MyRequestUtil.getIntAttribute(request,"reqType");
	if(reqType == 0){
		reqType = MyRequestUtil.getIntParameter(request,"reqType");
	}
	
	long memberId = MyRequestUtil.getMemberId(request);
 %>
<%@ include file="../pc/common/header.jsp"%>
	
		<!-- 会员中心首页头部  -->
		<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	    <link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
		<link rel="stylesheet" href="<%=path %>/css/extbutton.css" type="text/css"></link>
	    <link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
		<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
		<script type='text/javascript' src="<c:url value='/dwr/engine.js'/>"> </script>
		<script type='text/javascript' src="<c:url value='/dwr/util.js'/>"> </script>
		<script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
		<script type='text/javascript' src="<c:url value='/dwr/interface/memberResource.js'/>"></script>
		<script type="text/javascript" src="<%=path %>/js/platform/ResourceData.js?ui=zh-zw-20261025-1"></script>
		<script type="text/javascript">
		    var path = '<%=request.getContextPath() %>';
		    function getPath(){
	            return "<%=path %>";
	        }
		    var maxHeight = <%=ConstantsObj.TEXTAREA_MAX_HEIGHT %>;
		    var addOffset = null;
		</script>	    
		
		<!-- 会员中心中间 ============== 开始 ============== -->
		<div class="p-cnt ucdiv shadow">
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  		<b>
			  		<fmt:message key="edit.current.location"/>：			
			  		<a href="<%=path %>/index.htm" style="color: #4f4f4f"><fmt:message key="home.page"/></a> >> 
		  		  	<a href="<%=path %>/resource.htm?action=list" style="color: #4f4f4f"><fmt:message key="edit.personal.works.resources"/></a> >> 
			  		<fmt:message key="edit.revise.resources"/>
		  		</b>
		  		</div>
				
				<div class="resourceRemark" >
				&nbsp; &nbsp;  &nbsp; <fmt:message key="edit.permanent.preservation"/>
	            </div>
				
				<!-- 资源编辑表单开始 -->
				<c:if test="<%=memberId > 0 %>">
				<div style="width:100%;">
				 <form action="<%=path %>/sourceinput.htm?action=updateResourceData" name="sourceform" enctype="multipart/form-data" id="sourceform" method="post"> 
				 <input type="hidden" name="store" value="<%=resource.getRsdaRmstId() %>"></input>
				 <input type="hidden" name="stype" value="<%=resource.getRsdaRltyId() %>"></input>
				 <input type="hidden" name="lanId" value="${requestScope.languageId }"></input>
				 <input type="hidden" name="sid" value="<%=resource.getRsdaId() %>"></input>
				 <input type="hidden" name="reqType" value="<%=reqType %>"></input>
				 <input type="hidden" name="memberId" value="<%=resource.getRsdaUserId() %>"></input>
				 <input type="hidden" name="rihiId" value="${param.rihiId }"></input>
				 <input type="hidden" name="inputType" value="<%=ResSourceData.AUTHORTYPE_MEMBER_INPUT %>"></input>
				 <!-- <input type="hidden" name="language_id" value="${requestScope.language.rltyId }"> -->
				 
				 <table></table>
				 <!-- 循环文档开始 -->
				 <table cellpadding="0" width="100%" border="0" cellspacing="1" style="width: 100%">
				 	<tr class="table_title">
			        	<td colspan="2" align="left">
			        		<b class="title_font"> &nbsp;&nbsp;<fmt:message key="edit.the.content.of.resources"/></b>
			        	</td>
			        </tr>
				     <%
				       int i=1;
				       String bgc = "";
				     %>
				     <c:forEach items="<%=EleDataList %>" var="v" varStatus="s">
				        <c:if test="${v.repoType < 3 }">
				        <% i++; 
				           if(i % 2 == 0){
				              bgc = "#FAFAFA";
				           }else{
				              bgc = "#ffffff";
				           }
				        %>
				        
				         <!-- 循环核心元素 -->
				         <tr height="25px;" bgcolor="<%=bgc %>">
				             <td width="180px" align="right">
				               <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img>  
				               <strong class="td_font">${v.repoTitle }(${v.repoName })：</strong> 
				             </td>
				             <td align="left"> 
				             	<c:choose>
				             		<c:when test="${fn:toLowerCase(v.repoName) == 'accessrights' }">
					             		  <select name="${v.repoId }" id="${v.repoId }" sourceElement="true" style="width: 304px;margin-left: 2px;height: 22px;float:left"> 
								              <option value="<%=SysUserType.PURIVEW_DOWNLOAD %>" <c:if test="${v.repoData == 5 }"> selected="selected"</c:if>><fmt:message key="edit.downloadable"/></option>
							              	  <option value="<%=SysUserType.PURIVEW_BROWSE_SOURCE %>" <c:if test="${v.repoData == 4 }"> selected="selected"</c:if>><fmt:message key="edit.browse.the.source.text"/></option>
								              <option value="<%=SysUserType.PURIVEW_BROWSE_VIEW %>" <c:if test="${v.repoData == 3 }"> selected="selected"</c:if>><fmt:message key="edit.browse.preview"/></option>
								              <option value="<%=SysUserType.PURIVEW_CANNOT_BROWSE %>" <c:if test="${v.repoData == 2 }"> selected="selected"</c:if>><fmt:message key="edit.not.preview"/></option>
								              <option value="<%=SysUserType.PURIVEW_NOSEE %>" <c:if test="${v.repoData == 1 }"> selected="selected"</c:if>><fmt:message key="edit.not.accessible"/></option>
								          </select>
									          <a style="float:left;margin-left:6px;">
								                <c:if test="${v.repoRestrict == 'R' }">
								                    <img src="<%=path %>/images/icon/tuijian.png" title="<fmt:message key='edit.strong.recommendation'/>"></img>
								                </c:if>
								                <c:if test="${v.repoRestrict == 'M' }">
								                    <img src="<%=path %>/images/icon/bibei.png" title="<fmt:message key='edit.necessary'/>"></img>
								                </c:if>
								                <c:if test="${v.repoRestrict == 'MA' }">
								                   <img src="<%=path %>/images/icon/youzhebibei.png" title="<fmt:message key='edit.there.is.a.necessity'/>"></img>
								                </c:if>
								                <c:if test="${v.repoRestrict == 'O' }">
								                   <img src="<%=path %>/images/icon/kexuan.png" title="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "可选(O)") %>"></img>
								                </c:if>
							                 </a>
					             		</c:when>
				             		<c:when test="${fn:toLowerCase(v.repoName) == 'location' }">
				             			<select name="${v.repoId }" id="${v.repoId }" style="width: 304px;margin-left: 2px;height: 22px;"> 
							              <option value=""><fmt:message key="edit.please.choose"/></option>
							              <%=BasicDataUtil.getLocationOptions("") %>
							            </select>
							            
							            <script type="text/javascript">
							            	document.getElementById('${v.repoId }').value = '${v.repoData }';
							            </script>
							            <a id="location_msg" name="location_msg">
							                <c:if test="${v.repoRestrict == 'R' }">
							                    <img src="<%=path %>/images/icon/tuijian.png" title="<fmt:message key='edit.strong.recommendation'/>" class="icon"></img>
							                </c:if>
							                <c:if test="${v.repoRestrict == 'M' }">
							                    <img src="<%=path %>/images/icon/bibei.png" title="<fmt:message key='edit.necessary'/>" class="icon"></img>
							                </c:if>
							                <c:if test="${v.repoRestrict == 'MA' }">
							                   <img src="<%=path %>/images/icon/youzhebibei.png" title="<fmt:message key='edit.there.is.a.necessity'/>" class="icon"></img>
							                </c:if>
							                <c:if test="${v.repoRestrict == 'O' }">
							                   <img src="<%=path %>/images/icon/kexuan.png" title="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "可选(O)") %>" class="icon"></img>
							                </c:if>
						                </a>
				             		</c:when>
				             		<c:otherwise>
						                <textarea name="${v.repoId }" 
						                       class="source_text" 
						                       restrict="${v.repoRestrict }" 
						                       onpropertychange="setTextAreaHeight(this);"
						                       onkeyup="setTextAreaHeight(this);" 
						                       onkeydown="setTextAreaHeight(this);"
							                   sourceElement="true">${v.repoData }</textarea>
						                <a id="${v.repoId }_msg">
							                <c:if test="${v.repoRestrict == 'R' }">
							                    <img src="<%=path %>/images/icon/tuijian.png" title="<fmt:message key='edit.strong.recommendation'/>" class="icon"></img>
							                </c:if>
							                <c:if test="${v.repoRestrict == 'M' }">
							                    <img src="<%=path %>/images/icon/bibei.png" title="<fmt:message key='edit.necessary'/>" class="icon"></img>
							                </c:if>
							                <c:if test="${v.repoRestrict == 'MA' }">
							                   <img src="<%=path %>/images/icon/youzhebibei.png" title="<fmt:message key='edit.there.is.a.necessity'/>" class="icon"></img>
							                </c:if>
							                <c:if test="${v.repoRestrict == 'O' }">
							                   <img src="<%=path %>/images/icon/kexuan.png" title="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "可选(O)") %>" class="icon"></img>
							                </c:if>
						                </a>
						            </c:otherwise>           
				                </c:choose>       
				             </td>
				         </tr>
				         
				         <!-- 将所有的元素上级元素再和核心相比，显示出它的所有子元素 -->
				         <c:forEach items="<%=EleDataList %>" var="vc" varStatus="sc">
				          	 <c:if test="${v.repoId == vc.repoParent }">
					         <tr height="25px;" bgcolor="<%=bgc %>">
					           <td width="180px" align="right" class="td_font"> &nbsp;
						            ${vc.repoTitle }(${vc.repoName })：
					           </td>
					           <td align="left"> 
					           		<c:choose>
					           		
					           		<c:when test="${fn:toLowerCase(vc.repoName) == 'accessrights' }">
				             			<select name="${vc.repoId }" id="${vc.repoId }" style="width: 304px;margin-left: 2px;height: 22px;"> 
							              <option value="<%=SysUserType.PURIVEW_DOWNLOAD %>" <c:if test="${vc.repoData == 5 }"> selected="selected"</c:if>><fmt:message key="edit.downloadable"/></option>
							              <option value="<%=SysUserType.PURIVEW_BROWSE_SOURCE %>" <c:if test="${vc.repoData == 4 }"> selected="selected"</c:if>><fmt:message key="edit.browse.the.source.text"/></option>
							              <option value="<%=SysUserType.PURIVEW_BROWSE_VIEW %>" <c:if test="${vc.repoData == 3 }"> selected="selected"</c:if>><fmt:message key="edit.browse.preview"/></option>
							              <option value="<%=SysUserType.PURIVEW_CANNOT_BROWSE %>" <c:if test="${vc.repoData == 2 }"> selected="selected"</c:if>><fmt:message key="edit.not.preview"/></option>
							              <option value="<%=SysUserType.PURIVEW_NOSEE %>" <c:if test="${vc.repoData == 1 }"> selected="selected"</c:if>><fmt:message key="edit.not.accessible"/></option>
							            </select>
							            
							            <a >
								             <c:if test="${vc.repoRestrict == 'R' }">
							                     <img src="<%=path %>/images/icon/tuijian.png" title="<fmt:message key='edit.strong.recommendation'/>" class="icon"></img>
							                 </c:if>
							                 <c:if test="${vc.repoRestrict == 'M' }">
							                     <img src="<%=path %>/images/icon/bibei.png" title="<fmt:message key='edit.necessary'/>" class="icon"></img>
							                 </c:if>
							                 <c:if test="${vc.repoRestrict == 'MA' }">
							                     <img src="<%=path %>/images/icon/youzhebibei.png" title="<fmt:message key='edit.there.is.a.necessity'/>" class="icon"></img>
							                 </c:if>
							                 <c:if test="${vc.repoRestrict == 'O' }">
							                     <img src="<%=path %>/images/icon/kexuan.png" title="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "可选(O)") %>" class="icon"></img>
							                 </c:if>
							            </a>     
				             		</c:when>
					           		
				             		<c:when test="${fn:toLowerCase(vc.repoName) == 'location' }">
				             			<select name="${vc.repoId }" id="${vc.repoId }" style="width: 304px;margin-left: 2px;height: 22px;"> 
							              <option value=""><fmt:message key="edit.please.choose"/></option>
							              <%=BasicDataUtil.getLocationOptions("") %>
							            </select>
							            
							            <script type="text/javascript">
							            	document.getElementById('${vc.repoId }').value = '${vc.repoData }';
							            </script>
							            
							            <a id="location_msg" name="location_msg">
								             <c:if test="${vc.repoRestrict == 'R' }">
							                     <img src="<%=path %>/images/icon/tuijian.png" title="<fmt:message key='edit.strong.recommendation'/>" class="icon"></img>
							                 </c:if>
							                 <c:if test="${vc.repoRestrict == 'M' }">
							                     <img src="<%=path %>/images/icon/bibei.png" title="<fmt:message key='edit.necessary'/>" class="icon"></img>
							                 </c:if>
							                 <c:if test="${vc.repoRestrict == 'MA' }">
							                     <img src="<%=path %>/images/icon/youzhebibei.png" title="<fmt:message key='edit.there.is.a.necessity'/>" class="icon"></img>
							                 </c:if>
							                 <c:if test="${vc.repoRestrict == 'O' }">
							                     <img src="<%=path %>/images/icon/kexuan.png" title="<fmt:message key='edit.optional'/>" class="icon"></img>
							                 </c:if>
							            </a>     
				             		</c:when>
				             		<c:otherwise>
							              <textarea name="${vc.repoId }" 
							                     class="source_text"
							                     restrict="${vc.repoRestrict }" 
							                     sourceElement="true" 
							                     onpropertychange="setTextAreaHeight(this);"
						                         onkeyup="setTextAreaHeight(this);" 
						                         onkeydown="setTextAreaHeight(this);" >${vc.repoData }</textarea>
							              <a id="${vc.repoId }_msg">
								             <c:if test="${vc.repoRestrict == 'R' }">
							                     <img src="<%=path %>/images/icon/tuijian.png" title="<fmt:message key='edit.strong.recommendation'/>" class="icon"></img>
							                 </c:if>
							                 <c:if test="${vc.repoRestrict == 'M' }">
							                     <img src="<%=path %>/images/icon/bibei.png" title="<fmt:message key='edit.necessary'/>" class="icon"></img>
							                 </c:if>
							                 <c:if test="${vc.repoRestrict == 'MA' }">
							                     <img src="<%=path %>/images/icon/youzhebibei.png" title="<fmt:message key='edit.there.is.a.necessity'/>" class="icon"></img>
							                 </c:if>
							                 <c:if test="${vc.repoRestrict == 'O' }">
							                     <img src="<%=path %>/images/icon/kexuan.png" title="<fmt:message key='edit.optional'/>" class="icon"></img>
							                 </c:if>
						                 </a>
							        </c:otherwise>
							      </c:choose>             
					           </td>
					         </tr>
					         </c:if>
				         </c:forEach>
				       </c:if>
			       </c:forEach> 
			       </table>
			       
			       <!-- 选择分类 -->
				   <table cellpadding="0" width="100%" border="0" cellspacing="1" style="margin-top: 6px;width: 100%;" >
			        	<tr class="table_title">
				        	<td colspan="2" align="left">
				        		<b class="title_font"> &nbsp;&nbsp;<fmt:message key="edit.selection.classification"/></b>
				        	</td>
				        </tr>
				       <tr bgcolor="#FAFAFA" height="25">
				          <td width="180px" align="right" class="td_font">
				            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
				              <fmt:message key="repeat.classification.of.subjects"/>：
				          </td>
				          <td align="left">
				              <select name="rsty_id" id="rsty_id" style="width: 304px;margin-left: 2px;height: 22px;"> 
					              ${requestScope.subjectOptions }
					          </select>
					          
					          
					      </td>    
				       </tr>
				       
				        <!-- 自定义分类一 -->
					       <c:if test="${fn:length(dynamicTypeOptions) > 4 }">
						       <tr bgcolor="#FFFFFF" height="25">
						          <td width="180px" align="right" class="td_font">
						            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
						              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE) %>：
						          </td>
						          <td align="left">
						            <select name="rdty_id" id="rdty_id" style="width: 304px;margin-left: 2px;height: 22px;"> 
						              ${requestScope.dynamicTypeOptions }
						            </select>
						          </td>
						       </tr>
					       </c:if>
					       
					       <!-- 自定义分类二 -->
					       <c:if test="${fn:length(dynamicTypeOptions2) > 4 }">
						       <tr bgcolor="#FFFFFF" height="25">
						          <td width="180px" align="right" class="td_font">
						            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon" ></img> 
						              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO) %>：
						          </td>
						          <td align="left">
						            <select name="rdty_id_two" id="rdty_id_two" style="width: 304px;margin-left: 2px;height: 22px;"> 
						              ${requestScope.dynamicTypeOptions2 }
						            </select>
						          </td>
						       </tr>
					       </c:if>
					       <!-- 自定义分类三 -->
					       <c:if test="${fn:length(dynamicTypeOptions3) > 4 }">
						       <tr bgcolor="#FFFFFF" height="25">
						          <td width="180px" align="right" class="td_font">
						            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
						              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE) %>：
						          </td>
						          <td align="left">
						            <select name=rdty_id_three id="rdty_id_three" style="width: 304px;margin-left: 2px;height: 22px;"> 
						              ${requestScope.dynamicTypeOptions3 }
						            </select>
						          </td>
						       </tr>
					       </c:if>
				   
				 </table>
					   
				 
				    
				    <table cellpadding="0" width="100%" border="0" cellspacing="1" style="margin-top: 6px;width:100%;" >
				    	<tr class="table_title">
				        	<td colspan="2" align="left">
				        		<b class="title_font"> &nbsp;&nbsp;<fmt:message key="edit.file.upload"/></b>
				        	</td>
				        </tr>
				        <tr bgcolor="#FAFAFA" height="25">
				          <td width="180px" align="right" class="td_font">
				            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
				               <fmt:message key="edit.cover"/>：
				             <input type="hidden" name="bigimg_statu" id="bigimg_statu" value="0"> 
				          </td>
				          <td id="imageFileArea" align="left">
				               <%if(ToolUnit.NullToBlank(resource.getRsdaBigimgName()).length() > 3){ %>
				                  <div id="bigimgArea">
				                     <%=ToolUnit.getFileName(ToolUnit.NullToBlank(resource.getRsdaBigimgName())) %>
				                     <!-- <a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&sourceId=<%=resource.getRsdaId() %>&type=1" target="_blank">预览</a> -->
				                     <a href="javascript:delefile('bigimgArea','bigimg');"><fmt:message key="edit.delete"/></a>
				                  </div>      
				                  <input type="file" name="bigimg" id="bigimg" class="file text" style="display: none" onchange="fileChang('bigimg',true);">
				               <%}else{ %>
				                  <input type="file" name="bigimg" id="bigimg" class="file text" onchange="fileChang('bigimg',true);">
				                  &nbsp;<span style='color: #808080'><fmt:message key="edit.format"/>:jpg、jpeg、bmp、png、gif</span>
				               <%} %>
				          </td>
				        </tr>
				        
				        <tr bgcolor="#FAFAFA" height="25">
				          <td width="180px" align="right" class="td_font">
				            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
				              <fmt:message key="edit.source.file"/>：
				          </td>
				          <td align="left">
				               <%if(ToolUnit.NullToBlank(resource.getRsdaFileName()).length() > 3){ %>
				                  <div id="loadfileArea">
					                  <%=ToolUnit.getFileName(ToolUnit.NullToBlank(resource.getRsdaFileName())) %>
					                  
					                  <!-- <a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&sourceId=<%=resource.getRsdaId() %>&type=3" target="_blank">预览</a> -->
					                  <a href="javascript:deleLoadFile();"><fmt:message key="edit.delete"/></a>
				                  </div>
				                  
				                  <div id="testAA" style="display: none;">
				                   <input type="file" name="loadfile" class="file text" onchange="fileChang('loadfile');" value="test">
				                  </div>
				               <%}else{ %>
				                  <input type="file" name="loadfile" class="file text" statu="0" onchange="fileChang('loadfile');">
				               <%} %>
				               <a id="loadfile_msg"></a>
				               <input type="hidden" name="loadfile_statu" id="loadfile_statu" value="0"> 
				          </td>
				        </tr>
				     </table>
					   
					 <table border="0" width="100%" cellpadding="0" cellspacing="0" style="width: 100%;background: #fff">
					   <tr height="50px">
				          <td align="left">
					           <a href="javascript:doSave('5');" class="button-3" style="margin-right: 4px;"><b><fmt:message key="edit.preservation"/></b></a>
					           <a href="<%=path %>/resource.htm?action=list" class="button-3" onclick="window.location.href='<%=path %>/resource.htm?action=add'"><b><fmt:message key='edit.return'/></b></a>
				          </td>
				          
				          <td align="right">
				            <img src="<%=path %>/images/icon/bibei.png" class="icon" style="margin-bottom: -4px;"></img><fmt:message key='edit.necessary'/> &nbsp; 
				            <img src="<%=path %>/images/icon/youzhebibei.png" class="icon" style="margin-bottom: -4px;"></img><fmt:message key='edit.there.is.a.necessity'/> &nbsp;  
				            <img src="<%=path %>/images/icon/tuijian.png" class="icon" style="margin-bottom: -4px;"></img><fmt:message key='edit.strong.recommendation'/> &nbsp; 
				            <img src="<%=path %>/images/icon/kexuan.png" class="icon" style="margin-bottom: -4px;"></img><fmt:message key='edit.optional'/>
				            
				          </td>
				          
				       </tr>
				    </table>
				    <input type="hidden" name="req">
			    </form> 
			    
			   		  <!-- 保存结果提示区域 -->
				      <div id='SuccessMessageBox' class='SuccessMessageBox' style="position: fixed;"></div>
				      <!-- 保存等待提示区域 -->
				      <div id='WaitMessageBox' class='WaitMessage' style="position: fixed;"></div>
				      
			    <!-- 资源编辑表单END --> 
				<script type="text/javascript">initTextAreaHeight();</script>
				</div>
			   </c:if>
			   
			   	<c:if test="<%=memberId == 0 %>">
			   		<fmt:message key="edit.please.log.in"/>
			   	</c:if>
			</div> 
		</div>
		
	    ${requestScope.result }
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>
