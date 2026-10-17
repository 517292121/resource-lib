// 系统参数设置
Ext.onReady(function() {
	// 如果要体现FORM效验效果，必须添加下面两行代码
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

	var isUploadResource = new Ext.form.RadioGroup({
		fieldLabel : '是否允许用户上传资源',
		name : 'user_upload_resource_is_open',
		id : 'user_upload_resource_is_open',
		anchor : '70%',
		items : [{
			boxLabel : '是',
			name : 'user_upload_resource_is_open',
			inputValue : 1
		}, { 
			boxLabel : '否',
			name : 'user_upload_resource_is_open',
			inputValue : 0
		}]
	});

	var userRegisterCheckType = new Ext.form.RadioGroup({
		fieldLabel : '会员审核方式',
		name : 'user_register_check_type',
		id : 'user_register_check_type',
		anchor : '85%',
		value : 2,
		items : [{
			boxLabel : '邮件审核',
			name : 'user_register_check_type',
			inputValue : 2
		}, {
			boxLabel : '管理员审核',
			name : 'user_register_check_type',
			inputValue : 1
		}]
	});

	var sendMailHost = new Ext.form.TextField({
		fieldLabel : '邮件发送服务器(SMTP)',
		id : 'sendmail_host',
		name : 'sendmail_host',
		anchor : '90%',
		allowBlank : false,
		blankText : '邮件发送服务器不能为空!',
		maxLength : 40,
		maxLengthText : '邮件发送服务器不得超过40个字符!'
	});

	var sendMailFromAddress = new Ext.form.TextField({
		fieldLabel : '发信人邮件地址',
		id : 'sendmail_from_address',
		name : 'sendmail_from_address',
		anchor : '90%',
		allowBlank : false,
		blankText : '发信人邮件地址不能为空!',
		maxLength : 40,
		maxLengthText : '发信人邮件地址不得超过40个字符!'
	});

	var sendMailFromUserName = new Ext.form.TextField({
		fieldLabel : '发信人邮件登录用户名',
		id : 'sendmail_from_username',
		name : 'sendmail_from_username',
		anchor : '90%',
		allowBlank : false,
		blankText : '发信人邮件登录用户名不能为空!',
		maxLength : 40,
		maxLengthText : '发信人邮件登录用户名不得超过40个字符!'
	});

	var sendMailFromPassword = new Ext.form.TextField({
		fieldLabel : '发信人邮件登录密码',
		id : 'sendmail_from_password',
		name : 'sendmail_from_password',
		anchor : '90%',
		allowBlank : false,
		blankText : '发信人邮件登录密码不能为空!',
		maxLength : 40,
		maxLengthText : '发信人邮件登录密码不得超过40个字符!'
	});

	/*var sensitiveWords = new Ext.form.TextField({
		fieldLabel : "敏感词(多个词用'|'分隔)",
		id : 'sensitive_words',
		name : 'sensitive_words',
		anchor : '90%',
		maxLength : 350,
		maxLengthText : '敏感词不得超过350个字符!'
	});*/
	
	var resouceUPloadSourceFileMaxsize = new Ext.form.NumberField({
		fieldLabel : '允许上传的源文件大小(M)',
		id : 'RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE',
		name : 'RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE',
		anchor : '90%',
		minValue : 1,
		maxValue : 10240,
		allowBlank : false
	});
	
	var resourceUploadViewFileMaxsize = new Ext.form.NumberField({
		fieldLabel : '允许上传的预览文件大小(M)',
		id : 'RESOURCE_UPLOAD_VIEWFILE_MAXSIZE',
		name : 'RESOURCE_UPLOAD_VIEWFILE_MAXSIZE',
		anchor : '90%',
		minValue : 1,
		maxValue : 10240,
		allowBlank : false
	});
	
	var resourceUploadPhotoFileMaxsize = new Ext.form.NumberField({
		fieldLabel : '允许上传的封面文件大小(M)',
		id : 'RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE',
		name : 'RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE',
		anchor : '90%',
		minValue : 1,
		maxValue : 10240,
		allowBlank : false
	});
	
	
	/*var resourceLoctionOptions = new Ext.form.TextField({
		fieldLabel : '馆藏位置选项',
		id : 'SOURCE_LOCATION_OPTIONS',
		name : 'SOURCE_LOCATION_OPTIONS',
		anchor : '90%',
		minValue : 1,
		maxValue : 10240,
		allowBlank : false
	});*/
	
	
	var rsda_rdty_one = new Ext.form.TextField({
		fieldLabel : '自定义分类一名称',
		id : 'rsda_rdty_one_name',
		name : 'rsda_rdty_one_name',
		anchor : '90%',
		allowBlank : false,
		blankText : '自定义分类一名称不能为空!',
		maxLength : 40,
		maxLengthText : '自定义分类一名称不得超过40个字符!'
	});
	
	var rsda_rdty_two = new Ext.form.TextField({
		fieldLabel : '自定义分类二名称',
		id : 'rsda_rdty_two_name',
		name : 'rsda_rdty_two_name',
		anchor : '90%',
		allowBlank : false,
		blankText : '自定义分类二名称不能为空!',
		maxLength : 40,
		maxLengthText : '自定义分类二名称不得超过40个字符!'
	});
	
	var rsda_rdty_three = new Ext.form.TextField({
		fieldLabel : '自定义分类三名称',
		id : 'rsda_rdty_three_name',
		name : 'rsda_rdty_three_name',
		anchor : '90%',
		allowBlank : false,
		blankText : '自定义分类三名称不能为空!',
		maxLength : 40,
		maxLengthText : '自定义分类三名称不得超过40个字符!'
	});
	
	var resouceDownMaxsize = new Ext.form.NumberField({
		fieldLabel : '允许用户每天下载资源的次数',
		id : 'RESOURCE_DOWN_MAXSIZE',
		name : 'RESOURCE_DOWN_MAXSIZE',
		anchor : '90%',
		minValue : 1,
		maxValue : 10240,
		allowBlank : false
	});
	
	var resourceRootDir = new Ext.form.TextArea({
		fieldLabel : '资源目录',
		id : 'resource_root_dir',
		name : 'resource_root_dir',
		anchor : '90%',
		emptyText: '请输入资源主目录，一个资源主目录为一行',
	});
	
	var settingForm = new Ext.form.FormPanel({
		region : 'center',
		border : false,
		frame : false,
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelAlign : 'right',
		buttonAlign : 'center',
		id : 'settingForm',
		autoScroll : true,
		layout : 'hbox',
		layoutConfig : {
			align : 'middle',
			pack : 'center'
		},
		labelWidth : 250,
		items : [{
			xtype : 'fieldset',
			layout : 'form',
			width : 550,
			title : '系统参数设置',
			items : [resouceDownMaxsize,resouceUPloadSourceFileMaxsize,resourceUploadViewFileMaxsize,resourceUploadPhotoFileMaxsize,/*resourceLoctionOptions,
			sensitiveWords,*/userRegisterCheckType, isUploadResource, sendMailHost,sendMailFromAddress, sendMailFromUserName, sendMailFromPassword,
			rsda_rdty_one,rsda_rdty_two,rsda_rdty_three, resourceRootDir]
		}],
		buttons : [{
			text : '提&nbsp;交',
			handler : submitForm
		}, {
			text : '重&nbsp;置',
			handler : initData
		}]
	});
	var displayPanel = new Ext.Viewport({
		layout : 'border',
		items : [settingForm]
	});

	function initData() {
		Ext.Ajax.request({
			url : getPath() + 'console/data/sysSetting.htm?action=getMmSettingInfo',
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					
					// 初始化Form
					settingForm.form.setValues({
						user_upload_resource_is_open : txt.userUploadResourceIsOpen,
						user_register_check_type : txt.userRegisetrCheckType,
						sendmail_host : txt.sendMailHost,
						sendmail_from_address : txt.sendMailFromAddress,
						sendmail_from_username : txt.sendMailFromUsername,
						sendmail_from_password : txt.sendMailFromPassword,
						//sensitive_words : txt.sensitiveWords,
						RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE : txt.resouceUPloadSourceFileMaxsize,
						RESOURCE_UPLOAD_VIEWFILE_MAXSIZE : txt.resourceUploadViewFileMaxsize,
						RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE : txt.resourceUploadPhotoFileMaxsize,
						//SOURCE_LOCATION_OPTIONS : txt.resourceLoctionOptions
						RESOURCE_DOWN_MAXSIZE : txt.resouceDownMaxsize,
						
						rsda_rdty_one_name : txt.rsdaRdtyOneName,
						rsda_rdty_two_name : txt.rsdaRdtyTwoName,
						rsda_rdty_three_name : txt.rsdaRdtyOneThreeName,
						resource_root_dir : txt.resourceRootDir
					});

				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统忙，请稍后访问！');
			}
		});
	}

	function submitForm() {
		settingForm.getForm().submit({
			url : getPath() + 'console/data/sysSetting.htm?action=saveMmSetting',
			method : 'post',
			waitMsg : '正在保存数据，请稍候...',
			success : function(form, action) {
				alertSuccess(action.result.msg);
			},
			failure : function(form, action) {
				alertError(action.result.msg);
			}
		});
	}
	// 初始化网站参数
	initData();
});