<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<!-- 会员中心首页头部  -->
	<%@ include file="../pc/common/header.jsp"%>
	<link rel="stylesheet" href="<%=path %>/css/platform/docu.css" type="text/css"></link>
	<script type="text/javascript">
		function checkForm(){
	     	if(document.getElementById("uName").value.length<1){
    			alert("<fmt:message key='create.user.not.name'/>");
    			return false;
    		}
	     	if(document.getElementById("uEmail").value.length<1){
    			alert("<fmt:message key='create.email.cant.be.empty'/>");
    			return false;
    		}
	     	if(document.getElementById("uPhone").value.length<1){
    			alert("<fmt:message key='create.phone.not.null'/>");
    			return false;
    		}
	     	if(document.getElementById("sddeDocuTitle").value.length<1){
    			alert("<fmt:message key='create.title.not.null'/>");
    			return false;
    		}
    		pinjie();
	       	submit('form2');
	     }
		function pinjie(){
			var date ='';
    		var year = document.getElementById("sb-year").value;
    		var juan = document.getElementById("sb-juan").value;
    		var qi = document.getElementById("sb-qi").value;
	     	if(year.length > 0 ){
				date += year +"<fmt:message key='create.year'/>";
    		}
	     	if(juan.length > 0 ){
				date += juan +"<fmt:message key='create.volume'/>";
    		}
	     	if(qi.length > 0 ){
				date += qi +"<fmt:message key='create.stage'/>";
    		}
    		document.getElementById("sddeDocuDate").value = date;
    	
    		var page ='';
    		var start = document.getElementById("sb-start").value;
    		var end = document.getElementById("sb-end").value;
	     	if(start.length > 0 ){
				page += start +"<fmt:message key='create.to'/>";
    		}else{
    			page += "1<fmt:message key='create.to'/>";
    		}
	     	if(end.length > 0 ){
				page += end +"<fmt:message key='create.page'/>";
    		}else{
				page += "<fmt:message key='shadowe'/>";
    		}
    		if(start.length < 1 && end.length < 1 ){
    			page = "<fmt:message key='repeat.all'/>";
    		}
    		document.getElementById("sddeDocuPage").value = page;
		}
		
		function submit(val){
			var form1 = document.getElementById(val);
			if(form1)
				form1.submit();
		}
	</script>
		
		<div class="p-cnt ucdiv shadow" >
		   <div class="index_left">
		   	<%@ include file="usercenterLeft.jsp"%>	
		   </div>
	 		<div class="index_right">
		  		<div class="word_dqwz">
		  			<b><fmt:message key='edit.current.location'/>：
		  			<a href="<%=path %>/index" class="navigation"><fmt:message key='home.page'/></a> >> 
		  			<a href="<%=path %>/usercenter/docudelivery.htm?action=initDocuView" class="navigation">
		  			<fmt:message key='left.document.delivery'/></a>>><fmt:message key='create.document.application.form'/>
		  			</b></div>
				
		        <form action="<c:url value='/usercenter/docudelivery.htm'/>" method="post" id="form2">
		        <input name="action" type="hidden" value="submitDocuTable"/>
				<div class="zx_wx_list"> 
					<div class="docutitle"> 
                   		 <h5 class="navbtn selected"><fmt:message key='create.application.information'/></h5><div class="span_minor">
                   		 <fmt:message key='create.notes.fill.it.out'/><fmt:message key='create.belt'/>&nbsp;<span class="sred">*</span>&nbsp;<fmt:message key='create.number,is.required'/></div>
                    </div>
                    <div class="docuInfo">
	                    <table width="100%" border="0" cellpadding="0" cellspacing="10"> 
	                   	 <tr>
	                        <td align="left" ><fmt:message key='create.surname'/>&nbsp;&nbsp;<fmt:message key='create.name'/><span class="sred">*</span></td> 
	                        <td align="left" ><fmt:message key='create.post'/>&nbsp;&nbsp;<fmt:message key='create.box'/><span class="sred">*</span></td> 
	                        <td align="left" ><fmt:message key='create.electric'/>&nbsp;&nbsp;<fmt:message key='create.word'/><span class="sred">*</span></td> 
	                      </tr>
	                   	 <tr>
	                        <td align="left"><input id="uName" name="uName" class="w220 left" value="${userinfo.suinName }"/></td> 
	                        <td align="left"><input id="uEmail" name="uEmail" class="w220 left" value="${userinfo.suinEmail }"/></td> 
	                        <td align="left"><input id="uPhone" name="uPhone" class="w220 left" value="${userinfo.suinMobileTelephone }"/></td> 
	                      </tr>
	                     </table>
                     </div>
                     
                     <div class="docutitle"> 
                   		 <h5 class="navbtn selected"><fmt:message key='create.literature.information'/></h5><div class="span_minor">
                   		 	<fmt:message key='create.notes'/>：<fmt:message key='create.belt'/> &nbsp;<span class="sred">*</span>&nbsp;<fmt:message key='create.number,is.required'/></div>
                    </div>
                    <div class="docuInfo" style="padding-bottom: 20px;">
                    <table width="100%" border="0" cellpadding="0" cellspacing="10">  
                      <tr>
                        <td align="right" class="t2" style="width:100px"><fmt:message key='create.literature.information'/>：</td> 
                        <td align="left" class="t2"><select name="rltyId">${rltyOptions }</select><span class="sred">*</span></td> 
                      </tr>
                       <tr>
                        <td align="right" class="t2" style="width:100px"><fmt:message key='search.title'/>：</td> 
                        <td align="left" class="t2"><input id="sddeDocuTitle" name="sddeDocuTitle" value="${rsda.rsdaTitle}" /><span class="sred">*</span></td> 
                      </tr>
                   	 <tr>
                        <td align="right" class="t2" style="width:100px"><fmt:message key='create.author'/>：</td> 
                        <td align="left" class="t2"><input id="sddeDocuCreator" name="sddeDocuCreator" value="${rsda.rsdaCreator}"/></td> 
                      </tr>
                   	 <tr>
                        <td align="right" class="t2" style="width:100px"><fmt:message key='repeat.publisher'/>：</td> 
                        <td align="left" class="t2"><input name="sddeDocuPublisher" value="${rsda.rsdaPublisher}"/></td> 
                      </tr>
                   	 <tr>
                        <td align="right" class="t2" style="width:100px"><fmt:message key='repeat.detail.keyword'/>：</td> 
                        <td align="left" class="t2"><input name="sddeDocuKeyword" value="${rsda.rsdaKeyword}"/></td> 
                      </tr>
                   	 <tr>
                        <td align="right" class="t2" style="width:100px">ISSN/ISBN：</td> 
                        <td align="left" class="t2"><input name="sddeDocuIsbn"/></td> 
                      </tr>
                       <tr>
                        <td align="right" class="t2" style="width:100px"><fmt:message key='create.time.selection'/>：</td> 
                        <td align="left" class="t2">
	                        <input id="sddeDocuDate" name="sddeDocuDate" type="hidden"/>
	                        <input id="sb-year" style="width:100px;"/>&nbsp;<fmt:message key='create.year'/>
	                        <input id="sb-juan" style="width:100px;"/>&nbsp;<fmt:message key='create.volume'/>
	                        <input id="sb-qi" style="width:100px;"/>&nbsp;<fmt:message key='create.stage'/>
                        </td> 
                      </tr>
                   	 <tr>
                        <td align="right" class="t2" style="width:100px"><fmt:message key='create.page.selection'/>：</td> 
                        <td align="left" class="t2">
                        	<input id="sddeDocuPage" name="sddeDocuPage" type="hidden"/>
							<input id="sb-start" style="width:100px;"/>&nbsp;<fmt:message key='create.home.page'/>
	                        <input id="sb-end" style="width:100px;"/>&nbsp;<fmt:message key='create.terminate.page'/>
						</td> 
                      </tr>
                   	 <tr>
                        <td align="right" class="t2" valign="top"><fmt:message key='create.requirement.description'/>：</td> 
                        <td align="left" class="t2"><input name="sddeContent"/></td>  
                      </tr>
                      <tr>
                      	<td>&nbsp;</td>
                      	<td>
		                     <a href="javascript:void(0);" class="tbtn cabtn mgr" onclick="javascript:return checkForm();"><fmt:message key='consul.submit'/></a>
		                     <a href="<%=path %>/usercenter/docudelivery.htm?action=initDocuView" class="tbtn cabtn"><fmt:message key='edit.return'/></a>
                      	</td>
                      </tr>
                     </table>
                     </div>
			     </div>
                 </form>
				</div>
			<div class="clear"></div>
			</div> 
		</div>

 		<!--底部开始-->
		<div class="clear"></div>
		<%@ include file="../pc/common/footer.jsp"%>
	</body>
</html>