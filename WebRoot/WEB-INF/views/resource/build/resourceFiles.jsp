<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%> 
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%@include file="/WEB-INF/views/common/extInclude.jsp" %>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
	<style type="text/css">
		*{font-size: 12px;}
		td{padding: 5px; height: 30px;}
		/**自动省略超出区域的内容**/
		.subText {overflow: hidden;WHITE-SPACE: nowrap;TEXT-OVERFLOW: ellipsis;position:relative; height: 35px;}
	</style>
	<script type="text/javascript" src="<%=path %>/js/jquery-1.4.2.js"></script>
	<input type="hidden" name="store" value="" /> 
	<input type="hidden" name="rmin_id" value="" />
	<script type="text/javascript">
		var basePath = "<%=path %>";  
		var resSFileId = ${resId };
		var url_save_form = "<%=path %>/console/data/inputSingle.htm?action=saveResFile";
		var storeId = ${storeId };
		var rminid = ${rminid }; 
	</script>
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/build/ResourceFileEdit.js"></script>
  </head>
  
  <body>
  	 <!-- 附件文件 --> 
  	 <div class="tool">
          <ul class="nav">
             <li><a href="javascript:addResFile()"><img src="<%=path %>/images/common/common_add.gif"></img>新增</a></li>
             <li><a href="javascript:refresh()"><img src="<%=path %>/images/refresh.png"></img>刷新</a></li>
          	 <!-- <li><a href="javascript:goBack();"><img src="<%=path %>/images/common/common_back.png"></img>返回</a></li> -->
          	 <li>
          	 	<c:choose>
          	 		<c:when test="${pageSource == 'managerList' }">
          	 			<a href="<%=path %>/console/data/resource/SourceManager.htm?action=managerResourceList&searchtitleshow=${key }&pagecount=${beginRow }&rmstId=${rmst_id }&rltyId=${rlty_id }">
          	 		</c:when>
          	 		<c:otherwise>
		          	 	<a href="<%=path %>/console/data/inputSingle.htm?action=missionResourceList&searchtitle=${key }&pagecount=${beginRow }&rmin_id=${rminId }">
          	 		</c:otherwise>
          	 	</c:choose>
          	 	<img src="<%=path %>/images/common/common_back.png"></img>返回</a>
          	 </li>
          	 
          	 <li style="padding: 3px 0px 0px 20px;">“${rsdaTitle }” 的相关附件</li>
          </ul> 
	 </div>
     <table cellpadding="0" width="100%" border="0" cellspacing="1" id="file_table" bgcolor="#F0F1F3" >
     	<tr bgcolor="#F0F1F3" height="22" id="row0">
          <td width="60" align="center" style="padding-left: 0px;">显示序号</td>
          <td width="310px" class="th">文件</td>
          <td class="th">说明</td>
          <td class="th">仅限</td>
          <td class="th">操作</td>
        </tr>
        <c:forEach items="${list }" var="resfile">
          <tr height="25" bgcolor="#ffffff" id="tr${resfile.rsfiId }" onmouseout='mouseout(this)' onmouseover='mouseover(this);'> 
	          <td align="center" style="padding-left: 0px;" onclick="EditIndexOnclick(this,'${resfile.rsfiId }');">${resfile.resIndex }</td>
       		  <td align="left" width="200px;"> 
        		  ${resfile.resFileName }
	          </td>
	          <td width="400px;" class="subText" onclick="EditRemarkOnclick(this,'${resfile.rsfiId }');">${resfile.resFileRemark }</td>
	          <td width="60px;">
	          	<c:if test="${resfile.resFileAccessType == 'PUBLIC'}"><span style='color:green;'>公开</span></c:if>
	          	<c:if test="${resfile.resFileAccessType == 'PRIVATE'}"><span style='color:red;'>不公开</span></c:if>
	          </td>
              <td align="center" width="55px;"> 
       		   	  <a href="<%=path %>/console/data/inputSingle.htm?action=sourceFileView&sourceFileId=${resfile.rsfiId }" target="_blank">预览</a>
		          <a href="javascript:SaveEditDeleteResFile('${resfile.rsfiId }');">删除</a>
       		  </td>
       	   </tr>
        </c:forEach>
     </table>
  </body>
</html>
