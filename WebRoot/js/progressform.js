$(function(){
	var prompt = $(".prompt-layer");
	var elem = "<div class='progress'>" +
				"<div class='bar'></div>" +
				"<div class='percent'>0%</div>" +
			   "</div>数据提交中...";
	prompt.html(elem);
	
	var bar = $('.bar');
	var percent = $('.percent');
	
	var proWin = null;
	$('#sourceform').ajaxForm({
		beforeSerialize : function() {
//			 alert("表单数据序列化前执行的操作！");
			// $("#txt2").val("java");//如：改变元素的值
		},
		beforeSubmit : function() {
			
		},
		
		beforeSend : function() {
			proWin = layer.open({
				  type: 1,
				  title: false,
				  closeBtn: 0,
				  area: ['400px', '100px'],
				  shadeClose: false,
				  content: $('.prompt-layer')
			});
			var percentVal = '0%';
			bar.width(percentVal)
			percent.html(percentVal);
		},
		
		uploadProgress : function(event, position, total, percentComplete) {// 上传的过程
			// position 已上传了多少
			// total 总大小
			// 已上传的百分数
			var percentVal = percentComplete + '%';
			bar.width(percentVal)
			percent.html(percentVal);
			// console.log(percentVal, position, total);
		},
		
		success : function(data) {// 成功
			var percentVal = '100%';
			bar.width(percentVal)
			percent.html(percentVal);
			layer.close(proWin);
		},
		
		error : function(err) {//失败
			layer.close(proWin);
			alert("数据提交异常！" + err.msg);
		},
		
		complete : function(xhr) {//完成
			layer.close(proWin);
			var result = xhr.responseText;
			var data = eval('(' + result + ')');
			if(data.success == true){
				layer.confirm('保存成功，是否返回列表？', {
					btn: ['返回','取消'] //按钮
				}, function(){
				  	goback();
				}, function(){
				 
				});
			}else{
				layer.msg(data.msg, {icon:2,time:5000});
			}
		}
	});
})

