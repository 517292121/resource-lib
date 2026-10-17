package cn.com.adtech.riss2.domain.sc;

import java.util.Date;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;

/**
 * 求助回复
 * 
 * @author MaChao
 * @date 2010-11-1
 */
@SuppressWarnings("serial")
public class ScIssueReply implements java.io.Serializable {
	// Fields
	private Long scirId;

	private ScIssue scIssue;

	private Long scirScisId;

	private SysUserinfo sysUserinfo;

	private Long scirSuinId;

	private String scirContent;

	private Date scirReplyTime;

	private String scirAttachmentName;

	private String scirAttachmentFileName;

	private String scirAttachmentUrl;

	private Boolean scirIsuse;

	private Short scirGrade;

	// 回复是否可用:1可用，0禁用；
	public final static Boolean CAN_USE = true;

	public final static Boolean CAN_NOT_USE = false;

	public Boolean getScirIsuse() {
		return scirIsuse;
	}

	public void setScirIsuse(Boolean scirIsuse) {
		this.scirIsuse = scirIsuse;
	}

	public Long getScirScisId() {
		return scirScisId;
	}

	public void setScirScisId(Long scirScisId) {
		this.scirScisId = scirScisId;
	}

	public SysUserinfo getSysUserinfo() {
		return sysUserinfo;
	}

	public void setSysUserinfo(SysUserinfo sysUserinfo) {
		this.sysUserinfo = sysUserinfo;
	}

	public Long getScirSuinId() {
		return scirSuinId;
	}

	public void setScirSuinId(Long scirSuinId) {
		this.scirSuinId = scirSuinId;
	}

	// Constructors
	/** default constructor */
	public ScIssueReply() {}

	// Property accessors
	public Long getScirId() {
		return this.scirId;
	}

	public void setScirId(Long scirId) {
		this.scirId = scirId;
	}

	public ScIssue getScIssue() {
		return this.scIssue;
	}

	public void setScIssue(ScIssue scIssue) {
		this.scIssue = scIssue;
	}

	public String getScirContent() {
		return this.scirContent;
	}

	public void setScirContent(String scirContent) {
		this.scirContent = scirContent;
	}

	public Date getScirReplyTime() {
		return this.scirReplyTime;
	}

	public void setScirReplyTime(Date scirReplyTime) {
		this.scirReplyTime = scirReplyTime;
	}

	public String getScirAttachmentName() {
		return this.scirAttachmentName;
	}

	public void setScirAttachmentName(String scirAttachmentName) {
		this.scirAttachmentName = scirAttachmentName;
	}

	public String getScirAttachmentFileName() {
		return this.scirAttachmentFileName;
	}

	public void setScirAttachmentFileName(String scirAttachmentFileName) {
		this.scirAttachmentFileName = scirAttachmentFileName;
	}

	public String getScirAttachmentUrl() {
		return this.scirAttachmentUrl;
	}

	public void setScirAttachmentUrl(String scirAttachmentUrl) {
		this.scirAttachmentUrl = scirAttachmentUrl;
	}

	public Short getScirGrade() {
		return scirGrade;
	}

	public void setScirGrade(Short scirGrade) {
		this.scirGrade = scirGrade;
	}
	
	public String getFormateScirReplyTime(){
		if(this.scirReplyTime != null)
			return ConstantsObj.FORMAT_DATE_TIME.format(this.scirReplyTime);
		return "";
	}
	
	public String getHandlerScirContent(){
		if(ServiceUtil.isNotNull(this.scirContent))
			return CommonUtil.hangleTextArea(this.scirContent);
		else return "";
	}
}
