// -- FAQ维护 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var isEditing = false;
	var addIndexKey_Win = null;
	var editIndexKey_Win = null;

	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/resIndexKey.htm?action=getIndexKeyList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rikeId',
			type : 'int'
		}, {
			name : 'rikeName',
			type : 'string'
		}, {
			name : 'rikeSearchCount',
			type : 'int'
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

	// 定义列模型
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '检索词',
		width : 300,
		dataIndex : 'rikeName',
		sortable : true
	}, {
		header : '检索量',
		width : 200,
		dataIndex : 'rikeSearchCount',
		sortable : true
	}]);
	
	// --查询条件:标题
	var rikeName_search = new Ext.form.TextField({
		name : 'rikeName_search',
		width : 100,
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
	});
	var tb = new Ext.Toolbar([
		'检索词：', rikeName_search, '-', 
      {
      	text : '&nbsp;查&nbsp;询&nbsp;',
      	iconCls : 'common_search',
      	handler : searchMan
      }, '-', {
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : addIndexKey
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : editIndexKey
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : delIndexKey
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
		autoExpandColumn : 'rikeName',
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
	grid.on('dblclick', editIndexKey);
	
	
	// ----------------------- 新增 ----------------------------

	var addIndexKey_rikeName = new Ext.form.TextArea({
		fieldLabel : '检索词',
		id : 'rikeName',
		name : 'rikeName',
		anchor : '95%',
		height : 40,
		allowBlank : false,
		blankText : '检索词不能为空！',
		maxLength : 30,
		maxLengthText : '检索词不得超过30个字符!'
	});

	var addIndexKey_rikeSearchCount = new Ext.form.NumberField({
		fieldLabel : '检索量',
		id : 'rikeSearchCount',
		name : 'rikeSearchCount',
		anchor : '95%',
		height : 25,
		allowDecimals:false,
		maxValue:1000000000,//最大值  
		maxText:"检索量不能大于1000000000",
		allowNegative:false,//不能输入负数
		nanText:'请输入有效的整数'//无效数字提示 
	});

	var addIndexKey_Form = new Ext.form.FormPanel({
		id : 'addIndexKey_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 50,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [addIndexKey_rikeName,addIndexKey_rikeSearchCount],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveOrUpdateIndexKey
		}, {
			text : '取&nbsp;消',
			handler : function() {
				if(isEditing)
					editIndexKey_Win.hide();
				else
					addIndexKey_Win.hide();
			}
		}]
	});
	
	// ----------------------- 自定义函数 -----------------------
	// 新增
	function addIndexKey() {
		if(!addIndexKey_Win) {
			addIndexKey_Win = new Ext.Window({
				closeAction : 'hide',
				maximizable : false,
				resizable : false,
				width : 300,
				height : 180,
				title : '新增检索词',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [addIndexKey_Form],
				listeners : {
					hide : function() {
						addIndexKey_Form.form.reset();
					}
				}
			});
		}
		addIndexKey_Win.add(addIndexKey_Form);
		addIndexKey_Win.show();
	}

	// 编辑
	function editIndexKey() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			isEditing = true;
			if(!editIndexKey_Win) {
				editIndexKey_Win = new Ext.Window({
					closeAction : 'hide',
					width : 300,
					height : 180,
					title : '编辑检索词',
					layout : 'fit',
					modal : true,
					resizable : false,
					buttonAlign : 'center',
					items : [addIndexKey_Form],
					listeners : {
						hide : function() {
							isEditing = false;
							addIndexKey_Form.form.reset();
						}
					}
				});
			}
			editIndexKey_Win.add(addIndexKey_Form);
			initData(selectRow.json.rikeId);
			editIndexKey_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 删除关键词信息
	function delIndexKey() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该检索词吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var rikeId = selectRow.json.rikeId;
					deleteIndexKey(rikeId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}

	// 操作数据
	function saveOrUpdateIndexKey() {
		if(addIndexKey_Form.form.isValid()) {
			addIndexKey_Form.getForm().submit({
				url : getPath() + 'console/data/resIndexKey.htm?action=saveOrUpdateIndexKey',
				method : 'post',
				params : {
					isEditing : isEditing,
					rikeId : isEditing == true ? grid.getSelectionModel().getSelected().json.rikeId : 0
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					if(isEditing)
						editIndexKey_Win.hide();
					else
						addIndexKey_Win.hide();

				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	function initData(rikeId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/resIndexKey.htm?action=getIndexKeyDetail',
			params : {
				rikeId : rikeId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					addIndexKey_Form.getForm().findField('rikeName').setValue(txt.rikeName);
					addIndexKey_Form.getForm().findField('rikeSearchCount').setValue(txt.rikeSearchCount);
				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统忙，请稍后访问！');
			}
		});
	}
	//删除关键词信息
	function deleteIndexKey(rikeId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/resIndexKey.htm?action=delIndexKey',
			params : {
				rikeId : rikeId
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
		rikeName_Condition = rikeName_search.getValue();
		store.load({
			params : {
				start : 0,
				limit : limitLength,
				rikeName : rikeName_Condition
			}
		});
	}

});