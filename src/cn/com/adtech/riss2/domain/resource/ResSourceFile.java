package cn.com.adtech.riss2.domain.resource;

/**
 * 资源文件对象
 * 
 * @author MaChao
 */

public class ResSourceFile implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long rsfiId;
	private Long resRsdaId;
	private String resFileName;
	private String resLocalPath;
	private String resFilePath;
	private String resViewPath;
	private String resFileRemark;
	private Integer resIndex;
	
	/**
	 * 访问权限类型
	 */
	private String resFileAccessType;
	
	/**访问权限类型——公开***/
	final public static String FILE_ACCESS_PUBLIC = "PUBLIC";
	
	/**访问权限类型——不公开***/
	final public static String FILE_ACCESS_PRIVATE = "PRIVATE";

	// Constructors

	/** default constructor */
	public ResSourceFile() {
	}

	/** full constructor */
	// Property accessors

	public Long getRsfiId() {
		return this.rsfiId;
	}

	public void setRsfiId(Long rsfiId) {
		this.rsfiId = rsfiId;
	}

	public Long getResRsdaId() {
		return this.resRsdaId;
	}

	public void setResRsdaId(Long resRsdaId) {
		this.resRsdaId = resRsdaId;
	}

	public String getResLocalPath() {
		return this.resLocalPath;
	}

	public void setResLocalPath(String resLocalPath) {
		this.resLocalPath = resLocalPath;
	}

	public String getResFilePath() {
		return this.resFilePath;
	}

	public void setResFilePath(String resFilePath) {
		this.resFilePath = resFilePath;
	}

	public String getResViewPath() {
		return this.resViewPath;
	}

	public void setResViewPath(String resViewPath) {
		this.resViewPath = resViewPath;
	}

	public String getResFileRemark() {
		return this.resFileRemark;
	}

	public void setResFileRemark(String resFileRemark) {
		this.resFileRemark = resFileRemark;
	}

	public Integer getResIndex() {
		return this.resIndex;
	}

	public void setResIndex(Integer resIndex) {
		this.resIndex = resIndex;
	}

	public String getResFileName() {
		return resFileName;
	}

	public void setResFileName(String resFileName) {
		this.resFileName = resFileName;
	}

	public String getResFileAccessType() {
		return resFileAccessType;
	}

	public void setResFileAccessType(String resFileAccessType) {
		this.resFileAccessType = resFileAccessType;
	}

}
