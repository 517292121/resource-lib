
  var uiText=window.platformText||function(value,parameters){return String(value).replace(/\{([a-zA-Z0-9_]+)\}/g,function(match,name){return parameters&&Object.prototype.hasOwnProperty.call(parameters,name)?String(parameters[name]):match;});};
  var uiMarkup=window.platformMarkup||function(value){return value;};
/*
 * IE Alert! jQuery plugin
 * version 1
 * author: David Nemes http://nmsdvid.com
 * http://nmsdvid.com/iealert/
 */

(function($){
$("#goon").live("click", function(){
		$("#ie-alert-overlay").hide();	
		$("#ie-alert-panel").hide();						  
});
function initialize($obj, support, title, text){

		var panel = "<span>"+ title +"</span>"
				  + "<p> "+ text +"</p>"
			      + "<div class='browser'>"
			      + "<ul>"
			      + uiMarkup("<li><a class='chrome' href='https://www.google.cn/intl/zh-CN/chrome/' target='_blank'><span>*强烈建议</span></a></li>")
			      + "<li><a class='firefox' href='https://www.firefox.com.cn/' target='_blank'></a></li>"
			      + "<li><a class='ie9' href='https://support.microsoft.com/zh-cn/help/17621/internet-explorer-downloads' target='_blank'></a></li>"
			      + "<li><a class='opera' href='https://www.opera.com/zh-cn' target='_blank'></a></li>"
			      + "<li><a class='safari' href='https://www.360.cn/' target='_blank'></a></li>"
			      + "<ul>"
			      + "</div>"; 

		var overlay = $("<div id='ie-alert-overlay'></div>");
		var iepanel = $("<div id='ie-alert-panel'>"+ panel +"</div>");

		var docHeight = $(document).height();

		overlay.css("height", docHeight + "px");



		
		if (support === "ie9") { 			// shows the alert msg in IE9,IE8, IE7, IE6
			if ($.browser.msie  && parseInt($.browser.version, 10) < 10) {
				
				$obj.prepend(iepanel);
				$obj.prepend(overlay);
				
			}

			if ($.browser.msie  && parseInt($.browser.version, 10) === 6) {

				
				$("#ie-alert-panel").css("background-position","-626px -116px");
				$obj.css("margin","0");
  
			}
			
		} else if (support === "ie8") { 	// shows the alert msg in IE8, IE7, IE6
			
			if ($.browser.msie  && parseInt($.browser.version, 10) < 9) {
				
				$obj.prepend(iepanel);
				$obj.prepend(overlay);
			}
			
			if ($.browser.msie  && parseInt($.browser.version, 10) === 6) {
				
				$("#ie-alert-panel").css("background-position","-626px -116px");
				$obj.css("margin","0");
  
			}
			
		} else if (support === "ie7") { 	// shows the alert msg in IE7, IE6
			
			if ($.browser.msie  && parseInt($.browser.version, 10) < 8) {
				
				$obj.prepend(iepanel);
				$obj.prepend(overlay);
			}
			
			if ($.browser.msie  && parseInt($.browser.version, 10) === 6) {
				
				$("#ie-alert-panel").css("background-position","-626px -116px");
				$obj.css("margin","0");
  
			}
			
		} else if (support === "ie6") { 	// shows the alert msg only in IE6
			
			if ($.browser.msie  && parseInt($.browser.version, 10) < 7) {
				
				$obj.prepend(iepanel);
				$obj.prepend(overlay);
				
  				$("#ie-alert-panel").css("background-position","-626px -116px");
				$obj.css("margin","0");
				
			}
		}

}; //end initialize function

	$.fn.iealert = function(options){
		var defaults = { 
			support: "ie9",  // ie8 (ie6,ie7,ie8), ie7 (ie6,ie7), ie6 (ie6)
			title: uiText("\u4F60\u77E5\u9053\u4F60\u7684Internet Explorer\u662F\u8FC7\u65F6\u4E86\u5417?"), // title text
			text: uiMarkup("\u6211\u4EEC\u4F7F\u7528\u4E86\u6700\u65B0\u7684\u6280\u672F\u6765\u642D\u5EFA\u8BE5\u7F51\u7AD9\u7CFB\u7EDF\uFF0C\u4F7F\u6211\u4EEC\u7684\u5E73\u53F0\u66F4\u5FEB\u548C\u4F53\u9A8C\u66F4\u597D\u7684\u4F7F\u7528\u6548\u679C\u3002\u4E0D\u5E78\u7684\u662F\uFF0C\u4F60\u7684\u6D4F\u89C8\u5668\u7248\u672C\u8F83\u4F4E\uFF0C\u4E0D\u652F\u6301\u8FD9\u4E9B\u6280\u672F\u3002\u4F60\u53EF\u4EE5\u4E0B\u8F7D\u6216\u66F4\u65B0\u4EE5\u4E0B\u4EFB\u610F\u4E00\u79CD\u6D4F\u89C8\u5668\u6765\u8BBF\u95EE\u3002\u5982\u679C\u8981\u7EE7\u7EED\u4F7F\u7528\u8BE5\u6D4F\u89C8\u5668\u8BBF\u95EE\uFF0C\u53EF\u80FD\u4F1A\u5F71\u54CD\u4F60\u7684\u4F53\u9A8C\u6548\u679C\uFF01<br /><br /><h1 id='goon' style='font-size:16px;cursor:pointer;'>>>>\u7EE7\u7EED\u8BBF\u95EE</h1>")
		};
		
		var option = $.extend(defaults, options);

		
			return this.each(function(){
				if ( $.browser.msie ) {
					var $this = $(this);  
					initialize($this, option.support, option.title, option.text);
				} //if ie	
			});		       
	
	};
})(jQuery);
