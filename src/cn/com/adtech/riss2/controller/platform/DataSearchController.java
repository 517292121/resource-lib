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
import cn.com.adtech.riss2.core.i18n.FrontendMessages;
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
			Long categoryId = ServletRequestUtils.getLongParameter(request, "rdtyAId", 0L);
			if (categoryId > 0L) {
				StringBuilder ids = new StringBuilder();
				for (Long id : resourceSearchService.getLiteratureResourceIds(categoryId, storeId)) {
					if (ids.length() > 0) ids.append(','); ids.append(id.longValue());
				}
				params.put("resourceIds", ids.toString());
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
		Long categoryId = ServletRequestUtils.getLongParameter(request, "rdtyAId", 0L);
		if (categoryId > 0) query.put("literatureCategoryId", categoryId);
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
		List<Object[]> types = new ArrayList<Object[]>();
		for (Object[] row : resourceSearchService.getLiteratureTypes(storeId)) {
			Object[] item = row.clone();
			item[1] = FrontendMessages.backendName(request, (String)row[1], (String)row[4]);
			types.add(item);
		}
		model.addAttribute("advancedTypes", types);
		return "platform/pc/resAdvSearch";
	}
	
	
	
	// Advanced and ordinary results share one frontend template.
	@RequestMapping(value="/resAdvs")
	public String resAdvancedSearch(Model model, HttpServletRequest request) {
		model.addAttribute("advancedSearch", true);
		for (int i = 1; i <= 4; i++) {
			String key = ServletRequestUtils.getStringParameter(request, "KEY" + i, "").trim();
			if (!key.isEmpty()) { model.addAttribute("key", key); break; }
		}
		return "platform/pc/searchResults";
	}

	@RequestMapping(value="/search/advanced-results")
	public void advancedSearchResultsJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store");
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		try {
			Map<String, Object> criteria = new LinkedHashMap<String, Object>();
			for (int i = 1; i <= 4; i++) {
				String key = ServletRequestUtils.getStringParameter(request, "KEY" + i, "").trim();
				if (key.isEmpty()) continue;
				criteria.put("key" + i, key);
				criteria.put("col" + i, cn.com.adtech.riss2.bso.platform.AdvancedSearchQuery.field(
						ServletRequestUtils.getStringParameter(request, "COL" + i, "rsdaTitle")));
				criteria.put("like" + i, ServletRequestUtils.getStringParameter(request, "LIKE" + i, "LIKE"));
				criteria.put("con" + i, ServletRequestUtils.getStringParameter(request, "CON" + i, "AND"));
			}
			// Validate field names/operators before passing anything into HQL.
			cn.com.adtech.riss2.bso.platform.AdvancedSearchQuery.conditions(criteria, new ArrayList<Object>());
			criteria.put("advancedResultFields", true);
			String libraries = advancedFilterIds(request.getParameterValues("libId"), true);
			String types = advancedFilterIds(request.getParameterValues("typeId"), false);
			int requestedPage = Math.max(1, Math.min(100000,
					ServletRequestUtils.getIntParameter(request, "pn", 1)));
			Pagination pagination = new Pagination();
			pagination.setPageResults(SEARCH_RESULT_PAGE_SIZE);
			pagination.setCurrentPage(requestedPage - 1);
			List<Object[]> rows = resourceSearchService.getResAdvSearch(pagination, criteria, libraries, types, "rsdaBuilddate");
			if (pagination.getTotalPages() > 0 && pagination.getCurrentPage() >= pagination.getTotalPages()) {
				pagination.setCurrentPage(pagination.getTotalPages() - 1);
				rows = resourceSearchService.getResAdvSearch(pagination, criteria, libraries, types, "rsdaBuilddate");
			}
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
			if (rows != null) for (Object[] row : rows) {
				Map<String, Object> item = new LinkedHashMap<String, Object>();
				item.put("id", CommonUtil.idToString(String.valueOf(row[0])));
				item.put("title", row[1]); item.put("creator", row[2]); item.put("publisher", row[3]);
				item.put("content", row[5]); item.put("dataType", "资源");
				item.put("passages", new ArrayList<Object>());
				Map<String, Object> fields = new LinkedHashMap<String, Object>();
				fields.put("rsdaTitle", row[1]); fields.put("rsdaCreator", row[2]);
				fields.put("rsdaPublisher", row[3]); fields.put("rsdaDescription", row[5]);
				fields.put("rsdaKeyword", row[6]); fields.put("rsdaSubject", row[12]);
				fields.put("rsdaIdentifier", row[13]);
				item.put("fields", fields);
				items.add(item);
			}
			result.put("success", true); result.put("items", items);
			result.put("bookCount", pagination.getTotalResults());
			result.put("recordCount", resourceSearchService.getResAdvSearchHitCount(criteria, libraries, types));
			result.put("totalLine", pagination.getTotalResults()); result.put("totalPage", pagination.getTotalPages());
			result.put("currentPage", pagination.getCurrentPage() + 1); result.put("pageSize", SEARCH_RESULT_PAGE_SIZE);
		} catch (IllegalArgumentException e) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			result.put("success", false); result.put("msg", "高级检索条件无效，请重新选择。");
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(DataSearchController.class).error("高级检索结果加载失败", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("success", false); result.put("msg", "高级检索结果加载失败，请稍后重试。");
		}
		response.getWriter().write(JSONObject.fromObject(result).toString());
	}

	private String advancedFilterIds(String[] values, boolean encoded) {
		java.util.Set<Long> ids = new java.util.LinkedHashSet<Long>();
		if (values != null) for (String value : values) for (String token : value.split(",")) {
			StringBuilder decoded = new StringBuilder();
			for (char c : token.trim().toCharArray()) {
				int digit = "zKUOJMwAsE".indexOf(c);
				if (encoded && digit >= 0) decoded.append(digit);
				else if (c >= '0' && c <= '9') decoded.append(c);
				else throw new IllegalArgumentException("Invalid resource filter");
			}
			if (decoded.length() == 0) continue;
			long id = Long.parseLong(decoded.toString());
			if (id < 1L) throw new IllegalArgumentException("Invalid resource filter");
			ids.add(id);
		}
		StringBuilder result = new StringBuilder();
		for (Long id : ids) { if (result.length() > 0) result.append(','); result.append(id); }
		return result.toString();
	}
}







