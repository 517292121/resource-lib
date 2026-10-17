<%@page import="cn.com.unionlib.lucene.bean.ResMainBean"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>索引管理</title>
    
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<style type="text/css">
		*{font-family: 'Microsoft YaHei UI', 'Microsoft YaHei', SimSun, 'Segoe UI',Tahoma, Helvetica, Sans-Serif;}
		.pagetool{height: 40px; margin: 15px; border: 1px solid #D3DBDE; background: #F2F6F8; padding-top: 8px;}
		.pagetool .btn{height: 32px; padding: 0px 15px; margin-left: 5px; border: none; background: #EBEBEB; border: 1px solid #D9D9D9;}
		.pagetool .btn:hover{background: #D9D9D9;transition: all 0.3s;}
		
		.docs{margin: 5px 10px; padding: 2px 0px; background: #F8F8F8;}
		.docs #font{padding-left: 15px; color: #656565;}
		.docs li{width: 200px; float: left; text-align: center;}
		.docs li p{width:100px;height:100px; padding:0px;margin:20px 0px 10px 50px; -moz-border-radius: 50px; -webkit-border-radius: 50px; border-radius:50px;}
		.docs #max{background: #EF553A;}
		.docs #del{background: #9358AC;}
		.docs #num{background: #00ACED;}
		.docs li a{display: block; font-size: 16px; font-weight:bold; line-height: 100px; color: #FFF; text-align:center; text-decoration: none; border-radius: 50%;}
		.docs li a:hover {transition: all 0.3s;}
		.docs #max a:hover{background: #D53E24;}
		.docs #del a:hover{background: #743290;}
		.docs #num a:hover{background: #0091C8;}
		li{list-style-type: none;}
	</style>
	<script type="text/javascript">var basePath = "<%=basePath %>";</script>
	<script type="text/javascript" src="<%=path %>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=path %>/layer/layer.js"></script>
	<script type="text/javascript" src="<%=path %>/js/indexmanager.js"></script>
  </head>
  
  <body>
     <div class="pagetool">
	     <input type="button" value="重建文件资源索引" class="btn" onclick="reResIndex();" style="margin-left: 12px;" />
	     <input type="button" value="重建文本资源索引" class="btn" onclick="reTextIndex();" />
	     <input type="button" value="重建信息资讯索引" class="btn" onclick="reInfoIndex();" />
	     <input type="button" value="重建所有数据索引" class="btn" onclick="reAllIndex();" />
	     <input type="button" value="删除无效索引" class="btn" onclick="deleteInvalidIndex();" />
     </div>
     <ul class="docs">
     	 <p id="font">主索引存放目录：<%=SysConfigInfo.INDEX_MAIN_DIR %></p>
     </ul>
     <ul class="docs">
     	 <p id="font">聚类索引存放目录：<%=SysConfigInfo.INDEX_GROUP_DIR %></p>
     </ul>
     <ul class="docs">
     	 <p id="font">索引文件夹文件数量：<span id="indexFiles"></span></p>
     </ul>
     <ul class="docs">
     	 <p id="font">索引已占用空间大小：<span id="indexSize"></span></p>
     </ul>
     <ul class="docs res" style="padding: 0px;">
     	 <li><p id="max"><a href="<%=basePath %>console/indexManager.htm?action=resourcelist&dt=<%=ResMainBean.DATA_TYPE_RES %>">文件资源</a></p><span id="resdocs">0</span></li> 
	     <li><p id="del"><a href="<%=basePath %>console/indexManager.htm?action=resourcelist&dt=<%=ResMainBean.DATA_TYPE_TEXT %>">文本资源</a></p><span id="textdocs">0</span></li>
	     <li><p id="num"><a href="<%=basePath %>console/indexManager.htm?action=resourcelist&dt=<%=ResMainBean.DATA_TYPE_INFO %>">信息资讯</a></p><span id="infodocs">0</span></li>
     </ul>
     <p class="clear">
     <p style="height: 50px">&nbsp;</p>

  </body>
</html>
