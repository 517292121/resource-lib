// 网站参数设置
Ext.onReady(function() {
	// 如果要体现FORM效验效果，必须添加下面两行代码
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

	var asspoint_login = new Ext.form.NumberField({
		id : 'asspoint_login',
		name : 'asspoint_login',
		fieldLabel : '登录积分',
		allowDecimals : false, // 不允许输入小数
		nanText : '请输入有效整数', // 无效数字提示
		allowNegative : false,
		anchor : '90%',
		maxLength : 6,
		maxLengthText : '最大长度不能超过6位数！'
	}); 
	
	var asspoint_machining_res = new Ext.form.NumberField({
		id : 'asspoint_machining_res',
		name : 'asspoint_machining_res',
		fieldLabel : '加工资源积分',
		allowDecimals : false, // 不允许输入小数
		nanText : '请输入有效整数', // 无效数字提示
		allowNegative : false,
		anchor : '90%',
		maxLength : 6,
		maxLengthText : '最大长度不能超过6位数！'
	}); 
	
	var asspoint_machining_message = new Ext.form.NumberField({
		id : 'asspoint_machining_message',
		name : 'asspoint_machining_message',
		fieldLabel : '评论积分',
		allowDecimals : false, // 不允许输入小数
		nanText : '请输入有效整数', // 无效数字提示
		allowNegative : false,
		anchor : '90%',
		maxLength : 6,
		maxLengthText : '最大长度不能超过6位数！'
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
			title : '系统积分设置',
			items : [
				asspoint_login,
				asspoint_machining_res,
				asspoint_machining_message 
			]
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
			url : getPath() + 'console/data/sysSetting.htm?action=getAssPointsettingInfo',
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					// 初始化Form
					settingForm.form.setValues({
						asspoint_login : txt.loginpoint,
						asspoint_machining_res : txt.machiningres,
						asspoint_machining_message : txt.machiningmessage
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
			url : getPath() + 'console/data/sysSetting.htm?action=saveAssPointSetting',
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