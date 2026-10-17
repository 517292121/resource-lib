<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../WEB-INF/views/common/taglibInclude.jsp" %>
<%@ page language="java" import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
 	 <script type="text/javascript" src="<%=path %>/flashview/js/FusionCharts.js"></script>
 	 <style type="text/css">
 	 	* {
		    background-color: #fff;
		    color: #666;
 	 	}
 	 	table{
 	 		border-width: 1px;
		    border-style: solid;
		    border-color: #e6e6e6;
		    border-radius: 4px;
 	 	}
 	 	tr{
 	 		text-align: left;
 	 	}
 	 	th, td{
 		    position: relative;
		    padding: 9px 15px;
		    min-height: 20px;
		    line-height: 20px;
		    font-size: 14px;
	        border-color: #e6e6e6;
   		    border-width: 0px 0px 1px 0px;
 	 	}
 	 	table tr:hover td{
 		    background: #f4fbff;
    		transition: all 0.3s;
 	 	}
 	 </style>
 	 <script type="text/javascript">
 	  window.setTimeout(function(){
 		 window.location.reload(false);
 	  }, 5000)
 	 </script>
 </head>
  
  <body>
     <div class="form">
     	<table width="100%" cellpadding="0" cellspacing="0" id="formTable" border="1">
			<tr>
				<td width="20%" style="font-weight: bold;">属性</td>
				<td style="font-weight: bold;">值</td>
			</tr>
			<tr>
     			<td style="width:200px">服务器名：</td>
     			<td>
     				<%=request.getServerName() %> (<%=request.getRemoteAddr() %>)
    			</td>
     		</tr>
     		<tr>
     			<td>项目实际路径：</td>
     			<td><%=request.getRealPath("").replaceAll("\\\\", "/") %></td>
     		</tr>
     		<tr>
     			<td>资源存放根路径：</td>
     			<td><%=SysConfigInfo.FILE_ROOT_DIR %></td>
     		</tr>
     		<%
				float maxMemory=(float)Runtime.getRuntime().maxMemory();
				float fTotalMemory=(float)Runtime.getRuntime().totalMemory();
			  	float fFreeMemory= maxMemory - fTotalMemory + ((float)Runtime.getRuntime().freeMemory());
				float fPercent= fFreeMemory/maxMemory*100;
				request.setAttribute("fPercent", fPercent);
				request.setAttribute("maxMemory", maxMemory);
				request.setAttribute("fFreeMemory", fFreeMemory);
				request.setAttribute("fTotalMemory", fTotalMemory);
			 %>
     		<tr>
     			<td>jvm最大内存：</td>
     			<td><fmt:formatNumber value="${maxMemory/1024/1024 }" pattern="##" minFractionDigits="0"/>M</td>
     		</tr>
     		<tr>
     			<td>jvm占用内存：</td>
     			<td><fmt:formatNumber value="${fTotalMemory/1024/1024 }" pattern="##" minFractionDigits="0"/>M</td>
     		</tr>
     		<tr>
     			<td>jvm空闲内存：</td>
     			<td><fmt:formatNumber value="${fFreeMemory/1024/1024 }" pattern="##" minFractionDigits="0"/>M</td>
     		</tr>
     		<tr>
     			<td>jvm剩余内存率：</td>
     			<td>
     				<c:choose>
	                    <c:when test="${fPercent < 10}">
	                    	<span style="color:#F56C6C;"><fmt:formatNumber value="<%=fPercent %>" pattern="##.##" minFractionDigits="2"/></span>
	                    </c:when>
	                    <c:when test="${fPercent < 30}">
	                    	<span style="color:#E6A23C;"><fmt:formatNumber value="<%=fPercent %>" pattern="##.##" minFractionDigits="2"/></span>
	                    </c:when>
	                    <c:otherwise>
	                    	<span style="color:#67C23A;"><fmt:formatNumber value="<%=fPercent %>" pattern="##.##" minFractionDigits="2"/></span>
	                    </c:otherwise>
	                </c:choose>
	                %
	   			</td>
     		</tr>
     	</table>
     </div>
  </body>
</html>
