<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<jsp:useBean id="JSONRPCBridge" scope="session" class="com.metaparadigm.jsonrpc.JSONRPCBridge"/>
<%
	String path = request.getContextPath();
	IResourceRpc resourceRpc = (IResourceRpc)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("resourceRpc");

	JSONRPCBridge.registerObject("resourceRpc",resourceRpc);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>敦煌研究数据库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/inputSource.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/extbutton.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=path %>/css/message.css" type="text/css"></link>
	<script type="text/javascript" src="<%=path %>/js/editform.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jsonrpc.js"></script>
	<style type="text/css">
		*{
	      font-size: 12px;
	      font-family: 宋体;
	    }
	    table td{padding: 5px;}
	</style>
  </head>
  	<script type="text/javascript">
  		String.prototype.trim = function () {
		    return this.replace(/(^\s*)|(\s*$)/g, "");
		};
		function checkForm(){
			var rslaStatus = document.getElementById('rslaStatus').value;
			if(rslaStatus == 4){
				var rslaAuditing = document.getElementById('rslaAuditing');
				if(rslaAuditing){
					rslaAuditing = rslaAuditing.value;
					if(rslaAuditing.trim().length < 1) {
						alert('审核描述不能为空！');
						return false;
					}
					if(rslaAuditing.trim().length > 170) {
						alert("审核描述不得超过170个字符！");
						return false;
					}
				}
			}
			return true;
		}
		function save(){
			if(checkForm()==true)
				document.forms['form1'].submit();
		}
		function saveForm(status,val){
			var rslaStatus = document.getElementById('rslaStatus');
			if(rslaStatus){
				rslaStatus.value = status;
				var type = document.getElementById('type');
				if(type)type.value = val;
				save();
			}
		}
		function goBack(){
			document.location.href = "<%=path %>/console/data/otherLanguageMissionInfo.htm?action=initOtherLanguageRmsMissionInfoDataCheck&rmlaId=${rmlaId}";
		}

	   function getPath(){
           return "<%=path %>";
       }
	   var element_split = "<%=ConstantsObj.ELEMENT_SPLIT %>";
       var element_data_split = "<%=ConstantsObj.ELEMENT_DATA_SPLIT %>";

	   function updatePassLanguage(){
	       var sourceid = ${rsd.rsdaId};
	       var lanId = document.all.passLanguage.value;
	       changLanguage(sourceid, lanId, ${rsd.rsdaRmstId},${rsd.rsdaRltyId},'check');
	   }
	</script>
  <script type="text/javascript" src="<%=path %>/viewsjs/resource/build/interpret.js"></script>
  <body style="font-size:13px;">
	 <div class="title" style="margin-top: 0;padding-left: 5px;">资源详情 >> ${rsd.rsdaTitle}&nbsp;&nbsp;</div>




	 <%
       int bg = 1;
       String bgcolor = "";
     %>
	 <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	    <tr align="left">
	       <th class="th">属性名称</th>
	       <th class="th">源数据</th>
	       <th class="th">翻译数据</th>
	    </tr>
	    <tr bgcolor="#ECEDEE" height="25">
          <td width="230px" align="right">
             <img src="<%=path %>/images/extform/bullet_add_2.png"></img>
               语种：
          </td>

          <td>
             <select name="passLanguage" id="passLanguage" onchange="updatePassLanguage();">
	          ${lname }
             </select>
          </td>
          <td>
              <input name="language_id" id="language_id" type="hidden" value="${requestScope.language.rltyId }">
              ${language.rltyName }
          </td>
       </tr>

    	<c:forEach items="${rsdList}" var="rsdArr">
	 		<c:if test="${rsdArr[3] eq 0}">
	 			<% bg++;
		           if(bg % 2 == 0){
		              bgcolor = "#ffffff";
		           }else{
		              bgcolor = "#FAFAFA";
		           }
		        %>
		        <c:if test="${!empty rsdArr[2] || !empty rsdArr[5] }">
		         	<tr bgcolor="<%=bgcolor %>">
			         	<td width="20%" align="right">
			         		<img src="<%=path %>/images/extform/bullet_add_2.png"></img>
		               		<strong>${rsdArr[0]}(${rsdArr[1]})：</strong>
			         	</td>
			 			<td width="40%">
			 				<span id="data_${rsdArr[4]}">${rsdArr[2]}&nbsp;</span>
			 			</td>
			 			<td width="40%">
			 				${rsdArr[5]}&nbsp;
			 			</td>
		            </tr>
	            </c:if>

		         <c:forEach items="${rsdList}" var="rsdArr2">
		         	<c:if test="${rsdArr[4] eq rsdArr2[3]}">
		         		<% bg++;
				           if(bg % 2 == 0){
				              bgcolor = "#ffffff";
				           }else{
				              bgcolor = "#FAFAFA";
				           }
				        %>
				        <c:if test="${!empty rsdArr2[2] || !empty rsdArr2[5] }">
			         		<tr bgcolor="<%=bgcolor%>">
			         			<td width="20%" align="right">
					         		<h2>${rsdArr2[0]}(${rsdArr2[1]})：</h2>
					         	</td>
					 			<td width="40%">
					 				<span id="data_${rsdArr2[4]}">${rsdArr2[2]}&nbsp;</span>
					 			</td>
					 			<td width="40%">
					 				${rsdArr2[5]}&nbsp;
					 			</td>
			         		</tr>
		         		</c:if>
		         	</c:if>
		         </c:forEach>

	         </c:if>
	 	</c:forEach>
	 </table>
	 <br/>

	 <div class="title" style="margin-top: 0;padding-left: 5px;">审核资源</div>
	 	 <br/>
	 	 <form id='form1' name='form1' action="<%=path %>/console/data/otherLanguageMissionInfo.htm?action=saveRsdaOtherLanguageStatus" method="post" onsubmit="return checkForm();">
	 	 	<table cellpadding="0" width="100%" border="0" cellspacing="0" bgcolor="#ECECEC" >
	 	 	   <tr bgcolor="#FFFFFF" height="25">
	 	 	   		<td width="20%" align="right" valign="top" >描 述：</td>
	 	 	   		<td width="80%">
		 				<input type="hidden" id="rslaStatus" name="rslaStatus" value="3"/>
		 				<input type="hidden" id="rslaId" name="rslaId" value="${rsl.rslaId}"/>
		 				<input type="hidden" id="rsdaId" name="rsdaId" value="${rsd.rsdaId}"/>
		 				<input type="hidden" id="rltyId" name="rltyId" value="${rltyId}"/>
		 				<input type="hidden" id="rmlaId" name="rmlaId" value="${rmlaId}"/>
		 				<input type="hidden" id="type"   name="type" value=""/>
	 	 	   			<textarea rows="6" cols="50" name="rslaAuditing" id="rslaAuditing">${rsl.rslaAuditing }</textarea>
	 	 	   		</td>
	 	 	   </tr>
	 	 	   <tr bgcolor="#FFFFFF" height="25">
	 	 	   		<td width="20%">&nbsp;</td>
	 	 	   		<td style="padding-top: 5px;" width="80%">
	 	 	   			<div class="tool1" style="padding-top: 6px;">
	                      <ul class="nav">
							<li><a href="javascript:saveForm(3,3)" style="padding-top: 3px;" title="通过并继续审核下一条资源" class="btnb">通过并继续 </a></li>
		                    <li><a href="javascript:saveForm(3)" style="padding-top: 3px;">通 过</a></li>
		                    <li><a href="javascript:saveForm(4)" style="padding-top: 3px;">不通过</a></li>
							<li><a href="javascript:goBack()" style="padding-top: 3px;">返 回</a></li>
		                  </ul>
		               </div>
	 	 	   		</td>
	 	 	   </tr>
		    </table>
	 	 </form>
	 	 <br/>

	 	 <!-- 资源保存成功的图片提示 -->
         <div id="SuccessMessageBox" class="SuccessMessageBox"></div>
         ${result}
  </body>
</html>
