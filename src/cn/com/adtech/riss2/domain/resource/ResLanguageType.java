package cn.com.adtech.riss2.domain.resource;

/**
 * 语种分类
 * 
 * @author MaChao
 * @date 2010-9-21
 */
@SuppressWarnings("serial")
public class ResLanguageType implements java.io.Serializable {
	// Fields
	private Long rltyId;

	private String rltyName;

	// Constructors
	/** default constructor */
	public ResLanguageType() {}

	/** full constructor */
	public ResLanguageType(String rltyName) {
		this.rltyName = rltyName;
	}

	// Property accessors
	public Long getRltyId() {
		return this.rltyId;
	}

	public void setRltyId(Long rltyId) {
		this.rltyId = rltyId;
	}

	public String getRltyName() {
		return this.rltyName;
	}

	public void setRltyName(String rltyName) {
		this.rltyName = rltyName;
	}
}
