$(document).ready(function(){
	setHeight();
});

function setHeight(){
	var isIE=!!window.ActiveXObject; 
	var isIE6=isIE&&!window.XMLHttpRequest;
	
	//前台信息页的左右对齐
	try{
			var leftHei = document.getElementById("pageLeft").offsetHeight;
			var rightHei = document.getElementById("pageRight").offsetHeight;
			if(leftHei > rightHei){
				if(isIE6){
					document.getElementById("pageRight").style.height = leftHei + 'px';
				}else{
					$("#pageRight").css({"min-height": leftHei + 'px' , "height": leftHei + 'px'});
					$("#pageRight").addClass('autoHei');
				}
			}else{
				if(isIE6){
					document.getElementById("pageLeft").style.height = rightHei + 'px';
				}else{
					$("#pageLeft").css({"min-height": rightHei + 'px' , "height": rightHei + 'px'});
					$("#pageLeft").addClass('autoHei');
				}
			}
			
	}catch(e){
		alert(e.message);
	}	
}
