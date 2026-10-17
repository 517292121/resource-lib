<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="../pc/common/header.jsp"%>
		<script src="<%=basePath%>viewsjs/common/sha1.js"></script>
		<script type="text/javascript">
		// 会员中心 - 基本信息修改
		function UsrRegForm_Validator(theForm) {
			
			if(theForm.suinName.value == "") {
				alert("<fmt:message key='user.real.not.name'/>");
				theForm.suinName.focus();
				return (false);
			}
			if(theForm.suinName.value.length > 26) {
				alert("<fmt:message key='user.real.name.max'/>");
				theForm.suinName.focus();
				return (false);
			}
			
			if(theForm.suinBirthday.value != "") {
				if(checkDate(theForm.suinBirthday.value) == false) {
					alert("<fmt:message key='user.id.invalid'/>");
					theForm.suinBirthday.focus();
					return false;
				}
			}
			if(theForm.suinEmail.value == "") {
				alert("<fmt:message key='user.please.enter.email'/>");
				theForm.suinEmail.focus();
				return (false);
			}
			if(theForm.suinEmail.value != "") {
				if(CheckMail(theForm.suinEmail.value) == false) {
					alert("<fmt:message key='user.email.re.input'/>");
					theForm.suinEmail.focus();
					return (false);
				}
			}
			if(theForm.suinSex.value == "" || theForm.suinSex.value == 0) {
		        alert("<fmt:message key='user.sex'/>");
		        theForm.suinSex.focus();
		        return (false);
		      }
			if(theForm.suinMobileTelephone.value != "") {
				if(checkPhone(theForm.suinMobileTelephone.value) == false) {
					alert("<fmt:message key='user.phone.re.input'/>");
					theForm.suinMobileTelephone.focus();
					return (false);
				}
			}
			return (true);
		}
		function CheckMail(email) {
			var pattern = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		
			if(pattern.test(email)) {
				return true;
			}
		
			else {
				return false;
			}
		
		}
		function checkPhone(phone) {
			var pattern = /^(\d{11})|^((\d{7,8})|(\d{4}|\d{3})-(\d{7,8})|(\d{4}|\d{3})-(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1})|(\d{7,8})-(\d{4}|\d{3}|\d{2}|\d{1}))$/;
			if(pattern.test(phone))
				return true;
			else
				return false;
		
		}
		function checkDate(date) {
			var pattern = /(((19|20)\d{2})\-(0{0,1}[1-9]|1[0-2])\-(0{0,1}[1-9]|[12][0-9]|3[01]))$/;
			return pattern.test(date);
		}
		
		function jsSubmit(){
			var form = document.getElementById('userinfoForm');
			if(checkPasswordForm() && UsrRegForm_Validator(form)){
				var old_password = document.getElementById('old_password').value;
			 	var update_password = document.getElementById('update_password').value;
			 	if(old_password.trim().length > 0 && update_password.trim().length > 0){
					document.getElementById('old_password_md5').value = hex_sha1(old_password.trim());
					document.getElementById('update_password_md5').value = hex_sha1(update_password.trim());
			 	}
				form.submit();
			}
		}
		
		// 隐蔽密码表单
		function updatePwd(){
			var pwdtype = document.getElementById('pwdtype');
			if(pwdtype && pwdtype.value == 0){
				document.getElementById('updatepwd').style.display = 'block';
				pwdtype.value = '1';
				document.getElementById('userinfo_update_pwd').className = 'userinfo_update_up';
			}else{
				document.getElementById('updatepwd').style.display = 'none';
				document.getElementById('userinfo_update_pwd').className = 'userinfo_update_down';
				pwdtype.value = '0';
			}
		}
		
		function showPwd(){
           var pwdtype = document.getElementById('pwdtype');
		   if(pwdtype ){
				document.getElementById('updatepwd').style.display = 'block';
				pwdtype.value = '1';
				document.getElementById('userinfo_update_pwd').className = 'userinfo_update_up';
			}
         }		
		            		
		// 验证密码框:当三个密码输入框都为空时，则不验证
		function checkPasswordForm(){
			var old_password = document.getElementById('old_password').value;
			var update_password = document.getElementById('update_password').value;
			var update_password2 = document.getElementById('update_password2').value;
			
			
			if(old_password.trim().length > 0 || update_password.trim().length > 0 || update_password2.trim().length > 0){
				if(old_password.trim().length < 1){
					var info_1 = '<fmt:message key='user.password.not.null'/>';
					alert(info_1);
					return false;
				}else if(update_password.trim().length < 1){
					var info_2 = "<fmt:message key='user.new.password.not.null'/>";
					alert(info_2);
					return false;
				}else if(update_password.trim().length < 6 || update_password.trim().length > 16){
					var info_3 = "<fmt:message key='user.pass.number'/>";
					alert(info_3);
					return false;
				}else if(update_password.trim().length > 16){
					var info_4 = "<fmt:message key='user.new.pass.max'/>";
					alert(info_4);
					return false;
				}else if(update_password.trim() != update_password2.trim()){
					var info_5 = "<fmt:message key='user.confirm.pass.incorrect'/>";
					alert(info_5);
					return false;
				}	
			}
			return true;
		}
		</script>
		
		<!-- 会员中心中间 ============== 开始 ============== -->
		<div class="p-cnt ucdiv shadow" >
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b><fmt:message key='edit.current.location'/>：
		  				<a href="<%=path %>/index.htm" class="navigation"><fmt:message key='home.page'/></a> >> 
		  				<a href="<%=path %>/usercenter/infoSubscription.htm?action=showUpdate&display=block" class="navigation"><fmt:message key='addPage.membership.space'/></a> >> 
		  				<fmt:message key='left.personal.settings'/>
		  			</b></div>
			
			<form action="<%=path %>/updateUserinfo.htm" method="post" id="userinfoForm" enctype="multipart/form-data">
	            <div class="update" style="background-color: #FFFFFF; padding-top: 20px;">
		        	
		        	<span class="cjzh"><h4 class="n1"><fmt:message key='user.account.information'/></h4></span>
		            <table  cellpadding="0" cellspacing="5">
		               <tbody>
		               		<tr>
		                        <td class="td1"><fmt:message key='user.name'/>：&nbsp;</td>
		                        <td class="td2" align="left" >${sysUserinfo.suinLoginName }<input name="suinId" type="hidden" value="${sysUserinfo.suinId }"/></td>
		                        <td class="td3"></td>
		                    </tr>
		        			<tr>
		                        <td class="td1"><fmt:message key='user.password'/>：&nbsp;</td>
		                        <td class="td2" align="left">
		                       		<a href="javascript:updatePwd()" class="userinfo_update_down" id="userinfo_update_pwd"><fmt:message key='user.change.the.password'/></a>
		                       		<input type="hidden" id="pwdtype" value="0"/>
		                        </td>
		                        <td class="td3" align="left"></td>
		                     </tr>
		                 </tbody>
		            </table>
		            
		            <table cellpadding="0" cellspacing="5" style="width: 100%" border="0" style="display: none;" id="updatepwd">
		               <tbody>
		        			<tr>
		                        <td class="td1">
		                        	<fmt:message key='user.oid.cipher'/>：&nbsp;
		                        </td>
		                        <td class="td2"><input class="zckk" type="password"  id="old_password" maxlength="16" value=""></input></td>
		                        <td class="td3" align="left"><span class="update_2">*${error }</span></td>
		                     </tr>
		                     <tr>
		                        <td class="td1">
		                        	<fmt:message key='user.new.password'/>：&nbsp;
		                        </td>
		                        <td class="td2" id="reg_password_td">
		                        	<input class="zckk" type="password" id="update_password" value="" maxlength="16"></input>
		                        	<input type="hidden" id="old_password_md5" name="old_password" value=""></input>	
		                        	<input type="hidden" id="update_password_md5" name="update_password" value=""></input>
		                        </td>
		                        <td class="td3" align="left"><span class="update_2">*</span><fmt:message key='user.pass.number'/></td>
		                     <tr>
		                        <td class="td1">
		                        	<fmt:message key='user.confirm.the.password'/>：&nbsp;
		                        </td>
		                        <td class="td2" align="left"><input class="zckk" type="password" id="update_password2"  value="" maxlength="16"></input></td>
		                        <td class="td3" align="left"><span class="update_2">*</span></td>
		                     </tr>
		                 </tbody>
		            </table>
		            
		            <c:if test="${!empty error}">
		            	<script type="text/javascript">
		            		showPwd();
		            	</script>
		            </c:if>
		            
		            <span class="cjzh"><h4 class="n2"><fmt:message key='user.my.head'/></h4></span>
		            <table  cellpadding="0" cellspacing="5">
		               <tbody>
		        			<tr>
								<td style=" float: left; width: 85px; padding: 5px 0px 0px 80px">
							
									<c:choose>
										<c:when test="${!empty sysUserinfo.suinImageUrl}">  
											<img width="70" height="70" class="tx" src="<%=SysConfigInfo.FILE_URL_PATH %>${sysUserinfo.suinImageUrl}" onerror="this.src='<%=path %>/images/platform/face.jpg'"/>
										</c:when>
										<c:otherwise> 
											<img width="70" height="70" class="tx" src="<%=path %>/images/platform/face.jpg" />
										</c:otherwise>
									</c:choose>
								</td>
								<td align="left" class=" ts" colspan="2">
									<p style="line-height: 180%"><fmt:message key='user.suggest.uploading'/></p>
									<p style="line-height: 180%"><fmt:message key='user.allowed.upload.type'/>（<%=ConstantsObj.SUIN_IMAGE_TYPE %>）,<fmt:message key='user.cannot.exceed'/><%=ConstantsObj.SUIN_IMAGE_SIZE %>MB。</p>							
		            				<div class="upfile" style="float:left; padding:5px 0">
		            					 <span style="float:left"> 
											<input id="viewfile" class="zckk" type="text"  style="border:1px solid #DFDFDF; z-index:99"/> 
										</span>
										<span style="float:left;position:relative; ">
											<label class="upfile1"><img style="margin:5px 3px 0px 0px" src="<%=path %>/images/platform/upload.gif"/><fmt:message key='user.select.upload'/></label> 
											<input id="upload" type="file" name="suinImage" onchange="document.getElementById('viewfile').value=this.value;" class="upfile2" /> 
										</span> 
									</div> 
		            				<span class="update_2">${suinImageLocalNameError}</span>
								</td>
							</tr>
		                 </tbody>
		            </table>
		            
		            
		            
		            <span class="cjzh"><h4 class="n3"><fmt:message key='user.essential.information'/></h4></span>
		            <table  cellpadding="0" cellspacing="5">
		               <tbody>
		               		<tr>
		                        <td class="td1"><fmt:message key='user.real.name'/>：&nbsp;</td>
		                        <td class="td2" align="left"><input class="zckk" name="suinName" type="text" value="${sysUserinfo.suinName }"/></td>
		                        <td class="td3" align="left"><span class="update_2">*${suinNameError}</span></td>
		                    </tr>
		                    <tr>
		                        <td class="td1"><fmt:message key='user.mailbox'/>：&nbsp;</td>
		                        <td class="td2" align="left"><input class="zckk" name="suinEmail" type="text" value="${sysUserinfo.suinEmail }"/></td>
		                        <td class="td3" align="left"><span class="update_2">*${suinEmailError}</span></td>
		                    </tr>
		                    <tr>
		                        <td class="td1"><fmt:message key='user.gender'/>：&nbsp;</td>
		                        <td class="td2" align="left">
		                        	<input name="suinSex"   type="radio" value="1" <c:if test="${empty sysUserinfo.suinSex or sysUserinfo.suinSex eq 1 }">checked="checked"</c:if>> 男&nbsp;&nbsp;&nbsp;&nbsp;
									<input name="suinSex"   type="radio" value="2" <c:if test="${sysUserinfo.suinSex eq 2 }">checked="checked"</c:if>> 女
		                        </td>
		                        <td class="td3" align="left"><span class="update_2">*${suinSexError }</span></td>
		                    </tr>
		                    <tr>
		                        <td class="td1"><fmt:message key='user.birthday'/>：&nbsp;</td>
		                        <td class="td2" align="left"><input class="zckk" name="suinBirthday" type="text" value='<fmt:formatDate pattern="yyyy-MM-dd" value="${sysUserinfo.suinBirthday }"/>'/></td>
		                        <td class="td3" align="left"><span style="color:#8a8989;"><fmt:message key='edit.format'/>:yyyy-mm-dd</span></td>
		                    </tr>
		                    <tr>
		                        <td class="td1"><fmt:message key='user.contact.information'/>：&nbsp;</td>
		                        <td class="td2" align="left"><input class="zckk" name="suinMobileTelephone" type="text" value="${sysUserinfo.suinMobileTelephone }"/></td>
		                        <td class="td3"><span class="update_2">${suinMobileTelephoneError }</span></td>
		                    </tr>
		                    <tr>
		                        <td class="td1"><fmt:message key='user.last.login.time'/>：&nbsp;</td>
		                        <td class="td2" align="left"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${sysUserinfo.suinLastvisitDate }"/></td>
		                        <td class="td3"></td>
		                    </tr>
		                    <tr>
		                        <td class="td1">&nbsp;</td>
		                        <td colspan="2" align="left">
		                        	<a href="javascript:jsSubmit();" class="tbtn cabtn" ><fmt:message key='edit.preservation'/></a>
		                        </td>
		                     </tr>
		                 </tbody>
		            </table>
		        </div>
	            
            </form>
                
			</div> 
            <div class="clear"></div>
		</div>
		<!-- 会员中心中间 ============== 结束 ============== -->

 		<!--底部开始-->
		<div class="clear"></div>
		<script type="text/javascript">
			updatePwd();
			updatePwd();
		</script>
		<%@ include file="../pc/common/footer.jsp"%>
