<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IMetadataTemletService"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	List<ResElementPool> elementList = (List<ResElementPool>)request.getAttribute("elementlist");
	long stype = MyRequestUtil.getLongAttribute(request,"rltyId");
	
	
	IMetadataTemletService templet = (IMetadataTemletService)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("metadataTempletRpc");
	JSONRPCBridge.registerObject("templetRpc", templet);
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>资源类型模板设置</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
    <link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
    <script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
    <script type="text/javascript" src="<%=path %>/js/BasicUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/editform.js"></script>
    <script type="text/javascript" src="<%=path %>/js/jquery.min.js"></script>
    <script type="text/javascript" src="<%=path %>/plugin/layer/layer.js"></script>
    <style type="text/css">
        td{
            padding-left: 5px;
         }
    </style>
    <script type="text/javascript">
       
       //新增元素修饰 
       function addelement(eleType,parentId){
           var url = "<%=path %>/console/sourceTemplet.htm?action=OnCreateElement&sourceType=<%=stype %>&eleType=" + eleType + "&parentId=" + parentId;
           
           layer.open({
		        type: 2,
		        title: '新增元数据',
		        maxmin: true,
		        shadeClose: true, //点击遮罩关闭层
		        area : ['500px' , '550px'],
		        content: url
		   });
           
           /**
           var result = window.showModalDialog(url,"","dialogWidth:400px;dialogHeight:350px;status:0");
           if(result > 0){
               window.location.reload();
           }
           **/
       }
       
       
       //删除元素
       function deleteElement(eleid,parentid){
           if(!window.confirm("您确定要删除该元素吗？")){
               return;
           }
           try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.templetRpc.DeleteElement(eleid,parentid);
		        if(result){
		            window.location.reload();
		        }else{
		            alert("对不起，删除失败。该元素已保存有相关数据！");
		        }
	        }catch(e){
	           alert("获取JSONRpcClient对象时发生异常:" + e);
	        }
       }
       
       function updateValue(data,id,colname){
            try{
		        jsonrpc = new JSONRpcClient("<%=path %>/Json_Rpc");
		        var result = jsonrpc.templetRpc.updateSingleValue(colname,data,id);
		        return result;
	        }catch(e){
	           alert("获取JSONRpcClient对象时发生异常:" + e.message);
	           return false;
	        }
       }
       
       function editElement(eleid){
           var url = "<%=path %>/console/sourceTemplet.htm?action=OnEditElement&eleId=" + eleid + "&sourcetype=<%=stype %>" ;
           
           layer.open({
		        type: 2,
		        title: '新增元数据',
		        maxmin: true,
		        shadeClose: true, //点击遮罩关闭层
		        area : ['500px' , '550px'],
		        content: url
		   });
           
           /**
           var result = window.showModalDialog(url,"","dialogWidth:400px;dialogHeight:350px;status:0");
           if(result > 0){
               window.location.reload();
           }
           **/
       }
       
       function setCalis(id, item){
       	   var v = 0;
       	   if(item.checked){
       	   	   v = 1;
       	   }
       	   updateValue(v,id,'REPO_CALIS');
       }
    </script>
    
    <style type="text/css">
	   *{
	      font-size: 12px;
	      font-family: 宋体;
	   }
	</style>
</head>
<body>
      <div class="tool">
          <div style="float: left">
             	资源类型：【${requestScope.rltyName}】
          </div>
	      <ul class="nav" style="float: right;">
		    <li><a href="<%=path %>/console/data/sourceType.htm?action=SourceTypeList"><img src="<%=path %>/images/common/common_back.png"></img>返回</a></li>
		  </ul>  
	   </div>

		<!-- 循环文档开始 -->
		<div class="title" style="margin-top: 0">都柏林核心元素</div>
		<table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ececec" >
		     <tr align="left" height="24px;" class="lbtable_tou">
		         <td width="50px" align="center">&nbsp;</td>
		         <td width="60px;">CALIS收割</td>
		         <td width="200px;">核心元素</td>
		         <td width="400px;">元素修饰</td>
		         <td>编码体系</td>
		         <td width="66">约束</td>
		     </tr>
		     
		     
		     <c:forEach items="<%=elementList %>" var="v" varStatus="s">
	         <c:if test="${v.repoType == 1 }">
	         <tr height="33px;" bgcolor="#fafafa">
	         
	           <td align="center">
	             <a href="javascript:editElement('${v.repoId }');"><img src="<%=path %>/images/buildings.png" title="修改元素信息" /></a>
	           </td>
	           
	           <td align="center">
	           	  <input type="checkbox" name="repoCalis" title="是否导入为Calis收割元素"
	           	    <c:if test="${v.repoCalis == 1 }">checked="checked"</c:if>
	           	  	onclick="setCalis(${v.repoId }, this);"
	           	  />
	           </td>
	           <td>
	              <input class="input_edit" 
	                     onblur="onOut(this,'REPO_TITLE','${v.repoId }','${v.repoName }');" 
                         onclick="onInt(this,'${v.repoName }');"
                         orinData="${v.repoTitle }"
                         <c:if test="${v.repoRestrict eq 'M' or v.repoRestrict eq 'm'}">style="color:red;"</c:if>
                         onkeypress="if(event.keyCode==13){onOut(this,'REPO_TITLE','${v.repoId }','${v.repoName }');}"
	                     value="${v.repoTitle }" />
	                     ${v.repoName }
	           </td>
	           
	           <td valign="top">
				  <div class="extendsElement">
				      <c:forEach items="${v.childelements }" var="vr" varStatus="vs">
				        <div style="padding: 4px;">
				         <input type="checkbox" name="repoCalis" title="是否导入为Calis收割元素" 
			           	    <c:if test="${vr.repoCalis == 1 }">checked="checked"</c:if>
			           	  	onclick="setCalis(${vr.repoId }, this);"
			           	  />
				        <c:choose>
				        	<c:when test="${vr.repoRestrict eq 'M' or vr.repoRestrict eq 'm'}">
				        		<span style="color: red;">${vr.repoTitle }</span>
				        	</c:when>
				        	<c:otherwise>${vr.repoTitle }</c:otherwise>
				        </c:choose>
				        (${vr.repoName })
				        <a href="javascript:editElement('${vr.repoId }');"><img src="<%=path %>/images/buildings.png" title="修改元素信息" /></a>
				        <a href="javascript:deleteElement('${vr.repoId }','${vr.repoParent }');"><img src="<%=path %>/images/extform/01.png"  style="margin:2px 5px" title="彻底删除该修饰元素" /></a>
				        </div>
				      </c:forEach>
				  </div>
				      
				  <div style="width:23px; float:right; text-align:right; margin-top:3px; margin-right:3px;">
	                 <a href="javascript:addelement(3,'${v.repoId }');"><img src="<%=path %>/images/extform/03.png" title="新增修饰元素" /></a>
	              </div>
					
	           </td>
	           
	           <td>
	              <div class="codes">
	              <c:forEach items="${fn:split(v.repoCode,',') }" var="code">
	                 ${code } <br />
	              </c:forEach>
	              </div>
	           </td>
	           
	           <td>
	              <input class="input_edit" 
	                     onblur="onOut(this,'REPO_RESTRICT','${v.repoId }','${v.repoRestrict }');" 
                         onclick="onInt(this,'${v.repoRestrict }');"
                         orinData="${v.repoRestrict }"
                         onkeypress="if(event.keyCode==13){onOut(this,'REPO_RESTRICT','${v.repoId }','${v.repoRestrict }');}"
	                     value="${v.repoRestrict }" />
	           </td>
	         </tr>
	         </c:if>
	       </c:forEach> 
	</table>
		   
	
	<!-- 自定义核心元素START -->
    <div class="title">自定义核心元素</div>
    	  <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ececec" style="margin-bottom: 3px;">
	       <tr align="left" height="24px;" class="lbtable_tou">
	         <td width="50px" align="center">
	            <a href="javascript:addelement(2,0);"><img src="<%=path %>/images/extform/03.png" width="12px" style="margin-top: 1px;" align="absmiddle" title="新增自定义核心元素" /></a>
	         </td>
	         <td width="60px;">CALIS收割</td>
	         <td width="200px;">核心元素</td>
	         <td width="400px;">元素修饰</td>
	         <td>编码体系</td>
	         <td width="66">约束</td>
	       </tr>
	       <c:forEach items="<%=elementList %>" var="v">
	         <c:if test="${v.repoType > 1 }">
	         <tr height="29px;" bgcolor="#fafafa">
	           <td align="center" width="50px">
	             <a href="javascript:deleteElement('${v.repoId }','0');"><img src="<%=path %>/images/extform/01.png" title="删除该元素" /></a>
	             <a href="javascript:editElement('${v.repoId }');"><img src="<%=path %>/images/buildings.png" title="修改元素信息" /></a>
	           </td>
	           <td align="center">
	           	  <input type="checkbox" name="repoCalis" title="是否导入为Calis收割元素" 
	           	    <c:if test="${v.repoCalis == 1 }">checked="checked"</c:if>
	           	  	onclick="setCalis(${v.repoId }, this);"
	           	  />
	           </td>
	           <td width="200px;">
	           		<c:choose>
			        	<c:when test="${v.repoRestrict eq 'M' or v.repoRestrict eq 'm'}">
			        		<span style="color: red;">${v.repoTitle }</span>
			        	</c:when>
			        	<c:otherwise>${v.repoTitle }</c:otherwise>
			        </c:choose>
	                (${v.repoName })
	           </td>
	           <td valign="top" width="350px;">
	              <div style="float:left;padding-bottom: 2px;" class="codes">
	                 <c:forEach items="${v.childelements }" var="vr" varStatus="vs">
	                   <div style="padding: 4px;">
	                   	   <input type="checkbox" name="repoCalis" title="是否导入为Calis收割元素" 
			           	    <c:if test="${vr.repoCalis == 1 }">checked="checked"</c:if>
			           	  	onclick="setCalis(${vr.repoId }, this);"
			           	   />
	                   		<c:choose>
					        	<c:when test="${vr.repoRestrict eq 'M' or vr.repoRestrict eq 'm'}">
					        		<span style="color: red;">${vr.repoTitle }</span>
					        	</c:when>
					        	<c:otherwise>${vr.repoTitle }</c:otherwise>
					        </c:choose>
	                     	(${vr.repoName })
	                     <a href="javascript:editElement('${vr.repoId }');"><img src="<%=path %>/images/buildings.png" title="修改元素信息" /></a>	
	                     <a href="javascript:deleteElement('${vr.repoId }','${vr.repoParent }');"><img src="<%=path %>/images/extform/01.png"  style="margin:2px 5px" title="彻底删除该修饰元素" /></a>
	                   </div>  
	                 </c:forEach>    
	              </div>
	              
	              <div style="width:23px; float:right; text-align:right; margin-top:3px; margin-right:3px;">
	                 <a href="javascript:addelement(3,'${v.repoId }');"><img src="<%=path %>/images/extform/03.png" title="新增修饰元素" /></a>
	              </div>
	           
	           </td>
	           <td>
	              <div class="codes">
	              <c:forEach items="${fn:split(v.repoCode,',') }" var="code">
	                 ${code } <br />
	              </c:forEach>
	              </div>
	           </td>
	           
	           <td width="66">
	              ${v.repoRestrict }
	           </td>
	         </tr>
	         </c:if>
	       </c:forEach>
	    </table>
	    &nbsp;
    <!-- 自定义核心元素END -->
</body>
</html>

