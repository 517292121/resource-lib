package cn.com.adtech.riss2.domain.resource;


/**
 * 学科分类
 *
 */
@SuppressWarnings("serial")
public class ResSubjectType implements java.io.Serializable {
	// Fields
	private Long rstyId;

	private Long rstyPid;

	private String rstyName;

	private Long rstyGrade;

	private Integer rstyIndex; // 排序列

	private String rstyCode; //学科编号
	
	public String getRstyCode() {
		return rstyCode;
	}

	public void setRstyCode(String rstyCode) {
		this.rstyCode = rstyCode;
	}

	public Integer getRstyIndex() {
		return rstyIndex;
	}

	public void setRstyIndex(Integer rstyIndex) {
		this.rstyIndex = rstyIndex;
	}

	public ResSubjectType(Long rstyId, Long rstyPid, String rstyName) {
		this.rstyId = rstyId;
		this.rstyPid = rstyPid;
		this.rstyName = rstyName;
	}

	// Constructors
	public ResSubjectType() {}

	// Property accessors
	public Long getRstyId() {
		return this.rstyId;
	}

	public void setRstyId(Long rstyId) {
		this.rstyId = rstyId;
	}

	public Long getRstyPid() {
		return this.rstyPid;
	}

	public void setRstyPid(Long rstyPid) {
		this.rstyPid = rstyPid;
	}

	public String getRstyName() {
		return this.rstyName;
	}

	public void setRstyName(String rstyName) {
		this.rstyName = rstyName;
	}

	public Long getRstyGrade() {
		return this.rstyGrade;
	}

	public void setRstyGrade(Long rstyGrade) {
		this.rstyGrade = rstyGrade;
	}
}