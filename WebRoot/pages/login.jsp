<%@ page language="java" pageEncoding="utf-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html >
<html >
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1,maximum-scale=1, user-scalable=no">
<title>敦煌研究数据库后台管理登录</title>
<link href="<%=basePath %>pages/css/bootstrap.min.css" title="" rel="stylesheet" />
<link title="orange" href="<%=basePath %>pages/css/login.css" rel="stylesheet" type="text/css"/>


<script>if( self != top ){ window.top.location.href ="<c:url value='/pages/login.jsp'/>";}</script>
<script type="text/javascript" src="<c:url value="/viewsjs/common/sha1.js"/>"></script>
<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
<script type="text/javascript">
    function onSubmit(){
        if(document.getElementById("userName").value.trim() == '') {
        	layer.msg("请输入登录名");
    		return false;
    	}
    	if(document.getElementById("pwd1").value.trim() == '') {
        	layer.msg("请输入登录密码");
    		return false;
    	}
       	var form1 = document.getElementById('form1')
        var password = form1.pwd1.value;
        form1.pwd.value = hex_sha1(password);
	    form1.pwd1.remove();
        //form1.pwd.value = password;

        layer.load(4, {
		  shade: [0.1,'#fff'] //0.1透明度的白色背景
		});
        return true;
    }

    function _onfocus(){
    	document.getElementById('userName').focus();
    }
</script>
</head>
<body onload="_onfocus();">
  <div class="login-main">
		<h1>敦煌研究数据库</h1>
		<div class="login-content">
			<form name="form1" id="form1" method="post" action="<c:url value='/login.htm?action=consoleLogin'/>" onsubmit="javascript: return onSubmit();">
			<input type="hidden" value="" name="pwd"/>
			<div class="form-group user-name">
				<input type="text" name="userName" id="userName" placeholder="这里输入登录名" class="input-list" value="${loginName }" />

			</div>
			<div class="form-group pwd-text">
				<input type="password" name="pwd1" id="pwd1" placeholder="这里输入登录密码" class="input-list" value="" />
			</div>
			<div class="form-group">
				<input type="submit" class="btn" value="登 &nbsp; 录" />
			</div>
			</form>
			<p class="error">${error }</p>
		</div>
		   <span style="color: #acb3b5">@版权所有：西藏大学版权所有</span>
		</p>
  </div>
</body>
</html>

