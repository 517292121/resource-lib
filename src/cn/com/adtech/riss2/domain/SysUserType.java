package cn.com.adtech.riss2.domain;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * 用户类型表
 * 
 * @author MaChao
 * @actionscript.class bindable = true
 */
public class SysUserType implements Serializable {
	/** 游客 */
	public static final Long USER_TYPE_VISITOR = 1L;

	/** 普通会员 */
	public static final Long USER_TYPE_MEMBER = 2L;

	/** 不可浏览 */
	public static final Short USER_TYPE_DOC_CANNOT_BROWSE = 0;

	/** 可浏览预览版 */
	public static final Short USER_TYPE_DOC_CAN_BROWSE_LITTLE = 1;

	/** 可浏览全文 */
	public static final Short USER_TYPE_DOC_CAN_BROWSE_ALL = 2;

	/** 可下载 */
	public static final Short USER_TYPE_DOC_DOWNLOAD = 3;

	/** 不可浏览 */
	public static final Short USER_TYPE_PIC_CANNOT_BROWSE = 0;

	/** 浏览缩略图 */
	public static final Short USER_TYPE_PIC_CAN_BROWSE_LITTLE = 1;

	/** 浏览原图 */
	public static final Short USER_TYPE_PIC_CAN_BROWSE_ALL = 2;

	/** 不可观看 */
	public static final Short USER_TYPE_VIDEO_CANNOT_BROWSE = 0;

	/** 可观看普通 */
	public static final Short USER_TYPE_VIDEO_CAN_BROWSE_LITTLE = 1;

	/** 可观看高清且下载 */
	public static final Short USER_TYPE_VIDEO_CAN_BROWSE_ALL = 2;

	/** 不是默认 */
	public static final Boolean USER_TYPE_NOT_DEFAULT = false;

	/** 默认 */
	public static final Boolean USER_TYPE_DEFAULT = true;
	
	
	////修正后的权限，将之前的按资源载体分类合并为统一分配///////////////////////////
	/**权限——不可访问(1)**/
	final public static Integer PURIVEW_NOSEE = 1; 
	/**权限——不可预览(2)**/
	final public static Integer PURIVEW_CANNOT_BROWSE = 2; 
	/**权限——可浏览预览文件(3)**/
	final public static Integer PURIVEW_BROWSE_VIEW = 3; 
	/**权限——可浏览源文件(4)**/
	final public static Integer PURIVEW_BROWSE_SOURCE = 4; 
	/**权限——可下载(5)**/
	final public static Integer PURIVEW_DOWNLOAD = 5; 

	/** identifier field */
	private Long sutyId;

	/** persistent field */
	private String sutyName;

	private Short sutyDoc;

	private Short sutyPic;

	private Short sutyVideo;

	private Boolean sutyDefault;
	
	/**改造后的权限设置，即将按资源载体类型分配合为统一分配权限 */
	private Integer sutyPurview;

	/** full constructor */
	public SysUserType(Long sutyId, String sutyName) {
		this.sutyId = sutyId;
		this.sutyName = sutyName;
	}

	/** default constructor */
	public SysUserType() {}

	public Long getSutyId() {
		return this.sutyId;
	}

	public void setSutyId(Long sutyId) {
		this.sutyId = sutyId;
	}

	public String getSutyName() {
		return this.sutyName;
	}

	public void setSutyName(String sutyName) {
		this.sutyName = sutyName;
	}

	public String toString() {
		return new ToStringBuilder(this).append("sutyId", getSutyId()).toString();
	}

	public Short getSutyDoc() {
		return sutyDoc;
	}

	public void setSutyDoc(Short sutyDoc) {
		this.sutyDoc = sutyDoc;
	}

	public Short getSutyPic() {
		return sutyPic;
	}

	public void setSutyPic(Short sutyPic) {
		this.sutyPic = sutyPic;
	}

	public Short getSutyVideo() {
		return sutyVideo;
	}

	public void setSutyVideo(Short sutyVideo) {
		this.sutyVideo = sutyVideo;
	}

	public Boolean getSutyDefault() {
		return sutyDefault;
	}

	public void setSutyDefault(Boolean sutyDefault) {
		this.sutyDefault = sutyDefault;
	}

	public Integer getSutyPurview() {
		return sutyPurview;
	}

	public void setSutyPurview(Integer sutyPurview) {
		this.sutyPurview = sutyPurview;
	}
}
