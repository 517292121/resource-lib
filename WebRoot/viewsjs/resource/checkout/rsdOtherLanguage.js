// -- 其它语资源 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/resource/resourceManager.htm?action=interpretSourceLanguageListJson&sourceId=' + rsdaId + '&type=1'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'RLTY_NAME'
		}, {
			name : 'RSLA_USERNAME'
		}, {
			name : 'RSLA_DATE'
		}, {
			name : 'RSLA_STATUS'
		}, {
			name : 'RSLA_RSDA_ID'
		}, {
			name : 'RSLA_RLTY_ID'
		}]),
		sortInfo : {
			field : 'rslaDate',
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
		header : '语种',
		width : 200,
		dataIndex : 'RLTY_NAME',
		sortable : true
	}, {
		header : '加工者',
		width : 100,
		dataIndex : 'RSLA_USERNAME',
		sortable : true
	}, {
		header : '加工时间',
		width : 100,
		dataIndex : 'RSLA_DATE',
		sortable : true
	}, {
		header : '状态',
		width : 100,
		dataIndex : 'RSLA_STATUS',
		renderer : checkState,
		sortable : true
	}]);
	// 创建工具条
	var tbar = new Ext.Toolbar([{
		text : '&nbsp;校&nbsp;验&nbsp;',
		iconCls : 'common_details',
		handler : resSourceDataOtherLanguageDetail
	}, '-', {
		text : '&nbsp;返&nbsp;回&nbsp;',
		iconCls : 'application_go',
		handler : function() {
			document.location.href = getPath() + 'console/data/otherLanguageMissionInfo.htm?action=initOtherLanguageResSourceData&rminId='
					+ rminId + "&userid=" + userId;
		}
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
			cls : 'res_nocheck',
			width : 16
		}, {
			xtype : 'label',
			text : '待审核',
			style : 'padding-left: 3px;color:#364f56;',
			// labelStyle : 'my_status',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 15px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls : 'res_pass',
			width : 16
		}, {
			xtype : 'label',
			text : '审核通过',
			style : 'padding-left: 3px;color: #364f56;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 15px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls : 'res_nopass',
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
		autoExpandColumn : 'rltyName',
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
	 grid.on('dblclick', resSourceDataOtherLanguageDetail);

	// ---------------------------- 自定义函数 ----------------------------------
	function checkState(val) {
		if(val == '2')
			return '<img src="' + getPath() + 'images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
		else if(val == '3')
			return '<img src="' + getPath() + 'images/res_minfo/res_pass.png" title="审核通过" width="16" height="16" />';
		else
			return '<img src="' + getPath() + 'images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
	}
	function resSourceDataOtherLanguageDetail() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			document.location.href=getPath() + 'console/data/otherLanguageMissionInfo.htm?action=initResSourceDataOtherLanguageDetail&rsdaId='+selectRow.json.RSLA_RSDA_ID+"&rltyId="+selectRow.json.RSLA_RLTY_ID + "&userid=" + userId;
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	 
});