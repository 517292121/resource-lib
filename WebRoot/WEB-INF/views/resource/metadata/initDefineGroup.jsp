<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/extInclude.jsp" %>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
String path = request.getContextPath();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>敦煌研究数据库</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<style type="text/css">
		.rms_header{font-size:12px;padding-left:5px;color:#15428b;}
	</style>
	<script type="text/javascript">
		function getPath() {
			return '<%=path%>/';
		}
		var rmstId = '${rmstId}';

		function goBack(){
			document.location.href = getPath() + 'console/data/resource/defineStore.htm?action=initDefineStoreView';
		}
		function refresh(){
			window.location.reload();
		}
		var rdtyType = ${param.rdtyType };
	</script>
     <script type="text/javascript" language="javascript" src="<c:url value='/viewsjs/resource/metadata/initDefineGroup.js' />"></script>
     <script type='text/javascript' src="<c:url value='/dwr/engine.js'/>"> </script>
	 <script type='text/javascript' src="<c:url value='/dwr/util.js'/>"> </script>
	 <link rel="stylesheet" href="<%=path %>/css/form.css" type="text/css"></link>
	 <script type='text/javascript' src="<c:url value='/dwr/interface/resourceFacade.js'/>"></script>
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
		  	 <li><a href="javascript:goBack();"><img src="<%=path %>/images/icon/cancel.png"></img>返回</a></li>

		  	 <li><a href="<%=path %>/console/data/dynamic.htm?action=export&rmstId=1751318557776787&rdtyType=1"><img src="<%=path %>/images/icon/export.gif"></img>导出</a></li>
          </ul>

          <ul style="float: right;">
          	 分类：【${rdtyTypeName }】
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
  	<br/>
  </body>
</html>
