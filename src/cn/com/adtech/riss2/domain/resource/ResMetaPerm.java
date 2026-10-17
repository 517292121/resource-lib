package cn.com.adtech.riss2.domain.resource;

import java.util.Date;

import cn.com.adtech.riss2.domain.SysManager;

/**
 * 特色库权限
 * 
 * @author MaChao
 * @date 2010-8-31
 */
@SuppressWarnings("serial")
public class ResMetaPerm implements java.io.Serializable {
	// Fields
	private Long rmpeId;

	private ResMetaStore resMetaStore;

	private Long rmstId;

	private SysManager sysManager;

	private Long smanId;

	private Long rmpeType;

	private Date rmpeBuilddate;

	// 管理员权限类型 -- 1：管理权限、2：数据加工仅限、3：数据校验权限。
	public static final Long RMST_MANAGER = new Long(1);

	public static final Long RMST_DATAPROCESS = new Long(2);

	public static final Long RMST_DATACHECK = new Long(3);

	// Constructors
	public ResMetaPerm() {}

	// Property accessors
	public Long getRmpeId() {
		return this.rmpeId;
	}

	public void setRmpeId(Long rmpeId) {
		this.rmpeId = rmpeId;
	}

	public ResMetaStore getResMetaStore() {
		return this.resMetaStore;
	}

	public void setResMetaStore(ResMetaStore resMetaStore) {
		this.resMetaStore = resMetaStore;
	}

	public SysManager getSysManager() {
		return this.sysManager;
	}

	public void setSysManager(SysManager sysManager) {
		this.sysManager = sysManager;
	}

	public Long getRmpeType() {
		return this.rmpeType;
	}

	public void setRmpeType(Long rmpeType) {
		this.rmpeType = rmpeType;
	}

	public Date getRmpeBuilddate() {
		return this.rmpeBuilddate;
	}

	public void setRmpeBuilddate(Date rmpeBuilddate) {
		this.rmpeBuilddate = rmpeBuilddate;
	}

	public Long getRmstId() {
		return rmstId;
	}

	public void setRmstId(Long rmstId) {
		this.rmstId = rmstId;
	}

	public Long getSmanId() {
		return smanId;
	}

	public void setSmanId(Long smanId) {
		this.smanId = smanId;
	}
}
