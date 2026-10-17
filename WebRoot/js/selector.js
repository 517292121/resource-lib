/***页头检索资源类型下拉选项******/
$(function() {
	var select = $(".channelSelect");
	var selectul = select.find("ul");
	var selectullia = selectul.find("li a");
	var cur = $(".curChannel");

	select.click(function(){
		var sh = selectul.css("display");
		if(sh == 'none'){
			selectul.slideDown("fast");
		}else{
			selectul.slideUp("fast");
		}
		//对document绑定一个影藏Div方法
		$(document).one("click", function (){
			selectul.slideUp("fast");
		});
		//阻止事件向上冒泡
		//event.stopPropagation();
		return false;
	});
	
	selectul.click(function (event){
		//event.stopPropagation();//阻止事件向上冒泡
		return false;
	});
	
	if(rltyid != ''){
		selectullia.each(function(){
			var rtid = $(this).attr("rel");
			if(rltyid == rtid){
				cur.html($(this).text());
			}
		});
	}

	/*鼠标点击类型*/
	selectullia.click(function(){
		var html = $(this).text();
		var type = $(this).attr("rel");
		cur.html(html);
		$("#rltyId").val(type);
		selectul.slideUp("fast");
	});

});
