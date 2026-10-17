//初始化热门检索词
$(document).ready(function(){
	/** $.ajax({  
		// 跳转到 action
		url: path + '/list.htm?action=getHotSearchKey',
	    success:function(data) { 
	         var keyArr = data.split(",");
	         var html = "<b>热门检索：</b>";
	         for(i=0; i<keyArr.length; i++){
	         	 html += "<a href='" + path + "/search.htm?keyword=" + encodeURI(keyArr[i]) + "'>" + keyArr[i] + "</a> &nbsp; ";
	         }
	         document.getElementById("hotkey").innerHTML = html;
	    },  
	    error : function() {
	         alert("异常！");  
	    }
	}); */    
});
