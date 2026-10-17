package cn.com.adtech.riss2.controller.platform.member.restaginfo;

import java.util.Date;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.restaginfo.IResTagInfoService;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResTagInfo;

/**
 * 个性tag
 * 
 * @author MaChao
 * 
 */
public class ResTagInfoController extends MultiActionController {
	
	private ResourceBundleMessageSource messageSource;

	private IResTagInfoService resTagInfoService;

	public void addNewTag(HttpServletRequest request, HttpServletResponse response,
			ResTagInfo resTagInfo) throws Exception {
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if (user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return;
		} else {
			resTagInfo.setRtinDate(new Date());
			resTagInfo.setRtinSuinId(user.getSuinId());
			resTagInfo.setRtinRsdaId(ServletRequestUtils.getLongParameter(request, "rsdaId"));
			this.resTagInfoService.addTag(resTagInfo);
			request.setAttribute("success", messageSource.getMessage("search.tag.addTagSuccessInfo", null, Locale.getDefault()));
			request.getRequestDispatcher(
					"/platform/search/detailView.htm?rsdaId="
							+ ServletRequestUtils.getLongParameter(request, "rsdaId")).forward(
					request, response);
		}
	}

	public IResTagInfoService getResTagInfoService() {
		return resTagInfoService;
	}

	public void setResTagInfoService(IResTagInfoService resTagInfoService) {
		this.resTagInfoService = resTagInfoService;
	}

	public ResourceBundleMessageSource getMessageSource() {
		return messageSource;
	}

	public void setMessageSource(ResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}
	
}
