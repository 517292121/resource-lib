<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="common/header.jsp" %>
	<style>
		#txt a{
			color: #808080;
		    font-size: 18px;
		    text-decoration: underline;
		    padding: 0 3px;
		}
	</style>
	<%-- 
	<div class="main pageerror box-shadow">恭喜你！你的帐号已经激活成功啦！快来<a style="" href="#">登录</a>吧</div>
	--%>
	<div id="txt" class="main pageerror box-shadow">${msg}</div>
</html>
