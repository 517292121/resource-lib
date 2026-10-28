package cn.com.adtech.riss2.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.adtech.riss2.bso.cache.IPlatformCacheService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.DatabaseBackup;

/**
 * 一键更新系统前台索引以及首页缓存
 * 
 * @author MaChao
 * 
 * @date 2011-04-22
 *
 */
public class UpdateAllCache extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private IPlatformCacheService cacheService;

	public String updateCache(){
		SysConfigInfo.initConfig(getServletContext().getContextPath());
		cacheService = (IPlatformCacheService) WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("platformCacheService");
		
		long begin = System.currentTimeMillis();
		cacheService.updateAll();
		long end = System.currentTimeMillis();

		//启动数据库自动备份
		DatabaseBackup back = new DatabaseBackup();
		back.listenerRun();
		
		String useTime = (end - begin) / 1000.00 + " 秒";
		System.out.println("更新缓存用时：" + useTime);
		
		return "更新缓存用时："+(end - begin)/1000.0;
	}
	
	public void init() throws ServletException {
		updateCache();
	}
	
	/**
	 * 更新索引文件以及首页缓存数据
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String mesg = updateCache();
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		
		out.print(mesg);
		out.flush();
	}

	/**
	 * 直接启用doGet方法执行
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
