package cn.com.adtech.riss2.controller.platform.wap;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.platform.IResourceSearchService;
import cn.com.adtech.riss2.controller.platform.ResCommonController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.lucene.dao.ILuceneSearchService;

/**
 * 
 * 前端数据检索
 *
 * @author MaChao
 *
 * @date：Jan 25, 2018
 */
@Controller
public class WapDataSearchController {
	
	@Resource(name="luceneSearchService")
	private ILuceneSearchService luceneSearchService;
	
	@Resource(name="resourceSearchService")
	private IResourceSearchService resourceSearchService;

	//进入lucene全文检索
	@RequestMapping(value="/wap/ls/{key}")
	public String luceneSearch(@PathVariable String key, Model model, HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		int match = ServletRequestUtils.getIntParameter(request, "match", 0);
		
		map.put("key", key);
		map.put("match", match);
		model.addAllAttributes(map);
		saveSearchKey(key);
		return "platform/wap/luceneSearchList";
	}
	
	//lucene全文检索
	@RequestMapping(value="/wap/luceneSearch")
	public void luceneSearchJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> params = paramsParser(request);
		
		//分页设置
		int currentPage = ServletRequestUtils.getIntParameter(request, "pn", 1);
		int size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		PageBean page = new PageBean();
		page.setCurrentPage(currentPage);
		page.setPageSize(size);
		
		try {
			List<Object[]> dataList = this.luceneSearchService.luceneSearch(page, params);
			
			map.put("success", true);
			map.put("currentPage", page.getCurrentPage());
			map.put("totalPage", page.getCountPage());
			map.put("totalLine", page.getCountRows());
			map.put("pageSize", size);
			map.put("dataList", dataList);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
//			System.out.println("JSON:" + json);
			out.flush();
			out.close();
		}
	}
	
	//lucene检索结果聚类统计
	@RequestMapping(value="/wap/luceneGroup")
	public void luceneGroupJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> params = paramsParser(request);
		
		try {
			Map<String, List<Object[]>> groupMap = this.luceneSearchService.groupSearch(params);
			
			map.put("success", true);
			map.put("groupMap", groupMap);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
//			System.out.println("JSON:" + json);
			out.flush();
			out.close();
		}
	}
	
	/**
	 * 解析查询参数
	 * @param request
	 * @return
	 */
	private Map<String, String> paramsParser(HttpServletRequest request){
		Map<String, String> params = new HashMap<String, String>();
		//获取参数
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		String dataType = ServletRequestUtils.getStringParameter(request, "dataType", "");
		String resLibrary = ServletRequestUtils.getStringParameter(request, "resLib", "");
		String resType = ServletRequestUtils.getStringParameter(request, "resType", "");
		String subjectClass = ServletRequestUtils.getStringParameter(request, "subjectClass", "");
		String creator = ServletRequestUtils.getStringParameter(request, "creator", "");
		String publisher = ServletRequestUtils.getStringParameter(request, "publisher", "");
		String keyword = ServletRequestUtils.getStringParameter(request, "keyword", "");
		String date = ServletRequestUtils.getStringParameter(request, "date", "");
		
		if(CommonUtil.isNotNull(key)){
			params.put("key", key);
		}
		if(CommonUtil.isNotNull(dataType)){
			params.put("dataType", dataType);
		}
		if(CommonUtil.isNotNull(resLibrary)){
			params.put("resLib", resLibrary);
		}
		if(CommonUtil.isNotNull(resType)){
			params.put("resType", resType);
		}
		if(CommonUtil.isNotNull(subjectClass)){
			params.put("subjectClass", subjectClass);
		}
		if(CommonUtil.isNotNull(creator)){
			params.put("creator", creator);
		}
		if(CommonUtil.isNotNull(publisher)){
			params.put("publisher", publisher);
		}
		if(CommonUtil.isNotNull(keyword)){
			params.put("keyword", keyword);
		}
		if(CommonUtil.isNotNull(date)){
			params.put("date", date);
		}
		return params;
	}
	
	//开个新线程保存检索词，因为本方法保存的信息与页面展示的内容无关，所以后台执行即可，添加详细页面加载速度
	private void saveSearchKey(final String searchVal){
		new Thread(){
			@Override
			public void run() {
				super.run();
				resourceSearchService.saveSearchKey(searchVal);
			}
		}.start();
	}
	
	
	//进入高级检索
	@RequestMapping(value="/wap/resAdv")
	public String inResAdvancedSearch() {
		return "platform/pc/resAdvSearch";
	}
	
	
	
	//执行高级检索
	@RequestMapping(value="/wap/resAdvs")
	public String resAdvancedSearch(Model model, HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		Map<String, Object> params = new HashMap<String, Object>();
		
		//分页设置
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		int size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		int begin = 0;//当前页开始数
		int end = 0;//当前页结束数
		pagination.setPageResults(size);
		pagination.setCurrentPage(currentPage);
		String key1 = ServletRequestUtils.getStringParameter(request, "KEY1", "");
		String key2 = ServletRequestUtils.getStringParameter(request, "KEY2", "");
		String key3 = ServletRequestUtils.getStringParameter(request, "KEY3", "");
		String key4 = ServletRequestUtils.getStringParameter(request, "KEY4", "");
		String[] rmstIdArr = ServletRequestUtils.getStringParameters(request, "libId");
		String[] rltyIdArr = ServletRequestUtils.getStringParameters(request, "typeId");
		String sort = ServletRequestUtils.getStringParameter(request, "SORT", "rsdaBuilddate");
		
		String pageUrl = "resAdvs?SORT="+sort;//分页跳转地址
		
		if(CommonUtil.isNotNull(key1)){
			String col1 = ServletRequestUtils.getStringParameter(request, "COL1", "rsdaTitle");
			String like1 = ServletRequestUtils.getStringParameter(request, "LIKE1", "LIKE");
			params.put("key1", key1);
			params.put("col1", col1);
			params.put("like1", like1);
			
			map.put("col1", col1);
			map.put("key1", key1);
			pageUrl += "&KEY1="+key1+"&COL1="+col1+"&LIKE1="+like1;
		}
		if(CommonUtil.isNotNull(key2)){
			String con2 = ServletRequestUtils.getStringParameter(request, "CON2", "AND");
			String col2 = ServletRequestUtils.getStringParameter(request, "COL2", "rsdaTitle");
			String like2 = ServletRequestUtils.getStringParameter(request, "LIKE2", "LIKE");
			params.put("key2", key2);
			params.put("con2", con2);
			params.put("col2", col2);
			params.put("like2", like2);
			
			map.put("col2", col2);
			map.put("key2", key2);
			pageUrl += "&KEY4="+key4+"&CON4="+con2+"&COL4="+col2+"&LIKE4="+like2;
		}
		if(CommonUtil.isNotNull(key3)){
			String con3 = ServletRequestUtils.getStringParameter(request, "CON3", "AND");
			String col3 = ServletRequestUtils.getStringParameter(request, "COL3", "rsdaTitle");
			String like3 = ServletRequestUtils.getStringParameter(request, "LIKE3", "LIKE");
			params.put("key3", key3);
			params.put("con3", con3);
			params.put("col3", col3);
			params.put("like3", like3);
			
			map.put("col3", col3);
			map.put("key3", key3);
			pageUrl += "&KEY4="+key4+"&CON4="+con3+"&COL4="+col3+"&LIKE4="+like3;
		}
		if(CommonUtil.isNotNull(key4)){
			String con4 = ServletRequestUtils.getStringParameter(request, "CON4", "AND");
			String col4 = ServletRequestUtils.getStringParameter(request, "COL4", "rsdaTitle");
			String like4 = ServletRequestUtils.getStringParameter(request, "LIKE4", "LIKE");
			params.put("key4", key4);
			params.put("con4", con4);
			params.put("col4", col4);
			params.put("like4", like4);
			
			map.put("col4", col4);
			map.put("key4", key4);
			pageUrl += "&KEY4="+key4+"&CON4="+con4+"&COL4="+col4+"&LIKE4="+like4;
		}
		
		String rmstIdStr = "";
		if(rmstIdArr != null && rmstIdArr.length > 0){
			for(String id : rmstIdArr){
				rmstIdStr += CommonUtil.strToId(id) + ",";
			}
			rmstIdStr = rmstIdStr.substring(0, rmstIdStr.length() - 1);
			//params.put("rmstIds", rmstIdStr);
			
			pageUrl += "&libId="+rmstIdStr;
		}
		String rltyIdStr = "";
		if(rltyIdArr != null && rltyIdArr.length > 0){
			for(String id : rltyIdArr){
				rltyIdStr += id + ",";
			}
			rltyIdStr = rltyIdStr.substring(0, rltyIdStr.length() - 1);
			//params.put("rltyIds", rltyIdStr);
			
			pageUrl += "&typeId="+rltyIdStr;
		}
		
		List<Object[]> dataList = this.resourceSearchService.getResAdvSearch(pagination, params, rmstIdStr, rltyIdStr, sort);
		try {
			ResCommonController.parseCoverList(request, dataList, 7, 11, 10);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		begin = size*currentPage+1;
		end = (currentPage+1)*size;
		if(begin>pagination.getTotalResults()){
			begin = pagination.getTotalResults();
		}
		if(end>pagination.getTotalResults()){
			end = pagination.getTotalResults();
		}
		
		map.put("dataList", dataList);
		map.put("pagination",pagination);
		map.put("pageUrl", pageUrl);
		map.put("begin", begin);
		map.put("end", end);
		model.addAllAttributes(map);
		return "platform/pc/resAdvSearchResult";
	}
}







