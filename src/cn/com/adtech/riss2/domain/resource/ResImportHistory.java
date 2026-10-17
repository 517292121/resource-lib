package cn.com.adtech.riss2.domain.resource;

import java.util.Date;

/**
 * ResImportHistory entity.
 * 
 * @author MaChao
 */

public class ResImportHistory implements java.io.Serializable {

	// Fields

	private Long rmhiId;
	private ResMissionInfo resMissionInfo;
	private Long rmhiAuthor;
	private Long rmhiRminId;
	private Integer rmhiCheckMust;
	private Integer rmhiCheckType;
	private String rmhiFilefolder;
	private String rmhiLocalFilename;
	private String rmhiFilename;
	private String rmhiAddress;
	private Date rmhiImportDate;
	
	// Constructors

	/** default constructor */
	public ResImportHistory() {
	}

	// Property accessors

	public Long getRmhiId() {
		return this.rmhiId;
	}

	public void setRmhiId(Long rmhiId) {
		this.rmhiId = rmhiId;
	}

	public ResMissionInfo getResMissionInfo() {
		return this.resMissionInfo;
	}

	public void setResMissionInfo(ResMissionInfo resMissionInfo) {
		this.resMissionInfo = resMissionInfo;
	}

	public Long getRmhiAuthor() {
		return this.rmhiAuthor;
	}

	public void setRmhiAuthor(Long rmhiAuthor) {
		this.rmhiAuthor = rmhiAuthor;
	}

	public Integer getRmhiCheckMust() {
		return this.rmhiCheckMust;
	}

	public void setRmhiCheckMust(Integer rmhiCheckMust) {
		this.rmhiCheckMust = rmhiCheckMust;
	}

	public Integer getRmhiCheckType() {
		return this.rmhiCheckType;
	}

	public void setRmhiCheckType(Integer rmhiCheckType) {
		this.rmhiCheckType = rmhiCheckType;
	}

	public String getRmhiFilefolder() {
		return this.rmhiFilefolder;
	}

	public void setRmhiFilefolder(String rmhiFilefolder) {
		this.rmhiFilefolder = rmhiFilefolder;
	}

	public String getRmhiLocalFilename() {
		return this.rmhiLocalFilename;
	}

	public void setRmhiLocalFilename(String rmhiLocalFilename) {
		this.rmhiLocalFilename = rmhiLocalFilename;
	}

	public String getRmhiFilename() {
		return this.rmhiFilename;
	}

	public void setRmhiFilename(String rmhiFilename) {
		this.rmhiFilename = rmhiFilename;
	}

	public String getRmhiAddress() {
		return this.rmhiAddress;
	}

	public void setRmhiAddress(String rmhiAddress) {
		this.rmhiAddress = rmhiAddress;
	}

	public Date getRmhiImportDate() {
		return this.rmhiImportDate;
	}

	public void setRmhiImportDate(Date rmhiImportDate) {
		this.rmhiImportDate = rmhiImportDate;
	}

	public Long getRmhiRminId() {
		return rmhiRminId;
	}

	public void setRmhiRminId(Long rmhiRminId) {
		this.rmhiRminId = rmhiRminId;
	}

}
