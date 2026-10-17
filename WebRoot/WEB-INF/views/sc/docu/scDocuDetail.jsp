<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<% 
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>添加回复</title>
    
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<link rel="stylesheet" href="<%=path %>/css/platform/docu.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/jquery-1.4.2.js"></script>
    <script charset="utf-8" src="<%=path %>/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="<%=path %>/kindeditor/lang/zh_CN.js"></script>
    <script type="text/javascript" src="<%=path %>/viewsjs/console/web/resource.js"></script>
    <script type="text/javascript">
    	function checkForm(){
	     	if(document.getElementById("sddeReplyContent").value.length<1){
    			Msg.fail("回复内容不能为空！",4);
    			return false;
    		}
    		if(document.all.winfIsmain1.checked){
               document.all.winfIsmain.value = true;
          	}else{
               document.all.winfIsmain.value = false;
           	}
	     }
    	KindEditor.ready(function(K) {
        	 window.editor = K.create('#AlbumRemark');
    	 });
    </script>
  </head>
  
  <body>
    <div class="title">
    	<div style="float: left;padding-top: 3px;">
	  		<img src="<%=path %>/images/extform/02.png"></img> 回复信息
	  		&nbsp;&nbsp;&nbsp;
	    	<a href="<%=path %>/console/data/docudelivery.htm?action=initDocuView">
	  			<img src="<%=path %>/images/common/common_back.png"/> 返回
  			</a>
	  	</div>
  	</div>
  	
  	<div class="page_content">
  		<form action="<%=path %>/console/data/docudelivery.htm?action=saveReply" method="post" id="menuForm" enctype="multipart/form-data">
		<input name="sddeId" id="sddeId" type="hidden" value="${sdde.sddeId }" />
		<div class="docuDiv">
  		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listTableInfo">
  			<tr class="thead">
            	 <td>&nbsp;</td>
                 <td>文献信息</td>
            </tr> 
  			<tr id="color">
  				<td align="right" class="td150">文献类型：</td>
  				<td align="left" class="td500">
  					${sdde.sddeDocuType }
  				</td>
  			</tr>
  			<tr>
  				<td align="right" class="td150">文献标题：</td>
  				<td align="left" class="td500">
  					${sdde.sddeDocuTitle }
  				</td>
  			</tr>
  			<tr id="color">
  				<td align="right" class="td150">作者：</td>
  				<td align="left" class="td500">
  					${sdde.sddeDocuCreator }
  				</td>
  			</tr>
  			<tr >
  				<td align="right" class="td150">出版者：</td>
  				<td align="left" class="td500">
  					${sdde.sddeDocuPublisher }
  				</td>
  			</tr>
  			<tr id="color">
  				<td align="right" class="td150">关键词：</td>
  				<td align="left" class="td500">
  					${sdde.sddeDocuKeyword }
  				</td>
  			</tr>
  			<tr >
  				<td align="right" class="td150">时间选择：</td>
  				<td align="left" class="td500">
  					${sdde.sddeDocuDate }
  				</td>
  			</tr>
  			<tr id="color">
  				<td align="right" class="td150">页码选择：</td>
  				<td align="left" class="td500">
  					${sdde.sddeDocuPage }
  				</td>
  			</tr>
  			<tr align="left" class="td500">
  				<td align="right" class="td150">ISSN/ISBN：</td>
  				<td>
  					${sdde.sddeDocuIsbn }
  				</td>
  			</tr>
  			<tr id="color">
  				<td align="right" class="td150">需求说明：</td>
  				<td align="left" class="td500">
  					${sdde.sddeContent }
  				</td>
  		</table>
  		</div>
  		<div class="docuDiv">
  		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listTableInfo" >
  			<tr class="thead">
          	 	<td>&nbsp;</td>
               <td>用户信息</td>
             </tr> 
			<tr id="color" >
  				<td align="right" class="td150">姓名：</td>
  				<td align="left" class="td500">
					${sdde.sysUserinfo.suinName }  					
  				</td>
  			</tr>
  			<tr >
  				<td align="right" class="td150">邮箱：</td>
  				<td align="left" class="td500">
					${sdde.sysUserinfo.suinEmail }  					
  				</td>
  			</tr>
  			<tr id="color" >
  				<td align="right" class="td150">电话：</td>
  				<td align="left" class="td500">
					${sdde.sysUserinfo.suinMobileTelephone }  					
  				</td>
  			</tr>
  			<tr >
  				<td align="right" class="td150">申请时间：</td>
  				<td align="left" class="td500">
  					 ${sdde.sddeCreationTime }
  				</td>
  			</tr>  		
 		</table>
 		</div>
 		
 		<div class="docuDiv">
  		<table width="100%" border="0" cellpadding="0" cellspacing="0" class="listTableInfo" >
  			<tr class="thead">
          	 	<td>&nbsp;</td>
               <td>回复信息</td>
             </tr> 
  			<tr id="color">
  				<td align="right" class="td150">回复内容：</td>
  				<td align="left" class="td500">
  					<c:if test="${empty sdde.sddeReplyContent }">
  						<input name="sddeReplyContent" id="sddeReplyContent" class="inputText"/>
  					</c:if>
  					<c:if test="${!empty sdde.sddeReplyContent }">
  						${sdde.sddeReplyContent }
  					</c:if>
  				</td>
  			</tr>
  			<tr >
  				<td align="right" class="td150">原文链接：</td>
  				<td align="left" class="td500">
  					<c:if test="${sdde.sddeStatus ==1}">
  						<input type="file" name="sddeDocuUrl" id="sddeDocuUrl" onchange="fileChang('sddeDocuUrl',true);" class="inputFile"  />
  						<span style="color: #808080">&nbsp;&nbsp;<span style="color: #808080">文档格式:word、pdf</span>
  					</c:if>
  					<c:if test="${sdde.sddeStatus !=1}">
  						${sdde.sddeDocuUrl }
 					</c:if>
  				</td> 
  			</tr> 
  			<tr id="color">
  				<td align="right" class="td150">回复馆员：</td>
  				<td align="left" class="td500">
  					${sdde.sysManager.smanName }
  				</td>
  			</tr>
  			<tr >
  				<td align="right" class="td150">回复状态：</td>
  				<td align="left" class="td500">
  					<c:if test="${sdde.sddeStatus ==1}">
  					未回复
  					</c:if>
  					<c:if test="${sdde.sddeStatus !=1}">
  					已回复
  					</c:if>
  				</td>
  			</tr>
  			<c:if test="${!empty sdde.sddeReplyTime }">
  			<tr id="color">
  				<td align="right" class="td150">回复时间：</td>
  				<td align="left" class="td500">${sdde.sddeReplyTime }</td>
  			</tr>
  			</c:if>
  	
  			<tr >
  				<td align="right" class="td150">&nbsp; </td>
  				<td>
  					<c:if test="${sdde.sddeStatus ==1}">
  						<input type="submit" value=" 保 存 " class="buttonM" onclick="return checkForm();" />
  					</c:if>
  				</td>
  			</tr>
  		</table>
  		</div>
  		</form>
  	
  	</div>
  </body>
</html>
