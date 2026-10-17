package cn.com.adtech.riss2.rss.core;

import java.io.Serializable;
import java.util.List;

/**
 * rss内容模型
 * @author MaChao
 *
 */
public class RssModel implements Serializable {
	
	public static final String DEFAULT_FEED_TYPE = "rss_2.0";
	public static final String DEFAULT_LANGUAGE = "zh-cn";
	
	private String title;
	private String feedType = DEFAULT_FEED_TYPE;
	private String linkUrl;
	private String language = DEFAULT_LANGUAGE;
	private String description;
	private String encoding;
	private List entries;
    private List modules;
    private List authors;
    private List contributors;
    private List foreignMarkup;
    
	public RssModel() {
	}

	public RssModel(String title, String feedType, String linkUrl, String language,
			String description, String encoding, List entries, List modules, List authors,
			List contributors, List foreignMarkup) {
		super();
		this.title = title;
		this.feedType = feedType;
		this.linkUrl = linkUrl;
		this.language = language;
		this.description = description;
		this.encoding = encoding;
		this.entries = entries;
		this.modules = modules;
		this.authors = authors;
		this.contributors = contributors;
		this.foreignMarkup = foreignMarkup;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFeedType() {
		return feedType;
	}

	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public List getEntries() {
		return entries;
	}

	public void setEntries(List entries) {
		this.entries = entries;
	}

	public List getModules() {
		return modules;
	}

	public void setModules(List modules) {
		this.modules = modules;
	}

	public List getAuthors() {
		return authors;
	}

	public void setAuthors(List authors) {
		this.authors = authors;
	}

	public List getContributors() {
		return contributors;
	}

	public void setContributors(List contributors) {
		this.contributors = contributors;
	}

	public List getForeignMarkup() {
		return foreignMarkup;
	}

	public void setForeignMarkup(List foreignMarkup) {
		this.foreignMarkup = foreignMarkup;
	}
	
}
