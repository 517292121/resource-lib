<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%
	String path = request.getContextPath();
	long rminId = MyRequestUtil.getLongAttribute(request,"rminId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>资源建设--资源维护--资源列表</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
	<%@ include file="../../common/extInclude.jsp" %>   
	
	<script type="text/javascript">
			var pageNum = '20'; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/inputSingle.htm?action=resourceListJson&RSDA_RMIN_ID=<%=rminId %>'; 
			var url_save_form = ''; 
			var url_dele_form = ''; 
			var url_delete_icon = ''; 
			
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'题名', dataIndex:'RSDA_TITLE'},
				{header:'主题', dataIndex:'RSDA_SUBJECT'},
				{header:'中图法分类', dataIndex:'RSDA_MIDDLENAME'}, 
				{header:'所属作业', dataIndex:'RMIN_MISSNAME'}, 
				{header:'创建时间', dataIndex:'RSDA_BUILDDATE'}, 
				{header:'状态', dataIndex:'RSDA_STATUS',renderer : checkState } 
			]); 
			
			// 状态
			function checkState(val) {
				if(val == '2')
					return '<img src="<%=path %>/images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
				else if(val == '3')
					return '<img src="<%=path %>/images/res_minfo/res_pass.png" title="通过" width="16" height="16" />';
				else
					return '<img src="<%=path %>/images/res_minfo/res_nopass.png" title="未通过" width="16" height="16" />';
			}
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'RSDA_TITLE'}, 
				    {name:'RSDA_SUBJECT'}, 
					{name:'RSDA_MIDDLENAME'}, 
					{name:'RMIN_MISSNAME'}, 
					{name:'RSDA_BUILDDATE'}, 
					{name:'RSDA_STATUS'}, 
					{name: 'RSDA_ID'}, 
				]) 
			}); 
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
			grid_store.on('beforeload',function(){ 
				Ext.apply(  
				this.baseParams, 
				{ 
					RSDA_RLTY_ID: document.getElementById('index_RSDA_RLTY_ID').value,
					RSDA_RMST_ID: document.getElementById('index_RSDA_RMST_ID').value,
					RSDA_RMIN_ID: document.getElementById('index_RSDA_RMIN_ID').value
				}); 
			}); 
			var grid = new Ext.grid.GridPanel({ 
				height: document.documentElement.clientHeight-2, 
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				style: 'margin-top:1px;', 
				loadMask: true,     //加载时提示load... 
				viewConfig: { 
					forceFit: true 
				}, 
				cm: cm, 
				sm: sm, 
				autoScroll : true,
				region : 'center',
				tbar: new Ext.Toolbar({  //工具栏 
					items:[ 
						{ 
							xtype:'textfield', 
							emptyText:'资源类型', 
							id:'index_RSDA_RLTY_ID', 
							width:150 
						}, 
						{ 
							xtype:'textfield', 
							emptyText:'所属特色库', 
							id:'index_RSDA_RMST_ID', 
							width:150 
						}, 
						{ 
							xtype:'textfield', 
							emptyText:'所属作业', 
							id:'index_RSDA_RMIN_ID', 
							width:150 
						}, 
						{ 
							text: '查 询', 
							enableToggle: false, 
							width:55, 
							iconCls:'search', 
							handler: function(){ 
								grid_store.reload(); 
							} 
						} , 
						{ 
							text: '编 辑', 
							enableToggle: false, 
							width:55, 
							iconCls:'edit', 
							handler: function(){ 
							    dataEdit();
							} 
						} 
					] 
				}), 
				bbar: new Ext.PagingToolbar({  //分页信息栏 
					pageSize: pageNum, 
					store: grid_store, 
					displayInfo: true, 
					displayMsg: '显示第 <font color=blue>{0}</font> 条到 <font color=green>{1}</font> 条记录，一共 <font color=red>{2}</font> 条', 
					emptyMsg: '没有记录', 
					plugins: new Ext.ux.ProgressBarPager(),
					items : [{
						xtype : 'label',
						style : 'padding-left: 10px;',
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
						text : '通过',
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
						text : '未通过',
						style : 'padding-left: 3px;',
						width : 10
					}] 
				}) 
			}); 
			
			grid.on('dblclick', dataEdit); //双击进入模板设置
			
			function dataEdit(){
					var selectRow = grid.getSelectionModel().getSelected();
					var source_id = 0;
					if(selectRow){
					    source_id = selectRow.get("RSDA_ID");
					    window.location.href = "<%=path %>/console/data/inputSingle.htm?action=doResourceEdit&sid=" + source_id+"&lanId=" + 591285046607703;               
					    
						//Ext.Msg.alert("提示","此功能正在建设中..."+source_id);
					}else{
						Ext.Msg.alert("提示","请点击选择资源类型!");
					}
			}
			 
			
			function isDel(id){ 
				Ext.Msg.confirm('提示','您确定要删除该记录吗？\n',function(button,text){  
					if(button == 'yes'){ 
						Ext.Ajax.request({ 
							url:url_dele_form + '&id='+id  
						}); 
						grid_store.reload(); 
					} 
				}); 
			} 
			
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				Ext.QuickTips.init(); 
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