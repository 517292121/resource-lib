/**
 *	资源详细页前端处理（WAP版）
 *
 *  wanglin
 *  2018-03
 */
$(function(){
	//加载元数据信息
	getResMetadata();
	
	//加载资源附件
	getAttFile();
	
	//加载相关资源
	getSimilarRes();
	
	//保存—加载浏览历史
	saveBrowseHis();     
	loadBrowseHis("wap/resdetail/");
});

//加载资源元数据信息
function getResMetadata(){
	var _rsdaId = $("#_rsdaId").val();
	$.ajax({
	    type: 'GET',
	    url: basePath + 'res/meta/'+_rsdaId+'?t=' + new Date().getTime(),
	    cache:false,
	    success: function(data){
	    	try{
		    	var val = eval("("+data+")");
	    		var len = val.dataList.length;
	    		if(len < 1){
	    			return;
	    		}
				var html = "";
				var eleCode = "";
	    		for(i=0; i<len; i++){
	    			eleCode = val.dataList[i][0];
	    			if(eleCode != null){
	    				eleCode = eleCode.toLowerCase();
	    			}
	    			if(eleCode != "title"){
		    			html += "<li><b>" + val.dataList[i][1] + " ("+val.dataList[i][0]+")：</b> <p>" + val.dataList[i][2] + "</p></li>";
	    			}
	    		}
	    		
	    		if(len < 4){
	    			$(".w_base .base").append(html);
	    			$(".w_metadata").remove();
	    			$(".w_line").remove();
	    		}else{
		    		$("#metalist").html(html);
	    		}
		   	}catch(e){
		   	}
	    }
	});
} 

function keywordJump(key){
	return false;
}
function creatorJump(key){
	return false;
}


//获取相关资源（按相同资源库与资源类型获取）
function getSimilarRes(){
	var _rsdaId = $("#_rsdaId").val();
	var _rmstId = $("#_rmstId").val();
	var _rltyId = $("#_rltyId").val();
	$.ajax({
	    type: 'GET',
	    url: basePath + 'res/similar?t=' + new Date().getTime(),
	    data : {
	    	rsdaId : _rsdaId,
	    	rmstId : _rmstId,
	    	rltyId : _rltyId
	    },
	    cache:false,
	    success: function(data){
	    	try{
	    		if(data == null || data.length < 3){
	    			return;
	    		}
		    	var val = eval("("+data+")");
	    		var len = val.dataList.length;
	    		if(len < 1){
	    			return;
	    		}
				var html = "<div class='similar'>";
				html += "<h2>"+title_related_resources+"</h2>";
				html += "<ul>";
	    		for(i=0; i<len; i++){
					var imgUrl = val.dataList[i][3];
					var id = val.dataList[i][0];
					
	    			html += "<li>";
	    			if(imgUrl != null && imgUrl.length > 1){
						html += "<dl class='cover'><a href='"+basePath+"wap/resdetail/"+idToStr(id)+"'>";
		                html += "<img src='"+ imgUrl +"' onerror=\"this.src='"+basePath+"images/platform/doc-cover.jpg'\"></img>";
						html += "</a></dl>";
	    			}
					html += "<dl class='info'>";
					html += "<p><a href='"+basePath+"wap/resdetail/"+idToStr(id)+"'>"+subTxt(val.dataList[i][1], 30)+"</a></p>";
					html += "<p class='subText' title='"+val.dataList[i][2]+"'>"+title_input_responsible_person+"："+ nullToBlank(val.dataList[i][2],'-')+"</p>";
					html += "</dl>";
					html += "</li>";
	    		}
	    		html += "</ul>";
	    		html += "</div>";
	    		$("#similarbox").html(html);
		   	}catch(e){
		   		alert(e.message);
		   	}
	    }
	});
}

//获取资源附件
function getAttFile(){
	var _rsdaId = $("#_rsdaId").val();
	$.ajax({
	    type: 'GET',
	    url: basePath + 'res/attfile/'+_rsdaId+'?t=' + new Date().getTime(),
	    cache:false,
	    success: function(data){
	    	if(data == null || data.length < 1){
	    		return;
	    	}
	    	try{
		    	var val = eval("("+data+")");
	    		var len = val.dataList.length;
	    		if(len < 1){
	    			return;
	    		}
	    		var totalnums = val.totalnum;
		    	
				var html = "<div class='attfile'>";
				html += "<h2>"+title_resource_annex+"</h2>";
				html += "<ul>";
	    		for(i=0; i<len; i++){
	    			html += "<li>";
					html += "<dl class='subText'><a href='javascript:attDetail("+val.dataList[i][0]+")'>"+val.dataList[i][1]+"</a></dl>";
					if(val.dataList[i][2] != null && val.dataList[i][2].length > 4){
						html += "<span><a href='"+fileUrl + val.dataList[i][2] +"'>"+title_download+"</a></span>";
					}
					html += "</li>";
	    		}
	    		html += "</ul>";
	    		html += "</div>";
	    		$("#attachfile").html(html);
		   	}catch(e){
		   		$("#attachfile").html("<li class='nodata'>"+display_data_loading_failure+"："+e.message+"</li>");
		   	}
	    }
	});
}
function attDetail(fileId){
	layer.open({
	  type: 2,
	  title: false,
	  area: ['80%', '400px'],
	  shade: 0.8,
	  closeBtn: 1,
	  anim : 4,
	  shadeClose: true,
	  content: basePath + "res/attdetail/" + fileId
	});
}

//加入收藏
function saveFav(){
	var _userId = $("#_userId").val();
	if(_userId == null || _userId.length < 1 || _userId == "0"){
		var ts = layer.confirm(poput_alert_logging_in_collection, {
		    btn: [operation_log_right_away, operation_collection_time_being] //按钮
		}, function(){
			window.location.href = basePath + "wap/login";
		}, function(){
			
		});	
		return;
	}
	var _rsdaId = $("#_rsdaId").val();
	$.ajax({
	    type: 'GET',
	    url: basePath + 'res/favorite?t=' + new Date().getTime(),
	    data : {
	    	rsdaId : _rsdaId
	    },
	    cache:false,
	    success: function(data){
	    	try{
		    	var val = eval("("+data+")");
	    		if(val.success == true || val.success == "true"){
					layer.msg(poput_alert_collection_success, {icon:1});
	    		}else{
	    			layer.alert(val.errmsg);
	    		}
		   	}catch(e){
		   		layer.msg(poput_alert_error + e.message, {icon:2});
		   	}
	    }
	});
}

