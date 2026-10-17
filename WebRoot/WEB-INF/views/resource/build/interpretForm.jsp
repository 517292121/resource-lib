<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.core.BasicDataUtil"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	ResSourceData source = (ResSourceData)request.getAttribute("source");
	List<ResElementPool> elementList = (List<ResElementPool>)request.getAttribute("elelist");
	
	long store_id = source.getRsdaRmstId();
	long sourceType = source.getRsdaRltyId();
	
	IResourceRpc resourceRpc = (IResourceRpc)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("resourceRpc");
	
	JSONRPCBridge.registerObject("resourceRpc",resourceRpc);
	long userid = MyRequestUtil.getLoginUserId(request);
	int reqType = MyRequestUtil.getIntAttribute(request,"reqType");
	long languageId = ServletRequestUtils.getLongParameter(request,"languageId",0);
	String rminStatu = ServletRequestUtils.getStringParameter(request,"rminStatu","");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
    <link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
    <link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/extbutton.css" type="text/css"></link>
    <link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
    <script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/build/ResourceData.js"></script>
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
	   function windowBack(){
	       window.location.href = "<%=path %>/console/data/inputSingle.htm?action=interpretResourceList&rminStatu=<%=rminStatu %>&rmin_id=<%=source.getRsdaRminId() %>&lanId=<%=languageId %>";
	   }
	   var element_split = "<%=ConstantsObj.ELEMENT_SPLIT %>";
       var element_data_split = "<%=ConstantsObj.ELEMENT_DATA_SPLIT %>";
	   
	   function updatePassLanguage(){
	       var sourceid = <%=source.getRsdaId() %>;
	       var lanId = document.all.passLanguage.value;
	       changLanguage(sourceid, lanId, <%=store_id %>,<%=sourceType %>);
	   }
	   var maxHeight = <%=ConstantsObj.TEXTAREA_MAX_HEIGHT %>;
	   var addOffset = null;
	</script> 
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/build/interpret.js"></script>
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/build/ResourceData.js"></script>
  </head>
  
  <body onload="initInterpretTextArea();">
       <div class="tool">
          <div style="float: left">
             		<!-- 
                     <ul class="nav">
                       <li><a href="javascript:InterpretSave();" style="padding-top: 3px;">保存</a></li>
                     </ul>  
                     --> 
                     作业：【<a href="<%=path %>/console/data/resource/missionLanguage.htm?action=missionLanguageList&folder=build&rminId=<%=source.getRsdaRminId() %>">${requestScope.source.resMissionInfo.rminMissname }</a>】
                     语言：【<a href="<%=path %>/console/data/inputSingle.htm?action=interpretResourceList&rminStatu=<%=rminStatu %>&rmin_id=<%=source.getRsdaRminId() %>&lanId=${requestScope.languageId }">${requestScope.language.rltyName }</a>】
                     题名：【${requestScope.source.rsdaTitle }】
          </div>
	      <ul class="nav" style="float: right;">
		    <li><a href="javascript:windowBack();"><img src="<%=path %>/images/common/common_back.png"></img>返回</a></li>
		  </ul>  
	   </div>
	   
	 <c:if test="<%=userid>0 %>">  
	 <form action="<%=path %>/console/data/inputSingle.htm?action=SaveInterpret&rminStatu=<%=rminStatu %>&lanId=<%=languageId %>" name="sourceform" id="sourceform" enctype="multipart/form-data" method="post" onsubmit="return checkform();"> 
	   <input type="hidden" name="rmin_id" value="<%=source.getRsdaRminId() %>">
	   <input type="hidden" name="userid" value="<%=userid %>">
	   <input type="hidden" name="sourceId" value="<%=source.getRsdaId() %>">
	   <input type="hidden" name="store" value="<%=source.getRsdaRmstId() %>">
	   <input type="hidden" name="stype" value="<%=source.getRsdaRltyId() %>">
	   <input type="hidden" name="rminId" value="<%=source.getRsdaRminId() %>">
	   <input type="hidden" name="username" value="<%=MyRequestUtil.getLoginUserName(request) %>">
	   
	   <input type="hidden" name="languageId" value="<%=languageId %>">
	   <input type="hidden" name="rminStatu" value="<%=rminStatu %>">
	   <input type="hidden" name="reqType" value="1" id="reqType">
	 <!-- 循环文档开始 -->
	 <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	    <tr align="left">
	       <th class="th">属性名称</th>
	       <th class="th">源数据</th>
	       <th class="th">翻译数据</th>
	    </tr>
	    <tr bgcolor="#ECEDEE" height="25">
          <td width="230px" align="right">
             <img src="<%=path %>/images/extform/bullet_add_2.png"></img>  
               语种：
          </td>
          
          <td> 
             <select name="passLanguage" id="passLanguage" onchange="updatePassLanguage();">
	          ${requestScope.lname } 
             </select>
          </td>
          <td>
              <input name="language_id" id="language_id" type="hidden" value="${requestScope.language.rltyId }">
              ${requestScope.language.rltyName }
          </td>
       </tr>
	     
	 
	    <%
	       int i=1;
	       String bgc = "";
	     %>
	     <c:forEach items="<%=elementList %>" var="v" varStatus="s">
	        <c:if test="${v.repoType < 3}">
	        <% i++; 
	           if(i % 2 == 0){
	              bgc = "#FAFAFA";
	           }else{
	              bgc = "#ffffff";
	           }
	        %>
	         <!-- 循环核心元素 -->
	         <c:if test="${fn:length(v.repoData ) > 0 }">
	         <tr height="25px;" bgcolor="<%=bgc %>">
	             <td width="230px" align="right">
	               <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	               <strong> ${v.repoTitle }(${v.repoName })：</strong> 
	             </td>
	             
	               <td width="230px" style="padding-right: 3px;">
		             <textarea class="eledata" name="data_${v.repoId }" id="data_${v.repoId }">${v.repoData }</textarea>
		           </td>
	             <td valign="top" style="padding-top: 2px;"> 
	             	<c:choose>
	             		<c:when test="${fn:toLowerCase(v.repoName) == 'location' }">
	             			<select name="${v.repoId }" id="${v.repoId }" style="width: 300px;margin-left: 2px;height: 22px;"> 
				              <option value="">请选择...</option>
				              <%=BasicDataUtil.getLocationOptions("") %>
				            </select>
				            
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
			                       sourceElement="true" 
			                       title="${v.repoTitle }" 
			                       onpropertychange="setTextAreaHeight(this);"
			                       onkeyup="setTextAreaHeight(this);" 
			                       onkeydown="setTextAreaHeight(this);"><c:if test="${fn:toLowerCase(v.repoName) == 'date' }"><%=new Date().toLocaleString() %></c:if></textarea>
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
	                
	                <!--<img src="<%=path %>/images/application_put.png" title="${v.repoCode }"></img>--> 
	             </td>
	         </tr>
	         </c:if>
	         
	         <!-- 将所有的元素上级元素再和核心相比，显示出它的所有子元素 -->
	         <c:forEach items="<%=elementList %>" var="vc" varStatus="sc">
	          	 <c:if test="${v.repoId == vc.repoParent and fn:length(vc.repoData ) > 0}">
	          	 <!-- 循环子元素 -->
		         <tr height="25px;" bgcolor="<%=bgc %>">
		           <td width="230px" align="right"> 
		            &nbsp;${vc.repoTitle }(${vc.repoName })：
		           </td>
		           
		           <td width="230px" style="padding-right: 3px;">
		             <input value="${vc.repoData }" class="eledata" name="data_${vc.repoId }" id="data_${vc.repoId }">
		           </td>
		           <td valign="top" style="padding-top: 2px;"> 
		           	<c:choose>
	             		<c:when test="${fn:toLowerCase(vc.repoName) == 'location' }">
	             			<select name="${vc.repoId }" id="${vc.repoId }" style="width: 300px;margin-left: 2px;height: 22px;"> 
				              <option value="">请选择...</option>
				              <%=BasicDataUtil.getLocationOptions("") %>
				            </select>
				            
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
				                    restrict="${vc.repoRestrict }" 
				                    sourceElement="true" 
				                    title="${vc.repoTitle }" 
				                    class="source_text" 
				                    onpropertychange="setTextAreaHeight(this);"
			                        onkeyup="setTextAreaHeight(this);" 
			                        onkeydown="setTextAreaHeight(this);" ><c:if test="${fn:toLowerCase(vc.repoName) == 'created' }"><%=new Date().toLocaleString() %></c:if></textarea>
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
	                 <!-- <img src="<%=path %>/images/application_put.png" title="${vc.repoCode }"></img> --> 
		           </td>
		         </tr>
		         </c:if>
	         </c:forEach>
	         
	       </c:if>
	         
       </c:forEach> 
	 </table>  
	 </form> 
	 
	 <table border="0" width="100%">
		   <tr style="background-color: #ffffff" height="50px">
	          <td width="200px">
	               <div class="tool1" style="padding-top: 6px;">
	                      <ul class="nav">
		                    <li><a href="javascript:InterpretSave(1);" style="padding-top: 3px;">保 存</a></li>
		                    <li><a href="javascript:InterpretSave(2);" style="padding-top: 3px;" class="btnb">保存并继续</a></li>
							<li><a href="javascript:windowBack();" style="padding-top: 3px;"> 返 回 </a></li>
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
	  </c:if>  
	  
	  <!-- 用户登录过期的错误提示 --> 
    <c:if test="<%=userid == 0 %>">
        <h2>错误提示：</h2><hr>
        您还未登录或登录已过期，请登录！
    </c:if>  
    
    <!-- 资源保存成功的图片提示 -->
    <div id="SuccessMessageBox" class="SuccessMessageBox"></div>
    ${requestScope.result }
      
    <!-- 保存等待的提示 --> 
    <div id="WaitMessageBox" class="WaitMessage"></div>
  </body>
</html>
