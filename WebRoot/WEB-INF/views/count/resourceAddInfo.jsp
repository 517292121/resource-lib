<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	Date date = new Date();
    int year = date.getYear() + 1900;
    
    String beginYearList = "[0,'--不限--'],";
    for(int i=year-10; i<=year; i++){
    	beginYearList += "["+i+",'"+i+"'],";
    }
    beginYearList = beginYearList.substring(0,beginYearList.length()-1);
%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <script type="text/javascript">
    	Ext.onReady(function(){
    			var pageSize = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
    			// 如果要体现FORM效验效果，必须添加下面两行代码
				Ext.QuickTips.init();
				Ext.form.Field.prototype.msgTarget = 'side';
				
				//特色库store
				var metastore = new Ext.data.Store({
					autoLoad : true,
					proxy : new Ext.data.HttpProxy({
						url : '<%=path %>/console/data/extdata.htm?action=AllStoreJson&hasNull=hasNull'
					}),
					reader : new Ext.data.ArrayReader({}, [{
						name : 'rdtyId',
						type : 'int'
					}, {
						name : 'rdtyName',
						type : 'string'
					}])
				});
				
				var store_Combox = new Ext.form.ComboBox({
					mode : 'remote',
					store : metastore,
					resizable : true,
					name : 'rdtyName',
					emptyText : '请先选择特色库...',
					triggerAction : 'all',
					valueField : 'rdtyId',
					selectOnFocus : true,
					displayField : 'rdtyName',
					editable : false,
					width : 120
				});
				
				// 起始年
				var beginYear_store = new Ext.data.SimpleStore({
					fields : ['value', 'text'],
					data : [<%=beginYearList %>]
				});
			
				var beginYear_Combox = new Ext.form.ComboBox({
					fieldLabel : "起始年",
					mode : 'local',
					store : beginYear_store,
					emptyText : '不限',
					triggerAction : 'all',
					editable : false,
					valueField : 'value',
					displayField : 'text',
					width : 90
				});
				
				// 截止年
				var endYear_store = new Ext.data.SimpleStore({
					fields : ['value', 'text'],
					data : [<%=beginYearList %>]
				});
			
				var endYear_Combox = new Ext.form.ComboBox({
					fieldLabel : "截止年",
					mode : 'local',
					store : endYear_store,
					emptyText : '不限',
					triggerAction : 'all',
					editable : false,
					valueField : 'value',
					displayField : 'text',
					width : 90
				});
				
        	    //创建数据格式
        	    var records=Ext.data.Record.create([
					{name: 'RMST_NAME', type: 'string' }, //特色库名称
        	   		{name: 'YEAR', type: 'string' },    //年份
        	        {name: 'RESOURCENUM', type: 'string' }     //数量      	    
        	    ]);  
        	    
        	    //创建数据仓库
        	    var store =new Ext.data.Store({  //使用JSON传输入数据  	    	
        	       proxy: new Ext.data.HttpProxy({url:'<%=path %>/console/count/resourceAddInfo.htm?action=getJosnData'}),
        	       reader:new Ext.data.JsonReader({
        	             totalProperty:'totalProperty',
        	             root:'root' 
        	       },records),
        	      sortInfo: {field:'resourcenum', direction:'DESC'},
        	      remoteStore:true,    // 是否远程调用数据
	              remoteSort:true 
        	    });
        	    //加载数据
        	    store.load({params:{start:0, limit:pageSize}});
        	    
        	    //在数据调用前加载,将参数传给控制器
        	    store.on('beforeload',function(store, options){
					store.baseParams  = {
						beginYear : beginYear_Combox.getValue(),
						endYear : endYear_Combox.getValue(),
						rmstId: store_Combox.getValue(),
						start:0,
						limit:pageSize
					}
				});
				
        	    //构造列表
        	    var cm=new Ext.grid.ColumnModel([
        	         new Ext.grid.RowNumberer(),//显示行号
        	         { header:'特色库',
        	           dataIndex:'RMST_NAME',
        	           id:'RMST_NAME',
			    	   sortable: true
        	         },
        	         { header:'年份',
        	           dataIndex:'YEAR',
        	           id:'YEAR',
			    	   sortable: true
        	         },
        	         { header:'资源数量',
        	           dataIndex:'RESOURCENUM',
        	           id:'RESOURCENUM',
			    	   sortable: true
        	         }
        	    ]);
        	    //多行选种的ID
        	    var grid=new Ext.grid.GridPanel({
				     border: false,
					 region : 'center',
					 store:store,
					 columnLines:true,
					 loadMask: true,
					 frame:false,
					 autoScroll:true,
					 cm:cm,
					 viewConfig:{
					   forceFit:true
					 },
					 autoScroll:true,
					 tbar: new Ext.Toolbar({
					 	items:[
		        	         {
			        			xtype:'label',
					   			text:'统计条件：',
					   			width:15,
					   			allowBlank: false
		        			},					 	
					 		//分隔符
		        	         {
			        			xtype:'label',
					   			text:'特色库：',
					   			width:15,
					   			allowBlank: false
		        			},
		        			store_Combox,'-',
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
					   			text:'统计时间段：',
					   			width:15,
					   			allowBlank: false
		        			},
		        	       	 //时间段开始时间、指定日
		        	         beginYear_Combox,
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
		        	         endYear_Combox ,'-',
		        	         {
		        				text: '统 计',
		        				enableToggle: false,
		        				width:70,
		        				iconCls:'common_search', 
		        				handler: function(){
		        					var beginD = beginYear_Combox.getValue();
		        					var endD = endYear_Combox.getValue();
		        					if(endD !=null && endD !="" && beginD > endD){
		        						Ext.Msg.alert("开始时间不能大于结束时间!");
		        						return ;
		        					}
		        					store.reload();
		        				}
	        				 },'-',
	        				 {
		        				text: '导出数据至Excel',
		        				enableToggle: false,
		        				width:70,
		        				iconCls:'excel',
		        				handler: function(){	
		        					var beginD = beginYear_Combox.getValue();
		        					var endD = endYear_Combox.getValue();
		        					var rmstId = store_Combox.getValue();
		        					var dir = <%=request.getParameter("dir")%>;
		        					if(endD !=null && endD !="" && beginD > endD){
		        						Ext.Msg.alert("开始时间不能大于结束时间!");
		        						return ;
		        					}
		        					document.location.href="<%=path %>/console/count/resourceAddInfo.htm?action=exportToExcel&beginYear="+beginD+"&endYear="+endD+"&rmstId="+rmstId;
		        					
		        				}        				 
	        				 },'-',
	        				 {
		        				text: '图形模式',
		        				enableToggle: false,
		        				width:70,
		        				iconCls:'excel',
		        				handler: function(){	
		        					var beginD = beginYear_Combox.getValue();
		        					var endD = endYear_Combox.getValue();
		        					var rmstId = store_Combox.getValue();
		        					var dir = <%=request.getParameter("dir")%>;
		        					if(endD !=null && endD !="" && beginD > endD){
		        						Ext.Msg.alert("开始时间不能大于结束时间!");
		        						return ;
		        					}
		        					document.location.href="<%=path %>/console/count/resourceAddInfo.htm?action=initPageByPhoto&beginYear="+beginD+"&endYear="+endD+"&rmstId="+rmstId;
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
				
		});	
		
		
    </script>
    	
  </head>
  
  <body>
        <div id="mainDiv"></div>
  </body>
</html>
