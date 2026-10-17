var totalPages = 0;
function createPaginationNav(pageFunName, currentPage, totalPages, totalResult, mesbegin, mesend ) {
	totalPages = parseInt(totalPages);
	if(totalPages > 0){
		totalPages = parseInt(totalPages) + 1;
	}
	this.totalPages = totalPages;
	var pageText = "";
	if(totalResult > 0) {
		if(totalPages > 0) {
			pageText += "<div class='p_bar'>";
			//pageText += "<a class='p_total'>&nbsp;" + (currentPage + 1) + "/" + totalPages + "&nbsp;</a>";
			if(currentPage > 4) {
				//pageText += "<a href='javascript:void(0)' onclick='" + pageFunName + "(0)' class='p_num'>|‹</a>";
			}
			if(currentPage > 0) {
				pageText += "<a href='javascript:void(0)' onclick='" + pageFunName + "(" + (currentPage - 1)
						+ ")' class='on'></a>";
			}
			if(totalPages <= 9) {
				for(var i = 0; i < totalPages; i++) {
					if(currentPage != i) {
						pageText += "<a href='javascript:void(0)' onclick='" + pageFunName + "(" + i + ")' class='p_num'>" + (i + 1)
								+ "</a>";
					}else {
						pageText += "<a class='p_curpage'>" + (i + 1) + "</a>";
					}
				}
			}else {
				if(currentPage < 5) {
					for(var i = 0; i < 9; i++) {
						if(currentPage != i) {
							pageText += "<a href='javascript:void(0)' onclick='" + pageFunName + "(" + i + ")' class='p_num'>" + (i + 1)
									+ "</a>";
						}else {
							pageText += "<a class='p_curpage'>" + (i + 1) + "</a>";
						}
					}
				}else {
					if(totalPages > (currentPage + 3)) {
						for(var i = (currentPage - 4); i < (currentPage + 4); i++) {
							if(currentPage != i) {
								pageText += "<a href='javascript:void(0)' onclick='" + pageFunName + "(" + i + ")' class='p_num'>"
										+ (i + 1) + "</a>";
							}else {
								pageText += "<a class='p_curpage'>" + (i + 1) + "</a>";
							}
						}
					}else {
						for(var i = (totalPages - 10); i < (totalPages); i++) {
							if(currentPage != i) {
								pageText += "<a href='javascript:void(0)' onclick='" + pageFunName + "(" + i + ")' class='p_num'>"
										+ (i + 1) + "</a>";
							}else {
								pageText += "<a class='p_curpage'>" + (i + 1) + "</a>";
							}
						}
					}
				}
			}
			if(currentPage + 1 < totalPages) {
				pageText += "<a href='javascript:void(0)' onclick='" + pageFunName + "(" + (currentPage + 1)
						+ ")' class='next'></a>";
			}
			if(currentPage + 5 < totalPages) {
				//pageText += "<a href='javascript:void(0)' onclick='" + pageFunName + "(" + (totalPages - 1)
				//		+ ")' class='p_num'>|›</a>";
			}
			pageText += "<span class='total'>" + mesbegin + totalResult + mesend + "</span>";
			//pageText += "<a class='input'>";
			//pageText += "<input  id='numInput' name='custompage' onkeydown='if(event.keyCode==13) {valid(this," + pageFunName
			//		+ ")}' type='text'>";
			//pageText += "</a>";
		}
	}
	return pageText;
}
function valid(val, methodName) {
	var num = val.value;
	if(!isDigit(num)) {
		var ele = document.getElementById('numInput');
		ele.value = '';
		return false;
	}
	if(num >= totalPages)
		eval("(" + methodName(totalPages - 1) + ")");
	else
		eval("(" + methodName(num - 1) + ")");
}
function isDigit(val) {
	var patrn = /^[1-9][0-9]{0,19}$/;
	if(!patrn.exec(val)) return false;
	return true;
}
// 移除现有节点
function clearNode(id) {
	var blogReponse = document.getElementById(id);
	var a = blogReponse.childNodes;
	for(var i = a.length - 1; i >= 0; i--) {
		blogReponse.removeChild(a[i]);
	}
}