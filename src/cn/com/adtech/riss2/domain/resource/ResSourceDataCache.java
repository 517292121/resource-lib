package cn.com.adtech.riss2.domain.resource;

import java.util.Date;

/**
 * 首页资源显示缓存表
 * 
 * @author MaChao
 */
public class ResSourceDataCache {
	private Long rsdcId;

	private String rsdcTitle;

	private String rsdcCreator;

	private String rsdcPublisher;

	private String rsdcDescription;

	private Date rsdcBuildDate;

	private Long rsdcLookNum;

	private Long rsdcRcmd;

	private Long rsdcReviewNum;

	private Long rsdcRltyId;

	private String rsdcRltyName;

	private Integer rsdcRltyType;

	private String rsdcIconType;

	private String rsdcBigimgUrl;

	private String rsdcFileAddress;

	public ResSourceDataCache() {
		super();
	}

	public Long getRsdcId() {
		return rsdcId;
	}

	public void setRsdcId(Long rsdcId) {
		this.rsdcId = rsdcId;
	}

	public String getRsdcTitle() {
		return rsdcTitle;
	}

	public void setRsdcTitle(String rsdcTitle) {
		this.rsdcTitle = rsdcTitle;
	}

	public String getRsdcCreator() {
		return rsdcCreator;
	}

	public void setRsdcCreator(String rsdcCreator) {
		this.rsdcCreator = rsdcCreator;
	}

	public String getRsdcPublisher() {
		return rsdcPublisher;
	}

	public void setRsdcPublisher(String rsdcPublisher) {
		this.rsdcPublisher = rsdcPublisher;
	}

	public String getRsdcDescription() {
		return rsdcDescription;
	}

	public void setRsdcDescription(String rsdcDescription) {
		this.rsdcDescription = rsdcDescription;
	}

	public Date getRsdcBuildDate() {
		return rsdcBuildDate;
	}

	public void setRsdcBuildDate(Date rsdcBuildDate) {
		this.rsdcBuildDate = rsdcBuildDate;
	}

	public Long getRsdcLookNum() {
		return rsdcLookNum;
	}

	public void setRsdcLookNum(Long rsdcLookNum) {
		this.rsdcLookNum = rsdcLookNum;
	}

	public Long getRsdcRcmd() {
		return rsdcRcmd;
	}

	public void setRsdcRcmd(Long rsdcRcmd) {
		this.rsdcRcmd = rsdcRcmd;
	}

	public Long getRsdcReviewNum() {
		return rsdcReviewNum;
	}

	public void setRsdcReviewNum(Long rsdcReviewNum) {
		this.rsdcReviewNum = rsdcReviewNum;
	}

	public Long getRsdcRltyId() {
		return rsdcRltyId;
	}

	public void setRsdcRltyId(Long rsdcRltyId) {
		this.rsdcRltyId = rsdcRltyId;
	}

	public String getRsdcRltyName() {
		return rsdcRltyName;
	}

	public void setRsdcRltyName(String rsdcRltyName) {
		this.rsdcRltyName = rsdcRltyName;
	}

	public Integer getRsdcRltyType() {
		return rsdcRltyType;
	}

	public void setRsdcRltyType(Integer rsdcRltyType) {
		this.rsdcRltyType = rsdcRltyType;
	}

	public String getRsdcIconType() {
		return rsdcIconType;
	}

	public void setRsdcIconType(String rsdcIconType) {
		this.rsdcIconType = rsdcIconType;
	}

	public String getRsdcBigimgUrl() {
		return rsdcBigimgUrl;
	}

	public void setRsdcBigimgUrl(String rsdcBigimgUrl) {
		this.rsdcBigimgUrl = rsdcBigimgUrl;
	}

	public String getRsdcFileAddress() {
		return rsdcFileAddress;
	}

	public void setRsdcFileAddress(String rsdcFileAddress) {
		this.rsdcFileAddress = rsdcFileAddress;
	}
}
