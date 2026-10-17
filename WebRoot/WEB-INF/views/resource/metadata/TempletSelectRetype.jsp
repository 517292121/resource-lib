<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>模板设置--资源选择</title>
    
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
    
    <script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/templetManager.htm?action=getSourceTypeListJsonData'; 
			var url_data_store='<%=path %>/console/data/extdata.htm?action=store_select&hasNull=hasNull&ptype=1';
			var url_sourcetype_store='<%=path %>/console/data/templetManager.htm?action=getSourceTypeByStore';
			
			//特色库数据store
			var metastore_store = new Ext.data.Store({
			    autoLoad : false, 
			    proxy: new Ext.data.HttpProxy({url: url_data_store}),
			    reader: new Ext.data.ArrayReader({},[
			        {name: 'value'},
			        {name: 'text'}
			    ])
			});
			metastore_store.load();
			
			
			//资源类型数据store
			var sourcetype_store = new Ext.data.Store({
			    autoLoad : false, 
			    proxy: new Ext.data.HttpProxy({url: url_sourcetype_store}),
			    reader: new Ext.data.ArrayReader({},[
			        {name: 'value'},
			        {name: 'text'}
			    ])
			});
			
			var metastore = new Ext.form.ComboBox({
			    fieldLabel: "特色库",
			    mode: 'remote',
			    store: metastore_store,
			    name: '_index_RMST_ID',
			    emptyText: '请先选择特色库...',
			    triggerAction: 'all',
			    lazyInit: true,
			    valueField: 'value',
			    editable : false,
			    displayField: 'text'    
			});
			
			//当特色发生改变时，级联改变特色库所联系的资源类型
			metastore.on('select', function(comboBox){
			   var id = comboBox.getValue();
			   sourcetype.clearValue(); //清空之前的资源类型
			   var store_url = url_sourcetype_store + "&hasNull=hasNull&store=" + id;
			   sourcetype_store.proxy = new Ext.data.HttpProxy({url: store_url});
			   sourcetype_store.load();
			})
			
			var sourcetype = new Ext.form.ComboBox({
			    fieldLabel: "资源类型",
			    mode: 'remote',
			    store: sourcetype_store,
			    name: '_index_RMIN_RLTY_ID',
			    emptyText: '再选择资源类型...',
			    triggerAction: 'all',
			    lazyInit: true,
			    editable : false,
			    valueField: 'value',
			    displayField: 'text'    
			});
			
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'特色库', dataIndex:'RMST_NAME'}, 
				{header:'资源类型', dataIndex:'RLTY_NAME'},
				{header:'创建时间', dataIndex:'RMST_BUILDDATE'}
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:'<%=path %>/console/data/templetManager.htm?action=getSourceTypeListJsonData'}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'RMST_NAME'},
					{name:'RLTY_NAME'}, 
					{name:'RMST_BUILDDATE'}, 
					{name:'RMST_ID'}, 
					{name:'RLTY_ID'}
				]) 
			}); 
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
			grid_store.on('beforeload',function(){
				Ext.apply(  
				this.baseParams, 
				{ 
					RMST_ID: metastore.getValue(), 
					RLTY_ID: sourcetype.getValue()
				}); 
			}); 
			
			var grid = new Ext.grid.GridPanel({ 
				//height: document.documentElement.clientHeight,
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				loadMask: true,     //加载时提示load... 
				frame : false,
				border : false,
				viewConfig: { 
					forceFit: true 
				}, 
				enableColumnHide : false,
				columnLines : true, // True表示为在列分隔处显示分隔符
				cm: cm, 
				sm: sm, 
				autoScroll : true,
				region : 'center',
				tbar: new Ext.Toolbar({  //工具栏 
					items:['特色库：',
						metastore, 
						'-',
						sourcetype,
						'-',{ 
							text: '&nbsp;查&nbsp;询&nbsp;', 
							enableToggle: false, 
							iconCls:'common_search', 
							handler: function(){ 
								grid_store.load({params:{
						            start:0,
						            limit:pageNum,
						            RMST_ID: metastore.getValue(), 
									RLTY_ID: sourcetype.getValue()
						        }}); 
							} 
						} ,'-',
						{ 
							text: '模板设置', 
							enableToggle: false, 
							iconCls:'common_go',
							handler: function(){ 
								TempletEdit();
							} 
						},'-' ,
						/**
						{ 
							text: '导出模板', 
							enableToggle: false, 
							iconCls:'excel',
							handler: function(){ 
								exportExcel();
							} 
						},'-' ,
						**/
						{ 
							text: 'Excel模板设置', 
							enableToggle: false, 
							iconCls:'excel',
							handler: function(){ 
								importTempletEdit();
							} 
						},'-' ,
						{ 
							text: '导出Excel模板', 
							enableToggle: false, 
							iconCls:'excel',
							handler: function(){ 
								var selectRow = grid.getSelectionModel().getSelected();
								if(selectRow){
								    var rmst_id = selectRow.get("RMST_ID");
								    var rlty_id = selectRow.get("RLTY_ID");
								    var rmstName = selectRow.get("RMST_NAME");
								    var rltyName = selectRow.get("RLTY_NAME");
								    var export_url = "<%=path %>/console/data/excel.htm?action=exportTemplet&storeId="+rmst_id+"&sourceType=" +rlty_id +"&rmstName=" + rmstName + "&rltyName=" + rltyName;
								    window.location.href = export_url;
								}else{
									Ext.Msg.alert("提示","请点击选择需导出的模板!");
								}
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
			grid.on('dblclick', TempletEdit); //双击进入模板设置
			
			//进入模板设置函数页面
			function TempletEdit(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var rmst_id = selectRow.get("RMST_ID");
				    var rlty_id = selectRow.get("RLTY_ID");
				    var edit_url = "<%=path %>/console/data/templetManager.htm?action=TempletEdit&rmst="+rmst_id+"&rlty="+rlty_id;
				    window.location.href = edit_url;
				}else{
					Ext.Msg.alert("提示","请点击选择特色库资源!");
				}
			}
			
			function exportExcel(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var rmst_id = selectRow.get("RMST_ID");
				    var rlty_id = selectRow.get("RLTY_ID");
				    var rmstName = selectRow.get("RMST_NAME");
				    var rltyName = selectRow.get("RLTY_NAME");
				    var export_url = "<%=path %>/console/data/excel.htm?action=exportExcelTemplet&storeId="+rmst_id+"&sourceType=" +rlty_id +"&rmstName=" + rmstName + "&rltyName=" + rltyName;
				    window.location.href = export_url;
				}else{
					Ext.Msg.alert("提示","请点击选择需导出的模板!");
				}
			}
			 
			function importTempletEdit(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var rmst_id = selectRow.get("RMST_ID");
				    var rlty_id = selectRow.get("RLTY_ID");
				    var edit_url = "<%=path %>/console/data/importTemplet.htm?action=init&rmstId="+rmst_id+"&rltyId="+rlty_id;
				    window.location.href = edit_url;
				}else{
					Ext.Msg.alert("提示","请点击选择特色库资源!");
				}
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
    ${requestScope.result }
  </body>
</html>
