<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8" />
    <meta name="viewport" content="width=640, initial-scale=0.5, user-scalable=no" />
    <title>${resource.rsdaTitle }</title>
    <link rel="stylesheet" href="<%=basePath %>plugin/imgviewer/lib/openseadragon/openseadragon.css" />
    <link rel="stylesheet" href="<%=basePath %>plugin/imgviewer/lib/openseadragon/c.css">
    <link rel="stylesheet" href="<%=basePath %>plugin/imgviewer/lib/page-style.css">
    <script type="text/javascript">var basePath = "<%=basePath %>";</script>
</head>

<body>
    <div id="txt-win"><div class="yuanern-txt"></div></div>
    <div class="page-catalog" style="height:800px">
    	<c:if test="${isCatalog}"><iframe src="<%=basePath %>res/catalog/${imgDir }?key=${keys }" width="100%" height="100%" frameborder="0"></iframe></c:if>
    	<c:if test="${!isCatalog}"><div class="blank"><fmt:message key="resource.preview.not.directory"/></div></c:if>
    </div>
    <div class="page-search">
    	<c:if test="${haveSrcTxt}"><iframe src="<%=basePath %>res/pagesearch/${resource.rsdaId }" width="300px" height="100%" frameborder="0"></iframe></c:if>
    	<c:if test="${!haveSrcTxt}"><div class="blank"><fmt:message key="resource.preview.processing"/></div></c:if>
    </div>
    <div class="main">
        <div class="header clearfix">
            <p class="img-title f-left">${resource.rsdaTitle }</p>
            <div class="f-right work-right clearfix">
            	<%-- 切换语种 --%>
            	<div class="f-left xlk" data-type="0">
			        <span class="lang-icon"></span>
			        <ul class="choose-list">
			            <li onclick="javaScript:switch_lang('zh_CN');">中文</li>
			            <li onclick="javaScript:switch_lang('zw_XZ');" style="font-size: 16px; line-height: 27px;border-top: none;border-bottom: 1px solid #838b98;font-family: hm;">བོད་ཡིག </li>
			            <%-- <li onclick="javaScript:switch_lang('en_US');">English</li> --%>
			        </ul>
			    </div>
			    <c:if test="${haveSrcPdf }">
			    	<div class="f-left xlk" data-type="0">
		            	<span class="icon-pdf-browse f-left" onclick="pdfBrowse();"></span>
			    	</div>
			    </c:if>
            	<div class="catalog-btn f-left" title="目录"></div>
                <div class="search-btn f-left" title="搜索该资源内容"></div>
                <div class="btn-s f-left" title="转到第一页"></div>
                <div class="btn-prev-page f-left" title="上一页"></div>
                <div class="btn-prev-img f-left" title="上一张"></div>
                <div class="page-num-box f-left">
                	<input type="text" name="page-num" id="page-num" maxlength="4" ONKEYPRESS="event.returnValue=IsDigit();" />
                </div>
                <div class="page-nav f-left">/0</div>
                <div class="btn-next-img f-left" title="下一张"></div>
                <div class="btn-next-page f-left" title="下一页"></div>
                <div class="btn-e f-left" title="转到最后一页"></div>
                <!-- <div class="share f-right" title="分享"></div> -->                
                <div class="txt-rig n3 share f-right" title="原文放右边"></div> 
                <div class="txt-btm n2 share f-right" title="原文放下面"></div> 
                <div class="txt-win n1 share f-right" title="弹出原文"></div> 
            </div>
        </div>
        <div class="clearfix" id="imgbox">
        	<div id="container" class="f-left" style="width: 100%; height: 800px;margin-top: 5px"></div>
        	<div id="txt-bottom" class="f-right" style="margin-bottom: 5px"><div class="yuanern-txt"></div></div>
        	<div id="txt-right" class="f-right"><div class="yuanern-txt"></div></div>
        </div>
        
        <span class="pre a" id="imgPre"><img src="<%=basePath %>plugin/imgviewer/lib/left.png" alt=""></span>
        <span class="nex b"><img src="<%=basePath %>plugin/imgviewer/lib/right.png" alt=""></span>
        
        <div id="main-footer">
	        <div id="img-list">
	            <span class="pre"></span>
	            <span class="nex"></span>
	            <div id="img-nav">
	                <div class="img-nav-sc clearfix"><img src="<%=basePath %>images/platform/loading.gif" style="display:block;margin: 25px auto 0px auto;"></div>
	            </div>
	        </div>
	        <div class="banquan"><fmt:message key="software.copyright"/></div>
	    </div>
    </div>
    
    
    
	<input type="hidden" id="split" value="${param.split}">
	<input type="hidden" id="imgDir" value="${imgDir}">
	<input type="hidden" id="currname" value="${currname}">
	<input type="hidden" id="currxml" value="${currxml}">
	<input type="hidden" id="searchkey" value="${keys }">
	<input type="hidden" id="hasTxt" value="${haveSrcTxt }">
	<input type="hidden" id="nowPage" name="nowPage" value="0">
	<input type="hidden" id="left" name="left" value="0">
	<script src="<%=basePath %>plugin/imgviewer/lib/jquery-1.10.2.min.js" type="text/javascript"></script>
	<script type="text/javascript" src="<%=basePath %>layer/layer.js" type="text/javascript"></script>
	<script src="<%=basePath %>plugin/imgviewer/lib/openseadragon/openseadragon.js" type="text/javascript"></script>
	<script src="<%=basePath %>plugin/imgviewer/lib/index.js" type="text/javascript"></script>
	<!-- 语言切换 -->
	<script src="<%=basePath %>js/switchLang.js"></script>
</body>
</html>
<script type="text/javascript">
	//屏蔽鼠标右键
	document.oncontextmenu=new Function("event.returnValue=false;");
	//搜序下拉框
	var xl_flag = false;
	$(function() {
	    //下拉框显示隐藏
	    $(".xlk").click(function() {
	        xlk_($(this));
	    })
	    //下拉框值选择
	    $(".xlk .choose-list li").click(function() {
	        $(this).parent().siblings(".choose-val").find(".sel-txt").html($(this).text());
	        $(this).parent().siblings(".choose-val").find("input").val($(this).text())
	    })
	
	})
	//下拉框
	function xlk_(obj) {
	    xl_flag = obj.data("type");
	    if (!xl_flag) {
	        obj.data("type", 1) 
	        obj.find(".choose-list").css("display", "block");
	        obj.find(".choose-list").animate({
	            "opacity": 1,
	            "top": "170%"
	        },
	        300);
	    } else {
	        obj.data("type", 0)
	        obj.find(" .choose-list").animate({
	            "opacity": 0,
	            "top": "300%"
	        },
	        300);
	        setTimeout(function() {
	            obj.find(".choose-list").css("display", "none");
	        },
	        300)
	    }
	}
	
	//PDF预览
	function pdfBrowse(){
		//获取当前页数
	    var currpage = $(".img-nav-sc div.img-top.active").data("num");
	    
	    var title = "${resource.rsdaTitle }";
		var url = "<%=basePath %>res/browse/pdf?file="+encodeURIComponent("${pdfPath}")+"&page=${currpage }&title="+encodeURIComponent(title);
	　　 window.open(url, "_blank");
	}
</script>