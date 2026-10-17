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
	
	<link rel="stylesheet" href="<%=path%>/css/MessageUtil.css" type="text/css"></link>
    <script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path%>/js/MessageUtil.js"></script>
	
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/resource/SourceManager.htm?action=resourceListJson'; 
			var url_save_form = ''; 
			var url_dele_form = ''; 
			var url_delete_icon = ''; 
			var url_sourcetype_store='<%=path %>/console/data/templetManager.htm?action=getSourceTypeByStore';
			var pagecount = ${pagecount};
			//特色库store
			var metastore = new Ext.data.Store({
				autoLoad : true,
				proxy : new Ext.data.HttpProxy({
					url : '<%=path %>/console/data/extdata.htm?action=AllStoreJson&hasNull=hasNull'
				}),
				reader : new Ext.data.ArrayReader({}, [{
					name : 'value',
					type : 'string'
				}, {
					name : 'text',
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
				valueField : 'value',
				selectOnFocus : true,
				displayField : 'text',
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
			metastore.on('load', function(comboBox){
			   if('${searchRmstId }' != '' && '${searchRmstId }' != '0'){
					store_Combox.setValue('${searchRmstId }');
					sourcetype.clearValue(); //清空之前的资源类型
				    var store_url = url_sourcetype_store + "&store=${searchRmstId}";
				    sourcetype_store.proxy = new Ext.data.HttpProxy({url: store_url});
				    sourcetype_store.load();
				}
			})
			sourcetype_store.on('load', function(comboBox){
				if('${searchRltyId }'.length != '' && '${searchRltyId }' != '0'){
					sourcetype.setValue('${searchRltyId }');
				}
			})
			
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
				value : '${searchtitle}',
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
			//var sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn});
			var sm = new Ext.grid.CheckboxSelectionModel(); // 创建多选模式
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				sm,
				{header:'题名', dataIndex:'RSDA_TITLE', width:210,sortable: true},
				{header:'资源类型', dataIndex:'RLTY_NAME', width:60,sortable: true},
				{header:'特色库', dataIndex:'RMST_NAME', width:90,sortable: true},
				{header:'创建时间', dataIndex:'RSDA_BUILDDATE', width:90,sortable: true}, 
				{header:'评论', dataIndex:'RSDA_CMTSTUS', width:40,sortable: true, align:'center', renderer: function(v){
				    if(v == 1){
				        return "<img src='<%=path %>/images/icon/yes.png'>";
				    }else if(v == 0){
				    	return "<img src='<%=path %>/images/icon/no.png'>";
				    }
				}},
				{header:'推荐', dataIndex:'RSDA_RCMD', width:40,sortable: true, align:'center', renderer: function(v){
				    if(v == 1){
				        return "<img src='<%=path %>/images/icon/yes.png'>";
				    }else if(v == 0){
				    	return "<img src='<%=path %>/images/icon/no.png'>";
				    }
				}},
				{header:'显示', dataIndex:'RSDA_ISUSE', width:40,sortable: true, align:'center', renderer: function(v){
				    if(v == 1){
				        return "<img src='<%=path %>/images/icon/yes.png'>";
				    }else if(v == 0){
				    	return "<img src='<%=path %>/images/icon/no.png'>";
				    }
				}},
				{header:'评分', dataIndex:'RSDA_GRADE', width:40,sortable: true, align:'center'},
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
			
			grid_store.load({params:{
					start:pagecount,
			        limit:pageNum,
			        rsdaTitle:rsdaTitle.getValue(),
			        RSDA_RMST_ID: '${searchRmstId}',
					RSDA_RLTY_ID: '${searchRltyId}'
			}}); 
			grid_store.on('beforeload',function(){ 
				Ext.apply(  
				this.baseParams, 
				{ 
					RSDA_RLTY_ID: sourcetype.getValue(),
					RSDA_RMST_ID: store_Combox.getValue(),
					rsdaTitle: rsdaTitle.getValue()
				}); 
			}); 
			
			function searchData(){
				grid_store.load({params:{
		            start:0,
		            limit:pageNum,
		            RSDA_RLTY_ID: sourcetype.getValue(),
					RSDA_RMST_ID: store_Combox.getValue(),
					rsdaTitle: rsdaTitle.getValue()
		        }}); 
			}
			
			var tbar2 = new Ext.Toolbar([
				{ 
					text: '&nbsp;编&nbsp;辑&nbsp;', 
					enableToggle: false, 
					iconCls:'common_edit', 
					handler: function(){ 
					    dataEdit();
					} 
				}
				 , '-',
				 { 
					text: '&nbsp;附&nbsp;件&nbsp;', 
					enableToggle: false, 
					iconCls:'common_edit', 
					handler: function(){ 
					    accessFile();
					} 
				}
				 , '-',
				{ 
					text: '删除', 
					enableToggle: false, 
					iconCls:'common_delete', 
					handler: function(){ 
					    delresource();
					}  
				}  , '-',
				{ 
					text: '其它语', 
					enableToggle: false, 
					iconCls:'common_list', 
					handler: function(){ 
					    sourceOtherLanguage();
					} 
				},'-',
				{ 
					text: '评论开关', 
					enableToggle: false, 
					iconCls:'common_shutdown', 
					handler: function(){ 
					    setComment();
					} 
				} , '-',
				{ 
					text: '推荐开关', 
					enableToggle: false, 
					iconCls:'common_shutdown', 
					handler: function(){ 
					    setTuijian();
					} 
				} , '-',
				{ 
					text: '显示开关', 
					enableToggle: false, 
					iconCls:'common_shutdown', 
					handler: function(){ 
					    setUse();
					} 
				} , '-',
				{ 
					text: '导出Excel', 
					enableToggle: false, 
					iconCls:'excel', 
					handler: expExcel 
				}, '-',
				{ 
					text: '封面处理', 
					enableToggle: false, 
					iconCls:'cancel', 
					handler: coverCompress 
				}
			]);
			
			var tbar1 = new Ext.Toolbar([
				'特色库：',store_Combox,' ',
				sourcetype,'-',
				'标题：',rsdaTitle,
				{ 
					text: '&nbsp;查&nbsp;询&nbsp;', 
					enableToggle: false, 
					iconCls:'common_search', 
					handler: searchData 
				}
				/**
				 , '-',
				{ 
					text: '更新首页缓存', 
					enableToggle: false, 
					iconCls:'update_cache', 
					handler: RebuilderIndex 
				} , '-',
				{ 
					text: '更新分类统计', 
					enableToggle: false, 
					iconCls:'update_cache', 
					handler: updateGroupCount 
				} ,'-',
				{ 
					text: '更新全文索引', 
					enableToggle: false, 
					iconCls:'update_cache', 
					handler: updateFullTextSearch
				} ,'-'
				**/
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
				bbar: new Ext.PagingToolbar({  //分页信息栏 
					pageSize: pageNum, 
					store: grid_store, 
					displayInfo: true, 
					displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>', 
					emptyMsg: '没有记录', 
					plugins: new Ext.ux.ProgressBarPager()					
				})
				,
				listeners : {
					'render' : function() {
						tbar2.render(this.tbar); 
					}
				} 
			}); 
			
			grid.on('dblclick', dataEdit); //双击进入模板设置
			
			function dataEdit(){
				document.getElementById("searchtitle").value=rsdaTitle.getValue();
				document.getElementById("pagecount").value=grid.getBottomToolbar().getPageData().activePage;
				document.getElementById("sid").value=getFirstDataId();
				document.getElementById("rmstId").value = store_Combox.getValue();
				document.getElementById("rltyId").value = sourcetype.getValue();
				
				var id = getFirstDataId();
				if(id.length > 0){
				   document.getElementById("form1").submit();            
				}else{
					Ext.Msg.alert("提示","请选择编辑的资源!");
				}
			}
			
			function accessFile(){
				var id = getFirstDataId();
				if(id.length > 0){
					/**
					document.getElementById("key").value=rsdaTitle.getValue();
					document.getElementById("begin").value=grid.getBottomToolbar().getPageData().activePage;
					document.getElementById("resId").value=id;
					document.getElementById("rmst_id").value = store_Combox.getValue();
					document.getElementById("rlty_id").value = sourcetype.getValue();
				    document.getElementById("form2").submit();
				    **/
				    
				    var urlParam = "key=" + rsdaTitle.getValue();
				    urlParam += "&begin=" + grid.getBottomToolbar().getPageData().activePage;
				    urlParam += "&resId=" + id;
				    urlParam += "&rmst_id=" + store_Combox.getValue();
				    urlParam += "&rlty_id=" + sourcetype.getValue();
				    urlParam += "&pageSource=managerList";
				    window.location.href = "<%=path %>/console/data/inputSingle.htm?action=initResSourceFile&" + urlParam;            
				}else{
					Ext.Msg.alert("提示","请选择资源!");
				}
			}
			
			//进入其它语
			function sourceOtherLanguage(){
				var id = getFirstDataId();
				if(id.length > 0){
				    window.location.href = "<%=path %>/console/data/resource/SourceManager.htm?action=resourceLanguageList&sourceId=" + id;
				}else{
					Ext.Msg.alert("提示","请点击选择资源!");
				} 
			}
			
			function setComment(){
			    var idlist = getSelectId();
			    if(idlist.length < 1){
			        Msg.fail("请选择需要设置的资源!", 3);
			        return;
			    }
			    Ext.Ajax.request({ 
					url:'<%=path %>/console/data/resource/SourceManager.htm?action=changCommentStatu&idlist='+idlist,
					method: 'GET',   
           			text: "Updating...",     
           			success: function (result, request ){
           			    if(result.responseText == "1"){
	           			    Msg.success("设置成功！");
	           			    grid_store.reload();
           			    }else{
           			   		Ext.Msg.alert("消息","设置失败！");
           			    }
           			}
				}); 
			}
			
			function setUse(){
			    var idlist = getSelectId();
			    if(idlist.length < 1){
			        if(statu == 1){
			        	Msg.fail("请选择需要启用的资源!", 3);
			        }else{
			            Msg.fail("请选择需要禁用的资源!", 3);
			        }
			        return;
			    }
			    Ext.Ajax.request({ 
					url:'<%=path %>/console/data/resource/SourceManager.htm?action=changUseStatu&idlist='+idlist,
					method: 'GET',   
           			text: "Updating...",     
           			success: function (result, request ){
           			    if(result.responseText == "1"){
	           			    Msg.success("设置成功！");
	           			    grid_store.reload();
           			    }else{
           			   		Ext.Msg.alert("消息","设置失败！");
           			    }
           			}
				}); 
			}
			
			function setTuijian(){
			    var idlist = getSelectId();
			    Ext.Ajax.request({ 
					url:'<%=path %>/console/data/resource/SourceManager.htm?action=changRcmdStatu&idlist='+idlist,
					method: 'GET',   
           			text: "Updating...",     
           			success: function (result, request ){
           			    if(result.responseText == "1"){
	           			    Msg.success("设置成功！");
	           			    grid_store.reload();
           			    }else{
           			   		Ext.Msg.alert("消息","设置失败！");
           			    }
           			}
				}); 
			}
			
			function delresource(){
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
				});
			    var idlist = getSelectId();
			    if(idlist.length < 1){
			        Msg.fail("请选择需要删除的资源!", 3);
			        return;
			    }
			    Ext.Msg.confirm('提示','是否删除该记录？\n',function(button,text){    
	                if(button == "yes"){
					    Ext.Ajax.request({ 
							url:'<%=path %>/console/data/resource/SourceManager.htm?action=deleteResource&idlist='+idlist,
							method: 'GET',   
		           			text: "Updating...",     
		           			success: function (result, request ){
		           			    if(result.responseText == "1"){
		           			    	myMask.hide();
			           			    Msg.success("删除成功！");
			           			    grid_store.reload();
		           			    }else{
		           			    	myMask.hide();
		           			   		Ext.Msg.alert("错误","删除失败！");
		           			    }
		           			}
						});
					}
     			}); 
			}
			
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
			
			function encodeData(data){
				if(data == null || data.length < 1){
					return "";
				}
				return encodeURI(data);
			}
			
			function expExcel(){
				var rltyId = sourcetype.getValue();
				var rmstId = store_Combox.getValue();
				var title = rsdaTitle.getValue();
				var ids = getSelectId();
				title = encodeData(title);
				
				var sort = grid_store.getSortState().field;
				var dir = grid_store.getSortState().direction;
		
				var url = "<%=path %>/console/data/resource/SourceManager.htm?action=exportResource";
				url += "&rltyId=" + rltyId;
				url += "&rmstId=" + rmstId;
				url += "&rsdaTitle=" + title;
				url += "&ids=" + ids;
				url += "&sort=" + sort;
				url += "&dir=" + dir;
				
				window.location.href = url;				
			}
			
			// cover.jpg处理
			function coverCompress(){
				var sourcetype_store1 = new Ext.data.Store({
				    proxy: new Ext.data.HttpProxy({url: url_sourcetype_store}),
				    reader: new Ext.data.ArrayReader({},[
				        {name: 'value'},
				        {name: 'text'}
				    ]),
				    autoLoad : false
				});
				var missionInfo_store = new Ext.data.Store({
				    proxy: new Ext.data.HttpProxy({url: "<%=path %>/console/source/cover/compress.htm?action=getResMissionInfoJson"}),
				    reader: new Ext.data.ArrayReader({},[
				        {name: 'value'},
				        {name: 'text'}
				    ]),
				    autoLoad : false
				});
				var addPersonForm = new Ext.FormPanel({
						border : 0,
						frame : true ,
						labelAlign: 'right',
						buttonAlign : 'center' ,
						lablewidth : 70,
						lableheight: 30,
						layout : 'column' ,
		              	xtype: 'layout-column',
						height: 120,
						itemClas: 'required',
						style: 'margin-top: 10px;',
				        items: [{
				        	columnWidth:1,  //该列占用的宽度，标识为50％
				            layout: 'form',
				            border:false,
				            items: [{                     //这里可以为多个Item，表现出来是该列的多行
				            	id:	'_rmstId',
					        	xtype : 'combo',
					            fieldLabel:	'特色库',
					            store: metastore,
					            valueField:'value',
					        	displayField: "text",
					        	columnWidth : 1,
					        	mode:'local',
					        	triggerAction:'all',
					        	emptyText: "--请选择特色库--",
					        	listeners: {
					    			select : function(combo, record, index){
					    				Ext.getCmp('_rltyId').clearValue();
					    				sourcetype_store1.load({
				    	                    url: "findStation.action",
				    	                    params: {'store': combo.value}
			    	                	});
					    				Ext.getCmp('_rminId').clearValue();
					    				missionInfo_store.load({
				    	                    url: "findStation.action",
				    	                    params: {'RSDA_RMST_ID': combo.value}
			    	                	});
					                }
					        	}
				            }]
				        },{
				        	columnWidth:1,  //该列占用的宽度，标识为50％
				            layout: 'form',
				            border:false,
				            items: [{
					            id:	'_rltyId',
					        	xtype : 'combo',
					            fieldLabel:	'资源类型',
					            store: sourcetype_store1,
					            valueField:'value',
					        	displayField: "text",
					        	columnWidth : 1,
					        	mode:'local',
					        	triggerAction:'all',
					        	emptyText: "--请选择资源类型--",
					        	listeners: {
					    			select : function(combo, record, index){
					    				Ext.getCmp('_rminId').clearValue();
					    				missionInfo_store.load({
				    	                    url: "findStation.action",
				    	                    params: {'RSDA_RMST_ID': combo.value}
			    	                	});
					    				missionInfo_store.load({
				    	                    url: "findStation.action",
				    	                    params: {
				    	                    	'RSDA_RLTY_ID':	combo.value,
				    	                    	'RSDA_RMST_ID': Ext.getCmp('_rmstId').getValue()
				    	                    }
			    	                	});
					    			}
					        	}
					        }]
				        },{
				        	columnWidth:1,  //该列占用的宽度，标识为50％
				            layout: 'form',
				            border:false,
				            items: [{
					        	xtype : 'combo',
					        	id:	'_rminId',
					        	fieldLabel: "所属作业",
					        	store: missionInfo_store,
				                valueField:'value',
					        	displayField: "text",
					        	columnWidth : 1,
					        	mode:'local',
					        	triggerAction:'all',
					        	emptyText: "--请选择所属作业--",
					        	listeners: {
					    			select : function(combo, record, index){
					                }
					        	}
					        }]
				        },{
				        	columnWidth:0.3,
				            layout: 'form',
				            border:false,
				            items: [new Ext.form.NumberField({  //定义个数字输入框
							   	 id: 'minWidth',
							   	 fieldLabel: '图片宽度',
							   	 allowNegative: false,    //不能为负数
							   	 decimalPrecision: 0,      //小数精确位数
							   	 minValue: 180,
							   	 minText: '最小值为180',
							   	 value: 180
						   })]
				        },{
				        	columnWidth:0.3,
				            layout: 'form',
				            border:false,
				            items: [new Ext.form.NumberField({
							   	 id: 'minHeight',
							   	 fieldLabel: '图片高度',
							   	 allowNegative: false,    //不能为负数
							   	 decimalPrecision: 0,      //小数精确位数
							   	 minValue: 250,
							   	 minText: '最小值为250',
							   	 value: 250
						   })]
				        }]
				});
				var win = new Ext.Window({
					width : 550,
					height : 350,
					title : '封面处理',
					modal : true,
					resizable : false,
					border : false,
					draggable : true,
					closable : true,
					html : "<fieldset style='margin-top: 30px;border: none;border-color: #e6e6e6;'>"
							+"<legend style='margin-left: 5px;padding: 0 10px;font-size: 14px;color:red;'>注意事项</legend>"
							+"<div style='margin-left: 45px;'>"
								+"<p style='line-height: 24px;color: red;font-size: 12px;'>1. 仅处理图片格式的资源封面。</p>"
								+"<p style='line-height: 24px;color: red;font-size: 12px;'>2. 处理前请将cover.jpg存放到指定的资源目录</p>"
								+"<p style='line-height: 24px;color: red;font-size: 12px;'>3. 由于图片处理时间较长，请勿手动关闭当前界面，避免进度条丢失与重复执行。</p>"
							+"</div>"
							+"</fieldset>",
					items: addPersonForm,
					buttons: [{
                        text: "开始压缩", handler: function () {
							var rmstId = Ext.getCmp('_rmstId').getValue();
							var rltyId = Ext.getCmp('_rltyId').getValue();
							var rminId = Ext.getCmp('_rminId').getValue();
							var minWidth = Ext.getCmp("minWidth").getValue();
							var minHeight = Ext.getCmp("minHeight").getValue();
							if(minWidth < 180 || minHeight < 250){
								return false;
							}
                        	Ext.MessageBox.confirm("标题","此操作不可恢复，您确认按此配置执行吗？",function(btn) {
                        		if(btn=="yes"){
	                        		Ext.onReady(function(){
	    								Ext.Ajax.request({
	    								    url: '<%=path %>/console/source/cover/compress.htm?action=compressStart',
	    								    method : 'POST',
	    								    params: {
	    								    	'rmstId': rmstId,
	    								    	'rltyId': rltyId,
	    								    	'rminId': rminId,
	    								    	'minWidth': minWidth,
	    								    	'minHeight': minHeight
	    								    }
	    								});
	    							});
	                        		
	                        		// 进度条
									var msgbox=Ext.Msg.show({
							            title:"处理进度",
							            msg:"封面处理中......",
							            closable:true,
							            width:300,
							            modal:true,
							            progress:true
							        });
									setTimeout(function () {
										var coverTaskmgr = Ext.TaskMgr.start({
								            run:function () {
								                Ext.Ajax.request({
												    url: '<%=path %>/console/source/cover/compress.htm?action=getSchedule',
												    dataType: 'JSON',
												   	success: function(response){
												   		var data = Ext.decode(response.responseText);
												   		var count = data.count;
												   		var index = data.index;
												   		if (count <= index) {
										                    msgbox.hide();
															Ext.TaskMgr.stop(coverTaskmgr);
															Ext.MessageBox.alert("提示","封面压缩完成!",function(r) {
																win.hide();
																addPersonForm.removeAll();
																win.removeAll();
															});
										                    return false;
										                }
												   		var schedule = index/count;
												   		var message = (schedule*100).toFixed(2);
												   		msgbox.updateProgress(schedule, message+"%", "由于图片处理速度较慢，请耐心等待");
												    }
												});
								            },
								            interval:1000
								        })
									}, 1000);
                        		}
                       		});
                        }
                    }],
					buttonAlign: "center"
				}).show();
			}
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				Ext.QuickTips.init(); 
				Ext.Ajax.timeout = 30*60*1000;		//重设置超时
				//grid.render('grid'); 
								
				// 创建主面板
				var displayPanel = new Ext.Viewport({
					layout : 'border',
					items : [grid]
				}); 
			}) 

			///**********************2011-04-22添加***************************//
			//重建索引
			function RebuilderIndex(){
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '缓存更新中....',
						removeMask : true // 完成后移除
				});
				myMask.show();
			    Ext.Ajax.request({ 
					url:'<%=path %>/servlet/updateAllCache',
					method: 'GET',   
           			text: "Updating...",     
           			success: function (result, request ){
           				myMask.hide();
	           		    Ext.Msg.alert("更新完成！",result.responseText);
           			}
				});
			}
			
			function clearRepeatTemp(){
				Ext.Msg.confirm('提示','清空查重记录前请确认当前没有用户正在浏览查重结果，是否立即清空？\n',function(button,text){    
	                if(button == "yes"){
					    Ext.Ajax.request({ 
							url:'<%=path %>/console/data/resource/checkRepeat.htm?action=clearRepeat',
							method: 'GET',   
		           			text: "Updating...",     
		           			success: function (result, request ){
		           			    if(result.responseText == "1"){
			           			    Ext.Msg.alert("消息","查重临时数据已清空！");
		           			    }else{
		           			   		Ext.Msg.alert("错误","<span style='color:red'>"+result.responseText+"</span>");
		           			    }
		           			}
						});
					}
     			}); 
			}
			
			//更新分类统计
			function updateGroupCount(){
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '统计更新中....',
						removeMask : true // 完成后移除
				});
				myMask.show();
			    Ext.Ajax.request({ 
					url:'<%=path %>/servlet/updateGroupCount',
					method: 'GET',   
           			text: "Updating...",     
           			success: function (result, request ){
           				myMask.hide();
	           		    Ext.Msg.alert("更新完成！",result.responseText);
           			}
				});
			}
			
			//更新全文索引
			function updateFullTextSearch(){
				Ext.Msg.confirm('提示','本操作会将平台所有资源全部重建索引，您确定要执行该操作吗？\n',function(button,text){    
					var myMask = new Ext.LoadMask(Ext.getBody(), {
							msg : '索引更新中....',
							removeMask : true // 完成后移除
					});
					myMask.show();
				    Ext.Ajax.request({ 
						url:'<%=path %>/servlet/createLuceneIndex',
						method: 'GET',   
	           			text: "Updating...",     
	           			success: function (result, request ){
	           				myMask.hide();
		           		    Ext.Msg.alert("更新完成！",result.responseText);
	           			}
					});
     			}); 
			}
			
		</script> 
	</head> 
	<body> 
		<form action="<%=path %>/console/data/inputSingle.htm?action=doResourceEdit" method="post" id="form1" name="form1">
			<input type="hidden" id="searchtitle" name="searchtitle" />
		    <input type="hidden" id="reqType"  name="reqType" value="2"/>
		    <input type="hidden" id="pagecount" name="pagecount"/>
		    <input type="hidden" id="rmstId" name="rmstId"/>
		    <input type="hidden" id="rltyId" name="rltyId"/>
		    <input type="hidden" id="sid" name="sid"/>
		</form>
		
		<div id="grid"></div> 
	</body> 
</html>