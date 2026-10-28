
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};
/**
 *	全文检索前端处理
 *
 *  wanglin
 *  2018-01
 *
 */
 var LS_VIEW_PANEL_COOKIE = "LUCENERESULTVIEW";
 var comLoading = null; //数据查询加载弹出对象
$(function(){
	//初始化资源列表二种预览模式框架效果
	pageInit();
	
	//数据异步检索
	dataSearch();
	
	//分组检索
	searchGroup();
	
	//加载检索条件
	initCondition();
	
	//添加右边连接点击后选中样式
	$(".nav a").click(function(){
		$(this).addClass('active').siblings().removeClass('active');
	})
});

//初始化资源列表二种预览模式框架效果
function pageInit(){
	$(".listbox").height($(".listbox #res2").height());
	
	var resWid = $(".listbox").width();
	var panels = $(".listbox ul").length;
	var countWid = resWid * panels;
	$(".listbox #box").width(countWid);
	$("#models a").click(function(){
		var myNum = $(this).index();
		var moveLeft = myNum * resWid * -1;
		$(".listbox #box").animate({'left':moveLeft}, 300);
		
		var currpage = (myNum == 0) ? 2 : 3;
		var hei = $(".listbox #res"+currpage).height();
		$(".listbox").height(hei);
		
		//缓存用户预览模式编号
		$.cookie(LS_VIEW_PANEL_COOKIE, myNum, { expires: 30 });
	})
	initPanel();
}
function initPanel(){
	var viewNum = $.cookie(LS_VIEW_PANEL_COOKIE);
	if(viewNum == null || viewNum.length < 1 || viewNum == '0'){
		return;
	}
	viewNum = parseInt(viewNum);
	var currpage = (viewNum == 0) ? 2 : 3;
	
	var resWid = $(".listbox").width();
	var moveLeft = viewNum * resWid * -1;
	$(".listbox #box").animate({'left':moveLeft}, 0);
	$(".listbox").height($(".listbox #res"+currpage).height());
	$(".tools #models #"+currpage).addClass("active").siblings().removeClass('active');
}


//启动数据检索
function dataSearch(){
	var _attr = $("#s_params").attr("attr");
	var _key = $("#s_params").attr("key");
	var _dataType = $("#s_params").attr("datatype");
	var _reslib = $("#s_params").attr("reslib");
	var _restype = $("#s_params").attr("restype");
	var _xueke = $("#s_params").attr("xueke");
	var _creator = $("#s_params").attr("creator");
	var _publisher = $("#s_params").attr("publisher");
	var _keyword = $("#s_params").attr("keyword");
	var _date = $("#s_params").attr("date");
	var _pn = $("#s_params").attr("pn");
	var _match = $("#s_params").attr("match");
	
	$.ajax({
	    type: 'GET',
	    //url: basePath + 'luceneSearch',
	    url : basePath + 'searchForPage',
	    data: {
	    	attr : _attr,
	    	key : _key,
	    	dataType : _dataType,
	    	resLib : _reslib,
	    	resType : _restype,
	    	subjectClass : _xueke,
	    	creator : _creator,
	    	publisher : _publisher,
	    	keyword : _keyword,
	    	match: _match,
	    	date : _date,
	    	pn : _pn
	    },
	    cache:false,
	    success: function(data){
	    	if(comLoading != null){
	    		layer.close(comLoading);
	    	}
	    	//initlist(data);
	    	initListForObj(data);
	    },
	    fail: function (err, status) {
	    	$(".listbox").html($.i18n.prop("repeat.bar.record") + err);
	    }
	});
}
function initlist(data){
	try{
		var _key = $("#s_params").attr("key");
   		var reshtml = "";
    	var val = eval("("+data+")");
    	if(val.success == true){
    		if(val.dataList == null || val.dataList == ""){
    			cleardata();
    			return;
    		}
    		var len = val.dataList.length;
    		if(len < 1){
    			cleardata();
    			return;
    		}
    		
    		var res2html = "";
   			var res3html = "";
   			
   			var detailUrl = "";
    		for(i=0; i<len; i++){
    			if(val.dataList[i][3] == null || val.dataList[i][3].length < 1){
    				continue;
    			}
    			
    			//注：此处 的"+title_input_data_type+"名称一定要与DataBean类中的DATA_TYPE保持一致
    			if(val.dataList[i][3] == "资源"){
    				detailUrl = "res/";
    			}else if(val.dataList[i][3] == "文本"){
    				detailUrl = "text/";
    			}else if(val.dataList[i][3] == "资讯"){
    				detailUrl = "info/detail/";
    			}
    			
    			//详细模式资源信息封装
				res2html += "<dl class='luc-res'>";
				res2html += "<li class='tit'>";
				if(val.dataList[i][4] != null && val.dataList[i][4].length > 0){
					res2html += "<span>"+val.dataList[i][4]+"</span>";
				}
				res2html += "<a href='"+basePath + detailUrl + idToStr(val.dataList[i][0]) + "' target='_blank'>"+val.dataList[i][1]+"</a></li>";
				res2html += "<li class='des'>"+val.dataList[i][2]+"</li>";
				res2html += "<li class='info'>";
				if(val.dataList[i][5] != null && val.dataList[i][5].length > 0){
					res2html += title_input_Creator+"：" + parseLink(val.dataList[i][5], 'rsdaCreator') + " &nbsp;";
				}
				if(val.dataList[i][6] != null && val.dataList[i][6].length > 0){
					res2html += title_input_name+"：" + parseLink(val.dataList[i][6], 'rsdaPublisher') + " &nbsp;";
				}
				if(val.dataList[i][7] != null && val.dataList[i][7].length > 0){
					res2html += ""+title_input_Keyword+"：" + parseLink(val.dataList[i][7], 'rsdaKeyword') + " &nbsp;";
				}
				res2html += "<a href='"+basePath+"res/browse/"+idToStr(val.dataList[i][0])+"?key="+_key+"'>"+title_online_reading+"</a>";
				res2html += "</li>";
				res2html += "</dl>";
				
				
				//表格模式资源信息封装
				if(i%5 == 0){
    				res3html += "<div class='item' style='margin-left:0px;'>";
    			}else{
					res3html += "<div class='item'>";
    			}
				res3html += "<div class='item'>";
				if(isNull(val.dataList[i][9])){
					res3html += "<p><a href='"+basePath+detailUrl+idToStr(val.dataList[i][0])+"' target='_blank'><img src='"+basePath+"images/platform/doc-cover.jpg' /></a></p>";
				}else{
					res3html += "<p><a href='"+basePath+detailUrl+idToStr(val.dataList[i][0])+"' target='_blank'><img src='"+ val.dataList[i][9] +"' onerror='this.src="+basePath+"images/platform/doc-cover.jpg' /></a></p>";
				}
				res3html += "<li><a href='"+basePath+detailUrl+idToStr(val.dataList[i][0])+"' target='_blank'>"+val.dataList[i][1]+"</a></li>";
				res3html += "</div>";
    		}
    		res3html += "<p class='clear' />";
    		
    		var currPage = val.currentPage - 1;
    		var totalPage = val.totalPage;
    		var totalLine = val.totalLine;
    		var pageSize = val.pageSize;
    		var pageBar = getPagination(currPage, totalPage, totalLine);
    		res2html += pageBar;
    		res3html += pageBar;
    		
    		$(".listbox #res2").html(res2html);
    		$(".listbox #res3").html(res3html);
    		
    		var beginNum = currPage * pageSize;
    		$("#resultTotal").html(totalLine);
    		$("#currnums").html((beginNum + 1) + "-" + (beginNum+len));
    		var currBoxId = $("#models .active").attr("id");
    		$(".listbox").height($(".listbox #res" + currBoxId).height());
    	}else{
    		$(".listbox #box #res2").html(display_date_fail+"!<br/>" + val.msg);
    		$(".listbox #box #res3").html(display_date_fail+"!<br/>" + val.msg);
    	}
   	}catch(e){
   		$(".listbox #box #res2").html(display_date_fail+"!<br/>" + e.message);
   		$(".listbox #box #res3").html(display_date_fail+"!<br/>" + e.message);
   	}
} 
function initListForObj(data){
	try{
		var _key = $("#s_params").attr("key");
   		var reshtml = "";
    	var val = eval("("+data+")");
    	if(val.success == true){
    		if(val.dataList == null || val.dataList == ""){
    			cleardata();
    			return;
    		}
    		var len = val.dataList.length;
    		if(len < 1){
    			cleardata();
    			return;
    		}
    		
    		var res2html = "";
   			var res3html = "";
   			
   			var detailUrl = "";
   			var res = null;
    		for(i=0; i<len; i++){
    			res = val.dataList[i];
    			if(res.dataType == null || res.dataType.length < 1){
    				continue;
    			}
    			
    			//注：此处 的"+title_input_data_type+"名称一定要与DataBean类中的DATA_TYPE保持一致
    			if(res.dataType == "资源"){
    				detailUrl = "res/";
    			}else if(res.dataType == "文本"){
    				detailUrl = "text/";
    			}else if(res.dataType == "资讯"){
    				detailUrl = "info/detail/";
    			}
    			
    			var idcode = idToStr(res.id);
    			
    			//详细模式资源信息封装
				res2html += "<dl class='luc-res'>";
				
				//资源封面
				var coverUrl = res.coverUrl;
				if(!isNull(coverUrl) && coverUrl.indexOf("file/access") == 0){
					coverUrl = basePath+coverUrl;
				}
				
				if(!isNull(coverUrl)){
					res2html += "<div class='img-cover'><a href='"+basePath+detailUrl+idcode+"' target='_blank'><img src='"+ coverUrl +"' onerror=\"this.src='"+basePath+"images/platform/doc-cover.jpg'\" /></a></div>";
					res2html += "<div class='res-data'>"
				}else{
					res2html += "<div class='res-data reszoom'>"
				}
				
				res2html += "<div class='tit'>";
				if(res.resType != null && res.resType.length > 0){
					res2html += "<span>"+res.resType+"</span>";
				}
				res2html += "<a href='"+basePath + detailUrl + idcode + "' target='_blank'>"+res.title+"</a></div>";
				
				
				//列表元数据封装
				res2html += "<div class='info'>";
				res2html += "<p>";
				if(res.creator != null && res.creator.length > 0){
					res2html += title_input_Creator+"：" + parseLink(res.creator, 'rsdaCreator') + " &nbsp;";
				}
				if(res.publisher != null && res.publisher.length > 0){
					res2html += title_input_name+"：" + parseLink(res.publisher, 'rsdaPublisher') + " &nbsp;";
				}
				res2html += "</p>";
				
				if(res.keyword != null && res.keyword.length > 0){
					res2html += "<p>"+title_input_Keyword+"：" + parseLink(res.keyword, 'rsdaKeyword') + "</p>";
				}
				res2html += "</div>";
				//元数据END
				
				//子页目录
				if(res.pageList != null && res.pageList.length > 0){
					var pageLen = res.pageList.length;
					res2html += "<div class='child-page'>";
					
					res2html += "<div class='zuo'>"+directory+"</div>"
					res2html += "<div class='you'>"
					var pagedata = null;
					for(p=0; p<pageLen; p++){
						if(p<3){
							res2html += "<div class='pageline'><p>";
						}else{
							res2html += "<div class='pageline page-hide res"+idcode+"'><p>";
						}
						pagedata = res.pageList[p];
						var imglink = basePath + "res/browse/" + idcode + "?p="+pagedata.titleEncode+"&key="+_key + "&match=" + $("#s_params").attr("match");
						if(pagedata.pageType==1){
							res2html += "<span>pdf:</span>";
						}else{
							res2html += uiMarkup("<span>图文:</span>");
						}
						res2html += fpnum(pagedata.title) + " &nbsp; ";
						res2html += pagedata.content+"</p>";
						
						res2html += "<div class='viewer'>";
						if(pagedata.pageType==1){
							if(res.srcPath.indexOf(".") == -1 ){
								var pdflink = basePath + "res/browse/pdf?file="+encodeURIComponent(res.srcPath)+"&key="+_key+"&page="+pagedata.pageNum;
								res2html += "<a href='"+pdflink+"' class='pdf' title='pdf reading' target='_blank'></a>";
							}else{
								res2html += "<a href='"+imglink+"' class='pdf' title='pdf reading' target='_blank'></a>";
							}
						}else{
							res2html += "<a href='"+imglink+"' class='pic' title='image reading' target='_blank'></a>";
						}
						res2html += "<div class='clear'></div>"
						res2html += "</div>";
						
						res2html += "<div class='clear'></div>"
						res2html += "</div>";
					}
					if(pageLen > 3){
						res2html += "<div><a href='javascript:void(0)' onclick=\"showMore('"+idcode+"')\" class='more' id='more"+idcode+"'>"+title_more+"</a></div>";
					}
					if(pageLen > 10){
						res2html += "<p class='page-hide res"+idcode+"'><a href='"+basePath + "res/browse/" + idcode +"' target='_blank' class='more'>"+title_more_introduce+"</a></p>";
					}
					
					res2html += "</div>";
					res2html += "<p class='clear' />"
					res2html += "</div>";
				}else{
					res2html += "<div class='des'>"+res.content+"</div>";
				}
				
				res2html += "<div class='views'>";
				res2html += "<a href='"+basePath + detailUrl + idcode + "' target='_blank'>"+title_view_details+"</a> &nbsp; ";
				if(res.srcPath != null && res.srcPath.length > 1){
					if(res.srcPath.indexOf(".") == -1 ){
						var pdflink = basePath + "res/browse/pdf?file="+encodeURIComponent(res.srcPath)+"&key="+_key;
						res2html += "<a href='"+pdflink+"' target='_blank'>"+title_pdf_reading+"</a>";
						res2html += "<a href='"+basePath+"res/browse/"+idcode+"?key="+_key+"&match=" + $("#s_params").attr("match")+"' target='_blank'>"+title_img_reading+"</a>";
					}else{
						res2html += "<a href='"+basePath+"res/browse/"+idcode+"?key="+_key+"&match=" + $("#s_params").attr("match")+"' target='_blank'>"+title_online_reading+"</a>";
					}
				}else{
					res2html += "<a href='"+basePath+"res/browse/"+idcode+"?key="+_key+"&match=" + $("#s_params").attr("match")+"' target='_blank'>"+title_online_reading+"</a>";
				}
				res2html += "</div></div>";
				res2html += "<p class='clear' />";
				res2html += "</dl>";
				
				
				//表格模式资源信息封装
				if(i%5 == 0){
    				res3html += "<div class='item' style='margin-left:0px;'>";
    			}else{
					res3html += "<div class='item'>";
    			}
				res3html += "<p><a href='"+basePath+detailUrl+idcode+"' target='_blank'><img src='"+ coverUrl +"' onerror=\"this.src='"+basePath+"images/platform/doc-cover.jpg'\" /></a></p>";
				res3html += "<li><a href='"+basePath+detailUrl+idcode+"' target='_blank'>"+res.title+"</a></li>";
				res3html += "</div>";
    		}
    		res3html += "<p class='clear' />";
    		
    		var currPage = val.currentPage - 1;
    		var totalPage = val.totalPage;
    		var totalLine = val.totalLine;
    		var pageSize = val.pageSize;
    		var pageBar = getPagination(currPage, totalPage, totalLine);
    		res2html += pageBar;
    		res3html += pageBar;
    		
    		$(".listbox #res2").html(res2html);
    		$(".listbox #res3").html(res3html);
    		image_rendering();
    		var beginNum = currPage * pageSize;
    		$("#resultTotal").html(totalLine);
    		$("#currnums").html((beginNum + 1) + "-" + (beginNum+len));
    		var currBoxId = $("#models .active").attr("id");
    		$(".listbox").height($(".listbox #res" + currBoxId).height());
    	}else{
    		$(".listbox #box #res2").html(display_date_fail+"!<br/>" + val.msg);
    		$(".listbox #box #res3").html(display_date_fail+"!<br/>" + val.msg);
    	}
   	}catch(e){
   		$(".listbox #box #res2").html(display_date_fail+"!<br/>" + e.message);
   		$(".listbox #box #res3").html(display_date_fail+"!<br/>" + e.message);
   	}
} 


function parseLink(txt, ele){
	txt = txt.replace("，", ",");
	txt = txt.replace("；", ",");
	txt = txt.replace(";", ",");
	txt = txt.replace("、", ",");
	txt = txt.replace(" ", ",");
	
	var arr = txt.split(",");
	var result = "";
	for(var i=0; i<arr.length; i++){
		if(arr[i] != null && arr[i].length > 0) {
			result += "<a href='" + basePath + "resAdvs?COL1="+ele+"&LIKE1=LIKE&KEY1=" + arr[i] + "'>" + arr[i] + "</a> &nbsp;";
		}
	}
	return result;
}

function showMore(resIdCode){
	$(".res"+resIdCode).show();
	$("#more"+resIdCode).remove();
	
	//点击更多时动态设置高度
	$(".listbox").css({'height':$("#box").height()});
}


//查询分组统计
function searchGroup(){
	var _attr = $("#s_params").attr("attr");
	var _key = $("#s_params").attr("key");
	var _dataType = $("#s_params").attr("datatype");
	var _reslib = $("#s_params").attr("reslib");
	var _restype = $("#s_params").attr("restype");
	var _xueke = $("#s_params").attr("xueke");
	var _creator = $("#s_params").attr("creator");
	var _publisher = $("#s_params").attr("publisher");
	var _keyword = $("#s_params").attr("keyword");
	var _date = $("#s_params").attr("date");
	var _pn = $("#s_params").attr("pn");
	var _match = $("#s_params").attr("match");
	
	$.ajax({
	    type: 'GET',
	    url: basePath + 'luceneGroup?t=' + new Date().getTime(),
	    data: {
	    	attr : _attr,
	    	key : _key,
	    	dataType : _dataType,
	    	resLib : _reslib,
	    	resType : _restype,
	    	subjectClass : _xueke,
	    	creator : _creator,
	    	publisher : _publisher,
	    	keyword : _keyword,
	    	match: _match,
	    	date : _date,
	    	pn : _pn
	    },
	    cache:false,
	    success: function(data){
	    	initGroup(data);
	    },
	    fail: function (err, status) {
	    	$(".listbox").html(display_date_fail+":" + err);
	    }
	});
}
function initGroup(data){
	try{
   		var reshtml = "";
    	var val = eval("("+data+")");
    	if(val.success == true){
    		if(val.groupMap == null){
    			clearGroup();
    			return;
    		}
			var grouphtml = "";
			if(val.groupMap.dataType.length > 2){
				grouphtml = groupParser(val.groupMap.dataType, title_input_data_type, "datatype");
			}
			grouphtml += groupParser(val.groupMap.resLibrary, title_input_characteristic_library, "reslib");
			grouphtml += groupParser(val.groupMap.resType, title_input_literature_type, "restype");
			grouphtml += groupParser(val.groupMap.creator, title_input_Creator, "creator");
			grouphtml += groupParser(val.groupMap.keyword, title_input_Keyword, "keyword");
			grouphtml += groupParser(val.groupMap.publisher, title_input_name, "publisher");
			grouphtml += groupParser(val.groupMap.date, title_input_periodical_date, "date");
			grouphtml += groupParser(val.groupMap.subjectClass, title_input_classification, "xueke");
			
			if(grouphtml.length < 2){
				clearGroup();
			}else{
				$("#groupbox").html(grouphtml);
			}
    	}else{
    		clearGroup();
    	}
   	}catch(e){
   		clearGroup();
   	}
} 

function groupParser(g, gname, pname){
	if(g == null){
		return "";
	}
	if(g == "undefined" || g == undefined){
		return "";
	}
	if(g.length < 1){
		return "";
	}
	var len = g.length;
	var html = "<div class='resclass'>";
	html += "<p>"+gname+"</p>";
	html += "<ul id='ku' class='"+pname+"'>";
	for(i=0; i<len; i++){
		html += "<li><a href='javascript:void(0);' onclick=\"paramSearch('"+pname+"', '"+g[i][0]+"');\">" + g[i][0] + "（" + g[i][1] + "）</a></li>"; 
	}
	html += "</ul>";
	html += "</div>";
	return html;
}



//执行数据列表检索
function resSearch(){
	window.location.href = "#cntline";
	comLoading = layer.load(4, {
	  shade: [0.1,'#fff'] //0.1透明度的白色背景
	});
	dataSearch();
}
//分页检索
function turnPage(pageNo){
	$("#s_params").attr("pn", (pageNo+1));
	resSearch();
}
//点击聚类条件检索
function paramSearch(p, v){
	$("#s_params").attr(p, v);
	$("#s_params").attr("pn", "1"); //将页数页数重置到1
	resSearch();
	searchGroup();
	initCondition();
}


//清空聚类统计
function clearGroup(){
	var html = "<div class='resclass'>";
	html += "<p>"+title_input_data_type+"</p>";
	html += "<ul id='ku'>"+display_date_null+"</ul>";
	html += "</div>";
	
	html += "<div class='resclass'>";
	html += "<p>"+title_input_characteristic_library+"</p>";
	html += "<ul id='ku'>"+display_date_null+"</ul>";
	html += "</div>";
	
	html += "<div class='resclass'>";
	html += "<p>"+title_input_literature_type+"</p>";
	html += "<ul id='ku'>"+display_date_null+"</ul>";
	html += "</div>";
	$("#groupbox").html(html);
}

//清空页面数据
function cleardata(){
	$(".listbox").height(200);
	$("#resultTotal").html(0);
    $("#currnums").html("0-0");
    $(".listbox #res2").html("<p class='nodata'>"+display_not_related_data+"</p>");
    $(".listbox #res3").html("<p class='nodata'>"+display_not_related_data+"！</p>");
}


//加载已检索条件显示
function initCondition(){
	var _key = $("#s_params").attr("key");
	var _dataType = $("#s_params").attr("datatype");
	var _reslib = $("#s_params").attr("reslib");
	var _restype = $("#s_params").attr("restype");
	var _xueke = $("#s_params").attr("xueke");
	var _creator = $("#s_params").attr("creator");
	var _publisher = $("#s_params").attr("publisher");
	var _keyword = $("#s_params").attr("keyword");
	var _date = $("#s_params").attr("date");
	
	var conhtml = "<i>"+title_input_retrieval_condition+"：</i>";
	if(_key != null && _key.length > 0){
		conhtml += "<li><span>"+_key+"</span><a href='javascript:void(0);' onclick=\"delCon('key');\" title='"+operation_click_cancel_limit+"'></a></li>";
	}
	if(_dataType != null && _dataType.length > 0){
		conhtml += "<li><span>"+_dataType+"</span><a href='javascript:void(0);' onclick=\"delCon('datatype');\" title='"+operation_click_cancel_limit+"'></a></li>";
	}
	if(_reslib != null && _reslib.length > 0){
		conhtml += "<li><span>"+_reslib+"</span><a href='javascript:void(0);' onclick=\"delCon('reslib');\" title='"+operation_click_cancel_limit+"'></a></li>";
	}
	if(_restype != null && _restype.length > 0){
		conhtml += "<li><span>"+_restype+"</span><a href='javascript:void(0);' onclick=\"delCon('restype');\" title='"+operation_click_cancel_limit+"'></a></li>";
	}
	if(_xueke != null && _xueke.length > 0){
		conhtml += "<li><span>"+_xueke+"</span><a href='javascript:void(0);' onclick=\"delCon('xueke');\" title='"+operation_click_cancel_limit+"'></a></li>";
	}
	if(_creator != null && _creator.length > 0){
		conhtml += "<li><span>"+_creator+"</span><a href='javascript:void(0);' onclick=\"delCon('creator');\" title='"+operation_click_cancel_limit+"'></a></li>";
	}
	if(_publisher != null && _publisher.length > 0){
		conhtml += "<li><span>"+_publisher+"</span><a href='javascript:void(0);' onclick=\"delCon('publisher');\" title='"+operation_click_cancel_limit+"'></a></li>";
	}
	if(_keyword != null && _keyword.length > 0){
		conhtml += "<li><span>"+_keyword+"</span><a href='javascript:void(0);' onclick=\"delCon('keyword');\" title='"+operation_click_cancel_limit+"'></a></li>";
	}
	if(_date != null && _date.length > 0){
		conhtml += "<li><span>"+_date+"</span><a href='javascript:void(0);' onclick=\"delCon('date');\" title='"+operation_click_cancel_limit+"'></a></li>";
	}
	conhtml += "<p class='clear' />";
	$("#searchcon").html(conhtml);
	
}

//删除检索条件
function delCon(param){
	//将条件置为空后重新检索
	paramSearch(param, "");
}

/**
 *	表格模式图片渲染
 */
function image_rendering(){
	 $(".listbox #box #res3 .item p img").mouseenter(function(){
         var w = $(this).width(),
         Gleft = 152-w;
         if(Gleft < -10){
             $(this).animate({left:Gleft},500);
         }    
     }).mouseleave(function(){
         $(this).animate({left:0},500);
     })
}





















