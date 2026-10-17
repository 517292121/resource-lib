<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	Date date = new Date();
    int year = date.getYear() + 1900;
    
    String yearList = "[0,'--不限--'],";
    for(int i=0; i<6; i++){
    	yearList += "[" + (year-i) + ",'" + (year-i) + "'],";
    }
    yearList = yearList.substring(0,yearList.length()-1);
%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
     <script type="text/javascript">
			var pageNum = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>; //每页显示条数 
			//json元数据获取路径
			var url_data_grid = '<%=path %>/console/count/resmetastore.htm?action=getMetaCountJson'; 
			//特色库资源详细统计页面
			var url_two_info = '<%=path %>/console/count/resmetastore.htm?action=getMetaTwoinitPage'; 
			
			var cm = new Ext.grid.ColumnModel([ 
				new Ext.grid.RowNumberer(),
				{header:'特色库', dataIndex:'RMSTNAME',width:110, sortable : false},
				{header:'浏览量', dataIndex:'RMSTNUM',width:110, sortable : true}
			]);
			
			var grid_store = new Ext.data.Store({
				proxy: new Ext.data.HttpProxy({url:url_data_grid}),
				reader: new Ext.data.JsonReader({
					totalProperty: 'totalProperty',
					root: 'root'
				},[
					{name:'RMSTNAME'},
					{name:'RMSTNUM'},
					{name:'RMSTID'}
					
				]),
				sortInfo : {
					field : 'RMSTNUM',
					direction : 'DESC'
				},
				remoteStore : true, // 是否远程调用数据
				// 是否远程排序
				remoteSort : true
			});
			
		    var year_store = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [<%=yearList %>]
			});
		
			var yearCombox = new Ext.form.ComboBox({
				fieldLabel : "年",
				mode : 'local',
				store : year_store,
				emptyText : '不限',
				triggerAction : 'all',
				editable : false,
				valueField : 'value',
				displayField : 'text',
				width : 70
			});
			
			var month_store = new Ext.data.SimpleStore({
				fields : ['value', 'text'],
				data : [
					['0','不限...'],
					['01','01'],
					['02','02'],
					['03','03'],
					['04','04'],
					['05','05'],
					['06','06'],
					['07','07'],
					['08','08'],
					['09','09'],
					['10','10'],
					['11','11'],
					['12','12']
				]
			});
		
			var monthCombox = new Ext.form.ComboBox({
				fieldLabel : "月",
				mode : 'local',
				store : month_store,
				emptyText : '不限',
				triggerAction : 'all',
				editable : false,
				valueField : 'value',
				displayField : 'text',
				width : 70
			});
		   grid_store.load({params:{start:0,limit:pageNum}});
     	   var grid = new Ext.grid.GridPanel({ 
				//height: document.documentElement.clientHeight, 
				enableColumnHide : false,
				autoScroll : true,
				stripeRows: true,  //隔行换颜色 
				store: grid_store, 
				border : false,
				frame : false,
				loadMask: true,     //加载时提示load... 
				viewConfig: { 
					forceFit: true 
				}, 
				cm: cm, 
				autoScroll : true,
				region : 'center',  
				tbar: new Ext.Toolbar({  //工具栏 
					//height: 200,
					//autoWidth :false,
					//width : 100,
					items:[
						'年：',
						yearCombox,
						'月：',
						monthCombox,
        				{ 
							text: ' 查 询 ', 
							enableToggle: false, 
							iconCls:'common_search', 
							handler: searchData 
						},
						{
	        				text: '查看详细',
	        				enableToggle: false,
	        				width:70,
	        				iconCls:'common_list', 
	        				handler:selectTwo
        				}
					] 
				}),
				bbar: new Ext.PagingToolbar({  //分页信息栏 
					pageSize: pageNum, 
					store: grid_store, 
					displayInfo: true, 
					displayMsg: '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>', 
					emptyMsg: '没有记录', 
					plugins: new Ext.ux.ProgressBarPager() 
				}) 
			}); 
			//二级页面跳转
			grid.on('dblclick', selectTwo);
			function selectTwo(){
        		var selectRow = grid.getSelectionModel().getSelected();
        		if(selectRow){
        			var metaId = selectRow.get("RMSTID"); 
        			var year = yearCombox.getValue();
        			var month = monthCombox.getValue();
					window.location.href ='<%=path %>/console/count/resmetastore.htm?action=getMetaTwoinitPage&MetaId='+metaId + "&year=" + year + "&month=" + month;
        		}else{
        			Ext.Msg.alert('提示','请选择要操作的列!');
        		}
			}
			
			function searchData(){
				grid_store.load({params:{
		            start:0,
		            limit:pageNum,
		            year: yearCombox.getValue(),
					month: monthCombox.getValue()
		        }}); 
			}
			
     	 
     		Ext.onReady(function(){ 
				Ext.QuickTips.init(); 
				Ext.form.Field.prototype.msgTarget = 'side';  
				
				// 创建主面板
				var displayPanel = new Ext.Viewport({
					layout : 'border',
					items : [grid]
				});
			}) 
     </script>
  </head>
  
  <body>  
  <div id ="grid"></div>  
  </body>
</html>
