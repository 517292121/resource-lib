<%@ page language="java" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<%@ include file="/WEB-INF/views/platform/pc/common/frontendI18n.jspf" %>
    <meta charset="UTF-8">
    <title>${resource.rsdaTitle }<%= cn.com.adtech.riss2.core.i18n.FrontendMessages.html(request, "-在线预览") %></title>
    <style type="text/css">
        *{padding: 0;margin: 0;}
        iframe{position: fixed;width: 100%;height: 100%;left: 0;top: 0;border: 0;}
    </style>
</head>
<body>
<iframe src="${resource.rsdaFileUrl }"></iframe>
<script type="text/javascript" charset="utf-8">

    //解决iframe内弹窗鼠标滚轮事件失效
    (function () {
        var obj = window.parent.document;
        //
        function onMouseWheel(e) {
            var e = e || window.event;
            if (e.type == "mousewheel") {
                delta = e.wheelDelta / 12;
            } else {
                delta = e.detail / 3 * -10;
            };

            if (chrome != -1) {
            } else {
                obj.documentElement.scrollTop -= delta;
                //阻止默认事件
                if (e.preventDefault) {
                    e.preventDefault();
                }
                return false;
            }
        }

        if (obj != null && obj != undefined) {
            var chrome = navigator.userAgent.search(/chrome/i),
                delta = 0;

            if (chrome != -1) {
                document.addEventListener( "mousewheel", onMouseWheel, false);
            }
        }
    })();
</script>
</body>
</html>
