package cn.com.adtech.riss2.domain;

/**
 * ResFilesystem entity.
 * 
 * @author MaChao
 */

public class ResFilesystem implements java.io.Serializable {

	// Fields

	private Long rfilId;
	private String rfilName;
	private String rfilAddress;
	private String rfilFtpHost;
	private String rfilFtpUsername;
	private String rfilFtpPassword;
	private Long rfilNum;

	// Constructors

	/** default constructor */
	public ResFilesystem() {
	}

	/** minimal constructor */
	public ResFilesystem(long rfilId, String rfilName) {
		this.rfilName = rfilName;
		this.rfilId = rfilId;
	}

	/** full constructor */
	public ResFilesystem(String rfilName, String rfilAddress,
			String rfilFtpHost, String rfilFtpUsername, String rfilFtpPassword, Long rfilNum) {
		this.rfilName = rfilName;
		this.rfilAddress = rfilAddress;
		this.rfilFtpHost = rfilFtpHost;
		this.rfilFtpUsername = rfilFtpUsername;
		this.rfilFtpPassword = rfilFtpPassword;
		this.rfilNum = rfilNum;
	}

	// Property accessors

	public Long getRfilId() {
		return this.rfilId;
	}

	public void setRfilId(Long rfilId) {
		this.rfilId = rfilId;
	}

	public String getRfilName() {
		return this.rfilName;
	}

	public void setRfilName(String rfilName) {
		this.rfilName = rfilName;
	}

	public String getRfilAddress() {
		return this.rfilAddress;
	}

	public void setRfilAddress(String rfilAddress) {
		this.rfilAddress = rfilAddress;
	}

	public String getRfilFtpHost() {
		return this.rfilFtpHost;
	}

	public void setRfilFtpHost(String rfilFtpHost) {
		this.rfilFtpHost = rfilFtpHost;
	}

	public String getRfilFtpUsername() {
		return this.rfilFtpUsername;
	}

	public void setRfilFtpUsername(String rfilFtpUsername) {
		this.rfilFtpUsername = rfilFtpUsername;
	}

	public String getRfilFtpPassword() {
		return this.rfilFtpPassword;
	}

	public void setRfilFtpPassword(String rfilFtpPassword) {
		this.rfilFtpPassword = rfilFtpPassword;
	}

	public Long getRfilNum() {
		return this.rfilNum;
	}

	public void setRfilNum(Long rfilNum) {
		this.rfilNum = rfilNum;
	}

}
