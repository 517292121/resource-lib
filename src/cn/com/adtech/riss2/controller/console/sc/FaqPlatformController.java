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
import cn.com.adtech.riss2.domain.sc.ScFaqLibrary;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class FaqPlatformController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(FaqPlatformController.class);

	private IScService scServices;

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}

	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-9
	 */
	public ModelAndView initPlatformFaqView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		String search = ServletRequestUtils.getStringParameter(request, "search", "");
		Long scftId = ServletRequestUtils.getLongParameter(request, "scftId", 0);
		Pagination pagination = new Pagination();
		int size = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(size);
		pagination.setCurrentPage(currentPage);
		List faqList = this.scServices.getScFaqLibraryList(pagination, scftId, search);
		map.put("faqList", faqList);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/platform/faq.htm?action=initPlatformFaqView");
		map.put("search", search);
		return new ModelAndView("platform/userCenter/usercenterFaq", map);
	}
}
