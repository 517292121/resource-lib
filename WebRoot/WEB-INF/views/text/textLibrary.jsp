<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>资源类型</title>
    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>

		<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/text/textLibrary.htm?action=getLibListJson'; 
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'库名称', dataIndex:'rtliName', sortable : true},  //定义操作渲染函数，如果确实没有操作，可以删除此行
				{header:'语种', dataIndex:'rtliLanguage', sortable : true},
				{header:'序号', dataIndex:'rtliIndex', sortable : true}
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
					{name:'rtliName'},
					{name:'rtliIndex'},
					{name:'rtliId'},
					{name:'rtliLanguage'}
				]),
				sortInfo : {
					field : 'rtliIndex',
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
							text: '新增', 
							enableToggle: false, 
							iconCls:'common_add', 
							handler:function(){ 
								addResType(); 
							} 
						},'-',{ 
							text: '编辑', 
							enableToggle: false, 
							iconCls:'common_edit', 
							handler:function(){ 
								editResType();
							} 
						},'-',{ 
							text: '删除', 
							enableToggle: false, 
							iconCls:'common_delete', 
							handler:function(){ 
								delResType();
							} 
						},'-',{ 
							text: '分类设置', 
							enableToggle: false, 
							iconCls:'common_go', 
							handler:function(){ 
								var selectRow = grid.getSelectionModel().getSelected();
								var rtli_Id = 0;
								if(selectRow){
									rtli_Id = selectRow.get("rtliId");
								    window.location.href = "<%=path %>/console/data/text/textLibClassfiy.htm?rtliId="+rtli_Id;
								}else{
									Ext.Msg.alert("提示","请点击选择资源库!");
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
			
			grid.on('dblclick', editResType); //双击进入模板设置
			
			//新增资源类型
			function addResType(){
				window.location.href = "<%=path %>/console/data/text/textLibrary.htm?action=addLibrary";
			}
			
			
			
			//编辑资源类型
			function editResType(){
					var selectRow = grid.getSelectionModel().getSelected();
					var rtli_Id = 0;
					if(selectRow){
					    rtli_Id = selectRow.get("rtliId");
					    window.location.href = "<%=path %>/console/data/text/textLibrary.htm?action=addLibrary&rtliId="+rtli_Id;
					}else{
						Ext.Msg.alert("提示","请点击选择资源库!");
					}
			}
			
			//删除资源类型
			function delResType(){ 
			    var selectRow = grid.getSelectionModel().getSelected();
				var rtli_Id = 0;
				if(selectRow){
					rtli_Id = selectRow.get("rtliId");
				    Ext.Msg.confirm('提示','您确定要删除该资源库吗?',function(button,text){  
					if(button == 'yes'){
						window.location.href = "<%=path %>/console/data/text/textLibrary.htm?action=deleteLibrary&rtliId="+rtli_Id;
					}
				}); 
				    
				}else{
					Ext.Msg.alert("提示","请点击选择资源库!");
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
		 ${result }
	</body> 
</html>