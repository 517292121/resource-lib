// -- 其它语资源 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/otherLanguageMissionInfo.htm?action=getOtherLanguageResSourceDataList&rminId=' + rminId
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rsdaId',
			type : 'int'
		}, {
			name : 'rsdaTitle',
			type : 'string'
		}{
			name : 'rltyName',
			type : 'string'
		}, {
			name : 'smanName',
			type : 'string'
		}, {
			name : 'rsdaBuilddate',
			type : 'string'
		}, {
			name : 'rltyId',
			type : 'int'
		}, {
			name : 'rsLaNum',
			type : 'string'
		}]),
		sortInfo : {
			field : 'rsdaBuilddate',
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
		header : '资源名称',
		width : 200,
		dataIndex : 'rsdaTitle',
		sortable : true
	}, {
		header : '资源类型',
		width : 100,
		dataIndex : 'rltyName',
		sortable : true
	}, {
		header : '创建者',
		width : 100,
		dataIndex : 'smanName',
		sortable : true
	}, {
		header : '创建时间',
		width : 120,
		dataIndex : 'rsdaBuilddate',
		sortable : true
	}, {
		header : '未通过资源数',
		width : 70,
		dataIndex : 'rsLaNum',
		sortable : true
	}]);
	
	var txt = userId > 0 ? "其它语" : "我来审核";
	// 创建工具条
	var tbar = new Ext.Toolbar([
	{
		text : txt,
		iconCls : 'common_details',
		handler : resSourceDataDetail
	}
	, '-', {
		text : '&nbsp;返&nbsp;回&nbsp;',
		iconCls : 'application_go',
		handler : function() {
			document.location.href = getPath() + '/console/data/otherLanguageMissionInfo.htm?action=initOtherLanguageMissionInfo';
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
		autoExpandColumn : 'rsdaBuilddate',
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
	grid.on('dblclick', resSourceDataDetail);
	// 详情
	function resSourceDataDetail() {
	    if(userId > 0){
			var selectRow = grid.getSelectionModel().getSelected();
			if(selectRow) {
				document.location.href = getPath() + 'console/data/otherLanguageMissionInfo.htm?action=initRsdOtherLanguage&rsdaId='
						+ selectRow.json.rsdaId+'&rminId='+rminId + "&userid=" + userId;
			}else
				alertWarring('请至少选择一行数据然后才能够操作！');
	    }else{
	        getMission();
	    } 

	}
	
		function getMission(){
			     Ext.Msg.confirm('提示', '您确定要领取该作业的审核任务吗？\n', function(button, text) {
						if(button == 'yes') {
							Ext.Ajax.request({
								url : getPath() + "console/data/resource/missionInfoManagement.htm?action=setMissionUser&userType=3&userid="+loginUser+"&missionid=" + rminId,
								waitTitle : "数据提交",
								waitMsg : "作业提交中，请稍候...",
		
								success : function(form, action) {
									window.location.href = getPath() + "console/data/otherLanguageMissionInfo.htm?action=initOtherLanguageResSourceData&rminId="+rminId+"&userid=" + loginUser;
								},
								failure : function(form, action) {
									Ext.Msg.alert('错误', "<span style='color:red'>对不起，操作失败！</span>");
								}
							});
						}
					})
		}
});