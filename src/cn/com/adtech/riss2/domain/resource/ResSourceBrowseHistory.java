package cn.com.adtech.riss2.domain.resource;

import java.util.Date;

import cn.com.adtech.riss2.domain.SysUserinfo;

@SuppressWarnings("serial")
public class ResSourceBrowseHistory implements java.io.Serializable {
	// Fields
	private Long rsbhId;

	private ResSourceData resSourceData;

	private Long rsdaId;

	private SysUserinfo sysUserinfo;

	private Long suinId;

	private Date rsbhDate;

	// Constructors
	/** default constructor */
	public ResSourceBrowseHistory() {}

	public Long getRsbhId() {
		return rsbhId;
	}

	public void setRsbhId(Long rsbhId) {
		this.rsbhId = rsbhId;
	}

	public ResSourceData getResSourceData() {
		return resSourceData;
	}

	public void setResSourceData(ResSourceData resSourceData) {
		this.resSourceData = resSourceData;
	}

	public Long getRsdaId() {
		return rsdaId;
	}

	public void setRsdaId(Long rsdaId) {
		this.rsdaId = rsdaId;
	}

	public SysUserinfo getSysUserinfo() {
		return sysUserinfo;
	}

	public void setSysUserinfo(SysUserinfo sysUserinfo) {
		this.sysUserinfo = sysUserinfo;
	}

	public Long getSuinId() {
		return suinId;
	}

	public void setSuinId(Long suinId) {
		this.suinId = suinId;
	}

	public Date getRsbhDate() {
		return rsbhDate;
	}

	public void setRsbhDate(Date rsbhDate) {
		this.rsbhDate = rsbhDate;
	}

}