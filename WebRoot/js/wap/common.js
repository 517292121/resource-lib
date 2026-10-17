function simSearch(){
	var k = $("#searchkey").val();
	if(k == null || k.length < 1){
		layer.msg(title_input_key);
		return;
	}
	if(AntiScriptValid(k)){
		var matchtype = "";
		if($("#match").is(':checked')){
			matchtype = "?match=1";
		}
		
		window.location.href = basePath + "wap/ls/" + encodeURI(k) + matchtype;
	}
}

$(function(){
	 $('#searchkey').bind('keypress',function(event){  
           if(event.keyCode == "13") {  
 				simSearch();
           }  
       });
})

function inLogin(){
	window.location.href = basePath + "wap/login?from=" + encodeURIComponent(location.href);			
}
function inUsercenter(){
	layer.confirm(poput_user_only_support, {
	    btn: [poput_determine_user_of,operation_key_cancel] //按钮
	}, function(){
	    window.location.href = basePath + "member/center";
	}, function(){
	});
}


//退出登录
function exitLogin(){
	layer.confirm(poput_confirm_exit, {
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


function userWin(){
	if($(".w_userinfo").is(':hidden')){　　//如果node是隐藏的则显示node元素，否则隐藏
	　　$(".w_userinfo").show();　
		$(".w_userinfo").animate({height:"200px"}, 300);
	}else{
	　　$(".w_userinfo").hide();
		$(".w_userinfo").height("0px");
	}
}

//防止script和SQL注入
function AntiScriptValid(text) {
    re = /select |update |insert |delete |exec |count |;|>|<|%/i;
    var temp = true;
    if (re.test(text)) {
        temp = false;
        layer.msg("请您不要在参数中输入特殊字符和SQL关键字！"); //注意中文乱码
    }
    return temp;
}	
