package cn.com.adtech.riss2.domain.resource;

/**
 * 热门检索词
 * 
 * @author MaChao
 */

public class ResIndexKey implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long rikeId;
	private String rikeName;
	private Integer rikeSearchCount;

	// Constructors

	/** default constructor */
	public ResIndexKey() {
	}

	public Long getRikeId() {
		return this.rikeId;
	}

	public void setRikeId(Long rikeId) {
		this.rikeId = rikeId;
	}

	public String getRikeName() {
		return this.rikeName;
	}

	public void setRikeName(String rikeName) {
		this.rikeName = rikeName;
	}

	public Integer getRikeSearchCount() {
		return this.rikeSearchCount;
	}

	public void setRikeSearchCount(Integer rikeSearchCount) {
		this.rikeSearchCount = rikeSearchCount;
	}

}
