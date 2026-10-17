package cn.com.adtech.riss2.domain.resource;

/**
 * ResSourceLanguage entity.
 * 
 * @author MaChao
 */
public class ResSourceLanguage implements java.io.Serializable {
	/** RSLA_STATUS 通过 */
	public static final Long STATUS_PAST = 3l;

	/** 是否为第一语种 */
	public static final Boolean IS_FIRST_LANGUAGE = true;

	// Fields
	private Long rslaId;

	private ResSourceData resSourceData;

	private ResLanguageType resLanguageType;

	private String rslaUsername;

	private Long rslaUserId;

	private String rslaDate;

	private String rslaAuditing;

	// 2：待审核 3:审核通过 4:未通过
	private Long rslaStatus;

	private Boolean rslaIsfirst = true; // 是否为第一语种,1为是

	private Long resSourceId;

	private Long resLanguageId;
	
	private String rslaAuthorName;  //会员登录名
	
	private String rslCheckLoginname;// 审核者登录名
	
	private String rslCheckSuinname;// 审核者姓名
	
	public String getRslaAuthorName() {
		return rslaAuthorName;
	}

	public void setRslaAuthorName(String rslaAuthorName) {
		this.rslaAuthorName = rslaAuthorName;
	}

	// Constructors
	public Long getResSourceId() {
		return resSourceId;
	}

	public void setResSourceId(Long resSourceId) {
		this.resSourceId = resSourceId;
	}

	public Long getResLanguageId() {
		return resLanguageId;
	}

	public void setResLanguageId(Long resLanguageId) {
		this.resLanguageId = resLanguageId;
	}

	/** default constructor */
	public ResSourceLanguage() {}

	/** full constructor */
	public ResSourceLanguage(ResSourceData resSourceData, ResLanguageType resLanguageType, String rslaUsername, Long rslaUserId,
			String rslaDate, Long rslaStatus) {
		this.resSourceData = resSourceData;
		this.resLanguageType = resLanguageType;
		this.rslaUsername = rslaUsername;
		this.rslaUserId = rslaUserId;
		this.rslaDate = rslaDate;
		this.rslaStatus = rslaStatus;
	}

	// Property accessors
	public Long getRslaId() {
		return this.rslaId;
	}

	public void setRslaId(Long rslaId) {
		this.rslaId = rslaId;
	}

	public ResSourceData getResSourceData() {
		return this.resSourceData;
	}

	public void setResSourceData(ResSourceData resSourceData) {
		this.resSourceData = resSourceData;
	}

	public ResLanguageType getResLanguageType() {
		return this.resLanguageType;
	}

	public void setResLanguageType(ResLanguageType resLanguageType) {
		this.resLanguageType = resLanguageType;
	}

	public String getRslaUsername() {
		return this.rslaUsername;
	}

	public void setRslaUsername(String rslaUsername) {
		this.rslaUsername = rslaUsername;
	}

	public Long getRslaUserId() {
		return this.rslaUserId;
	}

	public void setRslaUserId(Long rslaUserId) {
		this.rslaUserId = rslaUserId;
	}

	public String getRslaDate() {
		return this.rslaDate;
	}

	public void setRslaDate(String rslaDate) {
		this.rslaDate = rslaDate;
	}

	public Long getRslaStatus() {
		return this.rslaStatus;
	}

	public void setRslaStatus(Long rslaStatus) {
		this.rslaStatus = rslaStatus;
	}

	public Boolean getRslaIsfirst() {
		return rslaIsfirst;
	}

	public void setRslaIsfirst(Boolean rslaIsfirst) {
		this.rslaIsfirst = rslaIsfirst;
	}

	public String getRslaAuditing() {
		return rslaAuditing;
	}

	public void setRslaAuditing(String rslaAuditing) {
		this.rslaAuditing = rslaAuditing;
	}

	public String getRslCheckLoginname() {
		return rslCheckLoginname;
	}

	public void setRslCheckLoginname(String rslCheckLoginname) {
		this.rslCheckLoginname = rslCheckLoginname;
	}

	public String getRslCheckSuinname() {
		return rslCheckSuinname;
	}

	public void setRslCheckSuinname(String rslCheckSuinname) {
		this.rslCheckSuinname = rslCheckSuinname;
	}
}
