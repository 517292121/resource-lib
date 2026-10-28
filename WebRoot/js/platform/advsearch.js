
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};
//高级检索

//检索条件。需要新增或删除直接修改以下配置即可
var condtion='';
condtion += '<option value="rsdaTitle">'+title_name+'(title)</option>';
condtion += '<option value="rsdaCreator">'+title_input_responsible_person+'(creator)</option>';
condtion += '<option value="rsdaPublisher">'+title_input_name+'(publisher)</option>';
condtion += '<option value="rsdaSubject">'+title_theme+'(subject)</option>';
condtion += '<option value="rsdaKeyword">'+title_input_Keyword+'(keyword)</option>';
condtion += '<option value="rsdaDescription">'+title_describe+'(description)</option>';
$(function(){
	$("#COL2").get(0).selectedIndex=1;
	$("#COL3").get(0).selectedIndex=2;
	$("#COL4").get(0).selectedIndex=4;
	// Keep the library/type selected on the page that opened this dialog.
	var context = new URLSearchParams(window.location.search);
	for (var row = 1; row <= 4; row++) {
		['KEY', 'COL', 'LIKE', 'CON'].forEach(function (prefix) {
			var name = prefix + row;
			var control = document.querySelector('[name="' + name + '"]');
			if (control && context.has(name)) control.value = context.get(name);
		});
	}
	['libId', 'typeId'].forEach(function (name) {
		var values = context.getAll(name);
		if (name === 'libId' && document.body.dataset.libraryFilter) values.push(document.body.dataset.libraryFilter);
		document.querySelectorAll('input[name="' + name + '"]').forEach(function (input) {
			if (values.indexOf(input.value) >= 0) input.checked = true;
		});
	});
})

function validform(){
	var k1 = $("#KEY1").val();
	var k2 = $("#KEY2").val();
	var k3 = $("#KEY3").val();
	var k4 = $("#KEY4").val();
	
	if(isNull(k1) && isNull(k2) && isNull(k3) && isNull(k4)){
		layer.msg(uiText("请至少输入一个检索条件"));
		return false;
	}
	if(!AntiScriptValid(k1)){
		return false;
	}
	if(!AntiScriptValid(k2)){
		return false;
	}
	if(!AntiScriptValid(k3)){
		return false;
	}
	if(!AntiScriptValid(k4)){
		return false;
	}
	return true;
}
//判断参数是否为空
function isNull(val){
	if(val == null || val == 'null' || val.length < 1){
		return true; 
	}else{
		return false;
	}
}
//防止script和SQL注入
function AntiScriptValid(text) {
    re = /select |update |insert |delete |exec |count |;|>|<|%/i;
    var temp = true;
    if (re.test(text)) {
        temp = false;
        layer.msg(uiText("请您不要在参数中输入特殊字符和SQL关键字！")); //注意中文乱码
    }
    return temp;
}	
