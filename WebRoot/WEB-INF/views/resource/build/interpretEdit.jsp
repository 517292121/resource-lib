<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.core.BasicDataUtil"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.com.adtech.riss2.jdbcms.ToolUnit"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	ResSourceData source = (ResSourceData)request.getAttribute("resource");
	List<ResElementPool> elementList = (List<ResElementPool>)request.getAttribute("EleDataList");
	
	long store_id = source.getRsdaRmstId();
	long sourceType = source.getRsdaRltyId();
	
	IResourceRpc resourceRpc = (IResourceRpc)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("resourceRpc");
	
	JSONRPCBridge.registerObject("resourceRpc",resourceRpc);
	long userid = MyRequestUtil.getLoginUserId(request);
	int reqType = MyRequestUtil.getIntAttribute(request,"reqType");
	if(reqType < 1){
		reqType = MyRequestUtil.getIntParameter(request,"reqType");
	}
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>资源其它语编辑</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
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
  
  <body onload="initTextAreaHeight();">
	 <c:if test="<%=userid>0 %>">  
	 <form action="<%=path %>/console/data/resource/resourceManager.htm?action=saveInterpretEdit&rminId=<%=source.getRsdaRminId() %>&lanId=${param.languageId }&rminStatu=${param.rminStatu }" name="sourceform" id="sourceform" method="post" onsubmit="return checkform();"> 
	   <input type="hidden" name="sourceId" value="<%=source.getRsdaId() %>">
	   <input type="hidden" name="store" value="<%=source.getRsdaRmstId() %>">
	   <input type="hidden" name="stype" value="<%=source.getRsdaRltyId() %>">
	   <input type="hidden" name="languageId" value="${param.languageId }">
	   <input type="hidden" name="reqType" value="<%=reqType %>">
	   
	   <input type="hidden" name="rminStatu" value="${param.rminStatu }">
	 <!-- 循环文档开始 -->
	 <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	    <tr align="left">
	       <th class="th">属性名称</th>
	       <th class="th">源数据</th>
	       <th class="th">翻译数据</th>
	    </tr>
	    <%
	       int i=1;
	       String bgc = "";
	     %>
	     <tr height="25px;" bgcolor="fafafa">
	         <td width="230px" align="right">语种： </td>
	         
	         <td> 
	             <c:choose>
	                <c:when test="${fn:length(passLanguage) > 1}">
			             <select name="passLanguage" id="passLanguage" onchange="updatePassLanguage();">
				          ${requestScope.passLanguage } 
			             </select>
	                </c:when>
	                <c:otherwise>
	                 	 <span style="color: #c0c0c0">该资源没有已通过的语种</span>
	                </c:otherwise>
	             </c:choose>
	          </td>
	         
	         <td style="color: #808080">
	             <input type="hidden" name="newLanguage" id="newLanguage" value="${requestScope.languageOption.rltyId }">
	             ${requestScope.languageOption.rltyName }
	         </td>
         </tr>
	     <c:forEach items="<%=elementList %>" var="v" varStatus="s">
	        <c:if test="${v.repoType < 3 }">
	        <% i++; 
	           if(i % 2 == 0){
	              bgc = "#ffffff";
	           }else{
	              bgc = "#FAFAFA";
	           }
	        %>
	         <!-- 循环核心元素 -->
	         <c:if test="${fn:length(v.repoData)>0 }">
	         <tr height="25px;" bgcolor="<%=bgc %>">
	             <td width="230px" align="right">
	               <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	               <strong> ${v.repoTitle }(${v.repoName })：</strong> 
	             </td>
	             
	             <td width="230px"> 
	               <textarea class="eledata" name="data_${v.repoId }" id="data_${v.repoId }"></textarea>
	             </td>
	             
	             <td> 
	             	<c:choose>
	             		<c:when test="${fn:toLowerCase(v.repoName) == 'location' }">
	             			<select name="${v.repoId }" id="${v.repoId }" style="width: 300px;margin-left: 2px;height: 22px;"> 
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
			                       sourceElement="true" 
			                       title="${v.repoTitle }" 
			                       onpropertychange="setTextAreaHeight(this);"
			                       onkeyup="setTextAreaHeight(this);" 
			                       onkeydown="setTextAreaHeight(this);">${v.repoData }</textarea>
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
	          	 <c:if test="${v.repoId == vc.repoParent and fn:length(vc.repoData) > 0}">
	          	 <!-- 循环子元素 -->
		         <tr height="25px;" bgcolor="<%=bgc %>">
		           <td width="230px" align="right"> 
		            &nbsp;${vc.repoTitle }(${vc.repoName })：
		           </td>
		           
		           <td width="230px">
		              <textarea value="${vc.repoId }" class="eledata" name="data_${vc.repoId }" id="data_${vc.repoId }"></textarea>
		           </td>
		           
		           <td> 
		          	 <c:choose>
	             		<c:when test="${fn:toLowerCase(vc.repoName) == 'location' }">
	             			<select name="${vc.repoId }" id="${vc.repoId }" style="width: 300px;margin-left: 2px;height: 22px;"> 
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
				                    restrict="${vc.repoRestrict }" 
				                    sourceElement="true" 
				                    title="${vc.repoTitle }" 
				                    class="source_text" 
				                    onpropertychange="setTextAreaHeight(this);"
			                        onkeyup="setTextAreaHeight(this);" 
			                        onkeydown="setTextAreaHeight(this);">${vc.repoData }</textarea>
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
		                    <li><a href="javascript:InterpretSave();" style="padding-top: 3px;">保 存</a></li>
							<c:if test="<%=reqType == 1 %>">
							<li><a href="<%=path %>/console/data/inputSingle.htm?action=interpretResourceList&rminStatu=${param.rminStatu }&rmin_id=<%=source.getRsdaRminId() %>&lanId=${param.languageId }" style="padding-top: 3px;"> 返 回 </a></li>
							</c:if>
							<c:if test="<%=reqType == 2 %>">
							<li><a href="<%=path %>/console/data/resource/SourceManager.htm?action=resourceLanguageList&sourceId=<%=source.getRsdaId() %>" style="padding-top: 3px;"> 返 回 </a></li>
							</c:if>
							
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
    
    <c:if test="${fn:length(passLanguage) > 1}">
	    <script type="text/javascript">
	       updatePassLanguage(); //初始化已翻译语的值
	    </script>  
    </c:if>
  </body>
</html>
