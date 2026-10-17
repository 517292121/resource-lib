package cn.com.adtech.riss2.controller.platform.rss;

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.com.adtech.riss2.bso.resource.IMetadataManagementService;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.rss.RssCreator;
import cn.com.adtech.riss2.rss.RssUtil;
import cn.com.adtech.riss2.rss.core.RssModel;

import com.jbetter.framework.session.Pagination;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedOutput;

/**
 * 某个特色库的rss资源聚合
 * @author MaChao
 *
 */
public class MetaStoreRssResourceController extends AbstractController {
	// RSS中资源数量
	private final int RSS_METASTORE_RESOURCES_COUNT = 100;
	// 连接
	public static final String RSS_RESSOURCE_ENTRY_LINK = "platform/search/detailView.htm?rsdaId=";
	
	private IMetadataManagementService metadataManagementService;
	
	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long metaStoreId = ServletRequestUtils.getLongParameter(request, "metaStoreId");
		if(metaStoreId != null) {
			final String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
			Pagination pagination = new Pagination();
			pagination.setPageResults(RSS_METASTORE_RESOURCES_COUNT);
			pagination.setCurrentPage(0);
			ResMetaStore metaData = (ResMetaStore) this.metadataManagementService.get(ResMetaStore.class, metaStoreId);
			List<ResSourceData> result = this.metadataManagementService.getAllRessourcesByMetastoreId(metaStoreId, pagination);
			RssModel rssModel = buildRssModel(result, metaData, basePath);
			SyndFeed feed = RssCreator.createSynFeed(rssModel);
			response.setContentType("text/xml;charset=UTF-8");
			PrintWriter writer = response.getWriter();
			SyndFeedOutput output = new SyndFeedOutput();
			output.output(feed, writer);
			writer.flush();
			return null;
		}
		return null;
	}
	
	private RssModel buildRssModel(List<ResSourceData> result, ResMetaStore metaData, final String basePath) {
		RssModel rssModel = new RssModel();
		rssModel.setDescription(metaData.getRmstName());
		rssModel.setFeedType("rss_2.0");
		rssModel.setLinkUrl(basePath);
		rssModel.setTitle(metaData.getRmstName());
		List<SyndEntry> entries = RssUtil.buildRssFeedEntry(result, basePath + RSS_RESSOURCE_ENTRY_LINK);
		rssModel.setEntries(entries);
		return rssModel;
	}
	
	public IMetadataManagementService getMetadataManagementService() {
		return metadataManagementService;
	}

	public void setMetadataManagementService(IMetadataManagementService metadataManagementService) {
		this.metadataManagementService = metadataManagementService;
	}

	
}
