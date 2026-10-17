<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	List<ResElementPool> elementList = (List<ResElementPool>)request.getAttribute("elementList");
%>

<html>
  <head>
    <title>资源预览</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
	<script type="text/javascript">
	    function backGoto(){
	         window.history.go(-1);
	    }
	</script>
  </head>
  
  <body>
  
  	<div class="title" style="margin-top: 0;height: 26px">资源基本信息</div>
    <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC">
       <%
    	ResElementPool element = null;
    	int r=1; String bgc = "";
    	for(int i=0,s=elementList.size(); i<s; i++){
    		element = elementList.get(i);
    		   if(r % 2 == 0){
	              bgc = "#FAFAFA";
	           }else{
	              bgc = "#ffffff";
	           }
    		if(MyRequestUtil.getStringParameter(request,element.getRepoId().toString()).length() > 0){
    			r++;
     %>
     		<tr height="25px;" bgcolor="<%=bgc %>">
	             <td width="250px" align="right">
	               <%=element.getRepoTitle() %>(<%=element.getRepoName() %>)：
	             </td>
	             <td><%=MyRequestUtil.getStringParameter(request,element.getRepoId().toString()) %></td>
	        </tr>     
    <%} }%>
    
    </table>
	<div class="title" style="margin-top: 0;height: 26px">资源分类</div>
	<table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC">
	   <tr bgcolor="#FFFFFF" height="25">
	   	  <td width="250px" align="right">中图法分类：</td>
	   	  <td><%=MyRequestUtil.getStringParameter(request,"rmty_id") %></td>
	   </tr>
	   <tr bgcolor="#FAFAFA" height="25">
	   	  <td width="250px" align="right">学科分类：</td>
	   	  <td>${requestScope.rstyName }</td>
	   </tr>
	   <tr bgcolor="#FFFFFF" height="25">
	   	  <td width="250px" align="right">自定义分类：</td>
	   	  <td>${requestScope.rdtyName }</td>
	   </tr>
	</table>
	    
    <div class="tool1" style="padding-top: 6px;">
        <ul class="nav">
              <li><a href="javascript:backGoto();">返 回</a></li>
        </ul>  
    </div> 
  </body>
</html>
