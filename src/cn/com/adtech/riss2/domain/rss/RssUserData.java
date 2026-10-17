package cn.com.adtech.riss2.domain.rss;

import java.io.Serializable;
import java.util.Date;

import cn.com.adtech.riss2.domain.SysUserinfo;

/**
 * 用户的RSS订阅信息
 * 
 * @author MaChao
 */
@SuppressWarnings("serial")
public class RssUserData implements Serializable {
	// 默认显示的条目
	public static final Integer RSS_DATA_DEFAULT_DISPLAY_NUM = 40;
	// 默认排序序号
	public static final Integer RSS_DATA_DFAULT_ORDER = 1;
	// 特色库订阅时默认排序号
	public static final Integer RSS_DATA_METASTORE_ORDER = 200;

	/** id */
	private Long rssDataId;

	/** rss连接名字 */
	private String rssDataName;

	/** rss连接地址 */
	private String rssDataSourceUrl;

	/** user id */
	private Long rssDataUserId;

	/** user */
	private SysUserinfo rssDataUser;

	/** rss data添加时间 */
	private Date rssDataAddDate;

	/** rss data最后一次修改时间 */
	private Date rssDataLastModifyDate;

	/** 要显示的信息条数 */
	private Integer rssDataDisplayNum = RSS_DATA_DEFAULT_DISPLAY_NUM;

	/** 排序 */
	private Integer rssDataOrder = RSS_DATA_DFAULT_ORDER;
	
	public RssUserData() {}

	public RssUserData(String rssDataName, String rssDataSourceUrl, 
			Long rssDataUserId, Date rssDataAddDate, Date rssDataLastModifyDate) {
		this.rssDataName = rssDataName;
		this.rssDataSourceUrl = rssDataSourceUrl;
		this.rssDataUserId = rssDataUserId;
		this.rssDataAddDate = rssDataAddDate;
		this.rssDataLastModifyDate = rssDataLastModifyDate;
		//this.rssDataOrder = rssDataOrder;
	}

	public RssUserData(Long rssDataId, String rssDataName, String rssDataSourceUrl, Long rssDataUserId, SysUserinfo rssDataUser,
			Date rssDataAddDate, Date rssDataLastModifyDate, Integer rssDataDisplayNum, Integer rssDataOrder) {
		this.rssDataId = rssDataId;
		this.rssDataName = rssDataName;
		this.rssDataSourceUrl = rssDataSourceUrl;
		this.rssDataUserId = rssDataUserId;
		this.rssDataUser = rssDataUser;
		this.rssDataAddDate = rssDataAddDate;
		this.rssDataLastModifyDate = rssDataLastModifyDate;
		this.rssDataDisplayNum = rssDataDisplayNum;
		this.rssDataOrder = rssDataOrder;
	}

	public Long getRssDataId() {
		return rssDataId;
	}

	public void setRssDataId(Long rssDataId) {
		this.rssDataId = rssDataId;
	}

	public String getRssDataName() {
		return rssDataName;
	}

	public void setRssDataName(String rssDataName) {
		this.rssDataName = rssDataName;
	}

	public String getRssDataSourceUrl() {
		return rssDataSourceUrl;
	}

	public void setRssDataSourceUrl(String rssDataSourceUrl) {
		this.rssDataSourceUrl = rssDataSourceUrl;
	}

	public Long getRssDataUserId() {
		return rssDataUserId;
	}

	public void setRssDataUserId(Long rssDataUserId) {
		this.rssDataUserId = rssDataUserId;
	}

	public SysUserinfo getRssDataUser() {
		return rssDataUser;
	}

	public void setRssDataUser(SysUserinfo rssDataUser) {
		this.rssDataUser = rssDataUser;
	}

	public Date getRssDataAddDate() {
		return rssDataAddDate;
	}

	public void setRssDataAddDate(Date rssDataAddDate) {
		this.rssDataAddDate = rssDataAddDate;
	}

	public Date getRssDataLastModifyDate() {
		return rssDataLastModifyDate;
	}

	public void setRssDataLastModifyDate(Date rssDataLastModifyDate) {
		this.rssDataLastModifyDate = rssDataLastModifyDate;
	}

	public Integer getRssDataDisplayNum() {
		return rssDataDisplayNum;
	}

	public void setRssDataDisplayNum(Integer rssDataDisplayNum) {
		this.rssDataDisplayNum = rssDataDisplayNum;
	}

	public Integer getRssDataOrder() {
		return rssDataOrder;
	}

	public void setRssDataOrder(Integer rssDataOrder) {
		this.rssDataOrder = rssDataOrder;
	}
}
