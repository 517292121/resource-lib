<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@page import="cn.com.adtech.riss2.jdbcms.ToolUnit"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.core.BasicDataUtil"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.core.TempletConfig"%>
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
    <title>用户资源查看/修改/审核</title>
    
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
    <link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
    <link rel="stylesheet" href="<%=path %>/css/extbutton.css" type="text/css"></link>
    <script type="text/javascript" src="<%=path %>/viewsjs/common/texiao.js"></script>
    <link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
    
    
	<style type="text/css">
	   *{
	      font-size: 12px;
	      font-family: 宋体;
	   }
	</style>
    <script type="text/javascript">
       function getPath(){
           return "<%=path %>";
       }
       
       function delefile(hideArea, showFile, icon){
           if(window.confirm("您确定要删除该文件并重新上传吗？") == false){
               return;
           }
           document.getElementById(hideArea).innerHTML = "";
           document.getElementById(hideArea).style.display = "none";
           document.getElementById(showFile).style.display = "block";
           document.getElementById(showFile+"_statu").value = "1";
           if(icon != null){
              document.getElementById(icon).style.display = "block";
           }
       }
       
       function changStatu(filestatu){
       	   document.getElementById(filestatu+"_statu").value = "1";
       }
       
       function deleLoadFile(){
           if(window.confirm("您确定要删除该文件并重新上传吗？") == false){
               return;
           }
           document.getElementById("loadfileArea").innerHTML = "";
           document.getElementById("loadfileArea").style.display = "none";
           document.getElementById("loadfile_statu").value = "1";
           document.getElementById("testAA").style.display="block";
       }
       
       function viewFile(id, col){
           window.open("<%=path %>/pages/FileView.jsp?id="+id+"&col=" + col);
       }
       
       function checkUpdateByType(eleType){
       	   var eleList = document.getElementsByTagName(eleType);
       	   for(i=0; i<eleList.length; i++){
       	       if(eleList[i].sourceElement == "true"){
       	           if(eleList[i].value != eleList[i].orgValue){
       	           	   document.all.isChang.value = 1;
       	           	   return false;
       	           }
       	       }
       	   }
       }
       
       function checkUpdate(){
       	  var result = checkUpdateByType("textarea");
       	  if(result == false){
       	  	  return;
       	  }
       	  result = checkUpdateByType("select");
       	  if(result == false){
       	  	  return;
       	  }
       	  result = checkUpdateByType("input");
       	  if(result == false){
       	  	  return;
       	  }
       	  if(document.getElementById("bigimg_statu").value == "1"){
       	  	  document.all.isChang.value = 1;
       	  	  return;
       	  }
       	  if(document.getElementById("viewfile_statu").value == "1"){
       	  	  document.all.isChang.value = 1;
       	  	  return;
       	  }
       	  if(document.getElementById("loadfile_statu").value == "1"){
       	  	  document.all.isChang.value = 1;
       	  	  return;
       	  }
       }
       var maxHeight = <%=ConstantsObj.TEXTAREA_MAX_HEIGHT %>;
       var addOffset = null;
    </script>
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/build/ResourceData.js"></script>
  </head>
  
 
  <body onload="initTextAreaHeight()">
	   
	 <form action="<%=path %>/console/data/inputSingle.htm?action=memberResourceChecked" name="sourceform" enctype="multipart/form-data" id="sourceform" method="post"> 
	 <input type="hidden" name="store" value="<%=resource.getRsdaRmstId() %>"></input>
	 <input type="hidden" name="stype" value="<%=resource.getRsdaRltyId() %>"></input>
	 <input type="hidden" name="rmin_id" value="<%=resource.getRsdaRminId() %>"></input>
	 <input type="hidden" name="lanId" value="${requestScope.languageId }"></input>
	 <input type="hidden" name="sid" value="<%=resource.getRsdaId() %>"></input>
	 <input type="hidden" name="userid" value="<%=resource.getRsdaSmanId() %>"></input>
	 <input type="hidden" name="rihiId" value="${param.rihiId }"></input>
	 <input type="hidden" name="rsdaStatus" id="rsdaStatus" value="0"></input>
	 <input type="hidden" name="type" id="type" value="0"></input>
	 <input type="hidden" name="isChang" value="0"></input>
	 <input type="hidden" name="inputType" value="<%=resource.getRsdaAuthortype() %>"></input>
	 <!-- <input type="hidden" name="language_id" value="${requestScope.language.rltyId }"> -->
	 
	 
	 <!-- 循环文档开始 -->
	 <div class="title" style="margin-top: 0">编辑资源内容
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
	             		<c:when test="${fn:toLowerCase(v.repoName) == 'accessrights' }">
	             		  <select name="${v.repoId }" id="${v.repoId }" sourceElement="true" style="width: 304px;margin-left: 2px;height: 22px;"> 
				              <option value="<%=SysUserType.PURIVEW_DOWNLOAD %>" <c:if test="${v.repoData == 5 }"> selected="selected"</c:if>>可下载</option>
				              <option value="<%=SysUserType.PURIVEW_BROWSE_SOURCE %>" <c:if test="${v.repoData == 4 }"> selected="selected"</c:if>>可浏览源文</option>
				              <option value="<%=SysUserType.PURIVEW_BROWSE_VIEW %>" <c:if test="${v.repoData == 3 }"> selected="selected"</c:if>>可浏览预览版</option>
				              <option value="<%=SysUserType.PURIVEW_CANNOT_BROWSE %>" <c:if test="${v.repoData == 2 }"> selected="selected"</c:if>>不可预览</option>
				              <option value="<%=SysUserType.PURIVEW_NOSEE %>" <c:if test="${v.repoData == 1 }"> selected="selected"</c:if>>不可访问</option>
				          </select>
					      <a>
				                <c:if test="${v.repoRestrict == 'R' }"><img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img></c:if>
				                <c:if test="${v.repoRestrict == 'M' }"><img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img></c:if>
				                <c:if test="${v.repoRestrict == 'MA' }"><img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img></c:if>
				                <c:if test="${v.repoRestrict == 'O' }"><img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img></c:if>
			              </a>
	             		</c:when>
	             	
	             		<c:when test="${fn:toLowerCase(v.repoName) == 'location' }">
	             			<select name="${v.repoId }" id="${v.repoId }" style="width: 304px;margin-left: 2px;height: 22px;" sourceElement="true" orgValue='${v.repoData }'> 
				              <option value="">请选择...</option>
				              <%=BasicDataUtil.getLocationOptions("") %>
				            </select>
				            
				            <script type="text/javascript">
				            	document.getElementById('${v.repoId }').value = '${v.repoData }';
				            </script>
				            <a id="location_msg" name="location_msg">
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
	             		</c:when>
	             		<c:otherwise>
			                <textarea name="${v.repoId }" 
			                       class="source_text" 
			                       restrict="${v.repoRestrict }" 
			                       onpropertychange="setTextAreaHeight(this);"
			                       onkeyup="setTextAreaHeight(this);" 
			                       onkeydown="setTextAreaHeight(this);"
			                       orgValue='${v.repoData }'
				                   sourceElement="true">${v.repoData }</textarea>
			                <a id="${v.repoId }_msg">
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
			            </c:otherwise>           
	                </c:choose>       
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
	             		  <select name="${vc.repoId }" id="${vc.repoId }" sourceElement="true" style="width: 304px;margin-left: 2px;height: 22px;"> 
				              <option value="<%=SysUserType.PURIVEW_DOWNLOAD %>" <c:if test="${vc.repoData == 5 }"> selected="selected"</c:if>>可下载</option>
				              <option value="<%=SysUserType.PURIVEW_BROWSE_SOURCE %>" <c:if test="${vc.repoData == 4 }"> selected="selected"</c:if>>可浏览源文</option>
				              <option value="<%=SysUserType.PURIVEW_BROWSE_VIEW %>" <c:if test="${vc.repoData == 3 }"> selected="selected"</c:if>>可浏览预览版</option>
				              <option value="<%=SysUserType.PURIVEW_CANNOT_BROWSE %>" <c:if test="${vc.repoData == 2 }"> selected="selected"</c:if>>不可预览</option>
				              <option value="<%=SysUserType.PURIVEW_NOSEE %>" <c:if test="${vc.repoData == 1 }"> selected="selected"</c:if>>不可访问</option>
				          </select>
					      <a>
				                <c:if test="${vc.repoRestrict == 'R' }"><img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img></c:if>
				                <c:if test="${vc.repoRestrict == 'M' }"><img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img></c:if>
				                <c:if test="${vc.repoRestrict == 'MA' }"><img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img></c:if>
				                <c:if test="${vc.repoRestrict == 'O' }"><img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img></c:if>
			              </a>
	             		</c:when>
		           		
	             		<c:when test="${fn:toLowerCase(vc.repoName) == 'location' }">
	             			<select name="${vc.repoId }" id="${vc.repoId }" style="width: 304px;margin-left: 2px;height: 22px;" sourceElement="true" orgValue='${vc.repoData }'> 
				              <option value="">请选择...</option>
				              <%=BasicDataUtil.getLocationOptions("") %>
				            </select>
				            
				            <script type="text/javascript">
				            	document.getElementById('${vc.repoId }').value = '${vc.repoData }';
				            </script>
				            
				            <a id="location_msg" name="location_msg">
					             <c:if test="${vc.repoRestrict == 'R' }">
				                     <img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'M' }">
				                     <img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'MA' }">
				                     <img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'O' }">
				                     <img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img>
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
			                         orgValue="${vc.repoData }"
			                         onkeydown="setTextAreaHeight(this);" >${vc.repoData }</textarea>
				              <a id="${vc.repoId }_msg">
					             <c:if test="${vc.repoRestrict == 'R' }">
				                     <img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'M' }">
				                     <img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'MA' }">
				                     <img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'O' }">
				                     <img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img>
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
       <div class="title" style="margin-top: 0">选择分类</div>
	     <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	       <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              学科分类：
	          </td>
	          <td>
	              <select name="rsty_id" id="rsty_id" style="width: 304px;margin-left: 2px;height: 22px;" sourceElement="true" orgValue="<%=resource.getRsdaRstyId() == null ? "" : resource.getRsdaRstyId() %>"> 
		              ${requestScope.subjectOptions }
		          </select>
		      </td>    
	       </tr>
	       <tr bgcolor="#FFFFFF" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE) %>：
	          </td>
	          <td>
	            <select name="rdty_id" id="rdty_id" style="width: 304px;margin-left: 2px;height: 22px;" sourceElement="true" orgValue="<%=resource.getRsdaRdtyId() == null ? "" : resource.getRsdaRdtyId() %>"> 
	              ${requestScope.dynamicTypeOptions }
	            </select>
	          </td>
	       </tr>
	       <tr bgcolor="#FFFFFF" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO) %>：
	          </td>
	          <td>
	            <select name="rdty_id_two" id="rdty_id_two" style="width: 304px;margin-left: 2px;height: 22px;" sourceElement="true" orgValue="<%=resource.getRsdaRdtyId() == null ? "" : resource.getRsdaRdtyId() %>"> 
	              ${requestScope.dynamicTypeOptions2 }
	            </select>
	          </td>
	       </tr>
	       <tr bgcolor="#FFFFFF" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE) %>：
	          </td>
	          <td>
	            <select name="rdty_id_three" id="rdty_id_three" style="width: 304px;margin-left: 2px;height: 22px;" sourceElement="true" orgValue="<%=resource.getRsdaRdtyId() == null ? "" : resource.getRsdaRdtyId() %>"> 
	              ${requestScope.dynamicTypeOptions3 }
	            </select>
	          </td>
	       </tr>
	       
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
	 </table>
		   
	 
	    
	    <div class="title" style="margin-top: 0">上传文件</div>
	     <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	        <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
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
	                  <input type="file" name="bigimg" id="bigimg" class="file text" style="display: none" onchange="fileChang('bigimg',true);">
	               <%}else{ %>
	                  <input type="file" name="bigimg" id="bigimg" class="file text" onchange="fileChang('bigimg',true);">
	                  &nbsp;<span style='color: #808080'>封面文件格式:jpg、jpeg、bmp、png、gif</span>
	               <%} %>
	          </td>
	        </tr>
	   	        
	        <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	               源 文 件：
	          </td>
	          <td>
	               <%if(ToolUnit.NullToBlank(resource.getRsdaFileName()).length() > 3){ %>
	                  <div id="loadfileArea">
		                  <%=ToolUnit.getFileName(ToolUnit.NullToBlank(resource.getRsdaFileName())) %>
		                  
		                  <a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&sourceId=<%=resource.getRsdaId() %>&type=3" target="_blank">预览</a>
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
		 
		 <div class="title" style="margin-top: 0">描述</div>
	     <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	   
		   
		   <tr height="50px">
	          <td style="padding-left: 10px;" align="left">
	               <div class="tool1">
	                      <ul class="nav">
							<li><a href="javascript: checkAction(3,3)" style="padding-top: 3px;" title="通过并继续审核下一条资源" class="btnb">通过并继续 </a></li>
		                    <li><a href="javascript: checkAction(3,0)" style="padding-top: 3px;" title="审核通过">通 过</a></li>
		                    <li><a href="javascript: checkAction(4,0)" style="padding-top: 3px;">不通过</a></li>
							<li><a href="<%=path %>/console/data/memberResource.htm?action=initMemberResourceList" style="padding-top: 3px;">返 回</a></li>
		                  </ul>  
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
</body>
</html>