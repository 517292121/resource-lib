package cn.com.adtech.riss2.controller.console.count;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.adtech.export.config.TableConfig;
import com.jbetter.hibernate.IdGenerator;

import cn.com.adtech.riss2.bso.count.ISysCountService;
import cn.com.adtech.riss2.bso.count.JavaBeanObject;
import cn.com.adtech.riss2.bso.resource.ISourceTypeService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ExportDataToExcelUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

/**
 * @copyright adtech
 * 类说明:资源使用情况统计
 * @author MaChao
 * @version 创建时间：2011-1-13
 */

public class ResourcesUsedCountController extends MultiActionController{

	public ModelAndView initView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("count/resourcesUsedCount");
	}
	
	/**
	 * 说明：得到资源使用情况数据
	 *@param
	 *@return
	 *@author MaChao
	 *@date 2011-1-13
	 */
	public void getResourcesUsedDataList(HttpServletRequest request,
			HttpServletResponse response,PageExtNative page) throws Exception {
		//查询条件
		String resourcesName = request.getParameter("resourcesName");
		String beginD = request.getParameter("beginD");
		String endD = request.getParameter("endD");
		String minHits = request.getParameter("minHits");
		String maxHits = request.getParameter("maxHits");
//		if( !ServiceUtil.isNotNull(endD)){
//			endD = ConstantsObj.FORMAT_DATE.format(new Date());
//		}	
		if(ServiceUtil.isNotNull(minHits) && checkChinese(minHits)){
			minHits = "";
		}
		if(ServiceUtil.isNotNull(maxHits) && checkChinese(maxHits) ){
			maxHits = "";
		}			
		//排序
		String dir = request.getParameter("dir");
		Map param = new HashMap();
		//分页
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		//排序
		String sort = request.getParameter("sort");		
		param.put("resourcesName", resourcesName);
		param.put("beginD", beginD);
		param.put("endD", endD);
		param.put("minHits", minHits);
		param.put("maxHits", maxHits);
		param.put("dir", dir);
		page.setMSearch(param);		
		List list = null;
		Wrapper wrap = null;
		wrap = sysCountService.getResourcesUsedCountList(page);
		String[] columns = new String[]{"resourcesName","hitsCount"};
		list = (List)wrap.getResult();
		if(list != null && list.size() > 0){
			Object[] obj = (Object[])list.get(0);
			if(obj.length>2){
				//hibernate执行分页sql的时候会自动带上rownum
				columns = new String[]{"resourcesName","hitsCount","rownum"};
			}				
		}
		String jsonResult = CommonUtil.createJsonTable(list, columns, wrap.getMessage());
		response.getWriter().write(jsonResult);
		response.getWriter().flush();					
	}
	
	/**
	 * 说明：导出数据到excel表格
	 *@param
	 *@return
	 *@author MaChao
	 *@date 2011-1-17
	 */
	public void exportLoginDataToExcel(HttpServletRequest request,
			HttpServletResponse response,PageExtNative page) throws Exception {
		//查询条件
		//查询条件
		String resourcesName = request.getParameter("resourcesName");
		String beginD = request.getParameter("beginD");
		String endD = request.getParameter("endD");
		String minHits = request.getParameter("minHits");
		String maxHits = request.getParameter("maxHits");
		if( !ServiceUtil.isNotNull(endD)){
			endD = ConstantsObj.FORMAT_DATE.format(new Date());
		}		
		if(ServiceUtil.isNotNull(minHits) && checkChinese(minHits)){
			minHits = "";
		}
		if(ServiceUtil.isNotNull(maxHits) && checkChinese(maxHits) ){
			maxHits = "";
		}			
		//排序
		String dir = "DESC";
		Map map = new HashMap();
		map.put("resourcesName", resourcesName);
		map.put("beginD",beginD );
		map.put("endD", endD);
		map.put("minHits", minHits);
		map.put("maxHits", maxHits);
		map.put("dir", dir);
		//不分页标识
		page.setLimit(0);
		page.setMSearch(map);
		//设置不分页
		page.setLimit(0);
		List list = null;
		Wrapper wrap = null;		
		List<JavaBeanObject> li = new ArrayList<JavaBeanObject>();
		
		wrap = sysCountService.getResourcesUsedCountList(page);		
		list = (List)wrap.getResult();
		if(list != null){
			for(int i=  0;i<list.size();i++){
				JavaBeanObject javabean = new JavaBeanObject();
				Object[] obj = (Object[])list.get(i);
				javabean.setProperty1(obj[0]);
				javabean.setProperty2(obj[1]);
				li.add(javabean);
				javabean = null;
				obj = null;
			}
		}	
		
		// 如果是下载文件，则设置下载名
		String fileName = "资源使用情况统计" + ".xls";		
		// 处理不同浏览器对文件名的解析问题
		String agent = (String)request.getHeader("USER-AGENT");
		if(agent != null && agent.indexOf("MSIE") == -1) { // FF
			fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
			fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
		}else { // IE
			fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
		}
		response.setContentType("application/x-msdownload; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName );
		try {
			String title = "资源名称,点击次数";       	
			String code = "property1,property2";
			String top = "资源使用情况统计";
			String footer = ""; //excel的尾巴，可以不用
			TableConfig tableConfig = ExportDataToExcelUtil.buildTableConfigForExcelUseState(title, code, top, footer);
			ExportDataToExcelUtil.writeExcel(li, tableConfig, response);
		} catch (IOException e) {
			throw new ErrorMessageException("资源使用情况数据导出失败！");
		}
	}
	
	
	//得到资源类型
	public ModelAndView getSourceType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		List<ResLiterType> list = sourcesTypeService.getAllResTypes();
		StringBuilder result = new StringBuilder();
		//result.append("[");
		result.append("[ ['','全部'],");
		int index = 0;
		for(ResLiterType rtype : list) {
			++index ;
			result.append("['").append(rtype.getRltyId()).append("','").append(rtype.getRltyName()).append("']");
			if(index != list.size()){
				result.append(",");
			}
		}
		result.append("]");
		PrintWriter out = response.getWriter();
		out.print(result);
		return null;
	}
	
	/**
	 * 说明：判断字符串中是否有中文，有中文返回true
	 *@param
	 *@return boolean
	 *@author MaChao
	 *@date 2011-1-27
	 */
	public  boolean checkChinese(String s){
		char[] word = s.toCharArray();
		for(int i = 0;i<word.length;i++){
			if ((word[i] >= 0x4e00)&&(word[i]<=0x9fbb)){
				return true;
			} 
		}
		return false;
	}

	private ISysCountService sysCountService;
	private ISourceTypeService sourcesTypeService;


	public ISourceTypeService getSourcesTypeService() {
		return sourcesTypeService;
	}

	public void setSourcesTypeService(ISourceTypeService sourcesTypeService) {
		this.sourcesTypeService = sourcesTypeService;
	}

	public ISysCountService getSysCountService() {
		return sysCountService;
	}
	public void setSysCountService(ISysCountService sysCountService) {
		this.sysCountService = sysCountService;
	}
	
}

