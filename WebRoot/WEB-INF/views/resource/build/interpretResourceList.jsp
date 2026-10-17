<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissionInfo"%>
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%
	String path = request.getContextPath();
	long rminId = MyRequestUtil.getLongAttribute(request,"rminId");
	long rminStatu = MyRequestUtil.getIntParameter(request,"rminStatu");
	//long userid = MyRequestUtil.getLongAttribute(request,"userid");
	//long loginuser = MyRequestUtil.getLoginUserId(request);
	long languageId = ServletRequestUtils.getLongParameter(request,"lanId",0);
%>
<%@ include file="../../common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
  <head>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
	
	
	<script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			var url_data_grid = '<%=path %>/console/data/resource/resourceManager.htm?action=interpretSourceList&RSDA_RMIN_ID=<%=rminId %>&lanId=<%=languageId %>'; 
			
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'题名', dataIndex:'RSDA_TITLE',sortable : true},
				{header:'创建者', dataIndex:'RSDA_CREATOR',sortable : true},
				{header:'出版者', dataIndex:'RSDA_PUBLISHER',sortable : true}, 
				{header:'录入时间', dataIndex:'RSLA_DATE',sortable : true}, 
				{header:'加工状态', dataIndex:'RSLA_ID',align:'center',sortable : true,renderer:function(v){
				    if(v != null && v>0){
				        return "<img src='<%=path %>/images/icon/yes.png' title='已加工'>";
				    }else{
				    	return "<img src='<%=path %>/images/icon/no.png'  title='未加工'>";
				    }
				}},
				{header:'审核状态', dataIndex:'RSLA_STATUS',align:'center',sortable : true, renderer: checkState }  ////如果今后要考虑到对未通过的资源进一步加工，则可以直接启用该状态
			]); 
			// 状态
			function checkState(val) {
				if(val == '2' || val == 2)
					return '<img src="<%=path %>/images/res_minfo/res_nocheck.png" title="待审核" width="16" height="16" />';
				else if(val == '3' || val == 3)
					return '<img src="<%=path %>/images/res_minfo/res_pass.png" title="审核通过" width="16" height="16" />';
				else if(val == '4' || val == 4) return '<img src="<%=path %>/images/res_minfo/res_nopass.png" title="审核未通过" width="16" height="16" />';
			}
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'RSDA_TITLE'}, 
				    {name:'RSDA_CREATOR'}, 
					{name:'RSDA_PUBLISHER'}, 
					{name:'RSLA_DATE'}, 
					{name: 'RSLA_ID'},
					{name: 'RSDA_STATUS'}, 
					{name: 'RSLA_STATUS'}, 
					{name: 'RSDA_ID'}, 
					{name: 'RSLA_RLTY_ID'},
					{name: 'RSLA_ISFIRST'},
				]),
					sortInfo : { field : 'RSLA_DATE', direction : 'DESC' }, //默认排序
					remoteStore : true, // 是否远程调用数据
					remoteSort : true  // 是否远程排序
			}); 
			
			grid_store.on('beforeload', function() {
				Ext.apply(this.baseParams, {
					title : sourceTitle.getValue(),
					statu : source_statu_Combox.getValue()
				});
			});
			
			
			//---------------------检索条件-------------------------------//
			var sourceTitle = new Ext.form.TextField({
				fieldLabel : '题名',
				id : 'sourceTitle',
				name : 'sourceTitle',
				emptyText : '请输入题名',
				maxLength : 40,
				maxLengthText : '作业名称不得超过40个字符!',
				listeners : {
					specialKey :function(field,e){
			            if (e.getKey() == Ext.EventObject.ENTER){
			               searchMan();
			            } 
			        }
				}
			});
			
			var source_statu_store = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [[0, '不限'], [1, '已加工'], [2, '未加工']]

			});
			var source_statu_Combox = new Ext.form.ComboBox({
				fieldLabel : "加工状态",
				mode : 'local',
				store : source_statu_store,
				emptyText : '不限',
				triggerAction : 'all',
				editable : false,
				valueField : 'value',
				displayField : 'text',
				width : 80,
				value : 0,
				listeners : {
					'select' : function() {
						searchMan();
					}
				}
			});
	
			var rminInfo = "作业：【<a href='<%=path %>/console/data/resource/missionLanguage.htm?action=missionLanguageList&folder=build&rminId=<%=rminId %>' style='text-decoration: none;color:#000000'>${requestScope.rminName }</a>】   语言：【${requestScope.language.rltyName}】";
			grid_store.load({params:{start:0,limit:pageNum}}); 
			var grid = new Ext.grid.GridPanel({ 
				// height: document.documentElement.clientHeight,
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				// style: 'margin-top:1px;', 
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
				tbar: new Ext.Toolbar({  //工具栏 
					items:[ 
					  	 '题名：' ,sourceTitle, '-',
					  	 '加工状态：' ,source_statu_Combox, '-',
						 { 
							text: '&nbsp;查&nbsp;询&nbsp;', 
							enableToggle: false, 
							iconCls:'common_search', 
							handler: function(){ 
								searchMan();
							} 
						}, '-',{ 
							text: '&nbsp;翻&nbsp;译&nbsp;', 
							enableToggle: false, 
							iconCls:'common_edit', 
							handler: function(){ 
								fanyi();
							} 
						}, '-',
					   { 
							text: '&nbsp;返&nbsp;回&nbsp;', 
							enableToggle: false, 
							iconCls:'common_back', 
							handler: function(){ 
								window.location.href = "<%=path %>/console/data/resource/missionLanguage.htm?action=missionLanguageList&folder=build&rminId=<%=rminId %>";
							} 
						},'-',{
							xtype: 'tbtext', 
							text: rminInfo
						}
					] 
				}), 
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
				}) 
			}); 
			
			// 查询
			var searchMan = function() {
				grid_store.reload({
					params : {
						start : 0,
						limit : pageNum,
						title : sourceTitle.getValue(),
						statu : source_statu_Combox.getValue()
					}
				});
			}
			
			grid.on('dblclick', fanyi); //双击进入作业编辑
			
			function fanyi(){
			    var selectRow = grid.getSelectionModel().getSelected();
				var source_id = 0;
				if(selectRow){
				    if(<%=rminStatu == ResMissionInfo.RMINSTATUS_COMMIT_NOTCHECK %>){
				        Ext.Msg.alert("错误","该作业处于审核中，暂不能进行翻译!");
				        return;
				    }
				    if(<%=rminStatu == ResMissionInfo.RMINSTATUS_COMMIT_PASS %>){
				        Ext.Msg.alert("错误","该作业已审核通过，不能再进行加工!");
				        return;
				    }
				    if(<%=rminStatu == ResMissionInfo.RMINSTATUS_DELIVERING%>){
				        Ext.Msg.alert("错误","该作业处于转交中，暂不能进行翻译!");
				        return;
				    }
				    source_id = selectRow.get("RSDA_ID");
				    languageId = selectRow.get("RSLA_RLTY_ID");
				    var statu = selectRow.get("RSLA_ID");
				    if(statu != null && statu>0){  //如果已翻译，则进入编辑页面
				    	window.location.href = "<%=path %>/console/data/resource/resourceManager.htm?action=interpertSourceEdit&reqType=1&sid=" + source_id + "&languageId=<%=languageId %>&rminStatu=<%=rminStatu %>";
				    }else{  //如果未翻译则进入翻译页面
				    	window.location.href = "<%=path %>/console/data/inputSingle.htm?action=InterpretSourceForm&sourceId=" + source_id+"&languageId=<%=languageId %>&rminStatu=<%=rminStatu %>";
				    }
				}else{
					Ext.Msg.alert("提示","请点击选择需要翻译的资源!");
				} 
			}
			 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
				//Ext.QuickTips.init(); 
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
		
		<!-- 资源保存成功的图片提示 -->
        <div id="SuccessMessageBox" class="SuccessMessageBox"></div>
        ${requestScope.result }
	</body> 
</html>