<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%
	String path = request.getContextPath();
%>
<%@ include file="../../common/extInclude.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>资源查重</title>
    
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<script type="text/javascript">var root = "<%=path %>/"</script>
	<script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
	<script type="text/javascript">
	
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数
			//获取查重结果访问路径
			var url_data_grid = '<%=path %>/console/data/resource/checkRepeat.htm?action=getRepeatResultJson&rminId=${rminId }&rsdaIds=${rsdaIds }&showAll=${showall}'; 
			
			//重重资源json数据基本访问路径
			var url_resource_basic_grid = "<%=path %>/console/data/resource/checkRepeat.htm?action=getRepeatResourceJson";
			var url_resource_grid = "";
			
			var RSDA_ID; //资源ID
			var CCTJ; //查询条件
			///////////////////////////重复资源列表//////////////////////
			
			// 创建多选模式
			var resourceSm = new Ext.grid.CheckboxSelectionModel();
	
			// 定义列模型
			var resourceCm = new Ext.grid.ColumnModel([
				new Ext.grid.RowNumberer(),resourceSm,
				{header : '题名', dataIndex : 'rsdaTitle',sortable : true},
				{header : '所属特色库',width : 40, dataIndex : 'rmstName', sortable : true},
				{header : '资源类型',width : 25, dataIndex : 'rltyName', sortable : true}
			]);
			
			var resourceStore = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_resource_basic_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'rsdaId'}, 
				    {name:'rsdaTitle'},
				    {name:'rmstName'}, 
					{name:'rltyName'}
				]),
				sortInfo : {
					field : 'rsdaId',
					direction : 'DESC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			}); 
			// 创建工具条
			var resourceTbar = new Ext.Toolbar([{
					text : '查看详细',
					enableToggle : false,
					iconCls : 'search',
					handler : onShowResourceInfo
				}, '-', {
					text : '&nbsp;删&nbsp;除&nbsp;',
					enableToggle : false,
					iconCls : 'common_delete',
					handler : deleteResource
				}, '-', {
					text : '&nbsp;关&nbsp;闭&nbsp;',
					iconCls : 'common_back',
					handler : function() {
						repeatResource_Win.hide();
					}
				}
			]);
			
			// 创建一个分页控件
			var resourcePbar = new Ext.PagingToolbar({
				pageSize: 15, 
				store: resourceStore, 
				displayInfo: true, 
				displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>', 
				emptyMsg: '没有记录', 
				plugins: new Ext.ux.ProgressBarPager()
			});
	
			// 组合表格
			var repeatResourceGrid = new Ext.grid.GridPanel({
				region : 'center',
				el : this.contentEl,
				cm : resourceCm,
				sm: resourceSm,
				store : resourceStore,
				viewConfig : {
					forceFit : true
				},
				border : false,
				loadMask : true, // 加载中显示一个读取中的覆盖提示
				frame : false,
				stripeRows : true, // 行分割符
				columnLines : true, // True表示为在列分隔处显示分隔符
				autoScroll : true,
				enableColumnHide : false,
				tbar : resourceTbar,
				bbar : resourcePbar
			});
			
			repeatResourceGrid.on('dblclick', showResourceInfo); //双击进入模板设置
			
			var repeatResource_Win
			function showRepeatResource(rsdaId, eleCode){
				//url_resource_grid = url_resource_basic_grid + "&rsdaId="+rsdaId+"&cctj=" + eleCode;
				RSDA_ID = rsdaId;
				CCTJ = eleCode;
				if (!repeatResource_Win) {
					repeatResource_Win = new Ext.Window({
						closeAction : 'hide',
						closable : true,
						width : 580,
						height : 350,
						title : '重复资源列表',
						layout : 'fit',
						modal : true,
						buttonAlign : 'center',
						items : [repeatResourceGrid]
					});
				}
				repeatResource_Win.on('beforeshow', function() {
					resourceStore.load({
						params : {
							start : 0,
							limit : 15,
							rsdaId: rsdaId,
							cctj: eleCode
						}
					});
				});
				repeatResource_Win.show();
			}
			
			resourceStore.on('beforeload',function(){ 
				Ext.apply(  
				this.baseParams, 
				{ 
					rsdaId: RSDA_ID,
					cctj: CCTJ
				}); 
			}); 
			
			function showAllRepeat(rsdaId){
				var cctj = "0";
				if(${repeatBean.checkTitle }){
					cctj += ",1"
				}
				if(${repeatBean.checkPublisher }){
					cctj += ",2";
				}
				if(${repeatBean.checkFilesize }){
					cctj += ",3";
				}
				if(${repeatBean.checkFiletype }){
					cctj += ",4";
				}
				
				if(${repeatBean.checkSubject }){
					cctj += ",5";
				}
				if(${repeatBean.checkDescription }){
					cctj += ",6";
				}
				if(${repeatBean.checkSource }){
					cctj += ",7";
				}
				if(${repeatBean.checkLanguage }){
					cctj += ",8";
				}
				if(${repeatBean.checkRelation }){
					cctj += ",9";
				}
				if(${repeatBean.checkCoverage }){
					cctj += ",10";
				}
				if(${repeatBean.checkCreator }){
					cctj += ",11";
				}
				if(${repeatBean.checkContributor }){
					cctj += ",12";
				}
				if(${repeatBean.checkRights }){
					cctj += ",13";
				}
				if(${repeatBean.checkDate }){
					cctj += ",14";
				}
				if(${repeatBean.checkType }){
					cctj += ",15";
				}
				if(${repeatBean.checkFormat }){
					cctj += ",16";
				}
				if(${repeatBean.checkIdentifier }){
					cctj += ",17";
				}
				showRepeatResource(rsdaId, cctj);
			}
			
			//删除重复的资源
			function deleteResource(){
				var rows = repeatResourceGrid.getSelectionModel().getSelections();
				var rsdaIds = "";
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
				});
				if(rows.length >= 1) {
					Ext.Msg.confirm("确认消息框", "您确定要删除选中的资源吗？\n", function(btn, txt) {
						if(btn == 'yes') {
							for (var i = 0; i < rows.length; i++) {
								if (i > 0){
									rsdaIds += ",";
								}	
								rsdaIds += rows[i].json.rsdaId;
							}
							Ext.Ajax.request({ 
								url:'<%=path %>/console/data/resource/SourceManager.htm?action=deleteResource&idlist='+rsdaIds,
								method: 'GET',   
			           			text: "Updating...",     
			           			success: function (result, request ){
			           			    if(result.responseText == "1"){
			           			    	myMask.hide();
				           			    Ext.Msg.alert("消息","删除成功！");
				           			    resourceStore.reload();
			           			    }else{
			           			    	myMask.hide();
			           			   		Ext.Msg.alert("错误","删除失败！");
			           			    }
			           			}
							});
						}
					});
				}else{
					alertWarring('请选择待删除的数据！\n\n');
				}	
			}
			
			function onShowResourceInfo(){
				var rows = repeatResourceGrid.getSelectionModel().getSelections();
				if(rows.length >= 1) {
					showResourceInfo();
				}else{
					alertWarring('请至选择待查看的资源！\n\n');
				}
			}
			
			//查看资源详细信息
			function showResourceInfo(){
				var rows = repeatResourceGrid.getSelectionModel().getSelections();
				if(rows.length >= 1) {
					rsdaId = rows[0].json.rsdaId;
					document.getElementById("rsdaId").value = rsdaId;
					document.forms.detailForm.submit();
				}
			}
			
			
			
			
			///////////////////////////////////查重结果////////////////////////////////////////
			
			// 创建多选模式
			var repeatSm = new Ext.grid.CheckboxSelectionModel();
			
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				repeatSm,
				{header:'资源题名', dataIndex:'rsreSourceTitle', sortable:true},
				
				<%if((Boolean)request.getAttribute("showall")){%>
				{header:'题名', dataIndex:'rsreTitle', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkTitle}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",1);'>"+(v == null ? 0 : v)+"</a>";
				} }, 
				{header:'出版者', dataIndex:'rsrePublisher', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkPublisher}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",2);'>"+(v == null ? 0 : v)+"</a>";
				} }, 
				{header:'源文大小', dataIndex:'rsreFileSize', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkFilesize}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",3);'>"+(v == null ? 0 : v) +"</a>";
				} },
				{header:'源文格式', dataIndex:'rsreFileType', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkFiletype}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",4);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				
				//-------------------新加的
				{header:'主题', dataIndex:'rsreSubject', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkSubject}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",5);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'描述', dataIndex:'rsreDescription', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkDescription}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",6);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'来源', dataIndex:'rsreSource', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkSource}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",7);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'语种', dataIndex:'rsreLanguage', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkLanguage}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",8);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'关联', dataIndex:'rsreRelation', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkRelation}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",9);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'覆盖范围', dataIndex:'rsreCoverage', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkCoverage}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",10);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'创建者', dataIndex:'rsreCreator', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkCreator}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",11);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'其它责任者', dataIndex:'rsreContributor', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkContributor}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",12);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'权限', dataIndex:'rsreRights', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkRights}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",13);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'日期', dataIndex:'rsreDate', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkDate}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",14);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'类型', dataIndex:'rsreType', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkType}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",15);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'格式', dataIndex:'rsreFormat', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkFormat}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",16);'>"+(v == null ? 0 : v)+"</a>";
				} },
				
				{header:'标识符', dataIndex:'rsreIdentifier', width:20, align:'center', sortable:true, hidden: ${!repeatBean.checkIdentifier}, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showRepeatResource("+rsdaId+",17);'>"+(v == null ? 0 : v)+"</a>";
				} },
				//---------------新加的END
				<%}%>
				
				
				
				{header:'全部重复', dataIndex:'rsreRepeatAll',width:20, align:'center', sortable:true, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsreRsdaId"];
					  return "<a href='javascript:showAllRepeat("+rsdaId+");' style='color:red'>"+(v == null ? 0 : v)+"</a>";
				} }
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'rsreRsdaId'}, 
				    {name:'rsreSourceTitle'},
				    {name:'rsreTitle'}, 
					{name:'rsrePublisher'}, 
					{name:'rsreFileSize'}, 
					{name:'rsreFileType'}, 
					{name: 'rsreRepeatAll'},
					
					{name:'rsreSubject'},
					{name:'rsreDescription'},
					{name:'rsreSource'},
					{name:'rsreLanguage'},
					{name:'rsreRelation'},
					{name:'rsreCoverage'},
					{name:'rsreCreator'},
					{name:'rsreContributor'},
					{name:'rsreRights'},
					{name:'rsreDate'},
					{name:'rsreType'},
					{name:'rsreFormat'},
					{name:'rsreIdentifier'}, 
				]),
				sortInfo : {
					field : 'rsreRepeatAll',
					direction : 'DESC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			}); 
			
			grid_store.on('beforeload',function(){ 
				Ext.apply(  
				this.baseParams, 
				{ 
					rsdaTitle: rsdaTitle.getValue()
				}); 
			}); 
			
			
			//标题检索关键字
			var rsdaTitle = new Ext.form.TextField({
				id : 'rsdaTitle',
				name : 'rsdaTitle',
				width: 110,
				listeners :{
			        specialKey :function(field,e){
			            if (e.getKey() == Ext.EventObject.ENTER){
			               searchMan();
			            } 
			        }
			    } 
			});
			
			function searchMan(){
				var rsdaTitleValue = rsdaTitle.getValue();
				grid_store.load({params:{
		            start:0,
		            limit:pageNum,
		            rsdaTitle: rsdaTitleValue
				}});  
			}
			
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
				sm : repeatSm,
				autoScroll : true,
				region : 'center',
				tbar: new Ext.Toolbar({  //工具栏 
					items:[ 
						'题名：', rsdaTitle, 
						{
							text : '&nbsp;查&nbsp;询&nbsp;',
							iconCls : 'common_search',
							handler : searchMan
						},'-',{
							text : '&nbsp;删&nbsp;除&nbsp;',
							iconCls : 'common_delete',
							handler : deleteRepeatResource
						},'-',{ 
							text: '&nbsp;返&nbsp;回&nbsp;', 
							enableToggle: false, 
							iconCls:'common_back', 
							handler: function(){ 
								if('${reqType }' == '1'){
									window.location.href = "<%=path %>/console/data/resource/checkRepeat.htm?action=initMissionResource&rminId=${rminId}";
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
			
			function deleteRepeatResource(){
				var rows = grid.getSelectionModel().getSelections();
				var rsdaIds = "";
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
				});
				
				var rsdaIds  = "";
				var text = "作业中所有重复";
				if(rows.length >= 1) {
					for (var i = 0; i < rows.length; i++) {
						if (i > 0){
							rsdaIds += ",";
						}	
						rsdaIds += rows[i].json.rsreRsdaId;
					}
					text = "选中";
				}
				Ext.Msg.confirm("确认消息框", "您确定要删除"+text+"的资源吗？\n", function(btn, txt) {
					if(btn == 'yes') {
						Ext.Ajax.request({ 
							url:'<%=path %>/console/data/resource/checkRepeat.htm?action=deleteResource&rminId=${rminId }&rsdaIds=' + rsdaIds,
							method: 'GET',   
		           			text: "Updating...",     
		           			success: function (result, request ){
		           			    if(result.responseText == "1"){
		           			    	myMask.hide();
			           			    Ext.Msg.alert("消息","删除成功！");
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
			

			
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				
				// 创建主面板
				var displayPanel = new Ext.Viewport({
					layout : 'border',
					items : [grid]
				}); 
			}) 

		</script> 
	
  </head>
  
  <body>
    <div id="grid" style=""></div> 
    
    <form action="<%=path %>/console/data/resource/checkRepeat.htm" name="detailForm" target="_blank">
    	<input type="hidden" name="action" id="action" value="detail" />
    	<input type="hidden" name="rsdaId" id="rsdaId" value="0" />
    </form>
    <!-- 处理结果 -->
    ${requestScope.result }
  </body>
</html>
