package cn.com.adtech.riss2.controller.console.resource.checkout;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.bso.resource.IMemberResourceService;
import cn.com.adtech.riss2.bso.resource.IResDynamicTypeService;
import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

public class MemberResourceCheckController extends MultiActionController{

	private IResourceDataService resourceService = null; 
	private IMemberResourceService memberResourceService = null;
	private IResDynamicTypeService dynamicTypeService = null;
	public void setDynamicTypeService(IResDynamicTypeService dynamicTypeService) {
		this.dynamicTypeService = dynamicTypeService;
	}
	public void setMemberResourceService(
			IMemberResourceService memberResourceService) {
		this.memberResourceService = memberResourceService;
	}
	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}


	public ModelAndView initMemberResourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/checkout/memberResourceList");
	}
	
	public void memberResourceListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		
		// 查询条件一：状态
		int rsdaStatus = ServletRequestUtils.getIntParameter(request, "rsdaStatus", 0);
		// 查询条件二：题名
		String title = ServletRequestUtils.getStringParameter(request, "rsdaTitle","");
		
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rsdaBuilddate");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
		
		List<Object> list = this.memberResourceService.getMemberResourceList(pagination, title, rsdaStatus, sort, dir);
		String result = CommonUtil.createJsonTable(list, new String[] {"rsdaId", "rsdaTitle", "rltyId", "rltyName",
				"rsdaBuilddate", "rsdaStatus", "suinLoginName"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}
	
	public ModelAndView initChecked(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long sourceId = MyRequestUtil.getLongParameter(request, "rsdaid");
		ResSourceData source = resourceService.getResourceMetaInfo(sourceId);
		long languageId = ConstantsObj.MEMBER_RESOURCE_DEFAULT_LANGUAGE;
		// 获取资源基本信息
		// 获取所有的元素信息
		List<ResElementPool> EleDataList = resourceService.getResourceAllData(source.getRsdaRmstId(), source.getRsdaRltyId(), languageId,
				sourceId);
		// 获取语种
		ResLanguageType language = (ResLanguageType) this.resourceService.get(ResLanguageType.class, languageId);
		// 获取学科下拉options选项列表
		String subjectOptions = resourceService.getSubjectOptions(ToolUnit.NullTo0(source.getRsdaRstyId()), 0, "1"); // 学科选项
		// 获取自定义分类下拉options选项列表
		String dynamicTypeOptions = resourceService.getDynamicTypeOptions(source.getRsdaRmstId(), source.getRsdaRdtyId() == null ? 0
				: source.getRsdaRdtyId(), 1); //分类一
		String dynamicTypeOptions2 = resourceService.getDynamicTypeOptions(source.getRsdaRmstId(), source.getRsdaRdtyTwo() == null ? 0
				: source.getRsdaRdtyTwo(), 2); //分类二 
//		String dynamicTypeOptions3 = resourceService.getDynamicTypeOptions(source.getRsdaRmstId(), source.getRsdaRdtyThree() == null ? 0
//				: source.getRsdaRdtyThree(), 3); //分类三
		
		String dynamicTypeOptions3 = dynamicTypeService.getDynamicOptions(source.getRsdaRmstId(), source.getRsdaRdtyThree() == null ? 0
				: source.getRsdaRdtyThree(), 3); //分类三
		request.setAttribute("languageId", languageId);
		request.setAttribute("language", language);
		request.setAttribute("subjectOptions", subjectOptions);
		request.setAttribute("dynamicTypeOptions", dynamicTypeOptions);
		request.setAttribute("dynamicTypeOptions2", dynamicTypeOptions2);
		request.setAttribute("dynamicTypeOptions3", dynamicTypeOptions3);
		request.setAttribute("resource", source);
		request.setAttribute("EleDataList", EleDataList);
		return new ModelAndView("resource/checkout/memberResourceChecked");
	}
	
	public ModelAndView checkedMemberResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int checkType = ServletRequestUtils.getIntParameter(request, "checkType", 0);
		
		if(checkType == 1){
			return new ModelAndView("");
		}else{
			return new ModelAndView("");
		}
	}
}













