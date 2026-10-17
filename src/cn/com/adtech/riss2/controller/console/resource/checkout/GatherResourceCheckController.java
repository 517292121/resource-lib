package cn.com.adtech.riss2.controller.console.resource.checkout;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IGatherResourceService;
import cn.com.adtech.riss2.core.CommonUtil;

import com.jbetter.framework.session.Pagination;
import com.oreilly.servlet.ServletUtils;

public class GatherResourceCheckController extends MultiActionController{

	private IGatherResourceService gatherResourceService = null;
	public void setGatherResourceService(
			IGatherResourceService gatherResourceService) {
		this.gatherResourceService = gatherResourceService;
	}


	public ModelAndView initResourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/checkout/gatherAuditList");
	}
	
	
	public void gatherResourceListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		
		// 查询条件一：状态
		int rsdaStatus = ServletRequestUtils.getIntParameter(request, "rsdaStatus", 0);
		// 查询条件二：题名
		String title = ServletRequestUtils.getStringParameter(request, "rsdaTitle","");
		
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaBuilddate");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
		
		List<Object> list = this.gatherResourceService.getGatherResourceList(pagination, title, rsdaStatus, sort, dir);
		String result = CommonUtil.createJsonTable(list, new String[] {"rsdaId", "rsdaTitle", "rltyId", "rmstName",
				"rsdaBuilddate", "rsdaStatus"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}
	
	
	public void auditGatherResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rsdaids = ServletRequestUtils.getStringParameter(request, "ids","");
		int statu = ServletRequestUtils.getIntParameter(request, "statu", 2);
		this.gatherResourceService.updateGatherResourceStatus(rsdaids, statu);
	}
}
