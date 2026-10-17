package cn.com.adtech.riss2.domain;

@SuppressWarnings("serial")
public class SysTouristBrowseCache implements java.io.Serializable {
	// Fields
	private Long stbhcId;

	private String stbhcKey;

	private Long stbhcValue;

	// Constructors
	/** default constructor */
	public SysTouristBrowseCache() {}

	// Property accessors
	public Long getStbhcId() {
		return this.stbhcId;
	}

	public void setStbhcId(Long stbhcId) {
		this.stbhcId = stbhcId;
	}

	public String getStbhcKey() {
		return this.stbhcKey;
	}

	public void setStbhcKey(String stbhcKey) {
		this.stbhcKey = stbhcKey;
	}

	public Long getStbhcValue() {
		return this.stbhcValue;
	}

	public void setStbhcValue(Long stbhcValue) {
		this.stbhcValue = stbhcValue;
	}
}