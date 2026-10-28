/***************************************************************************************
 	*	 
 	* 资源录入和编辑的相关验证和操作处理
 	*
 	* @see (input_single.jsp、resourceEdit.jsp、interpretForm.jsp、resourceEdit.jsp)
 	* 
 *
 * 开发公司: 重庆亚德科技研发中心
 * 
 * 版权:本文件版权归属重庆亚德科技研发中心
 * 
 	* @author  王林
 	* 
 	* @since 该文件所支持的浏览器：IE、火狐
 	*
 	* @date  (开发日期): 2010-09-29
 	* 
 	* @version 1.2
 	* 
 *
 * 最后更新日期: 2010-11-12
 *
 * 更改人: 王林
 * 
 * 更改内容:验证馆藏位置判断、加入资源输入框高度为自动增长
 *   
 * 复审人:   
 * 
 * 复审时间： 
 * 
 * 复审结果:
 *
 **************************************************************************************/ 

// 中图法检验结果
var ztfcheckResult = true;

// 保存
function doSave(reqType) {
	if(document.getElementById("reqType") != null){
		document.getElementById("reqType").value = reqType;
	}
	try{
		document.getElementById("req").value = reqType;
	}catch(e){
		if(document.getElementsByName("req") != null){
			document.getElementsByName("req").value = reqType;
		}
	}
	document.getElementById("sourceform").target = "_self";
	var checkResult = checkform();
	if(checkResult == true) {
		$(".p-btn").click();
//		document.getElementById("sourceform").submit();
//		loading();
		//showWaitMessage(getPath() + "/");
	}
}

 function backshow2(searchtitle,pagecount,rmin_id){
   document.getElementById("searchtitle").value=searchtitle;
   document.getElementById("pagecount").value=pagecount;
   document.getElementById("rmin_id").value=rmin_id;
   document.getElementById("form5").submit();
 }
 
function checkAction(rsdaStatu, reqType){
	document.getElementById("rsdaStatus").value = rsdaStatu;
	document.getElementById("type").type.value = reqType;
	document.getElementById("sourceform").target = "_self";
	var checkResult = checkform();
	if(checkResult == true) {
		checkUpdate();
		document.getElementById("sourceform").submit();
		showWaitMessage(getPath() + "/");
	}
}

function sourceView(reqItem){
	document.getElementById("req").value = "3";
	document.getElementById(reqItem).value = "3";
    var checkResult = checkform();
	if(checkResult == true) {
	    document.getElementById("sourceform").target = "_blank";
		document.getElementById("sourceform").submit();
	}
}

// 表单验证
function checkform() {
	if (window.validateResourceDirectoryUpload && !window.validateResourceDirectoryUpload()) return false;
	var texts = document.getElementsByTagName("textarea");
	var result = true;
	for(i = 0; i < texts.length; i++) {
		if(texts[i].sourceElement == "true") {
			if(texts[i].restrict == "M" && texts[i].value.length < 1) {
				document.getElementById(texts[i].name + "_msg").innerHTML = "<img src='" + getPath()
						+ "/images/icon/error.png' class='icon'><span style='color:red'>" + texts[i].title + " 不能为空！</span>"
				window.location.href = "#" + texts[i].name + "_msg";
				result = false;
			}else if(texts[i].value.length > 1600) {
				document.getElementById(texts[i].name + "_msg").innerHTML = "<img src='" + getPath()
						+ "/images/icon/error.png' class='icon'><span style='color:red'>元素内容不能超过1600！您已录入：" + texts[i].value.length + "</span>"
				window.location.href = "#" + texts[i].name + "_msg";
				result = false;
			}else {
				document.getElementById(texts[i].name + "_msg").innerHTML = "<img src='" + getPath() + "/images/icon/yes.png' class='icon'>"
			}
		}
	}
	if(document.getElementById("location_msg")!= null) {
		document.getElementById("location_msg").innerHTML = "<img src='" + getPath() + "/images/icon/yes.png' class='icon'>";
	}
//	if(ztfcheckResult == false) {
//		document.getElementById("rmtyMessage").innerHTML = "<img src='" + getPath() + "/images/icon/error.png' class='icon'><span style='color:red'>该中图法编码不存在!</span>";
//		window.location.href = "#rmtyMessage";
//		result = false;
//	}
	return result;
}

// 如果存在，返回true 反之亦然
function checkTitle(title, selfId) {
	try {
		jsonrpc = new JSONRpcClient(getPath() + "/Json_Rpc");
		var result = jsonrpc.resourceRpc.checkResourceTitle(title, selfId);
		return result;
	}catch(e) {
		return 0;
	}
}

function onCheckTitle(item, selfid){
	  var title = item.value;
	  if(title == null || title.length < 1){
	  	  return;
	  }
	  var msgid = item.name + "_msg";
	  var id = checkTitle(title, selfid);
	  if(id>0) {
		  document.getElementById(msgid).innerHTML = "<img src='" + getPath() + "/images/icon/jingao.jpg' class='icon'><span style='color:#978300'>已存在该题名的资源!</span>";
//		  ztfcheckResult = false;
	  }else {
	  	  document.getElementById(msgid).innerHTML = "<img src='" + getPath() + "/images/icon/yes.png' class='icon'>";
//		  ztfcheckResult = true;
	  }
}

// 如果存在，返回true 反之亦然
function checkZtf(ztfcode) {
	try {
		jsonrpc = new JSONRpcClient(getPath() + "/Json_Rpc");
		var result = jsonrpc.resourceRpc.checkZTFcode(ztfcode);
		return result;
	}catch(e) {
		return true;
	}
}
/**验证中图法分类**/
function OnCheckZTF() {
//	var ztfCode = document.getElementById("rmty_id").value;
//	if(ztfCode.length < 1) {
//		ztfcheckResult = true;
//		return;
//	}
//	var checkResult = checkZtf(ztfCode);
//	if(checkResult == false) {
//		document.getElementById("rmtyMessage").innerHTML = "<img src='" + getPath() + "/images/icon/error.png' class='icon'><span style='color:red'>该中图法编码不存在!</span>";
//		ztfcheckResult = false;
//	}else {
//		document.getElementById("rmtyMessage").innerHTML = "<img src='" + getPath() + "/images/icon/yes.png' class='icon'>";
//		ztfcheckResult = true;
//	}
}

/** 其它语录入表单提交* */
function InterpretSave(reqs) {
	if(checkInterpretForm() == true) {
		document.getElementById("reqType").value = reqs;
		document.getElementById("sourceform").submit();
		showWaitMessage(getPath() + "/");
	}
}

/** 其它语录入表单提交验证* */
function checkInterpretForm() {
	var texts = document.getElementsByTagName("textarea");
	var result = true;
	for(i = 0; i < texts.length; i++) {
		if(texts[i].sourceElement == "true") {
			if(texts[i].restrict == "M" && texts[i].value.length < 1) {
				document.getElementById(texts[i].name + "_msg").innerHTML = "<img src='" + getPath()
						+ "/images/icon/error.png'><span style='color:red'>" + texts[i].title + " 不能为空！</span>"
				window.location.href = "#" + texts[i].name + "_msg";
				result = false;
			}else if(texts[i].value.length > 1600) {
				document.getElementById(texts[i].name + "_msg").innerHTML = "<img src='" + getPath()
						+ "/images/icon/error.png'><span style='color:red'>元素内容不能超过1600,您已录入：" + texts[i].value.length + "！</span>"
				window.location.href = "#" + texts[i].name + "_msg";
				result = false;
			}else {
				document.getElementById(texts[i].name + "_msg").innerHTML = "<img src='" + getPath() + "/images/icon/yes.png'>"
			}
		}
	}
	if(document.getElementById("location_msg") != null) {
		document.getElementById("location_msg").innerHTML = "<img src='" + getPath() + "/images/icon/yes.png'>"
	}
	return result;
}

// 设置输入框的高度为自动增长
function setTextAreaHeight(textAreaObj, offsetPx) {
	if(textAreaObj.value.length > 1600) {
		document.getElementById(textAreaObj.name + "_msg").innerHTML = "<img src='" + getPath() + "/images/icon/error.png'><span style='color:red'>元素内容不能超过1600！您已录入：" + textAreaObj.value.length + "</span>"
	}else {
		document.getElementById(textAreaObj.name + "_msg").innerHTML = "";
	}
	
	var hei = 28;
	if(textAreaObj.scrollHeight > hei){
		hei = textAreaObj.scrollHeight;
	}
	if(navigator.userAgent.indexOf("MSIE") > 0) {  //IE浏览器
		try{
			textAreaObj.style.height = hei + "px";
		}catch(e){
		}
	}else { //火狐浏览器
		textAreaObj.style.height = hei + "px";
	}
	
}

/**初始化资源录入和修改的输入框高度**/
function initTextAreaHeight(offsetPx) {
	if(offsetPx == null){
		offsetPx = 4;
	}
	var texts = document.getElementsByTagName("textarea");
	for(i = 0; i < texts.length; i++) {
		if(texts[i].sourceElement == "true") {
			setTextAreaHeight(texts[i], offsetPx);
		}
	}
}

/**初始化其它语录入和修改的输入框高度**/
function initInterpretTextArea() {
	var texts = document.getElementsByTagName("textarea");
	for(i = 0; i < texts.length; i++) {
		if(texts[i].sourceElement == "true") {
			texts[i].style.height = document.getElementById('data_' + texts[i].name).scrollHeight;
		}
	}
}

/**检查上传封面文件有文件格式**/
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
		document.getElementById("imageFileArea").innerHTML = "<input type='file' name='bigimg' class='file text' onchange='checkImageFileType(this);'>&nbsp;<span style='color: #808080'>格式:jpg、jpeg、bmp、png、gif</span>";
		alert("封面格式只能为JPG、JPEG、GIF、BMP、PNG。请重新选择！");
    }
}

	function delefile(hideArea, showFile, icon){
         if(window.confirm("您确定要删除该文件并重新上传吗？") == false){
             return;
         }
         document.getElementById(hideArea).innerHTML = "";
         document.getElementById(hideArea).style.display = "none";
         document.getElementById(showFile).style.display = "block";
         document.getElementById(showFile+"_statu").value = "1";
         
         if(icon != null){
            document.getElementById(icon).style.display = "block";
         }
     }
     
     function fileChang(filestatu, isCheck){
         document.getElementById(filestatu+"_statu").value = "1";
         if(isCheck){
             checkImageFileType(document.getElementById("bigimg"));
         }
     }
     
     function deleLoadFile(){
         if(window.confirm("您确定要删除该文件并重新上传吗？") == false){
             return;
         }
         document.getElementById("loadfileArea").innerHTML = "";
         document.getElementById("loadfileArea").style.display = "none";
         document.getElementById("loadfile_statu").value = "1";
         document.getElementById("testAA").style.display="block";
     }
     
	//只能输入数字
	//用法 <input name="old" type="text" ONKEYPRESS="event.returnValue=IsDigit();">
	function IsDigit() {
	   return ((event.keyCode >= 48) && (event.keyCode <= 57));
	}

	//新增一行文件
	 function addRow(){
          var tableObj = document.getElementById("file_table");
          var rowNum = tableObj.rows.length;
          if(rowNum < 2){
          	return;
          }
          var newRow = tableObj.insertRow(rowNum);
          newRow.height = "25px";
          
          var fileNum = newRow.insertCell(0); //添加第二列，文件显示序号
          fileNum.align = "center";
          fileNum.innerHTML = "<input style='width: 33px;height: 20px;' name='annexNum"+rowNum+"' class='text' value='"+rowNum+"' ONKEYPRESS=\"event.returnValue=IsDigit();\">";
          
          var fileName = newRow.insertCell(1); //添加第三列，文件选择对象
          fileName.innerHTML = "<input type='file' name='annexFile"+rowNum+"' class='file text'>";
          
          var fileRemark = newRow.insertCell(2);  //添加第四行，文件说明
          fileRemark.innerHTML = "<input style=\"width: 100%;height: 21px;\" name='annexRemark"+rowNum+"' class=\"text\">";
          
          setCountNumber(); //设置文件总数
          //SetRowNum(); //设置文件序号
       }
       
       //删除最后一个文件
       function delRow(){
          var tableObj = document.getElementById("file_table");
          var rowNum = tableObj.rows.length;
          if(rowNum < 3){
          	  alert("最后一行不能删除！");
          	  return;
          }
          tableObj.deleteRow(rowNum-1);
          
          setCountNumber(); //设置文件总数
          //SetRowNum(); //设置文件序号
       }
       
       //文件自动排序
       function SetRowNum(){
          var tableObj = document.getElementById("file_table");
          var rows = tableObj.rows.length ;
          for(var i=1; i<rows; i++ ){
              tableObj.rows[i].cells[0].children[0].value = i;
          }
       }
       
       //设置文件总数
       function setCountNumber(){
       	  var files = document.getElementById("file_table").rows.length;
       	  document.getElementById("fileNumber").value = files - 1;
       }


 //文件上传tab切换处理
 $(function(){
	$('.tabbox .content > ul').width(800*$('.tabbox .content > ul > li').length+'px');
	$(".tabbox .tab a").click(function(){
		$("#resFileType").val($(this).data("type"));
		if (window.syncDirectoryUpload) window.syncDirectoryUpload();
		$(this).addClass('on').siblings().removeClass('on');
		var index = $(this).index();
		var distance = -800*index;
		$('.tabbox .content ul').stop().animate({
			left:distance
		});
	});
	
	var t = parseInt($("#resFileType").val());
	if(t > 0){
		var distance = -800*t;
		$('.tabbox .content ul').stop().animate({
			left:distance
		});
	}
 });

