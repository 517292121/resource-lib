<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath(); 
%>
<%@include file="/WEB-INF/views/common/extInclude.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
     
    <title>用户日志列表</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script> 
	<script type="text/javascript">
			//参数获取
			var username = encodeURI('${param.userName}');
			var type = encodeURI('${param.type}');
			//每页显示条数 
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; 
			//json元数据获取路径
			var url_data_grid = '<%=path %>/console/count/userAccess.htm?action=logList&userName='+username+'&type='+type;
			//返回界面
			var url_user_access='<%=path %>/console/count/userAccess.htm';
			//导出excel表
			var url_exp_to_excel='<%=path %>/console/count/userAccess.htm?action=exportLogtoExl&log=all';
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'用户名称', dataIndex:'sacoSuinName',width:110, sortable : true},
				{header:'操作时间', dataIndex:'sacoDate',width:110,sortable : true},
				{header:'操作类型', dataIndex:'sacoType',width:110, sortable : true},
				{header:'操作内容', dataIndex:'sacoContent',width:110, sortable : true},
				{header:'操作IP', dataIndex:'sacoIp',width:110,sortable : true}
			]); 
			
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[  
					{name:'sacoId'}, 
					{name:'sacoSuinName'}, 
					{name:'sacoType'},
					{name:'sacoDate'}, 
					{name:'sacoContent'}, 
					{name:'sacoIp'}
				]),
				sortInfo : {
					field : 'sacoDate',
					direction : 'DESC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			});
			
			//添加表格翻页后的事件
			grid_store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
					userName: username,
					type: type
				});
			});
	
			// 查询
			var searchMan = function() {
				grid_store.reload({
					params : {
						start : 0,
						limit : pageNum,
						userName: username,
						type: type,
						beginD : document.getElementById('beginD').value,
						endD : document.getElementById('endD').value
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
							//时间段开始时间、指定日
							'时间段:',
		        	         {	
		        	        	xtype:'datefield',
		        	            name:'beginD',
		        	            id:'beginD',
		        	            anchor:'97%',
		        	            altFormats: 'Y-m-d',
	                			format: 'Y-m-d',
	        					readOnly : false
		        	         },
		        	         //分隔符
		        	         {
			        			xtype:'label',
					   			text:'--',
					   			width:10,
					   			name:'divide',
					   			id:'divideD',
					   			allowBlank: false
		        			},
		        	         //时间段结束时间
		        	         {	
		        	        	xtype:'datefield',
		        	            name:'endD',
		        	            id:'endD',
		        	            anchor:'97%',
		        	            altFormats: 'Y-m-d',
	                			format: 'Y-m-d',
	        					readOnly : false
		        	         },'-',
		        	         {
		        				text: '统计',
		        				iconCls:'common_search', 
		        				handler: function(){
		        					var beginD = document.getElementById('beginD').value;
		        					var endD = document.getElementById('endD').value;
		        					if(beginD !=null && beginD !="" && endD !=null && endD !="" && beginD > endD){
		        						alertWarring("开始时间不能大于结束时间!");
		        						return ;
		        					}
		        					searchMan();
		        				} 
	        				 },'-', {
								text : '返回',
								iconCls : 'common_back',
								handler : function(){
									window.location.href = url_user_access;
								}
							},'-',
		        			{
		        				text: '导出日志',
		        				enableToggle: false,
		        				width:70,
		        				iconCls:'excel',
		        				handler: function(){	
		        					var beginD = document.getElementById('beginD').value;
		        					var endD = document.getElementById('endD').value;
		        					if(beginD !=null && beginD !="" && endD !=null && endD !="" && beginD > endD){
		        						alertWarring("开始时间不能大于结束时间!");
		        						return ;
		        					}
		        					window.location.href=url_exp_to_excel+"&userName="+username+"&type="+type+"&beginD="+beginD+"&endD="+endD;
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
	
			//**********************加载是将元素渲染到页面****************************// 
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
    <div id="grid"></div>
    ${result}
  </body>
</html>
