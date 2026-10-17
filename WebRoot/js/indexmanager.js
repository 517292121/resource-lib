/**
 * 后台索引控制面板管理
 * wanglin
 * 2018-02
 */
 
//加载主索引文件个数 
function loadindexfile(){
	$.ajax({
	    type: 'GET',
	    url: basePath + 'console/indexManager.htm?action=getIndexFiles' ,
	    cache:false,
	    success: function(data){
	    	$("#indexFiles").html(data);			    
	    }
	});
}

//加载主索引占用空间大小
function loadindexsize(){
	$.ajax({
	    type: 'GET',
	    url: basePath + 'console/indexManager.htm?action=getIndexSize' ,
	    cache:false,
	    success: function(data){
	    	$("#indexSize").html(data);			    
	    }
	});
}

function loadResNum(){
	$.ajax({
	    type: 'GET',
	    url: basePath + 'console/indexManager.htm?action=getResNum' ,
	    cache:false,
	    success: function(data){
	    	if(data != "fail"){
	    		var nums = data.split(",");
	    		document.getElementById("resdocs").innerHTML = nums[0];
	    		document.getElementById("textdocs").innerHTML = nums[1];
	    		document.getElementById("infodocs").innerHTML = nums[2];
	    	}
	    }
	});
}
function initData(){
	loadindexfile();
	loadindexsize();
	loadResNum();
}
$(function(){ 
	initData();
}); 

var loadwin = null;

//重建文件资源索引
function reResIndex(){
	if(window.confirm('您确定要重建所有文件资源到索引吗？')){
		reIndex("reResIndex");
	};
}
//重建文本资源索引
function reTextIndex(){
	if(window.confirm('您确定要重建所有文本资源到索引吗？')){
		reIndex("reTextIndex");
	};
}
//重建信息资讯到索引
function reInfoIndex(){
	if(window.confirm('您确定要重建所有信息资讯到索引吗？')){
		reIndex("reInfoIndex");
	};
}
//重建所有索引
function reAllIndex(){
	if(window.confirm('您确定要重建所有数据到索引吗？')){
		reIndex("reAllIndex");
	};
}
// 删除无效索引
function deleteInvalidIndex(){
	if(window.confirm('您确定要删除无效索引吗？')){
		// 通知后台处理
		$.ajax({
		    type: 'GET',
		    url: basePath + 'console/indexManager.htm?action=deleteInvalidIndex',
		    cache:false,
		    success: function(data){
		    }
		});
	
		// 定时器每隔0.5秒向后台获取一次处理进度
		Ext.MessageBox.progress("处理进度", "数据预处理中...", "0%");
		let f = setInterval(function(){
			$.ajax({
				type: 'post',
				url: basePath+'progress/get',
				data: {
					'key': 'INDEX_CREATE_PROGRESS_KEY'
				},
				cache: false,
				dataType: 'json',
				success: function(rep){
					let c = rep.c;
					if(c == 1){
						let t = rep.t;
						let n = c == 2 ? rep.t:rep.n;
		           		Ext.MessageBox.updateProgress(n/t,  n+" / "+t, rep.s);
						return false;
					}
					// 未知状态直接跳过，避免后台数据过大未及时处理
					if(c < 0){
						return false;
					}
					// 删除定时器
					clearInterval(f);
					// 弹窗显示提升信息
					alert(c == 2 ? "无效索引删除完成!":rep.s);
					// 隐藏进度条
					Ext.MessageBox.hide();
					// 刷新当前页面
					window.location.reload();
				},
				error: function(){
					clearInterval(f);
				}
			});
		}, 500);
	};
}

//重建索引统一入口
function reIndex(method){
	// 通知后台进行索引重建
	$.ajax({
	    type: 'GET',
	    url: basePath + 'console/indexManager.htm?action=' + method,
	    cache:false,
	    success: function(data){
	    }
	});
	
	// 定时器每隔0.5秒向后台获取一次处理进度
	Ext.MessageBox.progress("处理进度", "数据预处理中...", "0%");
	let f = setInterval(function(){
		$.ajax({
			type: 'post',
			url: basePath+'progress/get',
			data: {
				'key': 'INDEX_CREATE_PROGRESS_KEY'
			},
			cache: false,
			dataType: 'json',
			success: function(rep){
				let c = rep.c;
				if(c == 1){
					let t = rep.t;
					let n = c == 2 ? rep.t:rep.n;
	           		Ext.MessageBox.updateProgress(n/t,  n+" / "+t, rep.s);
					return false;
				}
				// 未知状态直接跳过，避免后台数据过大未及时处理
				if(c < 0){
					return false;
				}
				// 删除定时器
				clearInterval(f);
				// 弹窗显示提升信息
				alert(c == 2 ? "索引重建完成!":rep.s);
				// 隐藏进度条
				Ext.MessageBox.hide();
				// 刷新当前页面
				window.location.reload();
			},
			error: function(){
				clearInterval(f);
			}
		});
	}, 500);
}

