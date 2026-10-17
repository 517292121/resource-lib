<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!doctype html>
<html>
  <head>
    <title>layer网页弹窗/层/提示案例</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<script type="text/javascript" src="<%=path %>/js/jquery.min.js"></script>
  	<script type="text/javascript" src="<%=path %>/layer/layer.js"></script>
  	<script type="text/javascript">
  		function layerAlert(){
  			//如果不需要图标，", {icon: 3}"可以不要
  			layer.alert('内容', {icon: 3});
  		}
  		function layerConfirm(){
  			layer.confirm('您确定要执行该操作吗？', {
			    btn: ['确定','取消'] //按钮
			}, function(){
			    layer.alert('确定执行了');
			}, function(){
			  	layer.alert('取消执行了');
			});
  		}
  		function openTips(){
  			layer.msg('不开心', {icon: 5});
  		}
  		function openPage(){
  			layer.open({
		        type: 1, //type为1：content的参数按直接按内容展示，type为2：content按url路径解析
		        area: ['600px', '360px'],
		        shadeClose: true, //点击遮罩关闭
		        content: '\<\div style="padding:20px;">自定义内容\<\/div>'
		    });
  		}
  		function openFrame(){
  			layer.open({
		        type: 2,
		        title: 'iframe窗口',
		        maxmin: true,
		        shadeClose: true, //点击遮罩关闭层
		        area : ['800px' , '520px'],
		        content: 'http://www.baidu.com/'
		    });
  		}
  		function openLoading(){
  			var ii = layer.load();
  			
		    //如果需要自动关闭层，则通过以下方式指定关闭时间后关闭
		    //如果不需要自动关闭，以下代码可以不要.var ii也可以不用定义
		    setTimeout(function(){
		        layer.close(ii);
		    }, 1000);
  		}
  		
  		//此种方式官网上没有写关闭时间的处理
  		function openLoading2(){
  			layer.msg('数据提交中...', {icon: 16});
  		}
  		
  	</script>
  	
  </head>
  
  <body>
    <h2>layer网页弹窗/层/提示案例</h2><hr>
    <input type="button" value="alert提示" onclick="layerAlert();">
    <input type="button" value="confirm询问" onclick="layerConfirm();">
    <input type="button" value="简单提示" onclick="openTips();">
    <input type="button" value="弹出一个页面层" onclick="openPage();">
    <input type="button" value="弹出一个iframe层" onclick="openFrame();">
    <input type="button" value="弹出一个loading层" onclick="openLoading();">
    <input type="button" value="弹出一个loading层风格二" onclick="openLoading2();">
    
    <p><h4>使用说明：</h4></p>
    <p>其实此插件使用非常方便，只需要导入jquery.js(1.8版本以上)和layer.js即可使用</p>
	<p>其中lzyer文件夹需要整个导入项目中，其结构不能变</p>
	<p>更多示例请参考：<a href="http://layer.layui.com/">http://layer.layui.com/</a></p> 
	<p>
		其中，icon编号对应的图标为<br>
		1：正确 <br>
		2：错误 <br>
		3：问号 <br>
		4：锁 <br>
		5：表情—红色难过 <br>
		6：表情—绿色难过 <br>
		大于等于7：感叹号 <br>
	</p>   
  </body>
</html>
