package cn.com.adtech.riss2.controller.console.resource.manager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.resource.IExportResourceService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ExcelUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.exception.ErrorMessageException;

public class ExportResourceController extends MultiActionController {

	private IExportResourceService exportResourceService;
	public void setExportResourceService(
			IExportResourceService exportResourceService) {
		this.exportResourceService = exportResourceService;
	}
	
	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return new ModelAndView("resource/manager/exportResource");
	}
	
	
	public void getResourceListJson(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);
		pagination.setPageResults(limit);
		
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
		String title = ServletRequestUtils.getStringParameter(request, "rsdaTitle", "");
		String beginStr = ServletRequestUtils.getStringParameter(request, "beginDate", "");
		String endStr = ServletRequestUtils.getStringParameter(request, "endDate", "");
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if(CommonUtil.isNotNull(title)){
			paramMap.put("rsdaTitle", title);
		}
		if(CommonUtil.isNotNull(beginStr)){
			try {
				Date begin = ConstantsObj.FORMAT_DATE.parse(beginStr);
				paramMap.put("beginDate", begin);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(CommonUtil.isNotNull(endStr)){
			try {
				Date end = ConstantsObj.FORMAT_DATE.parse(endStr);
				paramMap.put("endDate", end);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaBuilddate");
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		
		if(sort.equals("rmstName")){
			sort = "resMetaStore.rmstName";
		}
		if(sort.equals("rltyName")){
			sort = "resLiterType.rltyName";
		}
		
		List<Object[]> list = this.exportResourceService.getResourceList(pagination, rmstId, rltyId, paramMap, sort, dir);
		
		String[] keys = { "rsdaId", "rsdaTitle", "rmstName", "rltyName", "rsdaBuilddate"};
		
		String jsondata = CommonUtil.createJsonTable(list, keys, pagination.getTotalResults() + "");
		
		out.write(jsondata);
		out.flush();
		out.close();
	}
	
	
	/**
	 * 导出资源
	 *
	 * @author MaChao
	 * @date Nov 7, 2016
	 *
	 * @param request
	 * @param response
	 * @param page
	 * @throws Exception
	 */
	public void exp(HttpServletRequest request,
			HttpServletResponse response,PageExtNative page) throws Exception {
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
		String title = ServletRequestUtils.getStringParameter(request, "rsdaTitle", "");
		String beginStr = ServletRequestUtils.getStringParameter(request, "beginDate", "");
		String endStr = ServletRequestUtils.getStringParameter(request, "endDate", "");
		
		try {
			if(rmstId < 1L){
				throw new ErrorMessageException("必须指定需导出的特色库！");
			}
			if(rltyId < 1L){
				throw new ErrorMessageException("必须指定需导出的资源类型！");
			}
			
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("rmstId", rmstId);
			paramMap.put("rltyId", rltyId);
			if(CommonUtil.isNotNull(title)){
				paramMap.put("rsdaTitle", title);
			}
			if(CommonUtil.isNotNull(beginStr)){
				try {
					Date begin = ConstantsObj.FORMAT_DATE.parse(beginStr);
					paramMap.put("beginDate", begin);
				} catch (ParseException e) {
					throw new ErrorMessageException("起始日期格式转换错误，请输入yyyy-MM-dd格式的日期");
				}
			}
			if(CommonUtil.isNotNull(endStr)){
				try {
					Date end = ConstantsObj.FORMAT_DATE.parse(endStr);
					paramMap.put("endDate", end);
				} catch (ParseException e) {
					throw new ErrorMessageException("结束日期格式转换错误，请输入yyyy-MM-dd格式的日期");
				}
			}
			
			String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaBuilddate");
			String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
			if(sort.equals("rmstName")){
				sort = "resMetaStore.rmstName";
			}
			if(sort.equals("rltyName")){
				sort = "resLiterType.rltyName";
			}
			paramMap.put("sort", sort);
			paramMap.put("dir", dir);
			
			response.setContentType("application/x-msdownload; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=resource.xls");
			OutputStream outStream = response.getOutputStream();
			this.exportResourceService.exportToExcel(outStream, paramMap);
			outStream.flush();
			outStream.close();
			outStream = null;
		} catch (Exception e) {
			e.printStackTrace();
			response.reset();
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
			out.print("<span style='font-size:12px;'>" + e.getMessage() + "<br>");
			out.print("<br><a href='javascript:window.history.go(-1);'><<返回</a></span></span>");
			out.flush();
			out.close();
		}
	}
	
}
