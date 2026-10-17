function switch_lang(lang){
	location.href= basePath + 'locale.htm?locale='+lang+'&from='+encodeURI(url_code(location.href));
}

//url进行编码
function url_code(url){
	if(url == null || url == "" || typeof url == "undefined"){
		return url;
	}
	//取出url请求路径
	var prefix_url = url.substring(0,url.indexOf("?")+1);
	prefix_url = encodeURI(prefix_url);
	//取出url参数部分
	var suffix_url = url.substring(url.indexOf("?")+1,url.length);
	
	var urlArr = suffix_url.split("&");
	
	//编码后的参数
	var codeStr;
	$.each(urlArr,function(index,emelent){
		if(index > 0){
			prefix_url += "splicing;";
		}
		codeStr = emelent.substring(0,emelent.indexOf("=")+1)+encodeURI(emelent.substring(emelent.indexOf("=")+1,emelent.length));
		prefix_url += codeStr;
	});
	return prefix_url;
}