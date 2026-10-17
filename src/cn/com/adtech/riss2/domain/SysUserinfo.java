package cn.com.adtech.riss2.domain;

import java.util.Date;

/**
 * SysUserinfo entity.
 * 
 * @author MaChao
 */
public class SysUserinfo implements java.io.Serializable {
	/** 未审核 SUIN_STATE */
	public static Short STATE_NOT_AUDIT = 0;
	/** 审核未通过 SUIN_STATE */
	public static Short STATE_NOT_PAST = -1;
	/** 审核通过 SUIN_STATE */
	public static Short STATE_PAST = 1;

	/** 未被停用 SUIN_IS_STOP */
	public static Boolean SUIN_IS_NOT_STOP = false;

	/** 男 */
	public static Short SEX_MALE = new Short("1");
	
	/** 女 */
	public static Short SEX_FEMALE = new Short("2");
	
	/** 未知 */
	public static Short SEX_UNKNOWN = new Short("3");
	
	// Fields
	private Long suinId;

	private String suinLoginName;

	private String suinPassword;

	private String suinName;

	private Short suinSex;

	private Date suinBirthday;

	private String suinEmail;

	private Boolean suinIsStop;

	private Date suinJoinDate;

	private Date suinLastvisitDate;

	private String suinMobileTelephone;

	private Short suinState;

	private Short suinAuditMode;

	private String suinQuestion;

	private String suinAnswer;

	private String suinHint;

	private SysUserType sysUserType;

	private SysStyle sysStyle;
	
	private Long suinSstyId;
	
	/**
	 * Add by YangJunping 20101216 头像
	 */
	private String suinImageUrl;
	
	private String suinImageLocalName;
	
	/**用户积分*/
	private Integer suinIntegral;
	
	/**用户下载量记数 每天凌晨更新*/
	private Integer suinDownNum;
	
	public Integer getSuinDownNum() {
		return suinDownNum;
	}

	public void setSuinDownNum(Integer suinDownNum) {
		this.suinDownNum = suinDownNum;
	}

	public String getSuinImageUrl() {
		return suinImageUrl;
	}

	public void setSuinImageUrl(String suinImageUrl) {
		this.suinImageUrl = suinImageUrl;
	}

	public String getSuinImageLocalName() {
		return suinImageLocalName;
	}

	public void setSuinImageLocalName(String suinImageLocalName) {
		this.suinImageLocalName = suinImageLocalName;
	}

	// Constructors
	/** default constructor */
	public SysUserinfo() {}

	/** minimal constructor */
	public SysUserinfo(Long suinId, String suinLoginName, String suinName, Date updatedate) {
		this.suinId = suinId;
		this.suinLoginName = suinLoginName;
		this.suinName = suinName;
	}

	public Long getSuinId() {
		return suinId;
	}

	public void setSuinId(Long suinId) {
		this.suinId = suinId;
	}

	public String getSuinLoginName() {
		return suinLoginName;
	}

	public void setSuinLoginName(String suinLoginName) {
		this.suinLoginName = suinLoginName;
	}

	public String getSuinPassword() {
		return suinPassword;
	}

	public void setSuinPassword(String suinPassword) {
		this.suinPassword = suinPassword;
	}

	public String getSuinName() {
		return suinName;
	}

	public void setSuinName(String suinName) {
		this.suinName = suinName;
	}

	public Short getSuinSex() {
		return suinSex;
	}

	public void setSuinSex(Short suinSex) {
		this.suinSex = suinSex;
	}

	public Date getSuinBirthday() {
		return suinBirthday;
	}

	public void setSuinBirthday(Date suinBirthday) {
		this.suinBirthday = suinBirthday;
	}

	public String getSuinEmail() {
		return suinEmail;
	}

	public void setSuinEmail(String suinEmail) {
		this.suinEmail = suinEmail;
	}

	public Boolean getSuinIsStop() {
		return suinIsStop;
	}

	public void setSuinIsStop(Boolean suinIsStop) {
		this.suinIsStop = suinIsStop;
	}

	public Date getSuinJoinDate() {
		return suinJoinDate;
	}

	public void setSuinJoinDate(Date suinJoinDate) {
		this.suinJoinDate = suinJoinDate;
	}

	public Date getSuinLastvisitDate() {
		return suinLastvisitDate;
	}

	public void setSuinLastvisitDate(Date suinLastvisitDate) {
		this.suinLastvisitDate = suinLastvisitDate;
	}

	public String getSuinMobileTelephone() {
		return suinMobileTelephone;
	}

	public void setSuinMobileTelephone(String suinMobileTelephone) {
		this.suinMobileTelephone = suinMobileTelephone;
	}

	public Short getSuinState() {
		return suinState;
	}

	public void setSuinState(Short suinState) {
		this.suinState = suinState;
	}

	public Short getSuinAuditMode() {
		return suinAuditMode;
	}

	public void setSuinAuditMode(Short suinAuditMode) {
		this.suinAuditMode = suinAuditMode;
	}

	public String getSuinQuestion() {
		return suinQuestion;
	}

	public void setSuinQuestion(String suinQuestion) {
		this.suinQuestion = suinQuestion;
	}

	public String getSuinAnswer() {
		return suinAnswer;
	}

	public void setSuinAnswer(String suinAnswer) {
		this.suinAnswer = suinAnswer;
	}

	public String getSuinHint() {
		return suinHint;
	}

	public void setSuinHint(String suinHint) {
		this.suinHint = suinHint;
	}

	public SysUserType getSysUserType() {
		return sysUserType;
	}

	public void setSysUserType(SysUserType sysUserType) {
		this.sysUserType = sysUserType;
	}

	public SysStyle getSysStyle() {
		return sysStyle;
	}

	public void setSysStyle(SysStyle sysStyle) {
		this.sysStyle = sysStyle;
	}

	public Long getSuinSstyId() {
		return suinSstyId;
	}

	public void setSuinSstyId(Long suinSstyId) {
		this.suinSstyId = suinSstyId;
	}

	public Integer getSuinIntegral() {
		return suinIntegral;
	}

	public void setSuinIntegral(Integer suinIntegral) {
		this.suinIntegral = suinIntegral;
	}
}
