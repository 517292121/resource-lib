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
	
	//根据特色库ID加载对应资源类型
	resQuery();
	
	//添加右边连接点击后选中样式
	$(".w_tab li a").click(function(){
		$(this).addClass('active').siblings().removeClass('active');
	})
});


//初始化资源列表三种预览模式框架效果
function pageInit(){
	//$(".w_listbox").height($(".w_listbox #res1").height());
	
	var resWid = $(".w_listbox").width();
	var panels = $(".w_listbox ul").length;
	var countWid = resWid * panels;
	$(".w_listbox #box").width(countWid);
	$(".w_listbox #box ul").width(resWid);
	$(".w_tab li a").click(function(){
		var myNum = $(this).index();
		var moveLeft = myNum * resWid * -1;
		$(".w_listbox #box").animate({'left':moveLeft}, 300);
		
		var hei = $(".w_listbox #res"+(myNum+1)).height();
		$(".w_listbox").height(hei);
		
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
	
	var resWid = $(".w_listbox").width();
	var moveLeft = viewNum * resWid * -1;
	$(".w_listbox #box").animate({'left':moveLeft}, 0);
	$(".w_listbox").height($(".w_listbox #res"+(viewNum+1)).height());
	$(".w_tab li #"+(viewNum+1)).addClass("active").siblings().removeClass('active');
}




//检索资源
function resQuery(){
	var _rmstId = $("#_rmstId").val();
	var _rltyId = $("#_rltyId").val();
	var _pn = $("#_currentPage").val();
	$.ajax({
	    type: 'GET',
	    url: basePath + '/res/db/list?t=' + new Date().getTime(),
	    data: {
	    	rmstId : _rmstId,
	    	rltyId : _rltyId,
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
   		var res1html = "";
		var res2html = "";
    	var val = eval("("+data+")");
    	if(val.success == true){
    		var json = "";
    		var len = val.dataList.length;
    		if(len < 1){
    			$(".w_listbox #box #res1").html(title_not_related_resources);
    			$(".w_listbox #box #res2").html(title_not_related_resources);
    			return;
    		}
    		for(i=0; i<len; i++){
    			//列表模式资源信息封装
    			res1html += "<div class='w_res_list'>";
    			res1html += "<li class='title'><a href='"+basePath+"wap/resdetail/"+idToStr(val.dataList[i][0])+"'>"+val.dataList[i][1]+"</a></li>";
    			res1html += "<li>";
    			if(!isNull(val.dataList[i][2])){
    				res1html += "<span>"+display_data_author+"：</span>" + val.dataList[i][2] + " &nbsp;";
    			}
    			if(!isNull(val.dataList[i][3])){
    				res1html += "<span>"+title_input_name+"：</span>" + val.dataList[i][3] + " &nbsp;";
    			}
    			if(!isNull(val.dataList[i][6])){
    				res1html += "<span>"+title_input_Keyword+"：</span>" + val.dataList[i][6] + " &nbsp;";
    			}
    			res1html += "<span>"+title_input_periodical_date+"：</span>" + val.dataList[i][8] + " &nbsp;";
    			res1html += "<span>"+title_amount_of_access+"：</span>" + val.dataList[i][9];
    			res1html += "</li>";
    			res1html += "</div>";
    			
				
				//表格模式资源信息封装
				if(i%2 == 0){
    				res2html += "<div class='w_res_table'>";
    			}else{
	    			res2html += "<div class='w_res_table'>";
    			}
    			
				if(isNull(val.dataList[i][7])){
					res2html += "<li><a href='"+basePath+"wap/resdetail/"+idToStr(val.dataList[i][0])+"'><img src='"+basePath+"images/platform/doc-cover.jpg' /></a></li>";
				}else{
					res2html += "<li><a href='"+basePath+"wap/resdetail/"+idToStr(val.dataList[i][0])+"'><img src='"+ val.dataList[i][7] +"' onerror='this.src="+basePath+"images/platform/doc-cover.jpg' /></a></li>";
				}
				res2html += "<li class='restitle'><a href='"+basePath+"wap/resdetail/"+idToStr(val.dataList[i][0])+"'>"+val.dataList[i][1]+"</a></li>";
				res2html += "</div>";
    		}
    		//res2html += "<p class='clear' />";
    		
    		var currPage = val.currentPage;
    		var totalPage = val.totalPage;
    		var totalLine = val.totalLine;
    		var pageBar = getPagination(currPage, totalPage, totalLine);
    		//res1html += pageBar;
    		//res2html += pageBar;
    		
    		$(".w_listbox #box #res1").append(res1html);
    		$(".w_listbox #box #res2").append(res2html);
    		$("#w_pages").html(pageBar);
    		
    		var currBoxId = $(".w_tab li .active").attr("id");
    		var divObj = $(".w_listbox #box #res" + currBoxId);
    		var hei = divObj.outerHeight(true);
    		$(".w_listbox").height(hei);
    	}else{
    		$(".listbox #box #res1").html(display_date_fail+"!<br/>" + val.msg);
    		$(".listbox #box #res2").html(display_date_fail+"!<br/>" + val.msg);
    	}
    	
    	
   	}catch(e){
   		$(".listbox #box #res1").html(display_date_fail+"：" + e.message);
   		$(".listbox #box #res2").html(display_date_fail+"：" + e.message);
   	}
}

//封装检索
function resSearch(){
	comLoading = layer.load(4, {
	  shade: [0.1,'#fff'] //0.1透明度的白色背景
	});
	resQuery();
}

function turnPage(pageNo){
	$("#_currentPage").val(pageNo);
	resSearch();
}