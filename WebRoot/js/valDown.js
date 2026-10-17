 //当点击下载按钮之后,验证用户的下载量是否达到系统设置的下载次数上限.
 function validSuinDownNum(){
 		var resId = document.getElementById("resId").value;
		$.ajax({
		   type: "POST",
		   url: path+"/platform/search/validCount.htm", 
		   data: "resId="+resId,
		   dataType:'json', 
		   success: function(data){
				if(data.result){
					document.getElementById('durl').click();
				}else{
					alert(data.msg);
				}
		   }
		});
} 