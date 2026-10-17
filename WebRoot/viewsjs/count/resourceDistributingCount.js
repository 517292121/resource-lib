// -- 资源分布统计 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var endTime = null;
	var defaltSort = 'rsdaCount';
	var defaltDir = 'DESC';

	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/distributingCounter.htm?action=getResourceDistributingCountList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rmstId',
			type : 'int'
		}, {
			name : 'rmstTitle',
			type : 'string'
		}, {
			name : 'checking',
			type : 'string'
		}, {
			name : 'passcheck',
			type : 'string'
		}, {
			name : 'notpasscheck',
			type : 'string'
		}, {
			name : 'rsdaCount',
			type : 'string'
		}]),
		sortInfo : {
			field : 'rsdaCount',
			direction : 'DESC'
		},
		remoteStore : true, // 是否远程调用数据
		// 是否远程排序
		remoteSort : true
	});
	// 定义列模型
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '特色库名称',
		width : 200,
		dataIndex : 'rmstTitle',
		sortable : true
	}, {
		header : '审核中资源（条）',
		width : 100,
		dataIndex : 'checking',
		sortable : true
	}, {
		header : '审核通过资源（条）',
		width : 100,
		dataIndex : 'passcheck',
		sortable : true
	}, {
		header : '审核未通过资源（条）',
		width : 100,
		dataIndex : 'notpasscheck',
		sortable : true
	}, {
		header : '资源总数（条）',
		width : 50,
		dataIndex : 'rsdaCount',
		sortable : true
	}]);
	
	var endTimeField = new Ext.form.DateField({ 
		id : 'endTime',
		emptyText : '截止时间',
		width : 150,
		format : 'Y-m-d',
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
	});
		
	

	var tbar = new Ext.Toolbar(['截止时间：', endTimeField, '-' , {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	},'-',{ 
		text: '导出到Excel文件', 
		iconCls:'excel',
		handler: function(){ 
		    var tempTime = '0';
		    var time = endTimeField.getValue();
			if(!Ext.getCmp('endTime').isValid()) {
				tempTime = Date.parse(new Date()).toString();
			}
			if(time){
				tempTime = Date.parse(time).toString();
			}
			var url = getPath() + 'console/data/distributingCounter.htm?action=exportToExcel&endTime='+tempTime+'&defaltSort='+defaltSort+'&defaltDir='+defaltDir;
			window.location.href = url;
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
//		items : [{
//			xtype : 'label',
//			text : '资源总计',
//			style : 'padding-left: 10px;',
//			width : 10
//		}]
	});
	// 组合表格
	var grid = new Ext.grid.GridPanel({
		region : 'center',
		el : grid,
		cm : cm,
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
		autoExpandColumn : 'rmstTitle',
		tbar : tbar,
		bbar : pagingBar
	});
	// 创建主面板
	var displayPanel = new Ext.Viewport({
		layout : 'border',
		items : [grid]
	});

	function searchMan() {
		if(isValid()) {
			endTime = endTimeField.getValue();
			store.reload({
				params : {
					start : 0,
					limit : limitLength,
					endTime : endTime
				}
			});
		}
	}
	
	store.on('beforeload', function() {
		defaltSort = store.getSortState().field;
		defaltDir = store.getSortState().direction;
		Ext.apply(this.baseParams, {
			endTime : endTime
		});
	});
	
	store.load({
		params : {
			start : 0,
			limit : limitLength
		}
	});

	function isValid() {
		var endTime = Ext.getCmp('endTime').getValue();
		if(!Ext.getCmp('endTime').isValid()) {
			alertWarring("时间格式不正确！");
			return false;
		}
		return true;
	}
});