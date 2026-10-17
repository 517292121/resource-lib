<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
%>
<%@include file="/WEB-INF/views/common/extInclude.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>项目统计管理———基本信息管理——基本信息</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			//json元数据获取路径
			var url_data_grid = '<%=path %>/console/count/pointcounter.htm?action=getPointCounterToJson';  
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true});
				var cm = new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), 
				{header:'登录名', dataIndex:'suinLoginName',width:70, sortable : true,align:'center',locked: true},
				{header:'用户姓名', dataIndex:'suinName',width:50, sortable : true,align:'center',locked: true},
				{header:'当前积分', dataIndex:'suinIntegral',width:60, sortable : true,align:'center',locked: true} 
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
					{name:'suinId'},
					{name:'suinLoginName'},
					{name:'suinName'},
					{name:'suinIntegral'} 
				]),
				sortInfo : {
					field : 'suinId',
					direction : 'DESC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			});  
			  
			var serach_suinLoginName = new Ext.form.TextField({
				width : 60 ,
				inputType :'text'
			});
			 
			var serach_suinName = new Ext.form.TextField({
				width : 80 ,
				inputType :'text'
			}); 
			
			// 查询
			var searchMan = function() { 
				 var params_suinLoginName = serach_suinLoginName.getValue();
				 var params_suinName = serach_suinName.getValue();
				grid_store.reload({
					params : {
						start : 0,
						limit : pageNum,
						loginname : params_suinLoginName,
						realname : params_suinName
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
				border : true,
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
					items:[ '登录名：',serach_suinLoginName,'-',
							'姓名：',serach_suinName,'-', 
					    {
						text : '&nbsp;查&nbsp;询&nbsp;',
						iconCls : 'common_search',
						handler : searchMan
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
				
			/**********************添加窗口end****************************/ 
			
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
