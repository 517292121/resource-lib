package cn.com.adtech.riss2.controller.platform.rss;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;

/**
 * rss频道聚合
 * @author MaChao
 *
 */
public class RssChannelsController extends AbstractController {
	
	private ISysUserPlatformService sysUserPlatformService;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest arg0, HttpServletResponse arg1)
			throws Exception {
		Map<String, Object> params = new HashMap<String, Object>();
		List<ResMetaStore> result = this.sysUserPlatformService.getMetaStoreList();
		params.put("result", result);
		return new ModelAndView("/platform/rssChannels", params);
	}

	public ISysUserPlatformService getSysUserPlatformService() {
		return sysUserPlatformService;
	}

	public void setSysUserPlatformService(ISysUserPlatformService sysUserPlatformService) {
		this.sysUserPlatformService = sysUserPlatformService;
	}
	

}
