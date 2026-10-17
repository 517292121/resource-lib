package cn.com.adtech.riss2.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 界面风格模板
 * 
 * @author MaChao
 * @actionscript.class bindable = true
 */
@SuppressWarnings("serial")
public class SysStyle implements Serializable {
	/** 蓝色风格 */
	public static final Long GREEN_SKIN = 1L;
	
	/** 绿色风格 */
	public static final Long BLUE_SKIN = 2L;
	
	/** 粉红色风格 */
	public static final Long PINK_SKIN = 3L;

	/** identifier field */
	private Long sstyId;

	/** persistent field */
	private String sstyName;

	/** nullable persistent field */
	private Boolean sstyIsActivation;

	/** default constructor */
	public SysStyle() {}

	public Long getSstyId() {
		return this.sstyId;
	}

	public void setSstyId(Long sstyId) {
		this.sstyId = sstyId;
	}

	public String getSstyName() {
		return this.sstyName;
	}

	public void setSstyName(String sstyName) {
		this.sstyName = sstyName;
	}

	public Boolean getSstyIsActivation() {
		return this.sstyIsActivation;
	}

	public void setSstyIsActivation(Boolean sstyIsActivation) {
		this.sstyIsActivation = sstyIsActivation;
	}

	public String toString() {
		return new ToStringBuilder(this).append("sstyId", getSstyId()).toString();
	}
}
