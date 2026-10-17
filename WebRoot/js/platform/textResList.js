/**
 *	文本资源列表页前端处理
 *  wanglin
 *  2018-01
 */
var VIEW_PANEL_COOKIE = "TEXTLISTVIEWPANEL";
var comLoading = null; //数据查询加载弹出对象
$(function(){

	//初始化资源列表三种预览模式框架效果
	pageInit(); 
	
	//检索资源
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
		
		var currpage = (myNum == 0) ? 1 : 3;
		var hei = $(".listbox #res"+currpage).height();
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
	var currpage = (viewNum == 0) ? 1 : 3;
	
	//alert("viewNum:" + viewNum + "  currpage:" + currpage);
	var resWid = $(".listbox").width();
	var moveLeft = viewNum * resWid * -1;
	$(".listbox #box").animate({'left':moveLeft}, 0);
	$(".listbox").height($(".listbox #res"+currpage).height());
	$(".toolbar #l #"+currpage).addClass("active").siblings().removeClass('active');
}

//检索资源
function resQuery(){
	var _rtliId = $("#_rtliId").val();
	var _rlclId = $("#_rlclId").val();
	var _sort = $("#_sort").val();
	var _pn = $("#_currentPage").val();
	$.ajax({
	    type: 'GET',
	    url: basePath + 'text/reslist?t=' + new Date().getTime(),
	    data: {
	    	rtliId : _rtliId,
	    	rlclId : _rlclId,
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
	    	$(".listbox").html(display_date_fail+":" + err);
	    }
	});
}
//初始化资源列表内容，与resQuery方法配合使用
function initlist(data){
   	try{
   		var res1html = "<table cellpadding='0' cellspacing='0' border='0'>";
   		res1html += "<tr id='tou'>";
   		res1html += "<th class='tm'>"+title_name+"</th>";
		res1html += "<th class='rq'>"+title_create_date+"</th>";
		res1html += "</tr>";
		
		var res2html = "";
    	var val = eval("("+data+")");
    	if(val.success == true){
    		var json = "";
    		var len = val.dataList.length;
    		if(len < 1){
    			$(".listbox #box #res1").html(title_not_related_resources);
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
				res1html += "<td class='tm'><a href='"+basePath+"text/"+idToStr(val.dataList[i][0])+"'>"+ (i+1) + "、" + val.dataList[i][1]+"</a></td>";
				res1html += "<td class='rq'>"+val.dataList[i][4]+"</td>";
				res1html += "</tr>";
				
				//表格模式资源信息封装
				res2html += "<div class='item'>";
				if(isNull(val.dataList[i][2])){
					res2html += "<p><a href='"+basePath+"text/"+idToStr(val.dataList[i][0])+"'><img src='"+basePath+"images/platform/doc-cover.jpg' /></a></p>";
				}else{
					res2html += "<p><a href='"+basePath+"text/"+idToStr(val.dataList[i][0])+"'><img src='"+fileUrl + val.dataList[i][2] +"' onerror='this.src="+basePath+"images/platform/doc-cover.jpg' /></a></p>";
				}
				res2html += "<li><a href='"+basePath+"text/"+idToStr(val.dataList[i][0])+"'>"+val.dataList[i][1]+"</a></li>";
				res2html += "</div>";
    		}
    		res1html += "</table>";
    		res2html += "<p class='clear' />";
    		
    		var currPage = val.currentPage;
    		var totalPage = val.totalPage;
    		var totalLine = val.totalLine;
    		var pageBar = getPagination(currPage, totalPage, totalLine);
    		res1html += pageBar;
    		res2html += pageBar;
    		
    		$(".listbox #box #res1").html(res1html);
    		$(".listbox #box #res3").html(res2html);
    		
    		var currBoxId = $(".toolbar #l .active").attr("id");
    		$(".listbox").height($(".listbox #res" + currBoxId).height());
    	}else{
    		$(".listbox #box #res1").html(display_date_fail+"!<br/>" + val.msg);
    		$(".listbox #box #res3").html(display_date_fail+"!<br/>" + val.msg);
    	}
   	}catch(e){
   		$(".listbox #box #res1").html(display_date_fail+"：" + e.message);
   		$(".listbox #box #res3").html(display_date_fail+"：" + e.message);
   	}
}


//封装检索
function resSearch(){
	window.location.href = "#cntline";
	comLoading = layer.load(4, {
	  shade: [0.1,'#fff'] //0.1透明度的白色背景
	});
	resQuery();
}
function classQuery(rlclId){
	$("#_rlclId").val(rlclId);
	$("#_currentPage").val("0");
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


















