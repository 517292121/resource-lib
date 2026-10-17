<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.bso.resource.IResDynamicTypeService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	
	IResDynamicTypeService dynamicService = (IResDynamicTypeService)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("dynamicService");
	JSONRPCBridge.registerObject("dynamicService", dynamicService);
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
	
		//保存分类
		function saveGroup(){
			var rmstId = document.getElementById("rmstId").value;
			var rdtyName = document.getElementById("rdtyName").value;
			var rdtyZwName = document.getElementById("rdtyZwName").value;
			var rdtyEnName = document.getElementById("rdtyEnName").value;
			if(rdtyName.length < 1){
				Msg.fail("分类名称不能为空！");
				return;
			}
			if(rmstId.length < 1){
				Msg.fail("错误！未知所属特色库。");
				return;
			}
			var rdtyPid = document.getElementById("rdtyPid").value;
			var rdtyIndex = document.getElementById("rdtyIndex").value;
			if(rdtyIndex.length < 1){
				rdtyIndex = 0;
			}
			var rdtyCode = document.getElementById("rdtyCode").value;
			if(rdtyCode.length < 1){
				Msg.fail("分类号不能为空！");
				return;
			}
			codeCover = checkCodeCover(0);
			if(codeCover == true){
				Msg.fail("该分类号已存在，请重新录入！");
				return;
			}
			if(rdtyPid.length == ''){
				rdtyPid = 0;
			}
			try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.dynamicService.saveDynamicType(0, rmstId, rdtyPid, rdtyName, rdtyZwName, rdtyEnName, 0, rdtyIndex, ${rdtyType }, rdtyCode);
		        if(result){
		            Msg.success("保存成功！");
		            RefTree();
		            window.location.href = "<%=path %>/console/data/resource/defineStore.htm?action=groupInfo&rmstId=" + rmstId + "&rdtyType=" + ${rdtyType };
		        }else{
		            Msg.fail("对不起，保存失败！");
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e);
	        }
		}
		
		//修改分类
		function updateGroup(){
			var rdtyId = document.getElementById("rdtyId").value;
			var rmstId = document.getElementById("rmstId").value;
			var rdtyName = document.getElementById("rdtyName").value;
			var rdtyZwName = document.getElementById("rdtyZwName").value;
			var rdtyEnName = document.getElementById("rdtyEnName").value;
			var rdtyPid = document.getElementById("rdtyPid").value;
			var rdtyIndex = document.getElementById("rdtyIndex").value;
			if(rdtyIndex.length < 1){
				rdtyIndex = 0;
			}
			
			var rdtyCode = document.getElementById("rdtyCode").value;
			if(rdtyId.length < 1){
				Msg.alert("请点击选择左边分类树中待修改的分类！");
				return;
			}
			if(rdtyName.length < 1){
				Msg.fail("类名称不能为空！");
				return;
			}
			if(rdtyCode.length < 1){
				Msg.fail("分类号不能为空！");
				return;
			}
			var rdtyId = document.getElementById("rdtyId").value;
			if(rdtyId.length < 1){
				rdtyId = 0;
			}
			codeCover = checkCodeCover(rdtyId);
			if(codeCover == true){
				Msg.fail("该分类号已存在，请重新录入！");
				return;
			}
			if(rmstId.length < 1){
				Msg.fail("错误！未知所属特色库。");
				return;
			}
			if(rdtyId == rdtyPid){
				Msg.fail("错误！上级分类不能为分类本身。");
				return;
			}
			if(rdtyPid.length == ''){
				rdtyPid = 0;
			}
			try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.dynamicService.saveDynamicType(rdtyId, rmstId, rdtyPid, rdtyName, rdtyZwName, rdtyEnName, 0, rdtyIndex, ${rdtyType }, rdtyCode);
		        if(result){
		            Msg.success("修改成功！");
		            RefTree();
		            window.location.href = "<%=path %>/console/data/resource/defineStore.htm?action=groupInfo&rmstId=" + rmstId + "&rdtyType=" + ${rdtyType };
		        }else{
		            Msg.fail("对不起，修改失败！");
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e);
	        }
		}
		
		//删除分类
		function deleteGroup(){
			var rdtyId = document.getElementById("rdtyId").value;
			var rmstId = document.getElementById("rmstId").value;
            if(rdtyId.length < 1){
                Msg.fail("请选择左边分类树中待删除的分类!");
                return;
            }
            if(!window.confirm("您确定要删除该分类吗")){
                return;
            }
           
            try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.dynamicService.deleteDynamicType(rdtyId);
		        if(result.length > 1){
		            Msg.fail("<span style='color:red'>" + result + "</span>",5);
		        }else{
		        	window.location.href = "<%=path %>/console/data/resource/defineStore.htm?action=groupInfo&rmstId=" + rmstId + "&rdtyType=" + ${rdtyType };
		            RefTree();
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e.message);
	           //TODO:如果创建JSONRpcClient发生异常.则跳转到后台程序处理登录
	        }
		}
		
		var codeCover = false;
		//验证分类号是否重复
		function checkCodeCover(rdtyId){
			
			var c = document.getElementById("rdtyCode").value;
			if(c.length < 1){
				return false;
			}
			try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.dynamicService.checkGroupCode(${rmstId }, ${rdtyType }, c, rdtyId);
		        if(result==false){
		        	//没有重复
		        	document.getElementById("codeMsg").innerHTML = "<img src='<%=path %>/images/icon/yes.png' class='icon'>";
		        	return false;
		        }else{
		        	//有重复
		            document.getElementById("codeMsg").innerHTML = "<img src='<%=path %>/images/icon/error.png' class='icon'><span style='color:red'>该分类号已存在！</span>";
		            return true;
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e.message);
	           return true;
	        }
		}
		
		function checkCode(){
			var rdtyId = document.getElementById("rdtyId").value;
			if(rdtyId.length < 1){
				rdtyId = 0;
			}
			checkCodeCover(rdtyId);
		}
		
		function goBack(){
			window.parent.window.location.href = "<%=path %>/console/data/resource/defineStore.htm?action=initDefineStoreView";
		}
		
		function IsDigit() {
		   return ((event.keyCode >= 48) && (event.keyCode <= 57));
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
  			<h2>错误提示</h2><hr>
  			<span style="color: red">${error }</span>
  		</c:when>
  		
  		<c:otherwise>
				<input type="hidden" name="rmstId" id="rmstId" value="${rmstId }" >
				<input type="hidden" name="rdtyId" id="rdtyId" value="${dynamic.rdtyId }" >
			    <table width="100%" cellpadding="0" cellspacing="0">
			    	<tr style="background-color: #F7F9FC;height: 32px;">
			    		<td width="120px;" align="right">上级分类：</td>
			    		<td>
			    			<select name="rdtyPid" id="rdtyPid">
			    			${options }
			    			</select>
			    		</td>
			    	</tr>
			    	
			    	<tr height="32px;" >
			    		<td width="120px;" align="right"><span style="color: red">*</span>中文-名称：</td>
			    		<td><input value="${dynamic.rdtyName }" name="rdtyName" id="rdtyName" class="groupText" /></td>
			    	</tr>
			    	
			    	<tr height="32px;" >
			    		<td width="120px;" align="right">藏文-名称：</td>
			    		<td><input value="${dynamic.rdtyZwName }" name="rdtyZwName" id="rdtyZwName" class="groupText" /></td> 
			    	</tr>
			    	 
			    	<tr height="32px;" >
			    		<td width="120px;" align="right">英文-名称：</td>
			    		<td><input value="${dynamic.rdtyEnName }" name="rdtyEnName" id="rdtyEnName" class="groupText" /></td>
			    	</tr>
			    	
			    	<tr height="32px;" style="background-color: #F7F9FC;height: 32px;" >
			    		<td width="120px;" align="right"><span style="color: red">*</span>分类号：</td>
			    		<td><input value="${dynamic.rdtyCode }" name="rdtyCode" id="rdtyCode" maxlength="64" class="groupText" onblur="checkCode();" /><a id="codeMsg"><span style="color: #c0c0c0">分类号为唯一不能重复！</span></a></td>
			    	</tr>
			    	
			    	<tr>
			    		<td width="120px;" align="right">排序号：</td>
			    		<td>
			    			<input value="${dynamic.rdtyIndex }" name="rdtyIndex" id="rdtyIndex" ONKEYPRESS="event.returnValue=IsDigit();" class="groupText" />
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
