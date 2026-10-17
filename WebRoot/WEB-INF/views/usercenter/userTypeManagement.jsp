<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.SysUserType"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
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
		var fm = Ext.form;
		
		var editWin;
		
		var workerRecords = new Ext.data.Record.create([
			{name:'sutyId', type:'string'},
			{name:'sutyName', type:'string'},
			{name:'sutyDoc', type:'string'},
			{name:'sutyPic', type:'string'},
			{name:'sutyVideo', type:'string'},
			{name:'sutyPurview', type:'string'},
			{name:'sutyDefault', type:'string'}
		]);
		
		// 页面数据仓库
	    var store = new Ext.data.Store({
	        reader: new Ext.data.JsonReader({ //使用JSON传输入数据
                root : 'root',
                totalProperty : 'totalProperty'           // 定义根节点和分页总条数属性，root与totalProperty已经在CommonUtil中包含，这里只需要在页面属性添加即可
            }, workerRecords),
	        proxy:new Ext.data.HttpProxy({url:'<%=basePath%>usercenter/userTypeManage.htm?action=initAllUserTypes'}),
	        sortInfo: {field:'sutyId', direction:'ASC'},
	        remoteStore:true,    // 是否远程调用数据
	        remoteSort:true      // 是否远程排序
	    });
	    
		// 加载数据
 		store.load({params:{start:0, limit:LIMIT}});
 	    store.on('beforeload',function(store, options){
 	     Ext.apply(this.baseParams, 
 	     	{ condition: Ext.getCmp('searchCondition').getValue(), start:0, limit:LIMIT}); 
		});
	    
	    var cm = new Ext.grid.ColumnModel([
	    	new Ext.grid.RowNumberer(),
	    	{
	    		id:'sutyName', 
	    		header: '类型名称', 
	    		width: 210, 
	    		sortable: true, 
	    		dataIndex:'sutyName'
            },{
	    		id:'sutyVideo', 
	    		header: '操作权限', 
	    		width: 150, 
	    		align : 'center',
	    		sortable: true, 
	    		dataIndex:'sutyPurview',
	    		renderer: function (v) {
	    			if(v == <%=SysUserType.PURIVEW_NOSEE %>){
	    				return "不可访问";
	    			} else if (v == <%=SysUserType.PURIVEW_CANNOT_BROWSE %>) {
	    				return "不可浏览";
	    			} else if (v == <%=SysUserType.PURIVEW_BROWSE_VIEW %>) {
	    				return "可浏览预览版";
	    			} else if (v == <%=SysUserType.PURIVEW_BROWSE_SOURCE %>) {
	    				return "可浏览全文版";
	    			} else if (v == <%=SysUserType.PURIVEW_DOWNLOAD %>) {
	    				return "可下载";
	    			} else {
	    				return v;	
	    			}
	    		}
            },{
	    		id:'sutyDefault', 
	    		header: '是否默认', 
	    		width: 90, 
	    		align : 'center',
	    		sortable: true, 
	    		dataIndex:'sutyDefault'
	    	}
	    ]);
		
var managerEditForm;
	
		
	    // GRID构造
	    var grid = new Ext.grid.GridPanel({
			border: false,
			region : 'center',
			columnLines:true,
			loadMask:true,
    		store: store,
    		frame:false,
     		sm: new Ext.grid.RowSelectionModel({
                singleSelect: true
            }),
            cm:cm,
 			autoScroll:true,
 			viewConfig : {
				forceFit : true
			},
			enableColumnHide : false,
			tbar: new Ext.Toolbar({
        		items:['类型名称：',
        			{
        				xtype:'textfield',
        				emptyText:'请输入类型名称',
        				id:'searchCondition',
        				width:150,
        				listeners : {
							'specialkey' : function(field, e) {
								if(e.getKey() == Ext.EventObject.ENTER) {
									store.load({params:{ condition: Ext.getCmp('searchCondition').getValue(), start:0, limit:LIMIT}});
								}
							}
						}
        			},'-',{
        				text: '&nbsp;查&nbsp;询&nbsp;',
        				enableToggle: false,
        				iconCls:'common_search',
        				handler: function(){
        					store.load({params:{ condition: Ext.getCmp('searchCondition').getValue(), start:0, limit:LIMIT}});
        				}
        			},'-',{
        			    text: '&nbsp;添&nbsp;加&nbsp;',
        				enableToggle: false,
        				iconCls:'common_add',
        				handler: function(){
	        				managerEditForm = new Ext.form.FormPanel({
		       	             border : false,
		   					 frame	: true,
		       				 labelAlign: 'right',
		       				 buttonAlign: 'center',
		       				 bodyStyle:'padding:5px 5px 0;',
		       				 labelWidth:100,
		       				 autoScroll:true,
							 containerScroll: true,
		       				 width: 170,
						     height: 250,       
		       				 items:[{
		        				        xtype:'textfield',
							        	fieldLabel:'类型名称',
		        				        id:'sutyName',
		        				        name:'sutyName',
						        		//anchor:'90%',
						        		width: 150,
						                allowBlank: false,
						                blankText: '该项不能为空'
					                }, 
					                {
						       			xtype: 'panel',
					        			fieldLabel:'权限设置',
					         			isFormField:true,//非常重要,否则panel默认不显示fieldLabel
					        			items: [{
					        				xtype: 'radio',
					        				boxLabel: '不可访问',
					        				name: 'sutyPurview',  
	                                        inputValue: <%=SysUserType.PURIVEW_NOSEE %>,  
	                                        checked: true
					        			}, {
					        				xtype: 'radio',
					        				boxLabel: '不可浏览',
					        				name: 'sutyPurview',  
	                                        inputValue: <%=SysUserType.PURIVEW_CANNOT_BROWSE %>,  
	                                        checked: false
					        			}, {
					        				xtype: 'radio',
					        				boxLabel: '可浏览预览版',
					        				name: 'sutyPurview',  
	                                        inputValue: <%=SysUserType.PURIVEW_BROWSE_VIEW %>,  
	                                        checked: false
					        			}, {
					        				xtype: 'radio',
					        				boxLabel: '可浏览全文',
					        				name: 'sutyPurview',  
	                                        inputValue: <%=SysUserType.PURIVEW_BROWSE_SOURCE %>,  
	                                        checked: false
					        			}, {
					        				xtype: 'radio',
					        				boxLabel: '可下载',
					        				name: 'sutyPurview',  
	                                        inputValue: <%=SysUserType.PURIVEW_DOWNLOAD %>,  
	                                        checked: false
					        			}]				                
	        				 		} , {
						       			xtype: 'panel',
					        			layout: 'column',
					        			fieldLabel:'默认',
					         			isFormField:true,
					        			items: [{
					        				xtype: 'radio',
					        				boxLabel: '是',
					        				id:	'sutydefaulty',
					        				name: 'sutyDefault',  
	                                        inputValue: true,  
	                                        checked: false
					        			}, {
					        				xtype: 'radio',
					        				boxLabel: '否',
					        				id:	'sutydefaultn',
					        				name: 'sutyDefault',  
	                                        inputValue: false,  
	                                        checked: true
					        			}]				                
	        				 }]
		       	   		});
		       	   				managerEditForm.form.url = '<%=basePath%>usercenter/userTypeManage.htm?action=addUserType';
								editUserType('add');
								editWin.add(managerEditForm);
								editWin.doLayout();
        				}
        			},'-',{
        				text: '&nbsp;编&nbsp;辑&nbsp;',
        				enableToggle: false,
        				iconCls:'common_edit',
        				handler:function(){
        					var selectRow = grid.getSelectionModel().getSelected();
        					if(selectRow)
        					{
        						//var userTypeId = selectRow.get('sutyId');
        						loadUserTypeInfo("action=getUserTypeById&userTypeId="+selectRow.get('sutyId'), selectRow, "action=modifyUserType&userTypeId="+selectRow.get('sutyId'));
        					}
        					else
        					{
        						alertWarring("请选择需要编辑的会员类型!!!");
        					}
        				}
        			},'-',{
		             	text: '&nbsp;删&nbsp;除&nbsp;',
        				enableToggle: false,
        				iconCls:'common_delete',
        				handler:function(){
        					var selectRow = grid.getSelectionModel().getSelected();
        					if(selectRow) {
        						var id = selectRow.get('sutyId');
        						if(id == 1){
        							alertWarring("对不起，该用户会系统默认用户，因此不能删除!\n");
        							return;
        						}
        					    Ext.MessageBox.show({
								    title: 'INFORMATION',
								    msg: "确定是否要删除当前会员类型信息!",
								    width:300,
								    buttons: Ext.MessageBox.YESNO,
								    fn: function(e) {
								    		if(e == 'yes') {
								    			gridSubAjax("action=deleteUserType&typeId=" + id);
								    		}
								    	},
								    icon : Ext.MessageBox.QUESTION
								});
        					} else {
        						alertWarring("请选择需要删除的会员类型!!!");
        					}
        				}
        		
        			}
        		]
        	}),
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
		
		
		function gridSubAjax(param)
		{
			Ext.Ajax.request({
				url : '<%=basePath%>usercenter/userTypeManage.htm', 
				params : param,
				method : 'POST',
				success: function (result, request) 
				{
					store.reload();
				},
				failure: function ( result, request) {
					alertError('系统运行超时或错误');
				}
			});
		}
		function loadUserTypeInfo(param, selectRow, param1)
		{
			Ext.Ajax.request({
				url : '<%=basePath%>usercenter/userTypeManage.htm', 
				params : param,
				method : 'POST',
				success: function(result, response) {
					managerEditForm = new Ext.form.FormPanel({
			        	             border : false,
			    					 frame	: true,
			        				 labelAlign: 'right',
			        				 buttonAlign: 'center',
			        				 bodyStyle:'padding:5px 5px 0;',
			        				 labelWidth:100,
			        				 autoScroll:true,
									 containerScroll: true,
			        				 width: 300,
								     height: 250,       
			        				 items:eval(result.responseText)
			        	   });
        			editUserType(selectRow)
					editWin.add(managerEditForm);
					editWin.doLayout();
     				managerEditForm.form.url = '<%=basePath%>usercenter/userTypeManage.htm?'+param1;
				},
				failure: function ( result, request) {
					alertError('系统运行超时或错误');
				}
			});
		}	
		function editUserType(row){
	         if(!editWin){
                   editWin = new Ext.Window({
				        id: 'editWin',
						layout: 'fit',
						width: 350,
						height: 250,
						plain:false,
    			        closeAction:'hide',
    			        buttonAlign: 'center',
						maximizable:false,
						modal:true,
					    buttons:[{
       				    text: '保存',
       				    handler:function(){
        				          if(managerEditForm.form.isValid()){
        				          	       managerEditForm.form.submit({
					                       method:'POST',
					                       waitMsg:'正在提交.....',
										   waitTitle:'请稍等',
					                       success:function(form, action){
					                      	  alertInformation(action.result.msg);
					                      	  store.load();
					                      	  editWin.hide();
					                      	  editWin.removeAll();
					                       },
					                       failure:function(form, action){
					                          alertWarring(action.result.msg);
					                       }
        				              });   
        				          }else{
        				               alertWarring("内容填写不完整");
        				          }
        				     }
        				 },{
	       				      text:'取消',
	       				      handler:function(){
				                   editWin.hide(); 
				                   editWin.removeAll();
	       				      }
        				}]
   				});
   				seteditUserTypeTitleAndUrl(row, editWin);
   				editWin.show();
             }else{
             	seteditUserTypeTitleAndUrl(row, editWin);
                editWin.show();
             }
	    }
	    
	    function seteditUserTypeTitleAndUrl(row, win) {
        	if(row == 'add') {
         		win.setTitle('增加会员类型');
        	} else {
         		win.setTitle('修改会员类型');
          		
        	}
	    }
	});
	</script>
  </head>
  
  <body>
      <div id="mainDiv"></div>
  </body>
</html>
