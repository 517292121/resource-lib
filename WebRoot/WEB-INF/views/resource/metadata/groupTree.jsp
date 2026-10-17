<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.core.TempletConfig"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResDynamicType"%>
<%@page import="java.util.List"%>
<%@page import="java.util.LinkedList"%>
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<%
	String path = request.getContextPath();
	List<ResDynamicType> groupList = null;
	int code = ServletRequestUtils.getIntParameter(request,"rdtyType",0);
	try{
		groupList = (List<ResDynamicType>)request.getAttribute("groupList");
	}catch(Exception e){
		groupList = new LinkedList<ResDynamicType>();
	}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>自定义分类—<%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE) %></title>
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
		    <a href="<%=path %>/console/data/dynamic.htm?action=initImport&rmstId=${param.rmstId }&rdtyType=${rdtyType }" target="_parent"><img src="<%=path %>/images/icon/import.gif"></img>导入</a>
		    <a href="<%=path %>/console/data/dynamic.htm?action=export&rmstId=${param.rmstId }&rdtyType=${rdtyType }"><img src="<%=path %>/images/icon/export.gif"></img>导出</a>
		    <a href="javascript:void(0);" onclick="window.location.reload();"><img src="<%=path %>/images/refresh.png"></img>刷新</a>
		</div>  
  	</div>
    <div class="table" style="padding: 15px;">
        <script>
           var d = new dTree("d");
	       d.add(0,-1,"${groupName }");
           <% ResDynamicType rdty = null;
              for(int i=0,s=groupList.size(); i<s; i++){
                  rdty = groupList.get(i);
           %>
              d.add("<%=rdty.getRdtyId() %>","<%=rdty.getRdtyPid() == null ? 0 : rdty.getRdtyPid() %>","<%=rdty.getRdtyName() %>","<%=path %>/console/data/resource/defineStore.htm?action=groupInfo&rmstId=${rmstId }&rdtyId=<%=rdty.getRdtyId() %>&rdtyType=${rdtyType }","","groupInfo");  
           <%} %>
           
           
           document.write(d);
        </script>
     </div>
  </body>
</html>
