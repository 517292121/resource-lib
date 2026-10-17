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

import com.adtech.export.config.TableConfig;
import com.jbetter.framework.session.Pagination;
import com.jbetter.hibernate.IdGenerator;

import cn.com.adtech.riss2.bso.count.ISysCountService;
import cn.com.adtech.riss2.bso.count.JavaBeanObject;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ExportDataToExcelUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.exception.ErrorMessageException;

/**
 * @copyright adtech
 * 类说明:会员登录统计
 * @author MaChao
 * @version 创建时间：2011-1-14
 */

public class UserLoginCountController extends MultiActionController{

	public ModelAndView initView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("count/userLoginCount");
	}
	
	
	/**
	 * 说明：得到会员登录统计数据
	 *@param
	 *@return
	 *@author MaChao
	 *@date 2011-1-14
	 */
	public void getLoginCountData(HttpServletRequest request,
			HttpServletResponse response,PageExtNative page) throws Exception {
		//查询条件
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
		map.put("userName", userName);
		map.put("beginD",beginD );
		map.put("endD", endD);
		map.put("dir", dir);
	
		page.setMSearch(map);
		List list = null;
		Wrapper wrap = null;
		
		wrap = sysCountService.getUserLoginCountList(page);
		String[] columns = new String[]{"userName","countNum"};
		list = (List)wrap.getResult();
		if(list != null && list.size() > 0){
			Object[] obj = (Object[])list.get(0);
			if(obj.length>2){
				//hibernate执行分页sql的时候会自动带上rownum
				columns = new String[]{"userName","countNum","rownum"};
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
		
		if(ServiceUtil.isNotNull(beginD) && ServiceUtil.isNotNull(endD)){
			wrap = sysCountService.getUserLoginCountList(page);		
		}
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
		String fileName = "会员登录数据统计" + ".xls";		
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
			String title = "会员名称,登录次数";       	
			String code = "property1,property2";
			String top = "会员登录数据统计";
			String footer = ""; //excel的尾巴，可以不用
			TableConfig tableConfig = ExportDataToExcelUtil.buildTableConfigForExcelUseState(title, code, top, footer);
			ExportDataToExcelUtil.writeExcel(li, tableConfig, response);
		} catch (IOException e) {
			throw new ErrorMessageException("会员登录数据统计导出失败！");
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

