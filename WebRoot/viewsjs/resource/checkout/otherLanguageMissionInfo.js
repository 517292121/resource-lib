// -- 作业 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var rmstId_combox = null;
	var rminStatus_combox = null;
	var resMissionInfoCheck_Win = null;
	var url_rlty_store = getPath() + 'console/data/templetManager.htm?action=getSourceTypeByStore';
	var url_sysMan_store = getPath() + 'console/data/resource/missionInfoManagement.htm?action=getSysManagerList&missiontype=' + deliverType;
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/otherLanguageMissionInfo.htm?action=getResMissionInfoList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'RMIN_ID',
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
			name : 'RMIN_REMARK',
			type : 'string'
		}, {
			name : 'RMIN_LANGUAGE_TYPE',
			type : 'string'
		}]),
		sortInfo : {
			field : 'RMIN_ENDDATE',
			direction : 'DESC'
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
		width : 200,
		dataIndex : 'RMIN_MISSNAME',
		sortable : true
	}, {
		header : '所属特色库',
		width : 100,
		dataIndex : 'RMST_NAME',
		sortable : true
	}, {
		header : '资源类型',
		width : 100,
		dataIndex : 'RLTY_NAME',
		sortable : true
	}, {
		header : '未审核语种',
		width : 200,
		dataIndex : 'RMIN_LANGUAGE_TYPE'
	}, {
		header : '描述',
		width : 100,
		dataIndex : 'RMIN_REMARK'
	}]);
	// 查询
	var searchMan = function() {
		rmstId_combox = rms_Combox.getValue();
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
		data : [[-1, '不限'], [3, '审核通过'], [5, '待审核'], [6, '审核未通过']]
	});

	// 创建工具条
	var tbar = new Ext.Toolbar(['特色库：', rms_Combox, '-', rlty_Combox,  '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text : '查看任务',
		iconCls : 'common_list',
		handler : resMissionInfoDetail
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
		},{
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls  : 'res_nocheck',
			width : 16
		}, {
			xtype : 'label',
			text : '待审核',
			style : 'padding-left: 3px;color:#364f56;',
//			labelStyle : 'my_status',
			width : 10
		},{
			xtype : 'label',
			style : 'padding-left: 15px;',
			width : 10
		},{
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls  : 'res_pass',
			width : 16
		}, {
			xtype : 'label',
			text : '审核通过',
			style : 'padding-left: 3px;color: #364f56;',
			width : 10
		},{
			xtype : 'label',
			style : 'padding-left: 15px;',
			width : 10
		},{
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls  : 'res_nopass',
			width : 16
		}, {
			xtype : 'label',
			text : '审核未通过',
			style : 'padding-left: 3px;color: #364f56;',
			width : 10
		}]
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
	// ---------------------------- 自定义函数 ----------------------------------
	// 作业状态
	function checkState(val) {
		if(val == '5')
			return '<img src="' + getPath() + 'images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
		else if(val == '3')
			return '<img src="' + getPath() + 'images/res_minfo/res_pass.png" title="通过" width="16" height="16" />';
		else if(val == '6')
			return '<img src="' + getPath() + 'images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
	}
	// 作业详情
	function resMissionInfoDetail(){
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			//document.location.href=getPath() + 'console/data/otherLanguageMissionInfo.htm?action=initOtherLanguageResSourceData&rminId='+selectRow.get("RMIN_ID")+"&userid="+selectRow.get("RMIN_OTHERCHECK");
			var rminId = selectRow.get("RMIN_ID");
			document.location.href = getPath() + "console/data/resource/missionLanguage.htm?action=missionLanguageList&folder=checkout&rminId=" + rminId;
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');

	}
	
});