package cn.com.adtech.riss2.domain.sc.docu;

import java.util.Date;

import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResSubjectType;

/**
 * ScDocuDelivery entity.
 * 
 * @author MaChao
 */

public class ScDocuDelivery implements java.io.Serializable {

	// Fields

	private long sddeId;
	
	private long sddeSuinId;
	private SysUserinfo sysUserinfo;
	
	private long sddeSmanId;
	private SysManager sysManager;
	
	private long sddeRstyId;
	private ResSubjectType rsty;
	
	private Date sddeCreationTime;
	private long sddeStatus;
	private String sddeReplyContent;
	private Date sddeReplyTime;
	private String sddeDocuType;
	private String sddeDocuTitle;
	private String sddeDocuCreator;
	private String sddeDocuPublisher;
	private String sddeDocuKeyword;
	private String sddeDocuIsbn;
	private String sddeDocuDate;
	private String sddeDocuUrl;
	private String sddeDocuPage;
	private String sddeContent;

	// Constructors

	/** default constructor */
	public ScDocuDelivery() {
	}

	/** minimal constructor */
	public ScDocuDelivery(long sddeRstyId, Date sddeCreationTime,
			long sddeStatus, String sddeDocuType, String sddeDocuTitle) {
		this.sddeRstyId = sddeRstyId;
		this.sddeCreationTime = sddeCreationTime;
		this.sddeStatus = sddeStatus;
		this.sddeDocuType = sddeDocuType;
		this.sddeDocuTitle = sddeDocuTitle;
	}

	/** full constructor */
	public ScDocuDelivery(long sddeSuinId, long sddeSmanId, long sddeRstyId,
			Date sddeCreationTime, long sddeStatus, String sddeReplyContent,
			Date sddeReplyTime, String sddeDocuType, String sddeDocuTitle,
			String sddeDocuCreator, String sddeDocuPublisher,
			String sddeDocuKeyword, String sddeDocuIsbn, String sddeDocuDate,
			String sddeDocuUrl, String sddeDocuPage, String sddeContent) {
		this.sddeSuinId = sddeSuinId;
		this.sddeSmanId = sddeSmanId;
		this.sddeRstyId = sddeRstyId;
		this.sddeCreationTime = sddeCreationTime;
		this.sddeStatus = sddeStatus;
		this.sddeReplyContent = sddeReplyContent;
		this.sddeReplyTime = sddeReplyTime;
		this.sddeDocuType = sddeDocuType;
		this.sddeDocuTitle = sddeDocuTitle;
		this.sddeDocuCreator = sddeDocuCreator;
		this.sddeDocuPublisher = sddeDocuPublisher;
		this.sddeDocuKeyword = sddeDocuKeyword;
		this.sddeDocuIsbn = sddeDocuIsbn;
		this.sddeDocuDate = sddeDocuDate;
		this.sddeDocuUrl = sddeDocuUrl;
		this.sddeDocuPage = sddeDocuPage;
		this.sddeContent = sddeContent;
	}

	// Property accessors

	public long getSddeId() {
		return this.sddeId;
	}

	public void setSddeId(long sddeId) {
		this.sddeId = sddeId;
	}

	public long getSddeSuinId() {
		return this.sddeSuinId;
	}

	public void setSddeSuinId(long sddeSuinId) {
		this.sddeSuinId = sddeSuinId;
	}

	public long getSddeSmanId() {
		return this.sddeSmanId;
	}

	public void setSddeSmanId(long sddeSmanId) {
		this.sddeSmanId = sddeSmanId;
	}

	public long getSddeRstyId() {
		return this.sddeRstyId;
	}

	public void setSddeRstyId(long sddeRstyId) {
		this.sddeRstyId = sddeRstyId;
	}

	public Date getSddeCreationTime() {
		return this.sddeCreationTime;
	}

	public void setSddeCreationTime(Date sddeCreationTime) {
		this.sddeCreationTime = sddeCreationTime;
	}

	public long getSddeStatus() {
		return this.sddeStatus;
	}

	public void setSddeStatus(long sddeStatus) {
		this.sddeStatus = sddeStatus;
	}

	public String getSddeReplyContent() {
		return this.sddeReplyContent;
	}

	public void setSddeReplyContent(String sddeReplyContent) {
		this.sddeReplyContent = sddeReplyContent;
	}

	public Date getSddeReplyTime() {
		return this.sddeReplyTime;
	}

	public void setSddeReplyTime(Date sddeReplyTime) {
		this.sddeReplyTime = sddeReplyTime;
	}

	public String getSddeDocuType() {
		return this.sddeDocuType;
	}

	public void setSddeDocuType(String sddeDocuType) {
		this.sddeDocuType = sddeDocuType;
	}

	public String getSddeDocuTitle() {
		return this.sddeDocuTitle;
	}

	public void setSddeDocuTitle(String sddeDocuTitle) {
		this.sddeDocuTitle = sddeDocuTitle;
	}

	public String getSddeDocuCreator() {
		return this.sddeDocuCreator;
	}

	public void setSddeDocuCreator(String sddeDocuCreator) {
		this.sddeDocuCreator = sddeDocuCreator;
	}

	public String getSddeDocuPublisher() {
		return this.sddeDocuPublisher;
	}

	public void setSddeDocuPublisher(String sddeDocuPublisher) {
		this.sddeDocuPublisher = sddeDocuPublisher;
	}

	public String getSddeDocuKeyword() {
		return this.sddeDocuKeyword;
	}

	public void setSddeDocuKeyword(String sddeDocuKeyword) {
		this.sddeDocuKeyword = sddeDocuKeyword;
	}

	public String getSddeDocuIsbn() {
		return this.sddeDocuIsbn;
	}

	public void setSddeDocuIsbn(String sddeDocuIsbn) {
		this.sddeDocuIsbn = sddeDocuIsbn;
	}

	public String getSddeDocuDate() {
		return this.sddeDocuDate;
	}

	public void setSddeDocuDate(String sddeDocuDate) {
		this.sddeDocuDate = sddeDocuDate;
	}

	public String getSddeDocuUrl() {
		return this.sddeDocuUrl;
	}

	public void setSddeDocuUrl(String sddeDocuUrl) {
		this.sddeDocuUrl = sddeDocuUrl;
	}

	public String getSddeDocuPage() {
		return this.sddeDocuPage;
	}

	public void setSddeDocuPage(String sddeDocuPage) {
		this.sddeDocuPage = sddeDocuPage;
	}

	public String getSddeContent() {
		return this.sddeContent;
	}

	public void setSddeContent(String sddeContent) {
		this.sddeContent = sddeContent;
	}

	public SysUserinfo getSysUserinfo() {
		return sysUserinfo;
	}

	public void setSysUserinfo(SysUserinfo sysUserinfo) {
		this.sysUserinfo = sysUserinfo;
	}

	public SysManager getSysManager() {
		return sysManager;
	}

	public void setSysManager(SysManager sysManager) {
		this.sysManager = sysManager;
	}

	public ResSubjectType getRsty() {
		return rsty;
	}

	public void setRsty(ResSubjectType rsty) {
		this.rsty = rsty;
	}

}
