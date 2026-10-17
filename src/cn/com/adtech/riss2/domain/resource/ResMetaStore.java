package cn.com.adtech.riss2.domain.resource;

import java.util.Date;

import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.SysManager;

/**
 * 特色库
 * 
 * @author MaChao
 * @date 2010-8-31
 */
@SuppressWarnings("serial")
// @Embeddable
public class ResMetaStore implements java.io.Serializable {
	/** RMST_STATUS 可正常使用 */
	public static final Long STATUS_USEABLENESS = 1l;

	// Fields
	private Long rmstId;

	private Long smanId;

	private SysManager sysManager;

	private String rmstName;

	private String rmstCompany;

	private String rmstRemark;

	private Date rmstBuilddate;

	// @Field
	private Long rmstStatus;

	private Long rmstRfilId; // 文件服务器ID

	private ResFilesystem resFilesystem;

	/**
	 * 特色库文件存在盘符，格式：32534534\\
	 */
	private String rmstFolder;

	// 增加评论开关
	private Long rmstCmtstus;
	
	/**
	 * 特色库专题图片
	 */
	private String rmstLogo;
	private String rmstLogoBg;

	private Long rmstIsUploadResource;
	
	/**
	 * 特色库首页排序
	 */
	private Long rmstIndexSort;
	
	
	//自定义分类一名称
	private String rmstGroupOne;
	//自定义分类二名称
	private String rmstGroupTwo;
	//自定义分类三名称
	private String rmstGroupThree;
	
	/**资源提交默认审核状态，即是否需要审核**/
	private Integer rmstResourceAudit;
	
	//资源库语种类型
	private String rmstLanguage;
	
	public Long getRmstIsUploadResource() {
		return rmstIsUploadResource;
	}

	public void setRmstIsUploadResource(Long rmstIsUploadResource) {
		this.rmstIsUploadResource = rmstIsUploadResource;
	}

	public Long getRmstCmtstus() {
		return rmstCmtstus;
	}

	public void setRmstCmtstus(Long rmstCmtstus) {
		this.rmstCmtstus = rmstCmtstus;
	}

	public String getRmstFolder() {
		return rmstFolder;
	}

	public void setRmstFolder(String rmstFolder) {
		this.rmstFolder = rmstFolder;
	}

	// Constructors
	/** default constructor */
	public ResMetaStore() {}

	/** minimal constructor */
	public ResMetaStore(String rmstName) {
		this.rmstName = rmstName;
	}

	/** full constructor */
	public ResMetaStore(Long smanId, String rmstName, String rmstCompany, String rmstRemark, Date rmstBuilddate, Long rmstStatus) {
		this.smanId = smanId;
		this.rmstName = rmstName;
		this.rmstCompany = rmstCompany;
		this.rmstRemark = rmstRemark;
		this.rmstBuilddate = rmstBuilddate;
		this.rmstStatus = rmstStatus;
	}

	// Property accessors
	public Long getRmstId() {
		return this.rmstId;
	}

	public void setRmstId(Long rmstId) {
		this.rmstId = rmstId;
	}

	public Long getSmanId() {
		return this.smanId;
	}

	public void setSmanId(Long smanId) {
		this.smanId = smanId;
	}

	public String getRmstName() {
		return this.rmstName;
	}

	public void setRmstName(String rmstName) {
		this.rmstName = rmstName;
	}

	public String getRmstCompany() {
		return this.rmstCompany;
	}

	public void setRmstCompany(String rmstCompany) {
		this.rmstCompany = rmstCompany;
	}

	public String getRmstRemark() {
		return this.rmstRemark;
	}

	public void setRmstRemark(String rmstRemark) {
		this.rmstRemark = rmstRemark;
	}

	public Date getRmstBuilddate() {
		return this.rmstBuilddate;
	}

	public void setRmstBuilddate(Date rmstBuilddate) {
		this.rmstBuilddate = rmstBuilddate;
	}

	public Long getRmstStatus() {
		return this.rmstStatus;
	}

	public void setRmstStatus(Long rmstStatus) {
		this.rmstStatus = rmstStatus;
	}

	public SysManager getSysManager() {
		return sysManager;
	}

	public void setSysManager(SysManager sysManager) {
		this.sysManager = sysManager;
	}

	public Long getRmstRfilId() {
		return rmstRfilId;
	}

	public void setRmstRfilId(Long rmstRfilId) {
		this.rmstRfilId = rmstRfilId;
	}

	public ResFilesystem getResFilesystem() {
		return resFilesystem;
	}

	public void setResFilesystem(ResFilesystem resFilesystem) {
		this.resFilesystem = resFilesystem;
	}

	public String getRmstLogo() {
		return rmstLogo;
	}

	public void setRmstLogo(String rmstLogo) {
		this.rmstLogo = rmstLogo;
	}

	public Long getRmstIndexSort() {
		return rmstIndexSort;
	}

	public void setRmstIndexSort(Long rmstIndexSort) {
		this.rmstIndexSort = rmstIndexSort;
	}

	public String getRmstGroupOne() {
		return rmstGroupOne;
	}

	public void setRmstGroupOne(String rmstGroupOne) {
		this.rmstGroupOne = rmstGroupOne;
	}

	public String getRmstGroupTwo() {
		return rmstGroupTwo;
	}

	public void setRmstGroupTwo(String rmstGroupTwo) {
		this.rmstGroupTwo = rmstGroupTwo;
	}

	public String getRmstGroupThree() {
		return rmstGroupThree;
	}

	public void setRmstGroupThree(String rmstGroupThree) {
		this.rmstGroupThree = rmstGroupThree;
	}

	public Integer getRmstResourceAudit() {
		return rmstResourceAudit;
	}

	public void setRmstResourceAudit(Integer rmstResourceAudit) {
		this.rmstResourceAudit = rmstResourceAudit;
	}

	public String getRmstLogoBg() {
		return rmstLogoBg;
	}

	public void setRmstLogoBg(String rmstLogoBg) {
		this.rmstLogoBg = rmstLogoBg;
	}

	public String getRmstLanguage() {
		return rmstLanguage;
	}

	public void setRmstLanguage(String rmstLanguage) {
		this.rmstLanguage = rmstLanguage;
	}
}
