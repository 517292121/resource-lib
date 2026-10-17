package cn.com.adtech.riss2.domain;

import java.util.Date;

/**
 * DbmsBakHistory entity.
 * 
 * @author MaChao
 */

public class DbmsBakHistory implements java.io.Serializable {

	// Fields

	private Long dbmsId;
	private String dbmsFilename;
	private String dbmsUrlpath;
	private Date dbmsBakdate;

	// Constructors

	/** default constructor */
	public DbmsBakHistory() {
	}

	/** full constructor */
	public DbmsBakHistory(String dbmsFilename, String dbmsUrlpath,
			Date dbmsBakdate) {
		this.dbmsFilename = dbmsFilename;
		this.dbmsUrlpath = dbmsUrlpath;
		this.dbmsBakdate = dbmsBakdate;
	}

	// Property accessors

	public Long getDbmsId() {
		return this.dbmsId;
	}

	public void setDbmsId(Long dbmsId) {
		this.dbmsId = dbmsId;
	}

	public String getDbmsFilename() {
		return this.dbmsFilename;
	}

	public void setDbmsFilename(String dbmsFilename) {
		this.dbmsFilename = dbmsFilename;
	}

	public String getDbmsUrlpath() {
		return this.dbmsUrlpath;
	}

	public void setDbmsUrlpath(String dbmsUrlpath) {
		this.dbmsUrlpath = dbmsUrlpath;
	}

	public Date getDbmsBakdate() {
		return this.dbmsBakdate;
	}

	public void setDbmsBakdate(Date dbmsBakdate) {
		this.dbmsBakdate = dbmsBakdate;
	}

}
