/**
 * Excel数据批量导入模板相关操作
 *
 */
 
 function onAction(){
   	var checkResult = checkform();
   	if(checkResult){
   		document.all.elementform.submit();
   	}
 }
 
 function checkform(){
 	var texts = document.getElementsByTagName("input");
	var result = true;
	var arr = new Array();
	var errorNum = 0;
	for(i = 0; i < texts.length; i++) {
		if(texts[i].isElement == "true") {
			if(texts[i].restrict == "M" && texts[i].value.length < 1) {
				document.getElementById(texts[i].name + "_msg").innerHTML = "<img src='" + rootPath + "/images/icon/error.png'><span style='color:red'>" + texts[i].title + " 未设置Excel对应列！</span>"
				arr[errorNum] = "#" + texts[i].name + "_msg";
				errorNum++;
				result = false;
			}else {
				document.getElementById(texts[i].name + "_msg").innerHTML = "<img src='" + rootPath + "/images/icon/yes.png'>"
			}
		}
	}
	if(arr.length > 0){
		window.location.href = arr[0];
	} 
	return result;
 }

 /**
  *只能输入字母
  *用法 <input name="old" type="text" ONKEYPRESS="event.returnValue=isChar();">
  */
 function isChar(item){
 	if(item.value.length >= 2){
 		return false;
 	}
 	var keycode = event.keyCode;
 	if((keycode >= 65 && keycode <=90) || (keycode >= 97 && keycode <=122)){
 		return true;
 	}else{
 		return false;
 	}
 }
 
  /**
  *只能输入数字
  *用法 <input name="old" type="text" ONKEYPRESS="event.returnValue=isNumbic());">
  */
 function isNumbic(){
 	var keycode = event.keyCode;
 	if((keycode >= 48 && keycode <=57)){
 		return true;
 	}else{
 		return false;
 	}
 }
 
