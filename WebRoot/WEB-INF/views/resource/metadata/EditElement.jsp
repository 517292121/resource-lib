<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IMetadataTemletService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	ResElementPool element = (ResElementPool)request.getAttribute("element");
	long lanId = element.getRepoPltyId();
	
	IMetadataTemletService templetRpc = (IMetadataTemletService)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("metadataTempletRpc");
	JSONRPCBridge.registerObject("templetRpc", templetRpc);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>修改元素</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
	<script type="text/javascript" src="<%=path %>/js/BasicUtil.js"></script>
	
	<base target="_self">
	<script type="text/javascript">
	   var Sys = {}; 
       var ua = navigator.userAgent.toLowerCase(); 
       if (window.ActiveXObject) 
           Sys.ie = ua.match(/msie ([\d.]+)/)[1] 
       else if (document.getBoxObjectFor) 
           Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1] 
       else if (window.MessageEvent && !document.getBoxObjectFor) 
           Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1] 
       else if (window.opera) 
           Sys.opera = ua.match(/opera.([\d.]+)/)[1] 
       else if (window.openDatabase) 
           Sys.safari = ua.match(/version\/([\d.]+)/)[1];
       // 换行符
       if (Sys.ie) {
          <%request.setAttribute("hhf", "\r\n");%>
       }else {
          <%request.setAttribute("hhf", "\n");%>
       }
	   var resultValue = '<%=MyRequestUtil.getLongAttribute(request,"eleId") %>';
	   var eleStatu = true;
    
       function onCloseWin(){
           window.returnValue = resultValue;
       }
       
       function checkElementName(elename){
            try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.templetRpc.checkElementName(elename,"<%=lanId%>");
		        return result;
	        }catch(e){
	            alert(e.message);
	            return true;
	        }
       }
	
	   function checkName(){
           var attName = document.getElementById("attName").value;
           if(attName.length > 0){
               if(checkElementName(attName) == false){
                   document.getElementById("attnameMesg").innerHTML = "<span style='color:red'>该元素标签已存在！</span>";
                   eleStatu = false;
               }else{
                   document.getElementById("attnameMesg").innerHTML = "<span style='color:blue'>该元素标签可用！</span>";
                   eleStatu = true;
               }
           } 
       }
       
	   function saveElement(){
	       var attTitle = document.getElementById("attTitle").value;
	       var attName = document.getElementById("attName").value;
	       var attCode = document.getElementById("attCode").value
	       if(attTitle.length < 1){
	           alert("元素名称不能为空！");
	           return false;
	       }
	       if(attName.length < 1){
	           alert("元素标签不能为空！");
	           return false;
	       }
	       if(attCode.length > 170){
	       	   alert("编码体系不能超过170个字符！");
	       	   return false;
	       }
	       if(eleStatu == false){
	           alert("该元素标签已存在，请重新录入！");
	           return false;
	       }
	       var dt = document.getElementsByName("inputType");
	   	   var dtData = jswork.getRadioValue(dt);
	   	   if(dtData == 1){
	   	   	   if(document.getElementById("dataType").value.length < 1){
	   	   	   	   alert("请输入元素下拉选项值！");
	           	   return false;
	   	   	   }
	   	   }
	   }
	   function dataTypeChange(){
	   	   var dt = document.getElementsByName("inputType");
	   	   var dtData = jswork.getRadioValue(dt);
	   	   if(dtData == 0){
	   	   	   document.getElementById("dataOptions").style.display = "none";
	   	   } else if (dtData == 1) {
	   	   	   document.getElementById("dataOptions").style.display = "block";
	   	   } 
	   }
	</script>
	
	<style type="text/css">
		html, body{height: auto;}
		table td{padding: 6px 3px;}
		label, input, select {vertical-align: middle;}
		.text{min-width: 220px; height: 26px; border: 1px solid #ccc; outline:none; padding: 2px 5px;}
		#dataOptions{transition:all 0.3s; }
	</style>
  </head>
  
  <body>
    <form action="<%=path %>/console/sourceTemplet.htm" onsubmit="return saveElement();" method="post">
    <input type="hidden" name="action" value="UpdateElement">
    <input type="hidden" name="eleId" value="${requestScope.element.repoId }">
    <input type="hidden" name="eleType" value="${requestScope.element.repoType }">
    <input type="hidden" name="sourceType" value="${requestScope.element.repoPltyId }">
    <input type="hidden" name="parentId" value="${requestScope.element.repoParent }">
    <c:if test="${element.repoType == 1}">
    <input type="hidden" name="attName" value="${requestScope.element.repoName }">
    </c:if>
    <table width="100%" style="margin-top: 5px;">
	    <tr>
	        <td align="right" width="140px"><font style="color:#ff0080">*</font>元素名称：</td>
	        <td id="parent_id" colspan="2"> 
               <input name="attTitle" id="attTitle" class="text" value="${requestScope.element.repoTitle }" placeholder="中文名称">
               <c:if test="${element.repoType == 1}">（${requestScope.element.repoName }）</c:if>
            </td>
	    </tr>
	    
	   <c:if test="${element.repoType != 1}">
       <tr>
	        <td align="right" width="140px"><font style="color:#ff0080">*</font>元素标签：</td>
	        <td id="parent_id" colspan="2" height="35px;"> 
               <input name="attName" id="attName" class="text" onblur="checkName();" value="${requestScope.element.repoName }" placeholder="英文标签">
               <a id="attnameMesg"></a>
            </td>
	    </tr>
	    </c:if>
	    
	    <tr>
	        <td align="right" width="140px">编码体系：</td>
	        <td id="parent_id" colspan="2"> 
               <textarea name="attCode" id="attCode" class="text" style="width: 250px;height: 80px;" placeholder="一行为一个编码体系">${fn:replace(requestScope.element.repoCode,',',hhf) }</textarea>
            </td>
	    </tr>
	    
	    <tr>
	        <td align="right" width="140px" valign="top">注入类型：</td>
	        <td id="parent_id">
	        	<p>
	           		<input type="radio" name="inputType" value="0" id="it_0" <c:if test="${empty element.repoDatatype or element.repoDatatype == 0 }">checked="checked"</c:if> onclick="dataTypeChange();"/><label for="it_0">输入框</label> &nbsp;
               		<input type="radio" name="inputType" value="1" id="it_1" <c:if test="${element.repoDatatype == 1 }">checked="checked"</c:if> onclick="dataTypeChange();"/><label for="it_1">下拉选择</label>
               	</p>
               		
            	<c:if test="${empty element.repoDatatype or element.repoDatatype == 0 }"><div id="dataOptions" style="display: none;"></c:if>
            	<c:if test="${element.repoDatatype == 1 }"><div id="dataOptions" style="display: block;"></c:if>
            	<textarea style="width: 250px;height: 75px;" name="dataType" id="dataType" class="text" placeholder="一行为一个编码体系">${fn:replace(requestScope.element.repoDataOptions,',',hhf) }</textarea>
            	</div>
            </td>
		</tr>
	    
	    <tr>
	        <td align="right" width="140px">Calis收割：</td>
	        <td id="parent_id">
               <input type="radio" name="repoCalis" value="0" id="rc_0" <c:if test="${empty element.repoCalis or element.repoCalis == 0 }">checked="checked"</c:if> /><label for="rc_0">不收割</label> &nbsp;
	           <input type="radio" name="repoCalis" value="1" id="rc_1" <c:if test="${element.repoCalis == 1 }">checked="checked"</c:if> /><label for="rc_1">收割</label>
            </td>
	    </tr>
	    
	    <tr>
	        <td align="right" width="140px">默认值：</td>
	        <td id="parent_id" colspan="2"> 
               <input name="attDefaultdata" id="attDefaultdata" class="text" value="${requestScope.element.repoDefaultdata }">
            </td>
	    </tr>
	    
	    <tr>
	        <td align="right" width="140px">显示模式：</td>
	        <td id="parent_id" colspan="2"> 
               <select name="repoShowmode" id="repoShowmode" style="width: 120px;">
               	   <option value="0" <c:if test="${element.repoShowmode == 0 }">selected="selected"</c:if>>有内容则显示</option>
               	   <option value="1" <c:if test="${element.repoShowmode == 1 }">selected="selected"</c:if>>显示</option>
               	   <option value="2" <c:if test="${element.repoShowmode == 2 }">selected="selected"</c:if>>不显示</option>
               </select>
            </td>
	    </tr>
	    
	    <tr>
	        <td align="right" width="140px">约束：</td>
	        <td id="parent_id" colspan="2"> 
               <select name="attRestrict" id="attRestrict" style="width: 120px;">
                 <option value="M">必备(M)</option>
                 <option value="MA">有则必备 (MA)</option>
                 <option value="R">强烈推荐(R)</option>
                 <option value="O">可选(O)</option>
               </select>
               
               <script type="text/javascript">
                  document.getElementById("attRestrict").value = '${requestScope.element.repoRestrict }'
               </script>
            </td>
	    </tr>
	    
	    <tr>
		        <td align="right" width="140px">列表显示：</td>
		        <td id="parent_id" colspan="4">
		           <input type="radio" name="repoIsShowList" value="0" id="ls_0" <c:if test="${empty element.repoIsShowList or element.repoIsShowList == 0 }">checked="checked"</c:if> /><label for="ls_0">否</label> &nbsp;
	               <input type="radio" name="repoIsShowList" value="1" id="ls_1" <c:if test="${element.repoIsShowList == 1 }">checked="checked"</c:if>/><label for="ls_1">是</label>
		        	<span style="color: #808080;padding-top: 4px;">（是否在资源列表页面显示）</span>
	            </td>
	    </tr>
	    
	    <tr>
		        <td align="right" width="140px">序号：</td>
		        <td id="parent_id" colspan="2"> 
	               <input name="repoIndex" id="repoIndex" class="text" value="${requestScope.element.repoIndex }">
	            </td>
	    </tr>
	    
	    <tr height="38">
	    	<td>&nbsp;</td>
             <td class="td_submit">
               <input type="submit" value=" 提 交 " class="button" style="width: 100px; height:30px;" /> 
             </td>
          </tr>
          
          <tr>
             <td colspan="2" style="padding-left: 90">
            ${requestScope.result }
             </td>
          </tr>
    </table>
    </form>
  </body>
</html>
<%request.removeAttribute("hhf");%>