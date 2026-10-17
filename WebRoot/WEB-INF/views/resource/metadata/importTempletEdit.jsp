<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.core.TempletConfig"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String path = request.getContextPath();
	List<ResElementPool> elementList = (List<ResElementPool>)request.getAttribute("elementList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>导入模板设置</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
	<script type="text/javascript">var rootPath = "<%=path %>";</script>
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/metadata/importTemplet.js"></script>
  </head>
  
  <body>
    <div class="tool">
          <div style="float: left">
			特色库：【${requestScope.rmstName }】&nbsp;资源类型：【${requestScope.rltyName }】
          </div>
	      <ul class="nav" style="float: right;">
		    <li><a href="javascript:onAction();"><img src="<%=path %>/images/common/common_submit.png"></img>保存</a></li>
		    <li><a href="<%=path %>/console/data/templetManager.htm?action=SourceTypeList"><img src="<%=path %>/images/common/common_back.png"></img>返回</a></li>
		  </ul>  
    </div>
	
	<form action="<%=path %>/console/data/importTemplet.htm" name="elementform" method="post">   
		<input type="hidden" name="action" value="saveTemplet"></input>
		<input type="hidden" name="rmstId" value="${rmstId }"></input>
		<input type="hidden" name="rltyId" value="${rltyId }"></input>
		
	    <div class="title" style="margin-top: 0;height: 21px">元素设置</div>
	    <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
		 	<tr>
		 	   <th class="th" width="250px" align="right">元素&nbsp;</th>
		 	   <th class="th" align="left">&nbsp;Excel列名</th>
		 	</tr>
		    <%
		       int i=1;
		       String bgc = "";
		     %>
		     <c:forEach items="<%=elementList %>" var="v" varStatus="s">
		        <c:if test="${v.repoType < 3 }">
		        <% i++; 
		           if(i % 2 == 0){
		              bgc = "#FAFAFA";
		           }else{
		              bgc = "#ffffff";
		           }
		        %>
		         <!-- 循环核心元素 -->
		         <tr height="25px;" bgcolor="<%=bgc %>">
		             <td width="250px" align="right">
		               <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
		               <strong> ${v.repoTitle }(${v.repoName })：</strong> 
		             </td>
		             <td>
		             	 <input name="${v.repoId }" ONKEYPRESS="event.returnValue=isChar(this);" id="${v.repoId }" isElement="true" restrict="${v.repoRestrict }" title="${v.repoTitle }" class="text" style="margin-left: 2px;" value="${v.excelColumn }" maxlength="10">                                                                                                
		             	 <a id="${v.repoId }_msg" name="${v.repoId }_msg">
			             	 <c:if test="${v.repoRestrict == 'M' }">
				                <span style="color: red">*</span>
				             </c:if>
			             </a>
		             </td>
		         </tr>
		         
		         <!-- 将所有的元素上级元素再和核心相比，显示出它的所有子元素 -->
		         <c:forEach items="<%=elementList %>" var="vc" varStatus="sc">
		          	 <c:if test="${v.repoId == vc.repoParent }">
		          	 <!-- 循环子元素 -->
			         <tr height="25px;" bgcolor="<%=bgc %>">
			           <td width="250px" align="right"> 
			            &nbsp;${vc.repoTitle }(${vc.repoName })：
			           </td>
			           
			           <td><input name="${vc.repoId }" id="${vc.repoId }" ONKEYPRESS="event.returnValue=isChar(this);" isElement="true" restrict="${vc.repoRestrict }" title="${vc.repoTitle }" class="text" style="margin-left: 2px;" value="${vc.excelColumn }" maxlength="10">
			             <a id="${vc.repoId }_msg" name="${vc.repoId }_msg">
				             <c:if test="${vc.repoRestrict == 'M' }">
				                <span style="color: red">*</span>
				             </c:if>
			             </a>
			           </td>
			         </tr>
			         </c:if>
		         </c:forEach>
		       </c:if>
	       </c:forEach> 
	       </table>
	       
	       <div class="title" style="margin-top: 0;height: 21px">分类/文件设置</div>
	       <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
		 	  <tr>
		 	     <th class="th" width="250px" align="right">元素&nbsp;</th>
		 	     <th class="th" align="left">&nbsp;Excel列名</th>
		 	  </tr>
		 	  
		 	  <tr bgcolor="#FAFAFA">
		 	  	 <td width="250px" align="right">
	               <strong> 学科分类：</strong> 
	             </td>
	             <td>
	             	 <input name="<%=TempletConfig.RSDA_RSTY_ID %>" ONKEYPRESS="event.returnValue=isChar(this);" id="<%=TempletConfig.RSDA_RSTY_ID %>" isElement="true" class="text" style="margin-left: 2px;" value="${id_1 }" maxlength="2"></input>
	             	 <a id="<%=TempletConfig.RSDA_RSTY_ID %>_msg" name="<%=TempletConfig.RSDA_RSTY_ID %>_msg"></a>                                                                                                
	             </td>
		 	  </tr>
		 	  
		 	  <tr bgcolor="#FFFFFF">
		 	  	 <td width="250px" align="right">
	               <strong> 中图法分类：</strong> 
	             </td>
	             <td>
	             	 <input name="<%=TempletConfig.RSDA_MIDDLENAME %>" ONKEYPRESS="event.returnValue=isChar(this);" id="<%=TempletConfig.RSDA_MIDDLENAME %>" isElement="true" class="text" style="margin-left: 2px;" value="${id_2 }" maxlength="2"></input>
	             	 <a id="<%=TempletConfig.RSDA_MIDDLENAME %>_msg" name="<%=TempletConfig.RSDA_MIDDLENAME %>_msg"></a>                                                                                                
	             </td>
		 	  </tr>
		 	  
		 	  <!-- 自定义分类一 -->
		 	  <tr bgcolor="#FAFAFA">
		 	  	 <td width="250px" align="right">
	               <strong> ${rmstData[1] }：</strong> 
	             </td>
	             <td>
	             	 <input name="<%=TempletConfig.RSDA_RDTY_ONE %>" ONKEYPRESS="event.returnValue=isChar(this);" id="<%=TempletConfig.RSDA_RDTY_ONE %>" isElement="true" class="text" style="margin-left: 2px;" value="${id_3 }" maxlength="2"></input>                                                                                                
	             	 <a id="<%=TempletConfig.RSDA_RDTY_ONE %>_msg" name="<%=TempletConfig.RSDA_RDTY_ONE %>_msg"></a>
	             </td>
		 	  </tr>
		 	   <!-- 自定义分类二 -->
		 	  <tr bgcolor="#FFFFFF">
		 	  	 <td width="250px" align="right">
	               <strong> ${rmstData[2] }：</strong> 
	             </td>
	             <td>
	             	 <input name="<%=TempletConfig.RSDA_RDTY_TWO %>" ONKEYPRESS="event.returnValue=isChar(this);" id="<%=TempletConfig.RSDA_RDTY_TWO %>" isElement="true" class="text" style="margin-left: 2px;" value="${id_4 }" maxlength="2"></input>                                                                                                
	                 <a id="<%=TempletConfig.RSDA_RDTY_TWO %>_msg" name="<%=TempletConfig.RSDA_RDTY_TWO %>_msg"></a>
	             </td>
		 	  </tr>
		 	   <!-- 自定义分类三 -->
		 	  <tr bgcolor="#FAFAFA">
		 	  	 <td width="250px" align="right">
	               <strong> ${rmstData[3] }：</strong> 
	             </td>
	             <td>
	             	 <input name="<%=TempletConfig.RSDA_RDTY_THREE %>" ONKEYPRESS="event.returnValue=isChar(this);" id="<%=TempletConfig.RSDA_RDTY_THREE %>" isElement="true" class="text" style="margin-left: 2px;" value="${id_5 }" maxlength="2"></input>                                                                                                
	             	 <a id="<%=TempletConfig.RSDA_RDTY_THREE %>_msg" name="<%=TempletConfig.RSDA_RDTY_THREE %>_msg"></a>
	             </td>
		 	  </tr>
		 	  
		 	   <!-- 文件名 -->
		 	  <tr bgcolor="#FFFFFF">
		 	  	 <td width="250px" align="right">
	               <strong> 文件名：</strong> 
	             </td>
	             <td>
	             	 <input name="<%=TempletConfig.RSDA_FILENAME %>" ONKEYPRESS="event.returnValue=isChar(this);" id="<%=TempletConfig.RSDA_FILENAME %>" isElement="true" class="text" style="margin-left: 2px;" value="${id_6 }" maxlength="2"></input>                                                                                                
	             	 <span id="<%=TempletConfig.RSDA_FILENAME %>_msg" name="<%=TempletConfig.RSDA_FILENAME %>_msg" style="color: #c0c0c0">建议将文件名列设置到表格最后一列</span>
	             </td>
		 	  </tr>
		 	  
		 	  
		 	   <!-- 主题 -->
		 	  <tr bgcolor="#FFFFFF">
		 	  	 <td width="250px" align="right">
	               <strong> 资源主题：</strong> 
	             </td>
	             <td>
	             	 <input name="<%=TempletConfig.RSDA_SOURCE_SUBJECT %>" ONKEYPRESS="event.returnValue=isChar(this);" id="<%=TempletConfig.RSDA_SOURCE_SUBJECT %>" isElement="true" class="text" style="margin-left: 2px;" value="${id_7 }" maxlength="2"></input>                                                                                                
	             	 <span id="<%=TempletConfig.RSDA_SOURCE_SUBJECT %>_msg" name="<%=TempletConfig.RSDA_SOURCE_SUBJECT %>_msg" style="color: #c0c0c0">资源主体是将一组相关资源组织在一起！</span>
	             </td>
		 	  </tr>
		   </table>	
	  </form>
	  
	  ${requestScope.result }
  </body>
</html>
