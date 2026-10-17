package cn.com.adtech.riss2.controller.console.sc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.sc.IScService;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.sc.ScConsultation;
import cn.com.adtech.riss2.domain.sc.ScConsultationType;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class ScConsultationPlatformController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ScConsultationPlatformController.class);
	
	private IScService scServices;

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}

	/**
	 * 参考咨询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-24
	 */
	public ModelAndView initConsultationView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		int size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(size);
		pagination.setCurrentPage(currentPage);
		String search = ServletRequestUtils.getStringParameter(request, "search", "");// 检索条件
		
		// 保存查询条件为分页提供数据
		// 咨询列表
		List list = this.scServices.getScConsultationList(pagination, null, search, null, null, "sccoCreationTime", "DESC", "1", null);
		// 咨询分类列表
		List listType = this.scServices.getScConsultationType(null, null, null);
		map.put("consultationList", list);
		map.put("consultationTypeList", listType);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/platform/scConsultation.htm?action=initConsultationView&search=" + search );
		map.put("search", search);
		return new ModelAndView("platform/userCenter/usercenterConsultation", map);
	}
	
	/**
	 * 根据咨询类型进行检索
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-24
	 */
	public ModelAndView initConsultationTypeView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		int size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(size);
		pagination.setCurrentPage(currentPage);
		String search = ServletRequestUtils.getStringParameter(request, "search", "");// 检索条件
		Long sctyId = ServletRequestUtils.getLongParameter(request, "sctyId",-1);
		
		// 保存查询条件为分页提供数据
		// 咨询列表
		List list = this.scServices.getScConsultationList(pagination, sctyId, search, null, null, "sccoCreationTime", "DESC", "1", null);
		// 咨询分类列表
		List listType = this.scServices.getScConsultationType(null, null, null);
		map.put("consultationList", list);
		map.put("consultationTypeList", listType);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/platform/scConsultation.htm?action=initConsultationTypeView&search=" + search + "&sctyId="+sctyId);
		map.put("search", search);
		
		ScConsultationType sct = (ScConsultationType)this.scServices.get(ScConsultationType.class, sctyId);
		map.put("sctyId", sctyId);
		if(sct != null)
			map.put("sct", sct);
		return new ModelAndView("platform/userCenter/usercenterConsultationType", map);
	}

	/**
	 * 我的咨询
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao 
	 * @date 2010-11-15
	 */
	public ModelAndView myConsultationView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		int size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(size);
		pagination.setCurrentPage(currentPage);
		String search = ServletRequestUtils.getStringParameter(request, "search", "");// 检索条件
		Long suinId = userinfo.getSuinId();
		// 保存查询条件为分页提供数据
		List myConsultationList = this.scServices.getScConsultationList(pagination, null, search, null, null, "sccoCreationTime", "DESC",
				"1", suinId);
		map.put("myConsultationList", myConsultationList);
		// 咨询分类列表
		List listType = this.scServices.getScConsultationType(null, null, null);
		map.put("consultationTypeList", listType);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/platform/scConsultation.htm?action=myConsultationView&search=" + search);
		map.put("search", search);
		return new ModelAndView("platform/userCenter/usercenterMyConsultation", map);
	}
	

	/**
	 * 删除咨询
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-24
	 */
	public ModelAndView deleteScCosultation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Long sccoId = ServletRequestUtils.getLongParameter(request, "sccoId", 0);
		ScConsultation sc = (ScConsultation)this.scServices.get(ScConsultation.class, sccoId);
		if(sc != null && sc.getSuinId().equals(userinfo.getSuinId())) {
			this.scServices.delete(sc);
		}
		response.sendRedirect(request.getContextPath()+"/platform/scConsultation.htm?action=myConsultationView");
		return null;
	}
	
	/**
	 * 咨询详情
	 * @param request
	 * @param response 
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-24
	 */
	public ModelAndView initCosultationDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Long sccoId = ServletRequestUtils.getLongParameter(request, "sccoId", 0);
		ScConsultation sc = (ScConsultation)this.scServices.get(ScConsultation.class, sccoId);
		if(sc != null)
			request.setAttribute("sc", sc);
		return new ModelAndView("platform/userCenter/usercenterConsultationDetail");
	}
}
