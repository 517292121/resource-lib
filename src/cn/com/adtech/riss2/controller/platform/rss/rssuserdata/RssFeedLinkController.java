package cn.com.adtech.riss2.controller.platform.rss.rssuserdata;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import com.sun.syndication.feed.synd.SyndEntry;

import cn.com.adtech.riss2.bso.rss.IRssUserDataService;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.rss.RssUserData;
import cn.com.adtech.riss2.jdbcms.ToolUnit;
import cn.com.adtech.riss2.rss.RssFeedReader;

/**
 * RSS资源连接
 * @author MaChao
 *
 */
public class RssFeedLinkController extends AbstractController {

	private IRssUserDataService rssUserDataService;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long rssFeedId = ServletRequestUtils.getLongParameter(request, "rssFeedId");
//		String showType = ServletRequestUtils.getStringParameter(request, "showType", "grid");
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		Map<String, Object> param = new HashMap<String, Object>();
		StringBuilder sb = new StringBuilder();
		if(user == null) {
//			response.sendRedirect(request.getContextPath() + "/index.htm");
			response.getWriter().write("{}");
			response.getWriter().flush();
			return null;
		} else {
			RssUserData rss = (RssUserData) this.rssUserDataService.get(RssUserData.class, rssFeedId);
			if(rss != null) {
//				param.put("showType", showType);
				param.put("rss", rss);
				List<SyndEntry> result = null;
				try {
					result = RssFeedReader.feedReader(rss.getRssDataSourceUrl(), rss.getRssDataDisplayNum());
				} catch (Exception e) {
					response.getWriter().write("error");
					return null;
				}
				sb.append("[");
				if(result != null && result.size() > 0) {
					for(int i = 0; i < result.size(); i++) {
						SyndEntry entry = result.get(i);
						sb.append("{").append("\"title\":").append("\"").append(ToolUnit.stringToJson(entry.getTitle())).append("\",").
//							append("\"desc\":").append("\"").append(ToolUnit.stringToJson(entry.getDescription().getValue())).append("\",").
							append("\"publishDate\":").append("\"").append(ConstantsObj.FORMAT_DATE_TIME.format(entry.getPublishedDate())).append("\",").
							append("\"link\":").append("\"").append(entry.getLink()).append("\"").
							append("},");;
					}
					sb.deleteCharAt(sb.length() - 1);
				}
				sb.append("]");
				response.getWriter().write(sb.toString());
				response.getWriter().flush();
				return null;
//				param.put("result", RssFeedReader.feedReader(rss.getRssDataSourceUrl(), rss.getRssDataDisplayNum()));
			}
		}
		return null;
//		return new ModelAndView("/platform/userCenterRssDataDisplay", param);
	}

	public IRssUserDataService getRssUserDataService() {
		return rssUserDataService;
	}

	public void setRssUserDataService(IRssUserDataService rssUserDataService) {
		this.rssUserDataService = rssUserDataService;
	}
}
