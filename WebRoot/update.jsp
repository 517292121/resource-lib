<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="cn.com.adtech.riss2.bso.ICommonService"%>
<%@page import="java.util.List"%>
<%@page import="cn.com.adtech.riss2.domain.resource.ResExtendData"%>
<%
	ICommonService commonService = (ICommonService)WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("commonService");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>元数据值相互同步</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  </head>
  
  <body>
    将自定义元素的值同步到核心RES_SOURCE_DATA表中 <hr>
    
    <%
		/*****运行前必须先给以下四个变量赋值**********/
    	Long rltyId = 7L; //资源类型ID 以视频为例
    	Long old_repoId = null;  //需要同步的自定义元数据ID
    	Long new_repoId = null;  //同步到的新元数据ID
    	String eleName = "rsdaDate"; //需要更新的ResSourceData对象的属性名称，以日期为例
    	
		//查询出所有待同步的资源ID，具体需要同步的查询条件根据情况而定    	
    	String hql = "select rsda.rsdaId from ResSourceData rsda where rsda.rsdaRltyId=" + rltyId;
    	List<Long> list = commonService.find(hql);
    	
    	String updateHql = "";
    	Long rsdaId = null;
    	ResExtendData extendData = null;
    	for(int i=0,s=list.size(); i<s; i++){
    		System.out.println("当前：" + (i+1) + "/" + s);
    		try{
    			rsdaId = list.get(i);
				//查出元数据的值
	    		hql = "select reda.redaData from ResExtendData reda where reda.redaRsdaId=" + rsdaId + " and reda.redaRepoId=" + old_repoId;	
	    		String redaData = commonService.getSingleData(hql);
    		
    			//将元数据的值同步到核心表中
    			updateHql = "update ResSourceData set "+eleName+"=? where rsdaId=" + rsdaId;
    			commonService.getServiceHibernateTemplate().bulkUpdate(updateHql, redaData);
				
				//同步将该元数据保存到    			
    			extendData = new ResExtendData();
   				extendData.setRedaRsdaId(rsdaId);
   				extendData.setRedaRepoId(new_repoId);
   				extendData.setRedaData(redaData);
   				extendData.setRedaLanguage(1L); //语种，默认为1
   				commonService.save(extendData);
    			
    			//将之前的元数据值删除
    			updateHql = "delete from ResExtendData where redaRepoId=" + old_repoId + " and redaRsdaId=" + rsdaId;
    			commonService.getServiceHibernateTemplate().bulkUpdate(updateHql);
	    		out.println(list.get(i) + "同步成功<br>");
    		} catch (Exception e){
    			out.println(list.get(i) + "保存失败"+e.getMessage()+"<br>");
	    		e.printStackTrace();
	    	}
    	}
    	
    	out.print("========保存完成！===========");
     %>
  </body>
</html>
