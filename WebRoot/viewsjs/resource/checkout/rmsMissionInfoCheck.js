// -- 作业校验 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var rmstId_combox = null;
	var rminStatus_combox = null;
	var resMissionInfoCheck_Win = null;
	var url_rlty_store = getPath() + 'console/data/templetManager.htm?action=getSourceTypeByStore';
	var url_sysMan_store = getPath() + 'console/data/resource/missionInfoManagement.htm?action=getSysManagerList&missiontype='
			+ deliverType;
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/rmsMissionInfoCheck.htm?action=getResMissionInfoList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'RMIN_CHECKUSER',
			type : 'int'
		}, {
			name : 'RMIN_ID',
			type : 'int'
		}, {
			name : 'RMIN_RMST_ID',
			type : 'int'
		}, {
			name : 'RMST_NAME',
			type : 'string'
		}, {
			name : 'RMIN_MISSNAME',
			type : 'string'
		}, {
			name : 'RLTY_NAME',
			type : 'string'
		}, {
			name : 'SMAN_NAME',
			type : 'string'
		}, {
			name : 'RMIN_BUILDDATE',
			type : 'string'
		}, {
			name : 'RMIN_ENDDATE',
			type : 'string'
		}, {
			name : 'RMIN_STATUS',
			type : 'string'
		}, {
			name : 'SOURCENUM',
			type : 'string'
		}, {
			name : 'NOPASSNUM',
			type : 'string'
		}, {
			name : 'RMIN_REMARK',
			type : 'string'
		}]),
		sortInfo : {
			field : 'RMIN_STATUS',
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
		header : '作业名称',
		width : 210,
		dataIndex : 'RMIN_MISSNAME',
		sortable : true
	}, {
		header : '所属特色库',
		width : 150,
		dataIndex : 'RMST_NAME',
		sortable : true
	}, {
		header : '资源类型',
		width : 120,
		dataIndex : 'RLTY_NAME',
		sortable : true
	}, {
		header : '审核人',
		width : 90,
		dataIndex : 'SMAN_NAME',
		sortable : true,
		renderer : function(v) {
			if(v == null || v.length < 1) {
				return onAuthor;
			}else {
				return v;
			}
		}
	}, {
		header : '审核时间',
		width : 120,
		dataIndex : 'RMIN_ENDDATE',
		sortable : true
	},
	{
		header : '状态',
		width : 75,
		align : 'center',
		dataIndex : 'RMIN_STATUS',
		sortable : true,
		renderer : checkState
	},
	{
		header : '未审核数',
		width : 75,
		sortable : true,
		dataIndex : 'SOURCENUM'
	},
	{
		header : '未通过数',
		width : 75,
		sortable : true,
		dataIndex : 'NOPASSNUM'
	}, {
		header : '描述',
		width : 180,
		dataIndex : 'RMIN_REMARK',
		sortable : true
	}]);
	// 查询
	var searchMan = function() {
		rmstId_combox = rms_Combox.getValue();
		rminStatus_combox = rmin_status_Combox.getValue();
		rlty_Combox = Ext.getCmp('rlty_Combox_Id').getValue();
		store.reload({
			params : {
				start : 0,
				limit : limitLength,
				rmstId_combox : rmstId_combox,
				rminStatus_combox : rminStatus_combox,
				rlty_Combox : rlty_Combox
			}
		});
	}
	var rms_store_select_hasNull = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/extdata.htm?action=all_store_select&ptype=3&hasNull=hasNull'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'rdtyId',
			type : 'int'
		}, {
			name : 'rdtyName',
			type : 'string'
		}])
	});
	var rmin_Rlty_Store_hasNull = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : url_rlty_store
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'value'
		}, {
			name : 'text'
		}])
	});
	var rms_Combox = new Ext.form.ComboBox({
		fieldLabel : "特色库",
		mode : 'remote',
		store : rms_store_select_hasNull,
		resizable : true,
		name : 'rdtyName',
		emptyText : '请先选择特色库...',
		triggerAction : 'all',
		valueField : 'rdtyId',
		selectOnFocus : true,
		displayField : 'rdtyName',
		editable : false,
		width : 150
	});
	var rlty_Combox = new Ext.form.ComboBox({
		id : 'rlty_Combox_Id',
		fieldLabel : "资源类型",
		mode : 'remote',
		store : rmin_Rlty_Store_hasNull,
		emptyText : '再选择资源类型...',
		triggerAction : 'all',
		lazyInit : true,
		resizable : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		width : 150,
		listeners : {
			'select' : function() {
				searchMan();
			}
		}
	});
	// 作业管理状态下拉框
	var rmin_status_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[-1, '不限'], [2, '待审核'], [3, '审核通过'], [4, '审核未通过']]
	});
	var rmin_status_Combox = new Ext.form.ComboBox({
		fieldLabel : "状态",
		mode : 'local',
		store : rmin_status_store,
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
	// 创建工具条
	var tbar = new Ext.Toolbar(['特色库：', rms_Combox, '-', rlty_Combox, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text : '查看资源',
		iconCls : 'common_list',
		handler : resMissionInfoDetail
	}, '-', {
		text : '提交完成',
		iconCls : 'common_submit',
		handler : resMissionInfoCheck
	}, '-', {
		text : '任务转交',
		iconCls : 'common_go',
		handler : missionDeliver
	}]);
	// 创建一个分页控件
	var pagingBar = new Ext.PagingToolbar({
		pageSize : limitLength,
		store : store,
		displayInfo : true,
		displayMsg : pagingDisplayMsg,
		emptyMsg : '没有记录',
		plugins : new Ext.ux.ProgressBarPager(),
		  items : [
		  	{ xtype : 'label', style : 'padding-left: 10px;', width : 10 },
		  	{ xtype : 'label', style : 'padding-left: 16px;', cls : 'res_nocheck', width : 16 },
		  	{ xtype : 'label', text : '待审核', style : 'padding-left: 3px;color:#364f56;',  labelStyle :  'my_status', width : 10 },
		  	{ xtype : 'label', style : 'padding-left: 15px;', width : 10 },
		  	{ xtype : 'label', style : 'padding-left: 16px;', cls : 'res_pass', width : 16 },
		  	{ xtype : 'label', text : '审核通过', style : 'padding-left: 3px;color: #364f56;', width : 10 },
		  	{ xtype : 'label', style : 'padding-left: 15px;', width : 10 },
		  	{ xtype : 'label', style : 'padding-left: 16px;', cls : 'res_nopass', width : 16 },
		  	{ xtype : 'label', text : '审核未通过', style : 'padding-left: 3px;color: #364f56;', width : 10 }
		]
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
		autoExpandColumn : 'rminMissName',
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
			rmstId_combox : rmstId_combox,
			rminStatus_combox : rminStatus_combox,
			rlty_Combox : rlty_Combox
		});
	});
	grid.on('dblclick', resMissionInfoDetail);
	// 当特色发生改变时，级联改变特色库所联系的资源类型
	rms_Combox.on('select', function(comboBox) {
		var id = comboBox.getValue();
		// rlty_Combox.clearValue(); // 清空之前的资源类型 注意：添加该行后报错！
		Ext.getCmp('rlty_Combox_Id').clearValue();
		rmin_Rlty_Store_hasNull.proxy = new Ext.data.HttpProxy({
			url : url_rlty_store + "&hasNull=hasNull&store=" + id
		});
		rmin_Rlty_Store_hasNull.load();
		searchMan();
	});
	// ---------------------------- 作业校验 -----------------------------------
	var resMissionInfoCheck_RminMissName = new Ext.form.TextField({
		fieldLabel : '作业名称',
		id : 'rminMissName',
		name : 'rminMissName',
		anchor : '95%'
	});
	var resMissionInfoCheck_RminStatus = new Ext.form.RadioGroup({
		id : 'resMissionInfoCheck_RminStatus',
		fieldLabel : '审核结果',
		name : 'rminStatus',
		anchor : '80%',
		items : [{
			boxLabel : '通过',
			name : 'rminStatus',
			inputValue : 3,
			checked : true
		}, {
			boxLabel : '不通过',
			name : 'rminStatus',
			inputValue : 4
		}]
	});
	var resMissionInfoCheck_Form = new Ext.form.FormPanel({
		id : 'resMissionInfoCheck_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [resMissionInfoCheck_RminMissName, resMissionInfoCheck_RminStatus],
		buttons : [{
			text : '保&nbsp;存',
			handler : resMissionInfoCheck_Form_Submit
		}, {
			text : '取&nbsp;消',
			handler : function() {
				resMissionInfoCheck_Win.hide();
			}
		}]
	});
	// ------------------------------任务转交----------------------------------
	function missionDeliver() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var missionUser = selectRow.get("RMIN_CHECKUSER");
			if(missionUser == null || missionUser < 1) {
				Ext.Msg.alert("错误", "该作业现不属于您审核，您不能进行转交!");
				return;
			}
			var rminId = selectRow.get("RMIN_ID");
			var rmstId = selectRow.get("RMIN_RMST_ID");
			var rminStatu = selectRow.get("RMIN_STATUS");
			if(rminStatu == 3){
			    Ext.Msg.alert("提示", '该作业已审核通过，不能再进行转交！');
			    return;
			}
			// 加载操作者数据
			rminAdd_SysMan_store.proxy = new Ext.data.HttpProxy({
				url : url_sysMan_store + "&rminId=" + rminId
			});
			rminAdd_SysMan_store.load();
			handlerMissionInfoChange();
		}else {
			Ext.Msg.alert("提示", '请点击选择需要转交的作业！');
		}
	}
	var rminAdd_SysMan_store = new Ext.data.Store({
		// autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : url_sysMan_store
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'smanId'
		}, {
			name : 'smanName'
		}])
	});
	var rminChange_RminUserName_Combox = new Ext.form.ComboBox({
		fieldLabel : "接收者",
		mode : 'remote',
		store : rminAdd_SysMan_store,
		hiddenName : 'smanId',
		emptyText : '请选择接收者',
		triggerAction : 'all',
		lazyInit : true,
		valueField : 'smanId',
		displayField : 'smanName',
		editable : false,
		anchor : '80%',
		allowBlank : false,
		blankText : '任务接收者不能为空！'
	});
	var remark = new Ext.form.TextArea({
		id : 'remark',
		name : 'remark',
		fieldLabel : '描述',
		height : 70,
		maxLength : 150,
		anchor : '95%',
		maxLength : 300,
		maxLengthText : '作业描述不得超过300个字符!'
	});
	var rminChange_Form = new Ext.form.FormPanel({
		id : 'rminChange_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [rminChange_RminUserName_Combox],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveRminChange
		}, {
			text : '取&nbsp;消',
			handler : function() {
				rminChange_Win.hide();
			}
		}]
	});
	var rminChange_Win = null;
	function handlerMissionInfoChange() {
		if(!rminChange_Win) {
			rminChange_Win = new Ext.Window({
				closeAction : 'hide',
				resizable : false,
				width : 280,
				height : 125,
				title : '任务转交',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [rminChange_Form],
				listeners : {
					hide : function() {
						rminChange_Form.form.reset();
						rminChange_RminUserName_Combox.clearValue();
						rminAdd_SysMan_store.removeAll();
					}
				}
			});
		}
		rminChange_Win.show();
	}
	function saveRminChange() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			if(rminChange_Form.form.isValid()) {
				var rminId = selectRow.get("RMIN_ID");
				rminChange_Form.getForm().submit({
					url : getPath() + 'console/data/resource/missionInfoManagement.htm?action=saveRminChange',
					method : 'post',
					params : {
						rminId : rminId,
						missiontype : deliverType
					},
					waitMsg : '正在保存数据，请稍候...',
					success : function(form, action) {
						// 提示信息
						Msg.success(action.result.msg);
						// 刷新列表
						store.reload();
						rminChange_Win.hide();
					},
					failure : function(form, action) {
						alertError(action.result.msg);
					}
				});
			}
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// ---------------------------- 自定义函数 ----------------------------------
	// 作业状态
	function checkState(val) {
		if(val == '2')
			return '<img src="' + getPath() + 'images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
		else if(val == '3')
			return '<img src="' + getPath() + 'images/res_minfo/res_pass.png" title="审核通过" width="16" height="16" />';
		else if(val == '4')
			return '<img src="' + getPath() + 'images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
		else if(val == '5')
			return '<img src="' + getPath() + 'images/res_minfo/res_deliver.png" title="转交中" width="16" height="16" />';
	}
	var noPassNum = 0;
	// 作业校验
	function resMissionInfoCheck() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var missionUser = selectRow.get("RMIN_CHECKUSER");
			var unCheck = selectRow.get("SOURCENUM");
			noPassNum = selectRow.get("NOPASSNUM");
			if(missionUser == null || missionUser < 1) {
				Ext.Msg.alert("错误", "该作业现不属于您审核，您不能进行提交!");
				return;
			}
			if(unCheck != null && unCheck > 0){
				Ext.Msg.alert("错误", "该作业有未审核的资源，暂时不能提交!");
				return;
			}
			if(!resMissionInfoCheck_Win) {
				resMissionInfoCheck_Win = new Ext.Window({
					closeAction : 'hide',
					maximizable : false,
					width : 300,
					height : 150,
					title : '完成',
					layout : 'fit',
					modal : true,
					buttonAlign : 'center',
					items : [resMissionInfoCheck_Form]
				});
			}
			initResMissionInfoCheckData();
			resMissionInfoCheck_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 初始化状态数据
	function initResMissionInfoCheckData() {
		var selectRow = grid.getSelectionModel().getSelected();
		resMissionInfoCheck_RminMissName.setValue(selectRow.get("RMIN_MISSNAME"));
		resMissionInfoCheck_RminMissName.disable();
		if(selectRow.json.rminStatus == 2 || selectRow.json.rminStatus == 4)
			resMissionInfoCheck_RminStatus.setValue(4);
		else
			resMissionInfoCheck_RminStatus.setValue(3);
	}
	// 提交resMissionInfoCheck_Form信息
	function resMissionInfoCheck_Form_Submit() {
		var result = resMissionInfoCheck_RminStatus.getValue();
		if(result == 3 && noPassNum > 0){
		    Ext.Msg.alert("错误", "<span style='color:red'>该作业有未通过的资源，不允许审核通过！</span>");
		    return;
		}
		resMissionInfoCheck_Form.getForm().submit({
			url : getPath() + 'console/data/rmsMissionInfoCheck.htm?action=saveResMissionInfoStatus',
			method : 'post',
			params : {
				rminId : grid.getSelectionModel().getSelected().get("RMIN_ID")
			},
			waitMsg : '正在保存数据，请稍候...',
			success : function(form, action) {
				// 提示信息
				Msg.success(action.result.msg);
				// 刷新列表
				searchMan();
				// 隐蔽窗口
				resMissionInfoCheck_Win.hide();
			},
			failure : function(form, action) {
				alertError(action.result.msg);
			}
		});
	}
	// 作业详情
	function resMissionInfoDetail() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var rminId = selectRow.get("RMIN_ID");
			var userId = selectRow.get("RMIN_CHECKUSER");
			document.location.href = getPath() + 'console/data/rmsMissionInfoDataCheck.htm?action=initRmsMissionInfoDataCheckView&rminId='
					+ rminId + "&userid=" + userId;
			//document.location.href = getPath() + "console/data/resource/missionLanguage.htm?action=missionLanguageList&folder=checkout&rminId=" + rminId;
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
});