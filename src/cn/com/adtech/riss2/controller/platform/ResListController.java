package cn.com.adtech.riss2.controller.platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.platform.IResourceSearchService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.json.DateJsonValueProcessor;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;

import com.jbetter.framework.session.Pagination;

/**
 * 
 * 资源二级页面控制器
 *
 * @author MaChao
 *
 * @date：Jan 8, 2018
 */
@Controller
public class ResListController {

	@Resource(name="resourceSearchService")
	private IResourceSearchService resourceSearchService;
	
	//按特色库进入资源列表
	@RequestMapping(value="/res/db/{rmstId}")
	public String listForMetastore(@PathVariable String rmstId, Model model) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		Long numId = null;
		try {
			numId = Long.parseLong(rmstId);
		} catch (Exception e) {
			numId = CommonUtil.strToId(rmstId);
		}
		
		//获取特色库对象
		ResMetaStore metaStore = (ResMetaStore) this.resourceSearchService.get(ResMetaStore.class, numId);
		
		//遍历特色库自定义分类
		List<ResDynamicType> groupList = this.resourceSearchService.getGroupList(numId);
		int groupA = 0, groupB = 0, groupC = 0;
		if(groupList != null && groupList.size() > 0){
			int gt = 0;
			for(int i=0,s=groupList.size(); i<s; i++){
				gt = groupList.get(i).getRdtyType();
				if(gt == 1){
					groupA++;
				}else if (gt == 2){
					groupB++;
				}else if (gt == 3){
					groupC++;
				}
			}
		}
		
		//获取所有学科分类
		List<ResSubjectType> subjectClass = this.resourceSearchService.getSubjectClassList();
		
		//保存参数
		map.put("rmstId", rmstId);
		map.put("numId", numId);
		map.put("metaStore", metaStore);
		map.put("title", metaStore.getRmstName());
		map.put("groupList", groupList);
		map.put("subjectClass", subjectClass);
		map.put("groupA", groupA);
		map.put("groupB", groupB);
		map.put("groupC", groupC);
		model.addAllAttributes(map);
		return "platform/pc/resList";
	}
	
	//按资源类型进入资源列表
	@RequestMapping(value="/res/type/{rltyId}")
	public String listForRestype(@PathVariable Long rltyId, Model model) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		//获取资源类型关联的特色库
		List<Object[]> storeList = this.resourceSearchService.getStoreList(rltyId);
		
		//获取所有学科分类
		List<ResSubjectType> subjectClass = this.resourceSearchService.getSubjectClassList();
		
		//获取分类对象
		ResLiterType resType = (ResLiterType) this.resourceSearchService.get(ResLiterType.class, rltyId);
		
		//保存参数
		map.put("rltyId", rltyId);
		map.put("title", resType.getRltyName());
		map.put("storeList", storeList);
		map.put("subjectClass", subjectClass);
		model.addAllAttributes(map);
		return "platform/pc/resList";
	}
	
	
	//进入资源列表
	@RequestMapping(value="/res/db/list")
	public void listForMetastore(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		
		//获取参数
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);		//特色库ID
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);		//资源类型ID
		Long rstyId = ServletRequestUtils.getLongParameter(request, "rstyId",0L);       //学科分类ID
		String tchar = ServletRequestUtils.getStringParameter(request, "tchar", "");
		Long rdtyAId = ServletRequestUtils.getLongParameter(request, "rdtyAId", 0L);
		Long rdtyBId = ServletRequestUtils.getLongParameter(request, "rdtyBId", 0L);
		Long rdtyCId = ServletRequestUtils.getLongParameter(request, "rdtyCId", 0L);
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "");
		
		//分页设置
		int currentPage = ServletRequestUtils.getIntParameter(request, "pn", 0);
		Pagination pagination = new Pagination();
		int size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(size);
		pagination.setCurrentPage(currentPage);
		
		if(rmstId > 0L){
			map.put("rmstId", rmstId);
		}
		if(rstyId > 0L){
			map.put("rstyId", rstyId);
		}
		if(rltyId > 0L){
			map.put("rltyId", rltyId);
		}
		if(CommonUtil.isNotNull(tchar)){
			map.put("tchar", tchar);
		}
		if(rdtyAId > 0L){
			map.put("rdtyAId", rdtyAId);
		}
		if(rdtyBId > 0L){
			map.put("rdtyBId", rdtyBId);
		}
		if(rdtyCId > 0L){
			map.put("rdtyCId", rdtyCId);
		}
		if(CommonUtil.isNotNull(sort)){
			map.put("sort", sort);
		}
		
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd"));
		try {
			List<Object[]> dataList = this.resourceSearchService.getResourceList(pagination, map);
			ResCommonController.parseCoverList(request, dataList, 7, 11, 10);
			
			map.put("success", true);
			map.put("currentPage", pagination.getCurrentPage());
			map.put("totalPage", pagination.getTotalPages());
			map.put("totalLine", pagination.getTotalResults());
			map.put("dataList", dataList);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map, config).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	/** Read the backend library/type associations, not library names or resource counts. */
	@RequestMapping(value="/literature/types")
	public void literatureTypes(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store");
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Long storeId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		try {
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
			for (Object[] row : resourceSearchService.getLiteratureTypes(storeId)) {
				Map<String, Object> item = new LinkedHashMap<String, Object>();
				item.put("id", String.valueOf(row[0]));
				item.put("name", row[1]);
				items.add(item);
			}
			result.put("success", true);
			result.put("items", items);
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(ResListController.class).error("文献类型加载失败", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("success", false);
			result.put("msg", "文献类型加载失败，请稍后重试。");
		}
		response.getWriter().write(JSONObject.fromObject(result).toString());
	}

	/** Literature design: five columns, three rows, and the real volume identifier. */
	@RequestMapping(value="/literature/resources")
	public void literatureResources(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setHeader("Cache-Control", "no-store");
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		Map<String, Object> query = new HashMap<String, Object>();
		Long storeId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		Long typeId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
		int page = Math.max(0, ServletRequestUtils.getIntParameter(request, "pn", 0));
		String key = ServletRequestUtils.getStringParameter(request, "key", "").trim();
		String field = ServletRequestUtils.getStringParameter(request, "field", "all");
		if (storeId > 0L) query.put("rmstId", storeId);
		if (typeId > 0L) query.put("rltyId", typeId);
		if (!key.isEmpty()) query.put("key", key);
		query.put("field", field);
		query.put("sort", "time");
		query.put("literatureFields", true);
		Pagination pagination = new Pagination();
		pagination.setPageResults(15);
		pagination.setCurrentPage(page);
		try {
			List<Object[]> rows = this.resourceSearchService.getResourceList(pagination, query);
			ResCommonController.parseCoverList(request, rows, 7, 11, 10);
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
			if (rows != null) {
				for (Object[] row : rows) {
					Map<String, Object> item = new LinkedHashMap<String, Object>();
					item.put("id", CommonUtil.idToString(String.valueOf(row[0])));
					item.put("title", row[1]);
					item.put("creator", row[2]);
					item.put("cover", row[7]);
					item.put("identifier", row[12]);
					items.add(item);
				}
			}
			result.put("success", true);
			result.put("items", items);
			result.put("currentPage", pagination.getCurrentPage());
			result.put("totalPage", pagination.getTotalPages());
			result.put("totalLine", pagination.getTotalResults());
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("success", false);
			result.put("msg", "文献资源加载失败，请稍后重试。");
		}
		response.getWriter().write(JSONObject.fromObject(result).toString());
	}

	@RequestMapping(value="/restype/{rmstId}")
	public void resTypeForStore(@PathVariable Long rmstId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			List<Object[]> dataList = this.resourceSearchService.getResType(rmstId);
			map.put("dataList", dataList);
			
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
		}
		out.flush();
		out.close();
	} 
	
	
	//热门资源获取接口
	@RequestMapping(value="/res/hotlist/{getNum}")
	public void getHotList(@PathVariable Integer getNum, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			List<Object[]> dataList = this.resourceSearchService.getHotReslist(getNum);
			
			map.put("success", true);
			map.put("dataList", dataList);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
}
