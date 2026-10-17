package cn.com.adtech.riss2.domain.resource;

/**
 * ResImportTemplet entity.
 * 
 * @author MaChao
 */

public class ResImportTemplet implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = -7500292529759080709L;
	private Long riteId;
	private Long riteRepoId;
	private String riteColumn;
	private Boolean riteIsFormat;
	private String titeRemark;
	
	private Long resRmstId; //特色库ID
	private Long resRltyId; //资源类型ID
	
	private String elementName; //元素名称
	private String repoRestrict;//元素约束类型
	private String elementTitle;//元素标识
	
	
	
	
	/**学科分类ID——1*/
	final public static Long RSDA_RSTY_ID = 1L;
	
	/**中图法分类ID——2*/
	final public static Long RSDA_MIDDLENAME_ID = 2L;
	
	/**自定义分类一ID——3*/
	final public static Long RSDA_RDTY_ONE_ID = 3L;
	
	/**自定义分类二ID——4**/
	final public static Long RSDA_RDTY_TWO_ID = 4L;
	
	/**自定义分类三ID——5**/
	final public static Long RSDA_RDTY_THREE_ID = 5L;
	
	
	/**文件名称ID——6*/
	final public static Long RSDA_FILENAME_ID = 6L;
	
	
	/**资源主题*/
	final public static Long RSDA_RSSU_ID = 7L;
	
	
	// Constructors
	
	public ResImportTemplet(Long rmstId, Long rltyId, Long repoId,String riteColumn, String remark, Boolean b){
		this.resRmstId = rmstId;
		this.resRltyId = rltyId;
		this.riteRepoId = repoId;
		this.riteColumn = riteColumn;
		this.titeRemark = remark;
		this.riteIsFormat = b;
	}
	public ResImportTemplet(Long rmstId, Long rltyId, Long repoId,String riteColumn){
		this.resRmstId = rmstId;
		this.resRltyId = rltyId;
		this.riteRepoId = repoId;
		this.riteColumn = riteColumn;
		this.titeRemark = null;
		this.riteIsFormat = false;
	}

	public Long getResRmstId() {
		return resRmstId;
	}

	public void setResRmstId(Long resRmstId) {
		this.resRmstId = resRmstId;
	}

	public Long getResRltyId() {
		return resRltyId;
	}

	public void setResRltyId(Long resRltyId) {
		this.resRltyId = resRltyId;
	}

	/** default constructor */
	public ResImportTemplet() {
	}

	// Property accessors

	public Long getRiteId() {
		return this.riteId;
	}

	public void setRiteId(Long riteId) {
		this.riteId = riteId;
	}

	public Long getRiteRepoId() {
		return this.riteRepoId;
	}

	public void setRiteRepoId(Long riteRepoId) {
		this.riteRepoId = riteRepoId;
	}

	public String getRiteColumn() {
		return this.riteColumn;
	}

	public void setRiteColumn(String riteColumn) {
		this.riteColumn = riteColumn;
	}

	public String getTiteRemark() {
		return this.titeRemark;
	}

	public void setTiteRemark(String titeRemark) {
		this.titeRemark = titeRemark;
	}

	public Boolean getRiteIsFormat() {
		return riteIsFormat;
	}

	public void setRiteIsFormat(Boolean riteIsFormat) {
		this.riteIsFormat = riteIsFormat;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getRepoRestrict() {
		return repoRestrict;
	}
	public void setRepoRestrict(String repoRestrict) {
		this.repoRestrict = repoRestrict;
	}
	public String getElementTitle() {
		return elementTitle;
	}
	public void setElementTitle(String elementTitle) {
		this.elementTitle = elementTitle;
	}

}
