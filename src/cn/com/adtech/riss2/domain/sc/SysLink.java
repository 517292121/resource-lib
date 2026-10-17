package cn.com.adtech.riss2.domain.sc;

/**
 * SysLink entity.
 * 
 * @author MaChao
 */

public class SysLink implements java.io.Serializable {

	// Fields

	private Long slinId;
	private String slinName;
	private String slinImgpath;
	private String slinLinkpath;
	private Integer slinType;
	private Integer slinIndex;

	// Constructors

	/** default constructor */
	public SysLink() {
	}


	public Long getSlinId() {
		return this.slinId;
	}

	public void setSlinId(Long slinId) {
		this.slinId = slinId;
	}

	public String getSlinName() {
		return this.slinName;
	}

	public void setSlinName(String slinName) {
		this.slinName = slinName;
	}

	public String getSlinImgpath() {
		return this.slinImgpath;
	}

	public void setSlinImgpath(String slinImgpath) {
		this.slinImgpath = slinImgpath;
	}

	public String getSlinLinkpath() {
		return this.slinLinkpath;
	}

	public void setSlinLinkpath(String slinLinkpath) {
		this.slinLinkpath = slinLinkpath;
	}

	public Integer getSlinType() {
		return this.slinType;
	}

	public void setSlinType(Integer slinType) {
		this.slinType = slinType;
	}

	public Integer getSlinIndex() {
		return this.slinIndex;
	}

	public void setSlinIndex(Integer slinIndex) {
		this.slinIndex = slinIndex;
	}

}
