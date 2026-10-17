<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%
	String path = request.getContextPath();
	long storeId = MyRequestUtil.getLongParameter(request,"RSDA_RMST_ID");
	long sourceType = MyRequestUtil.getLongParameter(request,"RSDA_RLTY_ID");
	long rminId = MyRequestUtil.getLongAttribute(request,"rminId");
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
			var url_data_grid = '<%=path %>/console/data/resource/ExportResToCalis.htm?action=resourceListJson'; 
			var url_save_form = ''; 
			var url_dele_form = ''; 
			var url_delete_icon = ''; 
			var url_sourcetype_store='<%=path %>/console/data/templetManager.htm?action=getSourceTypeByStore';
			var msgBox;
			var progressBarReturnNum = 0;
			//特色库store
			var metastore = new Ext.data.Store({
				autoLoad : true,
				proxy : new Ext.data.HttpProxy({
					url : '<%=path %>/console/data/extdata.htm?action=AllStoreJson&hasNull=hasNull'
				}),
				reader : new Ext.data.ArrayReader({}, [{
					name : 'rdtyId',
					type : 'int'
				}, {
					name : 'rdtyName',
					type : 'string'
				}])
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
			
			
			var store_Combox = new Ext.form.ComboBox({
				mode : 'remote',
				store : metastore,
				resizable : true,
				name : 'rdtyName',
				emptyText : '请先选择特色库...',
				triggerAction : 'all',
				valueField : 'rdtyId',
				selectOnFocus : true,
				displayField : 'rdtyName',
				editable : false,
				width : 120
			});
			
			var sourcetype = new Ext.form.ComboBox({
			    fieldLabel: "资源类型",
			    mode: 'remote',
			    store: sourcetype_store,
			    name: '_index_RMIN_RLTY_ID',
			    hiddenName: 'index_RMIN_RLTY_ID',
			    emptyText: '再选择资源类型...',
			    width: 100,
			    triggerAction: 'all',
			    valueField: 'value',
			    displayField: 'text',
			    width : 120
			});
			
			//当特色发生改变时，级联改变特色库所联系的资源类型
			store_Combox.on('select', function(comboBox){
			   var id = comboBox.getValue();
			   sourcetype.clearValue(); //清空之前的资源类型
			   var store_url = url_sourcetype_store + "&store=" + id;
			   sourcetype_store.proxy = new Ext.data.HttpProxy({url: store_url});
			   sourcetype_store.load();
			})
					
			//标题检索关键字
			var rsdaTitle = new Ext.form.TextField({
				id : 'rsdaTitle',
				name : 'rsdaTitle',
				width: 110,
				listeners :{
			        specialKey :function(field,e){
			            if (e.getKey() == Ext.EventObject.ENTER){
			               searchData();
			            } 
			        }
			    } 
			}); 
			
			//var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var sm = new Ext.grid.CheckboxSelectionModel({});
			var cm = new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), 
				sm,
				{header:'题名', dataIndex:'RSDA_TITLE', width:210,sortable: true},
				{header:'资源类型', dataIndex:'RLTY_NAME', width:60,sortable: true},
				{header:'特色库', dataIndex:'RMST_NAME', width:90,sortable: true},
				{header:'创建时间', dataIndex:'RSDA_BUILDDATE', width:90,sortable: true}, 
				{header:'浏览次数', dataIndex:'RSDA_LOOKNUM', width:60,sortable: true}
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'RMST_NAME'}, 
				    {name:'RLTY_NAME'},
				    {name:'RSDA_TITLE'}, 
					{name:'RSDA_BUILDDATE'}, 
					{name:'RSDA_CMTSTUS'},
					{name:'RSDA_GRADE'},
					{name:'RSDA_RCMD'},
					{name:'RSDA_ISUSE'},
					{name:'RSDA_LOOKNUM'},
					{name: 'RSDA_ID'}, 
				]) ,
				sortInfo : {
					field : 'RSDA_BUILDDATE',
					direction : 'DESC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
				
			}); 
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
			grid_store.on('beforeload',function(){ 
				Ext.apply(  
				this.baseParams, 
				{ 
					RSDA_RLTY_ID: sourcetype.getValue(),
					RSDA_RMST_ID: store_Combox.getValue(),
					rsdaTitle: rsdaTitle.getValue(),
					beginTime: document.getElementById('beginD').value,
					endTime: document.getElementById('endD').value
				}); 
			}); 
			
			function searchData(){
				var beginD = document.getElementById('beginD').value;
				var endD = document.getElementById('endD').value;
				if(endD !=null && endD !="" && beginD > endD){
					alertWarring("开始时间不能大于结束时间!");
					return ;
				}
				grid_store.load({params:{
		            start:0,
		            limit:pageNum,
		            RSDA_RLTY_ID: sourcetype.getValue(),
					RSDA_RMST_ID: store_Combox.getValue(),
					rsdaTitle: rsdaTitle.getValue(),
					beginTime: beginD,
					endTime: endD
		        }}); 
			}
			
			function exportCalis(){ 
			    var idlist = getSelectId();
			    if(idlist.length < 1){
			        Ext.Msg.alert("消息","请选择需要导出的资源!");
			        return;
			    }
			    		msgBox = Ext.MessageBox.show({  
					                title:'进度提示',  
					                msg:'',  
					                modal:true,  
					                progress:true,  
					                width:400  
				             	}); 
			    	Ext.Ajax.request({
							url:'<%=path %>/console/data/resource/ExportResToCalis.htm?action=progressBarReturnNum',
							method: 'POST',
		           			success: function (result, request ){
		           			progressBarReturnNum=result.responseText;
		           			Ext.Ajax.request({
								url:'<%=path %>/console/data/resource/ExportResToCalis.htm?action=exportCalis&idlist='+idlist+'&progressBarReturnNum='+progressBarReturnNum,
								method: 'POST',
			           			success: function (result, request ){ 
			           			}
							});
								progressBarOnload();
		           			}
						});
			}
			
			function progressBarOnload(){
				if(progressBarReturnNum == 0){
		           			     	return;
				}
				Ext.Ajax.request({
							url:'<%=path %>/console/data/resource/ExportResToCalis.htm?action=progressBar&progressBarNum='+progressBarReturnNum,
							method: 'POST',
		           			success: function (result, request ){
			           			if(result.responseText == "1"){
										msgBox.hide();
				           			    Ext.Msg.alert("消息","导出成功！");
				           			    grid_store.reload();
				           			    progressBarReturnNum = 0;
										return;
			           			    }else if(result.responseText == "0"){
										msgBox.hide();
			           			   		Ext.Msg.alert("错误","导出失败！");
			           			   		progressBarReturnNum = 0;
										return;
			           			    }
		           					processText = result.responseText;
		           			        var thisnum = new Array();
		           			        thisnum = processText.split("/");
		           			    	percentage = parseInt(thisnum[0])/parseInt(thisnum[1]);
                    				msgBox.updateProgress(percentage,processText,'<font color="red">正在导出数据，请耐心等待...</font>');  
		           			}
						});
								    setTimeout("progressBarOnload()",1000);
			}
			
			function exportCalisAll(){ 
			    var beginD = document.getElementById('beginD').value;
				var endD = document.getElementById('endD').value;
				if(endD !=null && endD !="" && beginD > endD){
					alertWarring("开始时间不能大于结束时间!");
					return ;
				} 
						msgBox = Ext.MessageBox.show({  
					                title:'进度提示',  
					                msg:'',  
					                modal:true,  
					                progress:true,  
					                width:400  
				             	}); 
			    	Ext.Ajax.request({
							url:'<%=path %>/console/data/resource/ExportResToCalis.htm?action=progressBarReturnNum',
							method: 'POST',
		           			success: function (result, request ){
		           				progressBarReturnNum=result.responseText;
		           				 Ext.Ajax.request({
									url:'<%=path %>/console/data/resource/ExportResToCalis.htm?action=exportCalis&isAll=true&progressBarReturnNum='+progressBarReturnNum,
									method: 'POST',
									params:{
								            RSDA_RLTY_ID: sourcetype.getValue(),
											RSDA_RMST_ID: store_Combox.getValue(),
											rsdaTitle: rsdaTitle.getValue(),
											beginTime: beginD,
											endTime: endD
											},
				           			success: function (result, request ){
				           			}
								});
								progressBarOnload();
		           			}
						});
						
					   
			}
			/**********************设置窗口***************************/
			
			var library = new Ext.form.TextField({
				id : 'library',
				name : 'library',
				fieldLabel : '图书馆代号',
				width: 180
			});
			
			var editWin ;
			var edit_Form = new Ext.form.FormPanel({
				id : 'edit_Form',
				border : false,
				frame : false,
				waitMsgTarget : true,
				labelAlign : 'right',
				bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
				labelWidth : 80,
				buttonAlign : 'center',
				defaulttype : 'textfield',
				items : [library],
				buttons : [{
					text : '保&nbsp;存',
					handler : saveWin
				}, {
					text : '取&nbsp;消',
					handler : function() {
						editWin.hide();
					}
				}]
			});
			
			function initLibrary(){
				Ext.Ajax.request({
							url:'<%=path %>/console/data/resource/ExportResToCalis.htm?action=initLibrary',
							method: 'POST',
		           			success: function (result, request ){
			           			   library.setValue(result.responseText);
		           			}
						});
			}
			
			function editWinShow() {
				initLibrary();
				if(!editWin) {
					editWin = new Ext.Window({
						closeAction : 'hide',
						resizable : false,
						width : 300,
						height : 125,
						title : 'Calis导出设置',
						layout : 'fit',
						modal : true,
						buttonAlign : 'center',
						items : [edit_Form],
						listeners : {
							hide : function() {
								edit_Form.form.reset()
							}
						}
					});
				}
				editWin.show();
			} 
			
			function saveWin(){
				Ext.Ajax.request({
							url:'<%=path %>/console/data/resource/ExportResToCalis.htm?action=saveLibrary&library='+library.getValue(),
							method: 'POST',
		           			text: "saving...",    
		           			success: function (result, request ){
			           			    Ext.Msg.alert("消息",result.responseText);
			           			    editWin.hide();
		           			}
						});
			}
			
			/**********************设置窗口END***************************/
			 
			
			var tbar1 = new Ext.Toolbar([
				'特色库：',store_Combox,' ',
				sourcetype,'-',
				'标题：',rsdaTitle,'-','时间段:',
				{	
       	        	xtype:'datefield',
       	            name:'beginD',
       	            id:'beginD',
       	            anchor:'97%',
       	            altFormats: 'Y-m-d',
              			format: 'Y-m-d',
      					readOnly : false
       	         },
       	         //分隔符
       	         {
        			xtype:'label',
		   			text:'--',
		   			width:10,
		   			name:'divide',
		   			id:'divideD',
		   			allowBlank: false
       			},
       	         //时间段结束时间
       	         {	
       	        	xtype:'datefield',
       	            name:'endD',
       	            id:'endD',
       	            anchor:'97%',
       	            altFormats: 'Y-m-d',
              			format: 'Y-m-d',
      					readOnly : false
       	         },
				{
					text: '&nbsp;查&nbsp;询&nbsp;', 
					enableToggle: false, 
					iconCls:'common_search', 
					handler: searchData 
				}
			]);
			
			var tbar2 = new Ext.Toolbar([
				{ 
					text: '导出Calis标准数据', 
					enableToggle: false, 
					iconCls:'common_go', 
					handler: exportCalis 
				},'-',{ 
					text: '导出所有', 
					enableToggle: false, 
					iconCls:'common_go', 
					handler: exportCalisAll 
				},'-',{
					text: '设置', 
					enableToggle: false, 
					iconCls:'common_edit',
					handler: editWinShow
				}
			]);
			
			var grid = new Ext.grid.GridPanel({ 
				height: document.documentElement.clientHeight, 
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				loadMask: true,     //加载时提示load... 
				frame:false,
				border:false,
				enableColumnHide : false,  //屏蔽勾选列
				viewConfig: { 
					forceFit: true 
				}, 
				cm: cm, 
				sm: sm, 
				autoScroll : true,
				region : 'center',
				tbar: tbar1, 
				listeners : {
					'render' : function() {
						tbar2.render(this.tbar); 
					}
				},
				bbar: new Ext.PagingToolbar({  //分页信息栏 
					pageSize: pageNum, 
					store: grid_store, 
					displayInfo: true, 
					displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>', 
					emptyMsg: '没有记录', 
					plugins: new Ext.ux.ProgressBarPager()					
				})
				 
			}); 
			  
			
			//获取所有选中的数据ID
			function getSelectId(){
			    var rows = grid.getSelectionModel().getSelections();
			    if(rows.length<1){
			        return "";
			    }
			    var ids = 0;
                for(var i=0; i<rows.length; i++){
                    ids +="," + rows[i].get("RSDA_ID");
                }
                return ids;
			}
			
			//获取选中的第一条数据ID
			function getFirstDataId(){
			    var rows = grid.getSelectionModel().getSelections();
			    if(rows.length<1){
			        return "";
			    }
			    var ids = rows[0].get("RSDA_ID");
                return ids;
			}
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				Ext.QuickTips.init(); 
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