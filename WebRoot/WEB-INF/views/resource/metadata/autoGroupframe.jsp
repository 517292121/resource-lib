<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>不限级数分类设置</title>
</HEAD>
<FRAMESET id="frame1" border="0" frameSpacing="0"  cols="300,*" frameBorder="NO" rows="*">
  <FRAME name="groupTree" id="groupTree" src="<%=path %>/console/data/resource/defineStore.htm?action=groupTree&rmstId=${rmstId }&rdtyType=${rdtyType }" noResize scrolling="auto">
  <FRAME name="groupInfo" id="groupInfo" src="<%=path %>/console/data/resource/defineStore.htm?action=groupInfo&rmstId=${rmstId }&rdtyType=${rdtyType }" noResize scrolling="auto">
</FRAMESET>
<noframes></noframes>
</HTML>