/**
 *	全文检索前端处理
 *
 *  wanglin
 *  2018-01
 *
 */
 var comLoading = null; //数据查询加载弹出对象
$(function(){
	
	//数据异步检索
	dataSearch();
		
	//加载检索条件
	//initCondition();
});

//启动数据检索
function dataSearch(){
	var _key = $("#s_params").attr("key");
	var _creator = $("#s_params").attr("creator");
	var _publisher = $("#s_params").attr("publisher");
	var _keyword = $("#s_params").attr("keyword");
	var _pn = $("#s_params").attr("pn");
	var _match = $("#s_params").attr("match");
	
	$.ajax({
	    type: 'GET',
	    url: basePath + 'luceneSearch?t=' + new Date().getTime(),
	    data: {
	    	key : _key,
	    	creator : _creator,
	    	publisher : _publisher,
	    	keyword : _keyword,
	    	match: _match,
	    	pn : _pn
	    },
	    cache:false,
	    success: function(data){
	    	if(comLoading != null){
	    		layer.close(comLoading);
	    	}
	    	initlist(data);
	    },
	    fail: function (err, status) {
	    	$(".listbox").html(display_date_fail+":" + err);
	    }
	});
}
function initlist(data){
	try{
   		var reshtml = "";
    	var val = eval("("+data+")");
    	if(val.success == true){
    		if(val.dataList == null || val.dataList == ""){
    			cleardata();
    			return;
    		}
    		var len = val.dataList.length;
    		if(len < 1){
    			cleardata();
    			return;
    		}
    		
    		var reshtml = "";
   			var detailUrl = "";
    		for(i=0; i<len; i++){
    			if(val.dataList[i][3] == null || val.dataList[i][3].length < 1){
    				continue;
    			}
    			
    			//注：此处 的数据类型名称一定要与DataBean类中的DATA_TYPE保持一致
    			if(val.dataList[i][3] == "资源"){
    				detailUrl = "wap/resdetail/";
    			}else if(val.dataList[i][3] == "文本"){
    				detailUrl = "wap/text/";
    			}else if(val.dataList[i][3] == "资讯"){
    				detailUrl = "wap/info/detail/";
    			}
    			
    			//详细模式资源信息封装
				reshtml += "<ul class='luc-res'>";
				
				//资源封面
				var coverUrl = val.dataList[i][9];
				if(!isNull(coverUrl) && coverUrl.indexOf("file/access") == 0){
					coverUrl = basePath+coverUrl;
				}
				
				if(!isNull(coverUrl)){
					reshtml += "<div class='cover'>";
					reshtml += "<a href='"+basePath+detailUrl+idToStr(val.dataList[i][0])+"'><img src='"+coverUrl +"' onerror='this.src="+basePath+"images/platform/doc-cover.jpg' /></a>";
					reshtml += "</div>";
				}
				
				reshtml += "<div class='info'>";
				reshtml += "<li class='tit'>";
				if(val.dataList[i][4] != null && val.dataList[i][4].length > 0){
					reshtml += "<span class='bcm'>"+val.dataList[i][4]+"</span>";
				}
				reshtml += "<a href='"+basePath + detailUrl + idToStr(val.dataList[i][0]) + "'>"+val.dataList[i][1]+"</a></li>";
				
				reshtml += "<li class='des'>"+val.dataList[i][2]+"</li>";
				reshtml += "</div>";
				reshtml += "</ul>";
    		}
    		
    		var currPage = val.currentPage - 1;
    		var totalPage = val.totalPage;
    		var totalLine = val.totalLine;
    		var pageBar = getPagination(currPage, totalPage, totalLine);
    		
    		if(currPage < 1){
    			$(".reslist").html(reshtml);
    		}else{
	    		$(".reslist").append(reshtml);
    		}
    		$("#w_pages").html(pageBar);
    	}else{
    		$(".reslist").html(display_date_fail+"!<br/>" + val.msg);
    	}
   	}catch(e){
   		$(".reslist").html(display_date_fail+"!<br/>" + e.message);
   	}
} 


function cleardata(){
	var html = "<div class='blank'>未找到相关数据，换个检索词试试！</div>";
	$(".reslist").html(html);
}

//执行数据列表检索
function resSearch(){
	comLoading = layer.load(4, {
	  shade: [0.1,'#fff'] //0.1透明度的白色背景
	});
	dataSearch();
}
//分页检索
function turnPage(pageNo){
	$("#s_params").attr("pn", (pageNo+1));
	resSearch();
}
























