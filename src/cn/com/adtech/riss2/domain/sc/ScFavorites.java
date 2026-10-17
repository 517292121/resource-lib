package cn.com.adtech.riss2.domain.sc;

import java.io.Serializable;
import java.util.Date;

import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

/**
 * 收藏
 * @author MaChao
 *
 */
public class ScFavorites implements Serializable {
	
	private Long scfId;
	
	private SysUserinfo scfUser;
	
	private Long scfUserId;
	
	private ResSourceData scfSourceData;
	
	private Long scfSourceDataId;
	
	private Date scfDate;

	public ScFavorites() {
	}

	public ScFavorites(Long scfId, SysUserinfo scfUser, Long scfUserId,
			ResSourceData scfSourceData, Long scfSourceDataId, Date scfDate) {
		this.scfId = scfId;
		this.scfUser = scfUser;
		this.scfUserId = scfUserId;
		this.scfSourceData = scfSourceData;
		this.scfSourceDataId = scfSourceDataId;
		this.scfDate = scfDate;
	}
	
	public ScFavorites(Long scfUserId, Long scfSourceDataId, Date scfDate) {
		this.scfUserId = scfUserId;
		this.scfSourceDataId = scfSourceDataId;
		this.scfDate = scfDate;
	}

	public Long getScfId() {
		return scfId;
	}

	public void setScfId(Long scfId) {
		this.scfId = scfId;
	}

	public SysUserinfo getScfUser() {
		return scfUser;
	}

	public void setScfUser(SysUserinfo scfUser) {
		this.scfUser = scfUser;
	}

	public ResSourceData getScfSourceData() {
		return scfSourceData;
	}

	public void setScfSourceData(ResSourceData scfSourceData) {
		this.scfSourceData = scfSourceData;
	}

	public Date getScfDate() {
		return scfDate;
	}

	public void setScfDate(Date scfDate) {
		this.scfDate = scfDate;
	}

	public Long getScfUserId() {
		return scfUserId;
	}

	public void setScfUserId(Long scfUserId) {
		this.scfUserId = scfUserId;
	}

	public Long getScfSourceDataId() {
		return scfSourceDataId;
	}

	public void setScfSourceDataId(Long scfSourceDataId) {
		this.scfSourceDataId = scfSourceDataId;
	}
	
}
