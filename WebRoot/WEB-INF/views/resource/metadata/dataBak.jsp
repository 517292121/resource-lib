<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    

		<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			//json元数据获取路径
			var url_data_grid = '<%=path %>/console/data/dataBak.htm?action=getDataBakJson'; 
			
			//备份提交路径
			var url_bak_form = '<%=path %>/console/data/dataBak.htm?action=saveDataBak'; 
			
			//删除路径
			var url_dele_form = '<%=path %>/console/data/dataBak.htm?action=deleteDatabak'; 
			
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'文件名', dataIndex:'dbmsFilename',width:150, sortable : true},
				//{header:'下载地址', dataIndex:'dbmsUrlpath',width:200, sortable : true}, 
				{header:'备份时间', dataIndex:'dbmsBakdate',width:150, sortable : true}
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
					{name:'dbmsFilename'}, 
					{name:'dbmsUrlpath'},
					{name:'dbmsBakdate'},
					{name:'dbmsId'}
				]),
				sortInfo : {
					field : 'dbmsBakdate',
					direction : 'DESC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			}); 
			
			//添加表格翻页后的事件
			/*grid_store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
					pdepName : serach_title.getValue()
				});
			});
			//添加表格双击事件
			//grid.on('dblclick', idsiEdit);
			
			// --查询条件:标题
			var serach_title = new Ext.form.TextField({
				width : 180,
				listeners : {
					'specialkey' : function(field, e) {
						if(e.getKey() == Ext.EventObject.ENTER) {
							searchMan();
						}
					}
				}
			});
			*/
			// 备份
			var dataBak = function() {
				Ext.Msg.confirm('提示','您确定要备份吗?\n',function(button,text){  
						if(button == 'yes'){ 
						    Ext.Ajax.request({
		                        url:url_bak_form ,
			                    success: function(response, option){ 
									Ext.Msg.alert('信息',response.responseText); 
									grid_store.reload(); 
								}
		                    });
		                    
						}
				}); 
			}
			
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
							text: '&nbsp;下&nbsp;载&nbsp;', 
							enableToggle: false, 
							iconCls:'icon_download', 
							handler: function(){ 
							    downLoadFile();
							} 
					},'-',{
						text : '&nbsp;备&nbsp;份&nbsp;',
						iconCls : 'common_add',
						handler : dataBak
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
			
			//grid.on('dblclick', dataUpdate); //双击进入模板设置
			
			function downLoadFile(){
					var selectRow = grid.getSelectionModel().getSelected();
					if(selectRow){
					    var filename = selectRow.get("dbmsUrlpath");
					    window.location.href = "${requestScope.address}" + filename;
					}else{
						Ext.Msg.alert("提示","请选择需要下载的数据!");
					}
			}			 
			
			function deleteData(){ 
			    var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var dbmsId = selectRow.get("dbmsId");
				    Ext.Msg.confirm('提示','您确定要删除该备份吗?\n',function(button,text){  
						if(button == 'yes'){ 
						    Ext.Ajax.request({
		                        url:url_dele_form + '&dbmsId='+dbmsId ,
			                    success: function(response, option){ 
									//Ext.Msg.alert('信息',response.responseText); 
									grid_store.reload(); 
								}
		                    });
						}
					}); 
				    
				}else{
					Ext.Msg.alert("提示","请选择需要删除的数据!");
				}
			} 
			
			
			///////弹出表单的window 
			/*var win; 
			function DataEdit(){ 
				dataForm.getForm().reset();  //将之前的清空 
				if(!win){ 
					win = new Ext.Window({ 
						title: '编辑院系信息', 
						border : false,
						name: 'editwin', 
						resizable : false,
						width: 320, 
						closeAction: 'hide', 
						modal: true, 
						items: [dataForm] 
					}); 
				} 
				win.show(); 
			} */
			 
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
