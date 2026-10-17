package cn.com.adtech.riss2.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author MaChao */
@SuppressWarnings("serial")
public class MmSetting implements Serializable {

	/** identifier field */
	private Long msetId;

	/** persistent field */
	private String msetName;

	/** nullable persistent field */
	private String msetTitle;

	/** persistent field */
	private String msetValue;

	/** persistent field */
	private Short msetType;

	/** full constructor */
	public MmSetting(Long msetId, String msetName, String msetTitle, String msetValue, Short msetType) {
		this.msetId = msetId;
		this.msetName = msetName;
		this.msetTitle = msetTitle;
		this.msetValue = msetValue;
		this.msetType = msetType;
	}

	/** default constructor */
	public MmSetting() {}

	/** minimal constructor */
	public MmSetting(String msetName, String msetValue, Short msetType) {
		this.msetName = msetName;
		this.msetValue = msetValue;
		this.msetType = msetType;
	}

	public String getMsetName() {
		return this.msetName;
	}

	public void setMsetName(String msetName) {
		this.msetName = msetName;
	}

	public String getMsetTitle() {
		return this.msetTitle;
	}

	public void setMsetTitle(String msetTitle) {
		this.msetTitle = msetTitle;
	}

	public String getMsetValue() {
		return this.msetValue;
	}

	public void setMsetValue(String msetValue) {
		this.msetValue = msetValue;
	}

	public Short getMsetType() {
		return this.msetType;
	}

	public void setMsetType(Short msetType) {
		this.msetType = msetType;
	}

	public String toString() {
		return new ToStringBuilder(this).append("msetName", getMsetName()).toString();
	}

	public Long getMsetId() {
		return msetId;
	}

	public void setMsetId(Long msetId) {
		this.msetId = msetId;
	}
}
