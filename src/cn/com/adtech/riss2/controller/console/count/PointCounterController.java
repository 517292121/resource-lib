package cn.com.adtech.riss2.controller.console.count;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.count.ISysCountService;

public class PointCounterController  extends MultiActionController{

	private ISysCountService sysCountService = null; 
	public void setSysCountService(ISysCountService sysCountService) {
		this.sysCountService = sysCountService;
	}
	
	
	public ModelAndView initPointCounterView(HttpServletRequest request,HttpServletResponse response){
		return new ModelAndView("count/PointCounter");
	}
	
	public void getPointCounterToJson(HttpServletRequest request, HttpServletResponse response){
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = null;
		try {
		    out = response.getWriter();
			int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
			int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
			int currentPage = start / limit;
			// 添加分页器
			Pagination pagination = new Pagination();
			pagination.setCurrentPage(currentPage);// 设置当前页
			pagination.setPageResults(limit);// 设置每页记录数
			
			String loginname = ServletRequestUtils.getStringParameter(request, "loginname","");
			String realname = ServletRequestUtils.getStringParameter(request, "realname","");
			//排序相关
			String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaCount");// 排序字段
			String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
			String json = sysCountService.getPointcountToJson(pagination, loginname, realname, sort, dir);
			out.print(json);
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
	}
}
