// -- 特色库权限管理 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var rmstName = null;
	var resMetaPermSetting_Win = null;
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/resMetaPermSetting.htm?action=getResMetaStoreList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rmstId',
			type : 'int'
		}, {
			name : 'rmstName',
			type : 'string'
		}, {
			name : 'smanName',
			type : 'string'
		}, {
			name : 'rmstCompany',
			type : 'string'
		}, {
			name : 'rmstBuilddate',
			type : 'string'
		}, {
			name : 'rmstStatus',
			type : 'string'
		}]),
		sortInfo : {
			field : 'rmstBuilddate',
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
		header : '特色库名称',
		width : 200,
		dataIndex : 'rmstName',
		sortable : true
	}, {
		header : '创建人',
		width : 150,
		dataIndex : 'smanName',
		sortable : true
	}, {
		header : '建设单位',
		width : 200,
		dataIndex : 'rmstCompany',
		sortable : true
	}, {
		header : '建设时间',
		width : 120,
		dataIndex : 'rmstBuilddate',
		sortable : true
	}, {
		header : '状态',
		width : 70,
		dataIndex : 'rmstStatus',
		renderer : checkState,
		sortable : true
	}]);

	// --查询条件
	var rmstName_TextField = new Ext.form.TextField({
		name : 'rmstName',
		width : 180,
		emptyText : '多条件可用逗号或者空格隔开!',
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
	});

	var tb = new Ext.Toolbar(['特色库：', rmstName_TextField, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text : '&nbsp;分配权限&nbsp;',
		iconCls : 'application_go',
		handler : resPermSetting
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
		autoExpandColumn : 'rmstName',
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
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			rmstName : rmstName
		});
	});
	// ----------------------- 自定义函数 -----------------------
	// 查询操作
	function searchMan() {
		rmstName = rmstName_TextField.getRawValue();
		store.reload({
			params : {
				start : 0,
				limit : limitLength,
				rmstName : rmstName
			}
		});
	}
	// 检测特色库状态
	function checkState(val) {
		if(val == '1')
			return '<font style="color:blue">可用</font>';
		else
			return '<font style="color:red">冻结</font>';
	}
	// 分配权限
	function resPermSetting() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			document.location.href = getPath() + 'console/data/resMetaPermSetting.htm?action=initPermSetting&rmstId='
					+ selectRow.json.rmstId;
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
});