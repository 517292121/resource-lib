package cn.com.adtech.riss2.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/** @author MaChao */
@SuppressWarnings("serial")
public class SysRoleFunction implements Serializable {

	/** identifier field */
	private Long srfuId;

	/** persistent field */
	private Long srfuSrolId;

	private Long srfuSfunId;

	/** nullable persistent field */
	private SysFunction sysFunction;
	
	private SysRole sysRole;

	/** full constructor */
	public SysRoleFunction(Long srfuId, Long srfuSrolId, SysFunction sysFunction) {
		this.srfuId = srfuId;
//		this.srfuSrolId = srfuSrolId;
		this.sysFunction = sysFunction;
	}

	/** default constructor */
	public SysRoleFunction() {}

	/** minimal constructor */
	public SysRoleFunction(Long srfuId, Long srfuSrolId) {
		this.srfuId = srfuId;
		this.srfuSrolId = srfuSrolId;
	}
	/** minimal constructor */
	public SysRoleFunction(Long srfuId) {
		this.srfuId = srfuId;
//		this.srfuSrolId = srfuSrolId;
	}

	public Long getSrfuId() {
		return this.srfuId;
	}

	public void setSrfuId(Long srfuId) {
		this.srfuId = srfuId;
	}

	public Long getSrfuSrolId() {
		return this.srfuSrolId;
	}

	public void setSrfuSrolId(Long srfuSrolId) {
		this.srfuSrolId = srfuSrolId;
	}

	public SysFunction getSysFunction() {
		return this.sysFunction;
	}

	public void setSysFunction(SysFunction sysFunction) {
		this.sysFunction = sysFunction;
	}

	public String toString() {
		return new ToStringBuilder(this).append("srfuId", getSrfuId()).toString();
	}

	public Long getSrfuSfunId() {
		return srfuSfunId;
	}

	public void setSrfuSfunId(Long srfuSfunId) {
		this.srfuSfunId = srfuSfunId;
	}

	public SysRole getSysRole() {
		return sysRole;
	}

	public void setSysRole(SysRole sysRole) {
		this.sysRole = sysRole;
	}

}
