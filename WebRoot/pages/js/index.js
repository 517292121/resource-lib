
function change_background_image(div){
			div.style.backgroundImage="url(../images/menu_item_over.jpg)";
			div.style.color="#FFF";
			div.style.fontSize="13px";
		}
		
function return_background_image(div){
			div.style.backgroundImage="url(../images/menu_item.jpg)";
			div.style.color="#0168b5";
			div.style.fontSize="12px";
		}

Ext.onReady(function(){	
	
	var _tbar=new Ext.Panel({
			        baseCls:'x-plain',
					tbar:[{
						   	xtype:"tbfill"
					},{
						    xtype:'tbbutton',
						    text:'主页',
						    listeners:{
						         click:function(){
						                var _tabPanel=Ext.getCmp("main_tab");
						                var tp = _tabPanel.getComponent('tab_first');
						                _tabPanel.setActiveTab(tp);
						          }
						     }
					},{
						     xtype:'tbseparator'
					},{
						     xtype:'tbbutton',
						     text:'修改密码',
						     listeners:{
						            click:function(){
						         	 	var url = 'console/changePassword.htm?action=initializePage';
						         	 	var id = 'tabChangepwd';
						        		var tabPanel = Ext.getCmp('main_tab');
						        		var n = tabPanel.getComponent(id); 
						        		if(!n)
						        		{        		
						        			addTab(tabPanel, id, '修改密码',rootPath+url);
						        		}
						        		else
						        		{
						        			tabPanel.setActiveTab(id);
						        		}  
						            }
						     }
					},{
						     xtype:'tbseparator'
					},{
						     xtype:'tbbutton',
						     text:'退出登录',
						     listeners:{
						            click:function(){
						            	Ext.Msg.confirm("提示", "确认要退出系统吗？\n", function(btn) {
												if (btn == "yes")
														window.location.href=rootPath+"console/logout.htm";
														
												})
						            }
						     }
					},{
						                xtype:'tbseparator'
					},{
						                xtype:'tbbutton',
						                text:'帮助'
					}]
				});
	
		// 顶层控制区
        var topPanel = new Ext.Panel({
       		id: 'topPanel',
       		title:' ',
       		region:'north',
       		split:false,
       		collapsible: false,
       		html:'<iframe scrolling="no" frameborder="0" width="100%" height="100%" src="../pages/pages/top.html"></iframe>',
       		height: 100,
       		items:[_tbar]
        });
        
        // 主面板（已有tab）  
        var _tabPanel = new Ext.TabPanel({  
        	id: 'main_tab',
            region:'center',  
            resizable:true, 
            deferredRender:false,  
            autoScroll: true,   
            autoShow: true,
            activeTab:0,  
            enableTabScroll:true,
            items:[{title:"首页",
	        		id:'tab_first',
	        		autoWidth:true,
	        		layout:'fit',
	        		autoLoad : Ext.basePath.path+'/pages/background.jsp'
	        	}]	
        });
  
		//  配置视图viewport  
        viewport = new Ext.Viewport(
        {  
            layout:'border',  
            items:[topPanel,_leftForm,_tabPanel]
        });
		
		initTree();
});
