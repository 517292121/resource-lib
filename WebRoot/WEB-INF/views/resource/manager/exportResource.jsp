<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.CommonUtil"%>
<%
	String path = request.getContextPath();
	String attDir = null;
%>
<%@ include file="../../common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<script type="text/javascript">var root = "<%=path %>/";</script>
	<script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
	
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/resource/exportResource.htm?action=getResourceListJson'; 
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
			
			var cm = new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(), 
				{header:'题名', dataIndex:'rsdaTitle', width:210,sortable: true},
				{header:'特色库', dataIndex:'rmstName', width:90,sortable: true},
				{header:'资源类型', dataIndex:'rltyName', width:60,sortable: true},
				{header:'创建时间', dataIndex:'rsdaBuilddate', width:90,sortable: true}
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'rsdaId'}, 
				    {name:'rsdaTitle'},
				    {name:'rmstName'}, 
					{name:'rltyName'}, 
					{name:'rsdaBuilddate'}
				]) ,
				sortInfo : {
					field : 'rsdaBuilddate',
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
					rltyId: sourcetype.getValue(),
					rmstId: store_Combox.getValue(),
					rsdaTitle: rsdaTitle.getValue(),
					beginDate: document.getElementById('beginD').value,
					endDate: document.getElementById('endD').value
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
		            rltyId: sourcetype.getValue(),
					rmstId: store_Combox.getValue(),
					rsdaTitle: rsdaTitle.getValue(),
					beginDate: beginD,
					endDate: endD
		        }}); 
			}
			
				
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
			
			//导出方法
			function exportFun(){ 
			    var beginD = document.getElementById('beginD').value;
				var endD = document.getElementById('endD').value;
				if(endD !=null && endD !="" && beginD > endD){
					alertWarring("开始时间不能大于结束时间!");
					return ;
				}
				
				var rmstId = store_Combox.getValue();
				var rltyId = sourcetype.getValue();
				if(!rmstId || rmstId == null || rmstId.length < 1){
					alert("请选择特色库！");
					return;
				}
				if(!rltyId || rltyId == null || rltyId.length < 1){
					alert("请选择资源类型！");
					return;
				}				
				var url = "<%=path %>/console/data/resource/exportResource.htm?action=exp";
				url += "&rmstId=" + rmstId;
				url += "&rltyId=" + rltyId;
				url += "&rsdaTitle=" + rsdaTitle.getValue();
				url += "&beginDate=" + beginD;
				url += "&endDate=" + endD;
				
				Msg.wait('数据组合中，请等待...');
				
				bindclose();
				window.location.href = url;
			}
			
			function bindclose(){
				var displayDiv=document.getElementById("displayDivId");
				if(window.addEventListener) { 
				    displayDiv.addEventListener("click", function(){closeTip();}, false); 
				} else if(window.attachEvent){  
				    displayDiv.attachEvent("onclick", function(){closeTip();});  
				} else { 
				    displayDiv.onclick= function(){closeTip();}; 
				}     
			}
			function closeTip(){
				Msg.removeAlertItem();
			 	Msg.closeDisplay();
			}
			
			var tbar2 = new Ext.Toolbar([
				{ 
					text: '导出EXCEL', 
					enableToggle: false, 
					iconCls:'common_go', 
					handler: exportFun 
				},'-','<c:if test="<%=CommonUtil.isNotNull(attDir) %>"> 注：附件将备份到“<%=attDir%>”，导出前请先将该目录清空！</c:if> '
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
			  
			
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				Ext.QuickTips.init(); 
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