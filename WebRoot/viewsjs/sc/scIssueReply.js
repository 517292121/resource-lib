// -- 求助回复 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	// 构造数据
	// 问题类型、问题标题、回复内容、回复人、回复时间、满意度；
	// scirId scitName scisTitle scirContent suinName scirReplyTime scirGrade
	var scitId_Combox;
	var scirGrade_Combox;
	var scisTitle_Condition;
	var scir_Win;

	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/scIssueReply.htm?action=getScIssueReplyList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'scirId',
			type : 'int'
		}, {
			name : 'scitName',
			type : 'string'
		}, {
			name : 'scisTitle',
			type : 'string'
		}, {
			name : 'suinName',
			type : 'string'
		}, {
			name : 'scirReplyTime',
			type : 'string'
		}, {
			name : 'scirGrade',
			type : 'string'
		}, {
			name : 'scirIsuse',
			type : 'string'
		}, {
			name : 'scirContent',
			type : 'string'
		}]),
		sortInfo : {
			field : 'scirReplyTime',
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
		dataIndex : 'scisTitle',
		sortable : true
	}, {
		header : '类型',
		width : 100,
		dataIndex : 'scitName',
		sortable : true
	}, {
		header : '回复内容',
		width : 200,
		dataIndex : 'scirContent',
		sortable : true
	}, {
		header : '回复人',
		width : 100,
		dataIndex : 'suinName',
		sortable : true
	}, {
		header : '回复时间',
		width : 100,
		dataIndex : 'scirReplyTime',
		sortable : true
	}, {
		header : '满意度',
		width : 100,
		dataIndex : 'scirGrade',
		sortable : true,
		renderer : checkGrade
	}, {
		header : '是否可用',
		width : 50,
		dataIndex : 'scirIsuse',
		sortable : true,
		align : 'center',
		renderer : checkIsuse
	}]);

	// --查询条件:标题
	var scisTitle_search = new Ext.form.TextField({
		name : 'scisTitle_condition',
		width : 180,
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
			url : getPath() + 'console/scIssue.htm?action=getScIssueTypeCombox'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'value'
		}, {
			name : 'text'
		}])
	});
	var scit_Combox = new Ext.form.ComboBox({
		id : 'scit_Combox',
		fieldLabel : "求助类型",
		mode : 'remote',
		store : scit_Store,
		emptyText : '请选择求助类型',
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
	// 求助状态
	var grade_store = new Ext.data.SimpleStore({
		fields : ['value', 'text'],
		data : [[-1, '不限'], [1, '1星'], [2, '2星'], [3, '3星'], [4, '4星'], [5, '5星']]
	});
	var grade_Combox = new Ext.form.ComboBox({
		fieldLabel : "满意度",
		mode : 'local',
		store : grade_store,
		emptyText : '不限',
		triggerAction : 'all',
		editable : false,
		valueField : 'value',
		displayField : 'text',
		width : 120,
		value : -1,
		listeners : {
			'select' : function() {
				searchMan();
			}
		}
	});
	var tb = new Ext.Toolbar(['标题：', scisTitle_search, '-', '类型：', scit_Combox, '-', '满意度：', grade_Combox, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}]);

	var tbar1 = new Ext.Toolbar([{
		text : '&nbsp;查&nbsp;看&nbsp;',
		iconCls : 'common_details',
		handler : scir
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : delScIssueType
	}, '-', {
		text : '&nbsp;启用开关&nbsp;',
		iconCls : 'common_shutdown',
		handler : isOpen
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
	grid.on('dblclick', scir);
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			scitId_Combox : scitId_Combox,
			scirGrade_Combox : scirGrade_Combox,
			scisTitle_Condition : scisTitle_Condition
		});
	});
	// ----------------------- 查看 ----------------------------
	// scirId scitName scisTitle scirContent suinName scirReplyTime scirGrade
	var sc_scisTitle = new Ext.form.TextField({
		fieldLabel : '求助标题',
		id : 'scisTitle',
		name : 'scisTitle',
		anchor : '95%',
		readOnly : true
	});
	var sc_scitName = new Ext.form.TextField({
		fieldLabel : '分类',
		id : 'scitName',
		name : 'scitName',
		anchor : '95%',
		readOnly : true
	});
	var sc_suinName = new Ext.form.TextField({
		fieldLabel : '求助人',
		id : 'suinName',
		name : 'suinName',
		anchor : '95%',
		readOnly : true
	});
	var sc_scirReplyTime = new Ext.form.TextField({
		fieldLabel : '回复时间',
		id : 'scirReplyTime',
		name : 'scirReplyTime',
		anchor : '95%',
		readOnly : true
	});
//	var sc_scirGrade = new Ext.form.TextField({
//		fieldLabel : '满意度',
//		id : 'scirGrade',
//		name : 'scirGrade',
//		anchor : '95%',
//		readOnly : true
//	});
	var sc_scirIsuse = new Ext.form.TextField({
		fieldLabel : '是否可用',
		id : 'scirIsuse',
		name : 'scirIsuse',
		anchor : '95%',
		readOnly : true
	});
	var sc_scirContent = new Ext.form.TextArea({
		fieldLabel : '回复内容',
		id : 'scirContent',
		name : 'scirContent',
		anchor : '95%',
		readOnly : true,
		height : 130
	});

	var sc_scirAttachmentName = {
		xtype : 'panel',
		region : 'center',
		fieldLabel : '附件下载',
		height : 25,
		anchor : '95%',
		id : 'scirAttachmentName',
		name : 'scirAttachmentName',
		html : ' <div id="DocPreview" style="padding:0 0 0 5px;"></div>'
	};
	
	var sc_scirGrade = {
		xtype : 'panel',
		region : 'center',
		fieldLabel : '满意度',
		height : 25,
		anchor : '95%',
		id : 'scirGrade',
		name : 'scirGrade',
		html : ' <div id="scirGrade" style="padding:0 0 0 5px;"></div>'
	};

	var scir_Form = new Ext.form.FormPanel({
		id : 'scir_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [sc_scisTitle, sc_scitName, sc_suinName, sc_scirReplyTime, sc_scirGrade, sc_scirIsuse, sc_scirContent,
				sc_scirAttachmentName],
		buttons : [{
			text : '删&nbsp;除',
			handler : delScIssueType
		}, {
			text : '&nbsp;返&nbsp;回&nbsp;',
			handler : function() {
				scir_Win.hide();
			}
		}]
	});
	// ----------------------- 自定义函数 -----------------------
	function scir() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			if(!scir_Win) {
				scir_Win = new Ext.Window({
					closeAction : 'hide',
					maximizable : true,
					resizable : false,
					width : 550,
					height : 400,
					title : '求助回复信息',
					layout : 'fit',
					modal : true,
					buttonAlign : 'center',
					items : [scir_Form],
					listeners : {
						beforehide : function() {
							scir_Form.form.reset();
							var docDiv = document.getElementById("DocPreview");
							if(docDiv) docDiv.innerHTML = '';
						}
					}
				});
			}
			initData(selectRow.json.scirId);
			scir_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	function initData(scirId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/scIssueReply.htm?action=initScIssueReplyDetail',
			params : {
				scirId : scirId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					scir_Form.form.setValues({
						scisTitle : txt.scisTitle,
						scitName : txt.scitName,
						scirReplyTime : txt.scirReplyTime,
						suinName : txt.suinName,
						scirIsuse : txt.scirIsuse,
						scirContent : txt.scirContent
					});
					if(txt.scirAttachmentName && txt.scirAttachmentName.length > 0) showDoc(txt.scirAttachmentName, txt.scirAttachmentUrl);
					// 显示满意度
					showIMG(txt.scirGrade);
					
				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError('系统出现未知错误！');
			}
		});
	}
	// 给下载单元格定义样式
	function showDoc(fileName, fileUrl) {
		var docDiv = document.getElementById("DocPreview");
		if(docDiv) docDiv.innerHTML = '<a href="' + downLoadPath + fileUrl + '" >' + fileName + '</a>';
	}
	
	function showIMG(star){
		var temp = document.getElementById("scirGrade");
		if(temp){
			var result = "";
			if(star && star > 0){
				for (var i = 0; i < star; i++) {
					result += '★';
				}
			}
			temp.innerHTML = '<span id="star" style="color: #FF0000; font-weight:bolder">'+result+'</span>';
		}
	}
	

	// 开关
	function checkIsuse(val) {
		if(val == 'true')
			return '<img src="' + getPath() + 'images/icon/yes.png" title="可用"  />';
		else
			return '<img src="' + getPath() + 'images/icon/no.png" title="禁用"  />';
	}
	function searchMan() {
		scitId_Combox = scit_Combox.getValue();
		scirGrade_Combox = grade_Combox.getValue();
		scisTitle_Condition = scisTitle_search.getValue();
		store.load({
			params : {
				start : 0,
				limit : limitLength,
				scitId_Combox : scitId_Combox,
				scirGrade_Combox : scirGrade_Combox,
				scisTitle_Condition : scisTitle_Condition
			}
		});
	}

	// 启用开关
	function isOpen() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var scirId = selectRow.json.scirId;
			var scirIsuse = selectRow.json.scirIsuse;
			openScIssueReply(scirId, scirIsuse);
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}

	function openScIssueReply(scirId, scirIsuse) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/scIssueReply.htm?action=openScIssueReply',
			params : {
				scirId : scirId,
				scirIsuse : scirIsuse
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
	// 删除
	function delScIssueType() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该回复吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					if(scir_Win) scir_Win.hide();
					var scirId = selectRow.json.scirId;
					scIssueReplyDelete(scirId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}

	function scIssueReplyDelete(scirId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/scIssueReply.htm?action=deleteIssueReply',
			params : {
				scirId : scirId
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
	
	function checkGrade(val){
		if(val == 1)
			return '<img src="' + getPath() + 'images/start1.gif" title="1星"  />';
		else if(val == 2)
			return '<img src="' + getPath() + 'images/start2.gif" title="2星"  />';
		else if(val == 3)
			return '<img src="' + getPath() + 'images/start3.gif" title="3星"  />';
		else if(val == 4)
			return '<img src="' + getPath() + 'images/start4.gif" title="4星"  />';
		else if(val == 5)
			return '<img src="' + getPath() + 'images/start5.gif" title="5星"  />';
		else
			return '<img src="' + getPath() + 'images/start0.gif"  />';
	}
});