// -- 求助分类 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var isEditing = false;
	var addScIssueType_Win = null;
	var editScIssueType_Win = null;

	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/scIssueType.htm?action=getScIssueTypeList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'scitId',   
			type : 'int'
		}, {
			name : 'scitName',
			type : 'string'
		}, {
			name : 'scitIndex',
			type : 'string'
		}]),
		sortInfo : {
			field : 'scitIndex',
			direction : 'DESC'
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
		dataIndex : 'scitName',
		sortable : true
	}, {
		header : '排序',
		width : 200,
		dataIndex : 'scitIndex',
		sortable : true
	}]);
	var tb = new Ext.Toolbar([{
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : addScIssueType
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : editScIssueType
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : delScIssueType
	}, '-', {
		text : '&nbsp;刷&nbsp;新&nbsp;',
		iconCls : 'refresh',
		handler : searchMan
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
		autoExpandColumn : 'scitName',
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
	grid.on('dblclick', editScIssueType);
	// ----------------------- 新增 ----------------------------
	var addScIssueType_scitName = new Ext.form.TextField({
		fieldLabel : '分类名称',
		id : 'scitName',
		name : 'scitName',
		anchor : '90%',
		allowBlank : false,
		blankText : '分类名称不能为空！',
		maxLength : 40,
		maxLengthText : '分类名称不得超过40个字符!'
	});

	var addScIssueType_scitIndex = new Ext.form.NumberField({
		id : 'scitIndex',
		name : 'scitIndex',
		fieldLabel : '排序',
		allowDecimals : false, // 不允许输入小数
		nanText : '请输入有效整数', // 无效数字提示
		allowNegative : false,
		emptyText : 1,
		anchor : '90%',
		maxLength : 6,
		maxLengthText : '排序号最大长度不能超过6位数！'
	});

	var addScIssueType_Form = new Ext.form.FormPanel({
		id : 'addScIssueType_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [addScIssueType_scitName, addScIssueType_scitIndex],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveOrUpdateScIssueType
		}, {
			text : '取&nbsp;消',
			handler : function() {
				if(isEditing)
					editScIssueType_Win.hide();
				else
					addScIssueType_Win.hide();
			}
		}]
	});
	
	// ----------------------- 自定义函数 -----------------------
	// 新增语言
	function addScIssueType() {
		if(!addScIssueType_Win) {
			addScIssueType_Win = new Ext.Window({
				closeAction : 'hide',
				maximizable : false,
				resizable : false,
				width : 300,
				height : 145,
				title : '新增求助分类',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [addScIssueType_Form],
				listeners : {
					hide : function() {
						addScIssueType_Form.form.reset();
					}
				}
			});
		}
		addScIssueType_Win.add(addScIssueType_Form);
		addScIssueType_Win.show();
	}
	
	// 编辑语种
	function editScIssueType() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			isEditing = true;
			if(!editScIssueType_Win) {
				editScIssueType_Win = new Ext.Window({
					closeAction : 'hide',
					width : 300,
					height : 145,
					title : '编辑求助分类',
					layout : 'fit',
					modal : true,
					resizable : false,
					buttonAlign : 'center',
					items : [addScIssueType_Form],
					listeners : {
						hide : function() {
							isEditing = false;
							addScIssueType_Form.form.reset();
						}
					}
				});
			}
			editScIssueType_Win.add(addScIssueType_Form);
			initData(selectRow.json.scitName, selectRow.json.scitIndex);
			editScIssueType_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 删除分类
	function delScIssueType() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该求助分类吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var scitId = selectRow.json.scitId;
					scIssueTypeDelete(scitId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	
	
	// 操作信息分类数据
	function saveOrUpdateScIssueType() {
		if(addScIssueType_Form.form.isValid()) {
			addScIssueType_Form.getForm().submit({
				url : getPath() + 'console/data/scIssueType.htm?action=saveOrUpdateScIssueType',
				method : 'post',
				params : {
					isEditing : isEditing,
					scitId : isEditing == true ? grid.getSelectionModel().getSelected().json.scitId : 0
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					if(isEditing)
						editScIssueType_Win.hide();
					else
						addScIssueType_Win.hide();

				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	function initData(scitName, scitIndex) {
		addScIssueType_Form.getForm().findField('scitName').setValue(scitName);
		addScIssueType_Form.getForm().findField('scitIndex').setValue(scitIndex);
	}
	function scIssueTypeDelete(scitId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/scIssueType.htm?action=deleteScIssueType',
			params : {
				scitId : scitId
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