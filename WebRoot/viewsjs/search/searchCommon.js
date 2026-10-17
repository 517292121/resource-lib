/**
 * js公共辅助
 * @author puzhijun
 */
 /**
String.prototype.trim = function() {
	return this != "" ? this.replace(/(\s*$)/g, "") : "";
}
Object.prototype.setOptions = function(param) {
    if(typeof param == "object") {
        for(var i in param) {
            this[i] = param[i];
        }
    }    
}

var $ = function(idStr, parent) {
    if(typeof idStr == "string") {
        if(idStr.indexOf("~") != -1) {
            var content = idStr.substring(idStr.indexOf("~") + 1, idStr.length);
	        if(parent) {
                return parent.getElementsByTagName(content);    
	        } else {
	            return document.getElementsByTagName(content);
            }
        } else if(idStr.indexOf("^") != -1) {
            var content = idStr.substring(idStr.indexOf("^") + 1, idStr.length);
	        if(parent) {
                return parent.getElementsByName(content);    
	        } else {
	            return document.getElementsByName(content);
            }
        }else {
            return document.getElementById(idStr)
        }
    } else {
        return idStr;
    }
} */
function getRadioValue(radios){
    var obj = radios;   
    if(obj!=null){
        for(var i=0;i<obj.length;i++){
            if(obj[i].checked){
                return obj[i].value;           
            }
        }
    }
    return "";
}

var Adtech = {};
Adtech.RISS2 = function(name) {
    var parent = window;
    var arr = name.split('.');
    for(var i=0; i <arr.length; i++) {
        if ( !parent[arr[i]] ) {
            parent[arr[i]] = {};
        }
        parent = parent[arr[i]];
    }       
} 
var riss2 = Adtech.RISS2;
//ajax请求
//riss2.ajax = {};
riss2.ajax = {
    xmlHttp: null,
    request: function(url, method, param, callback) {
        this.callback = callback;
        var that = this;
 //       this.xmlHttp = function() {
 //           if(!this.xmlHttp) {
				try {
				// Firefox, Opera 8.0+, Safari
				    this.xmlHttp=new XMLHttpRequest();
				} catch (e) {
				// Internet Explorer
					try {
					    this.xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
					} catch (e) {
					    this.xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
				    }
				}
  //          }
      // }
       var stateChanged = function() {
           if(that.xmlHttp.readyState==4) {
               if(that.xmlHttp.status == 200) {
                   that.callback();               
               }
           }
       }
       if(method) {
	       var urlParam = "";
           if(method == "GET") urlParam = url;
	       if(typeof param == "object") {
	           var j = 0;
	           for(var i in param) {
                    if(i != "setOptions") {
		                if(j == 0) {
		                    if(method == "GET") {
		                      urlParam += (urlParam.indexOf("?") == -1) ? ("?" + i + "=" + param[i]) : "&" + i + "=" + param[i];
		                    } else {
		                      urlParam += (i + "=" + param[i]);
		                    }
		                } else {
		                    urlParam += ("&" + i + "=" + param[i]);
		                }
		                j++;
		           }
               }
	       }
			this.xmlHttp.onreadystatechange=stateChanged;
            if(method == "GET") {
				this.xmlHttp.open("GET",urlParam,true);
				this.xmlHttp.send(null);
            } else {
				this.xmlHttp.open("POST",url,true);
                this.xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				this.xmlHttp.send(urlParam);
            }
       }
    }   
}
riss2.navigationAjax = {
    navigationXmlHttp: null,
    request: function(url, method, param, callback) {
    	this.callback = callback;
    	var that = this;
		try {
		    this.navigationXmlHttp=new XMLHttpRequest();
		} catch (e) {
			try {
			    this.navigationXmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
			} catch (e) {
			    this.navigationXmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
		    }
		}
       var stateChanged = function() {
           if(that.navigationXmlHttp.readyState==4) {
               if(that.navigationXmlHttp.status == 200) {
                  that.callback();                
               }
           }
       }
       if(method) {
	       var urlParam = "";
           if(method == "GET") urlParam = url;
	       if(typeof param == "object") {
	           var j = 0;
	           for(var i in param) {
                    if(i != "setOptions") {
		                if(j == 0) {
		                    if(method == "GET") {
		                      urlParam += (urlParam.indexOf("?") == -1) ? ("?" + i + "=" + param[i]) : "&" + i + "=" + param[i];
		                    } else {
		                      urlParam += (i + "=" + param[i]);
		                    }
		                } else {
		                    urlParam += ("&" + i + "=" + param[i]);
		                }
		                j++;
		           }
               }
	       }
			this.navigationXmlHttp.onreadystatechange=stateChanged;
            if(method == "GET") {
				this.navigationXmlHttp.open("GET",urlParam,true);
				this.navigationXmlHttp.send(null);
            } else {
				this.navigationXmlHttp.open("POST",url,true);
                this.navigationXmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
				this.navigationXmlHttp.send(urlParam);
            }
       }
    }   
}