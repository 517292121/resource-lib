package cn.com.adtech.riss2.domain.resource;

import java.util.Date;

import cn.com.adtech.riss2.domain.SysManager;

@SuppressWarnings("serial")
public class ResMissDevolve implements java.io.Serializable {
	// Fields
	private Long rmdeId;

	private ResMissionLanguage resMissionInfo;

	private Long rmdeRminId;

	private SysManager preSysManager;

	private Long rmdePreuid;

	private SysManager aftSysManager;

	private Long rmdeAftuid;

	private Short rmdeMissiontype;

	private Date rmdeMissdate;

	private Date rmdeDevodate;

	private String rmdeRemark;

	private Long rmdeStatus;

	private String rmdeMind;

	private Date rmdeAudidate;
	
	private Long missionId = 0L;
	
	public ResMissDevolve(long missionId, long preUid, long afterUid, short missionType){
		this.rmdeRminId = missionId;
		this.rmdePreuid = preUid;
		this.rmdeAftuid = afterUid;
		this.rmdeMissiontype = missionType;
	}
	
	
	/**
	 * 第一语加工任务
	 */
	final public static Short MISSION_JIAGONG = 1;
	
	/**
	 * 其它语加工任务
	 */
	final public static Short MISSION_FANYI = 2;
	
	/**
	 * 作业检验任务
	 */
	final public static Short MISSION_XIAOYAN =3;
	
	/**
	 * 其它语检验
	 */
	final public static Short MISSION_OTHERCHECK =4;
	
	////转交状态参数
	/**
	 * 等待确认
	 */
	final public static Long DELIVER_WAITING = 1L;
	
	/**
	 * 已接受
	 */
	final public static Long DELIVER_SUCCESS = 2L;
	
	/**
	 * 已拒绝
	 */
	final public static Long DELIVER_FAIL = 3L;
	

	// Constructors
	/** default constructor */
	public ResMissDevolve() {}

	/** minimal constructor */
	public ResMissDevolve(ResMissionLanguage resMissionInfo) {
		this.resMissionInfo = resMissionInfo;
	}

	// Property accessors
	public Long getRmdeId() {
		return this.rmdeId;
	}

	public void setRmdeId(Long rmdeId) {
		this.rmdeId = rmdeId;
	}

	public ResMissionLanguage getResMissionInfo() {
		return this.resMissionInfo;
	}

	public void setResMissionInfo(ResMissionLanguage resMissionInfo) {
		this.resMissionInfo = resMissionInfo;
	}

	public Long getRmdePreuid() {
		return this.rmdePreuid;
	}

	public void setRmdePreuid(Long rmdePreuid) {
		this.rmdePreuid = rmdePreuid;
	}

	public Long getRmdeAftuid() {
		return this.rmdeAftuid;
	}

	public void setRmdeAftuid(Long rmdeAftuid) {
		this.rmdeAftuid = rmdeAftuid;
	}

	public Date getRmdeMissdate() {
		return this.rmdeMissdate;
	}

	public void setRmdeMissdate(Date rmdeMissdate) {
		this.rmdeMissdate = rmdeMissdate;
	}

	public Date getRmdeDevodate() {
		return this.rmdeDevodate;
	}

	public void setRmdeDevodate(Date rmdeDevodate) {
		this.rmdeDevodate = rmdeDevodate;
	}

	public String getRmdeRemark() {
		return this.rmdeRemark;
	}

	public void setRmdeRemark(String rmdeRemark) {
		this.rmdeRemark = rmdeRemark;
	}

	public Long getRmdeStatus() {
		return this.rmdeStatus;
	}

	public void setRmdeStatus(Long rmdeStatus) {
		this.rmdeStatus = rmdeStatus;
	}

	public String getRmdeMind() {
		return this.rmdeMind;
	}

	public void setRmdeMind(String rmdeMind) {
		this.rmdeMind = rmdeMind;
	}

	public Date getRmdeAudidate() {
		return this.rmdeAudidate;
	}

	public void setRmdeAudidate(Date rmdeAudidate) {
		this.rmdeAudidate = rmdeAudidate;
	}

	public Long getRmdeRminId() {
		return rmdeRminId;
	}

	public void setRmdeRminId(Long rmdeRminId) {
		this.rmdeRminId = rmdeRminId;
	}

	public SysManager getPreSysManager() {
		return preSysManager;
	}

	public void setPreSysManager(SysManager preSysManager) {
		this.preSysManager = preSysManager;
	}

	public SysManager getAftSysManager() {
		return aftSysManager;
	}

	public void setAftSysManager(SysManager aftSysManager) {
		this.aftSysManager = aftSysManager;
	}

	public Short getRmdeMissiontype() {
		return rmdeMissiontype;
	}

	public void setRmdeMissiontype(Short rmdeMissiontype) {
		this.rmdeMissiontype = rmdeMissiontype;
	}

	public Long getMissionId() {
		return missionId;
	}

	public void setMissionId(Long missionId) {
		this.missionId = missionId;
	}
}