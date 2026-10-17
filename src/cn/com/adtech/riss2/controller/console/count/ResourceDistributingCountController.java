package cn.com.adtech.riss2.controller.console.count;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.count.ISysCountService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ExcelUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;

import com.jbetter.framework.session.Pagination;
import com.jbetter.hibernate.IdGenerator;

@SuppressWarnings("unchecked")
public class ResourceDistributingCountController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ResourceDistributingCountController.class);

	private ISysCountService countService;

	public ISysCountService getCountService() {
		return countService;
	}

	public void setCountService(ISysCountService countService) {
		this.countService = countService;
	}

	/**
	 * 初始化资源分布统计页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-17
	 */
	public ModelAndView initResourceDistributingCountView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("count/resourceDistributingCount");
	}

	/**
	 * 获取资源分布列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-17
	 */
	public void getResourceDistributingCountList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaCount");
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		// 查询条件
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		if(endTime != null && endTime.trim().length() >= 10) {
			endTime = endTime.substring(0, 10);
		}else{
//			endTime = ConstantsObj.FORMAT_DATE.format(new Date());
		}
		
		String[] str = new String[] {"rmstTitle", "checking", "passcheck", "notpasscheck","rsdaCount","rmstId"};
		
		Object[] obj = this.countService.getResourceDistributingCountList(endTime, sort, dir,pagination,null);
		List list = (List)obj[0];
		if(list.size() > 0){
			Object[] temp = (Object[])list.get(0);
			if(temp.length > 6){
				str = new String[] {"rmstTitle", "checking", "passcheck", "notpasscheck","rsdaCount","rmstId","rowNum"};
			}
		}
		String result = CommonUtil.createJsonTable(list,str , pagination.getTotalResults()+"");
		out.write(result);
		out.flush();
	}
	
	/**
	 * 导出到EXCEL文件
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-17
	 */
	public ModelAndView exportToExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 查询条件
		String endTime = "";
		Long time = ServletRequestUtils.getLongParameter(request, "endTime");
		// 排序方式
		String defaltSort = ServletRequestUtils.getStringParameter(request, "defaltSort", "rsdaCount");
		String defaltDir = ServletRequestUtils.getStringParameter(request, "defaltDir", "DESC");
		
		if(time != null && time > 0) {
			endTime = ConstantsObj.FORMAT_DATE.format(new Date(time));
		}else{
//			endTime = ConstantsObj.FORMAT_DATE.format(new Date());
		}
		// 获取资源分布列表信息
		Object[] obj = this.countService.getResourceDistributingCountList(endTime, defaltSort, defaltDir,null,"1");
		List list = (List)obj[0];// 列表信息
		String count = obj[1].toString();// 总记录数
		if(list == null || list.size() < 1) {
			request.setAttribute("result", "<script>alert('该系统中暂时还没有资源分布，文件创建失败!');</script>");
			return new ModelAndView("count/resourceDistributingCount");
		}else {
			ExcelUtil excelUtil = new ExcelUtil();
			String msg = "";
			if(ServiceUtil.isNotNull(endTime)){
				msg = "截止"+endTime;
			}
			excelUtil.setSheetName(msg+"资源分布统计如下");
			
//			String fileName = "File"+IdGenerator.generateId();
			String fileName = "资源分布统计";
			// 处理不同浏览器对文件名的解析问题
			String agent = (String)request.getHeader("USER-AGENT");
			if(agent != null && agent.indexOf("MSIE") == -1) { // FF
				fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
				fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
			}else { // IE
				fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
			}
			response.setContentType("application/x-msdownload; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
			OutputStream outStream = null;
			try {
				String[] titles = new String[]{"特色库名称","审核中资源数（条）","审核通过资源数（条）","审核未通过资源数（条）","资源总数（条）"};
				outStream = response.getOutputStream();
				String lastContent = "资源总计：" + count + "条！";
				excelUtil.exportToExcel(outStream, list, titles, 0, 5, lastContent);
				
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
			return null;
		}
	}
	
}
