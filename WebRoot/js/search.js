
//展开更多分类
function seeMore(obj){
	$(obj).addClass('hid').siblings('span').removeClass('hid');
	var cur = $(obj).parent().next().children('ul');
	if(cur.find('li').hasClass('hid')){
		cur.find('li.hid').removeClass('hid').addClass('show');
	}else{
		cur.find('li.show').removeClass('show').addClass('hid');
	}	
}

//多选
function checkcombox(obj){
	var cur = $(obj).parent().next().children('ul');
	if(cur.find('li').children().eq(0).hasClass('hide')){
		cur.find('li').children("input").removeClass('hide').addClass('show');
		$(obj).hide();
		$(obj).next().show();
		$(obj).next().next().show();
		$(obj).next().next().next().show();
	}
}

//取消多选
function donecheckbox(cls,obj){
	var cur = $(obj).parent().next().children('ul');
	cur.find('li').children("input").removeClass('show').addClass('hide');
	$(obj).hide();
	$(obj).prev().hide();
	//$(obj).prev().prev().hide().css({"width":"35px"}).text("全选");;
	$(obj).prev().prev().show();
	$("."+cls).each(function(){
		 this.checked = false;
	});
}

//全选
function checkall(cls,obj){
	//获取已经勾选的数量
	var a = 0;
	$("."+cls).each(function(){
		if(this.checked){
			a++;
		}
	});
	//判断勾选的数量和复选框数量是否相同
	//相同则取消全选,不相同则全部勾选
	if($("."+cls).length!=a && $(obj).text()=="全选"){
		 $("."+cls).each(function(){
		     this.checked = true;
		 });  
		 $(obj).css({"width":"55px"}).text("取消全选");
	}else{
		 $("."+cls).each(function(){
		     this.checked = false;
		 }); 
		 $(obj).css({"width":"35px"}).text("全选");
	}
}


//获取值
function GetcomboxValues(cls,coulmn){
	var text="";  
	var i = $("[class="+cls+"]:checked").length;
      $("."+cls).each(function(){    		
          if($(this).attr("checked")==true){  
             text += $(this).val()+"#";  
          }  
     });
     if(text==""){
     	alert("请选择一个");
     	return;
    }
    $("#"+coulmn).val(text);
    //ChangeValue(text,coulmn,cls);
    document.getElementById('submitForm').submit();
}

//检索传参数跳转
function keyJump(key){
	$("#key").val(key);
	document.getElementById('headSearchForm').submit();
}

//分类类型跳转
function typeJump(coulmn,val){
	document.getElementById(coulmn).value=val;
	$("#key").val("");
	document.getElementById('headSearchForm').submit();
}

//自定义分类点击跳转
function groupJump(coulmn,val,id){
	var coulmns = $("#assigncoulmn").val();
	 var va = $("#"+id).val();
	 val = val.substring(0,val.indexOf("【"));
	 if(va!="" && va!=null){
		if(va.indexOf(val)==-1){
			var reg=/,$/gi; 
			va=va.replace(reg,"");
			$("#"+id).val(va+","+val);  
		 }
    }else{
    	$("#"+id).val(val);  	
    }
	 document.getElementById('submitForm').submit();
}
//自定义分类父级复选框跳转
function groupJumpChange(obj,coulmn,val,id,cls){
	//最多能勾选5个父节点
	var i = $("[class="+cls+"]:checked").length;
	if(i>3){
		alert("最多能勾选3个");
		return ;
	}
	//获取以 id_名字格式的值
	val = val.substring(0,val.indexOf("【"));
	CheckboxChange(null,obj,val,coulmn,cls,id);
	
}

function navJump(obj,coulmn,val,cls){
    var va = $("#"+coulmn).val();
    $("#"+coulmn).val("");
    var i = $("[class="+cls+"]:checked").length;
	if(i>2){
		alert("最多只能勾选3个");
		$(obj).attr("checked",false);
		return;
	}
    ChangeValue(obj,val,coulmn,cls);
	document.getElementById('submitForm').submit();
}

//复选框改变
function CheckboxChange(endval,obj,val,coulmn,cls,id){
	//获取当前字段的值
	//获取当前复选框的长度
	var i = $("[class="+cls+"]:checked").length;
	if(i>3){
		alert("最多只能勾选3个");
		$(obj).attr("checked",false);
		return;
	}
	var va ="";
	if(id!= undefined){
	  va = $("#"+id).val();
	}else{
	  va = $("#"+coulmn).val();
	}
	//获取当前复选框状态
	var istrue =$(obj).attr("checked");
	//将当前字段以","分割为数组
	var text=va.split(","); 
	var values = "";
	//将组合条件字段以","分割为数组
	var coulmns = $("#assigncoulmn").val().split(",");
	var cou = "";
	//如果当前复选框状态为取消勾选
	if(!istrue){
		//先将当前字段值清空
		if(id != undefined){
	  	 $("#"+id).val("");
		}else{
	 	 $("#"+coulmn).val("");
		}
		//通过循环将除了当前值的值计算出来
	    for(var i = 0;i<text.length;i++){
	    	if(text[i]!="" && text[i]!=val){
	    		values+=text[i]+",";
	    	}
	    }
	  	 i = $("[class="+cls+"]:checked").length;
		//判断当前复选框是否为0 
		if(i==0){
			var c = "";
			//通过循环将当前字段从组合条件中去除
			for(var i = 0 ;i<coulmns.length;i++){
				if(coulmns[i]!="" && coulmns[i]!=coulmn ){
		    		c+=coulmns[i]+",";
		    	}
			}		
			//判断当前字段是还需要存在	
			if(c!=""){
				//当前id是否为自定义分类的父类id
				if(id=="rdtyId" || id=="rdtyId1" || id=="rdtyId2"){
					//再判断当前类型值是否为空
					if($("#"+coulmn).val()==""){
						$("#assigncoulmn").val(c);
					}
				}else{
					$("#assigncoulmn").val(c);
				}
			}else if($("#"+coulmn).val()=="" && c==""){				
				$("#assigncoulmn").val("");
			}
			document.getElementById('submitForm').submit();
			return ;
		}
	}
	//重新给字段赋值
	if(values!=""){
		val = values;
	}
	//通过通用方法进行字段赋值,组合条件字段计算
	if(id != undefined){
		ChangeValue(istrue,val,coulmn,cls,id);
	}else{
		ChangeValue(istrue,val,coulmn,cls);
	}
	document.getElementById('submitForm').submit();
}

//通用方法(动态修改组合条件)
function ChangeValue(ischeck,val,coulmn,cls,id){
	//获取当前字段的值	
	var va ="";
	if(id != undefined){
	 va = $("#"+id).val();
	}else{
 	 va = $("#"+coulmn).val();
 	}
 	//如果当前字段存在值并且分类字段同事存在值
	if($("#"+coulmn).val()!=""){
		//判断当前值是否重复添加	
		if(va.indexOf(val)==-1){
			//去掉最后一个逗号","
			var reg=/,$/gi; 
			va=va.replace(reg,"");
			//将当前字段值存入
			if(id != undefined){
				$("#"+id).val(va+","+val);		
			}else{
		 		$("#"+coulmn).val(va+","+val);  
		 	}	
    	}
    	//如果当前字段不存在值并且组合条件存在值
	}else if($("#"+coulmn).val()=="" ){
		//给当前字段赋值
		if(id != undefined){;
			$("#"+id).val(val);
		}else{
	 		$("#"+coulmn).val(val);
	 	}
	}else{
		if(id != undefined){
			$("#"+id).val(val);
		}else{
	 		$("#"+coulmn).val(val);
	 	}
	}
}

//分页跳转
function pageJump(page){
	document.getElementById('page').value = page;
	document.getElementById('submitForm').submit();
}

//搜索框
function clearVal(obj){
	if($(obj).val()=="全文检索"){
		$(obj).val("");
	}else if($(obj).val()==""){
		$(obj).val("全文检索");
	}
}
//搜索前检查
function SerachAll(){
	var key = $("#key").val();
	if(key=="全文检索" || key.trim()==""){
		alert("请输入关键词");
		return;
	}
	if(key.length>5){
		alert("关键词长度为5");
		$("#key").val("");
		return;
	}
	$("#keyword").val(key);
	document.getElementById('headSearchForm').submit();
}
//移动图片上方透明
function addopa(obj){
	$(obj).css({"opacity":"0.8"});
}
//移开恢复透明
function downpa(obj){
	$(obj).css({"opacity":"1"});
}
//点击主页特色库进行查询
function RmstSerach(keyword,jumpType){
	$("#keyword").val(keyword);
	$("#jumpType").val(jumpType);
	document.getElementById('headSearchForm').submit();
}

//二级页面通过关键词检索
function serachkey(){
  var key = $("#serach_top_input").val();
  if(key==""){
  	alert("请输入关键词");
  	return;
  }
  clearSerach();
  clerarInfo();
  $("#keyword").val(key);
  //window.location.href="http://localhost:8080/RISS2D/search.htm?keyword="+key;
  $("#submitForm").submit();
}
//回车事件
function keydownserach(e){
  if(!e){
    e = window.event;
   }
   if((e.keyCode || e.which) == 13){
   		serachkey();
   }
}
function indexserach(e){
  if(!e){
    e = window.event;
   }
   if((e.keyCode || e.which) == 13){
   		var key = $("#key").val();
		if(key==""){
			  alert("请输入关键词");
			  return;
		}
	  $("#keyword").val(key);
	  $("#headSearchForm").submit();
   }
}
//清空高级搜索数据
function clearSerach(){
  $("#action").val("");
  $("#jumpType").val("");
  $("#assigncoulmn").val("");
}
//回车绑定
function endsearchkey(e){
  if(!e){
    e = window.event;
   }
   if((e.keyCode || e.which) == 13){
   		endsearch();
   }	
}
//在结果中检索
function endsearch(){
	//clerarInfo();
	var k = $("#endsearch").val();
	var s = $("#endsearchs").val();
	if(k.length>10){
		alert("关键词长度不得大于10");
		return;
	}
	if(s==""){
		$("#endsearchs").val(k+",");
	}else{
		if(s.split(",").length>3){
			alert("最多在结果集中检索3次");
			$("#endsearch").val("");
			return;
		}
		$("#endsearchs").val(s+k+",");
	}
	$("#submitForm").submit();
	
}
function removekeys(key,coulmn){
	$("#"+coulmn).val("");
	$("#submitForm").submit();
}
function removekey(key){
	var e = $("#endsearchs").val();
	var c = ""
	var s = e.split(",");
	for(var i =0;i<s.length;i++){
		if(key!=s[i] && s[i]!=""){
			c +=s[i]+",";
		}
	}
	$("#endsearchs").val(c);
	//clerarInfo();
	$("#submitForm").submit();
}
//收藏信息
function keepinfo(uid,id){
	if(uid==""){
		showDiv('login','login');
		return;
	}
	//通过调用方法进行收藏
	riss2.favorites.addFavorite(path+"/user/favorite.htm?action=addFavorite", uid, id);
}

//清空分类信息
function clerarInfo(){
	$("#rdtyId").val("");
	$("#rdtyId1").val("");
	$("#rdtyId2").val("");
	$("#datasource").val("");
	$("#resourceType").val("");
	$("#creator").val("");
	$("#publisher").val("");
	$("#subjecttype").val("");
	$("#date").val("");
	$("#assigncoulmn").val("");
}
//指定字段查询
function appointQuerys(filed,obj){
	$("#"+filed).val($(obj).text());
	$("#submitForm").submit();
}
//指定字段查询
function appointQuery(filed,val){
	$("#"+filed).val(val);
	$("#submitForm").submit();
}
