<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="cn.com.adtech.riss2.cache.SysConfigInfo"%>
<%@page import="cn.com.adtech.riss2.core.ConstantsObj"%>
<c:choose>
	<%-- 文档 --%>
	<c:when test="${!empty browseUrl and resType eq 'document'}">
	   <div class="resview doc">	
	   <object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" 
		    codebase="http://fpdownload.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=9,0,18,0"  
		    width="100%" height="100%" id="test" align="middle"> 
			<param name="allowFullScreen" value="true" /> 
			<param name="bgcolor" value="#333333" /> 
			<param name="movie" value="<%=SysConfigInfo.FILE_URL_PATH %>${browseUrl }&isOnLine=true" /> 
			<embed src="<%=SysConfigInfo.FILE_URL_PATH %>${browseUrl }&isOnLine=true" allowFullScreen="true" bgcolor="#333333"
			    width="100%" height="100%"
			    name="test" align="middle" type="application/x-shockwave-flash"  
			    pluginspage="http://www.macromedia.com/go/getflashplayer" />
		</object>
		</div>
	</c:when>
	<%-- 图片 --%>
	<c:when test="${!empty browseUrl and resType eq 'photo'}">
		<div class="resview pic"><a href="<%=SysConfigInfo.FILE_URL_PATH %>${browseUrl }&isOnLine=true" target="blank"><img src="<%=SysConfigInfo.FILE_URL_PATH %>${browseUrl }&isOnLine=true"/></a></div>
    </c:when>
    <%-- 音频 --%>
	<c:when test="${!empty browseUrl and resType eq 'audio'}">
		<link rel="stylesheet" href="<%=basePath %>plugin/audio/style.css" type="text/css"></link>
		<script src="<%=basePath %>plugin/audio/js/weixinaudio.js" type="text/javascript" charset="utf-8"></script>
		<div class="weixinAudio">
			<audio src="" id="media" width="1" height="1" preload></audio>
			<span id="audio_area" class="db audio_area">
				<span class="audio_wrp db">
					<span class="audio_play_area">
						<i class="icon_audiodefault"><img src="<%=basePath %>images/platform/<%=ConstantsObj.getSkinCode() %>/shengyin.png" alt=""></i>
						<i class="icon_audioplaying"><img src="<%=basePath %>images/platform/<%=ConstantsObj.getSkinCode() %>/shengyin.gif" alt=""></i>
            		</span>
					<span id="audio_length" class="audio_length tips_global"></span>
					<span id="curent_time">0:00</span>
					<span class="db audio_info_area">
                		<strong class="db audio_title">${resource.rsdaTitle }</strong>
					</span>
					<span id="int_c"></span>
					<span class="progress_bar_box" style="width: 90%;">
						<span id="audio_progress" class="progress_bar" style="width: 0%;"></span>
					</span>
				</span>
			</span>
		</div>		
		<script type="text/javascript">
			$('.weixinAudio').weixinAudio({
				autoplay:true,
				src:'<%=SysConfigInfo.FILE_URL_PATH %>${browseUrl }&isOnLine=true',
			});
			var a=0;
			$(".audio_play_area").click(function () {
				a++;
				if(a%2!=0){
					$(".icon_audiodefault").css({"display":"none"});
					$(".icon_audioplaying").css({"display":"block"});
					console.log(a);
				}
				if(a%2==0){
					console.log(a);
					$(".icon_audiodefault").css({"display":"block"});
					$(".icon_audioplaying").css({"display":"none"});
				}
			})
		</script>
    </c:when>
	<%-- 视频 --%>
    <c:when test="${!empty browseUrl and resType eq 'video'}">
     	<div class="resview video">
     	
<video width="100%" height="100%" controls="controls" autoplay="autoplay">
  <source src="<%=SysConfigInfo.FILE_URL_PATH %>${fn:split(browseUrl,'&')[0] }" type="video/ogg" />
  <source src="<%=SysConfigInfo.FILE_URL_PATH %>${fn:split(browseUrl,'&')[0] }" type="video/FLV" />
  <source src="<%=SysConfigInfo.FILE_URL_PATH %>${fn:split(browseUrl,'&')[0] }" type="video/MP4" />
		<object type="application/x-shockwave-flash" data="<%=basePath %>flashview/player/vcastr3.swf" width="100%" height="100%" id="vcastr3">
			<param name="movie" value="<%=basePath %>flashview/player/vcastr3.swf"/> 
			<param name="allowFullScreen" value="true" />
			<param name="FlashVars" value="xml=
			<vcastr>
				<channel>
					<item>
						<source><%=SysConfigInfo.FILE_URL_PATH %>${fn:split(browseUrl,'&')[0] }</source>
						<duration></duration>
						<title></title>
					</item>
				</channel>
				<config>
				</config>
				<plugIns>
					<logoPlugIn>
					<url><%=basePath %>flashview/player/logoPlugIn.swf</url>
					<logoText></logoText>
					<logoTextAlpha>0.75</logoTextAlpha>
					<logoTextFontSize>30</logoTextFontSize>
					<logoTextLink><%=basePath %>images/platform/def/logo.png</logoTextLink>
					<logoTextColor>0xffffff</logoTextColor>
					<textMargin>20 20 auto auto</textMargin>
					</logoPlugIn>
				</plugIns>
			</vcastr>"/>
		</object>
		</video> 
		</div>
     </c:when>
     <c:when test="${!empty filemsg }">
		<div class="resview nobrowse">${filemsg }</div>
     </c:when>
	 <c:otherwise>
		<div class="resview nobrowse"><fmt:message key="bro.not.found.file"/></div>
	 </c:otherwise>
</c:choose>
