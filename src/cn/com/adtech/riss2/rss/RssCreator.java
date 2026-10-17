package cn.com.adtech.riss2.rss;

import cn.com.adtech.riss2.rss.core.RssModel;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndFeedImpl;

/**
 * rss生成工具
 * @author MaChao
 *
 */
public class RssCreator {
	
	public static SyndFeed createSynFeed(RssModel model) {
		SyndFeed feed = new SyndFeedImpl();
		feed.setAuthors(model.getAuthors());
		feed.setContributors(model.getContributors());
		feed.setDescription(model.getDescription());
		feed.setEncoding(model.getEncoding());
		feed.setEntries(model.getEntries());
		feed.setFeedType(model.getFeedType());
		feed.setLanguage(model.getLanguage());
		feed.setTitle(model.getTitle());
		feed.setLink(model.getLinkUrl());
		return feed;
	} 

}
