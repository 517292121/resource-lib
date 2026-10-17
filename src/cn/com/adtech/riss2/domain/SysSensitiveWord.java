package cn.com.adtech.riss2.domain;

/**
 * 系统敏感词实体类
 * @author MaChao
 * 2011-04-21
 */

public class SysSensitiveWord implements java.io.Serializable {

	private Long sswId;
	private String sswName;
	private Integer sswIndex;
	
	public SysSensitiveWord() {
		super();
	}
	
	public SysSensitiveWord(Long sswId, String sswName, Integer sswIndex) {
		super();
		this.sswId = sswId;
		this.sswName = sswName;
		this.sswIndex = sswIndex;
	}

	public Long getSswId() {
		return sswId;
	}

	public void setSswId(Long sswId) {
		this.sswId = sswId;
	}

	public String getSswName() {
		return sswName;
	}

	public void setSswName(String sswName) {
		this.sswName = sswName;
	}

	public Integer getSswIndex() {
		return sswIndex;
	}

	public void setSswIndex(Integer sswIndex) {
		this.sswIndex = sswIndex;
	}
	
}
