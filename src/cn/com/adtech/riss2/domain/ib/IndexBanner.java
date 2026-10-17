package cn.com.adtech.riss2.domain.ib;

/**
 * IndexBanner entity.
 * 
 * @author MaChao
 */

public class IndexBanner implements java.io.Serializable {

	// Fields

	private Long ibId;
	private String ibName;
	private String ibDescription;
	private String ibUrl;
	private String ibImgUrl;
	private Long ibRmstId;
	private int ibIndex;
	private String ibBgColor;
	private Long ibIsShow;

	// Constructors

	/** default constructor */
	public IndexBanner() {
	}

	/** full constructor */
	public IndexBanner(String ibName, String ibDescription, String ibUrl,
			Long ibRmstId, int ibIndex, String ibBgColor, Long ibIsShow) {
		this.ibName = ibName;
		this.ibDescription = ibDescription;
		this.ibUrl = ibUrl;
		this.ibRmstId = ibRmstId;
		this.ibIndex = ibIndex;
		this.ibBgColor = ibBgColor;
		this.ibIsShow = ibIsShow;
	}

	// Property accessors

	public Long getIbId() {
		return this.ibId;
	}

	public void setIbId(Long ibId) {
		this.ibId = ibId;
	}

	public String getIbName() {
		return this.ibName;
	}

	public void setIbName(String ibName) {
		this.ibName = ibName;
	}

	public String getIbDescription() {
		return this.ibDescription;
	}

	public void setIbDescription(String ibDescription) {
		this.ibDescription = ibDescription;
	}

	public String getIbUrl() {
		return this.ibUrl;
	}

	public void setIbUrl(String ibUrl) {
		this.ibUrl = ibUrl;
	}

	public Long getIbRmstId() {
		return this.ibRmstId;
	}

	public void setIbRmstId(Long ibRmstId) {
		this.ibRmstId = ibRmstId;
	}

	public int getIbIndex() {
		return this.ibIndex;
	}

	public void setIbIndex(int ibIndex) {
		this.ibIndex = ibIndex;
	}

	public String getIbBgColor() {
		return this.ibBgColor;
	}

	public void setIbBgColor(String ibBgColor) {
		this.ibBgColor = ibBgColor;
	}

	public Long getIbIsShow() {
		return this.ibIsShow;
	}

	public void setIbIsShow(Long ibIsShow) {
		this.ibIsShow = ibIsShow;
	}

	public String getIbImgUrl() {
		return ibImgUrl;
	}

	public void setIbImgUrl(String ibImgUrl) {
		this.ibImgUrl = ibImgUrl;
	}

}
