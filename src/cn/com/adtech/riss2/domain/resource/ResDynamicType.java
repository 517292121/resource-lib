package cn.com.adtech.riss2.domain.resource;

@SuppressWarnings("serial")
public class ResDynamicType implements java.io.Serializable {
	// Fields
	private Long rdtyId;

	private Long rmstId;

	private ResMetaStore resMetaStore;

	private String rdtyName;

	private Long rdtyGrade;

	private Long rdtyPid;

	//private ResDynamicType parentResDynamicType;

	private Integer rdtyIndex; // 排序列
	
	
	private String rdtyCode; //分类号
	private Integer rdtyCount; //资源统计量
	
	private String parentCode; //上级分类号，用于批量导入临时使用
	
	/**
	 * 自定义分类类型
	 * 1:自定义分类一
	 * 2:自定义分类二
	 * 3:自定义分类三
	 */
	private Integer rdtyType;
	
	/**2018扩展*/
	private String rdtyZwName;//藏文名称
	private String rdtyEnName;//英文名称

	public String getRdtyZwName() {
		return rdtyZwName;
	}

	public void setRdtyZwName(String rdtyZwName) {
		this.rdtyZwName = rdtyZwName;
	}

	public String getRdtyEnName() {
		return rdtyEnName;
	}

	public void setRdtyEnName(String rdtyEnName) {
		this.rdtyEnName = rdtyEnName;
	}

	public ResDynamicType() {}
	
	public ResDynamicType(Long rdtyId, Long rdtyPid, String rdtyName, Long rdtyGrade, Integer rdtyIndex, Integer rdtyType){
		this.rdtyId = rdtyId;
		this.rdtyPid = rdtyPid;
		this.rdtyName = rdtyName;
		this.rdtyGrade = rdtyGrade;
		this.rdtyIndex = rdtyIndex;
		this.rdtyType = rdtyType;
	}

	public Long getRdtyId() {
		return this.rdtyId;
	}

	public void setRdtyId(Long rdtyId) {
		this.rdtyId = rdtyId;
	}

	public ResMetaStore getResMetaStore() {
		return this.resMetaStore;
	}

	public void setResMetaStore(ResMetaStore resMetaStore) {
		this.resMetaStore = resMetaStore;
	}

	public Long getRdtyPid() {
		return this.rdtyPid;
	}

	public void setRdtyPid(Long rdtyPid) {
		this.rdtyPid = rdtyPid;
	}

	public String getRdtyName() {
		return this.rdtyName;
	}

	public void setRdtyName(String rdtyName) {
		this.rdtyName = rdtyName;
	}

	public Long getRdtyGrade() {
		return this.rdtyGrade;
	}

	public void setRdtyGrade(Long rdtyGrade) {
		this.rdtyGrade = rdtyGrade;
	}

	public Long getRmstId() {
		return rmstId;
	}

	public void setRmstId(Long rmstId) {
		this.rmstId = rmstId;
	}

//	public ResDynamicType getParentResDynamicType() {
//		return parentResDynamicType;
//	}
//
//	public void setParentResDynamicType(ResDynamicType parentResDynamicType) {
//		this.parentResDynamicType = parentResDynamicType;
//	}

	public Integer getRdtyIndex() {
		return rdtyIndex;
	}

	public void setRdtyIndex(Integer rdtyIndex) {
		this.rdtyIndex = rdtyIndex;
	}

	public Integer getRdtyType() {
		return rdtyType;
	}

	public void setRdtyType(Integer rdtyType) {
		this.rdtyType = rdtyType;
	}

	public String getRdtyCode() {
		return rdtyCode;
	}

	public void setRdtyCode(String rdtyCode) {
		this.rdtyCode = rdtyCode;
	}

	public Integer getRdtyCount() {
		return rdtyCount;
	}

	public void setRdtyCount(Integer rdtyCount) {
		this.rdtyCount = rdtyCount;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}
}