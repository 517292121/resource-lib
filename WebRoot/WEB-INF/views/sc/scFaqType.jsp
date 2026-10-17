<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
%>
<script type="text/javascript">
	function getPath() {
		return '<%=path%>/';
	}
	function refresh(){
		window.location.reload();
	}
</script>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>敦煌研究数据库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<link type="text/css" href="<c:url value='/ext/ux/treegrid/treegrid.css'/>" rel="stylesheet" />
    <script type="text/javascript" language="javascript" src="<c:url value='/viewsjs/sc/scFaqType.js' />"></script>
    <script type='text/javascript' src="<c:url value='/dwr/engine.js'/>"> </script>
	<script type='text/javascript' src="<c:url value='/dwr/util.js'/>"> </script>
	<script type='text/javascript' src="<c:url value='/dwr/interface/scFacade.js'/>"></script>
    <style type="text/css">
    .gridTitle { background:#ABC7EC; font-size:14px; font-weight:bold}
    .y_background{
	background-color: #f3f3f3;
	}
	.w_background{
	background-color: white;
	}
	.adTable td{padding :5px;}
	.f_p{padding-left: 40px;}
	.p_r{padding:0 5px 0 0;}
	.p_l_r{padding:0 5px 0 5px;}
	table{font-size: 12px;}
	.div_p_r{padding-right: 75px;}
	.input_edit{
	    border:0px solid #ffffff;
	    background:none;
	    font-size:12px;
	    height: 22px;
	}
	.title{
	   background-image: url("<%=path%>/images/title_bg.jpg");
	   height: 20px;
	   padding-top: 2px;
	   padding-left: 3px;
	   color: #15428B;
	   font-weight: bold;
	   font-size: 12px;
	   margin-top: 2px;
	}
	.tool{
	   background: url("<%=path%>/images/tool_bg.jpg") repeat-x;
	   height: 20px;
	   color: #333333;
	   padding: 6px 0 3px 15px;
	   font-size: 12px;
	}
	.tit{float:left;padding-top:2px;}
	.tit div{}
	.tit .imginfoa{ padding-top:7px;background: url("<%=path%>/images/common/common_add.gif") no-repeat 0 4px;cursor:pointer; padding-left:20px;}
	.tit .imginfob{ padding-top:5px;background: url("<%=path%>/images/refresh.png") no-repeat 0 3px;*background: url("<%=path%>/images/refresh.png") no-repeat 0 5px; cursor:pointer;padding-left:20px;}
	.tool .tool_pd{cursor:pointer;}
	.tool_pd{}
	.tool ul.nav li{float:left; list-style:none;margin-right:5px; font-size:12px; }
	.nav {padding-right: 45px;}
	.my_p_r {padding-right: 35px;}
	.tr_border {border:1px solid #e8e8e8;}
	.tr_top_no_border {border-left:1px solid #e8e8e8;border-right: 1px solid #e8e8e8;border-top: 1px solid #e8e8e8; }
    </style>
  </head>
  <body>
  		 <div class="tool">
          <div class="tit">
             <span onclick="addGrade()" class="imginfoa">新增一级分类</span>&nbsp;|&nbsp;
             <span onclick="refresh()" class="imginfob">刷&nbsp;新</span>
          </div>
	      <ul class="nav" style="float: right;">
		    <li><img src="<%=path%>/images/subject/add2.gif">&nbsp;新增&nbsp;&nbsp;</li>
		    <li><img src="<%=path%>/images/subject/edit.gif">&nbsp;编辑&nbsp;&nbsp;</li>
		    <li><img src="<%=path%>/images/subject/delete.gif">&nbsp;删除&nbsp;&nbsp;</li>
		  </ul>
	   </div>
  		<div id="dtable" class="dtable">
  			<c:forEach items="${scFaqTypeLlist}" var="sft">
  				<c:if test="${sft.scftParentId ==null}">
  				<table class="adTable tr_top_no_border" cellpadding="0" cellspacing="0" width="100%" id="table${sft.scftId}" style="border-collapse: collapse;">
  					<tbody id="${sft.scftId}" class="tr_top_no_border">
  						<tr  onmouseout='mouseout("${sft.scftId}")' onmouseover='mouseover("${sft.scftId}");' id="tr${sft.scftId}" class="y_background">
		  					<td align="left" >
		  						<div style="float: left;">
		  							<span class="p_l_r"></span><img align="absmiddle" src="<%=path%>/images/subject/class1.gif" ><b><span onclick="changeToInput('name${sft.scftId}')" id="name${sft.scftId}">${sft.scftName}</span></b>&nbsp;&nbsp;
		  							<span id="span${sft.scftId}" style="display: none;">
		  								<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/up1.gif" onclick="up('${sft.scftId}')"></a>&nbsp;
		  								<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/down1.gif" onclick="down('${sft.scftId}')"></a>
		  							</span>
		  						</div>
		  						<div style="float:right;" class="div_p_r">
		  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/add2.gif" onclick="add('${sft.scftId}')"></a><span class="my_p_r"></span>
		  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/edit.gif" onclick="changeToInput('name${sft.scftId}')"></a><span class="my_p_r"></span>
		  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/delete.gif" onclick="remove('${sft.scftId}')"></a>
		  						</div>
		  					</td>
		  				</tr>
		  				<c:forEach items="${scFaqTypeLlist}" var="sft2">
	  						<c:if test="${sft2.scftParentId !=null && sft2.scftParentId == sft.scftId}">
		  						<tr  onmouseout='mouseout("${sft2.scftId}")' onmouseover='mouseover("${sft2.scftId}");' id="tr${sft2.scftId}" class="w_background" style="">
				  					<td align="left" class="tr_top_no_border">
				  						<div style="float: left;" class="f_p">
				  							<span class="p_r"></span><img align="absmiddle" src="<%=path%>/images/subject/class2.gif"  ><b><span onclick="changeToInput('name${sft2.scftId}')" id="name${sft2.scftId}">${sft2.scftName}</span></b>&nbsp;&nbsp;
				  							<span id="span${sft2.scftId}" style="display:none;">
				  								<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/up1.gif" onclick="up('${sft2.scftId}')"></a>&nbsp;
				  								<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/down1.gif" onclick="down('${sft2.scftId}')"></a>
				  							</span>
				  						</div>
				  						<div style="float:right;" class="div_p_r">
				  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/edit.gif" onclick="changeToInput('name${sft2.scftId}')"></a><span class="my_p_r"></span>
				  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/delete.gif" onclick="remove('${sft2.scftId}')"></a>
				  						</div>
				  					</td>
				  				</tr>
			  				</c:if>
		  				</c:forEach>
	  				</tbody>
	  			</table>
	  		</c:if>
	  	</c:forEach>
	  </div>
  </body>
</html>
