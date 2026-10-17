// -- 作业管理 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var rmstId_combox = null;
	var rminStatus_combox = null;
	var rminAdd_Win = null;
	var rminEdit_Win = null;
	var rminChange_Win = null;
	var isEditing = false;
	var url_rlty_store = getPath() + 'console/data/templetManager.htm?action=getSourceTypeByStore';
	var url_sysMan_store = getPath() + 'console/data/resource/missionInfoManagement.htm?action=getSysManagerList';
	var url_firstLanguage = getPath() + 'console/data/extdata.htm?action=language';
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/resource/missionInfoManagement.htm?action=getResMissionInfoList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rminId',
			type : 'int'
		}, {
			name : 'rmstId',
			type : 'int'
		}, {
			name : 'rmstName',
			type : 'string'
		}, {
			name : 'rminMissName',
			type : 'string'
		}, {
			name : 'rltyName',
			type : 'string'
		},
		/*
		 * { name : 'smanUseName', type : 'string' }, { name : 'smanCreateName', type : 'string' },
		 */
		{
			name : 'rminBuildDate',
			type : 'string'
		}, {
			name : 'rminEndDate',
			type : 'string'
		}, {
			name : 'rminNum',
			type : 'int'
		}, {
			name : 'rminStatus',
			type : 'string'
		}, {
			name : 'rminRemark',
			type : 'string'
		}, {
			name : 'rminNameremark',
			type : 'string'
		}]),
		sortInfo : {
			field : 'rminBuildDate',
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
		width : 120,
		dataIndex : 'rminMissName',
		sortable : true
	}, {
		header : '备注',
		width : 180,
		dataIndex : 'rminNameremark',
		sortable : true
	}, {
		header : '所属特色库',
		width : 180,
		dataIndex : 'rmstName',
		sortable : true
	}, {
		header : '资源类型',
		width : 150,
		dataIndex : 'rltyName',
		sortable : true
	},
	/*
	 * { header : '操作者', width : 120, dataIndex : 'smanUseName', sortable : true }, { header : '创建者', width : 120, dataIndex :
	 * 'smanCreateName', sortable : true },
	 */
	{
		header : '创建时间',
		width : 120,
		dataIndex : 'rminBuildDate',
		sortable : true
	}, {
		header : '完成时间',
		width : 120,
		dataIndex : 'rminEndDate',
		sortable : true
	}, {
		header : '资源数',
		width : 75,
		dataIndex : 'rminNum',
		sortable : true
	}, {
		header : '状态',
		width : 75,
		align : 'center',
		dataIndex : 'rminStatus',
		renderer : checkState,
		sortable : true
	}, {
		header : '描述',
		width : 180,
		dataIndex : 'rminRemark',
		sortable : true,
		renderer : function(v) {
			if(v == null) {
				return "";
			}
			if(v.length < 15) {
				return "<div title='" + v + "'>" + v + "</div>";
			}
			return "<div title='" + v + "'>" + v.substring(0, 15) + "</div>";
		}
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
	// 特色库下拉框
	var rms_store_select = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/extdata.htm?action=all_store_select&ptype=2'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'rdtyId',
			type : 'int'
		}, {
			name : 'rdtyName',
			type : 'string'
		}])
	});
	var rms_store_select_hasNull = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/extdata.htm?action=all_store_select&hasNull=hasNull&ptype=2'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'rdtyId',
			type : 'int'
		}, {
			name : 'rdtyName',
			type : 'string'
		}])
	});
	var rminAdd_Rlty_store = new Ext.data.Store({
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
		mode : 'remote',
		store : rms_store_select_hasNull,
		resizable : true,
		name : 'rdtyName',
		emptyText : '请选择特色库',
		triggerAction : 'all',
		valueField : 'rdtyId',
		selectOnFocus : true,
		displayField : 'rdtyName',
		editable : false,
		width : 100
	});
	
	var rlty_Combox = new Ext.form.ComboBox({
		id : 'rlty_Combox_Id',
		fieldLabel : "资源类型",
		mode : 'remote',
		store : rmin_Rlty_Store_hasNull,
		emptyText : '请选择资源类型',
		triggerAction : 'all',
		lazyInit : true,
		resizable : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		width : 120,
		listeners : {
			'select' : function() {
				searchMan();
			}
		}
	});
	// 作业管理状态下拉框
	var rmin_status_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[-1, '不限'], [1, '加工中'], [2, '待审核'], [3, '审核通过'], [4, '审核未通过']]
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
		width : 80,
		value : -1,
		listeners : {
			'select' : function() {
				searchMan();
			}
		}
	});
	// 创建工具条
	var tbar = new Ext.Toolbar(['特色库：', rms_Combox, '-', '状态：', rmin_status_Combox, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text : '新增作业',
		iconCls : 'common_add',
		handler : rminAdd
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : rminEdit
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : rminDel
	}, '-', {
		text : '查看资源',
		iconCls : 'common_list',
		handler : toSourceList
	}, '-', {
		text : '提交审核',
		iconCls : 'common_submit',
		handler : rminCheck
	}, '-', {
		text : '作业转交',
		iconCls : 'common_go',
		handler : missionInfoChange
	}, '-', {
		text : '重建索引',
		iconCls : 'common_shutdown',
		handler : reIndex
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
			cls : 'res_nocommit',
			width : 16
		}, {
			xtype : 'label',
			text : '加工中',
			style : 'padding-left: 3px;',
			width : 10
		}, {
			xtype : 'label',
			style : 'padding-left: 15px;',
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
	grid.on('dblclick', toSourceList);
	function toSourceList() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var mission_id = selectRow.get("rminId");
			window.location.href = getPath() + "console/data/inputSingle.htm?action=missionResourceList&rmin_id=" + mission_id;
		}else {
			alertWarring('请选择作业！');
		}
	}
	// ----------------------- 新增作业 ----------------------------
	var rminAdd_RminMissName = new Ext.form.TextField({
		fieldLabel : '作业名称',
		id : 'rminMissName',
		name : 'rminMissName',
		anchor : '95%',
		allowBlank : false,
		blankText : '作业名称不能为空!',
		maxLength : 40,
		maxLengthText : '作业名称不得超过40个字符!'
	});
	var rminAdd_RminNameremark = new Ext.form.TextField({
		fieldLabel : '备注',
		id : 'RminNameremark',
		name : 'RminNameremark',
		anchor : '95%',
		maxLength : 40,
		maxLengthText : '备注不得超过40个字符!'
	});
	var rminAdd_Rmst_Combox = new Ext.form.ComboBox({
		fieldLabel : "所属特色库",
		mode : 'remote',
		store : rms_store_select,
		hiddenName : 'rmstId',
		emptyText : '请选择特色库',
		triggerAction : 'all',
		valueField : 'rdtyId',
		displayField : 'rdtyName',
		selectOnFocus : true,
		anchor : '95%',
		editable : false,
		allowBlank : false,
		blankText : '特色库不能为空！'
	});
	var rminAdd_Rlty_Combox = new Ext.form.ComboBox({
		fieldLabel : "资源类型",
		mode : 'remote',
		store : rminAdd_Rlty_store,
		hiddenName : 'rltyId',
		emptyText : '请选择资源类型',
		triggerAction : 'all',
		lazyInit : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		anchor : '95%',
		allowBlank : false,
		blankText : '资源类型不能为空！'
	});
	// 第一语种下拉选项store
	var firstLanguageStore = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : url_firstLanguage
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'value',
			type : 'int'
		}, {
			name : 'text',
			type : 'string'
		}])
	});
	var languageId = new Ext.form.ComboBox({
		fieldLabel : "第一语种",
		mode : 'remote',
		store : firstLanguageStore,
		hiddenName : 'languageId',
		emptyText : '请选择...',
		triggerAction : 'all',
		valueField : 'value',
		displayField : 'text',
		selectOnFocus : true,
		anchor : '95%',
		editable : false,
		allowBlank : false,
		blankText : '请选择作业第一语种!'
	});
	var rminAdd_Remark = new Ext.form.TextArea({
		id : 'rminAdd_Remark',
		name : 'rminRemark',
		fieldLabel : '描述',
		height : 100,
		maxLength : 350,
		anchor : '95%',
		maxLength : 350,
		maxLengthText : '作业描述不得超过350个字符!'
	});
	var rminAdd_Form = new Ext.form.FormPanel({
		id : 'rminAdd_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [rminAdd_RminMissName, rminAdd_RminNameremark, rminAdd_Rmst_Combox, rminAdd_Rlty_Combox, languageId, rminAdd_Remark],
		buttons : [{
			text : '保&nbsp;存',
			handler : function() {
				saveOrUpdateRMS('save');
			}
		}, {
			text : '重&nbsp;置',
			handler : function() {
				if(isEditing)
					initRminData();
				else
					reset();
			}
		}, {
			text : '取&nbsp;消',
			handler : function() {
				if(isEditing)
					rminEdit_Win.hide();
				else
					rminAdd_Win.hide();
			}
		}]
	});
	// 当特色发生改变时，级联改变特色库所联系的资源类型
	rminAdd_Rmst_Combox.on('select', function(comboBox) {
		var id = comboBox.getValue();
		rminAdd_Rlty_Combox.clearValue(); // 清空之前的资源类型
		rminAdd_Rlty_store.proxy = new Ext.data.HttpProxy({
			url : url_rlty_store + "&store=" + id
		});
		rminAdd_Rlty_store.load();
	});
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
	// ---------------------------- 任务转交 ----------------------------------
	var rminAdd_SysMan_store = new Ext.data.Store({
		autoLoad : true,
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
	// ---------------------------- 自定义函数 ----------------------------------
	// 作业状态
	function checkState(val) {
		if(val == '1')
			return '<img src="' + getPath() + 'images/res_minfo/res_nocommit.png" title="加工中" width="16" height="16" />';
		else if(val == '2')
			return '<img src="' + getPath() + 'images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
		else if(val == '3')
			return '<img src="' + getPath() + 'images/res_minfo/res_pass.png" title="审核通过" width="16" height="16" />';
		else if(val == '4')
			return '<img src="' + getPath() + 'images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
		else if(val == '5') return '<img src="' + getPath() + 'images/res_minfo/res_deliver.png" title="作业转交中" width="16" height="16" />';
	}
	// 新增作业
	function rminAdd() {
		if(!rminAdd_Win) {
			rminAdd_Win = new Ext.Window({
				closeAction : 'hide',
				maximizable : true,
				resizable : true,
				width : 400,
				height : 330,
				title : '新增作业',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [rminAdd_Form],
				listeners : {
					hide : function() {
						reset();
					}
				}
			});
		}
		rminAdd_Win.add(rminAdd_Form);
		rminAdd_Win.show();
		getMissionName();
		//alert(missionName);
		//rminAdd_RminNameremark.setValue(missionName);
	}
	
	function getMissionName(){
	    Ext.Ajax.request({ 
			url: getPath() + 'console/data/resource/missionInfoManagement.htm?action=getMissionName',
			method: 'GET',   
   			text: "Updating...",     
   			success: function (result, request ){
   			    var missionName = result.responseText;
   			    rminAdd_RminMissName.setValue(missionName);
   			},
   			failure : function(form, action) {
   				var missionName = result.responseText;
   			    rminAdd_RminMissName.setValue(missionName);
   			}
		}); 
	}
	// 编辑作业
	function rminEdit() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			// 校验作业是否存在资源，存在资源的作业不能编辑或者删除
			var rminNum = selectRow.json.rminNum;
			if(rminNum > 0) {
				alertWarring('该作业下已经存在资源，不允许再编辑！');
				return;
			}

			isEditing = true;
			if(!rminEdit_Win) {
				rminEdit_Win = new Ext.Window({
					closeAction : 'hide',
					width : 550,
					height : 350,
					title : '编辑作业',
					layout : 'fit',
					modal : true,
					resizable : true,
					maximizable : true,
					buttonAlign : 'center',
					items : [rminAdd_Form],
					listeners : {
						hide : function() {
							isEditing = false;
							reset();
						}
					}
				});
			}
			rminEdit_Win.add(rminAdd_Form);
			initRminData();
			rminEdit_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 单条资源录入
	function rminInput() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var mission_id = selectRow.get("rminId");
			window.location.href = getPath() + "console/data/inputSingle.htm?action=initData&req=1&rmin_id=" + mission_id;
		}else {
			alertWarring('请选择需要录入的作业！');
		}
	}
	// 提交作业审核
	function rminCheck() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var missionStatu = selectRow.get("rminStatus");
			var sourceNum = selectRow.get("rminNum");
			if(missionStatu == 2) {
				Ext.Msg.alert('错误', "<span style='color:red'>该作业已提交！</span>");
				return;
			}
			if(missionStatu == 3) {
				Ext.Msg.alert('错误', "<span style='color:red'>该作业已完成！</span>");
				return;
			}
			if(sourceNum < 1) {
				Ext.Msg.alert('错误', "<span style='color:red'>提交失败，该作业没有任何已加工资源！</span>");
				return;
			}
			Ext.Msg.confirm('提示', '您确定已加工完该作业并提交吗？\n', function(button, text) {
				if(button == 'yes') {
					var mission_id = selectRow.get("rminId");
					Ext.Ajax.request({
						url : getPath() + "console/data/resource/missionInfoManagement.htm?action=setMissionStatus&statu=2&missionid="
								+ mission_id,
						waitTitle : "数据提交",
						waitMsg : "作业提交中，请稍候...",
						success : function(form, action) {
							Msg.success('提交成功，请等待审核！');
							store.reload()
						},
						failure : function(form, action) {
							Ext.Msg.alert('错误', "<span style='color:red'>对不起，提交失败！</span>");
						}
					});
				}
			})
		}else {
			Ext.Msg.alert("提示", "请点击选择需要提交审核的作业!");
		}
	}
	// 重置表单
	function reset() {
		rminAdd_Form.form.reset();
		rminAdd_Rmst_Combox.clearValue();
		rminAdd_Rlty_store.removeAll();
	}
	// 操作作业数据
	function saveOrUpdateRMS(type) {
		if(rminAdd_Form.form.isValid()) {
			rminAdd_Form.getForm().submit({
				url : getPath() + 'console/data/resource/missionInfoManagement.htm?action=saveOrUpdateResMissionInfo',
				method : 'post',
				params : {
					isEditing : isEditing,
					rminId : isEditing == true ? grid.getSelectionModel().getSelected().json.rminId : 0
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					//alertSuccess(action.result.msg);
					Msg.success(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					if(isEditing)
						rminEdit_Win.hide();
					else
						rminAdd_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	// 初始化窗口
	function initRminData() {
		var rminId = grid.getSelectionModel().getSelected().json.rminId;
		Ext.Ajax.request({
			url : getPath() + 'console/data/resource/missionInfoManagement.htm?action=getResMissionInfo',
			params : {
				rminId : rminId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					// 初始化表单数据
					rminAdd_Form.getForm().findField('rminMissName').setValue(txt.rminMissName);
					rminAdd_Form.getForm().findField('RminNameremark').setValue(txt.rminNameremark);
					rminAdd_Form.getForm().findField('rminRemark').setValue(txt.rminRemark);
					rminAdd_Form.getForm().findField('rmstId').setValue(txt.rminRmstId);
					rminAdd_Form.getForm().findField('rltyId').setValue(txt.rminRltyName);
					rminAdd_Form.getForm().findField('languageId').setValue(txt.languageId);
					// 重新加载特色库及资源类型
					rms_store_select.load();
					rminAdd_Rlty_store.proxy = new Ext.data.HttpProxy({
						url : url_rlty_store + "&store=" + txt.rminRmstId
					});
					rminAdd_Rlty_store.load();
				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统忙，请稍后访问！');
			}
		});
	}
	// 删除作业
	function rminDel() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			// 校验作业是否存在资源，存在资源的作业不能编辑或者删除
			var rminNum = selectRow.json.rminNum;
			if(rminNum > 0) {
				alertWarring('该作业下已经存在资源，不允许删除！');
				return;
			}

			var rminId = selectRow.json.rminId;
			Ext.Msg.confirm("确认消息框", "您确定要删除该作业吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					rminDelete(rminId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	function rminDelete(rminId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/resource/missionInfoManagement.htm?action=delResMissionInfo',
			params : {
				rminId : rminId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					Msg.success(txt.msg);
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
	// 任务转交
	function missionInfoChange() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var missionStatu = selectRow.get("rminStatus");
			if(missionStatu == 2) {
				Ext.Msg.alert('错误', "<span style='color:red'>该作业已提交,不能转交！</span>");
				return;
			}
			if(missionStatu == 3) {
				Ext.Msg.alert('错误', "<span style='color:red'>该作业已完成,不能转交！</span>");
				return;
			}
			var missionStatu = selectRow.get("rminStatus");
			if(missionStatu > 1) {
				Ext.Msg.alert('错误', "<span style='color:red'>该作业已提交,不能再转交！</span>");
				return;
			}
			var rminId = selectRow.json.rminId;
			var rmstId = selectRow.json.rmstId;
			// 加载操作者数据
			rminAdd_SysMan_store.proxy = new Ext.data.HttpProxy({
				url : url_sysMan_store + "&rminId=" + rminId
			});
			rminAdd_SysMan_store.load();
			handlerMissionInfoChange();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	function handlerMissionInfoChange() {
		if(!rminChange_Win) {
			rminChange_Win = new Ext.Window({
				closeAction : 'hide',
				resizable : false,
				width : 300,
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
				var rminId = selectRow.json.rminId;
				rminChange_Form.getForm().submit({
					url : getPath() + 'console/data/resource/missionInfoManagement.htm?action=saveRminChange',
					method : 'post',
					params : {
						rminId : rminId
					},
					waitMsg : '正在保存数据，请稍候...',
					success : function(form, action) {
						// 提示信息
						alertSuccess(action.result.msg);
						// 刷新列表
						searchMan();
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
	
	
	//重建该作业下资源到索引
	function reIndex() {
		var myMask = new Ext.LoadMask(Ext.getBody(), {
				msg : '索引重建中...请稍后！',
				removeMask : true // 完成后移除
		});
		
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var rminStatu = selectRow.get("rminStatus");
			if(rminStatu != 3){
			    Ext.Msg.alert("提示", '该作业状态不允许创建索引！');
			    return;
			}
			Ext.Msg.confirm('提示','是否重建该作业下所有资源到索引？\n',function(button,text){    
		        if(button == "yes"){
					myMask.show();
					var rminId = selectRow.get("rminId");
				    Ext.Ajax.request({ 
						url: getPath() + 'console/data/rmsMissionInfoBatchCheck.htm?action=reMissionIndex&rminId='+rminId,
						method: 'GET',   
		       			text: "Updating...",     
		       			success: function (result, request ){
		       				myMask.hide();
		       				var txt = Ext.util.JSON.decode(result.responseText);
							if(txt.success) {
								alertSuccess(txt.msg);
							}else {
								alertError(txt.msg);
							}
		       			}
					});
				}
	     	});
		}else {
			Ext.Msg.alert("提示", '请点击选择需要重建索引的作业！');
		}
	}
});