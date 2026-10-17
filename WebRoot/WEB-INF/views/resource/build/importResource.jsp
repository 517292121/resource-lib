<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>导入模板设置</title>
    
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/console/resimp.css" type="text/css"></link>
	<style type="text/css">
		.tabbox{
		    background: none;
		}
		.tabbox .content li table tr td {
		    padding: 0px 5px;
		}
	</style>
	<script type="text/javascript">var rootPath = "<%=path %>";</script>
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/metadata/importTemplet.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jquery-1.8.3.min.js"></script>
	<script type="text/javascript" src="<%=path %>/layer/layer.js"></script>
	<script type="text/javascript">
		
		//封装layer数据提交时带遮罩的加载层
		function loading(){
			layer.msg('文件导入中，请不要关闭本页..', {
				icon: 16,
				shade: [0.5, '#000'],
				scrollbar: false,
				time:3600000
			}) ;
		}
		function imp01(){
			var isCheck = document.getElementById("exitSourcefile").checked;
			if(isCheck){
				var fileFormat = document.getElementById("sourceFormat").value;
				if(fileFormat.trim().length < 1){
					layer.msg("请输入文件格式");
					return;
				}
			}
			if(!checkXls("xlsFile01")){
				return;
			}

			loading();
			$("#form01").submit();
		}
		
	
		function imp02(){
			if(!checkXls("xlsFile02")){
				return;
			}
			if($("#imgRootDir").val().length < 1){
				layer.tips('资源目录不能为空！', '#imgRootDir', {
				  tips: [2, '#DF633D'],
				  time: 5000
				});
				return;
			}

			loading();
			$("#form02").submit();
		}
		function imp03(){
			if(!checkXls("xlsFile03")){
				return;
			}
			if($("#fileRootUrl").val().length < 1){
				layer.tips('html根路径不能为空！', '#fileRootUrl', {
				  tips: [2, '#DF633D'],
				  time: 5000
				});
				return;
			}
			if($("#indexHtml").val().length < 1){
				layer.tips('html引导页名称不能为空！', '#indexHtml', {
				  tips: [2, '#DF633D'],
				  time: 5000
				});
				return;
			}
			
			loading();
			$("#form03").submit();
		}
		
		
		function checkXls(fileId){
			var filePath = $("#"+fileId).val();
			if(filePath.length < 2){
				layer.tips('请选择需导入的excel文件！', '#'+fileId, {
				  tips: [2, '#DF633D'],
				  time: 5000
				});
				return false;
			}
			var fmt = filePath.substring(filePath.lastIndexOf('.')+1,filePath.length).toLowerCase();
			if(fmt != "xls"){
				layer.tips('请选择xls格式的excel文件！', '#'+fileId, {
				  tips: [2, '#DF633D'],
				  time: 5000
				});
				return false;
			}
			
			return true;
		}
		
		function checkFile(){
			var isCheck = document.getElementById("exitSourcefile").checked;
			if(isCheck){
				document.getElementById("fileFormat").style.display = "block";
			}else{
				document.getElementById("fileFormat").style.display = "none";
			}
		}
		
		//文件上传tab切换处理
		 $(function(){
			$('.tabbox .content ul').width(800*$('.tabbox .content li').length+'px');
			$(".tabbox .tab a").click(function(){
				$(this).addClass('on').siblings().removeClass('on');
				var index = $(this).index();
				var distance = -800*index;
				$('.tabbox .content ul').stop().animate({
					left:distance
				});
				
				rehei(index);
			});
			rehei("0");
		 });
		 
		 
		 function rehei(tnum){
		 	var hei = $("#tab"+tnum).height();
		 	hei = parseInt(hei);
		 	
		 	$('.tabbox .content').css("height",(hei+15)+'px');
		 	$(".tabbox").css("height",(hei+38+15)+'px');
		 }
		 
		  //将输入框中的“\”转换为“/”	
		function urlpath(obj){
		    var urlVal = $(obj).val();
		    if (urlVal) {
		        urlVal = urlVal.replace(/\\/g, "/");
		        $(obj).val(urlVal)
		    }
		}
	</script>
  </head>
  
  <body>
    <div class="pagenav">
     	首页：资源建设 > ${requestScope.missionName } > <a href="<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id=${requestScope.rminId }">数据录入</a> > 批量导入
    </div>
	
	<div class="tool">
		<a href="<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id=${requestScope.rminId }">&lt;返回列表</a>
		<a href="<%=path %>/console/data/resource/importExcel.htm?action=impdesc&rminId=${rminId }">导入说明</a>
	</div>
	<div class="form">
		 <div class="tabbox">
			<div class="tab">
		    	<a href="javascript:;" class="on" data-type='0'>文件格式</a>
		        <a href="javascript:;" data-type='1'>图片格式</a>
		        <a href="javascript:;" data-type='2'>HTML格式</a>
		    </div>
		    <div class="content">
		    	<ul>
		    		<!-- 文件格式 -->
		        	<li id="tab0">
		        		<form name="form01" id="form01" action="<%=path %>/console/data/resource/importExcel.htm" enctype="multipart/form-data" method="post">
						 <input type="hidden" name="action" value="importResourceData" /> 
						 <input type="hidden" name="rminId" value="${requestScope.rminId }" />
						 <input type="hidden" name="resFileType" id="resFileType" value="0" /> 
		        		 <table cellpadding="0" width="100%" border="0" cellspacing="0">
		        			<tr>
					          <td width="200px" align="right">文件目录名称：</td>
					          <td style="padding-left: 8px;">${requestScope.missionName }</td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">文件设置：</td>
					          <td> 
					          	  <input type="checkbox" name="exitFaceimg" value="1" id="c10" /><label for="c10">忽略封面存在</label> &nbsp;
					          	  <input type="checkbox" name="convertfileName" value="1" id="c11" /><label for="c11">将源文文件名转码</label> &nbsp;
					          	  <input type="checkbox" name="exitSourcefile" id="exitSourcefile" value="1" onclick="checkFile();" /><label for="exitSourcefile">忽略源文件存在</label>
					          	  <div id="fileFormat" class="filedef">
						          	  	源文件格式：<input type="text" name="sourceFormat" id="sourceFormat" style="width: 60px" />
						          	  	&nbsp; &nbsp; 
						          	  	源文预览格式：
						          	  	<select name="viewFormat" id="viewFormat">
						          	  		<option value="swf">swf</option>
						          	  		<option value="flv">flv</option>
						          	  		<option value="mp3">mp3</option>
						          	  		<option value="jpg">jpg</option>
						          	  	</select>
						          	  	
						          	  	<br />
						          	  	<p>注：请保持填写的格式与文件扩展名大小写保持一致。导入完成后最好用文档记录下相关文件路径等信息，避免日后遗忘！</p>
					          	  </div>
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">导入设置：</td>
					          <td> 
					          	  <input type="checkbox" name="checkMust" value="1" id="c1" /><label for="c1">忽略必填字段</label> &nbsp;
					          	  <input type="checkbox" name="checkSubject" value="1" id="c2" /><label for="c2">将不存在的主题新增到作业</label>
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">自定义分类匹配类型：</td>
					          <td style="padding-left: 2px;">
					          	  <input type="radio" name="impGroupType" value="0" id="r1" checked="checked" /><label for="r1">按分类名称</label> &nbsp;
					          	  <input type="radio" name="impGroupType" value="1" id="r2" /><label for="r2">按分类号</label>
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">起始行：</td>
					          <td><input class="input-text" name="beginRow" ONKEYPRESS="event.returnValue=isNumbic();" value="2" /> <span class="tishi">从Excel文件中的第几行开始导入，默认为2！</span></td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">选择Excel文件：</td>
					          <td><input type="file" name="excelfile" id="xlsFile01" class="xlsfile" /> <span class="tishi">选择需要导入的Excel文件！</span></td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">&nbsp;</td>
					          <td>
					          	<a href="javascript:imp01();" class="imp-btn">导 入</a>
					          	<a href="<%=path %>/res/demo/file" class="down-btn">资源示例下载</a>
					          </td> 
					        </tr>
		        		</table>
		        		</form>	
		        	</li>
		        	
		        	<li id="tab1">
		        		<form name="form02" id="form02" action="<%=path %>/console/data/resource/importExcel.htm" enctype="multipart/form-data" method="post">
						 <input type="hidden" name="action" value="importResourceData" /> 
						 <input type="hidden" name="rminId" value="${requestScope.rminId }" />
						 <input type="hidden" name="resFileType" id="resFileType" value="1" /> 
		        		 <table cellpadding="0" width="100%" border="0" cellspacing="0" >
		        			<tr>
					          <td width="200px" align="right">导入设置：</td>
					          <td> 
					          	  <input type="checkbox" name="checkMust" value="1" id="c25" /><label for="c25">忽略必填字段</label> &nbsp;
					          	  <input type="checkbox" name="checkSubject" value="1" id="c26" /><label for="c26">将不存在的主题新增到作业</label> &nbsp;
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">自定义分类匹配类型：</td>
					          <td style="padding-left: 2px;">
					          	  <input type="radio" name="impGroupType" value="0" id="r25" checked="checked" /><label for="r25">按分类名称</label> &nbsp;
					          	  <input type="radio" name="impGroupType" value="1" id="r26" /><label for="r26">按分类号</label>
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">起始行：</td>
					          <td><input class="input-text" name="beginRow" ONKEYPRESS="event.returnValue=isNumbic();" value="2" /> <span class="tishi">从Excel文件中的第几行开始导入，默认为2！</span></td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">选择Excel文件：</td>
					          <td id="fileArea"> <input type="file" name="excelfile" id="xlsFile02" class="xlsfile" /> <span class="tishi">选择需要导入的Excel文件！</span></td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">资源目录：</td>
					          <td> 
					          	  <input class="input-text dir" name="imgRootDir" id="imgRootDir" onchange="urlpath(this);" />
					          	  <span class="tishi">可以是服务器上任务目录</span>
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">&nbsp;</td>
					          <td id="fileArea"> 
					          	<a href="javascript:imp02();" class="imp-btn">导 入</a>
					          	<a href="<%=path %>/res/demo/img" class="down-btn">资源示例下载</a>
					          </td>
					        </tr>
		        		 </table>
		        		</form>
		        	</li>
		        	<li id="tab2">
		        		<form name="form03" id="form03" action="<%=path %>/console/data/resource/importExcel.htm" enctype="multipart/form-data" method="post">
						 <input type="hidden" name="action" value="importResourceData" /> 
						 <input type="hidden" name="rminId" value="${requestScope.rminId }" />
						 <input type="hidden" name="resFileType" id="resFileType" value="2" /> 
		        		<table cellpadding="0" width="100%" border="0" cellspacing="0" >
		        			<tr>
					          <td width="200px" align="right">导入设置：</td>
					          <td> 
					          	  <input type="checkbox" name="checkMust" value="1" id="c7" /><label for="c7">忽略必填字段</label> &nbsp;
					          	  <input type="checkbox" name="checkSubject" value="1" id="c8" /><label for="c8">将不存在的主题新增到作业</label> &nbsp;
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">自定义分类匹配类型：</td>
					          <td style="padding-left: 2px;">
					          	  <input type="radio" name="impGroupType" value="0" id="r7" checked="checked" /><label for="r7">按分类名称</label> &nbsp;
					          	  <input type="radio" name="impGroupType" value="1" id="r8" /><label for="r8">按分类号</label>
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">起始行：</td>
					          <td> <input class="input-text" name="beginRow" ONKEYPRESS="event.returnValue=isNumbic();" value="2" /> <span class="tishi">从Excel文件中的第几行开始导入，默认为3！</span></td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">选择Excel文件：</td>
					          <td id="fileArea"> <input type="file" name="excelfile" id="xlsFile03" class="xlsfile" /> <span class="tishi">选择需要导入的Excel文件！</span></td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">html根路径：</td>
					          <td> 
					          	  <input class="input-text dir" name="fileRootUrl" id="fileRootUrl" />
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">html引导页文件名：</td>
					          <td> 
					          	  <input class="input-text" name="indexHtml" id="indexHtml" value="index.html" />
					          </td>
					        </tr>
					        <tr>
					          <td width="200px" align="right">&nbsp;</td>
					          <td headers="60px"> 
					          	<a href="javascript:imp03();" class="imp-btn">导 入</a>
					          	<a href="<%=path %>/res/demo/html" class="down-btn">资源示例下载</a>
					          </td>
					        </tr>
		        		</table>
		        		</form>
		        	</li>
		        </ul>
		    </div>
		 </div>  
		 
	  </form>	
	</div> 
	<div class="jg-ts">
	 ${requestScope.result }
	</div>
	  
	  
  </body>
</html>
