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
	<style type="text/css">
		.progressLine {
			width: 200px;
			height: 16px;
			border-left: 1px solid #CACFDA;
			border-right: 1px solid #CACFDA;
			padding: 0px;
			background-image: url("<%=path %>/images/progress_bg.gif");
		}
		
		.completedLine {
			background-image: url("<%=path %>/images/progress_ok.gif");
			width: 0px;
			height: 16px;
		}
		
		.messageInfo {
			font-size: 12px;
			color: #273E56;
			height: 12px;
		}
	</style>
	
	<script type="text/javascript">var root = "<%=path %>/"</script>
	<script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
	<script type="text/javascript">
	
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数
			//获取查重结果访问路径
			var url_data_grid = '<%=path %>/console/data/resource/systemCheckRepeat.htm?action=getRepeatResultJson'; 
			
			//重重资源json数据基本访问路径
			var url_resource_basic_grid = "<%=path %>/console/data/resource/checkRepeat.htm?action=getRepeatResourceJson&&haveSelf=1";
			var url_resource_grid = "";
			
			var RSDA_ID; //资源ID
			var CCTJ; //查询条件
			
			
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
				var cctj = parseCCTJ();
				showRepeatResource(rsdaId, cctj);
			}

			function parseCCTJ(){
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
				return cctj;
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
							myMask.show();
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
				{header:'题名', dataIndex:'rsdaTitle', width:20, sortable:true, hidden: ${!repeatBean.checkTitle} }, 
				
				{header:'出版者', dataIndex:'rsdaPublisher', width:20, sortable:true, hidden: ${!repeatBean.checkPublisher} }, 
				
				{header:'源文大小', dataIndex:'rsdaFileSize', width:20, sortable:true, hidden: ${!repeatBean.checkFilesize} },
				
				{header:'源文格式', dataIndex:'rsdaFileType', width:20, sortable:true, hidden: ${!repeatBean.checkFiletype} },
				
				
				//-------------------更多的
				{header:'主题', dataIndex:'rsdaSubject', width:20, sortable:true, hidden: ${!repeatBean.checkSubject} },
				
				{header:'描述', dataIndex:'rsdaDescription', width:20, sortable:true, hidden: ${!repeatBean.checkDescription} },
				
				{header:'来源', dataIndex:'rsdaSource', width:20, sortable:true, hidden: ${!repeatBean.checkSource} },
				
				{header:'语种', dataIndex:'rsdaLanguage', width:20, sortable:true, hidden: ${!repeatBean.checkLanguage} },
				
				{header:'关联', dataIndex:'rsdaRelation', width:20, sortable:true, hidden: ${!repeatBean.checkRelation} },
				
				{header:'覆盖范围', dataIndex:'rsdaCoverage', width:20, sortable:true, hidden: ${!repeatBean.checkCoverage} },
				
				{header:'创建者', dataIndex:'rsdaCreator', width:20, sortable:true, hidden: ${!repeatBean.checkCreator} },
				
				{header:'其它责任者', dataIndex:'rsdaContributor', width:20, sortable:true, hidden: ${!repeatBean.checkContributor} },
				
				{header:'权限', dataIndex:'rsdaRights', width:20, sortable:true, hidden: ${!repeatBean.checkRights} },
				
				{header:'日期', dataIndex:'rsdaDate', width:20, sortable:true, hidden: ${!repeatBean.checkDate} },
				
				{header:'类型', dataIndex:'rsdaType', width:20, sortable:true, hidden: ${!repeatBean.checkType} },
				
				{header:'格式', dataIndex:'rsdaFormat', width:20, sortable:true, hidden: ${!repeatBean.checkFormat} },
				
				{header:'标识符', dataIndex:'rsdaIdentifier', width:20, sortable:true, hidden: ${!repeatBean.checkIdentifier} },
				//---------------新加的END
				
				
				
				{header:'数量', dataIndex:'rsyrRepeatnum',width:20, align:'center', sortable:true, renderer: function(v, cellmeta, record){
					  var rsdaId = record.data["rsyrRsdaId"];
					  return "<a href='javascript:showAllRepeat("+rsdaId+");' style='color:red'>"+(v == null ? 0 : v)+"</a>";
				} } 
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'rsyrId'}, 
					{name:'rsyrRsdaId'},
					{name:'rsyrRepeatnum'}, 
					{name:'rsdaTitle'},
					{name:'rsdaSubject'}, 
					{name:'rsdaDescription'}, 
					{name:'rsdaSource'}, 
					{name:'rsdaLanguage'},
					{name:'rsdaRelation'}, 
					{name:'rsdaCoverage'}, 
					{name:'rsdaCreator'}, 
					{name:'rsdaPublisher'},
					{name:'rsdaContributor'},
					{name:'rsdaRights'}, 
					{name:'rsdaDate'}, 
					{name:'rsdaType'},
					{name:'rsdaFormat'}, 
					{name:'rsdaIdentifier'}, 
					{name:'rsdaFileType'}, 
					{name:'rsdaFileSize'}
				]),
				sortInfo : {
					field : 'rsyrRepeatnum',
					direction : 'DESC'
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
				sm : repeatSm,
				autoScroll : true,
				region : 'center',
				tbar: new Ext.Toolbar({  //工具栏 
					items:[ 
						{
							text : '&nbsp;删&nbsp;除&nbsp;',
							iconCls : 'common_delete',
							handler : deleteRepeatResource
						},'-',{
							text : '一键清除',
							iconCls : 'common_delete',
							handler : deleteAllRepeat
						},'-',{ 
							text: '&nbsp;返&nbsp;回&nbsp;', 
							enableToggle: false, 
							iconCls:'common_back', 
							handler: function(){ 
								window.location.href = "<%=path %>/console/data/resource/systemCheckRepeat.htm?action=init";
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
			
			grid.on('dblclick', showAll); //双击进入查看重复资源
			
			function showAll(){
				var rows = grid.getSelectionModel().getSelections();
				if(rows.length > 0){
					var rsdaId = rows[0].json.rsyrRsdaId;
					showAllRepeat(rsdaId)
				}
			}
			
			function deleteRepeatResource(){
				var rows = grid.getSelectionModel().getSelections();
				var rsdaIds = "";
				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源删除中...请稍后！',
						removeMask : true // 完成后移除
				});
				
				if(rows.length < 1) {
					return;
				}
				var rsdaIds = "";
				for (var i = 0; i < rows.length; i++) {
					if (i > 0){
						rsdaIds += ",";
					}	
					rsdaIds += rows[i].json.rsyrRsdaId;
				}
				//var rsdaId  = rows[0].json.rsyrRsdaId;
				var cctj = parseCCTJ();
				Ext.Msg.confirm("确认消息框", "您确定要清除选中资源的重复记录吗？\n", function(btn, txt) {
					if(btn == 'yes') {
						myMask.show();
						Ext.Ajax.request({ 
							url:'<%=path %>/console/data/resource/systemCheckRepeat.htm?action=deleteRepeat&rsdaIds=' + rsdaIds + '&cctj=' + cctj,
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
			

			function deleteAllRepeat(){
				var cctj = parseCCTJ();
				Ext.Msg.confirm("确认消息框", "您确定要清除所有资源的重复记录吗？\n", function(btn, txt) {
					if(btn == 'yes') {
						Win.show(230,110,'progress','查重进度');

						Ext.Ajax.request({ 
							url:'<%=path %>/console/data/resource/systemCheckRepeat.htm?action=clearRepeat&cctj=' + cctj,
							method: 'GET',   
		           			text: "Updating...",     
		           			success: function (result, request ){
		           			    if(result.responseText == "1"){
			           			    Ext.Msg.alert("消息","清除成功！");
			           			    grid_store.reload();
									Win.hide();
		           			    }else{
		           			   		Ext.Msg.alert("错误","清除失败！");
		           			    }
		           			}
						});
						updateProgressCompleted();
					}
				});
			}

			function updateProgressCompleted(){
				var url_progress = "<%=path %>/console/data/resource/systemCheckRepeat.htm?action=getDeleteProgress";
				Ext.Ajax.request({ 
					url:url_progress,
					success: function(response, options) {
						var w = response.responseText;
						document.getElementById("successNum").innerHTML = w/2;
						document.getElementById("completedLine").style.width = w + "px"; 
						setTimeout(updateProgressCompleted, 500);
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

	<div id="progress" style="display: none;">
    	<table style="width: 205px;padding-left: 5px;" border="0px">
    		<tr style="height: 25px;">
    			<td style="padding-left: 5px;"><span id="messageInfo" class="messageInfo">资源清除中...</span></td>
    		</tr>
    		
    		<tr style="height: 20px;">
    			<td style="padding-left: 5px;">
    				<div class="progressLine">
    					<div id="completedLine" class="completedLine" style="height: 14px;"></div>
    				</div>
    			</td>
    		</tr>
    		<tr style="font-size: 12px;">
    			<td align="left" style="padding-left: 5px;">
    					已完成：<span id="successNum" style="color: blue">0</span>/100
    			</td>
    		</tr>
    	</table>
    </div>

    <!-- 处理结果 -->
    ${requestScope.result }
  </body>
</html>
