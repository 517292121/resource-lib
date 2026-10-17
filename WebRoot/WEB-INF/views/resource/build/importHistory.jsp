<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
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
			var url_data_grid = '<%=path %>/console/data/resource/importExcel.htm?action=importHistoryList&rminId=${param.rminId }'; 
			
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'文件名', dataIndex:'rmhiLocalFilename', width:210, sortable:true },
				{header:'导入时间', dataIndex:'rmhiImportDate', width:120, sortable:true}, 
				{header:'忽略必填元素', dataIndex:'rmhiCheckMust', width:210, sortable:true, renderer: function(v){
					if(v == "0"){
						return "否";
					}else{
						return "<span style='color:red'>是</span>";
					}
				}},
				{header:'资源数量', dataIndex:'sourceNum', width:120, sortable:true}
			]); 
			
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'rmhiLocalFilename'}, 
				    {name:'rmhiImportDate'}, 
				    {name:'rmhiAddress'}, 
					{name:'rmhiCheckMust'}, 
					{name:'sourceNum'}, 
					{name:'rmhiId'}, 
				]),
				sortInfo : {
					field : 'rmhiImportDate',
					direction : 'DESC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			}); 
			
			var rminInfo = "作业：【${mission.rminMissname}】";
			grid_store.load({params:{start:0,limit:pageNum}}); 
			var grid = new Ext.grid.GridPanel({ 
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
							text: '&nbsp;下&nbsp;载&nbsp;', 
							enableToggle: false, 
							iconCls:'icon_download', 
							handler: function(){ 
							    downLoadFile();
							} 
						},'-',{ 
							text: '查看资源', 
							enableToggle: false,  
							iconCls:'common_list', 
							handler: function(){ 
								sourceList();
							} 
						},'-',{ 
							text: '&nbsp;返&nbsp;回&nbsp;', 
							enableToggle: false, 
							iconCls:'common_back', 
							handler: function(){ 
								window.location.href = "<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id=${requestScope.rminId }";
							} 
						},'-',{ 
							text: '&nbsp;删&nbsp;除&nbsp;', 
							enableToggle: false, 
							iconCls:'common_delete', 
							handler: deleteHistory 
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
					plugins: new Ext.ux.ProgressBarPager()
				}) 
			}); 
			
			function downLoadFile(){
					var selectRow = grid.getSelectionModel().getSelected();
					if(selectRow){
					    var filename = selectRow.get("rmhiAddress");
					    window.location.href = "${requestScope.address}" + filename;
					}else{
						Ext.Msg.alert("提示","请点击选择导入记录!");
					}
			}
			
			function sourceList(){
					var selectRow = grid.getSelectionModel().getSelected();
					if(selectRow){
					    var rmhiId = selectRow.get("rmhiId");
					    window.location.href = "<%=path %>/console/data/resource/importExcel.htm?action=initImportResourceList&rminId=${requestScope.rminId }&rihiId=" + rmhiId;
					}else{
						Ext.Msg.alert("提示","请点击选择导入记录!");
					}
			}
			
			
			//删除导入记录
			function deleteHistory(){
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
				});
				var missionStatu = ${mission.rminStatus };
			    var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var rmhiId = selectRow.get("rmhiId");
				    if(missionStatu == 3 || missionStatu == 2){
				    	Ext.Msg.confirm('提示','您确定要删除该记录吗？\n',function(button,text){  
							if(button == 'yes'){ 
								myMask.show();
								Ext.Ajax.request({ 
								url: "<%=path %>/console/data/resource/importExcel.htm?action=deleteHistory&rmhiId=" + rmhiId,
								success : function(result, options) {
									if(result.responseText == "1"){
			           			    	myMask.hide();
				           			    Ext.Msg.alert("消息","删除成功！");
				           			    grid_store.reload();
			           			    }else{
			           			    	myMask.hide();
			           			   		Ext.Msg.alert("错误",result.responseText);
			           			    }
								}
							}); 
							}
						})	 
				    }
					Ext.Msg.confirm('提示','是否同时删除关联的资源？\n',function(button,text){  
						var action = "";
						if(button == 'yes'){
							action = "deleteHistoryAndResource"; 
						}else{
							action = "deleteHistory";
						} 
							 myMask.show();
							 Ext.Ajax.request({ 
								url: "<%=path %>/console/data/resource/importExcel.htm?action="+action+"&rmhiId=" + rmhiId,
								success : function(result, options) {
									if(result.responseText == "1"){
			           			    	myMask.hide();
				           			    Ext.Msg.alert("消息","删除成功！");
				           			    grid_store.reload();
			           			    }else{
			           			    	myMask.hide();
			           			   		Ext.Msg.alert("错误",result.responseText);
			           			    }
								}
							}); 
					}); 
				}else{
					Ext.Msg.alert("提示","请点击选择需要删除的资源!");
				}
			}
	
			
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
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