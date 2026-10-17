package cn.com.adtech.riss2.controller.platform.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IMemberResourceService;
import cn.com.adtech.riss2.bso.resource.IMetadataElementPoolService;
import cn.com.adtech.riss2.bso.resource.IResDynamicTypeService;
import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

import com.jbetter.framework.session.Pagination;

public class MemberResourceController extends MultiActionController {
	
	private IMemberResourceService memberResourceService = null;
	private IMetadataElementPoolService elementPoolService = null;
	private IResourceDataService resourceService = null;
	private IResDynamicTypeService dynamicTypeService = null;
	public void setDynamicTypeService(IResDynamicTypeService dynamicTypeService) {
		this.dynamicTypeService = dynamicTypeService;
	}
	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}
	public void setMemberResourceService(
			IMemberResourceService memberResourceService) {
		this.memberResourceService = memberResourceService;
	}
	public void setElementPoolService(IMetadataElementPoolService elementPoolService) {
		this.elementPoolService = elementPoolService;
	} 

	/**
	 * 用户资源列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response,
			Pagination pagination) throws Exception {
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		Map<String, Object> param = new HashMap<String, Object>();
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		} else {
			int sourceType = ServletRequestUtils.getIntParameter(request, "sourceType", 0);
			int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
			pagination.setCurrentPage(currentPage);
			pagination.setPageResults(ConstantsObj.DEFAULT_PAGE_SIZE);
			String keyWord = ServletRequestUtils.getStringParameter(request, "keyWord","");
			long userid = user.getSuinId();
			List<ResSourceData> result = memberResourceService.getMemberResourceList(userid, keyWord,sourceType, pagination);
			param.put("sourceList", result);
			param.put("pagination", pagination);
			param.put("currentPage", currentPage);
			param.put("pageUrl", "/resource.htm?action=list&keyWord="+keyWord+"&sourceType=" + sourceType);
			param.put("display", ServletRequestUtils.getStringParameter(request, "display"));
		}
		return new ModelAndView("platform/userCenter/usercenterResourceList", param);
	}	
	
	/**
	 * 新增资源选择
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView add(HttpServletRequest request, HttpServletResponse response){
		List<ResMetaStore> storeList = memberResourceService.getStoreList();
		request.setAttribute("storeList", storeList);
		return new ModelAndView("platform/userCenter/usercenterAddType");
	} 
	
	/**
	 * 新增资源
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initAdd(HttpServletRequest request, HttpServletResponse response){
		long store = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		long stype = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
		List<ResElementPool> elementList = elementPoolService.getElementByTemplet(store, stype);
		String subjectOptions = resourceService.getSubjectOptions(-1L, 0L, "1"); // 学科选项
		String dynamicTypeOptions = resourceService.getDynamicTypeOptions(store, -1, 1); //自定义分类一
		String dynamicTypeOptions2 = resourceService.getDynamicTypeOptions(store, -1, 2); //自定义分类2
//		String dynamicTypeOptions3 = resourceService.getDynamicTypeOptions(store, -1, 3); //自定义分类3
		//String sourceSubjectOptions = resourceService.getSourceSubjectOptions(store, -1);
		String dynamicTypeOptions3 = dynamicTypeService.getDynamicOptions(store, 0L, 3); //自定义分类3
		String sysUserTypeOptions = resourceService.getSourceRightsOptions(-1);
		// 限制会员上传的类型
		ResLiterType literType = (ResLiterType)resourceService.get(ResLiterType.class, stype);
		String stintTypesTemp = ""; // 从数据库取值
		if(ResLiterType.TYPE_DOCUMENT.intValue() == literType.getRltyType().intValue()){
			stintTypesTemp = ConstantsObj.getSysProperty(MmSettingConstant.getAllowUserUploadFileTypeDocument());
		}else if (ResLiterType.TYPE_MEDIA.intValue() == literType.getRltyType().intValue()) {
			stintTypesTemp = ConstantsObj.getSysProperty(MmSettingConstant.getAllowUserUploadFileTypeAudieo());
		}else if (ResLiterType.TYPE_PICTURE.intValue() == literType.getRltyType().intValue()) {
			stintTypesTemp = ConstantsObj.getSysProperty(MmSettingConstant.getAllowUserUploadFileTypePicture());
		}else if (ResLiterType.TYPE_VIDEO.intValue() == literType.getRltyType().intValue()) {
			stintTypesTemp = ConstantsObj.getSysProperty(MmSettingConstant.getAllowUserUploadFileTypeVideo());
		}
		String stintTypes = ""; // 用于界面显示
		if(ServiceUtil.isNotNull(stintTypesTemp)){
			String[]types = stintTypesTemp.toLowerCase().split("\\|");
			for(int i=0;i<types.length;i++){
				if(i !=0 ){
					stintTypes += "、";
				}
				stintTypes += types[i];
			}
		}
		request.setAttribute("elementList", elementList);
		request.setAttribute("subjectOptions", subjectOptions);
		request.setAttribute("store", store);
		request.setAttribute("stype", stype);
		request.setAttribute("dynamicTypeOptions", dynamicTypeOptions);
		request.setAttribute("dynamicTypeOptions2", dynamicTypeOptions2);
		request.setAttribute("dynamicTypeOptions3", dynamicTypeOptions3);
		//request.setAttribute("dynamicTypeOptions3", sourceSubjectOptions);
		request.setAttribute("sysUserTypeOptions", sysUserTypeOptions);
		
		request.setAttribute("stintTypes", stintTypes);
		return new ModelAndView("platform/userCenter/usercenterAddPage");
	}
	
	/**
	 * 删除资源
	 */
	public ModelAndView deleteResource(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long sourceId = MyRequestUtil.getLongParameter(request, "id");
		boolean b = resourceService.deleteResource(sourceId, request);
		
		String resultMessage = "";
		if(b){
			resultMessage = "<script>showMsg('删除成功!');</script>";
		}else{
			resultMessage = "<script>showMsg('删除失败!');</script>";
		}
		Pagination pagination = new Pagination();
		request.setAttribute("result", resultMessage);
		return list(request, response, pagination);
	}

}
