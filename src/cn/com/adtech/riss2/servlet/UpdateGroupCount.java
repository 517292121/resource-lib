package cn.com.adtech.riss2.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.com.adtech.riss2.bso.resource.IResDynamicTypeService;

/**
 * 更新自定义分类统计
 * 
 * @author MaChao
 * 
 * @date 2011-04-22
 *
 */
public class UpdateGroupCount extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private IResDynamicTypeService typeService;

	
	/**
	 * 更新索引文件以及首页缓存数据
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		logger.info("更新缓存开始---------------------------");
		long begin = System.currentTimeMillis();
		typeService = (IResDynamicTypeService) WebApplicationContextUtils.getWebApplicationContext(getServletContext()).getBean("dynamicService");
		try {
			typeService.updateDynamicCount();
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print(e.getMessage());
		}
		long end = System.currentTimeMillis();
		
		String useTime = (end - begin) / 1000.00 + " 秒";
		
		logger.info("更新缓存结束---------------------------");
		response.getWriter().print("此次更新缓存用时：" + useTime);
		response.getWriter().flush();
	}

	/**
	 * 直接启用doGet方法执行
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
