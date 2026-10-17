<%@ page language="java" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
     <script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			//json元数据获取路径
			var url_data_grid = '<%=path %>/console/links/linkList.htm?action=getLinkInfoToJson';
			//保存或者更改页面
			var url_saveOrUpdate_form = '<%=path %>/console/links/linkList.htm?action=saveOrUpdateInitpage';
			//删除
			var url_delete_form = '<%=path %>/console/links/linkList.htm?action=deleteLinkInfo';
			
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(),
				{header:'链接名称', dataIndex:'slinName',width:110, sortable : true},
				{header:'链接网址', dataIndex:'slinLinkpath',width:110, sortable : true}, 
				{header:'链接类型', dataIndex:'slinType',width:110, sortable : true,renderer:status}, 
				{header:'排序号', dataIndex:'slinIndex',width:110, sortable : true}
				
			]); 
			function status(data){
				if(data == 0){
					return '图片';
				}else{
					return '文字';
				}
			}
			var grid_store = new Ext.data.Store({
				proxy: new Ext.data.HttpProxy({url:url_data_grid}),
				reader: new Ext.data.JsonReader({
					totalProperty: 'totalProperty',
					root: 'root'
				},[
					{name:'slinName'}, 
					{name:'slinLinkpath'}, 
					{name:'slinType'}, 
					{name:'slinIndex'},
					{name:'slinImgPath'},
					{name:'slinId'}
					
				]),
				sortInfo : {
					field : 'slinIndex',
					direction : 'asc'
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
				tbar: new Ext.Toolbar({  //工具栏 
					//height: 200,
					//autoWidth :false,
					//width : 100,
					items:[{ 
							text: '&nbsp;新&nbsp;增&nbsp;', 
							enableToggle: false, 
							iconCls:'common_add', 
							handler: DataAdd
							},'-',{ 
							text: '&nbsp;修&nbsp;改&nbsp;', 
							enableToggle: false, 
							iconCls:'common_edit', 
							handler: DataUpdate
							},'-',{ 
							text: '&nbsp;删&nbsp;除&nbsp;', 
							enableToggle: false, 
							iconCls:'common_delete', 
							handler: DeleteForm
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
			
			/**************************** 自定义元素 **********************************/
			var cltyName =new Ext.form.TextField({
				fieldLabel:'分类名称',
				inputType :'text',
				name: 'clty_Name',
				id:'clty_Name',
				allowBlank : false,
				blankText : '不能为空！',
				maxLength : 42,
				maxLengthText : '登录名不得超过42个字符!',
				width:180
			});
			
			var cltyId = new Ext.form.Hidden({ 
				name : "clty_Id", 
				id: "clty_Id" 
			}); 
			cltyId.setValue("0"); 
			/**************************** 自定义元素结束 *******************************/
			
			/**************************** 操作 **********************************/
			
			function DataAdd(){
				window.location.href= url_saveOrUpdate_form;
			}
			
			function DataUpdate(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var slinId = selectRow.get("slinId"); 
					window.location.href= url_saveOrUpdate_form+"&slinId="+slinId;
				}else{
					Ext.Msg.alert("提示","请点击选择要修改的链接信息!");
				}
			}
			grid.on('dblclick', DataUpdate);
			
			function DeleteForm(){
				 var selectRow = grid.getSelectionModel().getSelected();
					if(selectRow){
					    var slinId = selectRow.get("slinId");
					    Ext.Msg.confirm('提示','您确定要删除该链接吗?\n',function(button,text){  
						if(button == 'yes'){ 
						    Ext.Ajax.request({
		                        url:url_delete_form + '&slinId='+slinId ,
			                    success: function(response, option){ 
									Ext.Msg.alert('信息',response.responseText); 
									grid_store.reload(); 
								}
		                    });
						}
					}); 
					    
					}else{
						Ext.Msg.alert("提示","请点击选择要删除的链接!");
					}
			}
			
			
			/**************************** 操作结束 **********************************/
     	 
     		Ext.onReady(function(){ 
				Ext.QuickTips.init(); 
				Ext.form.Field.prototype.msgTarget = 'side';  
				
				// 创建主面板
				var displayPanel = new Ext.Viewport({
					layout : 'border',
					items : [grid]
				});
			}) 
     </script>
  </head>
  
  <body>  
  <div id ="grid"></div>  
  </body>
</html>
