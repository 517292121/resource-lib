package cn.com.adtech.riss2.rss;

import java.util.LinkedList;
import java.util.List;

import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.rss.core.RssModel;

import com.sun.syndication.feed.synd.SyndContent;
import com.sun.syndication.feed.synd.SyndContentImpl;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndEntryImpl;

/**
 * rss工具
 * @author MaChao
 *
 */
public class RssUtil {

	private RssUtil() {}
	
	/**
	 * 根据ResSourcesData构造List<SyndEntry>
	 */ 
	public static List<SyndEntry> buildRssFeedEntry(List<ResSourceData> result, String rssResourceEntryBaseLink) {
		List<SyndEntry> ret = new LinkedList<SyndEntry>();
		if(result != null && result.size() > 0) {
			for (int j = 0; j < result.size(); j++) {
				ResSourceData res = result.get(j);
				SyndEntry entry = new SyndEntryImpl();
				SyndContent description = new SyndContentImpl();
				description.setType("text/xml");
				description.setValue(res.getRsdaDescription());
				entry.setDescription(description);
				entry.setTitle(res.getRsdaTitle());
				entry.setLink(rssResourceEntryBaseLink + res.getRsdaId());
				entry.setPublishedDate(res.getRsdaBuilddate());
				ret.add(entry);
			}
		}
		return ret;
	}
}
