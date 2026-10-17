// -- 求助管理 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var scitId_Combox;
	var scisState_Combox;
	var scisTitle_Condition;
	var sc_Win;

	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/scIssue.htm?action=getScIssueList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'scisId',
			type : 'int'
		}, {
			name : 'scisTitle',
			type : 'string'
		}, {
			name : 'scitName',
			type : 'string'
		}, {
			name : 'suinName',
			type : 'string'
		}, {
			name : 'scisCreationTime',
			type : 'string'
		}, {
			name : 'scisState',
			type : 'string'
		}, {
			name : 'scisReplyNumber',
			type : 'string'
		}, {
			name : 'scisIsuse',
			type : 'string'
		}, {
			name : 'scisLastReplyTime',
			type : 'string'
		}]),
		sortInfo : {
			field : 'scisCreationTime',
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
		header : '标题',
		width : 200,
		dataIndex : 'scisTitle',
		sortable : true
	}, {
		header : '分类',
		width : 100,
		dataIndex : 'scitName',
		sortable : true
	}, {
		header : '求助人',
		width : 100,
		dataIndex : 'suinName',
		sortable : true
	}, {
		header : '求助时间',
		width : 100,
		dataIndex : 'scisCreationTime',
		sortable : true
	}, {
		header : '最后更新时间',
		width : 100,
		dataIndex : 'scisLastReplyTime',
		sortable : true
	}, {
		header : '回复数',
		width : 50,
		dataIndex : 'scisReplyNumber',
		align : 'center',
		sortable : true
	}, {
		header : '状态',
		width : 50,
		dataIndex : 'scisState',
		sortable : true,
		align : 'center',
		renderer : checkState
	}, {
		header : '是否可用',
		width : 50,
		dataIndex : 'scisIsuse',
		sortable : true,
		align : 'center',
		renderer : checkIsuse
	}]);
	// --查询条件:标题
	var scisTitle_search = new Ext.form.TextField({
		name : 'scisTitle_condition',
		width : 180,
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
	});
	// 类型
	var scit_Store = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/scIssue.htm?action=getScIssueTypeCombox'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'value'
		}, {
			name : 'text'
		}])
	});
	var scit_Combox = new Ext.form.ComboBox({
		id : 'scit_Combox',
		fieldLabel : "求助类型",
		mode : 'remote',
		store : scit_Store,
		emptyText : '请选择求助类型',
		triggerAction : 'all',
		lazyInit : true,
		resizable : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		width : 120,
		listeners : {
			'select' : function() {
				searchMan();
			}
		}
	});
	// 求助状态
	var scIssue_status_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[-1, '不限'], [1, '待解决'], [2, '已解决'], [3, '关闭']]
	});
	var scIssue_status_Combox = new Ext.form.ComboBox({
		fieldLabel : "状态",
		mode : 'local',
		store : scIssue_status_store,
		emptyText : '不限',
		triggerAction : 'all',
		editable : false,
		valueField : 'value',
		displayField : 'text',
		width : 120,
		value : -1,
		listeners : {
			'select' : function() {
				searchMan();
			}
		}
	});

	var tb = new Ext.Toolbar(['标题：', scisTitle_search, '-', '类型：', scit_Combox, '-', '状态：', scIssue_status_Combox, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}]);

	var tbar1 = new Ext.Toolbar([{
		text : '&nbsp;查&nbsp;看&nbsp;',
		iconCls : 'common_details',
		handler : sc
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : delScIssueType
	}, '-', {
		text : '&nbsp;添加到FAQ库&nbsp;',
		iconCls : 'common_add',
		handler : addToFaqLibrary
	}, '-', {
		text : '&nbsp;启用开关&nbsp;',
		iconCls : 'common_shutdown',
		handler : isOpen
	}]);

	// 创建一个分页控件
	var pagingBar = new Ext.PagingToolbar({
		pageSize : limitLength,
		store : store,
		displayInfo : true,
		displayMsg : pagingDisplayMsg,
		emptyMsg : '没有记录',
		plugins : new Ext.ux.ProgressBarPager(),
		items : [{
			xtype : 'label',
			style : 'padding-left: 10px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls : 'sc_not_solve',
			width : 16
		}, {
			xtype : 'label',
			text : '待解决',
			style : 'padding-left: 3px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 15px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls : 'sc_solve',
			width : 16
		}, {
			xtype : 'label',
			text : '已解决',
			style : 'padding-left: 3px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 15px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls : 'sc_close',
			width : 16
		}, {
			xtype : 'label',
			text : '关闭',
			style : 'padding-left: 3px;',
			width : 10
		}]
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
		autoExpandColumn : 'scisTitle',
		tbar : tb,
		bbar : pagingBar,
		listeners : {
			'render' : function() {
				tbar1.render(this.tbar);
			}
		}
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
	grid.on('dblclick', sc);
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			scitId_Combox : scitId_Combox,
			scisState_Combox : scisState_Combox,
			scisTitle_Condition : scisTitle_Condition
		});
	});
	// -------------------- 查看求助 --------------------------
	var sc_scisTitle = new Ext.form.TextField({
		fieldLabel : '标题',
		id : 'scisTitle',
		name : 'scisTitle',
		anchor : '95%',
		readOnly : true
	});
	var sc_scitName = new Ext.form.TextField({
		fieldLabel : '分类',
		id : 'scitName',
		name : 'scitName',
		anchor : '95%',
		readOnly : true
	});
	var sc_suinName = new Ext.form.TextField({
		fieldLabel : '求助人',
		id : 'suinName',
		name : 'suinName',
		anchor : '95%',
		readOnly : true
	});
	var sc_scisCreationTime = new Ext.form.TextField({
		fieldLabel : '发布时间',
		id : 'scisCreationTime',
		name : 'scisCreationTime',
		anchor : '95%',
		readOnly : true
	});
	var sc_scisState = new Ext.form.TextField({
		fieldLabel : '状态',
		id : 'scisState',
		name : 'scisState',
		anchor : '95%',
		readOnly : true
	});
	var sc_scisIsuse = new Ext.form.TextField({
		fieldLabel : '是否可用',
		id : 'scisIsuse',
		name : 'scisIsuse',
		anchor : '95%',
		readOnly : true
	});
	var sc_scisContent = new Ext.form.TextArea({
		fieldLabel : '内容',
		id : 'scisContent',
		name : 'scisContent',
		anchor : '95%',
		readOnly : true,
		height : 100
	});

	var sc_scirContent = new Ext.form.TextArea({
		fieldLabel : '采纳的回复',
		id : 'scirContent',
		name : 'scirContent',
		anchor : '95%',
		readOnly : true,
		height : 100
	});

	var sc_Form = new Ext.form.FormPanel({
		id : 'sc_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [sc_scisTitle, sc_scitName, sc_suinName, sc_scisCreationTime, sc_scisState, sc_scisIsuse, sc_scisContent, sc_scirContent],
		buttons : [{
			text : '删&nbsp;除',
			handler : delScIssueType
		}, {
			text : '&nbsp;添加到FAQ库&nbsp;',
			handler : addToFaqLibrary
		}, {
			text : '&nbsp;返&nbsp;回&nbsp;',
			handler : function() {
				sc_Win.hide();
			}
		}]
	});

	// -------------------- 自定义函数 -------------------------

	function sc() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			if(!sc_Win) {
				sc_Win = new Ext.Window({
					closeAction : 'hide',
					maximizable : true,
					resizable : false,
					width : 550,
					height : 450,
					title : '求助信息',
					layout : 'fit',
					modal : true,
					buttonAlign : 'center',
					items : [sc_Form],
					listeners : {
						beforehide : function() {
							sc_Form.form.reset();
						}
					}
				});
			}
			initData(selectRow.json.scisId);
			sc_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}

	// 状态
	function checkState(val) {
		if(val == '1')
			return '<img src="' + getPath() + 'images/sc/sc_not_solve.png" title="待解决"  />';
		else if(val == '2')
			return '<img src="' + getPath() + 'images/sc/sc_solve.png" title="已解决"  />';
		else if(val == '3') return '<img src="' + getPath() + 'images/sc/sc_close.png" title="关闭"  />';
	}
	// 开关
	function checkIsuse(val) {
		if(val == 'true')
			return '<img src="' + getPath() + 'images/icon/yes.png" title="可用"  />';
		else
			return '<img src="' + getPath() + 'images/icon/no.png" title="禁用"  />';
	}

	function searchMan() {
		scitId_Combox = scit_Combox.getValue();
		scisState_Combox = scIssue_status_Combox.getValue();
		scisTitle_Condition = scisTitle_search.getValue();
		store.load({
			params : {
				start : 0,
				limit : limitLength,
				scitId_Combox : scitId_Combox,
				scisState_Combox : scisState_Combox,
				scisTitle_Condition : scisTitle_Condition
			}
		});
	}

	function initData(scisId) {
		Ext.Ajax.request({
			url : getPath() + 'console/scIssue.htm?action=getScIssueDetail',
			params : {
				scisId : scisId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					sc_Form.form.setValues({
						scisTitle : txt.scisTitle,
						scitName : txt.scitName,
						suinName : txt.suinName,
						scisCreationTime : txt.creationTime,
						scisState : txt.scisState,
						scisIsuse : txt.scisIsuse,
						scisContent : txt.scisContent,
						scirContent : txt.scirContent
					});
				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统出现未知错误！');
			}
		});
	}

	// 删除
	function delScIssueType() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该求助吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var scisId = selectRow.json.scisId;
					if(sc_Win) sc_Win.hide();
					scIssueDelete(scisId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}

	function scIssueDelete(scisId) {
		Ext.Ajax.request({
			url : getPath() + 'console/scIssue.htm?action=deleteScIssue',
			params : {
				scisId : scisId
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
	// 启用开关
	function isOpen() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var scisId = selectRow.json.scisId;
			var scisIsuse = selectRow.json.scisIsuse;
			openScIssue(scisId, scisIsuse);
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}

	function openScIssue(scisId, scisIsuse) {
		Ext.Ajax.request({
			url : getPath() + 'console/scIssue.htm?action=openScIssue',
			params : {
				scisId : scisId,
				scisIsuse : scisIsuse
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
	// ----------------------- 添加到FAQ库 ---------------------------------
	function addToFaqLibrary() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var scisState = selectRow.json.scisState;
			if(scisState == '1') {
				alertWarring('该求助还未解决，不能添加到FAQ库！');
				return;
			}
			var scisId = selectRow.json.scisId;
			addFaqLibrary();
			// saveFaq(scisId);//scisTitle scitName scisContent scirContent

		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 类型
	var scft_Store = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/scFaqLibrary.htm?action=getScFaqTypeCombox'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'value'
		}, {
			name : 'text'
		}])
	});
	var scft_Combox = new Ext.form.ComboBox({
		id : 'scft_Combox',
		fieldLabel : "分类",
		mode : 'remote',
		store : scft_Store,
		emptyText : '请选择分类',
		triggerAction : 'all',
		hiddenName : 'scftId',
		lazyInit : true,
		resizable : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		anchor : '95%',
		allowBlank : false,
		blankText : '分类不能为空！'
	});

	var addFaqLibrary_scflQuestion = new Ext.form.TextArea({
		fieldLabel : '问题',
		id : 'scflQuestion',
		name : 'scflQuestion',
		anchor : '95%',
		height : 80,
		allowBlank : false,
		blankText : '问题不能为空！',
		maxLength : 1024,
		maxLengthText : '问题不得超过1024个字符!'
	});

	var addFaqLibrary_scflAnswer = new Ext.form.TextArea({
		fieldLabel : '回答',
		id : 'scflAnswer',
		name : 'scflAnswer',
		anchor : '95%',
		height : 140,
		allowBlank : false,
		blankText : '回答不能为空！',
		maxLength : 1024,
		maxLengthText : '回答不得超过1024个字符!'
	});

	var addFaqLibrary_Form = new Ext.form.FormPanel({
		id : 'addFaqLibrary_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 50,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [addFaqLibrary_scflQuestion, scft_Combox, addFaqLibrary_scflAnswer],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveOrUpdateFaqLibrary
		}, {
			text : '取&nbsp;消',
			handler : function() {
				addFaqLibrary_Win.hide();
			}
		}, {
			text : '返&nbsp;回',
			handler : function() {
				addFaqLibrary_Win.hide();
			}
		}]
	});
	var addFaqLibrary_Win;
	function addFaqLibrary() {
		if(!addFaqLibrary_Win) {
			addFaqLibrary_Win = new Ext.Window({
				closeAction : 'hide',
				maximizable : false,
				resizable : false,
				width : 500,
				height : 350,
				title : '添加求助到FAQ库',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [addFaqLibrary_Form],
				listeners : {
					hide : function() {
						addFaqLibrary_Form.form.reset();
					}
				}
			});
		}
		initFaqData(grid.getSelectionModel().getSelected().json.scisId);
		addFaqLibrary_Win.show();
	}
	function initFaqData(scisId) {
		Ext.Ajax.request({
			url : getPath() + 'console/scIssue.htm?action=getScIssueDetail',
			params : {
				scisId : scisId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					addFaqLibrary_Form.form.setValues({
						scflQuestion : txt.scisTitle + '\n' + txt.scisContent,
						scflAnswer : txt.scirContent,
						scftId : txt.scitName
					});
				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统出现未知错误！');
			}
		});
	}

	function saveOrUpdateFaqLibrary() {
		if(addFaqLibrary_Form.form.isValid()) {
			addFaqLibrary_Form.getForm().submit({
				url : getPath() + 'console/scIssue.htm?action=addToFaqLibrary',
				method : 'post',
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					addFaqLibrary_Win.hide();
					// 隐蔽编辑窗口
					if(sc_Win) sc_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}

});