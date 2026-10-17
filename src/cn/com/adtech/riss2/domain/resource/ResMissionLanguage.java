package cn.com.adtech.riss2.domain.resource;

import java.util.Date;

import cn.com.adtech.riss2.domain.SysManager;

/**
 * ResMissionLanguage entity.
 * 
 * @author MaChao
 */
public class ResMissionLanguage implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private Long rmlaId;

	private Long resMissionId; // 作业ID

	private Long resLanguageId; // 语种ID

	private Integer rmlaStatus;

	private Long rmlaProcessor;

	private Long rmlaAssessor;

	private Date rmlaProctime;

	private Date rmlaAssetime;

	private Boolean rmlaIsfirst;

	private ResMissionInfo resMissionInfo;

	private ResLanguageType resLanguageType;

	private SysManager processorAuthor; // 加工者

	private SysManager assessorAuthor; // 校验者

	/**
	 * 加工中
	 */
	final public static int STATU_PROCESSING = 1;

	/**
	 * 校验中
	 */
	final public static int STATU_CHECKING = 2;

	/**
	 * 校验通过
	 */
	final public static int STATU_CHECK_SUCCESS = 3;

	/**
	 * 校验未通过
	 */
	final public static int STATU_CHECK_FAIL = 4;

	/**
	 * 转交中
	 */
	final public static int STATU_INTERPRETING = 5;

	// Constructors
	/** default constructor */
	public ResMissionLanguage() {}

	/** full constructor */
	public ResMissionLanguage(ResMissionInfo resMissionInfo, ResLanguageType resLanguageType, Integer rmlaStatus, Long rmlaProcessor,
			Long rmlaAssessor, Date rmlaProctime, Date rmlaAssetime, Boolean rmlaIsfirst) {
		this.resMissionInfo = resMissionInfo;
		this.resLanguageType = resLanguageType;
		this.rmlaStatus = rmlaStatus;
		this.rmlaProcessor = rmlaProcessor;
		this.rmlaAssessor = rmlaAssessor;
		this.rmlaProctime = rmlaProctime;
		this.rmlaAssetime = rmlaAssetime;
		this.rmlaIsfirst = rmlaIsfirst;
	}

	// Property accessors
	public Long getRmlaId() {
		return this.rmlaId;
	}

	public void setRmlaId(Long rmlaId) {
		this.rmlaId = rmlaId;
	}

	public ResMissionInfo getResMissionInfo() {
		return this.resMissionInfo;
	}

	public void setResMissionInfo(ResMissionInfo resMissionInfo) {
		this.resMissionInfo = resMissionInfo;
	}

	public ResLanguageType getResLanguageType() {
		return this.resLanguageType;
	}

	public void setResLanguageType(ResLanguageType resLanguageType) {
		this.resLanguageType = resLanguageType;
	}

	public Integer getRmlaStatus() {
		return this.rmlaStatus;
	}

	public void setRmlaStatus(Integer rmlaStatus) {
		this.rmlaStatus = rmlaStatus;
	}

	public Long getRmlaProcessor() {
		return this.rmlaProcessor;
	}

	public void setRmlaProcessor(Long rmlaProcessor) {
		this.rmlaProcessor = rmlaProcessor;
	}

	public Long getRmlaAssessor() {
		return this.rmlaAssessor;
	}

	public void setRmlaAssessor(Long rmlaAssessor) {
		this.rmlaAssessor = rmlaAssessor;
	}

	public Date getRmlaProctime() {
		return this.rmlaProctime;
	}

	public void setRmlaProctime(Date rmlaProctime) {
		this.rmlaProctime = rmlaProctime;
	}

	public Date getRmlaAssetime() {
		return this.rmlaAssetime;
	}

	public void setRmlaAssetime(Date rmlaAssetime) {
		this.rmlaAssetime = rmlaAssetime;
	}

	public Boolean getRmlaIsfirst() {
		return this.rmlaIsfirst;
	}

	public void setRmlaIsfirst(Boolean rmlaIsfirst) {
		this.rmlaIsfirst = rmlaIsfirst;
	}

	public Long getResMissionId() {
		return resMissionId;
	}

	public void setResMissionId(Long resMissionId) {
		this.resMissionId = resMissionId;
	}

	public Long getResLanguageId() {
		return resLanguageId;
	}

	public void setResLanguageId(Long resLanguageId) {
		this.resLanguageId = resLanguageId;
	}

	public SysManager getProcessorAuthor() {
		return processorAuthor;
	}

	public void setProcessorAuthor(SysManager processorAuthor) {
		this.processorAuthor = processorAuthor;
	}

	public SysManager getAssessorAuthor() {
		return assessorAuthor;
	}

	public void setAssessorAuthor(SysManager assessorAuthor) {
		this.assessorAuthor = assessorAuthor;
	}
}
