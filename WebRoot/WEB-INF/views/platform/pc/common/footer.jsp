<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>

<div class="footer">
	<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "版权所有") %> <span>&#169;</span> <%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "西藏大学文学院") %>
</div>
	<%if(language.equals("zw")){ %>
  		<link rel="stylesheet" type="text/css" href="<%=basePath %>css/platform/zw.css" />
  	<%} %>
  </body>
</html>
