package cn.com.adtech.riss2.domain;

@SuppressWarnings("serial")
public class SysEmailActivation implements java.io.Serializable {
	// Fields
	private Long seacId;

	private SysUserinfo sysUserinfo;

	private Long seacSuinId;

	private Long seacKey;

	// Constructors
	/** default constructor */
	public SysEmailActivation() {}

	// Property accessors
	public Long getSeacId() {
		return this.seacId;
	}

	public void setSeacId(Long seacId) {
		this.seacId = seacId;
	}

	public Long getSeacKey() {
		return this.seacKey;
	}

	public void setSeacKey(Long seacKey) {
		this.seacKey = seacKey;
	}

	public SysUserinfo getSysUserinfo() {
		return sysUserinfo;
	}

	public void setSysUserinfo(SysUserinfo sysUserinfo) {
		this.sysUserinfo = sysUserinfo;
	}

	public Long getSeacSuinId() {
		return seacSuinId;
	}

	public void setSeacSuinId(Long seacSuinId) {
		this.seacSuinId = seacSuinId;
	}
}