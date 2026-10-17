
//--------------以下为采用COOKIE对浏览记录的管理及展示-----------////////
var BROWSE_COOKIE_NAME = "RESBROWSEHIS";
var saveDays = 30; //保存30天
var maxNums = 6; //最大保存条数

//保存当前资源到cookie
function saveBrowseHis(){
	var rsdaId = $("#_rsdaId").val();
	if(rsdaId == null || rsdaId.length <1 || rsdaId == 'undefined'){
		return;
	}
	var rsdaTitle = escape($("#rsdaTitle").html());
	var src = $("#coverImg").attr("src");
	var resItem = rsdaId + ";" + rsdaTitle+";"+src;
	var resString = resItem + getBeforeRes(rsdaId);
	$.cookie(BROWSE_COOKIE_NAME, resString, {expires: saveDays,  path: "/"}); //cookie值保存30天
}
function getBeforeRes(rsdaId){
	var resCookie = $.cookie(BROWSE_COOKIE_NAME);
	if(resCookie == null || resCookie.length < 1){
		return "";
	}
	var resArr = resCookie.split(",");
	var resNum = resArr.length;
	var resStr = "";
	var nums = 0;
	for(i=0; i<resNum; i++){
		var id = getResId(resArr[i]);
		if(id != rsdaId){
			resStr += "," + resArr[i];
			nums++;
		}
		if(nums >= (maxNums-1)){
			break;
		}
	}
	return resStr;
}
function getResId(resItem){
	if(resItem == null || resItem.length < 3){
		return "";
	}
	var dataArr = resItem.split(";");
	return dataArr[0];
}

//加载浏览历史
function loadBrowseHis(linkadd){
	var resCookie = $.cookie(BROWSE_COOKIE_NAME);
	if(resCookie == null || resCookie.length < 1){
		return "";
	}
	var resArr = resCookie.split(",");
	var resNum = resArr.length;
	var html = "";
	var nums = 0;
	for(i=0; i<resNum; i++){
		if(resArr[i].length > 1){
			var resData = resArr[i].split(";");
			if((i+1)%6 == 0){
				html += "<li class='subText' style='margin-right:0px;'>";
			}else{
				html += "<li class='subText'>";
			}	html += "<a href='"+basePath+linkadd+idToStr(resData[0])+"'>";
				html += "<div>";
					html += "<img src='"+resData[2]+"' onerror=\"this.src='"+basePath+"images/platform/doc-cover.jpg'\"/>";
				html += "</div>";
				html += "<p>"+unescape(resData[1])+"</p>";
				html += "</a>";
			html += "</li>";
		}
		if(i >= (maxNums-1)){
			break;
		}
	}
	html += "<p class='clear'/>";
	$("#resBrowseHis").html(html);
	image_rendering();
}

/**
 *	表格模式图片渲染
 */
function image_rendering(){
     $("#resBrowseHis li a div img").mouseenter(function(){
         var w = $(this).width(),
         Gleft = 180-w;
         if(Gleft < -10){
             $(this).animate({left:Gleft},500);
         }    
     }).mouseleave(function(){
         $(this).animate({left:0},500);
     })
}
