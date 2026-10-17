package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.bso.resource.IMetadataElementPoolService;
import cn.com.adtech.riss2.bso.resource.IMetadataTempletManagementService;
import cn.com.adtech.riss2.bso.resource.MetadataElementPoolImp;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.jsonrpc.MetadataTempletRpc;

/**
 * 资源录入模板管理控制器
 * 
 * @author MaChao
 */
public class MetadataTempletManager extends MultiActionController {
	
	private IMetadataTempletManagementService itm = null;
	private IMetadataElementPoolService elementPool = null;
	private ICommonService commonService = null;
	public void setElementPool(IMetadataElementPoolService elementPool) {
		this.elementPool = elementPool;
	}

	public void setItm(IMetadataTempletManagementService itm) {
		this.itm = itm;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	/**
	 * 初始化资源类型选择列表页面
	 */
	public ModelAndView SourceTypeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/metadata/TempletSelectRetype");
	}

	/**
	 * 进入模板设置页面
	 */
	public ModelAndView TempletEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rmstId = MyRequestUtil.getLongParameter(request, "rmst"); // 特色库ID
		long rltyId = MyRequestUtil.getLongParameter(request, "rlty"); // 资源类型ID
		// 获取资源类型下的所有元素
		List<ResElementPool> sourceElements = elementPool.getElementList(rltyId);
		// 获取该模板中拥有的元素ID
		List<Long> templetElementIds = elementPool.getElementIdByTemplet(rmstId, rltyId);
		
		request.setAttribute("elementlist", sourceElements);
		request.setAttribute("tmpIds", templetElementIds);
		request.setAttribute("rmstId", rmstId);
		request.setAttribute("rltyId", rltyId);
		String sql = "select RMST_NAME from RES_META_STORE where RMST_ID=" + rmstId;
		
		request.setAttribute("rmstName", commonService.getFirstData(sql));
		sql = "select RLTY_NAME from RES_LITER_TYPE where RLTY_ID=" + rltyId;
		request.setAttribute("rltyName", commonService.getFirstData(sql));
		return new ModelAndView("resource/metadata/MetadataTempletEdit");
	}

	/**
	 * 获取资源类型JSON数据
	 */
	public ModelAndView getSourceTypeListJsonData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		long store_id = MyRequestUtil.getLongParameter(request, "RMST_ID");
		long stype_id = MyRequestUtil.getLongParameter(request, "RLTY_ID");
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) { // 如果用户为空
			response.sendRedirect(request.getContextPath() + "/console/index.htm");
			return null;
		}
		String json = itm.getSourceTypeList(sysManager.getSmanId(), store_id, stype_id, begin, pageNum);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(json);
		return null;
	}

	/**
	 * 动态根据特色库获取所联系的资源类型
	 */
	public ModelAndView getSourceTypeByStore(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		long store_id = MyRequestUtil.getLongParameter(request, "store");
		if(store_id < 1) {
			out.print("[]");
		}else {
			String json = itm.getSourceTypeByStore(store_id);
			String hasNull = request.getParameter("hasNull");
			if("hasNull".equals(hasNull))
				json = CommonUtil.createStoreSelectHasNull(json, "0", "不限");
			out.print(json);
		}
		return null;
	}

	/**
	 * 进入新增核心元素选择列表
	 */
	public ModelAndView AddMetadataElementList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long store = MyRequestUtil.getLongParameter(request, "store");
		long sourceType = MyRequestUtil.getLongParameter(request, "sourceType");
		int eleType = MyRequestUtil.getIntParameter(request, "eleType");
		List<ResElementPool> eleList = elementPool.getElementByUnTemplet(store, sourceType, eleType);
		request.setAttribute("elementlist", eleList);
		request.setAttribute("rmstId", store);
		request.setAttribute("rltyId", sourceType);
		return new ModelAndView("resource/metadata/ElementAddList");
	}

	/**
	 * 弹出扩展元素界面
	 */
	public ModelAndView OnAddExtendElement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long store = MyRequestUtil.getLongParameter(request, "store");
		long sourceType = MyRequestUtil.getLongParameter(request, "sourceType");
		request.setAttribute("rmstId", store);
		request.setAttribute("rltyId", sourceType);
		return new ModelAndView("resource/metadata/AddExtendElement");
	}

	/**
	 * 进入新增元素修饰
	 */
	public ModelAndView OnAddChildElement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long eleid = MyRequestUtil.getLongParameter(request, "eid");
		long sourceType = MyRequestUtil.getLongParameter(request, "sourceType");
		request.setAttribute("eleid", eleid);
		request.setAttribute("rltyId", sourceType);
		return new ModelAndView("resource/metadata/AddChildElement");
	}

	/**
	 * 新增自定义扩展元素
	 */
	public ModelAndView AddExtendElement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long store = MyRequestUtil.getLongParameter(request, "store");
		long sourceType = MyRequestUtil.getLongParameter(request, "stype");
		String attName = MyRequestUtil.getStringParameter(request, "attName");
		String attTitle = MyRequestUtil.getStringParameter(request, "attTitle");
		String attCode = MyRequestUtil.getStringParameter(request, "attCode");
		String attRestrict = MyRequestUtil.getStringParameter(request, "attRestrict");
		ResElementPool element = new ResElementPool();
		element.setRepoCode(attCode);
		element.setRepoName(attName);
		element.setRepoPltyId(sourceType);
		element.setRepoRestrict(attRestrict);
		element.setRepoSerianum(0);
		element.setRepoTitle(attTitle);
		element.setRepoType(3L);
		try {
			Serializable s = elementPool.saveElement(element);
			long id = 0;
			if(s!= null && Long.parseLong(s.toString()) > 0) {
				id = Long.parseLong(s.toString());
				MetadataTempletRpc mtr = new MetadataTempletRpc();
				mtr.addMetaElement(store, sourceType, id);
			}
			request.setAttribute("result", "<span style='color:blue'>保存成功</span>");
			request.setAttribute("eleId", id);
		}catch(Exception e) {
			request.setAttribute("result", "<span style='color:red'>保存失败</span>");
			request.setAttribute("eleId", 0);
		}
		return OnAddExtendElement(request, response);
	}

	/**
	 * 保存资源录入模板
	 * 
	 * @param request
	 * @param response
	 */
	public ModelAndView SaveTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String[] eleidList = request.getParameterValues("metaElementId");
		long rmstId = MyRequestUtil.getLongParameter(request, "rmst"); // 特色库ID
		long rltyId = MyRequestUtil.getLongParameter(request, "rlty"); // 资源类型ID
		boolean b = itm.updateTempletElements(rmstId, rltyId, eleidList);
		if(b) {
			request.setAttribute("result", "<script>alert('模板更新成功!');</script>");
		}else {
			request.setAttribute("result", "<script>alert('对不起，更新失败!');</script>");
		}
		return TempletEdit(request, response);
	}

}
