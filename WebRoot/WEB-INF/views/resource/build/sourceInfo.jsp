<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	
	IResourceRpc resourceRpc = (IResourceRpc)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("resourceRpc");
	
	JSONRPCBridge.registerObject("resourceRpc",resourceRpc);
	
	long sourceId = MyRequestUtil.getLongAttribute(request,"sourceId");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link> 
	<script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
	<%@ include file="../../common/extInclude.jsp" %> 
	
	<script type="text/javascript">
	   function changLanguage(){
	     try{
		   jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		   var lanId = document.getElementById("lanId").value;
		   var result = jsonrpc.resourceRpc.getSourceInfo("<%=sourceId %>",lanId);
		   document.all.resourceContent.innerHTML = result;
		  }catch(e){
		      return true;
		  }
	   }
	</script>  
  </head>
  
  <body style="margin-top: 0px;">
     <div class="tool">
         已录入语种：
        <select name="lanId" id="lanId" onchange="changLanguage();">
           ${requestScope.languageOptions }
        </select>
     </div>
     <div id="resourceContent">
          ${requestScope.sourceData }
     </div>
  </body>
</html>
