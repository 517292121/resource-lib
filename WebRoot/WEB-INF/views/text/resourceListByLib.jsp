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
	<script type="text/javascript">var root = "<%=path %>/";</script>
    <script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
    
    <script type="text/javascript">
		//每页显示条数
		var limitLength = <%=ConstantsObj.DEFAULT_PAGE_SIZE %>;
		
		// 检索条件——检索属性
		var searchParamsStore = new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [
				['rtreTitle', '${rtli.rtliTitle }'],
				<c:if test="${!empty rtli.rtliCol1Label }">['rtreCol1', '${rtli.rtliCol1Label }'],</c:if> 
				<c:if test="${!empty rtli.rtliCol2Label }">['rtreCol2', '${rtli.rtliCol2Label }'],</c:if> 
				<c:if test="${!empty rtli.rtliCol3Label }">['rtreCol3', '${rtli.rtliCol3Label }'],</c:if> 
				<c:if test="${!empty rtli.rtliCol4Label }">['rtreCol4', '${rtli.rtliCol4Label }'],</c:if> 
				<c:if test="${!empty rtli.rtliCol5Label }">['rtreCol5', '${rtli.rtliCol5Label }'],</c:if> 
				<c:if test="${!empty rtli.rtliCol6Label }">['rtreCol6', '${rtli.rtliCol6Label }'],</c:if> 
				<c:if test="${!empty rtli.rtliCol7Label }">['rtreCol7', '${rtli.rtliCol7Label }'],</c:if> 
				<c:if test="${!empty rtli.rtliCol8Label }">['rtreCol8', '${rtli.rtliCol8Label }'],</c:if> 
				<c:if test="${!empty rtli.rtliCol9Label }">['rtreCol9', '${rtli.rtliCol9Label }'],</c:if> 
				<c:if test="${!empty rtli.rtliCol10Label }">['rtreCol10', '${rtli.rtliCol10Label }'],</c:if> 
			]
		});

	</script>
	<script type="text/javascript" src="<%=path %>/js/searchUtil.js"></script>
    
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var pagingDisplayMsg = '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>';
			var url_data_grid = '<%=path %>/console/data/text/textResource.htm?action=getResourceListJson&rtliId=${rtliId }';
			
			var sm = new Ext.grid.CheckboxSelectionModel(); // 创建多选模式
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				sm,
				{header:'${rtli.rtliTitle }', dataIndex:'rtreTitle', sortable: true},
				<c:if test="${!empty rtli.rtliCol1Label }">{header:'${rtli.rtliCol1Label }', dataIndex:'rtreCol1', sortable: true},</c:if>
				<c:if test="${!empty rtli.rtliCol2Label }">{header:'${rtli.rtliCol2Label }', dataIndex:'rtreCol2', sortable: true},</c:if> 
				<c:if test="${!empty rtli.rtliCol3Label }">{header:'${rtli.rtliCol3Label }', dataIndex:'rtreCol3', sortable: true},</c:if> 
				<c:if test="${!empty rtli.rtliCol4Label }">{header:'${rtli.rtliCol4Label }', dataIndex:'rtreCol4', sortable: true},</c:if> 
				<c:if test="${!empty rtli.rtliCol5Label }">{header:'${rtli.rtliCol5Label }', dataIndex:'rtreCol5', sortable: true},</c:if> 
				<c:if test="${!empty rtli.rtliCol6Label }">{header:'${rtli.rtliCol6Label }', dataIndex:'rtreCol6', sortable: true},</c:if> 
				<c:if test="${!empty rtli.rtliCol7Label }">{header:'${rtli.rtliCol7Label }', dataIndex:'rtreCol7', sortable: true},</c:if> 
				<c:if test="${!empty rtli.rtliCol8Label }">{header:'${rtli.rtliCol8Label }', dataIndex:'rtreCol8', sortable: true},</c:if> 
				<c:if test="${!empty rtli.rtliCol9Label }">{header:'${rtli.rtliCol9Label }', dataIndex:'rtreCol9', sortable: true},</c:if> 
				<c:if test="${!empty rtli.rtliCol10Label }">{header:'${rtli.rtliCol10Label }', dataIndex:'rtreCol10', sortable: true},</c:if> 
				{header:'所属分类', dataIndex:'rlclName', sortable: true},
				{header:'是否启用', dataIndex:'rtreUsed', width:40,sortable: true, align:'center', renderer: function(v){
				    if(v == 1){
				        return "<img src='<%=path %>/images/icon/yes.png'>";
				    }else if(v == 0){
				    	return "<img src='<%=path %>/images/icon/no.png'>";
				    }
				}},
				{header:'是否推荐', dataIndex:'rtreRecommend', width:40,sortable: true, align:'center', renderer: function(v){
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
				    <c:if test="${!empty rtli.rtliCol1Label }">{name:'rtreCol1'}, </c:if>
				    <c:if test="${!empty rtli.rtliCol2Label }">{name:'rtreCol2'}, </c:if>
				    <c:if test="${!empty rtli.rtliCol3Label }">{name:'rtreCol3'}, </c:if>
				    <c:if test="${!empty rtli.rtliCol4Label }">{name:'rtreCol4'}, </c:if>
				    <c:if test="${!empty rtli.rtliCol5Label }">{name:'rtreCol5'}, </c:if>
				    <c:if test="${!empty rtli.rtliCol6Label }">{name:'rtreCol6'}, </c:if>
				    <c:if test="${!empty rtli.rtliCol7Label }">{name:'rtreCol7'}, </c:if>
				    <c:if test="${!empty rtli.rtliCol8Label }">{name:'rtreCol8'}, </c:if>
				    <c:if test="${!empty rtli.rtliCol9Label }">{name:'rtreCol9'}, </c:if>
				    <c:if test="${!empty rtli.rtliCol10Label }">{name:'rtreCol10'}, </c:if>
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
			grid_store.load(); 
			
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
				'属性：',
				searchParamsCombox1,
				searchIndexKey1,
				{
					text : '&nbsp;检&nbsp;索&nbsp;',
					iconCls : 'common_search',
					handler : searchMan
				},
				{ 
					text: '新增资源', 
					enableToggle: false, 
					iconCls:'common_add', 
					tooltip: '手工录入单条资源',
					handler: resourceAdd 
				} , '-',
				{ 
					text: 'excel导入', 
					enableToggle: false, 
					iconCls:'excel', 
					tooltip: '通过excel批量导入元数据及资源',
					handler: function(){
						window.location.href = "<%=path %>/console/data/text/textResourceImport.htm?source=resList&rtliId=${rtliId}";
					} 
				},'-', { 
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
				} , '-',{ 
					text: '返回', 
					enableToggle: false, 
					iconCls:'common_back', 
					tooltip: '返回资源类型列表',
					handler: function(){
						window.location.href = "<%=path %>/console/data/text/textResource.htm";
					} 
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
			
			function resourceAdd(){
				window.location.href = "<%=path %>/console/data/text/textResource.htm?action=inputResource&source=resList&rtliId=${rtliId }";
			}
			function resourceUpdate(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
					var rtreId = selectRow.get("rtreId");
					window.location.href = "<%=path %>/console/data/text/textResource.htm?action=updateResource&source=reslist&rtreId=" + rtreId;
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
					indexKey1 : searchIndexKey1.getValue()
				}
				return searchParamsData;
			}
						
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