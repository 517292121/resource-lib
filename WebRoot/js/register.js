 // &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   验证开始     &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
// 全局变量用于验证用户名是否存在
var loginNameIsExit = false;
// 全局变量用于验证邮箱是否使用
var emailIsUse = false;

function jsSubmit(){
	var regForm = document.getElementById('regForm1');
	if(regForm && onFormSubmit() && !loginNameIsExit && !emailIsUse){
		document.getElementById('password_md5').value = hex_sha1($('#reg_password').attr("value")); 
		regForm.submit();
	}
}
// ----------------------- 通用函数 begin ---------------------
// 获取字符串长度
function getStrLength(str) {
	var chineseRegex = /[^\x00-\xff]/g;
	var strLength = str.replace(chineseRegex, "**").length;
	return strLength;
}

function checkLoginNameIsExit(loginName,row){
	sysUserPlatformFacade.checkUserNameIsExit(loginName,function(data){
		var msgs = eval("("+data+")");
		// 当用户名已经存在时，显示提示信息
		var inputobj = row.inputobj;
		// 右侧提示对象，包含了两个div,一个提示小icon.一个提示框。
		var tipobj = $('#' + inputobj.attr("id") + "_infoDiv");
		// 提示icon
		var tipicon = $(tipobj.children()[0]);
		var reg_loginName_isexit_err = $('#reg_loginName_isexit_err');
		
		if (!msgs.isExit) {
			tipobj.show();
			if(tipicon != null) {
				tipicon.attr("className", "infoIcon_ok");
				tipicon.show();
			}
			reg_loginName_isexit_err.attr("className", "loginnameisunuse");
			reg_loginName_isexit_err.attr("innerHTML", msg_ok);
			reg_loginName_isexit_err.show();
			
			loginNameIsExit = false;
			return;
		}else{
			tipobj.show();
			if(tipicon != null) {
				tipicon.attr("className", "infoIcon_err");
				tipicon.show();
			}
			reg_loginName_isexit_err.attr("className", "loginnameisuse");
			reg_loginName_isexit_err.attr("innerHTML", msg_error);
			reg_loginName_isexit_err.show();
			
			loginNameIsExit = true;
		}
	});
}


function checkEmailIsUse(email,row){
	sysUserPlatformFacade.checkEmailIsUse(email,function(data){
		var msgs = eval("("+data+")");
		// 当用户名已经存在时，显示提示信息
		var inputobj = row.inputobj;
		// 右侧提示对象，包含了两个div,一个提示小icon.一个提示框。
		var tipobj = $('#' + inputobj.attr("id") + "_infoDiv");
		// 提示icon
		var tipicon = $(tipobj.children()[0]);
		var reg_loginName_isexit_err = $('#reg_email_isuse_err');
		
		if (!msgs.isExit) {
			tipobj.show();
			if(tipicon != null) {
				tipicon.attr("className", "infoIcon_ok");
				tipicon.show();
			}
			reg_loginName_isexit_err.attr("className", "emailisunuse");
			reg_loginName_isexit_err.attr("innerHTML", msg_email_unuse);
			reg_loginName_isexit_err.show();
			
			emailIsUse = false;
			return;
		}else{
			tipobj.show();
			if(tipicon != null) {
				tipicon.attr("className", "infoIcon_err");
				tipicon.show();
			}
			reg_loginName_isexit_err.attr("className", "emailisuse");
			reg_loginName_isexit_err.attr("innerHTML", msg_email_isuse);
			reg_loginName_isexit_err.show();
			
			emailIsUse = true;
		}
	
	
	});

}
// ----------------------- 通用函数 end ---------------------

// ----------------------- 验证信息辅助函数 begin ---------------------
function checkusername(username) {
	return /^[a-zA-Z]([a-zA-Z]|\d|_){2,16}([a-zA-Z]|\d)$/.test(username);
}

function checkpassword(password) {
	var len = getStrLength(password);
	var isCorrect = true;
	if(len < 6 || len > 18) {
		isCorrect = false;
	}
	return isCorrect;
}

function checkRepeatPassword(repeatPassword) {
	return repeatPassword == $('#reg_password').attr("value");
}

function checkQuestion() {
	var isCorrect = false;
	var v = $('#reg_question').attr("value");
	if(v > 0){
		isCorrect = true;
	}
	return isCorrect;
}

function checkAnswer(answer) {
	var len = getStrLength(answer);
	return len >= 2 && len <= 30;
}

function checkMail(mail) {
	return /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/.test(mail);
}

function checkName(name) {
	// 真实姓名必填或者真实姓名选填但是用户填了，就需要校验合法性
	var len = getStrLength(name);
	if(len <= 0 || len > 26) return false;
	var invalidWords = ['netease', '163.com', '126.com', 'yeah.net', '188.com'];
	for(var i = 0, size = invalidWords.length; i < size; ++i) {
		if(name.indexOf(invalidWords[i]) != -1) {
			name_row.errtip = msg_name_info;
			return false;
		}
	}
	var invalidChars = '|+)(*&^%$#@!~=\\}{][:;?></';
	for(var i = 0, size = invalidChars.length; i < size; ++i) {
		if(name.indexOf(invalidChars.charAt(i)) != -1) {
			name_row.errtip = msg_name_info;
			return false;
		}
	}
	return true;
}


function checkDate(date) {
	var pattern = /(((19|20)\d{2})\-(0{0,1}[1-9]|1[0-2])\-(0{0,1}[1-9]|[12][0-9]|3[01]))$/;
	return pattern.test(date);
}

function checkPhone(phone) {
	// 该正则表达式可验证：手机号和固定电话。
	var pattern = /^(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/;
	if(pattern.test(phone))
		return true;
	else
		return false;
}
// ----------------------- 验证信息辅助函数 end ---------------------

var reg_row = function(inputobj, isOption, checkfunction) {
	this.inputobj = inputobj;
	this.isOption = isOption;
	this.checkfunction = checkfunction;
	this.errtip = "";
	this.initMsg = "";

};

var username_row = new reg_row($('#reg_loginName'), false, checkusername);
var password_row = new reg_row($('#reg_password'), false, checkpassword);
var password2_row = new reg_row($('#reg_password2'), false, checkRepeatPassword);
var question_row = new reg_row($('#reg_question'), false, checkQuestion);
var answer_row = new reg_row($('#reg_answer'), false, checkAnswer);
var email_row = new reg_row($('#reg_email'), false, checkMail);
var name_row = new reg_row($('#reg_name'), false, checkName);
var birthday_row = new reg_row($('#reg_birthday'), false, checkDate);
var mobileTelephone_row = new reg_row($('#reg_mobileTelephone'), false, checkPhone);


var rows = [];
rows.push(username_row);
rows.push(password_row);
rows.push(password2_row);
rows.push(question_row);
rows.push(answer_row);
rows.push(email_row);
rows.push(name_row);
rows.push(birthday_row);
rows.push(mobileTelephone_row);


function onFocus(event) {
	var inputobj = $(this);
	var row = event.data.row;

	// 右侧提示对象，包含了两个div,一个提示小icon.一个提示框。
	var tipobj = $('#' + inputobj.attr("id") + "_infoDiv");

	// 提示icon
	var tipicon = $(tipobj.children()[0]);
	// 文字提示
	var tiptext = $(tipobj.children()[1]);

	tipobj.show();
	if(tipicon != null) tipicon.hide();
	tiptext.attr("className", "infoBox_ok");
	tiptext.show();
	if(row.initMsg != null && row.initMsg != "") {
		$(tiptext.children()[0]).attr("innerHTML", row.initMsg);
	}
	
	// 隐蔽验证提示信息
	var reg_loginName_isexit_err = $('#reg_loginName_isexit_err');
	var reg_email_isuse_err = $('#reg_email_isuse_err');
	if(reg_loginName_isexit_err){
		reg_loginName_isexit_err.attr("className", "hide");
		reg_loginName_isexit_err.attr("innerHTML", "");
		reg_loginName_isexit_err.hide();
	}
	if(reg_email_isuse_err){
		reg_email_isuse_err.attr("className", "emailisunuse");
		reg_email_isuse_err.attr("innerHTML", "");
		reg_email_isuse_err.hide();
	}
}


function checkrow(row) {
	var isRowInputCorrect = false;

	var inputobj = row.inputobj;

	// 右侧提示对象，包含了两个div,一个提示小icon.一个提示框。
	var tipobj = $('#' + inputobj.attr("id") + "_infoDiv");
	// 提示icon
	var tipicon = $(tipobj.children()[0]);
	// 文字提示
	var tiptext = $(tipobj.children()[1]);

	tipobj.show();

	// 需要校验的值。
	var value_checked;
	if(row.checkvalue != null) {
		value_checked = row.checkvalue;
	}else
		value_checked = inputobj.attr("value");

	if(row.checkfunction(value_checked)) {
		// 当用户名已经存在时，不更改图标
		var tempId = inputobj.attr("id");
		if(tempId == 'reg_loginName'){
			// 提示输入正确
			if(tipicon != null && !loginNameIsExit) {
				tipicon.attr("className", "infoIcon_ok");
				tipicon.show();
			}	
		}else if(tempId == 'reg_email'){
			if(tipicon != null && !emailIsUse){
				tipicon.attr("className", "infoIcon_ok");
				tipicon.show();
			}
		}else{
			// 提示输入正确
			if(tipicon != null) {
				tipicon.attr("className", "infoIcon_ok");
				tipicon.show();
			}
		}
		// 隐藏文字提示
		tiptext.hide();
		isRowInputCorrect = true;
	}else {
		// 提示输入错误
		if(tipicon != null) {
			tipicon.attr("className", "infoIcon_err");
			tipicon.show();
		}

		// 显示输入提示
		if(row.errtip != "") {
			// tiptext.attr("innerHTML",row.errtip);
			try {
				// 显示校验函数返回的错误提示。
				var errTipText = $(tiptext.children()[0]);
				errTipText.attr("innerHTML", row.errtip);
			}catch(e) {

			}
		}

		tiptext.attr("className", "infoBox_err");
		tiptext.show();
		isRowInputCorrect = false;
	}

	return isRowInputCorrect;

}
function onBlur(event) {
	var inputobj = $(this);
	var row = event.data.row;

	// 如果什么都不输入。则隐藏提示
	if(getStrLength(inputobj.attr("value")) == 0) {
		$('#' + inputobj.attr("id") + "_infoDiv").hide();
		return;
	}

	if(!checkrow(row)){
		return ;
	}
	
	// 提交和blur时，如果重复登录密码和密码相同，但是登录密码不对，应该隐藏掉重复登录密码的提示。
//	if(checkrow(password2_row) && !checkrow(password_row)) {
//		var inputobj = password2_row.inputobj;
//
//		// 右侧提示对象，包含了两个div,一个提示小icon.一个提示框。
//		var tipobj = $('#' + inputobj.attr("id") + "_infoDiv");
//		// 提示icon
//		var tipicon = $(tipobj.children()[0]);
//		// 文字提示
//		var tiptext = $(tipobj.children()[1]);
//		if(tipicon != null) tipicon.hide();
//	}
	
	// AJAX验证该用户名是否存在
	var tempId = inputobj.attr("id");
	var tempLoginName = inputobj.attr("value");
	if(tempId == 'reg_loginName'){
		checkLoginNameIsExit(tempLoginName,row);
	}
	
	// AJAX验证邮箱是否被用户使用
	if(tempId == 'reg_email'){
		checkEmailIsUse(tempLoginName,row);
	}
}
for(var i = 0; i < rows.length; ++i) {
	// var row = rows[i];
	rows[i].inputobj.bind("focus", {
		row : rows[i]
	}, onFocus);
	rows[i].inputobj.bind("blur", {
		row : rows[i]
	}, onBlur);
	
}


 

// 表单的submit事件处理。
// 可选项
var option_rows = [];
option_rows.push(birthday_row);
option_rows.push(mobileTelephone_row);

function onFormSubmit() {
	// alert(document.getElementById("mobcheckcode").style.display);

	var s_firstRow;// 第一个填错的行。
	
	// 必填项
	var mandatory_rows = [];
	mandatory_rows.push(username_row);
	mandatory_rows.push(password_row);
	mandatory_rows.push(password2_row);
	mandatory_rows.push(question_row);
	mandatory_rows.push(answer_row);
	mandatory_rows.push(email_row);
	mandatory_rows.push(name_row);
	var isFormCorrect = true;
	// 必选项必须填,而且需要填对。
	for(var i = 0; i < mandatory_rows.length; ++i) {
		var row = mandatory_rows[i];
		if(!checkrow(row)) {
			isFormCorrect = false;
			if(s_firstRow == null) s_firstRow = row;
		}
	}

	// 可选项填了就必须填对;
	for(var i = 0; i < option_rows.length; ++i) {
		var row = option_rows[i];
		var inputobj = row.inputobj;
		if(getStrLength(inputobj.attr("value")) > 0) {
			if(!checkrow(row)) {
				isFormCorrect = false;
				if(s_firstRow == null) s_firstRow = row;
			}
		}

	}
	// 提交和blur时，如果重复登录密码和密码相同，但是登录密码不对，应该隐藏掉重复登录密码的提示。
	if(checkrow(password2_row) && !checkrow(password_row)) {
		var inputobj = password2_row.inputobj;

		// 右侧提示对象，包含了两个div,一个提示小icon.一个提示框。
		var tipobj = $('#' + inputobj.attr("id") + "_infoDiv");
		// 提示icon
		var tipicon = $(tipobj.children()[0]);

		if(tipicon != null) tipicon.hide();

	}
	// patch ： 只要用户输入了验证码，就隐藏掉右侧的提示 .

	if(isFormCorrect) {
		/*
		 * try{ if(mobShouldCallPhone){ //表单项检查正确 //如果输入了手机号，提示其拨打400电话验证。 var mobile = mobile_row.inputobj.attr("value"); if(mobile.length >
		 * 0){ if(window.confirm( "请在注册前使用您的手机" + mobile + "拨打400-6780-163免费电话进行验证","我已经拨打过电话", "删除或者更换手机号")) isFormCorrect = true; else {
		 * clickIgnore(); isFormCorrect = false; mobile_row.inputobj.focus(); } } } }catch(e){ isFormCorrect = true; }
		 */

	}else {
		// 有填错的表单域。
		if(s_firstRow != null) {
			try {
				var s_id = s_firstRow.inputobj.parent().attr("id");
				if(s_id != null && s_id != "") window.location.hash = ('#' + s_id);

			}catch(e) {
			}
		}
	}

	return isFormCorrect;

}
// &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&   验证 结束     &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&
