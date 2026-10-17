<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.core.TempletConfig"%>
<%@page import="cn.com.adtech.riss2.jdbcms.ToolUnit"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@page import="java.util.List"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResElementPool"%>
<%@page import="cn.com.adtech.riss2.jsonrpc.IResourceRpc"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="com.metaparadigm.jsonrpc.JSONRPCBridge"%>
<%@page import="cn.com.adtech.riss2.controller.MyRequestUtil"%>
<%@page import="cn.com.adtech.riss2.core.BasicDataUtil"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
String downLoadPath = request.getSession().getServletContext().getInitParameter("downLoadPath");
%>
<%
	List<ResElementPool> EleDataList = (List<ResElementPool>)request.getAttribute("EleDataList");
	ResSourceData rsd = (ResSourceData)request.getAttribute("rsd");

	IResourceRpc resourceRpc = (IResourceRpc)WebApplicationContextUtils.getWebApplicationContext(
	pageContext.getServletContext()).getBean("resourceRpc");


	int reqType = MyRequestUtil.getIntAttribute(request,"reqType");
	if(reqType == 0){
		reqType = MyRequestUtil.getIntParameter(request,"reqType");
	}
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
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/build/ResourceData.js"></script>
	<style type="text/css">
		.my_btn {background:url('<%=path %>/images/rms_download.png') no-repeat; width:53px; height:21px;}
		.viewFile_btn {background:url('<%=path %>/images/viewFile.png') no-repeat; width:53px; height:21px;}
		.rms_tr a:hover{ background:url('<%=path %>/images/rms_download2.png') no-repeat ; width:53px; height:21px;display: block;}
		*{font-size: 12px; font-family: 宋体;}
		.back{display: block;float: right;padding-right: 10px;font-weight: lighter;font-size: 12px;}
		.td_p {padding: 5px;}
	</style>
  </head>
  	<script type="text/javascript">
	  	String.prototype.trim = function () {
		    return this.replace(/(^\s*)|(\s*$)/g, "");
		};
		function checkForm(){
			var rsdaStatus = document.getElementById('rsdaStatus').value;
			if(rsdaStatus == 4){
				return true;
			}else{
				var b = checkform();
				return b;
			}
		}
		function save(){
			if(checkForm()==true)
				document.forms['form1'].submit();
		}
		function saveForm(status,val){
			var rsdaStatus = document.getElementById('rsdaStatus');
			if(rsdaStatus){
				rsdaStatus.value = status;
				var type = document.getElementById('type');
				if(type)type.value = val;
				save();
			}
		}
		function goBack(){
			if(${param.userid} == -1){
				document.location.href = "<%=path %>/console/data/memberResource.htm?action=initMemberResourceList";
			}else{
			  document.getElementById("form2").submit()
			}
		}
	</script>
	<script type="text/javascript">
       function getPath(){
           return "<%=path %>";
       }


       function viewFile(id, col){
           window.open("<%=path %>/pages/FileView.jsp?id="+id+"&col=" + col);
       }

       function checkRsdaTitle(item, selfid){
       	   var data = item.value;
       	   if(item.sdata == data){
       	   	   return;
       	   }
       	   item.sdata = data;
       	   onCheckTitle(item, selfid);
       }
       var maxHeight = <%=ConstantsObj.TEXTAREA_MAX_HEIGHT %>;
       var addOffset = null;
    </script>
	<script type="text/javascript" src="<%=path %>/viewsjs/resource/build/ResourceData.js"></script>
  <body style="font-size:13px;">
    <form action="<%=path%>/console/data/rmsMissionInfoDataCheck.htm?action=initRmsMissionInfoDataCheckView" id="form2" name="form2" method="post">
    <input type="hidden" id="rminId" name="rminId" value="${rsd.rsdaRminId}"/>
    <input type="hidden" id="userid" name="userid"  value="${param.userid}"/>
    <input type="hidden"  id="pagecount" name="pagecount" value="${pagecount }"/>
    <input type="hidden" id="titleshow" name="titleshow" value="${titleshow}"/>
    </form>

	 <form id='form1' name='form1' action="<%=path %>/console/data/rmsMissionInfoDataCheck.htm?action=saveRsdaStatus" method="post" enctype="multipart/form-data" onsubmit="return checkForm();">
	 	 <input type="hidden" name="store" value="<%=rsd.getRsdaRmstId() %>"></input>
		 <input type="hidden" name="stype" value="<%=rsd.getRsdaRltyId() %>"></input>
		 <input type="hidden" name="rmin_id" value="<%=rsd.getRsdaRminId() %>"></input>
		 <input type="hidden" name="rminId" value="<%=rsd.getRsdaRminId() %>"></input>
		 <input type="hidden" name="lanId" value="${requestScope.languageId }"></input>
		 <input type="hidden" name="sid" value="<%=rsd.getRsdaId() %>"></input>
		 <input type="hidden" name="userid" value="<%=rsd.getRsdaSmanId() %>"></input>
		 <input type="hidden" name="rihiId" value="${param.rihiId }"></input>
		 <input type="hidden" name="rstyId" id="rstyId" value="${rstyId }"></input>

		 <input type="hidden" name="searchtitle" id="searchtitle" value="${titleshow}"/>
		 <input type="hidden" id="rsdaId" name="rsdaId" value="${rsd.rsdaId}"/>
		 <input type="hidden" id="rsdaStatus" name="rsdaStatus"  value="3"/>
		 <input type="hidden" id="type" name="type" value="3"/>
	 	 <div class="title" style="margin-top: 0;padding-left: 5px;">资源详情 >> ${rsd.rsdaTitle}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	 	录入者：<%=rsd.getSysManager().getSmanName() %>&nbsp;&nbsp;&nbsp;&nbsp;
	 	录入时间：<%=rsd.getRsdaBuilddate() %>&nbsp;&nbsp;&nbsp;&nbsp;
	 	录入者ID：<%=rsd.getSysManager().getSmanId() %>&nbsp;&nbsp;&nbsp;&nbsp;
	 </div>

	 <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC">

	     <%
	       int i=1;
	       String bgc = "";
	     %>
	     <c:forEach items="<%=EleDataList %>" var="v" varStatus="s">
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
	             	<c:choose>
	             		<c:when test="${fn:toLowerCase(v.repoName) == 'location' }">
	             			<select name="${v.repoId }" id="${v.repoId }" style="width: 300px;margin-left: 2px;height: 22px;">
				              <option value="">请选择...</option>
				              <%=BasicDataUtil.getLocationOptions("") %>
				            </select>

				            <script type="text/javascript">
				            	document.getElementById('${v.repoId }').value = '${v.repoData }';
				            </script>
				            <a id="location_msg" name="location_msg">
				                <c:if test="${v.repoRestrict == 'R' }">
				                    <img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img>
				                </c:if>
				                <c:if test="${v.repoRestrict == 'M' }">
				                    <img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img>
				                </c:if>
				                <c:if test="${v.repoRestrict == 'MA' }">
				                   <img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img>
				                </c:if>
				                <c:if test="${v.repoRestrict == 'O' }">
				                   <img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img>
				                </c:if>
			                </a>
	             		</c:when>

	             		<c:when test="${fn:toLowerCase(v.repoName) == 'title' }">
	             			<textarea name="${v.repoId }"
			                       class="source_text"
			                       restrict="${v.repoRestrict }"
			                       onblur="checkRsdaTitle(this,'<%=rsd.getRsdaId() %>');"
			                       onpropertychange="setTextAreaHeight(this);"
			                       onkeyup="setTextAreaHeight(this);"
			                       onkeydown="setTextAreaHeight(this);"
			                       sdata="${v.repoData }"
				                   sourceElement="true">${v.repoData }</textarea>
			                <a id="${v.repoId }_msg">
				                <c:if test="${v.repoRestrict == 'R' }">
				                    <img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img>
				                </c:if>
				                <c:if test="${v.repoRestrict == 'M' }">
				                    <img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img>
				                </c:if>
				                <c:if test="${v.repoRestrict == 'MA' }">
				                   <img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img>
				                </c:if>
				                <c:if test="${v.repoRestrict == 'O' }">
				                   <img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img>
				                </c:if>
			                </a>
	             		</c:when>

	             		<c:otherwise>
			                <textarea name="${v.repoId }"
			                       class="source_text"
			                       restrict="${v.repoRestrict }"
			                       onpropertychange="setTextAreaHeight(this);"
			                       onkeyup="setTextAreaHeight(this);"
			                       onkeydown="setTextAreaHeight(this);"
				                   sourceElement="true">${v.repoData }</textarea>
			                <a id="${v.repoId }_msg">
				                <c:if test="${v.repoRestrict == 'R' }">
				                    <img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img>
				                </c:if>
				                <c:if test="${v.repoRestrict == 'M' }">
				                    <img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img>
				                </c:if>
				                <c:if test="${v.repoRestrict == 'MA' }">
				                   <img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img>
				                </c:if>
				                <c:if test="${v.repoRestrict == 'O' }">
				                   <img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img>
				                </c:if>
			                </a>
			            </c:otherwise>
	                </c:choose>
	             </td>
	         </tr>

	         <!-- 将所有的元素上级元素再和核心相比，显示出它的所有子元素 -->
	         <c:forEach items="<%=EleDataList %>" var="vc" varStatus="sc">
	          	 <c:if test="${v.repoId == vc.repoParent }">
		         <tr height="25px;" bgcolor="<%=bgc %>">
		           <td width="250px" align="right"> &nbsp;
			            ${vc.repoTitle }(${vc.repoName })：
		           </td>
		           <td>
		           		<c:choose>
	             		<c:when test="${fn:toLowerCase(vc.repoName) == 'location' }">
	             			<select name="${vc.repoId }" id="${vc.repoId }" style="width: 300px;margin-left: 2px;height: 22px;">
				              <option value="">请选择...</option>
				              <%=BasicDataUtil.getLocationOptions("") %>
				            </select>

				            <script type="text/javascript">
				            	document.getElementById('${vc.repoId }').value = '${vc.repoData }';
				            </script>

				            <a id="location_msg" name="location_msg">
					             <c:if test="${vc.repoRestrict == 'R' }">
				                     <img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'M' }">
				                     <img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'MA' }">
				                     <img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'O' }">
				                     <img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img>
				                 </c:if>
				            </a>
	             		</c:when>
	             		<c:otherwise>
				              <textarea name="${vc.repoId }"
				                     class="source_text"
				                     restrict="${vc.repoRestrict }"
				                     sourceElement="true"
				                     onpropertychange="setTextAreaHeight(this);"
			                         onkeyup="setTextAreaHeight(this);"
			                         onkeydown="setTextAreaHeight(this);" >${vc.repoData }</textarea>
				              <a id="${vc.repoId }_msg">
					             <c:if test="${vc.repoRestrict == 'R' }">
				                     <img src="<%=path %>/images/icon/tuijian.png" title="强烈推荐(R)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'M' }">
				                     <img src="<%=path %>/images/icon/bibei.png" title="必备(M)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'MA' }">
				                     <img src="<%=path %>/images/icon/youzhebibei.png" title="有则必备(MA)"></img>
				                 </c:if>
				                 <c:if test="${vc.repoRestrict == 'O' }">
				                     <img src="<%=path %>/images/icon/kexuan.png" title="可选(O)"></img>
				                 </c:if>
			                 </a>
				        </c:otherwise>
				      </c:choose>
		           </td>
		         </tr>
		         </c:if>
	         </c:forEach>
	       </c:if>
       </c:forEach>
       </table>

	 	<div class="title" style="margin-top: 0">选择分类</div>
	     <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	       <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img>
	              学科分类：
	          </td>
	          <td>
	              <select name="rsty_id" id="rsty_id" style="width: 300px;margin-left: 2px;height: 22px;">
		              ${requestScope.subjectOptions }
		          </select>


		      </td>
	       </tr>

	       <!-- 自定义分类一 -->
	       <c:if test="${fn:length(dynamicTypeOptions) > 4 }">
		       <tr bgcolor="#FFFFFF" height="25">
		          <td width="250px" align="right">
		            <img src="<%=path %>/images/extform/bullet_add_2.png"></img>
		              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE) %>：
		          </td>
		          <td>
		            <select name="rdty_id" id="rdty_id" style="width: 300px;margin-left: 2px;height: 22px;">
		              ${requestScope.dynamicTypeOptions }
		            </select>
		          </td>
		       </tr>
	       </c:if>

	       <!-- 自定义分类二 -->
	       <c:if test="${fn:length(dynamicTypeOptions2) > 4 }">
		       <tr bgcolor="#FFFFFF" height="25">
		          <td width="250px" align="right">
		            <img src="<%=path %>/images/extform/bullet_add_2.png"></img>
		              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO) %>：
		          </td>
		          <td>
		            <select name="rdty_id_two" id="rdty_id_two" style="width: 300px;margin-left: 2px;height: 22px;">
		              ${requestScope.dynamicTypeOptions2 }
		            </select>
		          </td>
		       </tr>
	       </c:if>

	       <!-- 自定义分类三 -->
	       <c:if test="${fn:length(dynamicTypeOptions3) > 4 }">
		       <tr bgcolor="#FFFFFF" height="25">
		          <td width="250px" align="right">
		            <img src="<%=path %>/images/extform/bullet_add_2.png"></img>
		              <%=ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE) %>：
		          </td>
		          <td>
		            <select name="rdty_id_three" id="rdty_id_three" style="width: 300px;margin-left: 2px;height: 22px;">
		              ${requestScope.dynamicTypeOptions3 }
		            </select>
		          </td>
		       </tr>
	       </c:if>

	       <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	             <img src="<%=path %>/images/extform/bullet_add_2.png"></img>
	               语种：
	          </td>
	          <td>
	               <span style="color: #808080;padding-left: 2px;">${requestScope.language.rltyName }</span>
	          </td>
	       </tr>
	       <tr bgcolor="#FAFAFA" height="25">
		       	<td width="250px" align="right">
		       		<img src="<%=path %>/images/extform/bullet_add_2.png"></img>
		       		主题：
		       	</td>
		       	<td><select name="rsdaRssuId" id="rsdaRssuId" sourceElement="true" style="width: 300px;margin-left: 2px;height: 22px;">
					    ${requestScope.sourceSubjectOptions }
					</select></td>
	       </tr>
	 </table>

	 <div class="title" style="margin-top: 0">上传文件</div>
	     <table cellpadding="0" width="100%" border="0" cellspacing="1" bgcolor="#ECECEC" >
	        <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img>
	               封 面：
	             <input type="hidden" name="bigimg_statu" id="bigimg_statu" value="0">
	          </td>
	          <td id="imageFileArea">
	               <%if(ToolUnit.NullToBlank(rsd.getRsdaBigimgName()).length() > 3){ %>
	                  <div id="bigimgArea">
	                     <%=ToolUnit.getFileName(ToolUnit.NullToBlank(rsd.getRsdaBigimgName())) %>
	                     <a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&sourceId=<%=rsd.getRsdaId() %>&type=1" target="_blank">预览</a>
	                     <a href="javascript:delefile('bigimgArea','bigimg');">删除</a>
	                  </div>
	                  <input type="file" name="bigimg" id="bigimg" class="file text" style="display: none" onchange="fileChang('bigimg',true);" />
	               <%}else{ %>
	                  <input type="file" name="bigimg" id="bigimg" class="file text" onchange="fileChang('bigimg',true);" />
	                  &nbsp;<span style='color: #808080'>封面文件格式:jpg、jpeg、bmp、png、gif</span>
	               <%} %>
	          </td>
	        </tr>

	        <tr bgcolor="#FAFAFA" height="25">
	          <td width="250px" align="right">
	            <img src="<%=path %>/images/extform/bullet_add_2.png"></img>
	               源 文 件：
	          </td>
	          <td>
	               <%if(ToolUnit.NullToBlank(rsd.getRsdaFileName()).length() > 3){ %>
	                  <div id="loadfileArea">
		                  <%=ToolUnit.getFileName(ToolUnit.NullToBlank(rsd.getRsdaFileName())) %>

		                  <a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&sourceId=<%=rsd.getRsdaId() %>&type=3" target="_blank">预览</a>
		                  <a href="javascript:deleLoadFile();">删除</a>
	                  </div>

	                  <div id="testAA" style="display: none;">
	                   <input type="file" name="loadfile" class="file text" onchange="fileChang('loadfile');" value="test">
	                  </div>
	               <%}else{ %>
	                  <input type="file" name="loadfile" class="file text" statu="0" onchange="fileChang('loadfile');">
	               <%} %>
	               <a id="loadfile_msg"></a>
	               <input type="hidden" name="loadfile_statu" id="loadfile_statu" value="0">
	          </td>
	        </tr>
	     </table>

	 	 <div class="title" style="margin-top: 0;padding-left: 5px;">审核资源</div>
	 	 <br/>
	 	 	<table cellpadding="0" width="100%" border="0" cellspacing="0" bgcolor="#ECECEC" >
	 	 	   <tr bgcolor="#FFFFFF" height="25">
	 	 	   		<td width="250px">&nbsp;</td>
	 	 	   		<td style="padding-top: 5px;">
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
	 	 <br/>

	  <!-- 资源保存成功的图片提示 -->
      <div id="SuccessMessageBox" class="SuccessMessageBox"></div>
      ${result}
 	 </form>
  </body>
</html>
