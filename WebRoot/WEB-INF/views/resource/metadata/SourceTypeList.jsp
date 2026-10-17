<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResLiterType"%>
<%
	String path = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>在这里输入标题</title>
    
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    

		<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/sourceType.htm?action=SourceTypeListJson'; 
			var url_save_form = '<%=path %>/console/data/sourceType.htm?action=SaveSourceType'; 
			var url_dele_form = '<%=path %>/console/data/sourceType.htm?action=DelSourceType'; 
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'名称', dataIndex:'RLTY_NAME', sortable : true},  //定义操作渲染函数，如果确实没有操作，可以删除此行
				{header:'编码', dataIndex:'RLTY_CODE', sortable : true},
				{header:'类别', dataIndex:'RLTY_TYPE', sortable : true, renderer: function(v){
				    if(v == <%=ResLiterType.TYPE_DOCUMENT %>){
				    	return "文档";
				    }
				    if(v == <%=ResLiterType.TYPE_PICTURE %>){
				    	return "图片";
				    }
				    if(v == <%=ResLiterType.TYPE_MEDIA %>){
				    	return "音频";
				    }
				    if(v == <%=ResLiterType.TYPE_VIDEO%>){
				    	return "视频";
				    }
				}} 	
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
					{name:'RLTY_NAME'},
					{name:'RLTY_CODE'},
					{name:'RLTY_TYPE'},
					{name:'RLTY_ID'},
				]),
				sortInfo : {
					field : 'RLTY_ID',
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
							text: '&nbsp;新&nbsp;增&nbsp;', 
							enableToggle: false, 
							iconCls:'common_add', 
							handler:function(){ 
								DataEdit(); 
							} 
						},'-',{ 
							text: '&nbsp;编&nbsp;辑&nbsp;', 
							enableToggle: false, 
							iconCls:'common_edit', 
							handler:function(){ 
								 dataUpdate();
							} 
						},'-',{ 
							text: '&nbsp;删&nbsp;除&nbsp;', 
							enableToggle: false, 
							iconCls:'common_delete', 
							handler:function(){ 
								 deleteData();
							} 
						},'-',{ 
							text: '元素设置', 
							enableToggle: false, 
							iconCls:'common_go', 
							handler:function(){ 
								var selectRow = grid.getSelectionModel().getSelected();
								var rlty_id = 0;
								if(selectRow){
								    rlty_id = selectRow.get("RLTY_ID");
								    window.location.href = "<%=path %>/console/data/sourceType.htm?action=OnTempletEdit&RLTY_ID="+rlty_id;
								}else{
									Ext.Msg.alert("提示","请点击选择资源类型!");
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
			
			grid.on('dblclick', dataEdit); //双击进入模板设置
			function dataEdit(){
					var selectRow = grid.getSelectionModel().getSelected();
					var rlty_id = 0;
					if(selectRow){
					    rlty_id = selectRow.get("RLTY_ID");
					    window.location.href = "<%=path %>/console/data/sourceType.htm?action=OnTempletEdit&RLTY_ID="+rlty_id;
					}else{
						Ext.Msg.alert("提示","请点击选择资源类型!");
					}
			}
			 
			function dataUpdate(){ 
				var selectRow = grid.getSelectionModel().getSelected();
				var rlty_id = 0;
				var rlty_name = "";
				var rlty_code = "";
				if(selectRow){
				    rlty_id = selectRow.get("RLTY_ID");
				    rlty_code = selectRow.get("RLTY_CODE");
				    rlty_name = selectRow.get("RLTY_NAME");
				    DataEdit();
				    idhide.setValue(rlty_id);
				    RLTY_NAME.setValue(rlty_name);
				    RLTY_CODE.setValue(rlty_code);
				    type_combox.setValue(selectRow.get("RLTY_TYPE"));
				    
				}else{
					Ext.Msg.alert("提示","请点击选择资源类型!");
				}
			}
			
			function deleteData(){ 
			    var selectRow = grid.getSelectionModel().getSelected();
				var rlty_id = 0;
				var rlty_name = "";
				if(selectRow){
				    rlty_id = selectRow.get("RLTY_ID");
				    Ext.Msg.confirm('提示','您确定要删除该资源类型吗?',function(button,text){  
					if(button == 'yes'){ 
					    Ext.Ajax.request({
	                        url:url_dele_form + '&RLTY_ID='+rlty_id,
	                        method : 'POST',
	                        success: function(result, request){
	                        	var msg = result.responseText;
								alertInformation(msg);
	                        }
	                    });
	                    grid_store.reload(); 
					}
				}); 
				    
				}else{
					Ext.Msg.alert("提示","请点击选择资源类型!");
				}
			} 
			
			
			///*******************定义操作表单**************************************// 
			var idhide = new Ext.form.Hidden({ 
				name : "RLTY_ID", 
				id: "RLTY_ID" 
			}); 
			idhide.setValue("0"); 
			
			///null 
			var RLTY_NAME = new Ext.form.TextField({ 
				fieldLabel: '名称', 
				name: 'RLTY_NAME', 
				anchor : '90%',
				allowBlank : false,
				blankText : '请输入资源类型名称！',
				maxLength : 40,
				maxLengthText : '类型名称不得超过40个字符!'
			}); 
			var RLTY_CODE = new Ext.form.TextField({ 
				fieldLabel: '编码', 
				name: 'RLTY_CODE', 
				anchor : '90%',
				allowBlank : true,
				blankText : '请输入资源类型名称！',
				maxLength : 40,
				maxLengthText : '类型名称不得超过40个字符!'
			});
			//所属大类store
			var type_store = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [
							[<%=ResLiterType.TYPE_DOCUMENT %>, '文档'],
							[<%=ResLiterType.TYPE_PICTURE %>, '图片'],
							[<%=ResLiterType.TYPE_MEDIA %>, '音频'],
							[<%=ResLiterType.TYPE_VIDEO %>, '视频']
					   ]
			});
			var type_combox = new Ext.form.ComboBox({
				fieldLabel : "类别",
				mode : 'local',
				store : type_store,
				triggerAction : 'all',
				name: "type_combox",
				hiddenName: "rltyType",
				editable : false,
				valueField : 'value',
				displayField : 'text',
				anchor : '90%',
				value : 1,
				allowBlank : false,
				blankText : '请选择类别！'
			});
			
			///定义表单 
			var dataForm = new Ext.form.FormPanel({ 
				height: 140, //定义表单窗体高度，根据实际情况何必. 
				labelWidth: 60, 
				labelAlign: 'right', 
				buttonAlign: 'center', 
				url: url_save_form, 
				frame: true,  //如果设为false或不设则表单的背景为白色 
				bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
				items: [ 
					idhide, 
					RLTY_NAME,
					RLTY_CODE,
					type_combox 
				], 
			
				buttons:[ 
					{ 
						text: '保&nbsp;存', 
						handler: function(){ 
							DataSaveOrUpdate(); 
						} 
					}, 
					{ 
						text: '取&nbsp;消', 
						handler: function(){ 
							win.hide(); 
						} 
					} 
				] 
			}); 
			
			//保存或更新数据 
			function DataSaveOrUpdate(){ 
				dataForm.getForm().submit({ 
					waitTitle:"数据提交", 
					waitMsg:"正在提交表单数据，请稍候...", 
					success: function(form, action){ 
						win.hide(); 
						Ext.Msg.alert('信息',action.result.msg); 
						grid_store.reload(); 
					}, 
					failure:function(form, action) 
					{ 
						Ext.Msg.alert('失败',action.result.msg); 
					} 
				}); 
			} 
			
			///////弹出表单的window 
			var win; 
			function DataEdit(){ 
				dataForm.getForm().reset();  //将之前的清空 
				if(!win){ 
					win = new Ext.Window({ 
						title: '资源类型信息', 
						border : false,
						name: 'editwin', 
						resizable : false,
						width: 320,
						closeAction: 'hide', 
						modal: true, 
						items: [dataForm] 
					}); 
				} 
				win.show(); 
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
	</body> 
</html>