// -- 馆藏位置设置 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var scpAdd_Win;
	var scpEdit_Win;
	var scpName_Condition;
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/sysCollectionPositionSet.htm?action=getSCPList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'scpId',
			type : 'string'
		}, {
			name : 'scpName',
			type : 'string'
		}, {
			name : 'scpIndex',
			type : 'string'
		}]),
		sortInfo : {
			field : 'scpIndex',
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
		header : '馆藏位置',
		width : 500,
		dataIndex : 'scpName',
		sortable : true
	}, {
		header : '序号',
		width : 100,
		dataIndex : 'scpIndex',
		sortable : true
	}]);
	// 查询
	var searchMan = function() {
		scpName_Condition = scpName_search.getValue();
		store.reload({
			params : {
				start : 0,
				limit : limitLength,
				scpName : scpName_Condition
			}
		});
	}

	// --查询条件:标题
	var scpName_search = new Ext.form.TextField({
		width : 200,
		emptyText : '请输入馆藏位置...',
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
	});

	// 创建工具条
	var tbar = new Ext.Toolbar(['馆藏位置：', scpName_search, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : scpAdd
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : scpEdit
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : scpDelete
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
		autoExpandColumn : 'scpIndex',
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
			scpName : scpName_Condition
		});
	});
	grid.on('dblclick', scpEdit);
	// ----------------------- 新增信息 ----------------------------
	var scpName_Add = new Ext.form.TextField({
		fieldLabel : '馆藏位置',
		id : 'scpName_Add',
		name : 'scpName_Add',
		anchor : '90%',
		allowBlank : false,
		blankText : '馆藏位置不能为空!',
		maxLength : 256,
		maxLengthText : '馆藏位置不得超过256个字符!'
	});
	var scpIndex_Add = new Ext.form.TextField({
		fieldLabel : '序号',
		id : 'scpIndex_Add',
		name : 'scpIndex_Add',
		anchor : '90%',
		allowBlank : false,
		blankText : '序号不能为空!',
		maxLength : 9,
		validator:function check(){
        	if(/^[0-9]*$/.test(Ext.getCmp('scpIndex_Add').getValue())){
        		return true;
        	}else{
        		return "只能输入数字";
        	}
        }
	});
	var scpAdd_Form = new Ext.form.FormPanel({
		id : 'scpAdd_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 100,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [scpName_Add, scpIndex_Add],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveScp
		}, {
			text : '取&nbsp;消',
			handler : function() {
				scpAdd_Win.hide();
			}
		}]
	});
	// ----------------------- 编辑信息 ----------------------------
	var scpId_Edit = new Ext.form.TextField({
		fieldLabel : '馆藏位置ID',
		id : 'scpId_Edit',
		name : 'scpId_Edit',
		anchor : '90%',
		allowBlank : false,
		hidden : true,
		hideLabel : true
	});
	var scpName_Edit = new Ext.form.TextField({
		fieldLabel : '馆藏位置',
		id : 'scpName_Edit',
		name : 'scpName_Edit',
		anchor : '90%',
		allowBlank : false,
		blankText : '馆藏位置不能为空!',
		maxLength : 256,
		maxLengthText : '馆藏位置不得超过256个字符!'
	});
	var scpIndex_Edit = new Ext.form.TextField({
		fieldLabel : '序号',
		id : 'scpIndex_Edit',
		name : 'scpIndex_Edit',
		anchor : '90%',
		allowBlank : false,
		blankText : '序号不能为空!',
		maxLength : 9,
		validator:function check(){
        	if(/^[0-9]*$/.test(Ext.getCmp('scpIndex_Edit').getValue())){
        		return true;
        	}else{
        		return "只能输入数字";
        	}
        }
	});

	var scpEdit_Form = new Ext.form.FormPanel({
		id : 'scpEdit_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [scpId_Edit, scpName_Edit, scpIndex_Edit],
		buttons : [{
			text : '保&nbsp;存',
			handler : updateScp
		}, {
			text : '取&nbsp;消',
			handler : function() {
				scpEdit_Win.hide();
			}
		}]
	});
	// ------------------ 自定义函数 -------------------------------
	// 新增
	function scpAdd() {
		if(!scpAdd_Win) {
			scpAdd_Win = new Ext.Window({
				closeAction : 'hide',
				//maximizable : false,
				//maximizable : true,
				resizable : false,
				width : 400,
				height : 150,
				title : '新增馆藏位置',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [scpAdd_Form],
				listeners : {
					hide : function() {
						scpAdd_Form.form.reset();
					}
				}
			});
		}
		scpAdd_Win.show();
	}

	// 编辑
	function scpEdit() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			if(!scpEdit_Win) {
				scpEdit_Win = new Ext.Window({
					closeAction : 'hide',
					//maximizable : false,
					//maximizable : true,
					resizable : false,
					width : 400,
					height : 150,
					title : '编辑馆藏位置',
					layout : 'fit',
					modal : true,
					buttonAlign : 'center',
					items : [scpEdit_Form],
					listeners : {
						hide : function() {
							scpEdit_Form.form.reset();
						}
					}
				});
			}
			scpEdit_Form.form.setValues({
						scpId_Edit : selectRow.json.scpId,
						scpName_Edit : selectRow.json.scpName,
						scpIndex_Edit : selectRow.json.scpIndex
					});
			scpEdit_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 保存
	function saveScp() {
		if(scpAdd_Form.form.isValid()) {
			scpAdd_Form.getForm().submit({
				url : getPath() + 'console/data/sysCollectionPositionSet.htm?action=saveScp',
				method : 'post',
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					scpAdd_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	// 更新信息
	function updateScp() {
		if(scpEdit_Form.form.isValid()) {
			scpEdit_Form.getForm().submit({
				url : getPath() + 'console/data/sysCollectionPositionSet.htm?action=updateScp',
				method : 'post',
				params : {
					scpId : grid.getSelectionModel().getSelected().json.scpId
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					scpEdit_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	function scpDelete() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该馆藏位置吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var scpId = selectRow.json.scpId;
					scpDel(scpId);
				}
			});
		}else
			alertWarring('请选择要删除的一行数据操作！');
	}
	// 删除
	function scpDel(scpId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/sysCollectionPositionSet.htm?action=delScp',
			params : {
				scpId : scpId
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