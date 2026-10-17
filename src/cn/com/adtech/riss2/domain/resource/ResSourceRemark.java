package cn.com.adtech.riss2.domain.resource;

/**
 * ResSourceRemark entity.
 * 
 * @author MaChao
 */

public class ResSourceRemark implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long rsreId;
	private Long rsreRsdaId;
	private String rsreRemark;

	// Constructors

	/** default constructor */
	public ResSourceRemark() {
	}

	// Property accessors

	public Long getRsreId() {
		return this.rsreId;
	}

	public void setRsreId(Long rsreId) {
		this.rsreId = rsreId;
	}

	public String getRsreRemark() {
		return this.rsreRemark;
	}

	public void setRsreRemark(String rsreRemark) {
		this.rsreRemark = rsreRemark;
	}

	public Long getRsreRsdaId() {
		return rsreRsdaId;
	}

	public void setRsreRsdaId(Long rsreRsdaId) {
		this.rsreRsdaId = rsreRsdaId;
	}

}
