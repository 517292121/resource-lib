<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<script type="text/javascript">
    var LIMIT = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>;
	Ext.onReady(function()
	{
		// 如果要体现FORM效验效果，必须添加下面两行代码
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';
		
		var managerEditForm = Ext.form;		
		var editWin;
		var selectRow;
		var checkRows;
		
		var reviewRecords = new Ext.data.Record.create([
			{name:'rscoId', type:'string'},
			{name:'rscoDate', type:'string'},
			{name:'rscoContent', type:'string'},
			{name:'rscoStatus', type:'string'},
			{name:'suinLoginName', type:'string'},
			{name:'suinName', type:'string'},
			{name:'tempContent',type:'string'}
		]);
		
		// 页面数据仓库
	    var store = new Ext.data.Store({
	        reader: new Ext.data.JsonReader({ //使用JSON传输入数据
                root : 'root',
                totalProperty : 'totalProperty'           // 定义根节点和分页总条数属性，root与totalProperty已经在CommonUtil中包含，这里只需要在页面属性添加即可
            }, reviewRecords),
	        proxy:new Ext.data.HttpProxy({url:'<%=basePath%>console/data/reviewManage.htm?action=getResSourceComtList'}),
	        sortInfo: {field:'rscoDate', direction:'desc'},
	        remoteStore:true,    // 是否远程调用数据
	        remoteSort:true      // 是否远程排序
	    });
	    
		// 加载数据
 		store.load({params:{start:0, limit:LIMIT}});
 	    store.on('beforeload',function(store, options){
 	     Ext.apply(this.baseParams, 
 	     	{ 
 	     		start:0, 
 	     		limit:LIMIT
 	     	}); 
		});
		
		function formatState(value) {
			if(value == '1') {
				return '<font style="color:gray">已通过</font>';
			}else if(value == '0') {
				return '<font style="color:red">未通过</font>';
			}else {
				return '<font style="color:blue">待审核</font>';
			}
		};
		
		function showContent(value) {
			var temp = value;
			temp = temp.replace(/<font style=color:red>/g,"");
			temp = temp.replace(/<\/font>/g,"");
			return '<p title=' + temp + ' >' + value + '</p>'
		}
		
		// 评论审核状态
		var scIssue_status_store = new Ext.data.SimpleStore({
			fields : ['value', 'text'],
			data : [[3, '不限'], [0, '未通过'], [1, '已通过'], [2, '待审核']]
		});
		
		var scIssue_status_Combox = new Ext.form.ComboBox({
			id : 'checkStateCombox',
			fieldLabel : "状态",
			mode : 'local',
			store : scIssue_status_store,
			//emptyText : '不限',
			triggerAction : 'all',
			editable : false,
			valueField : 'value',
			displayField : 'text',
			width : 100,
			value : 2,
			listeners : {
				'select' : function() {
					searchMan();
				}
			}
		});
		
		var sm = new Ext.grid.CheckboxSelectionModel(); 
	    
	    var cm = new Ext.grid.ColumnModel([
	    	new Ext.grid.RowNumberer(),
	    	sm,
	    	{
	    		id:'rscoContent', 
	    		header: '评论内容', 
	    		width: 300, 
	    		sortable: true, 
	    		dataIndex:'tempContent',
	    		renderer: showContent
            },{
	    		id:'rscoDate', 
	    		header: '评论时间', 
	    		width: 120, 
	    		sortable: true, 
	    		dataIndex:'rscoDate'
            },{
	    		id:'suinLoginName', 
	    		header: '用户名', 
	    		width: 90, 
	    		sortable: true, 
	    		dataIndex:'suinLoginName'
            },{
	    		id:'suinName', 
	    		header: '姓名', 
	    		width: 90, 
	    		sortable: true, 
	    		dataIndex:'suinName'
            },{
	    		id:'rscoStatus', 
	    		header: '状态', 
	    		width: 70, 
	    		sortable: true, 
	    		renderer : formatState,
	    		dataIndex:'rscoStatus'
            }
	    ]);
		
        var combo = new Ext.data.Record.create([
			{name:'sutyId', mapping:'sutyId', type: 'string'},
			{name:'sutyName', mapping:'sutyName', type: 'string'}
		]);
		
	// 查询
	var searchMan = function() {
		//if(isValid()) {
			//var beginDate = document.getElementById('beginDate').value;
			//var endDate = document.getElementById('endDate').value;
			store.reload({
				params : {
					start : 0,
					limit : LIMIT,
					condition: Ext.getCmp('searchCondition').getValue(),
					checkState: Ext.getCmp('checkStateCombox').getValue()
					//beginDate : beginDate,
					//endDate : endDate
				}
			});
		//}
	}
	// 创建工具条
	var tb = new Ext.Toolbar(['评论人：',
	{
		xtype:'textfield',
		emptyText:'请输入用户姓名或者登录名',
		id:'searchCondition',
		width:180,
		listeners : {
			'specialkey' : function(field, e) {
				if(e.getKey() == Ext.EventObject.ENTER) {
					searchMan();
				}
			}
		}
    },'-', '状态', scIssue_status_Combox/*'评论时间：', {
		xtype : 'datefield',
		id : 'beginDate',
		emptyText : '开始时间',
		width : 95,
		format : 'Y-m-d'
	}, '至', {
		xtype : 'datefield',
		id : 'endDate',
		emptyText : '结束时间',
		width : 95,
		format : 'Y-m-d'
	}*/, '-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text: '&nbsp;审&nbsp;核&nbsp;',
		enableToggle: false,
		iconCls:'common_edit',
		handler:function(){
			selectRow = grid.getSelectionModel().getSelected();
			if(selectRow) {
				reviewCheck();
			}else {
				alertWarring('请选择需要审核的评论！');
			}
		}
	}, '-', {
		text : '&nbsp;删&nbsp;除&nbsp;',
		iconCls : 'common_delete',
		handler : function() {
			deleteReview(grid);
		}
	}, '-', {
		text: '&nbsp;通&nbsp;过&nbsp;',
		enableToggle: false,
		iconCls:'common_past',
		handler:function(){
			checkOk();
		}
	}, '-', {
		text: '不&nbsp;通&nbsp;过',
		enableToggle: false,
		iconCls:'common_nopast',
		handler:function(){
			checkFalse();
		}
	}]);
	
		managerEditForm = new Ext.form.FormPanel({
             border : false,
			 frame	: true,
			 labelAlign: 'right',
			 buttonAlign: 'center',
			 bodyStyle:'padding:5px 5px 0; margin-top: 10px;',
			 labelWidth:80,
			 autoScroll:true,
			 containerScroll: true,
	    	 width: 200,
    		 items:[{
					xtype : 'htmleditor',
					fieldLabel : '评论内容',
					name : 'rscoContent',
					readOnly : true,
					editable : false,
					//disabled : true,
					maxLength : 300,
					anchor : '95%',
					height : 150,
					hideParent : true,
					enableFormat : false,
					enableColors: false,
					enableSourceEdit : false,
					enableLists : false,
					enableAlignments : false,
					enableFont : false,
					enableLinks : false,
					enableFontSize : false
				},{
					xtype : 'textfield',
					fieldLabel : '用户名',
					name : 'audit_suinLoginName',
					readOnly : true,
					editable : false,
					disabled : true,
					anchor : '95%'
				}, {
					xtype : 'textfield',
					fieldLabel : '用户姓名',
					name : 'audit_suinName',
					readOnly : true,
					editable : false,
					disabled : true,
					anchor : '95%'
				}, {
					xtype : 'textfield',
					fieldLabel : '评论时间',
					name : 'rscoDate',
					readOnly : true,
					editable : false,
					disabled : true,
					anchor : '95%'
				},{
	       			xtype: 'panel',
	       			layout: 'column',
	       			fieldLabel:'审核状态',
	        		isFormField:true,
	        		anchor : '95%',
	       			items: [{
	       				xtype: 'radio',
	       				boxLabel: '通过',
	       				id:	'noseedoc',
	       				name: 'rscoStatus',  
	                    inputValue: 1,  
	                    checked: true
	       			}, {
	       				xtype: 'radio',
	       				boxLabel: '不通过',
	       				id:	'seesmalldoc',
	       				name: 'rscoStatus',  
	                    inputValue: 0,  
	                    checked: false
	       			}]
       			}]				                
        });
        
	    // GRID构造
	    var grid = new Ext.grid.GridPanel({
			border: false,
			region : 'center',
			columnLines:true,
			loadMask:true,
    		store: store,
    		frame:false,
    		sm: sm,
    		cm:cm,
 			autoScroll:true,
 			viewConfig : {
				forceFit : true
			},
			enableColumnHide : false,
			tbar: tb,
	        bbar: new Ext.PagingToolbar({
	            pageSize:LIMIT,
		        store:store,
		        displayInfo: true,
       		    displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>',
   			    emptyMsg: "没有记录",
   			    plugins: new Ext.ux.ProgressBarPager()
	        })
	    });
	    
		var displayPanel = new Ext.Viewport({
			renderTo : 'mainDiv',
			layout   : 'border',
			items 	 : [grid]
		});
		
	// 评论内容显示窗口FORM
	var contentShowForm = new Ext.form.FormPanel({
		id : 'contentShowForm',
		border : false,
		frame : true,
		labelAlign : 'left',
		buttonAlign : 'center',
		bodystyle : 'padding:5px 5px 0',
		defaulttype : 'textfield',
		items : [{
			xtype : 'textarea',
			height : 100,
			width : 400,
			anchor : '100%',
			id : 'contentShow',
			name : 'contentShow',
			hideLabel : true,
			readOnly : true
		}]
	});
		
	// 删除
	function deleteReview(grid) {
		var rows = grid.getSelectionModel().getSelections();
		if(rows < 1) {
			alertWarring('请至少选择一行数据然后才能够操作！');
		}else {
			Ext.Msg.confirm("确认消息框", "您确定要删除被选中的记录吗？\n", function(btn, txt) {
				if(btn == "yes") {
					var ids = "";
					for(var i = 0; i < rows.length; i++) {
						if(i > 0) ids += ",";
						ids += rows[i].json.rscoId;
					}
					Ext.Ajax.request({
						url : '<%=basePath%>console/data/reviewManage.htm?action=deleteResSourceComt', // 批量删除
						params : {
							ids : ids
						},
						success : function(response, options) {
							var txt = Ext.util.JSON.decode(response.responseText);
							if(txt.success) {
								searchMan();
								alertSuccess('删除成功！');
							}else {
								alertError(txt.msg);
							}
						},
						failure : function(form, action) {
							alertError('系统出现未知错误，删除失败！');
						}
					});
				}
			});
		}
	}
	
	function checkOk(){
		checkRows = grid.getSelectionModel().getSelections();
		if(checkRows < 1) {
			alertWarring('请至少选择一行数据然后才能够操作！');
		}else {
			Ext.Msg.confirm("确认消息框", "您确定要通过被选中的记录吗？\n", function(btn, txt) {
				if(btn == "yes") {
					var ids = "";
					for(var i = 0; i < checkRows.length; i++) {
						if(i > 0) ids += ",";
						ids += checkRows[i].json.rscoId;
					}
					Ext.Ajax.request({
						url : '<%=basePath%>console/data/reviewManage.htm?action=checkReviewBatch', // 批量通过
						params : {
							check : '1',
							ids : ids
						},
						success : function(response, options) {
							var txt = Ext.util.JSON.decode(response.responseText);
							if(txt.success) {
								searchMan();
								alertSuccess('审核成功！');
							}else {
								alertError(txt.msg);
							}
						},
						failure : function(form, action) {
							alertError('系统出现未知错误，删除失败！');
						}
					});
				}
			});
		}
	}
	
	function checkFalse(){
		checkRows = grid.getSelectionModel().getSelections();
		if(checkRows < 1) {
			alertWarring('请至少选择一行数据然后才能够操作！');
		}else {
			Ext.Msg.confirm("确认消息框", "您确定要不通过被选中的记录吗？\n", function(btn, txt) {
				if(btn == "yes") {
					var ids = "";
					for(var i = 0; i < checkRows.length; i++) {
						if(i > 0) ids += ",";
						ids += checkRows[i].json.rscoId;
					}
					Ext.Ajax.request({
						url : '<%=basePath%>console/data/reviewManage.htm?action=checkReviewBatch', // 批量通过
						params : {
							check : '0',
							ids : ids
						},
						success : function(response, options) {
							var txt = Ext.util.JSON.decode(response.responseText);
							if(txt.success) {
								searchMan();
								alertSuccess("审核成功！");
							}else {
								alertError(txt.msg);
							}
						},
						failure : function(form, action) {
							alertError('系统出现未知错误，删除失败！');
						}
					});
				}
			});
		}
	}
			
	function reviewCheck(){
         if(!editWin){
                editWin = new Ext.Window({
		        id: 'editWin',
				layout: 'fit',
				width: 450,
				height: 350,
				plain:false,
  			    closeAction:'hide',
  			    buttonAlign: 'center',
				title: '评论审核',
				maximizable:false,
				modal:true,
			    items :[managerEditForm],
			    buttons:[{
     				    text: '确定',
     				    handler:function(){
     				         if(managerEditForm.form.isValid()){
     				         	selectRow = grid.getSelectionModel().getSelected();
 				          	       managerEditForm.form.submit({
	  				                   url:'<%=basePath%>console/data/reviewManage.htm?action=checkResSourceComt&rscoId='+selectRow.get('rscoId'),
				                       method:'POST',
				                       waitMsg:'正在提交.....',
									   waitTitle:'请稍等',
				                       success:function(form, action){
				                       		if(action.result.success){
					                      	   alertSuccess(action.result.msg);
					                      	   searchMan();
					                      	   editWin.hide();
				                       		}else{
				                       			alertWarring(action.result.msg);
				                       		}
				                   	   },
				                       failure:function(form, action){
				                          alertError("审核失败！");
				                       }
     				              });   
     				          }
      				     }
      				 },{
      				      text:'取消',
      				      handler:function(){
		                   editWin.hide(); 
      				      }
      				}]
 				});
           		createData(selectRow);
 				editWin.show();
           }else{
           		createData(selectRow);
           		editWin.show();
           }
    	}
		// 给表单真充数据
		function createData(selectRow) {
			managerEditForm.form.setValues({
				audit_suinLoginName : selectRow.get('suinLoginName'),
				audit_suinName : selectRow.get('suinName'),
				rscoDate : selectRow.get('rscoDate'),
				rscoContent : selectRow.get('rscoContent')
			});
		}
		// 验证输入数据是否合法
		/*function isValid() {
			var beginDate = Ext.getCmp('beginDate').getValue();
			var endDate = Ext.getCmp('endDate').getValue();
	
			if(!Ext.getCmp('beginDate').isValid()) {
				Ext.Msg.show({
					title : '提示',
					msg : '开始时间格式不正确！',
					icon : Ext.MessageBox.ERROR,
					buttons : Ext.Msg.OK
				});
				return false;
			}
			if(!Ext.getCmp('endDate').isValid()) {
				Ext.Msg.show({
					title : '提示',
					msg : '结束时间格式不正确！',
					icon : Ext.MessageBox.ERROR,
					buttons : Ext.Msg.OK
				});
				return false;
			}
			if(!(Ext.isEmpty(beginDate) || Ext.isEmpty(endDate))) {
				if(beginDate > endDate) {
					Ext.Msg.show({
						title : '提示',
						msg : '登录时间段中开始时间要在结束时间前!',
						icon : Ext.MessageBox.ERROR,
						buttons : Ext.Msg.OK
					});
					return false;
				}
			}
			return true;
		}*/
	});
	</script>
	</head>

	<body>
		<div id="mainDiv"></div>
	</body>
</html>
