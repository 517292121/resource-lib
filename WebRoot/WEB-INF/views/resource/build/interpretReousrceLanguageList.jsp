<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissionInfo"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%
	String path = request.getContextPath();
	long sourceId = ServletRequestUtils.getLongParameter(request,"sourceId",0L);
	long rminId = ServletRequestUtils.getLongParameter(request,"rminId",0L);
	long rminStatu = MyRequestUtil.getIntAttribute(request,"rminStatu");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>资源语种列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
    <%@ include file="../../common/extInclude.jsp" %>  
    <link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
    
    <script type="text/javascript">
        var pageNum = 20;
        var url_data_grid = "<%=path %>/console/data/resource/resourceManager.htm?action=interpretSourceLanguageListJson&sourceId=<%=sourceId %>";
        var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'语种', dataIndex:'RLTY_NAME'},
				{header:'加工者', dataIndex:'RSLA_USERNAME'},
				{header:'加工时间', dataIndex:'RSLA_DATE'}, 
				{header:'状态', dataIndex:'RSLA_STATUS', renderer: function(val){
				    if(val == '2')
						return '<img src="<%=path %>/images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
					else if(val == '3')
						return '<img src="<%=path %>/images/res_minfo/res_pass.png" title="通过" width="16" height="16" />';
					else if(val == '4')
						return '<img src="<%=path %>/images/res_minfo/res_nopass.png" title="未通过" width="16" height="16" />';
				}} 
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'RLTY_NAME'}, 
				    {name:'RSLA_USERNAME'}, 
					{name:'RSLA_DATE'}, 
					{name:'RSLA_STATUS'}, 
					{name:'RSLA_RSDA_ID'}, 
					{name:'RSLA_RLTY_ID'}, 
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
				autoScroll : true,
				region : 'center',
				tbar: new Ext.Toolbar({  //工具栏 
					items:[ 
						{ 
							text: '翻 译', 
							enableToggle: false, 
							iconCls:'fanyi', 
							handler: function(){ 
							    fanyi();
							} 
						},'-',' ',{ 
							text: '修 改', 
							enableToggle: false, 
							iconCls:'common_edit', 
							handler: function(){ 
							    sourceEdit();
							} 
						},'-',' ',{ 
							text: '删 除', 
							enableToggle: false, 
							iconCls:'common_delete', 
							handler: function(){ 
							    sourceDelete();
							} 
						},'-',' ',{ 
							text: '返 回', 
							enableToggle: false, 
							width:55, 
							iconCls:'common_back', 
							handler: function(){ 
								window.location.href = "<%=path %>/console/data/inputSingle.htm?action=interpretResourceList&rmin_id=<%=rminId%>";
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
		},{
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls  : 'res_nocheck',
			width : 16
		}, {
			xtype : 'label',
			text : '待审核',
//			style : 'padding-left: 3px;color:#364f56;',
			labelStyle : 'my_status',
			width : 10
		},{
			xtype : 'label',
			style : 'padding-left: 15px;',
			width : 10
		},{
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls  : 'res_pass',
			width : 16
		}, {
			xtype : 'label',
			text : '通过',
			style : 'padding-left: 3px;color: #364f56;',
			width : 10
		},{
			xtype : 'label',
			style : 'padding-left: 15px;',
			width : 10
		},{
			xtype : 'label',
			style : 'padding-left: 16px;',
			cls  : 'res_nopass',
			width : 16
		}, {
			xtype : 'label',
			text : '未通过',
			style : 'padding-left: 3px;color: #364f56;',
			width : 10
		}]
				}) 
			}); 
			
			grid.on('dblclick', fanyi); //双击进入作业编辑
			
			function fanyi(){
			    var selectRow = grid.getSelectionModel().getSelected();
				var source_id = 0;
				if(selectRow){
				    source_id = selectRow.get("RSLA_RSDA_ID");
				    languageId = selectRow.get("RSLA_RLTY_ID");
				    sourceStatu = selectRow.get("RSLA_STATUS");
				    if(sourceStatu == 2){
				        Ext.Msg.alert("错误","该资源语种还未审核，暂不能翻译该语种");
				        return;
				    }
				    if(sourceStatu == 4){
				        Ext.Msg.alert("错误","该资源语种未审核通过，暂不能翻译该语种");
				        return;
				    }
				    if(<%=rminStatu == ResMissionInfo.RMINSTATUS_INTERPRET_CHECKOUT %>){
				        Ext.Msg.alert("错误","该作业处于审核中，暂不能进行翻译!");
				        return;
				    }else{
					    window.location.href = "<%=path %>/console/data/inputSingle.htm?action=InterpretSourceForm&sourceId=" + source_id+"&languageId=" + languageId;
				    }
				}else{
					Ext.Msg.alert("提示","请点击选择需要翻译的资源!");
				} 
			}
			
			//编辑资源
			function sourceEdit(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    source_id = selectRow.get("RSLA_RSDA_ID");
				    languageId = selectRow.get("RSLA_RLTY_ID");
				    sourceStatu = selectRow.get("RSLA_STATUS");
				    if(sourceStatu == 3){
				        Ext.Msg.alert("错误","该资源已审核通过，不能再次编辑！");
				        return;
				    }
				    if(<%=rminStatu == ResMissionInfo.RMINSTATUS_INTERPRET_CHECKOUT %>){
				        Ext.Msg.alert("错误","该作业处于审核中，暂不能进行编辑!");
				        return;
				    }
				    window.location.href = "<%=path %>/console/data/resource/resourceManager.htm?action=interpertSourceEdit&reqType=1&sid=" + source_id + "&languageId=" + languageId;
				}else{
					Ext.Msg.alert("提示","请点击选择需要编辑的资源!");
				} 
			}
			
			function sourceDelete(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    source_id = selectRow.get("RSLA_RSDA_ID");
				    languageId = selectRow.get("RSLA_RLTY_ID");
				    sourceStatu = selectRow.get("RSLA_STATUS");
				    if(sourceStatu == 3){
				        Ext.Msg.alert("错误","该资源已审核通过，不能删除！");
				        return;
				    }
				    Ext.Msg.confirm('提示','您确定要删除该资源吗？\n',function(button,text){    
		                if(button == "yes"){
		                    var delUrl = "<%=path %>/console/data/resource/resourceManager.htm?action=deleteSourceByLanguage&sourceId=" + source_id + "&languageId=" + languageId;
						    Ext.Ajax.request({ 
								url:delUrl,
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
	     			}); 
				    
				}else{
					Ext.Msg.alert("提示","请点击选择需要删除的资源!");
				} 
			}
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				//Ext.QuickTips.init(); 
				grid.render('grid'); 
				
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
