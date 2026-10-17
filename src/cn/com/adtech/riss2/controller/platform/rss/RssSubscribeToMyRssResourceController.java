package cn.com.adtech.riss2.controller.platform.rss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.com.adtech.riss2.bso.rss.IRssUserDataService;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;

/**
 * 订阅到我的RSS资源
 * @author MaChao
 *
 */
public class RssSubscribeToMyRssResourceController extends AbstractController {
	
	// rss地址
	public static final String RSS_BASE_PATH = "rss/metastoreRssChannel.htm?metaStoreId=";
	
	private IRssUserDataService rssUserDataService;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		// 获取登录用户ID
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		} else {
			Long metaStoreId = ServletRequestUtils.getLongParameter(request, "metaStoreId");
			if(metaStoreId != null) {
				String path = basePath + RSS_BASE_PATH + metaStoreId;
				Long rssDataId = this.rssUserDataService.addToMyRssData(metaStoreId, path, userinfo);
				if(rssDataId == null){
					request.setAttribute("repeat", "true");
				}else {
					request.setAttribute("success", "true");
				}
			}
			request.getRequestDispatcher("/rss/rssChannels.htm").forward(request, response);
		}
		return null;
	}

	public IRssUserDataService getRssUserDataService() {
		return rssUserDataService;
	}

	public void setRssUserDataService(IRssUserDataService rssUserDataService) {
		this.rssUserDataService = rssUserDataService;
	}

}
