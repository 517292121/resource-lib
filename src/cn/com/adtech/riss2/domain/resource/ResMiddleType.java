package cn.com.adtech.riss2.domain.resource;

import org.apache.commons.lang.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class ResMiddleType implements java.io.Serializable {
	private Long bztyId;

	private String bztyName;

	private ResMiddleType parentMiddleType;

	private String bztyCode;

	private Short bztyLayer;

	// 中图法编码 解析后编码集合字符串
	private String bztyParseCode;

	// 自编号
	private String bztyCustomCode;

	public ResMiddleType() {}

	public Long getBztyId() {
		return bztyId;
	}

	public void setBztyId(Long bztyId) {
		this.bztyId = bztyId;
	}

	public ResMiddleType getParentMiddleType() {
		return parentMiddleType;
	}

	public void setParentMiddleType(ResMiddleType parentMiddleType) {
		this.parentMiddleType = parentMiddleType;
	}

	public String getBztyName() {
		return this.bztyName;
	}

	public void setBztyName(String bztyName) {
		this.bztyName = bztyName;
	}

	public String getBztyCode() {
		return this.bztyCode;
	}

	public void setBztyCode(String bztyCode) {
		this.bztyCode = bztyCode;
	}

	public Short getBztyLayer() {
		return bztyLayer;
	}

	public void setBztyLayer(Short bztyLayer) {
		this.bztyLayer = bztyLayer;
	}

	public String getBztyCustomCode() {
		return bztyCustomCode;
	}

	public void setBztyCustomCode(String bztyCustomCode) {
		this.bztyCustomCode = bztyCustomCode;
	}

	public String getBztyParseCode() {
		return bztyParseCode;
	}

	public void setBztyParseCode(String bztyParseCode) {
		this.bztyParseCode = bztyParseCode;
	}

	public String toString() {
		return new ToStringBuilder(this).append("bztyId", getBztyId()).toString();
	}
}