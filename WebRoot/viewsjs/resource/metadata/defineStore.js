// -- 元数据管理 --
var addMetaStore_Win = null;
var editMetaStore_Win = null;
var metaStoreIp_Win = null;
var metaStoreIpAdd_Win = null;
var metaStoreIpEdit_Win = null;
var store = null;
var ipStore = null;
var alertMetaWarning = null;
var ipRmstId = '';
var ids = '';
var rows = null;

function myHandlerDefineGroup(rdtyType, rmstId) {
		document.location.href = getPath() + 'console/data/resource/defineStore.htm?action=initDefineGroupView&rdtyType='+rdtyType+'&rmstId='
				+ rmstId;
}

Ext.onReady(function() {
	Ext.QuickTips.init();
	Ext.form.Field.prototype.msgTarget = 'side';
	// 变量设置
	var defineGroupWin = null;
	var RMSTID = null;
	var RDTTYID = null;
	var iframe_url_add = getPath() + 'console/data/resource/defineStore.htm?action=initAddMetaStore';
	var iframe_url_edit = getPath() + 'console/data/resource/defineStore.htm?action=initEditMetaStore';
	// 构造数据
	store = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/resource/defineStore.htm?action=getResMetaStoreList'
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
		}, {
			name : 'rmstIndexSort',
			type : 'string'
		}, {
			name : 'rmstFolder',
			type : 'string'
		}, {
			name : 'rmstGroupOne',
			type : 'string'
		}, {
			name : 'rmstGroupTwo',
			type : 'string'
		}, {
			name : 'rmstGroupThree',
			type : 'string'
		}, {
			name : 'rmstLanguage',
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
	
	// 构造数据
	ipStore = new Ext.data.Store({
		proxy : new Ext.data.HttpProxy({
			url : getPath() + 'console/data/IPDeploy.htm?action=initMetaStoreIpDeploy'
		}),
		reader : new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root'
		}, [{
			name : 'rsidId',
			type : 'int'
		}, {
			name : 'rsidIpPart',
			type : 'string'
		}]),
		sortInfo : {
			field : 'rsidIpPart',
			direction : 'ASC'
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
		dataIndex : 'rmstName',
		width: 220,
		sortable : true
	}, {
		header : '语种',
		dataIndex : 'rmstLanguage',
		sortable : true,
		renderer: function (v) {
			if(v == 'zh'){
				return "中文";
			}else if(v == 'zw'){
				return "藏文";
			}else if(v == 'mw'){
				return "蒙文";
			}else if(v == 'en'){
				return "英文";
			}else{
				return "-";
			}
		}
	}, {
		header : '创建人',
		dataIndex : 'smanName',
		sortable : true
	}, {
		header : '特色库ID',
		dataIndex : 'rmstId',
		sortable : true
	}, {
		header : '显示序号',
		dataIndex : 'rmstIndexSort',
		sortable : true
	}, {
		header : '状态',
		align : 'center',
		dataIndex : 'rmstStatus',
		renderer : checkState,
		sortable : true
	}, {
		header : '文件存放路径',
		align : 'center',
		dataIndex : 'rmstFolder',
		sortable : true
	}, {
		header : '分类设置',
		align : 'center',
		dataIndex : 'rmstId',
		width: 180,
		renderer: function (value, cellMeta, record) {
			var htmlCode = "<div class='groupManager'><a href='javascript:myHandlerDefineGroup(1,"+value+");'>" + record.data['rmstGroupOne'] + "</a>┆";
			htmlCode += "<a href='javascript:myHandlerDefineGroup(2,"+value+");'>"+record.data['rmstGroupTwo']+"</a>┆";
			htmlCode += "<a href='javascript:myHandlerDefineGroup(3,"+value+");'>"+record.data['rmstGroupThree']+"</a></div>";
			return htmlCode;
		}
	}]);
	
	function getStartIpPart(IP){
		var ipPart = IP.split('——');
		return ipPart[0];
	}
	
	function getEndIpPart(IP){
		var ipPart = IP.split('——');
		return ipPart[1];
	}
	
	// 创建多选模式
	var smIP = new Ext.grid.CheckboxSelectionModel();
	// 定义列模型
	var cmIP = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), smIP, {
		header : 'ID',
		width : 100,
		dataIndex : 'rsidId',
		hidden : true,
		sortable : true
	}, {
		header : 'IP段',
		width : 300,
		dataIndex : 'rsidIpPart',
		sortable : true
	}]);
	
	// 创建工具条
	var tb = new Ext.Toolbar([{
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : addMetaStore
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : resMetaStoreEdit
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : function() {
			var selectRow = grid.getSelectionModel().getSelected();
			if(selectRow) {
				var rmstId = selectRow.json.rmstId;
				if(rmstId < 10000){
					alertWarring("<span style='color:red'>对不起，该特色库为系统内置特色库，你不能删除！\n</span>");
					return;
				}
				Ext.Msg.confirm("确认消息框", "您确定要删除该特色库吗？\n", function(btn, txt) {
					if(btn == 'yes') {
						delResMetaStore(rmstId);
					}
				});
			}else
				alertWarring('请至少选择一行数据然后才能够操作！');
		}
	}	
	, '-', {
		text : '特色库IP配置',
		iconCls : 'common_go',
		handler : goToMetaStoreIpDeploy
	}]);
	
	//IP段新增窗口具体属性 
	  var storeIpAddForm = new Ext.form.FormPanel({
		id : 'storeIpAddForm' ,
		border : false ,
		frame : true ,
		labelAlign: 'right',
		buttonAlign : 'center' ,
		bodystyle : 'padding:5px 5px 0' ,
		lablewidth : 100 ,
		width : 400 ,
		height : 120 ,
		defaulttype : 'textfield' ,
		items : [
			{ xtype:'textfield',
           		fieldLabel:'IP段起始',
           		id:'ipPartStart',
           		name: 'ipPartStart',
           		anchor:'90%',
           		allowBlank: false,
           		validator:function check(){
           		if(/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(Ext.getCmp('ipPartStart').getValue())){
           		   return true;
           	      }else{
           		   return "只能输入有效的IP地址";
           	      }}
           	},{ xtype:'textfield',
           		fieldLabel:'IP段结束',
           		id:'ipPartEnd',
           		name: 'ipPartEnd',
           		anchor:'90%',
           		allowBlank: false,
           	    validator:function check(){
           	     if(/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(Ext.getCmp('ipPartEnd').getValue())){
           		   return true;
           	      }else{
           		   return "只能输入有效的IP地址";
           	      }}
           	}],
		buttons : [
			{
				text : '保 存',
				id:'addIP',
				handler:function(){
					addFormSubmit();	
				}
			},
			{
				text : '取 消',
				id:'resetIP',
				handler:function(){
					metaStoreIpAdd_Win.hide();
				}
			}
		]
	});
	
	//IP段编辑窗口具体属性 
	  var storeIpEditForm = new Ext.form.FormPanel({
		id : 'storeIpEditForm' ,
		border : false ,
		frame : true ,
		labelAlign: 'right',
		buttonAlign : 'center' ,
		bodystyle : 'padding:5px 5px 0' ,
		lablewidth : 100 ,
		width : 400 ,
		height : 120 ,
		defaulttype : 'textfield' ,
		items : [{ xtype:'textfield',
           		fieldLabel:'ID',
           		id:'ipDeployId',
           		name: 'ipDeployId',
           		anchor:'90%',
           		allowBlank: false,
           		hidden : true,
           		hideLabel : true
           	},
			{ xtype:'textfield',
           		fieldLabel:'IP段起始',
           		id:'ipPartStartEdit',
           		name: 'ipPartStartEdit',
           		anchor:'90%',
           		allowBlank: false,
           		validator:function check(){
           		if(/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(Ext.getCmp('ipPartStartEdit').getValue())){
           		   return true;
           	      }else{
           		   return "只能输入有效的IP地址";
           	      }}
           	},{ xtype:'textfield',
           		fieldLabel:'IP段结束',
           		id:'ipPartEndEdit',
           		name: 'ipPartEndEdit',
           		anchor:'90%',
           		allowBlank: false,
           	    validator:function check(){
           	     if(/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/.test(Ext.getCmp('ipPartEndEdit').getValue())){
           		   return true;
           	      }else{
           		   return "只能输入有效的IP地址";
           	      }}
           	}],
		buttons : [
			{
				text : '保 存',
				id:'editIP',
				handler:function(){
					editFormSubmit();	
				}
			},
			{
				text : '重 置',
				id:'resetEditIP',
				handler:function(){
					storeIpEditForm.form.setValues({
										   	ipDeployId : rows[0]
													.get('rsidId'),
									   		ipPartStartEdit : getStartIpPart(rows[0].get('rsidIpPart')),
											ipPartEndEdit : getEndIpPart(rows[0].get('rsidIpPart'))
										})
				}
			}
		]
	});
	
	//表单提交，新增IP段
		function addFormSubmit(){
			if(storeIpAddForm.form.isValid()){
            		storeIpAddForm.form.submit({
        				                   url:getPath() + 'console/data/IPDeploy.htm?action=addStoreIpPart&metaStoreId='+ipRmstId,
					                       method:'POST',
					                       waitMsg:'正在提交.....',
										   waitTitle:'请稍等',
										   
					                       success:function(form, action){
					                          alertSuccess(action.result.msg);
					                          storeIpAddForm.form.reset();
					                          metaStoreIpAdd_Win.hide();
											  ipStore.reload({
													params:{  //里面是属性可以随便写
															 start:0,
			    											 limit:limitLength,
			    											 metaStoreId:ipRmstId
													}
												    });
					                       },
					                       failure:function(form, action){
					                          alertError('系统超时');
					                          storeIpAddForm.form.reset();
					                          metaStoreIpAdd_Win.hide();
					                       }
        				              }); 
            	}
            	
		}
		
	//表单提交，新增IP段
		function editFormSubmit(){
			if(storeIpEditForm.form.isValid()){
            		storeIpEditForm.form.submit({
        				                   url:getPath() + 'console/data/IPDeploy.htm?action=editStoreIpPart&metaStoreId='+ipRmstId,
					                       method:'POST',
					                       waitMsg:'正在提交.....',
										   waitTitle:'请稍等',
										   
					                       success:function(form, action){
					                          alertSuccess(action.result.msg);
					                          storeIpEditForm.form.reset();
					                          metaStoreIpEdit_Win.hide();
											  ipStore.reload({
													params:{  //里面是属性可以随便写
															 start:0,
			    											 limit:limitLength
													}
												    });
					                       },
					                       failure:function(form, action){
					                          alertError('系统超时');
					                          storeIpEditForm.form.reset();
					                          metaStoreIpEdit_Win.hide();
					                       }
        				              }); 
            	}
            	
		}
	
	// 创建工具条
	var tbIp = new Ext.Toolbar(['IP段:', {
						xtype : 'textfield',
						id : 'rsidIpPart',
						name : 'rsidIpPart',
						width : 200,
						allowBlank : true,
						listeners:{   
				            specialkey:function(field,e){   
				                if (e.getKey()==Ext.EventObject.ENTER){   
				                    ipStore.reload({
										params : {
											start : 0,
											limit : limitLength,
											metaStoreId : ipRmstId,
											rsidIpPart : Ext.getCmp('rsidIpPart').getValue()
										}
									}); 
				                }   
				            }   
				        }   
					}, '-', {
						text : '&nbsp;查&nbsp;询&nbsp;',
						enableToggle : false,
						iconCls : 'search',
						handler : function() {
							ipStore.reload({
										params : {
											start : 0,
											limit : limitLength,
											metaStoreId : ipRmstId,
											rsidIpPart : Ext.getCmp('rsidIpPart').getValue()
										}
									});
						}
					}, '-', {
		text : '&nbsp;新&nbsp;增&nbsp;',
		iconCls : 'common_add',
		handler : function() {
			if(!metaStoreIpAdd_Win){
					        		metaStoreIpAdd_Win = new Ext.Window({       	
					        			closeAction:'hide',
					        			width : 400,
					        			height : 150,
					        			title :'新增IP段',
					        			layout : 'fit',
					        			modal : true,
					        			buttonAlign:'center',
					        			items : [storeIpAddForm]   //一个单独项，或子组件组成的数组，加入到此容器中
				        		});
				        		metaStoreIpAdd_Win.show();
			}else{
				        		storeIpAddForm.form.reset();
				        		metaStoreIpAdd_Win.show();
				        	}
		}
	}, '-', {
		text : '&nbsp;编&nbsp;辑&nbsp;',
		iconCls : 'common_edit',
		handler : function() {
			rows = gridIp.getSelectionModel().getSelections();
			if (rows.length == 1) {
				if(!metaStoreIpEdit_Win){
						        		metaStoreIpEdit_Win = new Ext.Window({       	
						        			closeAction:'hide',
						        			width : 400,
						        			height : 150,
						        			title :'编辑IP段',
						        			layout : 'fit',
						        			modal : true,
						        			buttonAlign:'center',
						        			items : [storeIpEditForm]   //一个单独项，或子组件组成的数组，加入到此容器中
					        		});
					        		storeIpEditForm.form.setValues({
										   	ipDeployId : rows[0]
													.get('rsidId'),
									   		ipPartStartEdit : getStartIpPart(rows[0].get('rsidIpPart')),
											ipPartEndEdit : getEndIpPart(rows[0].get('rsidIpPart'))
										});
					        		metaStoreIpEdit_Win.show();
				}else{
					        		storeIpEditForm.form.setValues({
										   	ipDeployId : rows[0]
													.get('rsidId'),
									   		ipPartStartEdit : getStartIpPart(rows[0].get('rsidIpPart')),
											ipPartEndEdit : getEndIpPart(rows[0].get('rsidIpPart'))
										});
					        		metaStoreIpEdit_Win.show();
					        	}
			}else{
				alertInformation('只能对一个IP配置进行编辑');
			}		        	
		}
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : function() {
			rows = gridIp.getSelectionModel().getSelections();
			if(rows.length >= 1) {
				Ext.Msg.confirm("确认消息框", "您确定要删除该特色库IP配置吗？\n", function(btn, txt) {
					if(btn == 'yes') {
					for (var i = 0; i < rows.length; i++) {
						if (i > 0)
							ids += ",";
						ids += rows[i].json.rsidId;
					}
						var param = "ids=" + ids;
						delMetaStoreIpDeploy(param);
						ids = '';
					}
				});
			}else
				alertWarring('请至少选择一行数据然后才能够操作！');
		}
	}, '-', {
		text : '&nbsp;取&nbsp;消&nbsp;',
		iconCls : 'common_back',
		handler : function() {
			metaStoreIp_Win.hide();
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
	
	// 创建一个分页控件
	var pagingBarIp = new Ext.PagingToolbar({
		pageSize : limitLength,
		store : ipStore,
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
	
	// 组合表格
	var gridIp = new Ext.grid.GridPanel({
		region : 'center',
		el : this.contentEl,
		cm : cmIP,
		sm : smIP,
		store : ipStore,
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
		autoExpandColumn : 'rsidIpPart',
		tbar : tbIp,
		bbar : pagingBarIp
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
	
	// 加载数据
	ipStore.on('beforeload',function(store, options){            
					store.baseParams  = {					     																						
						start:0,
						limit:limitLength,
						metaStoreId : ipRmstId,
						rsidIpPart : Ext.getCmp('rsidIpPart').getValue()
					}
					     
				});
	
	grid.on('dblclick', resMetaStoreEdit);
	// ------------------------ 自定义JS函数 --------------------------------------
	// 检测特色库状态
	function checkState(val) {
		if(val == '1')
			return '<font style="color:blue">可用</font>';
		else
			return '<font style="color:red">冻结</font>';
	}
	function goToMetaStoreIpDeploy() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
		ipRmstId = selectRow.json.rmstId;
			if (!metaStoreIp_Win) {
								metaStoreIp_Win = new Ext.Window({
											closeAction : 'hide',
											closable : true,
											width : 580,
											height : 350,
											title : '特色库IP配置',
											layout : 'fit',
											modal : true,
											buttonAlign : 'center',
											items : [gridIp]
										});
								metaStoreIp_Win.on('beforeshow', function() {
											ipStore.load({
														params : {
															start : 0,
															limit : limitLength,
															metaStoreId : ipRmstId
														}
													});
										});
								metaStoreIp_Win.show();
							} else {
								metaStoreIp_Win.on('beforeshow', function() {
											ipStore.load({
														params : {
															start : 0,
															limit : limitLength,
															metaStoreId : ipRmstId
														}
													});
										});
								metaStoreIp_Win.show();
							}
			
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	
	
	// 删除特色库
	function delResMetaStore(rmstId) {
		Ext.Ajax.request({
			url : getPath() + 'console/data/resource/defineStore.htm?action=delResMetaStore',
			params : {
				rmstId : rmstId
			},
			success : function(response, options) {
				var txt = Ext.util.JSON.decode(response.responseText);
				if(txt.success) {
					alertSuccess(txt.msg);
					store.load({
						params : {
							start : 0,
							limit : limitLength
						}
					});
				}else
					alertError(txt.msg);
			},
			failure : function(form, action) {
				alertError('系统忙，请稍后访问！');
			}
		});
	}
	
	// 创建与底层交互
	function delMetaStoreIpDeploy(param) {
		Ext.Ajax.request({
					url : getPath() + 'console/data/IPDeploy.htm?action=deleteStoreIpPart',
					params : param,
					method : 'POST',
					success : function(result, request) {
						var msg = result.responseText.split(":");
						if (msg[0] == "success") {
							alertSuccess(msg[1]);
							ipStore.reload();
						} else {
							alertSuccess(msg[1]);
						}
					},
					failure : function(result, request) {
						alertError('系统超时');
					}
				});
	}
	
	function addMetaStore() {
		addMetaStore_Win = new Ext.Window({
			bodyStyle : 'background-color: #dfe8f6;',
			width : 550,
			height : 380,
			title : '定义特色库',
			layout : 'fit',
			modal : true,
			resizable : false,
			border : true,
			html : getOs(iframe_url_add)
			// 此处在IE和FF中有差异
	}
		);
		addMetaStore_Win.show();
	}
	
	// 兼容IE和FF
	function getOs(url) {
		if(navigator.userAgent.indexOf("MSIE") > 0) {
			return '<iframe name="addStore" id="addStore" width="555px" height="350px" frameborder="no" src="' + url + '"></iframe>';
		}else {
			return '<iframe name="addStore" id="addStore" width="100%" height="100%" frameborder="no" src="' + url + '"></iframe>';
		}
	}
	function resMetaStoreEdit() {
		var selectRow = grid.getSelectionModel().getSelected();
		if(selectRow) {
			var url = iframe_url_edit + '&rmstId=' + selectRow.json.rmstId;
			editMetaStore_Win = new Ext.Window({
				bodyStyle : 'background-color: #dfe8f6;',
				width : 550,
				height : 380,
				title : '编辑特色库',
				layout : 'fit',
				modal : true,
				resizable : false,
				border : true,
				html : getOs(url)
			});
			editMetaStore_Win.show();
		}else
			alertWarring('请至少选择一行数据然后才能够操作！');
	}
	alertMetaWarning = function(type) {
		if(type == 'save') {
			addMetaStore_Win.hide();
			alertSuccess('特色库定义成功！');
		}else if('edit') {
			editMetaStore_Win.hide();
			alertSuccess('特色库更新成功！');
		}
	}
});