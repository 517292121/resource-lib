package cn.com.adtech.riss2.domain.resource;

import java.util.HashSet;
import java.util.Set;

import cn.com.adtech.riss2.jdbcms.ToolUnit;

/**
 * 元素池
 * 
 * @author MaChao
 * @date 2010-9-3
 */
@SuppressWarnings("serial")
public class ResElementPool implements java.io.Serializable {
	/**1:核心元素*/
	public static final Long REPO_TYPE_CORE = 1l;
	// Fields
	private Long repoId;

	private Long repoPltyId;

	private ResLiterType resLiterType;

	private String repoName;

	private String repoTitle;

	private Long repoType;

	private String repoCode;

	private String repoRestrict;

	private int repoSerianum;

	private Long repoParent;

	private String repoData; // 元素值，主要用于查询的时候方便封装值。
	private String excelColumn; //批量导入中和Excel中对应的列,主要用于查询的时候方便封装值。

	private Set<ResElementPool> childelements = new HashSet<ResElementPool>();
	
	private String repoDefaultdata;//元素默认值
	private Integer repoShowmode; //元素在前台显示方式(0或null:不为空则显示、1：显示、2：不显示)
	
	
	private Integer repoDatatype; //元素值录入类型 0:输入框、1：下拉
	private String repoDataOptions; //下拉数据选项
	
	private Integer repoCalis; //是否为calis收割.1:收割、0或null为不收割
	
	private Integer repoIsShowList; //是否在资源列表页面显示 0：默认不显示 1：显示
	
	private Integer repoIndex; //序号 控制显示顺序
	
	public Integer getRepoIsShowList() {
		return repoIsShowList;
	}

	public void setRepoIsShowList(Integer repoIsShowList) {
		this.repoIsShowList = repoIsShowList;
	}

	public Integer getRepoIndex() {
		return repoIndex;
	}

	public void setRepoIndex(Integer repoIndex) {
		this.repoIndex = repoIndex;
	}

	public Integer getRepoCalis() {
		return repoCalis;
	}

	public void setRepoCalis(Integer repoCalis) {
		this.repoCalis = repoCalis;
	}

	public Set<ResElementPool> getChildelements() {
		return childelements;
	}

	public void setChildelements(Set<ResElementPool> childelements) {
		this.childelements = childelements;
	}

	// Constructors
	/** default constructor */
	public ResElementPool() {}

	// Property accessors
	public Long getRepoId() {
		return this.repoId;
	}

	public void setRepoId(Long repoId) {
		this.repoId = repoId;
	}

	public Long getRepoPltyId() {
		return this.repoPltyId;
	}

	public void setRepoPltyId(Long repoPltyId) {
		this.repoPltyId = repoPltyId;
	}

	public String getRepoName() {
		return ToolUnit.NullToBlank(this.repoName);
	}

	public void setRepoName(String repoName) {
		this.repoName = repoName;
	}

	public String getRepoTitle() {
		return this.repoTitle;
	}

	public void setRepoTitle(String repoTitle) {
		this.repoTitle = repoTitle;
	}

	public Long getRepoType() {
		return this.repoType;
	}

	public void setRepoType(Long repoType) {
		this.repoType = repoType;
	}

	public String getRepoCode() {
		return this.repoCode;
	}

	public void setRepoCode(String repoCode) {
		this.repoCode = repoCode;
	}

	public String getRepoRestrict() {
		return this.repoRestrict;
	}

	public void setRepoRestrict(String repoRestrict) {
		this.repoRestrict = repoRestrict;
	}

	public ResLiterType getResLiterType() {
		return resLiterType;
	}

	public void setResLiterType(ResLiterType resLiterType) {
		this.resLiterType = resLiterType;
	}

	public int getRepoSerianum() {
		return repoSerianum;
	}

	public void setRepoSerianum(int repoSerianum) {
		this.repoSerianum = repoSerianum;
	}

	public Long getRepoParent() {
		return repoParent;
	}

	public void setRepoParent(Long repoParent) {
		this.repoParent = repoParent;
	}

	public String getRepoData() {
		return repoData;
	}

	public void setRepoData(String reopData) {
		this.repoData = reopData;
	}

	public String getExcelColumn() {
		return excelColumn;
	}

	public void setExcelColumn(String excelColumn) {
		this.excelColumn = excelColumn;
	}

	public String getRepoDefaultdata() {
		return repoDefaultdata;
	}

	public void setRepoDefaultdata(String repoDefaultdata) {
		this.repoDefaultdata = repoDefaultdata;
	}

	public Integer getRepoShowmode() {
		return repoShowmode;
	}

	public void setRepoShowmode(Integer repoShowmode) {
		this.repoShowmode = repoShowmode;
	}

	public Integer getRepoDatatype() {
		return repoDatatype;
	}

	public void setRepoDatatype(Integer repoDatatype) {
		this.repoDatatype = repoDatatype;
	}

	public String getRepoDataOptions() {
		return repoDataOptions;
	}

	public void setRepoDataOptions(String repoDataOptions) {
		this.repoDataOptions = repoDataOptions;
	}
}
