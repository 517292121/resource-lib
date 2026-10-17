<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>用户资源列表</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>   
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
	<script type="text/javascript">
		var limitLength = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>;
		var pagingDisplayMsg = '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>';
		var loginUser = <%=MyRequestUtil.getLoginUserId(request) %>
	
		// -- 资源审核 --
		Ext.onReady(function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'side';
			var rsdaStatus_combox = null;
			var resMissionInfoDataCheck_Win = null;
			var resSourceDataDetail_Win = null;
			// 构造数据
			var store = new Ext.data.Store({
				proxy : new Ext.data.HttpProxy({
					url : '<%=path %>/console/data/gatherResource.htm?action=gatherResourceListJson'
				}),
				reader : new Ext.data.JsonReader({
					totalProperty : 'totalProperty',
					root : 'root'
				}, [{
					name : 'rsdaId',
					type : 'int'
				}, {
					name : 'rsdaTitle',
					type : 'string'
				}, {
					name : 'rmstName',
					type : 'string'
				}, {
					name : 'rltyId',
					type : 'string'
				}, {
					name : 'rsdaBuilddate',
					type : 'string'
				}, {
					name : 'rsdaStatus',
					type : 'string'
				}]),
				sortInfo : {
					field : 'rsdaStatus',
					direction : 'ASC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			});
			// 创建选择模式对象
			var cb = new Ext.grid.CheckboxSelectionModel({
				//singleSelect : true
			});
			
			var sm = new Ext.grid.CheckboxSelectionModel(); // 创建多选模式
			// 定义列模型
			var cm = new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), 
				sm,
				{
					header : '题名',
					dataIndex : 'rsdaTitle',
					sortable : true
				}, {
					header : '所属特色库',
					dataIndex : 'rmstName',
					sortable : true
				}, {
					header : '创建时间',
					dataIndex : 'rsdaBuilddate',
					sortable : true
				}, {
					header : '状态',
					//width : 35,
					align : 'center',
					dataIndex : 'rsdaStatus',
					renderer : checkState,
					sortable : true
				}
			]);
			// 查询
			var searchMan = function() {
				//rsdaTitle = rsdaTitle.getValue();
				//rsdaStatuValue = rsda_status_Combox.getValue();
				store.reload({
					params : {
						start : 0,
						limit : limitLength,
						rsdaTitle : rsdaTitle.getValue(),
						rsdaStatus: rsda_status_Combox.getValue()
					}
				});
			}
			// 资源状态下拉框
			var rsda_status_store = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [[-1, '不限'], [2, '待审核'],[3, '审核通过'], [4, '审核未通过']]
			});
			var rsda_status_Combox = new Ext.form.ComboBox({
				fieldLabel : "状态",
				mode : 'local',
				store : rsda_status_store,
				emptyText : '不限',
				triggerAction : 'all',
				editable : false,
				valueField : 'value',
				displayField : 'text',
				width : 100,
				value : -1,
				listeners : {
					'select' : function() {
						searchMan();
					}
				}
			});
			
			var rsdaTitle = new Ext.form.TextField({
				id : 'rsdaTitle',
				name : 'rsdaTitle',
				anchor : '95%'
			});
			// 创建工具条
			var tbar = new Ext.Toolbar(['题名：', rsdaTitle, '-','状态：',rsda_status_Combox, '-', {
				text : '&nbsp;查&nbsp;询&nbsp;',
				iconCls : 'common_search',
				handler : searchMan
			}, '-', {
				text : '&nbsp;修&nbsp;改&nbsp;',
				iconCls : 'common_edit',
				tooltip : '修改选择的资源<br>当选择多条时以第一条为准！',
				handler : updateResource
			}, '-', {
				text : '&nbsp;审&nbsp;核&nbsp;',
				iconCls : 'common_edit',
				tooltip : '查看并审核选择的资源<br>当选择多条时以第一条为准！',
				handler : resChecked
			}, '-', {
				text : '&nbsp;通&nbsp;过&nbsp;',
				iconCls : 'common_past',
				tooltip : '设置选择的资源为审核<span style="color:blue">通过</span><br>如果不选择任何资源,则设置所有未审核的资源为审核通过！',
				handler : function(){
					updateStatus(<%=ResSourceData.STATUS_PASS %>);
				}
			}, '-', {
				text : '不通过',
				iconCls : 'common_nopast',
				tooltip : '设置选择的资源为审核<span style="color:blue">不通过</span><br>如果不选择任何资源,则设置所有未审核的资源为审核不通过！',
				handler : function(){
					updateStatus(<%=ResSourceData.STATUS_NOTPASS %>);
				}
			}, '-', {
				text : '&nbsp;删&nbsp;除&nbsp;',
				iconCls : 'common_delete',
				tooltip : '删除选择的资源！',
				handler : function() {
					deleteResource();
				}
			}]);
			// 创建一个分页控件
			var pagingBar = new Ext.PagingToolbar({
				pageSize : limitLength,
				store : store,
				displayInfo : true,
				displayMsg : pagingDisplayMsg,
				emptyMsg : '没有记录',
				plugins : new Ext.ux.ProgressBarPager(),
				items : [ {
					xtype : 'label',
					style : 'padding-left: 15px;',
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
			});
			// 组合表格
			var grid = new Ext.grid.GridPanel({
				region : 'center',
				el : grid,
				cm : cm,
				sm : sm,
				store : store,
				viewConfig : {
					forceFit : true
				},
				border : false,
				loadMask : true, // 加载中显示一个读取中的覆盖提示
				frame : false,
				stripeRows : true, // 行分割符
				columnLines : true, // True表示为在列分隔处显示分隔符
				autoScroll : true,
				autoExpandColumn : 'rsdaBuilddate',
				enableColumnHide : false,
				tbar : tbar,
				bbar : pagingBar
			});
			// 创建主面板
			var displayPanel = new Ext.Viewport({
				layout : 'border',
				items : [grid]
			});
			// 加载数据
			store.load({
				params : {
					start : 0,
					limit : limitLength
				}
			});
			store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
					rsdaTitle : rsdaTitle.getValue(),
					rsdaStatus: rsda_status_Combox.getValue()
				});
			});
			
			// ---------------------------- 自定义函数 ----------------------------------
			// 状态
			function checkState(val) {
				if(val == '2')
					return '<img src="<%=path %>/images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
				else if(val == '3')
					return '<img src="<%=path %>/images/res_minfo/res_pass.png" title="审核通过" width="16" height="16" />';
				else if(val == '4') return '<img src="<%=path %>/images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
			}
			
			function resChecked(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow) {
					var rsdaId = selectRow.json.rsdaId;
					var rltyId = selectRow.json.rltyId;
					window.location.href = "<%=path %>/console/data/rmsMissionInfoDataCheck.htm?action=initResSourceDataDetail&rltyId=1&reqType=gather&rsdaId=" + rsdaId;
				}else{
					Ext.Msg.alert("提示","请点击选择需要审核的资源!");
				}	
			}
			
			//获取选中的第一条数据ID
			function getFirstDataId(){
			    var rows = grid.getSelectionModel().getSelections();
			    if(rows.length<1){
			        return "";
			    }
			    var ids = rows[0].get("rsdaId");
                return ids;
			}
			
			//修改资源信息
			function updateResource(){
				var id = getFirstDataId();
				if(id.length < 1){
					Ext.Msg.alert("提示","请点击选择需要修改的资源!");
				}
				window.location.href = "<%=path %>/console/data/inputSingle.htm?action=doResourceEdit&reqType=6&sid=" + id;
			}
			
			function delresource(){
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
				});
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow) {
					var rsdaId = selectRow.json.rsdaId;
					Ext.Msg.confirm('提示','是否删除该记录？\n',function(button,text){    
		                if(button == "yes"){
						    myMask.show();
							 Ext.Ajax.request({ 
								url: '<%=path %>/console/data/resource/SourceManager.htm?action=deleteResource&idlist='+rsdaId,
								success : function(response, options) {
									myMask.hide();
									store.reload();
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
			
			function deleteResource(){
				var rows = grid.getSelectionModel().getSelections();
				var rsdaIds = "";
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
				});
				if(rows.length >= 1) {
					Ext.Msg.confirm("确认消息框", "您确定要删除选中的资源吗？\n", function(btn, txt) {
						if(btn == 'yes') {
							for (var i = 0; i < rows.length; i++) {
								if (i > 0){
									rsdaIds += ",";
								}	
								rsdaIds += rows[i].json.rsdaId;
							}
							Ext.Ajax.request({ 
								url:'<%=path %>/console/data/resource/SourceManager.htm?action=deleteResource&idlist='+rsdaIds,
								method: 'GET',   
			           			text: "Updating...",     
			           			success: function (result, request ){
			           			    if(result.responseText == "1"){
			           			    	myMask.hide();
				           			    Ext.Msg.alert("消息","删除成功！");
				           			    store.reload();
			           			    }else{
			           			    	myMask.hide();
			           			   		Ext.Msg.alert("错误","删除失败！");
			           			    }
			           			}
							});
						}
					});
				}else{
					alertWarring('请选择待删除的数据！\n\n');
				}	
			}
			
			
			function updateStatus(statu){
				var rows = grid.getSelectionModel().getSelections();
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
				});
				var statuText = "";
				if(statu == <%=ResSourceData.STATUS_PASS %>){
					statuText = "<span style='color:blue'>通过</span>";
				} if (statu == <%=ResSourceData.STATUS_NOTPASS %>) {
					statuText = "<span style='color:red'>不通过</span>";
				}
				var rsdaIds = "";
				if(rows.length >= 1) {
					Ext.Msg.confirm("确认消息框", "您确定要设置选择的资源为审核"+statuText+"吗？\n", function(btn, txt) {
						if(btn == 'yes') {
							for (var i = 0; i < rows.length; i++) {
								if (i > 0){
									rsdaIds += ",";
								}	
								rsdaIds += rows[i].json.rsdaId;
							}
							Ext.Ajax.request({ 
								url:'<%=path %>/console/data/gatherResource.htm?action=auditGatherResource&ids='+rsdaIds +"&statu=" + statu,
								method: 'GET',   
			           			text: "Updating...",     
			           			success: function (result, request ){
			           			    store.reload();
		           			    	myMask.hide();
			           			}
							});
						}
					});
				}else{
					Ext.Msg.confirm("确认消息框", "您确定要设置所有未审核的资源为审核"+statuText+"吗？\n", function(btn, txt) {
						if(btn == 'yes') {
							for (var i = 0; i < rows.length; i++) {
								if (i > 0){
									rsdaIds += ",";
								}	
								rsdaIds += rows[i].json.rsdaId;
							}
							Ext.Ajax.request({ 
								url:"<%=path %>/console/data/gatherResource.htm?action=auditGatherResource&statu=" + statu,
								method: 'GET',   
			           			text: "Updating...",     
			           			success: function (result, request ){
			           			    store.reload();
		           			    	myMask.hide();
			           			}
							});
						}
					});
				}
			}
			
			grid.on('dblclick', resChecked);
		});
	</script> 
  </head>
  
  <body>
    <!-- 资源保存成功的图片提示 -->
    <div id="SuccessMessageBox" class="SuccessMessageBox"></div>
    ${result}
    
  </body>
</html>
