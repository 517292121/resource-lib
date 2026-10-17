<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
     <script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			//json元数据获取路径
			var url_data_grid = '<%=path %>/console/count/resmetastore.htm?action=getMetaTwoCountJson&MetaId=${MetaId }&year=${year}&month=${month}';
			
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(),
				{header:'资源类型', dataIndex:'RLTYNAME',width:110, sortable : false},
				{header:'浏览量', dataIndex:'COUNTNUM',width:110, sortable : true}
			]);
			
			var grid_store = new Ext.data.Store({
				proxy: new Ext.data.HttpProxy({url:url_data_grid}),
				reader: new Ext.data.JsonReader({
					totalProperty: 'totalProperty',
					root: 'root'
				},[
					{name:'RLTYID'},
					{name:'COUNTNUM'},
					{name:'RLTYNAME'}  
				]),
				sortInfo : {
					field : 'COUNTNUM',
					direction : 'DESC'
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
				tbar: new Ext.Toolbar({  //工具栏 
					//height: 200,
					//autoWidth :false,
					//width : 100,
					items:['当前特色库：${metaStore.rmstName }',
		        	         '-',{ 
							 text: ' 返回 ', 
							 enableToggle: false, 
							 iconCls:'common_back', 
							 handler: function(){ 
							 window.location.href="<%=path %>/console/count/resmetastore.htm?action=initPage";
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
			grid_store.on('beforeload',function(store, options){
					store.baseParams  = { 
						beginD : document.getElementById('beginD').value,
						endD : document.getElementById('endD').value, 
						start:0,
						limit:pageNum
					}
				}); 
     	 
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
  <div id ="grid"></div>  
  </body>
</html>
