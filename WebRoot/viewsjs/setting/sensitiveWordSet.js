// -- 敏感词设置 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var sswAdd_Win;
	var sswEdit_Win;
	var sswName_Condition;
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/sysSensitiveWordSet.htm?action=getSSWList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'sswId',
			type : 'string'
		}, {
			name : 'sswName',
			type : 'string'
		}, {
			name : 'sswIndex',
			type : 'string'
		}]),
		sortInfo : {
			field : 'sswIndex',
			direction : 'ASC'
		},
		remoteStore : true, // 是否远程调用数据
		// 是否远程排序
		remoteSort : true
	});
	// 创建选择模式对象
	var cb = new Ext.grid.CheckboxSelectionModel({
		singleSelect : true
	});
	// 定义列模型
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '敏感词',
		width : 500,
		dataIndex : 'sswName',
		sortable : true
	}, {
		header : '序号',
		width : 100,
		dataIndex : 'sswIndex',
		sortable : true
	}]);
	// 查询
	var searchMan = function() {
		sswName_Condition = sswName_search.getValue();
		store.reload({
			params : {
				start : 0,
				limit : limitLength,
				sswName : sswName_Condition
			}
		});
	}

	// --查询条件:敏感词
	var sswName_search = new Ext.form.TextField({
		width : 200,
		emptyText : '请输入敏感词...',
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
	});

	// 创建工具条
	var tbar = new Ext.Toolbar(['敏感词：', sswName_search, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : sswAdd
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : sswEdit
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : sswDelete
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
		sm : cb,
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
		autoExpandColumn : 'sswIndex',
		enableColumnHide : false,
		tbar : tbar,
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
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			sswName : sswName_Condition
		});
	});
	grid.on('dblclick', sswEdit);
	// ----------------------- 新增信息 ----------------------------
	var sswName_Add = new Ext.form.TextField({
		fieldLabel : '敏感词',
		id : 'sswName_Add',
		name : 'sswName_Add',
		anchor : '90%',
		allowBlank : false,
		blankText : '敏感词不能为空!',
		maxLength : 256,
		maxLengthText : '敏感词不得超过256个字符!'
	});
	var sswIndex_Add = new Ext.form.TextField({
		fieldLabel : '序号',
		id : 'sswIndex_Add',
		name : 'sswIndex_Add',
		anchor : '90%',
		allowBlank : false,
		blankText : '序号不能为空!',
		maxLength : 9,
		validator:function check(){
        	if(/^[0-9]*$/.test(Ext.getCmp('sswIndex_Add').getValue())){
        		return true;
        	}else{
        		return "只能输入数字";
        	}
        }
	});
	var sswAdd_Form = new Ext.form.FormPanel({
		id : 'sswAdd_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 100,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [sswName_Add, sswIndex_Add],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveSsw
		}, {
			text : '取&nbsp;消',
			handler : function() {
				sswAdd_Win.hide();
			}
		}]
	});
	// ----------------------- 编辑信息 ----------------------------
	var sswId_Edit = new Ext.form.TextField({
		fieldLabel : '敏感词ID',
		id : 'sswId_Edit',
		name : 'sswId_Edit',
		anchor : '90%',
		allowBlank : false,
		hidden : true,
		hideLabel : true
	});
	var sswName_Edit = new Ext.form.TextField({
		fieldLabel : '敏感词',
		id : 'sswName_Edit',
		name : 'sswName_Edit',
		anchor : '90%',
		allowBlank : false,
		blankText : '敏感词不能为空!',
		maxLength : 256,
		maxLengthText : '敏感词不得超过256个字符!'
	});
	var sswIndex_Edit = new Ext.form.TextField({
		fieldLabel : '序号',
		id : 'sswIndex_Edit',
		name : 'sswIndex_Edit',
		anchor : '90%',
		allowBlank : false,
		blankText : '序号不能为空!',
		maxLength : 9,
		validator:function check(){
        	if(/^[0-9]*$/.test(Ext.getCmp('sswIndex_Edit').getValue())){
        		return true;
        	}else{
        		return "只能输入数字";
        	}
        }
	});

	var sswEdit_Form = new Ext.form.FormPanel({
		id : 'sswEdit_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [sswId_Edit, sswName_Edit, sswIndex_Edit],
		buttons : [{
			text : '保&nbsp;存',
			handler : updateSsw
		}, {
			text : '取&nbsp;消',
			handler : function() {
				sswEdit_Win.hide();
			}
		}]
	});
	// ------------------ 自定义函数 -------------------------------
	// 新增
	function sswAdd() {
		if(!sswAdd_Win) {
			sswAdd_Win = new Ext.Window({
				closeAction : 'hide',
				//maximizable : false,
				//maximizable : true,
				resizable : false,
				width : 400,
				height : 150,
				title : '新增敏感词',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [sswAdd_Form],
				listeners : {
					hide : function() {
						sswAdd_Form.form.reset();
					}
				}
			});
		}
		sswAdd_Win.show();
	}

	// 编辑
	function sswEdit() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			if(!sswEdit_Win) {
				sswEdit_Win = new Ext.Window({
					closeAction : 'hide',
					//maximizable : false,
					//maximizable : true,
					resizable : false,
					width : 400,
					height : 150,
					title : '编辑敏感词',
					layout : 'fit',
					modal : true,
					buttonAlign : 'center',
					items : [sswEdit_Form],
					listeners : {
						hide : function() {
							sswEdit_Form.form.reset();
						}
					}
				});
			}
			sswEdit_Form.form.setValues({
						sswId_Edit : selectRow.json.sswId,
						sswName_Edit : selectRow.json.sswName,
						sswIndex_Edit : selectRow.json.sswIndex
					});
			sswEdit_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 保存
	function saveSsw() {
		if(sswAdd_Form.form.isValid()) {
			sswAdd_Form.getForm().submit({
				url : getPath() + 'console/data/sysSensitiveWordSet.htm?action=saveSsw',
				method : 'post',
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					sswAdd_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	// 更新信息
	function updateSsw() {
		if(sswEdit_Form.form.isValid()) {
			sswEdit_Form.getForm().submit({
				url : getPath() + 'console/data/sysSensitiveWordSet.htm?action=updateSsw',
				method : 'post',
				params : {
					sswId : grid.getSelectionModel().getSelected().json.sswId
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					sswEdit_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	function sswDelete() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该敏感词吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var sswId = selectRow.json.sswId;
					sswDel(sswId);
				}
			});
		}else
			alertWarring('请选择要删除的一行数据操作！');
	}
	// 删除
	function sswDel(sswId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/sysSensitiveWordSet.htm?action=delSsw',
			params : {
				sswId : sswId
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
				alertError('系统出现未知错误，删除失败！');
			}
		});
	}
}
);