<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    <%@ include file="../common/extInclude.jsp" %>
   
  <script type="text/javascript" src="<c:url value='/viewsjs/common/sha1.js'/>"></script>
    <script type="text/javascript">
    var LIMIT = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>;
	Ext.onReady(function()
	{
		// 如果要体现FORM效验效果，必须添加下面两行代码
		Ext.QuickTips.init();
		Ext.form.Field.prototype.msgTarget = 'side';
		var fm = Ext.form;
		
		var editWin;
		var editUserTypeWin;
		var loaded = false;
		
		var workerRecords = new Ext.data.Record.create([
			{name:'suinId', type:'string'},
			{name:'suinName', type:'string'},
			{name:'suinLoginName', type:'string'},
			{name:'suinEmail', type:'string'},
			{name:'sutyName', type:'string'},
			{name:'suinJoinDate', type:'string'},
			{name:'suinIsStop', type:'string'}
		]);
		
		// 页面数据仓库
	    var store = new Ext.data.Store({
	        reader: new Ext.data.JsonReader({ //使用JSON传输入数据
                root : 'root',
                totalProperty : 'totalProperty'           // 定义根节点和分页总条数属性，root与totalProperty已经在CommonUtil中包含，这里只需要在页面属性添加即可
            }, workerRecords),
	        proxy:new Ext.data.HttpProxy({url:'<%=basePath%>usercenter/userManage.htm?action=initAllUsers'}),
	        sortInfo: {field:'suinJoinDate', direction:'DESC'},
	        remoteStore:true,    // 是否远程调用数据
	        remoteSort:true      // 是否远程排序
	    });
	    
		// 加载数据
 		store.load({params:{start:0, limit:LIMIT}});
 	    store.on('beforeload',function(store, options){
 	     Ext.apply(this.baseParams, 
 	     	{ condition: Ext.getCmp('searchCondition').getValue(), start:0, limit:LIMIT}); 
		});
		var sm = new Ext.grid.CheckboxSelectionModel(); 
	  
	    
	    var cm = new Ext.grid.ColumnModel([
	    	new Ext.grid.RowNumberer(),
	    	sm,
	    	{
	    		id:'suinName', 
	    		header: '真实姓名', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'suinName'
            },{
	    		id:'suinLoginName', 
	    		header: '登录名', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'suinLoginName'
            },{
	    		id:'sutyName', 
	    		header: '类型', 
	    		width: 120, 
	    		align : 'center',
	    		sortable: true, 
	    		dataIndex:'sutyName'
            },{
	    		id:'suinEmail', 
	    		header: '邮箱', 
	    		width: 180, 
	    		sortable: true, 
	    		dataIndex:'suinEmail'
            },{
	    		id:'suinJoinDate', 
	    		header: '注册日期', 
	    		width: 150, 
	    		sortable: true, 
	    		dataIndex:'suinJoinDate'
            },{
	    		id:'suinIsStop', 
	    		header: '状态', 
	    		width: 90, 
	    		align : 'center',
	    		sortable: true, 
	    		dataIndex:'suinIsStop'
            }
	    ]);
		
		var managerEditForm = new Ext.form.FormPanel({
        	             border : false,
    					 frame	: true,
        				 labelAlign: 'right',
        				 buttonAlign: 'center',
        				 bodyStyle:'padding:5px 5px 0; margin-top: 10px;',
        				 labelWidth:50,
        				 autoScroll:true,
						 containerScroll: true,
        				 width: 200,
					     height: 40,
        				 items:[{
        				        xtype:'textfield',
					        	fieldLabel:'新密码',
        				        id:'suinPwd',
        				        inputType: 'password',
				        		anchor:'90%',
				                blankText:'该字段不允许为空',
				                allowBlank: false
        				 }]

        	   });
		var editUserTypeForm = new Ext.form.FormPanel({
        	             border : false,
    					 frame	: true,
        				 labelAlign: 'right',
        				 buttonAlign: 'center',
        				 bodyStyle:'padding:5px 5px 0; margin-top: 10px;',
        				 labelWidth:70,
        				 autoScroll:true,
						 containerScroll: true,
        				 width: 200,
					     height: 150,
        				 items:[{
        				        xtype:'panel',
			        			anchor: '100%',
			        			frame: true,
								labelAlign	: 'left',         			
			        			layout: 'column',
			        			layoutConfig:{columns:3},
			        			fieldLabel:'会员类型',
			         			isFormField:true,//非常重要,否则panel默认不显示fieldLabel
								autoHeight: true,
								autoWidth: true,
								items: [ 
										
								]
        				 }]

        	   });
		
		var pagingToolBar = new Ext.PagingToolbar({
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
			tbar: new Ext.Toolbar({
        		items:['用户状态:',
        			{
        				id: 'suinIsStopCombo',
        				xtype: 'combo',
        				typeAhead: true,
        				labelSeparator: ':',
        				multiSelect: false,
        				triggerAction: 'all',
        				hiddenName: 'suinIsStop',
        				editable : false,
        				mode : 'local',
        				width: 70,
						store: new Ext.data.ArrayStore({
					        fields: [
					            'valueField',
					            'displayField'
					        ],
					        data: [['true', '禁用'], ['false', '启用'], ['', '全部']]
					    }),
						valueField: 'valueField',
						displayField: 'displayField',
						listeners: {
								select: function(combo, record, index) {
									store.load({params:{condition: Ext.getCmp('searchCondition').getValue(), suinIsStop: Ext.getCmp('suinIsStopCombo').getValue(), start:0, limit:LIMIT}});
								}, 
								render: function(combo) {
									combo.setValue("全部");
								}
						}						
        			},'用户:','-',{
        				xtype:'textfield',
        				emptyText:'请输入用户姓名或者登录名',
        				id:'searchCondition',
        				width:180,
        				listeners : {
							'specialkey' : function(field, e) {
								if(e.getKey() == Ext.EventObject.ENTER) {
									store.load({params:{ condition: Ext.getCmp('searchCondition').getValue(), start:pagingToolBar.cursor, limit:LIMIT, suinIsStop: Ext.getCmp('suinIsStopCombo').getValue()}});
								}
							}
						}
        			},'-',{
        				text: '&nbsp;查&nbsp;询&nbsp;',
        				enableToggle: false,
        				iconCls:'common_search',
        				handler: function(){
        					store.load({params:{ condition: Ext.getCmp('searchCondition').getValue(), start:pagingToolBar.cursor, limit:LIMIT, suinIsStop: Ext.getCmp('suinIsStopCombo').getValue()}});
        				}
        			},'-',{
        				text: '启用&nbsp;/&nbsp;禁用',
        				enableToggle: false,
        				iconCls:'common_edit',
        				handler:function(){
        					 userIds="";
        					 var msgInfo = "是否要禁用该会员?";
        				     var rows=grid.getSelectionModel().getSelections();
        				     
        				     if(rows.length == 1) {
        				     	if(rows[0].get("suinIsStop") == "禁用") {
        				     		msgInfo = "是否要启用该会员?";
        				     	}
        				     } else {
        				     	if(Ext.getCmp('suinIsStopCombo').getValue() == "true") {
        				     		msgInfo = "是否要启用这些会员?";
        				     	} else if(Ext.getCmp('suinIsStopCombo').getValue() == "false") {
        				     		msgInfo = "是否要禁用这些会员?";
        				     	} else {
        				     		msgInfo = "是否要启用/禁用这些会员?";
        				     	}
        				     }
        				     for(var i=0;i<rows.length;i++){
        				          var result=rows[i];
        				          userIds+=result.get('suinId')+" ";
        				     }
        				     if(userIds !=""){
        				     		Ext.util.Format.trim(userIds);
        				              Ext.MessageBox.show({
        				                    title: 'INFORMATION',
									        msg: msgInfo,
									      //  width:300,
									        buttons: Ext.MessageBox.YESNO,
									        fn: function(e) {
									        	if(e == 'yes') {
									        		gridSubAjax('?action=modifyUsersStopState', "userIds=" + userIds);
									        	}
									        },
									        icon : Ext.MessageBox.QUESTION
        				              });
        				     }else{
        				     		Ext.MessageBox.show({
        				                    title: 'INFORMATION',
									        msg: "请选择需要启用/禁用的会员",
									        width:300,
									        buttons: Ext.MessageBox.OK
        				              });
        				     }		
        				}
        			},'-',{
		             	text: '&nbsp;删&nbsp;除&nbsp;',
        				enableToggle: false,
        				iconCls:'common_delete',
        				handler:function(){
        					 userIds="";
        				     var rows=grid.getSelectionModel().getSelections();
        				     for(var i=0;i<rows.length;i++){
        				          var result=rows[i];
        				          userIds+=result.get('suinId')+" ";
        				     }
        				     if(userIds !=""){
        				     		Ext.util.Format.trim(userIds);
        				              Ext.MessageBox.show({
        				                    title: 'INFORMATION',
									        msg: "是否要删除该会员",
									        width:300,
									        buttons: Ext.MessageBox.YESNO,
									        fn: function(e) {
									        	if(e == 'yes') {
									        		gridSubAjax('?action=deleteUsers', "userIds=" + userIds);
									        	}
									        },
									        icon : Ext.MessageBox.QUESTION
        				              });
        				     }else{
        				     		Ext.MessageBox.show({
        				                    title: 'INFORMATION',
									        msg: "请选择需要删除的会员",
									        width:300,
									        buttons: Ext.MessageBox.OK
        				              });
        				     }		
        				}
        			},'-',{
        			    text: '重置密码',
        				enableToggle: false,
        				iconCls:'common_go',
        				handler: function(){
        					 userIds="";
        				     var rows=grid.getSelectionModel().getSelections();
        				     for(var i=0;i<rows.length;i++){
        				          var result=rows[i];
        				          userIds+=result.get('suinId')+" ";
        				     }
        				     if(userIds !=""){
       				     		Ext.util.Format.trim(userIds);
  					      		editPwd(userIds);
  					      		managerEditForm.getForm().reset();
        				     }else{
        				     		Ext.MessageBox.show({
        				                    title: 'INFORMATION',
									        msg: "请选择需重置密码的会员",
									        width:300,
									        buttons: Ext.MessageBox.OK
        				              });
        				     }	
        				}
        			}, '-', {
        				text: '修改会员类型',
        				enableToggle: false,
        				iconCls:'common_edit',
        				handler: function(){ 
        					 userIds="";
        				     var rows=grid.getSelectionModel().getSelections();
        				     for(var i=0;i<rows.length;i++){
        				          var result=rows[i];
        				          userIds+=result.get('suinId')+" ";
        				     }
        				     if(userIds !=""){
       				     		Ext.util.Format.trim(userIds);
  					      		editUserType(userIds);
  					      		//managerEditForm.getForm().reset();
        				     }else{
        				     		 Ext.MessageBox.show({
        				                    title: 'INFORMATION',
									        msg: "请选择需要修改类型的会员",
									        width:300,
									        buttons: Ext.MessageBox.OK
        				              });
        				     }	        				
        				}
        			}
        		]
        	}),
	        bbar: pagingToolBar
	    });
	     
	      
		var displayPanel = new Ext.Viewport({
			renderTo : 'mainDiv',
			layout   : 'border',
			items 	 : [grid]
		});
			
		function gridSubAjax(action,param)
		{
			Ext.Ajax.request({
				url : '<%=basePath%>usercenter/userManage.htm' + action, 
				params : param,
				method : 'POST',
				success: function (result, request) 
				{
					var msg = result.responseText;
					alertInformation(msg);
					//store.load({params:{start:0, limit:LIMIT}});
					store.reload({params:{ condition: Ext.getCmp('searchCondition').getValue(), start:pagingToolBar.cursor, limit:LIMIT, suinIsStop: Ext.getCmp('suinIsStopCombo').getValue()}});
					//store.reload();
				},
				failure: function ( result, request) {
					alertError("该用户已做关联，请勿删除！");
				}
			});
		}
		
		function editUserType(userIds) {
	//		editUserTypeForm.show();
			this.userIds = userIds;
			var that = this;
			if(!editUserTypeWin) {
				editUserTypeWin = new Ext.Window({
					id: 'editUserTypeWin',
					layout: 'fit',
					resizable: false,
					width: 350,
					height: 200,
					plain: false,
   			        closeAction:'hide',
   			        buttonAlign: 'center',
					title: '修改会员类型',
					maximizable:false,
					modal:true,
				    items :[editUserTypeForm],
				    buttons: [{
				    	text: '保存',
				    	handler: function() {
				    		Ext.Ajax.request({
				    			url: '<%=basePath %>usercenter/userManage.htm?action=modifyUserType',
				    			method: 'POST',
				    			params: {userIds: that.userIds, userTypeId: editUserTypeForm.form.findField("userType").getGroupValue()},
				    			success: function(result, request) {
				    				store.reload();
				    				alertWarring("修改会员类型成功");
				    			}, 
				    			failure: function(result, request) {
									alertError("系统运行超时或执行失败");
				    			}
				    		});
				    		editUserTypeForm.items.first().items.each(function() {
				    			editUserTypeForm.items.first().remove(this);
				    		});
				    		editUserTypeForm.items.first().doLayout(true);
				    		editUserTypeWin.hide();
				    	}
				    },  {
				    	text: '关闭',
				    	handler: function() {
				    		editUserTypeWin.hide();
				    	}
				    }],
				    listeners : {
						'hide' : function() {
							editUserTypeForm.items.first().items.each(function() {
				    			editUserTypeForm.items.first().remove(this);
				    		});
				    		editUserTypeForm.items.first().doLayout(true);
						}
					}
				});
				editUserTypeWin.show();
			} else {
				editUserTypeWin.show();
			}
			//if(!loaded) {
				Ext.Ajax.request({
					url: '<%=basePath%>usercenter/userManage.htm?action=getAllUserTypes&userIds='+that.userIds,
					method: 'POST',
					success: function(result, request) {
						editUserTypeForm.items.first().add(eval("[" + result.responseText + "]"));
						editUserTypeForm.items.first().doLayout(true);
						loaded = true;
					},
					failure: function(result, request) {
						alertError("系统运行超时或执行失败");
					}
				});
			//}
		}
		
		function editPwd(userIds){
	         if(!editWin){
                   editWin = new Ext.Window({
				        id: 'editWin',
						layout: 'fit',
						width: 350,
						height: 150,
						plain:false,
    			        closeAction:'hide',
    			        buttonAlign: 'center',
						title: '重置密码',
						maximizable:false,
						modal:true,
					    items :[managerEditForm],
					    buttons:[{
       				     text: '保存',
       				     handler:function(){
        				          if(managerEditForm.form.isValid()){
        				          	 Ext.MessageBox.show({
        				                    title: 'INFORMATION',
									        msg: "是否要重置该会员密码？",
									        width:300,
									        buttons: Ext.MessageBox.YESNO,
									        fn: function(e) {
									        	if(e == 'yes') {
			        				          	       managerEditForm.form.submit({
			        				                   url:'<%=basePath%>usercenter/userManage.htm?action=resetUsersPassword&userIds='+userIds,
								                       params: {
								                       		newSuinPwd: hex_sha1(Ext.getCmp('suinPwd').getValue())
								                       },
								                       method:'POST',
								                       waitMsg:'正在提交.....',
													   waitTitle:'请稍等',
								                       success:function(form, action){
								                      	  alertWarring(action.result.msg);
								                      	  store.reload();
								                      	  editWin.hide();
								                       },
								                       failure:function(form, action){
								                          alertWarring(action.result.msg);
								                       }
			        				              });   
									        	}
									        },
									        icon : Ext.MessageBox.QUESTION
        				              });
        				          }else{
        				               alertWarring("内容填写不完整");
        				          }
        				     }
        				 },{
	       				      text:'重置',
	       				      handler:function(){
				                   managerEditForm.form.reset(); 
	       				      }
        				}]
   				});
   				editWin.show();
             }else{
                editWin.show();
             }
	    }
	});
	</script>

  </head>
  
  <body>
      <div id="mainDiv"></div>
  </body>
</html>
