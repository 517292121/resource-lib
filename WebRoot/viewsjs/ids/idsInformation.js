// -- 信息管理 --
Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	var idsiAdd_Win;
	var idsiEdit_Win;
	// 构造数据
	var store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/idsInformation.htm?action=getIdsIList'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'idsiId',
			type : 'int'
		}, {
			name : 'idsiIdstId',
			type : 'int'
		}, {
			name : 'idstName',
			type : 'string'
		}, {
			name : 'smanName',
			type : 'string'
		}, {
			name : 'idsiTitle',
			type : 'string'
		}, {
			name : 'idsiDate',
			type : 'string'
		}, {
			name : 'idsiHitCount',
			type : 'string'
		}]),
		sortInfo : {
			field : 'idsiDate',
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
	
	var groupstore = new Ext.data.Store({
		autoLoad : true,
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/idsInformation.htm?action=getGroupJson'
		}),
		reader : new Ext.data.ArrayReader({}, [
			{name : 'value',type : 'string'}, 
			{name : 'text',type : 'string'}
		])
	});
	groupstore.on('load', function(comboBox){	
		groupCombox.setValue(idstIds);
	})
	var groupCombox = new Ext.form.ComboBox({
		mode : 'remote',
		store : groupstore,
		resizable : true,
		name : 'groupName',
		emptyText : '请先选类型...',
		triggerAction : 'all',
		valueField : 'value',
		selectOnFocus : true,
		displayField : 'text',
		editable : true,
		width : 120,
	});
	

	
	// 定义列模型
	var cm = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), {
		header : '标题',
		width : 300,
		dataIndex : 'idsiTitle',
		sortable : true
	}, {
		header : '分类',
		width : 100,
		dataIndex : 'idstName',
		sortable : true
	}, {
		header : '发布人',
		width : 120,
		dataIndex : 'smanName',
		sortable : true
	}, {
		header : '发布时间',
		width : 120,
		dataIndex : 'idsiDate',
		sortable : true
	}, {
		header : '点击量',
		width : 50,
		dataIndex : 'idsiHitCount',
		sortable : true
	}]);
	// 查询
	var searchMan = function() {
		if(idsTitle_Condition.trim()=="" || idsTitle_Condition!=idsTitle_search.getValue()){		
		  idsTitle_Condition = idsTitle_search.getValue();
		}
		if(idstIds.trim()=="" || idstIds !=groupCombox.getValue()){
		  idstIds = groupCombox.getValue();
		}
		store.reload({
			params : {
				start : 0,
				limit : limitLength,
				idsTitle_Condition : idsTitle_Condition,
				idstId:idstIds
			}
		});
	}

	// --查询条件:标题
	var idsTitle_search = new Ext.form.TextField({
		width : 180,
		name : 'idsTitle_Condition',
		emptyText : '多条件可用逗号或者空格隔开!',
		value:idsTitle_Condition,
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
	});

	// 创建工具条
	var tbar = new Ext.Toolbar([
		'分类：', groupCombox,
		'标题：', idsTitle_search, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : idsiAdd
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : idsiEdit
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : idsiDelete
	} , '-', { 
		text: 'Excel导入', 
		enableToggle: false, 
		iconCls:'excel', 
		handler: impExcel 
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
		autoExpandColumn : 'idsiTitle',
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
			start : start,
			limit : limitLength,
			idsTitle_Condition : idsTitle_Condition,
			idstId:idstIds
		}
	});
	store.on('beforeload', function() {
		Ext.apply(this.baseParams, {
			idsTitle_Condition : idsTitle_Condition
		});
	});
	grid.on('dblclick', idsiEdit);
	// ----------------------- 新增信息 ----------------------------
	var idsiType_Store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/idsInformation.htm?action=getIdsITCombox'
		}),
		reader : new Ext.data.ArrayReader({}, [{
			name : 'value'
		}, {
			name : 'text'
		}])
	});
	var idsiType_Combox = new Ext.form.ComboBox({
		fieldLabel : "信息分类",
		mode : 'remote',
		store : idsiType_Store,
		hiddenName : 'idsiIdstId',
		emptyText : '请选择信息分类',
		triggerAction : 'all',
		lazyInit : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		anchor : '95%',
		allowBlank : false,
		blankText : '信息分类不能为空！'
	});
	var idsiTitle = new Ext.form.TextField({
		fieldLabel : '标题',
		id : 'idsiTitle',
		name : 'idsiTitle',
		anchor : '95%',
		allowBlank : false,
		blankText : '标题不能为空!',
		maxLength : 80,
		maxLengthText : '标题不得超过80个字符!'
	});
	var idsiContent = {
		xtype : 'tinymce',
		id : 'idsiContent',
		name : 'idsiContent',
		fieldLabel : '内容',
		anchor : '95% -60',
		tinymceSettings : {
			mode : "textareas",
			theme : "advanced",
			language : "zh",
			plugins : "pagebreak,style,layer,table,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
			theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,forecolor,backcolor,|,formatselect,fontselect,fontsizeselect",
			theme_advanced_buttons2 : "preview,code,cleanup,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,sub,sup,|,undo,redo,|,link,unlink,anchor,|,insertdate,inserttime",
			theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,charmap,advhr,|,print,|,ltr,rtl",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "bottom",
			theme_advanced_resizing : false,
			extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]",
			template_external_list_url : "template_list.js",
			accessibility_focus : false
		}
	};

	var idsAdd_Form = new Ext.form.FormPanel({
		id : 'idsAdd_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [idsiTitle, idsiType_Combox, idsiContent],
		buttons : [{
			text : '保&nbsp;存',
			handler : saveIdsI
		}, {
			text : '取&nbsp;消',
			handler : function() {
				idsiAdd_Win.hide();
			}
		}]
	});
	// ----------------------- 编辑信息 ----------------------------
	var idsiType_Combox_Edit = new Ext.form.ComboBox({
		fieldLabel : "信息分类",
		mode : 'remote',
		store : idsiType_Store,
		hiddenName : 'idsiIdstId_Edit',
		emptyText : '请选择信息分类',
		triggerAction : 'all',
		lazyInit : true,
		valueField : 'value',
		displayField : 'text',
		editable : false,
		anchor : '95%',
		allowBlank : false,
		blankText : '信息分类不能为空！'
	});
	var idsiTitle_Edit = new Ext.form.TextField({
		fieldLabel : '标题',
		id : 'idsiTitle_Edit',
		name : 'idsiTitle_Edit',
		anchor : '95%',
		allowBlank : false,
		blankText : '标题不能为空!',
		maxLength : 80,
		maxLengthText : '标题不得超过80个字符!'
	});
	var idsiContent_Edit = {
		xtype : 'tinymce',
		id : 'idsiContent_Edit',
		name : 'idsiContent_Edit',
		fieldLabel : '内容',
		anchor : '95% -60',
		tinymceSettings : {
			mode : "textareas",
			theme : "advanced",
			language : "zh",
			plugins : "pagebreak,style,layer,table,advhr,advimage,advlink,emotions,iespell,insertdatetime,preview,media,searchreplace,print,contextmenu,paste,directionality,noneditable,visualchars,nonbreaking,xhtmlxtras,template",
			theme_advanced_buttons1 : "bold,italic,underline,strikethrough,|,justifyleft,justifycenter,justifyright,justifyfull,|,forecolor,backcolor,|,formatselect,fontselect,fontsizeselect",
			theme_advanced_buttons2 : "preview,code,cleanup,|,search,replace,|,bullist,numlist,|,outdent,indent,blockquote,|,sub,sup,|,undo,redo,|,link,unlink,anchor,|,insertdate,inserttime",
			theme_advanced_buttons3 : "tablecontrols,|,hr,removeformat,visualaid,|,charmap,advhr,|,print,|,ltr,rtl",
			theme_advanced_toolbar_location : "top",
			theme_advanced_toolbar_align : "left",
			theme_advanced_statusbar_location : "bottom",
			theme_advanced_resizing : false,
			extended_valid_elements : "a[name|href|target|title|onclick],img[class|src|border=0|alt|title|hspace|vspace|width|height|align|onmouseover|onmouseout|name],hr[class|width|size|noshade],font[face|size|color|style],span[class|align|style]",
			template_external_list_url : "template_list.js",
			accessibility_focus : false
		}
	};

	var idsEdit_Form = new Ext.form.FormPanel({
		id : 'idsEdit_Form',
		border : false,
		frame : false,
		waitMsgTarget : true,
		labelAlign : 'right',
		bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
		labelWidth : 80,
		buttonAlign : 'center',
		defaulttype : 'textfield',
		items : [idsiTitle_Edit, idsiType_Combox_Edit, idsiContent_Edit],
		buttons : [{
			text : '保&nbsp;存',
			handler : updateIdsI
		}, {
			text : '取&nbsp;消',
			handler : function() {
				idsiEdit_Win.hide();
			}
		}]
	});
	// ------------------ 自定义函数 -------------------------------
	// 新增信息
	/*function idsiAdd() {
		if(!idsiAdd_Win) {
			idsiAdd_Win = new Ext.Window({
				closeAction : 'hide',
				maximizable : false,
				maximizable : true,
				resizable : false,
				width : 550,
				height : 370,
				title : '发布信息',
				layout : 'fit',
				modal : true,
				buttonAlign : 'center',
				items : [idsAdd_Form],
				listeners : {
					hide : function() {
						idsAdd_Form.form.reset();
						idsiType_Combox.clearValue();
					}
				}
			});
		}
		idsiAdd_Win.show();
	}*/
	
	
	function idsiAdd() {
		if(idsTitle_Condition.trim()=="" || idsTitle_Condition!=idsTitle_search.getValue()){		
		  idsTitle_Condition = idsTitle_search.getValue();
		}
		if(idstIds.trim()=="" || idstIds !=groupCombox.getValue()){
		  idsiId = groupCombox.getValue();
		}
		//当前页
		var pageNum = pagingBar.getPageData().activePage;
		window.location.href=getPath() + "console/data/idsInformation.htm?action=saveOrUpdateInitpage&idstIds="+idstIds+"&idsTitle_Condition="+idsTitle_Condition+"&start="+(pageNum-1)*20;"";
	}
	
	function impExcel(){
		window.location.href=getPath() + "console/data/idsImport.htm?action=initImport";
	}
	
	// 编辑信息
	function idsiEdit() {
		if(idsTitle_Condition.trim()=="" || idsTitle_Condition!=idsTitle_search.getValue()){		
		  idsTitle_Condition = idsTitle_search.getValue();
		}
		if(idstIds.trim()=="" || idstIds !=groupCombox.getValue()){
		  idsiId = groupCombox.getValue();
		}
		var selectRow = grid.getSelectionModel().getSelected();
		//当前页
		var pageNum = pagingBar.getPageData().activePage;
		
		if(selectRow){
		    var idsiId = selectRow.get("idsiId"); 
			window.location.href= getPath() + "console/data/idsInformation.htm?action=saveOrUpdateInitpage&idsiId="+idsiId+"&idstIds="+idstIds+"&idsTitle_Condition="+idsTitle_Condition+"&start="+(pageNum-1)*20;
		}else{
			Ext.Msg.alert("提示","请点击选择要修改的信息!");
		}
		
		/*var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			if(!idsiEdit_Win) {
				idsiEdit_Win = new Ext.Window({
					closeAction : 'hide',
					maximizable : false,
					maximizable : true,
					resizable : false,
					width : 550,
					height : 370,
					title : '编辑信息',
					layout : 'fit',
					modal : true,
					buttonAlign : 'center',
					items : [idsEdit_Form],
					listeners : {
						hide : function() {
							idsEdit_Form.form.reset();
							idsiType_Combox_Edit.clearValue();
						}
					}
				});
			}
			idsiType_Store.reload();
			initData(selectRow.json.idsiId);
			idsiEdit_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');*/
	}
	
	
	
	// 去掉html标签
	function replaceAll(val) {
		var regHtml = new RegExp(/<[^>]+>/g);
		var regNBSP = new RegExp(/&nbsp;/gm);
		var val_replace = val.replace(regHtml, '');
		val_replace = val_replace.replace(regNBSP, '');
		return val_replace;
	}
	// 保存相关信息
	function saveIdsI() {
		if(idsAdd_Form.form.isValid()) {
			var idsiContent = Ext.getCmp('idsiContent').getValue();
			var idsiContent_replace = replaceAll(idsiContent);
			if(idsiContent == null || idsiContent.trim().length < 1 || idsiContent_replace == null || idsiContent_replace.trim().length < 1) {
				alertWarring('信息内容不能为空！');
				return;
			}
			if(idsiContent.trim().length > 50000) {
				alertWarring('信息内容不能超过5万个字符！');
				return;
			}
			idsAdd_Form.getForm().submit({
				url : getPath() + 'console/data/idsInformation.htm?action=saveIdsI',
				method : 'post',
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					idsiAdd_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	// 更新信息
	function updateIdsI() {
		if(idsEdit_Form.form.isValid()) {
			var idsiContent = Ext.getCmp('idsiContent_Edit').getValue();
			var idsiContent_replace = replaceAll(idsiContent);
			if(idsiContent == null || idsiContent.trim().length < 1 || idsiContent_replace == null || idsiContent_replace.trim().length < 1) {
				alertWarring('信息内容不能为空！');
				return;
			}
			if(idsiContent.trim().length > 50000) {
				alertWarring('信息内容不能超过5万个字符！');
				return;
			}
			idsEdit_Form.getForm().submit({
				url : getPath() + 'console/data/idsInformation.htm?action=updateIdsI',
				method : 'post',
				params : {
					idsiId : grid.getSelectionModel().getSelected().json.idsiId
				},
				waitMsg : '正在保存数据，请稍候...',
				success : function(form, action) {
					// 提示信息
					alertSuccess(action.result.msg);
					// 刷新列表
					searchMan();
					// 隐蔽窗口
					idsiEdit_Win.hide();
				},
				failure : function(form, action) {
					alertError(action.result.msg);
				}
			});
		}
	}
	function initData(idsiId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/idsInformation.htm?action=getIdsIContent',
			params : {
				idsiId : idsiId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					idsEdit_Form.form.setValues({
						idsiIdstId_Edit : txt.idsiIdstId,
						idsiTitle_Edit : txt.idsiTitle,
						idsiContent_Edit : txt.idsiContent
					});
				}else {
					alertError(txt.msg);
				}
			},
			failure : function(form, action) {
				alertError("系统忙，请稍后访问！");
			}
		});
	}
	function idsiDelete() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			Ext.Msg.confirm("确认消息框", "您确定要删除该信息吗？\n", function(btn, txt) {
				if(btn == 'yes') {
					var idsiId = selectRow.json.idsiId;
					idsIDel(idsiId);
				}
			});
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	// 删除信息
	function idsIDel(idsiId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/idsInformation.htm?action=delIdsI',
			params : {
				idsiId : idsiId
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
				alertError('系统出现未知错误，删除失败！');
			}
		});
	}
}
);