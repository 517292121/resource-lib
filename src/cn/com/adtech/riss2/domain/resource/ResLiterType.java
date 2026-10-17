package cn.com.adtech.riss2.domain.resource;

/**
 * 文献类型
 * 
 * @author MaChao
 * @date 2010-8-31
 */
@SuppressWarnings("serial")
public class ResLiterType implements java.io.Serializable {
	// Constants三大类
	
	/** 没有此类型 */
	public static final Short TYPE_NO_TYPES = Short.valueOf("0");
	/** 文档 */
	public static final Short TYPE_DOCUMENT = Short.valueOf("1"); // 文档
	/** 图片 */
	public static final Short TYPE_PICTURE = Short.valueOf("2"); // 图片

	/** 音频 */
	public static final Short TYPE_MEDIA = Short.valueOf("3"); // 音频
	
	/** 视频 */
	public static final Short TYPE_VIDEO = Short.valueOf("4"); // 视频

	// Fields
	private Long rltyId;

	private String rltyName;

	private Short rltyType;
	
	/**资源类型编码*/
	private String rltyCode;

	// Constructors
	/** default constructor */
	public ResLiterType() {}

	/** minimal constructor */
	public ResLiterType(String rltyName) {
		this.rltyName = rltyName;
	}

	// Property accessors
	public Long getRltyId() {
		return this.rltyId;
	}

	public void setRltyId(Long rltyId) {
		this.rltyId = rltyId;
	}

	public String getRltyName() {
		return this.rltyName;
	}

	public void setRltyName(String rltyName) {
		this.rltyName = rltyName;
	}

	public Short getRltyType() {
		return rltyType;
	}

	public void setRltyType(Short rltyType) {
		this.rltyType = rltyType;
	}

	public ResLiterType(Short rltyType, String rltyName) {
		super();
		this.rltyType = rltyType;
		this.rltyName = rltyName;
	}

	public String getRltyCode() {
		return rltyCode;
	}

	public void setRltyCode(String rltyCode) {
		this.rltyCode = rltyCode;
	}
}
