package cn.com.adtech.riss2.domain.infosubscription;

import java.io.Serializable;
import java.util.Date;

import cn.com.adtech.riss2.domain.SysUserinfo;

/**
 * 消息订阅
 * 	一个关键字一条记录 
 * @author MaChao
 *
 */
public class InfoSubscription implements Serializable {

	private Long issId;
	
	private SysUserinfo issUser;
	
	private Long issUserId;
	
	private String issKeyWord;
	
	private Date issDate;

	public InfoSubscription() {
	}

	public InfoSubscription(Long issId, SysUserinfo issUser, Long issUserId, String issKeyWord,
			Date issDate) {
		this.issId = issId;
		this.issUser = issUser;
		this.issUserId = issUserId;
		this.issKeyWord = issKeyWord;
		this.issDate = issDate;
	}

	public Long getIssId() {
		return issId;
	}

	public void setIssId(Long issId) {
		this.issId = issId;
	}

	public SysUserinfo getIssUser() {
		return issUser;
	}

	public void setIssUser(SysUserinfo issUser) {
		this.issUser = issUser;
	}

	public Long getIssUserId() {
		return issUserId;
	}

	public void setIssUserId(Long issUserId) {
		this.issUserId = issUserId;
	}

	public String getIssKeyWord() {
		return issKeyWord;
	}

	public void setIssKeyWord(String issKeyWord) {
		this.issKeyWord = issKeyWord;
	}

	public Date getIssDate() {
		return issDate;
	}

	public void setIssDate(Date issDate) {
		this.issDate = issDate;
	}

	
}
