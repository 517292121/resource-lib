package cn.com.adtech.riss2.filter;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.com.adtech.riss2.core.IoWork;

public class UseDateFilter implements Filter {

	private static long enddate = 0l;
	private Map<String, Integer> dataCache = new HashMap<String, Integer>();
	
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		
		String path = httpRequest.getContextPath();
		String requrl = httpRequest.getRequestURI();  //请求进来的url路径
		
		// 如果请求的页面是登录页面.则直接退出
		if (requrl.contains("/images/")
				|| (requrl.contains("/css/"))
				|| (requrl.contains("/ext/"))
				|| (requrl.contains("/pages/"))
				|| (requrl.contains("/freeze.html"))
				|| (requrl.contains("/viewsjs/"))
				|| (requrl.contains("/js/"))) {
			chain.doFilter(request, response);
			return;
		}
		int nowDay = new Date().getDay();
		if(dataCache.get("day") != null && nowDay == dataCache.get("day")){
			chain.doFilter(request, response);
			return;
		}
		
		String lastfilepath = this.getClass().getResource("/lastdate.properties").toString().substring(6);
		String strdata = IoWork.ReaderFile(lastfilepath).trim();
		long predate = 0L;
		if(strdata.length() > 4){
			predate = Long.parseLong(strdata);
		}
		
		long now = System.currentTimeMillis();
//		System.out.println("系统结束时间：" + new Date(enddate).toLocaleString());
//		System.out.println("系统当前时间：" + new Date(now).toLocaleString());
		if(now < predate){
			httpResponse.sendRedirect(path+"/freeze.html");
			return;
		}
		if(now < enddate){
			IoWork.WriteFile(now+"", lastfilepath);
			
			dataCache.put("day", nowDay); //记录当天到缓存
			chain.doFilter(request, response);		
		}else{
			httpResponse.sendRedirect(path+"/freeze.html");
		}
	}

	public void init(FilterConfig config) throws ServletException {
		System.out.println("进入init——————————————————————————————————————————");
		String endfilePath = this.getClass().getResource("/end.properties").toString();
		endfilePath = endfilePath.substring(6);
		File file= new File(endfilePath);
		if(file.exists() == false){
			try {
				boolean b = file.createNewFile();
				if(b == false){
					return;
				}
			} catch (IOException e) {
				return ;
			}
		}
		String content = IoWork.ReaderFile(endfilePath);
		content = content.replace("\n", "");
		if(content == null || content.length() < 4){
			IoWork.WriteFile(getEnddate(), endfilePath);
		}
		
		if(enddate < 1){
			try {
				enddate = Long.parseLong(IoWork.ReaderFile(endfilePath).trim());
			} catch (Exception e) {
				enddate = Long.parseLong(getEnddate());
			}
		}
		System.out.println("初始化试用时间完成，系统结束时间：" + new Date(enddate).toLocaleString());
	}
	
	private String getEnddate(){
		long dayM = 86400000l; //一天的毫秒数
		long end = System.currentTimeMillis() + (dayM*90);
		return end + "";
	}
}
