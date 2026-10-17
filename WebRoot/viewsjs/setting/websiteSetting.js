// 网站参数设置
Ext.onReady(function() {
	// 如果要体现FORM效验效果，必须添加下面两行代码
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';

	var defaultPageSize = new Ext.form.NumberField({
		id : 'default_page_size',
		name : 'default_page_size',
		fieldLabel : '列表默认显示条数(条)',
		allowDecimals : false, // 不允许输入小数
		nanText : '请输入有效整数', // 无效数字提示
		allowNegative : false,
		emptyText : 1,
		anchor : '90%',
		maxLength : 6,
		maxLengthText : '最大长度不能超过6位数！'
	});

	var defaultIssueAttarchementSize = new Ext.form.NumberField({
		id : 'default_issue_attachement_size',
		name : 'default_issue_attachement_size',
		fieldLabel : '求助回复附件默认大小(M)',
		allowDecimals : false, // 不允许输入小数
		nanText : '请输入有效整数', // 无效数字提示
		allowNegative : false,
		emptyText : 1,
		anchor : '90%',
		maxLength : 4,
		maxLengthText : '最大长度不能超过4位数！'
	});

	var riss2Website = new Ext.form.TextField({
		fieldLabel : 'RISS2域名设置',
		id : 'riss2_website',
		name : 'riss2_website',
		anchor : '90%',
		allowBlank : false,
		blankText : 'RISS2域名设置不能为空!',
		maxLength : 40,
		maxLengthText : 'RISS2域名设置不得超过40个字符!'
	});
	
	var webSiteFooter4zh = new Ext.form.TextField({
		fieldLabel : '简体中文版网站页脚设置',
		id : 'web_site_footer_zh',
		name : 'web_site_footer_zh',
		anchor : '90%',
		allowBlank : false,
		blankText : '简体中文版网站版权设置不能为空!',
		maxLength : 350,
		maxLengthText : '简体中文版网站版权设置不得超过350个字符!'
	});
	
	var webSiteFooter4en = new Ext.form.TextField({
		fieldLabel : '英文版网站页脚设置',
		id : 'web_site_footer_en',
		name : 'web_site_footer_en',
		anchor : '90%',
		allowBlank : false,
		blankText : '英文版网站版权设置不能为空!',
		maxLength : 350,
		maxLengthText : '英文版网站版权设置不得超过350个字符!'
	});
	
	var webSiteFooter4zw = new Ext.form.TextField({
		fieldLabel : '藏文版网站页脚设置',
		id : 'web_site_footer_zw',
		name : 'web_site_footer_zw',
		anchor : '90%',
		allowBlank : false,
		blankText : '藏文版网站版权设置不能为空!',
		maxLength : 350,
		maxLengthText : '藏文版网站版权设置不得超过350个字符!'
	});
	
	var hotresource_select_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[1, '最近一个月'], [3, '最近三个月'], [6, '最近半年']]
	});
	
	var riss2IndexHotResource = new Ext.form.ComboBox({
		fieldLabel : "热门资源设置",
		mode : 'local',
		store : hotresource_select_store,
		emptyText : '不限',
		hiddenName : 'riss2_index_hotresource',
		triggerAction : 'all',
		editable : false,
		valueField : 'value',
		displayField : 'text',
		anchor : '90%',
		value : 1
	});
	
	
	var allowUserUploadFileType_Picture = new Ext.form.TextField({
		fieldLabel : "允许会员上传的图片类型(多种类型用'|'分隔)",
		id : 'allow_user_upload_file_type_picture)',
		name : 'allow_user_upload_file_type_picture',
		anchor : '90%',
		allowBlank : false,
		blankText : '图片类型不能为空!',
		maxLength : 350,
		maxLengthText : '图片类型不得超过350个字符!'
	});
	
	var allowUserUploadFileType_Document = new Ext.form.TextField({
		fieldLabel : "允许会员上传的文档类型(多种类型用'|'分隔)",
		id : 'allow_user_upload_file_type_document)',
		name : 'allow_user_upload_file_type_document',
		anchor : '90%',
		allowBlank : false,
		blankText : '文档类型不能为空!',
		maxLength : 350,
		maxLengthText : '文档类型不得超过350个字符!'
	});
	
	var allowUserUploadFileType_Video = new Ext.form.TextField({
		fieldLabel : "允许会员上传的视频类型(多种类型用'|'分隔)",
		id : 'allow_user_upload_file_type_video)',
		name : 'allow_user_upload_file_type_video',
		anchor : '90%',
		allowBlank : false,
		blankText : '视频类型不能为空!',
		maxLength : 350,
		maxLengthText : '视频类型不得超过350个字符!'
	});
	
	var allowUserUploadFileType_Audieo = new Ext.form.TextField({
		fieldLabel : "允许会员上传的音频类型(多种类型用'|'分隔)",
		id : 'allow_user_upload_file_type_audieo',
		name : 'allow_user_upload_file_type_audieo',
		anchor : '90%',
		allowBlank : false,
		blankText : '音频类型不能为空!',
		maxLength : 350,
		maxLengthText : '音频类型不得超过350个字符!'
	});
	
	var unloginUserCanBrowseVideoNumber = new Ext.form.NumberField({
		id : 'unlogin_user_can_browse_video_number',
		name : 'unlogin_user_can_browse_video_number',
		fieldLabel : '游客每天最多浏览的视频数(个)',
		allowDecimals : false, // 不允许输入小数
		nanText : '请输入有效整数', // 无效数字提示
		allowNegative : false,
		emptyText : 1,
		anchor : '90%',
		maxLength : 6,
		maxLengthText : '最大长度不能超过6位数！'
	});
	
	
	var riss2DefaultSkinStore = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[1, '绿色风格'], [2, '蓝色风格'], [3, '粉红色风格']]
	});
	
	var riss2DefaultSkin = new Ext.form.ComboBox({
		fieldLabel : "默认皮肤设置",
		mode : 'local',
		store : riss2DefaultSkinStore,
		emptyText : '不限',
		hiddenName : 'riss2_default_skin',
		triggerAction : 'all',
		editable : false,
		valueField : 'value',
		displayField : 'text',
		anchor : '90%',
		value : 2
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
			title : '网站参数设置',
			items : [riss2DefaultSkin,riss2IndexHotResource, defaultPageSize, unloginUserCanBrowseVideoNumber, defaultIssueAttarchementSize,
					riss2Website,allowUserUploadFileType_Picture,allowUserUploadFileType_Document,allowUserUploadFileType_Video,
					allowUserUploadFileType_Audieo,webSiteFooter4zh,webSiteFooter4en,webSiteFooter4zw]
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
			url : getPath() + 'console/data/sysSetting.htm?action=getWebsiteSettingInfo',
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					// 初始化Form
					settingForm.form.setValues({
						default_page_size : txt.defaultPageSize,
						default_issue_attachement_size : txt.defaultIssueAttarchementSize,
						riss2_website : txt.riss2Website,
						web_site_footer_zh : txt.webSiteFooter4zh,
						web_site_footer_en : txt.webSiteFooter4en,
						web_site_footer_zw : txt.webSiteFooter4zw,
						riss2_index_hotresource : txt.riss2IndexHotResource,
						unlogin_user_can_browse_video_number : txt.unloginUserCanBrowseVideoNumber,
						allow_user_upload_file_type_picture : txt.allowUserUploadFileType_Picture,
						allow_user_upload_file_type_video : txt.allowUserUploadFileType_Video,
						allow_user_upload_file_type_document : txt.allowUserUploadFileType_Document,
						allow_user_upload_file_type_audieo : txt.allowUserUploadFileType_Audieo,
						riss2_default_skin : txt.riss2DefaultSkin
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
			url : getPath() + 'console/data/sysSetting.htm?action=saveWebsiteSetting',
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