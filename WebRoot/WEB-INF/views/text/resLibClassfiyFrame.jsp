<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>资源库分类设置</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    

</HEAD>
<FRAMESET id="frame1" border="0" frameSpacing="0"  cols="300,*" frameBorder="NO" rows="*">
  <FRAME name="groupTree" id="groupTree" src="<%=path %>/console/data/text/textLibClassfiy.htm?action=initGroupTree&rtliId=${rtliId }" noResize scrolling="auto">
  <FRAME name="groupInfo" id="groupInfo" src="<%=path %>/console/data/text/textLibClassfiy.htm?action=groupInfo&rtliId=${rtliId }" noResize scrolling="auto">
</FRAMESET>
<noframes></noframes>
</HTML>