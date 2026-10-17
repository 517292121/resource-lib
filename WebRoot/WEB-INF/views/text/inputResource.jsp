<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>资源单条录入</title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    
	<link rel="stylesheet" href="<%=path %>/css/common.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
  	<style type="text/css">
  		table td{height: 36px;}
  		.htmlText{width: 480px; height: 28px;padding-left: 3px;}
  		.text{padding-left: 3px;width: 480px;height: 120px;}
  		.rfile{width: 480px; height: 28px; margin-left: 1px;}
  	</style>
  	<script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/BasicUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/tableUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/jquery-1.4.2.js"></script>
    <script charset="utf-8" src="<%=path %>/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="<%=path %>/kindeditor/lang/zh_CN.js"></script>
    <script type="text/javascript">
    	function onSubmit(){
    		Msg.wait("数据提交中...");
    		return true;
    		//document.getElementById("configForm").submit();
    	}
    	function goBack(){
    		if('${source}' == 'resList'){
    			window.location.href = "<%=path %>/console/data/text/textResource.htm?action=resourceList&rtliId=${rtliId}";
    		}else if ('${source}' == 'resLib'){
	    		window.location.href = "<%=path %>/console/data/text/textResource.htm";
    		}else if ('${source}' == 'manager'){
	    		window.location.href = "<%=path %>/console/data/text/textResourceManager.htm";
    		}else{
    			window.location.href = "<%=path %>/console/data/text/textResource.htm?action=resourceList&rtliId=${rtliId}";
    		}
    	}
    	
    	function coverDel(){
    		if(window.confirm("您确定要删除封面吗？")){
    			document.getElementById("coverTd").innerHTML = "<input type='file' name='rtreCover' class='htmlFile rfile' onchange=\"filechange('coverChange');\">";
    			filechange("coverChange");
    		}
    	}
    	
    	function filechange(fid){
    		document.getElementById(fid).value = "1";
    	}
    	
    	KindEditor.ready(function(K) {
    		<c:if test="${!empty rtli.rtliCol13Label }">window.editor = K.create('#kind13');</c:if>
        	<c:if test="${!empty rtli.rtliCol14Label }">window.editor = K.create('#kind14');</c:if>
        	<c:if test="${!empty rtli.rtliCol15Label }">window.editor = K.create('#kind15');</c:if>
    	});
    	
    	$(document).ready(function(){
		  $(".htmlText").keydown(function(e) {  
	           if (e.keyCode == 13) {  
	           		return false;
	           }  
	      });  
	      
		});
		
		//检测非法字符
		function onFocusOff(e){
			var text = $(e).val();
			if(text == "null"){
				Msg.fail("非法字符："+text);
				$(e).val("");
    			return false;
			}
		}
    </script>
  </head>
  
  <body style="padding-bottom: 100px;" onload="tableBg()">
  	<div class="tool extbtn" style="margin-top: 0">
  		<div style="float: left;padding-top: 3px;">
  			<img src="<%=path %>/images/extform/02.png"></img> 资源编辑
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
			<form action="<%=path %>/console/data/text/textResource.htm" id="configForm" method="post" enctype="multipart/form-data">	
				<input type="hidden" name="action" value="saveResource" />
				<input type="hidden" name="rtreId" value="${resource.rtreId }" />	
				<input type="hidden" name="rtliId" value="${rtliId }" />
				<input type="hidden" name="source" value="${source }" />
						
				<input type="hidden" name="coverChange" id="coverChange" value="0" />
			    <table width="100%" cellpadding="0" cellspacing="0" id="Page_data">
			    	<tr>
			    		<td width="180px;" align="right">所属资源库：</td>
			    		<td style="color: #007EB8">${rtli.rtliName }</td>
			    	</tr>
			    	
			    	<c:if test="${!empty rtli.rtliTitle}">
				    	<tr>
				    		<td width="180px;" align="right"><span style="color: red">*</span>${rtli.rtliTitle }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreTitle }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreTitle" id="rtreTitle" maxlength="170" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol1Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol1Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol1 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol1" id="rtreCol1" maxlength="85" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
					<c:if test="${!empty rtli.rtliCol2Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol2Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol2 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol2" id="rtreCol2" maxlength="85" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
					<c:if test="${!empty rtli.rtliCol3Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol3Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol3 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol3" id="rtreCol3" maxlength="85" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol4Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol4Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol4 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol4" id="rtreCol4" maxlength="85" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol5Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol5Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol5 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol5" id="rtreCol5" maxlength="85" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol6Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol6Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol6 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol6" id="rtreCol6" maxlength="170" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol7Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol7Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol7 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol7" id="rtreCol7" maxlength="170" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol8Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol8Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol8 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol8" id="rtreCol8" maxlength="170" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol9Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol9Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol9 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol9" id="rtreCol9" maxlength="170" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol10Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol10Label }：</td>
				    		<td>
				    			<input value="<c:out value='${resource.rtreCol10 }'></c:out>" onblur="javascript:onFocusOff(this);" name="rtreCol10" id="rtreCol10" maxlength="170" class="htmlText" />
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol11Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol11Label }：</td>
				    		<td>
				    			<textarea rows="8" maxlength="340" onchange="this.value=this.value.substring(0, 340" name="rtreCol11" id="rtreCol11" class="text">${resource.rtreCol11 }</textarea>
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol12Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol12Label }：</td>
				    		<td>
				    			<textarea rows="8" maxlength="340" onchange="this.value=this.value.substring(0, 340" name="rtreCol12" id="rtreCol12" class="text">${resource.rtreCol12 }</textarea>
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol13Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol13Label }：</td>
				    		<td>
				    			<textarea id="kind13" name="rtreCol13" style="width:700px;height:300px;">${resource.rtreCol13 }</textarea>
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol14Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol14Label }：</td>
				    		<td>
				    			<textarea id="kind14" name="rtreCol14" style="width:700px;height:300px;">${resource.rtreCol14 }</textarea>
				    		</td>
				    	</tr>
			    	</c:if>
			    	<c:if test="${!empty rtli.rtliCol15Label}">
				    	<tr>
				    		<td width="180px;" align="right">${rtli.rtliCol15Label }：</td>
				    		<td>
				    			<textarea id="kind15" name="rtreCol15" style="width:700px;height:300px;">${resource.rtreCol15 }</textarea>
				    		</td>
				    	</tr>
			    	</c:if>
			    	
			    	<c:if test="${!empty libClassfiyOptions}">
			    	<tr>
			    		<td width="180px;" align="right"><span style="color: red">*</span>${rtli.rtliName }分类：</td>
			    		<td>
			    			<select name="rtreRlclId" class="htmlSelect">
			    				<%-- 
			    				<option value="">请选择...</option>
			    				--%>
			    				${libClassfiyOptions }
			    			</select>
			    		</td>
			    	</tr>
			    	</c:if>
			    	
			    	<tr>
			    		<td width="180px;" align="right">封面：</td>
			    		<td id="coverTd">
			    			<c:choose>
			    				<c:when test="${!empty resource.rtreCover}">
			    					<img src="<%=SysConfigInfo.FILE_URL_PATH %>${resource.rtreCover }" width="120px;">
			    					<a href="javascript:coverDel();">删除</a>
			    				</c:when>
			    				<c:otherwise>
			    					<input type="file" name="rtreCover" class="htmlFile rfile" onchange="filechange('coverChange');">
			    				</c:otherwise>
			    			</c:choose>
			    		</td>
			    	</tr>
			    	
			    	<tr height="38px;">
			    		<td>&nbsp;</td>
			    		<td style="padding-top: 10px;padding-left: 1px;">
			    			<input type="submit" value=" 保 存 " onclick="onSubmit()" class="saveBtn" />
			    			<input type="button" value=" 返 回 " onclick="goBack()" class="saveBtn" />
			    		</td>
			    	</tr>
			    </table>
		    </form>
  		</c:otherwise>
  	</c:choose>
  	</div>
  	
  	${result }
  </body>
</html>