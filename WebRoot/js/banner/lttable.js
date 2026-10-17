
/***************************************************************************************
 	* 平台框架中表格处理对象
 	* 主要功能包括：
 	* 1、根据json数据接口和配置自动创建表格
 	* 2、点击后选定当前行数据，并支持复选和单选两种试，复选支持全选和全不选
 	* 3、获取选中行的数据ID，多选表格获取第一条选中的数据ID和所有选中ID方法
 	* 4、表格双击处理
 	*	 
 	* 该接口已测试过
 	*
 	* @see lttable.css
 	* 
 *
 ***主要配置项：截止2019-09-29
 *	url：string [必备] 数据请求接口地址
 *	idfiled: string [必备] 每列数据的ID字段
 *	sortInfo: object [可选] 排序信息，包括排序字段和排序方式
 *	dbclick：function [可选]  双击事件时调用处理方法
 *	Mchoose: boolean [可选] 是否开启多选，默认false
 *	ajaxType：post|get [可选] 数据请求类型，默认post
 *	isLoad: boolean [可选] 打开页面是否立即加载数据，默认true
 *	minwid: number [可选] 表格最小宽度，低于该宽度时出现横向滚动条
 *	loadfun: function(json) [可选] 数据请求成功后执行方法，可对json数据用作其它处理
 *	checks: array [可选] 默认选中的ID数组
 *	cols: [	array [必备] 显示列配置
 *	lastCount: boolean [可选] 最后一行是否为统计，如果是则序号变为合计，其它值加粗
 *		{
 *			field：string [必备] 对应json数组中的key
 *			title: string [必备] 表格表头名称
 *			width: number [可选] 列固定宽度，单位px
 *			align: string [可选] 文字对齐方式，默认left
 *			sort: boolean [可选] 是否支持排序，默认false
 *			alt: boolean [可选] 是否支持鼠标放上时提示该列内容，默认false
 *			blank: string [可选] 当值为null或空白时默认显示内容
 *			fun: function(data) [可选] 加载时启用方法，可对该列值进行二次处理
 *		},...
 *	]
 *
 *
 *
 * 
 	* @author  唐莉
 	* 
 	* @date  (开发日期): 2016-06-24
 	* 
 	* @version 1.2
 	*
 	* @remark 使用方式说明
 	* 
 	* 本插件需要依赖jquery，如果要调用通用删除方法则需要依赖layer.js
 	* 
 *
 * 最后更新日期: 2020-04-23
 *
 * 更改人: 王林
 * 
 * 更改内容: 完善配置项注释说明
 * 
 *   
 * 复审人:   王林
 * 
 * 复审时间： 2020-04-23
 * 
 * 复审结果:  已完成
 *
 **************************************************************************************/
var sendData={};  //调用接口所传递的参数
(function ($) {
    $.fn.ctb = function (options) {
    	//获取当前网址，如： http://localhost:8083/uimcardprj/share/meun.jsp
            var curWwwPath = window.document.location.href;
            //获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
            var pathName = window.document.location.pathname;
            var pos = curWwwPath.indexOf(pathName);
            //获取主机地址，如： http://localhost:8083
            var localhostPaht = curWwwPath.substring(0, pos);
            //获取带"/"的项目名，如：/uimcardprj
            var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);
    		var basePath = localhostPaht+projectName;
    
    	var tabWid = options.minwid ? "style='min-width:"+options.minwid+"px'" : "";
    	
        //标签拼接字符串
        var fContent = $(this),
            html="<div class='tl-main-table'>" +
                "<div class='mask'><img height='24px' src='"+basePath+"/images/console/ltplugin/load.gif' alt=''> <label>数据请求中...</label></div>" +
                "<table border='0' cellspacing='1' class='table-body' "+tabWid+">" +
                "<thead><tr><th class='table-biaohao'>&nbsp;</th>", //表格默认

            tbhead = options.cols;//表头创建
            

        //是否支持多选
        if (options.Mchoose == true){
            html = html+ "<th class='choose-box'>" +
             //   "<input type='checkbox' class='choose-all-tr'/></th>"
               "<span class='icon-input-normal choose-all-tr'></span></th>"
        }
        //表头
        $.each(tbhead,function (i,n) {
            //判断是否有回调函数
            html = html + "<th ";
            var style = "";
            if (n.width){
            	style += "width: "+n.width+"px;"
            }
            if (n.align){
            	style += "text-align:" + n.align+";";
            }
            if(style.length > 1){
            	html+="style='"+style+"'";
            }
            html = html + ">"
            if (n.sort){
                if (options.sortInfo != undefined && options.sortInfo.field==n.field){
                    if (options.sortInfo.direction == "ASC"){
                        html = html + "<p class=\"clearfix xu-btn\">\n" +
                            "            <span data-field='"+n.field+"' class=\"xu-up active\" ></span>\n" +
                            "            <span data-field='"+n.field+"' class=\"xu-dowm\" ></span>\n" +
                            "        </p>"
                    }else if (options.sortInfo.direction == "DESC"){
                        html = html + "<p class=\"clearfix xu-btn\">\n" +
                            "            <span data-field='"+n.field+"' class=\"xu-up\" ></span>\n" +
                            "            <span data-field='"+n.field+"' class=\"xu-dowm active\" ></span>\n" +
                            "        </p>"
                    }
                } else {
                    html = html + "<p class=\"clearfix xu-btn\">\n" +
                        "            <span data-field='"+n.field+"' class=\"xu-up\" ></span>\n" +
                        "            <span data-field='"+n.field+"' class=\"xu-dowm\" ></span>\n" +
                        "        </p>"
                }
            }
            html = html + n.title + "</th>";
        })

        html = html + "</tr></thead><tbody></tbody></table></div>";
        //创建表格主体结构
        fContent.append(html);

        //创建分页
        fContent.Page = function (currPage, totalPage, totalNums){
            if(totalPage < 2){
                return "";
            }
            var pageHtml = "<div class='pBar'>";
            if(currPage > 0){
                pageHtml += "<span data-page='0' class='bian fir'>首页</span>";
                pageHtml += "<span class='st' data-page='"+(currPage-1)+"'>&lt;</span>";
            }else{
                pageHtml += "<span class='disable bian fir'>首页</span>";
                pageHtml += "<span class='disable st'>&lt;</span>";
            }

            var begin = (currPage < 5) ? 0 : (currPage - 4);
            var end = begin + 9;
            if(end > totalPage){
                end = totalPage;
            }
            if((end - begin < 9) && begin > 0){
                begin = begin - (9 - (end - begin));
                begin = begin < 0 ? 0 : begin;
            }

            for(p=begin; p<end; p++){
                if(p == currPage){
                    pageHtml += "<span id='curr' class='pageNum'>"+(p+1)+"</span>";
                }else{
                    pageHtml += "<span class='pageNum' data-page='"+p+"'>"+(p+1)+"</span>";
                }
            }
            if(currPage < totalPage-1){
                pageHtml += "<span class='st' data-page='"+(currPage+1)+"'>&gt;</span>";
                pageHtml += "<span data-page='"+(totalPage-1)+"' class='bian'>尾页</span>";
            }else{
                pageHtml += "<span class='disable st'>&gt;</span>";
                pageHtml += "<span class='disable bian'>尾页</span>";
            }
            pageHtml += "<label>共：" + totalNums + "条记录</label>";
            pageHtml += "</div>";
            return pageHtml;
        }

        //请求接口
        fContent.getData = function(urlData){
        //"background":"rgba(0,0,0,0.1)"
			fContent.find(".mask").css({"display":"block"});
            // fContent.data("sendDate",urlData);
            // fContent.data("options",options);
            jQuery.support.cors = true;
            $.ajax({
                url:options.url,
                type:options.ajaxType?options.ajaxType:"post",
                dataType:"json",
                cache:false,
                data:urlData,
                error:function (data) {
                	fContent.find(".mask").css({"display":"block","background":"rgba(0,0,0,0.1)"});
                },
                success:function (data) {
                	//配置中可自定义处理函数，数据加载完成后做其它自定义处理
                	if(typeof (options.loadfun) == "function"){
                        options.loadfun(data);
                    }
                	
                    if(data.success){
                    	
                        //全选按钮清除
                        fContent.find("thead tr .choose-all-tr").removeClass("active");

                        //设置样式
                        var t_min_height = 56+37*data.dataList.length;
                        fContent.find(".tl-main-table").css("height",t_min_height+"px")
                        
                        var Atbody = '';
						if(data.dataList == null || data.dataList.length<1){
							if(fContent.find(".tl-main-table").find("#nodata").length<1){
								fContent.find("tbody").html('');
								fContent.find(".pBar").remove();
								fContent.find(".tl-main-table").append("<p id='nodata' style='padding-top:15px'>没有相关数据...</p>");
							}
							return ;
						}
						$("#nodata").remove();
						
						var checkArr = options.checks;
						
                        //创建tbody
						var len = data.dataList.length;
                        $.each(data.dataList,function (i,n) {

                            var id = i;
                            var trData = n;
                            var dataId = trData[options.idfiled];
                            var isActive = "";
                            if(checkArr && checkArr.indexOf(dataId) >= 0){
                            	isActive = "active";
                            }
                            var tr = "<tr data-id='"+dataId+"' class='"+isActive+"'>";
                            
                            if(options.lastCount && id == len -1){
                            	tr += "<td class='table-biaohao'>合计</td>";
                            }else{
                            	tr += "<td class='table-biaohao'>"+(id+1)+"</td>";
                            }
                            
                                if (options.Mchoose == true){
                                    // tr = tr + "<td class='choose-box'><input data-id='"+trData[options.idfiled]+"'  class='choose-the-tr' type='checkbox'></td>";
                                    tr = tr + "<td class='choose-box'><span data-id='"+dataId+"'  class='icon-input-normal choose-the-tr "+isActive+"'></span></td>";

                                }
                                $.each(options.cols,function (k,h) {
                                    $.each(trData,function (i,n) {
                                    	if(n == null || n.length < 1){
                                    		if(h.blank != undefined){
                                    			n = h.blank;
                                    		}else{
                                    			n = '';
                                    		} 
                                    	}
                                        if (h.field==i){
                                            if (typeof(h.fun) == "function" ){
                                                n = h.fun(n)
                                            }
                                            
                                            var alt = "";
                                            if(h.alt){
                                            	alt = " title='"+n+"'";
                                            }
                                            if (h.align){
                                               tr = tr + "<td"+alt+" style='text-align: "+h.align+"'>"+n+"</td>";
                                            } else {
                                               tr = tr + "<td"+alt+">"+n+"</td>";
                                            }

                                        }
                                    })
                                })
                                tr = tr+"</tr>";
                                Atbody = Atbody + tr;
                        })
                        fContent.find("tbody").html(Atbody)

                        //创建页码  currPage, totalPage, totalNums
                        fContent.find(".pBar").remove();
                        var pagehtm = fContent.Page(data.currtPage,data.totalPage,data.totalRows);

                        //页码点击事件
                        fContent.append(pagehtm).find(".pBar span").click(function () {
							if($(this).hasClass("disable")){
								return;
							}
                            fContent.data("page",$(this).data("page")-0);
                            sendData.currentPage = $(this).data("page")-0;
                            console.log($(this).data("page"))
                            fContent.getData(sendData);
                        })
                        //双击事件
                        if (typeof (options.dbclick) == "function"){
                            fContent.find("tbody tr").dblclick(function () {
                                var id = $(this).data("id");
                                options.dbclick(id);
                            });
                        }
                        //行点击事件   单行选中
                        fContent.find("tbody tr,tbody tr .choose-the-tr").click(function () {
                            inputCheck($(this));
                        })
                        //复选框处理函数
                        function inputCheck(obj) {
                            if (options.Mchoose){ //可多选
                                var objTagName = obj[0].tagName;
                                if (objTagName=="TR"){
                                    obj.toggleClass("active");
                                    if (obj.hasClass("active")){
                                        // obj.find(".choose-the-tr").attr("checked", "checked");
                                        obj.find(".choose-the-tr").addClass("active");
                                        var flag = false;
                                        $.each(obj.siblings(),function (i,n) {
                                            // if (!$(n).find(".choose-the-tr").attr("checked")){
                                            if (!$(n).find(".choose-the-tr").hasClass("active")){
                                                flag = true;
                                                return;
                                            }
                                        })
                                        if (!flag){
                                            // fContent.find("thead tr .choose-all-tr").attr("checked","checked");
                                            fContent.find("thead tr .choose-all-tr").addClass("active");
                                        }
                                    }else {
                                        // obj.find(".choose-the-tr").attr("checked", false);
                                        obj.find(".choose-the-tr").removeClass("active");
                                        // fContent.find("thead tr .choose-all-tr").attr("checked",false);
                                        fContent.find("thead tr .choose-all-tr").removeClass("active");
                                    }
                                }
                            } else { //不可多选
                                obj.toggleClass("active").siblings().removeClass("active");
                            }
                        }
                    }
                },
                complete:function () {
                    setTimeout(function(){
                    fContent.find(".mask").css("display","none");
                    },300)
                },
                error:function (data) {

                }
            })
        }

        //全选按钮
        fContent.find("thead tr .choose-all-tr").click(function () {
            var type = false;
            $(this).toggleClass("active");
            if ($(this).hasClass("active")){
                type =  true;
            }
            $.each(fContent.find("tbody tr"),function (i,n) {
                if (type==true){
                    $(n).addClass("active").find(".choose-the-tr").addClass("active");
                } else {
                    $(n).removeClass("active").find(".choose-the-tr").removeClass("active");
                }
                // $(n).find(".choose-the-tr").attr("checked",type);
            })
        })
        
        //页面初始化
        if (options.sortInfo != undefined && options.sortInfo.field){
            //初始化排序参数配置
            sendData = {sort:options.sortInfo.field,dir:options.sortInfo.direction}
        }else {
            sendData = {} ;
        }
        
        //是否即时加载数据
        if(options.isLoad == undefined || options.isLoad == true){
        	fContent.getData(sendData)
        }else{
        	$(".mask").hide();
        }
        
        fContent.find(".xu-up").click(function () {
            fContent.find(".xu-dowm,.xu-up").removeClass("active");
            $(this).addClass("active").siblings().removeClass("active");
            
            //升序
            fContent.find("tbody").html('');
            sendData.sort = $(this).data("field");
            sendData.dir = "ASC";
            fContent.getData(sendData)
        })
        fContent.find(".xu-dowm").click(function () {
            fContent.find(".xu-dowm,.xu-up").removeClass("active");
            $(this).addClass("active").siblings().removeClass("active");
            //降序
            fContent.find("tbody").html('');
            sendData.sort = $(this).data("field");
            sendData.dir = "DESC";
            fContent.getData(sendData)
        })


        //查询
        fContent.sh = function (key) {
            $.each(key,function (i,n) {
                sendData[i] = n ;
            })
            // fContent.update(sendData)
            sendData.currentPage = 0;
            fContent.getData(sendData);
        }

        //刷新
        fContent.reload = function () {
            fContent.find("thead tr .choose-all-tr").removeClass("active");
            fContent.getData(sendData);
        }
        //获取选中对象第一个的id值
        fContent.id = function () {
            var s = fContent.find(".tl-main-table tbody tr.active").length;
            if(s < 1){
                return "";
            }
            var FirstId = fContent.find(".tl-main-table tbody tr.active")[0];
            return String($(FirstId).data("id"));
        }


        //获取所有选中对象的id值
        fContent.ids = function () {
            var AllDate = fContent.find(".tl-main-table tbody tr.active"),
                idList = [];
            if(AllDate.length < 1){
                return "";
            }
            $.each(AllDate,function (i,n) {
                idList.push($(n).data("id"))
            })
            return String(idList);
        }

		//普通删除数据通用方法
        fContent.del = function (delurl) {
        	var ids = "";
        	if(options.Mchoose == true){
        		ids = fContent.ids();
        	}else{
        		ids = fContent.id();
        	}
			if(ids.length < 1){
				layer.msg("请选择需删除的记录",{icon:7});
				return;
			}
			layer.confirm('您确定要删除选择的记录吗？', {
			    btn: ['确定','取消'] //按钮
			}, function(){
				var comLoading = layer.load(4, {
				  shade: [0.1,'#fff'] //0.1透明度的白色背景
				});
				$.ajax({
				    type: 'GET',
				    url: delurl + ids,
				    cache:false,
				    dataType:'json',
				    success: function(data){
				    	layer.close(comLoading);
				    	if(data.success){
				    		layer.msg("删除成功！",{icon:1});
				    		fContent.reload();
				    	}else{
				    		layer.msg(data.msg, {icon: 2});
				    	}	
				    },
				    fail: function (err, status) {
				    	layer.close(comLoading);
				    	layer.msg('错误：'+err, {icon: 2});
				    }
				});
			});
        }
        
        return fContent;
    }
})(window.jQuery);
