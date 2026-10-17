package cn.com.adtech.riss2.controller.console.sc.docu;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import cn.com.adtech.riss2.bso.sc.docu.IDocuDeliveryService;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.sc.docu.ScDocuDelivery;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.upload.IUploadService;
import com.jbetter.framework.session.Pagination;
/**
 * 文献传递 前台展示
 * @author MaChao
 *
 * @date Aug 28, 2015 - 9:59:03 AM
 */
@SuppressWarnings("unchecked")
public class DocuDeliveryController extends MultiActionController {
	private IDocuDeliveryService docuServices;
	private IUploadService uploadService;

	/**
	 * 初始化 文献传递服务 前台页面
	 */
	public ModelAndView initDocuView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		int WEB_DEFAULT_PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(WEB_DEFAULT_PAGE_SIZE);
		pagination.setCurrentPage(currentPage);
		
		List noResoleList = this.docuServices.getDocuList(pagination,userinfo.getSuinId());
		map.put("noResoleList", noResoleList);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/usercenter/docudelivery.htm?action=initDocuView");
		return new ModelAndView("platform/userCenter/usercenterDocu", map);
	}
	
	/**
	 * 初始化 文献传递服务表单
	 */
	public ModelAndView initDocuTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0);
		if(rsdaId > 0){
			ResSourceData rsda = (ResSourceData)docuServices.get(ResSourceData.class, rsdaId);
			map.put("rsda", rsda);
		}
		String subjectOptions = this.docuServices.getSubjectOptions(-1L, 0L, "1"); // 学科选项
		String rltyOptions = this.docuServices.getRltyString(0L);
		map.put("rltyOptions", rltyOptions);
		map.put("subjectOptions", subjectOptions);
		map.put("userinfo", userinfo);
		return new ModelAndView("platform/userCenter/usercenterDocuCreate", map);
	}	
	
	/**
	 * 查看 文献传递服务 页面
	 */
	public ModelAndView initDocuDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Long sddeId = ServletRequestUtils.getLongParameter(request, "sddeId", 0);
		ScDocuDelivery sdde = (ScDocuDelivery)this.docuServices.get(ScDocuDelivery.class, sddeId);
		map.put("sdde", sdde);
		map.put("userinfo", userinfo);
		return new ModelAndView("platform/userCenter/usercenterDocuDetail", map);
		
	}
	
	/**
	 * 提交 文献传递服务表单
	 */
	public ModelAndView submitDocuTable(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		//人员信息
		String uName = ServletRequestUtils.getStringParameter(request, "uName", "");
		String uEmail = ServletRequestUtils.getStringParameter(request, "uEmail", "");
		String uPhone = ServletRequestUtils.getStringParameter(request, "uPhone", "");
		
		//文献信息
		Long rstyId = ServletRequestUtils.getLongParameter(request, "rstyId", 0L);
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0);
		ResLiterType rlty =(ResLiterType)this.docuServices.get(ResLiterType.class, rltyId);
		String sddeDocuTitle = ServletRequestUtils.getStringParameter(request, "sddeDocuTitle", "");
		String sddeDocuCreator = ServletRequestUtils.getStringParameter(request, "sddeDocuCreator", "");
		String sddeDocuPublisher = ServletRequestUtils.getStringParameter(request, "sddeDocuPublisher", "");
		String sddeDocuKeyword = ServletRequestUtils.getStringParameter(request, "sddeDocuKeyword", "");
		String sddeDocuDate = ServletRequestUtils.getStringParameter(request, "sddeDocuDate", "");
		String sddeDocuPage = ServletRequestUtils.getStringParameter(request, "sddeDocuPage", "");
		String sddeDocuIsbn = ServletRequestUtils.getStringParameter(request, "sddeDocuIsbn", "");
		String sddeContent = ServletRequestUtils.getStringParameter(request, "sddeContent", "");
		
		ScDocuDelivery sdde = new ScDocuDelivery();
		sdde.setSddeSmanId(1L); 
		sdde.setSddeSuinId(userinfo.getSuinId());
		sdde.setSddeCreationTime(new Date());
		sdde.setSddeRstyId(rstyId);
		sdde.setSddeStatus(1L);
		sdde.setSddeDocuTitle(sddeDocuTitle);
		sdde.setSddeDocuType(rlty.getRltyName());
		sdde.setSddeDocuCreator(sddeDocuCreator);
		sdde.setSddeDocuPublisher(sddeDocuPublisher);
		sdde.setSddeDocuKeyword(sddeDocuKeyword);
		sdde.setSddeDocuDate(sddeDocuDate);
		sdde.setSddeDocuPage(sddeDocuPage);
		sdde.setSddeDocuIsbn(sddeDocuIsbn);
		sdde.setSddeContent(sddeContent);
		
		userinfo.setSuinName(uName);
		userinfo.setSuinEmail(uEmail);
		userinfo.setSuinMobileTelephone(uPhone);
		try{
			docuServices.save(sdde);
			docuServices.save(userinfo);
		}catch (Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath()+"/usercenter/docudelivery.htm?action=initDocuView");
		return null;
	}
	
	/**
	 * 删除文献传递
	 */
	public ModelAndView delDocu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ScDocuDelivery sdde = null;
		Long sddeId = null;
		try {
			SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
			if(userinfo == null) {
				response.sendRedirect(request.getContextPath() + "/index.htm");
				return null;
			}
			sddeId = ServletRequestUtils.getLongParameter(request, "sddeId", 0);
			sdde = (ScDocuDelivery)this.docuServices.get(ScDocuDelivery.class, sddeId);
			if(sdde == null)
				throw new ErrorMessageException("服务不存在！");
			if(!userinfo.getSuinId().equals(sdde.getSddeSuinId()))
				throw new ErrorMessageException("你没有权限删除该服务！");
			this.docuServices.delete(sdde);
		}catch(ErrorMessageException e) {
			e.printStackTrace();
		}
		response.sendRedirect(request.getContextPath() + "/usercenter/docudelivery.htm?action=initDocuView");
		return null;
	}
	
	
	public IDocuDeliveryService getDocuServices() {
		return docuServices;
	}
	
	public void setDocuServices(IDocuDeliveryService docuServices) {
		this.docuServices = docuServices;
	}

	public IUploadService getUploadService() {
		return uploadService;
	}

	public void setUploadService(IUploadService uploadService) {
		this.uploadService = uploadService;
	}
}
