<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
    
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var pagingDisplayMsg = '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>';
			var url_data_grid = '<%=path %>/console/data/text/textLibrary.htm?action=getLibListJson';
			
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'资源库', dataIndex:'rtliName', width:80, sortable: true},
				{header:'序号', dataIndex:'rtliIndex', sortable: true}
			]); 
			
			
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'rtliId'}, 
				    {name:'rtliName'},
				    {name:'rtliIndex'}
				]) ,
				sortInfo : {
					field : 'rtliIndex',
					direction : 'ASC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
				
			}); 
			grid_store.load(); 
			
			var tbar1 = new Ext.Toolbar([
				{ 
					text: '查看资源', 
					enableToggle: false, 
					iconCls:'common_list', 
					handler: resourceList 
				} , '-',{ 
					text: '资源录入', 
					enableToggle: false, 
					iconCls:'common_add', 
					handler: addResource 
				} , '-',
				{ 
					text: 'excel导入', 
					enableToggle: false, 
					iconCls:'excel', 
					handler: excelImport 
				}
			]);
			
			// 创建一个分页控件
			var pagingBar = new Ext.PagingToolbar({
				pageSize : pageNum,
				store : grid_store,
				displayInfo : true,
				displayMsg : pagingDisplayMsg,
				emptyMsg : '没有记录',
				plugins : new Ext.ux.ProgressBarPager()
			});
			
			var grid = new Ext.grid.GridPanel({ 
				height: document.documentElement.clientHeight, 
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				sm: new Ext.grid.RowSelectionModel({
				singleSelect :true
				}),
				loadMask: true,     //加载时提示load... 
				frame:false,
				border:false,
				viewConfig: { 
					forceFit: true 
				}, 
				cm: cm, 
				autoScroll : true,
				region : 'center',
				tbar: tbar1,
				bbar : pagingBar
			}); 
			
			grid.on('dblclick', resourceList); //双击进入模板设置
			
			
			function addResource(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
					var rtliId = selectRow.get("rtliId");
					window.location.href = "<%=path %>/console/data/text/textResource.htm?action=inputResource&source=resLib&rtliId=" + rtliId;
				}else{
					Msg.alert('请点击选择待录入的资源库！',3);
				}
			}
			function excelImport(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
					var rtliId = selectRow.get("rtliId");
					window.location.href = "<%=path %>/console/data/text/textResourceImport.htm?source=resLib&rtliId=" + rtliId;
				}else{
					Msg.alert('请点击选择待导入的资源库！',3);
				}
			}
			
			function resourceList(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
					var rtliId = selectRow.get("rtliId");
					window.location.href = "<%=path %>/console/data/text/textResource.htm?action=resourceList&rtliId=" + rtliId;
				}else{
					Msg.alert('请点击选择查看资源的资源库！',3);
				}
			}
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				Ext.QuickTips.init(); 
			    Ext.form.Field.prototype.msgTarget = 'side';
				
				// 创建主面板
				var displayPanel = new Ext.Viewport({
					layout : 'border',
					items : [grid]
				}); 
			}) 

		</script> 
	</head> 
	<body> 
		${result }
	</body> 
</html>