<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissDevolve"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissionLanguage"%>
<%@page import="org.springframework.web.bind.ServletRequestUtils"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissionInfo"%>
<%
	String path = request.getContextPath();
	long rminId = MyRequestUtil.getLongAttribute(request,"rminId");
	long loginUser = MyRequestUtil.getLoginUserId(request);
	long sourceCount = MyRequestUtil.getLongAttribute(request,"sourceCount");
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
			var url_data_grid = '<%=path %>/console/data/resource/missionLanguage.htm?action=missionLanguageListJson&usercolumn=RMLA_PROCESSOR&rminId=<%=ServletRequestUtils.getLongParameter(request,"rminId",0) %>'; 
			var url_sysMan_store = '<%=path %>/console/data/resource/missionInfoManagement.htm?action=getSysManagerList&missiontype=<%=ResMissDevolve.MISSION_JIAGONG %>';
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'语种', dataIndex:'RLTY_NAME'},
				{header:'翻译人', dataIndex:'SMAN_NAME',renderer: function(v){
				    if(v == null || v.length < 1){
				        return "<%=ConstantsObj.noAuthor %>";
				    }else{
				        return v;
				    }
				}},
				{header:'完成时间', dataIndex:'RMLA_PROCTIME'}, 
				{header:'未翻译数', dataIndex:'INTERPRETNUM', renderer: function(v){
				    try{
				        return <%=sourceCount %> - v;
				    }catch(e){
				    	return "";
				    }
				}}, 
				{header:'状态', dataIndex:'RMLA_STATUS', align:'center', renderer: function(v){
				    if(v == <%=ResMissionLanguage.STATU_PROCESSING %>){ //加工中
				       return "<img src='<%=path %>/images/res_minfo/res_nocommit.png' >";
				       //return "加工中";
				    }if(v == <%=ResMissionLanguage.STATU_CHECKING %>){  //待审核
				       return "<img src='<%=path %>/images/res_minfo/res_nocheck.png' >";
				       //return "待审核";
				    }if(v == <%=ResMissionLanguage.STATU_CHECK_SUCCESS %>){ //审核成功
				       return "<img src='<%=path %>/images/res_minfo/res_pass.png' >";
				       //return "审核通过";
				    }if(v == <%=ResMissionLanguage.STATU_CHECK_FAIL %>){ //审核未通过
				       return "<img src='<%=path %>/images/res_minfo/res_nopass.png' >";
				       //return "审核未通过";
				    }if(v == <%=ResMissionLanguage.STATU_INTERPRETING %>){ //转交中
				       return "<img src='<%=path %>/images/res_minfo/res_deliver.png' >";
				       //return "转交申请中";
				    }else{
				       return "未加工";
				    }
				    
				}}
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'RLTY_NAME'}, 
				    {name:'SMAN_NAME'}, 
				    {name:'RMLA_PROCTIME'},
				    {name:'RMLA_STATUS'}, 
				    {name:'RMLA_RMIN_ID'},  //作业ID
				    {name:'RMLA_PROCESSOR'}, //加工者ID
				    {name:'INTERPRETNUM'}, //已加工数
				    {name:'RLTY_ID'}, //语种ID
				    {name:'RMLA_ID'}, //作业语种ID
				]) 
			}); 
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
			
			var missionInfo = "作业：【${requestScope.missionName}】";
			var grid = new Ext.grid.GridPanel({ 
				// height: document.documentElement.clientHeight,
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				// style: 'margin-top:1px;', 
				loadMask: true,     //加载时提示load...
				frame : false,
				border: false, 
				viewConfig: { 
					forceFit: true 
				}, 
				cm: cm, 
				sm: sm, 
				autoScroll : true,
				region : 'center',
				tbar: new Ext.Toolbar({  //工具栏 
					items:[ 
						{ 
							text: '我来翻译', 
							enableToggle: false, 
							iconCls:'common_flag', 
							handler: function(){ 
								 getMission();
							} 
						},'-',{ 
							text: '查看资源', 
							enableToggle: false, 
							iconCls:'common_list', 
							handler: function(){ 
								 toSourceList();
							} 
						},'-',{ 
							text: '提交审核', 
							enableToggle: false, 
							iconCls:'common_submit', 
							handler: function(){ 
								 submitCheckout();
							} 
						},'-',{ 
							text: '任务转交', 
							enableToggle: false, 
							iconCls:'common_go', 
							handler: function(){ 
								 missionInfoChange();
							} 
						},'-',{ 
							text: '&nbsp;返&nbsp;回&nbsp;', 
							enableToggle: false, 
							iconCls:'common_back', 
							handler: function(){ 
								window.location.href = "<%=path %>/console/data/inputSingle.htm?action=doInterpretMission"
							} 
						},'-',{
							xtype: 'tbtext', 
							text: missionInfo
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
					items : [ 
						 {
							xtype : 'label',
							style : 'padding-left: 15px;',
							width : 10
						},{
							xtype : 'label',
							style : 'padding-left: 16px;',
							cls : 'res_nocommit',
							width : 16
						}, {
							xtype : 'label',
							text : '加工中',
							style : 'padding-left: 3px;',
							width : 10
						}, {
							xtype : 'label',
							style : 'padding-left: 15px;',
							width : 10
						},{
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
						}, {
							xtype : 'label',
							style : 'padding-left: 15px;',
							width : 10
						}, {
							xtype : 'label',
							style : 'padding-left: 16px;',
							cls : 'res_dliver',
							width : 16
						}, {
							xtype : 'label',
							text : '转交中',
							style : 'padding-left: 3px;',
							width : 10
						}] 
				}) 
			}); 
			
			grid.on('dblclick', toSourceList); //双击进入作业编辑
			function toSourceList(){
			    var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				   var mission_id = <%=rminId %>;
				     var lanId = selectRow.get("RLTY_ID");
				     var processor = selectRow.get("RMLA_PROCESSOR");
				     var statu = selectRow.get("RMLA_STATUS");
				    if(processor != <%=loginUser %>){
				        Ext.Msg.alert("错误","您没有该作业的加工权限!");
				        return;
				    }
				    window.location.href = "<%=path %>/console/data/inputSingle.htm?action=interpretResourceList&rminStatu="+statu+"&rmin_id="+mission_id+"&lanId=" + lanId;
				   
				}else{
					Ext.Msg.alert("提示","请点击选择作业语种!");
				}
			}
			
			//接受任务
			function getMission(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				   var mission_id = <%=rminId %>;
				     var processor = selectRow.get("RMLA_PROCESSOR");
				     if(processor != null && processor>0){
				         Ext.Msg.alert("提示","该作业已有人加工，不能再次领取!");
				         return;
				     }
				     
				     var lanId = selectRow.get("RLTY_ID");
				     Ext.Msg.confirm('提示', '您确定要领取该作业的翻译任务吗？\n', function(button, text) {
							if(button == 'yes') {
								Ext.Ajax.request({
									url : "<%=path %>/console/data/resource/missionLanguage.htm?action=saveMission&statu=1&userid=<%=loginUser%>&missionid=<%=rminId %>&languageId=" + lanId,
									waitTitle : "数据提交",
									waitMsg : "作业提交中，请稍候...",
			
									success : function(form, action) {
										Ext.Msg.alert('信息', '任务接受成功！');
										window.location.reload();
									},
									failure : function(form, action) {
										Ext.Msg.alert('错误', "<span style='color:red'>对不起，操作失败！</span>");
									}
								});
							}
						})
				   
				}else{
					Ext.Msg.alert("提示","请点击选择作业!");
				}
			}
			
			//提交校验
			function submitCheckout(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				    var lanId = selectRow.get("RLTY_ID");
				    var missionUser = selectRow.get("RMLA_PROCESSOR");
				    var missionStatu = selectRow.get("RMLA_STATUS");
				    var missLanId = selectRow.get("RMLA_ID");
				    var interpertNum = selectRow.get("INTERPRETNUM");
				    if(missionStatu == '<%=ResMissionInfo.RMINSTATUS_COMMIT_PASS %>'){
				    	Ext.Msg.alert("错误","该作业已完成，不能再提交!");
				    	return;
				    }
				    if(missionStatu == '<%=ResMissionInfo.RMINSTATUS_COMMIT_NOTCHECK %>'){
				    	Ext.Msg.alert("错误","该作业已提交!");
				    	return;
				    }
				    if(missionUser != <%=loginUser %>){
				        Ext.Msg.alert("错误","该作业现不属于您加工，您不能提交审核!");
				    	return;
				    }
				    if(missionStatu == <%=ResMissionInfo.RMINSTATUS_DELIVERING %> ){
				        Ext.Msg.alert("错误","该作业处于转交请求中，暂不能提交!");
				    	return;
				    }
				    if(<%=sourceCount %> - interpertNum > 0){
				    	Ext.Msg.alert("错误","该作业有未翻译的资源，暂不能提交!");
				    	return;
				    }
				    Ext.Msg.confirm('提示', '您确定已加工完该作业并提交吗？\n', function(button, text) {
						if(button == 'yes') {
							Ext.Ajax.request({
								url : "<%=path %>/console/data/resource/missionInfoManagement.htm?action=setMissionLanguageStatus&statu=<%=ResMissionInfo.RMINSTATUS_COMMIT_NOTCHECK %>&missId=" + missLanId,            
								waitTitle : "数据提交",
								waitMsg : "作业提交中，请稍候...",
		
								success : function(form, action) {
									Ext.Msg.alert('信息', '提交成功，请等待审核！');
									grid_store.reload()
								},
								failure : function(form, action) {
									Ext.Msg.alert('错误', '对不起，提交失败！');
								}
							});
						}
					})
				}else{
					Ext.Msg.alert("提示","请点击选择作业!");
				}
			}
					
			// ------------------------ 任务转交 ------------------------------//
			var rminAdd_SysMan_store = new Ext.data.Store({
				autoLoad : true,
				proxy : new Ext.data.HttpProxy({
					url : url_sysMan_store
				}),
				reader : new Ext.data.ArrayReader({}, [{
					name : 'smanId'
				}, {
					name : 'smanName'
				}])
			});
			var rminChange_RminUserName_Combox = new Ext.form.ComboBox({
				fieldLabel : "接收者",
				mode : 'remote',
				store : rminAdd_SysMan_store,
				hiddenName : 'smanId',
				emptyText : '请选择接收者',
				triggerAction : 'all',
				lazyInit : true,
				valueField : 'smanId',
				displayField : 'smanName',
				editable : false,
				anchor : '80%',
				allowBlank : false,
				blankText : '任务接收者不能为空！'
			});
			var rminChange_Form = new Ext.form.FormPanel({
				id : 'rminChange_Form',
				border : false,
				frame : false,
				waitMsgTarget : true,
				labelAlign : 'right',
				bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
				labelWidth : 80,
				buttonAlign : 'center',
				defaulttype : 'textfield',
				items : [rminChange_RminUserName_Combox],
				buttons : [{
					text : '保&nbsp;存',
					handler : saveRminChange
				}, {
					text : '取&nbsp;消',
					handler : function() {
						rminChange_Win.hide();
					}
				}]
			});
	
	
			function missionInfoChange() {
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow) {
			       var missionStatu = selectRow.get("RMLA_STATUS");
						if(missionStatu == <%=ResMissionInfo.RMINSTATUS_COMMIT_NOTCHECK %>){
						    Ext.Msg.alert('错误', "<span style='color:red'>该作业已提交,不能转交！</span>");
						    return;
						}
						if(missionStatu == <%=ResMissionInfo.RMINSTATUS_COMMIT_PASS %>){
						    Ext.Msg.alert('错误', "<span style='color:red'>该作业已完成,不能转交！</span>");
						    return;
						}
					var processor = selectRow.get("RMLA_PROCESSOR");
					    if(processor != <%=loginUser %>){
					        Ext.Msg.alert("错误","该作业不属于您加工，您不能进行转交!");
					        return;
					    }	
					if(missionStatu == <%=ResMissionInfo.RMINSTATUS_DELIVERING %> ){
				        Ext.Msg.alert("错误","该作业处于转交请求中，不能重复转交!");
				    	return;
				    }    
					var rminId = <%=rminId %>;
					var rmstId = selectRow.json.rmstId;
					// 加载操作者数据
					rminAdd_SysMan_store.proxy = new Ext.data.HttpProxy({
						url : url_sysMan_store + "&rminId=" + rminId + "&userid=" + selectRow.get("RMLA_PROCESSOR")
					});
					rminAdd_SysMan_store.load();
					handlerMissionInfoChange();
				}else
					alertWarring('请至少选择一行数据然后才能够操作！');
			}
			
			var rminChange_Win = null;
			function handlerMissionInfoChange() {
				if(!rminChange_Win) {
					rminChange_Win = new Ext.Window({
						closeAction : 'hide',
						resizable : false,
						width : 300,
						height : 125,
						title : '任务转交',
						layout : 'fit',
						modal : true,
						buttonAlign : 'center',
						items : [rminChange_Form],
						listeners : {
							hide : function() {
								rminChange_Form.form.reset();
								rminChange_RminUserName_Combox.clearValue();
								rminAdd_SysMan_store.removeAll();
							}
						}
					});
				}
				rminChange_Win.show();
			}
			function saveRminChange() {
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow) {
					if(rminChange_Form.form.isValid()) {
						var rminId = selectRow.get("RMLA_ID");
						rminChange_Form.getForm().submit({
							url : '<%=path %>/console/data/resource/missionInfoManagement.htm?action=saveMissionLanguageChange',
							method : 'post',
							params : {
								missId : rminId,
								preUid: selectRow.get("RMLA_PROCESSOR"),
								afterUid: document.getElementById("smanId").value,
								missiontype: <%=ResMissDevolve.MISSION_FANYI %>,
								remark: ""
							},
							waitMsg : '正在保存数据，请稍候...',
							success : function(form, action) {
								// 提示信息
								alertSuccess(action.result.msg);
								// 刷新列表
								grid_store.reload();
								rminChange_Win.hide();
							},
							failure : function(form, action) {
								alertError(action.result.msg);
							}
						});
					}
				}else
					alertWarring('请至少选择一行数据然后才能够操作！');
			}
			// ------------------------ 任务转交结束 ------------------------------// 
			///**********************加载是将元素渲染到页面****************************// 
			Ext.onReady(function(){ 
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