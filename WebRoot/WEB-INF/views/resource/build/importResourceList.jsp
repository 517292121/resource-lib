<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissionInfo"%>
<%
	String path = request.getContextPath();
	ResMissionInfo mission = (ResMissionInfo)request.getAttribute("mission");
	long missionStatu = 0;
	if(mission != null){
	    missionStatu = mission.getRminStatus();
	}
	long rihiId = MyRequestUtil.getLongAttribute(request,"rihiId");
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
			var url_data_grid = '<%=path %>/console/data/resource/importExcel.htm?action=importResourceList&rihiId=<%=rihiId %>'; 
			var url_save_form = ''; 
			var url_dele_form = ''; 
			var url_delete_icon = ''; 
			var missionStatu = <%=missionStatu %>;
			
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'题名', dataIndex:'RSDA_TITLE', width:210, sortable:true},
				{header:'创建者', dataIndex:'RSDA_CREATOR', width:120, sortable:true}, 
				{header:'出版者', dataIndex:'RSDA_PUBLISHER', width:210, sortable:true},
				{header:'录入时间', dataIndex:'RSDA_BUILDDATE', width:120, sortable:true},
				{header:'审核意见', dataIndex:'RSDA_AUDITING', width:240, renderer: function (v){
				    if(v.length < 15){
				        return v;
				    }
				    return "<div title='"+v+"'>" + v.substring(0,15) + "</span>";
				}},  
				{header:'状态', dataIndex:'RSDA_STATUS',renderer : checkState, width:75, align:'center', sortable:true} 
			]); 
			
			// 状态
			function checkState(val) {
				if(val == '2')
					return '<img src="<%=path %>/images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
				else if(val == '3')
					return '<img src="<%=path %>/images/res_minfo/res_pass.png" title="审核通过" width="16" height="16" />';
				else
					return '<img src="<%=path %>/images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
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
					{name:'RSDA_BUILDDATE'}, 
					{name:'RSDA_STATUS'}, 
					{name:'RSDA_AUDITING'}, 
					{name: 'RSDA_ID'}, 
					{name: 'RSDA_CREATOR'},
					{name: 'RSDA_PUBLISHER'},
					{name: 'RSLA_RLTY_ID'},
				]),
				sortInfo : {
					field : 'RSDA_BUILDDATE',
					direction : 'ASC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			}); 
			
			var rminInfo = "文件：【${importHistory.rmhiLocalFilename }】";
			grid_store.load({params:{start:0,limit:pageNum}}); 
			var grid = new Ext.grid.GridPanel({ 
				//height: document.documentElement.clientHeight, 
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				loadMask: true,     //加载时提示load... 
				frame : false,
				border: false,
				enableColumnHide : false,  //屏蔽勾选列
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
							text: '&nbsp;编&nbsp;辑&nbsp;', 
							enableToggle: false, 
							iconCls:'common_edit', 
							handler: function(){ 
							    dataEdit();
							} 
						},'-',{ 
							text: '&nbsp;删&nbsp;除&nbsp;', 
							enableToggle: false, 
							iconCls:'common_delete', 
							handler: function(){ 
							    deleResource();
							} 
						},'-',{ 
							text: '&nbsp;返&nbsp;回&nbsp;', 
							enableToggle: false, 
							iconCls:'common_back', 
							handler: function(){ 
								window.location.href = "<%=path %>/console/data/resource/importExcel.htm?action=initImportHistoryList&rminId=${param.rminId }";
							} 
						},'-',{
							xtype: 'tbtext', 
							text: rminInfo
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
						text : '审核通过',
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
						text : '审核未通过',
						style : 'padding-left: 3px;',
						width : 10
					}] 
				}) 
			}); 
			
			grid.on('dblclick', dataEdit); //双击进入作业编辑
			
			function dataEdit(){
					var selectRow = grid.getSelectionModel().getSelected();
					var source_id = 0;
					if(selectRow){
					    source_id = selectRow.get("RSDA_ID");
					    languageId = selectRow.get("RSLA_RLTY_ID");
					    sourceStatu = selectRow.get("RSDA_STATUS");
					    if(missionStatu == 3){
					        Ext.Msg.alert("提示","该资源所属作业已审核通过，不能再进行编辑!");
					        return;
					    }
					    if(missionStatu == 2){
					        Ext.Msg.alert("提示","该资源所属作业处于审核中，暂不能进行编辑!");
					        return;
					    }
					    if(missionStatu == 4 && (sourceStatu == 3 || sourceStatu == '3')){
					        Ext.Msg.alert("提示","该资源已审核通过，不能再进行编辑!");
					        return;
					    }
					    else{
					    	window.location.href = "<%=path %>/console/data/inputSingle.htm?action=doResourceEdit&reqType=4&sid=" + source_id+"&lanId=<%=mission.getRminLanugage() %>&rihiId=${param.rihiId }";
					    }	               
					}else{
						Ext.Msg.alert("提示","请点击选择资源类型!");
					}
			}
			 
			function deleResource(){ 
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
				});
			    var selectRow = grid.getSelectionModel().getSelected();
				var source_id = 0;
				if(selectRow){
				    sourceStatu = selectRow.get("RSDA_STATUS");
				    if(missionStatu == 3){
				        Ext.Msg.alert("提示","该资源所属作业已审核通过，不能再删除该资源!");
				        return;
				    }
				    if(missionStatu == 2){
				        Ext.Msg.alert("提示","该资源所属作业处于审核中，暂不能删除该资源!");
				        return;
				    }
				    if(missionStatu == 4 && (sourceStatu == 3 || sourceStatu == '3')){
				        Ext.Msg.alert("提示","该资源已审核通过，不能再删除该资源!");
				        return;
				    }
				    source_id = selectRow.get("RSDA_ID");
				    languageId = selectRow.get("RSLA_RLTY_ID");
					Ext.Msg.confirm('提示','您确定要删除该记录吗？\n',function(button,text){  
						if(button == 'yes'){ 
							 myMask.show();
							 Ext.Ajax.request({ 
								url: "<%=path %>/console/data/resource/resourceManager.htm?action=deleteResource&sourceId=" + source_id + "&languageId=<%=mission.getRminLanugage() %>",
								success : function(response, options) {
									myMask.hide();
									grid_store.reload();
								},
								failure : function(form, action) {
									myMask.hide();
									alertError('对不起，删除失败！');
								}
							}); 
						} 
					}); 
				}else{
					Ext.Msg.alert("提示","请点击选择需要删除的资源!");
				}
			} 
			
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				//Ext.QuickTips.init(); 
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
		
		<!-- 资源保存成功的图片提示 -->
        <div id="SuccessMessageBox" class="SuccessMessageBox"></div>
        ${requestScope.result }
	</body> 
</html>