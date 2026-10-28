
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};
$(function(){
	$('#search_but').click(function(){
		complateSearch();
	});  
	
	$("#searchkey").bind('keypress',function(event){  
        if(event.keyCode == "13") {  
			complateSearch();
        }  
    });
})

//一键检索
function complateSearch(){
	var k = $("#searchkey").val();
	if(k == null || k.length < 1){
		layer.msg(title_input_key);
		return;
	}
	
	if(AntiScriptValid(k)){
		var att = $("#attr").val();
		var match = $("#accurate").val();
		
		var searchUrl = "";
		if(att == "all"){
			searchUrl = basePath + "ls/" + encodeURI(k);
			if(match != '0'){
				searchUrl += "?accurate=" + match;
			}
		}else{
			//---------指定字段查询
			//数据库模式-属性支持所有核心元数据
			//searchUrl = basePath + "resAdvs?COL1="+att+"&KEY1=" + encodeURI(k);
			
			//lucene模式-属性支持ResMainBean中所定义的，默认包括：题名、创建者、出版者、关键词
			searchUrl =  basePath + "ls/" + encodeURI(k) + "?attr=" + att;
			
			if(match != '0'){
				searchUrl += "&accurate=" + match;
			}
		}
		window.location.href = searchUrl;
	}
}

//高级检索（弹出）
function advSearch(){
	layer.tab({
	  area: ['600px', '550px'],
	  shade: 0.8,
	  closeBtn: 1,
	  anim : 2,
	  shadeClose: false,
	  tab: [{
		  title: title_resource_retrieval,
		  content: getOs(basePath + "resAdv")
	  }]
	});
}
function getOs(url) {
	if(navigator.userAgent.indexOf("MSIE") > 0) {
		return '<iframe name="addStore" id="addStore" width="100%" height="500px" frameborder="no" src="' + url + '"></iframe>';
	}else {
		return '<iframe name="addStore" id="addStore" width="100%" height="500px" frameborder="no" src="' + url + '"></iframe>';
	}
}

//弹出登录
var loginWin = null;
function openLogin(){
	if(regWin != null){
		layer.close(regWin);
	}
	loginWin = layer.open({
	  type: 2,
	  title: false,
      skin: 'platform-member-login-layer',
      area: [Math.min(540, document.documentElement.clientWidth - 24) + 'px', Math.min(500, window.innerHeight - 32) + 'px'],
	  shade: 0.8,
	  closeBtn: 1,
	  anim : 1,
	  shadeClose: true,
	  content: basePath + "member/login"
	});
}
//退出登录
function exitLogin(){
	layer.confirm(poput_confirm_exit, {
		title:title_exit_information,
	    btn: [operation_key_submit,operation_key_cancel] //按钮
	}, function(){
	    ajaxExit();
	}, function(){
	});
}
function ajaxExit(){
	$.ajax({
	    type: 'GET',
	    url: basePath + 'member/exit?t=' + new Date().getTime(),
	    cache:false,
	    success: function(data){
	    	try{
		    	var val = eval("("+data+")");
	    		if(val.success == true || val.success == "true"){
	    			window.location.reload();
	    		}else{
	    			layer.alert(val.msg, {icon: 2});
	    		}
		   	}catch(e){
		   		layer.alert(poput_alert_abnormal+"：" + e.message, {icon: 2});
		   	}
	    }
	});
}

var regWin = null;
function openReg(){
	if(loginWin != null){
		layer.close(loginWin);
	}
	regWin = layer.open({
	  type: 2,
	  title: false,
	  skin: 'platform-member-login-layer',
	  area: [Math.min(540, document.documentElement.clientWidth - 24) + 'px', Math.min(720, window.innerHeight - 32) + 'px'],
	  shade: 0.8,
	  closeBtn: 1,
	  anim : 1,
	  shadeClose: true,
	  content: basePath + "register"
	});
}
window.addEventListener('message', function(event){
	if(event.origin !== window.location.origin || !event.data || event.data.type !== 'platform-member-resize' || regWin == null) return;
	var frame = document.querySelector('#layui-layer' + regWin + ' iframe');
	if(!frame || event.source !== frame.contentWindow) return;
	var maxHeight = Math.max(180, Math.min(window.innerHeight - 64, Math.floor(window.innerHeight * 0.92) - 34));
	var height = Math.min(Math.max(180, Number(event.data.height) || 0), maxHeight);
	layer.style(regWin, {height: height + 'px'});
});
//弹出服务协议 
function openRegServer(){
	layer.open({
	  type: 2,
	  title: false,
	  area: ['650px', '600px'],
	  shade: 0.8,
	  closeBtn: 1,
	  anim : 2,
	  shadeClose: true,
	  content: basePath + "reg/server"
	});
}


//判断参数是否为空
function isNull(val){
	if(val == null || val == 'null' || val.length < 1){
		return true; 
	}else{
		return false;
	}
}

//将null转换为指定字符
function nullToBlank(val, def){
	if(val == null || val == 'null'){
		if(def){
			return def;
		}else{
			return "";
		}
	}
	return val;
}

//截取字符串
function subTxt(val, maxLen){
	if(val == null || val == "null"){
		return "";
	}
	if(val.length <= maxLen){
		return val;
	}
	return val.substring(0, maxLen) + "...";
}

//将数字ID转换为字符串ID，需与CommonUtil中的strToId方法保持一致
function idToStr(numId){
	if(isNull(numId)){
		return "";
	}
	try{
		numId = numId + "";
		var arr = numId.split("");
		var len = arr.length;
		var idStr = "";
		for(ni=0; ni<len; ni++){
			if(arr[ni] == "0" || arr[ni] == 0){
				idStr += "z";
			}else if (arr[ni] == "1" || arr[ni] == 1){
				idStr += "K";
			}else if (arr[ni] == "2" || arr[ni] == 2){
				idStr += "U";
			}else if (arr[ni] == "3" || arr[ni] == 3){
				idStr += "O";
			}else if (arr[ni] == "4" || arr[ni] == 4){
				idStr += "J";
			}else if (arr[ni] == "5" || arr[ni] == 5){
				idStr += "M";
			}else if (arr[ni] == "6" || arr[ni] == 6){
				idStr += "w";
			}else if (arr[ni] == "7" || arr[ni] == 7){
				idStr += "A";
			}else if (arr[ni] == "8" || arr[ni] == 8){
				idStr += "s";
			}else if (arr[ni] == "9" || arr[ni] == 9){
				idStr += "E";
			}else{
				idStr += arr[ni];
			}
		}
		
		return idStr;
	}catch(e){
		return numId;
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
		

//判断字符串是否为空，为空返回true，不为空返回false
function isEmpty(v){
	if(v == null || typeof 'undefined' == v 
		|| v == 'null' || v == 'NULL' || v == '' ||  typeof v == 'undefined'){
		return true;
	}
	return false;
}
