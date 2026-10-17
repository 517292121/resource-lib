<%@ page contentType="text/html; charset=utf-8" language="java" errorPage="" %>
<%
   String path = request.getContextPath();
   String type = request.getParameter("type");
 %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
 
    
    <title>My JSP 'uploadfile.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

    <link href="editor_dialog.css" rel="stylesheet" type="text/css">
    
    <script type="text/javascript">
    /**检查上传格式**/
	function checkImageFileType(fileItem){ 
			  var filetag = new Array();  //允许的文件格式
		      filetag[0] = "jpg";
		      filetag[1] = "jpeg";
		      filetag[2] = "bmp";
		      filetag[3] = "png";
		      filetag[4] = "gif";
		    var result = false;  
			var filepath=fileItem.value;
			if(filepath)filepath=filepath.substring(filepath.lastIndexOf('.')+1,filepath.length).toLowerCase();
		    for(i=0; i<filetag.length; i++){
		         if(filetag[i].toLowerCase() == filepath){
		             result = true;
		             break;
		         }
		    }
		    if(result == false){
				document.getElementById("imageFileArea").innerHTML = "<input type='file' name='file' onchange='checkImageFileType(this);'>";
				alert("图片格式只能为jpg、jpg、gif、bmp、png.请重新选择！");
		    }
		}
        function checkSubmit(){
            if(document.all.file.value.length<3){
                document.all.msg.innerHTML="请选择图片";
                return false;
            }
        }
        function clearmsg(){
            alert("dfa");
            document.all.msg.innerHTML = "";
        }
        
    </script>
  </head>
  
  <body  bgcolor=D4D0C8 topmargin=0 leftmargin=0> 
  <form action="<%=path %>/console/data/idsInformation.htm?action=uploadAction&type=<%=type %>" method="post" enctype="multipart/form-data" name="form1" onsubmit="return checkSubmit();">
      <%
      	if(type !="" && type == "pic"){
      		%><span id="imageFileArea"><input type="file" id="file" name="file" onchange="checkImageFileType(this)" ></span><%
      	}else{
      		%><span id="imageFileArea"><input type="file" id="file" name="file" ></span><%
      	}
       %>
      
      <input type="submit" name="Submit" value="上传" onchange="clearmsg();">
      
  </form>
  </body>
</html>
