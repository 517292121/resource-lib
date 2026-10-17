// -- 工作量统计 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var endTime = null;
	var beginTime = null;
	var suinLoginName_condition = null;
	var suinName_condition = null;
	var type_Combox_condition = null;
	var rmst_Combox_condition = null;
	var defaltSort = 'rsdaCount';
	var defaltDir = 'DESC';
	
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/count/resourceHandlerCount.htm?action=getResourceHandlerCountList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rsla_username',
			type : 'string'
		}, {
			name : 'rsla_authorname',
			type : 'string'
		}, {
			name : 'rsla_isfirst_status',
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
		header : '登录名',
		width : 100,
		dataIndex : 'rsla_username',
		sortable : true
	}, {
		header : '姓名',
		width : 100,
		dataIndex : 'rsla_authorname',
		sortable : true
	}, {
		header : '工作类型',
		width : 100,
		dataIndex : 'rsla_isfirst_status',
		sortable : true,
		renderer : checkState
	}, {
		header : '工作量（条）',
		width : 100,
		dataIndex : 'rsdaCount',
		sortable : true
	}]);
	
	var suinLoginName = new Ext.form.TextField({
		fieldLabel : '登录名',
		id : 'suinLoginName',
		name : 'suinLoginName',
		anchor : '95%',
		maxLength : 40,
		maxLengthText : '登录名不得超过40个字符!'
	});
	
	var suinName = new Ext.form.TextField({
		fieldLabel : '姓名',
		id : 'suinName',
		name : 'suinName',
		anchor : '95%',
		maxLength : 40,
		maxLengthText : '姓名不得超过40个字符!'
	});
	
	var beginTimeField = new Ext.form.DateField({ 
		id : 'beginTime',
		emptyText : '开始时间',
		width : 150,
		format : 'Y-m-d'
	});
	
	var endTimeField = new Ext.form.DateField({ 
		id : 'endTime',
		emptyText : '结束时间',
		width : 150,
		format : 'Y-m-d'
	});
	
	var type_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[0, '不限'],[1, '第一语加工'], [2, '其它语加工'],[3, '第一语审核'], [4, '其它语审核']]
	});
	var type_Combox = new Ext.form.ComboBox({
		fieldLabel : "工作类型",
		mode : 'local',
		store : type_store,
		emptyText : '不限',
		triggerAction : 'all',
		editable : false,
		valueField : 'value',
		displayField : 'text',
		width : 150,
		value : 0
	});
	
	var rms_store = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/extdata.htm?action=all_store&hasNull=hasNull'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'rdtyId',
			type : 'int'
		}, {
			name : 'rdtyName',
			type : 'string'
		}])
	});
	
	var rmst_Combox = new Ext.form.ComboBox({
		fieldLabel : "特色库",
		mode : 'remote',
		store : rms_store,
		resizable : true,
		name : 'rdtyName',
		emptyText : '请选择特色库',
		triggerAction : 'all',
		valueField : 'rdtyId',
		selectOnFocus : true,
		displayField : 'rdtyName',
		editable : false,
		width : 150
	});

	var tbar = new Ext.Toolbar(['登录名：',suinLoginName,'-','姓名：',suinName,'-','特色库：',rmst_Combox,'-','工作类型：',type_Combox]);
	
	var tbar2 = new Ext.Toolbar(['开始时间：', beginTimeField,'-','结束时间：',endTimeField,'-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	},'-',{ 
		text: '导出到Excel文件', 
		iconCls:'excel',
		handler: function(){
			var tempEndTime = '0';
			var tempBeginTime = '0';
			if(endTime){
				tempEndTime = Date.parse(endTime).toString();
			}
			if(beginTime){
				tempBeginTime = Date.parse(beginTime).toString();
			}
			
			 var url = getPath() + 'console/count/resourceHandlerCount.htm?action=exportToExcel&suinName='
			 +suinName_condition+'&suinLoginName='+suinLoginName_condition+'&type='+type_Combox_condition+'&rmstId='+rmst_Combox_condition
			 +'&sort='+defaltSort+'&dir='+defaltDir+'&endTime='+tempEndTime+'&beginTime='+tempBeginTime;
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
//		bbar : pagingBar,
		listeners : {
			'render' : function() {
				tbar2.render(this.tbar); 
			}
		}
	});
	// 创建主面板
	var displayPanel = new Ext.Viewport({
		layout : 'border',
		items : [grid]
	});

	function searchMan() {
		if(isValid()) {
			endTime = endTimeField.getValue();
			beginTime = beginTimeField.getValue();
			suinLoginName_condition = suinLoginName.getValue();
			suinName_condition = suinName.getValue();
			type_Combox_condition = type_Combox.getValue();
			rmst_Combox_condition = rmst_Combox.getValue();
			store.reload({
				params : {
					start : 0,
					limit : limitLength,
					endTime : endTime,
					beginTime : beginTime,
					suinLoginName : suinLoginName_condition,
					suinName : suinName_condition,
					type : type_Combox_condition,
					rmstId : rmst_Combox_condition
				}
			});
		}
	}
	
	store.on('beforeload', function() {
		defaltSort = store.getSortState().field;
		defaltDir = store.getSortState().direction;
		Ext.apply(this.baseParams, {
			endTime : endTime,
			beginTime : beginTime,
			suinLoginName : suinLoginName_condition,
			suinName : suinName_condition,
			type : type_Combox_condition,
			rmstId : rmst_Combox_condition,
			defaltSort : defaltSort,
			defaltDir : defaltDir
		});
	});
	
	store.load({
		params : {
			start : 0,
			limit : limitLength
		}
	});

	function isValid() {
		var beginTime = Ext.getCmp('beginTime').getValue();
		var endTime = Ext.getCmp('endTime').getValue();
		if(!Ext.getCmp('beginTime').isValid()) {
			alertError('起始时间格式不正确！');
			return false;
		}
		if(!Ext.getCmp('endTime').isValid()) {
			alertError('终止时间格式不正确！');
			return false;
		}
		if(!(Ext.isEmpty(beginTime) || Ext.isEmpty(endTime))) {
			if(beginTime > endTime) {
				alertError('开始时间要在结束时间前！');
				return false;
			}
		}
		return true;
	}
	function checkState(val) {
		var isFirst = val.substring(0,1);
		var status = val.substring(1,2);
		if(isFirst == '1' && status == '1'){
			return '第一语加工';
		}else if(isFirst == '0' && status == '1'){
			return '其它语加工';
		}else if(isFirst == '1' && status == '2'){
			return '第一语审核';
		}else if(isFirst == '0' && status == '2'){
			return '其它语审核';
		}else{
			return '工作类型不明';	
		}
	
	}
	
});