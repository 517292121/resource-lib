<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.com.adtech.riss2.bso.resource.ISubjectTypeService"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	
	ISubjectTypeService subjectService = (ISubjectTypeService)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("subjectTypeService");
	JSONRPCBridge.registerObject("subjectService", subjectService);
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
			var rstyName = document.getElementById("rstyName").value;
			if(rstyName.length < 1){
				Msg.fail("分类名称不能为空！");
				return;
			}
			var rstyPid = document.getElementById("rstyPid").value;
			var rstyIndex = document.getElementById("rstyIndex").value;
			if(rstyIndex.length < 1){
				rstyIndex = 0;
			}
			var rstyCode = document.getElementById("rstyCode").value;
			if(rstyCode.length < 1){
				Msg.fail("分类号不能为空！");
				return;
			}
			codeCover = checkCodeCover(0);
			if(codeCover == true){
				Msg.fail("该分类号已存在，请重新录入！");
				return;
			}
			if(rstyPid.length == ''){
				rstyPid = 0;
			}
			try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.subjectService.saveSubject(0, rstyPid, rstyName, rstyIndex, rstyCode);
		        if(result){
		            Msg.success("保存成功！");
		            RefTree();
		            window.location.href = "<%=path %>/console/data/resSubjectType.htm?action=groupInfo";
		        }else{
		            Msg.fail("对不起，保存失败！");
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e);
	        }
		}
		
		//修改分类
		function updateGroup(){
			var rstyId = document.getElementById("rstyId").value;
			var rstyName = document.getElementById("rstyName").value;
			var rstyPid = document.getElementById("rstyPid").value;
			var rstyIndex = document.getElementById("rstyIndex").value;
			if(rstyIndex.length < 1){
				rstyIndex = 0;
			}
			
			var rstyCode = document.getElementById("rstyCode").value;
			if(rstyId.length < 1){
				Msg.alert("请点击选择左边分类树中待修改的分类！");
				return;
			}
			if(rstyName.length < 1){
				Msg.fail("类名称不能为空！");
				return;
			}
			if(rstyCode.length < 1){
				Msg.fail("分类号不能为空！");
				return;
			}
			var rstyId = document.getElementById("rstyId").value;
			if(rstyId.length < 1){
				rstyId = 0;
			}
			codeCover = checkCodeCover(rstyId);
			if(codeCover == true){
				Msg.fail("该分类号已存在，请重新录入！");
				return;
			}
			if(rstyId == rstyPid){
				Msg.fail("错误！上级分类不能为分类本身。");
				return;
			}
			if(rstyPid.length == ''){
				rstyPid = 0;
			}
			try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.subjectService.saveSubject(rstyId, rstyPid, rstyName, rstyIndex, rstyCode);
		        if(result){
		            Msg.success("修改成功！");
		            RefTree();
		            window.location.href = "<%=path %>/console/data/resSubjectType.htm?action=groupInfo";
		        }else{
		            Msg.fail("对不起，修改失败！");
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e);
	        }
		}
		
		//删除分类
		function deleteGroup(){
			var rstyId = document.getElementById("rstyId").value;
            if(rstyId.length < 1){
                Msg.fail("请选择左边分类树中待删除的分类!");
                return;
            }
            if(!window.confirm("您确定要删除该分类吗")){
                return;
            }
           
            try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.subjectService.deleteSubject(rstyId);
		        if(result.length > 1){
		            Msg.fail("<span style='color:red'>" + result + "</span>",5);
		        }else{
		        	window.location.href = "<%=path %>/console/data/resSubjectType.htm?action=groupInfo";
		            RefTree();
		        }
	        }catch(e){
	           Msg.fail("获取JSONRpcClient对象时发生异常:" + e.message);
	           //TODO:如果创建JSONRpcClient发生异常.则跳转到后台程序处理登录
	        }
		}
		
		var codeCover = false;
		//验证分类号是否重复
		function checkCodeCover(rstyId){
			
			var c = document.getElementById("rstyCode").value;
			if(c.length < 1){
				return false;
			}
			try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.subjectService.checkSubjectCode(c, rstyId);
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
	           return false;
	        }
		}
		
		function checkCode(){
			var rstyId = document.getElementById("rstyId").value;
			if(rstyId.length < 1){
				rstyId = 0;
			}
			checkCodeCover(rstyId);
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
				<input type="hidden" name="rstyId" id="rstyId" value="${subject.rstyId }" >
			    <table width="100%" cellpadding="0" cellspacing="0">
			    	<tr style="background-color: #F7F9FC;height: 32px;">
			    		<td width="120px;" align="right">上级分类：</td>
			    		<td>
			    			<select name="rstyPid" id="rstyPid">
			    			${options }
			    			</select>
			    		</td>
			    	</tr>
			    	
			    	<tr height="32px;" >
			    		<td width="120px;" align="right"><span style="color: red">*</span>分类名称：</td>
			    		<td><input value="${subject.rstyName }" name="rstyName" id="rstyName" class="groupText" /></td>
			    	</tr>
			    	
			    	<tr height="32px;" style="background-color: #F7F9FC;height: 32px;" >
			    		<td width="120px;" align="right"><span style="color: red">*</span>分类号：</td>
			    		<td><input value="${subject.rstyCode }" name="rstyCode" id="rstyCode" maxlength="64" class="groupText" onblur="checkCode();" /><a id="codeMsg"><span style="color: #c0c0c0">分类号为唯一不能重复！</span></a></td>
			    	</tr>
			    	
			    	<tr>
			    		<td width="120px;" align="right">排序号：</td>
			    		<td>
			    			<input value="${subject.rstyIndex }" name="rstyIndex" id="rstyIndex" ONKEYPRESS="event.returnValue=IsDigit();" class="groupText" />
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
