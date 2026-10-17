<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>分类新增</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<%=path %>/plugin/jqueryFrom/jquery.form.js"></script>
	<script type="text/javascript" src="<%=path %>/js/platform/common.js"></script>
	<style type="text/css">
		html, body{height: auto;}
		table td{padding: 6px 3px;}
		label, input, select {vertical-align: middle;}
		.text{min-width: 220px; height: 26px; border: 1px solid #ccc; outline:none; padding: 2px 5px;}
		#dataOptions{transition:all 0.3s; }
	</style>
	
	<script type="text/javascript">
		$(function(){
			$('#myform').ajaxForm({
				dataType: 'JSON',
				success:function(data) {
					if(!data.success){
						alert(data.msg, {icon: 2});
						return false;
					}
					//删除当前弹窗层
					closeWin();
					//调用父级刷新提示层
					parent.reloadTbale("保存成功");
				},
				error: function(){
					alert("网络错误，请检查网络链接!");
				}
			});
		})
		
		function checkform(){
			var name = $.trim($("#name").val());
			if(isEmpty(name)){
				layer.tips("分类名不能为空!",$("#name"));
				return false;
			}
		}
		
		//关闭当前弹出层
		function closeWin(){
			var index = parent.layer.getFrameIndex(window.name); 
			parent.layer.close(index);//关闭当前页
		}
	</script> 
  </head>
  
  <body>
    <form action="<%=path %>/console/data/idsInformationType.htm" method="post" id="myform">
	    <input type="hidden" name="action" value="save"/>
	    <input type="hidden" name="id" value="${t.idstId }"/>
	    <table width="100%" style="margin-top: 5px;">
		    <tr>
		        <td align="right" width="140px"><font style="color:#ff0080">*</font>分类语种：</td>
		        <td>
	  				<input type="radio" name="lan" id="lan_zh" value="zh" <c:if test="${empty t.idstLanguage || 'zh' eq t.idstLanguage}">checked</c:if>><label for="lan_zh">中文</label> &nbsp;					
	  				<input type="radio" name="lan" id="lan_zw" value="zw" <c:if test="${'zw' eq t.idstLanguage}">checked</c:if>><label for="lan_zw">藏文</label> &nbsp;
	  				<input type="radio" name="lan" id="lan_mw" value="mw" <c:if test="${'mw' eq t.idstLanguage}">checked</c:if>><label for="lan_mw">蒙文</label> &nbsp;
	  				<input type="radio" name="lan" id="lan_en" value="en" <c:if test="${'en' eq t.idstLanguage}">checked</c:if>><label for="lan_en">英文</label>
	  			</td>
		    </tr>
	       <tr>
		        <td align="right" width="140px"><font style="color:#ff0080">*</font>分类名：</td>
		        <td id="parent_id" colspan="2" height="35px;"> 
	               <input name="name" id="name" class="text" value="${t.idstName }" placeholder="请填写分类名">
	            </td>
		    </tr>
		    <tr>
		        <td align="right" width="140px">序号：</td>
		        <td id="parent_id" colspan="2" height="35px;"> 
	               <input name="index" class="text" value="${t.idstIndex }" placeholder="序号" oninput="value=value.replace(/[^\d]/g,'')">
	            </td>
		    </tr>
		    <tr height="38">
		    	<td>&nbsp;</td>
	             <td class="td_submit">
	               <input type="submit" value=" 提 交 " class="button" style="width: 100px; height:30px;" /> 
	             </td>
          	</tr>
	    </table>
    </form>
  </body>
</html>
<%request.removeAttribute("hhf");%>