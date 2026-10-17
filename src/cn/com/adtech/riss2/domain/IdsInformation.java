package cn.com.adtech.riss2.domain;

import java.io.Serializable;
import java.util.Date;

import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ServiceUtil;

/**
 * 信息
 * 
 */
@SuppressWarnings("serial")
public class IdsInformation implements Serializable {
	/** 信息ID IDSI_ID */
	private Long idsiId;

	/** 分类ID IDSI_IDST_ID */
	private Long idsiIdstId;

	private IdsInformationType idsInformationType;

	/** 发布人ID IDSI_SMAN_ID */
	private Long idsiSmanId;

	private SysManager sysManager;

	/** 标题 IDSI_TITLE */
	private String idsiTitle;

	/** 内容 IDSI_CONTENT */
	private String idsiContent;

	/** 发布时间 IDSI_DATE */
	private Date idsiDate;
	
	/** 信息描述 */
	private String idsiDescription;
	
	/** 信息封面 */
	private String idsiImgUrl;
	
	private Integer idsiHitCount;
	
	//语种
	private String idsiLanguage;

	public Integer getIdsiHitCount() {
		return idsiHitCount;
	}

	public void setIdsiHitCount(Integer idsiHitCount) {
		this.idsiHitCount = idsiHitCount;
	}

	public Long getIdsiId() {
		return idsiId;
	}

	public void setIdsiId(Long idsiId) {
		this.idsiId = idsiId;
	}

	public Long getIdsiIdstId() {
		return idsiIdstId;
	}

	public void setIdsiIdstId(Long idsiIdstId) {
		this.idsiIdstId = idsiIdstId;
	}

	public Long getIdsiSmanId() {
		return idsiSmanId;
	}

	public void setIdsiSmanId(Long idsiSmanId) {
		this.idsiSmanId = idsiSmanId;
	}

	public String getIdsiTitle() {
		return idsiTitle;
	}

	public void setIdsiTitle(String idsiTitle) {
		this.idsiTitle = idsiTitle;
	}

	public String getIdsiContent() {
		return idsiContent;
	}

	public void setIdsiContent(String idsiContent) {
		this.idsiContent = idsiContent;
	}

	public Date getIdsiDate() {
		return idsiDate;
	}

	public void setIdsiDate(Date idsiDate) {
		this.idsiDate = idsiDate;
	}

	public IdsInformation() {
		super();
	}
	public IdsInformation(Long idsiId, String idsiTitle) {
		super();
		this.idsiId = idsiId;
		this.idsiTitle = idsiTitle;
	}

	public IdsInformation(Long idsiId, Long idsiIdstId, Long idsiSmanId, String idsiTitle, String idsiContent, Date idsiDate) {
		super();
		this.idsiId = idsiId;
		this.idsiIdstId = idsiIdstId;
		this.idsiSmanId = idsiSmanId;
		this.idsiTitle = idsiTitle;
		this.idsiContent = idsiContent;
		this.idsiDate = idsiDate;
	}

	public IdsInformationType getIdsInformationType() {
		return idsInformationType;
	}

	public void setIdsInformationType(IdsInformationType idsInformationType) {
		this.idsInformationType = idsInformationType;
	}

	public SysManager getSysManager() {
		return sysManager;
	}

	public void setSysManager(SysManager sysManager) {
		this.sysManager = sysManager;
	}

	public String getSubIdsiTitle() {
		String temp = "";
		if(this.idsiTitle.length() > 13)
			temp = ServiceUtil.subString(this.idsiTitle, 14, "");
		else
			temp = this.idsiTitle;
		return temp;
	}
	
	public String getSubIdsiTitleList() {
		String temp = "";
		if(this.idsiTitle.length() > 28)
			temp = ServiceUtil.subString(this.idsiTitle, 29, "...");
		else
			temp = this.idsiTitle;
		return temp;
	}
	
	public String getFormateIdsiDate(){
		return ConstantsObj.FORMAT_DATE_TIME.format(this.idsiDate);
	}

	public String getIdsiDescription() {
		return idsiDescription;
	}

	public void setIdsiDescription(String idsiDescription) {
		this.idsiDescription = idsiDescription;
	}

	public String getIdsiImgUrl() {
		return idsiImgUrl;
	}

	public void setIdsiImgUrl(String idsiImgUrl) {
		this.idsiImgUrl = idsiImgUrl;
	}

	public String getIdsiLanguage() {
		return idsiLanguage;
	}

	public void setIdsiLanguage(String idsiLanguage) {
		this.idsiLanguage = idsiLanguage;
	}
}
