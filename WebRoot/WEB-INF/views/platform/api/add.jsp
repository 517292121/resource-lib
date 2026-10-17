<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <meta name="referrer" content="origin" />
    <meta http-equiv="Cache-Control" content="no-transform" />
    <meta http-equiv="Cache-Control" content="no-siteapp" />
    <title>创建数据接口</title>
	<link rel="stylesheet" href="<%=basePath %>css/apidoc.css" type="text/css"></link>
	
	<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
	
	
	
	<!-- HTML编辑器引入 -->
  	<script charset="utf-8" src="<%=basePath %>kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="<%=basePath %>kindeditor/lang/zh_CN.js"></script>
	<script>
		//该变量不可少，文本编辑器需要使用
		var basePath = "<%=basePath %>";
		
		var paramsEdit = null;
		var resultEdit = null;
		var remarkEdit = null;
		$(function(){
			KindEditor.ready(function(K) {
				paramsEdit = K.create('#sadaReqParams');
				resultEdit = K.create('#sadaResult');
				remarkEdit = K.create('#sadaRemark');
			});
		});
		
		function IsDigit() {
		   return ((event.keyCode >= 48) && (event.keyCode <= 57));
		}
		function checkform(){
			if($("#sadaName").val().length < 1){
				alert("请录入接口名称！");
				return false;
			}
			if($("#sadaReqUrl").val().length < 1){
				alert("请录入接口请求地址！");
				return false;
			}
			return true;
		}
	</script>
	
	
</head>
<body>
	<div class="webhead">
		<div id="logoname">
			创建数据接口
		</div>
	</div>
	<div class="mainframe">
		<div class="toolbar">
			<a href="<%=basePath %>dataapi/list">返回列表</a>
			<p class="clearfix" />
		</div>
		
		<div class="toolbar">
			说明：“请求地址”与“接口示例”可以采用“<\%=basePath\%>”字符串代表项目根路径，路径最后带“/”。注意中间不能有空格
		</div>
		
		<div class="editform">
		<form action="<%=basePath %>dataapi/save" method="post" onsubmit="return checkform()">
		<input type="hidden" name="sadaId" value="${apidata.sadaId }" />
		<table>
			<tr>
				<td>接口类型：</td>
				<td>
					<input type="radio" name="sadaType" id="type1" value="通用接口" <c:if test="${empty apidata or apidata.sadaType=='通用接口' }">checked="checked"</c:if>><label for="type1">产品通用接口</label> &nbsp;
					<input type="radio" name="sadaType" id="type2" value="定制接口" <c:if test="${apidata.sadaType=='定制接口' }">checked="checked"</c:if>><label for="type2">项目定制接口</label> &nbsp;
					<input type="radio" name="sadaType" id="type3" value="其它接口" <c:if test="${apidata.sadaType=='其它接口' }">checked="checked"</c:if>><label for="type3">其它接口</label> &nbsp;
				</td>
			</tr>	
			<tr>
				<td>接口名称：</td>
				<td>
					<input type="text" class="input-txt w400" name="sadaName" id="sadaName" value="${apidata.sadaName }" maxlength="128" placeholder="如：获取信息评论记录" />
				</td>
			</tr>	
			<tr>
				<td>请求地址：</td>
				<td>
					<input type="text" class="input-txt w600" name="sadaReqUrl" id="sadaReqUrl" value="${apidata.sadaReqUrl }" maxlength="512" placeholder="如：<\%=basePath\%>jsondata/content/comments/{infoId}" />
				</td>
			</tr>	
			<tr>
				<td>接口示例：</td>
				<td>
					<input type="text" class="input-txt w600" name="sadaDemoUrl" id="sadaDemoUrl" value="${apidata.sadaDemoUrl }" maxlength="512" placeholder="如：<\%=basePath\%>jsondata/content/comments/KMAKMwAEEAKJKJUK" />
				</td>
			</tr>	
			<tr>
				<td>请求方式：</td>
				<td>
					<input type="radio" name="sadaReqType" id="reqtype1" value="GET" <c:if test="${empty apidata or apidata.sadaReqType=='GET' }">checked="checked"</c:if>><label for="reqtype1">GET</label> &nbsp;
					<input type="radio" name="sadaReqType" id="reqtype2" value="POST" <c:if test="${apidata.sadaReqType=='POST' }">checked="checked"</c:if>><label for="reqtype2">POST</label> &nbsp;
					<input type="radio" name="sadaReqType" id="reqtype3" value="不限" <c:if test="${apidata.sadaReqType=='不限' }">checked="checked"</c:if>><label for="reqtype3">不限</label> &nbsp;
				</td>
			</tr>
			<tr>
				<td>请求参数：</td>
				<td>
					<textarea style="width:700px;height:300px;" name="sadaReqParams" id="sadaReqParams">${apidata.sadaReqParams }</textarea>
					<p class="tips">对接口请求参数作详细说明；</p>
				</td>
			</tr>	
			<tr>
				<td>请求结果：</td>
				<td>
					<textarea style="width:700px;height:300px;" name="sadaResult" id="sadaResult">${apidata.sadaResult }</textarea>
					<p class="tips">对接口请求返回结果返回值进行详细描述；</p>
				</td>
			</tr>	
			<tr>
				<td>研发备注：</td>
				<td>
					<textarea style="width:700px;height:300px;" name="sadaRemark" id="sadaRemark">${apidata.sadaRemark }</textarea>
					<p class="tips">研发时调用该接口需要注意的问题说明；</p>
				</td>
			</tr>	
			<tr>
				<td>更新说明：</td>
				<td>
					<textarea class="input-txt w600" style="height:80px;" name="sadaUpdateRemark" id="sadaUpdateRemark">${apidata.sadaUpdateRemark }</textarea>
					<p class="tips">该接口更新记录备注，如XX时做了XX更新。该字段不展示到接口文档中；</p>
				</td>
			</tr>	
			<tr>
				<td>排序号：</td>
				<td>
					<input type="text" class="input-txt w200" name="sadaIndex" id="sadaIndex" value="${apidata.sadaIndex }" maxlength="9" ONKEYPRESS="event.returnValue=IsDigit();" />
				</td>
			</tr>
			<tr>
				<td>&nbsp;</td>
				<td>
					<input type="submit" value=" 提 交  " class="btn">
					<input type="button" value=" 返回  " class="btn" onclick="window.location.href='<%=basePath %>dataapi/list'">
				</td>
			</tr>
		</table>
		</form>
		</div>
	</div>
	
	${message }
</body>
</html>