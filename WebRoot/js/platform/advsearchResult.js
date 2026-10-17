var VIEW_PANEL_COOKIE = "RESLISTVIEWPANEL";
var comLoading = null; //数据查询加载弹出对象
$(function(){
	//关键词高亮
	highlight();
	//初始化资源列表三种预览模式框架效果
	pageInit();
	//热门检索
	inithotsearch();
	//添加左边边连接点击后选中样式
	$(".nav a").click(function(){
		$(this).addClass('active').siblings().removeClass('active');
	});
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
	});
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

//加载热门检索词
function inithotsearch(){
	$.ajax({
	    type: 'GET',
	    url: basePath + 'index/hotsearch?t=' + new Date().getTime(),
	    cache:false,
	    success: function(data){
	    	try{
		    	var val = eval("("+data+")");
		    	if(val.success == true){
		    		var json = "";
		    		var len = val.dataList.length;
		    		for(i=0; i<len; i++){
		    			if(i%2 == 0){
		    				json += "<li class='slide-bottom js'>";
		    			}else{
		    				json += "<li class='slide-bottom'>";
		    			}
	            		json += "<a href='"+basePath+"ls/"+val.dataList[i]+"'>"+val.dataList[i]+"</a>";
	            		json += "</li>";
		    		}
			    	$("#keys ul").html(json);
		    	}else{
		    		$("#keys ul").html(display_date_fail);
		    	}
	    	}catch(e){
	    		$("#keys ul").html(display_date_parsing_failure);
	    	}
	    },
	    fail: function (err, status) {
	    	$("#keys ul").html(display_date_fail);
	    }
	});
}	
/**检索词高亮**/
function highlight(){
    var key1 = $('#key1').val();//获取你输入的关键字；
    var key2 = $('#key2').val();
    var key3 = $('#key3').val();
    var key4 = $('#key4').val();
    var col1 = $('#col1').val();//获取检索条件
    var col2 = $('#col2').val();
    var col3 = $('#col3').val();
    var col4 = $('#col4').val();
    var searchText = null;
    var str = "";
    var regExp = null;
    /**第一个条件**/
    if(key1 != null && key1 != ""){
    	regExp = new RegExp(key1, 'gi');//创建正则表达式，g表示全局的，如果不用g，则查找到第一个就不会继续向下查找了；
    	if(col1 == "rsdaTitle"){
    		str = "#tm a";
    	}
    	if(col1 == "rsdaCreator"){
    		str = "#zrz a";
    	}
    	if(col1 == "rsdaPublisher"){
    		str = "#zrz a";
    	}
    	if(col1 == "rsdaKeyword"){
    		str = "#gjc a";
    	}
    	if(col1 == "rsdaDescription"){
    		str = "#ms ";
    	}
    	$(str).each(function(){//遍历文章；
            var html = $(this).html();
            var newHtml = html.replace(regExp, '<span class="highlight" >'+key1+'</span>');//将找到的关键字替换，加上highlight属性；
             $(this).html(newHtml);//更新文章；
        });
    }
    
    /**第二个条件**/
    if(key2 != null && key2 != ""){
    	regExp = new RegExp(key2, 'gi');//创建正则表达式，g表示全局的，如果不用g，则查找到第一个就不会继续向下查找了；
    	if(col2 == "rsdaTitle"){
    		str = "#tm a";
    	}
    	if(col2 == "rsdaCreator"){
    		str = "#zrz a";
    	}
    	if(col2 == "rsdaPublisher"){
    		str = "#zrz a";
    	}
    	if(col2 == "rsdaKeyword"){
    		str = "#gjc a";
    	}
    	if(col2 == "rsdaDescription"){
    		str = "#ms ";
    	}
    	$(str).each(function(){//遍历文章；
            var html = $(this).html();
            var newHtml = html.replace(regExp, '<span class="highlight" >'+key2+'</span>');//将找到的关键字替换，加上highlight属性；
             $(this).html(newHtml);//更新文章；
        });
    }
    
    /**第三个条件**/
    if(key3 != null && key3 != ""){
    	regExp = new RegExp(key3, 'gi');//创建正则表达式，g表示全局的，如果不用g，则查找到第一个就不会继续向下查找了；
    	if(col3 == "rsdaTitle"){
    		str = "#tm a";
    	}
    	if(col3 == "rsdaCreator"){
    		str = "#zrz a";
    	}
    	if(col3 == "rsdaPublisher"){
    		str = "#zrz a";
    	}
    	if(col3 == "rsdaKeyword"){
    		str = "#gjc a";
    	}
    	if(col3 == "rsdaDescription"){
    		str = "#ms ";
    	}
    	$(str).each(function(){//遍历文章；
            var html = $(this).html();
            var newHtml = html.replace(regExp, '<span class="highlight" >'+key3+'</span>');//将找到的关键字替换，加上highlight属性；
             $(this).html(newHtml);//更新文章；
        });
    }
    /**第四个条件**/
    if(key4 != null && key4 != ""){
    	regExp = new RegExp(key4, 'gi');//创建正则表达式，g表示全局的，如果不用g，则查找到第一个就不会继续向下查找了；
    	if(col4 == "rsdaTitle"){
    		str = "#tm a";
    	}
    	if(col4 == "rsdaCreator"){
    		str = "#zrz a";
    	}
    	if(col4 == "rsdaPublisher"){
    		str = "#zrz a";
    	}
    	if(col4 == "rsdaKeyword"){
    		str = "#gjc a";
    	}
    	if(col4 == "rsdaDescription"){
    		str = "#ms ";
    	}
    	$(str).each(function(){//遍历文章；
            var html = $(this).html();
            var newHtml = html.replace(regExp, '<span class="highlight" >'+key4+'</span>');//将找到的关键字替换，加上highlight属性；
             $(this).html(newHtml);//更新文章；
        });
    }
}