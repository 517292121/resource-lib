<%@page import="cn.com.unionlib.lucene.bean.ResMainBean"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
  <head>
    <title>项目分类列表</title>
	<meta http-equiv="pragma" content="no-cache"></meta>
	<meta http-equiv="cache-control" content="no-cache"></meta>
	<meta http-equiv="expires" content="0"></meta>    
	<link rel="stylesheet" href="<%=basePath %>css/console/common.css" type="text/css"></link>
	<link rel="stylesheet" href="<%=basePath %>plugin/table/lttable.css" type="text/css"></link>
	<style type="text/css">
		table tr td em{font-style: normal; color: red;}
		a.perview{color: #136ec2; text-decoration: underline;}
	</style>
	
	<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
	<script type="text/javascript" src="<%=basePath %>plugin/layer/layer.js"></script>
	<script type="text/javascript" src="<%=basePath %>js/ui-table.js"></script>  
	<script type="text/javascript" src="<%=basePath %>plugin/table/lttable.js"></script> 
	<script type="text/javascript" src="<%=basePath %>js/platform/common.js"></script> 
	
	<script type="text/javascript">
		var myWin = null;
		var myTable = null;
		$(function(){
			myTable = $(".table-box").ctb({
	            dbclick:function (id) {  //id为双击选中行数据的id值
	            	detail(id)
	            },
	            url: '<%=basePath %>console/indexManager.htm?action=listJson&dataType=${dataType}',
	            idfiled:"id", //id所对应的字段
	            Mchoose:true,    //是否开启多选默认false
	            cols: [  //显示的字段
	            	{
	                    field:'title',
	                    title: '题名' 
	                },
	                ${groupConfig}
	                {
	                    field:'language',
	                    title: '语种',
	                    width: 100
	                },
	                {
	                    field:'id',
	                    title: '预览',
	                    width: 100,
	                    fun:function (data) { //对应的回调函数
	                        if ('${dataType}' == "<%=ResMainBean.DATA_TYPE_RES %>"){
	                            return "<a href='<%=basePath %>res/"+ idToStr(data) +"' class='perview' target='_blank'>预览</a>";
	                        }else if ('${dataType}' == "<%=ResMainBean.DATA_TYPE_TEXT %>"){
	                            return "<a href='<%=basePath %>text/"+ idToStr(data) +"' class='perview' target='_blank'>预览</a>";
	                        }else if ('${dataType}' == "<%=ResMainBean.DATA_TYPE_INFO %>"){
	                            return "<a href='<%=basePath %>info/detail/"+ idToStr(data) +"' class='perview' target='_blank'>预览</a>";
	                        }else {
	                            return data;
	                        }
	                    }
	                }
	            ]
	        });
	        
	        $("#key").bind('keypress',function(event){
	            if(event.keyCode == "13") {
	 				search();
	            }
	        });
		})
		
		function search(){
			var keyValue = $("#key").val();
			var language = $("#lan").val();
            myTable.sh({
            	key:keyValue,
            	lan: language
            });
		}
		
		function detail(id){
			if(id == null || id.length < 1){
				id = myTable.id();
			}
			if(id == null || id.length < 1){
				layer.msg("请选择待查看的资源", {icon:2});
				return;
			}
			window.location.href="<%=basePath %>console/indexManager.htm?action=resdetail&id=" + id;
		}
		
		function del(){
			var ids = myTable.ids();
			if(ids.length < 1){
				layer.msg("请选择需删除的记录",{icon:7});
				return;
			}
			myTable.del("<%=basePath %>console/indexManager.htm?action=delindex&ids="+ids);
		}
		
		function back(){
			window.location.href = "<%=basePath %>console/indexManager.htm";
		}

		function tbref(){
			myTable.reload();
		}
			
	</script>
  </head>

  <body>
     <div class="pagenav">
     	当前位置：<a href="<%=basePath %>console/indexManager.htm">索引管理</a> > 资源管理 
     </div>
     <div class="pagetool">
     		<input type="text" name="key" id="key" value="" class="input-text" style="width: 130px" placeholder='检索词' /> 
     		<select name="lan" id="lan" class="input-text">
     			<option value="">所有语种</option>
     			<option value="zh">中文</option>
     			<option value="zw">藏文</option>
     			<option value="mw">蒙文</option>
     			<option value="en">英文</option>
     			<option value="none">其它</option>
     		</select>
     		<input type="button" class="btn" value="检索" onclick="search();"/>
	     	<input type="button" class="btn" value="删除" onclick="del();" />
	     	<input type="button" class="btn" value="详情" onclick="detail('');" />
	     	<input type="button" class="btn" value="返回" onclick="back();"/>
     </div>
     
  	 <div class="table-box"></div>
     ${message }
  </body>
</html>
