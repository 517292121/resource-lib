<%@ page language="java"  pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
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
		
		var workerRecords = new Ext.data.Record.create([
			{name:'suinId', type:'string'},
			{name:'suinName', type:'string'},
			{name:'suinLoginName', type:'string'},
			{name:'suinEmail', type:'string'},
			{name:'sutyName', type:'string'},
			{name:'suinJoinDate', type:'string'},
			{name:'suinState', type:'string'},
			{name:'suinAuditMode', type:'string'},
			{name:'suinSex', type:'string'},
			{name:'suinMobileTelephone', type:'string'},
			{name:'sutyId', type:'string'}
		]);
		
		// 页面数据仓库
	    var store = new Ext.data.Store({
	        reader: new Ext.data.JsonReader({ //使用JSON传输入数据
                root : 'root',
                totalProperty : 'totalProperty'           // 定义根节点和分页总条数属性，root与totalProperty已经在CommonUtil中包含，这里只需要在页面属性添加即可
            }, workerRecords),
	        proxy:new Ext.data.HttpProxy({url:'<%=basePath%>usercenter/userManage.htm?action=initAllNotAuditUsers'}),
	        sortInfo: {field:'suinJoinDate', direction:'ASC'},
	        remoteStore:true,    // 是否远程调用数据
	        remoteSort:true      // 是否远程排序
	    });
	    
		// 加载数据
 		store.load({params:{start:0, limit:LIMIT}});
 	    store.on('beforeload',function(store, options){
 	     Ext.apply(this.baseParams, 
 	     	{ 
 	     		start:paging.cursor, limit:LIMIT
 	     	}); 
		});
		var sm = new Ext.grid.CheckboxSelectionModel({singleSelect : true}); 
	    
	    var cm = new Ext.grid.ColumnModel([
	    	new Ext.grid.RowNumberer(),
//	    	sm,
	    	{
	    		id:'suinLoginName', 
	    		header: '用户名', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'suinLoginName'
            },{
	    		id:'suinName', 
	    		header: '姓名', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'suinName'
            },{
	    		id:'sutyName', 
	    		header: '类型', 
	    		width: 90, 
	    		align : 'center',
	    		sortable: true, 
	    		dataIndex:'sutyName'
            },{
	    		id:'suinEmail', 
	    		header: '邮箱', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'suinEmail'
            },{
	    		id:'suinJoinDate', 
	    		header: '注册日期', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'suinJoinDate'
            },{
	    		id:'suinState', 
	    		header: '状态', 
	    		width: 90, 
	    		align : 'center',
	    		sortable: true, 
	    		dataIndex:'suinState'
            },{
	    		id:'suinAuditMode', 
	    		header: '审核方式', 
	    		width: 120, 
	    		align : 'center',
	    		sortable: true, 
	    		dataIndex:'suinAuditMode'
            }
	    ]);
		
        var combo = new Ext.data.Record.create([
			{name:'sutyId', mapping:'sutyId', type: 'string'},
			{name:'sutyName', mapping:'sutyName', type: 'string'}
		]);
		
	// 用户类型数据仓库
    var userTypeStore = new Ext.data.Store({
    	autoLoad : true,
        reader: new Ext.data.JsonReader({ //使用JSON传输入数据
               root : 'root'
               //totalProperty : 'totalProperty'           // 定义根节点和分页总条数属性，root与totalProperty已经在CommonUtil中包含，这里只需要在页面属性添加即可
           }, combo),
        proxy:new Ext.data.HttpProxy({url:'<%=basePath%>usercenter/userTypeManage.htm?action=getUserTypeList'}),
        remoteStore:true,
        remoteSort:true
    });
    
	// 查询
	var searchMan = function() {
		store.reload({
			params : {
				start : 0,
				limit : LIMIT,
				condition: Ext.getCmp('searchCondition').getValue(),
				suinState: Ext.getCmp('suinStateCombo').getValue()
			}
		});
	}
	// 创建工具条
	var tb = new Ext.Toolbar(['用户状态:',
	{
			id: 'suinStateCombo',
			xtype: 'combo',
			label: '用户状态',
			typeAhead: true,
			labelSeparator: ':',
			multiSelect: false,
			triggerAction: 'all',
			hiddenName: 'suinState',
			editable : false,
			mode : 'local',
			width: 70,
			store: new Ext.data.ArrayStore({
		        fields: [
		            'valueField',
		            'displayField'
		        ],
		        data: [['-1', '未通过'], ['0', '待审核'], ['', '全部']]
//		        data: [['-1', '未通过'], ['0', '待审核'],['1', '已通过']]
		    }),
			valueField: 'valueField',
			displayField: 'displayField',
			listeners: {
				select: function(combo, record, index) {
					store.reload({params:{condition: Ext.getCmp('searchCondition').getValue(), suinState: Ext.getCmp('suinStateCombo').getValue(), start:paging.cursor, limit:LIMIT}});
				}, 
				render: function(combo) {
					combo.setValue("待审核");
			}
		}
	},'-', '用户:',{
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
    },'-', {
		text : '&nbsp;查&nbsp;询&nbsp;',
		iconCls : 'common_search',
		handler : searchMan
	}, '-', {
		text: '&nbsp;审&nbsp;核&nbsp;',
		enableToggle: false,
		iconCls:'common_edit',
		handler:function(){
			var selectRow = grid.getSelectionModel().getSelected();
			if(selectRow) {
				if(selectRow.length = 1) {
					auditUsers(selectRow);
				}
			}else {
				Ext.Msg.show({
					title : '提示',
					msg : '请选择需要审核的会员！',
					icon : Ext.MessageBox.INFO,
					buttons : Ext.Msg.OK
				});
			}
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
		     height: 100,
    		 items:[{
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
					fieldLabel : '性别',
					name : 'audit_suinSex',
					readOnly : true,
					editable : false,
					disabled : true,
					anchor : '95%'
				},{
					xtype : 'textfield',
					fieldLabel : '联系电话',
					name : 'audit_suinMobileTelephone',
					readOnly : true,
					editable : false,
					disabled : true,
					anchor : '95%'
				},{
					xtype : 'textfield',
					fieldLabel : '邮箱',
					name : 'audit_suinEmail',
					readOnly : true,
					editable : false,
					disabled : true,
					anchor : '95%'
					}, {
					xtype : 'textfield',
					fieldLabel : '注册时间',
					name : 'audit_suinJoinDate',
					readOnly : true,
					editable : false,
					disabled : true,
					anchor : '95%'
				}, {
	       			xtype: 'combo',
        			width : 150,
        			fieldLabel:'会员类型',
        			id: 'userTypeCombo',
        			name: 'userTypeCombo',
         			store : userTypeStore,
					emptyText : '请选择会员类型',
					hiddenName : 'sutyName',
					displayField : 'sutyName',
					valueField : 'sutyId',
					mode : 'remote',
					editable : false,
					triggerAction : 'all',
					selectOnFocus : true,
					allowBlank : false,
					blankText : '会员类型不能为空!',
					anchor : '95%',
					listeners : {
						'focus' : function() {
							userTypeStore.reload();
						}
					}
       			},{
       			xtype: 'panel',
       			layout: 'column',
       			fieldLabel:'是否通过',
        		isFormField:true,
       			items: [{
	       				xtype: 'radio',
	       				boxLabel: '通过',
	       				id:	'noseedoc',
	       				name: 'suinState',  
	                    inputValue: 1,  
	                    checked: true
	       			}, {
	       				xtype: 'radio',
	       				boxLabel: '不通过',
	       				id:	'seesmalldoc',
	       				name: 'suinState',  
	                    inputValue: -1,  
	                    checked: false
	       			}]
       			}]				                
        });
        var paging = new Ext.PagingToolbar({
	            pageSize:LIMIT,
		        store:store,
		        displayInfo: true,
       		    displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>',
   			    emptyMsg: "没有记录",
   			    plugins: new Ext.ux.ProgressBarPager()
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
	        bbar: paging
	    });
	    
		var displayPanel = new Ext.Viewport({
			renderTo : 'mainDiv',
			layout   : 'border',
			items 	 : [grid]
		});
			
	function auditUsers(selectRow){
         if(!editWin){
                editWin = new Ext.Window({
		        id: 'editWin',
				layout: 'fit',
				width: 400,
				height: 350,
				plain:false,
			       closeAction:'hide',
			       buttonAlign: 'center',
				title: '会员审核',
				maximizable:false,
				modal:true,
			    items :[managerEditForm],
			    buttons:[{
     				    text: '保存',
     				    handler:submitAuditUsers
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
    	
    	function submitAuditUsers(){
    		var selectRow = grid.getSelectionModel().getSelected();
	         if(managerEditForm.form.isValid()){
	      	       managerEditForm.form.submit({
	                 url:'<%=basePath%>usercenter/userManage.htm?action=auditUsers&userId='+selectRow.get('suinId'),
	                  method:'POST',
	                  waitMsg:'正在提交.....',
	   				  waitTitle:'请稍等',
	                  success:function(form, action){
	                  		if(action.result.success){
	                  	   alertWarring(action.result.msg);
	                  	   if(Ext.getCmp('suinStateCombo').getValue() === '待审核') {
	                  	   		Ext.getCmp('suinStateCombo').setValue('0');
	                  	   }
	                  	   store.reload({params:{condition: Ext.getCmp('searchCondition').getValue(), suinState: Ext.getCmp('suinStateCombo').getValue(), start:paging.cursor, limit:LIMIT}});
	                  	   editWin.hide();
	                  		}else{
	                  			alertWarring(action.result.msg);
	                  		}
	              	   },
	                  failure:function(form, action){
	                     alertWarring("审核失败！");
	                  }
	              });   
	          }else{
	               alertWarring("内容填写不完整");
	          }
	     }
		// 给表单真充数据
		function createData(selectRow) {
			managerEditForm.form.setValues({
				audit_suinLoginName : selectRow.get('suinLoginName'),
				audit_suinName : selectRow.get('suinName'),
				audit_suinEmail : selectRow.get('suinEmail'),
				audit_suinJoinDate : selectRow.get('suinJoinDate'),
				audit_suinSex : selectRow.get('suinSex'),
				audit_suinMobileTelephone : selectRow.get('suinMobileTelephone'),
				userTypeCombo : selectRow.get('sutyId')
			});
		}
	});
	</script>
  </head>
  
  <body>
      <div id="mainDiv"></div>
  </body>
</html>
