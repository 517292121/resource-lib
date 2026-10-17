
//--------------以下为采用COOKIE对浏览记录的管理及展示-----------////////
var BROWSE_COOKIE_NAME = "RESBROWSEHIS";
var saveDays = 30; //保存30天
var maxNums = 12; //最大保存条数

//保存当前资源到cookie
function saveBrowseHis(){
	var rsdaId = $("#_rsdaId").val();
	if(rsdaId == null || rsdaId.length <1 || rsdaId == 'undefined'){
		return;
	}
	var rsdaTitle = escape($("#rsdaTitle").html());
	
	var resItem = rsdaId + ":" + rsdaTitle;
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
	var dataArr = resItem.split(":");
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
			var resData = resArr[i].split(":");
			if(resData[1] && resData[1] != undefined){
				html += "<li class='subText'><a href='"+basePath+linkadd+idToStr(resData[0])+"'>" + unescape(resData[1]) + "</a></li>";
			}
		}
		if(i >= (maxNums-1)){
			break;
		}
	}
	$("#resBrowseHis").html(html);
}
