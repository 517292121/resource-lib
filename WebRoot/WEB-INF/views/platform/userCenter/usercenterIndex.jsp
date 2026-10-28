<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<c:set var="memberSection" value="settings" />
<%@ include file="memberCenterStart.jspf" %>
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
<main class="annotation-records-layout">
    <%@ include file="memberCenterNav.jspf" %>
    <section class="annotation-records-main">
        <div class="annotation-records-heading"><h1><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "个人设置") %></h1></div>
        <form class="member-settings-form" action="<%=basePath%>updateUserinfo.htm" method="post" id="userinfoForm" enctype="multipart/form-data" onsubmit="jsSubmit();return false;">
            <input name="suinId" type="hidden" value="<c:out value='${sysUserinfo.suinId}'/>" />
            <fieldset><legend><fmt:message key="user.account.information" /></legend>
                <div class="member-field"><span class="member-field-label"><fmt:message key="user.name" /></span><div class="member-field-control"><span class="member-readonly"><c:out value="${sysUserinfo.suinLoginName}" /></span></div></div>
                <div class="member-field"><span class="member-field-label"><fmt:message key="user.password" /></span><div class="member-field-control"><button type="button" onclick="updatePwd()" class="userinfo_update_down" id="userinfo_update_pwd"><fmt:message key="user.change.the.password" /></button><input type="hidden" id="pwdtype" value="0" /></div></div>
                <div class="member-password-fields" style="display:none" id="updatepwd">
                    <div class="member-field"><label for="old_password"><fmt:message key="user.oid.cipher" /></label><div class="member-field-control"><input type="password" id="old_password" maxlength="16" autocomplete="current-password" /><span class="member-form-error"><c:out value="${error}" /></span></div></div>
                    <div class="member-field"><label for="update_password"><fmt:message key="user.new.password" /></label><div class="member-field-control"><input type="password" id="update_password" maxlength="16" autocomplete="new-password" /><small class="member-form-hint"><fmt:message key="user.pass.number" /></small></div></div>
                    <div class="member-field"><label for="update_password2"><fmt:message key="user.confirm.the.password" /></label><div class="member-field-control"><input type="password" id="update_password2" maxlength="16" autocomplete="new-password" /></div></div>
                </div>
                <input type="hidden" id="old_password_md5" name="old_password" value="" />
                <input type="hidden" id="update_password_md5" name="update_password" value="" />
            </fieldset>
            <fieldset><legend><fmt:message key="user.my.head" /></legend>
                <div class="member-field"><label for="upload"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "头像") %></label><div class="member-field-control">
                    <c:choose><c:when test="${not empty sysUserinfo.suinImageUrl}"><img class="member-avatar" src="<%=SysConfigInfo.FILE_URL_PATH%><c:out value='${sysUserinfo.suinImageUrl}'/>" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "个人头像") %>" onerror="this.onerror=null;this.src='<%=basePath%>images/platform/face.jpg'" /></c:when><c:otherwise><img class="member-avatar" src="<%=basePath%>images/platform/face.jpg" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "个人头像") %>" /></c:otherwise></c:choose><br />
                    <input id="upload" type="file" name="suinImage" accept=".jpg,.jpeg,.bmp,.png,.gif" />
                    <small class="member-form-hint"><fmt:message key="user.suggest.uploading" /><br /><fmt:message key="user.allowed.upload.type" />（<%=ConstantsObj.SUIN_IMAGE_TYPE%>），<fmt:message key="user.cannot.exceed" /><%=ConstantsObj.SUIN_IMAGE_SIZE%>MB。</small>
                    <span class="member-form-error"><c:out value="${suinImageLocalNameError}" /></span>
                </div></div>
            </fieldset>
            <fieldset><legend><fmt:message key="user.essential.information" /></legend>
                <div class="member-field"><label for="member-name"><fmt:message key="user.real.name" /><span class="member-required">*</span></label><div class="member-field-control"><input id="member-name" name="suinName" type="text" maxlength="26" value="<c:out value='${sysUserinfo.suinName}'/>" autocomplete="name" /><span class="member-form-error"><c:out value="${suinNameError}" /></span></div></div>
                <div class="member-field"><label for="member-email"><fmt:message key="user.mailbox" /><span class="member-required">*</span></label><div class="member-field-control"><input id="member-email" name="suinEmail" type="email" value="<c:out value='${sysUserinfo.suinEmail}'/>" autocomplete="email" /><span class="member-form-error"><c:out value="${suinEmailError}" /></span></div></div>
                <div class="member-field"><span class="member-field-label"><fmt:message key="user.gender" /><span class="member-required">*</span></span><div class="member-field-control"><div class="member-radio-options" role="group" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "性别") %>"><label><input name="suinSex" type="radio" value="1" <c:if test="${empty sysUserinfo.suinSex or sysUserinfo.suinSex eq 1}">checked</c:if> /><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "男") %></label><label><input name="suinSex" type="radio" value="2" <c:if test="${sysUserinfo.suinSex eq 2}">checked</c:if> /><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "女") %></label></div><span class="member-form-error"><c:out value="${suinSexError}" /></span></div></div>
                <div class="member-field"><label for="member-birthday"><fmt:message key="user.birthday" /></label><div class="member-field-control"><input id="member-birthday" name="suinBirthday" type="text" placeholder="yyyy-mm-dd" value="<fmt:formatDate pattern='yyyy-MM-dd' value='${sysUserinfo.suinBirthday}'/>" autocomplete="bday" /><small class="member-form-hint"><fmt:message key="edit.format" />：yyyy-mm-dd</small></div></div>
                <div class="member-field"><label for="member-phone"><fmt:message key="user.contact.information" /></label><div class="member-field-control"><input id="member-phone" name="suinMobileTelephone" type="tel" maxlength="16" value="<c:out value='${sysUserinfo.suinMobileTelephone}'/>" autocomplete="tel" /><span class="member-form-error"><c:out value="${suinMobileTelephoneError}" /></span></div></div>
                <div class="member-field"><span class="member-field-label"><fmt:message key="user.last.login.time" /></span><div class="member-field-control"><span class="member-readonly"><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${sysUserinfo.suinLastvisitDate}" /></span></div></div>
            </fieldset>
            <div class="member-form-actions"><button type="submit" class="member-save"><fmt:message key="edit.preservation" /></button></div>
        </form>
        <c:if test="${not empty error}"><script>showPwd();</script></c:if>
    </section>
</main>
<%@ include file="memberCenterEnd.jspf" %>
