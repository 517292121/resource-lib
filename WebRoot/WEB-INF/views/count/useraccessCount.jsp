<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
 		//jso数据列表
 		var url_json ="console/count/userAccess.htm?action=getUserCountData";
 		//导出统计
 		var url_count_exp="<%=basePath%>console/count/userAccess.htm?action=exportUserAccessDataToExcel";
 		//导出日志
 		var url_log_exp="<%=basePath%>console/count/userAccess.htm?action=exportLogtoExl&log=all";
 		//日志列表
 		var url_log_list="<%=path %>/console/count/userAccess.htm?action=initLogView";
    	Ext.onReady(function(){
    			var pageSize = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
    			// 如果要体现FORM效验效果，必须添加下面两行代码
				Ext.QuickTips.init();
				Ext.form.Field.prototype.msgTarget = 'side';
				
        	    //创建数据格式 
        	    var records=Ext.data.Record.create([
				{
        	         name: 'userName', //登录名
        	         type: 'string'
        	    },
        	    {
        	         name: 'countType',//类型
        	         type: 'string'
        	    },    
        	    {
        	         name: 'countNum',//数量
        	         type: 'string'
        	    },    
        	    {
        	         name: 'rownum',//数量
        	         type: 'string'
        	    }          	    
        	    ]);  
        	    
        	    //创建数据仓库
        	    var store =new Ext.data.Store({  //使用JSON传输入数据  	    	
        	       proxy: new Ext.data.HttpProxy({url:url_json}),
        	       reader:new Ext.data.JsonReader({
        	             totalProperty:'totalProperty',
        	             root:'root' 
        	       },records),
        	      sortInfo: {field:'countNum', direction:'DESC'},
        	      remoteStore:true,    // 是否远程调用数据
	              remoteSort:true 
        	    });
        	    //加载数据
        	    store.load({params:{start:0, limit:pageSize}});
        	    
        	    //在数据调用前加载,将参数传给控制器
        	    store.on('beforeload',function(store, options){
					store.baseParams  = {
						countType : Ext.getCmp('countTypeCombo').getValue(),
						userName : Ext.getCmp('userName').getValue(),
						beginD : document.getElementById('beginD').value,
						endD : document.getElementById('endD').value,
						start:0,
						limit:pageSize
					}
				});
				
        	    //构造列表
        	    var cm=new Ext.grid.ColumnModel([
        	         new Ext.grid.RowNumberer(),//显示行号
        	         { header:'会员名称',
        	           dataIndex:'userName',
        	           id:'userName',
        	           width: 50, 
			    	   sortable: false
        	         },
        	         { header:'操作类型',
        	           dataIndex:'countType',
        	           id:'countType',
        	           width: 50, 
			    	   sortable: false
        	         },
        	         { header:'操作次数',
        	           dataIndex:'countNum',
        	           id:'countNum',
        	           width: 50, 
			    	   sortable: true
        	         }
        	    ]);
        	    //多行选种的ID
        	    var grid=new Ext.grid.GridPanel({
        	    	 stripeRows: true,  //隔行换颜色 
					 store:store,
					 loadMask: true,     //加载时提示load... 
					 frame:false,
				     border: false,
		             enableColumnHide : false,  //屏蔽勾选列
					 viewConfig:{
					   forceFit:true
					 },
					 cm:cm,
					 autoScroll:true,
					 region : 'center',
					 tbar: new Ext.Toolbar({
					 	items:['统计类型:',
        					{
		        				id: 'countTypeCombo',
		        				xtype: 'combo',
		        				typeAhead: true,
		        				labelSeparator: ':',
		        				multiSelect: false,
		        				triggerAction: 'all',
		        				hiddenName: 'countType',
		        				editable : false,
		        				mode : 'local', 
		        				width: 70,
								store: new Ext.data.ArrayStore({
							        fields: [
							            'valueField',
							            'displayField'
							        ],
							        data: [['0','全部'],['1', '登录'], ['2', '检索'], ['3', '阅读'], ['4', '下载'], ['5', '访问']]
							    }),
								valueField: 'displayField',
								displayField: 'displayField',
								listeners: {
										select: function(combo, record, index) {
											store.load();
										}, 
										render: function(combo) {
											combo.setValue("全部");
										}
								}						
		        			},'-','用户名称:',
		        			{	
		        	        	xtype:'textfield',
		        	        	fieldLabel:'会员名称',
		        	            name:'userName',
		        	            id:'userName',
		        	            width:'80',
		        	            anchor:'97%'
		        	         },'-',
		        	         //分隔符
		        	         {
			        			xtype:'label',
					   			width:15,
					   			allowBlank: false
		        			},	
		        	         //分隔符
		        	         {
			        			xtype:'label',
					   			width:15,
					   			allowBlank: false
		        			},	
		        	         {
			        			xtype:'label',
					   			text:'统计时间段:',
					   			width:15,
					   			allowBlank: false
		        			},
		        	       	 //时间段开始时间、指定日
		        	         {	
		        	        	xtype:'datefield',
		        	            name:'beginD',
		        	            id:'beginD',
		        	            anchor:'97%',
		        	            altFormats: 'Y-m-d',
	                			format: 'Y-m-d',
	        					readOnly : false
		        	         },
		        	         //分隔符
		        	         {
			        			xtype:'label',
					   			text:'--',
					   			width:10,
					   			name:'divide',
					   			id:'divideD',
					   			allowBlank: false
		        			},
		        	         //时间段结束时间
		        	         {	
		        	        	xtype:'datefield',
		        	            name:'endD',
		        	            id:'endD',
		        	            anchor:'97%',
		        	            altFormats: 'Y-m-d',
	                			format: 'Y-m-d',
	        					readOnly : false
		        	         },'-',
		        	         {
		        				text: '统计',
		        				iconCls:'common_search', 
		        				handler: function(){
		        					var beginD = document.getElementById('beginD').value;
		        					var endD = document.getElementById('endD').value;
		        					if(endD !=null && endD !="" && beginD > endD){
		        						alertWarring("开始时间不能大于结束时间!");
		        						return ;
		        					}
		        					store.load();
		        				}
	        				 },'-',{ 
								text: '查看日志', 
								enableToggle: false,  
								iconCls:'common_list', 
								handler: function(){ 
									logList();
								} 
							},'-',
	        				 {
		        				text: '导出统计',
		        				enableToggle: false,
		        				width:70,
		        				iconCls:'excel',
		        				handler: function(){	
		        					var beginD = document.getElementById('beginD').value; 
		        					var endD = document.getElementById('endD').value;
		        					var userName = document.getElementById('userName').value;
		        					var type = Ext.getCmp('countTypeCombo').getValue();
		        					if(beginD !=null && beginD !="" && endD !=null && endD !="" && beginD > endD){
		        						alertWarring("开始时间不能大于结束时间!");
		        						return ;
		        					}
		        					document.location.href=url_count_exp+"&beginD="+beginD+"&endD="+endD+"&userName="+userName+"&countType="+type;
		        				}        				 
	        				 },'-',
	        				 {
		        				text: '导出日志',
		        				enableToggle: false,
		        				width:70,
		        				iconCls:'excel',
		        				handler: function(){	
		        					var beginD = document.getElementById('beginD').value; 
		        					var endD = document.getElementById('endD').value;
		        					var userName = document.getElementById('userName').value;
		        					var type = Ext.getCmp('countTypeCombo').getValue();
		        					if(beginD !=null && beginD !="" && endD !=null && endD !="" && beginD > endD){
		        						alertWarring("开始时间不能大于结束时间!");
		        						return ;
		        					} 
		        					document.location.href=url_log_exp+"&beginD="+beginD+"&endD="+endD+"&userName="+userName+"&type="+type;
		        				}        				 
	        				 }
		        	        ]
					 }),
					 bbar:new Ext.PagingToolbar({
					       pageSize:pageSize,
					       store:store,
					       displayInfo: true,
	            		   displayMsg: '显示 {0} 到 {1} 条记录，一共 {2} 条',
            			   emptyMsg: "没有记录",
            			   plugins: new Ext.ux.ProgressBarPager()
					 })
        	    });
        	    
        	 
                var displayPanel = new Ext.Viewport({
					renderTo : 'mainDiv',
					layout     : 'border',
					items 	  : [grid]
				}); 
				
				//添加表格双击事件
				grid.on('dblclick', logList);
			
				function logList(){
					var selectRow = grid.getSelectionModel().getSelected();
					if(selectRow){
					    var name = selectRow.get("userName");
					    var type = selectRow.get("countType"); 
					    window.location.href =url_log_list+"&beginD="+beginD+"&endD="+endD + "&userName=" + name + "&type=" + type;
					}else{
						alertWarring("请选择用户!");
					}
				}
		});	
		
		
    </script>
    	
  </head>
  
  <body>
        <div id="mainDiv"></div>
  </body>
</html>
