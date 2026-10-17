<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>自定义分类导入</title>
    
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
	<script type="text/javascript">var root = "<%=path %>/";</script>
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
	
		function onAction(){
			if(!checkXls("xlsFile01")){
				return;
			}
			
			loading();
			$("#form01").submit();
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
	</script>
  </head>
  
  <body>
  	<div class="pagenav">
     	首页：文献库设置 > ${store.rmstName } > ${groupname } > 批量导入
    </div>
	
	<div class="tool">
		<a href="<%=path %>/console/data/resource/defineStore.htm?action=initDefineGroupView&rdtyType=${rdtyType }&rmstId=${rmstId }">&lt;返回列表</a>
	</div>
	
	<div class="form">
		<div class="tabbox">
			<div class="content">
		    	<ul>
		    		<!-- 文件格式 -->
		        	<li id="tab0">
		        		<form name="form01" id="form01" action="<%=path %>/console/data/dynamic.htm" enctype="multipart/form-data" method="post">
						 <input type="hidden" name="action" value="importGroup" /> 
						 <input type="hidden" name="rmstId" value="${rmstId }" />
		 				 <input type="hidden" name="rdtyType" value="${rdtyType }" />
						 <input type="hidden" name="resFileType" id="resFileType" value="0" /> 
		        		 <table cellpadding="0" width="100%" border="0" cellspacing="0">
					        <tr>
					          <td width="200px" align="right">导入设置：</td>
					          <td> 
						          <input type="radio" name="coverType" value="1" id="c1" checked="checked" /><label for="c1">覆盖同级同分类名同分类号</label>
					          	  <input type="radio" name="coverType" value="2" id="c2" /><label for="c2">覆盖同级同分类名</label>
					          	  <input type="radio" name="coverType" value="3" id="c3" /><label for="c3">覆盖同级同分类号</label>
					          	  <input type="radio" name="coverType" value="4" id="c4" /><label for="c4">不覆盖重复</label>
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
					          	<a href="javascript:onAction();" class="imp-btn">导 入</a>
					          	<a href="<%=path %>/res/demo/dynamic" class="down-btn">导入模板下载</a>
					          </td>
					        </tr>
		        		</table>
		        		</form>	
		        	</li>
		        </ul>
	       </div>
		</div>
	</div>
	<div class="jg-ts">
	 ${requestScope.result }
	</div>
	  
  </body>
</html>