//图片资源预览时，弹出的数据检索子页面处理
var comLoading = null; //数据查询加载弹出对象
var imgDir = "";
$(function(){
	$("#key").focus(function(){
		$("#key").css('background-color','#FFF');
	});
	$("#key").blur(function(){
		var sval = $("#key").val(); 
		if(sval == null || sval.length < 1){
 			$("#key").css('background-color','#2A2A2A');
		}				
	});
	
	var bodyHei = $(window).height();
	$(".info-list-f").height((bodyHei-122) + "px");
	
	$('#s-btn').click(function(){
		getContext();
	});  
	
	$("#key").bind('keypress',function(event){  
        if(event.keyCode == "13") {  
			getContext();
        }  
    });
    
    imgDir = $("#imgDir").val();
})
		
//搜索
function getContext(){
	var v = $("#key").val();
	if(v.length < 1){
		alert("search key");
		return ;
	}
	comLoading = layer.load(4, {
	  	shade: [0.1,'#fff'] //0.1透明度的白色背景
	});
	datasearch(v);
};
 
function datasearch(key){
    var rsdaId = $("#rsdaId").val();
    var match = $('input[name="match"]:checked').val();
	$.ajax({
	 	type: 'post',
	 	url: basePath + "lucene/pageSearch",
	 	data:{
	 		"resId": rsdaId,
	 		"key": key,
	 		"encode" : 1,
	 		"match" : match,
	 		"pn" : 0
	 	},
	 	dataType:"json",
	 	success:function(data){
	 		if(data.success){
	 				$("#keys").val(data.keys);
			 		//json书籍
			 		var reslist = data.dataList;
			 		if(reslist == null || reslist.length < 1){
			 			blankdata(resource_preview_not_data);
			 			return;
			 		}
			 		$(".s-result").html(resource_preview_total_page+data.totalLine+resource_preview_page);
			 		
			 		var html = "";
			 		for(var i = 0;i<reslist.length;i++){
			 			html += "<li>";
			    		html += 	"<p><a href='javascript:toPage(\""+reslist[i].titleEncode+"\",\""+ reslist[i].title +"\");'>" + fpnum(reslist[i].title) +"</a></p>";
			    		html += 	"<p class='ms'>"+reslist[i].content+"</p>";
			    		html += "</li>";
			 		}
			 		$("#reslist").html(html);
			 		if(comLoading != null){
				   		layer.close(comLoading);
				   	}
	 		}else{
	 			blankdata(data.msg);
	 		}
	 	},error:function(e){
	 		blankdata("请求错误！");
	 	}
 	});	
}
function blankdata(msg){
	if(comLoading != null){
   		layer.close(comLoading);
   	}
   	$("#reslist").html("<li>"+msg+"</li>");
   	$(".s-result").html(resource_preview_total_page+"0"+resource_preview_page);
}

function toPage(fname, title){
	var xmlpath = imgDir + fname + ".xml";
	var v = "";
	var match = $('input[name="match"]:checked').val();
	if(match == 1){
		v = $("#key").val();
	}else{
		v = $("#keys").val();
	}
	window.parent.catalog(title,xmlpath,v);
} 
 
 