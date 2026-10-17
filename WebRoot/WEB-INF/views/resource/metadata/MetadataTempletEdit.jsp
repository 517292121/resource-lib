<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.jdbcms.ToolUnit"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	String path = request.getContextPath();
	List<ResElementPool> elementList = (List<ResElementPool>)request.getAttribute("elementlist"); //资源类型下的所有元素
	long store = MyRequestUtil.getLongAttribute(request,"rmstId");
	long stype = MyRequestUtil.getLongAttribute(request,"rltyId");
	
	List templetIds = (List)request.getAttribute("tmpIds"); //该模板中拥有的元素ID
	String idStr = ToolUnit.ConvertListToString(templetIds);
	
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
	<title>录入资源模板设置</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0" />
    <link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
    <script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
    <script type="text/javascript" src="<%=path %>/js/BasicUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/editform.js"></script>
    
    <script type="text/javascript">
       function doSave(){
       	   var ids = jswork.getCheckboxValue(document.all.metaElementId);	
       	   if(ids == null || ids.length<1){
       	       alert("元素不能全部置为空！");
       	       return;
       	   } 
           document.all.elementform.submit();
       }
       
       function selectSwitch(item,restrict){
           var itemId = item.value;
           var statu = item.checked;
           if(item.checked == false && restrict == 'M'){
           	   alert("该元素为必备！");
           	   item.checked = true;
           	   return;
           }
           var childArr = document.getElementsByTagName("input");
           for(var i=0; i<childArr.length; i++){
               if(childArr[i].parentId && childArr[i].parentId == itemId){
                   childArr[i].checked = statu;
                   if(childArr[i].restrict == 'M'){
                   	   childArr[i].checked = true;
                   }
               }
           }
       }
       
       function setParent(item,parentIdname,restrict){
       	   if(item.checked == false && restrict == 'M'){
           	   alert("该元素为必备！");
           	   item.checked = true;
           	   return;
           }
           if(item.checked == true){
               document.getElementById(parentIdname).checked = true;
           }
       }
       
       function mustCheck(item){
       	   if(item.checked == false){
       	   	   alert("该元素为必备元素！");
               item.checked == true;
           }
       }
       
       function switchElement(selfItem){
       		var item = document.all.metaElementId;
       		if(selfItem.checked == true){
		        jswork.selectAll(item);
		    }else{
		        for(var i=0;i<item.length;i++){
		        	if(item[i].restrict == 'M'){
		        		item[i].checked	= true;
		        	}else{
				       item[i].checked= false;
		        	}
			    }
			    if(!item.length && item.restrict == 'M'){
			       item.checked = false;
			    }
		    }
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
			特色库：【${requestScope.rmstName}】&nbsp;资源类型：【${requestScope.rltyName}】
          </div>
	      <ul class="nav" style="float: right;">
		    <li><a href="#" onclick="doSave();"><img src="<%=path %>/images/common/common_submit.png"></img>保存</a></li>
		    <li><a href="<%=path %>/console/data/templetManager.htm?action=SourceTypeList"><img src="<%=path %>/images/common/common_back.png"></img>返回</a></li>
		  </ul>  
	   </div>
	   
	    <form action="<%=path %>/console/data/templetManager.htm" method="post" name="elementform">
	    <input type="hidden" name="action" value="SaveTemplet" > 
	    <input type="hidden" name="rmst" value="<%=store %>" > 
	    <input type="hidden" name="rlty" value="<%=stype %>" > 
	    <div class="title" style="margin-top: 0">都柏林核心元素</div>
		<table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ececec" >
		     <tr align="left" height="24px;" class="lbtable_tou">
		         <td width="23px" align="center">&nbsp;</td>
		         <td width="25px" align="center">
		           <input type="checkbox" name="selectAll" onclick="switchElement(this);" />
		         </td>
		         <td width="300px;">核心元素</td>
		         <td width="350px;">元素修饰</td>
		     </tr>
		     
		     <% int index = 0; %>
		     <c:forEach items="<%=elementList %>" var="v" varStatus="s">
		        <c:if test="${v.repoType == 1 }">
		         <% index++; %>
		         <tr height="33px;" bgcolor="#fafafa">
		           <td align="center"><%=index %></td>
		           <td align="center" width="25px">
		              
		             <input type="checkbox" name="metaElementId" value="${v.repoId }" onclick="selectSwitch(this,'${v.repoRestrict  }');" id="${v.repoId }" restrict="${v.repoRestrict  }" /><span <c:if test="${v.repoRestrict == 'M' }">style='color:red'</c:if>>${v.repoRestrict }</span>
		             
		           </td>
		         
		           <td> ${v.repoTitle }(${v.repoName })</td>
		           
		           <td valign="top">
					  <div class="extendsElement">
					      <c:forEach items="${v.childelements }" var="vr" varStatus="vs">
					        <div style="padding: 4px;">
					          <input type="checkbox" name="metaElementId" value="${vr.repoId }" parentId="${v.repoId }" onclick="setParent(this,'${v.repoId }','${vr.repoRestrict }')" restrict="${vr.repoRestrict  }"  /><span <c:if test="${vr.repoRestrict == 'M' }">style='color:red'</c:if>>${vr.repoRestrict }</span>
					         &nbsp; ${vr.repoTitle }(${vr.repoName })
					        </div>
					      </c:forEach>
					  </div>
		           </td>
		         </tr>
		       </c:if>  
		   </c:forEach> 
	</table>
	
	<div class="title">自定义核心元素</div>
	<table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ececec" >
		     <tr align="left" height="24px;" class="lbtable_tou">
		         <td width="23px" align="center">&nbsp;</td>
		         <td width="25px" align="center">
		            &nbsp;
		         </td>
		         <td width="300px;">核心元素</td>
		         <td width="350px;">元素修饰</td>
		     </tr>
		     
		     <% index = 0; %>
		     <c:forEach items="<%=elementList %>" var="v" varStatus="s">
		        <c:if test="${v.repoType > 1 }">
		          <% index++; %>
		         <tr height="33px;" bgcolor="#fafafa">
		           <td align="center"><%=index %></td>
		           <td align="center" width="25px">
		             <input type="checkbox" name="metaElementId" value="${v.repoId }" onclick="selectSwitch(this, '${v.repoRestrict }');"  id="${v.repoId }" restrict="${v.repoRestrict  }" /><span <c:if test="${v.repoRestrict == 'M' }">style='color:red'</c:if>>${v.repoRestrict }</span>
		           </td>
		         
		           <td> ${v.repoTitle }(${v.repoName })</td>
		           
		           <td valign="top">
					  <div class="extendsElement">
					      <c:forEach items="${v.childelements }" var="vr" varStatus="vs">
					        <div style="padding: 4px;">
					          <input type="checkbox" name="metaElementId" value="${vr.repoId }" parentId="${v.repoId }"  onclick="setParent(this,'${v.repoId }', '${vr.repoRestrict }')" restrict="${vr.repoRestrict  }"  /><span <c:if test="${vr.repoRestrict == 'M' }">style='color:red'</c:if>>${vr.repoRestrict }</span>
					          &nbsp; ${vr.repoTitle }(${vr.repoName })
					        </div>
					      </c:forEach>
					  </div>
		           </td>
		         </tr>
		       </c:if>  
		   </c:forEach> 
	</table>
	
	
	</form>
	<script type="text/javascript">
	       var ids = "<%=idStr %>";
	       jswork.InitCheckboxByStr(ids,document.all.metaElementId);
	</script>
	${requestScope.result }
</body>
</html>

