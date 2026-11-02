<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.core.BasicDataUtil"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@page import="cn.com.adtech.riss2.core.TempletConfig"%>
<%@page import="cn.com.adtech.riss2.domain.SysUserType"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	List<ResElementPool> elementList = (List<ResElementPool>)request.getAttribute("elementlist");
	long store = MyRequestUtil.getLongAttribute(request,"rmstId");
	long stype = MyRequestUtil.getLongAttribute(request,"rltyId");
	
	IResourceRpc resourceRpc = (IResourceRpc)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("resourceRpc");
	JSONRPCBridge.registerObject("resourceRpc",resourceRpc);
	
	long userid = MyRequestUtil.getLoginUserId(request);
%>
 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>资源录入</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
    <link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
    <link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/extbutton.css" type="text/css"></link>
    <link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
    <script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
    <script type="text/javascript" src="<%=path %>/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=path %>/plugin/layer/layer.js"></script>
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/build/ResourceData.js?v=discipline-input-20261031-2"></script>
	<link rel="stylesheet" href="<%=path%>/css/console/resource-association.css?v=20261025-1" />
	<script defer="defer" src="<%=path%>/viewsjs/resource/build/resource-association.js?v=20261025-1"></script>
	
	<script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script>
	<script type="text/javascript" src="<%=path %>/js/progressform.js?v=maintenance-return-20261025-2"></script>
	
	<style type="text/css">
	   *{
	      font-size: 12px;
	      font-family: 宋体;
	   }
	   
	  .prompt-layer{width: 400px; height: 80px; display: none; background: #FFF; text-align: center;}
	  .prompt-layer .progress { position:relative; width:300px; min-height:20px;border: 1px solid #ddd; padding: 1px; border-radius: 3px; background: #FFF; margin: 15px auto; }
	  .prompt-layer .progress .bar {position:absolute; left:0%;background-color: #B4F5B4; width:0%; height:20px; border-radius: 3px;}
	  .prompt-layer .progress .percent { position:absolute; display:inline-block; top:3px; left:1%; }

	</style>
    <script type="text/javascript">
       function getPath(){
           return "<%=path %>";
       }
       function viewSource(){
           document.all.req.value = "3";
           document.all.sourceform.submit();
       }
       var maxHeight = <%=ConstantsObj.TEXTAREA_MAX_HEIGHT %>;
       var addOffset = null;
       
       function goback(){
		   if (${maintenance == true}) { window.location.href='<%=path%>/console/data/resource/SourceManager.htm?action=managerResourceList'; return; }
		   var id = $("input[name='rmin_id']").val();
		   window.location.href = "<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id="+id;
	   }
    </script>
</head>



<body>
	 <c:if test="<%=userid>0 %>">
	 <form action="<%=path %>/console/data/inputSingle.htm?action=saveResourceData" name="sourceform" id="sourceform" data-save-return="${maintenance ? 'maintenance' : ''}" enctype="multipart/form-data" method="post" onsubmit="return checkform();"> 
	 <input type="hidden" name="store" value="${requestScope.store }"></input>
	 <input type="hidden" name="stype" value="${requestScope.stype }"></input>
	 <input type="hidden" name="rmin_id" value="${requestScope.rmin_id }"></input>
	 <input type="hidden" name="req" id="req" value="${req }"></input>
	 <input type="hidden" name="rstyId" id="rstyId" value="${rstyId }"></input>
	 <input type="hidden" name="userid" value="<%=userid %>"></input>
	 <input type="hidden" name="inputType" value="<%=ResSourceData.AUTHORTYPE_ADMIN_INPUT %>"></input>
	 <c:if test="${maintenance}"><input type="hidden" name="maintenance" value="1" /></c:if>
	 
	 <!-- 循环文档开始 -->
	 <div class="title" style="margin-top: 0;height: 26px">录入资源内容, 作业：【<a href="<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id=${param.rmin_id }" style="font-weight: normal">${requestScope.missName }</a>】</div>
	 <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	     <tr bgcolor="#FAFAFA"><td width="250" align="right">资源类型：</td><td><c:out value="${resourceTypeName}" /> <c:if test="${maintenance}">（<a href="<%=path%>/console/data/inputSingle.htm?action=initMaintenance">重新选择资源类型 / 所属作业</a>）</c:if></td></tr>
	     <%@ include file="researchAssociation.jspf" %>
	 
	    <%
	       int i=1;
	       String bgc = "";
	     %>
	     <c:forEach items="<%=elementList %>" var="v" varStatus="s">
	        <c:if test="${v.repoType < 3 }">
	        <% i++; 
	           if(i % 2 == 0){
	              bgc = "#FAFAFA";
	           }else{
	              bgc = "#ffffff";
	           }
	        %>
	         <!-- 循环核心元素 -->
	         <tr height="25px;" bgcolor="<%=bgc %>" data-research-relation="${fn:toLowerCase(v.repoName) == 'relation'}">
	             <td width="250px" align="right">
	               <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	               <strong> ${v.repoTitle }(${v.repoName })：</strong> 
	             </td>
	             <td> 
	             	<c:choose>
	             		<c:when test="${fn:toLowerCase(v.repoName) == 'title' }">
	             		  <textarea name="${v.repoId }" 
			             		  onblur="onCheckTitle(this,'0');"
			                       class="source_text" 
			                       restrict="${v.repoRestrict }" 
			                       sourceElement="true" 
			                       title="${v.repoTitle }" 
			                       onpropertychange="setTextAreaHeight(this);"
			                       onkeyup="setTextAreaHeight(this);" 
			                       onkeydown="setTextAreaHeight(this);">${v.repoDefaultdata }</textarea>
	             		</c:when>
	             		
					<%-- 学科改为手工输入；原下拉实现保留在下方 repoDatatype 分支。 --%>
					<c:when test="${fn:toLowerCase(fn:trim(v.repoName)) == 'discipline'}">
						<%-- 原学科多行输入保留，现改为单行输入框。
						<textarea name="${v.repoId}" class="source_text"
						          restrict="${v.repoRestrict}" sourceElement="true"
						          title="${fn:escapeXml(v.repoTitle)}"
						          onpropertychange="setTextAreaHeight(this);"
						          onkeyup="setTextAreaHeight(this);"
						          onkeydown="setTextAreaHeight(this);">${fn:escapeXml(v.repoDefaultdata)}</textarea>
						--%>
						<input type="text" name="${v.repoId}" class="source_text"
						       data-discipline-input="true" maxlength="1600"
						       restrict="${v.repoRestrict}" sourceElement="true"
						       title="${fn:escapeXml(v.repoTitle)}"
						       style="width: 514px;margin-left: 2px;height: 22px;"
						       value="${fn:escapeXml(v.repoDefaultdata)}">
					</c:when>
					<c:when test="${fn:toLowerCase(v.repoName) == 'language' }">
	             		  <input name="${v.repoId }" 
			                       type="text" class="source_text"
			                       restrict="${v.repoRestrict }" 
			                       sourceElement="true" 
			                       list="resource-language-${v.repoId}"
			                       value="${fn:escapeXml(empty libLan ? v.repoDefaultdata : libLan)}"
			                       title="${fn:escapeXml(v.repoTitle)}">
			              <datalist id="resource-language-${v.repoId}">
			                  <option value="中文"></option><option value="藏文"></option>
			                  <option value="蒙文"></option><option value="英文"></option>
			                  <c:forEach items="${fn:split(v.repoDataOptions, ',')}" var="languageOption">
			                      <option value="${fn:escapeXml(languageOption)}"></option>
			                  </c:forEach>
			              </datalist>
	             		</c:when>
	             		
		                <c:otherwise>
		                	<c:choose>
		                		<c:when test="${v.repoDatatype == 1}">
		                			<select name="${v.repoId }" style="width: 514px;margin-left: 2px;height: 22px;">
		                				<c:forEach items="${fn:split(v.repoDataOptions,',')}" var="optionData">
		                					<option value="${optionData }" <c:if test="${v.repoDefaultdata eq optionData }">selected</c:if>>${optionData }</option>
		                				</c:forEach>
		                			</select>
		                		</c:when>
		                		<c:otherwise>
					                <textarea name="${v.repoId }" 
					                       class="source_text" 
					                       restrict="${v.repoRestrict }" 
					                       sourceElement="true" 
					                       title="${v.repoTitle }" 
					                       onpropertychange="setTextAreaHeight(this);"
					                       onkeyup="setTextAreaHeight(this);" 
					                       onkeydown="setTextAreaHeight(this);">${v.repoDefaultdata }</textarea>
		                		</c:otherwise>
		                	</c:choose>
		                </c:otherwise>       
	                </c:choose>     
	                <a id="${v.repoId }_msg" name="${v.repoId }_msg">
		                <c:if test="${v.repoRestrict == 'R' }">
		                    <img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img>
		                </c:if>
		                <c:if test="${v.repoRestrict == 'M' }">
		                    <img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img>
		                </c:if>
		                <c:if test="${v.repoRestrict == 'MA' }">
		                   <img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img>
		                </c:if>
		                <c:if test="${v.repoRestrict == 'O' }">
		                   <img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img>
		                </c:if>
	                </a>      
	                
	             </td>
	         </tr>
	         
	         <!-- 将所有的元素上级元素再和核心相比，显示出它的所有子元素 -->
	         <c:forEach items="<%=elementList %>" var="vc" varStatus="sc">
	          	 <c:if test="${v.repoId == vc.repoParent }">
	          	 <!-- 循环子元素 -->
		         <tr height="25px;" bgcolor="<%=bgc %>">
		           <td width="250px" align="right"> 
		            &nbsp;${vc.repoTitle }(${vc.repoName })：
		           </td>
		           <td> 
		           	   <c:choose>
					<c:when test="${fn:toLowerCase(fn:trim(vc.repoName)) == 'discipline'}">
					    <%-- 子元素原下拉实现保留在下方 repoDatatype 分支。 --%>
					    <input type="text" name="${vc.repoId}" class="source_text"
					           data-discipline-input="true" maxlength="1600"
					           restrict="${vc.repoRestrict}" sourceElement="true"
					           title="${fn:escapeXml(vc.repoTitle)}"
					           style="width: 514px;margin-left: 2px;height: 22px;"
					           value="${fn:escapeXml(vc.repoDefaultdata)}">
					</c:when>
		           	   	  <c:when test="${fn:toLowerCase(vc.repoName) == 'accessrights' }">
		             		  <select name="${vc.repoId }" id="${vc.repoId }" style="width: 514px;margin-left: 2px;height: 22px;"> 
					              <option value="<%=SysUserType.PURIVEW_DOWNLOAD %>">可下载</option>
					              <option value="<%=SysUserType.PURIVEW_BROWSE_SOURCE %>">可浏览源文</option>
					              <option value="<%=SysUserType.PURIVEW_BROWSE_VIEW %>">可浏览预览版</option>
					              <option value="<%=SysUserType.PURIVEW_CANNOT_BROWSE %>">不可预览</option>
					              <option value="<%=SysUserType.PURIVEW_NOSEE %>">不可访问</option>
					          </select>
		           	   	  </c:when>
		           	   	  
		           	   	  <c:otherwise>
				               <c:choose>
						          <c:when test="${vc.repoDatatype == 1}">
			                			<select name="${vc.repoId }" style="width: 514px;margin-left: 2px;height: 22px;">
			                				<c:forEach items="${fn:split(vc.repoDataOptions,',')}" var="optionData">
			                					<option value="${optionData }" <c:if test="${vc.repoDefaultdata eq optionData }">selected</c:if>>${optionData }</option>
			                				</c:forEach>
			                			</select>
			                		</c:when>
			                		<c:otherwise>
						                <textarea name="${vc.repoId }" 
						                       class="source_text" 
						                       restrict="${vc.repoRestrict }" 
						                       sourceElement="true" 
						                       title="${vc.repoTitle }" 
						                       onpropertychange="setTextAreaHeight(this);"
						                       onkeyup="setTextAreaHeight(this);" 
						                       onkeydown="setTextAreaHeight(this);">${vc.repoDefaultdata }</textarea>
			                		</c:otherwise>
				               </c:choose>
		           	   	  </c:otherwise>
		           	   </c:choose>
			           <a id="${vc.repoId }_msg" name="${vc.repoId }_msg">
				             <c:if test="${vc.repoRestrict == 'R' }"><img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img></c:if>
			                 <c:if test="${vc.repoRestrict == 'M' }"><img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img></c:if>
			                 <c:if test="${vc.repoRestrict == 'MA' }"><img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img></c:if>
			                 <c:if test="${vc.repoRestrict == 'O' }"><img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img></c:if>
		               </a>
		             
	                 <!-- <img src="<%=path %>/images/application_put.png" title="${vc.repoCode }"></img> --> 
		           </td>
		         </tr>
		         </c:if>
	         </c:forEach>
	         
	       </c:if>
	         
       </c:forEach> 
       </table>
       
       <!-- 选择分类  -->
       <div class="title" style="margin-top: 0;height: 26px">选择分类</div>
       <%-- 隐藏分类控件后仍提交必要的内部字段。 --%>
       <input type="hidden" name="rsty_id" value="">
       <input type="hidden" name="language_id" value="${requestScope.language.rltyId}">
       <input type="hidden" name="rsdaRssuId" value="${fn:escapeXml(param.rsdaRssuId)}">
       <input type="hidden" name="rdty_id_two" value="">
       <input type="hidden" name="rdty_id_three" value="">
       <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
       <%-- 暂停显示中图法、学科分类，保留原控件实现。
       <tr bgcolor="#FFFFFF" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              中图法分类：
	          </td>
	          <td><input name="rmty_id" id="rmty_id" class="text" style="width: 300px;margin-left: 2px;">
	              <a id="rmtyMessage" name="rmtyMessage"></a>
	          </td>
	       </tr>
	       <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              学科分类：
	          </td>
	          <td>
	              <input type="hidden" name="rsty_id" id="rsty_id" value="">
	              <input type="text" name="rsty_name" id="rsty_name" list="resource-subject-options"
	                     class="text" style="width: 300px;margin-left: 2px;height: 22px;" aria-label="学科分类">
	              <datalist id="resource-subject-options">
	                  <c:forEach items="${requestScope.subjectList}" var="subjectOption">
	                      <option value="${fn:escapeXml(subjectOption.rstyName)}"></option>
	                  </c:forEach>
	              </datalist>
		          
		          
		      </td>    
	       </tr>
	       --%>
	       <!-- 自定义分类一 -->
	       <c:if test="${fn:length(dynamicTypeOptions) > 4 }">
		       <tr bgcolor="#FFFFFF" height="25">
		          <td width="250px" align="right">
		            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
		              ${groupData[0] }：
		          </td>
		          <td>
		            <select name="rdty_id" id="rdty_id" style="width: 300px;margin-left: 2px;height: 22px;"> 
		              ${requestScope.dynamicTypeOptions }
		            </select>
		          </td>
		       </tr>
	       </c:if>
	       
	       <%-- 暂停显示其余分类、语种及主题，保留原控件实现。
	       <!-- 自定义分类二 -->
	       <c:if test="${fn:length(dynamicTypeOptions2) > 4 }">
		       <tr bgcolor="#FFFFFF" height="25">
		          <td width="250px" align="right">
		            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
		              ${groupData[1] }：
		          </td>
		          <td>
		            <select name="rdty_id_two" id="rdty_id_two" style="width: 300px;margin-left: 2px;height: 22px;"> 
		              ${requestScope.dynamicTypeOptions2 }
		            </select>
		          </td>
		       </tr>
	       </c:if>
	       <!-- 自定义分类三 -->
	       <c:if test="${fn:length(dynamicTypeOptions3) > 4 }">
		       <tr bgcolor="#FFFFFF" height="25">
		          <td width="250px" align="right">
		            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
		               ${groupData[2] }：
		          </td>
		          <td>
		            <select name=rdty_id_three id="rdty_id_three" style="width: 300px;margin-left: 2px;height: 22px;"> 
		              ${requestScope.dynamicTypeOptions3 }
		            </select>
		          </td>
		       </tr>
	       </c:if>
	       
	       <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	             <img src="<%=path %>/images/extform/bullet_add_2.png"></img>  
	               语种：
	          </td>
	          <td>
	              <input type="hidden" name="language_id" value="${requestScope.language.rltyId }">
	              <span style="color: #808080;padding-left: 2px;">${requestScope.language.rltyName }</span>
	          </td>
	       </tr>
	       <tr bgcolor="#FAFAFA" height="25">
	       	<td width="250px" align="right">
	       		<img src="<%=path %>/images/extform/bullet_add_2.png"></img>  
	       		主题：
	       	</td>
	       	<td><select name="rsdaRssuId" id="rsdaRssuId" sourceElement="true" style="width: 300px;margin-left: 2px;height: 22px;"> 
				    ${requestScope.sourceSubjectOptions }
				</select></td>
	       </tr>
	       --%>
	 </table>
	 
	 <!-- 文件上传 -->
	  <div class="title" style="margin-top: 0;height: 26px">上传文件</div>
	  <!-- 代码部分begin -->
	  <input type="hidden" name="resFileType" id="resFileType" value="0" />
	  <div class="tabbox">
		<div class="tab">
	    	<a href="javascript:;" class="on" data-type='0'>文件格式</a>
	        <a href="javascript:;" data-type='1'>目录资源（图片／文本）</a>
	        <a href="javascript:;" data-type='2'>HTML格式</a>
	    </div>
	    <div class="content">
	    	<ul>
	        	<li>
	        		<table cellpadding="0" width="100%" border="0" cellspacing="1" >
				        <tr height="25">
				          <td width="150px" align="right">
				            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
				               封 面：
				          </td>
				          <td id="imageFileArea">
				             <input type="file" name="bigimg" class="file text" onchange="checkImageFileType(this);">&nbsp;<span style="color: #808080">封面文件格式:jpg、jpeg、bmp、png、gif</span>
				          </td>
				        </tr>
				        
				        <!-- 
				        <tr height="25">
				          <td width="150px" align="right">
				            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
				               预览文件：
				          </td>
				          <td>
				               <input type="file" name="viewfile" class="file text" />
				          </td>
				        </tr>
				         -->
				        
				        <tr height="25">
				          <td width="150px" align="right">
				            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
				               源 文 件：
				          </td>
				          <td>
				               <input type="file" name="loadfile" class="file text" />
				               <a id="loadfile_msg"></a>
				          </td>
				        </tr>
				     </table>
	        	</li>
	            <li>
	            	<%@ include file="directoryUpload.jspf" %>
	            </li>
	            <li>
	            	<p>请输入html文件的访问地址：</p>
	            	<p><input type="text" name="htmlDir" class="text" maxlength="512" /></p>
	            	<p class="tips">如：http://192.168.1.156:8008/htmlfile/book01/index.html</p>
	            	<p>
	            		<a href="<%=path %>/res/demo/html">资源示例下载</a>
	            	</p>
	            </li>
	        </ul>
	    </div>
	  </div>
	  
	  
	   <table border="0" width="100%">
		   <tr style="background-color: #ffffff" height="50px">
	          <td width="450px">
	               <div class="tool1" style="padding: 5px;">
	                      <ul class="nav">
		                    <li><a href="javascript:doSave('0');" style="padding-top: 3px;">保 存</a></li>
		                    <!-- <li><a href="javascript:doSave('1');" style="padding-top: 3px;" class="btnb">保存并新增</a></li> -->
		                    <!-- <li><a href="javascript:sourceView('req');" style="padding-top: 3px;">预 览</a></li> --> <!-- 进度条代码使用改变了原有方法，预览功能咱不可使用 -->
		                    <input type="submit" class="p-btn" style="cursor: pointer;display:none;" value="提交" />
		                    <c:choose>
		                    	<c:when test="${maintenance}">
									<li><a href="<%=path%>/console/data/resource/SourceManager.htm?action=managerResourceList" style="padding-top: 3px;"> 返 回 </a></li>
		                    	</c:when>
		                    	<c:when test="${req == 7 }">
									<li><a href="<%=path %>/console/data/inputSingle.htm?action=initSubject&rmin_id=${rmin_id }" style="padding-top: 3px;"> 返 回 </a></li>
		                    	</c:when>
		                    	<c:when test="${req == 8 }">
									<li><a href="<%=path %>/console/data/inputSingle.htm?action=subjectResourceList&rmin_id=${rmin_id }&rstyId=${rstyId }" style="padding-top: 3px;"> 返 回 </a></li>
		                    	</c:when>
		                    	<c:otherwise>
									<li><a href="<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id=${rmin_id }" style="padding-top: 3px;"> 返 回 </a></li>
		                    	</c:otherwise>
		                    </c:choose>
		                  </ul>  
	               </div>
	          </td>
	          
	          <td align="right" class="icon-sm">
	            <img src="<%=path %>/images/icon/bibei.png"></img>必备(M) &nbsp; 
	            <img src="<%=path %>/images/icon/youzhebibei.png"></img>有则必备(MA) &nbsp;  
	            <img src="<%=path %>/images/icon/tuijian.png"></img>强烈推荐(R) &nbsp; 
	            <img src="<%=path %>/images/icon/kexuan.png"></img>可选(O) 
	          </td>
	       </tr>
	    </table>
    </form> 
    </c:if>  
    <input type="hidden" name="loadfile_statu" value="2"> 
     
    <!-- 保存等待的提示 --> 
    <div id="WaitMessageBox" class="WaitMessage"></div>  
     
    <!-- 用户登录过期的错误提示 --> 
    <c:if test="<%=userid == 0 %>">
        <h2>错误提示：</h2><hr>
        您还未登录或登录已过期，请登录！
    </c:if>
    
      <!-- 资源保存成功的图片提示 -->
      <div id="SuccessMessageBox" class="SuccessMessageBox"></div>
      ${requestScope.result }
      
      <script type="text/javascript">initTextAreaHeight();</script>
      <!-- 进度条 -->
      <div class="prompt-layer"></div>	
</body>
</html>

