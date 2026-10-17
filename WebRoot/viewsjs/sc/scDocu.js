// -- 求助管理 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var sddeTitle_Condition;
	var loginName_Condition;
	var sddeStatus_Condition;

	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/docudelivery.htm?action=getDocuList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'sddeId',
			type : 'int'
		}, {
			name : 'sddeDocuTitle',
			type : 'string'
		}, {
			name : 'suinName',
			type : 'string'
		}, {
			name : 'suinLoginName',
			type : 'string'
		}, {
			name : 'sddeCreationTime',
			type : 'string'
		}, {
			name : 'sddeStatus',
			type : 'string'
		}]),
		sortInfo : {
			field : 'sddeStatus',
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
		header : '标题',
		width : 200,
		dataIndex : 'sddeDocuTitle',
		sortable : true
	},  {
		header : '姓名',
		width : 100,
		dataIndex : 'suinName',
		sortable : true
	}, {
		header : '用户名',
		width : 100,
		dataIndex : 'suinLoginName',
		sortable : true
	}, {
		header : '传递时间',
		width : 100,
		dataIndex : 'sddeCreationTime',
		sortable : true
	}, {
		header : '状态',
		width : 50,
		dataIndex : 'sddeStatus',
		sortable : true,
		align : 'center',
		renderer : checkState
	}]);
	// --查询条件:标题
	var sddeTitle_search = new Ext.form.TextField({
		name : 'sddeTitle_search',
		width : 100,
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
	});
	// 用户名
	var loginName_search = new Ext.form.TextField({
		name : 'loginName_search',
		width : 100,
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
	});
	// 状态
	var sddeStatus_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[0, '不限'], [1, '未回复'], [2, '已回复']]
	});
	var sddeStatus_Combox = new Ext.form.ComboBox({
		fieldLabel : "状态",
		mode : 'local',
		store : sddeStatus_store,
		emptyText : '不限',
		triggerAction : 'all',
		editable : false,
		valueField : 'value',
		displayField : 'text',
		width : 100,
		value : 0,
		listeners : {
			'select' : function() {
				searchMan();
			}
		}
	});

	var tb = new Ext.Toolbar([
	'状态：',sddeStatus_Combox, '-', 
	'标题：', sddeTitle_search, '-', 
	/***'用户名：', loginName_search,  '-',*/ 
	{
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	},'-',
	{
		text : '&nbsp;回&nbsp;复&nbsp;',
		iconCls : 'common_details',
		handler : addReply
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : delDocu
	}
	]);
			
			
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
			cls : 'sc_not_solve',
			width : 16
		}, {
			xtype : 'label',
			text : '未回复',
			style : 'padding-left: 3px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 15px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls : 'sc_solve',
			width : 16
		}, {
			xtype : 'label',
			text : '已回复',
			style : 'padding-left: 3px;',
			width : 10
		}]
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
		autoExpandColumn : 'scisTitle',
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
	grid.on('dblclick', addReply);
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			sddeTitle : sddeTitle_Condition,
			loginName : loginName_Condition,
			sddeStatus : sddeStatus_Condition
		});
	});

	// 状态
	function checkState(val) {
		if(val == '1')
			return '<img src="' + getPath() + 'images/sc/sc_not_solve.png" title="未回复"  />';
		else if(val == '2') return '<img src="' + getPath() + 'images/sc/sc_solve.png" title="已回复"  />';
	}

	function searchMan() {
		sddeTitle_Condition = sddeTitle_search.getValue();
		loginName_Condition = loginName_search.getValue();
		sddeStatus_Condition = sddeStatus_Combox.getValue();

		store.load({
			params : {
				start : 0,
				limit : limitLength,
				sddeTitle : sddeTitle_Condition,
				loginName: loginName_Condition,
				sddeStatus : sddeStatus_Condition
			}
		});
	}

	// 查看/回复信息
	function addReply(){
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow){
			var Id = selectRow.json.sddeId;
			window.location.href =getPath() + "console/data/docudelivery.htm?action=addReply&sddeId=" + Id+"";
		}else{
			alertWarring('请点击选择需要回复的传递信息!');
		}
	}
	
	//删除
	function delDocu() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该服务吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var sddeId = selectRow.json.sddeId;
					docuDelete(sddeId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	
	function docuDelete(sddeId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/docudelivery.htm?action=deleteDocu',
			params : {
				sddeId : sddeId
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

});