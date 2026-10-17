package cn.com.adtech.riss2.domain.sc;

import cn.com.adtech.riss2.core.ServiceUtil;


/**
 * 求助分类
 * 
 * @author MaChao
 * @date 2010-11-1
 */
@SuppressWarnings("serial")
public class ScIssueType implements java.io.Serializable {
	// Fields
	private Long scitId;

	private String scitName;

	private Long scitIndex;

	private Long scitNumber;

	// Constructors
	/** default constructor */
	public ScIssueType() {}

	// Property accessors
	public Long getScitId() {
		return this.scitId;
	}

	public void setScitId(Long scitId) {
		this.scitId = scitId;
	}

	public String getScitName() {
		return this.scitName;
	}

	public void setScitName(String scitName) {
		this.scitName = scitName;
	}

	public Long getScitIndex() {
		return this.scitIndex;
	}

	public void setScitIndex(Long scitIndex) {
		this.scitIndex = scitIndex;
	}

	public Long getScitNumber() {
		return this.scitNumber;
	}

	public void setScitNumber(Long scitNumber) {
		this.scitNumber = scitNumber;
	}
	
	public String getSubScitName() {
		if(this.scitName.trim().length() > 7)
			return ServiceUtil.subString(this.scitName, 7, "");
		else
			return this.scitName;
	}
}
