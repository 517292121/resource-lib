<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>文件服务器管理</title>
    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    

		<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/fileService.htm?action=fileSystemList'; 
			var url_save_form = '<%=path %>/console/data/fileService.htm?action=saveOrUpdate'; 
			var url_dele_form = '<%=path %>/console/data/fileService.htm?action=DelFilesystem'; 
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'文件服务器名称', dataIndex:'RFIL_NAME', sortable : true},  //定义操作渲染函数，如果确实没有操作，可以删除此行
				{header:'文件服务器地址', dataIndex:'RFIL_ADDRESS', sortable : true}, 
				{header:'FTP主机', dataIndex:'RFIL_FTP_HOST', sortable : true}, 
				{header:'FTP用户名', dataIndex:'RFIL_FTP_USERNAME', sortable : true}, 
				{header:'FTP密码', dataIndex:'RFIL_FTP_PASSWORD', sortable : true}, 
				{header:'序号', dataIndex:'RFIL_NUM', sortable : true}
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
					{name:'RFIL_NAME'}, 
					{name:'RFIL_ADDRESS'},
					{name:'RFIL_FTP_HOST'},
					{name:'RFIL_FTP_USERNAME'},
					{name:'RFIL_FTP_PASSWORD'},
					{name:'RFIL_NUM'},
					{name:'RFIL_ID'}
				]),
				sortInfo : {
					field : 'RFIL_NUM',
					direction : 'ASC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			}); 
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
			var grid = new Ext.grid.GridPanel({ 
				//height: document.documentElement.clientHeight, 
				enableColumnHide : false,
				autoScroll : true,
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				border : false,
				frame : false,
				loadMask: true,     //加载时提示load... 
				viewConfig: { 
					forceFit: true 
				}, 
				cm: cm, 
				
				autoScroll : true,
				region : 'center',
				
				//sm: sm, 
				tbar: new Ext.Toolbar({  //工具栏 
					items:[ 
						{ 
							text: '&nbsp;新&nbsp;增&nbsp;', 
							enableToggle: false, 
							iconCls:'common_add', 
							handler:function(){ 
								DataEdit(); 
							} 
						},'-',{ 
							text: '&nbsp;编&nbsp;辑&nbsp;', 
							enableToggle: false, 
							iconCls:'common_edit', 
							handler:function(){ 
								 dataUpdate();
							} 
						},'-',{ 
							text: '&nbsp;删&nbsp;除&nbsp;', 
							enableToggle: false, 
							iconCls:'common_delete', 
							handler:function(){ 
								 deleteData();
							} 
						}
					] 
				}), 
				bbar: new Ext.PagingToolbar({  //分页信息栏 
					pageSize: pageNum, 
					store: grid_store, 
					displayInfo: true, 
					displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>', 
					emptyMsg: '没有记录', 
					plugins: new Ext.ux.ProgressBarPager() 
				}) 
			}); 
			
			grid.on('dblclick', dataUpdate); //双击进入编辑
			function dataUpdate(){ 
				var selectRow = grid.getSelectionModel().getSelected();
				var rlty_id = 0;
				var rlty_name = "";
				if(selectRow){
				    DataEdit();
				    idhide.setValue(selectRow.get("RFIL_ID"));
				    RFIL_NAME.setValue(selectRow.get("RFIL_NAME"));
				    RFIL_ADDRESS.setValue(selectRow.get("RFIL_ADDRESS"));
				    RFIL_FTP_HOST.setValue(selectRow.get("RFIL_FTP_HOST"));
				    RFIL_FTP_USERNAME.setValue(selectRow.get("RFIL_FTP_USERNAME"));
				    RFIL_FTP_PASSWORD.setValue(selectRow.get("RFIL_FTP_PASSWORD"));
				    RFIL_NUM.setValue(selectRow.get("RFIL_NUM"));
				}else{
					Ext.Msg.alert("提示","请点击选择文件服务器!");
				}
			}
			
			function deleteData(){ 
			    var selectRow = grid.getSelectionModel().getSelected();
				var rlty_id = 0;
				var rlty_name = "";
				if(selectRow){
				    rlty_id = selectRow.get("RFIL_ID");
				    Ext.Msg.confirm('提示','您确定要删除该文件服务器配置吗？\n',function(button,text){  
					if(button == 'yes'){ 
					    Ext.Ajax.request({
	                        url:url_dele_form + '&RFIL_ID='+rlty_id 
	                    });
	                    grid_store.reload(); 
					}
				}); 
				    
				}else{
					Ext.Msg.alert("提示","请点击选择文件服务器!");
				}
			} 
			
			
			///*******************定义操作表单**************************************// 
			var idhide = new Ext.form.Hidden({ 
				name : "RFIL_ID", 
				id: "RFIL_ID" 
			}); 
			idhide.setValue("0"); 
			
			///文件服务器名称
			var RFIL_NAME = new Ext.form.TextField({ 
				fieldLabel: '文件服务器名称', 
				name: 'RFIL_NAME', 
				width: 200 ,
				allowBlank : false,
				blankText : '请输入文件服务器名称！',
				maxLength : 40,
				maxLengthText : '文件服务器名称不得超过40个字符!'
			}); 
			
			///RFIL_ADDRESS
			var RFIL_ADDRESS = new Ext.form.TextField({ 
				fieldLabel: '文件服务器地址', 
				name: 'RFIL_ADDRESS', 
				width: 200 ,
				allowBlank : false,
				blankText : '请输入文件服务器地址！(如：http://192.168.1.104:8008/fileService)',
				maxLength : 400,
				maxLengthText : '文件服务器地址不得超过400个字符!'
			});
			
			///ftp.host
			var RFIL_FTP_HOST = new Ext.form.TextField({ 
				fieldLabel: 'FTP主机', 
				name: 'RFIL_FTP_HOST', 
				width: 200 ,
				allowBlank : false,
				blankText : '请输入FTP主机！(如：192.168.1.104)',
				maxLength : 40,
				maxLengthText : 'FTP主机不得超过40个字符!'
			});
			
			///ftp.username
			var RFIL_FTP_USERNAME = new Ext.form.TextField({ 
				fieldLabel: 'FTP用户名', 
				name: 'RFIL_FTP_USERNAME', 
				width: 200 ,
				allowBlank : false,
				blankText : '请输入FTP用户名！',
				maxLength : 40,
				maxLengthText : 'FTP用户名不得超过40个字符!'
			});
			
			///ftp.password
			var RFIL_FTP_PASSWORD = new Ext.form.TextField({ 
				fieldLabel: 'FTP密码', 
				name: 'RFIL_FTP_PASSWORD', 
				width: 200 ,
				allowBlank : false,
				blankText : '请输入FTP密码！',
				maxLength : 40,
				maxLengthText : 'FTP密码不得超过40个字符!'
			});
			
			var RFIL_NUM = new Ext.form.NumberField({ 
				fieldLabel: '序号', 
				name: 'RFIL_NUM', 
				width: 200 
			});
			
			
			///定义表单 
			var dataForm = new Ext.form.FormPanel({ 
				height: 210, //定义表单窗体高度，根据实际情况何必. 
				labelWidth: 120, 
				labelAlign: 'right', 
				buttonAlign: 'center',
				url: url_save_form, 
				frame: true,  //如果设为false或不设则表单的背景为白色 
				items: [ 
					idhide, 
					RFIL_NAME,
					RFIL_ADDRESS,
					RFIL_FTP_HOST,
					RFIL_FTP_USERNAME,
					RFIL_FTP_PASSWORD,
					RFIL_NUM
				], 
				buttons:[ 
					{ 
						text: '保&nbsp;存', 
						handler: function(){ 
							DataSaveOrUpdate(); 
						} 
					}, 
					{ 
						text: '取&nbsp;消', 
						handler: function(){ 
							win.hide(); 
						} 
					} 
				] 
			}); 
			
			//保存或更新数据 
			function DataSaveOrUpdate(){ 
				dataForm.getForm().submit({ 
					waitTitle:"数据提交", 
					waitMsg:"正在提交表单数据，请稍候...", 
					success: function(form, action){ 
						win.hide(); 
						Ext.Msg.alert('信息',action.result.msg); 
						grid_store.reload(); 
					}, 
					failure:function(form, action) 
					{ 
						Ext.Msg.alert('失败',action.result.msg); 
					} 
				}); 
			} 
			
			///////弹出表单的window 
			var win; 
			function DataEdit(){ 
				dataForm.getForm().reset();  //将之前的清空 
				if(!win){ 
					win = new Ext.Window({ 
						title: '服务器信息', 
						layout: 'fit', 
						border : false,
						name: 'editwin', 
						width: 400, 
						closeAction: 'hide', 
						modal: true, 
						items: [dataForm] 
					}); 
				} 
				win.show(); 
			} 
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				Ext.QuickTips.init(); 
				Ext.form.Field.prototype.msgTarget = 'side'; 
				//grid.render('grid'); 
				
				// 创建主面板
				var displayPanel = new Ext.Viewport({
					layout : 'border',
					items : [grid]
				});
			}) 

		</script> 
	</head> 
	<body> 
		<div id="grid"></div> 
	</body> 
</html>