 	
var clickColor = "#DFE8F6";
var moveColor = "#ECECEC";
var outColor = "#FAFAFA"; 
	
 	// JScript 文件
    //设置GridView行的颜色
function tableBg(){
     try{   
           var trs = document.getElementById("Page_data").getElementsByTagName('tr');
	       for(var i=0; i<trs.length; i++){
	       	   if((i+2) % 2 == 1){
	       	   	   trs[i].style.background = "#F2F2F2";
	       	   }
	                     
           }
     }catch(e){
        
     }
}

