<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<style type="text/css">
		.progressLine{
			width: 200px;
			height: 16px;
			border-left: 1px solid #CACFDA;
			border-right: 1px solid #CACFDA;
			padding: 0px;
			background-image: url("<%=path %>/images/console/progress_bg.gif");
		}
		.completedLine{
			background-image: url("<%=path %>/images/console/progress_ok.gif");
			width: 0px;
			height: 16px;
		}
		.messageInfo{
			font-size: 12px;
			color: #273E56;
			height: 12px;
		}
	</style>
	<script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
    
    <script type="text/javascript">
		//每页显示条数
		var limitLength = <%=ConstantsObj.DEFAULT_PAGE_SIZE %>;
		var url_sourcetype_store='<%=path %>/console/data/text/textResourceManager.htm?action=AllType&hasNull=hasNull';
		
		//资源库store
		var metastore = new Ext.data.Store({
			autoLoad : true,
			proxy : new Ext.data.HttpProxy({
				url : '<%=path %>/console/data/extdata.htm?action=AllLibJson&hasNull=hasNull'
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
		        {name: 'text'},
		        {name: 'value'}
		    ]),
		    autoLoad : false
		});
		
		// 检索条件——检索属性
		var searchParamsStore = new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [
				['rtreTitle', '题名标题'], 
			]
		});
		
		var store_Combox = new Ext.form.ComboBox({
			mode : 'remote',
			store : metastore,
			resizable : true,
			name : 'rtliName',
			emptyText : '请先选择资源库...',
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
		    name: '_index_rlclId',
		    hiddenName: 'index_rlclId',
		    emptyText: '再选择资源类型...',
		    width: 100,
		    triggerAction: 'all',
		    valueField: 'value',
		    displayField: 'text',
		    width : 120
		});
		
		//当特色发生改变时，级联改变特色库所联系的资源类型
		metastore.on('load', function(comboBox){
		   if('${searchRtliId }' != '' && '${searchRtliId }' != '0'){
				store_Combox.setValue('${searchRtliId }');
				sourcetype.clearValue(); //清空之前的资源类型
			    var store_url = url_sourcetype_store + "&rtliId=${searchRtliId}";
			    sourcetype_store.proxy = new Ext.data.HttpProxy({url: store_url});
			    sourcetype_store.load();
			}
		})
		sourcetype_store.on('load', function(comboBox){
			if('${searchRlclId }'.length != '' && '${searchRlclId }' != '0'){
				sourcetype.setValue('${searchRlclId }');
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
		
		//默认选中的属性，该变量为必备
		var selectCode = 'rsdaTitle';
	</script>
	<script type="text/javascript" src="<%=path %>/js/searchUtil.js"></script>
    
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var pagingDisplayMsg = '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>';
			var url_data_grid = '<%=path %>/console/data/text/textResourceManager.htm?action=getResourceListJson';
			
			var sm = new Ext.grid.CheckboxSelectionModel(); // 创建多选模式
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				sm,
				{header:'资源名称', dataIndex:'rtreTitle', sortable: true},
				{header:'资源库', dataIndex:'rtliName', sortable: true},
				{header:'资源分类', dataIndex:'rlclName', sortable: true},
				{header:'启用', dataIndex:'rtreUsed',sortable: true, align:'center', renderer: function(v){
				    if(v == 1){
				        return "<img src='<%=path %>/images/icon/yes.png'>";
				    }else if(v == 0){
				    	return "<img src='<%=path %>/images/icon/no.png'>";
				    }
				}},
				{header:'推荐', dataIndex:'rtreRecommend',sortable: true, align:'center', renderer: function(v){
				    if(v == 1){
				        return "<img src='<%=path %>/images/icon/yes.png'>";
				    }else if(v == 0){
				    	return "<img src='<%=path %>/images/icon/no.png'>";
				    }
				}},
				{header:'录入时间', dataIndex:'rtreDate', sortable: true}
			]); 
			
			
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[
				    {name:'rtreId'}, 
				    {name:'rtreTitle'},
				    {name:'rtreRtliId'}, 
					{name:'rtliName'}, 
					{name:'rlclName'}, 
					{name:'rtreUsed'},
					{name:'rtreRecommend'},
					{name:'rtreDate'}
				]) ,
				sortInfo : {
					field : 'rtreDate',
					direction : 'DESC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
				
			}); 
			
			var searchParamsCombox1 = new Ext.form.ComboBox({
				mode : 'local',
				store : searchParamsStore,
				emptyText : '请选择...',
				triggerAction : 'all',
				editable : false,
				value : '',
				valueField : 'value',
				displayField : 'text',
				width : 80
			});
			
			// 检索条件1
			var searchIndexKey1 = new Ext.form.TextField({
				emptyText:'检索关键字',
				id:'searchIndexKey1',
				width:80
			});
			
			var tbar1 = new Ext.Toolbar([
				'资源库：',store_Combox,' ',
				sourcetype,'-',
				'属性：',
				searchParamsCombox1,
				searchIndexKey1,
				{
					text : '&nbsp;检&nbsp;索&nbsp;',
					iconCls : 'common_search',
					handler : searchMan
				} ,'-', { 
					text: '修改', 
					enableToggle: false, 
					iconCls:'common_edit', 
					tooltip: '修改资源信息',
					handler: resourceUpdate 
				} , '-',{ 
					text: '删除', 
					enableToggle: false, 
					iconCls:'common_delete', 
					tooltip: '删除选中的资源',
					handler: resourceDelete 
				}, '-', { 
					text: '推荐开关', 
					enableToggle: false, 
					iconCls:'common_shutdown', 
					handler: setTuijian
				} , '-', { 
					text: '显示开关', 
					enableToggle: false, 
					iconCls:'common_shutdown', 
					handler: setUse
				}
			]);
			
			// 创建一个分页控件
			var pagingBar = new Ext.PagingToolbar({
				pageSize : pageNum,
				store : grid_store,
				displayInfo : true,
				displayMsg : pagingDisplayMsg,
				emptyMsg : '没有记录',
				plugins : new Ext.ux.ProgressBarPager()
			});
			
			var grid = new Ext.grid.GridPanel({ 
				height: document.documentElement.clientHeight, 
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				sm: new Ext.grid.RowSelectionModel({
				singleSelect :true
				}),
				loadMask: true,     //加载时提示load... 
				frame:false,
				border:false,
				viewConfig: { 
					forceFit: true 
				}, 
				cm: cm, 
				sm: sm,
				autoScroll : true,
				region : 'center',
				tbar: tbar1,
				bbar : pagingBar
			}); 
			
			grid.on('dblclick', resourceUpdate); //双击进入模板设置
			function resourceUpdate(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
					var rtreId = selectRow.get("rtreId");
					window.location.href = "<%=path %>/console/data/text/textResource.htm?action=updateResource&source=manager&rtreId=" + rtreId;
				}else{
					Msg.alert('请点击选择待修改的资源！',3);
				}
			}
			function resourceDelete(){
				var ids = getSelectId();
				if(ids.length > 0){
					var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
					});
			    	Ext.Msg.confirm('提示','您确定要删除选中的资源吗？\n',function(button,text){    
		                if(button == "yes"){
						    Ext.Ajax.request({ 
								url:'<%=path %>/console/data/text/textResource.htm?action=deleteResource&ids=' + ids,
								method: 'GET',   
			           			text: "Updating...",     
			           			success: function (result, request ){
			           			    if(result.responseText == "1"){
			           			    	myMask.hide();
				           			    grid_store.reload();
				           			    Msg.success("删除成功！");
			           			    }else{
			           			    	myMask.hide();
			           			   		Msg.fail("删除失败！");
			           			    }
			           			}
							});
						}
     				}); 
				}else{
					Msg.alert('请点击选择待删除的资源！',3);
				}
			}
			
			function setTuijian(){
			    var idlist = getSelectId();
			    if(idlist.length < 1){
			        Msg.fail("请选择需要设置的资源!", 3);
			        return;
			    }
			    Ext.Ajax.request({ 
					url:'<%=path %>/console/data/text/textResourceManager.htm?action=changRcmdStatu&idlist='+idlist,
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
			        Msg.fail("请选择需要设置的资源!", 3);
			        return;
			    }
			    Ext.Ajax.request({ 
					url:'<%=path %>/console/data/text/textResourceManager.htm?action=changUseStatu&idlist='+idlist,
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
			
			function getSelectId(){
				var rows = grid.getSelectionModel().getSelections();
				var rtreIds = "";
				if(rows.length >= 1) {
					for (var i = 0; i < rows.length; i++) {
						if (i > 0){
							rtreIds += ",";
						}	
						rtreIds += rows[i].json.rtreId;
					}
				}
				return rtreIds;
			}
			
			//创建Ext的参数
			function getSearchParams(){
				var searchParamsData = {	
					start : 0,
					limit : limitLength,
					eleCode1 : searchParamsCombox1.getValue(),
					indexKey1 : searchIndexKey1.getValue(),
					rtliId : store_Combox.getValue(),
					rlclId : sourcetype.getValue()
				}
				return searchParamsData;
			}
			
			grid_store.on('beforeload',function(){ 
				Ext.apply(   
				this.baseParams, 
				{ 
					eleCode1 : searchParamsCombox1.getValue(),
					indexKey1 : searchIndexKey1.getValue(),
					rtliId : store_Combox.getValue(),
					rlclId : sourcetype.getValue()
				}); 
			}); 
			
			grid_store.load({
				params : getSearchParams()
			}); 
			
			function searchMan() {
				grid_store.load({
					params : getSearchParams()
				});
			}
			
			///**********************加载是将元素渲染到页面****************************// 
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
		${result }
	</body> 
</html>