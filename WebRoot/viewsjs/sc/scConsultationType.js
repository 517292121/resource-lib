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
}
function mouseover(count) {
	var tr = $("tr" + count);
	if(!tr)return;
	tr.style.backgroundColor = "#ccffcc";

	var span = $("span" + count);
	if(span != null) span.style.display = "";
}
function remove(count){
	// 检测是否有内容未保存
	if(!testIsEdition())return;
	// 检测是否有新增分类未保存
	if(!testIsAdding())return;
	
	var tr = $("tr" + count);
	if(!tr)return;
	var temp = tr.parentNode;
	var con = confirm("你确定要删除该分类吗？");
	if(con){
//		temp.removeChild(tr);// 1108
		//调用 dwr 更新数据
		delRST(count);
	}
}
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
	// 添加行间隔色
	var xia = tr.className;
	if(index > 0){
		pNode.removeChild(tr);
		// 添加隔行变色
		var temp_01 = getChildObj(cNodes,index-1);
		var shang = temp_01.className;
		temp_01.className = xia;
		tr.className = shang;
		pNode.insertBefore(tr,temp_01);
		// 更新数据
		handlerRSTIndex(count,"up");
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
	// 添加隔行变色
	var shang = tr.className;
	if(index>-1 && index < nodeLength - 1 ){
		// 添加隔行变色
		var temp_02 = getChildObj(cNodes,index+1);
		var xia = temp_02.className;
		tr.className = xia;
		temp_02.className = shang;
		pNode.insertBefore(temp_02,tr);
		// 更新数据
		handlerRSTIndex(count,"down");
	}
}



var i=0;
function add(){
	// 检测是否有内容未保存
	if(!testIsEdition())return;
	// 检测是否有新增分类未保存
	if(!testIsAdding())return;
	
	var tbody_ = $("tbody_");
	i++;
	var id = "20000"+i;
	oldId = id;
	if(tbody_){
		var row = document.createElement("tr");
		row.id = "tr"+id;
		
		// 添加行间隔色
		var cNodes = tbody_.childNodes;
		var nodeLength = getElementChildNum(cNodes);
		if(nodeLength>0){
			var obj=getChildObj(cNodes,nodeLength-1);
			if("y_background" == obj.className)
				row.className = "w_background" ;
			else
				row.className = "y_background" ;
		}else{
			row.className = "y_background";
		}
		
		row.onmouseout = function(){    
			mouseout(id);
		}
		row.onmouseover = function(){ 
			mouseover(id);
		}
		var td = document.createElement("td");
		td.align = "left";
		td.className = "tr_top_no_border";
		
		var txt = '<div style="float: left;">'
				 + '<span class="p_l_r"></span><img align="absmiddle" src="'+getPath()+'images/subject/class1.gif" ><b><input id="name'+id+'"  size="20"  style="height:20px;border:1px solid #ABC7EC;background-color: #ffffff;" onblur="saveEdit(\'name'+id+'\',this)" onkeypress="if(event.keyCode==13){saveEdit(\'name'+id+'\',this)}"  value="" /></b>&nbsp;&nbsp;'
				 + '<span id="span'+id+'" style="display: none;"><a href="javascript:void(0)"><img id="up'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/up1.gif" onclick="up(\''+id+'\')"></a>&nbsp;&nbsp;<a href="javascript:void(0)"><img id="down'+id+'" border="0" align="absmiddle"   src="'+getPath()+'images/subject/down1.gif" onclick="down(\''+id+'\')"></a></span>'
		         + '</div>'
		         + '<div style="float:right;" class="div_p_r">'
		         + '<a href="javascript:void(0)"><img id="edit'+id+'" border="0" src="'+getPath()+'images/subject/edit.gif" onclick="changeToInput(\'name'+id+'\')"></a><span class="my_p_r"></span>&nbsp;'
		         + '<a href="javascript:void(0)"><img id="delete'+id+'" border="0" src="'+getPath()+'images/subject/delete.gif" onclick="remove(\''+id+'\')"></a>'
		         + '</div>';
		
		td.innerHTML = txt;
		row.appendChild(td);
		tbody_.appendChild(row);
		
		window.setTimeout(function(){$('name'+id).focus();}, 0);
		
		//给新增变量赋初始值
		isAdding = true;
		isAdding_id = id;
		addType = "2";
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
			item.title = "咨询分类名称不能为空！";
			window.setTimeout(function(){item.focus();}, 0);
			return false;
		}
		if(subjectName.trim().length > 20){
			item.style.border = "1px solid red";
			item.style.backgroundColor = "#ffffff";
			item.title = "咨询分类名称长度不能超过20个字符！";
			window.setTimeout(function(){item.focus();}, 0);
			return false;
		}
		
		var tbody_ = $("tr"+count);
		var pid = tbody_.parentNode.id;
		if(isEditing){
			updateRST(count , subjectName);
		}else{
			addRST(count, 0 , subjectName);
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
// 删除分类
function delRST(rstyId){
	scFacade.deleteScConsultationType(rstyId, function(data){
		var msgs = eval("("+data+")");
		var isOk = msgs.isOk;
		if(!isOk) {
			alert(msgs.msg);
		}else {
			var tr = $("tr" + rstyId);
			if(!tr) return;
			var temp = tr.parentNode;
			temp.removeChild(tr);
		}
		
	});
}
// 新增咨询分类
function addRST(tempId , pId , rstyName){
	scFacade.addScConsultationType(tempId, pId , rstyName, function(data){
		var msgs = eval("("+data+")");
		var isOk = msgs.isOk;
		var rstyId = msgs.rstyId;
		if (isOk && rstyId!=null) {
			// 将Id值更新
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
			
			var add_ = $("add"+oldId);
			if(add_){
				add_.onclick = function(){
					add(rstyId);
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
	scFacade.updateScConsultationType(rstyId,rstyName, function(data){
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
	scFacade.handlerScConsultationTypeIndex(rstyId,type, function(data){
		var msgs = eval("("+data+")");
		var isOk = msgs.isOk;
		if(!isOk){
			alert(msgs.msg);
		}
	});
}  