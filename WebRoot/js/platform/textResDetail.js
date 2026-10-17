/**
 *	文本资源详细页前端处理
 *  wanglin
 *  2018-01
 */
$(function(){
	hotResInit("rtreAccess", "resHot");
	hotResInit("rtreDate", "resNew");
});

//初始化热门资源
function hotResInit(sort,areaid){
	$.ajax({
	    type: 'GET',
	    url: basePath + 'text/reslist?t=' + new Date().getTime(),
	    data: {
	    	pagesize: 8,
	    	sort : sort
	    },
	    cache:false,
	    success: function(data){
	    	var val = eval("("+data+")");
	    	if(val.success == true){
		    	var reshtml = "";
	    		var len = val.dataList.length;
	    		if(len < 1){
	    			return;
	    		}
	    		for(i=0; i<len; i++){
					reshtml += "<li><a href='"+basePath+"text/"+idToStr(val.dataList[i][0])+"'>"+val.dataList[i][1]+"</a></li>";
	    		}
			    $("#"+areaid).html(reshtml);
		    }
	    }
	});
}
