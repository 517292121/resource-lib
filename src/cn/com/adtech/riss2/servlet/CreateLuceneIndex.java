package cn.com.adtech.riss2.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.unionlib.lucene.dao.IAllDataIndexService;

public class CreateLuceneIndex extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		IAllDataIndexService dataIndexService = (IAllDataIndexService) WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("alldataIndexService");
		
		logger.info("更新全文检索索引开始---------------------------");
		long begin = System.currentTimeMillis();
		
		//将所有资源创建到索引
		dataIndexService.saveAlldataToIndex(request);
		
		long end = System.currentTimeMillis();
		
		String useTime = (end - begin) / 1000.00 + " 秒";
		logger.info("更新全文检索索引结束---------------------------");
		response.getWriter().print("更新索引用时：" + useTime);
		response.getWriter().flush();
	}
	

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
