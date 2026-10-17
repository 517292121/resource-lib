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
		.page_content{
		    padding-top:30px;
		}
		.form_table{
			width: 100%;
		}
		.form_table tr{
			height: 30px;
		}
		.form_table #color{
			background-color: #F7F9FC;
		}
		.form_table #left{
			width: 180px;
			text-align: right;
		}
		.inputText {
			height: 22px;
			border: 1px solid #BDC7C9;
			background-image: url("../images/input_bg.jpg");
			background-repeat: repeat-x;
			margin: 1px;
			width: 250px;
		}
		.inputFile {
			height: 24px;
			border: 1px solid #BDC7C9;
			background-image: url("../images/input_bg.jpg");
			background-repeat: repeat-x;
			margin: 1px;
			width: 252px;
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
     	var name = document.getElementById("ibName").value;
        if(name ==""){
        	alert("名称不能为空！");
        	return false;
        }
        return true;
     }
	</script>
  </head>

  <body>
  <div class="title">
    	<div style="float: left;padding-top: 3px;">
	  		<img src="<%=path %>/images/extform/02.png"></img> 新增Banner
	  	</div>
  	</div>

  	<div class="page_content">
  		<form id='form1' name='form1' action="<%=path %>/console/links/bannerList.htm?action=saveOrUpdateLinkInfo" method="post" enctype="multipart/form-data">
  		<input type="hidden" value="${ib.ibId }" name="ibId" id="ibId"/>
  		<table class="form_table">
  			<tr id="color">
  				<td width="250px" id="left"><span style="color: red;">*</span>Banner名称：</td>
  				<td>
  					<input type="text" name="ibName" value="${ib.ibName }" class="text" style="width: 300px;line-height: 20px"></input>
  				</td>
  			</tr>
  			<tr id="white">
  				<td id="left"><span style="color: red;">*</span>Banner图片：</td>
  				<td id="imageFileArea">
  					<div id="testAA"  style="display: none;">
		                  <input type="hidden" id="loadfile_statu" name="loadfile_statu" value="0">
		                  <c:if test="${! empty  ib.ibImgUrl}">
		                  <script type="text/javascript">
    											document.getElementById("loadfile_statu").value = "2";
		            		</script>
		                  </c:if>
 	    				<input type="file" name="ibImgUrl" class="file text" id="ibImgUrl">
 	    				<span style="color:#666;">&nbsp;建议尺寸：980*410</span>
 	 	   			  </div>
		            <c:choose>
		            	<c:when test="${! empty  ib.ibImgUrl }">
		                  <div id="loadfileArea">
		 	 	   				<a href="<%=path %>/console/links/bannerList.htm?action=companyImgShow&url=${ib.ibImgUrl }" target="_blank">预览</a>
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
  				<td>
  				</td>
  			</tr>
  			<tr id="color">
  				<td width="250px" id="left">链接地址：</td>
  				<td>
  					<input type="text" name="ibUrl" value="${ib.ibUrl  }" class="text" style="width: 300px;line-height: 20px"></input>
  				</td>
  			</tr>
  			<tr id="white">
  				<td id="left">背景颜色：</td>
  				<td>
  					<input type="text" name="ibBgColor" value="${ib.ibBgColor}" class="text" style="width: 300px;line-height: 20px"></input>
  					<span style="color:#666;">&nbsp;格式：#FFFFFF 代表白色</span>
  				</td>
  			</tr>
  			<tr id="color">
  				<td id="left">显示序号：</td>
  				<td>
  					<input type="text" name="ibIndex" value="${ibIndex}" class="text" style="width: 300px;line-height: 20px" ></input>
  				</td>
  			</tr>

  			<tr id="white">
  				<td id="left">是否显示：</td>
  				<td>
  					<input name="ibIsShow" type="radio" value="1" <c:if test="${empty ib.ibIsShow || ib.ibIsShow == 1}">checked="checked"</c:if>/>是 &nbsp;
  					<input name="ibIsShow" type="radio" value="0" <c:if test="${ib.ibIsShow == 0}">checked="checked"</c:if>/>不
  				</td>
  			</tr>

  			<tr id="white">
  				<td id="left">内容：</td>
  				<td>
  					<textarea id="ibDescription" name="ibDescription" style="width:700px;height:100px;line-height: 20px">${ib.ibDescription }</textarea>
  				</td>
  			</tr>
  			<tr id="white">
  				<td id="left">&nbsp; </td>
  				<td>
  					<input type="submit" value=" 保 存 " class="buttonM" onclick="return checkForm();" />
  					<input type="button" value=" 返 回 " class="buttonM" onclick="window.location.href='<%=path %>/console/links/bannerList.htm?action=initPage'" />
  				</td>
  			</tr>
  		</table>
  		</form>

  	</div>
		${result }
  </body>
</html>
