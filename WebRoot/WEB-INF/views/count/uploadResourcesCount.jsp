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
        	    },{
        	         name: 'realName',		//姓名
        	         type: 'string'
        	    },{
        	         name: 'countNum',		//数量
        	         type: 'string'
        	    },   
        	    {
        	         name: 'rownum',		//数量
        	         type: 'string'
        	    }   
        	    ]);  
        	    
        	    //创建数据仓库
        	    var store =new Ext.data.Store({  //使用JSON传输入数据  	    	
        	       proxy: new Ext.data.HttpProxy({url:'console/count/uploadResources.htm?action=getUploadDataList'}),
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
						userName : Ext.getCmp('userName').getValue(),
						realName : Ext.getCmp('realName').getValue(),
						beginD : document.getElementById('beginD').value,
						endD : document.getElementById('endD').value,
						start:0,
						limit:pageSize
					}
				});
				
        	    //构造列表
        	    var cm=new Ext.grid.ColumnModel([
        	         new Ext.grid.RowNumberer(),//显示行号
        	         { header:'登录名',
        	           dataIndex:'userName',
        	           id:'userName',
        	           width: 50, 
			    	   sortable: false
        	         },{ header:'姓名',
        	           dataIndex:'realName',
        	           id:'realName',
        	           width: 50, 
			    	   sortable: false
        	         },
        	         { header:'上传数量',
        	           dataIndex:'countNum',
        	           id:'countNum',
        	           width: 50, 
			    	   sortable: true
        	         }
        	    ]);
        	    //多行选种的ID
        	    var grid=new Ext.grid.GridPanel({
				     border: false,
					 region : 'center',
					 store:store,
					 columnLines:true,
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
					   			text:'登录名：',
					   			width:15,
					   			allowBlank: false
		        			},
						 	//用户名
		        			{	
		        	        	xtype:'textfield',
		        	        	fieldLabel:'登录名',
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
					   			text:'姓名：',
					   			width:15,
					   			allowBlank: false
		        			},
		        	         //真实姓名
		        	        {	
		        	        	xtype:'textfield',
		        	        	fieldLabel:'姓名：',
		        	        	name:'realName',
		        	            id:'realName',
		        	            width:'80',
		        	            anchor:'97%'
		        	         },'-',
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
		        				text: '统 计',
		        				enableToggle: false,
		        				width:70,
		        				iconCls:'common_search', 
		        				handler: function(){
		        					var beginD = document.getElementById('beginD').value;
		        					var endD = document.getElementById('endD').value;
		        					if(beginD !=null && beginD !="" && endD !=null && endD !="" && beginD > endD){
		        						alertWarring("开始时间不能大于结束时间!");
		        						return ;
		        					}
		        					store.load();
		        				}
	        				 },'-',
	        				 {
		        				text: '导出数据至Excel',
		        				enableToggle: false,
		        				width:70,
		        				iconCls:'excel',
		        				handler: function(){	
		        					var beginD = document.getElementById('beginD').value;
		        					var endD = document.getElementById('endD').value;
		        					var userName = document.getElementById('userName').value;
		        					var realName = document.getElementById('realName').value;
		        					var dir = <%=request.getParameter("dir")%>;
		        					var sort = <%=request.getParameter("sort")%>;
		        					if(beginD !=null && beginD !="" && endD !=null && endD !="" && beginD > endD){
		        						alertWarring("开始时间不能大于结束时间!");
		        						return ;
		        					}
		        					document.location.href="<%=basePath%>console/count/uploadResources.htm?action=exportDataToExcel&beginD="+beginD+"&endD="+endD+"&userName="+userName+"&realName="+realName+"&sort="+sort+"&dir="+dir;
		        					
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
