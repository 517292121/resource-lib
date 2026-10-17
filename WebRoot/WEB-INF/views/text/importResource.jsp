<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>资源批量导入</title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    
	<link rel="stylesheet" href="<%=path %>/css/common.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
  	<style type="text/css">
  		*{font-size: 14px;}
  		table td{height: 36px; padding: 7px 0px;}
  		.text{width: 400px;height: 80px;}
  		.rfile{width: 280px; height: 28px; margin-left: 1px;}
  		.impMsg{padding-top: 15px; line-height: 28px;}
  		.prompt{color: #808080;padding-left: 10px;}
  		
  		.tips{
  		    display: block;
		    width: 100%;
		    text-align: left;
		    text-indent: 40px;
		    font-size: 18px;
		    height: 50px;
		    line-height: 50px;
		    background-color: #e3e3e3;
  		}
  		.tip-de{
  			text-indent: 90px;
  		}
  	</style>
  	<script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/BasicUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/tableUtil.js"></script>
    <script type="text/javascript">
    	function onSubmit(){
    		if(document.getElementById("beginLine").value.length < 1){
    			Msg.fail('请输入导入起始行！');
    			return;
    		}
    		
    		if(document.getElementById("classfiy").value.length < 1){
    			Msg.fail('请输入Excel分类列设置！');
    			return;
    		}
    		
    		if(document.getElementById("importExcel").value.length < 1){
    			Msg.fail('请选择导入EXCEL文件！');
    			return;
    		}
    		
    		Msg.wait("数据提交中...");
    		document.getElementById("importForm").submit();
    	}
    	function goBack(){
    		if('${source}' == 'resList'){
    			window.location.href = "<%=path %>/console/data/text/textResource.htm?action=resourceList&rtliId=${rtliId}";
    		}else if ('${source}' == 'resLib'){
	    		window.location.href = "<%=path %>/console/data/text/textResource.htm";
    		}else{
    			window.location.href = "<%=path %>/console/data/text/textResource.htm?action=resourceList&rtliId=${rtliId}";
    		}
    	}
    </script>
  </head>
  
  <body style="padding-bottom: 100px;" onload="tableBg()">
  	<div class="tool extbtn" style="margin-top: 0">
  		<div style="float: left;padding-top: 3px;">
  			<img src="<%=path %>/images/extform/02.png"></img> 资源导入
  		</div>
  		<ul class="nav" style="float: right;padding-right: 10px;">
		    <li><a href="javascript:goBack();"><img src="<%=path %>/images/console/icon/common_back.png" align="absmiddle"></img> 返回</a></li>
		</ul> 
  	</div>
  	
  	<div>
  	<c:choose>
		<c:when test="${!empty error }">
			<div style="padding: 0px 10px 0px 20px;">
  			<h2>错误提示</h2><hr />
  			<span style="color: red">${error }</span>
  			</div>
  		</c:when>
  		
  		<c:otherwise>
			<form action="<%=path %>/console/data/text/textResourceImport.htm" id="importForm" method="post" enctype="multipart/form-data">	
				<input type="hidden" name="action" value="importResourceData" />
				<input type="hidden" name="rtliId" value="${rtliId }" />	
				<input type="hidden" name="source" value="${source }" />
			    <table width="100%" cellpadding="0" cellspacing="0" id="Page_data">
			    	<tr>
			    		<td width="180px;" align="right">导入库类型：</td>
			    		<td style="color: #007EB8">${rtli.rtliName }</td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">导入起始行：</td>
			    		<td>
			    			<input type="text" name="beginLine" id="beginLine" value="2" class="htmlText" style="width: 215px">
			    			<span class="prompt">资源从EXCEL文件中的第几行开始读取</span>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">封面目录：</td>
			    		<td>
			    			<input type="text" name="fileFolder" id="fileFolder" value="${fileFolder }" class="htmlText" style="width: 215px">
			    			<span class="prompt">资源的封面文件在服务器中的存放位置，格式如：“D:/ResourceFile/images/201710”</span>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">excel文件：</td>
			    		<td>
			    			<input type="file" name="importExcel" id="importExcel" class="htmlFile rfile">
			    		</td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">封面列设置：</td>
			    		<td>
			    			<input type="text" name="coverF" id="coverF" value="" class="htmlText" maxlength="2" style="width: 215px">
			    			<span class="prompt">Excel封面列设置。封面格式如：“coverImage1.png”</span>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right"><span style="color: red">*</span>分类列设置：</td>
			    		<td>
			    			<input type="text" name="classfiy" id="classfiy" value="" maxlength="2" class="htmlText" style="width: 215px">
			    			<span class="prompt">Excel分类列设置，必须关联分类</span>
			    		</td>
			    	</tr>
			    	
			    	
			    	<tr height="38px;">
			    		<td>&nbsp;</td>
			    		<td style="padding-top: 10px;padding-left: 1px;">
			    			<input type="button" value=" 保 存 " onclick="onSubmit()" class="saveBtn" />
			    			<input type="button" value=" 返 回 " onclick="goBack()" class="saveBtn" />
			    		</td>
			    	</tr>
			    	
			    	<tr></tr>
			    	<c:if test="${!empty impMsg }">
			    	<tr>
			    		<td>&nbsp;</td>
			    		<td>
			    			<div class="impMsg">${impMsg }</div>
			    		</td>
			    	</tr>
			    	</c:if>
			    </table>
		    </form>
  		</c:otherwise>
  	</c:choose>
	  	<div>
	  		<span class="tips">注意：</span>
	  		<p CLASS="tip-de">1.导入前请确认导入设置正确</p>
	  		<p CLASS="tip-de">2.导入前请确认导入数据格式和模板一致，模板设置如下:</p>
	  		<table width="100%" cellpadding="0" cellspacing="0">
	  			<c:if test="${!empty rtli.rtliTitle}">
			    	<tr>
			    		<td width="180px;" align="right">题名标题：</td>
			    		<td style="color: #007EB8">${rtli.rtliTitleImp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol1Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol1Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol1Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol2Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol2Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol2Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol3Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol3Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol3Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol4Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol4Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol4Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol5Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol5Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol5Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol6Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol6Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol6Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol7Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol7Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol7Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol8Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol8Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol8Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol9Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol9Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol9Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol10Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol10Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol10Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol11Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol11Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol11Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol12Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol12Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol12Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol13Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol13Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol13Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol14Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol14Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol14Imp }</td>
			    	</tr>
		    	</c:if>
		    	<c:if test="${!empty rtli.rtliCol15Label}">
			    	<tr>
			    		<td width="180px;" align="right">${rtli.rtliCol15Label }：</td>
			    		<td style="color: #007EB8">${rtli.rtliCol15Imp }</td>
			    	</tr>
		    	</c:if>
		    	<tr>
		    		<td width="180px;" align="right">（可选）封面：</td>
		    		<td style="color: #007EB8">自定义</td>
		    	</tr>
			</table>
	  	</div>
  	</div>
  </body>
</html>