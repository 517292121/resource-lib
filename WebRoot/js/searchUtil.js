//lucene全文检索示例项目相关js处理函数

//检索框得到焦点
function inInput(){
	var key = document.getElementById("key");
	if(key.value == '输入文章标题、源文内容进行检索'){
		key.value = "";
	}
	key.className = "nrco";
}

//检索框失败焦点
function outInput(){
	var key = document.getElementById("key");
	if(key.value == ''){
		key.value = "输入文章标题、源文内容进行检索";
		key.className = "tsco";
	}else{
		key.className = "nrco";
	}
}

//验证检索词
function valkey(){
	var k = document.getElementById("key").value;
	if(k == '输入文章标题、源文内容进行检索' || k.length < 1){
		alert("请输入检索词！");
		return false;
	}else{
		return true;
	}
}