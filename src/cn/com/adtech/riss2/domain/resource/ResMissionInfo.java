package cn.com.adtech.riss2.domain.resource;

import java.util.Date;

import cn.com.adtech.riss2.domain.SysManager;

@SuppressWarnings("serial")
//@Embeddable
public class ResMissionInfo implements java.io.Serializable {
	// Fields
	private Long rminId;

	private ResMetaStore resMetaStore;

	private Long rminRmstId;

	private SysManager rminAuthorSysManager;

	private Long rminAuthor;

	private SysManager rminUserSysManager;

	private Long rminUser;

	private String rminMissname;

	private String rminRemark;

	private Date rminBuilddate;

	private Date rminEnddate;

	//@Field
	private Long rminStatus;

	private Long rminRltyId;

	private ResLiterType rminResLiterType;
	
	private Long rminLanugage; //第一语种ID
	
	private Integer rminResCount; //作业中的资源数量统计
	
//	/**
//	 * 其它语录入人员
//	 */
//	private Long rminInteruser;
//	private SysManager rminInteruserManager;
	
	/**
	 * 作业校验人员
	 */
	private Long rminCheckuser;
	private SysManager rminCheckuserManager;
	
	/**作业名称描述**/
	private String rminNameremark;
	
//	/**
//	 * 其它语校验人员
//	 */
//	private Long rminOthercheck;
//	private SysManager rminOthercheckManager;

	/* 状态 */
	public static final Long RMINSTATUS_NOTCOMMIT = 1L; // 加工中

	public static final Long RMINSTATUS_COMMIT_NOTCHECK = 2L; // 审核中

	public static final Long RMINSTATUS_COMMIT_PASS = 3L; // 已完成

	public static final Long RMINSTATUS_COMMIT_NOTPASS = 4L; // 审核未通过
	
	final public static Long RMINSTATUS_DELIVERING = 5L; // 任务转交中
	
	final public static Long RMINSTATUS_INTERPRET_CHECKOUT = 5L; // 其它语审核中

	final public static Long RMINSTATUS_INTERPRET_FAIL = 6L; // 其它语审核未通过

	final public static String getMissionStatu(long statu) {
		if(statu == RMINSTATUS_NOTCOMMIT) {
			return "加工中";
		}
		if(statu == RMINSTATUS_COMMIT_NOTCHECK) {
			return "审核中";
		}
		if(statu == RMINSTATUS_COMMIT_PASS) {
			return "已完成";
		}
		if(statu == RMINSTATUS_COMMIT_NOTPASS) {
			return "审核未通过";
		}
		if(statu == RMINSTATUS_DELIVERING) {
			return "转交审核中";
		}
		return "";
	}

	public ResMissionInfo() {}

	public Long getRminId() {
		return this.rminId;
	}

	public void setRminId(Long rminId) {
		this.rminId = rminId;
	}

	public ResMetaStore getResMetaStore() {
		return this.resMetaStore;
	}

	public Long getRminRmstId() {
		return rminRmstId;
	}

	public void setRminRmstId(Long rminRmstId) {
		this.rminRmstId = rminRmstId;
	}

	public SysManager getRminAuthorSysManager() {
		return rminAuthorSysManager;
	}

	public void setRminAuthorSysManager(SysManager rminAuthorSysManager) {
		this.rminAuthorSysManager = rminAuthorSysManager;
	}

	public Long getRminAuthor() {
		return rminAuthor;
	}

	public void setRminAuthor(Long rminAuthor) {
		this.rminAuthor = rminAuthor;
	}

	public SysManager getRminUserSysManager() {
		return rminUserSysManager;
	}

	public void setRminUserSysManager(SysManager rminUserSysManager) {
		this.rminUserSysManager = rminUserSysManager;
	}

	public ResLiterType getRminResLiterType() {
		return rminResLiterType;
	}

	public void setRminResLiterType(ResLiterType rminResLiterType) {
		this.rminResLiterType = rminResLiterType;
	}

	public void setResMetaStore(ResMetaStore resMetaStore) {
		this.resMetaStore = resMetaStore;
	}

	public Long getRminUser() {
		return this.rminUser;
	}

	public void setRminUser(Long rminUser) {
		this.rminUser = rminUser;
	}

	public String getRminMissname() {
		return this.rminMissname;
	}

	public void setRminMissname(String rminMissname) {
		this.rminMissname = rminMissname;
	}

	public String getRminRemark() {
		return this.rminRemark;
	}

	public void setRminRemark(String rminRemark) {
		this.rminRemark = rminRemark;
	}

	public Date getRminBuilddate() {
		return this.rminBuilddate;
	}

	public void setRminBuilddate(Date rminBuilddate) {
		this.rminBuilddate = rminBuilddate;
	}

	public Date getRminEnddate() {
		return this.rminEnddate;
	}

	public void setRminEnddate(Date rminEnddate) {
		this.rminEnddate = rminEnddate;
	}

	public Long getRminStatus() {
		return this.rminStatus;
	}

	public void setRminStatus(Long rminStatus) {
		this.rminStatus = rminStatus;
	}

	public Long getRminRltyId() {
		return this.rminRltyId;
	}

	public void setRminRltyId(Long rminRltyId) {
		this.rminRltyId = rminRltyId;
	}

//	public Long getRminInteruser() {
//		return rminInteruser;
//	}
//
//	public void setRminInteruser(Long rminInteruser) {
//		this.rminInteruser = rminInteruser;
//	}

	public Long getRminCheckuser() {
		return rminCheckuser;
	}

	public void setRminCheckuser(Long rminCheckuser) {
		this.rminCheckuser = rminCheckuser;
	}

//	public Long getRminOthercheck() {
//		return rminOthercheck;
//	}
//
//	public void setRminOthercheck(Long rminOthercheck) {
//		this.rminOthercheck = rminOthercheck;
//	}
//
//	public SysManager getRminInteruserManager() {
//		return rminInteruserManager;
//	}
//
//	public void setRminInteruserManager(SysManager rminInteruserManager) {
//		this.rminInteruserManager = rminInteruserManager;
//	}

	public SysManager getRminCheckuserManager() {
		return rminCheckuserManager;
	}

	public void setRminCheckuserManager(SysManager rminCheckuserManager) {
		this.rminCheckuserManager = rminCheckuserManager;
	}

//	public SysManager getRminOthercheckManager() {
//		return rminOthercheckManager;
//	}
//
//	public void setRminOthercheckManager(SysManager rminOthercheckManager) {
//		this.rminOthercheckManager = rminOthercheckManager;
//	}

	public Long getRminLanugage() {
		return rminLanugage;
	}

	public void setRminLanugage(Long rminLanugage) {
		this.rminLanugage = rminLanugage;
	}

	public String getRminNameremark() {
		return rminNameremark;
	}

	public void setRminNameremark(String rminNameremark) {
		this.rminNameremark = rminNameremark;
	}

	public Integer getRminResCount() {
		return rminResCount;
	}

	public void setRminResCount(Integer rminResCount) {
		this.rminResCount = rminResCount;
	}
}