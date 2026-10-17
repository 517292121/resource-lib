// -- FAQ维护 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var isEditing = false;
	var addFaqLibrary_Win = null;
	var editFaqLibrary_Win = null;

	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/scFaqLibrary.htm?action=getScFaqLibraryList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'scflId',
			type : 'int'
		}, {
			name : 'scftName',
			type : 'string'
		}, {
			name : 'scflQuestion',
			type : 'string'
		}, {
			name : 'scflTopTime',
			type : 'string'
		}, {
			name : 'scflIstop',
			type : 'string'
		}]),
		sortInfo : {
			field : 'scflIstop',
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
	// scflId scftName scflQuestion scflTopTime scflIstop
	// 定义列模型
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '问题',
		width : 300,
		dataIndex : 'scflQuestion',
		sortable : true
	}, {
		header : '分类',
		width : 200,
		dataIndex : 'scftName',
		sortable : true
	}, {
		header : '置顶时间',
		width : 100,
		dataIndex : 'scflTopTime',
		sortable : true
	}, {
		header : '置顶',
		width : 60,
		dataIndex : 'scflIstop',
		sortable : true,
		align : 'center',
		renderer : checkIsuse
	}]);
	var tb = new Ext.Toolbar([{
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : addFaqLibrary
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : editFaqLibrary
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : delScFaqLibrary
	}, '-', {
		text : '&nbsp;置顶开关&nbsp;',
		iconCls : 'common_shutdown',
		handler : toTop
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
		autoExpandColumn : 'scflQuestion',
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
	grid.on('dblclick', editFaqLibrary);
	// ----------------------- 新增 ----------------------------
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
				if(isEditing)
					editFaqLibrary_Win.hide();
				else
					addFaqLibrary_Win.hide();
			}
		}]
	});

	// ----------------------- 自定义函数 -----------------------
	// 新增
	function addFaqLibrary() {
		if(!addFaqLibrary_Win) {
			addFaqLibrary_Win = new Ext.Window({
				closeAction : 'hide',
				maximizable : false,
				resizable : false,
				width : 500,
				height : 350,
				title : '新增FAQ',
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
		addFaqLibrary_Win.add(addFaqLibrary_Form);
		addFaqLibrary_Win.show();
	}

	// 编辑
	function editFaqLibrary() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			isEditing = true;
			if(!editFaqLibrary_Win) {
				editFaqLibrary_Win = new Ext.Window({
					closeAction : 'hide',
					width : 500,
					height : 350,
					title : '编辑FAQ',
					layout : 'fit',
					modal : true,
					resizable : false,
					buttonAlign : 'center',
					items : [addFaqLibrary_Form],
					listeners : {
						hide : function() {
							isEditing = false;
							addFaqLibrary_Form.form.reset();
						}
					}
				});
			}
			editFaqLibrary_Win.add(addFaqLibrary_Form);
			initData(selectRow.json.scflId);
			editFaqLibrary_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 删除分类
	function delScFaqLibrary() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该问题吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var scflId = selectRow.json.scflId;
					faqLibraryDelete(scflId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}

	// 操作数据
	function saveOrUpdateFaqLibrary() {
		if(addFaqLibrary_Form.form.isValid()) {
			addFaqLibrary_Form.getForm().submit({
				url : getPath() + 'console/data/scFaqLibrary.htm?action=saveOrUpdateFaqLibrary',
				method : 'post',
				params : {
					isEditing : isEditing,
					scflId : isEditing == true ? grid.getSelectionModel().getSelected().json.scflId : 0
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					if(isEditing)
						editFaqLibrary_Win.hide();
					else
						addFaqLibrary_Win.hide();

				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	function initData(scflId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/scFaqLibrary.htm?action=getScFaqLibraryDetail',
			params : {
				scflId : scflId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					scft_Store.reload();
					addFaqLibrary_Form.getForm().findField('scflAnswer').setValue(txt.scflAnswer);
					addFaqLibrary_Form.getForm().findField('scflQuestion').setValue(txt.scflQuestion);
					addFaqLibrary_Form.getForm().findField('scftId').setValue(txt.scftId);
				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统忙，请稍后访问！');
			}
		});
	}
	function faqLibraryDelete(scflId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/scFaqLibrary.htm?action=deleteFaqLibrary',
			params : {
				scflId : scflId
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
	function toTop() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var scflId = selectRow.json.scflId;
			var scflIstop = selectRow.json.scflIstop;
			goToTop(scflId,scflIstop);
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	function goToTop(scflId,scflIstop){
		Ext.Ajax.request({
			url : getPath() + 'console/data/scFaqLibrary.htm?action=faqLibraryToTop',
			params : {
				scflId : scflId,
				scflIstop : scflIstop
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
	// 开关
	function checkIsuse(val) {
		if(val == 'true')
			return '<img src="' + getPath() + 'images/icon/yes.png" title="置顶"  />';
		else
			return '<img src="' + getPath() + 'images/icon/no.png" title="正常"  />';
	}

});