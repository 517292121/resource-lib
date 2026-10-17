<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Iterator"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
%>

<html>
  <head>
    <title>敦煌研究数据库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/extbutton.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/jquery-1.4.2.js"></script>
    <script charset="utf-8" src="<%=path %>/kindeditor/kindeditor.js"></script>
	<script charset="utf-8" src="<%=path %>/kindeditor/lang/zh_CN.js"></script>
	<style>
		.form_textarea{
			height: 80px;
			width: 350px;
		}
	</style>
	<script type="text/javascript">
	function deleLoadFile(){
         if(window.confirm("您确定要删除该文件并重新上传吗？") == false){
             return;
         }
         document.getElementById("loadfileArea").innerHTML = "";
         document.getElementById("loadfileArea").style.display = "none";
         document.getElementById("loadfile_statu").value = "1";
         document.getElementById("testAA").style.display="block";
     }

     function checkForm(){

     	if (editor.EditMode.checked == true) {
			document.all.content.value = editor.HtmlEdit.document.body.innerText;
		} else {
			document.all.content.value = editor.HtmlEdit.document.body.innerHTML;
		}
		if (document.all.content.value.length < 2) {
			alert("文章内容不能为空!");
			return false;
		}

     }

     function delimg(){
     	if(window.confirm("您确定要删除封面吗？")){
     		document.getElementById("coverTd").innerHTML = "<input type='file' name='infoCover' id='infoCover' class='file' onchange='changImg();'>";
     		changImg();
     	}
     }
     function changImg(){
     	document.getElementById("imgChang").value = "1";
     }
     KindEditor.ready(function(K) {
         window.editor = K.create('#content');
     });
	</script>
  </head>

  <body>
  			<form id='form1' name='form1' action="<%=path %>/console/data/idsInformation.htm?action=saveOrUpdateIdsInfomatin" method="post" enctype="multipart/form-data">
  				<input type="hidden" name="imgChang" id="imgChang" value="0">
  				<input type="hidden" value="${idsinfo.idsiId }" name="idsiId" id="idsiId"/>
		 			<div class="title" style="margin-top: 0;padding-left: 5px;">信息发布</div>
  						<table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >

						       <tr bgcolor="#FFFFFF" height="25">
							      <td width="250px" align="right">
							          <span style="color: red;">*</span><strong>信息标题：</strong>
							      </td>
							      <td class="td_p">
				                       <input type="text" name="idsiTitle" value="${idsinfo.idsiTitle }" class="text" style="width: 300px;"></input>
			                       </td>
							   </tr>
							    <tr bgcolor="#FFFFFF" height="25">
							      <td width="250px" align="right">
							          <strong>信息类别：</strong>
							      </td>
							      <td class="td_p">
							      		<select name="idsiIdstId" class="text" >
							      			${idsType }
							      		</select>
			                       </td>
							   </tr>

							  <tr bgcolor="#FFFFFF" height="25">
							      <td width="250px" align="right"><b>封面：</b></td>
							      <td class="td_p" id="coverTd">
							      	 <c:if test="${!empty idsinfo.idsiImgUrl }">
							      	 <img src="${idsinfo.idsiImgUrl }" width="100px" height="100px">
							      	 <a href="javascript:delimg();">删除</a>
							      	 </c:if>
							      	 <c:if test="${empty idsinfo.idsiImgUrl }">
							      	 <input type="file" name="infoCover" id="infoCover" class="file" onchange="changImg();">
							      	 </c:if>
			                      </td>
							   </tr>

							   <tr bgcolor="#FFFFFF" height="25">
							      <td width="250px" align="right">
							          <strong>信息内容：</strong>
							      </td>
							      <td class="td_p">
							           <textarea id="content" name="content" style="width:700px;height:300px;">${idsinfo.idsiContent }</textarea>
			                       </td>
							   </tr>
		 				</table>
		 				<table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
				 	 	   <tr bgcolor="#FFFFFF" height="25">
				 	 	   		<td width="250px">&nbsp;</td>
				 	 	   		<td style="padding-top: 5px;">
				 	 	   			<div class="tool1" style="padding-top: 6px;">
				                      <ul class="nav">
					                    <li><input type="submit" value="保&nbsp;&nbsp;存" onclick="return checkForm();"  style="padding-top: 3px;"></li>

										<li><input type="button" value="返&nbsp;&nbsp;回" onclick="javascript:window.location='<%=path %>/console/data/idsInformation.htm?action=initIdsView${backurl}'" style="padding-top: 3px;"></li>
					                  </ul>
					               </div>
				 	 	   		</td>
				 	 	   </tr>
					    </table>
		</form>
		${result }
  </body>
</html>

