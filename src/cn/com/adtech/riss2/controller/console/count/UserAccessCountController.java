package cn.com.adtech.riss2.controller.console.count;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.com.adtech.riss2.bso.count.ISysCountService;
import cn.com.adtech.riss2.bso.count.JavaBeanObject;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ExcelUtil;
import cn.com.adtech.riss2.core.ExportDataToExcelUtil;
import cn.com.adtech.riss2.core.FileNameParsingUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.adtech.export.config.TableConfig;
import com.jbetter.framework.session.Pagination;


/**
 * 用户操作统计
 * 登录 检索 阅读 下载 访问
 * 
 * @description 
 *
 * @author MaChao
 *
 * @date Nov 30, 2015 - 1:00:32 PM
 */
public class UserAccessCountController extends MultiActionController{
	
	private ISysCountService sysCountService;
	public void setSysCountService(ISysCountService sysCountService) {
		this.sysCountService = sysCountService;
	}	
	
	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return new ModelAndView("count/useraccessCount");
	}

	/**
	 * 初始化
	 *
	 * @author MaChao
	 * @date Nov 30, 2015
	 * @time 1:00:57 PM
	 */
	public void getUserCountData(HttpServletRequest request,
			HttpServletResponse response,PageExtNative page) throws Exception {
		//查询条件
		String countType = request.getParameter("countType");
		if(CommonUtil.isNotNull(countType) && countType.equals("全部")){
			countType =null;
		}
		String userName = request.getParameter("userName");
		String beginD = request.getParameter("beginD");
		if(ServiceUtil.isNotNull(beginD)){
			beginD += ":00:00:00";
		}
		String endD = request.getParameter("endD");
		if( !ServiceUtil.isNotNull(endD)){
			endD = ConstantsObj.FORMAT_DATE.format(new Date());
		}			
		endD += ":23:59:59";
		//排序
		String dir = request.getParameter("dir");
		Map map = new HashMap();
		map.put("countType", countType);
		map.put("userName", userName);
		map.put("beginD",beginD );
		map.put("endD", endD);
		map.put("dir", dir);
	
		page.setMSearch(map);
		List list = null;
		Wrapper wrap = null;
		
		wrap = sysCountService.getUserAccessCountList(page);
		String[] columns = new String[]{"userName","countType","countNum"};
		list = (List)wrap.getResult();
		if(list != null && list.size() > 0){
			Object[] obj = (Object[])list.get(0);
			if(obj.length>4){
				//hibernate执行分页sql的时候会自动带上rownum
				columns = new String[]{"userName","countType","countNum","rownum"};
			}				
		}
		String jsonResult = CommonUtil.createJsonTable(list, columns, wrap.getMessage());
		response.getWriter().write(jsonResult);
		response.getWriter().flush();				
	}
	
	/**
	 * 导出excel表
	 *
	 * @author MaChao
	 * @date Nov 30, 2015
	 * @time 1:01:08 PM
	 */
	public void exportUserAccessDataToExcel(HttpServletRequest request,
			HttpServletResponse response,PageExtNative page) throws Exception {
		//查询条件
		String countType = request.getParameter("countType");
		if(CommonUtil.isNotNull(countType) && countType.equals("全部")){
			countType =null;
		} 
		String userName = request.getParameter("userName");
		String beginD = request.getParameter("beginD");
		if(ServiceUtil.isNotNull(beginD)){
			beginD += ":00:00:00";
		}
		String endD = request.getParameter("endD");
		if( !ServiceUtil.isNotNull(endD)){
			endD = ConstantsObj.FORMAT_DATE.format(new Date());
		}	
		endD += ":23:59:59";
		//排序
		String dir = "DESC";
		Map map = new HashMap();
		map.put("countType", countType);
		map.put("userName", userName);
		map.put("beginD",beginD );
		map.put("endD", endD);
		map.put("dir", dir);
		//不分页标识
		page.setLimit(0);
		page.setMSearch(map);
		//设置不分页
		page.setLimit(0);
		List list = null;
		Wrapper wrap = null;		
		List<JavaBeanObject> li = new ArrayList<JavaBeanObject>();
		
		wrap = sysCountService.getUserAccessCountList(page);		
		list = (List)wrap.getResult();
		if(list != null){
			for(int i=  0;i<list.size();i++){
				JavaBeanObject javabean = new JavaBeanObject();
				Object[] obj = (Object[])list.get(i);
				javabean.setProperty1(obj[0]);
				javabean.setProperty2(obj[1]);
				javabean.setProperty3(obj[2]);
				li.add(javabean);
				javabean = null;
				obj = null;
			}
		}	
		
		// 如果是下载文件，则设置下载名
		String fileName = "用户操作统计";		
		//处理不同浏览器对文件名的解析问题
		response.setHeader("Cache-Control", "private");  
		response.setHeader("Pragma", "private");  
		response.setContentType("application/vnd.ms-excel;charset=utf-8");   
		response.setHeader("Content-Type", "application/force-download"); 
		fileName = FileNameParsingUtil.processFileName(request, fileName);
		response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
		
		try {
			String title = "用户名称,操作类型,操作次数";       	
			String code = "property1,property2,property3";
			String top = "用户操作记录统计";
			String footer =  "共：" + list.size() + "条记录！"; //excel的尾巴，可以不用
			TableConfig tableConfig = ExportDataToExcelUtil.buildTableConfigForExcelUseState(title, code, top, footer);
			ExportDataToExcelUtil.writeExcel(li, tableConfig, response);
		} catch (IOException e) {
			throw new ErrorMessageException("用户操作记录统计导出失败！");
		}
	}
	
	/**
	 * 查看日志列表-指定用户&类型
	 *
	 * @author MaChao
	 * @date Nov 30, 2015
	 * @time 3:01:01 PM
	 */
	public ModelAndView initLogView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("/count/useraccessLogList");
	}
	
	/**
	 * 获取列表
	 *
	 * @author MaChao
	 * @date Nov 30, 2015
	 * @time 3:06:03 PM
	 */
	public void logList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//查询条件
		String userName = ServletRequestUtils.getStringParameter(request, "userName");
		String type = ServletRequestUtils.getStringParameter(request, "type");
		String beginD = request.getParameter("beginD");
		if(ServiceUtil.isNotNull(beginD)){
			beginD += ":00:00:00";
		} 
		String endD = request.getParameter("endD");
		if(!ServiceUtil.isNotNull(endD)){
			endD = ConstantsObj.FORMAT_DATE.format(new Date());
		}	
		endD += ":23:59:59";
		
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);// 设置当前页
		pagination.setPageResults(limit);// 设置每页记录数
		
		//排序相关
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "sacoDate");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
		
		String jsondata = sysCountService.getLoglistJson(pagination,userName,type,beginD,endD,sort, dir);

		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 导出日志记录为Excel
	 */
	public void exportLogtoExl(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//查询条件
		String userName = ServletRequestUtils.getStringParameter(request, "userName");
		String type = ServletRequestUtils.getStringParameter(request, "type");
		if(CommonUtil.isNotNull(type) && type.equals("全部")){
			type =null;
		} 
		String beginD = request.getParameter("beginD");
		if(ServiceUtil.isNotNull(beginD)){
			beginD += ":00:00:00";
		}
		String endD = request.getParameter("endD");
		if(!ServiceUtil.isNotNull(endD)){
			endD = ConstantsObj.FORMAT_DATE.format(new Date());
		}	
		endD += ":23:59:59";
		
		//分页参数
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		
		//分页器
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(currentPage);// 设置当前页
		pagination.setPageResults(limit);// 设置每页记录数
		
		//排序相关
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "sacoDate");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
		
		//日志参数-导出所有
		String log = ServletRequestUtils.getStringParameter(request, "log", "");
		if(CommonUtil.isNotNull(log)){
			pagination = null;
		}
		
		List list = sysCountService.getLoglistObj(pagination,userName,type,beginD,endD,sort,dir);
		
		//导出参数
		String fileName = "用户日志记录";
		ExcelUtil excelUtil = new ExcelUtil();
		excelUtil.setSheetName(fileName);
		
		//处理不同浏览器对文件名的解析问题
		response.setHeader("Cache-Control", "private");  
		response.setHeader("Pragma", "private");  
		response.setContentType("application/vnd.ms-excel;charset=utf-8");   
		response.setHeader("Content-Type", "application/force-download"); 
		fileName = FileNameParsingUtil.processFileName(request, fileName);
		response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
		
		OutputStream outStream = null;
		try {
			String[] titles = new String[]{"用户名称","操作类型","操作时间","操作内容","操作IP"};
			outStream = response.getOutputStream();
			String lastContent = "共：" + list.size() + "条记录！";
			excelUtil.exportListToExcel(outStream, list, titles,lastContent); 
			outStream.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(outStream != null) {
				try {
					outStream.close();
				}catch(IOException e) { 
					e.printStackTrace();
				}
				outStream = null;
			}
		}
	}
	
	
}

