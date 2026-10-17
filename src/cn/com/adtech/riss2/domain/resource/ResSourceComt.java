package cn.com.adtech.riss2.domain.resource;

import java.io.Serializable;
import java.util.Date;

import cn.com.adtech.riss2.domain.SysUserinfo;

/**
 * 资源评论
 * 
 * @author MaChao
 * @date 2010-10-12
 */
public class ResSourceComt implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 状态 正常 */
	public static final Short STATUS_PAST = 1;

	/** 状态 已屏蔽 */
	public static final Short STATUS_NOT_PAST = 0;

	private Long rscoId;

	/** 资源ID RSCO_RSDA_ID */
	private Long rscoRsdaId;

	private ResSourceData resSourceData;

	/** 引用评论ID RSCO_REFID */
	private Long rscoRefid;

	private ResSourceComt parentSourceComt;

	/** 评论者ID RSCO_SUIN_ID */
	private Long rscoSuinId;

	private SysUserinfo sysUserinfo;

	/** 评论内容 RSCO_CONTENT */
	private String rscoContent;

	/** 评分 RSCO_GRADE */
	private Short rscoGrade;

	/** 评论时间 RSCO_DATE */
	private Date rscoDate;

	/** 状态 RSCO_STATUS */
	private Short rscoStatus;

	public ResSourceComt() {
		super();
	}

	public ResSourceComt(Long rscoId, Long rscoRsdaId, ResSourceData resSourceData, Long rscoRefid, ResSourceComt parentSourceComt,
			Long rscoSuinId, SysUserinfo sysUserinfo, String rscoContent, Short rscoGrade, Date rscoDate, Short rscoStatus) {
		super();
		this.rscoId = rscoId;
		this.rscoRsdaId = rscoRsdaId;
		this.resSourceData = resSourceData;
		this.rscoRefid = rscoRefid;
		this.parentSourceComt = parentSourceComt;
		this.rscoSuinId = rscoSuinId;
		this.sysUserinfo = sysUserinfo;
		this.rscoContent = rscoContent;
		this.rscoGrade = rscoGrade;
		this.rscoDate = rscoDate;
		this.rscoStatus = rscoStatus;
	}

	public Long getRscoId() {
		return rscoId;
	}

	public void setRscoId(Long rscoId) {
		this.rscoId = rscoId;
	}

	public Long getRscoRsdaId() {
		return rscoRsdaId;
	}

	public void setRscoRsdaId(Long rscoRsdaId) {
		this.rscoRsdaId = rscoRsdaId;
	}

	public ResSourceData getResSourceData() {
		return resSourceData;
	}

	public void setResSourceData(ResSourceData resSourceData) {
		this.resSourceData = resSourceData;
	}

	public Long getRscoRefid() {
		return rscoRefid;
	}

	public void setRscoRefid(Long rscoRefid) {
		this.rscoRefid = rscoRefid;
	}

	public ResSourceComt getParentSourceComt() {
		return parentSourceComt;
	}

	public void setParentSourceComt(ResSourceComt parentSourceComt) {
		this.parentSourceComt = parentSourceComt;
	}

	public Long getRscoSuinId() {
		return rscoSuinId;
	}

	public void setRscoSuinId(Long rscoSuinId) {
		this.rscoSuinId = rscoSuinId;
	}

	public SysUserinfo getSysUserinfo() {
		return sysUserinfo;
	}

	public void setSysUserinfo(SysUserinfo sysUserinfo) {
		this.sysUserinfo = sysUserinfo;
	}

	public String getRscoContent() {
		return rscoContent;
	}

	public void setRscoContent(String rscoContent) {
		this.rscoContent = rscoContent;
	}

	public Short getRscoGrade() {
		return rscoGrade;
	}

	public void setRscoGrade(Short rscoGrade) {
		this.rscoGrade = rscoGrade;
	}

	public Date getRscoDate() {
		return rscoDate;
	}

	public void setRscoDate(Date rscoDate) {
		this.rscoDate = rscoDate;
	}

	public Short getRscoStatus() {
		return rscoStatus;
	}

	public void setRscoStatus(Short rscoStatus) {
		this.rscoStatus = rscoStatus;
	}
	
	public String getReplaceRscoContent(){
		if(this.rscoContent == null)return "";
		return this.rscoContent;
	}
}
