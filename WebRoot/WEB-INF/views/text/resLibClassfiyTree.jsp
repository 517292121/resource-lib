<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="cn.com.adtech.riss2.domain.text.ResLibraryClassify"%>
<% 
	String path = request.getContextPath();
	List<ResLibraryClassify> resLibClassfiyList = null;
	try{
	    resLibClassfiyList = (List<ResLibraryClassify>)request.getAttribute("resLibClassfiyList");
	}catch(Exception e){
	    resLibClassfiyList = new LinkedList<ResLibraryClassify>();
	}
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>资源类型分类</title>
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
  			<div style="float: left;padding-top: 2px;">
	  		&nbsp;<img src="<%=path %>/images/icon/subject.gif"></img>	
	  		分类树结构
	  		</div>
	  		<div class="button_ext" style="float: right;padding-right: 10px;">
	  			<%--
			    <a href="<%=path %>/console/data/resourceTypeGroup.htm?action=initImport&rrtyId=${rrtyId }" target="_parent"><img src="<%=path %>/images/icon/import.gif"></img>导入</a>
			    <a href="<%=path %>/console/data/resourceTypeGroup.htm?action=export&rrtyId=${rrtyId }"><img src="<%=path %>/images/icon/export.gif"></img>导出</a>
			    --%>
			    <a href="javascript:void(0);" onclick="window.location.reload();"><img src="<%=path %>/images/icon/refresh.png"></img>刷新</a>
			</div> 
  	</div>
     <div class="table" style="padding: 15px;">
     	<script>
           var d = new dTree("d");
	       d.add(0,-1,"${rtliName}分类");
           <% ResLibraryClassify rst = null;
              for(int i=0,s=resLibClassfiyList.size(); i<s; i++){
                  rst = resLibClassfiyList.get(i);
                  if(rst.getRlclId() > 0){
           %>
              d.add("<%=rst.getRlclId() %>","<%=rst.getRlclParentId() == null ? 0 : rst.getRlclParentId() %>","<%=rst.getRlclName() %>","<%=path %>/console/data/text/textLibClassfiy.htm?action=groupInfo&rtliId=${rtliId}&rlclId=<%=rst.getRlclId() %>","","groupInfo");  
           <%} }%>
           document.write(d);
        </script>
     </div>
  </body>
</html>
