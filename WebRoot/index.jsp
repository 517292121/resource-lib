<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	// 暂时关闭设备识别，统一进入 PC 版本。
	/*
	boolean isPhone = cn.com.adtech.riss2.core.CommonUtil.judgeIsPhone(request);
	if(isPhone){
		session.setAttribute("CLIENTTYPE", "WAP");
	}else{
		session.setAttribute("CLIENTTYPE", "PC");
	}
	*/
	session.setAttribute("CLIENTTYPE", "PC");
%>
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">  
	<script type="text/javascript">
		/* 暂时停用浏览器设备识别。
		function IsPC() {
		    var userAgentInfo = navigator.userAgent;
		    var Agents = ["Android", "iPhone", "SymbianOS", "Windows Phone", "iPad", "iPod"];
		    var flag = true;
		    for (var v = 0; v < Agents.length; v++) {
		        if (userAgentInfo.indexOf(Agents[v]) > 0) {
		            flag = false;
		            break;
		        }
		    }
		    return flag;
		}
		*/
		
		function tomain(){
			/*
			var pc = IsPC();
			if(pc){
				window.location.href = "<%=path %>/index";
			}else{
				window.location.href = "<%=path %>/mobile";
			}
			*/
			window.location.href = "<%=path %>/index";
		}
		
	</script>  
  </head>
  
  <body onload="tomain();">
  </body>
</html>
