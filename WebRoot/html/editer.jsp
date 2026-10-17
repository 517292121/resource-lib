<%@ page contentType="text/html; charset=gb2312" language="java" import="java.sql.*" errorPage="" %>
<%
    String param = "";
    if(request.getParameter("table") != null && request.getParameter("table").length() > 0){
        param += "&table=" + request.getParameter("table");
    }
    if(request.getParameter("col") != null && request.getParameter("col").length() > 0){
        param += "&col=" + request.getParameter("col");
    }
    if(request.getParameter("id") != null && request.getParameter("id").length() > 0){
        param += "&id=" + request.getParameter("id");
    }
 %>

<html>
	<head>
		<title>HTML在线编辑器</title>
		<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
		<link rel="STYLESHEET" type="text/css" href="editor.css">
			<script language="JavaScript" type="text/JavaScript">
//菜单列表
var menu_table="<table width='100%' cellspacing='0' cellpadding='2'>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_cr.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertTable()'>插入表格</a></td></tr>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_sx.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='tableProp()'>表格属性</a></td></tr>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_sx2.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='cellProp()'>单元格属性</a></td></tr>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_tr.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='tablecommand(1)'>插入一行</a></td></tr>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_trdel.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='tablecommand(2)'>删除一行</a></td></tr>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_td.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='tablecommand(3)'>插入一列</a></td></tr>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_tddel.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='tablecommand(4)'>删除一列</a></td></tr>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_hby.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='tablecommand(5)'>向右合并</a></td></tr>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_hbx.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='tablecommand(6)'>向下合并</a></td></tr>";
menu_table+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/table_cf.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='tablecommand(7)'>拆分单元格</a></td></tr>";
menu_table+="</table>";
var menu_chars="<table width='100%' cellspacing='0' cellpadding='2'>";
menu_chars+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/chars1.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertChars(0)'>换行符</a></td></tr>";
menu_chars+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/chars2.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertChars(1)'>版权符号</a></td></tr>";
menu_chars+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/chars3.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertChars(2)'>注册商标</a></td></tr>";
menu_chars+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/chars4.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertChars(3)'>商标符号</a></td></tr>";
menu_chars+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/chars5.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertChars(4)'>圆点</a></td></tr>";
menu_chars+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/chars6.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertChars(5)'>省略号</a></td></tr>";
menu_chars+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/chars7.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertChars(6)'>破折号</a></td></tr>";
menu_chars+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/chars8.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertChars(7)'>中划线</a></td></tr>";
menu_chars+="</table>";
var menu_eq="<table width='100%' cellspacing='0' cellpadding='2'>";
menu_eq+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/eq1.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InsertEQ()'>插入公式</a></td></tr>";
menu_eq+="<tr onmouseout='scolor(this)' onmouseover='rcolor(this)'><td><img src='images/editor/eq2.gif' width='16' height='16' align='absmiddle'></td><td><a href='#' onclick='InstallEQ()'>安装公式编辑器插件</a></td></tr>";
menu_eq+="</table>";

//下拉菜单相关代码
 var h;
 var w;
 var l;
 var t;
 var topMar = 1;
 var leftMar = -2;
 var space = 1;
 var isvisible;
 var MENU_SHADOW_COLOR='#E1F4EE';//定义下拉菜单阴影色
 var global = window.document
 global.fo_currentMenu = null
 global.fo_shadows = new Array

function HideMenu() 
{
 var mX;
 var mY;
 var vDiv;
 var mDiv;
	if (isvisible == true)
{
		vDiv = document.all("menuDiv");
		mX = window.event.clientX + document.body.scrollLeft;
		mY = window.event.clientY + document.body.scrollTop;
		if ((mX < parseInt(vDiv.style.left)) || (mX > parseInt(vDiv.style.left)+vDiv.offsetWidth) || (mY < parseInt(vDiv.style.top)-h) || (mY > parseInt(vDiv.style.top)+vDiv.offsetHeight)){
			vDiv.style.visibility = "hidden";
			isvisible = false;
		}
}
}

function ShowMenu(vMnuCode,tWidth) {
	vSrc = window.event.srcElement;
	vMnuCode = "<table id='submenu' cellspacing=1 cellpadding=3 style='width:"+tWidth+"' class=menu onmouseout='HideMenu()'><tr height=23><td nowrap align=left class=MenuBody>" + vMnuCode + "</td></tr></table>";

	h = vSrc.offsetHeight;
	w = vSrc.offsetWidth;
	l = vSrc.offsetLeft + leftMar+4;
	t = vSrc.offsetTop + topMar + h + space-2;
	vParent = vSrc.offsetParent;
	while (vParent.tagName.toUpperCase() != "BODY")
	{
		l += vParent.offsetLeft;
		t += vParent.offsetTop;
		vParent = vParent.offsetParent;
	}

	menuDiv.innerHTML = vMnuCode;
	menuDiv.style.top = t;
	menuDiv.style.left = l;
	menuDiv.style.visibility = "visible";
	isvisible = true;
    makeRectangularDropShadow(submenu, MENU_SHADOW_COLOR, 4)
}

function makeRectangularDropShadow(el, color, size)
{
	var i;
	for (i=size; i>0; i--)
	{
		var rect = document.createElement('div');
		var rs = rect.style
		rs.position = 'absolute';
		rs.left = (el.style.posLeft + i) + 'px';
		rs.top = (el.style.posTop + i) + 'px';
		rs.width = el.offsetWidth + 'px';
		rs.height = el.offsetHeight + 'px';
		rs.zIndex = el.style.zIndex - i;
		rs.backgroundColor = color;
		var opacity = 1 - i / (i + 1);
		rs.filter = 'alpha(opacity=' + (100 * opacity) + ')';
		el.insertAdjacentElement('afterEnd', rect);
		global.fo_shadows[global.fo_shadows.length] = rect;
	}
}
function scolor(obj)
{
  obj.style.backgroundColor="";
}
function rcolor(obj)
{
  obj.style.backgroundColor="#E1F4EE";
}
			</script>
	</head>
	<script language="C#" runat="server">
int id;
void Page_Load(Object  s,EventArgs e)
{
  id=Convert.ToInt32(Request.QueryString["id"]);
}
	</script>
	<body bgcolor="#FFFFFF" leftmargin='0' topmargin='0' onmousemove='HideMenu()'>
		<div id="menuDiv" style='Z-INDEX: 1000; VISIBILITY: hidden; WIDTH: 1px; POSITION: absolute; HEIGHT: 1px; BACKGROUND-COLOR: #9cc5f8'></div>
		<div class="yToolbar">
			<div class="TBHandle">
			</div>
			<div class="Btn" TITLE="全部选择" LANGUAGE="javascript" onClick="format('selectall')">
				<img class="Ico" src="images/editor/selectall.gif" WIDTH="18" HEIGHT="18" onClick="format('selectall')">
			</div>
			<div class="TBSep"></div>
			<div class="Btn" TITLE="删除" LANGUAGE="javascript" onClick="format('delete')">
				<img class="Ico" src="images/editor/del.gif" WIDTH="18" HEIGHT="18" onClick="format('delete')">
			</div>
			<div class="TBSep"></div>
			<div class="Btn" TITLE="剪切" LANGUAGE="javascript" onClick="format('cut')">
				<img class="Ico" src="images/editor/cut.gif" WIDTH="18" HEIGHT="18" onClick="format('cut')">
			</div>
			<div class="Btn" TITLE="复制" LANGUAGE="javascript" onClick="format('copy')">
				<img class="Ico" src="images/editor/copy.gif" WIDTH="18" HEIGHT="18" onClick="format('copy')">
			</div>
			<div class="Btn" TITLE="粘贴" LANGUAGE="javascript" onClick="format('paste')">
				<img class="Ico" src="images/editor/paste.gif" WIDTH="18" HEIGHT="18" onClick="format('paste')">
			</div>
			<div class="Btn" TITLE="从word中粘贴" LANGUAGE="javascript" onClick="word()">
				<img class="Ico" src="images/editor/wordpaste.gif" WIDTH="18" HEIGHT="18" onClick="word()">
			</div>
			<div class="TBSep"></div>
			<div class="Btn" TITLE="撤消" LANGUAGE="javascript" onClick="format('undo')">
				<img class="Ico" src="images/editor/undo.gif" WIDTH="18" HEIGHT="18" onClick="format('undo')">
			</div>
			<div class="Btn" TITLE="恢复" LANGUAGE="javascript" onClick="format('redo')">
				<img class="Ico" src="images/editor/redo.gif" WIDTH="18" HEIGHT="18" onClick="format('redo')">
			</div>
			<div class="TBSep"></div>
			<div class="Btn" TITLE="查找" LANGUAGE="javascript" onClick="findstr()">
				<img class="Ico" src="images/editor/find.gif" WIDTH="18" HEIGHT="18" onClick="findstr()">
			</div>
			<div class="Btn" TITLE="计算器" LANGUAGE="javascript" onClick="calculator()">
				<img class="Ico" src="images/editor/calculator.gif" WIDTH="18" HEIGHT="18" onClick="calculator()">
			</div>

			<div class="TBSep"></div>
			<div class="Btn" TITLE="插入超级连接" LANGUAGE="javascript" onClick="UserDialog('CreateLink')">
				<img class="Ico" src="images/editor/url.gif" WIDTH="18" HEIGHT="18" onClick="UserDialog('CreateLink')">
			</div>
			<div class="Btn" TITLE="取消超级链接" LANGUAGE="javascript" onClick="UserDialog('unLink')">
				<img class="Ico" src="images/editor/nourl.gif" WIDTH="18" HEIGHT="18" onClick="UserDialog('unLink')">
			</div>
			<div class="Btn" TITLE="删除文字格式" LANGUAGE="javascript" onClick="format('RemoveFormat')">
				<img class="Ico" src="images/editor/clear.gif" WIDTH="18" HEIGHT="18" onClick="format('RemoveFormat')">
			</div>
			<div class="Btn" TITLE="下标" LANGUAGE="javascript" onClick="format('subscript')">
				<img class="Ico" src="images/editor/sub.gif" WIDTH="18" HEIGHT="18" onClick="format('subscript')">
			</div>
			  <div class="Btn" TITLE="插入滚动文本" LANGUAGE="javascript" onClick="insermarquee()"> 
    <img class="Ico" src="images/editor/Marquee.gif" WIDTH="18" HEIGHT="18" onClick="insermarquee()"> </div>
				<div class="Btn" TITLE="上标" LANGUAGE="javascript" onClick="format('superscript')">
				<img class="Ico" src="images/editor/sup.gif" WIDTH="18" HEIGHT="18" onClick="format('superscript')">
			</div>			<div class="TBSep"></div>
			<div class="Btn" TITLE="查看帮助" LANGUAGE="javascript" onClick="help()"><img class="Ico" onClick="help()" src="images/editor/help.gif" WIDTH="18" HEIGHT="18">
			</div>
		</div>
		<div class="yToolbar">
			<div class="TBHandle">
			</div>
			<select ID="formatSelect" class="TBGen" onChange="format('FormatBlock',this[this.selectedIndex].value);this.selectedIndex=0">
				<option selected>段落格式</option>
				<option VALUE="&lt;P&gt;">普通</option>
				<option VALUE="&lt;PRE&gt;">已编排格式</option>
				<option VALUE="&lt;H1&gt;">标题一</option>
				<option VALUE="&lt;H2&gt;">标题二</option>
				<option VALUE="&lt;H3&gt;">标题三</option>
				<option VALUE="&lt;H4&gt;">标题四</option>
				<option VALUE="&lt;H5&gt;">标题五</option>
				<option VALUE="&lt;H6&gt;">标题六</option>
				<option VALUE="&lt;H7&gt;">标题七</option>
			</select>
			<select id="FontName" class="TBGen" onChange="format('fontname',this[this.selectedIndex].value);this.selectedIndex=0">
				<option selected>字体</option>
				<option value="宋体">宋体</option>
				<option value="黑体">黑体</option>
				<option value="楷体_GB2312">楷体</option>
				<option value="仿宋_GB2312">仿宋</option>
				<option value="隶书">隶书</option>
				<option value="幼圆">幼圆</option>
				<option value="Arial">Arial</option>
				<option value="Arial Black">Arial Black</option>
				<option value="Arial Narrow">Arial Narrow</option>

			</select>
			<select id="FontSize" class="TBGen" onChange="format('fontsize',this[this.selectedIndex].value);this.selectedIndex=0">
				<option selected>字号</option>
				<option value="1">一号</option>
				<option value="2">二号</option>
				<option value="3">三号</option>
				<option value="4">四号</option>
				<option value="5">五号</option>
				<option value="6">六号</option>
				<option value="7">七号</option>
			</select>
			<div class="TBSep"></div>
			<div class="Btn" TITLE="字体颜色" LANGUAGE="javascript" onClick="foreColor()">
				<img class="Ico" src="images/editor/fgcolor.gif" WIDTH="18" HEIGHT="18" onClick="foreColor()">
			</div>
			<div class="Btn" TITLE="文字背景色" LANGUAGE="javascript" onClick="backColor()">
				<img class="Ico" src="images/editor/fgbgcolor.gif" WIDTH="18" HEIGHT="18" onClick="backColor()">
			</div>
			<div class="TBSep"></div>
			<div class="Btn" TITLE="加粗" LANGUAGE="javascript" onClick="format('bold')">
				<img class="Ico" src="images/editor/bold.gif" WIDTH="18" HEIGHT="18" onClick="format('bold')">
			</div>
			<div class="Btn" TITLE="斜体" LANGUAGE="javascript" onClick="format('italic')">
				<img class="Ico" src="images/editor/italic.gif" WIDTH="18" HEIGHT="18" onClick="format('italic')">
			</div>
			<div class="Btn" TITLE="下划线" LANGUAGE="javascript" onClick="format('underline')">
				<img class="Ico" src="images/editor/underline.gif" WIDTH="18" HEIGHT="18" onClick="format('underline')">
			</div>
			<div class="TBSep"></div>
			<div class="Btn" TITLE="左对齐" NAME="Justify" LANGUAGE="javascript" onClick="format('justifyleft')">
				<img class="Ico" src="images/editor/aleft.gif" WIDTH="18" HEIGHT="18" onClick="format('justifyleft')">
			</div>
			<div class="Btn" TITLE="居中" NAME="Justify" LANGUAGE="javascript" onClick="format('justifycenter')">
				<img class="Ico" src="images/editor/acenter.gif" WIDTH="18" HEIGHT="18" onClick="format('justifycenter')">
			</div>
			<div class="Btn" TITLE="右对齐" NAME="Justify" LANGUAGE="javascript" onClick="format('justifyright')">
				<img class="Ico" src="images/editor/aright.gif" WIDTH="18" HEIGHT="18" onClick="format('justifyright')">
			</div>
		</div>
		<div class="yToolbar">
			<div class="TBHandle">
			</div>
			<div class="Btn" TITLE="编号" LANGUAGE="javascript" onClick="format('insertorderedlist')">
				<img class="Ico" src="images/editor/num.gif" WIDTH="18" HEIGHT="18" onClick="format('insertorderedlist')">
			</div>
			<div class="Btn" TITLE="项目符号" LANGUAGE="javascript" onClick="format('insertunorderedlist')">
				<img class="Ico" src="images/editor/list.gif" WIDTH="18" HEIGHT="18" onClick="format('insertunorderedlist')">
			</div>
			<div class="Btn" TITLE="减少缩进量" LANGUAGE="javascript" onClick="format('outdent')">
				<img class="Ico" src="images/editor/outdent.gif" WIDTH="18" HEIGHT="18" onClick="format('outdent')">
			</div>
			<div class="Btn" TITLE="增加缩进量" LANGUAGE="javascript" onClick="format('indent')">
				<img class="Ico" src="images/editor/indent.gif" WIDTH="18" HEIGHT="18" onClick="format('indent')">
			</div>
			<div class="TBSep"></div>
			
			
			<div class="Btn" TITLE="插入普通水平线" LANGUAGE="javascript" onClick="format('InsertHorizontalRule')">
				<img class="Ico" src="images/editor/line.gif" WIDTH="18" HEIGHT="18" onClick="format('InsertHorizontalRule')">
			</div>
			<div class="Btn" TITLE="插入特殊水平线" LANGUAGE="javascript" onClick="hr()"><img class="Ico" src="images/editor/sline.gif" WIDTH="18" HEIGHT="18" onClick="hr()">
			</div>
			<div class="Btn" TITLE="插入当前日期" LANGUAGE="javascript" onClick="nowdate()">
				<img class="Ico" src="images/editor/date.gif" WIDTH="18" HEIGHT="18" onClick="nowdate()">
			</div>
			<div class="Btn" TITLE="插入当前时间" LANGUAGE="javascript" onClick="nowtime()">
				<img class="Ico" src="images/editor/time.gif" WIDTH="18" HEIGHT="18" onClick="nowtime()">
			</div>
			<div class="Btn" TITLE="插入栏目框" LANGUAGE="javascript" onClick="FIELDSET()"><img class="Ico" onClick="FIELDSET()" src="images/editor/fieldset.gif" WIDTH="18" HEIGHT="18">
			</div>
			<div class="Btn" TITLE="插入网页" LANGUAGE="javascript" onClick="iframe()"><img class="Ico" onClick="iframe()" src="images/editor/htm.gif" WIDTH="18" HEIGHT="18">
			</div>
			<div class="Btn" TITLE="插入表格" LANGUAGE="javascript" onClick="InsertTable()">
				<img class="Ico" src="images/editor/table.gif" WIDTH="18" HEIGHT="18" onClick="InsertTable()">
			</div>
			<div class="BtnMenu" TITLE="表格操作" LANGUAGE="javascript" onClick="ShowMenu(menu_table,100)">
				<img class="Ico" src="images/editor/arrow.gif" WIDTH="5" HEIGHT="18" onClick="ShowMenu(menu_table,100)">
			</div>
			<div class="Btn" TITLE="插入换行符号" LANGUAGE="javascript" onClick="InsertChars(0)">
				<img class="Ico" src="images/editor/chars.gif" WIDTH="18" HEIGHT="18" onClick="InsertChars(0)">
			</div>
			<div class="BtnMenu" TITLE="更多特殊符号" LANGUAGE="javascript" onClick="ShowMenu(menu_chars,100)">
				<img class="Ico" src="images/editor/arrow.gif" WIDTH="5" HEIGHT="18" onClick="ShowMenu(menu_chars,100)">
			</div>
			<div class="TBSep"></div>
			<div class="Btn" TITLE="添加文档附件" LANGUAGE="javascript" onClick="excel()">
				<img class="Ico" src="images/editor/excel.gif" WIDTH="18" HEIGHT="18" onClick="excel()">
			</div>
			<div class="Btn" TITLE="插入图片，支持格式为：jpg、gif、bmp、png等" LANGUAGE="javascript" onClick="pic()">
			  <img class="Ico" src="images/editor/img.gif" WIDTH="18" HEIGHT="18"  onClick="pic()">
			</div>
			<!-- <div class="Btn" TITLE="插入flash多媒体文件" LANGUAGE="javascript" onClick="swf()">
				<img class="Ico" src="images/editor/flash.gif" WIDTH="18" HEIGHT="18" onClick="swf()">
			</div>
			<div class="Btn" TITLE="插入视频文件，支持格式为：avi、wmv、asf" LANGUAGE="javascript" onClick="wmv()">
				<img class="Ico" src="images/editor/wmv.gif" WIDTH="18" HEIGHT="18" onClick="wmv()">
			</div>
			<div class="Btn" TITLE="插入RealPlay文件，支持格式为：rm、ra、ram" LANGUAGE="javascript" onClick="rm()">
				<img class="Ico" src="images/editor/rm.gif" WIDTH="18" HEIGHT="18" onClick="rm()">
			</div> -->
		</div>
		<iframe class="HtmlEdit" ID="HtmlEdit" src="<%=request.getContextPath() %>/console/data/idsInformation.htm?action=getIdsContent&cid=${param.cid }" MARGINHEIGHT="1" MARGINWIDTH="1" style="width=100%; height=296;">
		</iframe>
		<iframe class="HtmlEdit" ID="HtmlPreview" MARGINHEIGHT="1" MARGINWIDTH="1" style="width=100%; height=296; display:none"></iframe>
		<img id="setMode0" src="images/editor/Editor2.gif" width="59" height="20" onClick="setMode(0)">
		<img id="setMode1" src="images/editor/html.gif" width="59" height="20" onClick="setMode(1)">
		<img id="setMode2" src="images/editor/browse.gif" width="59" height="20" onClick="setMode(2)">
		<script type="text/javascript">
SEP_PADDING = 5;
HANDLE_PADDING = 7;

var yToolbars =	new Array();
var YInitialized = false;
var bLoad=false;
var pureText=true;
var bodyTag="<head><style type=\"text/css\">body {font-size:	9pt}</style><meta http-equiv=Content-Type content=\"text/html; charset=gb2312\"></head><BODY bgcolor=\"#FFFFFF\" MONOSPACE>";
var EditMode=true;
var SourceMode=false;
var PreviewMode=false;
var CurrentMode=0;
function document.onreadystatechange(){
  if (YInitialized) return;
  YInitialized = true;

  var i, s, curr;

  for (i=0; i<document.body.all.length;	i++)
  {
    curr=document.body.all[i];
    if (curr.className == "yToolbar")
    {
      InitTB(curr);
      yToolbars[yToolbars.length] = curr;
    }
  }

  DoLayout();
  window.onresize = DoLayout;

  //HtmlEdit.document.open();
  //HtmlEdit.document.write(bodyTag);
  //HtmlEdit.document.close();
  HtmlEdit.document.designMode="On";
}

function InitBtn(btn)
{
  btn.onmouseover = BtnMouseOver;
  btn.onmouseout = BtnMouseOut;
  btn.onmousedown = BtnMouseDown;
  btn.onmouseup	= BtnMouseUp;
  btn.ondragstart = YCancelEvent;
  btn.onselectstart = YCancelEvent;
  btn.onselect = YCancelEvent;
  btn.YUSERONCLICK = btn.onclick;
  btn.onclick =	YCancelEvent;
  btn.YINITIALIZED = true;
  return true;
}

function InitBtnMenu(BtnMenu)
{
  BtnMenu.onmouseover = BtnMenuMouseOver;
  BtnMenu.onmouseout = BtnMenuMouseOut;
  BtnMenu.onmousedown = BtnMenuMouseDown;
  BtnMenu.onmouseup	= BtnMenuMouseUp;
  BtnMenu.ondragstart = YCancelEvent;
  BtnMenu.onselectstart = YCancelEvent;
  BtnMenu.onselect = YCancelEvent;
  BtnMenu.YUSERONCLICK = BtnMenu.onclick;
  BtnMenu.onclick =	YCancelEvent;
  BtnMenu.YINITIALIZED = true;
  return true;
}

function InitTB(y)
{
  y.TBWidth = 0;

  if (!	PopulateTB(y)) return false;

  y.style.posWidth = y.TBWidth;

  return true;
}


function YCancelEvent()
{
  event.returnValue=false;
  event.cancelBubble=true;
  return false;
}

function PopulateTB(y)
{
  var i, elements, element;

  elements = y.children;
  for (i=0; i<elements.length; i++) {
    element = elements[i];
    if (element.tagName	== "SCRIPT" || element.tagName == "!") continue;

    switch (element.className) {
      case "Btn":
        if (element.YINITIALIZED == null)	{
          if (! InitBtn(element))
          return false;
        }
        element.style.posLeft = y.TBWidth;
        y.TBWidth	+= element.offsetWidth + 1;
        break;
      
	  case "BtnMenu":
        if (element.YINITIALIZED == null)	{
          if (! InitBtnMenu(element))
          return false;
        }
        element.style.posLeft = y.TBWidth;
        y.TBWidth	+= element.offsetWidth + 1;
        break;

      case "TBGen":
        element.style.posLeft = y.TBWidth;
        y.TBWidth	+= element.offsetWidth + 1;
        break;

      case "TBSep":
        element.style.posLeft = y.TBWidth	+ 2;
        y.TBWidth	+= SEP_PADDING;
        break;

      case "TBHandle":
        element.style.posLeft = 2;
        y.TBWidth	+= element.offsetWidth + HANDLE_PADDING;
        break;

      default:
        return false;
      }
  }

  y.TBWidth += 1;
  return true;
}

function DebugObject(obj)
{
  var msg = "";
  for (var i in	TB) {
    ans=prompt(i+"="+TB[i]+"\n");
    if (! ans) break;
  }
}

function LayoutTBs()
{
  NumTBs = yToolbars.length;

  if (NumTBs ==	0) return;

  var i;
  var ScrWid = (document.body.offsetWidth) - 6;
  var TotalLen = ScrWid;
  for (i = 0 ; i < NumTBs ; i++) {
    TB = yToolbars[i];
    if (TB.TBWidth > TotalLen) TotalLen	= TB.TBWidth;
  }

  var PrevTB;
  var LastStart	= 0;
  var RelTop = 0;
  var LastWid, CurrWid;
  var TB = yToolbars[0];
  TB.style.posTop = 0;
  TB.style.posLeft = 0;

  var Start = TB.TBWidth;
  for (i = 1 ; i < yToolbars.length ; i++) {
    PrevTB = TB;
    TB = yToolbars[i];
    CurrWid = TB.TBWidth;

    if ((Start + CurrWid) > ScrWid) {
      Start = 0;
      LastWid =	TotalLen - LastStart;
    }
    else {
       LastWid =	PrevTB.TBWidth;
       RelTop -=	TB.offsetHeight;
    }

    TB.style.posTop = RelTop;
    TB.style.posLeft = Start;
    PrevTB.style.width = LastWid;

    LastStart =	Start;
    Start += CurrWid;
  }

  TB.style.width = TotalLen - LastStart;

  i--;
  TB = yToolbars[i];
  var TBInd = TB.sourceIndex;
  var A	= TB.document.all;
  var item;
  for (i in A) {
    item = A.item(i);
    if (! item)	continue;
    if (! item.style) continue;
    if (item.sourceIndex <= TBInd) continue;
    if (item.style.position == "absolute") continue;
    item.style.posTop =	RelTop;
  }
}

function DoLayout()
{
  LayoutTBs();
}

function BtnMouseOver()
{
  if (event.srcElement.tagName != "IMG") return	false;
  var image = event.srcElement;
  var element =	image.parentElement;

  if (image.className == "Ico")	element.className = "BtnMouseOverUp";
  else if (image.className == "IcoDown") element.className = "BtnMouseOverDown";

  event.cancelBubble = true;
}

function BtnMouseOut()
{
  if (event.srcElement.tagName != "IMG") {
    event.cancelBubble = true;
    return false;
  }

  var image = event.srcElement;
  var element =	image.parentElement;
  yRaisedElement = null;

  element.className = "Btn";
  image.className = "Ico";

  event.cancelBubble = true;
}

function BtnMouseDown()
{
  if (event.srcElement.tagName != "IMG") {
    event.cancelBubble = true;
    event.returnValue=false;
    return false;
  }

  var image = event.srcElement;
  var element =	image.parentElement;

  element.className = "BtnMouseOverDown";
  image.className = "IcoDown";

  event.cancelBubble = true;
  event.returnValue=false;
  return false;
}

function BtnMouseUp()
{
  if (event.srcElement.tagName != "IMG") {
    event.cancelBubble = true;
    return false;
  }

  var image = event.srcElement;
  var element =	image.parentElement;

  if (element.YUSERONCLICK) eval(element.YUSERONCLICK +	"anonymous()");

  element.className = "BtnMouseOverUp";
  image.className = "Ico";

  event.cancelBubble = true;
  return false;
}

function BtnMenuMouseOver()
{
  if (event.srcElement.tagName != "IMG") return	false;
  var image = event.srcElement;
  var element =	image.parentElement;

  if (image.className == "Ico")	element.className = "BtnMenuMouseOverUp";
  else if (image.className == "IcoDown") element.className = "BtnMenuMouseOverDown";

  event.cancelBubble = true;
}

function BtnMenuMouseOut()
{
  if (event.srcElement.tagName != "IMG") {
    event.cancelBubble = true;
    return false;
  }

  var image = event.srcElement;
  var element =	image.parentElement;
  yRaisedElement = null;

  element.className = "BtnMenu";
  image.className = "Ico";

  event.cancelBubble = true;
}

function BtnMenuMouseDown()
{
  if (event.srcElement.tagName != "IMG") {
    event.cancelBubble = true;
    event.returnValue=false;
    return false;
  }

  var image = event.srcElement;
  var element =	image.parentElement;

  element.className = "BtnMenuMouseOverDown";
  image.className = "IcoDown";

  event.cancelBubble = true;
  event.returnValue=false;
  return false;
}

function BtnMenuMouseUp()
{
  if (event.srcElement.tagName != "IMG") {
    event.cancelBubble = true;
    return false;
  }

  var image = event.srcElement;
  var element =	image.parentElement;

  if (element.YUSERONCLICK) eval(element.YUSERONCLICK +	"anonymous()");

  element.className = "BtnMenuMouseOverUp";
  image.className = "Ico";

  event.cancelBubble = true;
  return false;
}

function cleanHtml()
{
  var fonts = HtmlEdit.document.body.all.tags("FONT");
  var curr;
  for (var i = fonts.length - 1; i >= 0; i--) {
    curr = fonts[i];
    if (curr.style.backgroundColor == "#ffffff") curr.outerHTML	= curr.innerHTML;
  }
}

function validateMode()
{
  if (EditMode) return true;
  alert("请先点编辑器下方的“编辑”按钮，进入“编辑”状态，然后再使用系统编辑功能!");
  HtmlEdit.focus();
  return false;
}

function UserDialog(what)
{
  if (!validateMode()) return;

  HtmlEdit.document.execCommand(what, true);

  pureText = false;
  HtmlEdit.focus();
}

function format(what,opt)
{
  if (!validateMode()) return;
  if (opt=="removeFormat")
  {
    what=opt;
    opt=null;
  }

  if (opt==null) HtmlEdit.document.execCommand(what);
  else HtmlEdit.document.execCommand(what,"",opt);

  pureText = false;
  HtmlEdit.focus();
}

function setMode(newMode)
{
  var cont;
  if (CurrentMode==newMode){
    return false;
  }
  
  if (newMode==0)
  {
	setMode0.src="images/editor/Editor2.gif";
	setMode1.src="images/editor/html.gif";
	setMode2.src="images/editor/browse.gif";
	if (PreviewMode){
	  document.all.HtmlEdit.style.display="";
	  document.all.HtmlPreview.style.display="none";
	}
	if(SourceMode){
	  cont=HtmlEdit.document.body.innerText;
      HtmlEdit.document.body.innerHTML=cont;
	}
    EditMode=true;
	SourceMode=false;
	PreviewMode=false;
  }
  else if (newMode==1)
  {
	setMode0.src="images/editor/Editor.gif";
	setMode1.src="images/editor/html2.gif";
	setMode2.src="images/editor/browse.gif";
	if (PreviewMode){
	  document.all.HtmlEdit.style.display="";
	  document.all.HtmlPreview.style.display="none";
	}
	if(EditMode){
	  cleanHtml();
      cleanHtml();
      cont=HtmlEdit.document.body.innerHTML;
      HtmlEdit.document.body.innerText=cont;
	}
    EditMode=false;
	SourceMode=true;
	PreviewMode=false;
  }
  else if (newMode==2)
  {
	setMode0.src="images/editor/Editor.gif";
	setMode1.src="images/editor/html.gif";
	setMode2.src="images/editor/browse2.gif";
	var str1="<head><style type=\"text/css\">body {font-size:	9pt}</style><meta http-equiv=Content-Type content=\"text/html; charset=gb2312\"></head><BODY bgcolor=\"#F6F6F6\" MONOSPACE>";
	if(CurrentMode==0){
	  str1=str1+HtmlEdit.document.body.innerHTML;
	}
	else{
	  str1=str1+HtmlEdit.document.body.innerText;
	}
    HtmlPreview.document.open();
	HtmlPreview.document.write(str1);
    HtmlPreview.document.close();
    document.all.HtmlEdit.style.display="none";
	document.all.HtmlPreview.style.display="";
	PreviewMode=true;
  }
  CurrentMode=newMode;
  HtmlEdit.focus();
}

function foreColor()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var RangeType = HtmlEdit.document.selection.type;
  if (RangeType != "Text"){
    alert("请先选择一段文字！");
    return;
  }
  var arr = showModalDialog("editor_selcolor.html", "", "dialogWidth:18.5em; dialogHeight:17.5em; help: no; scroll: no; status: no");
  if (arr != null) format('forecolor', arr);
  else HtmlEdit.focus();
}

function backColor()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var RangeType = HtmlEdit.document.selection.type;
  if (RangeType != "Text"){
    alert("请先选择一段文字！");
    return;
  }
  var arr = showModalDialog("editor_selcolor.html", "", "dialogWidth:18.5em; dialogHeight:17.5em; help: no; scroll: no; status: no");
  if (arr != null){
    range.pasteHTML("<span style='background-color:"+arr+"'>"+range.text+"</span> ");
	range.select();
  }
  HtmlEdit.focus();
}
function page()
{
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  if(range.text!=""){
    alert("请不要选择任何文本");
  }
  else{
    range.text="\n\n[NextPage]\n\n";
	parent.selectPaginationType()
  }
}

function InsertTable()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_inserttable.html", "", "dialogWidth:450px;dialogHeight:200px;help: no; scroll: no; status: no");

  if (arr != null){
	range.pasteHTML(arr);
  }
  HtmlEdit.focus();
}

function FIELDSET()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_fieldset.html", "", "dialogWidth:25em; dialogHeight:12.5em; help: no; scroll: no; status: no");
  if (arr != null){
    range.pasteHTML(arr);
  }
  HtmlEdit.focus();
}

function iframe()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_insertiframe.html", "", "dialogWidth:30em; dialogHeight:12em; help: no; scroll: no; status: no");  
  if (arr != null){
    range.pasteHTML(arr);
  }
  HtmlEdit.focus();
}

function hr()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_inserthr.html", "", "dialogWidth:30em; dialogHeight:12em; help: no; scroll: no; status: no"); 
  if (arr != null){
    range.pasteHTML(arr);
  }
  HtmlEdit.focus();
}

function pic()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_insertpic.html", "", "dialogWidth:31em; dialogHeight:17em; help: no; scroll: no; status: no");  
  if (arr != null){
    var ss=arr.split("$$$");
    range.pasteHTML(ss[0]);
	if (ss[1]!="None")
	{
	  parent.AddItem(ss[1]);
	}
	if(ss[3]!="0")
	{
	 parent.document.all.image.value=ss[2];
	}
  }
  HtmlEdit.focus();
}

function swf()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_insertflash.html", "", "dialogWidth:31em; dialogHeight:12em; help: no; scroll: no; status: no"); 
  if (arr != null){
    var ss=arr.split("$$$");
    range.pasteHTML(ss[0]);
   // parent.frmAnnounce.IncludePic.checked=true;
	if (ss[1]!="None")
	{
	  parent.AddItem(ss[1]);
	}

  }
  HtmlEdit.focus();
}

function wmv()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_insertmedia.html", "", "dialogWidth:31em; dialogHeight:12em; help: no; scroll: no; status: no");
  if (arr != null){
    var ss=arr.split("$$$");
    range.pasteHTML(ss[0]);
    //parent.frmAnnounce.IncludePic.checked=true;
	if (ss[1]!="None")
	{
	  parent.AddItem(ss[1]);
	}
	//parent.document.frmAnnounce.shipins.value=parseInt(parent.document.frmAnnounce.shipins.value)+1;
  }
  HtmlEdit.focus();
}


function rm()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_insertrm.html", "", "dialogWidth:31em; dialogHeight:12em; help: no; scroll: no; status: no");  
  if (arr != null){
    var ss=arr.split("$$$");
    range.pasteHTML(ss[0]);
    //parent.frmAnnounce.IncludePic.checked=true;
	if (ss[1]!="None")
	{
	  parent.AddItem(ss[1]);
	}
	//parent.document.frmAnnounce.shipins.value=parseInt(parent.document.frmAnnounce.shipins.value)+1;
  }
  HtmlEdit.focus();
}

function excel()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_insertword.html", "", "dialogWidth:31em; dialogHeight:200px; help: no; scroll: no; status: no");  
  if (arr != null){
    var ss=arr.split("$$$");
    range.pasteHTML(ss[0]);
	//parent.document.frmAnnounce.Pics.value=parseInt(parent.document.frmAnnounce.Pics.value)+1;
  }
  HtmlEdit.focus();
}

function nowdate()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range =HtmlEdit.document.selection.createRange();
  var d = new Date();
  var str1=d.getYear()+"年"+(d.getMonth() + 1)+"月"+d.getDate() +"日";
  range.pasteHTML(str1);
  HtmlEdit.focus();
}

function nowtime()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range =HtmlEdit.document.selection.createRange();
  var d = new Date();
  var str1=d.getHours() +":"+d.getMinutes()+":"+d.getSeconds();
  range.pasteHTML(str1);
  HtmlEdit.focus();
}

function findstr()
{
  if (!	validateMode())	return;
  var arr = showModalDialog("editor_find.html", window, "dialogWidth:420px; dialogHeight:125px; help: no; scroll: no; status: no");
}

function help()
{
  var arr = showModalDialog("help.html", "", "dialogWidth:300px; dialogHeight:268px; help: no; scroll: no; status: no");
}

function save()
{
  if (CurrentMode==0){
//编辑器嵌入其他网页时使用下面这一句（请将form1改成相应表单名）
    window.parent.document.getElementById('content').value=HtmlEdit.document.body.innerHTML;
//单独打开编辑器时使用下面这一句（请将form1改成相应表单名）  
//  self.opener.form1.content.value+=HtmlEdit.document.body.innerHTML;
  }
  else if(CurrentMode==1){
//编辑器嵌入其他网页时使用下面这一句（请将form1改成相应表单名）
    window.parent.document.getElementById('content').value=HtmlEdit.document.body.innerText;
//单独打开编辑器时使用下面这一句（请将form1改成相应表单名）  
//  self.opener.form1.content.value+=HtmlEdit.document.body.innerText;
  }
  else
  {
    alert("预览状态不能保存！请先回到编辑状态后再保存");
  }
  HtmlEdit.focus();
}


function tablecommand(command)
{
	var cellflag=false;
	var rowflag=false;
	var tableflag=false;
	var cellindex,rowindex,tableref;
	HtmlEdit.focus();
	var xsel=HtmlEdit.document.selection;
	var xobj=HtmlEdit.document.selection.createRange();
	if(xsel.type=="None"||xsel.type=="Text"){
		xsel=xobj.parentElement();
		while(xsel.tagName!="BODY"&&cellflag==false){
			if(xsel.tagName=="TD"){cellindex=xsel.cellIndex;cellflag=true;}
			if(cellflag==false){xsel=xsel.parentElement;}
		}
	}else if(xsel.type=="Control"){
		xsel=xobj.item(0);
		if(xsel.tagName=="TD"){
			cellindex=xsel.cellIndex;
			cellflag=true;
		}else{
			while(xsel.tagName!="BODY"&&cellflag==false){
				if(xsel.tagName=="TD"){cellindex=xsel.cellIndex;cellflag=true;}
				if(cellflag==false){xsel=xsel.parentElement;}
			}
		}
	}
	if(cellflag==true){
		xsel=HtmlEdit.document.selection;
		xobj=HtmlEdit.document.selection.createRange();
		if(xsel.type=="None"||xsel.type=="Text"){
			xsel=xobj.parentElement();
			while(xsel.tagName!="BODY"&&rowflag==false){
				if(xsel.tagName=="TR"){
					rowindex=xsel.rowIndex;
					rowflag=true;
				}
				if(rowflag==false){xsel=xsel.parentElement;}
			}
		}else if(xsel.type=="Control"){
			xsel=xobj.item(0);
			if(xsel.tagName=="TR"){
				rowindex=xsel.rowIndex;
				rowflag=true;
			}else{
				while(xsel.tagName!="BODY"&&rowflag==false){
					if(xsel.tagName=="TR"){
						rowindex=xsel.rowIndex;
						rowflag=true;
					}
					if(rowflag==false){
						xsel=xsel.parentElement;
					}
				}
			}
		}
		xsel=HtmlEdit.document.selection;
		xobj=HtmlEdit.document.selection.createRange();
		if(xsel.type=="None"||xsel.type=="Text"){
			xsel=xobj.parentElement();
			while(xsel.tagName!="BODY"&&tableflag==false){
				if(xsel.tagName=="TABLE"){tableflag=true;}
				if(tableflag==false){xsel=xsel.parentElement;}
			}
		}else if(xsel.type=="Control"){
			xsel=xobj.item(0);
			if(xsel.tagName=="TABLE"){
				tableflag=true;
			}else{
				while(xsel.tagName!="BODY"&&tableflag==false){
					if(xsel.tagName=="TABLE"){tableflag=true;}
					if(tableflag==false){xsel=xsel.parentElement;}
				}
			}
		}
		if(command==3){
			var temprowcount=xsel.rows.length;
			var tempcell;
			var tempspancount=0;
			var tempspanholder;
			var tempcellwidth=xsel.rows[rowindex].cells[cellindex].width;
			var xpositequiv=-1;
			var xposcount=0;
			while(xposcount<=cellindex){
				xpositequiv+=parseInt(xsel.rows[rowindex].cells[xposcount].colSpan);
				xposcount++;
			}
			var ypositequiv=-1;
			var yposcount=0;
			var ymax=xsel.rows[rowindex].cells.length;
			while(yposcount<=ymax-1){
				ypositequiv+=parseInt(xsel.rows[rowindex].cells[yposcount].colSpan);
				yposcount++;
			}
			var idealinsert=xpositequiv+1;
			var zi2=0;
			var zirowtouse=0;
			var zirowtot=xsel.rows.length;
			var rowarray=new Array(zirowtot);
			var rowarray2=new Array(zirowtot);
			for(init1=0;init1<=zirowtot-1;init1++){
				rowarray[init1]=0;
				rowarray2[init1]=0;
			}
			for(zi1=0;zi1<=zirowtot-1;zi1++){
				zi2=0;
				while(zi2<idealinsert&&(rowarray[zi1]==null||rowarray[zi1]<idealinsert)){
					rowarray[zi1]+=parseInt(xsel.rows[zi1].cells[zi2].colSpan);
					rowarray2[zi1]++;
					zi2++;
				}
			}
			var allequal=true;
			var zi3a,zi3b;
			var zthemax=0;
			for(zi3=0;zi3<=zirowtot-1;zi3++){
				zi3a=rowarray[0];
				zi3b=rowarray[zi3];
				if(zi3b>zthemax){zthemax=zi3b;}
				if(zi3a!=zi3b){allequal=false;}
			}
			if(allequal==false){
				var zi4=0;
				var allequal2=true;
				while(zthemax<=ypositequiv&&allequal==false){
					for(zi5=0;zi5<=zirowtot-1;zi5++){
						rowarray[zi5]+=parseInt(xsel.rows[zi5].cells[rowarray2[zi5]].colSpan);
					}
					for(zi3=0;zi3<=zirowtot-1;zi3++){
						zi3a=rowarray[0];
						zi3b=rowarray[zi3];
						if(zi3b>zthemax){zthemax=zi3b;}
						if(zi3a!=zi3b){allequal2=false;}
					}
					if(allequal2==true){allequal=true;}
					for(zi8=0;zi8<=zirowtot-1;zi8++){rowarray2[zi8]++;}
					}
				}
				var zi9;
				for(zi7=0;zi7<=zirowtot-1;zi7++){
					zi9=xsel.rows[zi7].insertCell(rowarray2[zi7]);
					zi9.width=tempcellwidth;
				}
		}else if(command==4){
			var temprowcount=xsel.rows.length;
			for(iccount=0;iccount<=temprowcount-1;iccount++){
				xsel.rows[iccount].deleteCell(cellindex);
			}
			}else if(command==1){
				var tempcell;
				var tempcellb;
				var tempcellcount=xsel.rows[rowindex].cells.length;
				var cellcolarray=new Array(tempcellcount);
				var cellrowarray=new Array(tempcellcount);
				for(cacount=0;cacount<=tempcellcount-1;cacount++){
					cellcolarray[cacount]=xsel.rows[rowindex].cells(cacount).colSpan;
					cellrowarray[cacount]=xsel.rows[rowindex].cells(cacount).rowSpan;
				}
				tempcell=xsel.insertRow(rowindex);
				for(cbcount=0;cbcount<=tempcellcount-1;cbcount++){
					tempcellb=tempcell.insertCell();
					if(cellcolarray[cbcount]!=1){tempcellb.colSpan=cellcolarray[cbcount];}
				}
		}else if(command==2){
				var temprowcount=xsel.rows.length;tempcell=xsel.deleteRow(rowindex);
		}else if(command==5){
				if(xsel.rows[rowindex].cells[cellindex+1]){
					var x=parseInt(xsel.rows[rowindex].cells[cellindex].colSpan)+parseInt(xsel.rows[rowindex].cells[cellindex+1].colSpan);
					var y=xsel.rows[rowindex].cells[cellindex].innerHTML+" "+xsel.rows[rowindex].cells[cellindex+1].innerHTML;
					xsel.rows[rowindex].deleteCell(cellindex+1);
					xsel.rows[rowindex].cells[cellindex].colSpan=x;
					xsel.rows[rowindex].cells[cellindex].innerHTML=y;
				}
		}else if(command==6){
				var yatemprow=xsel.rows.length;
				var yamax=0;
				for(ya1=0;ya1<=yatemprow-1;ya1++){
					var ypositequiv=-1;
					var yposcount=0;
					var ymax=xsel.rows[ya1].cells.length;
					while(yposcount<=ymax-1){
						ypositequiv+=parseInt(xsel.rows[ya1].cells[yposcount].colSpan);
						yposcount++;
					}
					if(ypositequiv>yamax){yamax=ypositequiv;}
				}
				var rowarray=new Array();
				var rowarray2=new Array();
				var myrowcount=xsel.rows.length;
				for(ra1=0;ra1<=myrowcount-1;ra1++){
					rowarray[ra1]=new Array();
					rowarray2[ra1]=0;
					for(cr1=0;cr1<=yamax;cr1++){rowarray[ra1][cr1]=777;}
				}
				var tempra;
				var ra2=0;
				for(ra3=0;ra3<=yamax;ra3++){
					ra2=0;
					while(ra2<=myrowcount-1){
						if(xsel.rows[ra2].cells[ra3]){
							tempra=parseInt(xsel.rows[ra2].cells[ra3].rowSpan);
							if(tempra>1){
								rowarray[ra2][ra3]=ra3+rowarray2[ra2];
								for(zoo=1;zoo<=tempra-1;zoo++){rowarray2[ra2+zoo]--;}
							}
						}
						if(rowarray[ra2][ra3-1]!=ra3+rowarray2[ra2]){
							rowarray[ra2][ra3]=ra3+rowarray2[ra2];
						}else{
							rowarray[ra2][ra3]=555;
						}
						ra2++;
					}
				}
				var samx="";
				var samcount=0;
				for(rx1=0;rx1<=myrowcount-1;rx1++){
					samcount=rowarray[rx1].length;
					for(rx2=0;rx2<=samcount-1;rx2++){
						samx+="-"+rowarray[rx1][rx2];
					}
					samx+="\n";
				}
				var j=parseInt(xsel.rows[rowindex].cells[cellindex].rowSpan);
				var jcount=rowarray[rowindex].length;
				var jval=0;
				for(jc1=0;jc1<=jcount-1;jc1++){
					if(rowarray[rowindex][jc1]==cellindex){jval=jc1;}
				}
				if(xsel.rows[rowindex+j]){
					var cellindex2=rowarray[rowindex+j][jval];
					var x=parseInt(xsel.rows[rowindex].cells[cellindex].rowSpan)+parseInt(xsel.rows[rowindex+j].cells[cellindex2].rowSpan);
					var y=xsel.rows[rowindex].cells[cellindex].innerHTML+" "+xsel.rows[rowindex+j].cells[cellindex2].innerHTML;
					xsel.rows[rowindex+j].deleteCell(cellindex2);
					xsel.rows[rowindex].cells[cellindex].rowSpan=x;
					xsel.rows[rowindex].cells[cellindex].innerHTML=y;
				}
		}else if(command==7){
				var getrowspan=parseInt(xsel.rows[rowindex].cells[cellindex].rowSpan);
				var getcolspan=parseInt(xsel.rows[rowindex].cells[cellindex].colSpan);
				if(getrowspan>1){
					var xr1=getrowspan-1;
					var xrposit=rowindex;
					var xrcposit=cellindex;
					var xrholder;xsel.rows[rowindex].cells[cellindex].rowSpan=1;
					for(xr2=1;xr2<=xr1;xr2++){
						xrholder=xsel.rows[xrposit+xr2].insertCell(xrcposit);
						xrholder.colSpan=xsel.rows[rowindex].cells[cellindex].colSpan;
					}
				}
				if(getcolspan>1){
					var yr1=getcolspan-1;
					var yrposit=rowindex;
					var yrcposit=cellindex;
					var yrholder;xsel.rows[rowindex].cells[cellindex].colSpan=1;
					for(yr2=1;yr2<=yr1;yr2++){
						yrholder=xsel.rows[yrposit].insertCell(yrcposit);
						yrholder.rowSpan=xsel.rows[rowindex].cells[cellindex].rowSpan;
					}
				}
			}
		}
}


function tableProp(){
	var tableflag=false;
	HtmlEdit.focus();
	var xsel=HtmlEdit.document.selection;
	var xobj=HtmlEdit.document.selection.createRange();
	if(xsel.type=="None"||xsel.type=="Text"){
		xsel=xobj.parentElement();
		while(xsel.tagName!="BODY"&&tableflag==false){
			if(xsel.tagName=="TABLE"){tableflag=true;}
			if(tableflag==false){xsel=xsel.parentElement;}
		}
	}else if(xsel.type=="Control"){
		xsel=xobj.item(0);
		if(xsel.tagName=="TABLE"){
			tableflag=true;
		}else{
			while(xsel.tagName!="BODY"&&tableflag==false){
				if(xsel.tagName=="TABLE"){tableflag=true;}
				if(tableflag==false){xsel=xsel.parentElement;}
			}
		}
	}
	if(tableflag==true){
		if(xsel.className!=""&&xsel.className!=null){tableclass=xsel.className;}else{tableclass="";}
		if(xsel.width!=""&&xsel.width!=null){tablewidthspecified="yes";tablewidth=xsel.width;}else{tablewidthspecified="no";tablewith="";}
		if(xsel.align!=""&&xsel.align!=null){tablealign=xsel.align;}else{tablealign="";}
		if(xsel.border!=""&&xsel.border!=null){tablebordersize=xsel.border;}else{tablebordersize="";}
		if(xsel.cellPadding!=""&&xsel.cellPadding!=null){tablecellpadding=xsel.cellPadding;}else{tablecellpadding="";}
		if(xsel.cellSpacing!=""&&xsel.cellSpacing!=null){tablecellspacing=xsel.cellSpacing;}else{tablecellspacing="";}
		if(xsel.borderColor!=""&&xsel.borderColor!=null){tablebordercolor=xsel.borderColor;}else{tablebordercolor="";}
		if(xsel.bgColor!=""&&xsel.bgColor!=null){tablebackgroundcolor=xsel.bgColor;}else{tablebackgroundcolor="";}
		tableiscancel="";
		window.showModalDialog("editor_tableprops.asp",window," dialogWidth: 350px; dialogHeight: 300px; help: no;scroll: no; status: no");
		if(tableiscancel=="no"){
			if(tablewidthspecified=="yes"){
				var tw1="";
				if(tablewidthtype=="percentage"){
					tw1=tablewidth+"%";
				}else{
					tw1=tablewidth;
				}
				xsel.width=tw1;
			}else{
				xsel.removeAttribute("width",0);
			}
			if(tablealign!=""&&tablealign!="Default"){xsel.align=tablealign;}else{xsel.removeAttribute("align",0);}
			if(tableclass!=""&&tableclass!="Default"){xsel.className=tableclass;}else{xsel.removeAttribute("className",0);}
			if(tablebordersize!=""&&tablebordersize!=null){xsel.border=tablebordersize;}else{xsel.removeAttribute("border",0);}
			if(tablecellpadding!=""&&tablecellpadding!=null){xsel.cellPadding=tablecellpadding;}else{xsel.removeAttribute("cellPadding",0);}
			if(tablecellspacing!=""&&tablecellspacing!=null){xsel.cellSpacing=tablecellspacing;}else{xsel.removeAttribute("cellSpacing",0);}
			if(tablebordercolor!=""&&tablebordercolor!="Default"){xsel.borderColor=tablebordercolor;}else{xsel.removeAttribute("borderColor",0);}
			if(tablebackgroundcolor!=""&&tablebackgroundcolor!="Default"){xsel.bgColor=tablebackgroundcolor;}else{xsel.removeAttribute("bgColor",0);}
		}
	}
}

function cellProp(){
	var cellflag=false;
	HtmlEdit.focus();
	var xsel=HtmlEdit.document.selection;
	var xobj=HtmlEdit.document.selection.createRange();
	if(xsel.type=="None"||xsel.type=="Text"){
		xsel=xobj.parentElement();
		while(xsel.tagName!="BODY"&&cellflag==false){
			if(xsel.tagName=="TD"){cellflag=true;}
			if(cellflag==false){xsel=xsel.parentElement;}
		}
	}else if(xsel.type=="Control"){
		xsel=xobj.item(0);
		if(xsel.tagName=="TD"){
			cellflag=true;
		}else{
			while(xsel.tagName!="BODY"&&cellflag==false){
				if(xsel.tagName=="TD"){cellflag=true;}
				if(cellflag==false){xsel=xsel.parentElement;}
			}
		}
	}
	if(cellflag==true){
		if(xsel.width!=""&&xsel.width!=null){tablewidthspecified="yes";tablewidth=xsel.width;}else{tablewidthspecified="no";tablewith="";}
		if(xsel.align!=""&&xsel.align!=null){tablealign=xsel.align;}else{tablealign="";}
		if(xsel.className!=""&&xsel.className!=null){tablecellclass=xsel.className;}else{tablecellclass="";}
		if(xsel.vAlign!=""&&xsel.vAlign!=null){tablevalign=xsel.vAlign;}else{tablevalign="";}
		if(xsel.borderColor!=""&&xsel.borderColor!=null){tablebordercolor=xsel.borderColor;}else{tablebordercolor="";}
		if(xsel.bgColor!=""&&xsel.bgColor!=null){tablebackgroundcolor=xsel.bgColor;}else{tablebackgroundcolor="";}
		tableiscancel="";
		window.showModalDialog("editor_cellprops.asp",window,"dialogWidth: 400px; dialogHeight: 230px;help: no;scroll: no; status: no");
		if(tableiscancel=="no"){
			if(tablewidthspecified=="yes"){
				var tw1="";
				if(tablewidthtype=="percentage"){tw1=tablewidth+"%";}else{tw1=tablewidth;}
				xsel.width=tw1;
			}else{
				xsel.removeAttribute("width",0);
			}
			if(tablealign!=""&&tablealign!="Default"){xsel.align=tablealign;}else{xsel.removeAttribute("align",0);}
			if(tablevalign!=""&&tablevalign!="Default"){xsel.vAlign=tablevalign;}else{xsel.removeAttribute("vAlign",0);}
			if(tablecellclass!=""&&tablecellclass!="Default"){xsel.className=tablecellclass;}else{xsel.removeAttribute("className",0);}
			if(tablebordercolor!=""&&tablebordercolor!="Default"){xsel.borderColor=tablebordercolor;}else{xsel.removeAttribute("borderColor",0);}
			if(tablebackgroundcolor!=""&&tablebackgroundcolor!="Default"){xsel.bgColor=tablebackgroundcolor;}else{xsel.removeAttribute("bgColor",0);}
		}
	}
}
function table_ir()
{
	tablecommand("ir");
}
function table_dr()
{
	tablecommand("dr");
}
function table_ic()
{
	tablecommand("ic");
}
function table_dc()
{
	tablecommand("dc");
}
function table_mc()
{
	tablecommand("mc");
}
function table_md()
{
	tablecommand("md");
}
function table_sc()
{
	tablecommand("sc");
}

function word()
{
	HtmlEdit.document.execCommand("Paste",false);
	var editBody=HtmlEdit.document.body;
	for(var intLoop=0;intLoop<editBody.all.length;intLoop++){
		el=editBody.all[intLoop];
		el.removeAttribute("className","",0);
		el.removeAttribute("style","",0);
		el.removeAttribute("font","",0);
	}
	var html=HtmlEdit.document.body.innerHTML;
	html=html.replace(/<o:p>&nbsp;<\/o:p>/g,"");
	html=html.replace(/o:/g,"");
	html=html.replace(/<font>/g, "");
	html=html.replace(/<FONT>/g, "");
	html=html.replace(/<span>/g, "");
	html=html.replace(/<SPAN>/g, "");
	html=html.replace(/<SPAN lang=EN-US>/g, "");
	html=html.replace(/<P>/g, "");
	html=html.replace(/<\/P>/g, "");
 	html=html.replace(/<\/SPAN>/g, "");
	HtmlEdit.document.body.innerHTML = html;
	format('selectall');
	format('RemoveFormat');
}

function InsertChars(CharIndex)
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range =HtmlEdit.document.selection.createRange();
  var Chars=new Array("<br>","&copy;","&reg;","&#8482;","&#8226;","&#8230;","&#8212;","&#8211;");
  range.pasteHTML(Chars[CharIndex]);
  HtmlEdit.focus();
}
function InsertEQ()
{
  HtmlEdit.focus();
  var range =HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_inserteq.asp", "", "dialogWidth:40em; dialogHeight:20em; status:0;help:0");
  
  if (arr != null){
    var ss;
    ss=arr.split("*")
    a=ss[0];
    b=ss[1];
    var str1;
    str1="<applet codebase='./' code='webeq3.ViewerControl' WIDTH=320 HEIGHT=100>"
    str1=str1+"<PARAM NAME='parser' VALUE='mathml'><param name='color' value='"+b+"'><PARAM NAME='size' VALUE='18'>"
    str1=str1+"<PARAM NAME=eq id=eq VALUE='"+a+"'></applet>"
    range.pasteHTML(str1);
  }
  HtmlEdit.focus();
}
function InstallEQ()
{
  window.open ("editor_inserteq.asp?Action=Install", "", "height=200, width=300,left="+(screen.AvailWidth-300)/2+",top="+(screen.AvailHeight-200)/2+", toolbar=no, menubar=no, scrollbars=no, resizable=no,location=no, status=no")
}

function insermarquee()
{
  if (!	validateMode())	return;
  HtmlEdit.focus();
  var range = HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_marquee.html", "", "dialogWidth:270pt;dialogHeight:95pt;help:0;status:0");  
  if (arr != null){
    range.pasteHTML(arr);
  }
  HtmlEdit.focus();
}

function calculator()
{
  HtmlEdit.focus();
  var range =HtmlEdit.document.selection.createRange();
  var arr = showModalDialog("editor_calculator.html", "", "dialogWidth:205px; dialogHeight:210px; status:0;help:0");
  
  if (arr != null){
    var ss;
    ss=arr.split("*")
    a=ss[0];
    b=ss[1];
    var str1;
    str1=""+a+""
    range.pasteHTML(str1);
  }
  HtmlEdit.focus();
}
		</script>
	</body>
</html>
