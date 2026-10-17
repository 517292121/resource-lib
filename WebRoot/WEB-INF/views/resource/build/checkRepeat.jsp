<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%
	String path = request.getContextPath();
%>
<%@ include file="../../common/extInclude.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>资源查重</title>
    
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<script type="text/javascript">var root = "<%=path %>/"</script>
	<script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
	
	<style type="text/css">
		.progressLine{
			width: 200px;
			height: 16px;
			border-left: 1px solid #CACFDA;
			border-right: 1px solid #CACFDA;
			padding: 0px;
			background-image: url("<%=path %>/images/progress_bg.gif");
		}
		.completedLine{
			background-image: url("<%=path %>/images/progress_ok.gif");
			width: 0px;
			height: 16px;
		}
		.messageInfo{
			font-size: 12px;
			color: #273E56;
			height: 12px;
		}
	</style>
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数
			//获取作业下资源的访问路径
			var url_data_grid = '<%=path %>/console/data/inputSingle.htm?action=resourceListJsonByMission&RSDA_RMIN_ID=${rminId}'; 
			
			///////////////////////////查重属性设置//////////////////////
			
			//复选
			
			//题名
			var param_Title = new Ext.form.Checkbox({
			    boxLabel : '资源题名',
			    value:'1',
			    checked: true,
			    style: 'margin-top: 3px;',
			    name:'checkTitle',
			    id:'checkTitle',
			    checked:true
			});
			
			//出版者
			var param_publisher = new Ext.form.Checkbox({
				boxLabel:'出版者',
				value:'2',
				style: 'margin-top: 3px;',
				name:'checkPublisher',
				id:'checkPublisher'
			});
			
			//资源大小
			var param_filesize = new Ext.form.Checkbox({
				boxLabel:'源文大小',
				value:'3',
				name:'checkFilesize',
				checked: true,
				style: 'margin-top: 3px;',
				id:'checkFilesize'
			});
			
			//资源类型
			var param_filetype = new Ext.form.Checkbox({
				boxLabel:'源文格式',
				value:'4',
				name:'checkFiletype',
				checked: true,
				style: 'margin-top: 3px;', 
				id:'checkFiletype'
			});
			
			
			//资源类型
			var param_subject = new Ext.form.Checkbox({
				boxLabel:'主题',
				value:'5',
				name:'checkSubject',
				id:'checkSubject'
			});
			
			//描述
			var param_description = new Ext.form.Checkbox({
				boxLabel:'描述',
				value:'6',
				width: 55,
				name:'checkDescription',
				id:'checkDescription'
			});
			
			//来源
			var param_source = new Ext.form.Checkbox({
				boxLabel:'来源',
				value:'7',
				name:'checkSource',
				id:'checkSource'
			});
			
			//语种
			var param_language = new Ext.form.Checkbox({
				boxLabel:'语种',
				value:'8',
				name:'checkLanguage',
				id:'checkLanguage'
			});
			
			//关联
			var param_relation = new Ext.form.Checkbox({
				boxLabel:'关联',
				value:'9',
				name:'checkRelation',
				id:'checkRelation'
			});
			
			//覆盖范围
			var param_coverage = new Ext.form.Checkbox({
				boxLabel:'覆盖范围',
				value:'10',
				name:'checkCoverage',
				id:'checkCoverage'
			});
			
			//创建者
			var param_creator = new Ext.form.Checkbox({
				boxLabel:'创&nbsp;建&nbsp;者',
				value:'11',
				name:'checkCreator',
				checked: true,
				id:'checkCreator'
			});
			
			//其它责任者
			var param_contributor = new Ext.form.Checkbox({
				boxLabel:'其它责任者',
				value:'12',
				name:'checkContributor',
				id:'checkContributor'
			});
			
			//权限
			var param_rights = new Ext.form.Checkbox({
				boxLabel:'权限',
				value:'13',
				name:'checkRights',
				id:'checkRights'
			});
			
			//日期
			var param_date = new Ext.form.Checkbox({
				boxLabel:'日期',
				value:'14',
				name:'checkDate',
				id:'checkDate'
			});
			
			//类型
			var param_type = new Ext.form.Checkbox({
				boxLabel:'类型',
				value:'15',
				name:'checkType',
				id:'checkType'
			});
			
			//格式
			var param_format = new Ext.form.Checkbox({
				boxLabel:'格式',
				value:'16',
				name:'checkFormat',
				id:'checkFormat'
			});
			
			//标识符
			var param_identifier = new Ext.form.Checkbox({
				boxLabel:'标识符',
				value:'17',
				name:'checkIdentifier',
				id:'checkIdentifier'
			});
			
			//基本查重条件复选组
			var myCheckboxGroup = new Ext.form.CheckboxGroup({
			    id:'basicCondition',
			    xtype: 'checkboxgroup',
			    height:20,
				width:280,
			    items: [
			        param_Title,
			        param_creator,
			        param_filesize, 
			        param_filetype
			    ]
			});
			
			//更多查重条件复选组
			var myCheckboxGroup1 = new Ext.form.CheckboxGroup({
			    id:'moreCondition',
			    xtype: 'checkboxgroup',
			    height:20,
				//autoWidth: true,
				width: 720,
			    items: [
			        param_subject,
			        param_description,
			        param_source,
			        param_language,
			        param_relation,
			        param_publisher,
			        param_contributor,
			        param_rights,
			        param_date,
			        param_type,
			        param_format,
			        param_identifier,
			        param_coverage
			    ]
			});
			
			
						
			//是否显示所有条件查重结果
			var param_showAll = new Ext.form.Checkbox({
				boxLabel:'分类显示',
				value:'1',
				style: 'margin-top: 0px;', 
				name:'showAll',
				id:'showAll'
			});
			
			var tbar2 = new Ext.Toolbar({height :28,items:[
				"查重条件:&nbsp; ",myCheckboxGroup,"&nbsp;"
				,
				{
							text: '更多', 
							enableToggle: false, 
							iconCls:'tool_more',
							style: 'margin-top: 1px;', 
							handler: function(){
								var id = tbar3.id;
								
								if(document.getElementById(id).style.display == "none" || document.getElementById(id).style.display == ""){
									document.getElementById(id).style.display = "block";
								}else{
									document.getElementById(id).style.display = "none";
								}
							} 
				},
				'-', ' ',param_showAll,' '
				,'-',
				{ 
							text: '开始查重', 
							enableToggle: false, 
							iconCls:'chachong', 
							style: 'margin-top: 1px;', 
							handler: checkRepeatStart 
						}
			]});
			
			var tbar3 = new Ext.Toolbar({
			      height :28,
			      //hidden: true,
			      items:[
				  	 myCheckboxGroup1
				  ]
			});
						
			
			var sm = new Ext.grid.CheckboxSelectionModel({handleMouseDown: Ext.emptyFn}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				sm,
				{header:'题名', dataIndex:'RSDA_TITLE', width:210, sortable:true},
				{header:'主题', dataIndex:'RSSU_NAME', width:120, sortable:true}, 
				{header:'创建者', dataIndex:'RSDA_CREATOR', width:120, sortable:true}, 
				{header:'出版者', dataIndex:'RSDA_PUBLISHER', width:210, sortable:true},
				{header:'录入时间', dataIndex:'RSDA_BUILDDATE', width:120, sortable:true},
				{header:'状态', dataIndex:'RSDA_STATUS',renderer : checkState, width:75, align:'center', sortable:true} 
			]); 
			
			// 状态
			function checkState(val) {
				if(val == '2')
					return '<img src="<%=path %>/images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
				else if(val == '3')
					return '<img src="<%=path %>/images/res_minfo/res_pass.png" title="审核通过" width="16" height="16" />';
				else
					return '<img src="<%=path %>/images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
			}
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'RSDA_TITLE'}, 
				    {name:'RSSU_NAME'},
				    {name:'RSDA_SUBJECT'}, 
					{name:'RSDA_MIDDLENAME'}, 
					{name:'RSDA_BUILDDATE'}, 
					{name:'RSDA_STATUS'}, 
					{name: 'RSDA_ID'}, 
					{name: 'RSDA_CREATOR'},
					{name: 'RSDA_PUBLISHER'},
					{name: 'RSLA_RLTY_ID'},
				]),
				sortInfo : {
					field : 'RSDA_BUILDDATE',
					direction : 'ASC'
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
			
			
			var tbar1 = new Ext.Toolbar({
			      items:[ 
						'题名：', rsdaTitle, 
						{
							text : '&nbsp;查&nbsp;询&nbsp;',
							iconCls : 'common_search',
							handler : searchMan
						},'-',{ 
							text: '&nbsp;返&nbsp;回&nbsp;', 
							enableToggle: false, 
							iconCls:'common_back', 
							handler: function(){ 
								window.location.href = "<%=path %>/console/data/inputSingle.htm?action=missionResourceList&rmin_id=${rminId}";
							} 
						}
					] 
			});
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
			var grid = new Ext.grid.GridPanel({ 
				height: document.documentElement.clientHeight, 
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
				tbar: tbar1, 
				bbar: new Ext.PagingToolbar({  //分页信息栏 
					pageSize: pageNum, 
					store: grid_store, 
					displayInfo: true, 
					displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>', 
					emptyMsg: '没有记录', 
					plugins: new Ext.ux.ProgressBarPager(),
					items : [{
						xtype : 'label',
						style : 'padding-left: 10px;',
						width : 10
					}, {
						xtype : 'label',
						style : 'padding-left: 16px;',
						cls : 'res_nocheck',
						width : 16
					}, {
						xtype : 'label',
						text : '待审核',
						style : 'padding-left: 3px;',
						width : 10
					}, {
						xtype : 'label',
						style : 'padding-left: 15px;',
						width : 10
					}, {
						xtype : 'label',
						style : 'padding-left: 16px;',
						cls : 'res_pass',
						width : 16
					}, {
						xtype : 'label',
						text : '审核通过',
						style : 'padding-left: 3px;',
						width : 10
					}, {
						xtype : 'label',
						style : 'padding-left: 15px;',
						width : 10
					}, {
						xtype : 'label',
						style : 'padding-left: 16px;',
						cls : 'res_nopass',
						width : 16
					}, {
						xtype : 'label',
						text : '审核未通过',
						style : 'padding-left: 3px;',
						width : 10
					}] 
				}) ,
				listeners : {
					'render' : function() {
						tbar2.render(this.tbar); 
						tbar3.render(this.tbar); 
					}
				} 
			}); 
			

			//获取所有选中的数据ID
			function getSelectId(){
			    var rows = grid.getSelectionModel().getSelections();
			    if(rows.length<1){
			        return "";
			    }
			    var ids = "0";
                for(var i=0; i<rows.length; i++){
                    ids +="," + rows[i].get("RSDA_ID");
                }
                return ids;
			}			
			
			/**
			 * 查重开始。如果用户选了资源，则查已选择的资源，否则查重整个作业所有资源。
			 */
			function checkRepeatStart(){
				
				//查重条件
				var checkStr = "";  
 				var myGroups=Ext.getCmp("basicCondition");  //获取复选项的集合
 				var myGroups1=Ext.getCmp("moreCondition");  //获取复选项的集合
 				myGroups.eachItem(function(item){
 					if(item.checked == true){
 						if(checkStr == ""){
 							checkStr =item.value;
 						}else{
 							checkStr+=","+item.value;
 						}
 						
 					}
 				});
 				myGroups1.eachItem(function(item){
 					if(item.checked == true){
 						if(checkStr == ""){
 							checkStr =item.value;
 						}else{
 							checkStr+=","+item.value;
 						}
 						
 					}
 				});
 				if(checkStr==""){
 					Ext.Msg.alert("温馨提示","请选择查重条件");
 					return false;
 				}
 				
 				 //查重范围
 				 /**
 				var srStr =""; 
				var sr=Ext.getCmp('selectResource');
				 sr.eachItem(function(item){
				      if(item.checked===true){
        				srStr = item.inputValue;
  				 	  }

 				});**/
 				var showall = param_showAll.getValue();
 				var checkUrl = "<%=path %>/console/data/resource/checkRepeat.htm?action=checkRepeatByResource&rminId=${rminId}&rsdaId=" + getSelectId();
				
				checkUrl+="&cctj="+checkStr;
				checkUrl+="&showall="+showall;
				window.location.href = checkUrl;
				
 				Win.show(230,110,'progress','查重进度');
				updateProgressCompleted();
			}
			
			function updateProgressCompleted(){
				var url_progress = "<%=path %>/console/data/resource/checkRepeat.htm?action=getCompleted";
				Ext.Ajax.request({ 
					url:url_progress,
					success: function(response, options) {
						var w = response.responseText;
						document.getElementById("successNum").innerHTML = w/2;
						document.getElementById("completedLine").style.width = w + "px"; 
						setTimeout(updateProgressCompleted, 1000);
					}  
				}); 
				
			}
			
			function endRepeat(){
				Ext.Msg.confirm("确认消息框", "查重还未完成，您确定要取消剩下的资源查重吗？\n", function(btn, txt) {
					if(btn == 'yes') {
						var url_progress = "<%=path %>/console/data/resource/checkRepeat.htm?action=endRepeat";
						Ext.Ajax.request({ 
							url:url_progress
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
				
				var id = tbar3.id;
				document.getElementById(id).style.display = "none";
			}) 

		</script> 
	
  </head>
  
  <body>
    <div id="grid" ></div> 
    
    <div id="progress" style="display: none;">
    	<table style="width: 205px;padding-left: 5px;" border="0px">
    		<tr style="height: 25px;">
    			<td colspan="2" style="padding-left: 5px;"><span id="messageInfo" class="messageInfo">资源查重中...</span></td>
    		</tr>
    		
    		<tr style="height: 20px;">
    			<td colspan="2" style="padding-left: 5px;">
    				<div class="progressLine">
    					<div id="completedLine" class="completedLine" style="height: 14px;"></div>
    				</div>
    			</td>
    		</tr>
    		<tr style="font-size: 12px;">
    			<td align="left" style="padding-left: 5px;">
    					已完成：<span id="successNum" style="color: blue">0</span>/100
    			</td>
    			<td align="right">
		    		<a href="javascript:endRepeat();"><img src="<%=path %>/images/stop_btn.gif" border="0" style="height: 20px;"/></a>
		    	</td>
    		</tr>
    	</table>
    </div>
    
    <!-- 处理结果 -->
    ${requestScope.result }
  </body>
</html>
