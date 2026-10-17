<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
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
			var url_data_grid = '<%=path %>/console/data/resource/SourceManager.htm?action=resourceListJson'; 
			
			///////////////////////////查重属性设置//////////////////////
			
			//复选
			
			//题名
			var param_Title = new Ext.form.Checkbox({
			    boxLabel : '资源题名',
			    value:'1',
			    checked: true,
			    style: 'margin-top: 3px;',
			    name:'checkTitle',
			    id:'checkTitle'
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
				}
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
 				var myMask = new Ext.LoadMask(Ext.getBody(), {
						msg : '资源查重中...请稍后！',
						removeMask : true // 完成后移除
				});
				myMask.show();
				window.location.href = "<%=path %>/console/data/resource/systemCheckRepeat.htm?action=repeatStart&cctj="+checkStr;
			}
			
			
					
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
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'题名', dataIndex:'RSDA_TITLE', width:210,sortable: true},
				{header:'资源类型', dataIndex:'RLTY_NAME', width:60,sortable: true},
				{header:'特色库', dataIndex:'RMST_NAME', width:90,sortable: true},
				{header:'创建时间', dataIndex:'RSDA_BUILDDATE', width:90,sortable: true}
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
					rsdaTitle: rsdaTitle.getValue()
				}); 
			}); 
			
			function searchData(){
				grid_store.load({params:{
		            start:0,
		            limit:pageNum,
					rsdaTitle: rsdaTitle.getValue()
		        }}); 
			}
			var tbar1 = new Ext.Toolbar([
				'标题：',rsdaTitle,
				{ 
					text: '&nbsp;查&nbsp;询&nbsp;', 
					enableToggle: false, 
					iconCls:'common_search', 
					handler: searchData 
				} , '-',
				{ 
					text: '清除查重临时数据', 
					enableToggle: false, 
					iconCls:'lajixiang', 
					handler: clearRepeatTemp 
				} ,'-'
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
				bbar: new Ext.PagingToolbar({  //分页信息栏 
					pageSize: pageNum, 
					store: grid_store, 
					displayInfo: true, 
					displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>', 
					emptyMsg: '没有记录', 
					plugins: new Ext.ux.ProgressBarPager()					
				}) ,
				listeners : {
					'render' : function() {
						tbar2.render(this.tbar); 
						tbar3.render(this.tbar); 
					}
				}
			}); 
			
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				Ext.QuickTips.init(); 
				// 创建主面板
				var displayPanel = new Ext.Viewport({
					layout : 'border',
					items : [grid]
				}); 
				var id = tbar3.id;
				document.getElementById(id).style.display = "none";
			}) 

			///**********************2011-04-22添加***************************//
			
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
		</script> 
	</head> 
	<body> 
		<div id="grid"></div> 
	</body> 
</html>