package cn.com.adtech.riss2.controller.platform.rss;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.rss.RssCreator;
import cn.com.adtech.riss2.rss.RssUtil;
import cn.com.adtech.riss2.rss.core.RssModel;

import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * 资源RSS
 * @author MaChao
 *
 */
public class ResSourceDataRssFeedController extends AbstractController {
	// RSS条目
	private static final int RSS_INDEX_RESSOURCE_FEED_COUNT = 100;
	// RSS描述
	private static final String RSS_RESOURCE_DESC = "最新资源";
	// RSS标题连接
	private static final String RSS_RESSOURCE_LINK_URL = "";
	// RSS标题
	private static final String RSS_RESSOURCE_TITLE = "最新资源";
	// 连接
	private static final String RSS_RESSOURCE_ENTRY_LINK = "platform/search/detailView.htm?rsdaId=";
	
	private IResourceDataService resourceDataService;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		final String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		List<ResSourceData> result = resourceDataService.getFeedResSourceDatas(RSS_INDEX_RESSOURCE_FEED_COUNT);
		RssModel rssModel = buildRssModel(result, basePath);
		SyndFeed feed = RssCreator.createSynFeed(rssModel);
		response.setContentType("text/xml;charset=UTF-8");
		PrintWriter writer = response.getWriter();
		SyndFeedOutput output = new SyndFeedOutput();
		output.output(feed, writer);
		writer.flush();
		return null;
	}

	private RssModel buildRssModel(List<ResSourceData> result, final String basePath) {
		RssModel rssModel = new RssModel();
		rssModel.setDescription(RSS_RESOURCE_DESC);
		rssModel.setFeedType("rss_2.0");
		rssModel.setLinkUrl(RSS_RESSOURCE_LINK_URL);
		rssModel.setTitle(RSS_RESSOURCE_TITLE);
		List<SyndEntry> entries = RssUtil.buildRssFeedEntry(result, basePath + RSS_RESSOURCE_ENTRY_LINK);
		rssModel.setEntries(entries);
		return rssModel;
	}

	public IResourceDataService getResourceDataService() {
		return resourceDataService;
	}

	public void setResourceDataService(IResourceDataService resourceDataService) {
		this.resourceDataService = resourceDataService;
	}

}
