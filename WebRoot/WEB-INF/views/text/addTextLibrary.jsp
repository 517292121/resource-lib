<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="/WEB-INF/taglibs/c.tld"%>
<%
String path = request.getContextPath();
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>资源库编辑</title>
	<meta http-equiv="pragma" content="no-cache" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="expires" content="0" />    
	<link rel="stylesheet" href="<%=path %>/css/common.css" type="text/css"></link>
  	<link rel="stylesheet" href="<%=path %>/css/MessageUtil.css" type="text/css"></link>
  	<style type="text/css">
  		*{margin:0;padding:0;font-size:13px;vertical-align:middle;}
  		table td{height: 36px;}
  		.htmlText{width: 280px; height: 28px;padding-left: 3px;}
  		.htmlText1{width: 80px; height: 28px;padding-left: 3px;ime-mode:Disabled;}
  		.text{width: 400px;height: 80px;}
  		.tips{font-size:12px;color:#a1a1a1;margin-left:10px;}
  	</style>
  	<script type="text/javascript">var root = "<%=path %>/";</script>
  	<script type="text/javascript" src="<%=path %>/js/jquery-1.4.2.js"></script>
    <script type="text/javascript" src="<%=path %>/js/MessageUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/BasicUtil.js"></script>
    <script type="text/javascript" src="<%=path %>/js/tableUtil.js"></script>
    <script type="text/javascript">
    	function onSubmit(){
    		if(document.getElementById("rtliName").value.length < 1){
    			Msg.fail('请填写资源库名称！');
    			return;
    		}
    		
    		$(".tiperr").remove();
    		var inputIndex = 0;
    		//检测字段和对应excel是否填写
    		var key = document.getElementById("rtliTitle").value.length;
			var val = document.getElementById("rtliTitleImp").value.length;
			var item = document.getElementById("col0tip");
			if(key > 0 && val == 0){
				$("#col0tip").html("<span class='tiperr' style='color:red;'>字段标签和对应Excel模板设置必须同时填写！</span>"+$("#col0tip").text());
				return;
			}else if(key == 0 && val > 0){
				$("#col0tip").html("<span class='tiperr' style='color:red;'>字段标签和对应Excel模板设置必须同时填写！</span>"+$("#col0tip").text());
				return;
			}
			
    		for (var i=1;i<=15;i++){
				var key = document.getElementById("rtliCol"+i+"Label").value.length;
				var val = document.getElementById("rtliCol"+i+"Imp").value.length;
				//alert(i+":"+key+":"+val);
				var item = document.getElementById("col"+i+"tip");
				if(key > 0 && val == 0){
					$("#col"+i+"tip").html("<span class='tiperr' style='color:red;'>字段标签和对应Excel模板设置必须同时填写！</span>"+$("#col"+i+"tip").text());
					return;
				}else if(key == 0 && val > 0){
					$("#col"+i+"tip").html("<span class='tiperr' style='color:red;'>字段标签和对应Excel模板设置必须同时填写！</span>"+$("#col"+i+"tip").text());
					return;
				}else if(key == 0 && val == 0){
					inputIndex += 1;
				}
			}
			if(inputIndex == 15){
				Msg.fail('请至少设置一个可选字段！');
    			return;
			}
    		
    		Msg.wait("数据提交中...");
    		document.getElementById("configForm").submit();
    		
    	}
    	
    	$(document).ready(function(){
		  $(".htmlText1").keyup(function(){
		  	var value = $(this).val();
		  	value = value.replace(/[^\a-\z\A-\Z]/g,'');
		  	if(value.length > 0){
		  		//转大写
			  	value = value.toUpperCase();
			  	//截取第一位
			  	value = value.substring(0,1);
		  	}
		  	$(this).val(value);
		  });
		});
    	
    	function goBack(){
    		window.location.href = "<%=path %>/console/data/text/textLibrary.htm";
    	}
    	
    </script>
  </head>
  
  <body style="padding-bottom: 100px;" onload="tableBg()">
  	<div class="tool extbtn" style="margin-top: 0">
  		<div style="float: left;padding-top: 3px;">
  			<img src="<%=path %>/images/extform/02.png"></img> 资源库编辑
  		</div>
  		<ul class="nav" style="float: right;padding-right: 10px;">
		    <li><a href="javascript:goBack();"><img src="<%=path %>/images/console/icon/common_back.png" align="absmiddle"></img> 返回</a></li>
		</ul> 
  	</div>
  	
  	<div>
  	<c:choose>
		<c:when test="${!empty error }">
			<div style="padding: 0px 10px 0px 20px;">
  			<h2>错误提示</h2><hr />
  			<span style="color: red">${error }</span>
  			</div>
  		</c:when>
  		
  		<c:otherwise>
			<form action="<%=path %>/console/data/text/textLibrary.htm" id="configForm" method="post" enctype="multipart/form-data">	
				<input type="hidden" name="action" value="saveLibrary" />
				<input type="hidden" value="<c:out value='${lib.rtliId}'></c:out>" name="rtliId" id="rtliId"/>			
			    <table width="100%" cellpadding="0" cellspacing="0" id="Page_data">
		    		<tr style="height:40px;">
		    			<th style="text-align:right;">输入项&nbsp;&nbsp;&nbsp;</th>
		    			<th style="text-align:center;width:300px;">输入值</th>
		    			<th style="text-align:center;width:80px;">Excel模板列</th>
		    			<th></th>
		    		</tr>
			    	<tr>
			    		<td width="180px;" align="right"><span style="color: red">*</span> 资源库名称：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliName }'></c:out>" name="rtliName" id="rtliName" maxlength="64" class="htmlText" />
			    		</td>
			    		<td>&nbsp;</td>
			    		<td>&nbsp;</td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right"><span style="color: red">*</span> 资源库语种：</td>
			    		<td colspan="3">
			    			<input type="radio" name="rtliLanguage" id="lan_zh" value="zh" <c:if test="${empty lib.rtliLanguage or lib.rtliLanguage == 'zh' }">checked="checked"</c:if>><label for="lan_zh">中文</label> &nbsp;					
			  				<input type="radio" name="rtliLanguage" id="lan_zw" value="zw" <c:if test="${lib.rtliLanguage == 'zw' }">checked="checked"</c:if>><label for="lan_zw">藏文</label> &nbsp;
			  				<input type="radio" name="rtliLanguage" id="lan_mw" value="mw" <c:if test="${lib.rtliLanguage == 'mw' }">checked="checked"</c:if>><label for="lan_mw">蒙文</label> &nbsp;
			  				<input type="radio" name="rtliLanguage" id="lan_en" value="en" <c:if test="${lib.rtliLanguage == 'en' }">checked="checked"</c:if>><label for="lan_en">英文</label>
			    		</td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">显示序号：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliIndex }'></c:out>" name="rtliIndex" id="rtliIndex" maxlength="5" class="htmlText" />
			    		</td>
			    		<td>&nbsp;</td>
			    		<td>&nbsp;</td>
			    	</tr>
			    	
			    	<tr>
			    		<td width="180px;" align="right"><span style="color: red">*</span>题名标题字段：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliTitle }'></c:out>" name="rtliTitle" id="rtliTitle" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliTitleImp }'></c:out>" name="rtliTitleImp" id="rtliTitleImp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col0tip" class="tips">512字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段1：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol1Label }'></c:out>" name="rtliCol1Label" id="rtliCol1Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol1Imp }'></c:out>" name="rtliCol1Imp" id="rtliCol1Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col1tip" class="tips">256字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段2：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol2Label }'></c:out>" name="rtliCol2Label" id="rtliCol2Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol2Imp }'></c:out>" name="rtliCol2Imp" id="rtliCol2Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col2tip" class="tips">256字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段3：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol3Label }'></c:out>" name="rtliCol3Label" id="rtliCol3Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol3Imp }'></c:out>" name="rtliCol3Imp" id="rtliCol3Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col3tip" class="tips">256字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段4：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol4Label }'></c:out>" name="rtliCol4Label" id="rtliCol4Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol4Imp }'></c:out>" name="rtliCol4Imp" id="rtliCol4Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col4tip" class="tips">256字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段5：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol5Label }'></c:out>" name="rtliCol5Label" id="rtliCol5Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol5Imp }'></c:out>" name="rtliCol5Imp" id="rtliCol5Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col5tip" class="tips">256字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段6：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol6Label }'></c:out>" name="rtliCol6Label" id="rtliCol6Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol6Imp }'></c:out>" name="rtliCol6Imp" id="rtliCol6Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col6tip" class="tips">512字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段7：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol7Label }'></c:out>" name="rtliCol7Label" id="rtliCol7Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol7Imp }'></c:out>" name="rtliCol7Imp" id="rtliCol7Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col7tip" class="tips">512字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段8：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol8Label }'></c:out>" name="rtliCol8Label" id="rtliCol8Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol8Imp }'></c:out>" name="rtliCol8Imp" id="rtliCol8Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col8tip" class="tips">512字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段9：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol9Label }'></c:out>" name="rtliCol9Label" id="rtliCol9Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol9Imp }'></c:out>" name="rtliCol9Imp" id="rtliCol9Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col9tip" class="tips">512字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段10：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol10Label }'></c:out>" name="rtliCol10Label" id="rtliCol10Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol10Imp }'></c:out>" name="rtliCol10Imp" id="rtliCol10Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col10tip" class="tips">512字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段11：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol11Label }'></c:out>" name="rtliCol11Label" id="rtliCol11Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol11Imp }'></c:out>" name="rtliCol11Imp" id="rtliCol11Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col11tip" class="tips">1024字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段12：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol12Label }'></c:out>" name="rtliCol12Label" id="rtliCol12Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol12Imp }'></c:out>" name="rtliCol12Imp" id="rtliCol12Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col12tip" class="tips">1024字节</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段13：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol13Label }'></c:out>" name="rtliCol13Label" id="rtliCol13Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol13Imp }'></c:out>" name="rtliCol13Imp" id="rtliCol13Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col13tip" class="tips">CLOB大字段</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段14：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol14Label }'></c:out>" name="rtliCol14Label" id="rtliCol14Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol14Imp }'></c:out>" name="rtliCol14Imp" id="rtliCol14Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col14tip" class="tips">CLOB大字段</span></td>
			    	</tr>
			    	<tr>
			    		<td width="180px;" align="right">字段15：</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol15Label }'></c:out>" name="rtliCol15Label" id="rtliCol15Label" maxlength="64" class="htmlText" />---
			    		</td>
			    		<td>
			    			<input value="<c:out value='${lib.rtliCol15Imp }'></c:out>" name="rtliCol15Imp" id="rtliCol15Imp" maxlength="2" class="htmlText1" />
			    		</td>
			    		<td><span id="col15tip" class="tips">CLOB大字段</span></td>
			    	</tr>
			    	
			    	<tr height="38px;">
			    		<td>&nbsp;</td>
			    		<td style="padding-top: 10px;padding-left: 1px;">
			    			<input type="button" value=" 保 存 " onclick="onSubmit()" class="saveBtn" />
			    			<input type="button" value=" 返 回 " onclick="goBack()" class="saveBtn" />
			    		</td>
			    	</tr>
			    </table>
		    </form>
  		</c:otherwise>
  	</c:choose>
  	</div>
  	
  	${result }
  </body>
</html>