
var jswork = {
    tableid: "data_table",
    loginname_regex: /^[0-9a-z][\w-.]*[0-9a-z]$/i,  //用户名应该由不区分大小写“a-z 0-9 ._-”字符组成！"
    numeric_regex: /^d+(.d+)?$/,  //验证数据正则表达式     
    getCheckboxValue:function(item){
         if(item.length == undefined){
	          if(item.checked == true){
	              return item.value;
	          }else{
	              return "";
	          }
	      }
	      var result = "";
	      for(i=0; i<item.length; i++){
	         if(item[i].checked==true){
	             result += item[i].value+",";
	         }
	      }
	      if(result.length>1){
	          result = result.substring(0,result.length-1);
	      }
	      return result;
    }
}
//获取radio元素所选中的值
jswork.getRadioValue = function(item){
	  var result = "";
      for(i=0; i<item.length; i++){
         if(item[i].checked==true){
             result += item[i].value;
             break;
         }
      }
      return result;
}

//checkbox反选
jswork.reverseSelect = function(item){
      for(var i=0;i<item.length;i++){
          item[i].checked= item[i].checked ? false : true;
      }
}

//checkbox全选
jswork.selectAll = function(item){
    for(var i=0;i<item.length;i++){
       item[i].checked= true;
    }
    if(!item.length){
       item.checked = true;
    }
}

//checkbox全不选
jswork.unSelectAll = function(item){
    for(var i=0;i<item.length;i++){
       item[i].checked= false;
    }
    if(!item.length){
       item.checked = false;
    }
}

//自动全选和反选
jswork.switchSelect = function(ckItem, selfItem){
    if(selfItem.checked == true){
        jswork.selectAll(ckItem);
    }else{
        jswork.unSelectAll(ckItem);
    }
}

//根据ID数据初始化复选框
jswork.InitCheckboxByArr = function(valueArr,item){
      for(i=0; i<item.length; i++){
         for(j=0; j<valueArr.length; j++){
             if(item[i].value == valueArr[j]){
                 item[i].checked = true;
                 break;
             }
         }
      }    
}

//根据以','分割的ID字符串数据初始化复选框
jswork.InitCheckboxByStr = function(str,item){
      strlist = str.split(",");
      for(i=0; i<item.length; i++){
         for(j=0; j<strlist.length; j++){
             if(item[i].value == strlist[j]){
                 item[i].checked = true;
                 break;
             }
         }
      }    
}






//////////////////////////////////////////////////////////表单验证
//验证会员名格式
jswork.check_loginname = function(item){
    var itemname = item.value;
    return jswork.loginname_regex.test(itemname);
}

//验证是否为数字
jswork.check_numeric = function(data_content){
      var pattern = /^([0-9]*|[0-9]{1}\d*\.\d{1}?\d*)$/;
      if(pattern.test(data_content))
      {
         return true;
      }
      return false;
}

jswork.check_mail = function(mail){
    var mail_regex = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    return mail_regex.test(mail);
}

//只能输入数字
//用法 <input name="old" type="text" ONKEYPRESS="event.returnValue=IsDigit();">
function IsDigit() {
   return ((event.keyCode >= 48) && (event.keyCode <= 57));
}

//图片自适应
function DrawImage(ImgD,kw,kh)
{
 var image=new Image();
 image.src=ImgD.src;

 
if(image.height<image.width)//说明宽》高＝＝》以宽为标准
 {
    if(image.width>kw)
    {
   		ImgD.width=kw;
   		ImgD.height=(image.height*kw)/image.width;
    }else{
   		ImgD.width=image.width; 
   		ImgD.height=image.height;
  	}
 }
 else//以高为标准
 {
    if(image.height>kh){
	   ImgD.height=kh;
	   ImgD.width=(image.width*kh)/image.height;
    }else{
	   ImgD.width=image.width; 
	   ImgD.height=image.height;
  	}
 }
 
}

//图片自适应
function setImageSize(img, w, h){
	var imgWidth = img.width;
	var imgHeight = img.height; 
	if((imgWidth/imgHeight) > (w/h)){
	    img.width = w;
	}else{
		img.height = h;
	}
}


