package cn.com.adtech.riss2.domain;

import java.util.Date;

@SuppressWarnings("serial")
public class SysForgetPassword implements java.io.Serializable {
	// Fields
	private Long sfpaId;

	private Long sfpaSuinId;

	private Date sfpaDate;

	private Long sfpaKey;

	// Constructors
	/** default constructor */
	public SysForgetPassword() {}

	/** full constructor */
	public SysForgetPassword(Long sfpaSuinId, Date sfpaDate, Long sfpaKey) {
		this.sfpaSuinId = sfpaSuinId;
		this.sfpaDate = sfpaDate;
		this.sfpaKey = sfpaKey;
	}

	// Property accessors
	public Long getSfpaId() {
		return this.sfpaId;
	}

	public void setSfpaId(Long sfpaId) {
		this.sfpaId = sfpaId;
	}

	public Long getSfpaSuinId() {
		return this.sfpaSuinId;
	}

	public void setSfpaSuinId(Long sfpaSuinId) {
		this.sfpaSuinId = sfpaSuinId;
	}

	public Date getSfpaDate() {
		return this.sfpaDate;
	}

	public void setSfpaDate(Date sfpaDate) {
		this.sfpaDate = sfpaDate;
	}

	public Long getSfpaKey() {
		return this.sfpaKey;
	}

	public void setSfpaKey(Long sfpaKey) {
		this.sfpaKey = sfpaKey;
	}
}