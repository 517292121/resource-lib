<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/common/taglibInclude.jsp" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=no">
    <title>${resource.rsdaTitle }</title>
    <link href="<%=path %>/plugin/wapImgViewer/bootstrap.min.css" rel="stylesheet">
    <link href="<%=path %>/plugin/wapImgViewer/common.css" rel="stylesheet">
    <link href="<%=path %>/plugin/wapImgViewer/demo.css" rel="stylesheet" media="all">
    <script type="text/javaScript">
    	var imgDir = "${imgDir}";
    	var path = "<%=path %>";
    </script>
</head>

<body class="default-variant not-front navbar-is-fixed-top biblissima site-demos scroll-demo osd_chtrx">
     <div class="" id="gjl" style="display:none"></div>
    <div class="osd-demo main-container container-fluid" role="main">
        <div class="navbar-control clearfix">
            <div class="container">
                <div class="navbar-control-inner">
                    <div class="navbar-row">
                        <div class="navbar-cell">
                            <div class="controls">
                                <div class="labels"></div>
                                <a class="previous hud-control" title="Previous"></a>
                                <a class="next hud-control" title="Next"></a>
                            </div>
                        </div>
                        <div class="navbar-cell buttons">
                            <a href="javascript:;" class="button thumbs" title="Thumbnails"></a>
                            <a href="javascript:;" class="button scroll" title="Scroll"></a>
                            <a href="javascript:;" class="button page" title="Page"></a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div id="contentDiv" class="openseadragon1 viewer-position"></div>
        <div class="scroll-cover viewer-position">
            <div class="scroll-inner"></div>
        </div>
    </div>

    <script src="<%=path %>/js/jquery-1.7.2.min.js"></script> 

    <script src='<%=path %>/plugin/wapImgViewer/openseadragon.min.js'></script>
    <script src='<%=path %>/plugin/wapImgViewer/d3.v3.min.js'></script> 
    <script src='<%=path %>/plugin/wapImgViewer/main.js'></script> 
    <script src='<%=path %>/plugin/wapImgViewer/page.js'></script>
    <!-- <script src='./demo/demo.js'></script>
    <script src='./demo/demo1.js'></script> -->
    <script src='<%=path %>/plugin/wapImgViewer/openseadragon-svg-overlay.js'></script>
</body>
</html>