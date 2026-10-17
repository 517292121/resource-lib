<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>资源增长情况统计</title>
		<script language="JavaScript" src="<%=path%>/flashview/js/FusionCharts.js"></script>
		<link rel="stylesheet" href="<%=path%>/css/form.css" type="text/css"></link>
	</head>

	<body>
	   <div class="tool">
	      <ul class="nav" style="float: right;">
		    <li><a href="<%=path %>/console/count/resourceAddInfo.htm?action=initPage"><img src="<%=path %>/images/common/common_back.png"></img>返回</a></li>
		  </ul>  
	   </div>
	   
	   
		<table width="98%" border="0" cellspacing="0" cellpadding="3"
			align="center">
			<tr>
				<td valign="top" class="text" align="center">
					<div id="chartdiv" align="center">
					</div>
					
					<script type="text/javascript">
		  		   var chart = new FusionCharts("<%=path%>/flashview/player/MSLine.swf", "ChartId", "800", "400", "0", "0");
				   chart.setDataURL("<%=path%>/console/count/resourceAddInfo.htm?action=getXmlData&beginYear=${param.beginYear}&endYear=${param.endYear}&rmstId=${param.rmstId}");
				   //chart.setDataURL("<%=path%>/flashview/XMLData/MSLine.xml");		   
				   chart.render("chartdiv");
		   
		</script>
				</td>
			</tr>
			<tr>
				<td valign="top" class="text" align="center">
					&nbsp;
				</td>
			</tr>
			<tr>
			</tr>
		</table>
	</body>
</html>


