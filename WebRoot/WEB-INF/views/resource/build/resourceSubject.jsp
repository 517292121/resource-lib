<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%
	String path = request.getContextPath();
	long rminId = MyRequestUtil.getLongAttribute(request,"rminId");
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
			var url_data_grid = '<%=path %>/console/data/inputSingle.htm?action=subjectListJsonByMission&rminId=<%=rminId %>'; 
			var url_save_form = '<%=path %>/console/data/inputSingle.htm?action=saveOrUpdateSubject&rminId=<%=rminId %>'; 
			var url_dele_form = ''; 
			var url_delete_icon = ''; 
			
			
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'主题名', dataIndex:'RSSU_NAME', width:100, sortable:true},
				{header:'备注', dataIndex:'RSSU_REMARK', width:210,sortable:true}
			]); 
			
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'RSSU_ID'}, 
				    {name:'RSSU_NAME'}, 
				    {name:'RSSU_RMIN_ID'}, 
					{name:'RSSU_REMARK'}, 
				]),
				sortInfo : {
					field : 'RSSU_ID',
					direction : 'ASC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			}); 
			
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
							text: '新增主题', 
							enableToggle: false, 
							iconCls:'common_add', 
							handler:function(){ 
								DataEdit(); 
							} 
						},'-',{ 
							text: '&nbsp;编&nbsp;辑&nbsp;', 
							enableToggle: false, 
							iconCls:'common_edit', 
							handler: function(){ 
							    dataUpdate();
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
								window.location.href = "<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id=<%=rminId%>";
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
			
			
			///*******************定义操作表单**************************************// 
			//ID值储存隐藏域
			var idhide = new Ext.form.Hidden({ 
				name : "rssuId", 
				id: "rssuId" 
			}); 
			idhide.setValue("0"); 
			
			
			///主题名称
			var rssuName = new Ext.form.TextField({ 
				fieldLabel: '主题名称', 
				name: 'rssuName', 
				id:'rssuName',
				anchor : '90%',
				allowBlank : false,
				blankText : '主题名称！',
				maxLength : 100,
				maxLengthText : '主题名称不能超过100个字符!'
			}); 
			
			///主题备注
			var rssuRemark = new Ext.form.TextArea({ 
				fieldLabel: '备注', 
				name: 'rssuRemark', 
				id:'rssuRemark',
				height : 70,
				anchor : '90%',
				maxLength : 1000,
				maxLengthText : '主题备注不得超过1000个字符!'
			}); 
			
			
			
			///定义表单 
			var dataForm = new Ext.form.FormPanel({ 
				height: 155, //定义表单窗体高度，根据实际情况何必. 
				labelWidth: 80, 
				labelAlign: 'right', 
				buttonAlign: 'center', 
				url: url_save_form, 
				frame: true,  //如果设为false或不设则表单的背景为白色 
				bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
				items: [ 
					idhide,
					rssuName,
					rssuRemark
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
			
			
			grid.on('dblclick', dataUpdate); //双击进入作业编辑
			
			function dataUpdate(){ 
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				
					//获取基本信息的值
				    var rId = selectRow.get("RSSU_ID");
				    var rRemark = selectRow.get("RSSU_REMARK");
				    var rName = selectRow.get("RSSU_NAME");
					
				    //弹出编辑窗口
				    DataEdit();
				    
				    //将值赋到窗口中对应的元素中
				    idhide.setValue(rId);
				    rssuRemark.setValue(rRemark);
				    rssuName.setValue(rName);
				}else{
					Ext.Msg.alert("提示","请点击选择编辑项!");
				}
			}
			
			function deleResource(){ 
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '主题删除中...请稍后！',
						removeMask : true // 完成后移除
				});
			    var selectRow = grid.getSelectionModel().getSelected();
				var source_id = 0;
				if(selectRow){
				    var rssuId = selectRow.get("RSSU_ID");
					Ext.Msg.confirm('提示','您确定要删除该记录吗？\n',function(button,text){  
						if(button == 'yes'){ 
							 myMask.show();
							 Ext.Ajax.request({ 
								url: "<%=path %>/console/data/inputSingle.htm?action=deleteSubject&rssuId="+rssuId,
								success : function(response, options) {
									if(response.responseText =="0"){
										Ext.Msg.alert('温馨提示','该主题已经关联数据，不能删除！');
									}
									myMask.hide();
									grid_store.reload();
								},
								failure : function(form, action) {
									myMask.hide();
									Ext.Msg.alert('系统维护中，请稍后再试！');
								}
							}); 
						} 
					}); 
				}else{
					Ext.Msg.alert("提示","请点击选择需要删除的资源!");
				}
			} 
			
			
			
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
						title: '编辑主题信息', 
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
	</body> 
</html>