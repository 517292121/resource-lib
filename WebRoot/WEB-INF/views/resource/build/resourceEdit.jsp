<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@page import="cn.com.adtech.riss2.jdbcms.ToolUnit"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.core.CommonUtil"%>
<%@page import="cn.com.adtech.riss2.domain.SysUserType"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	List<ResElementPool> EleDataList = (List<ResElementPool>)request.getAttribute("EleDataList");
	ResSourceData resource = (ResSourceData)request.getAttribute("resource");
	
	IResourceRpc resourceRpc = (IResourceRpc)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("resourceRpc");
	
	JSONRPCBridge.registerObject("resourceRpc",resourceRpc);
	
	int reqType = MyRequestUtil.getIntAttribute(request,"reqType");
	if(reqType == 0){
		reqType = MyRequestUtil.getIntParameter(request,"reqType");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>资源查看/修改</title>
    
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
    <link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
    <script type="text/javascript" src="<%=path %>/viewsjs/common/texiao.js"></script>
    <link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
	<link rel="stylesheet" href="<%=path%>/css/MessageUtil.css" type="text/css"></link>
    <script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path%>/js/MessageUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/jquery-1.8.3.min.js"></script>
    <script type="text/javascript" src="<%=path %>/layer/layer.js"></script>
    
    <script type="text/javascript" src="<%=path %>/js/jquery.form.js"></script>
	<script type="text/javascript" src="<%=path %>/js/progressform.js"></script>
	
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
       
      
       function viewFile(id, col){
           window.open("<%=path %>/pages/FileView.jsp?id="+id+"&col=" + col);
       }
       
       function checkRsdaTitle(item, selfid){
       	   var data = item.value;
       	   if(item.sdata == data){
       	   	   return;
       	   }
       	   item.sdata = data;
       	   onCheckTitle(item, selfid);
       }
       var maxHeight = <%=ConstantsObj.TEXTAREA_MAX_HEIGHT %>;
       var addOffset = null;
       
        function showgo(){
       document.getElementById("formshow").submit();
       }
       
              
       function goback(){
		   var id = $("input[name='rmin_id']").val();
		   if($("#reqType").val() == 1){
		   		window.location.href = "<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id="+id;
		   }else{
		   		showgo();
		   }
	   }
    </script>
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/build/ResourceData.js"></script>
  </head>
  
 
  <body onload="initTextAreaHeight()">
	   
	 <form action="<%=path %>/console/data/inputSingle.htm?action=missionResourceList" id="form5" name="form5" method="post">
	<input type="hidden"  id="searchtitle" name="searchtitle" />
	<input type="hidden"  id="pagecount" name="pagecount"/>
	<input type="hidden"  id="rmin_id" name="rmin_id" value="${requestScope.rminId }"/>
	</form>   
	 <form action="<%=path %>/console/data/resource/SourceManager.htm?action=managerResourceList" id="formshow" name="formshow" method="post">
	 <input type="hidden" id="searchtitleshow" name="searchtitleshow" value="${searchtitle}"/>
	 <input type="hidden" id="pagecount" name="pagecount" value="${pagecount}"/>
	 <input type="hidden" id="rmstId" name="rmstId" value="${searchRmstId}"/>
	 <input type="hidden" id="rltyId" name="rltyId" value="${searchRltyId}"/>
	</form>
	  
	 <form action="<%=path %>/console/data/inputSingle.htm?action=updateResourceData" name="sourceform" enctype="multipart/form-data" id="sourceform" method="post"> 
	 <input type="hidden" name="store" value="<%=resource.getRsdaRmstId() %>"></input>
	 <input type="hidden" name="stype" value="<%=resource.getRsdaRltyId() %>"></input>
	 <input type="hidden" name="rmin_id" value="<%=resource.getRsdaRminId() %>"></input>
	 <input type="hidden" name="lanId" value="${requestScope.languageId }"></input>
	 <input type="hidden" name="sid" value="<%=resource.getRsdaId() %>"></input>
	 <input type="hidden" name="reqType" id="reqType" value="<%=reqType %>"></input>
	 <input type="hidden" name="userid" value="<%=resource.getRsdaSmanId() %>"></input>
	 <input type="hidden" name="rihiId" value="${param.rihiId }"></input>
	 <input type="hidden" name="req" id="req" value="<%=reqType %>"></input>
	 <input type="hidden" name="pagecount" id="pagecount" value="${pagecount}"></input>
	 <input type="hidden" name="searchtitle" id="searchtitle" value="${searchtitle}"/>
	 <input type="hidden" id="rmstId" name="rmstId" value="${searchRmstId}"/>
	 <input type="hidden" id="rltyId" name="rltyId" value="${searchRltyId}"/>
	 <!-- <input type="hidden" name="language_id" value="${requestScope.language.rltyId }"> -->
	 
	 
	 <!-- 循环文档开始 -->
	 <div class="title" style="margin-top: 0">编辑资源内容	- ${resLan }
	 <%if(resource.getResMissionInfo() != null){ %>
	 , 作业：【<a href="<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id=<%=resource.getRsdaRminId() %>" style="font-weight: normal"><%=resource.getResMissionInfo().getRminMissname() %></a>】
	 <%} %>
	 </div>
	 <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC">
	 
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
	             <td width="250px" align="right">
	               <img src="<%=path %>/images/extform/bullet_add_2.png"></img>  
	               <strong>${v.repoTitle }(${v.repoName })：</strong> 
	             </td>
	             <td> 
	             	<c:choose>
	             		<c:when test="${fn:toLowerCase(v.repoName) == 'title' }">
	             			<textarea name="${v.repoId }" 
			                       class="source_text" 
			                       restrict="${v.repoRestrict }" 
			                       onblur="checkRsdaTitle(this,'<%=resource.getRsdaId() %>');"
			                       onpropertychange="setTextAreaHeight(this);"
			                       onkeyup="setTextAreaHeight(this);" 
			                       onkeydown="setTextAreaHeight(this);"
			                       sdata="${v.repoData }"
				                   sourceElement="true">${v.repoData }</textarea>
	             		</c:when>
	             		
	             		<c:when test="${!empty resLan and fn:toLowerCase(v.repoName) == 'language' }">
	             		  <input name="${v.repoId }" 
			                       class="source_text disabled" 
			                       restrict="${v.repoRestrict }" 
			                       sourceElement="true" 
			                       readonly="readonly"
			                       value="${resLan }"
			                       title="${v.repoTitle }">
	             		</c:when>
	             		
	             		<c:otherwise>
	             			<c:choose>
		                		<c:when test="${v.repoDatatype == 1}">
		                			<select name="${v.repoId }" style="width: 514px;margin-left: 2px;height: 22px;">
		                				<option value="${v.repoData }">${v.repoData }</option>
		                				<c:forEach items="${fn:split(v.repoDataOptions,',')}" var="optionData">
		                					<c:if test="${v.repoData eq optionData == false }">
			                					<option value="${optionData }">${optionData }</option>
		                					</c:if>
		                				</c:forEach>
		                			</select>
		                		</c:when>
		                		<c:otherwise>
					                <textarea name="${v.repoId }" 
				                       class="source_text" 
				                       restrict="${v.repoRestrict }" 
				                       onpropertychange="setTextAreaHeight(this);"
				                       onkeyup="setTextAreaHeight(this);" 
				                       onkeydown="setTextAreaHeight(this);"
					                   sourceElement="true">${v.repoData }</textarea>
		                		</c:otherwise>
		                	</c:choose>
			            </c:otherwise>           
	                </c:choose>       
	                <a id="${v.repoId }_msg">
		                <c:if test="${v.repoRestrict == 'R' }"><img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img></c:if>
		                <c:if test="${v.repoRestrict == 'M' }"><img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img></c:if>
		                <c:if test="${v.repoRestrict == 'MA' }"><img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img></c:if>
		                <c:if test="${v.repoRestrict == 'O' }"><img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img></c:if>
	                </a>
	             </td>
	         </tr>
	         
	         <!-- 将所有的元素上级元素再和核心相比，显示出它的所有子元素 -->
	         <c:forEach items="<%=EleDataList %>" var="vc" varStatus="sc">
	          	 <c:if test="${v.repoId == vc.repoParent }">
		         <tr height="25px;" bgcolor="<%=bgc %>">
		           <td width="250px" align="right"> &nbsp;
			            ${vc.repoTitle }(${vc.repoName })：
		           </td>
		           <td> 
		           	<c:choose>
		           		<c:when test="${fn:toLowerCase(vc.repoName) == 'accessrights' }">
	             		  <select name="${vc.repoId }" id="${vc.repoId }" sourceElement="true" style="width: 514px;margin-left: 2px;height: 22px;"> 
				              <option value="<%=SysUserType.PURIVEW_DOWNLOAD %>" <c:if test="${vc.repoData == 5 }"> selected="selected"</c:if>>可下载</option>
				              <option value="<%=SysUserType.PURIVEW_BROWSE_SOURCE %>" <c:if test="${vc.repoData == 4 }"> selected="selected"</c:if>>可浏览源文</option>
				              <option value="<%=SysUserType.PURIVEW_BROWSE_VIEW %>" <c:if test="${vc.repoData == 3 }"> selected="selected"</c:if>>可浏览预览版</option>
				              <option value="<%=SysUserType.PURIVEW_CANNOT_BROWSE %>" <c:if test="${vc.repoData == 2 }"> selected="selected"</c:if>>不可预览</option>
				              <option value="<%=SysUserType.PURIVEW_NOSEE %>" <c:if test="${vc.repoData == 1 }"> selected="selected"</c:if>>不可访问</option>
				          </select>
	             		</c:when>
		           		
	             		<c:otherwise>
				              
			                  <c:choose>
		                		<c:when test="${vc.repoDatatype == 1}">
		                			<select name="${vc.repoId }" style="width: 514px;margin-left: 2px;height: 22px;">
		                				<option value="${vc.repoData }">${vc.repoData }</option>
		                				<c:forEach items="${fn:split(vc.repoDataOptions,',')}" var="optionData">
		                					<c:if test="${vc.repoData eq optionData == false }">
		                						<option value="${optionData }">${optionData }</option>
		                					</c:if>	
		                				</c:forEach>
		                			</select>
		                		</c:when>
		                		<c:otherwise>
					                <textarea name="${vc.repoId }" 
					                     class="source_text"
					                     restrict="${vc.repoRestrict }" 
					                     sourceElement="true" 
					                     onpropertychange="setTextAreaHeight(this);"
				                         onkeyup="setTextAreaHeight(this);" 
				                         onkeydown="setTextAreaHeight(this);" >${vc.repoData }</textarea>
		                		</c:otherwise>
		                	</c:choose>       
				        </c:otherwise>
				      </c:choose>             
		              <a id="${vc.repoId }_msg">
			             <c:if test="${vc.repoRestrict == 'R' }"><img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img></c:if>
		                 <c:if test="${vc.repoRestrict == 'M' }"><img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img></c:if>
		                 <c:if test="${vc.repoRestrict == 'MA' }"><img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img></c:if>
		                 <c:if test="${vc.repoRestrict == 'O' }"><img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img></c:if>
	                  </a>
		           </td>
		         </tr>
		         </c:if>
	         </c:forEach>
	       </c:if>
       </c:forEach> 
       </table>
       
       <!-- 选择分类 -->
       <div class="title" style="margin-top: 0">选择分类</div>
	     <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	       <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              学科分类：
	          </td>
	          <td>
	              <select name="rsty_id" id="rsty_id" style="width: 300px;margin-left: 2px;height: 22px;"> 
		              ${requestScope.subjectOptions }
		          </select>
		          
		          
		      </td>    
	       </tr>
	       
	       <!-- 自定义分类一 -->
	       <c:if test="${fn:length(dynamicTypeOptions) > 4 }">
		       <tr bgcolor="#FFFFFF" height="25">
		          <td width="250px" align="right">
		            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
		              ${resource.resMetaStore.rmstGroupOne }：
		          </td>
		          <td>
		            <select name="rdty_id" id="rdty_id" style="width: 300px;margin-left: 2px;height: 22px;"> 
		              ${requestScope.dynamicTypeOptions }
		            </select>
		          </td>
		       </tr>
	       </c:if>
	       
	       <!-- 自定义分类二 -->
	       <c:if test="${fn:length(dynamicTypeOptions2) > 4 }">
		       <tr bgcolor="#FFFFFF" height="25">
		          <td width="250px" align="right">
		            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
		              ${resource.resMetaStore.rmstGroupTwo }：
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
		              ${resource.resMetaStore.rmstGroupThree }：
		          </td>
		          <td>
		            <select name="rdty_id_three" id="rdty_id_three" style="width: 300px;margin-left: 2px;height: 22px;"> 
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
	 </table>
		   
	 
	    
	    <div class="title" style="margin-top: 0">上传文件</div>
	      <input type="hidden" name="resFileType" id="resFileType" value="${resource.rsdaSourceType }" />
		  <div class="tabbox">
			<div class="tab">
		    	<a href="javascript:;" <c:if test="${empty resource.rsdaSourceType or resource.rsdaSourceType == 0 }">class="on"</c:if> data-type='0'>文件格式</a>
		        <a href="javascript:;" <c:if test="${resource.rsdaSourceType == 1 }">class="on"</c:if> data-type='1'>图片格式</a>
		        <a href="javascript:;" <c:if test="${resource.rsdaSourceType == 2 }">class="on"</c:if> data-type='2'>HTML格式</a>
		    </div>
		    <div class="content">
		    	<ul>
		        	<li>
		        		<table cellpadding="0" width="100%" border="0" cellspacing="1" >
					        <tr height="25">
					          <td width="150px" align="right">
					            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
					               封 面：
					             <input type="hidden" name="bigimg_statu" id="bigimg_statu" value="0"> 
					          </td>
					          <td id="imageFileArea">
					               <%if(ToolUnit.NullToBlank(resource.getRsdaBigimgName()).length() > 3){ %>
					                  <div id="bigimgArea">
					                     <%=ToolUnit.getFileName(ToolUnit.NullToBlank(resource.getRsdaBigimgName())) %>
 					                     <a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&sourceId=<%=resource.getRsdaId() %>&type=1" target="_blank">预览</a>
					                     <a href="javascript:delefile('bigimgArea','bigimg');">删除</a>
					                  </div>      
					                  <input type="file" name="bigimg" id="bigimg" class="file text" style="display: none" onchange="fileChang('bigimg',true);" />
					               <%}else{ %>
					                  <input type="file" name="bigimg" id="bigimg" class="file text" onchange="fileChang('bigimg',true);" />
					                  &nbsp;<span style='color: #808080'>封面文件格式:jpg、jpeg、bmp、png、gif</span>
					               <%} %>
					          </td>
					        </tr>
					        
					        <tr height="25">
					          <td width="150px" align="right">
					            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
					               源 文 件：
					          </td>
					          <td>
					               <%if(ToolUnit.NullToBlank(resource.getRsdaFileName()).length() > 3){ %>
					                  <div id="loadfileArea">
						                  <%=ToolUnit.getFileName(ToolUnit.NullToBlank(resource.getRsdaFileName())) %>
						                  
						                  <%if(CommonUtil.isNotNull(resource.getRsdaFileSwfUrl()) || 
						                		   (CommonUtil.isNotNull(resource.getRsdaFileType()) && resource.getRsdaFileType().toLowerCase().equals("pdf")) ||
						                		   (CommonUtil.isNotNull(resource.getRsdaFileType()) && resource.getRsdaFileType().toLowerCase().equals("doc")) ||
						                		   (CommonUtil.isNotNull(resource.getRsdaFileType()) && resource.getRsdaFileType().toLowerCase().equals("docx"))
						                		  ){ %>
						                  <%-- <a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&sourceId=<%=resource.getRsdaId() %>&type=3" target="_blank">预览</a> --%>
						                  <%} %>
						                  <a href="javascript:deleLoadFile();">删除</a>
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
		        	</li>
		            <li>
		            	<p>请输入资源文件在服务器中的目录地址：</p>
		            	<p><input type="text" name="imgDir" class="text" <c:if test="${resource.rsdaSourceType == 1 }">value="${resource.rsdaFileUrl }"</c:if> maxlength="512" onchange="urlpath(this);" /></p>
		            	<p class="tips">如：D:/RissResourceFile/imgbook/b003/</p>
		            	<p>
		            		<a href="<%=path %>/res/demo/img">资源示例下载</a>
		            	</p>
		            </li>
		            <li>
		            	<p>请输入html文件的访问地址：</p>
		            	<p><input type="text" name="htmlDir" class="text" <c:if test="${resource.rsdaSourceType == 2 }">value="${resource.rsdaFileUrl }"</c:if> maxlength="512" /></p>
		            	<p class="tips">如：http://192.168.1.156:8008/htmlfile/book01/index.html</p>
		            	<p>
		            		<a href="<%=path %>/res/demo/html">资源示例下载</a>
		            	</p>
		            </li>
		        </ul>
		    </div>
		  </div>      	
	    
	    
	    
	     
		   
		 <table border="0" width="100%" cellpadding="0" cellspacing="0">
		   <tr height="50px">
	          <td style="padding-left: 10px;" align="left">
	               <div class="tool1"> ${rsdaRmstId}
	                      <ul class="nav">
		                    <li><a href="javascript:doSave('<%=reqType %>');" style="padding-top: 4px;">保 存</a></li>
							<!-- <li><a href="javascript:sourceView('reqType');" style="padding-top: 3px;">预 览</a></li> --> <!-- 进度条代码使用改变了原有方法，预览功能咱不可使用 -->
							<li><a href="<%=path %>/console/data/inputSingle.htm?action=initResSourceFile&resId=<%=resource.getRsdaId() %>" style="padding-top: 3px;" > 附 件 </a></li>
							<li>
							    <c:if test="${param.reqType == 1 }">
						        <a href="javascript:backshow2('${searchtitle}',${pagecount},${requestScope.rminId });" style="padding-top: 4px;"> 返 回 </a>
							    </c:if>
							    <c:if test="${param.reqType == 2 }">
						        <a href="javascript:showgo();" style="padding-top: 4px;"> 返 回 </a>
							    </c:if>
							    <c:if test="${param.reqType == 4 }">
						        <a href="<%=path %>/console/data/resource/importExcel.htm?action=initImportResourceList&rminId=<%=resource.getRsdaRminId() %>&rihiId=${param.rihiId }" style="padding-top: 4px;"> 返 回 </a>
							    </c:if>
							    <c:if test="${param.reqType == 6 }">
						        <a href="<%=path %>/console/data/gatherResource.htm?action=initResourceList" style="padding-top: 4px;"> 返 回 </a>
							    </c:if>
							</li>
		                  </ul>  
		                  <input type="submit" class="p-btn" style="cursor: pointer;display:none;" value="提交" />
	               </div>	
	          </td>
	          
	          <td align="right">
	            <img src="<%=path %>/images/icon/bibei.png"></img>必备(M) &nbsp; 
	            <img src="<%=path %>/images/icon/youzhebibei.png"></img>有则必备(MA) &nbsp;  
	            <img src="<%=path %>/images/icon/tuijian.png"></img>强烈推荐(R) &nbsp; 
	            <img src="<%=path %>/images/icon/kexuan.png"></img>可选(O) 
	            
	          </td>
	          
	       </tr>
	    </table>
	    <input type="hidden" name="req">
    </form> 
    
    <!-- 保存等待的提示 --> 
    <div id="WaitMessageBox" class="WaitMessage"></div> 
    
    <!-- 资源保存成功的图片提示 -->
    <div id="SuccessMessageBox" class="SuccessMessageBox"></div>
    ${requestScope.result }
   	<!-- 进度条 -->
    <div class="prompt-layer"></div>	
</body>
</html>