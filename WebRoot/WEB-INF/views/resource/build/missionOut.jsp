<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissDevolve"%>
<%
	String path = request.getContextPath();
	long rminId = MyRequestUtil.getLongAttribute(request,"rminId");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>我转交给别人的任务</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
	
	<%@ include file="../../common/extInclude.jsp" %>   
	
	<script type="text/javascript">
			var pageNum = 20; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/resource/missionInfoManagement.htm?action=deliverList&type=2'; 
			
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'作业', dataIndex:'rminMissname'},
				{header:'任务类型', dataIndex:'rmdeMissiontype', renderer: function(v){
				    if(v == <%=ResMissDevolve.MISSION_JIAGONG %>){
				       return "数据录入";
				    }if(v == <%=ResMissDevolve.MISSION_FANYI %>){
				       return "其它语录入";
				    }if(v == <%=ResMissDevolve.MISSION_XIAOYAN %>){
				       return "数据审核";
				    }
				}},
				{header:'转交给', dataIndex:'afterSmanName'},
				{header:'转交时间', dataIndex:'rmdeDevodate'}, 
				{header:'状态', dataIndex:'rmdeStatus', renderer: function(v){
				    if(v == <%=ResMissDevolve.DELIVER_WAITING %>){
				       return "<img src='<%=path %>/images/res_minfo/res_nocheck.png' >";
				    }if(v == <%=ResMissDevolve.DELIVER_SUCCESS %>){
				       return "<img src='<%=path %>/images/res_minfo/res_pass.png' >";
				    }if(v == <%=ResMissDevolve.DELIVER_FAIL %>){
				       return "<img src='<%=path %>/images/res_minfo/res_nopass.png' >";
				    }
				    
				}} ,
				{header:'备注', dataIndex:'rmdeRemark'} 
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'rminMissname'}, 
				    {name:'rmdeMissiontype'}, 
				    {name:'afterSmanName'}, 
					{name:'rmdeDevodate'}, 
					{name:'rmdeStatus'}, 
					{name:'rmdeRemark'}, 
					
					{name: 'rmdeId'},  //转交记录ID
					{name: 'rmdeRminId'}, //作业ID
				]) 
			}); 
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
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
				tbar: new Ext.Toolbar({  //工具栏 
					items:[ 
						{ 
							text: '撤 销', 
							enableToggle: false, 
							iconCls:'back', 
							handler: function(){ 
								chexiao();
							} 
						}, '-' ,{ 
							text: '删 除', 
							enableToggle: false, 
							iconCls:'delete', 
							handler: function(){ 
								deleteData();
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
					items : [ 
						 {
							xtype : 'label',
							style : 'padding-left: 15px;',
							width : 10
						},{
							xtype : 'label',
							style : 'padding-left: 16px;',
							cls : 'res_nocheck',
							width : 16
						}, {
							xtype : 'label',
							text : '未处理',
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
							text : '已接受',
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
							text : '已拒绝',
							style : 'padding-left: 3px;',
							width : 10
						}] 
				}) 
			}); 
			
			function chexiao(){
			    var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var s = selectRow.get("rmdeStatus");
				    if(s == 2){
				        Ext.Msg.alert("错误","该任务对方已接受，不能再撤销！");
				        return;
				    }
				    if(s == 3){
				    	Ext.Msg.alert("错误","该任务对方已拒绝，现已转入您的任务加工中！");
				        return;
				    }
				    Ext.Msg.confirm('提示','您确定要撤销该转交任务吗？\n',function(button,text){    
		                if(button == "yes"){
						    var rmdeId = selectRow.get("rmdeId");  //转交记录ID
						    Ext.Ajax.request({ 
								url:"<%=path %>/console/data/resource/missionInfoManagement.htm?action=chexiaoDeliver&deliverId="+rmdeId,
								method: 'GET',   
			           			text: "Updating...",     
			           			success: function (result, request ){
			           			    if(result.responseText == "1"){
				           			    grid_store.reload();
			           			    }else{
			           			   		Ext.Msg.alert("错误","撤销失败！");
			           			    }
			           			}
							});
						}
					})		
				}else{
					Ext.Msg.alert("提示","请点击选择需要撤销的记录!");
				} 
			}

			function deleteData(){
			    var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var statu = selectRow.get("rmdeStatus");
				    if(statu == <%=ResMissDevolve.DELIVER_WAITING %>){
				         Ext.Msg.alert("错误","该申请对方还未处理，暂不能删除！");
				         return;
				    }
				    Ext.Msg.confirm('提示','是否删除该记录？\n',function(button,text){    
		                if(button == "yes"){
						    var rmdeId = selectRow.get("rmdeId");  //转交记录ID
						    Ext.Ajax.request({ 
								url:"<%=path %>/console/data/resource/missionInfoManagement.htm?action=deleteMissionDeliver&deliverId="+rmdeId,
								method: 'GET',   
			           			text: "Updating...",     
			           			success: function (result, request ){
			           			    if(result.responseText == "1"){
				           			    grid_store.reload();
			           			    }else{
			           			   		Ext.Msg.alert("消息","设置失败！");
			           			    }
			           			}
							});
						}
					})		
				}else{
					Ext.Msg.alert("提示","请点击选择需要删除的记录!");
				} 
			}
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				grid.render('grid'); 
			}) 

		</script> 
	</head> 
	<body>
		<div id="grid"></div> 
	</body> 
</html>