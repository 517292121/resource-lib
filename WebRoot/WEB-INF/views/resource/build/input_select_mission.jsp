<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
    <title>资源建设——作业选择</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
	<%@ include file="../../common/extInclude.jsp" %>   
	<script type="text/javascript">
			var pageNum = '20'; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/extdata.htm?action=missionlist'; 
			var url_data_store='<%=path %>/console/data/extdata.htm?action=store_select&ptype=2';
			var url_sourcetype_store='<%=path %>/console/data/templetManager.htm?action=getSourceTypeByStore';
			
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
			
			var metastore = new Ext.form.ComboBox({
			    fieldLabel: "特色库",
			    mode: 'remote',
			    store: metastore_store,
			    name: '_index_RMST_ID',
			    hiddenName: 'index_RMST_ID',
			    width: 150,
			    emptyText: '请选择特色库...',
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
			    emptyText: '请选择资源类型...',
			    width: 150,
			    triggerAction: 'all',
			    valueField: 'value',
			    displayField: 'text'    
			});
			
			// 作业管理状态下拉框
			var rmin_status_store = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [[-1, '不限状态...'], [1, '未提交'], [2, '待审核'], [3, '通过'], [4, '未通过']]
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
				{header:'作业名称', dataIndex:'RMIN_MISSNAME'}, 
				{header:'所属特色库', dataIndex:'RMST_NAME'}, 
				{header:'资源类型', dataIndex:'RLTY_NAME'},
				{header:'操作者', dataIndex:'SMAN_NAME'}, 
				{header:'创建时间', dataIndex:'RMIN_BUILDDATE'}, 
				{header:'状态', dataIndex:'RMIN_STATUS',renderer : function(val){
					if(val == '1')
						return '<font style="color:red">未提交</font>';
					else if(val == '2')
						return '<font style="color:yellow">待审核</font>';
					else if(val == '3')
						return '<font style="color:green">已通过</font>';
					else
						return '未通过';
				}} 
			]); 
			
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
					{name:'SMAN_NAME'}, 
					{name:'RMIN_BUILDDATE'}, 
					{name:'RMIN_STATUS'} 
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
						metastore, 
						' ', 
						sourcetype,
						' ',
						rmin_status_Combox,
						{ 
							text: '查 询', 
							enableToggle: false, 
							iconCls:'search', 
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
							text: '单条录入', 
							enableToggle: false, 
							iconCls:'input_single', 
							handler: function(){ 
								var selectRow = grid.getSelectionModel().getSelected();
								if(selectRow){
								    var mission_id = selectRow.get("RMIN_ID");
								    var store_id = selectRow.get("RMIN_RMST_ID");
								    var source_type = selectRow.get("RMIN_RLTY_ID");
								    window.location.href = "../../console/data/inputSingle.htm?action=initData&req=3&store="+store_id+"&stype="+source_type+"&rmin_id="+mission_id;       
								}else{
	        						Ext.Msg.alert("提示","请点击选择需要资源录入的作业!");
	        					}
							} 
						} ,
						{ 
							text: '批量导入', 
							enableToggle: false, 
							iconCls:'input_import', 
							handler: function(){ 
								grid_store.reload(); 
							} 
						},
						{ 
							text: '提交完成', 
							enableToggle: false, 
							iconCls:'qxfp',
							handler: function(){ 
								var selectRow = grid.getSelectionModel().getSelected();
								if(selectRow){
								    Ext.Msg.confirm('提示','您确定已加工完该作业并提交吗？\n',function(button, text){
									    if(button == 'yes'){ 
										    var mission_id = selectRow.get("RMIN_ID");
					                        Ext.Ajax.request({
						                        url:"<%=path %>/console/data/resource/missionInfoManagement.htm?action=setMissionStatus&statu=2&missionid="+mission_id,
						                        waitTitle:"数据提交", 
												waitMsg:"作业提交中，请稍候...", 
												
												success: function(form, action){ 
													Ext.Msg.alert('信息','提交成功，请等待审核！'); 
												    grid_store.reload()
												}, 
												failure:function() 
												{ 
													Ext.Msg.alert('错误','对不起，提交失败！'); 
												} 
					                    });
										}
								    }) 
								}else{
	        						Ext.Msg.alert("提示","请点击选择需要提交检验的作业!");
	        					}
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
					plugins: new Ext.ux.ProgressBarPager() 
				}) 
			}); 
			function edit(id){ 
				var result = ''; 
				return result; 
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
	    <table width="100%" height="100%" cellpadding="0" cellspacing="0" border="0">
       
       <tr>
         <td valign="top">
          <div id="grid"></div>
         </td>
       </tr>
       
      </table> 
	</body> 
</html>