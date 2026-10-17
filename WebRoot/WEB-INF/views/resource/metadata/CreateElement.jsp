<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IMetadataTemletService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	IMetadataTemletService templetRpc = (IMetadataTemletService)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("metadataTempletRpc");
	JSONRPCBridge.registerObject("templetRpc", templetRpc);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>创建元素</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
	<script type="text/javascript" src="<%=path %>/js/BasicUtil.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jquery.min.js"></script>
	
	<base target="_self">
	<script type="text/javascript">
	   var resultValue = '<%=MyRequestUtil.getLongAttribute(request,"eleId") %>';
	   var eleStatu = true;
    
       function onCloseWin(){
           window.returnValue = resultValue;
       }
       
       function checkElementName(elename){
            try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.templetRpc.checkElementName(elename,"<%=MyRequestUtil.getLongAttribute(request,"rltyId") %>");
		        return result;
	        }catch(e){
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
	   			$("#dataOptions").hide();
	   	   } else if (dtData == 1) {
	   	   		$("#dataOptions").show();
	   	   } 
	   }
	</script>
	
	<style type="text/css">
		table td{padding: 6px 3px;}
		label, input, select {vertical-align: middle;}
		.text{min-width: 220px; height: 26px; border: 1px solid #ccc; outline:none; padding: 2px 5px;}
		#dataOptions{transition:all 0.3s; }
	</style>
  </head>
  
  <body>
    <br>
    <form action="<%=path %>/console/sourceTemplet.htm" onsubmit="return saveElement();" method="post">
    <input type="hidden" name="action" value="CreateElement">
    <input type="hidden" name="eleType" value="${requestScope.eleType }">
    <input type="hidden" name="sourceType" value="${requestScope.rltyId }">
    <input type="hidden" name="parentId" value="${requestScope.parentId }">
    <table width="100%" border="0px">
	    <tr>
	        <td align="right" width="140px"><font style="color:#ff0080">*</font>元素名称：</td>
	        <td id="parent_id" colspan="2"> 
               <input name="attTitle" id="attTitle" class="text" maxlength="40" placeholder="中文名称">
            </td>
	    </tr>
	    
       <tr>
	        <td align="right" width="140px"><font style="color:#ff0080">*</font>元素标签：</td>
	        <td id="parent_id" colspan="2"> 
               <input name="attName" id="attName" class="text" onblur="checkName();" placeholder="英文标签" maxlength="64">
               <a id="attnameMesg"></a>
            </td>
	    </tr>
	    
	    <tr>
	        <td align="right" width="140px">编码体系：</td>
	        <td id="parent_id" colspan="2"> 
               <textarea name="attCode" id="attCode" class="text" style="width: 250px;height: 80px;" placeholder="一行为一个编码体系"></textarea>
            </td>
	    </tr>
	    
	  
   		   <tr>
		        <td align="right" width="140px" valign="top">注入类型：</td>
		        <td id="parent_id">
		        	<p>
			           <input type="radio" name="inputType" id="it_0" value="0" checked="checked" onclick="dataTypeChange();" /><label for="it_0">输入框</label> &nbsp;
		               <input type="radio" name="inputType" id="it_1" value="1" onclick="dataTypeChange();"/><label for="it_1">下拉选择</label>
	               </p>
	               <div id="dataOptions" style="display: none;">
	            	<textarea style="width: 250px;height: 75px;" name="dataType" id="dataType" class="text" placeholder="选项内容，一行为一个选项"></textarea>
	               </div>
	            </td>
		    </tr>
		 
		    
		    <tr>
		        <td align="right" width="140px">Calis收割：</td>
		        <td id="parent_id">
	               <input type="radio" name="repoCalis" id="rc_0" value="0" checked="checked" /><label for="rc_0">不收割</label> &nbsp;
		           <input type="radio" name="repoCalis" id="rc_1" value="1" /><label for="rc_1">收割</label>
	            </td>
		    </tr>
		    
		    <tr>
		        <td align="right" width="140px">默认值：</td>
		        <td id="parent_id" colspan="2"> 
	               <input name="attDefaultdata" id="attDefaultdata" class="text">
	            </td>
		    </tr>
		    
		    <tr>
		        <td align="right" width="140px">显示模式：</td>
		        <td id="parent_id" colspan="2"> 
	               <select name="repoShowmode" id="repoShowmode" style="width: 120px;">
	               	   <option value="0">有内容则显示</option>
	               	   <option value="1">显示</option>
	               	   <option value="2">不显示</option>
	               </select>
	            </td>
		    </tr>
		    
		    
		    <tr>
		        <td align="right" width="140px">约束：</td>
		        <td id="parent_id" colspan="2"> 
	               <select name="attRestrict" id="attRestrict" style="width: 120px;">
	                 <option value="">请选择...</option>
	                 <option value="M">必备(M)</option>
	                 <option value="MA">有则必备 (MA)</option>
	                 <option value="R">强烈推荐(R)</option>
	                 <option value="O">可选(O)</option>
	               </select>
	            </td>
		    </tr>

			<tr>
		        <td align="right" width="140px">列表显示：</td>
		        <td id="parent_id" colspan="4">
		           <input type="radio" name="repoIsShowList" id="ls_0" value="0" checked="checked"/><label for="ls_0">否</label> &nbsp;
	               <input type="radio" name="repoIsShowList" id="ls_1" value="1"/><label for="ls_1">是</label>
		        	<span style="color: #808080;padding-top: 4px;">（是否在资源列表页面显示）</span>
	            </td>
		    </tr>
		    <tr>
			        <td align="right" width="140px">序号：</td>
			        <td id="parent_id" colspan="2"> 
		               <input name="repoIndex" id="repoIndex" class="text">
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
