package cn.com.adtech.riss2.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

import cn.com.adtech.riss2.core.CommonUtil;

/** @author MaChao */
@SuppressWarnings( {"serial", "unchecked"})
public class SysFunction implements Serializable, Comparable {
	/** identifier field */
	private Long sfunId;

	/** persistent field */
	private String sfunName;

	/** nullable persistent field */
	private String sfunDesc;

	/** nullable persistent field */
	private String sfunUrl;

	/** persistent field */
	private SysCatalog parentCatalog;

	private Short sfunIndex;

	// 功能点编码
	private String sfunCode;

	// 是否隐藏该功能点
	private Boolean sfunIsHidden;

	/** default constructor */
	public SysFunction() {}

	public Long getSfunId() {
		return this.sfunId;
	}

	public void setSfunId(Long sfunId) {
		this.sfunId = sfunId;
	}

	public String getSfunName() {
		return this.sfunName;
	}

	public void setSfunName(String sfunName) {
		this.sfunName = sfunName;
	}

	public String getSfunDesc() {
		return this.sfunDesc;
	}

	public void setSfunDesc(String sfunDesc) {
		this.sfunDesc = sfunDesc;
	}

	public String getSfunUrl() {
		return this.sfunUrl;
	}

	public void setSfunUrl(String sfunUrl) {
		this.sfunUrl = sfunUrl;
	}

	public String toString() {
		return new ToStringBuilder(this).append("sfunId", getSfunId()).toString();
	}

	public Short getSfunIndex() {
		return sfunIndex;
	}

	public void setSfunIndex(Short sfunIndex) {
		this.sfunIndex = sfunIndex;
	}

	public SysCatalog getParentCatalog() {
		return parentCatalog;
	}

	public void setParentCatalog(SysCatalog parentCatalog) {
		this.parentCatalog = parentCatalog;
	}

	public String getSfunCode() {
		return sfunCode;
	}

	public void setSfunCode(String sfunCode) {
		this.sfunCode = sfunCode;
	}

	public int compareTo(Object o) {
		SysFunction that = (SysFunction)o;
		if(!CommonUtil.isNotNull(this.getSfunCode()))
			return -1;
		if(!CommonUtil.isNotNull(that.getSfunCode()))
			return -1;
		return Integer.parseInt(this.getSfunCode()) > Integer.parseInt(that.getSfunCode()) ? 1 : -1;
	}

	public Boolean getSfunIsHidden() {
		return sfunIsHidden;
	}

	public void setSfunIsHidden(Boolean sfunIsHidden) {
		this.sfunIsHidden = sfunIsHidden;
	}

	public boolean equals(Object obj) {
		if(obj instanceof SysFunction) {
			return this.getSfunId().longValue() == ((SysFunction)obj).getSfunId().longValue();
		}
		return false;
	}
}
