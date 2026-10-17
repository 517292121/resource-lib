<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.CommonUtil"%>
<%
	String path = request.getContextPath();
	boolean isPhone = CommonUtil.judgeIsPhone(request);
	if(isPhone){
		session.setAttribute("CLIENTTYPE", "WAP");
	}else{
		session.setAttribute("CLIENTTYPE", "PC");
	}
%>
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">  
	<script type="text/javascript">
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
		
		function tomain(){
			var pc = IsPC();
			if(pc){
				window.location.href = "<%=path %>/index";
			}else{
				window.location.href = "<%=path %>/mobile";
			}
		}
		
	</script>  
  </head>
  
  <body onload="tomain();">
  </body>
</html>