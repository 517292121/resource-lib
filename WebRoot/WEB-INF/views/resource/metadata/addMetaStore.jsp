<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="cn.com.adtech.riss2.domain.SysManager"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResSourceData"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>敦煌研究数据库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link type="text/css" href="<c:url value='/css/addMetaStore.css'/>" rel="stylesheet"/>

	<style type="text/css">
		*{margin:0;padding:0;font-size:13px;vertical-align:middle;}
		li{list-style:none;}

		#tab1,#tab2{margin:0 auto;padding:0 2px;font-size:13px;height:23px;*height:10px;background:#dfe8f6;}
		#tab1 li,#tab2 li{float:left;font-size:12px;color:#416aa3;display:block;width:64px;height:16px;margin-right:5px;padding-top:7px;*padding-top:3px;text-decoration:none;text-align:center;background:url(<%=path %>/images/console/res_meta_link.jpg);cursor:pointer;}
		#tab1 li.now,#tab2 li.now{font-size:12px;color:#15428b;display:block;float:left;width:64px;height:16px;margin-right:5px;font-weight:700;padding-top:7px;*padding-top:3px;text-align:center;background:url(<%=path %>/images/console/res_meta_hover.jpg);}
		.tablist{border:#8db2e3 solid;border-width:1px 0;background:#dfe8f6;height:270px;display:none; overflow:auto;}
		.tablist td{padding:3px 0;}
		.btnbg{ padding:5px 0;background:#dfe8f6;text-align:center;}
		.block{display:block; }
		.store_button{ background:url(<%=path %>/images/console/res_store_btn.png) no-repeat; width:73px;height:21px;}
		.inputinfo{width:273px;}
		.tableline{border-collapse:collapse;border:solid #8db2e3;border-width:1px 0 0 1px;}
		.tableline td{border:solid #8db2e3;border-width:0 1px 1px 0;height:25px;}
		.tablist a{text-decoration: none; color: Black;}
		.tablist a:hover{text-decoration: none; color: Black;}
		.grouparea{height: 90px;padding: 5px 0px 0px 7px;}
		.grouparea p{height: 25px;}
		.picarea{height: 122px;	padding-top: 3px;}
		.picBorder{border: 1px solid #e8e8e8;padding: 2px;width: 140px;height: 70px;margin-bottom: 4px;background-color: #ffffff;}
		.picBorder img{width: 100px;height: 50px;}
	</style>

	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
  </head>
  	<script type="text/javascript">
  	/**检查上传封面文件有文件格式**/
	function checkImageFileType(fileItem){
		  var filetag = new Array();  //允许的文件格式
	      filetag[0] = "jpg";
	      filetag[1] = "jpeg";
	      filetag[2] = "bmp";
	      filetag[3] = "png";
	      filetag[4] = "gif";
	      var result = false;
		  var filepath=fileItem.value;
		  if(filepath)filepath=filepath.substring(filepath.lastIndexOf('.')+1,filepath.length).toLowerCase();
	      for(i=0; i<filetag.length; i++){
	           if(filetag[i].toLowerCase() == filepath){
	               result = true;
	               break;
	           }
	      }
	      if(result == false){
			  document.getElementById("imageFileArea").innerHTML = "<input type='file' id='rmstLogo' name=\"rmstLogo\" style=\"display: none;\" onchange=\"checkImageFileType(this)\">";
			  alert("封面格式只能为JPG、JPEG、GIF、BMP、PNG。请重新选择！");
	      }
	}

  	function IsDigit()
	{
	   return ((event.keyCode >= 48) && (event.keyCode <= 57));
	}



  		// ----------------------------------  滑动条 begin ----------------------
  		// 页面中的滑动tab效果
		function setTab(m, n) {
			var menu = document.getElementById("tab" + m).getElementsByTagName("li");
			var div = document.getElementById("tablist" + m).getElementsByTagName("div");
			var showdiv = [];
			for(i = 0; j = div[i]; i++) {
				if((" " + div[i].className + " ").indexOf(" tablist ") != -1) {
					showdiv.push(div[i]);
				}
			}
			for(i = 0; i < menu.length; i++) {
				menu[i].className = i == n ? "now" : "";
				showdiv[i].style.display = i == n ? "block" : "none";
			}
		}
  		// ----------------------------------  滑动条 end ------------------------
		var limitLength = <%=ConstantsObj.DEFAULT_PAGE_SIZE%>;
		var pagingDisplayMsg = '<%=ConstantsObj.PAGING_TOOLBAR_DISPLAY_MSG%>';
		function getPath() {
			return '<%=path%>/';
		}
		var error = '${error}';
		var msg = '${msg}';
		var save = '${save}';
		var edit = '${edit}';
		// ------------------------------------ begin  ---------------------------
		var checkForm;
		var cancelForm;
		Ext.onReady(function() {
			Ext.QuickTips.init();
			Ext.form.Field.prototype.msgTarget = 'side';
			// 提示错误信息
			if(error) {
				alertError(error);
			}
			// 提示成功信息
			if(msg) {
				if(save)
					window.parent.alertMetaWarning('save');
				if(edit)
					window.parent.alertMetaWarning('edit');
				window.parent.store.load({
					params : {
						start : 0,
						limit : limitLength
					}
				});
			}
			cancelForm = function() {
				if(window.parent.addMetaStore_Win) window.parent.addMetaStore_Win.hide();
				if(window.parent.editMetaStore_Win) window.parent.editMetaStore_Win.hide();
			}
		});

			checkForm = function() {
				var rmstName = document.getElementById('rmstName').value;
				var rmstCompany = document.getElementById('rmstCompany').value;
				var rmstStatus = document.getElementById('rmstStatus');
				var hidStatus = document.getElementById("_rmstStatus");
				var rmstGroupOne = document.getElementById("rmstGroupOne").value;
				var rmstGroupTwo = document.getElementById("rmstGroupTwo").value;
				var rmstGroupThree = document.getElementById("rmstGroupThree").value;

				if(rmstStatus.checked){

					hidStatus.value=1;
				}else{
					hidStatus.value=0;
				}
				var rmstCmtstus = document.getElementById('rmstCmtstus');
				var hidCmtstus = document.getElementById("_rmstCmtstus");
				if(rmstCmtstus.checked){

					hidCmtstus.value=1;
				}else{
					hidCmtstus.value=0;
				}

				var rmstIsUploadResource = document.getElementById('rmstIsUploadResource');
				var hidIsUploadResource = document.getElementById("_rmstIsUploadResource");
				if(rmstIsUploadResource.checked){

					hidIsUploadResource.value=1;
				}else{
					hidIsUploadResource.value=0;
				}


				var rmstRemark = document.getElementById('rmstRemark').value;
				var rltyIds = getSelectCount('form1', 'rltyId');
				//var smanIds = getSelectCount('form1', 'smanId');
				if(rmstName == null || rmstName.trim().length < 1) {
					alertWarring("特色库名称不能为空！");
					return false;
				}
				if(rmstName.trim().length > 40) {
					alertWarring("特色库名称不得超过40个字符！");
					return false;
				}
				if(rmstCompany == null || rmstCompany.trim().length < 1) {
					alertWarring("特色库建设单位不能为空！");
					return false;
				}
				if(rmstCompany.trim().length > 40) {
					alertWarring("特色库建设单位不得超过40个字符！");
					return false;
				}
				if(rmstGroupOne.length < 1){
					alertWarring("请输入自定义分类一的分类名称！");
					return false;
				}
				if(rmstGroupTwo.length < 1){
					alertWarring("请输入自定义分类二的分类名称！");
					return false;
				}
				if(rmstGroupThree.length < 1){
					alertWarring("请输入自定义分类三的分类名称！");
					return false;
				}
				if(rmstRemark && rmstRemark.trim().length > 350) {
					alertWarring("特色库描述不得超过350个字符！");
					return false;
				}
				if(rltyIds < 1) {
					alertWarring("资源类型不能为空！");
					return false;
				}
				// 验证是否勾选了权限复选框

				if(!checkRights()) {
					alertWarring("权限类型不能为空！");
					return false;
				}

				return true;
			}
			// 验证权限类型是否为空
			function checkRights(){
				var count = 0;
				<%
				List sysManagerList = (List)request.getAttribute("sysManagerList");
				for(int j=0;j<sysManagerList.size();j++){
					SysManager sma = (SysManager)sysManagerList.get(j);
					%>
						var smanIds = getSelectCount('form1', 'riss2<%=sma.getSmanId()%>');
						if(smanIds > 0){
							count++;
						}
					<%
				}
				%>
				if(count>0){
					return true;
				}
				return false;
			}

			// 返回被选中的记录数
			function getSelectCount(formName, cbName) {
				var count = 0;
				var cbx = document.forms[formName].elements[cbName];
				if(cbx.length) {
					for(var i = 0; i < cbx.length; i++) {
						if(cbx[i].checked) {
							count++;
						}
					}
				}
				return count;
			}

		// ------------------------------------ JS 文件 内容 end ---------------------------

	</script>
	<script type="text/javascript" language="javascript" src="<c:url value='/viewsjs/resource/metadata/addMetaStore.js' />"></script>
  <body>
  <div style="width:100%; height:100%; background:#dfe8f6;">
	  <form id="form1" name="form1" method="post" action="<%=path %>${url}"  enctype="multipart/form-data"  onsubmit="return checkForm();">
	 <div id="tab2">
	  <ul>
	    <li onclick="setTab(2,0)" class="now">基本信息</li>
	    <li onclick="setTab(2,1)">资源类型</li>
	    <li onclick="setTab(2,2)">管理员</li>
	  </ul>
	</div>
   <div id="tablist2">
	  <div class="tablist block" style="text-align:center;padding-top: 7px;">
	    <table border="0" cellpadding="0" cellspacing="0" width="100%">
	     <tr>
	      <td width="335px;" style="padding-right: 6px;padding-left: 5px;">
	      		<fieldset style="height:260;border:1px #B9B7C4 solid;">
				<legend><b style="color: #104589;">基本信息</b></legend>
				  	<table style="margin: auto;">
				  		<tr>
				  			<td align="right"><span style="color:red;">*</span>名称：</td>
				  			<td>
				  				<input type="hidden" id="rmstId" name="rmstId" value="${resMetaStore.rmstId}"/>
				  				<input type="text" id="rmstName" name="rmstName" class="text" value="${resMetaStore.rmstName}" style="width: 250px;"/>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td align="right"><span style="color:red;">*</span>建设单位：</td>
				  			<td><input type="text" id="rmstCompany" name="rmstCompany" class="text" value="${resMetaStore.rmstCompany}" style="width: 250px;"/></td>
				  		</tr>
				  		<tr>
				  			<td align="right" style="padding-top: 4px;">文件服务器：</td>
				  			<td>
				  				<select name="fileServiceId" style="width: 120px;">
				  					${serverList}
				  				</select>
				  				&nbsp; 显示序号：
				  				<input id="rmstIndexNo" class="text" ONKEYPRESS="event.returnValue=IsDigit();" name="rmstIndexNo" style="width: 52px;" value="${resMetaStore.rmstIndexSort}" title="特色库在前台显示的顺序号，序号越小显示在越前面" />
				  			</td>
				  		</tr>
				  		<tr>
				  			<td align="right" style="padding-top: 4px;">状态设置：</td>
				  			<td>
				  			<c:if test="${resMetaStore.rmstStatus == null}">
				  				<input type="checkbox" id="rmstStatus" checked="checked" />是否可用
				  			</c:if>
				  			<c:if test="${resMetaStore.rmstStatus == 1 }">
				  				<input type="checkbox" id="rmstStatus" checked="checked" />是否可用
				  			</c:if>
				  			<c:if test="${resMetaStore.rmstStatus == 0 }">是否可用
				  				<input type="checkbox" id="rmstStatus"/>
				  			</c:if>
				  			<input type="hidden" name="rmstStatus" id="_rmstStatus" />

				  			<c:if test="${resMetaStore.rmstCmtstus == null}">
				  				<input type="checkbox" id="rmstCmtstus" checked="checked" />评论开关
				  			</c:if>
				  			<c:if test="${resMetaStore.rmstCmtstus == 1 }">
				  				<input type="checkbox" id="rmstCmtstus" checked="checked" />评论开关
				  			</c:if>
				  			<c:if test="${resMetaStore.rmstCmtstus == 0 }">
				  				<input type="checkbox" id="rmstCmtstus" />评论开关
				  			</c:if>
				  			<input type="hidden" name="rmstCmtstus" id="_rmstCmtstus" />

				  			<c:if test="${resMetaStore.rmstIsUploadResource == null}">
				  				<input type="checkbox" id="rmstIsUploadResource"  checked="checked"/>用户上传开关
				  			</c:if>
				  			<c:if test="${resMetaStore.rmstIsUploadResource == 1 }">
				  				<input type="checkbox" id="rmstIsUploadResource" checked="checked" />用户上传开关
				  			</c:if>
				  			<c:if test="${resMetaStore.rmstIsUploadResource == 0 }">
				  				<input type="checkbox" id="rmstIsUploadResource" />用户上传开关
				  			</c:if>
				  			<input type="hidden" name="rmstIsUploadResource" id="_rmstIsUploadResource" />
				  			</td>
				  		</tr>
				  		<tr>
				  			<td align="right" valign="top" style="padding-top: 5px;">描述：</td>
				  			<td><textarea  id="rmstRemark" class="text" name="rmstRemark" style="width: 250px;height:55px;" >${resMetaStore.rmstRemark}</textarea></td>
				  		</tr>
				  		<tr>
				  			<td align="right">资源状态：</td>
				  			<td>
				  				<input type="radio" name="resourceAuditStatu" id="audit_waiting" value="<%=ResSourceData.STATUS_CHECK %>" <c:if test="${empty resMetaStore.rmstResourceAudit or resMetaStore.rmstResourceAudit == 2 }">checked="checked"</c:if>><label for="audit_waiting">待审核</label> &nbsp;
				  				<input type="radio" name="resourceAuditStatu" id="audit_pass" value="<%=ResSourceData.STATUS_PASS %>" <c:if test="${resMetaStore.rmstResourceAudit == 3 }">checked="checked"</c:if>><label for="audit_pass">审核通过</label>
				  			</td>
				  		</tr>
				  		<tr>
				  			<td align="right">资源语种：</td>
				  			<td>
				  				<input type="radio" name="rmstLanguage" id="lan_zh" value="zh" <c:if test="${empty resMetaStore.rmstLanguage or resMetaStore.rmstLanguage == 'zh' }">checked="checked"</c:if>><label for="lan_zh">中文</label> &nbsp;
				  				<input type="radio" name="rmstLanguage" id="lan_zw" value="zw" <c:if test="${resMetaStore.rmstLanguage == 'zw' }">checked="checked"</c:if>><label for="lan_zw">藏文</label> &nbsp;
				  				<input type="radio" name="rmstLanguage" id="lan_mw" value="mw" <c:if test="${resMetaStore.rmstLanguage == 'mw' }">checked="checked"</c:if>><label for="lan_mw">蒙文</label> &nbsp;
				  				<input type="radio" name="rmstLanguage" id="lan_en" value="en" <c:if test="${resMetaStore.rmstLanguage == 'en' }">checked="checked"</c:if>><label for="lan_en">英文</label>
				  			</td>
				  		</tr>
				  	</table>
				</fieldset>
	  	</td>

	  	<td valign="top" style="padding-right: 8px;padding-left: 5px;">
	  		<fieldset style="border:1px #B9B7C4 solid;">
	  	    <legend><b style="color: #104589;">封面(980*410)</b></legend>
	  	      <div class="picarea" align="center">
	  	      	 <div class="picBorder" style="width:50px;height:65;float:left">
			  	    <a href="<%=path %>/console/data/resource/resourceManager.htm?action=sourceView&rmstId=${resMetaStore.rmstId }" target="_blank" title="预览">
	  	      	 	<c:if test="${empty resMetaStore.rmstLogo}">
	  	      	 		<img src="<%=path %>/images/nopic.gif" style="width:50px;height:65;" id="viewPic"></img>
	  	      	 	</c:if>
	  	      	 	<c:if test="${! empty resMetaStore.rmstLogo}">
	  	      	 		<img src="<%=SysConfigInfo.FILE_URL_PATH %>${resMetaStore.rmstLogo }" style="width:50px;height:65;" id="viewPic"></img>
	  	      	 	</c:if>
		  	        </a>
	  	      	 </div><br/>
	  	      	 背景色值<input type="text" name="rmstLogoBg"  style="width:65px;" id="rmstLogoBg" value="${resMetaStore.rmstLogoBg }" />
				 <input type="file" name="rmstLogo" class="file txt" style="width:160px;" onchange="checkImageFileType(this)" id="rmstLogo" />
	  	      </div>
	  	    </fieldset>
	  		<fieldset style="border:1px #B9B7C4 solid;margin-top: 7px;">
	  	    <legend><b style="color: #104589;">分类名称</b></legend>
	  	      <div class="grouparea">
	  	      	 <p><span style="color:red;">*</span>分类一：<input type="text" id="rmstGroupOne" name="rmstGroupOne" class="text" value="${rmstGroupOne}" style="width: 105px;" maxlength="80"></p>
	  	      	 <p><span style="color:red;">*</span>分类二：<input type="text" id="rmstGroupTwo" name="rmstGroupTwo" class="text" value="${rmstGroupTwo}" style="width: 105px;" maxlength="80"></p>
	  	      	 <p><span style="color:red;">*</span>分类三：<input type="text" id="rmstGroupThree" name="rmstGroupThree" class="text" value="${rmstGroupThree}" style="width: 105px;" maxlength="80"></p>
	  	      </div>
	  	    </fieldset>
	  	</td>

	  	</tr></table>
	  </div>

	  <div class="tablist" style="background-color: #ffffff">
	  	<table border="1" cellpadding="0" cellspacing="0" style="border-collapse: collapse;" width="100%" bordercolor="#e8e8e8">
	          	 	<tr class="th">
	          	 		<td width="20">&nbsp;</td>
	          	 		<td width="20" align="center"><input type="checkbox" name="rltyId_chkAll" onclick="checkAll('form1','rltyId','rltyId_chkAll')"/></td>
	          	 		<td style="padding-left:5px;">名称</td>
	          	 	</tr>
	          	 	<% int i=0; %>
	          	 	<c:forEach items="${resLiterTypeList}" var="resLiterType">
	          	 		<%i++; %>
	          	 		<tr>
		          	 		<td align="center"><%=i %></td>
		          	 		<td align="center">

			          	 	 <c:if test="${rltyIds[0] != null}">
			          	 	 	<% int k=0; %>
			          	 	 	<c:forEach items="${rltyIds}" var="rltyId">
				          	 		<c:if test="${rltyId == resLiterType.rltyId}">
				          	 			<%k++; %>
				          	 			<input type="checkbox" name="rltyId" value="${resLiterType.rltyId}" checked="checked"/>
				          	 		</c:if>
			          	 		</c:forEach>
			          	 		 <%
			          	 			if(k==0){
			          	 			%>
			          	 				<input type="checkbox" name="rltyId" value="${resLiterType.rltyId}" />
			          	 			<%
			          	 			}
			          	 		 %>
			          	 	 </c:if>
			          	 	 <c:if test="${rltyIds[0] == null}">
			          	 	 	<input type="checkbox" name="rltyId" value="${resLiterType.rltyId}"/>
			          	 	 </c:if>


		          	 		</td>
		          	 		<td style="padding-left:5px;">${resLiterType.rltyName}</td>
		          	 	</tr>
	          	 	</c:forEach>
	          	 </table>

	  </div>
	  <div class="tablist" style="background-color: #ffffff">
	  	<table border="1" cellpadding="0" cellspacing="0" style="border-collapse: collapse;" width="100%" bordercolor="#e8e8e8">
	          	 	<tr class="th">
	          	 		<td width="20">&nbsp;</td>
	          	 		<td width="130" style="padding-left:5px;">登录名</td>
	          	 		<td width="130" style="padding-left:5px;">姓名</td>
	          	 		<td style="padding-left:5px;">权限类型</td>
	          	 	</tr>
	          	 	<% int j=0; %>
	          	 	<c:forEach items="${sysManagerList}" var="sysManager" >
	          	 		<%j++; %>
	          	 		<tr>
		          	 		<td align="center"><%=j %></td>
		          	 		<td style="padding-left:5px;"><a href="javascript:void(0);" title="${sysManager.smanLoginName}">${sysManager.subSuinLoginName}</a></td>
		          	 		<td style="padding-left:5px;"><a href="javascript:void(0);" title="${sysManager.smanName}">${sysManager.smanNameBy10}</a></td>
		          	 		<td style="padding-left:5px;">
		          	 			<c:if test="${sysManager.rightsNumber == null ||sysManager.rightsNumber == 0}">
		          	 				<!-- 创建特色库时默认给当前用户分配管理权限 -->
		          	 				<c:if test="${currentSmanId == sysManager.smanId}">
		          	 					<input type="checkbox" name="riss2${sysManager.smanId}" value="1" checked="checked"/>管理
		          	 				</c:if>
		          	 				<c:if test="${currentSmanId != sysManager.smanId}">
								    	<input type="checkbox" name="riss2${sysManager.smanId}" value="1" />管理
		          	 				</c:if>
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="2" />加工
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="3" />审核
							    </c:if>
		          	 			<c:if test="${sysManager.rightsNumber == 1}">
							    	<input type="checkbox" name="riss2${sysManager.smanId}" value="1" checked="checked"/>管理
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="2" />加工
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="3" />审核
							    </c:if>
		          	 			<c:if test="${sysManager.rightsNumber == 2}">
							    	<input type="checkbox" name="riss2${sysManager.smanId}" value="1" />管理
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="2" checked="checked"/>加工
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="3" />审核
							    </c:if>
		          	 			<c:if test="${sysManager.rightsNumber == 4}">
							    	<input type="checkbox" name="riss2${sysManager.smanId}" value="1" />管理
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="2" />加工
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="3" checked="checked"/>审核
							    </c:if>
		          	 			<c:if test="${sysManager.rightsNumber == 3}">
							    	<input type="checkbox" name="riss2${sysManager.smanId}" value="1" checked="checked"/>管理
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="2" checked="checked"/>加工
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="3" />审核
							    </c:if>
		          	 			<c:if test="${sysManager.rightsNumber == 5}">
							    	<input type="checkbox" name="riss2${sysManager.smanId}" value="1" checked="checked"/>管理
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="2" />加工
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="3" checked="checked"/>审核
							    </c:if>
		          	 			<c:if test="${sysManager.rightsNumber == 6}">
							    	<input type="checkbox" name="riss2${sysManager.smanId}" value="1" />管理
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="2" checked="checked"/>加工
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="3" checked="checked"/>审核
							    </c:if>
		          	 			<c:if test="${sysManager.rightsNumber == 7}">
							    	<input type="checkbox" name="riss2${sysManager.smanId}" value="1" checked="checked"/>管理
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="2" checked="checked"/>加工
								    <input type="checkbox" name="riss2${sysManager.smanId}" value="3" checked="checked"/>审核
							    </c:if>
		          	 		</td>
		          	 	</tr>
	          	 	</c:forEach>
	          	 </table>
	  </div>
	</div>
	<div class="btnbg">
	    <input type="submit" class="store_button" value="保&nbsp;存" style="border: none;" />
	    <input type="button" class="store_button" value="取&nbsp;消" style="border: none;" onclick="cancelForm()"/>
	</div>
	</form></div>
  </body>
</html>
