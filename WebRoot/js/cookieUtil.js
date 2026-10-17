//cookie basic util

function SetCookie(name,value)
{
    var Days = 30; //cookie save days
    var exp  = new Date();    //new Date("December 31, 9998");
    exp.setTime(exp.getTime() + Days*24*60*60*1000);
    document.cookie = name + "="+ escape (value);
    //document.cookie = name + "="+ escape (value) + ";expires=" + exp.toGMTString();ä¸å æ¶é´ï¼é»è®¤å°±æ¯å³é­æµè§å¨åcookieè¿æ
}
function getCookie(name)        
{

    var arr = document.cookie.match(new RegExp("(^| )"+name+"=([^;]*)(;|$)"));
     if(arr != null) return unescape(arr[2]); return null;

}
function delCookie(name)
{	
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval=getCookie(name);
    if(cval!=null) document.cookie= name + "="+cval+";expires="+exp.toGMTString();
}
