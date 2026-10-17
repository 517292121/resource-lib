package cn.com.adtech.riss2.domain.resource;

/**
 * ResSourceSubject 资源主题，目的是将一组相关资源按照一个主题的形式组合在一起.
 * 
 * @author MaChao
 */
public class ResSourceSubject implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Fields
	private Long rssuId;

	private Long rssuRminId;

	private String rssuName;

	private String rssuRemark;

	// Constructors
	/** default constructor */
	public ResSourceSubject() {}

	/** full constructor */
	// Property accessors
	public Long getRssuId() {
		return this.rssuId;
	}

	public void setRssuId(Long rssuId) {
		this.rssuId = rssuId;
	}

	public Long getRssuRminId() {
		return this.rssuRminId;
	}

	public void setRssuRminId(Long rssuRminId) {
		this.rssuRminId = rssuRminId;
	}

	public String getRssuName() {
		return this.rssuName;
	}

	public void setRssuName(String rssuName) {
		this.rssuName = rssuName;
	}

	public String getRssuRemark() {
		return this.rssuRemark;
	}

	public void setRssuRemark(String rssuRemark) {
		this.rssuRemark = rssuRemark;
	}
}
