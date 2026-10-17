// -- 求助管理 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var sc_Win;
	var sc_Win_1;
	var sccoTitle_Condition;
	var loginName_Condition;
	var scit_Combox_Condition;;
	var scIssue_status_Combox_Condition;

	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/scConsultation.htm?action=getScConsultationList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'sccoId',// sccoId sccoTitle sctyName suinName suinLoginName smanName sccoCreationTime sccoReplyTime sccoState
			type : 'int'
		}, {
			name : 'sccoTitle',
			type : 'string'
		}, {
			name : 'sctyName',
			type : 'string'
		}, {
			name : 'suinName',
			type : 'string'
		}, {
			name : 'suinLoginName',
			type : 'string'
		}, {
			name : 'sccoCreationTime',
			type : 'string'
		}, {
			name : 'sccoState',
			type : 'string'
		}]),
		sortInfo : {
			field : 'sccoState',
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
	// sccoId sccoTitle sctyName suinName suinLoginName smanName sccoCreationTime sccoReplyTime sccoState
	// 定义列模型
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '标题',
		width : 200,
		dataIndex : 'sccoTitle',
		sortable : true
	}, {
		header : '分类',
		width : 100,
		dataIndex : 'sctyName',
		sortable : true
	}, {
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
		header : '咨询时间',
		width : 100,
		dataIndex : 'sccoCreationTime',
		sortable : true
	}, {
		header : '状态',
		width : 50,
		dataIndex : 'sccoState',
		sortable : true,
		align : 'center',
		renderer : checkState
	}]);
	// --查询条件:标题
	var sccoTitle_search = new Ext.form.TextField({
		name : 'sccoTitle_search',
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
	// 类型
	var scit_Store = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/scConsultation.htm?action=getScConsultationTypeCombox&type=1'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'value'
		}, {
			name : 'text'
		}])
	});
	var scit_Combox = new Ext.form.ComboBox({
		id : 'scit_Combox',
		fieldLabel : "咨询类型",
		mode : 'remote',
		store : scit_Store,
		emptyText : '请选择咨询类型',
		triggerAction : 'all',
		lazyInit : true,
		resizable : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		width : 100,
		listeners : {
			'select' : function() {
				searchMan();
			}
		}
	});
	// 状态
	var scConsultation_status_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[0, '不限'], [1, '未回复'], [2, '已回复']]
	});
	var scIssue_status_Combox = new Ext.form.ComboBox({
		fieldLabel : "状态",
		mode : 'local',
		store : scConsultation_status_store,
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

	var tb = new Ext.Toolbar(['标题：', sccoTitle_search, '-', '用户名：', loginName_search, '-', '类型：', scit_Combox, '-', '状态：',
	scIssue_status_Combox, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}]);
	var tbar1 = new Ext.Toolbar([{
		text : '&nbsp;回&nbsp;复&nbsp;',
		iconCls : 'common_details',
		handler : sc
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : delConsultation
	}, '-', {
		text : '&nbsp;添加到FAQ库&nbsp;',
		iconCls : 'common_add',
		handler : addToFaqLibrary
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
		bbar : pagingBar,
		listeners : {
			'render' : function() {
				tbar1.render(this.tbar);
			}
		}
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
	grid.on('dblclick', sc);
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			sccoTitle_Condition : sccoTitle_Condition,
			loginName_Condition : loginName_Condition,
			scit_Combox_Condition : scit_Combox_Condition,
			scIssue_status_Combox_Condition : scIssue_status_Combox_Condition
		});
	});
	// -------------------- 查看咨询 --------------------------
	// sccoId sctyName suinLoginName smanName suinName sccoTitle sccoContent sccoCreationTime sccoState sccoReplyContent sccoReplyTime
	var sc_sccoTitle = new Ext.form.TextField({
		fieldLabel : '标题',
		id : 'sccoTitle',
		name : 'sccoTitle',
		anchor : '95%',
		allowBlank : false,
		readOnly : true,
		blankText : '咨询标题不能为空!',
		maxLength : 300,
		maxLengthText : '咨询标题不得超过300个字符!'
	});

	var sc_scit_Store = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/scConsultation.htm?action=getScConsultationTypeCombox'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'value'
		}, {
			name : 'text'
		}])
	});
	var sc_scit_Combox = new Ext.form.ComboBox({
		id : 'sc_scit_Combox',
		fieldLabel : "咨询类型",
		mode : 'remote',
		store : sc_scit_Store,
		emptyText : '请选择咨询类型',
		triggerAction : 'all',
		hiddenName : 'sctyName',
		lazyInit : true,
		resizable : true,
		readOnly : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		anchor : '95%',
		allowBlank : false,
		blankText : '咨询类型不能为空!'
	});

	var sc_sccoState = new Ext.form.TextField({
		fieldLabel : '状态',
		id : 'sccoState',
		name : 'sccoState',
		anchor : '95%',
		readOnly : true
	});
	var sc_suinName = new Ext.form.TextField({
		fieldLabel : '咨询人',
		id : 'suinName',
		name : 'suinName',
		anchor : '95%',
		readOnly : true
	});
	var sc_sccoCreationTime = new Ext.form.TextField({
		fieldLabel : '咨询时间',
		id : 'sccoCreationTime',
		name : 'sccoCreationTime',
		anchor : '95%',
		readOnly : true
	});
	var sc_sccoContent = new Ext.form.TextArea({
		fieldLabel : '内容',
		id : 'sccoContent',
		name : 'sccoContent',
		anchor : '95%',
		height : 70,
		readOnly : true,
		maxLength : 8000,
		maxLengthText : '咨询内容不得超过8000个字符!'
	});
	var sc_sccoReplyContent = new Ext.form.TextArea({
		fieldLabel : '回复内容',
		id : 'sccoReplyContent',
		name : 'sccoReplyContent',
		anchor : '95%',
		height : 70,
		allowBlank : false,
		blankText : '咨询回复内容不能为空!',
		maxLength : 8000,
		maxLengthText : '咨询回复内容不得超过8000个字符!'
	});// sccoTitle sccoState sctyName suinName sccoCreationTime sccoContent sccoReplyContent \ sccoReplyTime smanName
	var sc_smanName = new Ext.form.TextField({
		fieldLabel : '回复人',
		id : 'smanName',
		name : 'smanName',
		anchor : '95%',
		readOnly : true
	});
	var sc_sccoReplyTime = new Ext.form.TextField({
		fieldLabel : '回复时间',
		id : 'sccoReplyTime',
		name : 'sccoReplyTime',
		anchor : '95%',
		readOnly : true
	});

	var sc_Form = new Ext.form.FormPanel({
		id : 'sc_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [sc_sccoTitle, sc_scit_Combox, sc_suinName, sc_sccoCreationTime, sc_sccoState, sc_smanName, sc_sccoContent,
				sc_sccoReplyContent, sc_sccoReplyTime],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveScConsultation
		}, {
			text : '删&nbsp;除',
			handler : delConsultation
		}, {
			text : '&nbsp;添加到FAQ库&nbsp;',
			handler : addToFaqLibrary
		}, {
			text : '&nbsp;返&nbsp;回&nbsp;',
			handler : function() {
				sc_Win.hide();
			}
		}]
	});

	// -------------------- 查看咨询 -------------------------------------------------------
	var sc_sccoTitle_1 = new Ext.form.TextField({
		fieldLabel : '标题',
		id : 'sc_sccoTitle_1',
		name : 'sc_sccoTitle_1',
		anchor : '95%',
		allowBlank : false,
		readOnly : true,
		blankText : '咨询标题不能为空!',
		maxLength : 300,
		maxLengthText : '咨询标题不得超过300个字符!'
	});

	var sc_scit_Combox_1 = new Ext.form.ComboBox({
		id : 'sc_scit_Combox_1',
		fieldLabel : "咨询类型",
		mode : 'remote',
		store : sc_scit_Store,
		emptyText : '请选择咨询类型',
		triggerAction : 'all',
		hiddenName : 'sctyId_1',
		lazyInit : true,
		resizable : true,
		readOnly : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		anchor : '95%',
		allowBlank : false,
		blankText : '咨询类型不能为空!'
	});

	var sc_sccoState_1 = new Ext.form.TextField({
		fieldLabel : '状态',
		id : 'sc_sccoState_1',
		name : 'sc_sccoState_1',
		anchor : '95%',
		readOnly : true
	});
	var sc_suinName_1 = new Ext.form.TextField({
		fieldLabel : '咨询人',
		id : 'sc_suinName_1',
		name : 'sc_suinName_1',
		anchor : '95%',
		readOnly : true
	});
	var sc_sccoCreationTime_1 = new Ext.form.TextField({
		fieldLabel : '咨询时间',
		id : 'sc_sccoCreationTime_1',
		name : 'sc_sccoCreationTime_1',
		anchor : '95%',
		readOnly : true
	});
	var sc_sccoContent_1 = new Ext.form.TextArea({
		fieldLabel : '内容',
		id : 'sc_sccoContent_1',
		name : 'sc_sccoContent_1',
		anchor : '95%',
		readOnly : true,
		height : 70,
		maxLength : 8000,
		maxLengthText : '咨询内容不得超过8000个字符!'
	});
	var sc_sccoReplyContent_1 = new Ext.form.TextArea({
		fieldLabel : '回复内容',
		id : 'sc_sccoReplyContent_1',
		name : 'sc_sccoReplyContent_1',
		anchor : '95%',
		height : 70,
		allowBlank : false,
		blankText : '咨询回复内容不能为空!',
		maxLength : 8000,
		maxLengthText : '咨询回复内容不得超过8000个字符!'
	});

	var sc_Form_1 = new Ext.form.FormPanel({
		id : 'sc_Form_1',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [sc_sccoTitle_1, sc_scit_Combox_1, sc_suinName_1, sc_sccoCreationTime_1, sc_sccoState_1, sc_sccoContent_1,
				sc_sccoReplyContent_1],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveScConsultation1
		}, {
			text : '删&nbsp;除',
			handler : delConsultation
		}, {
			text : '&nbsp;返&nbsp;回&nbsp;',
			handler : function() {
				sc_Win_1.hide();
			}
		}]
	});

	// -------------------- 自定义函数 -------------------------

	function sc() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var sccoState = selectRow.json.sccoState;
			if(sccoState == 1) {
				if(!sc_Win_1) {
					sc_Win_1 = new Ext.Window({
						id : 'sc_Win_1',
						closeAction : 'hide',
						maximizable : true,
						resizable : false,
						width : 550,
						height : 380,
						title : '咨询详细信息',
						layout : 'fit',
						modal : true,
						buttonAlign : 'center',
						items : [sc_Form_1],
						listeners : {
							hide : function() {
								sc_Form_1.form.reset();
							}
						}
					});
				}
				initData(selectRow.json.sccoId);
				sc_Win_1.show();
			}else {
				if(!sc_Win) {
					sc_Win = new Ext.Window({
						id : 'sc_Win',
						closeAction : 'hide',
						maximizable : true,
						resizable : false,
						width : 550,
						height : 420,
						title : '咨询详细信息',
						layout : 'fit',
						modal : true,
						buttonAlign : 'center',
						items : [sc_Form],
						listeners : {
							hide : function() {
								sc_Form.form.reset();
							}
						}
					});
				}
				initData(selectRow.json.sccoId);
				sc_Win.show();
			}

		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}

	function hideField(field) {
		field.disable();// for validation
		field.hide();
		field.getEl().up('.x-form-item').setDisplayed(false); // hide label
	}

	function showField(field) {
		field.enable();
		field.show();
		field.getEl().up('.x-form-item').setDisplayed(true);// show label
	}

	// 状态
	function checkState(val) {
		if(val == '1')
			return '<img src="' + getPath() + 'images/sc/sc_not_solve.png" title="未回复"  />';
		else if(val == '2') return '<img src="' + getPath() + 'images/sc/sc_solve.png" title="已回复"  />';
	}

	function searchMan() {
		sccoTitle_Condition = sccoTitle_search.getValue();
		loginName_Condition = loginName_search.getValue();
		scit_Combox_Condition = scit_Combox.getValue();
		scIssue_status_Combox_Condition = scIssue_status_Combox.getValue();

		store.load({
			params : {
				start : 0,
				limit : limitLength,
				sccoTitle_Condition : sccoTitle_Condition,
				loginName_Condition : loginName_Condition,
				scit_Combox_Condition : scit_Combox_Condition,
				scIssue_status_Combox_Condition : scIssue_status_Combox_Condition
			}
		});
	}

	function initData(sscoId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/scConsultation.htm?action=initConsultation',
			params : {
				sscoId : sscoId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					sc_scit_Store.reload();

					if(sc_Form) {
						sc_Form.form.setValues({
							sccoTitle : txt.sccoTitle,
							sccoState : txt.sccoState,
							sctyName : txt.sctyName,
							suinName : txt.suinName,
							sccoCreationTime : txt.sccoCreationTime,
							sccoContent : txt.sccoContent,
							sccoReplyContent : txt.sccoReplyContent,
							sccoReplyTime : txt.sccoReplyTime,
							smanName : txt.smanName
						});
					}
					if(sc_Form_1) {
						sc_Form_1.form.setValues({
							sc_sccoTitle_1 : txt.sccoTitle,
							sc_sccoState_1 : txt.sccoState,
							sc_scit_Combox_1 : txt.sctyName,
							sc_suinName_1 : txt.suinName,
							sc_sccoCreationTime_1 : txt.sccoCreationTime,
							sc_sccoContent_1 : txt.sccoContent
						});
					}

				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统出现未知错误！');
			}
		});
	}

	// 删除
	function delConsultation() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该咨询吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var sccoId = selectRow.json.sccoId;
					if(sc_Win) sc_Win.hide();
					if(sc_Win_1) sc_Win_1.hide();
					consultationDelete(sccoId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}

	function consultationDelete(sccoId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/scConsultation.htm?action=deleteScCosultation',
			params : {
				sccoId : sccoId
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
	function saveScConsultation() {
		if(sc_Form.form.isValid()) {
			sc_Form.getForm().submit({
				url : getPath() + 'console/data/scConsultation.htm?action=saveScConsultation',
				method : 'post',
				params : {
					sccoId : grid.getSelectionModel().getSelected().json.sccoId
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					sc_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	function saveScConsultation1() {
		if(sc_Form_1.form.isValid()) {
			sc_Form_1.getForm().submit({
				url : getPath() + 'console/data/scConsultation.htm?action=saveScConsultation1',
				method : 'post',
				params : {
					sccoId : grid.getSelectionModel().getSelected().json.sccoId
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					sc_Win_1.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	// ----------------------- 添加到FAQ库 ---------------------------------
	function addToFaqLibrary() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var sccoState = selectRow.json.sccoState;
			if(sccoState == '1') {
				alertWarring('该咨询还未回复，不能添加到FAQ库！');
				return;
			}else {
				addFaqLibrary();
			}
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 类型
	var scft_Store = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/scFaqLibrary.htm?action=getScFaqTypeCombox'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'value'
		}, {
			name : 'text'
		}])
	});
	var scft_Combox = new Ext.form.ComboBox({
		id : 'scft_Combox',
		fieldLabel : "分类",
		mode : 'remote',
		store : scft_Store,
		emptyText : '请选择分类',
		triggerAction : 'all',
		hiddenName : 'scftId',
		lazyInit : true,
		resizable : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		anchor : '95%',
		allowBlank : false,
		blankText : '分类不能为空！'
	});

	var addFaqLibrary_scflQuestion = new Ext.form.TextArea({
		fieldLabel : '问题',
		id : 'scflQuestion',
		name : 'scflQuestion',
		anchor : '95%',
		height : 80,
		allowBlank : false,
		blankText : '问题不能为空！',
		maxLength : 1024,
		maxLengthText : '问题不得超过1024个字符!'
	});

	var addFaqLibrary_scflAnswer = new Ext.form.TextArea({
		fieldLabel : '回答',
		id : 'scflAnswer',
		name : 'scflAnswer',
		anchor : '95%',
		height : 140,
		allowBlank : false,
		blankText : '回答不能为空！',
		maxLength : 1024,
		maxLengthText : '回答不得超过1024个字符!'
	});

	var addFaqLibrary_Form = new Ext.form.FormPanel({
		id : 'addFaqLibrary_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 50,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [addFaqLibrary_scflQuestion, scft_Combox, addFaqLibrary_scflAnswer],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveOrUpdateFaqLibrary
		}, {
			text : '取&nbsp;消',
			handler : function() {
				addFaqLibrary_Win.hide();
			}
		}]
	});
	var addFaqLibrary_Win;
	function addFaqLibrary() {
		if(!addFaqLibrary_Win) {
			addFaqLibrary_Win = new Ext.Window({
				closeAction : 'hide',
				maximizable : false,
				resizable : false,
				width : 500,
				height : 350,
				title : '添加咨询到FAQ库',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [addFaqLibrary_Form],
				listeners : {
					hide : function() {
						addFaqLibrary_Form.form.reset();
					}
				}
			});
		}
		initFaqData();
		addFaqLibrary_Win.show();
	}
	function initFaqData(sccoId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/scConsultation.htm?action=initConsultation',
			params : {
				sscoId : grid.getSelectionModel().getSelected().json.sccoId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					addFaqLibrary_Form.form.setValues({
						scflQuestion : txt.sccoTitle + '\n' + txt.sccoContent,
						scflAnswer : txt.sccoReplyContent,
						scftId : txt.sctyName2
					});
				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统出现未知错误！');
			}
		});
	}

	function saveOrUpdateFaqLibrary() {
		if(addFaqLibrary_Form.form.isValid()) {
			addFaqLibrary_Form.getForm().submit({
				url : getPath() + 'console/data/scConsultation.htm?action=addToFaqLibrary',
				method : 'post',
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					addFaqLibrary_Win.hide();
					// 隐蔽编辑窗口
					if(sc_Win) sc_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}

});