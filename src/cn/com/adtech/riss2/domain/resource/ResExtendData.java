package cn.com.adtech.riss2.domain.resource;

@SuppressWarnings("serial")
public class ResExtendData implements java.io.Serializable {
	// Fields
	private Long redaId;

	private ResElementPool resElementPool;
	private Long  redaRepoId;
	
	private Long redaRsdaId;
	private ResSourceData resSourceData;

	private String redaData;
	
	
	private Long redaLanguage;

	public Long getRedaLanguage() {
		return redaLanguage;
	}

	public void setRedaLanguage(Long redaLanguage) {
		this.redaLanguage = redaLanguage;
	}

	// Constructors
	/** default constructor */
	public ResExtendData() {}
	/**导出CALIS标准使用**/
	public ResExtendData(String redaData,ResElementPool resElementPool) {
		this.redaData = redaData;
		this.resElementPool = resElementPool;
	}
	
	public ResExtendData(Long rsdaId, Long languageId, Long repoId, String data) {
		this.redaRsdaId = rsdaId;
		this.redaLanguage = languageId;
		this.redaRepoId = repoId;
		this.redaData = data;
	}

	public Long getRedaId() {
		return redaId;
	}

	public void setRedaId(Long redaId) {
		this.redaId = redaId;
	}

	public ResElementPool getResElementPool() {
		return resElementPool;
	}

	public void setResElementPool(ResElementPool resElementPool) {
		this.resElementPool = resElementPool;
	}

	public Long getRedaRepoId() {
		return redaRepoId;
	}

	public void setRedaRepoId(Long redaRepoId) {
		this.redaRepoId = redaRepoId;
	}

	public Long getRedaRsdaId() {
		return redaRsdaId;
	}

	public void setRedaRsdaId(Long redaRsdaId) {
		this.redaRsdaId = redaRsdaId;
	}

	public ResSourceData getResSourceData() {
		return resSourceData;
	}

	public void setResSourceData(ResSourceData resSourceData) {
		this.resSourceData = resSourceData;
	}

	public String getRedaData() {
		return redaData;
	}

	public void setRedaData(String redaData) {
		this.redaData = redaData;
	}

}