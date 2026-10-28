
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};
/**
 *	资源列表页前端处理
 *  wanglin
 *  2018-01
 */
var VIEW_PANEL_COOKIE = "RESLISTVIEWPANEL";
var comLoading = null; //数据查询加载弹出对象
$(function(){

	//初始化资源列表三种预览模式框架效果
	pageInit(); 
	
	//加载资源
	loadResType();
	
	//根据特色库ID加载对应资源类型
	resQuery();
	
	//添加右边连接点击后选中样式
	$(".nav a").click(function(){
		$(this).addClass('active').siblings().removeClass('active');
	})
});


//初始化资源列表三种预览模式框架效果
function pageInit(){
	$(".listbox").height($(".listbox #res1").height());
	
	var resWid = $(".listbox").width();
	var panels = $(".listbox ul").length;
	var countWid = resWid * panels;
	$(".listbox #box").width(countWid);
	$(".toolbar #l a").click(function(){
		var myNum = $(this).index();
		var moveLeft = myNum * resWid * -1;
		$(".listbox #box").animate({'left':moveLeft}, 300);
		
		var hei = $(".listbox #res"+(myNum+1)).height();
		$(".listbox").height(hei);
		
		//缓存用户预览模式编号
		$.cookie(VIEW_PANEL_COOKIE, myNum, { expires: 30 });
	})
	initPanel();
}
function initPanel(){
	var viewNum = $.cookie(VIEW_PANEL_COOKIE);
	if(viewNum == null || viewNum.length < 1 || viewNum == '0'){
		return;
	}
	viewNum = parseInt(viewNum);
	
	var resWid = $(".listbox").width();
	var moveLeft = viewNum * resWid * -1;
	$(".listbox #box").animate({'left':moveLeft}, 0);
	$(".listbox").height($(".listbox #res"+(viewNum+1)).height());
	$(".toolbar #l #"+(viewNum+1)).addClass("active").siblings().removeClass('active');
}



//根据资源库加载对应的资源类型
function loadResType(){
	var _rmstId = $("#_rmstId").val();
	if(isNull(_rmstId) || _rmstId.length < 1){
		return;
	}
	$.ajax({
	    type: 'GET',
	    url: basePath + '/restype/'+_rmstId+'?t=' + new Date().getTime(),
	    cache:false,
	    success: function(data){
	    	try{
		    	var val = eval("("+data+")");
	    		var len = val.dataList.length;
	    		if(len < 1){
	    			return;
	    		}
				var html = "<a href='javascript:rltyQuery(0);' id='all' style='margin-left: 0px' class='active'>ALL</a>";
	    		for(i=0; i<len; i++){
	    			html += "<a href='javascript:rltyQuery("+val.dataList[i][0]+");'>"+val.dataList[i][1]+"</a>";
	    		}
	    		html += "<li class='clear'></li>";
	    		$("#restype").html(html);
	    		
	    		$("#restype a").click(function(){
					$(this).addClass('active').siblings().removeClass('active');
				})
		   	}catch(e){
		   		
		   	}
	    },
	    fail: function (err, status) {
	    }
	});
}

//检索资源
function resQuery(){
	var _rmstId = $("#_rmstId").val();
	var _rltyId = $("#_rltyId").val();
	var _rstyId = $("#_rstyId").val();
	var _tchar = $("#_tchar").val();
	var _rdtyAId = $("#_rdtyAId").val();
	var _rdtyBId = $("#_rdtyBId").val();
	var _rdtyCId = $("#_rdtyCId").val();
	var _sort = $("#_sort").val();
	var _pn = $("#_currentPage").val();
	$.ajax({
	    type: 'GET',
	    url: basePath + '/res/db/list?t=' + new Date().getTime(),
	    data: {
	    	rmstId : _rmstId,
	    	rltyId : _rltyId,
	    	rstyId : _rstyId,
	    	tchar : _tchar,
	    	rdtyAId : _rdtyAId,
	    	rdtyBId : _rdtyBId,
	    	rdtyCId : _rdtyCId,
	    	sort : _sort,
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
		$(".listbox").html(uiText("数据加载失败:") + err);
	    }
	});
}
//初始化资源列表内容，与resQuery方法配合使用
function initlist(data){
   	try{
   		var res1html = "<table cellpadding='0' cellspacing='0' border='0'>";
   		res1html += "<tr id='tou'>";
   		res1html += "<th class='tm'>"+title_name+"</th>";
		res1html += "<th class='zrz'>"+title_input_Creator+"</th>";
		res1html += "<th class='rq'>"+title_create_date+"</th>";
		res1html += "</tr>";
		
		var res2html = "";
   		var res3html = "";
    	var val = eval("("+data+")");
    	if(val.success == true){
    		var json = "";
    		var len = val.dataList.length;
    		if(len < 1){
    			$(".listbox #box #res1").html(title_not_related_resources);
    			$(".listbox #box #res2").html(title_not_related_resources);
    			$(".listbox #box #res3").html(title_not_related_resources);
    			return;
    		}
    		for(i=0; i<len; i++){
    			//列表模式资源信息封装
    			if(i%2 == 1){
    				res1html += "<tr class='c'>";
    			}else{
	    			res1html += "<tr>";
    			}
				res1html += "<td class='tm'><a href='"+basePath+"res/"+idToStr(val.dataList[i][0])+"'>"+ (i+1) + "、" + val.dataList[i][1]+"</a></td>";
				res1html += "<td class='zrz'>"+nullToBlank(val.dataList[i][2], '-')+"</td>";
				res1html += "<td class='rq'>"+val.dataList[i][8]+"</td>";
				res1html += "</tr>";
				
				//详细模式资源信息封装
				res2html += "<div class='item'>";
				if(isNull(val.dataList[i][7])){
					res2html += "<div class='fm'><a href='"+basePath+"res/"+idToStr(val.dataList[i][0])+"'><img src='"+basePath+"images/platform/doc-cover.jpg' /></a></div>";
				}else{
					res2html += "<div class='fm'><a href='"+basePath+"res/"+idToStr(val.dataList[i][0])+"'><img src='"+val.dataList[i][7] +"' onerror=\"this.src='"+basePath+"images/platform/doc-cover.jpg'\" /></a></div>";
				}
				res2html += "<div class='xx'>";
					res2html += "<p><a href='"+basePath+"res/"+idToStr(val.dataList[i][0])+"'>"+val.dataList[i][1]+"</a></p>";
					res2html += "<li><b>"+title_input_responsible_person+"： </b>"+parseLink(nullToBlank(val.dataList[i][2], '-'), 'creator')+" &nbsp; "+title_input_name+"："+nullToBlank(val.dataList[i][3], '-')+"</li>";
					res2html += "<li class='ms'><b>"+title_describe+"：</b> "+subTxt(val.dataList[i][5], 120)+"</li>";
					res2html += "<li><b>"+title_input_Keyword+"： </b>"+parseLink(nullToBlank(val.dataList[i][6], '-'), 'keyword')+"</li>";
					res2html += "<dl><b>"+title_date+"： </b>"+val.dataList[i][8]+"  &nbsp; "+title_amount_of_access+"："+val.dataList[i][9]+"</dl>";
				res2html += "</div>";
				res2html += "</div>";
				
				//表格模式资源信息封装
				if(i%5 == 0){
    				res3html += "<div class='item' style='margin-left:0px;'>";
    			}else{
					res3html += "<div class='item'>";
    			}
				if(isNull(val.dataList[i][7])){
					res3html += "<p><a href='"+basePath+"res/"+idToStr(val.dataList[i][0])+"'><img src='"+basePath+"images/platform/doc-cover.jpg' /></a></p>";
				}else{
					res3html += "<p><a href='"+basePath+"res/"+idToStr(val.dataList[i][0])+"'><img src='"+ val.dataList[i][7] +"' onerror=\"this.src='"+basePath+"images/platform/doc-cover.jpg'\" /></a></p>";
				}
				res3html += "<li><a href='"+basePath+"res/"+idToStr(val.dataList[i][0])+"'>"+val.dataList[i][1]+"</a></li>";
				res3html += "</div>";
    		}
    		res1html += "</table>";
    		res3html += "<p class='clear' />";
    		
    		var currPage = val.currentPage;
    		var totalPage = val.totalPage;
    		var totalLine = val.totalLine;
    		var pageBar = getPagination(currPage, totalPage, totalLine);
    		res1html += pageBar;
    		res2html += pageBar;
    		res3html += pageBar;
    		
    		$(".listbox #box #res1").html(res1html);
    		$(".listbox #box #res2").html(res2html);
    		$(".listbox #box #res3").html(res3html);
    		image_rendering();
    		var currBoxId = $(".toolbar #l .active").attr("id");
    		$(".listbox").height($(".listbox #res" + currBoxId).height());
    	}else{
    		$(".listbox #box #res1").html(display_date_fail+"!<br/>" + val.msg);
    		$(".listbox #box #res2").html(display_date_fail+"!<br/>" + val.msg);
    		$(".listbox #box #res3").html(display_date_fail+"!<br/>" + val.msg);
    	}
   	}catch(e){
   		$(".listbox #box #res1").html(display_date_fail+"：" + e.message);
   		$(".listbox #box #res2").html(display_date_fail+"：" + e.message);
   		$(".listbox #box #res3").html(display_date_fail+"：" + e.message);
   	}
}

function parseLink(txt, t){
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
			result += "<a href='javascript:void(0);' onclick='"+t+"Jump(\""+arr[i]+"\");'>" + arr[i] + "</a> &nbsp;";
		}
	}
	return result;
}
function keywordJump(key){
	window.location.href = basePath + "resAdvs?COL1=rsdaKeyword&LIKE1=LIKE&KEY1=" + key;
}
function creatorJump(key){
	window.location.href = basePath + "resAdvs?COL1=rsdaCreator&LIKE1=LIKE&KEY1=" + key;
}



//封装检索
function resSearch(){
	window.location.href = "#cntline";
	comLoading = layer.load(4, {
	  shade: [0.1,'#fff'] //0.1透明度的白色背景
	});
	resQuery();
}
function rmstQuery(id){
	$("#_rmstId").val(id);
	$("#_currentPage").val("0");
	resSearch();
	$("#ku_"+id).addClass('curr').siblings().removeClass('curr');
}
function rltyQuery(id){
	$("#_rltyId").val(id);
	$("#_currentPage").val("0");
	resSearch();
}
function rdtyQuery(id, type){
	if(type == 1){
		$("#_rdtyAId").val(id);
	} else if(type == 2){
		$("#_rdtyBId").val(id);
	} else if(type == 3){
		$("#_rdtyCId").val(id);
	}
	$("#_currentPage").val("0");
	resSearch();
}
function rstyQuery(id){
	$("#_rstyId").val(id);
	$("#_currentPage").val("0");
	resSearch();
}
function charQuery(c){
	$("#_tchar").val(c);
	$("#_currentPage").val("0"); //除点分页以外，其它条件都要将页码重置为0
	resSearch();
}
function sortQuery(sort){
	$("#_sort").val(sort);
	$("#_currentPage").val("0");
	resSearch();
}
function turnPage(pageNo){
	$("#_currentPage").val(pageNo);
	resSearch();
}

/**
 *	表格模式图片渲染
 */
function image_rendering(){
	 $(".listbox #box #res3 .item p img").mouseenter(function(){
         var w = $(this).width(),
         Gleft = 152-w;
         if(Gleft < -10){
             $(this).animate({left:Gleft},500);
         }    
     }).mouseleave(function(){
         $(this).animate({left:0},500);
     })
     
     $(".listbox #box #res2 .item .fm a img").mouseenter(function(){
         var w = $(this).width(),
         Gleft = 150-w;
         if(Gleft < -10){
             $(this).animate({left:Gleft},500);
         }    
     }).mouseleave(function(){
         $(this).animate({left:0},500);
     })
}
