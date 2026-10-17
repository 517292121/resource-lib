// -- 语种分类 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var addLanguage_Win = null;
	var editLanguage_Win = null;
	var isEditing = false;

	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/resLanguageType.htm?action=getResLanguageTypeList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rltyId',
			type : 'int'
		}, {
			name : 'rltyName',
			type : 'string'
		}]),
		sortInfo : {
			field : 'rltyId',
			direction : 'ASC'
		},
		remoteStore : true, // 是否远程调用数据
		// 是否远程排序
		remoteSort : true
	});
	// 创建单选模式
	var sm = new Ext.grid.RowSelectionModel({
		singleSelect : true
	});
	// 定义列模型
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '名称',
		width : 200,
		dataIndex : 'rltyName',
		sortable : true
	}]);

	var tb = new Ext.Toolbar([{
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : addLanguage
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : editLanguage
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : delLanguage
	}]);

	// 创建一个分页控件
	var pagingBar = new Ext.PagingToolbar({
		pageSize : limitLength,
		store : store,
		displayInfo : true,
		displayMsg : pagingDisplayMsg,
		emptyMsg : '没有记录',
		plugins : new Ext.ux.ProgressBarPager()
	});
	// 组合表格
	var grid = new Ext.grid.GridPanel({
		region : 'center',
		el : grid,
		cm : cm,
		sm : sm,
		store : store,
		viewConfig : {
			forceFit : true
		},
		border : false,
		loadMask : true, // 加载中显示一个读取中的覆盖提示
		frame : false,
		stripeRows : true, // 行分割符
		columnLines : true, // True表示为在列分隔处显示分隔符
		autoScroll : true,
		enableColumnHide : false,
		autoExpandColumn : 'rltyName',
		tbar : tb,
		bbar : pagingBar
	});
	// 创建主面板
	var displayPanel = new Ext.Viewport({
		layout : 'border',
		items : [grid]
	});
	// 加载数据
	store.load({
		params : {
			start : 0,
			limit : limitLength
		}
	});
	grid.on('dblclick', editLanguage);
	// ----------------------- 新增 ----------------------------
	var addLanguage_RltyName = new Ext.form.TextField({
		fieldLabel : '语言名称',
		id : 'rltyName',
		name : 'rltyName',
		anchor : '90%',
		allowBlank : false,
		blankText : '语言名称不能为空！',
		maxLength : 20,
		maxLengthText : '语言名称不得超过20个字符!'
	});
	var addLanguage_Form = new Ext.form.FormPanel({
		id : 'addLanguage_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [addLanguage_RltyName],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveOrUpdateLanguageType
		}, {
			text : '取&nbsp;消',
			handler : function() {
				if(isEditing)
					editLanguage_Win.hide();
				else
					addLanguage_Win.hide();
			}
		}]
	});

	// ----------------------- 自定义函数 -----------------------
	// 新增语言
	function addLanguage() {
		if(!addLanguage_Win) {
			addLanguage_Win = new Ext.Window({
				closeAction : 'hide',
				maximizable : false,
				resizable : false,
				width : 300,
				height : 120,
				title : '新增语种',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [addLanguage_Form],
				listeners : {
					hide : function() {
						addLanguage_Form.form.reset();
						addLanguage_RltyName.setValue(' ');
					}
				}
			});
		}
		addLanguage_Win.add(addLanguage_Form);
		addLanguage_Win.show();
	}
	// 编辑语种
	function editLanguage() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			isEditing = true;
			if(!editLanguage_Win) {
				editLanguage_Win = new Ext.Window({
					closeAction : 'hide',
					width : 300,
					height : 120,
					title : '编辑语种',
					layout : 'fit',
					modal : true,
					resizable : false,
					buttonAlign : 'center',
					items : [addLanguage_Form],
					listeners : {
						hide : function() {
							isEditing = false;
							addLanguage_Form.form.reset();
							addLanguage_RltyName.setValue(' ');
						}
					}
				});
			}
			editLanguage_Win.add(addLanguage_Form);
			initRltyData(selectRow.json.rltyName);
			editLanguage_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 删除语种
	function delLanguage() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该语种吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var rltyId = selectRow.json.rltyId;
					languageDelete(rltyId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 操作语种数据
	function saveOrUpdateLanguageType() {
		if(addLanguage_Form.form.isValid()) {
			addLanguage_Form.getForm().submit({
				url : getPath() + 'console/data/resLanguageType.htm?action=saveOrUpdateResLanguageType',
				method : 'post',
				params : {
					isEditing : isEditing,
					rltyId : isEditing == true ? grid.getSelectionModel().getSelected().json.rltyId : 0
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					if(isEditing)
						editLanguage_Win.hide();
					else
						addLanguage_Win.hide();

				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	function initRltyData(rltyName) {
		addLanguage_Form.getForm().findField('rltyName').setValue(rltyName);
	}
	function languageDelete(rltyId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/resLanguageType.htm?action=delResLanguageType',
			params : {
				rltyId : rltyId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					alertSuccess(txt.msg);
					searchMan();
				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统忙，请稍后访问！');
			}
		});

	}
	function searchMan() {
		store.load({
			params : {
				start : 0,
				limit : limitLength
			}
		});
	}
});