<%@ page language="java" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>资源导入介绍</title>
    <style type="text/css">
        *{padding: 0;margin: 10px;font-family: 'Microsoft YaHei UI', 'Microsoft YaHei', SimSun, 'Segoe UI',Tahoma, Helvetica, Sans-Serif;}
        h4{color: #333;}
        ul{margin-bottom: 20px;}
        ul li,ul li b{font-size: 14px; color: #444;}
        .a{list-style: none; margin: 0px;}
    </style>
</head>
<body>
    <h2>资源导入介绍</h2> 
    <a href="<%=basePath %>console/data/resource/importExcel.htm?action=doImport&rminId=${rminId }">&lt;返回</a>
    <hr />
    
    <h4>文件格式资源</h4>
    <ul>
    	<li>文件格式即传统的资源格式，其单亲录入与批量导入与以往的资源录入格式保持不变</li>
    	<li class="a"><a href="<%=path %>/res/demo/file">资源示例下载</a></li>
    </ul>
    
    <h4>目录树格式资源</h4>
    <ul>
    	<li>每个资源为一个文件夹，文件夹名称与元数据表中资源名称相同</li>
    	<li>资源文件夹中的最完整的可包含以下内容：<br />
    		<span style="color: red">[必须]</span><b>图片（jpg）</b>：用于资源展示，图片格式包括：jpg、jpeg、png、gif、bmp，命名未限制；<br />
    		<span style="color: #19967a">[可选]</span><b>封面cover.jsp</b>：尺寸为宽：180px，高：250px，未指定时系统根据第一张图片自动生成；<br />
    		<span style="color: #19967a">[可选]</span><b>目录信息表（xls）</b>：用于预览时章节目录导航，也可进行检索。命名与文件夹名称相同；<br />
    		<span style="color: #19967a">[可选]</span><b>完整版全文（txt）</b>：整个资源所有的总全文内容，命名与文件夹名称相同；<br />
    		<span style="color: #19967a">[可选]</span><b>单页全文（txt）</b>：每一页对应的全文，命名与图片名称相同。可以每页都有，也可以只有部分；<br />
	    	<p><i style="color: #ca3131;">资源文件夹中的内容中，除图片jpg外，其它的都不是必须的。封面系统可自动生成，其它内容没有便没有相应的功能而已！</i></p>
    	</li>
    	
    	<li>批量导入时：资源目录为资源文件夹的上一级目录。由于不受项目配置文件的主目录限制，资源可以存放在服务器任务位置；</li>  
    	<li>由于资源阅读时，平台根据目录位置即时读取，因此资源导入前或导入后任何时候创建资源文件都能生效。<br/>
    		但是如果资源审核时还未创建，则无法读取全文进行索引创建，不能进行全文检索；
    	</li>	
    	<li class="a"><a href="<%=path %>/res/demo/img">资源示例下载</a></li>
    </ul>
    
    <h4>HTML格式</h4>
    <ul>
    	<li>文件名的取法与传统资源一致，首先以设置的文件名为主，如果没有则采用题名作为文件名；</li>
    	<li>html格式预览的资源，每个资源为一个文件夹，文件夹名称与资源名称相同，系统支持中文名，但推荐采用英文名；</li>
    	<li>资源文件夹中的内容，必须有一个统一html文件，其文件名为导入时设置的“html引导页文件名”；</li>
    	<li>由于html需要在线打开，因此必须存放在tomcat或iis的服务器当中，可以是本地服务器，也可以是外部任何服务器；</li>
    	<li>导入时配置的“html根路径”为html在tomcat所在文件夹的上一级访问地址，如：http://222.222.1.17:8008/htmlfile；</li>
    	<li>系统自动根据“html根路径”+文件名+“html引导页文件名”，如：http://222.222.1.17:8008/htmlfile/book01/index.html；</li>
    	<li class="a"><a href="<%=path %>/res/demo/html">资源示例下载</a></li>
    </ul>
    
    <p><a href="<%=basePath %>console/data/resource/importExcel.htm?action=doImport&rminId=${rminId }">&lt;返回</a></p>
  </body>
</html>
