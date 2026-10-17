package cn.com.adtech.riss2.controller.console.count;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.count.ISysCountService;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ExcelUtil;

public class ResourceAddInfoController extends MultiActionController{

	private ISysCountService countService;
	public void setCountService(ISysCountService countService) {
		this.countService = countService;
	}
	
	public ModelAndView initPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("count/resourceAddInfo");
	}
	
	public ModelAndView initPageByPhoto(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("count/resourceAddInfoByPhoto");
	}
	
	/**
	 * 页面列表JSON数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getJosnData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "year"); // 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L); //特色库ID
		String beginYear = ServletRequestUtils.getStringParameter(request, "beginYear", ""); //起始年
		String endYear = ServletRequestUtils.getStringParameter(request, "endYear", ""); //截止年
		
		String result = countService.getResourceAddInfoJson(rmstId, beginYear, endYear, start, limit, dir, sort);
		out.write(result);
		out.flush();
	}
	
	
	
	/**
	 * 页面列表JSON数据
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getXmlData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/xml;charset=gb2312");
		PrintWriter out = response.getWriter();
		
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L); //特色库ID
	    
		Date date = new Date();
		int year = date.getYear() + 1900;
		String beginYear = ServletRequestUtils.getStringParameter(request, "beginYear", ""); //起始年
		String endYear = ServletRequestUtils.getStringParameter(request, "endYear", ""); //截止年
		if(beginYear == null || beginYear.length() < 1){
			beginYear = "2010";
		}
		if(endYear == null || endYear.length() < 1){
			endYear = year + "";
		}
		
		List<String[]> datalist = countService.getResourceAddInfoList(rmstId, beginYear, endYear);
		String result = parseXmlData(Integer.parseInt(beginYear), Integer.parseInt(endYear), datalist);
		System.out.println(result);
		out.write(result);
		out.flush();
	}
	
	/**
	 * 导出到Excel
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView exportToExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L); //特色库ID
		String beginYear = ServletRequestUtils.getStringParameter(request, "beginYear", ""); //起始年
		String endYear = ServletRequestUtils.getStringParameter(request, "endYear", ""); //截止年
		
		List<String[]> datalist = countService.getResourceAddInfoList(rmstId, beginYear, endYear);
		
		if(datalist == null || datalist.size() < 1) {
			request.setAttribute("result", "<script>alert('对不起，未找到相关数据!');</script>");
			return new ModelAndView("count/resourceDistributingCount");
		}else {
			ExcelUtil excelUtil = new ExcelUtil();
			excelUtil.setSheetName("资源增长情况统计");
			String fileName = "资源增长情况统计";
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
				String[] titles = new String[]{"特色库名称","年份","资源数量（条）"};
				outStream = response.getOutputStream();
				String lastContent = "共：" + datalist.size() + "条记录！";
				List<Object[]> list = parseObject(datalist);
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
			return null;
		}
	}
	
	public List<Object[]> parseObject(List<String[]> datalist){
		 Object[] o = null;
		 List<Object[]> list = new ArrayList<Object[]>();
		 for(int i=0,s=datalist.size(); i<s; i++){
			 o = datalist.get(i);
			 list.add(o);
		 }
		 return list;
	}
	
	public String parseXmlData(int beginYear, int endYear, List<String[]> list){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		buffer.append("<chart caption='资源增长情况统计' subcaption='(从 "+beginYear+" 至 "+endYear+")' lineThickness='1' showValues='0' formatNumberScale='0' anchorRadius='2'   divLineAlpha='20' divLineColor='CC3300' divLineIsDashed='1' showAlternateHGridColor='1' alternateHGridAlpha='5' alternateHGridColor='CC3300' shadowAlpha='40' labelStep='2' numvdivlines='5' chartRightMargin='35' bgColor='FFFFFF,CC3300' bgAngle='270' bgAlpha='10,10'>");
		buffer.append("<categories >");
		for(int i=beginYear; i<=endYear; i++){
			buffer.append("<category label='"+i+"' />");
		} 
		buffer.append("</categories>");
		Map<String, List<String>> map = parseListData(beginYear, endYear, list);
		if(map != null && map.size()>0){
			List<String> datalist = null;
			String rmstName;
			Set<String> keys = map.keySet();
			Iterator<String> iter = keys.iterator();
			String color;
			while(iter.hasNext()){
				rmstName = iter.next();
				datalist = map.get(rmstName);
				color = 0xffffff*Math.random() + "";
				buffer.append("<dataset seriesName='"+rmstName+"' color='"+color+"' anchorBorderColor='"+color+"' anchorBgColor='"+color+"'>");
				for(int i=0,s=datalist.size(); i<s; i++){
					buffer.append("<set value='"+datalist.get(i)+"' />");
				}
				buffer.append("</dataset>");
			}
		}
		buffer.append("</chart>");
		map = null;
		String xmldata = buffer.toString();
		buffer = null;
		return xmldata;
	}
	
	public Map<String, List<String>> parseListData(int beginYear, int endYear, List<String[]> list){
		if(null == list || list.size() < 1){
			return null;
		}
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		
		List<String> data = null;
		String[] arr = null;
		String[] arrTemp = null;
		String dataValue = "";
		String rmstName = "";
		for(int i=0,s=list.size(); i<s; i++){
			arr = list.get(i);
			rmstName = arr[0].trim();
			if(map.containsKey(rmstName)){
				continue;
			}
			data = new ArrayList<String>();
			for(int y=beginYear; y<=endYear; y++){
				boolean isGet = false;
				for(int j=0; j<s; j++){
					arrTemp = list.get(j);
					if(arrTemp[1].trim().equals(y+"") && rmstName.equals(arrTemp[0].trim())){
						dataValue = arrTemp[2].trim();
						isGet = true;
						break;
					}
				}
				if(isGet == false){
					dataValue = "0";
				}
				data.add(dataValue);
			}
			map.put(arr[0].trim(), data);
		}
		return map;
	}
	
	public static void main(String[] a){
		System.out.println(0xffffff*Math.random() );
	}
}
