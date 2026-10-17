<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.cache.PlatformCacheFactory"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
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
}
%>

<!DOCTYPE html>
<html lang="en">
  <head>
    <title><fmt:message key="adv.advanced.search"/></title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
		<!-- JS语言包 -->
  	<script src="<%=basePath %>js/platform/lankey/<%=language %>.js"></script>
  	<link rel="stylesheet" href="<%=basePath %>css/platform/advsearch.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=basePath %>css/platform/skin-<%=ConstantsObj.getSkinCode() %>.css" type="text/css"></link>
    <link rel="stylesheet" href="<%=basePath%>css/platform/advanced-search-dialog.css?v=20261017-1">
  	<script type="text/javascript" src="<%=basePath %>js/jquery.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
  	<script type="text/javascript" src="<%=basePath %>js/platform/advsearch.js?v=20261017-1"></script>
  	
  	
  </head>
  
  <body class="dh-advanced-form" data-library-filter="${advancedLibraryId}">
  	<div class="conbox">
  	<form action="<%=basePath %>resAdvs" target="_parent" onsubmit="return validform();">
    <table cellpadding="1" cellspacing="1">
    	<tr>
    		<td class="tdlogic">&nbsp;</td>
    		<td class="tdele">
    			<select class="sea_select" name="COL1">
    				<script type="text/javascript">document.write(condtion);</script>
    			</select>
    		</td>
    		<td>
    			<input type="text" class="skey" name="KEY1" id="KEY1">
    		</td>
    		<td class="tdlike">
    			<select class="sea_select" name="LIKE1">
    				<option value="LIKE"><fmt:message key="adv.vague"/></option>
    				<option value="="><fmt:message key="adv.accurate"/></option>
    			</select>
    		</td>
    	</tr>
    	<tr>
    		<td class="tdlogic">
    			<select class="sea_select" name="CON2">
    				<option value="AND"><fmt:message key="adv.also"/></option>
    				<option value="OR"><fmt:message key="adv.perhaps"/></option>
    			</select>
    		</td>
    		<td class="tdele">
    			<select class="sea_select" name="COL2" id="COL2">
    				<script type="text/javascript">document.write(condtion);</script>
    			</select>
    		</td>
    		<td>
    			<input type="text" class="skey" name="KEY2" id="KEY2">
    		</td>
    		<td class="tdlike">
    			<select class="sea_select" name="LIKE2">
    				<option value="LIKE"><fmt:message key="adv.vague"/></option>
    				<option value="="><fmt:message key="adv.accurate"/></option>
    			</select>
    		</td>
    	</tr>
    	<tr>
    		<td class="tdlogic">
    			<select class="sea_select" name="CON3">
    				<option value="AND"><fmt:message key="adv.also"/></option>
    				<option value="OR"><fmt:message key="adv.perhaps"/></option>
    			</select>
    		</td>
    		<td class="tdele">
    			<select class="sea_select" name="COL3" id="COL3">
    				<script type="text/javascript">document.write(condtion);</script>
    			</select>
    		</td>
    		<td>
    			<input type="text" class="skey" name="KEY3" id="KEY3">
    		</td>
    		<td class="tdlike">
    			<select class="sea_select" name="LIKE3">
    				<option value="LIKE"><fmt:message key="adv.vague"/></option>
    				<option value="="><fmt:message key="adv.accurate"/></option>
    			</select>
    		</td>
    	</tr>
    	<tr>
    		<td class="tdlogic">
    			<select class="sea_select" name="CON4">
    				<option value="AND"><fmt:message key="adv.also"/></option>
    				<option value="OR"><fmt:message key="adv.perhaps"/></option>
    			</select>
    		</td>
    		<td class="tdele">
    			<select class="sea_select" name="COL4" id="COL4">
    				<script type="text/javascript">document.write(condtion);</script>
    			</select>
    		</td>
    		<td>
    			<input type="text" class="skey" name="KEY4" id="KEY4">
    		</td>
    		<td class="tdlike">
    			<select class="sea_select" name="LIKE4">
    				<option value="LIKE"><fmt:message key="adv.vague"/></option>
    				<option value="="><fmt:message key="adv.accurate"/></option>
    			</select>
    		</td>
    	</tr>
    </table>
    
    <p class="title"><fmt:message key="repeat.resource.library"/></p>
    <ul class="groupbox">
    	<c:forEach items="<%=PlatformCacheFactory.instance().getMetastoreList() %>" var="v" varStatus="s" end="9">
		   <input type="checkbox" name="libId" id="lib_${v[0] }" value="${v[0] }"><label for="lib_${v[0] }">${v[1] }</label> &nbsp;
		</c:forEach>
    </ul>
    
    <p class="title"><fmt:message key="repeat.resource.type"/></p>
    <ul class="groupbox">
    	<c:forEach items="${advancedTypes}" var="v">
		   <input type="checkbox" name="typeId" id="t_${v[0]}" value="${v[0]}"><label for="t_${v[0]}"><c:out value="${v[1]}"/></label> &nbsp;
		</c:forEach>
    </ul>
    
    <p class="seabox">
    	<input type="submit" class="seabtn bcm" value="<fmt:message key='adv.retrieval'/>">
    </p>
    </form>
    </div>
  </body>
</html>
