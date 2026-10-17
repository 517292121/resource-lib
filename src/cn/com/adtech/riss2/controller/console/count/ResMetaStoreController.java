package cn.com.adtech.riss2.controller.console.count;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.count.ISysCountService;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;

/**
 * 特色库资源访问量统计
 * @author MaChao
 *
 */
public class ResMetaStoreController extends MultiActionController{
	private ISysCountService sysCountService = null; 
	public void setSysCountService(ISysCountService sysCountService) {
		this.sysCountService = sysCountService;
	}
	
	public ModelAndView initPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("count/resMetaStoreInfo");
	}
	
	public ModelAndView getMetaTwoinitPage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long MetaId = ServletRequestUtils.getLongParameter(request, "MetaId",0l);
		int year = ServletRequestUtils.getIntParameter(request, "year", 0);
		int month = ServletRequestUtils.getIntParameter(request, "month", 0);
		
		request.setAttribute("MetaId", MetaId);
		ResMetaStore metaStore = (ResMetaStore)sysCountService.get(ResMetaStore.class,MetaId);
		request.setAttribute("metaStore", metaStore);
		request.setAttribute("year", year);
		request.setAttribute("month", month);
		return new ModelAndView("count/resMetaStoreTwo");
	}
	
	public void getMetaCountJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);// 设置当前页
		pagination.setPageResults(limit);// 设置每页记录数
		
		int year = ServletRequestUtils.getIntParameter(request, "year", 0);
		int month = ServletRequestUtils.getIntParameter(request, "month", 0);
		
		//排序相关
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaCount");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
		String json = sysCountService.getRisMetaStoreJson(pagination, sort, dir, year, month);
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		
	}
	
	public void getMetaTwoCountJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		
		int year = ServletRequestUtils.getIntParameter(request, "year", 0);
		int month = ServletRequestUtils.getIntParameter(request, "month", 0);
		
		// 添加分页器
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);// 设置当前页
		pagination.setPageResults(limit);// 设置每页记录数
		Long MetaId = ServletRequestUtils.getLongParameter(request, "MetaId",0l);
		//排序相关
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaCount");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
		String json = sysCountService.getRisMetaStoreTwoJson(pagination,MetaId,year,month, sort, dir);
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
		
	}
	

}
