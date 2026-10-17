var totalPages = 0;
var totalResult = 0;
var currentpage = 0;
var responseIsNull = true;
// 获取日志回复列表
function getTsTeamBlogResponse(currentPage) {
	scFacade.getIssueReplyList(currentPage, scisId, function(data) {
		// 给分页参数赋值
		currentpage = currentPage;
		var lists = eval("(" + data + ")");
		totalResult = lists.totalProperty;
		totalPages = Math.ceil(totalResult / pageSize);
		var blogReponse = document.getElementById("blogReponse");
		// 移除现有节点
		clearNode('blogReponse');
		if(totalResult > 0) {
			responseIsNull = false;
			for(var i = 0; i < lists.root.length; i++) {
				//"scirId", "suinName", "scirReplyTime", "scirContent",	"scirAttachmentName", "scirAttachmentUrl"
				var div = document.createElement("div");
				div.innerHTML = '<div >回复者：' + lists.root[i].suinName + '&nbsp;|&nbsp;时间：'
						+ lists.root[i].scirReplyTime + '<p>'+lists.root[i].scirContent+'</p></div>';
				blogReponse.appendChild(div);
			}
			var pagination = document.getElementById('pagination');
			// 添加分页组建
			pagination.innerHTML = createPaginationNav("getTsTeamBlogResponse", currentPage, totalPages, totalResult);
		}else {
			responseIsNull = true;
			// 隐蔽分页组建
			if(responseIsNull) {
				clearNode('pagination');
				document.getElementById('pagination').className = 'hide';
			}
		}
	}
	);
}
// 移除现有节点
function clearNode(id) {
	var blogReponse = document.getElementById(id);
	var a = blogReponse.childNodes;
	for(var i = a.length - 1; i >= 0; i--) {
		blogReponse.removeChild(a[i]);
	}
}

function valid(val) {
	var num = val.value;
	if(!isDigit(num)) {
		var ele = document.getElementById('numInput');
		ele.value = '';
		return false;
	}
	if(num >= totalPages)
		getTsTeamBlogResponse(totalPages - 1);
	else
		getTsTeamBlogResponse(num - 1);
}
function isDigit(val) {
	var patrn = /^[1-9]{1,20}$/;
	if(!patrn.exec(val)) return false;
	return true;
}