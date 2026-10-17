<%@ page language="java" pageEncoding="UTF-8"%>

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


     }
	</script>
  </head>

  <body>
  			<form id='form1' name='form1' action="<%=path %>/console/links/linkList.htm?action=saveOrUpdateLinkInfo" method="post" enctype="multipart/form-data">
  			<input type="hidden" value="${sysLink.slinId }" name="slinId" id="slinId"/>
		 			<div class="title" style="margin-top: 0;padding-left: 5px;">链接信息</div>
  						<table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >

						       <tr bgcolor="#FFFFFF" height="25">
							      <td width="250px" align="right">
							          <span style="color: red;">*</span><strong>名称：</strong>
							      </td>
							      <td class="td_p">
				                       <input type="text" name="slinName" value="${sysLink.slinName }" class="text" style="width: 300px;"></input>
			                       </td>
							   </tr>

							    <tr bgcolor="#FFFFFF" height="25">
							      <td width="250px" align="right">
							          <strong>链接地址：</strong>
							      </td>
							      <td class="td_p">
				                       <input type="text" name="slinLinkpath" value="${sysLink.slinLinkpath }" class="text" style="width: 300px;"></input>
			                       </td>
							   </tr>
							     <tr bgcolor="#FFFFFF" height="25" >
					 	 	   		<td width="250px" align="right"><strong>logo图片：</strong></td>
					 	 	   		<td>
						                  <div id="testAA"  style="display: none;">
							                  <input type="hidden" id="loadfile_statu" name="loadfile_statu" value="0">
							                  <c:if test="${! empty  sysLink.slinImgpath}">
							                  <script type="text/javascript">
         											document.getElementById("loadfile_statu").value = "2";
							            		</script>
							                  </c:if>
		  	 	    							<input type="file" name="slinImgpath" class="file text" id="slinImgpath">
					 	 	   			  </div>
							            <c:choose>
							            	<c:when test="${! empty  sysLink.slinImgpath}">
							                  <div id="loadfileArea">
								 	 	   				<a href="<%=path %>/console/links/linkList.htm?action=companyImgShow&url=${sysLink.slinImgpath }" target="_blank">预览</a>
									               	 	<a href="javascript:deleLoadFile();">删除</a>
									          </div>
							                </c:when>
							            	<c:otherwise>
							            		<script type="text/javascript">
										           document.getElementById("testAA").style.display="block";
							            		</script>
						 	 	   			</c:otherwise>
						 	 	   		</c:choose>
					 	 	   		</td>
					 	 	   </tr>
							    <tr bgcolor="#FFFFFF" height="25">
							      <td width="250px" align="right">
							           <strong>链接类型：</strong>
							      </td>
							      <td class="td_p">
							      		<select name="slinType">
							      			<option value="0" <c:if test="${sysLink.slinType==0 }">selected="selected"</c:if> >图片</option>
							      			<option value="1" <c:if test="${sysLink.slinType==1 }">selected="selected"</c:if>>文字</option>
							      		</select>
			                       </td>
							   </tr>

						       <tr bgcolor="#FFFFFF" height="25">
							      <td width="250px" align="right">
							          <strong>排序号：</strong>
							      </td>
							      <td class="td_p">
				                       <input type="text" name="slinIndex" value="${slinIndex}" class="text" style="width: 300px;"></input>
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

										<li><input type="button" value="返&nbsp;&nbsp;回" onclick="javascript:window.location='<%=path %>/console/links/linkList.htm?action=initPage'"  style="padding-top: 3px;"></li>
					                  </ul>
					               </div>
				 	 	   		</td>
				 	 	   </tr>
					    </table>
		</form>
		${result }
  </body>
</html>
