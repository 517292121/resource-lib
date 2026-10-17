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
    <script type="text/javascript" language="javascript" src="<c:url value='/viewsjs/resource/metadata/resSubjectType2.js' />"></script>
    <script type='text/javascript' src="<c:url value='/dwr/engine.js'/>"> </script>
	<script type='text/javascript' src="<c:url value='/dwr/util.js'/>"> </script>
	<script type='text/javascript' src="<c:url value='/dwr/interface/resourceFacade.js'/>"></script>
	<link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
    <style type="text/css">
    .gridTitle { background:#ABC7EC; font-size:14px; font-weight:bold}
	    .y_background{
		background-color: #F0E8F6;
		}
		.w_background{
		background-color: #f3f3f3;
		}
		.x_background{
		background-color:#ffffff;
		}
		.td_p {padding:5px 0 5px 0;}
		.f_p{padding-left: 40px;}
		.f_p_3{padding-left: 80px;}
		.p_r{padding:0 5px 0 0;}
		.p_l_r{padding:0 5px 0 5px;}
		table{font-size: 12px;}
		.div_p_r{padding-right: 75px;}
		.div_p_r_2{padding-right: 75px;}
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
		.tit .imginfoc{ padding-top:5px;background: url("<%=path%>/images/application_go.png") no-repeat 0 3px;*background: url("<%=path%>/images/application_go.png") no-repeat 0 5px; cursor:pointer;padding-left:20px;}
		.tool .tool_pd{cursor:pointer;}
		.tool_pd{}
		.nav {padding-right: 45px;}
		.tool ul.nav li{float:left; list-style:none;margin-right:5px; font-size:12px; }
		.my_p_r {padding-right: 35px;}
		.tr_top_no_border {border-left:1px solid #e8e8e8;border-right: 1px solid #e8e8e8;border-top: 1px solid #e8e8e8; }
    </style>
  </head>
  <body>
  		 <div class="tool">
          <ul class="nav">
             <li><a href="javascript:add1();"><img src="<%=path%>/images/common/common_add.gif"/>新增</a></li>
		  	 <li><a href="javascript:refresh();"><img src="<%=path%>/images/refresh.png" />刷新</a></li>
          </ul>

	   </div>
	   <div id="dtable">
  			<c:forEach items="${rdtGrade1}" var="grade1">
  				<table class="tr_top_no_border" width="100%"  cellpadding="0" cellspacing="0" id="table${grade1[0]}" bordercolor="#e8e8e8" style="border-collapse: collapse;">
					<tbody id="${grade1[0]}">
						<!-- 此处为一级目录 -->
						<tr onmouseout='mouseout("${grade1[0]}")' onmouseover='mouseover("${grade1[0]}");' id="tr${grade1[0]}"  style="" class="y_background">
							<td align="left" class="td_p">
								<div style="float: left;">
									<span class="p_l_r"></span><img align="absmiddle" src="<%=path%>/images/subject/class1.gif" ><b><span onclick="changeToInput('name${grade1[0]}')" id="name${grade1[0]}">${grade1[2] }</span></b>&nbsp;&nbsp;
									<span id="span${grade1[0]}" style="display: none;">
										<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/up1.gif" onclick="up('${grade1[0]}','a')"></a>&nbsp;
										<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/down1.gif" onclick="down('${grade1[0]}','a')"></a>
									</span>
								</div>
								<div style="float:right;" class="div_p_r">
									<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/add2.gif" onclick="add2('${grade1[0]}')"></a><span class="my_p_r"></span>
									<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/edit.gif" onclick="changeToInput('name${grade1[0]}')"></a><span class="my_p_r"></span>
									<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/delete.gif" onclick="remove('${grade1[0]}')"></a>
								</div>
							</td>
						</tr>

						<c:forEach items="${rdtGrade2}" var="grade2">
							<c:if test="${grade2[1] == grade1[0] }">
								<tr id="tr2_${grade2[0]}">
									<td align="left" >
										<table class="adTable2 tr_top_no_border" width="100%" cellpadding="0" cellspacing="0" id="table${grade2[0]}" bordercolor="#e8e8e8" style="border-collapse: collapse;">
											<tbody id="${grade2[0]}">
												<!-- 此处为二级目录 -->
												<tr onmouseout='mouseout("${grade2[0]}")' onmouseover='mouseover("${grade2[0]}");' id="tr${grade2[0]}"  style="" class="w_background ">
													<td align="left" class="td_p tr_top_no_border">
														<div style="float: left;" class="f_p">
															<span class="p_r" ></span><img align="absmiddle" src="<%=path%>/images/subject/class2.gif" ><b><span onclick="changeToInput('name${grade2[0]}')" id="name${grade2[0]}">${grade2[2] }</span></b>&nbsp;&nbsp;
															<span id="span${grade2[0]}" style="display: none;">
																<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/up1.gif" onclick="up('${grade2[0]}','b')"></a>&nbsp;
																<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/down1.gif" onclick="down('${grade2[0]}','b')"></a>
															</span>
														</div>
														<div style="float:right;" class="div_p_r_2">
															<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/add2.gif" onclick="add3('${grade2[0]}')"></a><span class="my_p_r"></span>
															<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/edit.gif" onclick="changeToInput('name${grade2[0]}')"></a><span class="my_p_r"></span>
															<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/delete.gif" onclick="remove('${grade2[0]}')"></a>
														</div>
													</td>
												</tr>
												<!-- 此处为三级目录 -->
												<c:forEach items="${rdtGrade3}" var="grade3">
													<c:if test="${grade3[1] == grade2[0] }">
														<tr onmouseout='mouseout("${grade3[0]}")' onmouseover='mouseover("${grade3[0]}");' id="tr${grade3[0]}"  style="" class="x_background">
															<td align="left" class="td_p tr_top_no_border">
																<div style="float: left;" class="f_p_3">
																	<span class="p_r"></span><img align="absmiddle" src="<%=path%>/images/subject/class3.gif"  ><b><span onclick="changeToInput('name${grade3[0]}')" id="name${grade3[0]}">${grade3[2] }</span></b>&nbsp;&nbsp;
																	<span id="span${grade3[0]}" style="display: none;">
																		<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/up1.gif" onclick="up('${grade3[0]}')"></a>&nbsp;
																		<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/down1.gif" onclick="down('${grade3[0]}')"></a>
																	</span>
																</div>
																<div style="float:right;" class="div_p_r_2">
																	<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/edit.gif" onclick="changeToInput('name${grade3[0]}')"></a><span class="my_p_r"></span>
																	<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/delete.gif" onclick="remove('${grade3[0]}')"></a>
																</div>
															</td>
														</tr>
													</c:if>
												</c:forEach>
											</tbody>
										</table>
									</td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
  			</c:forEach>
	  </div>

  		<!-- <div id="dtable" class="dtable">
  			<c:forEach items="${rstList}" var="rst"><c:if test="${rst.rstyPid !=null && rst.rstyPid == 0}">

  				<table class="adTable tr_top_no_border" cellpadding="0" cellspacing="0" width="100%" id="table${rst.rstyId}" style="border-collapse: collapse;">
  					<tbody id="${rst.rstyId}" class="tr_top_no_border">
  						<tr  onmouseout='mouseout("${rst.rstyId}")' onmouseover='mouseover("${rst.rstyId}");' id="tr${rst.rstyId}" class="y_background">
		  					<td align="left" >
		  						<div style="float: left;">
		  							<span class="p_l_r"></span><img align="absmiddle" src="<%=path%>/images/subject/class1.gif" ><b><span onclick="changeToInput('name${rst.rstyId}')" id="name${rst.rstyId}">${rst.rstyName}</span></b>&nbsp;&nbsp;
		  							<span id="span${rst.rstyId}" style="display: none;">
		  								<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/up1.gif" onclick="up('${rst.rstyId}')"></a>&nbsp;
		  								<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/down1.gif" onclick="down('${rst.rstyId}')"></a>
		  							</span>
		  						</div>
		  						<div style="float:right;" class="div_p_r">
		  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/add2.gif" onclick="add('${rst.rstyId}')"></a><span class="my_p_r"></span>
		  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/edit.gif" onclick="changeToInput('name${rst.rstyId}')"></a><span class="my_p_r"></span>
		  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/delete.gif" onclick="remove('${rst.rstyId}')"></a>
		  						</div>
		  					</td>
		  				</tr>
		  				<c:forEach items="${rstList}" var="rst2">
	  						<c:if test="${rst2.rstyPid !=null && rst2.rstyPid == rst.rstyId}">
		  						<tr  onmouseout='mouseout("${rst2.rstyId}")' onmouseover='mouseover("${rst2.rstyId}");' id="tr${rst2.rstyId}" class="w_background" style="">
				  					<td align="left" class="tr_top_no_border">
				  						<div style="float: left;" class="f_p">
				  							<span class="p_r"></span><img align="absmiddle" src="<%=path%>/images/subject/class2.gif"  ><b><span onclick="changeToInput('name${rst2.rstyId}')" id="name${rst2.rstyId}">${rst2.rstyName}</span></b>&nbsp;&nbsp;
				  							<span id="span${rst2.rstyId}" style="display:none;">
				  								<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/up1.gif" onclick="up('${rst2.rstyId}')"></a>&nbsp;
				  								<a href="javascript:void(0)"><img border="0" align="absmiddle"   src="<%=path%>/images/subject/down1.gif" onclick="down('${rst2.rstyId}')"></a>
				  							</span>
				  						</div>
				  						<div style="float:right;" class="div_p_r">
				  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/edit.gif" onclick="changeToInput('name${rst2.rstyId}')"></a><span class="my_p_r"></span>
				  							<a href="javascript:void(0)"><img border="0" src="<%=path%>/images/subject/delete.gif" onclick="remove('${rst2.rstyId}')"></a>
				  						</div>
				  					</td>
				  				</tr>
			  				</c:if>
		  				</c:forEach>
	  				</tbody>
	  			</table>
	  		</c:if>
	  	</c:forEach>
	  </div>-->
  </body>
</html>
