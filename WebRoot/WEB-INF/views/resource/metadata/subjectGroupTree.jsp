<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSubjectType"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%
	String path = request.getContextPath();
	List<ResSubjectType> subjectList = null;
	try{
		subjectList = (List<ResSubjectType>)request.getAttribute("subjectList");
	}catch(Exception e){
		subjectList = new LinkedList<ResSubjectType>();
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>学科分类</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" href="<%=path %>/css/dtree.css" type="text/css" />
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<script type="text/javascript">
	<!--
		var root = "<%=path %>/";
	//-->
	</script>
	<script type="text/javascript" src="<%=path %>/js/dtree.js"></script>
	
  </head>
  
   <body style="overflow: auto;">
  	<div class="title" style="margin-top: 0;background-image: url('<%=path %>/images/subject_bg.jpg');height:29px;padding-top:5px;">
	  		&nbsp;<img src="<%=path %>/images/icon/subject.gif"></img>	
	  		分类树结构
  	</div>
     <div class="table" style="padding: 15px;">
     	<script>
           var d = new dTree("d");
	       d.add(0,-1,"学科分类");
           <% ResSubjectType rst = null;
              for(int i=0,s=subjectList.size(); i<s; i++){
                  rst = subjectList.get(i);
                  if(rst.getRstyId() > 0){
           %>
              d.add("<%=rst.getRstyId() %>","<%=rst.getRstyPid() == null ? 0 : rst.getRstyPid() %>","<%=rst.getRstyName() %>","<%=path %>/console/data/resSubjectType.htm?action=groupInfo&rstyId=<%=rst.getRstyId() %>","","groupInfo");  
           <%} }%>
           
           
           document.write(d);
        </script>
     </div>
  </body>
</html>
