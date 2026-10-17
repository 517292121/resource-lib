//初始化检索历史
$(document).ready(function(){
	$.ajax({  
		// 跳转到 action
		url: path + '/list.htm?action=getSearchHistory',
	    success:function(data) { 
	         var keyArr = data.split(",");
			//二级页面-样式单独处理
	        var nowHref = window.location.href; 
			var action = nowHref.substring(nowHref.lastIndexOf("/")+1,nowHref.indexOf(".htm"));
			if(action == 'list'){
				if(keyArr != ''){
					html = '<p class="leftmenulih">检索历史</p><div class="history-div">';
					var key = '';
					for(i=0; i<keyArr.length; i++){
						key = keyArr[i];
						if(key.length > 5){
							key = key.substring(0,5)+"...";
						}
			         	 html += "<a href='" + path + "/list.htm?key=" + encodeURI(keyArr[i]) + "' title='"+keyArr[i]+"'>" + key + "</a>";
			        }
			        html +="</div>";
		        }
	         	document.getElementById("listhotkey").innerHTML = html;
			}
	        
	    }, 
	    error : function() {
	         alert("异常！");  
	    }
	});   
});
