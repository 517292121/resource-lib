/**
 *	将文件名称格式化为页码数
 *
 *  wanglin
 *  2019-09
 *
 */
//将文件名称转换为页数
function fpnum(title){
	var pnum = "<span>";
	if(title.length > 3 && title.substring(0,3).toLocaleLowerCase() == 'ltk'){
		pnum += parseLtk(title);
	}else if(title.length > 2 && title.substring(0,2).toLocaleLowerCase() == 'qh'){
		pnum += parseQh(title);
	}else if(title.split("-").length >= 2){
		pnum += parse2(title);
	}else{
	    pnum += parseCom(title);
	}
	pnum += "</span>";
	return pnum;
}

//解析两个组数字中以-分割格式，如“105174-0016”，取第二组数字
function parse2(fname){
	var arr = fname.split("-");
	var result = title_first;
	if(isNums(arr[0]) && isNums(arr[1])){
		result += parseInt(arr[1]);
	}else{
		result += arr[1];
	}
	result += title_page;
	return result;
}
	
//解析纯数字与字母开头的图书类型，如“T00068_00”或“0069”类型
function parseCom(fname){
	var name = fname.split("_")[0];
	
	// 纯数字模式
	if(isNums(name)){
		var res = title_first + parseInt(name) + title_page;
		return res;
	}
	
	// 联图命名模式，字母+页码
	var begin = name.substring(0, 1).toLocaleUpperCase();
	var result = "";
	if(begin == "A"){
		result = title_cover
	}else if(begin == "B"){
		result = title_before;
	}else if(begin == "C"){
		result = title_directory;
	}else if(begin == "D"){
		result = title_after;
	}else if(begin == "T"){
		result = title_body;
	}else if(begin == "Y"){
		result = title_attached;
	}else if(begin == "Z"){
		result = title_backCover;
	}else{
		return title_first + fname + title_page;
	}
	
	var num = name.substring(1, name.length);
	if(isNums(num)){
		num = parseInt(num);
	}
	result += title_first + num + title_page;
	return result;
}
function parseLtk(fname){
	var num = fname.substring(fname.length-5, fname.length);
	
	var result = title_first
	if(isNums(num)){
		result += parseInt(num) + title_page;
	}else{
		result += num + title_page;
	}
	return result;
}
function parseQh(fname){
	var arr = fname.split("-");
	var num = "";
	if(arr.length > 1){
		num = arr[1];
	}else{
		num = arr[0];
	}
	return num;
}
function isNums(v){
	if(/^[0-9]+$/.test(v)){
		return true;
	}else{
		return false;
	}
}