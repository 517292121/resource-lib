package cn.com.adtech.riss2.domain.infosubscription;

import java.util.Date;

import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

/**
 * 忽略的更新的资源
 * @author MaChao
 *
 */
public class IsIgnoreResSource {
	
	private Long isInRsId;
	
	private SysUserinfo isInRsUser;
	
	private Long isInRsUserId;
	
	private ResSourceData isInRsSourceData;
	
	private Long isInRsSourceId;
	
	private Date isInRsDate;

	public IsIgnoreResSource() {
	}

	public IsIgnoreResSource(Long isInRsId, SysUserinfo isInRsUser, Long isInRsUserId,
			ResSourceData isInRsSourceData, Long isInRsSourceId, Date isInRsDate) {
		this.isInRsId = isInRsId;
		this.isInRsUser = isInRsUser;
		this.isInRsUserId = isInRsUserId;
		this.isInRsSourceData = isInRsSourceData;
		this.isInRsSourceId = isInRsSourceId;
		this.isInRsDate = isInRsDate;
	}

	public Long getIsInRsId() {
		return isInRsId;
	}

	public void setIsInRsId(Long isInRsId) {
		this.isInRsId = isInRsId;
	}

	public SysUserinfo getIsInRsUser() {
		return isInRsUser;
	}

	public void setIsInRsUser(SysUserinfo isInRsUser) {
		this.isInRsUser = isInRsUser;
	}

	public Long getIsInRsUserId() {
		return isInRsUserId;
	}

	public void setIsInRsUserId(Long isInRsUserId) {
		this.isInRsUserId = isInRsUserId;
	}

	public ResSourceData getIsInRsSourceData() {
		return isInRsSourceData;
	}

	public void setIsInRsSourceData(ResSourceData isInRsSourceData) {
		this.isInRsSourceData = isInRsSourceData;
	}

	public Long getIsInRsSourceId() {
		return isInRsSourceId;
	}

	public void setIsInRsSourceId(Long isInRsSourceId) {
		this.isInRsSourceId = isInRsSourceId;
	}

	public Date getIsInRsDate() {
		return isInRsDate;
	}

	public void setIsInRsDate(Date isInRsDate) {
		this.isInRsDate = isInRsDate;
	}
	
}

