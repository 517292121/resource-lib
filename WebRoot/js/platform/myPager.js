//封装分页信息
function getPagination(currPage, totalPage, totalNums){
	if(totalPage < 2){
		return "";
	}
	var pageHtml = "<div class='pBar'>";
	if(currPage > 0){
		pageHtml += "<a href='javascript:turnPage(0);'>"+title_index+"</a>";
		pageHtml += "<a href='javascript:turnPage("+(currPage-1)+");'>"+title_previous_page+"</a>";
	}else{
		pageHtml += "<a href='javascript:void(0);' class='disable'>"+title_index+"</a>";
		pageHtml += "<a href='javascript:void(0);' class='disable'>"+title_previous_page+"</a>";
	}
	
	var begin = (currPage < 5) ? 0 : (currPage - 4);
	var end = begin + 9;
	if(end > totalPage){
		end = totalPage;
	}
	if((end - begin < 9) && begin > 0){
		begin = begin - (9 - (end - begin));
		begin = begin < 0 ? 0 : begin;
	}
	for(p=begin; p<end; p++){
		if(p == currPage){
			pageHtml += "<a href='javascript:void(0);' id='curr'>"+(p+1)+"</a>";
		}else{
			pageHtml += "<a href='javascript:turnPage("+p+");'>"+(p+1)+"</a>";
		}
	}
	if(currPage < totalPage-1){
		pageHtml += "<a href='javascript:turnPage("+(currPage+1)+");'>"+title_next_page+"</a>";
		pageHtml += "<a href='javascript:turnPage("+(totalPage-1)+");'>"+title_shadowe+"</a>";
	}else{
		pageHtml += "<a href='javascript:void(0);' class='disable'>"+title_next_page+"</a>";
		pageHtml += "<a href='javascript:void(0);' class='disable'>"+title_shadowe+"</a>";
	}
	pageHtml += "<span>"+title_common+"：" + totalNums + title_bar_record+"</span>";
	pageHtml += "</div>";
	return pageHtml;
}