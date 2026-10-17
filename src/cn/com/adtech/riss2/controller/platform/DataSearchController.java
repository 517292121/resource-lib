package cn.com.adtech.riss2.controller.platform;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.ArrayList;
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

import cn.com.adtech.riss2.bso.platform.IResourceSearchService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.bean.ResPageBean;
import cn.com.unionlib.lucene.dao.ILuceneSearchService;

import com.jbetter.framework.session.Pagination;

/**
 * 
 * 前端数据检索
 *
 * @author MaChao
 *
 * @date：Jan 25, 2018
 */
@Controller
public class DataSearchController {
	private static final int SEARCH_RESULT_PAGE_SIZE = 6;
	
	@Resource(name="luceneSearchService")
	private ILuceneSearchService luceneSearchService;
	
	
	@Resource(name="resourceSearchService")
	private IResourceSearchService resourceSearchService;

	//进入lucene全文检索
	@RequestMapping(value="/ls/{key}")
	public String luceneSearch(@PathVariable String key, Model model, HttpServletRequest request) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		String attr = ServletRequestUtils.getStringParameter(request, "attr", "");
		int match = ServletRequestUtils.getIntParameter(request, "accurate", 0);
		
		map.put("key", key);
		map.put("match", match);
		if(CommonUtil.isNotNull(attr)){
			map.put("attr", attr);
		}
		
		model.addAllAttributes(map);
		saveSearchKey(key);
		return "platform/pc/searchResults";
	}

	@RequestMapping(value="/search")
	public String searchResults(HttpServletRequest request, Model model) {
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		model.addAttribute("key", key);
		if (CommonUtil.isNotNull(key)) saveSearchKey(key);
		return "platform/pc/searchResults";
	}

	/** Design 5.psb: preserve the existing full-text index and child-page links. */
	@RequestMapping(value="/search/results")
	public void searchResultsJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store");
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, String> params = paramsParser(request);
		int requestedPage = Math.max(1, Math.min(100000,
				ServletRequestUtils.getIntParameter(request, "pn", 1)));
		PageBean pagination = new PageBean(requestedPage, SEARCH_RESULT_PAGE_SIZE);
		try {
			// Volume identifiers are in the resource table, not the legacy full-text index.
			if ("identifier".equals(params.get("attr"))) {
				identifierSearchResults(request, requestedPage, result);
				response.getWriter().write(JSONObject.fromObject(result).toString());
				return;
			}
			Long storeId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
			if (storeId > 0L) {
				ResMetaStore store = (ResMetaStore) resourceSearchService.get(ResMetaStore.class, storeId);
				if (store == null) throw new IllegalArgumentException("Resource library does not exist");
				params.put("resLib", store.getRmstName());
			}
			long[] counts = luceneSearchService.getSearchResultCounts(params);
			int totalPages = (int) Math.min(Integer.MAX_VALUE,
					(counts[0] + SEARCH_RESULT_PAGE_SIZE - 1L) / SEARCH_RESULT_PAGE_SIZE);
			pagination.setCurrentPage(Math.max(1, Math.min(requestedPage, totalPages)));
			List<ResMainBean> rows = luceneSearchService.luceneSearchForChildPage(pagination, params);
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
			if (rows != null) for (ResMainBean row : rows) {
				Map<String, Object> item = new LinkedHashMap<String, Object>();
				item.put("id", CommonUtil.idToString(String.valueOf(row.getId())));
				item.put("title", row.getTitle());
				item.put("content", row.getContent());
				item.put("creator", row.getCreator());
				item.put("publisher", row.getPublisher());
				item.put("dataType", row.getDataType());
				List<Map<String, Object>> passages = new ArrayList<Map<String, Object>>();
				if (row.getPageList() != null) for (ResPageBean page : row.getPageList()) {
					if (passages.size() >= 3) break;
					Map<String, Object> passage = new LinkedHashMap<String, Object>();
					passage.put("content", page.getContent());
					passage.put("title", page.getTitle());
					passage.put("pageNum", page.getPageNum());
					passages.add(passage);
				}
				item.put("passages", passages);
				items.add(item);
			}
			result.put("success", true);
			result.put("items", items);
			result.put("bookCount", counts[0]);
			result.put("recordCount", counts[1]);
			result.put("totalLine", pagination.getCountRows());
			result.put("totalPage", totalPages);
			result.put("currentPage", pagination.getCurrentPage());
			result.put("pageSize", SEARCH_RESULT_PAGE_SIZE);
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(DataSearchController.class).error("检索结果加载失败", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("success", false);
			result.put("msg", "检索结果加载失败，请稍后重试。");
		}
		response.getWriter().write(JSONObject.fromObject(result).toString());
	}

	private void identifierSearchResults(HttpServletRequest request, int requestedPage,
			Map<String, Object> result) throws Exception {
		Map<String, Object> query = new HashMap<String, Object>();
		query.put("field", "identifier");
		query.put("literatureFields", true);
		query.put("sort", "time");
		String key = ServletRequestUtils.getStringParameter(request, "key", "").trim();
		if (!key.isEmpty()) query.put("key", key);
		Long storeId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		if (storeId > 0) query.put("rmstId", storeId);
		Long typeId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
		if (typeId > 0) query.put("rltyId", typeId);
		Pagination pagination = new Pagination();
		pagination.setPageResults(SEARCH_RESULT_PAGE_SIZE);
		pagination.setCurrentPage(requestedPage - 1);
		List<Object[]> rows = resourceSearchService.getResourceList(pagination, query);
		List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
		if (rows != null) for (Object[] row : rows) {
			Map<String, Object> item = new LinkedHashMap<String, Object>();
			item.put("id", CommonUtil.idToString(String.valueOf(row[0])));
			item.put("title", row[1]);
			item.put("creator", row[2]);
			item.put("content", "卷号：" + CommonUtil.nullToBlank(row[12]));
			item.put("dataType", "资源");
			item.put("passages", new ArrayList<Object>());
			items.add(item);
		}
		result.put("success", true);
		result.put("items", items);
		result.put("bookCount", pagination.getTotalResults());
		result.put("recordCount", resourceSearchService.getIdentifierHitCount(query));
		result.put("totalLine", pagination.getTotalResults());
		result.put("totalPage", pagination.getTotalPages());
		result.put("currentPage", pagination.getCurrentPage() + 1);
		result.put("pageSize", SEARCH_RESULT_PAGE_SIZE);
	}
	
	//lucene全文检索
	@RequestMapping(value="/luceneSearch")
	public void luceneSearchJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
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
	
	//lucene全文检索-带子页
	@RequestMapping(value="/searchForPage")
	public void luceneSearchForPageJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		PrintWriter out = response.getWriter();
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> params = paramsParser(request);
		
		//分页设置
		int currentPage = ServletRequestUtils.getIntParameter(request, "pn", 1);
		int size = ServletRequestUtils.getIntParameter(request, "size", 0);
		if(size<=0){
			size=Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		}
		PageBean page = new PageBean();
		page.setCurrentPage(currentPage);
		page.setPageSize(size);
		
		try {
			List<ResMainBean> dataList = this.luceneSearchService.luceneSearchForChildPage(page, params);
			dataList = parsePdfExist(dataList);
			
			
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
	//解析资源pdf文件是否存在
	private List<ResMainBean> parsePdfExist(List<ResMainBean> reslist) throws UnsupportedEncodingException{
		if(reslist == null){
			return null;
		}
		for(ResMainBean res : reslist){
			String srcpath = res.getSrcPath();
			if(!CommonUtil.isNotNull(srcpath)){
				continue;
			}
			File f = new File(srcpath);
			if(!f.exists() || !f.isDirectory()){
				continue;
			}
			String dirname = f.getName();
			f = new File(srcpath, dirname + ".pdf");
			if(f.exists()){
				String pdfpath = f.getAbsolutePath();
				pdfpath = CommonUtil.encode(pdfpath);
				res.setSrcPath(pdfpath);
			}else{
				res.setSrcPath("");
			}
		}
		return reslist;
	}
	
	//lucene检索结果聚类统计
	@RequestMapping(value="/luceneGroup")
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
		String attr = ServletRequestUtils.getStringParameter(request, "attr", "");
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		String dataType = ServletRequestUtils.getStringParameter(request, "dataType", "");
		String resLibrary = ServletRequestUtils.getStringParameter(request, "resLib", "");
		String resType = ServletRequestUtils.getStringParameter(request, "resType", "");
		String subjectClass = ServletRequestUtils.getStringParameter(request, "subjectClass", "");
		String creator = ServletRequestUtils.getStringParameter(request, "creator", "");
		String publisher = ServletRequestUtils.getStringParameter(request, "publisher", "");
		String keyword = ServletRequestUtils.getStringParameter(request, "keyword", "");
		String date = ServletRequestUtils.getStringParameter(request, "date", "");
		String match = ServletRequestUtils.getStringParameter(request, "match", "0");	//匹配方式，默认模糊匹配(0)
		
		if(CommonUtil.isNotNull(attr)){
			params.put("attr", attr);
		}
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
		params.put("match", match);
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
	@RequestMapping(value="/resAdv")
	public String inResAdvancedSearch(HttpServletRequest request, Model model) {
		Long storeId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		if (storeId > 0L) model.addAttribute("advancedLibraryId", CommonUtil.idToString(String.valueOf(storeId)));
		model.addAttribute("advancedTypes", resourceSearchService.getLiteratureTypes(storeId));
		return "platform/pc/resAdvSearch";
	}
	
	
	
	//进入高级检索
	@RequestMapping(value="/resAdvs")
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
			
			map.put("key", key1);	//简单检索时使用
			map.put("attr", col1);
			map.put("redkey", "<span style='color:red'>"+key1+"</span>");
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







