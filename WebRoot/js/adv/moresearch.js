
function checksearch(){
	var leng = $("#table1").find("input").length;
	var inputVal="";
	for(var i=0;i<leng;i++){
		inputVal += $("#table1").find("input")[i].value; 
	}
	if(inputVal.length<1){
		alert('请至少录入一个检索条件', 11, !1);
		return false;
	}else{
		return true;
	}
}

var row_count = 4; 
function addTab(){
 	if(row_count<6){
	 	var table1 = $('#table1'); 
		var row = $("<tr></tr>"); 
		var td1 = $("<td></td>"); 
		var td2 = $("<td></td>"); 
		var td3 = $("<td></td>");
		td1.append($("<select name='logic"+row_count+"' class='sea_select'><option value='and'>"+and+"</option><option value='or'>"+or+"</option></select>&nbsp;&nbsp;"));
		td1.append($("<select name='ele"+row_count+"' class='sea_select' style='margin-left: 4px;'>").append(condtion).append("</select>")); 
		td2.append($("<input name='key"+row_count+"' id='key"+row_count+"' class='sea_input'>"));
		td3.append($("<select name='like"+row_count+"' class='sea_select'><option value='1'>精确</option><option value='2'>模糊</option></select> "));
		row.append(td1); 
		row.append(td2);
		row.append(td3);
		table1.append(row); 
		row_count++; 
 	}else{
 		alert("可增加检索栏数量值已最大！", 5, !1);
 	}	
}