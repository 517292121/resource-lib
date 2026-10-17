//封装分页信息
function getPagination(currPage, totalPage, totalNums){
	if(totalPage < 2){
		return "";
	}
	var pageHtml = "<div class='pBar'>";

	if(currPage < totalPage-1){
		pageHtml += "<a href='javascript:turnPage("+(currPage+1)+");'>"+display_data_load_more+"</a>";
	}else{
		pageHtml += "<li>"+display_data_no_more+"</li>";
	}
	pageHtml += "</div>";
	return pageHtml;
}