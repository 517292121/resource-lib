$(function () {
    $(".txt-size li").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
        var fontsize = $(this).data("id");
        $(".yuanern-txt").css("font-size", fontsize);
        $(".txt-size").hide();
        
        setColWid();
    });
    $(".txt-family-list li").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
        var fontfamily = $(this).data("id");
        $(".yuanern-txt p").css("font-family", fontfamily);
        $(".txt-family-list").hide();
    });
    $(".txt-line-list li").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
        var lineSize = $(this).data("id");
        $(".yuanern-txt").css("line-height", lineSize);
        $(".txt-line-list").hide();
    });
    
    
    $(".txt-align").click(function () {
        $(this).addClass("active").siblings().removeClass("active");
        var align = $(this).data("id");
        $(".yuanern-txt").css("text-align", align);
        $(".txt-size,.txt-family-list").hide();
    });
    
    $(".txt-size-title").click(function () {
        $(".txt-size").toggle();
        $(".txt-family-list").hide();
        $(".txt-line-list").hide();
    });
    $(".txt-family").click(function () {
        $(".txt-family-list").toggle();
        $(".txt-size").hide();
        $(".txt-line-list").hide();
    });
    
    $(".txt-line-size").click(function () {
        $(".txt-line-list").toggle();
        $(".txt-family-list").hide();
        $(".txt-size").hide();
    });
    
    $("#txtdir").click(function () {
    	var dir = $(".txt-direction").data("dir");
        if(dir == 'row'){
        	$("#txtdir").data("dir", "column");
        	$("#txtdir span").html("纵");
        	$("#txtdir").attr("title", "文字纵向排列");
        	$("#txtdir").removeClass("btn-row").addClass("btn-col");
        }else{
        	$("#txtdir").data("dir", "row");
        	$("#txtdir span").html("横");
        	$("#txtdir").attr("title", "文字横向排列");
        	$("#txtdir").removeClass("btn-col").addClass("btn-row");
        }
        changDir();
    });
    
    
    $(".show-btn").click(function(){
        if($(".setting-info").hasClass("active")){
            hideTool();
        }else{
            $(".setting-info").addClass("active").animate({right:0},336)
        }
    });
})
function hideTool(){
	$(".setting-info").removeClass("active").animate({right:-334+"px"},300);
    $(".txt-family-list").hide();
	$(".txt-size").hide();
	$(".txt-line-list").hide();
}
function txtColorChange(color) {
     if (obj == "background") {
         $("#txt-bottom").css("background", color);
         $("#txt-right").css("background", color);
     } else if (obj = "full") {
         $(".yuanern-txt").css("color", color);
     }
}

//改变文字排列方向
function changDir(){
	var dir = $(".txt-direction").data("dir");
	if(dir == "row"){
		$(".yuanern-txt").addClass("line-col");
		$(".txtcont").addClass("txt-content");
	}else{
		$(".yuanern-txt").removeClass("line-col");
		$(".txtcont").removeClass("txt-content");
		setColWid();
	}
}
//根据文字大小动态设置纵向排列时每列宽度
function setColWid(){
	var fontsize = $(".txt-size .active").data("id");
	$(".line-col p").css("width", fontsize);
}




