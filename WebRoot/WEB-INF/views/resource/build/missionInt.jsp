<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissDevolve"%>
<%
	String path = request.getContextPath();
	long rminId = MyRequestUtil.getLongAttribute(request,"rminId");
	long loginUser = MyRequestUtil.getLoginUserId(request);
%>
<%@ include file="../../common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
	
	
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/resource/missionInfoManagement.htm?action=deliverList&mtypes=<%=ResMissDevolve.MISSION_JIAGONG %>,<%=ResMissDevolve.MISSION_FANYI %>'; 
			
			
				// 转交类型
				var deliver_type_store = new Ext.data.SimpleStore({
					fields : ['value', 'text'],
					data : [[-1, '不限'], [1, '转出'], [2, '转入']]
				});
			
				var deliver_type_Combox = new Ext.form.ComboBox({
					fieldLabel : "转交类型",
					mode : 'local',
					store : deliver_type_store,
					emptyText : '不限',
					triggerAction : 'all',
					editable : false,
					valueField : 'value',
					displayField : 'text',
					width : 120,
					value : -1
				});
				
				// 任务类型
				var mission_type_store = new Ext.data.SimpleStore({
					fields : ['value', 'text'],
					data : [
					     [-1, '不限'],
						 [<%=ResMissDevolve.MISSION_JIAGONG %>, '数据录入'], 
						 [<%=ResMissDevolve.MISSION_FANYI %>, '其它语录入']
					]
				});
				
				var mission_type_Combox = new Ext.form.ComboBox({
					fieldLabel : "任务类型",
					mode : 'local',
					store : mission_type_store,
					emptyText : '不限',
					triggerAction : 'all',
					editable : false,
					valueField : 'value',
					displayField : 'text',
					width : 120,
					value : -1
				});
			
			
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
				    }if(v == <%=ResMissDevolve.MISSION_OTHERCHECK %>){
				    	return "其它语审核";
				    }
				}},
				{header:'转交类型', dataIndex:'rmdePreuid', renderer: function(v){
				    if(v == <%=loginUser %> ){
				       return "<span style='color:blue'>转出</span>";
				    }else{
				        return "转入";
				    }
				}},
				{header:'转交人', dataIndex:'preSmanName'},
				{header:'目标人', dataIndex:'afterSmanName'},
				{header:'转交时间', dataIndex:'rmdeDevodate'}, 
				{header:'状态', dataIndex:'rmdeStatus', align:'center', renderer: function(v){
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
				    {name:'rmdePreuid'},
				    {name:'preSmanName'}, 
				    {name:'afterSmanName'}, 
					{name:'rmdeDevodate'}, 
					{name:'rmdeStatus'}, 
					{name:'rmdeRemark'}, 
					{name: 'rmdeId'},  //转交记录ID
					{name: 'rmdeRminId'}, //作业ID
				]) 
			}); 
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
			grid_store.on('beforeload',function(){
				Ext.apply(  
				this.baseParams, 
				{ 
					type: deliver_type_Combox.getValue(), 
					missType: mission_type_Combox.getValue()
				}); 
			}); 
			
			var grid = new Ext.grid.GridPanel({ 
				// height: document.documentElement.clientHeight-2, 
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				// style: 'margin-top:1px;', 
				loadMask: true,     //加载时提示load... 
				frame : false,
				border: false,
				viewConfig: { 
					forceFit: true 
				}, 
				cm: cm, 
				sm: sm, 
				autoScroll : true,
				region : 'center',
				tbar: new Ext.Toolbar({  //工具栏 
					items:['转交类型：',
					    deliver_type_Combox, '-',
					    '任务类型：',
					    mission_type_Combox, '-',
						{ 
							text: '&nbsp;查&nbsp;询&nbsp;', 
							enableToggle: false, 
							iconCls:'common_search', 
							handler: function(){ 
								grid_store.load({params:{
						            start:0,
						            limit:pageNum,
						            type: deliver_type_Combox.getValue(), 
									missType: mission_type_Combox.getValue()
						        }}); 
							} 
						}, '-', 
						{ 
							text: '&nbsp;撤&nbsp;销&nbsp;', 
							enableToggle: false, 
							iconCls:'cancel', 
							handler: function(){ 
								chexiao();
							} 
						},'-' ,{ 
							text: '&nbsp;接&nbsp;受&nbsp;', 
							enableToggle: false, 
							iconCls:'jieshou', 
							handler: function(){ 
							    accede(<%=ResMissDevolve.DELIVER_SUCCESS %>);
							} 
						},'-',{ 
							text: '&nbsp;拒&nbsp;绝&nbsp;', 
							enableToggle: false, 
							iconCls:'jujue', 
							handler: function(){ 
								accede(<%=ResMissDevolve.DELIVER_FAIL %>);
							} 
						},'-',{ 
							text: '&nbsp;删&nbsp;除&nbsp;', 
							enableToggle: false, 
							iconCls:'common_delete', 
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
					displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>', 
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
			
			function accede(result){
			    var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
					var s = selectRow.get("rmdeStatus");
					var preUser = selectRow.get("rmdePreuid");
				    if(s == 2){
				        Ext.Msg.alert("错误","该任务您已接受，不能进行处理！");
				        return;
				    }if(s == 3){
				        Ext.Msg.alert("错误","该任务您已拒绝，不能进行处理！");
				        return;
				    }
				    if(<%=loginUser %> == preUser){
				        Ext.Msg.alert("错误","该任务为您的转出任务，操作无效！");
				        return;
				    }
				    
				    var rmdeId = selectRow.get("rmdeId");  //转交记录ID
				    var msg = result == 2 ? "您确定要接受该任务吗？\n" : "您确定要拒绝该任务吗？\n";
				    Ext.Msg.confirm('提示',msg,function(button,text){    
		                if(button == "yes"){
						    Ext.Ajax.request({ 
								url:"<%=path %>/console/data/resource/missionInfoManagement.htm?action=revertDeliver&deliverId="+rmdeId+"&statu=" + result,
								method: 'GET',   
			           			text: "Updating...",     
			           			success: function (result, request ){
			           			    if(result.responseText == "1"){
				           			    Ext.Msg.alert("消息","设置成功！");
				           			    grid_store.reload();
			           			    }else{
			           			   		Ext.Msg.alert("消息","设置失败！");
			           			    }
			           			}
							});
						}
					})
				}else{
					Ext.Msg.alert("提示","请点击选择需要设置的记录!");
				} 
			}
			
			function deleteData(){
			    var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var s = selectRow.get("rmdeStatus");
				    if(s == 1){
				        Ext.Msg.alert("错误","该任务您还未处理，暂不能删除！");
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
			           			   		Ext.Msg.alert("消息","删除失败！");
			           			    }
			           			}
							});
						}
					})		
				}else{
					Ext.Msg.alert("提示","请点击选择需要删除的记录!");
				} 
			}
			
			function chexiao(){
			    var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var s = selectRow.get("rmdeStatus");
				    var preUser = selectRow.get("rmdePreuid");
				    if(s == 2){
				        Ext.Msg.alert("错误","该任务对方已接受，不能再撤销！");
				        return;
				    }
				    if(s == 3){
				    	Ext.Msg.alert("错误","该任务对方已拒绝，现已转入您的任务加工中！");
				        return;
				    }
				    if(<%=loginUser %> != preUser){
				        Ext.Msg.alert("错误","该任务为转入任务，撤销无效！");
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
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
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