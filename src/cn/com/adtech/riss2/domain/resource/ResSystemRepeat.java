package cn.com.adtech.riss2.domain.resource;

/**
 * 系统资源查重结果
 * 
 * @author MaChao
 * 
 * @date 2011-08-25
 */

public class ResSystemRepeat implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Fields

	private Long rsyrId;
	private Long rsyrRsdaId;
	private String rsyrRsdaTitle;
	private Integer rsyrRepeatnum;
	
	//资源对象，主要用于查询
	private ResSourceData sourceData;

	// Constructors

	/** default constructor */
	public ResSystemRepeat() {
	}

	/** full constructor */

	public Long getRsyrId() {
		return this.rsyrId;
	}

	public void setRsyrId(Long rsyrId) {
		this.rsyrId = rsyrId;
	}

	public Long getRsyrRsdaId() {
		return this.rsyrRsdaId;
	}

	public void setRsyrRsdaId(Long rsyrRsdaId) {
		this.rsyrRsdaId = rsyrRsdaId;
	}

	public String getRsyrRsdaTitle() {
		return this.rsyrRsdaTitle;
	}

	public void setRsyrRsdaTitle(String rsyrRsdaTitle) {
		this.rsyrRsdaTitle = rsyrRsdaTitle;
	}

	public Integer getRsyrRepeatnum() {
		return this.rsyrRepeatnum;
	}

	public void setRsyrRepeatnum(Integer rsyrRepeatnum) {
		this.rsyrRepeatnum = rsyrRepeatnum;
	}

	public ResSourceData getSourceData() {
		return sourceData;
	}

	public void setSourceData(ResSourceData sourceData) {
		this.sourceData = sourceData;
	}

}
