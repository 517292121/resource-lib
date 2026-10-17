<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>信息导入</title>
    
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/extbutton.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
	
	<style type="text/css">
		.remark{
			color: red;
		}
		.remark li{
			list-style: none;
			height: 20px;
			color: #394748;
		}
		.remark table tr td{
			color: #4A5556;
		}
		.infotable td{
			padding:5px;		
		}
	</style>
	
	<script type="text/javascript">var root = "<%=path %>/";</script>
	<script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
	<script type="text/javascript">
		function onAction(){
			var filename = document.getElementById("excelfile").value;
			if(filename.length < 3){
				Msg.alert("请选择正确的EXCEL数据文件！");
				return;
			}
			Msg.wait('数据导入中...');
			document.all.importform.submit();
		}
		
		function checkFileType(){
			var filename = document.all.excelfile.value;
			filename=filename.substring(filename.lastIndexOf('.')+1,filename.length).toLowerCase();
			if(filename != 'xls'){
				document.getElementById("fileArea").innerHTML = " <input type='file' name='excelfile' class='file text' style='width: 200px;margin-left: 2px;' onchange='checkFileType();'> <span class='tishi'>选择需要导入的Excel文件！</span>";
				Msg.alert("文件格式只能为xls。请重新选择！");
			}
		}
	</script>
  </head>
  
  <body>
    <div class="tool" style="height: 24px;">
          <div style="float: left">
			首页：服务管理 > 信息发布 > 信息管理 > 批量导入
          </div>
	      <ul class="nav" style="float: right;">
		    <li><a href="<%=path %>/console/data/idsInformation.htm?action=initIdsView"><img src="<%=path %>/images/common/common_back.png"></img>返回</a></li>
		  </ul>  
    </div>
	
	<c:if test="${empty error }">
		<form name="importform" action="<%=path %>/console/data/idsImport.htm" enctype="multipart/form-data" method="post">
		 <input type="hidden" name="action" value="importInfo" /> 
		 <table class="infotable" cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
		        <tr bgcolor="#FAFAFA" height="25">
		          <td width="200px" align="right">起始行：</td>
		          <td> <input class="text" name="beginRow" style="width: 200px;margin-left: 2px;" ONKEYPRESS="event.returnValue=isNumbic();" value="2"/> <span class="tishi">从Excel文件中的第几行开始导入，默认为2！</span></td>
		        </tr>
		        
		        <tr bgcolor="#FAFAFA" height="25">
		          <td width="200px" align="right">选择Excel文件：</td>
		          <td id="fileArea"> <input type="file" name="excelfile" id="excelfile" class="file text" style="width: 204px;margin-left: 2px;height: 23px;" onchange="checkFileType();"/> <span class="tishi">选择需要导入的Excel文件！</span></td>
		        </tr>
		          
		        <tr bgcolor="#FAFAFA" height="33">
		          <td width="200px" align="right"> </td>
		          <td>
		          <div class="tool1">
	                      <ul class="nav">
		                    <li><a href="javascript:onAction();" style="padding-top: 2px;">导 入</a></li>
		                  </ul>  
	               </div>
		          </td>
		        </tr>
		        <tr bgcolor="#FAFAFA" height="33">
		          <td width="200px" align="right" valign="top">注意：</td>
		          <td  valign="top">
		          	<div class="remark">
				  	  <li>1：批量导入的文件必须是正规有效的Excle文件(2003版本)！</li>
				  	  <li>2：导入的的Excle文件内容必须遵循如下模板规则：</li>
				  	  <table cellpadding="0" cellspacing="0" style="margin-left: 17px;">
				  	  	<tr>
				  	  		<td width="80px;"><div style="border-bottom: 1px solid #c0c0c0;padding-left: 3px;">参数</div></td>
				  	  		<td width="45px;"><div style="border-bottom: 1px solid #c0c0c0;">Excle列</div></td>
				  	  	</tr>
				  	  	<tr>
				  	  		<td style="padding-left: 3px;">信息标题</td>
				  	  		<td>A</td>
				  	  	</tr>
				  	  	<tr>
				  	  		<td style="padding-left: 3px;">信息分类</td>
				  	  		<td>B</td>
				  	  	</tr>
				  	  	<tr>
				  	  		<td style="padding-left: 3px;">信息内容</td>
				  	  		<td>C</td>
				  	  	</tr>
				  	  </table>
				  </div>
		          </td>
		        </tr>
		        
		        <c:if test="${!empty requestScope.result }">
		        <tr bgcolor="#FAFAFA" height="33">
		          <td width="200px" align="right" valign="top">提示：</td>
		          <td  valign="top">
						${requestScope.result }
		          </td>
		        </tr>
		        </c:if>
		</table>	
	  </form>	
	  
	  
	  
	  
	  </c:if>
	  
	  <c:if test="${ ! empty error }">
	  	  <h3>错误提示</h3>
	  	  <hr />
	  	  <span style="color: red;">${error }</span>
	  </c:if>
	  
  </body>
</html>