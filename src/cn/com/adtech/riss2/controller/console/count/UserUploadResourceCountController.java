package cn.com.adtech.riss2.controller.console.count;

import java.io.IOException;
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

import cn.com.adtech.riss2.bso.count.ISysCountService;
import cn.com.adtech.riss2.bso.count.JavaBeanObject;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ExportDataToExcelUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.adtech.export.config.TableConfig;
import com.jbetter.hibernate.IdGenerator;

/**
 * @copyright adtech
 * 类说明:会员上传资源统计
 * @author MaChao
 * @version 创建时间：2011-1-12
 */

public class UserUploadResourceCountController extends MultiActionController{
	
	

	public ModelAndView initView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("count/uploadResourcesCount");
	}
	public void getUploadDataList(HttpServletRequest request,
			HttpServletResponse response,PageExtNative page) throws Exception {
		//查询条件
		String userName = request.getParameter("userName");
		String realName = request.getParameter("realName");
		String beginD = request.getParameter("beginD");
		String endD = request.getParameter("endD");
		Map param = new HashMap();
		//分页
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		//排序
		String dir = request.getParameter("dir");
		String sort = request.getParameter("sort");		
//		if( !ServiceUtil.isNotNull(endD)){
//			endD = ConstantsObj.FORMAT_DATE.format(new Date());
//		}
		param.put("userName", userName);
		param.put("realName", realName);
		param.put("beginD", beginD);
		param.put("endD", endD);
		param.put("dir", dir);
		param.put("sort", sort);
		page.setMSearch(param);
		List list = null;
		Wrapper wrap = null;
		
		wrap = sysCountService.getUploadResourcesCountList(page);
		String[] columns = new String[]{"userName","realName","countNum"};
		list = (List)wrap.getResult();
		if(list != null && list.size() > 0){
			Object[] obj = (Object[])list.get(0);
			if(obj.length>3){
				//hibernate执行分页sql的时候会自动带上rownum
				columns = new String[]{"userName","realName","countNum","rownum"};
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
	 *@date 2011-1-13
	 */
	public void exportDataToExcel(HttpServletRequest request,
			HttpServletResponse response,PageExtNative page) throws Exception {
		//查询条件
		String userName = request.getParameter("userName");
		String realName = request.getParameter("realName");
		String beginD = request.getParameter("beginD");
		String endD = request.getParameter("endD");
		if( !ServiceUtil.isNotNull(endD)){
			endD = ConstantsObj.FORMAT_DATE.format(new Date());
		}		
		Map param = new HashMap();
		param.put("userName", userName);
		param.put("realName", realName);
		param.put("beginD", beginD);
		param.put("endD", endD);
		page.setMSearch(param);
		List list = null;
		Wrapper wrap = null;		
		//默认排序为数量倒序
		String dir = "DESC";
		String sort = "countNum";
		param.put("userName", userName);
		param.put("realName", realName);
		param.put("beginD", beginD);
		param.put("endD", endD);
		param.put("dir", dir);
		param.put("sort", sort);
		
		// 如果是下载文件，则设置下载名
		String fileName = "会员上传资源数量统计" + ".xls";		
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
		
		if(ServiceUtil.isNotNull(beginD) && ServiceUtil.isNotNull(endD)){
			wrap = sysCountService.getUploadResourcesCountList(page);	
			list = (List)wrap.getResult();
		}
		List<JavaBeanObject> li = new ArrayList<JavaBeanObject>();
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
		try {
			String title = "登录名,姓名,上传数量";       	
			String code = "property1,property2,property3";
			String top = "会员上传资源数量统计";
			String footer = ""; //excel的尾巴，可以不用
			TableConfig tableConfig = ExportDataToExcelUtil.buildTableConfigForExcelUseState(title, code, top, footer);
			ExportDataToExcelUtil.writeExcel(li, tableConfig, response);
		} catch (IOException e) {
			throw new ErrorMessageException("会员上传资源统计导出数据失败！");
		}
	}
	
	private ISysCountService sysCountService;


	public ISysCountService getSysCountService() {
		return sysCountService;
	}
	public void setSysCountService(ISysCountService sysCountService) {
		this.sysCountService = sysCountService;
	}
	
}

