package cn.com.adtech.riss2.domain.count;

import java.util.Date;

/**
 * 站点访问量统计
 * @description 
 *
 * @author MaChao
 *
 * @date Nov 28, 2015 - 4:36:57 PM
 */
public class SysAccessCounter implements java.io.Serializable {
	
	public static String TYPE_NO="游客";
	public static String TYPE_LOGIN="登录";
	public static String TYPE_SEARCH="检索";
	public static String TYPE_READ="阅读";
	public static String TYPE_DOWN="下载";
	public static String TYPE_ACCESS="访问";
	
	private long sacoId; 
	private long sacoSuinId;
	private String sacoSuinName;
	private String sacoType;
	private Date sacoDate;
	private double sacoTime;
	private String sacoContent;
	private String sacoIp;

	// Constructors

	/** default constructor */
	public SysAccessCounter() {
	}

	/** minimal constructor */
	public SysAccessCounter(long sacoId, Date sacoDate) {
		this.sacoId = sacoId;
		this.sacoDate = sacoDate;
	}

	/** full constructor */
	public SysAccessCounter(long sacoId, long sacoSuinId, String sacoSuinName,
			String sacoType, Date sacoDate, double sacoTime,
			String sacoContent, String sacoIp) {
		this.sacoId = sacoId;
		this.sacoSuinId = sacoSuinId;
		this.sacoSuinName = sacoSuinName;
		this.sacoType = sacoType;
		this.sacoDate = sacoDate;
		this.sacoTime = sacoTime;
		this.sacoContent = sacoContent;
		this.sacoIp = sacoIp;
	}

	// Property accessors

	public long getSacoId() {
		return this.sacoId;
	}

	public void setSacoId(long sacoId) {
		this.sacoId = sacoId;
	}

	public long getSacoSuinId() {
		return this.sacoSuinId;
	}

	public void setSacoSuinId(long sacoSuinId) {
		this.sacoSuinId = sacoSuinId;
	}

	public String getSacoSuinName() {
		return this.sacoSuinName;
	}

	public void setSacoSuinName(String sacoSuinName) {
		this.sacoSuinName = sacoSuinName;
	}

	public String getSacoType() {
		return this.sacoType;
	}

	public void setSacoType(String sacoType) {
		this.sacoType = sacoType;
	}

	public Date getSacoDate() {
		return this.sacoDate;
	}

	public void setSacoDate(Date sacoDate) {
		this.sacoDate = sacoDate;
	}

	public double getSacoTime() {
		return this.sacoTime;
	}

	public void setSacoTime(double sacoTime) {
		this.sacoTime = sacoTime;
	}

	public String getSacoContent() {
		return this.sacoContent;
	}

	public void setSacoContent(String sacoContent) {
		this.sacoContent = sacoContent;
	}

	public String getSacoIp() {
		return this.sacoIp;
	}

	public void setSacoIp(String sacoIp) {
		this.sacoIp = sacoIp;
	}

}
