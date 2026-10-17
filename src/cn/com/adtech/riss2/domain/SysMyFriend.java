package cn.com.adtech.riss2.domain;

import java.io.Serializable;
import java.util.Date;

public class SysMyFriend implements Serializable {
	/** ID*/
	private Long smfrId;
	
	/** 主人id*/
	private Long smfrMasterId;
	
	/** 好友id*/
	private Long smfrFriendId;
	
	/** 添加时间*/
	private Date smfrDateTime;
	
	/** 状态*/
	private Long smfrApply;

	public Date getSmfrDateTime() {
		return smfrDateTime;
	}

	public void setSmfrDateTime(Date smfrDateTime) {
		this.smfrDateTime = smfrDateTime;
	}

	public Long getSmfrFriendId() {
		return smfrFriendId;
	}

	public void setSmfrFriendId(Long smfrFriendId) {
		this.smfrFriendId = smfrFriendId;
	}

	public Long getSmfrId() {
		return smfrId;
	}

	public void setSmfrId(Long smfrId) {
		this.smfrId = smfrId;
	}

	public Long getSmfrMasterId() {
		return smfrMasterId;
	}

	public void setSmfrMasterId(Long smfrMasterId) {
		this.smfrMasterId = smfrMasterId;
	}

	public Long getSmfrApply() {
		return smfrApply;
	}

	public void setSmfrApply(Long smfrApply) {
		this.smfrApply = smfrApply;
	}
}
