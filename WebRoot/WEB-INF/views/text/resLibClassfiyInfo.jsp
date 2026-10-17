<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.com.adtech.riss2.bso.text.ITextLibClassfiyService"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	
ITextLibClassfiyService textLibClassfiyService = (ITextLibClassfiyService)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("textLibClassfiyService");
	JSONRPCBridge.registerObject("textLibClassfiyService", textLibClassfiyService);
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0"> 
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
	<script type="text/javascript">var root = "<%=path %>/";</script>
	<script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
	<script type="text/javascript">
		function RefTree(){
           window.parent.groupTree.window.location.reload();
        }
		//跳转连接
		var tourl = "<%=path %>/console/data/text/textLibClassfiy.htm?action=groupInfo&rtliId=${rtliId}";
		
		//保存分类
		function saveGroup(){
			var rtliId = document.getElementById("rtliId").value;
			var rlclName = document.getElementById("rlclName").value;
			if(rlclName.length < 1){
				Msg.fail("分类名称不能为空！");
				return;
			}
			var rlclParentId = document.getElementById("rlclParentId").value;
			var rlclIndex = document.getElementById("rlclIndex").value;
			if(rlclIndex.length < 1){
				rlclIndex = 0;
			}
			if(rlclParentId.length == ''){
				rlclParentId = 0;
			}
			try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.textLibClassfiyService.saveLibClassfiy(rtliId,0, rlclParentId, rlclName, rlclIndex);
		        if(result){
		            Msg.success("保存成功！");
		            RefTree();
		            window.location.href = tourl;
		        }else{
		            Msg.fail("对不起，保存失败！");
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e);
	        }
		}
		
		//修改分类
		function updateGroup(){
			var rtliId = document.getElementById("rtliId").value;
			var rlclId = document.getElementById("rlclId").value;
			var rlclName = document.getElementById("rlclName").value;
			var rlclParentId = document.getElementById("rlclParentId").value;
			var rlclIndex = document.getElementById("rlclIndex").value;
			if(rlclIndex.length < 1){
				rlclIndex = 0;
			}
			
			if(rlclId.length < 1){
				Msg.alert("请点击选择左边分类树中待修改的分类！");
				return;
			}
			if(rlclName.length < 1){
				Msg.fail("类名称不能为空！");
				return;
			}
			var rlclId = document.getElementById("rlclId").value;
			if(rlclId.length < 1){
				rlclId = 0;
			}

			if(rlclId == rlclParentId){
				Msg.fail("错误！上级分类不能为分类本身。");
				return;
			}
			if(rlclParentId.length == ''){
				rlclParentId = 0;
			}
			try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.textLibClassfiyService.saveLibClassfiy(rtliId,rlclId, rlclParentId, rlclName, rlclIndex);
		        if(result){
		            Msg.success("修改成功！");
		            RefTree();
		            window.location.href = tourl;
		        }else{
		            Msg.fail("对不起，修改失败！");
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e);
	        }
		}
		
		//删除分类
		function deleteGroup(){
			var rlclId = document.getElementById("rlclId").value;
            if(rlclId.length < 1){
                Msg.fail("请选择左边分类树中待删除的分类!");
                return;
            }
            if(!window.confirm("您确定要删除该分类吗")){
                return;
            }
           
            try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.textLibClassfiyService.deleteLibClassfiy(rlclId);
		         if(!result){
		            Msg.fail("<span style='color:red'>删除失败，请检查该分类下是否存在资源！</span>",5);
		        }else{
		        	window.location.href = tourl;
		            RefTree();
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e.message);
	           //TODO:如果创建JSONRpcClient发生异常.则跳转到后台程序处理登录
	        }
		}
		
		function IsDigit() {
		   return ((event.keyCode >= 48) && (event.keyCode <= 57));
		}
		
		function goBack(){
			window.parent.window.location.href = "<%=path %>/console/data/text/textLibrary.htm";
		}
		
	</script>
  </head>
  
  <body style="border-left:2px solid #BFCAE6;overflow: hidden;">
  	<div class="tool" style="margin-top: 0">
  		<div style="float: left;padding-top: 3px;">
  			<img src="<%=path %>/images/extform/02.png"></img> 分类信息设置
  		</div>
  		<ul class="nav" style="float: right;">
		    <li><a href="javascript:goBack();"><img src="<%=path %>/images/common/common_back.png"></img>返回</a></li>
		  </ul>  
  	</div>
  	
  	<div>
  	<c:choose>
		<c:when test="${!empty error }">
			<div style="padding: 0px 10px 0px 20px;">
  			<h2>错误提示</h2><hr>
  			<span style="color: red">${error }</span>
  			</div>
  		</c:when>
  		
  		<c:otherwise>
  				<input type="hidden" name="rtliId" id="rtliId" value="${rtliId }" />
				<input type="hidden" name="rlclId" id="rlclId" value="${subject.rlclId }" >
			    <table width="100%" cellpadding="0" cellspacing="0">
			    	<tr style="background-color: #F7F9FC;height: 32px;">
			    		<td width="120px;" align="right">上级分类：</td>
			    		<td>
			    			<select name="rlclParentId" id="rlclParentId">
			    			${options }
			    			</select>
			    		</td>
			    	</tr>
			    	
			    	<tr height="32px;" >
			    		<td width="120px;" align="right"><span style="color: red">*</span>分类名称：</td>
			    		<td><input value="${subject.rlclName }" name="rlclName" id="rlclName" class="groupText" /></td>
			    	</tr>
			    	
			    	<tr>
			    		<td width="120px;" align="right">排序号：</td>
			    		<td>
			    			<input value="${subject.rlclIndex }" name="rlclIndex" id="rlclIndex" ONKEYPRESS="event.returnValue=IsDigit();" class="groupText" />
			    		</td>
			    	</tr>
			    	
			    	<tr height="38px;" style="background-color: #F7F9FC;height: 32px;">
			    		<td>&nbsp;</td>
			    		<td>
			    			<div class="tool1">
		                      <ul class="nav">
			                    <li><a href="javascript:saveGroup();" style="padding-top: 4px;">新 增</a></li>
			                    <li><a href="javascript:updateGroup();" style="padding-top: 4px;">修 改</a></li>
			                    <li><a href="javascript:deleteGroup();" style="padding-top: 4px;">删 除</a></li>
			                  </ul>  
			                </div>	
			    		</td>
			    	</tr>
			    </table>
  		</c:otherwise>
  	</c:choose>
  	</div>
  </body>
</html>
