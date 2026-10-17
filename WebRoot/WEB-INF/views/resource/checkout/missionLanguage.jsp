<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissDevolve"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissionLanguage"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResMissionInfo"%>
<%
	String path = request.getContextPath();
	long rminId = MyRequestUtil.getLongAttribute(request,"rminId");
	long loginUser = MyRequestUtil.getLoginUserId(request);
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
			var url_data_grid = '<%=path %>/console/data/resource/missionLanguage.htm?action=missionLanguageCheckedListJson&rminId=<%=rminId %>'; 
			var url_sysMan_store = '<%=path %>/console/data/resource/missionInfoManagement.htm?action=getSysManagerList&missiontype=<%=ResMissDevolve.MISSION_XIAOYAN %>';
			
			var sm = new Ext.grid.RowSelectionModel({singleSelect : true}); //定义复选框列 
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(), 
				{header:'语种', dataIndex:'languageName'},
				{header:'审核人', dataIndex:'assessorName', renderer: function(v){
				    if(v != null && v.length > 0){
				        return v;
				    }else{
				        return "<%=ConstantsObj.noAuthor %>";
				    }
				}},
				{header:'完成时间', dataIndex:'assetime'}, 
				{header:'未审核数', dataIndex:'sourceNum'},
				{header:'状态', dataIndex:'statu', align:'center', renderer: function(v){
				    if(v == <%=ResMissionLanguage.STATU_CHECKING %>){ //待审核
				       return "<img src='<%=path %>/images/res_minfo/res_nocheck.png' >";
				    }if(v == <%=ResMissionLanguage.STATU_CHECK_FAIL %>){  //审核未通过
				       return "<img src='<%=path %>/images/res_minfo/res_nopass.png' >";
				    }if(v == <%=ResMissionLanguage.STATU_CHECK_SUCCESS %>){ //审核通过
				       return "<img src='<%=path %>/images/res_minfo/res_pass.png' >";
				    }if(v == <%=ResMissionLanguage.STATU_INTERPRETING %>){ //转交中
				       return "<img src='<%=path %>/images/res_minfo/res_deliver.png' >";
				    }else{
				       return v;
				    }
				    
				}}
			]); 
			
			var grid_store = new Ext.data.Store({ 
				proxy: new Ext.data.HttpProxy({url:url_data_grid}), 
				reader: new Ext.data.JsonReader({ 
					totalProperty: 'totalProperty', 
					root: 'root' 
				},[ 
				    {name:'languageName'}, 
				    {name:'assessorName'}, 
				    {name:'assetime'},
				    {name:'sourceNum'},
				    {name:'statu'}, 
				    {name:'assessor'}, 
				    {name:'isfirst'}, 
				    {name:'rmlaId'}, 
				    {name:'languageId'}, 
				]) 
			}); 
			
			grid_store.load({params:{start:0,limit:pageNum}}); 
			
			var grid = new Ext.grid.GridPanel({ 
				//height: document.documentElement.clientHeight-2, 
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				//style: 'margin-top:1px;', 
				loadMask: true,     //加载时提示load... 
				border : false,
				frame : false,
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
							text: '我来审核', 
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
							text: '提交完成', 
							enableToggle: false, 
							iconCls:'common_submit', 
							handler: function(){ 
								 resMissionInfoCheck();
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
								window.location.href="<%=path %>/console/data/otherLanguageMissionInfo.htm?action=initOtherLanguageMissionInfo";
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
					plugins: new Ext.ux.ProgressBarPager(),
					items : [ 
						 {
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
			grid.on('dblclick', toSourceList);
			
			//接受任务
			function getMission(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow){
				   var mission_id = <%=rminId %>;
				     var assessor = selectRow.get("assessor");
				     var isfirst = selectRow.get("isfirst");
				     if(isfirst == 0){
				         isfirst = 4;
				     }if(isfirst == 1){
				         isfirst = 3;
				     }
				     if(assessor != null && assessor>0){
				         Ext.Msg.alert("提示","该作业已有人加工，不能再次领取!");
				         return;
				     }
				     
				     var lanId = selectRow.get("languageId");
				     Ext.Msg.confirm('提示', '您确定要领取该作业的审核任务吗？\n', function(button, text) {
							if(button == 'yes') {
								Ext.Ajax.request({
									url : "<%=path %>/console/data/resource/missionLanguage.htm?action=getCheckedMission&userid=<%=loginUser%>&missionid=<%=rminId %>&languageId=" + lanId + "&model=" + isfirst,
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
			
			// 作业校验
			var resMissionInfoCheck_RminMissName = new Ext.form.TextField({
				fieldLabel : '作业名称',
				id : 'rminMissName',
				name : 'rminMissName',
				anchor : '95%'
			});
			var resMissionInfoCheck_RminStatus = new Ext.form.RadioGroup({
				id : 'resMissionInfoCheck_RminStatus',
				fieldLabel : '审核结果',
				name : 'rminStatus',
				anchor : '80%',
				items : [{
					boxLabel : '通过',
					name : 'rminStatus',
					inputValue : 3,
					checked : true
				}, {
					boxLabel : '不通过',
					name : 'rminStatus',
					inputValue : 4
				}]
			});
			var resMissionInfoCheck_Form = new Ext.form.FormPanel({
				id : 'resMissionInfoCheck_Form',
				border : false,
				frame : false,
				waitMsgTarget : true,
				labelAlign : 'right',
				bodyStyle : 'background-color:#dfe8f6;padding:10px 0px 0',
				labelWidth : 80,
				buttonAlign : 'center',
				defaulttype : 'textfield',
				items : [resMissionInfoCheck_RminMissName, resMissionInfoCheck_RminStatus],
				buttons : [{
					text : '保&nbsp;存',
					handler : resMissionInfoCheck_Form_Submit
				}, {
					text : '取&nbsp;消',
					handler : function() {
						resMissionInfoCheck_Win.hide();
					}
				}]
			});
			var resMissionInfoCheck_Win = null;
			function resMissionInfoCheck() {
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow) {
					var missionUser = selectRow.get("assessor");
					var sourceNum = selectRow.get("sourceNum");
					if(missionUser != <%=loginUser %>) {
						Ext.Msg.alert("错误", "该作业现不属于您审核，您不能提交!");
						return;
					}
					if(sourceNum>0){
						Ext.Msg.alert("错误", "该作业有未审核的资源，暂不能提交!");
						return;
					}
					if(!resMissionInfoCheck_Win) {
						resMissionInfoCheck_Win = new Ext.Window({
							closeAction : 'hide',
							maximizable : false,
							width : 300,
							height : 150,
							title : '完成',
							layout : 'fit',
							modal : true,
							buttonAlign : 'center',
							items : [resMissionInfoCheck_Form]
						});
					}
					initResMissionInfoCheckData();
					resMissionInfoCheck_Win.show();
				}else
					alertWarring('请至少选择一行数据然后才能够操作！');
			}
			// 初始化状态数据
			function initResMissionInfoCheckData() {
				var selectRow = grid.getSelectionModel().getSelected();
				resMissionInfoCheck_RminMissName.setValue(selectRow.get("languageName"));
				resMissionInfoCheck_RminMissName.disable();
				
				if(selectRow.json.statu == 2 || selectRow.json.statu == 4)
					resMissionInfoCheck_RminStatus.setValue(4);
				else
					resMissionInfoCheck_RminStatus.setValue(3);
			}
			// 提交resMissionInfoCheck_Form信息
			function resMissionInfoCheck_Form_Submit() {
				resMissionInfoCheck_Form.getForm().submit({
					url : '<%=path %>/console/data/rmsMissionInfoCheck.htm?action=checkedMissionLanguage',
					method : 'post',
					params : {
						missLanId : grid.getSelectionModel().getSelected().get("rmlaId"),
						rminId : <%=rminId %>,
						isfirst: "false"
					},
					waitMsg : '正在保存数据，请稍候...',
					success : function(form, action) {
						// 提示信息
						alertSuccess(action.result.msg);
						// 刷新列表
						grid_store.reload();
						// 隐蔽窗口
						resMissionInfoCheck_Win.hide();
					},
					failure : function(form, action) {
						alertError(action.result.msg);
					}
				});
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
			
			function missionInfoChange() {
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow) {
			       var missionStatu = selectRow.get("statu");
						if(missionStatu == <%=ResMissionInfo.RMINSTATUS_COMMIT_PASS %>){
						    Ext.Msg.alert('错误', "<span style='color:red'>该作业已完成,不能转交！</span>");
						    return;
						}
					var assessor = selectRow.get("assessor");
					    if(assessor != <%=loginUser %>){
					        Ext.Msg.alert("错误","该作业不属于您审核，您不能进行转交!");
					        return;
					    }	
					if(missionStatu == <%=ResMissionInfo.RMINSTATUS_DELIVERING %> ){
				        Ext.Msg.alert("错误","该作业处于转交请求中，不能重复转交!");
				    	return;
				    }    
					var rminId = <%=rminId %>;
					var rmstId = selectRow.json.rmstId;
					var userid = selectRow.json.assessor;
					// 加载操作者数据
					rminAdd_SysMan_store.proxy = new Ext.data.HttpProxy({
						url : url_sysMan_store + "&rminId=" + rminId + "&userid=" + userid
					});
					rminAdd_SysMan_store.load();
					handlerMissionInfoChange();
				}else
					alertWarring('请至少选择一行数据然后才能够操作！');
			}
			
			function saveRminChange() {
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow) {
					if(rminChange_Form.form.isValid()) {
						var rminId = selectRow.get("rmlaId");
						rminChange_Form.getForm().submit({
							url : '<%=path %>/console/data/resource/missionInfoManagement.htm?action=saveMissionLanguageChange&missiontype=<%=ResMissDevolve.MISSION_OTHERCHECK%>',
							method : 'post',
							params : {
								missId : rminId,
								preUid: selectRow.get("assessor"),
								afterUid: document.getElementById("smanId").value,
								isfirst: "false",
								missionId: <%=rminId %>,
								missiontype: <%=ResMissDevolve.MISSION_OTHERCHECK %>,
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
			// 进入资源列表
			function toSourceList(){
				var selectRow = grid.getSelectionModel().getSelected();
				if(selectRow) {
					document.location.href = "<%=path %>/console/data/otherLanguageMissionInfo.htm?action=initOtherLanguageRmsMissionInfoDataCheck&rmlaId=" + selectRow.json.rmlaId;
				}else
					alertWarring('请至少选择一行数据然后才能够操作！');
			}
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