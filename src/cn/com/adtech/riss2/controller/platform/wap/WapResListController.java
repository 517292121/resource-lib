package cn.com.adtech.riss2.controller.platform.wap;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.platform.IResourceSearchService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;

/**
 * 
 * wap版资源列表页面
 *
 * @author MaChao
 *
 * @date：Mar 23, 2018
 */
@Controller
public class WapResListController {

	@Resource(name="resourceSearchService")
	private IResourceSearchService resourceSearchService;
	
	//按特色库进入资源列表
	@RequestMapping(value="/wap/res/db/{rmstId}")
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
		
		//保存参数
		map.put("rmstId", rmstId);
		map.put("numId", numId);
		map.put("metaStore", metaStore);
		map.put("title", metaStore.getRmstName());
		model.addAllAttributes(map);
		return "platform/wap/resList";
	}
	
	//按资源类型进入资源列表
	@RequestMapping(value="/wap/res/type/{rltyId}")
	public String listForRestype(@PathVariable Long rltyId, Model model) {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		
		//获取分类对象
		ResLiterType resType = (ResLiterType) this.resourceSearchService.get(ResLiterType.class, rltyId);
		
		//保存参数
		map.put("rltyId", rltyId);
		map.put("title", resType.getRltyName());
		model.addAllAttributes(map);
		return "platform/wap/resList";
	}
}
