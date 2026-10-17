  //////////////////////针对可编辑表格的文本框操作
   function onInt(ele,title){
       ele.style.border = "1px solid #333333";
       //ele.value = ele.value.replace("(" + title + ")","");
       ele.focus();
   }

   function onOut(item,colname,id,title,tablename,idcolumn){
        
        item.style.border = "0px solid #ddd";
        item.blur();
        var now_data = item.value;
        var old_data = item.orinData;
        
        if(now_data!=old_data){ 
            var result = updateValue(now_data,id,colname); 
            if(result){
               item.orinData = now_data;
            } else {
               alert(poput_failure_to_modify);
               item.value = old_data;
            }
        }
       // item.value += "(" + title + ")";
    }
    
	/**
      *  showtime: 显示时间：如果为空则一直显示
      *  root: 网站根路径，一般为<%=path %>即可
      *  msgTitle: 提示信息：如果为空则默认为'正在提交您的请求，请稍后...'
      */
     function showWaitMessage(root,msgTitle){
         if(root == null){
             root = "";
         }
         if(msgTitle == null){
            msgTitle = poput_being_submitted;
         }
         var y = document.body.scrollTop;
         var pageHiegh = document.body.clientHeight / 2;
         var messageBoxDiv;
         if(document.getElementById("WaitMessageBox") == null){
         	 messageBoxDiv = document.createElement('div');
	         messageBoxDiv.id = 'WaitMessageBox';
	         messageBoxDiv.className = 'WaitMessage';
         }else{
         	 messageBoxDiv = document.getElementById("WaitMessageBox");
         }
         showDisplay();
         messageBoxDiv.style.top = y + pageHiegh-25;
         messageBoxDiv.innerHTML = "<br>";
         messageBoxDiv.innerHTML += "<img src='"+root+"images/message/loading.gif' style='vertical-align:middle;'></img>";
         messageBoxDiv.innerHTML += "<b> &nbsp;"+msgTitle+"</b>";
         messageBoxDiv.style.display="block";
         if(document.getElementById("WaitMessageBox") == null){
         	document.body.appendChild(messageBoxDiv);
         }	
	}
	
	function showSuccessMessage(root, showtime, msgTitle){
         showMsg(msgTitle, showtime);
	}
	
	/**自动消失提示框**/
	function showMsg(msgTitle, showtime){
         if(msgTitle == null){
            msgTitle = poput_submission_of_success;
         }
         if(showtime == null){
             showtime = 2;
         }
         var y = document.body.scrollTop;
         var pageHiegh = document.body.clientHeight / 2;
         var messageBoxDiv = null;
         if(document.getElementById("SuccessMessageBox") == null){
//         	messageBoxDiv = document.createElement("<div id='SuccessMessageBox' class='SuccessMessageBox'></div>"); //创建提示框区域
         	messageBoxDiv = document.createElement('div');
         	messageBoxDiv.id = 'SuccessMessageBox';
         	messageBoxDiv.className = 'SuccessMessageBox';
         }else{
            messageBoxDiv = document.getElementById("SuccessMessageBox");
         }
         messageBoxDiv.style.top = y + pageHiegh-25;
         messageBoxDiv.style.display="block";
         messageBoxDiv.innerHTML = "<div class='successMessage'><b>"+msgTitle+"</b></div>";
         document.body.appendChild(messageBoxDiv);
         setTimeout(hiddenmsg,showtime*1000); 
	}
	
	//隐藏提示框     
	function hiddenmsg(){
	    var md=document.getElementById("SuccessMessageBox");
	    md.style.display = "none";
	    document.body.removeChild(md); //删除提示框区域
	}
	
	//根据浏览器的大小自动改变屏蔽层的大小
	window.onresize = function() {
			 var displayObj = document.getElementById("displayDivId"); 
			 if(displayObj!=null){
				 var width = document.body.clientWidth+document.body.scrollLeft+document.body.scrollWidth;
				 var height = document.body.clientHeight+document.body.scrollTop+document.body.scrollHeight;  
			     displayObj.style.width=width+"px";
				 displayObj.style.height=height+"px";
			 }
	}
	
	/**
	 * 屏蔽层显示	
	 **/
	 function showDisplay(){
		 var width = document.body.clientWidth+document.body.scrollLeft;
		 var height = document.body.clientHeight+document.body.scrollTop;  
	 	var displayObj = document.createElement("div");
	 	displayObj.id="displayDivId";
	 	displayObj.className="displayDiv";
 		displayObj.style.width=width+"px";
 		displayObj.style.height=height+"px";
	 	document.body.appendChild(displayObj); 
	 }
	 
	 //关闭遮罩层
	 function closeDisplay(){
	 		 var displayDiv=document.getElementById("displayDivId");
		    if(displayDiv != null){
			    document.body.removeChild(displayDiv); //删除屏蔽层
		    }
	 }
	 
	//将输入框中的“\”转换为“/”	
	function urlpath(obj){
	    var urlVal = $(obj).val();
	    if (urlVal) {
	        urlVal = urlVal.replace(/\\/g, "/");
	        $(obj).val(urlVal)
	    }
	}
	
	
	//封装layer数据提交时带遮罩的加载层，可用于表单提交
	function loading(){
		var ld = layer.msg('数据提交中..', {
			icon: 16,
			shade: [0.5, '#000'],
			scrollbar: false,
			time:3600000
		}) ;
		return ld;
	}