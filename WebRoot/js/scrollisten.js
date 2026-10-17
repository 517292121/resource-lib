$(document).ready(function(){
	showtop();
	cleareendsearchinfo();
});

//查询不到数据用户返回上一页时,清空错误的数据
function cleareendsearchinfo(){
	var v = $("#endsearch").val();
	if(v!="" && v!=undefined){
		var e = $("#endsearchs").val();
		var c = ""
		var s = e.split(",");
		for(var i =0;i<s.length;i++){
			if(v!=s[i] && s[i]!=""){
				c +=s[i]+",";
			}
		}
		$("#endsearch").val("");
		$("#endsearchs").val(c);
	}
}

//滑动条监听事件
function showtop(){
	var gh = $(".content-group").height();
	var ch = $(".content-box").height();
	if(ch<gh){
		$(".content-box").css({"height":gh+"px"});
	}
	var wh=$(window).height();
	$(window).scroll(function(){
		var s=$(window).scrollTop();
		if(s>=470){
			$(".postion").show();
		}else{
			$(".postion").hide();
		}
	});
}
//回到顶部
function backtotop(){
	var speed=500;
    $('body,html').animate({ scrollTop: 0 }, speed);
    return false;
}