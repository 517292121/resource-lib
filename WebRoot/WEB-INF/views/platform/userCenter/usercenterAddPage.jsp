<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.BasicDataUtil"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.metaparadigm.jsonrpc.JSONRPCBridge"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@page import="cn.com.adtech.riss2.core.TempletConfig"%>
<%@page import="cn.com.adtech.riss2.domain.SysUserType"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	IResourceRpc resourceRpc = (IResourceRpc)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("resourceRpc");
	JSONRPCBridge.registerObject("resourceRpc",resourceRpc);
	
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
		<script type="text/javascript">
		    var path = '<%=request.getContextPath() %>';
		    function getLiter(){
		    	var rmstid = document.all.rmstId.value;
		    	if(rmstid.length < 1){
		    	    return;
		    	}
		    	memberResource.getListOptionByStore(rmstid,function(data){
		    		document.all.rltyId_Area.innerHTML = "<select name='rltyId' id='rltyId'>" + data + "</select>";
		    	});
		    }
		    function getPath(){
	            return "<%=path %>";
	        }
		    var maxHeight = <%=ConstantsObj.TEXTAREA_MAX_HEIGHT %>;
		    var addOffset = 0;
		    
		    /**检查上传文件格式 stintTypes允许的文件格式 **/
			function stintTypes(fileItem,stintTypes){ 
				var filetag = stintTypes.split("、");
			    var result = false;  
				var filepath=fileItem.value;
				if(filepath)filepath=filepath.substring(filepath.lastIndexOf('.')+1,filepath.length).toLowerCase();
			    for(i=0; i<filetag.length; i++){
			         if(filetag[i].toLowerCase() == filepath){
			             result = true;
			             break;
			         }
			    }
			    if(result == false){
			    	fileItem.value = "";
			    	var allTypes = stintTypes;
			    	if(stintTypes.length > 20){
			    	    stintTypes = stintTypes.substring(0,20) + "...";
			    	}
			    	if(fileItem.name == "viewfile"){
			    		var content = "<input type='file' name='viewfile' class='file text' onchange='stintTypes(this,\"" + stintTypes + "\");'>";
			    		if(stintTypes){
			    			content += "&nbsp;<span style='color: #808080' title='"+allTypes+"'>"+<fmt:message key='edit.format'/>+": " + stintTypes + "</span>";
			    		}
			    		document.getElementById("viewFileArea").innerHTML = content;
						alert(<fmt:message key='addPage.file.format.only'/> +","+<fmt:message key='addPage.please.rechoose'/>);
			    	}else if(fileItem.name == "loadfile"){
			    		var content = "<input type='file' name='loadfile' class='file text' onchange='stintTypes(this,\"" + stintTypes + "\");'>";
			    		if(stintTypes){
			    			content += "&nbsp;<span style='color: #808080' title='"+allTypes+"'><fmt:message key='edit.format'/>+": " + stintTypes + "</span>";
			    		}
			    		document.getElementById("loadFileArea").innerHTML = content;
			    		alert(<fmt:message key='addPage.source.file.format.only'/>+ allTypes + ','+<fmt:message key="addPage.please.rechoose"/>);
			    	}
			    }
			    return;
			}
		</script>	    
		<script type="text/javascript" src="<%=path %>/js/platform/ResourceData.js"></script>
	<style type="text/css">
		.icon{float: none;}
	</style>
   		
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
		  			<fmt:message key="addPage.new.entry"/>
		  		</b>
		  	</div>
				
				<div class="resourceRemark" >
				&nbsp; &nbsp;  &nbsp; <fmt:message key="edit.permanent.preservation"/>
	            </div>
				
				<c:if test="<%=memberId > 0 %>">
				<div style="width:100%;">
				<form action="<%=basePath %>/sourceinput.htm?action=saveResourceData" name="sourceform" id="sourceform" enctype="multipart/form-data" method="post" onsubmit="return checkform();"> 
					 <input type="hidden" name="store" value="${requestScope.store }">
					 <input type="hidden" name="stype" value="${requestScope.stype }">
					 <input type="hidden" name="req" id="req" value="5">
					 <input type="hidden" name="language_id" value="<%=ConstantsObj.MEMBER_RESOURCE_DEFAULT_LANGUAGE %>">
					 <input type="hidden" name="inputType" id="inputType" value="<%=ResSourceData.AUTHORTYPE_MEMBER_INPUT %>">
					 <input type="hidden" name="memberId" value="<%=memberId %>">
					 <input type="hidden" name="inputType" value="<%=ResSourceData.AUTHORTYPE_MEMBER_INPUT %>">
						<table>
						</table>
					
					 <table cellpadding="0" border="0" cellspacing="1" style="width: 100%">
				        <tr class="table_title">
				        	<td colspan="2" align="left">
				        		<b class="title_font"> &nbsp;&nbsp;<fmt:message key="addPage.input.resources.centent"/></b>
				        	</td>
				        </tr>
					     <%
					       int i=1;
					       String bgc = "";
					     %>
					     <c:forEach items="${requestScope.elementList }" var="v" varStatus="s">
					        <c:if test="${v.repoType < 3 }">
					        <% i++; 
					           if(i % 2 == 0){
					              bgc = "#FAFAFA";
					           }else{
					              bgc = "#ffffff";
					           }
					        %>
					         <!-- 循环核心元素 -->
					         <tr height="27px;" bgcolor="<%=bgc %>">
					             <td width="180px" align="right">
					               <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon" /> 
					               <strong class="font1"> ${v.repoTitle }(${v.repoName })：</strong> 
					             </td>
					             <td align="left" valign="bottom"> 
					             	<c:choose>
					             		<c:when test="${fn:toLowerCase(v.repoName) == 'accessrights' }">
					             		  <select name="${v.repoId }" id="${v.repoId }" sourceElement="true" style="width: 304px;margin-left: 2px;height: 22px;float:left"> 
								                 <option value="<%=SysUserType.PURIVEW_DOWNLOAD %>"><fmt:message key='edit.downloadable'/></option>
								                 <option value="<%=SysUserType.PURIVEW_BROWSE_SOURCE %>"><fmt:message key='edit.browse.the.source.text'/></option>
								                 <option value="<%=SysUserType.PURIVEW_BROWSE_VIEW %>"><fmt:message key='edit.browse.preview'/></option>
								                 <option value="<%=SysUserType.PURIVEW_CANNOT_BROWSE %>"><fmt:message key='edit.not.preview'/></option>
								                 <option value="<%=SysUserType.PURIVEW_NOSEE %>"><fmt:message key='edit.not.accessible'/></option>
								          </select>
									          <a id="location_msg" name="location_msg" style="float:left;margin-left: 7px;">
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
								                   <img src="<%=path %>/images/icon/kexuan.png" title="<fmt:message key='edit.optional'/>"></img>
								                </c:if>
							                 </a>
					             		</c:when>
					             		<c:when test="${fn:toLowerCase(v.repoName) == 'location' }">
					             		  <select name="${v.repoId }" id="${v.repoId }" sourceElement="true" style="width: 304px;margin-left: 2px;height: 22px;"> 
								              <option value=""><fmt:message key="edit.please.choose"/></option>
								              <%=BasicDataUtil.getLocationOptions("") %>
								          </select>
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
								                   <img src="<%=path %>/images/icon/kexuan.png" title="<fmt:message key='edit.optional'/>" class="icon"></img>
								                </c:if>
							                 </a>
					             		</c:when>
						                <c:otherwise>
							                <textarea name="${v.repoId }" 
							                       class="source_text" 
							                       restrict="${v.repoRestrict }" 
							                       sourceElement="true" 
							                       title="${v.repoTitle }" 
							                       onpropertychange="setTextAreaHeight(this);"
							                       onkeyup="setTextAreaHeight(this);" 
							                       onkeydown="setTextAreaHeight(this);"></textarea>
							                 <a id="${v.repoId }_msg" name="${v.repoId }_msg">
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
								                   <img src="<%=path %>/images/icon/kexuan.png" title="<fmt:message key='edit.optional'/>" class="icon"></img>
								                </c:if>
							                </a>      
						                </c:otherwise>       
					                </c:choose>     
					                
					                
					                <!--<img src="<%=path %>/images/application_put.png" title="${v.repoCode }"></img>--> 
					             </td>
					         </tr>
					         
					         <!-- 将所有的元素上级元素再和核心相比，显示出它的所有子元素 -->
					         <c:forEach items="${requestScope.elementList }" var="vc" varStatus="sc">
					          	 <c:if test="${v.repoId == vc.repoParent }">
					          	 <!-- 循环子元素 -->
						         <tr height="27px;" bgcolor="<%=bgc %>">
						           <td width="180px" align="right" class="font1"> 
						            &nbsp;${vc.repoTitle }(${vc.repoName })：
						           </td>
						           <td align="left"> 
						             <c:choose>
						             	<c:when test="${fn:toLowerCase(vc.repoName) == 'accessrights' }">
					             		  <select name="${vc.repoId }" id="${vc.repoId }" sourceElement="true" style="width: 304px;margin-left: 2px;height: 22px;float:left"> 
								                 <option value="<%=SysUserType.PURIVEW_DOWNLOAD %>"><fmt:message key='edit.downloadable'/></option>
								                 <option value="<%=SysUserType.PURIVEW_BROWSE_SOURCE %>"><fmt:message key='edit.browse.the.source.text'/></option>
								                 <option value="<%=SysUserType.PURIVEW_BROWSE_VIEW %>"><fmt:message key='edit.browse.preview'/></option>
								                 <option value="<%=SysUserType.PURIVEW_CANNOT_BROWSE %>"><fmt:message key='edit.not.preview'/></option>
								                 <option value="<%=SysUserType.PURIVEW_NOSEE %>"><fmt:message key='edit.not.accessible'/></option>
								          </select>
									          <a style="float:left;margin-left:6px;" id="location_msg" name="location_msg">
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
								                   <img src="<%=path %>/images/icon/kexuan.png" title="<fmt:message key='edit.optional'/>"></img>
								                </c:if>
							                 </a>
					             		</c:when>
						             	<c:when test="${fn:toLowerCase(vc.repoName) == 'location' }">
					             		  <select name="${vc.repoId }" id="${vc.repoId }" sourceElement="true" style="width: 304px;margin-left: 2px;height: 22px;"> 
								              <option value=""><fmt:message key='edit.please.choose'/></option>
								              <%=BasicDataUtil.getLocationOptions("") %>
								          </select>
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
								                    restrict="${vc.repoRestrict }" 
								                    sourceElement="true" 
								                    title="${vc.repoTitle }" 
								                    class="source_text" 
								                    onpropertychange="setTextAreaHeight(this);"
								                    onkeyup="setTextAreaHeight(this);" 
							                        onkeydown="setTextAreaHeight(this);"/></textarea>
								           <a id="${vc.repoId }_msg" name="${vc.repoId }_msg">
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
						             
					                 <!-- <img src="<%=path %>/images/application_put.png" title="${vc.repoCode }"></img> --> 
						           </td>
						         </tr>
						         </c:if>
					         </c:forEach>
					         
					       </c:if>
					         
				       </c:forEach> 
				       </table>
				       
				       <!-- 选择分类  -->
				       <table cellpadding="0" width="100%" border="0" cellspacing="1" style="margin-top: 6px;width: 100%">
				      	   <tr class="table_title">
					        	<td colspan="2" align="left">
					        		<b class="title_font"> &nbsp;&nbsp;<fmt:message key="edit.selection.classification"/></b>
					        	</td>
					       </tr>
				      	   <tr bgcolor="#FFFFFF" height="25">
					          <td width="180px" align="right" class="td_font">
					            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
					              <fmt:message key="addPage.classification.medium.graph"/>：
					          </td>
					          <td align="left"><input name="rmty_id" id="rmty_id" class="text" onblur="OnCheckZTF();" style="width: 300px;margin-left: 2px;">
					              <a id="rmtyMessage" name="rmtyMessage"></a>
					          </td>
					       </tr>
					       <tr bgcolor="#FAFAFA" height="25">
					          <td width="180px" align="right" class="td_font">
					            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
					             <fmt:message key="repeat.classification.of.subjects"/>：
					          </td>
					          <td align="left">
					              <select name="rsty_id" id="rsty_id" style="width: 300px;margin-left: 2px;height: 22px;"> 
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
						            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
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
					 
					 <!-- 文件上传 -->
					     <table cellpadding="0" width="100%" border="0" cellspacing="1" style="margin-top: 6px;width:100%">
					       <tr class="table_title">
					        	<td colspan="2" align="left">
					        		<b class="title_font"> &nbsp;&nbsp;<fmt:message key="edit.file.upload"/></b>
					        	</td>
					       </tr>
					        
					        <tr bgcolor="#FAFAFA" height="25">
					          <td width="180px" align="right" class="td_font">
					            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
					              <fmt:message key="edit.cover"/>：
					          </td>
					          <td id="imageFileArea" align="left" style="padding-left: 2px;">
					             <input type="file" name="bigimg" class="file text" onchange="checkImageFileType(this);">&nbsp;<span style="color: #808080"><fmt:message key="edit.format"/>:jpg、jpeg、bmp、png、gif</span>
					          </td>
					        </tr>
					        <tr bgcolor="#FFFFFF" height="25">
					          <td width="180px" align="right" class="td_font">
					            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
					               <fmt:message key="addPage.preview.area"/>：
					          </td>
					          <td id="viewFileArea" align="left" style="padding-left: 2px;">
					               <input type="file" name="viewfile" class="file text" onchange="stintTypes(this,'${stintTypes}');">&nbsp;<c:if test="${!empty stintTypes}"><span style="color: #808080" title="${stintTypes}"><fmt:message key='edit.format'/>:
					               <c:if test="${fn:length(stintTypes) > 20}">
					               	${fn:substring(stintTypes,0,20) }... 
					               </c:if>
					               <c:if test="${fn:length(stintTypes) <= 20}">
					                 ${stintTypes }
					               </c:if>
					               </span></c:if>
					               <!-- 
					               <input name="viewfile" class="text" style="width: 256px;margin-left: 2px;">
					               <input type="button" value="上传" class="web_button" onclick="uploadfile(viewfile);">
					                -->
					          </td>
					        </tr>
					        
					        <tr bgcolor="#FAFAFA" height="25">
					          <td width="180px" align="right" class="td_font">
					            <img src="<%=path %>/images/extform/bullet_add_2.png" class="icon"></img> 
					              <fmt:message key="edit.source.file"/>：
					          </td>
					          <td id="loadFileArea" align="left" style="padding-left: 2px;">
					               <input type="file" name="loadfile" class="file text" onchange="stintTypes(this,'${stintTypes}');">&nbsp;<c:if test="${!empty stintTypes}"><span style="color: #808080" title="${stintTypes}"><fmt:message key='edit.format'/>:
						               <c:if test="${fn:length(stintTypes) > 20}">
						               	${fn:substring(stintTypes,0,20) }... 
						               </c:if>
						               <c:if test="${fn:length(stintTypes) <= 20}">
						                 ${stintTypes }
						               </c:if>
					               </span></c:if>
					               <a id="loadfile_msg"></a>
					          </td>
					        </tr>
					     </table>
					     <table border="0" width="100%" cellpadding="0" cellspacing="0" style="width: 100%">
						   <tr style="background-color: #ffffff" height="50px">
					          <td width="180px">
					               <a href="javascript:void(0);" class="button-3" onclick="doSave('5');" style="margin-right: 4px;"><b><fmt:message key="edit.preservation"/></b></a>
					               <a href="javascript:void(0);" class="button-3" onclick="window.location.href='<%=path %>/resource.htm?action=add'"><b><fmt:message key="edit.return"/></b></a>
					          </td>
					          
					          <td align="right">
					            <img src="<%=path %>/images/icon/bibei.png" class="icon" style="margin-bottom: -4px;"></img><fmt:message key='edit.necessary'/> &nbsp; 
					            <img src="<%=path %>/images/icon/youzhebibei.png" class="icon" style="margin-bottom: -4px;"></img><fmt:message key='edit.there.is.a.necessity'/> &nbsp;  
					            <img src="<%=path %>/images/icon/tuijian.png" class="icon" style="margin-bottom: -4px;"></img><fmt:message key='edit.strong.recommendation'/> &nbsp; 
					            <img src="<%=path %>/images/icon/kexuan.png" class="icon" style="margin-bottom: -4px;"></img><fmt:message key='edit.optional'/> 
					            
					          </td>
					       </tr>
					    </table>
				    </form> 
				    <input type="hidden" name="loadfile_statu" value="2"> 
				     
				     
				      <!-- 资源保存成功的图片提示 -->
				      ${requestScope.result }
				      
				      <!-- 保存结果提示区域 -->
				      <div id='SuccessMessageBox' class='SuccessMessageBox' style="position: fixed;"></div>
				      <!-- 保存等待提示区域 -->
				      <div id='WaitMessageBox' class='WaitMessage' style="position: fixed;"></div>
				      
				      <script type="text/javascript">initTextAreaHeight(4);</script>
				</div>
			   </c:if>
			   
			   	<c:if test="<%=memberId < 1 %>">
			   		<fmt:message key="edit.please.log.in"/>
			   	</c:if>
			</div> 
			<div class="clear"></div>
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>
