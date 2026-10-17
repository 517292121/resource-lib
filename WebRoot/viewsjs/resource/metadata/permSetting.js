// -- 特色库权限管理 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var rmstName = null;
	var resMetaPermSetting_Win = null;
	var rmpeType = null;
	var permSetting_Win = null;
	var permSettingEdit_Win = null;
	var isEditing = false;
	var rmpeId = null;
	var url_sman_store = getPath() + 'console/data/resMetaPermSetting.htm?action=getSysManagerList';
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/resMetaPermSetting.htm?action=getResMetaPermList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rmpeId',
			type : 'int'
		}, {
			name : 'rmpeType',
			type : 'string'
		}, {
			name : 'smanName',
			type : 'string'
		}, {
			name : 'rmpeBuilddate',
			type : 'string'
		}]),
		sortInfo : {
			field : 'rmpeType',
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
		header : '权限类型',
		width : 200,
		dataIndex : 'rmpeType',
		renderer : checkRmpeType,
		sortable : true
	}, {
		header : '操作人',
		dataIndex : 'smanName',
		sortable : true
	}, {
		header : '创建时间',
		dataIndex : 'rmpeBuilddate',
		sortable : true
	}]);
	// 创建工具条
	var rmpe_type_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[-1, '不限'], [2, '数据加工'], [3, '数据审核']]
	});

	var rmep_type_Combox = new Ext.form.ComboBox({
		fieldLabel : "权限类型",
		mode : 'local',
		store : rmpe_type_store,
		emptyText : '不限',
		triggerAction : 'all',
		editable : false,
		valueField : 'value',
		displayField : 'text',
		width : 100,
		value : -1,
		listeners : {
			'select' : function() {
				searchMan();
			}
		}
	});

	var tb = new Ext.Toolbar(['权限类型：', rmep_type_Combox, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : addPermSetting
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : permSettingEdit
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : delResMetaPerm
	}, '-', {
		text : '&nbsp;返&nbsp;回&nbsp;',
		iconCls : 'application_go',
		handler : function() {
			document.location.href = getPath() + 'console/data/resMetaPermSetting.htm?action=initResMetaPermSettingView'
		}
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
		autoExpandColumn : 'rmpeType',
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
			limit : limitLength,
			rmstId : rmstId
		}
	});
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			rmstId : rmstId,
			rmpeType : rmpeType
		});
	});
	grid.on('dblclick', permSettingEdit);
	// ----------------------- 新增 ---------------------------
	// 权限类型
	var rmpe_type_store_add = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[2, '数据加工'], [3, '数据审核']]
	});

	var rmep_type_Combox_add = new Ext.form.ComboBox({
		fieldLabel : "权限类型",
		mode : 'local',
		store : rmpe_type_store_add,
		hiddenName : 'rmpeType',
		emptyText : '请选择权限类型',
		triggerAction : 'all',
		editable : false,
		valueField : 'value',
		displayField : 'text',
		anchor : '90%',
		allowBlank : false,
		blankText : '权限类型不能为空！'
	});
	// 操作人员
	var permSetting_SysMan_store = new Ext.data.Store({
		autoLoad : false,
		proxy : new Ext.data.HttpProxy({
			url : url_sman_store
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'smanId'
		}, {
			name : 'smanName'
		}])
	});
	var permSetting_SysMan_Combox = new Ext.form.ComboBox({
		fieldLabel : "操作者",
		mode : 'remote',
		store : permSetting_SysMan_store,
		hiddenName : 'smanId',
		emptyText : '请选择操作者',
		triggerAction : 'all',
		lazyInit : true,
		valueField : 'smanId',
		displayField : 'smanName',
		editable : false,
		anchor : '90%',
		allowBlank : false,
		blankText : '操作者不能为空！'
	});
	// 动态修改操作人
	rmep_type_Combox_add.on('select', function(comboBox) {
		var rmpeType = rmep_type_Combox_add.getValue();
		if(!isEditing) permSetting_SysMan_Combox.clearValue();
		permSetting_SysMan_store.proxy = new Ext.data.HttpProxy({
			url : url_sman_store + "&rmpeType=" + rmpeType + "&rmstId=" + rmstId
		});
		permSetting_SysMan_store.load();
	});

	// 设置操作人员Form
	var permSetting_Form = new Ext.form.FormPanel({
		id : 'permSetting_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [rmep_type_Combox_add, permSetting_SysMan_Combox],
		buttons : [{
			text : '保&nbsp;存',
			handler : function() {
				saveOrUpdateRMPE();
			}
		}, {
			text : '取&nbsp;消',
			handler : function() {
				if(isEditing)
					permSettingEdit_Win.hide();
				else
					permSetting_Win.hide();
			}
		}]
	});

	// ----------------------- 自定义函数 -----------------------
	// 查询操作
	function searchMan() {
		rmpeType = rmep_type_Combox.getValue();
		store.reload({
			params : {
				start : 0,
				limit : limitLength,
				rmstId : rmstId,
				rmpeType : rmpeType
			}
		});
	}
	// 检测特色库状态
	function checkRmpeType(val) {
		if(val == '2')
			return '数据加工';
		else
			return '数据审核';
	}
	// 新增管理人员
	function addPermSetting() {
		if(!permSetting_Win) {
			permSetting_Win = new Ext.Window({
				frame : true,
				closeAction : 'hide',
				resizable : false,
				border : false,
				width : 300,
				height : 145,
				title : '新增操作人员',
				modal : true,
				items : [permSetting_Form],
				listeners : {
					'hide' : function() {
						reset();
					}
				}
			});
		}
		permSetting_Win.add(permSetting_Form);
		permSetting_Win.show();
	}
	// 编辑管理人员
	function permSettingEdit() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			isEditing = true;
			if(!permSettingEdit_Win) {
				permSettingEdit_Win = new Ext.Window({
					frame : true,
					closeAction : 'hide',
					resizable : false,
					border : false,
					width : 300,
					height : 145,
					title : '编辑操作人员',
					modal : true,
					items : [permSetting_Form],
					listeners : {
						'hide' : function() {
							isEditing = false;
							reset();
						}
					}
				});
			}
			permSettingEdit_Win.add(permSetting_Form);
			initPermSettingData(selectRow);
			permSettingEdit_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 初始化编辑窗口数据
	function initPermSettingData(selectRow) {
		var rmpeType_ = selectRow.json.rmpeType;
		permSetting_Form.getForm().findField('rmpeType').setValue(rmpeType_);
		permSetting_Form.getForm().findField('smanId').setValue(selectRow.json.smanName);
		// 重新加载操作者
		permSetting_SysMan_store.proxy = new Ext.data.HttpProxy({
			url : url_sman_store + "&rmpeType=" + rmpeType_ + "&rmstId=" + rmstId
		});
		permSetting_SysMan_store.load();
	}
	// 操作数据
	function saveOrUpdateRMPE() {
		if(permSetting_Form.form.isValid()) {
			permSetting_Form.getForm().submit({
				url : getPath() + 'console/data/resMetaPermSetting.htm?action=saveOrUpdateResMetaPerm',
				method : 'post',
				params : {
					rmstId : rmstId,
					isEditing : isEditing,
					rmpeId : isEditing == true ? grid.getSelectionModel().getSelected().json.rmpeId : 0
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					if(isEditing)
						permSettingEdit_Win.hide();
					else
						permSetting_Win.hide();

				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	function reset() {
		permSetting_Form.form.reset();
		rmep_type_Combox_add.clearValue();
		permSetting_SysMan_store.removeAll();
	}
	// 删除权限
	function delResMetaPerm() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var tempRmpeId = selectRow.json.rmpeId;
					delFun(tempRmpeId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	function delFun(rmpeId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/resMetaPermSetting.htm?action=delResMetaPerm',
			params : {
				rmpeId : rmpeId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					alertSuccess(txt.msg);
					searchMan();
				}else
					alertError(txt.msg);
			},
			failure : function(form, action) {
				alertError('系统忙，请稍后访问！');
			}
		});
	}

});