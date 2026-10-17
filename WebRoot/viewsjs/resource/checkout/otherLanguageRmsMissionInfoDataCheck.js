// -- 其它语资源审核 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var rsdaStatus_combox = null;
	var resMissionInfoDataCheck_Win = null;
	var resSourceDataDetail_Win = null;
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/otherLanguageMissionInfo.htm?action=getOtherLanguageResMissionLanguageDataList'
		}),
		baseParams : {
			rmlaId : rmlaId
		},
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rsdaId',
			type : 'int'
		}, {
			name : 'rsdaTitle',
			type : 'string'
		}, {
			name : 'rsdaCreator',
			type : 'string'
		}, {
			name : 'rltyName',
			type : 'string'
		}, {
			name : 'rsdaPublisher',
			type : 'string'
		}, {
			name : 'rslaDate',
			type : 'string'
		}, {
			name : 'rslaStatus',
			type : 'string'
		}, {
			name : 'rltyId',
			type : 'int'
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
		header : '题名',
		width : 210,
		dataIndex : 'rsdaTitle',
		sortable : true
	}, {
		header : '资源类型',
		width : 120,
		dataIndex : 'rltyName',
		sortable : true
	}, {
		header : '创建者',
		width : 120,
		dataIndex : 'rsdaCreator',
		sortable : true
	}, {
		header : '出版者',
		width : 120,
		dataIndex : 'rsdaPublisher',
		sortable : true
	}, {
		header : '录入时间',
		width : 120,
		dataIndex : 'rslaDate',
		sortable : true
	}, {
		header : '状态',
		width : 75,
		align : 'center',
		dataIndex : 'rslaStatus',
		renderer : checkState,
		sortable : true
	}]);
	// 查询
	var searchMan = function() {
		rsdaStatus_combox = rsda_status_Combox.getValue();
		store.reload({
			params : {
				start : 0,
				limit : limitLength,
				rsdaStatus_combox : rsdaStatus_combox,
				title: sourceTitle.getValue()
			}
		});
	}
	// 作业管理状态下拉框
	var rsda_status_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[-1, '不限'], [2, '待审核'], [3, '审核通过'], [4, '审核未通过']]
	});
	var rsda_status_Combox = new Ext.form.ComboBox({
		fieldLabel : "状态",
		mode : 'local',
		store : rsda_status_store,
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
	
	var sourceTitle = new Ext.form.TextField({
		fieldLabel : '题名',
		id : 'sourceTitle',
		name : 'sourceTitle',
		emptyText : '请输入题名',
		maxLength : 40,
		maxLengthText : '作业名称不得超过40个字符!',
		listeners : {
			specialKey :function(field,e){
	            if (e.getKey() == Ext.EventObject.ENTER){
	               searchMan();
	            } 
	        }
		}
	});
	// 创建工具条
	var tbar = new Ext.Toolbar([
		'题名：', sourceTitle, '-', 
	'状态：', rsda_status_Combox, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-',  {
		text : '我来审核',
		iconCls : 'common_flag',
		handler : getMission,
		hidden : (assessorId != null && assessorId != "null" && assessorId > 0) ? true : false
	},{
		text : '&nbsp;审&nbsp;核&nbsp;',
		iconCls : 'common_edit',
		handler : resSourceDataOtherLanguageDetail,
		hidden : (assessorId != null && assessorId != "null" && assessorId > 0) ? false : true
	}, '-', {
		text : '&nbsp;返&nbsp;回&nbsp;',
		iconCls : 'common_back',
		handler : function() {
			document.location.href = getPath() + "console/data/resource/missionLanguage.htm?action=missionLanguageList&folder=checkout&rminId=" + rminId;
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
			style : 'padding-left: 3px;',
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
			style : 'padding-left: 3px;',
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
			style : 'padding-left: 3px;',
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
		autoExpandColumn : 'rslaDate',
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
			rsdaStatus_combox : rsdaStatus_combox,
			title: sourceTitle.getValue()
		});
	});
	 grid.on('dblclick', resSourceDataOtherLanguageDetail);
	// ---------------------------- 自定义函数 ----------------------------------
	// 状态
	function checkState(val) {
		if(val == '2')
			return '<img src="' + getPath() + 'images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
		else if(val == '3')
			return '<img src="' + getPath() + 'images/res_minfo/res_pass.png" title="审核通过" width="16" height="16" />';
		else if(val == '4') return '<img src="' + getPath() + 'images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
	}
	function resSourceDataOtherLanguageDetail() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			if(assessorId == null || assessorId == "null" || assessorId != loginUser){
				Ext.Msg.alert('错误', "<span style='color:red'>对不起，您没有该作业的校验权限！</span>");
				return;
			}
			document.location.href=getPath() + 'console/data/otherLanguageMissionInfo.htm?action=initResSourceDataOtherLanguageDetail&rsdaId='+selectRow.json.rsdaId+"&rltyId="+selectRow.json.rltyId+"&rmlaId="+rmlaId;
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	function getMission() {
		Ext.Msg.confirm('提示', '您确定要领取该作业的其它语审核任务吗？\n', function(button, text) {
			var reqPath = getPath() + "console/data/resource/missionInfoManagement.htm?action=setMissionUser&userType=4&userid=" + loginUser + "&missionid=" + rminId + "&languageId=" + languageId;
			if(button == 'yes') {
				Ext.Ajax.request({
					url : reqPath,
					waitTitle : "数据提交",
					waitMsg : "作业提交中，请稍候...",
					success : function(form, action) {
						 window.location.reload();
						//window.location.href = getPath()
						//		+ "console/data/rmsMissionInfoDataCheck.htm?action=initRmsMissionInfoDataCheckView&rminId=" + rminId
						//		+ "&userid=" + loginUser;
					},
					failure : function(form, action) {
						Ext.Msg.alert('错误', "<span style='color:red'>对不起，操作失败！</span>");
					}
				});
			}
		})
	}
});