<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>资源文件预览</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
  </head>
   
  <body>
   <center>
    <h2>资源文件预览</h2>
    ${sourceTitle } ——${fileName }
    <hr>
    
    <div >
    <c:choose>
		        <c:when test="${requestScope.sourceType =='document' && !empty requestScope.fileUrl}">
				    <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" 
					    codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,18,0"  
					    width="715" height="545" id="test" align="middle"> 
						<param name="allowFullScreen" value="true" /> 
						<param name="bgcolor" value="#333333" /> 
						<param name="movie" value="${requestScope.fileAddress }${requestScope.fileUrl}" /> 
						<embed src="${requestScope.fileAddress }${requestScope.fileUrl}" allowFullScreen="true" bgcolor="#333333"
						    width="715" height="545"
						    name="test" align="middle" type="application/x-shockwave-flash"  
						    pluginspage="http://www.macromedia.com/go/getflashplayer" />
					</object>
		        </c:when>
		        
		        <c:when test="${requestScope.sourceType=='video'&&!empty requestScope.fileUrl}">
					<object type="application/x-shockwave-flash" data="<%=basePath %>/flashview/player/vcastr3.swf" width="715" height="545" id="vcastr3">
					<param name="movie" value="<%=basePath %>/flashview/player/vcastr3.swf"/> 
					<param name="allowFullScreen" value="true" />
					<param name="FlashVars" value="xml=
						<vcastr>
							<channel>
								<item>
									<source>${requestScope.fileAddress }${requestScope.fileUrl}</source>
									<duration></duration>
									<title></title>
								</item>
							</channel>
							<config>
							</config>
							<plugIns>
								<logoPlugIn>
								<url><%=basePath %>/flashview/player/logoPlugIn.swf</url>
								<logoText></logoText>
								<logoTextAlpha>0.75</logoTextAlpha>
								<logoTextFontSize>30</logoTextFontSize>
								<logoTextLink><%=basePath %></logoTextLink>
								<logoTextColor>0xffffff</logoTextColor>
								<textMargin>20 20 auto auto</textMargin>
								</logoPlugIn>
							</plugIns>
						</vcastr>"/>
					</object>
		        </c:when>
		        
		        <c:when test="${requestScope.sourceType=='photo'&&!empty requestScope.fileUrl}">
				   	<img src="${requestScope.fileAddress }${requestScope.fileUrl}"/>
		        </c:when>
		        
		        <c:when test="${requestScope.sourceType=='audio'&&!empty requestScope.fileUrl}">
		        	<div style="width:200px;margin-top:30px;height:30px">
				   		<object id="dewplayer_content" data="<%=basePath %>/flashview/player/dewplayer.swf" width="200" height="20"
							name="dewplayer" id="dewplayer"
							type="application/x-shockwave-flash">
							<param name="movie" value="<%=basePath %>/flashview/player/dewplayer.swf" />
							<param name="flashvars" value="mp3=${requestScope.fileAddress }${requestScope.fileUrl}" />
							<param name="wmode" value="transparent" />
						</object>
						<script type="text/javascript" src="<%=basePath %>/js/swfobject.js"></script>
						<script type="text/javascript">
							var flashvars = {
							  mp3: "${requestScope.fileAddress }${requestScope.fileUrl}"
							};
							var params = {
							  wmode: "transparent"
							};
							var attributes = {
							  id: "dewplayer"
							};
							swfobject.embedSWF("<%=basePath %>/flashview/player/dewplayer.swf", "dewplayer_content", "200", "20", "9.0.0", false, flashvars, params, attributes);
						</script>
					</div>
		        </c:when>
		        <c:otherwise>
		        </c:otherwise>
	        </c:choose>
		</div>
	</center>	
  </body>
</html>
