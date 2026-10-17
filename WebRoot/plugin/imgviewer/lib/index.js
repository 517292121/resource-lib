var viewer = null;
var imgtotal = 0; /*图片总量*/ ;
var maxImgNum = 0; /*每页显示图片最大的张数*/ ;
var txtNums = 0; /**文本源文数量*/



//获取项目文件访问根路径
function getRoot(){
	return basePath + "file/viewer?path=";
}


//关闭搜索框
function closeSearch() {
    if ($(".page-search").hasClass("active")) {
        $(".page-search").removeClass("active").animate({
            right: -300 + "px"
        }, 300);
    } else {
        $(".page-search").addClass("active").animate({
            right: 0
        }, 300);
    }
}
//关闭目录框
function closeCatalog() {
    if ($(".page-catalog").hasClass("active")) {
		hideTree();        
    } else {
    	openTree();
    }
}
function openTree(){
    $(".page-catalog").addClass("active").animate({
        left: 0
    }, 300);
       
	$("#imgbox").width("76%");
    $("#imgbox").animate({
        marginLeft: "24%"
    }, 300);
    $("#imgPre").css("marginLeft","24%"); //上一页按钮也要移动
}
function hideTree(){
	var catalogWid = $(".page-catalog").width();
	$(".page-catalog").removeClass("active").animate({
        left: (0-catalogWid) + "px"
    }, 300);
	      
	var conWid = $("#container").width() + catalogWid;
	$("#imgbox").width("100%");
	$("#imgbox").animate({
		marginLeft: "0px"
	}, 300);
	$(".pre").css("marginLeft","0px"); //上一页按钮也要移动
}


// 搜索后更新目录
function catalog(name, xmlpath, _key) {
    var left = 0;
    $.each($(".img-nav-sc .img-top"), function (i, n) {
        if ($(n).data("name") == name) {
            var nowId = $(n).index(),
                imgPage = Math.floor(nowId / maxImgNum) /*当前选中图片在第几页*/ ,
                nowPage = Math.floor((0 - $(".img-nav-sc").css("left").replace("px", "")) / 82 / maxImgNum) /*当前目录在哪一页*/ ;
            if (nowId == $(".img-top.active").index()) {
                return false;
            }
            $(n).addClass("active").siblings().removeClass("active");
            left = -82 * maxImgNum * imgPage;
            if (nowPage != imgPage) {
                $(".img-nav-sc").animate({
                    left: left + "px"
                }, 500, function () {
                    showImg(xmlpath);
                });
            } else {
                showImg(xmlpath);
            }
            
            if(_key.length > 0){
	            $("#searchkey").val(_key);
            }
            txtToDiv();
            $("#nowPage").val(nowPage);
            return false;
        }
    })
    //设置当前页码，便于懒加载计算
    $("#left").val(left);
    //图片懒加载触发事件
    $("#left").click();
    $(".img-title").width($(".main").width()-$(".work-right").width()-50);
}

function showImg(path) {
	if(path == null || path.length < 1){
		return;
	}
    //清空之前的渲染的html
    if (viewer != null) {
        //用于销毁查看器并清除OpenSeadragon创建的所有内容的功能。示例：var viewer = OpenSeadragon（{[...]}）; //完成查看器后：viewer.destroy（）; viewer = null; 
        viewer.destroy();
    }
    viewer = OpenSeadragon({
        id: "container", //容器id
        showHomeControl: false,
        showNavigator: true,
        blendTime: 1,
        constrainDuringPan: true,
        visibilityRatio: 1,
        prefixUrl: basePath + "plugin/imgviewer/lib/openseadragon/images/", //openseadragon插件资源路径
        tileSources: getRoot() + path, //openseadragon 图片资源xml
        showNavigator: true, //是否显示控制按钮
        tilesources: true, //设置为true以使查看器将您的tilesources视为要一次打开一个图像而不是一次打开的一系列图像。
    });
    
    var currpage = $(".img-nav-sc div.img-top.active").data("num");
    if(currpage != null){
	    $(".page-nav").html("/" + imgtotal);
	    $("#page-num").val(currpage);
    }
}


//判断是否全屏
function IsFull() {
    var fullscreenElement =
        document.fullscreenEnabled ||
        document.mozFullscreenElement ||
        document.webkitFullscreenElement;
    var fullscreenEnabled =
        document.fullscreenEnabled ||
        document.mozFullscreenEnabled ||
        document.webkitFullscreenEnabled;
        
    if (fullscreenElement == null) {
        return false;
    } else {
        return true;
    }
}

//加载数据
function loadData() {
	var currname = $("#currname").val();
	var currxml = $("#currxml").val();
    $.ajax({
        url: basePath + "imgview/json",
        dataType: "json",
        type: "get",
        data:{
        	imgDir: $("#imgDir").val(),
        	split: $("#split").val()
        },
        success: function (data) {
            if (data.success) {
                var imgListData = data.imgList;
                if(imgListData == null || imgListData.length < 1){
                	return;
                }
                //没有指定选中图片时，默认加载第一张
                if(currname.length < 1){
                	var xmlp = imgListData[0].xmlpath;
                	showImg(xmlp);
                }
                
                $("#img-nav .img-nav-sc").html("");
                $.each(imgListData, function (i, n) {
                    var imgHtml = "<div onclick=\"changeChoose(this)\" data-name=\"" + n.filename + "\" data-num='"+(i+1)+"' class=\"img-top";
                    if (i == 0 && currname.length < 1) {
                        imgHtml = imgHtml + " active";
                    }
                    imgHtml = imgHtml + "\"><span class=\"w\" ></span><img class='lazyload' data-url=" + n.xmlpath + " data-original=" + getRoot() + n.smallpath + " /></div>";
                    $("#img-nav .img-nav-sc").append(imgHtml);
                    
                })
                imgtotal = imgListData.length;
                createImgNav();
                
                if(currname.length > 0){
                	//大图已在进入页面时加载，此时只需要定位缩约图即可
                	catalog(currname, null, ""); 
                }
                
                //设置导航页码
                var currpage = $(".img-nav-sc div.img-top.active").data("num");
    			$(".page-nav").html("/" + imgtotal);
    		    $("#page-num").val(currpage);
    			
    			if(txtNums > 0){
    				//如果有原文，则加载文字设置相关功能。
    				initToolCode();
    			}
    			$(".img-title").width($(".main").width()-$(".work-right").width()-50);
           		
           		//缩略图懒加载
    			lazyLoad();
            }
        }
    })
}

//创建目录
function createImgNav() {
    var scWidth = imgtotal * 82,
        navWidth = $(window).width();

    maxImgNum = Math.floor((navWidth - 100) / 82) /*最大显示张数*/ ;
    $(".img-nav-sc").width(scWidth);
    if (scWidth - 100 > navWidth) {
        $("#img-nav").width(maxImgNum * 82);
        $("#img-list>.pre").css("display", "block");
        $("#img-list>.nex").css("display", "block");
    }

}

//目录上下页
function move(direction) {
    var totalPage = Math.ceil(imgtotal / maxImgNum) /*总的页数*/ ,
        nowPage = Math.floor((0 - $(".img-nav-sc").css("left").replace("px", "")) / 82 / maxImgNum) /*当前在哪一页*/ ;
    var left = 0;
    if (direction > 0) { //上一页
        if (nowPage == 0) {
            return false;
        }
    	left = -82 * maxImgNum * (nowPage - 1);
        $(".img-nav-sc").animate({
            left: left + "px"
        }, maxImgNum * 10);
    } else { //下一页	
        if (nowPage >= totalPage - 1) {
            return false;
        }
        left = -82 * maxImgNum * (nowPage + 1);
        $(".img-nav-sc").animate({
            left: left + "px"
        }, maxImgNum * 10);
    }
    $("#nowPage").val(nowPage);
    $("#left").val(left);
    //图片懒加载触发事件
    $("#left").click();
}

//搜索更新
function changeChoose(obj) {
    $(obj).addClass("active").siblings().removeClass("active");
    showImg($(obj).find("img").data("url"));
    txtToDiv();
}

$(function () {
	var currname = $("#currname").val();
	if(currname.length > 0){
		var currxml = $("#currxml").val();
    	showImg(currxml);
    }
    
    var hasTxt = $("#hasTxt").val();
    if(hasTxt == "true"){
    	txtNums = 1;
    }

    loadData();//加载数据
	
	
	var h = $(".main").height() - 165;
        $(".small_list").width($(".small_box").width() - 60);
        $(".page-search").height(h);
        $(".page-catalog").height(h);
        $("#container").height(h-3);
        $(".openseadragon-canvas+div").removeClass("IsFull");
        //$(".main>.pre,.main>.nex").css("top", ($(".main").height() - 100) / 2 + "px");
        $.each($(".quanping img"), function (i, n) {
            $(n).attr("src",basePath+"plugin/imgviewer/lib/openseadragon/images/fullpage_grouphover.png");
        })
        $(".img-title").width($(".main").width()-$(".work-right").width()-50);
        
	
	/*
    if (!IsFull()) {//非全屏状态
    	var h = $(".main").height() - 165;
        $(".small_list").width($(".small_box").width() - 60);
        $(".page-search").height(h);
        $(".page-catalog").height(h);
        $("#container").height(h-3);
        $(".openseadragon-canvas+div").removeClass("IsFull");
        //$(".main>.pre,.main>.nex").css("top", ($(".main").height() - 100) / 2 + "px");
        $.each($(".quanping img"), function (i, n) {
            $(n).attr("src","http://192.168.1.168:8008/qhzhsyjxzyyfwpt/plugin/imgviewer/lib/openseadragon/images/fullpage_grouphover.png");
        })
    } else {//全屏状态
        $(".openseadragon-canvas+div").addClass("IsFull");
        $.each($(".quanping img"), function (i, n) {
            $(n).attr("src","http://192.168.1.168:8008/qhzhsyjxzyyfwpt/plugin/imgviewer/lib/openseadragon/images/fullpage_grouphover1.png");
        })
    }
*/
    //搜索板块控制 
    $(".search-btn").click(function () {
        closeSearch();
    });
    
    //章节目录板块控制 
    $(".catalog-btn").click(function () {
        closeCatalog();
    })

document.addEventListener("fullscreenchange", function( event ) {
	  if (document.fullscreenElement) {
		$(".openseadragon-canvas+div").addClass("IsFull");
        $.each($(".quanping img"), function (i, n) {
            $(n).attr("src",basePath+"plugin/imgviewer/lib/openseadragon/images/fullpage_grouphover1.png");
        })
	  } else {
	  	var h = $(".main").height() - 165;
            $(".small_list").width($(".small_box").width() - 60);
            $(".page-search").height(h);
            $(".page-catalog").height(h);
            $("#container").height(h-3);
            $(".openseadragon-canvas+div").removeClass("IsFull");
            //$(".main>.pre,.main>.nex").css("top", ($(".main").height() - 100) / 2 + "px");
             $.each($(".quanping img"), function (i, n) {
	            $(n).attr("src",basePath+"plugin/imgviewer/lib/openseadragon/images/fullpage_grouphover.png");
	        })
	  }
		$(".img-title").width($(".main").width()-$(".work-right").width()-50)
 });
    //窗口大小变化
    var kh = $(".main").height() - 168;
    $(window).resize(function () {
   
        createImgNav();
        if($(".main").length>0){         
		        if ($(".main").height() ==  window.screen.height) {   
		        	var h = $(".main").height() - 165;
		            $(".small_list").width($(".small_box").width() - 60);
		            $(".page-search").height(h);
		            $(".page-catalog").height(h);
		            $("#container").height(h-3);
		            kh = h-3;
		            $(".openseadragon-canvas+div").removeClass("IsFull");
		            //$(".main>.pre,.main>.nex").css("top", ($(".main").height() - 100) / 2 + "px");
		             $.each($(".quanping img"), function (i, n) {
		            $(n).attr("src",basePath+"plugin/imgviewer/lib/openseadragon/images/fullpage_grouphover.png");
		        })
		        
		    } else {//全屏状态    
		    		var h = $(".main").height() - 165;
		            $(".small_list").width($(".small_box").width() - 60);                                     
		            $(".page-search").height(h);
		            $(".page-catalog").height(h);
		            $("#container").height(h);
			        $(".openseadragon-canvas+div").addClass("IsFull");
			        $.each($(".quanping img"), function (i, n) {
			            $(n).attr("src",basePath+"plugin/imgviewer/lib/openseadragon/images/fullpage_grouphover1.png");
			        })
		    }   
        
        }
        $(".img-title").width($(".main").width()-$(".work-right").width()-50)
	    txtResize();
	    
	    var nowPage = Math.floor((0 - $(".img-nav-sc").css("left").replace("px", "")) / 82 / maxImgNum) /*当前在哪一页*/ ;
	    //设置当前页码，懒加载时需要根据当前页码计算
	    $("#nowPage").val(nowPage - 1);
	    //图片懒加载触发事件
	    lazyLoad();
    })

    //图片目录上下页
    $("#img-list>.pre,.btn-prev-page").click(function () {
        move(1)
    })
    $("#img-list>.nex,.btn-next-page").click(function () {
        move(-1)
    })

    //图片上下页
    $(".main>.pre,.btn-prev-img").click(function () {
        var nowImg = $(".img-top.active"),
            nowId = nowImg.index(),
            imgPage = Math.floor(nowId / maxImgNum) /*当前选中图片在第几页*/ ,
            nowPage = Math.floor((0 - $(".img-nav-sc").css("left").replace("px", "")) / 82 /
                maxImgNum) /*当前目录在哪一页*/ ;
        if (nowPage == imgPage) {
            if (nowId == 0) {
                return false;
            } else {
                if ((nowId) % maxImgNum == 0) {
                    move(1)
                }
                nowImg.prev().addClass("active").siblings().removeClass("active");
                showImg(nowImg.prev().find("img").data("url"));
                txtToDiv();
            }

        } else {
            $(".img-nav-sc").animate({
                left: -82 * maxImgNum * imgPage + "px"
            }, 500, function () {
                if (nowId == 0) {
                    return false;
                } else {
                    if (nowImg.parent().hasClass("end")) {
                        if ((imgtotal - (nowId - 1) - 1) % maxImgNum == 0) {
                            move(1)
                        }
                    } else {
                        if ((nowId) % maxImgNum == 0) {
                            move(1)
                        }
                    }
                    nowImg.prev().addClass("active").siblings().removeClass("active");
                    showImg(nowImg.prev().find("img").data("url"));
                    txtToDiv();
                }
            });
        }
    });

    $(".main>.nex,.btn-next-img").click(function () {
        var nowImg = $(".img-top.active"),
            nowId = nowImg.index(),
            imgPage = Math.floor(nowId / maxImgNum) /*当前选中图片在第几页*/ ,
            nowPage = Math.floor((0 - $(".img-nav-sc").css("left").replace("px", "")) / 82 /
                maxImgNum) /*当前目录在哪一页*/ ;

        if (nowPage != imgPage) {
            $(".img-nav-sc").animate({
                left: -82 * maxImgNum * imgPage + "px"
            }, 500, function () {
                if (nowId == imgtotal - 1) {
                    return false;
                } else {
                    if ((nowId + 1) % maxImgNum == 0) {
                        move(-1)
                    }
                    nowImg.next().addClass("active").siblings().removeClass("active");
                    showImg(nowImg.next().find("img").data("url"));
                    txtToDiv();
                }
            });
        } else {
            if (nowId == imgtotal - 1) {
                return false;
            } else {
                if ((nowId + 1) % maxImgNum == 0) {
                    move(-1)
                }
                nowImg.next().addClass("active").siblings().removeClass("active");
                showImg(nowImg.next().find("img").data("url"));
                txtToDiv();
            }
        }

    });

    //返回首页
    $(".btn-s").click(function () {
        var nowLeft = 0 - $(".img-nav-sc").css("left").replace("px", "");
        if (nowLeft <= 0) {
            return false;
        } else {
            $(".img-nav-sc").animate({
                left: "0px"
            }, 200);
        }
        //设置当前页码，便于懒加载计算
        $("#nowPage").val(0);
        $("#left").val(0);
        //图片懒加载触发事件
        $("#left").click();
    })
    //跳到尾页
    $(".btn-e").click(function () {
        var left = 0;
        var totalPage = Math.ceil(imgtotal / maxImgNum),
            nowLeft = 0 - $(".img-nav-sc").css("left").replace("px", "");
        if (nowLeft >= 82 * maxImgNum * (totalPage - 1)) {
            return false;
        } else {
        	left = -82 * maxImgNum * (totalPage - 1);
            $(".img-nav-sc").animate({
                left: left + "px"
            }, 200);
        }
        //设置当前页码，便于懒加载计算
        $("#nowPage").val(totalPage - 1);
        $("#left").val(left);
        //图片懒加载触发事件
        $("#left").click();
    })
    
    $(".work-right .n1").click(function () {
        openSrc(1);
    })
    $(".work-right .n2").click(function () {
        openSrc(2)
    })
    $(".work-right .n3").click(function () {
        openSrc(3)
    })
    
    $("#page-num").keydown(function(e) {  
        if (e.keyCode == 13) {
        	var p = $("#page-num").val();
        	if(p > imgtotal || p < 1){
        		layer.tips("页码不在资源范围！", "#page-num", {tips: [2, '#FF8000'], time: 4000});
				return;
        	}
        	var page = $(".img-nav-sc div.img-top[data-num=" + p + "]");
        	
        	var pname = page.data("name");
        	var xmlpath = page.find("img").data("url");
        	catalog(pname, xmlpath, "");
        }  
    });  
})

function openSrc(showType){
	if(txtNums < 1){
		layer.msg("本页文本正在加工中...");
		return;
	}

	txtToDiv();
	showTxt(showType);
	toolopen();
}

function txtToDiv(){
	if(txtNums < 1){
		return;
	}
	
	var activeImg = $(".img-nav-sc .img-top.active").find("img").data("url").replace(".xml",".txt");
	$.ajax({
        url: basePath + "page/srctxt?txtpath=" + activeImg,
        dataType: "json",
        type: "post",
        cache: false,
        success: function (data) {
        	writeTxt(data.txt);
        }
    })
}
function writeTxt(data){
	var _key = $("#searchkey").val();
	var a=data;
	if(_key != null && _key.length > 0){
		var searchkey = _key.split('、');
		if(searchkey == null || searchkey.length < 1){
			return;
		}
		if(searchkey.length>0){
			$.each(searchkey,function(i,k){
				var v = new RegExp(k, "g");
				a = a.replace(v,"<span class='red-txt'>"+k+"</span>");
			})
		}	
	}
	
	var dir = $(".txt-direction").data("dir");
	var txtdata = "";
	if(dir == 'row'){
		txtdata = "<div class='txtcont txt-content'>" + a + "</div>";
	}else{
		txtdata = "<div class='txtcont'>" + a + "</div>";
	}
	
	$("#txt-right .yuanern-txt").html(txtdata);
	$("#txt-bottom .yuanern-txt").html(txtdata);
	$("#txt-win .yuanern-txt").html(txtdata);
}

var srcwin = null;
function showTxt(type){
	if(txtNums < 1){
		return;
	}

	var $height = $(".main").height() - 170;
	//弹窗模式
	if(type == 1){
		srcwin = layer.open({
		  type: 1,
		  title: "本页对应的原文内容",
		  closeBtn: 1,
		  area: ['600px', '400px'],
		  shadeClose: false,
		  content: $('#txt-win')
		});
	}
	
	//上下
	else if(type == 2){
		$("#txt-right").css("display","none");
		$("#txt-bottom").css("display","block");
		
		$(".n2").addClass("active");
		if($(".n3").hasClass("active")){
			$("#txt-right").width(0).removeClass("active");
			$("#container").css("width","100%");	
			$(".n3").removeClass("active")	
		}
		
		if($("#txt-bottom").hasClass("active")){
			$("#txt-bottom").animate({height:"0px"},250,function(){
				$("#txt-bottom").css("display","none");
			}).removeClass("active");
			$("#container").animate({height:$height+"px"},250);
		}else{
			$("#txt-bottom").animate({height:$height/2-5+"px"},250).addClass("active");
			$("#container").animate({height:$height/2+"px"},250);
		}
		txtResize()
	} 
	
	//左右
	else {
		$("#txt-right").css("display","block");
		$("#txt-bottom").css("display","none");
		
		$(".n3").addClass("active");
		if($(".n2").hasClass("active")){
			$("#txt-bottom").animate({height:"0px"},1).removeClass("active");
			$("#container").animate({height:$height+"px"},1);
			$(".n2").removeClass("active")	
		}
		$("#txt-right,#container").height($height);
		if($("#txt-right").hasClass("active")){
			$("#container").animate({width:"100%"},250);
			$("#txt-right").animate({width:"0px"},250).removeClass("active");			
		}else{
			$("#container").animate({width:"50%"},200);
			$("#txt-right").animate({width:"50%"},300).addClass("active");			
		}
		txtResize()
	}	
}

function txtResize(){
	if(txtNums < 1){
		return;
	}

	var r_status = $("#txt-right").css("display");
	if(r_status  == 'block'){
		var $height = $(".main").height() - 170;
		$("#txt-right").height($height+10);	
	}
	
	var b_status = $("#txt-bottom").css("display");
	if(b_status  == 'block'){
		var $height = $(".main").height() - 170;
		$height = $height/2;
		$("#txt-bottom").height($height-5);
		$("#container").height($height);
	}
	$("#txt-right .yuanern-txt").height($("#txt-right").height()-40);
	$("#txt-bottom .yuanern-txt").height($("#txt-bottom").height()-40)
}

//文字工具栏按钮显示开关设置
function toolopen(){
	if($("#txt-right").hasClass("active") || $("#txt-bottom").hasClass("active")){
		$(".show-btn").show();
	}else{
		$(".show-btn").hide();
		hideTool(); //at setting.js
	}
}


//加载文字设置工具所有代码
//为了不节省不必要的内容加载，只有当资源有全文的时候才加载相关内容
function initToolCode(){
	var html = "<div class='setting-info'>";
    	html +="	<div class='setting-info-f'>";
   	 	html +="        <p class='show-btn' title='文字面板设置工具'></p>";
    	html +="        <div class='txt-btn-list clear'>";
    	html +="            <p class='txt-direction btn-col' id='txtdir' data-dir='col' title='文字纵向排列'><span>纵</span></p>";
    	html +="            <p class='txt-family' title='字体'></p>";
    	html +="            <p class='txt-size-title' title='文字大小'></p>";
    	html +="            <div class='txt-color' title='文字颜色'><input id='full' /></div>";
	    html +="            <div class='txt-background' title='背景色'><input id='background' /></div>";
	    html +="            <p class='txt-line-size' title='行间距'></p>";                
	    html +="            <p class='txt-align align-left active' data-id='left' title='文字居左'></p>";
	    html +="            <p class='txt-align align-center' data-id='center' title='文字居中'></p>";
	    html +="            <p class='txt-align align-right' data-id='right' title='文字居右'></p>";
	    html +="            <ul class='clear txt-size'>";
	    html +="                <li class='txt-size-12' data-id='12px' style='font-size: 12px'>12px</li>";
	    html +="                <li class='txt-size-14' data-id='14px' style='font-size: 14px'>14px</li>";
	    html +="                <li class='txt-size-16' data-id='16px' style='font-size: 16px'>16px</li>";
	    html +="                <li class='txt-size-18 active' data-id='18px' style='font-size: 18px'>18px</li>";
	    html +="                <li class='txt-size-24' data-id='24px' style='font-size: 24px'>24px</li>";
	    html +="                <li class='txt-size-32' data-id='32px' style='font-size: 32px'>32px</li>";
	    html +="            </ul>";
	    html +="            <ul class='clear txt-family-list'>";
	    html +="                <li class='txt-family-li' data-id='宋体' style='font-family: 宋体'>宋体</li>";
	    html +="                <li class='txt-family-li' data-id='楷体' style='font-family: 楷体'>楷体</li>";
	    html +="                <li class='txt-family-li' data-id='微软雅黑' style='font-family: 微软雅黑'>微软雅黑</li>";
	    html +="                <li class='txt-family-li' data-id='hm' style='font-family: 微软雅黑'>Himalaya</li>";
	    html +="            </ul>";
	    html +="            <ul class='clear txt-line-list'>";
	    html +="                <li class='txt-line-li active' data-id='12px'>12px</li>";
	    html +="                <li class='txt-line-li' data-id='24px'>24px</li>";
	    html +="                <li class='txt-line-li' data-id='36px'>36px</li>";
	    html +="                <li class='txt-line-li' data-id='48px'>48px</li>";
	    html +="                <li class='txt-line-li' data-id='64px'>64px</li>";
	    html +="                <li class='txt-line-li' data-id='72px'>72px</li>";
	    html +="                <li class='txt-line-li' data-id='96px'>96px</li>";
	    html +="            </ul>";
	    
	    
	    
	    html +="        </div>";
	    html +="    </div>";
	    html +="</div>";
    $('body').append(html);
	    
	var script = "<link rel='stylesheet' href='"+basePath+"plugin/imgviewer/fontstyle/spectrum.css' />";
	    script += "<link rel='stylesheet' href='"+basePath+"plugin/imgviewer/fontstyle/font-style.css' />";
		script += "<script src='"+basePath+"plugin/imgviewer/fontstyle/spectrum.js' type='text/javascript'></script>";
	    script += "<script src='"+basePath+"plugin/imgviewer/fontstyle/docs.js' type='text/javascript'></script>";
		script += "<script src='"+basePath+"plugin/imgviewer/fontstyle/setting.js' type='text/javascript'></script>";	
	$('body').append(script);			
}

function IsDigit() {
   return ((event.keyCode >= 48) && (event.keyCode <= 57));
}

//图片懒加载
function lazyLoad(){
	 "use strict";

	function _instanceof(left, right) { if (right != null && typeof Symbol !== "undefined" && right[Symbol.hasInstance]) { return right[Symbol.hasInstance](left); } else { return left instanceof right; } }
	
	function _classCallCheck(instance, Constructor) { if (!_instanceof(instance, Constructor)) { throw new TypeError("Cannot call a class as a function"); } }
	
	function _defineProperties(target, props) { for (var i = 0; i < props.length; i++) { var descriptor = props[i]; descriptor.enumerable = descriptor.enumerable || false; descriptor.configurable = true; if ("value" in descriptor) descriptor.writable = true; Object.defineProperty(target, descriptor.key, descriptor); } }
	
	function _createClass(Constructor, protoProps, staticProps) { if (protoProps) _defineProperties(Constructor.prototype, protoProps); if (staticProps) _defineProperties(Constructor, staticProps); return Constructor; }
	
	// 图片懒加载类
	var LazyLoad =
	/*#__PURE__*/
	function () {
	  function LazyLoad(el) {
	    _classCallCheck(this, LazyLoad);
	
	    this.imglist = [].slice.call(document.querySelectorAll(el)); // 需使用懒加载的图片集合
	
	    this.imgtotal = Math.ceil(imgtotal / maxImgNum) - 1;
	    /*总的页数*/
	
	    this.isOne = false; //是否点击跳转到第一页，默认不是（false）；
	
	    this.index = 1;
	    this.init(); // 初始化
	  } // 判断是否该图片是否可以加载
	
	
	  _createClass(LazyLoad, [{
	    key: "canILoad",
	    value: function canILoad() {
	      var imglist = this.imglist;
	      this.index = 1;
	
	      for (var i = imglist.length; i--;) {
	        // 缩写，相当于if true
	        this.getBound(imglist[i]) && this.loadImage(imglist[i], i);
	      }
	    } 
		// 获取图片与窗口的信息
	  }, {
	    key: "getBound",
	    value: function getBound(el) {
	      var bound = el.getBoundingClientRect(); //当前页码
	
	      var nowPage = $("#nowPage").val(); //父级元素距离左边的距离
	
	      var left = 0 - $("#left").val(); //获取当前屏幕最大的宽度
	
	      var w = $("#img-nav").width(); //针对点击跳转到第一页的时候特别判断，目前发现的问题，如果进入的时候指定了跳转页码，会在初始化的时候进行渲染
	
	      if (this.isOne && nowPage < 1) {
	        if (el.parentNode.getAttribute("data-num") <= maxImgNum * 2) {
	          return true;
	        }
	      } //针对点击跳转到最后一页的时候特别判断
	
	
	      if (this.imgtotal <= nowPage) {
	        if (el.parentNode.getAttribute("data-num") > nowPage * maxImgNum || el.parentNode.getAttribute("data-num") > (nowPage - 1) * maxImgNum) {
	          return true;
	        }
	      } // 图片左边的距离+当前页*当前屏幕所能显示的最大宽度 > 父级div所left的距离
	      // 并且图片左边的距离+当前页*当前屏幕所能显示的最大宽度 < 父级div所left的距离+当前屏幕所能显示的最大宽度，从而推算出是否需要加载
	
	
	      return bound.left + nowPage * w + w > left && bound.left + nowPage * w < w * 2 + left;
	    } // 加载图片
	
	  }, {
	    key: "loadImage",
	    value: function loadImage(el, index) {
	      // 获取之前设置好的data-original值
	      var src = el.getAttribute('data-original'); //移除当前元素的lazyload属性，避免屏幕大小发生改变造成二次加载
	      $(el).removeClass("lazyload"); // 赋值到src，从而请求资源
	
	      el.src = src; // 避免重复判断，已经确定加载的图片应当从imglist移除
	
	      this.imglist.splice(index, 1);
	    } // 当左边距发生变化并点击的时候，继续判断
	
	  }, {
	    key: "bindEvent",
	    value: function bindEvent() {
	      var thi = this;
	      this.isOne = true;
	      document.getElementById("left").addEventListener('click', function () {
	        thi.imglist.length && thi.canILoad();
	      });
	    } // 初始化
	
	  }, {
	    key: "init",
	    value: function init() {
	      this.canILoad();
	      this.bindEvent();
	    }
	  }]);
	
	  return LazyLoad;
	}(); // 实例化对象，参数则是需要使用懒加载的图片类名

	var lazy = new LazyLoad('.lazyload');
}