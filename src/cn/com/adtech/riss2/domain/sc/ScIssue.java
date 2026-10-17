package cn.com.adtech.riss2.domain.sc;

import java.util.Date;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;

/**
 * 文献求助
 * 
 * @author MaChao
 * @date 2010-11-1
 */
@SuppressWarnings("serial")
public class ScIssue implements java.io.Serializable {
	// Fields
	private Long scisId;

	private SysUserinfo scisLastReply;

	private Long scisLastReplyManId;

	private SysUserinfo scisSuin;

	private Long scisSuinId;

	private ScIssueType scIssueType;

	private Long scisScitId;

	private Date scisLastReplyTime;

	private String scisTitle;

	private String scisContent;

	private Date scisCreationTime;

	private Long scisState;

	private Date scisCloseTime;

	private String scisCloseComment;

	private Long scisCloseScirId;

	private ScIssueReply closeScIssueReply;

	private Long scisReplyNumber;

	private Boolean scisIsuse;

	// 求助状态：1未解决，2解决，3关闭；
	public final static Long UN_SOLVE = 1L;

	public final static Long SOLVE = 2L;

	public final static Long CLOSE = 3L;

	// 是否可用：1可用，0可用；
	public final static Boolean CAN_USE = true;

	public final static Boolean CAN_NOT_USE = false;

	public Boolean getScisIsuse() {
		return scisIsuse;
	}

	public void setScisIsuse(Boolean scisIsuse) {
		this.scisIsuse = scisIsuse;
	}

	// Constructors
	/** default constructor */
	public ScIssue() {}

	// Property accessors
	public Long getScisId() {
		return this.scisId;
	}

	public void setScisId(Long scisId) {
		this.scisId = scisId;
	}

	public ScIssueType getScIssueType() {
		return this.scIssueType;
	}

	public void setScIssueType(ScIssueType scIssueType) {
		this.scIssueType = scIssueType;
	}

	public Date getScisLastReplyTime() {
		return this.scisLastReplyTime;
	}

	public void setScisLastReplyTime(Date scisLastReplyTime) {
		this.scisLastReplyTime = scisLastReplyTime;
	}

	public String getScisTitle() {
		return this.scisTitle;
	}

	public void setScisTitle(String scisTitle) {
		this.scisTitle = scisTitle;
	}

	public String getScisContent() {
		return this.scisContent;
	}

	public void setScisContent(String scisContent) {
		this.scisContent = scisContent;
	}

	public Date getScisCreationTime() {
		return this.scisCreationTime;
	}

	public void setScisCreationTime(Date scisCreationTime) {
		this.scisCreationTime = scisCreationTime;
	}

	public Long getScisState() {
		return this.scisState;
	}

	public void setScisState(Long scisState) {
		this.scisState = scisState;
	}

	public Date getScisCloseTime() {
		return this.scisCloseTime;
	}

	public void setScisCloseTime(Date scisCloseTime) {
		this.scisCloseTime = scisCloseTime;
	}

	public String getScisCloseComment() {
		return this.scisCloseComment;
	}

	public void setScisCloseComment(String scisCloseComment) {
		this.scisCloseComment = scisCloseComment;
	}

	public Long getScisCloseScirId() {
		return this.scisCloseScirId;
	}

	public void setScisCloseScirId(Long scisCloseScirId) {
		this.scisCloseScirId = scisCloseScirId;
	}

	public Long getScisReplyNumber() {
		return this.scisReplyNumber;
	}

	public void setScisReplyNumber(Long scisReplyNumber) {
		this.scisReplyNumber = scisReplyNumber;
	}

	public SysUserinfo getScisLastReply() {
		return scisLastReply;
	}

	public void setScisLastReply(SysUserinfo scisLastReply) {
		this.scisLastReply = scisLastReply;
	}

	public Long getScisLastReplyManId() {
		return scisLastReplyManId;
	}

	public void setScisLastReplyManId(Long scisLastReplyManId) {
		this.scisLastReplyManId = scisLastReplyManId;
	}

	public SysUserinfo getScisSuin() {
		return scisSuin;
	}

	public void setScisSuin(SysUserinfo scisSuin) {
		this.scisSuin = scisSuin;
	}

	public Long getScisSuinId() {
		return scisSuinId;
	}

	public void setScisSuinId(Long scisSuinId) {
		this.scisSuinId = scisSuinId;
	}

	public Long getScisScitId() {
		return scisScitId;
	}

	public void setScisScitId(Long scisScitId) {
		this.scisScitId = scisScitId;
	}

	public ScIssueReply getCloseScIssueReply() {
		return closeScIssueReply;
	}

	public void setCloseScIssueReply(ScIssueReply closeScIssueReply) {
		this.closeScIssueReply = closeScIssueReply;
	}

	public String getFormateCloseTime() {
		if(this.scisCloseTime != null)
			return ConstantsObj.FORMAT_DATE_TIME.format(this.scisCloseTime);
		return "";
	}

	public String getFormateScisCreationTime() {
		if(this.scisCreationTime != null)
			return ConstantsObj.FORMAT_DATE_TIME.format(this.scisCreationTime);
		return "";
	}

	public String getFormateScisLastReplyTime() {
		if(this.scisLastReplyTime != null)
			return ConstantsObj.FORMAT_DATE_TIME.format(this.scisLastReplyTime);
		return "";
	}
	
	public String getSubScisTitle() {
		if(this.scisTitle.trim().length() > 15)
			return ServiceUtil.subString(this.scisTitle, 15, "");
		else
			return this.scisTitle;
	}
	
	public String getSubScisTitle2() {
		if(this.scisTitle.trim().length() > 20)
			return ServiceUtil.subString(this.scisTitle, 20, "");
		else
			return this.scisTitle;
	}
	
	public String getSubScisTitle3() {
		if(this.scisTitle.trim().length() > 24)
			return ServiceUtil.subString(this.scisTitle, 24, "");
		else
			return this.scisTitle;
	}
	
	public String getHandlerScisContent(){
		if(ServiceUtil.isNotNull(this.scisContent))
			return CommonUtil.hangleTextArea(this.scisContent);
		else 
			return "";
	}
	
	public String getHandlerScisCloseContent(){
		if(ServiceUtil.isNotNull(this.scisCloseComment))
			return CommonUtil.hangleTextArea(this.scisCloseComment);
		else
			return "";
		
	}
}
