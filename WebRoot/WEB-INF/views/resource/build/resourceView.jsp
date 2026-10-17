<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.TempletConfig"%>
<%
	String path = request.getContextPath();
%>
<%@ include file="../../common/extInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    
    <title>${resource.rsdaTitle }</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>
	
	<style type="text/css">
		.title{
			height: 26px;
			background-color: #e8e8e8;
			padding-top: 5px;
			text-align: center;
			font-weight: bold;
			font-family: 华文仿宋;
			margin-top: 1px;
		}
		.resource{
			border-top: 1px solid #c0c0c0;
			border-bottom: 1px solid #c0c0c0;
			padding: 10px 0px 10px 100px;
		}
		img{
			border: 0px;
			margin: 2px;
		}
	</style>
  </head>
  
  <body>
  	<center>
  	<div style="font-size: 45px;font-family: 华文新魏;">
  		资源详细信息
  	</div>
  	</center>
  
  	<div class="resource">
  		<table cellpadding="0" border="0" cellspacing="1">
  			<tr style="font-size: 26px;height: 40px;">
  				<td><b>资源：<span style="color: #ff8040">${resource.rsdaTitle }</span></b></td>
  			</tr>
  			<tr style="font-size: 12px;">
  				<td style="padding-left: 2px;color: #808080">
  					所属特色库：${resource.resMetaStore.rmstName }
  					&nbsp;
  					资源类型：${resource.resLiterType.rltyName }
  					&nbsp;
  					所属作业：${resource.resMissionInfo.rminMissname }
  					&nbsp;
  					来源类型：<c:if test="${resource.rsdaAuthortype  == 1 }">管理员手工录入</c:if><c:if test="${resource.rsdaAuthortype  == 2 }">管理员批量导入</c:if><c:if test="${resource.rsdaAuthortype  == 3 }">用户上传</c:if>
  					&nbsp;
  					创建者：${resource.sysManager.smanLoginName }(${resource.sysManager.smanName })
  				</td>
  			</tr>
  		</table>
  	</div>
  	<div class="title">资源元素信息</div>
    	<table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC">
	 
	     <%
	       int i=1;
	       String bgc = "";
	     %>
	     <c:forEach items="${EleDataList }" var="v" varStatus="s">
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
	               <strong>${v.repoTitle }(${v.repoName })：</strong> 
	             </td>
	             <td> 
	             	${v.repoData }      
	             </td>
	         </tr>
	         
	         <!-- 将所有的元素上级元素再和核心相比，显示出它的所有子元素 -->
	         <c:forEach items="${EleDataList }" var="vc" varStatus="sc">
	          	 <c:if test="${v.repoId == vc.repoParent }">
		         <tr height="25px;" bgcolor="<%=bgc %>">
		           <td width="250px" align="right"> &nbsp;
			            ${vc.repoTitle }(${vc.repoName })：
		           </td>
		           <td> 
		           		${vc.repoData }             
		           </td>
		         </tr>
		         </c:if>
	         </c:forEach>
	       </c:if>
       </c:forEach> 
       </table>
       
       <!-- 选择分类 -->
       <div class="title">资源分类</div>
	     <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	       <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              学科分类：
	          </td>
	          <td>
	              ${resource.resSubjectType.rstyName }
		      </td>    
	       </tr>
	       
	       <!-- 自定义分类一 -->
	       <tr bgcolor="#FFFFFF" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE) %>：
	          </td>
	          <td>
	              ${rdtyName1 }
	          </td>
	       </tr>
	       
	       <!-- 自定义分类二 -->
	       <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO) %>：
	          </td>
	          <td>
	              ${rdtyName2 }
	          </td>
	       </tr>
	       
	       <!-- 自定义分类三 -->
	       <tr bgcolor="#FFFFFF" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE) %>：
	          </td>
	          <td>
	              ${rdtyName3 }
	          </td>
	       </tr>
	       
	       <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	             <img src="<%=path %>/images/extform/bullet_add_2.png"></img>  
	               语种：
	          </td>
	          <td> ${language } </td>
	       </tr>
	       <tr bgcolor="#FFFFFF" height="25">
		       	<td width="250px" align="right">
		       		<img src="<%=path %>/images/extform/bullet_add_2.png"></img>  
		       		主题：
		       	</td>
		       	<td>${rssuName } </td>
	       </tr>
	 </table>
		   
	 
	    
	    <div class="title">资源文件</div>
	     <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	        <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	               封 面：
	          </td>
	          <td id="imageFileArea" style="padding: 2px;">
	          	 <c:choose >
	          	 	<c:when test="${empty resource.rsdaBigimgUrl}"><font color="#5f5f5f">无</font></c:when>
	          	 	<c:otherwise>
	          	 		<img src="${fileAddress }${resource.rsdaBigimgUrl} }&amp;isOnLine=true" width="100px" height="110px;" style="border: 1px solid #c0c0c0" title="${resource.rsdaBigimgName}"></img>
	          	 	</c:otherwise>
	          	 </c:choose>
	          </td>
	        </tr>
	        
	        <tr bgcolor="#FFFFFF" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	               预览文件：
	          </td>
	          <td id="imageFileArea">
	          	  <c:choose >
	          	 	<c:when test="${empty resource.rsdaViewfileSwfUrl}"><font color="#5f5f5f">无</font></c:when>
	          	 	<c:otherwise>
	          	 		<a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&sourceId=${resource.rsdaId }&type=2" target="_blank"><img src="<%=path %>/images/buttonView.png" /></a>
	          	 	</c:otherwise>
	          	  </c:choose>
	          </td>
	        </tr>
	        
	        <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img> 
	               源 文 件：
	          </td>
	          <td>
	          	  <c:choose >
	          	 	<c:when test="${empty resource.rsdaFileUrl}">
	          	 		<img src="<%=path %>/images/buttonDonwloadFreeze.png" onclick="alert('该资源没有源文件！');" />
	          	 	</c:when>
	          	 	<c:otherwise>
	          	 		<a href="${fileAddress }${resource.rsdaFileUrl}"><img src="<%=path %>/images/buttonDonwload.png" title="${resource.rsdaFileName }"/></a>
	          	 	</c:otherwise>
	          	  </c:choose>
	          	  
	          	  <c:choose >
	          	 	<c:when test="${empty resource.rsdaFileUrl}">
	          	 		<img src="<%=path %>/images/buttonViewFreeze.png" onclick="alert('该资源源文无法在线预览！');" />
	          	 	</c:when>
	          	 	<c:otherwise>
	          	 		<a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&sourceId=${resource.rsdaId }&type=3" target="_blank"><img src="<%=path %>/images/buttonView.png" /></a>
	          	 	</c:otherwise>
	          	  </c:choose>
	          </td>
	        </tr>
	    </table>
		   
  </body>
</html>
