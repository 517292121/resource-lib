package cn.com.adtech.riss2.domain.sc;


/**
 * FAQ 分类
 * 
 * @author MaChao
 * @date 2010-11-4
 */
@SuppressWarnings("serial")
public class ScFaqType implements java.io.Serializable {
	// Fields
	private Long scftId;

	private ScFaqType scFaqType;
	
	private Long scftParentId;

	private String scftName;

	private Long scftGrade;

	private Long scftIndex;

	public Long getScftGrade() {
		return scftGrade;
	}

	public void setScftGrade(Long scftGrade) {
		this.scftGrade = scftGrade;
	}

	public Long getScftIndex() {
		return scftIndex;
	}

	public void setScftIndex(Long scftIndex) {
		this.scftIndex = scftIndex;
	}

	// Constructors
	/** default constructor */
	public ScFaqType() {}

	// Property accessors
	public Long getScftId() {
		return this.scftId;
	}

	public void setScftId(Long scftId) {
		this.scftId = scftId;
	}

	public ScFaqType getScFaqType() {
		return this.scFaqType;
	}

	public void setScFaqType(ScFaqType scFaqType) {
		this.scFaqType = scFaqType;
	}

	public String getScftName() {
		return this.scftName;
	}

	public void setScftName(String scftName) {
		this.scftName = scftName;
	}

	public Long getScftParentId() {
		return scftParentId;
	}

	public void setScftParentId(Long scftParentId) {
		this.scftParentId = scftParentId;
	}
}
