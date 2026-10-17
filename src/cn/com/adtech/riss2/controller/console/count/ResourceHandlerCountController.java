package cn.com.adtech.riss2.controller.console.count;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletRequest;
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
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;

import com.jbetter.framework.session.Pagination;
import com.jbetter.hibernate.IdGenerator;

@SuppressWarnings("unchecked")
public class ResourceHandlerCountController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ResourceHandlerCountController.class);

	private ISysCountService countService;

	public ISysCountService getCountService() {
		return countService;
	}

	public void setCountService(ISysCountService countService) {
		this.countService = countService;
	}

	/**
	 * 初始化资源工作量统计页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao 
	 * @date 2011-1-19
	 */
	public ModelAndView initResourceHandlerCountView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("count/resourceHandlerCount");
	}

	/**
	 * 获取资源工作量统计列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-19
	 */
	public void getResourceHandlerCountList(HttpServletRequest request, HttpServletResponse response,PageExtNative page) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		
		// 检索条件
		String endTime = ServletRequestUtils.getStringParameter(request, "endTime", "");
		String beginTime = ServletRequestUtils.getStringParameter(request, "beginTime", "");
		String suinLoginName = ServletRequestUtils.getStringParameter(request, "suinLoginName", "");
		String suinName = ServletRequestUtils.getStringParameter(request, "suinName", "");
		String type = ServletRequestUtils.getStringParameter(request, "type", "0");
		String rmstId = ServletRequestUtils.getStringParameter(request, "rmstId","0");
		
		Map map = new HashMap();
		map.put("endTime", endTime);
		map.put("beginTime",beginTime );
		map.put("suinLoginName", suinLoginName);
		map.put("suinName", suinName);
		map.put("type", type);
		map.put("rmstId", rmstId);
		page.setMSearch(map);
		
		List list = null;
		Wrapper wrap = null;
		wrap = this.countService.getResourcesHandlerCountList(page);
		
		String[] str = new String[] {"rsla_username", "rsla_authorname", "rsdaCount","rsla_isfirst_status"};
		list = (List)wrap.getResult();
		String jsonResult = CommonUtil.createJsonTable(list, str, wrap.getMessage());
		response.getWriter().write(jsonResult);
		response.getWriter().flush();				
	}
	
	/**
	 * 导出到EXCEL文件
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2011-1-17
	 */
	public ModelAndView exportToExcel(HttpServletRequest request, HttpServletResponse response,PageExtNative page) throws Exception {
		
		
		// 检索条件
		
		String beginTime = "";
		String endTime = "";
		Long time1 = ServletRequestUtils.getLongParameter(request, "beginTime");
		Long time2 = ServletRequestUtils.getLongParameter(request, "endTime");
		
		String suinLoginName = ServletRequestUtils.getStringParameter(request, "suinLoginName", "");
		String suinName = ServletRequestUtils.getStringParameter(request, "suinName", "");
		String type = ServletRequestUtils.getStringParameter(request, "type", "0");
		String rmstId = ServletRequestUtils.getStringParameter(request, "rmstId","0");
		
		if(time1 != null && time1 > 0) {
			beginTime = ConstantsObj.FORMAT_DATE.format(new Date(time1));
		}
		if(time2 != null && time2 > 0) {
			endTime = ConstantsObj.FORMAT_DATE.format(new Date(time2));
		}
		
		
		Map map = new HashMap();
		map.put("endTime", endTime);
		map.put("beginTime",beginTime );
		map.put("suinLoginName", suinLoginName);
		map.put("suinName", suinName);
		map.put("type", type);
		map.put("rmstId", rmstId);
		map.put("methodType", "1");
		page.setMSearch(map);
		
		Wrapper wrap = this.countService.getResourcesHandlerCountList(page);
		List list = (List)wrap.getResult();
		
		if(list == null || list.size() < 1) {
			request.setAttribute("result", "<script>alert('该系统中暂时还没有工作量统计，文件创建失败!');</script>");
			return new ModelAndView("count/resourceHandlerCount");
		}else {
			for(int i=0,j=list.size();i<j;i++){
				Object[] obj = (Object[])list.get(i);
				String tempType = obj[3].toString();
				String pre_type = tempType.substring(0,1);
				String after_type = tempType.substring(1, 2);
				if("1".equals(pre_type) && "1".equals(after_type)){
					obj[3] = "第一语加工";
				}else if("0".equals(pre_type) && "1".equals(after_type)){
					obj[3] = "其它语加工";
				}else if("1".equals(pre_type) && "2".equals(after_type)){
					obj[3] = "第一语审核";
				}else if("0".equals(pre_type) && "2".equals(after_type)){
					obj[3] = "其它语审核";
				}
			}
			
			
			
			ExcelUtil excelUtil = new ExcelUtil();
			String titleMsg = "";
			if(ServiceUtil.isNotNull(beginTime) && !ServiceUtil.isNotNull(endTime)){
				titleMsg = "从"+beginTime+"至今";
			}else if(!ServiceUtil.isNotNull(beginTime) && ServiceUtil.isNotNull(endTime)){
				titleMsg = "至"+endTime;
			}else if(ServiceUtil.isNotNull(beginTime) && ServiceUtil.isNotNull(endTime)){
				titleMsg = "从"+beginTime+"至"+endTime;
			}
			excelUtil.setSheetName(titleMsg+"工作量统计");
			
//			String fileName = "File"+IdGenerator.generateId();
			String fileName = titleMsg+"工作量统计";
			
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
				String[] str = new String[] {"rsla_username", "rsla_authorname", "rsdaCount","rsla_isfirst_status"};
				String[] titles = new String[]{"登录名","姓名","工作量（条）","工作类型"};
				outStream = response.getOutputStream();
				excelUtil.exportToExcel(outStream, list, titles, 0, 4, wrap.getMessage());
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
