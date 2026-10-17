/**
 *	用户注册处理
 *  wanglin
 *  2018-01
 */

$(function(){
	//var width = 400; //跟外面盒子的宽度一样，或者写成 var width = $(".content").width();
	var width = $(".container").width();
	$(".content ul").width(width);
	var ulNum = $(".content ul").length; //获取ul的个数
	var contentWidth = width * ulNum; //获取整个box应该的长度，刚开始box设置成了1100，但是应该把所有的ul防到一行里面去，这样box向左移动的时候才是无缝滚动

	$(".box").width(contentWidth); //给box设置宽度  .width() 是获取宽度  .width(value)是设置宽度
	$(".reg_btn").width($("#e_imgCode").width());

	$(".nav span").click(function(){
		$(this).addClass('active').siblings().removeClass('active');
		var clickNum = $(this).index(); //判断点击的是第几个span .index()方法返回第几个，从0开始算起
		var moveLeft = clickNum * width * -1; //应该向左移动的距离
		$(".box").animate({'left':moveLeft}, 600);
	})

});

 
//弹出服务协议 
function openserver(){
	window.parent.openRegServer();
}

function inLogin(){
	window.parent.openLogin();
}

function refcode(){
	var dat = new Date();
    var image = basePath + "yzm.jsp?id=" + dat.getTime();
    $("#imgcode1").attr('src',image);
    $("#imgcode2").attr('src',image); 
}

function regCheck(reg, str) {
    if (reg.test(str)) {
        return true;
    }else{
        return false;
    }
}

function checkPwd(eleId){
	var pwdval = $("#" + eleId).val().trim();
	if(pwdval == ""){
		return poput_alert_password_not_null;
	}
	if(pwdval.length < 6){
		return poput_alert_pass_min_number;
	}
	if(pwdval.length > 24){
		return poput_alert_pass_max_number;
	}
	return "";
}

function getMobileCode(){
	var telVal = $("#mobileTel").val().trim(); 
	if(telVal == ""){
		layer.tips(poput_alert_phone_number, '#mobileTel', {tips: [3, '#FF7F00'], time: 4000});
		return;
	}
    if (regCheck(/^[1][34578][0-9]{9}$/, telVal) == false) {
        layer.tips(poput_alert_phone_incorrect, '#mobileTel', {tips: [3, '#FF7F00'], time: 5000});
		return;
    }
    
    var pwdres = checkPwd("loginPwd");
    if(pwdres.length > 0){
    	layer.tips(pwdres, '#loginPwd', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }

	var imgVal = $("#imgCode").val().trim().toUpperCase();
	if (imgVal == "") {
        layer.tips(poput_alert_input_picture, '#imgCode', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    if (imgVal.length != 4) {
        layer.tips(poput_alert_code_input_incorrect, '#imgCode', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    
    var loadobj = layer.load(4, {shade: [0.1,'#fff']});
    $.ajax({
	    type: 'GET',
	    url: basePath + 'reg/imgcode?t=' + new Date().getTime(),
	    cache:false,
	    success: function(data){
	    	layer.close(loadobj);
	    	if(data == null || data.length < 1){
	    		layer.msg(poput_alert_code_obtain_incorrect, {icon:2});
	    		return;
	    	}
	    	if(imgVal != data){
	    		layer.msg(poput_alert_picture_input_incorrect, {icon:2});
	    		return;
	    	}
	    	sendSmsCode();
	    },
	    fail: function (err, status) {
	    	layer.close(loadobj);
	    	layer.msg(poput_alert_code_obtain_incorrect, {icon:2});
	    }
	});
}

function sendSmsCode(){
	var telVal = $("#mobileTel").val().trim(); 
	$.ajax({
	    type: 'GET',
	    url: basePath + 'reg/sendsms/'+telVal+'?t=' + new Date().getTime(),
	    cache:false,
	    success: function(data){
	    	try{
		    	var val = eval("("+data+")");
	    		if(val.success == true || val.success == "true"){
					layer.msg(poput_alert_code_success, {icon:1});
					var btn = $("#smsbtn");
					startNext(btn);
	    		}else{
	    			layer.alert(val.errmsg);
	    		}
		   	}catch(e){
		   		layer.msg(poput_alert_error + e.message, {icon:2});
		   	}
	    },
	    fail: function (err, status) {
	    	layer.msg(poput_alert_code_error, {icon:2});
	    }
	});
}

var countdown=120;
function startNext(obj){
	if (countdown == 0) { 
        obj.attr('disabled',false); 
        obj.val(poput_alert_obtain_code);
        countdown = 120; 
        return;
    } else { 
        obj.attr('disabled',true);
        obj.val(countdown + poput_alert_seconds_later_resend);
        countdown--; 
    } 
    setTimeout(function(){ 
    	startNext(obj) 
    },1000);
}


function mobileRegSubmit(){
	var telVal = $("#mobileTel").val().trim(); 
	if(telVal == ""){
		layer.tips(poput_alert_phone_number, '#mobileTel', {tips: [3, '#FF7F00'], time: 4000});
		return;
	}
    if (regCheck(/^[1][34578][0-9]{9}$/, telVal) == false) {
        layer.tips(poput_alert_phone_incorrect, '#mobileTel', {tips: [3, '#FF7F00'], time: 5000});
		return;
    }
    
    var pwdres = checkPwd("loginPwd");
    if(pwdres.length > 0){
    	layer.tips(pwdres, '#loginPwd', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }

	var imgVal = $("#imgCode").val().trim().toUpperCase();
	if (imgVal == "") {
        layer.tips(poput_alert_input_picture, '#imgCode', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    if (imgVal.length != 4) {
        layer.tips(poput_alert_picture_input_incorrect, '#imgCode', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    
    var smsCode = $("#smsCode").val();
    if (smsCode == "") {
        layer.tips(poput_alert_input_sms_code, '#smsCode', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    if (smsCode.length != 6) {
        layer.tips(poput_alert_sms_code_input_error, '#smsCode', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    
    if($("#regServer").is(':checked') == false){
    	layer.tips(poput_alert_reginstration_agreement, '#regServer', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    if($("#regServer").is(':checked') == false){
    	layer.tips('请阅读并同意服务条款！', '#regServer', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    
    $.ajax({
	    type: 'GET',
	    url: basePath + 'reg/imgcode?t=' + new Date().getTime(),
	    cache:false,
	    success: function(data){
	    	if(data == null || data.length < 1){
	    		layer.msg(poput_alert_code_obtain_incorrect, {icon:2});
	    		return;
	    	}
	    	if(imgVal != data){
	    		layer.msg(poput_alert_picture_input_incorrect, {icon:2});
	    		return;
	    	}
	    	checkSmsCode();
	    },
	    fail: function (err, status) {
	    	layer.msg(poput_alert_code_obtain_incorrect, {icon:2});
	    }
	});
}
function checkSmsCode(){	
	var smsCode = $("#smsCode").val();
	$.ajax({
	    type: 'GET',
	    url: basePath + 'reg/smscode?t=' + new Date().getTime(),
	    cache:false,
	    success: function(data){
	    	if(data == null || data.length < 1){
	    		layer.msg(poput_alert_sms_code_error, {icon:2});
	    		return;
	    	}
	    	if(smsCode != data){
	    		layer.msg(poput_alert_sms_code_input_error, {icon:2});
	    		return;
	    	}
	    	mobileReg();
	    },
	    fail: function (err, status) {
	    	layer.msg(poput_alert_sms_code_error, {icon:2});
	    }
	});
}
function mobileReg(){
	var telVal = $("#mobileTel").val().trim();
	var pwdval = $("#loginPwd").val().trim();
	var imgVal = $("#imgCode").val().trim();
	var smsCode = $("#smsCode").val().trim();
	var md5pwd = hex_sha1(pwdval); //用户密码进行md5加密
	
	var loadobj = layer.load(4, {shade: [0.1,'#fff']});
	$.ajax({
	    type: 'GET',
	    url: basePath + 'reg/mobile/save?t=' + new Date().getTime(),
	    data: {
	    	telVal : telVal,
	    	pwdval : md5pwd,
	    	imgCode : imgVal,
	    	smsCode : smsCode
	    },
	    cache:false,
	    success: function(data){
	    	layer.close(loadobj);
	    	try{
		    	var val = eval("("+data+")");
	    		if(val.success == true || val.success == "true"){
					//layer.msg("注册成功！", {icon:1});
					printResult(telVal, poput_alert_click_enter+"<a href='"+basePath+"member/center' target='_parent'>"+poput_alert_user_center+"</a>");
	    		}else{
	    			layer.alert(val.errmsg, {icon:2});
	    		}
		   	}catch(e){
		   		layer.msg(poput_alert_error + e.message, {icon:2});
		   	}
	    },
	    fail: function (err, status) {
	    	layer.close(loadobj);
	    	layer.msg(poput_alert_login_has_failed, {icon:2});
	    }
	});
}

function emailReg(){
	var loginName = $("#e_loginName").val();
	if(loginName == ""){
		layer.tips(poput_alert_input_user_name, '#e_loginName', {tips: [3, '#FF7F00'], time: 4000});
		return;
	}
	if(loginName.length < 6 || loginName.length > 64){
		layer.tips(poput_alert_user_name, '#e_loginName', {tips: [3, '#FF7F00'], time: 4000});
		return;
	}
	
	var loginPwd = $("#e_loginPwd").val();
	var pwdres = checkPwd("e_loginPwd");
    if(pwdres.length > 0){
    	layer.tips(pwdres, '#e_loginPwd', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }	
	var md5pwd = hex_sha1(loginPwd); //用户密码进行md5加密
	
	var reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/; 
	var email = $("#e_email").val().trim();
	if(regCheck(reg, email) == false){
		layer.tips(poput_alert_mailbox_format_error, '#e_email', {tips: [3, '#FF7F00'], time: 4000});
		return;
	}
	
	var imgCode = $("#e_imgCode").val().trim().toUpperCase();
	if (imgCode == "") {
        layer.tips(poput_alert_input_picture, '#e_imgCode', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    if (imgCode.length != 4) {
        layer.tips(poput_alert_picture_input_incorrect, '#e_imgCode', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
    console.log($("#e_regServer").is(':checked'));
    if($("#e_regServer").is(':checked') == false){
    	layer.tips('请阅读并同意服务条款！', '#e_regServer', {tips: [3, '#FF7F00'], time: 4000});
		return;
    }
	
	var loadobj = layer.load(4, {shade: [0.1,'#fff']});
	$.ajax({
	    type: 'GET',
	    url: basePath + 'reg/email/save?t=' + new Date().getTime(),
	    data: {
	    	loginName : loginName,
	    	loginPwd : md5pwd,
	    	email : email,
	    	imgCode : imgCode
	    },
	    cache:false,
	    success: function(data){
	    	layer.close(loadobj);
	    	try{
		    	var val = eval("("+data+")");
	    		if(val.success == true || val.success == "true"){
					//layer.msg(val.result, {icon:1});
					printResult(loginName, val.result);
	    		}else{
	    			layer.alert(val.errmsg, {icon:2});
	    		}
		   	}catch(e){
		   		layer.msg(poput_alert_error + e.message, {icon:2});
		   	}
	    },
	    fail: function (err, status) {
	    	layer.close(loadobj);
	    	layer.msg(poput_alert_login_has_failed, {icon:2});
	    }
	});
}

function printResult(loginName, msg){
	var html = "<div class='regTips'>";
	html += "<img src='"+basePath+"images/platform/"+skin+"/success.png'></img> ";
	html += "<span><i>"+loginName+"</i> "+poput_alert_login_success+"</span>";
	html += "</div>";
	html += "<p class='incenter'><br>";
	html += msg;
	html += "</p>";
	$(".container").html(html);
}

