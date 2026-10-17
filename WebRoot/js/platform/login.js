function onLogin(src){
		$("#msg").html("");
		var loginName = $("#loginname").val();
		if(!loginName || loginName.trim().length < 1) {
			layer.msg("请输入登录名！");
			return false;
		}
		var password = $("#loginpwd").val();
		if(!password || password.trim().length < 1) {
			layer.msg("请输入登录密码！");
			return false;
		}
		//密码加密
		password = hex_sha1(password);
		
		$.ajax({
		    type: 'GET',
		    url: basePath + 'member/memberLogin.htm?t=' + new Date().getTime(),
		    data : {
		    	name : loginName,
		    	pwd : password
		    },
		    cache:false,
		    success: function(data){
			   	try{
			    	var val = eval("("+data+")");
		    		if(val.isSuccess == true || val.isSuccess == "true"){
						//登录成功，现在的操作是刷新当前父页面，如有需求也可以直接跳转到用户中心
						if(src == 'wap'){
							window.location.href = srcfrom;
						}else{
			  				window.parent.location.reload();
						}
		    		}else{
		    			layer.msg(val.msg);
		    		}
			   	}catch(e){
			   		layer.msg("错误：" + e.message, {icon:2});
			   	}
		    }
		});
}
	
$(function(){
	$("#loginform").keydown(function(e){
		var e = e || event,
		keycode = e.which || e.keyCode;
		if (keycode==13) {
			$("#anniu").trigger("click");
		}
	});
})










