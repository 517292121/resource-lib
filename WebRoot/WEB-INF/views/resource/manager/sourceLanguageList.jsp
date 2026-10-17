<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%
	String path = request.getContextPath();
	long sourceId = ServletRequestUtils.getLongParameter(request,"sourceId",0L);
	long rminId = ServletRequestUtils.getLongParameter(request,"rminId",0L);
	long rminStatu = MyRequestUtil.getIntAttribute(request,"rminStatu");
%>
<%@ include file="../../common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>资源语种列表</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
    
    <script type="text/javascript">
        var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>;
        var url_data_grid = "<%=path %>/console/data/resource/resourceManager.htm?action=interpretSourceLanguageListJson&sourceId=<%=sourceId %>&statu=3";
        var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'语种', dataIndex:'RLTY_NAME'},
				{header:'翻译人', dataIndex:'RSLA_USERNAME'},
				{header:'加工时间', dataIndex:'RSLA_DATE'} 
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
				// height: document.documentElement.clientHeight-2, 
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				// style: 'margin-top:1px;', 
				loadMask: true,     //加载时提示load...
				border:false,
				frame:false, 
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
							    sourceEdit();
							} 
						},'-',{ 
							text: '&nbsp;删&nbsp;除&nbsp;', 
							enableToggle: false, 
							iconCls:'common_delete', 
							handler: function(){ 
							    sourceDelete();
							} 
						},'-',{ 
							text: '&nbsp;返&nbsp;回&nbsp;', 
							enableToggle: false, 
							iconCls:'common_back', 
							handler: function(){ 
								window.location.href = "<%=path %>/console/data/resource/SourceManager.htm?action=managerResourceList";
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
			
			grid.on('dblclick', sourceEdit);
			
			//编辑资源
			function sourceEdit(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    source_id = selectRow.get("RSLA_RSDA_ID");
				    languageId = selectRow.get("RSLA_RLTY_ID");
				    window.location.href = "<%=path %>/console/data/resource/resourceManager.htm?action=interpertSourceEdit&reqType=2&sid=" + source_id + "&languageId=" + languageId;
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
				new Ext.Viewport({
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
