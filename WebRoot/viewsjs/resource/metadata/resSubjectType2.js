//------------- 解决FF不支持innerText问题 ------------
var lBrowser = {};
lBrowser.agt = navigator.userAgent.toLowerCase();
lBrowser.isW3C = document.getElementById ? true:false;
lBrowser.isIE = ((lBrowser.agt.indexOf("msie") != -1) && (lBrowser.agt.indexOf("opera") == -1) && (lBrowser.agt.indexOf("omniweb") == -1));
lBrowser.isNS6 = lBrowser.isW3C && (navigator.appName=="Netscape") ;
lBrowser.isOpera = lBrowser.agt.indexOf("opera") != -1;
lBrowser.isGecko = lBrowser.agt.indexOf("gecko") != -1;
lBrowser.ieTrueBody =function (){
  return (document.compatMode && document.compatMode!="BackCompat")? document.documentElement : document.body;
};

//为Firefox下的DOM对象增加innerText属性
if(lBrowser.isNS6){ //firefox innerText define
 HTMLElement.prototype.__defineGetter__( "innerText", 
 function(){ 
 return this.textContent; 
 } 
 ); 
 HTMLElement.prototype.__defineSetter__( "innerText", 
 function(sText){ 
 this.textContent=sText; 
 } 
 ); 
}
// 解决IE和FF中childNodes的差别
function getElementChildNum(childId){
	var childNum = 0; 
    for(var i=0;i<childId.length;i++){                      
         if(childId[i].nodeType==1){                          
              childNum +=1; 
         } 
    } 
    return childNum;
}
// 获取指定节点对象
function getChildObj(nodes,index){
	var childNum = 0; 
    for(var i=0;i<nodes.length;i++){                      
	     if(nodes[i].nodeType==1){                          
	          if(childNum == index) return nodes[i];
	          childNum +=1; 
	     } 
    } 
    return null;
}
// 获取节点下标
function getNodeIndex(nodes,id){
	var childNum = 0; 
    for(var i=0;i<nodes.length;i++){                      
         if(nodes[i].nodeType==1){                          
             if(id == nodes[i].id){
				return childNum;
			 } 
             childNum +=1; 
         } 
    } 
    return childNum;
}

// 去除空格
String.prototype.trim = function()    
{    
return this.replace(/(^\s*)|(\s*$)/g, "");    
} 
//----------------------------------------------
// 编辑时使用到的变量
var isEditing = false;
var isEditing_id ;
var isEditing_name;

//新增时使用到的变量
var isAdding = false;
var isAdding_id ;
var addType = "";

var oldId = "";
function mouseout(count) {
	var tr = $("tr" + count);
	if(!tr)return;
	tr.style.backgroundColor = "";
	
	var span = $("span" + count);
	if(span != null) span.style.display = "none";
	
	//修改input的背景色
//	var input = $("name" + count);
//	if(input != null) 
//	if(input != null) {
//		if(!isEditing){
//			input.style.backgroundColor = tr.style.backgroundColor;
//		}else{
//			input.style.backgroundColor = "#ffffff";
//		}
//	}
}
function mouseover(count) {
	var tr = $("tr" + count);
	if(!tr)return;
	tr.style.backgroundColor = "#ccffcc";

	var span = $("span" + count);
	if(span != null) span.style.display = "";
	
	//修改input的背景色
//	var input = $("name" + count);
//	if(input != null) {
//		if(!isEditing){
//			input.style.backgroundColor = "#ccffcc";
//		}else{
//			input.style.backgroundColor = "#ffffff";
//		}
//	}
}
function remove(count){
	// 检测是否有内容未保存
	 if(!testIsEdition())return;
	 // 检测是否有新增分类未保存
	 if(!testIsAdding())return;

	 
	var tr = $("tr" + count);
	if(!tr) return;
	var temp = tr.parentNode;
	var nodeLength = getElementChildNum(temp.childNodes);
	if(temp && temp.id == count && nodeLength > 1) {
		alert('该分类下已经存在子分类，不允许删除！');
		return;
	}
	var con = confirm("你确定要删除该分类吗？");
	if(con) {
//		 var tr2_ = $("tr2_"+count);
//		 if(tr2_){
//		 	tr2_.parentNode.removeChild(tr2_);
//		 }else{
//			temp.removeChild(tr);
//		 }
		// 调用 dwr 更新数据
		delRST(count);
	}
}

/*
function up(count){
	// 检测是否有内容未保存
	if(!testIsEdition())return;
	// 检测是否有新增分类未保存
	if(!testIsAdding())return;
	
	var tr = $("tr" + count);
	if(!tr)return;
	var pNode = tr.parentNode;
	var cNodes = pNode.childNodes;
	var index = getNodeIndex(cNodes,tr.id);
	if(index > 1){
		pNode.removeChild(tr);
		pNode.insertBefore(tr,getChildObj(cNodes,index-1));
		// 更新数据
		handlerRSTIndex(count,"up");
	}else if(index == 0){
		// 移动整个分类
		var tableNode = pNode.parentNode;
		var divNode = tableNode.parentNode;
		var div_Nodes = divNode.childNodes;
		var tableIndex = getNodeIndex(div_Nodes,tableNode.id);
		if(tableIndex > 0){
			divNode.removeChild(tableNode);
			divNode.insertBefore(tableNode,getChildObj(div_Nodes,tableIndex-1));
			// 更新数据
			handlerRSTIndex(count,"up");
		}
	}
	tr.style.backgroundColor = "";
	var span = $("span" + count);
	if(span != null) span.style.display = "none";
}
function down(count){
	// 检测是否有内容未保存
	if(!testIsEdition())return;
	// 检测是否有新增分类未保存
	if(!testIsAdding())return;
	
	var tr = $("tr" + count);
	if(!tr)return;
	var pNode = tr.parentNode;
	var cNodes = pNode.childNodes;
	var index = getNodeIndex(cNodes,tr.id);
	var nodeLength = getElementChildNum(cNodes);
	if(index>0 && index < nodeLength - 1 ){
		pNode.insertBefore(getChildObj(cNodes,index+1),tr);
		// 更新数据
		handlerRSTIndex(count,"down");
	}else if(index == 0){
		// 移动整个分类
		var tableNode = pNode.parentNode;
		var divNode = tableNode.parentNode;
		var div_Nodes = divNode.childNodes;
		var tableIndex = getNodeIndex(div_Nodes,tableNode.id);
		var div_nodeLength = getElementChildNum(div_Nodes);
		if(tableIndex > -1 && tableIndex < div_nodeLength - 1 ){
			divNode.insertBefore(getChildObj(div_Nodes,tableIndex+1),tableNode);
			// 更新数据
			handlerRSTIndex(count,"down");
		}
	}
}



var i=0;
function add(count){
	// 检测是否有内容未保存
	if(!testIsEdition())return;
	// 检测是否有新增分类未保存
	if(!testIsAdding())return;
	
	var tr = $("tr" + count);
	i++;
	var id = "20000"+i;
	oldId = id;
	if(tr){
		var temp = tr.parentNode;
		var row = document.createElement("tr");
		row.id = "tr"+id;
		row.className = "w_background";
		row.onmouseout = function(){    
			mouseout(id);
		}
		row.onmouseover = function(){ 
			mouseover(id);
		}
		var td = document.createElement("td");
		td.align = "left";
		td.className = "tr_top_no_border";
		
		var txt = '<div style="float: left;" class="f_p">'
				 + '<span class="p_r"></span><img align="absmiddle" src="'+getPath()+'images/subject/class2.gif" ><b><input id="name'+id+'"  size="20"  style="height:20px;border:1px solid #ABC7EC;background-color: #ffffff;" onblur="saveEdit(\'name'+id+'\',this)" onkeypress="if(event.keyCode==13){saveEdit(\'name'+id+'\',this)}"  value="" /></b>&nbsp;&nbsp;'
				 + '<span id="span'+id+'" style="display: none;"><a href="javascript:void(0)"><img id="up'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/up1.gif" onclick="up(\''+id+'\')"></a>&nbsp;&nbsp;<a href="javascript:void(0)"><img id="down'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/down1.gif" onclick="down(\''+id+'\')"></a></span>'
		         + '</div>'
		         + '<div style="float:right;" class="div_p_r">'
		         + '<a href="javascript:void(0)"><img id="edit'+id+'" border="0" src="'+getPath()+'images/subject/edit.gif" onclick="changeToInput(\'name'+id+'\')"></a><span class="my_p_r"></span>&nbsp;'
		         + '<a href="javascript:void(0)"><img id="delete'+id+'" border="0" src="'+getPath()+'images/subject/delete.gif" onclick="remove(\''+id+'\')"></a>'
		         + '</div>';
		
		td.innerHTML = txt;
		row.appendChild(td);
		temp.appendChild(row);
		
		window.setTimeout(function(){$('name'+id).focus();}, 0);
//		isEditing = true;
//		isNew = true;
		
		//给新增变量赋初始值
		isAdding = true;
		isAdding_id = id;
		addType = "2";
	}
}
var j=0;
function addGrade(){
	// 检测是否有内容未保存
	if(!testIsEdition())return;
	// 检测是否有新增分类未保存
	if(!testIsAdding())return;
	
	
	var div = $("dtable");
	j++;
	var id = "10000"+j;
	oldId = id;
	if(div){
//		var lastBG = div.lastChild.id;
//		if(!lastBG)return;
//		var count = getCount(lastBG);
//		var lastCN = $("tr"+count).className
		var tempCN = "";
//		if("w_background" == lastCN)
//			tempCN = "y_background";
//		else
//			tempCN = "w_background";
			
		var table_ = document.createElement("table");
		table_.className = "adTable tr_top_no_border";
		table_.width = "100%";
		table_.cellpadding = "0";
		table_.cellspacing = "0";
		table_.id = "table"+id;
		table_.style.borderCollapse = "collapse";
		
		var tbody_ = document.createElement("tbody");
		tbody_.id = id;
		
		var row = document.createElement("tr");
		row.id = "tr"+id;
		row.className = "y_background";
		row.onmouseout = function(){    
			mouseout(id);
		}
		row.onmouseover = function(){ 
			mouseover(id);
		}
		var td = document.createElement("td");
		td.align = "left";
		td.className = "tr_top_no_border";
		
		var txt = '<tr  onmouseout="mouseout(\''+id+'\')" onmouseover="mouseover(\''+id+'\')" id="tr'+id+'" class="'+tempCN+'" style="">'
		         + '<td align="left" >'
				 + '<div style="float: left;">'
				 + '<span class="p_l_r"></span><img align="absmiddle" src="'+getPath()+'images/subject/class1.gif"><b><input id="name'+id+'"  size="20"  style="height:20px;border:1px solid #ABC7EC;background-color: #ffffff;" onblur="saveEdit(\'name'+id+'\',this)" onkeypress="if(event.keyCode==13){saveEdit(\'name'+id+'\',this)}"   value="" /></b>&nbsp;&nbsp;'
				 + '<span id="span'+id+'" style="display: none;"><a href="javascript:void(0)"><img id="up'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/up1.gif" onclick="up(\''+id+'\')"></a>&nbsp;&nbsp;<a href="javascript:void(0)"><img id="down'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/down1.gif" onclick="down(\''+id+'\')"></a></span>'
		         + '</div>'
		         + '<div style="float:right;" class="div_p_r">'
		         + '<a href="javascript:void(0)"><img id="add'+id+'" border="0" src="'+getPath()+'images/subject/add2.gif" onclick="add(\''+id+'\')"></a><span class="my_p_r">&nbsp;</span>'
		         + '<a href="javascript:void(0)"><img id="edit'+id+'" border="0" src="'+getPath()+'images/subject/edit.gif" onclick="changeToInput(\'name'+id+'\')"></a><span class="my_p_r"></span>&nbsp;'
		         + '<a href="javascript:void(0)"><img id="delete'+id+'" border="0" src="'+getPath()+'images/subject/delete.gif" onclick="remove(\''+id+'\')"></a>'
		         + '</div>'
		         + '</td>'
		         + '</tr>';
		         
		td.innerHTML = txt;
		row.appendChild(td);
		tbody_.appendChild(row);
		table_.appendChild(tbody_);
		div.appendChild(table_);
		window.setTimeout(function(){$('name'+id).focus();}, 0);
//		isEditing = true;
//		isNew = true;
		
		//给新增变量赋初始值
		isAdding = true;
		isAdding_id = id;
		addType = "1";
	}
}
*/


function up(count, type) {
	 // 检测是否有内容未保存
	 if(!testIsEdition())return;
	 // 检测是否有新增分类未保存
	 if(!testIsAdding())return;

	var tr = $("tr" + count);
	if(!tr) return;
	var pNode = tr.parentNode;
	var cNodes = pNode.childNodes;
	var index = getNodeIndex(cNodes, tr.id);
	if(index > 1) {
		// 移动二级分类
		pNode.removeChild(tr);
		pNode.insertBefore(tr, getChildObj(cNodes, index - 1));
		// // 更新数据
		handlerRSTIndex(count,"up");
	}else if(index == 0) {
		if("a" == type) {
			var table1Node = $("table" + count);
			if(!table1Node) return;
			var divNode = table1Node.parentNode;
			var div_Nodes = divNode.childNodes;
			var table1Index = getNodeIndex(div_Nodes, table1Node.id);

			if(table1Index > 0) {
				divNode.removeChild(table1Node);
				divNode.insertBefore(table1Node, getChildObj(div_Nodes, table1Index - 1));
				// 更新数据
				handlerRSTIndex(count,"up");
			}
		}else {
			var table2Node = pNode.parentNode;
			var table2_Tr = table2Node.parentNode.parentNode;
			var table2Tbody = table2_Tr.parentNode;
			var table2Tbody_Nodes = table2Tbody.childNodes;
			var tableIndex = getNodeIndex(table2Tbody_Nodes, table2_Tr.id);
			if(tableIndex > 1) {
				table2Tbody.removeChild(table2_Tr);
				table2Tbody.insertBefore(table2_Tr, getChildObj(table2Tbody_Nodes, tableIndex - 1));
				// 更新数据
				handlerRSTIndex(count,"up");
			}
		}
	}
	tr.style.backgroundColor = "";
	var span = $("span" + count);
	if(span != null) span.style.display = "none";
}
function down(count, type) {
	 // 检测是否有内容未保存
	 if(!testIsEdition())return;
	 // 检测是否有新增分类未保存
	 if(!testIsAdding())return;

	var tr = $("tr" + count);
	if(!tr) return;
	var pNode = tr.parentNode;
	var cNodes = pNode.childNodes;
	var index = getNodeIndex(cNodes, tr.id);
	var nodeLength = getElementChildNum(cNodes);
	if(index > 0 && index < nodeLength - 1) {
		pNode.insertBefore(getChildObj(cNodes, index + 1), tr);
		// 更新数据
		handlerRDTIndex(count,"down");
	}else if(index == 0) {
		if("a" == type) {
			var table1Node = $("table" + count);
			if(!table1Node) return;
			var divNode = table1Node.parentNode;
			var div_Nodes = divNode.childNodes;
			var table1Index = getNodeIndex(div_Nodes, table1Node.id);
			var div_nodeLength = getElementChildNum(div_Nodes);
			if(table1Index > -1 && table1Index < div_nodeLength - 1) {
				divNode.insertBefore(getChildObj(div_Nodes, table1Index + 1), table1Node);
				// 更新数据
				handlerRSTIndex(count,"down");
			}

		}else {
			var table2Node = pNode.parentNode;
			var table2_Tr = table2Node.parentNode.parentNode;
			var table2Tbody = table2_Tr.parentNode;
			var table2Tbody_Nodes = table2Tbody.childNodes;
			var tableIndex = getNodeIndex(table2Tbody_Nodes, table2_Tr.id);
			var div_nodeLength_2 = getElementChildNum(table2Tbody_Nodes);

			if(tableIndex > 0 && tableIndex < div_nodeLength_2 - 1) {
				table2Tbody.insertBefore(getChildObj(table2Tbody_Nodes, tableIndex + 1), table2_Tr);
				// 更新数据
				handlerRSTIndex(count,"down");
			}
		}
	}
}

var j = 0;
function add1() {
	// 检测是否有内容未保存
	if(!testIsEdition()) return;
	// 检测是否有新增分类未保存
	if(!testIsAdding()) return;

	var div = $("dtable");
	j++;
	var id = "10000" + j;
	oldId = id;
	if(div) {
		var table_ = document.createElement("table");
		table_.className = "adTable tr_top_no_border";
		table_.width = "100%";
		table_.cellspacing = "0";
		table_.cellspacing = "0";
		table_.id = "table" + id;
		table_.borderColor = "#e8e8e8";
//		table_.border = "1";
		table_.style.borderCollapse = "collapse";
		

		var tbody_ = document.createElement("tbody");
		tbody_.id = id;

		var row = document.createElement("tr");
		row.id = "tr"+id;
		row.className = "y_background";
		row.onmouseout = function(){    
			mouseout(id);
		}
		row.onmouseover = function(){ 
			mouseover(id);
		}
		var td = document.createElement("td");
		td.align = "left";
		td.className = "td_p tr_top_no_border";

		var txt = '<tr  onmouseout="mouseout(\''+id+'\')" onmouseover="mouseover(\''+id+'\')" id="tr'+id+'" style="">'
		         + '<td align="left" class="td_p">'
				 + '<div style="float: left;">'
				 + '<span class="p_l_r"></span><img align="absmiddle" src="'+getPath()+'images/subject/class1.gif"  ><b><input id="name'+id+'"  size="20"  style="height:20px;border:1px solid #ABC7EC;background-color: #ffffff;" onblur="saveEdit(\'name'+id+'\',this)" onkeypress="if(event.keyCode==13){saveEdit(\'name'+id+'\',this)}"  value="" /></b>&nbsp;&nbsp;'
				 + '<span id="span'+id+'" style="display: none;"><a href="javascript:void(0)"><img id="upa'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/up1.gif" onclick="up(\''+id+'\',\'a\')"></a>&nbsp;&nbsp;<a href="javascript:void(0)"><img id="downa'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/down1.gif" onclick="down(\''+id+'\',\'a\')"></a></span>'
		         + '</div>'
		         + '<div style="float:right;" class="div_p_r">'
		         + '<a href="javascript:void(0)"><img id="add2'+id+'" border="0" src="'+getPath()+'images/subject/add2.gif" onclick="add2(\''+id+'\')"></a><span class="my_p_r"></span>&nbsp;'
		         + '<a href="javascript:void(0)"><img id="edit'+id+'" border="0" src="'+getPath()+'images/subject/edit.gif" onclick="changeToInput(\'name'+id+'\')"></a><span class="my_p_r"></span>&nbsp;'
		         + '<a href="javascript:void(0)"><img id="delete'+id+'" border="0" src="'+getPath()+'images/subject/delete.gif" onclick="remove(\''+id+'\')"></a>'
		         + '</div>'
		         + '</td>'
		         + '</tr>';

		td.innerHTML = txt;
		row.appendChild(td);
		tbody_.appendChild(row);
		table_.appendChild(tbody_);
		div.appendChild(table_);
		window.setTimeout(function() {$('name' + id).focus();}, 0);

		// 给新增变量赋初始值
		isAdding = true;
		isAdding_id = id;
		addType = "1";
	}
}

var k = 0;
function add2(count){
	
	// 检测是否有内容未保存
	if(!testIsEdition())return;
	// 检测是否有新增分类未保存
	if(!testIsAdding())return;
	
	var tr = $(count);
	k++;
	var id = "20000"+k;
	oldId = id;
	if(tr){
//		var temp = tr.parentNode;
		var row = document.createElement("tr");
		row.id = "tr2_"+id;
		var td = document.createElement("td");
		td.align = "left";
		
		var table_ = document.createElement("table");
		table_.className = "adTable2 tr_top_no_border";
		table_.width = "100%";
		table_.cellspacing = "0";
		table_.cellspacing = "0";
		table_.id = "table" + id;
		table_.borderColor = "#e8e8e8";
//		table_.border = "1";
		table_.style.borderCollapse = "collapse";

		var tbody_ = document.createElement("tbody");
		tbody_.id = id;

		var tr_ = document.createElement("tr");
		tr_.id = "tr"+id;
		tr_.className = "w_background";
		tr_.onmouseout = function(){    
			mouseout(id);
		}
		tr_.onmouseover = function(){ 
			mouseover(id);
		}
		var td_ = document.createElement("td");
		td_.align = "left";
		td_.className = "td_p tr_top_no_border";
		
		
		
		var txt = '<div style="float: left;" class="f_p">'
				 + '<span class="p_r"></span><img align="absmiddle" src="'+getPath()+'images/subject/class2.gif"  ><b><input id="name'+id+'"  size="20"  style="height:20px;border:1px solid #ABC7EC;background-color: #ffffff;" onblur="saveEdit(\'name'+id+'\',this)" onkeypress="if(event.keyCode==13){saveEdit(\'name'+id+'\',this)}"  value="" /></b>&nbsp;&nbsp;'
				 + '<span id="span'+id+'" style="display: none;"><a href="javascript:void(0)"><img id="upb'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/up1.gif" onclick="up(\''+id+'\',\'b\')"></a>&nbsp;&nbsp;<a href="javascript:void(0)"><img id="downb'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/down1.gif" onclick="down(\''+id+'\',\'b\')"></a></span>'
		         + '</div>'
		         + '<div style="float:right;" class="div_p_r_2">'
		         + '<a href="javascript:void(0)"><img id="add3'+id+'" border="0" src="'+getPath()+'images/subject/add2.gif" onclick="add3(\''+id+'\')"></a><span class="my_p_r"></span>&nbsp;'
		         + '<a href="javascript:void(0)"><img id="edit'+id+'" border="0" src="'+getPath()+'images/subject/edit.gif" onclick="changeToInput(\'name'+id+'\')"></a><span class="my_p_r"></span>&nbsp;'
		         + '<a href="javascript:void(0)"><img id="delete'+id+'" border="0" src="'+getPath()+'images/subject/delete.gif" onclick="remove(\''+id+'\')"></a>'
		         + '</div>';
		
		td_.innerHTML = txt;
		tr_.appendChild(td_);
		tbody_.appendChild(tr_);
		table_.appendChild(tbody_);
		td.appendChild(table_);         
		row.appendChild(td);
		tr.appendChild(row);
		
		window.setTimeout(function(){$('name'+id).focus();}, 0);
		
		//给新增变量赋初始值
		isAdding = true;
		isAdding_id = id;
		addType = "2";
	}
}

var i=0;
function add3(count){
	// 检测是否有内容未保存
	if(!testIsEdition())return;
	// 检测是否有新增分类未保存
	if(!testIsAdding())return;
	
	var tr = $("tr" + count);
	i++;
	var id = "30000"+i;
	oldId = id;
	if(tr){
		var temp = tr.parentNode;
		var row = document.createElement("tr");
		row.id = "tr"+id;
		row.className = "x_background";
		row.onmouseout = function(){    
			mouseout(id);
		}
		row.onmouseover = function(){ 
			mouseover(id);
		}
		var td = document.createElement("td");
		td.align = "left";
		td.className = "td_p tr_top_no_border";
		var txt = '<div style="float: left;" class="f_p_3">'
				 + '<span class="p_r"></span><img align="absmiddle" src="'+getPath()+'images/subject/class3.gif"  ><b><input id="name'+id+'"  size="20"  style="height:20px;border:1px solid #ABC7EC;background-color: #ffffff;" onblur="saveEdit(\'name'+id+'\',this)" onkeypress="if(event.keyCode==13){saveEdit(\'name'+id+'\',this)}"  value="" /></b>&nbsp;&nbsp;'
				 + '<span id="span'+id+'" style="display: none;"><a href="javascript:void(0)"><img id="up'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/up1.gif" onclick="up(\''+id+'\')"></a>&nbsp;&nbsp;<a href="javascript:void(0)"><img id="down'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/down1.gif" onclick="down(\''+id+'\')"></a></span>'
		         + '</div>'
		         + '<div style="float:right;" class="div_p_r_2">'
		         + '<a href="javascript:void(0)"><img id="edit'+id+'" border="0" src="'+getPath()+'images/subject/edit.gif" onclick="changeToInput(\'name'+id+'\')"></a><span class="my_p_r"></span>&nbsp;'
		         + '<a href="javascript:void(0)"><img id="delete'+id+'" border="0" src="'+getPath()+'images/subject/delete.gif" onclick="remove(\''+id+'\')"></a>'
		         + '</div>';
		
		td.innerHTML = txt;
		row.appendChild(td);
		temp.appendChild(row);
		
		window.setTimeout(function(){$('name'+id).focus();}, 0);
		
		//给新增变量赋初始值
		isAdding = true;
		isAdding_id = id;
		addType = "3";
	}
}





function changeToInput(id){
	// 检测是否有内容未保存
	if(!testIsEdition())return;
	// 检测是否有新增分类未保存
	if(!testIsAdding())return;
	
	var temp = $(id);
	var txt = temp.innerText;
	if(temp){
		var count = id.substring(4);
		temp.parentNode.innerHTML = '<input id="name'+count+'"  size="20"  style="height:20px;border:1px solid #ABC7EC;background-color: #ffffff;" onblur="saveEdit(\''+id+'\',this)" onkeypress="if(event.keyCode==13){saveEdit(\'' + id + '\',this)}"  value="'+txt+'" />';
		window.setTimeout(function(){$('name'+count).focus();}, 0);
		
		// 给编辑状态变量赋初值
		isEditing = true;
		isEditing_id = count;
		isEditing_name = $("name"+count).value;
	}
	
}
function saveEdit(id,item){
	var temp = $(id);
	var subjectName = item.value.trim();
	var count = id.substring(4);
	if(temp){
		if(subjectName.trim().length<1 || subjectName==null || subjectName==""){
			item.style.border = "1px solid red";
			item.style.backgroundColor = "#ffffff";
			item.title = "学科分类名称不能为空！";
			window.setTimeout(function(){item.focus();}, 0);
			return false;
		}
		if(subjectName.trim().length > 20){
			item.style.border = "1px solid red";
			item.style.backgroundColor = "#ffffff";
			item.title = "学科分类名称长度不能超过20个字符！";
			window.setTimeout(function(){item.focus();}, 0);
			return false;
		}
		// 校验名称是否存在
		 if(!checkSubjectTypeName(count,subjectName)){
		 	item.style.border = "1px solid red";
			item.style.backgroundColor = "#ffffff";
			item.title = "学科分类名称已经存在！";
			item.value = "";
			alert("学科分类名称已经存在！");
			window.setTimeout(function(){item.focus();}, 0);
			return false;
		 }
		
		
		var tbody_ = $("tr"+count);
		var pid = tbody_.parentNode.id;
		if(isEditing){
			updateRST(count , subjectName);
		}else{
			if(addType == "2"){
				var tbody_1 = $("tr2_"+count);
				var pid_1 = tbody_1.parentNode.id;
				addRST(count,  pid_1,  subjectName);
			}else{
				var tbody_2 = $("tr"+count);
				var pid_2 = tbody_2.parentNode.id;
				addRST(count,  pid_2,  subjectName);
			}
		}
		
		// 将编辑时使用到的变量置空；
		isEditing = false;
		isEditing_id = "";
		isEditing_name = "";
		// 将新增时使用到的变量置空；
		isAdding = false;
		isAdding_id = "";
		addType = "";
		
		temp.parentNode.innerHTML = '<span onclick="changeToInput(\'name'+count+'\')" id="name'+count+'">'+subjectName+'</span>';
	}
//	isEditing = false;
//	isNew = false;
}
function getCount(val){
	if(!val)return 0;
	return val.substring(5);
}
function checkInput(val){
	if(val.trim().length<1 || val==null || val==""){
		alert("学科分类名称不能为空！")
		return false;
	}
	if(val.trim().length > 20){
		alert("学科分类名称长度不能超过20个字符！");
		return false;
	}
	return true;
}
// 校验分类名称是否存在
function checkSubjectTypeName(id,subjectName){
	if("1" == addType){
		var div = $("dtable");
		if(div){
			var temp = div.childNodes;
			var nodeLength = getElementChildNum(temp);
			for(var i=0;i<nodeLength;i++){
				var t_id = getChildObj(temp,i).id.substring(5);
				var name = $("name"+t_id);
				if(name){
					var s_name = name.innerText;
					if(s_name && s_name == subjectName.trim())
						return false;
				}
			}
		}
	}else{
		var obj;
		if(isEditing){
			obj = $("table"+id);
			if(!obj) obj = $("tr"+id);
		}else{
			obj = $("tr"+id);
		}
		var temp = obj.parentNode.childNodes;
		var temp_nodeLength = getElementChildNum(temp);
		for(var i=1;i<temp_nodeLength;i++){
			var temp_childNode = getChildObj(temp,i);
			var t_id = temp_childNode.id.substring(0,2);
			if("tr" != t_id)
				t_id = temp_childNode.id.substring(5);
			else
				t_id = temp_childNode.id.substring(2);
			var name = $("name"+t_id);
			if(name){
				var s_name = name.innerText;
				if(s_name && s_name == subjectName.trim())
					return false;
			}
		}
	}
	return true;
}
// 检测是否有数据未保存:此处主要是针对当值为空或者名称过长的时候；
function testIsEdition(){
	if(isEditing){
		var con = confirm("有编辑的内容还未保存，是否保存？");
		if(con){
			window.setTimeout(function(){$('name'+isEditing_id).focus();}, 0);
			return false;
		}else{
			$('name'+isEditing_id).parentNode.innerHTML = '<span onclick="changeToInput(\'name'+isEditing_id+'\')" id="name'+isEditing_id+'">'+isEditing_name+'</span>';
			isEditing = false;
			isEditing_id = "";
			isEditing_name = "";
			return false;
		}
	}
	return true;
}
// 检测是否有新增分类还未保存：主要针对分类名称为空或者名称过长的情况；
function testIsAdding(){
	if(isAdding){
		var con = confirm("有新增的分类还未保存，是否保存？");
		if(con){
			window.setTimeout(function(){$('name'+isAdding_id).focus();}, 0);
			return false;
		}else{
//			var temp = isAdding_id.substring(0,1);
			var temp = mySubString(isAdding_id,0,1);
			if(temp == "1"){
				var obj = $("table"+isAdding_id);
				obj.parentNode.removeChild(obj);
			}else{
				var obj2 = $("tr"+isAdding_id);
				obj2.parentNode.removeChild(obj2);
			}
			isAdding_id = "";
			isAdding = false;
			addType = "";
			return false;
		}
	}
	return true;
}
// 自定义substring
function mySubString(val,begin,end){
	if(val||val.trim().length<1)return "";
	if(end||end<0)
		return val+"".substring(begin);
	return val+"".substring(begin,end);
}
// --------------------------------- DWR --------------------------------
// 删除学科分类
function delRST(rstyId){
	resourceFacade.deleteResSubjectType(rstyId, function(data){
		var msgs = eval("("+data+")");
		var isOk = msgs.isOk;
		var tempGrade = msgs.tempGrade;
		if(!isOk) {
			alert(msgs.msg);
		}else {
			 var tr = $("tr" + rstyId);
			 var table_2 = $("table" + rstyId);
			 if(!tr) return;
			 var temp = tr.parentNode;
			 var tr2_ = $("tr2_"+rstyId);
			 if(tr2_){
			 	tr2_.parentNode.removeChild(tr2_);
			 }else if(table_2){
			 	var dtable = $("dtable");
			 	dtable.removeChild(table_2);
			 }else{
				temp.removeChild(tr);
			 }
		}
		
	});
}
// 新增学科分类
function addRST(tempId , pId , rstyName){
	resourceFacade.addResSubjectType( tempId,  pId,  rstyName, function(data){
		var msgs = eval("("+data+")");
		var isOk = msgs.isOk;
		var rstyId = msgs.rstyId;
		if (isOk && rstyId!=null) {
			// 将Id值更新
			var table = $("table"+oldId)
			if(table)table.id = "table"+rstyId;
			
			var tbody = $(oldId)
			if(tbody)tbody.id = rstyId;
			
			var tr = $("tr"+oldId);
			if(tr){
				tr.id = "tr"+rstyId;
				tr.onmouseout = function(){    
					mouseout(rstyId);
				}
				tr.onmouseover = function(){ 
					mouseover(rstyId);
				}
			}
			
			var name = $("name"+oldId);
			if(name){
				name.id = "name"+rstyId;
				name.onclick = function(){    
					changeToInput('name'+rstyId);
				}
			}
			
			var span = $("span"+oldId);
			if(span){
				span.id = "span"+rstyId;
			}
			
			var upa = $("upa"+oldId);
			if(upa){
				upa.onclick = function(){
					up(rstyId,'a');
				}
			}
			
			var upb = $("upb"+oldId);
			if(upb){
				upb.onclick = function(){
					up(rstyId,'b');
				}
			}
			
			var up_ = $("up"+oldId);
			if(up_){
				up_.onclick = function(){
					up(rstyId);
				}
			}
			
			var down_ = $("down"+oldId);
			if(down_){
				down_.onclick = function(){
					down(rstyId);
				}
			}
			
			var downa = $("downa"+oldId);
			if(downa){
				downa.onclick = function(){
					down(rstyId,'a');
				}
			}
			
			var downb = $("downb"+oldId);
			if(downb){
				downb.onclick = function(){
					down(rstyId,'b');
				}
			}
			
			var add2_ = $("add2"+oldId);
			if(add2_){
				add2_.onclick = function(){
					add2(rstyId);
				}
			}
			
			var add3_ = $("add3"+oldId);
			if(add3_){
				add3_.onclick = function(){
					add3(rstyId);
				}
			}
			
			var edit = $("edit"+oldId);
			if(edit){
				edit.onclick = function(){
					changeToInput('name'+rstyId);
				}
			}
			
			var del = $("delete"+oldId);
			if(del){
				del.onclick = function(){
					remove(rstyId);
				}
			}
			
			var tr2_ = $("tr2_"+oldId);
			if(tr2_)tr2_.id = "tr2_"+rstyId;
			
			
			oldId = "";
			
		}else{
			alert(msgs.msg);
			var tempId = msgs.tempId;
			if(tempId && tempId>0){
				$("name"+tempId).parentNode.innerHTML = '<input id="name'+tempId+'"  size="20"  style="height:20px;border:1px solid #ABC7EC;background-color: #ffffff;" onblur="saveEdit(\'name'+tempId+'\',this)" onkeypress="if(event.keyCode==13){saveEdit(\'name'+tempId+'\',this)}"  value="" />';
				window.setTimeout(function(){$('name'+tempId).focus();}, 0);
				isAdding = true;
				isAdding_id = tempId;
			}
		}
	});
}
// 修改分类名称
function updateRST(rstyId , rstyName){
	resourceFacade.updateResSubjectType(rstyId,rstyName, function(data){
		var msgs = eval("("+data+")");
		var isOk = msgs.isOk;
		if(!isOk){
			alert(msgs.msg);
			var tempId = msgs.tempId;
			if(tempId && tempId>0){
				$("name"+tempId).parentNode.innerHTML = '<input id="name'+tempId+'"  size="20"  style="height:20px;border:1px solid #ABC7EC;background-color: #ffffff;" onblur="saveEdit(\'name'+tempId+'\',this)" onkeypress="if(event.keyCode==13){saveEdit(\'name'+tempId+'\',this)}"  value="" />';
				window.setTimeout(function(){$('name'+tempId).focus();}, 0);
				isEditing = true;
				isEditing_id = ""+tempId+"";
				isEditing_name = msgs.rstyName;
			}
		}
	});
}
// 操作分类排序
function handlerRSTIndex(rstyId,type){
	resourceFacade.handlerResSubjectTypeIndex(rstyId,type, function(data){
		var msgs = eval("("+data+")");
		var isOk = msgs.isOk;
		if(!isOk){
			alert(msgs.msg);
		}
	});
}