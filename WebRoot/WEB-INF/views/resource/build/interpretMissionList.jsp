<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissionInfo"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissDevolve"%>
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
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数
			var url_data_grid = '<%=path %>/console/data/extdata.htm?action=interpretMissionList'; 
			var url_data_store='<%=path %>/console/data/extdata.htm?action=store_select&ptype=2&hasNull=hasNull';
			var url_sourcetype_store='<%=path %>/console/data/templetManager.htm?action=getSourceTypeByStore&hasNull=hasNull';
			//var url_sysMan_store = '<%=path %>/console/data/resource/missionInfoManagement.htm?action=getSysManagerList&missiontype=<%=ResMissDevolve.MISSION_FANYI %>';
			
			//特色库数据store
			var metastore_store = new Ext.data.Store({
			    proxy: new Ext.data.HttpProxy({url: url_data_store}),
			    reader: new Ext.data.ArrayReader({},[
			        {name: 'value'},
			        {name: 'text'}
			    ])
			});
			
			//资源类型数据store
			var sourcetype_store = new Ext.data.Store({
			    proxy: new Ext.data.HttpProxy({url: url_sourcetype_store}),
			    reader: new Ext.data.ArrayReader({},[
			        {name: 'value'},
			        {name: 'text'}
			    ]),
			    autoLoad : false
			});
			
			// ---------------------------- 任务转交 ----------------------------------
			var metastore = new Ext.form.ComboBox({
			    fieldLabel: "特色库",
			    mode: 'remote',
			    store: metastore_store,
			    name: '_index_RMST_ID',
			    hiddenName: 'index_RMST_ID',
			    width: 150,
			    emptyText: '请先选择特色库...',
			    triggerAction: 'all',
			    valueField: 'value',
			    displayField: 'text'    
			});
			
			//当特色发生改变时，级联改变特色库所联系的资源类型
			metastore.on('select', function(comboBox){
			   var id = comboBox.getValue();
			   sourcetype.clearValue(); //清空之前的资源类型
			   var store_url = url_sourcetype_store + "&store=" + id;
			   sourcetype_store.proxy = new Ext.data.HttpProxy({url: store_url});
			   sourcetype_store.load();
			})
			
			var sourcetype = new Ext.form.ComboBox({
			    fieldLabel: "资源类型",
			    mode: 'remote',
			    store: sourcetype_store,
			    name: '_index_RMIN_RLTY_ID',
			    hiddenName: 'index_RMIN_RLTY_ID',
			    emptyText: '再选择资源类型...',
			    width: 150,
			    triggerAction: 'all',
			    valueField: 'value',
			    displayField: 'text'    
			});
			
			// 作业管理状态下拉框
			var rmin_status_store = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [[-1, '不限'], [1, '加工中'], [2, '待审核'], [3, '审核通过'], [4, '审核未通过']]
			});
		
			var rmin_status_Combox = new Ext.form.ComboBox({
				fieldLabel : "状态",
				mode : 'local',
				store : rmin_status_store,
				emptyText : '不限',
				triggerAction : 'all',
				editable : false,
				valueField : 'value',
				displayField : 'text',
				width : 100,
				value : -1
			});
			
			
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				//sm, 
				{header:'作业名称', dataIndex:'RMIN_MISSNAME',sortable : true}, 
				{header:'所属特色库', dataIndex:'RMST_NAME'}, 
				{header:'资源类型', dataIndex:'RLTY_NAME'},
				/**
				{header:'操作者', dataIndex:'SMAN_NAME', renderer: function(v){
				     if(v.length < 1){
				        return "<%=ConstantsObj.noAuthor %>";
				     }else{
				        return v;
				     }
				}}, 
				{header:'创建时间', dataIndex:'RMIN_BUILDDATE'}, 
				**/
				{header:'提交时间', dataIndex:'RMIN_ENDDATE',sortable : true},
				{header:'资源数量', dataIndex:'SOURCENUM',sortable : true},
				{header:'描述', dataIndex:'RMIN_REMARK', renderer: function(v){
				    if(v == null){
				       return "";
				    }
				    if(v.length < 15){
				       return "<div title='"+v+"'>"+v+"</div>";
				    }
				    return "<div title='"+v+"'>"+v.substring(0,15)+"</div>";
				}}
				//{header:'状态', dataIndex:'RMIN_STATUS',renderer : checkState} 
			]); 
			
			// 作业状态
			function checkState(val) {
				if (val == '<%=ResMissionInfo.RMINSTATUS_COMMIT_PASS %>')
					return '<img src="<%=path %>/images/res_minfo/res_pass.png" title="审核通过" width="16" height="16" />';
				else if(val == '<%=ResMissionInfo.RMINSTATUS_INTERPRET_CHECKOUT %>')
					return '<img src="<%=path %>/images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
				if(val == '<%=ResMissionInfo.RMINSTATUS_INTERPRET_FAIL %>')
					return '<img src="<%=path %>/images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
			}
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'RMIN_ID'},
				    {name:'RMIN_RMST_ID'},  //特色库ID
				    {name:'RMIN_RLTY_ID'},  //资源类型ID
					{name:'RMIN_MISSNAME'}, 
					{name:'RMST_NAME'}, 
					{name:'RLTY_NAME'}, 
					/**
					{name:'SMAN_NAME'}, 
					{name:'RMIN_BUILDDATE'}, **/
					{name:'RMIN_ENDDATE'}, 
					{name:'SOURCENUM'},
					{name:'RMIN_REMARK'}, 
					//{name:'RMIN_STATUS'},
					{name:'RMIN_INTERUSER'}
				]) 
			}); 
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
			grid_store.on('beforeload',function(){ 
				Ext.apply(  
				this.baseParams, 
				{ 
					store: metastore.getValue(), 
					sourceType: sourcetype.getValue(),
					statu: rmin_status_Combox.getValue()
				}); 
			}); 
			var grid = new Ext.grid.GridPanel({ 
				// height: document.documentElement.clientHeight, 
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
					items:['特色库：',
						metastore, 
						'-', 
						sourcetype,
						//'-','状态：',
						//rmin_status_Combox,
						'-',
						{ 
							text: '&nbsp;查&nbsp;询&nbsp;', 
							enableToggle: false, 
							iconCls:'common_search', 
							handler: function(){ 
								grid_store.load({params:{
						            start:0,
						            limit:pageNum,
						            RMST_ID: metastore.getValue(), 
									RLTY_ID: sourcetype.getValue(),
									store: metastore.getValue(), 
									sourceType: sourcetype.getValue(),
									statu: rmin_status_Combox.getValue()
						        }}); 
							} 
						} ,
						{ 
							text: '查看任务', 
							enableToggle: false, 
							iconCls:'common_list', 
							handler: function(){ 
								toSourceList();
							} 
						}
						/**
						,{ 
							text: '提交审核', 
							enableToggle: false, 
							iconCls:'qxfp', 
							handler: function(){ 
								submitCheckout();
							} 
						},{ 
							text: '任务转交', 
							enableToggle: false, 
							iconCls:'qxfp', 
							handler: function(){ 
								missionDeliver();
							} 
						} **/
					] 
				}), 
				bbar: new Ext.PagingToolbar({  //分页信息栏 
					pageSize: pageNum, 
					store: grid_store, 
					displayInfo: true, 
					displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>', 
					emptyMsg: '没有记录', 
					plugins: new Ext.ux.ProgressBarPager()
					/** 
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
					*/
				}) 
			}); 
			
			grid.on('dblclick', toSourceList);
			
			function toSourceList(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var mission_id = selectRow.get("RMIN_ID");
				   // window.location.href = "<%=path %>/console/data/inputSingle.htm?action=interpretResourceList&rmin_id="+mission_id;
				   window.location.href = "<%=path %>/console/data/resource/missionLanguage.htm?action=missionLanguageList&folder=build&rminId=" + mission_id;       
				   
				}else{
					Ext.Msg.alert("提示","请点击选择作业！");
				}
			}
			
			////////////////////////////////////////////任务转交
			
			 
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
	    <table width="100%" height="100%" cellpadding="0" cellspacing="0" border="0">
       
       <tr>
         <td valign="top">
          <div id="grid"></div>
         </td>
       </tr>
       
      </table> 
	</body> 
</html>