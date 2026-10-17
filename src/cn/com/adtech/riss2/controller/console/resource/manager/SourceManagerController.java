package cn.com.adtech.riss2.controller.console.resource.manager;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
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

import cn.com.adtech.riss2.bso.cache.IPlatformCacheService;
import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.bso.resource.IResourceLuceneService;
import cn.com.adtech.riss2.bso.resource.ResourceLuceneServiceImp;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ExcelUtil;
import cn.com.adtech.riss2.core.PageExtNative;

public class SourceManagerController extends MultiActionController {

	private IResourceDataService resourceService = null;
	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}

	private IPlatformCacheService cacheService;
	public void setCacheService(IPlatformCacheService cacheService) {
		this.cacheService = cacheService;
	}
	
	/**
	 * 进入资源维护资源列表
	 */
	public ModelAndView managerResourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int pagecount = ServletRequestUtils.getIntParameter(request, "pagecount", 0);
		String searchtitle =  ServletRequestUtils.getStringParameter(request, "searchtitle","");
	    String searchtitleshow = ServletRequestUtils.getStringParameter(request, "searchtitleshow","");
	    Long searchRmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
	    Long searchRltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
	    
	    if (searchtitleshow!=null && !searchtitleshow.equals("")) {
	    	searchtitle = searchtitleshow;
		}
		request.setAttribute("pagecount", pagecount);
		request.setAttribute("searchtitle", searchtitle);
		request.setAttribute("searchRmstId", searchRmstId);
		request.setAttribute("searchRltyId", searchRltyId);
		return new ModelAndView("resource/manager/managerResourceList");
	}

	/**
	 * 资源列表JSON数据
	 */
	public ModelAndView resourceListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long storeId = MyRequestUtil.getLongParameter(request, "RSDA_RMST_ID");
		long sourceType = MyRequestUtil.getLongParameter(request, "RSDA_RLTY_ID");
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		String rsdaTitle = ServletRequestUtils.getStringParameter(request, "rsdaTitle", "");
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "RSDA_BUILDDATE");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		String jsondata = resourceService.getAllResourceListJson(storeId, sourceType, begin, pageNum, sort, dir, rsdaTitle);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
		return null;
	}

	/**
	 * 设置资源评论状态
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void changCommentStatu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sourceIdlist = MyRequestUtil.getStringParameter(request, "idlist");
		boolean b = resourceService.updateCommentStatus(sourceIdlist);
		String jsondata = "";
		if(b) {
			jsondata = "1";
		}else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}

	/**
	 * 设置资源使用状态
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void changUseStatu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sourceIdlist = MyRequestUtil.getStringParameter(request, "idlist");
		// int statu = MyRequestUtil.getIntParameter(request, "statu");
		boolean b = resourceService.updateUseStatus(sourceIdlist);
		String jsondata = "";
		if(b) {
			//资源删除后重新计算资源统计
			this.cacheService.updateResCount();
			this.cacheService.updateResforType(); //有可能删除了推荐资源，所以同时更新推荐资源缓存
			
			jsondata = "1";
		}else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}

	/**
	 * 设置资源使用状态
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void changRcmdStatu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sourceIdlist = MyRequestUtil.getStringParameter(request, "idlist");
		boolean b = resourceService.updateRcmdStatus(sourceIdlist);
		String jsondata = "";
		if(b) {
			//更新首页推荐资源缓存
			this.cacheService.updateResforType();
			
			jsondata = "1";
		}else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}

	/**
	 * 删除资源
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void deleteResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String sourceIdlist = MyRequestUtil.getStringParameter(request, "idlist");
		String[] arr = sourceIdlist.split(",");
		long sid;
		boolean b = true;
		
		//此处调用的是里面的删除索引方法，没有操作数据库，因此可以直接通过new创建
		IResourceLuceneService luceneService = new ResourceLuceneServiceImp();
		for(int i = 0; i < arr.length; i++) {
			sid = Long.parseLong(arr[i]);
			if(sid > 0) {
				if(resourceService.deleteResource(sid, request) == false) {
					b = false;
				}else{
					luceneService.deleteIndexById(sid);
					resourceService.deleteComt(sid);//删除资源相关评论
				}
			}
		}
		String jsondata = "";
		if(b) {
			//资源删除后重新计算资源统计
			this.cacheService.updateResCount();
			this.cacheService.updateResforType(); //有可能删除了推荐资源，所以同时更新推荐资源缓存
			
			jsondata = "1";
		}else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}

	/**
	 * 资源列表JSON数据
	 */
	public ModelAndView resourceLanguageList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long sourceId = ServletRequestUtils.getLongParameter(request, "sourceId", 0);
		request.setAttribute("sourceid", sourceId);
		return new ModelAndView("resource/manager/sourceLanguageList");
	}
	
	
	/**
	 * 导出元数据信息
	 *
	 * @author MaChao
	 * @date 2013-12-5
	 *
	 * @param request
	 * @param response
	 * @param page
	 * @throws Exception
	 */
	public void exportResource(HttpServletRequest request,
			HttpServletResponse response,PageExtNative page) throws Exception {
		//查询条件
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
		String rsdaTitle = ServletRequestUtils.getStringParameter(request, "rsdaTitle", "");
		String selectIds = ServletRequestUtils.getStringParameter(request, "ids", "");
		
		//排序
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "");
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		if(sort.equals("RMST_NAME")){
			sort = "resMetaStore.rmstName";
		}
		if(sort.equals("RLTY_NAME")){
			sort = "resLiterType.rltyName";
		}
		if(sort.equals("RSDA_TITLE")){
			sort = "rsdaTitle";
		}
		if(sort.equals("RSDA_BUILDDATE")){
			sort = "rsdaBuilddate";
		}
		if(sort.equals("RSDA_CMTSTUS")){
			sort = "rsdaCmtstus";
		}
		if(sort.equals("RSDA_GRADE")){
			sort = "rsdaGrade";
		}
		if(sort.equals("RSDA_RCMD")){
			sort = "rsdaRcmd";
		}
		if(sort.equals("RSDA_ISUSE")){
			sort = "rsdaIsuse";
		}
		if(sort.equals("RSDA_LOOKNUM")){
			sort = "rsdaLooknum";
		}
		
		//封装参数
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("sort", sort);
		params.put("dir", dir);
		params.put("selectIds", selectIds);
		if(rmstId > 0){
			params.put("rmstId", rmstId);
		}
		if(rltyId > 0){
			params.put("rltyId", rltyId);
		}
		if(CommonUtil.isNotNull(rsdaTitle)){
			params.put("rsdaTitle", rsdaTitle);
		}
		
		List<Object[]> list = this.resourceService.getExportResource(params);
		if(list == null){
			list = new LinkedList<Object[]>();
		}
		
		//导出数据部分开始
		
		ExcelUtil excelUtil = new ExcelUtil();

		String fileName = "资源元数据信息";
		excelUtil.setSheetName(fileName);

		String footerInfo = "共有：" + list.size() + " 条记录！";
		
		// 处理不同浏览器对文件名的解析问题
		String agent = (String) request.getHeader("USER-AGENT");
		if (agent != null && agent.indexOf("MSIE") == -1) { // FF
			fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
			fileName = "=?UTF-8?B?"
					+ (new String(Base64.encodeBase64(fileName
							.getBytes("UTF-8")))) + "?=";
		} else { // IE
			fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		}
		response.setContentType("application/x-msdownload; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName + ".xls");
		OutputStream outStream = null;
		try {
			String[] titles = new String[] { "特色库", "资源类型", "题名", "主题", "描述",
					"来源", "语种", "关联", "时空范围", "责任者", "出版者", "其它责任者", "权限",
					"日期", "类型", "格式", "标识符" };
			outStream = response.getOutputStream();
			excelUtil.exportToExcel(outStream, list, titles, 0, 17,	footerInfo);
			outStream.flush();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				outStream = null;
			}
		}
	}

}
