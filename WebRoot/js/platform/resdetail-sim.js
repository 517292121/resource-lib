/**
 *	资源详细页前端处理（简洁版）
 *
 *  简洁版详细页面不再采用左右排版方式，同时取消评论与相关资源版块。
 *  另外资源主题下其它资源与资源附件默认关闭，如果需要打开加载方法中的注释即可
 *  wanglin
 *  2018-01
 */
$(function(){
	//以下版块需要哪些打开哪些即可
	
	//加载元数据信息
	getResMetadata();
	
	//加载该资源主题中其它资源 
	getResRelated();
	
	//加载资源附件
	//getAttFile();
	
	//保存—加载浏览历史（要打开该版块需要同时打开页面中注释的html标签）
	//saveBrowseHis();
	//loadBrowseHis("res/");
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
	    				html += "<li><b>" + val.dataList[i][1];
		    			if(language == 'zw'){
		    				html += "&nbsp;&nbsp;";
		    			}else{
		    				html += "：";
		    			}
		    			html += "</b> <p>" + val.dataList[i][2] + "</p></li>";
	    			}
	    		}
	    		$("#baseinfo").append(html);
		   	}catch(e){
		   		console.log(e.message);
		   	}
	    }
	});
}
function keywordJump(key){
	window.location.href = basePath + "resAdvs?COL1=rsdaKeyword&LIKE1=LIKE&KEY1=" + key;
}
function creatorJump(key){
	window.location.href = basePath + "resAdvs?COL1=rsdaCreator&LIKE1=LIKE&KEY1=" + key;
}

//加入收藏
function saveFav(){
	var _userId = $("#_userId").val();
	if(_userId == null || _userId.length < 1 || _userId == "0"){
		var ts = layer.confirm(poput_alert_logging_in_collection, {
		    btn: [operation_key_submit] //按钮
		}, function(){
			openLogin();
			layer.close(ts);
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


//获取同一主题下相关资源
function getResRelated(){
	var _rssuId = $("#_rssuId").val();
	var _rsdaId = $("#_rsdaId").val();
	if(_rssuId == null || _rssuId.length < 1){
		return;
	}
	$.ajax({
	    type: 'GET',
	    url: basePath + 'res/related/'+_rssuId+'?t=' + new Date().getTime(),
	    data: {
	    	rsdaId : _rsdaId
	    },
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
				var html = "<div class='related'>";
				html += "<p id='tool'>";
				html += "<b>"+poput_alert_other_resources+"</b>";
				html += "</p>";
				html += "<div class='wrapper'>";
				html += "<div id='focus'>";
				html += "<ul>";
				
				html += "<li>";
				
				var isLi = true;
	    		for(i=0; i<len; i++){
					var imgUrl = val.dataList[i][2];
	    			html += "<dd>";
	    			if(imgUrl == null || imgUrl.length < 1){
	    				html += "<p><a href='"+basePath+"res/"+idToStr(val.dataList[i][0])+"'><img src='"+basePath + "images/platform/doc-cover.jpg'></img></a></p>";
	    			}else{
		                html += "<p><a href='"+basePath+"res/"+idToStr(val.dataList[i][0])+"'>";
		                html += "<img src='"+val.dataList[i][2] +"' onerror=\"this.src='"+basePath+"images/platform/doc-cover.jpg'\"></img></a></p>";
	    			}
	                html += "<p class='tit'><a href='"+basePath+"res/"+val.dataList[i][0]+"'>"+val.dataList[i][1]+"</a></p>";
	                html += "</dd>";
	                if((i+5) % 5 == 4){
	                	html += "</li>";
	                	isLi = false;
		                if(i < (len-1)){
		                	html += "<li>";
		                	isLi = true;
		                }
	                }
	    		}
	    		if(isLi){
		    		html += "</li>";
	    		}
	    		html += "</ul></div></div></div>";
	    		
	    		$("#related-panel").html(html);
	    		initRelated();
		   	}catch(e){
		   	}
	    }
	});
}

//初始化该资源相同主题下资源
function initRelated(){
	var sWidth = $("#focus").width(); //获取焦点图的宽度（显示面积） 
  var len = $("#focus ul li").length; //获取焦点图个数  
  var index = 0;  
  var picTimer;  
    
  //以下代码添加数字按钮和按钮后的半透明条，还有上一页、下一页两个按钮  
  var btn = "<div class='btn'>";  
  for(var i=0; i < len; i++) {  
      btn += "<span></span>";  
  }  
  btn += "</div";  
  $("#tool").append(btn);  

  //为小按钮添加鼠标滑入事件，以显示相应的内容  
  $(".related .btn span").css("opacity",0.4).click(function() {  
      index = $(".related .btn span").index(this);  
      showPics(index);  
  }).eq(0).trigger("click");  

  //本例为左右滚动，即所有li元素都是在同一排向左浮动，所以这里需要计算出外围ul元素的宽度  
  $("#focus ul").css("width",sWidth * (len));  
    
  //鼠标滑上焦点图时停止自动播放，滑出时开始自动播放  
  $("#focus").hover(function() {  
      clearInterval(picTimer);  
  },function() {  
      picTimer = setInterval(function() {  
          showPics(index);  
          index++;  
          if(index == len) {index = 0;}  
      },6000); //此4000代表自动播放的间隔，单位：毫秒  
  }).trigger("mouseleave");  
    
  //显示图片函数，根据接收的index值显示相应的内容  
  function showPics(index) { //普通切换  
      var nowLeft = -index*sWidth; //根据index值计算ul元素的left值  
      $("#focus ul").stop(true,false).animate({"left":nowLeft},300); //通过animate()调整ul元素滚动到计算出的position  
      $(".related .btn span").stop(true,false).animate({"opacity":"0.4"},300).eq(index).stop(true,false).animate({"opacity":"1"},300);//为当前的按钮切换到选中的效果   
  }  
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
		    	
				var html = "<div class='r-panel attfile'>";
				html += "<h2 class='bcm'>"+title_resource_annex+"</h2>";
				html += "<ul>";
	    		for(i=0; i<len; i++){
	    			html += "<li>";
					html += "<dl class='subText'><a href='javascript:attDetail("+val.dataList[i][0]+")'>"+val.dataList[i][1]+"</a></dl>";
					//if(val.dataList[i][2] != null && val.dataList[i][2].length > 4){
					//	html += "<span><a href='"+fileUrl + val.dataList[i][2] +"'>"+title_download+"</a></span>";
					//}
					html += "</li>";
	    		}
	    		html += "</ul>";
	    		html += "<p class='clear' />";
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
	  area: ['600px', '400px'],
	  shade: 0.8,
	  closeBtn: 1,
	  anim : 4,
	  shadeClose: true,
	  content: basePath + "res/attdetail/" + fileId
	});
}

function parseCreator(txt){
	if(txt == '-'){
		return '-';
	}
	txt = txt.replace("，", ",");
	txt = txt.replace("；", ",");
	txt = txt.replace(";", ",");
	txt = txt.replace("、", ",");
	txt = txt.replace(" ", ",");
	
	var arr = txt.split(",");
	var result = "";
	for(var i=0; i<arr.length; i++){
		if(arr[i] != null && arr[i].length > 0) {
			result += "<a href='javascript:void(0);' onclick='creatorJump(\""+arr[i]+"\");'>" + arr[i] + "</a> &nbsp;";
		}
	}
	return result;
}











