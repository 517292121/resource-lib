
function onInput(obj){
	obj.style.borderColor = "000000";
	var key = obj.value;
	if(key == "请输入题名关键词"){
		obj.value = "";
	}
}

function onOutput(obj){
	obj.style.borderColor = "BDC7C9";
	var key = obj.value;
	if(key.length < 1){
		obj.value = "请输入题名关键词";
	}
}

function checkSearch(){
	var key = document.getElementById("keyWord").value;
	if(key.length < 1 || key == "请输入题名关键词"){
		alert("请输入检索关键词");
		return;
	}else{
		document.getElementById("searchForm").submit();
	}
}