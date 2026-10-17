<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="java.util.Locale"%>
<%@ page import="org.springframework.web.servlet.i18n.SessionLocaleResolver"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	
	//切换语种
	String language = "zh";
	Object lang = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
	Locale locale ;
	if(lang != null){
		locale= (Locale)lang;
		language = locale.getLanguage();
		request.setAttribute("language",language);
	}
%>
<!DOCTYPE html>
<html>
  <head>
    <title>资源检索</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  	<link rel="stylesheet" href="<%=basePath %>css/platform/page-search.css" type="text/css"></link>
  	<script type="text/javascript" src="<%=basePath %>js/jquery-1.11.0.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
  	<script type="text/javascript">var basePath = "<%=basePath %>";</script>
  	 	<script src="<%=basePath %>js/platform/lankey/<%=language %>.js"></script>
  	<script type="text/javascript" src="<%=basePath %>js/platform/pagenumFormat.js"></script>
  	<script type="text/javascript" src="<%=basePath %>js/platform/res-browse-search.js"></script>
  	<script>
		$(function(){
			$("#gb").click(function(){
				window.parent.window.closeSearch();
			})
		})
	</script>
  </head>
  
  <body>
    <div class="s-body">
    	<div class="keep-top">
	    	<p class="header">
	    		<span id="js"><fmt:message key="main.simplesearch"/></span>
	    		<span id="gb"><a href="">×</a></span>
	    	</p>
	    	
	    	<c:if test="${havetxt}">
		    	<div class="s-box">
		    		<p style="width: 100%; height: 32px;">
		    		<input type="text" id="key" placeholder="<fmt:message key='input.retrieval.content'/>">
		    		<input type="button" id="s-btn" value="<fmt:message key='main.simplesearch'/>">
		    		</p>
		    		<p style="padding-left: 6px;">
		    			<input type="radio" value="0" name="match" id="match0" <c:if test="${empty match or match==0 }">checked="checked"</c:if> style="vertical-align:middle; margin-top:-4px;"><label for="match0"><fmt:message key="advanced.fuzzy.matching"/></label> &nbsp;
		    			<input type="radio" value="1" name="match" id="match1" <c:if test="${match==1 }">checked="checked"</c:if> style="vertical-align:middle; margin-top:-4px;"><label for="match1"><fmt:message key="advanced.precise.search"/></label> &nbsp;
		    			<input type="radio" value="2" name="match" id="match2" <c:if test="${match==2 }">checked="checked"</c:if> style="vertical-align:middle; margin-top:-4px;"><label for="match2"><fmt:message key="advanced.banjingque.matching"/></label>
		    		</p>	
		    	</div>	
		    	<p class="s-result">
		    		<fmt:message key="resource.preview.total.page"/>0<fmt:message key="create.page"/>
		    	</p>
	    	</c:if>
	    	<c:if test="${havetxt == false}">
	    		<p class="no-search"><fmt:message key="resource.preview.not.search"/></p>
	    	</c:if>
    	</div>
    	<div class="info-list-f">
    		<ul id="reslist"></ul>
    	</div>
    </div>
    
    <input type="hidden" name="rsdaId" id="rsdaId" value="${rsdaId }" />
    <input type="hidden" name="imgDir" id="imgDir" value="${imgDir }" />
    <input type="hidden" name="keys" id="keys" />
  </body>
</html>
<link rel="stylesheet" href="<%=basePath %>/css/platform/<%=language %>.css">