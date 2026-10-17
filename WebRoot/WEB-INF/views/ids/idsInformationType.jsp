<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>院系列表</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<script type="text/javascript" src="<%=basePath %>/js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>/js/platform/common.js"></script>
    <link rel="stylesheet" href="<%=basePath %>/plugin/layui/css/layui.css">
    <style type="text/css">
    	.layui-unselect {height: 30px;}
    	.layui-form-select dl {top: 38px;}
    	.layui-form-select dl dd, .layui-form-select dl dt {line-height: 30px;}
    	.layui-form-select dl dd.layui-this {
		    background-color: #1e9fff;
		}
		.layui-btn-container .layui-btn {
		    margin-bottom: 0px;
		    vertical-align: baseline;
		}
		.ew-tree-table-arrow.ew-tree-table-arrow-hide {
		    display: none;
		}
    </style>
  </head>
  
	<body style="padding: 0px;">
		<div class="page-wrapper">
			<div style="background: #d5e2f2;border-bottom: 1px solid #8db2e3;">
		    	<form class="layui-form" style="display: inline-block;width: 100px;padding: 10px 0px 10px 5px;">
			    	<div class="layui-inline">
				      <div class="layui-input-inline">
				        <select name="lan" id="lan" style="height:30px;" lay-verify="required" lay-filter="lanChange">
				          <option value="zh">中文</option>
				          <option value="zw">藏文</option>
				          <option value="mw">蒙文</option>
				          <option value="en">英文</option>
				        </select>
				      </div>
				    </div>
			  	</form>
		   		<input class="layui-input" id="edtSearch" placeholder="输入关键字" style="display: inline-block;width: 200px;height: 30px;line-height: 30px;padding: 10px 5px;margin-right: 5px;vertical-align: middle;"/>
			    <div class="layui-btn-container" style="display: inline-block;padding: 10px 0px;">
			        <button id="btnSearch" class="layui-btn layui-btn-sm layui-btn-primary">
			            <i class="layui-icon">&#xe615;</i>搜索
			        </button>
			    </div>
				<div class="layui-btn-container" style="display: inline-block;padding: 10px 0px;">
					<a href="javaScript:openWin('新增', '<%=basePath %>/console/data/idsInformationType.htm?action=edit', '500px', '240px');" class="layui-btn layui-btn-sm layui-btn-primary">
			            <i class="layui-icon">&#xe654;</i>新增
			        </a>
				</div>
			</div>
		    <table id="demoTreeTable1"></table>
		</div>

		<!-- 表格操作列 -->
		<script type="text/html" id="demoTreeTableBar1">
    		<a class="layui-btn layui-btn-primary layui-btn-xs" lay-event="edit">修改</a>
			{{#if(!d.children || d.children.length<1){}}
				<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del" lay-filter="ckState">删除</a> 
			{{#  } }}
		</script>

		<!-- 加载层 -->
		<div class="page-loading">
		    <div class="ball-loader sm">
		        <span></span><span></span><span></span><span></span>
		    </div>
		</div>
    	${message }
	</body>
  	<script src="<%=basePath %>/plugin/layui/layui.js" type="text/javascript"></script>
	<script type="text/javascript">
		//参考网站: https://gitee.com/whvse/treetable-lay/
		var layer;
		var insTb;
	    layui.config({
	        base: '<%=basePath %>/plugin/'
	    }).extend({
	        treeTable: 'treeTable/treeTable'
	    }).use(['form', 'layer', 'util', 'treeTable'], function () {
	    	
	    	var $ = layui.jquery;
	    	layer = layui.layer;
	        var util = layui.util;
	        var treeTable = layui.treeTable;
			var url = "<%=basePath %>/console/data/idsInformationType.htm?action=";
	        // 渲染表格
	        insTb = treeTable.render({
	            elem: '#demoTreeTable1',
	            tree: {
	                iconIndex: 1,
	                idName: 'id',
	                pidName: 'pid',
	                isPidData: true,
	                getIcon: function (d) {
                        return '';
	                }
	            },
	            cols: [
	                {type: 'numbers'},
	                {field: 'name', title: '分类名'},
	                {field: 'index', title: '序号', width: 80, align: 'center',},
	                {toolbar: '#demoTreeTableBar1', title: '操作', width: 220}
	            ],
	            reqData: function (data, callback) {
                    $.getJSON(url+"list&lan="+$("#lan").val(), function (res) {
                    	if(!res.success){
                    		layer.msg("请求错误，请联系管理员!", {icon: 2});
                    		return false;
                    	}
                        callback(res.data);
                    });
	            },
	            style: 'margin-top:0;',
	            is_cache: true,
	        });
	
	        //监听工具栏
	        treeTable.on('tool(demoTreeTable1)', function (obj) {
	            var event = obj.event;
	            var data = obj.data;
	            if (event == 'del') {	//删除
	            	$.getJSON(url+"del&id="+data.id, function(req){
	            		if(!req.success){
	            			layer.msg(req.msg, {icon: 2});
	            			return false;
	            		}
	            		insTb.reload();
		                layer.msg('删除成功', {icon: 1});
	            	});
	            } else if (event == 'edit') {
	            	openWin('编辑', url+'edit&id='+data.id, '500px', '240px');
	            }
	        });
	
	        //使用页面检索，不执行数据库检索
	        $('#btnSearch').click(function () {
	            var keywords = $('#edtSearch').val();
	            if (keywords) {
	                insTb.filterData(keywords);
	            } else {
	                insTb.clearFilter();
	            }
	        });
	        
	        var form = layui.form;
	      	//监听指定开关
	        form.on('select(lanChange)', function(data){
	        	reloadTbale();
	        });
	    });
    
    	//treetable表格刷新，根据msg弹出响应的提示
    	function reloadTbale(msg){
    		if(!isEmpty(msg)){
	   			layer.msg(msg, {icon: 1});
    		}
    		insTb.reload();
    	}
    	
    	function openWin(title, url, w, h){
    		var win = layer.open({
    	        type: 2,
    	        title: title, //不显示标题栏
    	        area: [w, h],
    	        shade: 0.2,
    	        id: (new Date()).valueOf(), //设定一个id，防止重复弹出 时间戳1280977330748
    	        content: url
    	    });
    		
    		return win;
    	}
	</script>
</html>
