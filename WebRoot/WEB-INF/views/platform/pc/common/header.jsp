<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@page import="cn.com.adtech.riss2.cache.PlatformCacheFactory"%>
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
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <title><c:if test="${!empty title }">${title }—</c:if><fmt:message key="main.title"/></title>
	<meta name="description" content="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "藏汉敦煌文献研究数据库，汇聚敦煌文献、历史图像与数字人文研究资源。") %>">
	<meta property="og:title" content="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "藏汉敦煌文献研究数据库") %>">
	<meta property="og:description" content="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "汇聚藏汉敦煌文献、历史图像与研究数据。") %>">
	<meta property="og:type" content="website">
	<meta property="og:image" content="<%=basePath %>images/platform/dunhuang/og.png">
	<meta name="twitter:card" content="summary_large_image">
	<meta name="twitter:title" content="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "藏汉敦煌文献研究数据库") %>">
	<meta name="twitter:description" content="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "汇聚藏汉敦煌文献、历史图像与研究数据。") %>">
	<meta name="twitter:image" content="<%=basePath %>images/platform/dunhuang/og.png">
	<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	<meta name=renderer content=webkit> <!-- 默认采用极速模式 -->
  	<link rel="stylesheet" href="<%=basePath %>css/platform/common.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=basePath %>css/platform/skin-<%=ConstantsObj.getSkinCode() %>.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=basePath %>css/platform/shared-header.css?v=dunhuang-shared-6" type="text/css"></link>
    <link rel="stylesheet" href="<%=basePath%>css/platform/member-login.css?v=frontend-login-20261025-1">
  	<script type="text/javascript">var basePath = "<%=basePath %>"; var fileUrl = "<%=SysConfigInfo.FILE_URL_PATH %>"; var language = "<%=language%>"; </script>
  	

  	<!-- JS语言包 -->
  	<script src="<%=basePath %>js/platform/lankey/<%=language %>.js"></script>
  	<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
  	<script type="text/javascript" src="<%=basePath %>js/jquery.cookie.js"></script>
	<script type="text/javascript" src="<%=basePath %>layer/layer.js"></script>
	<script src="<%=basePath %>js/platform/iealert.js?ui=zh-zw-20261025-1" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>css/platform/iealert.css" />
	<script type="text/javascript" src="<%=basePath %>js/platform/common.js?v=frontend-member-dialog-20261025-2&amp;ui=zh-zw-20261025-1"></script>
    <%@ include file="/WEB-INF/views/platform/pc/common/advancedSearchInclude.jsp" %>
	
	<!-- 下拉框插件 -->
	<link rel="stylesheet" type="text/css" href="<%=basePath %>plugin/selector/css/cs-select.css" />
	<link rel="stylesheet" type="text/css" href="<%=basePath %>plugin/selector/css/cs-skin-elastic.css" />
	<script src="<%=basePath %>plugin/selector/js/classie.js"></script>
	<script src="<%=basePath %>plugin/selector/js/selectFx.js"></script>
	<script src="<%=basePath %>js/platform/jquery.i18n.properties.js?ui=zh-zw-20261025-1"></script>
	<!-- 语言切换 -->
	<script src="<%=basePath %>js/switchLang.js"></script>
	
	<script type="text/javascript">
		$(function() {
			var COOKIE_NAME = "showbox";
			if(!$.cookie(COOKIE_NAME)) {
				$("body").iealert();
				$.cookie(COOKIE_NAME, '1');
			}
			
		});
	</script>	
	
  </head>
 
  
  <body class="dh-shared-layout">
    <%-- Legacy top1/top2 navigation replaced by the shared Dunhuang header. --%>
    <%--
    <div class="top1 bcm">
    	<ul class="main">
    		<li id="left">
    			<c:choose>
    				<c:when test="${!empty sessionScope.sysUserinfo}">

    					<fmt:message key="main.welcome"/> ：<a href="<%=basePath %>member/center">${sysUserinfo.suinLoginName }<c:if test="${!empty sysUserinfo.suinName }">（${sysUserinfo.suinName}）</c:if></a>
    					<a href="javascript:void(0);" onclick="exitLogin();" class="exitlogin"><fmt:message key='pc.exit'/></a>
    				</c:when>
    				<c:otherwise>
		    			<fmt:message key="pc.hello"/><a href="javascript:openLogin();"><fmt:message key="pc.login"/> /<fmt:message key="pc.register"/> </a>         
    				</c:otherwise>
    			</c:choose>
    		</li>
    		<li class="bdsharebuttonbox" id="right">
    			<a href="javascript:void();"  class="more" data-cmd="more" title="<fmt:message key='title.shara'/>" style="padding: 0px;"></a>
		        <script>window._bd_share_config={"common":{"bdSnsKey":{},"bdText":"","bdMini":"2","bdPic":"","bdStyle":"0","bdSize":"16"},"share":{}};with(document)0[(getElementsByTagName('head')[0]||body).appendChild(createElement('script')).src='http://bdimg.share.baidu.com/static/api/js/share.js?v=89860593.js?cdnversion='+~(-new Date()/36e5)];</script> 
    		</li>
    		<li id="lang">
    			<div class="switch">
    				<c:choose>
    					<c:when test='<%=lang == null || language.equals("zh") %>'>
    						<span>中文</span> &nbsp; 
    						<a href="javascript:switch_lang('zw_XZ');" style="font-family: hm; font-size:10px;">བོད་ཡིག</a>
    					</c:when>
    					<c:otherwise>
    						<a  href="javascript:switch_lang('zh_CN');">中文</a> &nbsp; 
    						<span style="font-size:10px;">བོད་ཡིག</span>
    					</c:otherwise>
    				</c:choose>
    			
    			 </div>
    		</li>
    	</ul>
    </div>
    
    <div class="top2">
    	<div class="main">
    	<div id="left">
    		<a href="<%=path %>/index">&nbsp;</a>
    	</div>
    	<div id="right">
    		<table border="0" cellpadding="0" cellspacing="0">
    			<tr>
    				<td><a href="<%=path %>/index" ${curr }><fmt:message key='home.page'/></a></td>
    				<td class="mycs" style="padding-left: 25px;">
			    		<section>
							<select class="cs-select cs-skin-elastic cs-store">
								<option value="all" disabled><fmt:message key='thematic.library.navigation'/></option>
								<c:forEach items="<%=PlatformCacheFactory.instance().getMetastoreList() %>" var="v">
								<option value="${v[0] }" <c:if test="${rmstId eq v[0] }">selected=true</c:if> data-link='<%=basePath %>res/db/${v[0] }'>${v[1] }</option>
								</c:forEach>
							</select>
						</section>
						<script>
							(function() {
								[].slice.call(document.querySelectorAll('select.cs-store')).forEach(function(el){	
									new SelectFx(el);
								});
							})();
						</script>
    				</td>
    				<td class="mycs">
			    		<section>
							<select class="cs-select cs-skin-elastic cs-type">
								<option value="all" disabled><fmt:message key='repeat.resource.type'/></option>
								<c:forEach items="<%=PlatformCacheFactory.instance().getResCountList() %>" var="v" varStatus="s" end="9">
								<option value="${v.id }" <c:if test="${rltyId eq v.id }">selected=true</c:if> data-link='<%=basePath %>res/type/${v.id } }'>${v.groupName }</option>	
								</c:forEach>
							</select>
						</section>
						<script>
							(function() {
								[].slice.call(document.querySelectorAll('select.cs-type')).forEach(function(el){	
									new SelectFx(el);
								});
							})();
						</script>
    				</td>
    				
    				<c:if test="<%=PlatformCacheFactory.instance().getTextstoreList().size() > 0 %>">
    				<td class="mycs">
			    		<section>
							<select class="cs-select cs-skin-elastic text-lib">
								<option value="all" disabled><fmt:message key='text.library.navigation'/></option>
								<c:forEach items="<%=PlatformCacheFactory.instance().getTextstoreList() %>" var="v" varStatus="s" end="9">
								<option value="${v[0] }" <c:if test="${rtliId eq v[0] }">selected=true</c:if> data-link='<%=basePath %>text/lib/${v[0] } }'>${v[1] }</option>	
								</c:forEach>
							</select>
						</section>
						<script>
							(function() {
								[].slice.call(document.querySelectorAll('select.text-lib')).forEach(function(el){	
									new SelectFx(el);
								});
							})();
						</script>
    				</td>
    				</c:if>
    				
    				<c:if test="<%=PlatformCacheFactory.instance().getInfoClassList().size() > 0 %>">
    				<td class="mycs">
			    		<section>
							<select class="cs-select cs-skin-elastic info-class">
								<option value="all" disabled><fmt:message key='information.navigation'/></option>
								<c:forEach items="<%=PlatformCacheFactory.instance().getInfoClassList() %>" var="v" varStatus="s" end="9">
								<option value="${v[0] }" <c:if test="${classId eq v[0] }">selected=true</c:if> data-link='<%=basePath %>info/list?cid=${v[0] } }'>${v[1] }</option>	
								</c:forEach>
							</select>
						</section>
						<script>
							(function() {
								[].slice.call(document.querySelectorAll('select.info-class')).forEach(function(el){	
									new SelectFx(el);
								});
							})();
						</script>
    				</td>
    				</c:if>
    			</tr>
    		</table>
	</div>
	</div>
    --%>
    <header class="dh-shared-header">
      <a class="dh-shared-brand" href="<%=path%>/index" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库首页") %>">
        <img src="<%=basePath%>images/platform/psb/home/brand-logo.png" alt="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学敦煌文献智能数据库") %>" width="537" height="74">
      </a>
      <nav class="dh-shared-nav" aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "主导航") %>">
        <a href="<%=path%>/index"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "首 页") %></a>
        <a class="is-current" href="<%=path%>/literature" aria-current="page"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "文献库") %></a>
        <a href="<%=path%>/database"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "数据库") %></a>
      </nav>
      <div class="dh-shared-tools">
        <label class="dh-shared-language"><span class="dh-shared-sr-only"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "切换语言") %></span>
          <select aria-label="<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "切换语言") %>" onchange="switch_lang(this.value)">
            <option value="zh_CN" lang="zh-CN" translate="no" <%= "zh".equals(language) ? "selected" : "" %>><%= "汉文" %></option>
            <option value="zw_XZ" lang="bo" translate="no" <%= "zw".equals(language) ? "selected" : "" %>>&#3926;&#3964;&#3921;&#3851;&#3937;&#3954;&#3906;</option>
          </select>
        </label>
        <c:choose>
          <c:when test="${not empty sessionScope.sysUserinfo}"><a class="dh-shared-login" href="<%=basePath%>member/center"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "进入个人中心") %></a></c:when>
          <c:otherwise><button class="dh-shared-login" type="button" onclick="openLogin();"><%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "登 录") %></button></c:otherwise>
        </c:choose>
      </div>
    </header>
    <div class="banner">
		<div id="search" class="box-shadow">
			<table cellpadding="0" cellspacing="0" border="0">
				<tr>
					<td width="160px" style="border-right: 1px solid #E8E8E8;">
						<section>
						<select name="attr" id="attr" class="cs-select cs-skin-elastic search-attr">
							<option value="all"><fmt:message key="repeat.all"/></option>
							<option value="rsdaTitle" <c:if test="${attr=='rsdaTitle' }">selected="selected"</c:if>><fmt:message key="search.title"/></option>
							<option value="rsdaCreator" <c:if test="${attr=='rsdaCreator' }">selected="selected"</c:if>><fmt:message key="repeat.responsible.person"/></option>
							<option value="rsdaPublisher" <c:if test="${attr=='rsdaPublisher' }">selected="selected"</c:if>><fmt:message key="repeat.publisher"/></option>
							<option value="rsdaKeyword" <c:if test="${attr=='rsdaKeyword' }">selected="selected"</c:if>><fmt:message key="repeat.detail.keyword"/></option>
						</select>
						</section>
					</td>
					<td width="160px" style="border-right: 1px solid #E8E8E8;">	
						<section>
						<select name="accurate" id="accurate" class="cs-select cs-skin-elastic match-type">
							<option value="1" <c:if test="${empty match or match==1 }">selected="selected"</c:if>><fmt:message key="advanced.precise.search"/></option>
							<option value="0" <c:if test="${match==0 }">selected="selected"</c:if>><fmt:message key="advanced.fuzzy.matching"/></option>
							<!-- <option value="2" <c:if test="${match==2 }">selected="selected"</c:if>><fmt:message key="advanced.banjingque.matching"/></option> -->
						</select>
						</section>
						
						<script>
							(function() {
								[].slice.call(document.querySelectorAll('select.search-attr')).forEach(function(el){	
									new SelectFx(el);
								});
							})();
							(function() {
								[].slice.call(document.querySelectorAll('select.match-type')).forEach(function(el){	
									new SelectFx(el);
								});
							})();
						</script>
					</td>
					<td>
						<input type="text" name="key" value="${key }" id="searchkey" placeholder="<fmt:message key='input.retrieval.content'/>">
					</td>
					<td width="80px">
						<input type="button" id="search_but" value="">
					</td>
					<td width="160px" valign="top">
						<input type="button" id="gjjs" value="<fmt:message key='advanced.retrieval'/>" onclick="advSearch();">
					</td>
				</tr>
			</table>
		</div>
	</div>
