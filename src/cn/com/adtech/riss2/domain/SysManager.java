package cn.com.adtech.riss2.domain;

import java.util.List;

import cn.com.adtech.riss2.domain.resource.ResMetaPerm;

/**
 * SysManager entity.
 * 
 * @author MaChao
 */
public class SysManager implements java.io.Serializable {
	// Fields
	private Long smanId;

	private String smanName;

	private String smanLoginName;

	private String smanPwd;

	private String smanEmail;

	private String smanTel;

	private String smanRemark;

	private String tempSmanRemark;
	
	private Boolean smanIsStop;

	private List<ResMetaPerm> resMetaPermList;

	private Long rightsNumber;// 权限类型
	
	/**用户类型，1：管理员 2：导师***/
	private Integer smanType;
	
	/**所属院系ID。仅限用于导师类型用户**/
	private Long smanDeptId;
	
	/**用户类型——管理员(1)**/
	final public static int SMANTYPE_ADMIN = 1;
	
	/**用户类型——导师(2)**/
	final public static int SMANTYPE_TEACHER = 2;

	public Integer getSmanType() {
		return smanType;
	}

	public void setSmanType(Integer smanType) {
		this.smanType = smanType;
	}

	public Long getSmanDeptId() {
		return smanDeptId;
	}

	public void setSmanDeptId(Long smanDeptId) {
		this.smanDeptId = smanDeptId;
	}

	public Long getRightsNumber() {
		return rightsNumber;
	}

	public void setRightsNumber(Long rightsNumber) {
		this.rightsNumber = rightsNumber;
	}

	public List<ResMetaPerm> getResMetaPermList() {
		return resMetaPermList;
	}

	public void setResMetaPermList(List<ResMetaPerm> resMetaPermList) {
		this.resMetaPermList = resMetaPermList;
	}

	public String getTempSmanRemark() {
		return tempSmanRemark;
	}

	public void setTempSmanRemark(String tempSmanRemark) {
		this.tempSmanRemark = tempSmanRemark;
	}

	// Constructors
	/** default constructor */
	public SysManager() {}

	/** minimal constructor */
	public SysManager(Long smanId, String smanName, String smanLoginName, String smanPwd) {
		this.smanId = smanId;
		this.smanName = smanName;
		this.smanLoginName = smanLoginName;
		this.smanPwd = smanPwd;
	}

	/** full constructor */
	public SysManager(Long smanId, String smanName, String smanLoginName, String smanPwd, String smanEmail, String smanTel,
			String smanRemark, Boolean smanIsStop) {
		this.smanId = smanId;
		this.smanName = smanName;
		this.smanLoginName = smanLoginName;
		this.smanPwd = smanPwd;
		this.smanEmail = smanEmail;
		this.smanTel = smanTel;
		this.smanRemark = smanRemark;
		this.smanIsStop = smanIsStop;
	}

	// Property accessors
	public Long getSmanId() {
		return this.smanId;
	}

	public void setSmanId(Long smanId) {
		this.smanId = smanId;
	}

	public String getSmanName() {
		return this.smanName;
	}

	public void setSmanName(String smanName) {
		this.smanName = smanName;
	}

	public String getSmanLoginName() {
		return this.smanLoginName;
	}

	public void setSmanLoginName(String smanLoginName) {
		this.smanLoginName = smanLoginName;
	}

	public String getSmanPwd() {
		return this.smanPwd;
	}

	public void setSmanPwd(String smanPwd) {
		this.smanPwd = smanPwd;
	}

	public String getSmanEmail() {
		return this.smanEmail;
	}

	public void setSmanEmail(String smanEmail) {
		this.smanEmail = smanEmail;
	}

	public String getSmanTel() {
		return this.smanTel;
	}

	public void setSmanTel(String smanTel) {
		this.smanTel = smanTel;
	}

	public String getSmanRemark() {
		return this.smanRemark;
	}

	public void setSmanRemark(String smanRemark) {
		this.smanRemark = smanRemark;
	}

	public String getSmanNameBy10() {
		if(this.smanName.length() < 8)
			return this.smanName;
		return this.smanName.substring(0, 8) + "...";
	}

	public String getSubSuinLoginName() {
		if(this.smanLoginName.length() < 15)
			return this.smanLoginName;
		return this.smanLoginName.substring(0, 15) + "...";
	}

	public Boolean getSmanIsStop() {
		return smanIsStop;
	}

	public void setSmanIsStop(Boolean smanIsStop) {
		this.smanIsStop = smanIsStop;
	}
}
