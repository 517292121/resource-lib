<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
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
		
		var counterRecords = new Ext.data.Record.create([
			{name:'year', type:'string'},
			{name:'accesscount', type:'string'}
		]);
		
		// 页面数据仓库
	    var store = new Ext.data.Store({
	        reader: new Ext.data.JsonReader({}, counterRecords),
	        proxy:new Ext.data.HttpProxy({url:'<%=basePath%>console/data/accessCounter.htm?action=getAccessCounterList'}),
	        remoteStore:true,    // 是否远程调用数据
	        remoteSort:false      // 是否远程排序
	    });
	    
	    var cm = new Ext.grid.ColumnModel([
	    	new Ext.grid.RowNumberer(),
	    	{
	    		id:'year', 
	    		header: '年份', 
	    		width: 100, 
	    		sortable: true, 
	    		dataIndex:'year'
            },{
	    		id:'accesscount', 
	    		header: '访问量', 
	    		width: 100, 
	    		sortable: true, 
	    		dataIndex:'accesscount'
            }
	    ]);

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
        		items:['统计年份：',
        			{
        				xtype:'numberfield',
        				emptyText:'开始年',
        				allowDecimals:false,
        				allowNegative:false,
        				id:'beginYear',
        				width:60
        			},'—',{
        				xtype:'numberfield',
        				emptyText:'结束年',
        				allowDecimals:false,
        				allowNegative:false,
        				id:'endYear',
        				width:60
        			},'-',{
        				text: '&nbsp;查&nbsp;询&nbsp;',
        				enableToggle: false,
        				iconCls:'common_search',
        				handler: function(){
        					var begin = Ext.getCmp('beginYear').getValue();
        					var end = Ext.getCmp('endYear').getValue();
        					if(begin<1970 || begin>2030){
        						alertWarring("请输入开始年份！");
        						return;
        					}
        					if(end<1970 || end>2030){
        						alertWarring("请输入结束年份！");
        						return;
        					}
        					if(begin>end){
        						alertWarring("开始年份不能大于结束年份！");
        						return;
        					}
        					store.load({params:{ 
        						beginYear:begin, 
        						endYear:end
        					}});
        				}
        			}
        		]
        	})
	    });
	    
	    store.load({
			params : {
				start : 0,
				limit : LIMIT
			}
		});
	      
		var displayPanel = new Ext.Viewport({
			renderTo : 'mainDiv',
			layout   : 'border',
			items 	 : [grid]
		});
	});
	</script>
  </head>
  
  <body>
      <div id="mainDiv"></div>
  </body>
</html>
