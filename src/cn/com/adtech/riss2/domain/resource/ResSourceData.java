package cn.com.adtech.riss2.domain.resource;

import java.util.Date;

import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;

public class ResSourceData implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** RSDA_ISUSE 可用 */
	public static final Long ISUSE_USEABLENESS = 1l;

	/** RSDA_ISUSE 冻结 */
	public static final Long ISUSE_FREEZE = 0l;

	/** 首页随机资源条数*/
	public static final Short RANDOM_NUMBER = 10;

	/** 是否为推荐资源 */
	public static final Long IS_RECOMMEND = 1l;

	/** 评论开关状态(开)RSDA_CMTSTUS */
	public static final Long REVIEW_SWITCH_OPEN = 1l;

	/** 评论开关状态(关)RSDA_CMTSTUS */
	public static final Long REVIEW_SWITCH_CLOSE = 0l;
	
	/** 2:待审核 */
	public static final Long WAITING_CHECK = 2L;
	
	/** 4:审核未通过 */
	public static final Long NO_PAST_CHECK = 4L;

	private Long rsdaRssuId;
	/******截取的年份*/
	private Long rsdaYear;
	
	// Fields
	private Long rsdaId;

	private ResDynamicType resDynamicType;

	//特色库自定义分类id，主要是为了做索引
	private Long rsdaRdtyId;

	private SysManager sysManager;
	
	/**
	 * 录入者ID
	 */
	private Long rsdaSmanId;

	//@IndexedEmbedded(prefix="resMissionInfo_")
	private ResMissionInfo resMissionInfo;
	
	/**
	 * 作业ID
	 */
	private Long rsdaRminId;
	
	/**
	 * 特色库ID
	 */
	private Long rsdaRmstId;
	
	//@IndexedEmbedded(prefix="resMetaStore_")
	private ResMetaStore resMetaStore;
	
	// 特色库状态,主要是为了检索用puzhijun
	// 主要是特色库状态改变的话有两种方式，一种是利用ResMetaStoreStatusChangeListener中onPostUpdate方法自动建增量索引，
	// 	可以达到实时的效果，但是数据量大的话时间会非常的长，不是很推荐
	// 一种就是通过现在这种，重建索引来实现，但是不能达到实时的效果
	//@Field(name="resMetaStoreStatus", index=Index.UN_TOKENIZED)
//	private Long resMetaStoreStatus;
	
	/**
	 *  资源类型id，主要是为了做索引，检索用 puzhijun
	 */
	private Long rsdaRltyId;
	
	private ResLiterType resLiterType;

//	private Long resSubjectTypeId;
	/**
	 * 学科分类id，主要是为了做索引，分类导航 puzhijun
	 */
	private Long rsdaRstyId;
	
	/**
	 * 学科分类
	 */
	private ResSubjectType resSubjectType;
	
	/**
	 * 录入时间
	 */
	private Date rsdaBuilddate;

	/**
	 * 状态
	 */
	private Long rsdaStatus;
	
	/**
	 * 评论开关状态
	 */
	private Long rsdaCmtstus;
	
	/**
	 * 是否为推荐资源
	 */
	private Long rsdaRcmd;
	
	/**所属加工会员ID**/
	private Long rsdaUserId;
	
	/**
	 * 点击量
	 */
	private Long rsdaLooknum;
	
	/**
	 * 评分
	 */
	private Long rsdaGrade;
	
	/**
	 * 题名
	 */
	private String rsdaTitle;
	
	/**
	 * 主题
	 */
	private String rsdaSubject;
	
	/**
	 * 描述
	 */
	private String rsdaDescription;
	
	/**
	 * 来源
	 */
	private String rsdaSource;
	
	/**
	 * 语种
	 */
	private String rsdaLanguage;
	
	/**
	 * 关联
	 */
	private String rsdaRelation;
	
	/**
	 * 覆盖范围
	 */
	private String rsdaCoverage;
	
	/**
	 * 创建者
	 */
	private String rsdaCreator;
	
	/**
	 * 出版者
	 */
	private String rsdaPublisher;
	
	/**
	 * 其他责任者
	 */
	private String rsdaContributor;
	
	/**
	 * 权限
	 */
	private String rsdaRights;
	
	/**
	 * 日期
	 */
	//@FieldBridge(impl=RsdaDateStringToYearString.class)
	private String rsdaDate;
	
	/**
	 * 类型
	 */
	private String rsdaType;
	
	/**
	 * 格式
	 */
	private String rsdaFormat;
	
	/**
	 * 标识符
	 */
	private String rsdaIdentifier;

	/**
	 * 是否冻结
	 * 可用：1 ； 不可用：0；
	 */ 
	private Long rsdaIsuse;
	
	/**
	 * 原始文件名
	 */
	private String rsdaFileName;

	/**
	 * 原始文件下载链接
	 */
	private String rsdaFileUrl;

	/**
	 * 原始文件SWF链接
	 */
	private String rsdaFileSwfUrl;
	

	/**
	 * 原始文件物理文件名
	 */
	private String rsdaFileLocalName;

	/**
	 * 预览图物理文件名
	 */
	private String rsdaBigimgLocalName;

	/**
	 * 预览图URL
	 */
	private String rsdaBigimgUrl;

	/**
	 * 预览图文件名
	 */
	private String rsdaBigimgName;
	
	/**特色库状态*/
	private Long rsdaRmstStatus;
	
	/**作业状态*/
	private Long rsdaRminStatus;
	
	/**
	 * 不要持久化此字段，主要是为了hibernatesearch做高亮关键字操作用
	 */
	private transient String findResultStr;
	
	private String rsdaFileAddress; // 文件服务器地址
	
	/**
	 * 封面临时设置字段
	 */
	private String rsdaPhotoImage;

	// 资源来源类型
	// 如果是会员录入则没有作业
	// 取值为AUTHORTYPE_ADMIN_INPUT:管理员录入, AUTHORTYPE_ADMIN_IMPORT: 管理员导入, AUTHORTYPE_MEMBER_INPUT: 会员上传
	private Integer rsdaAuthortype;
	
	private Long rsdaRihiId;  //所属Excel导入批次
	
	//文件格式，如doc、txt等
	private String rsdaFileType;
	
	// 高级检索资源标题截取结果
	private String advanceResultStr;
	
	/* 状态 */
	public static final Long STATUS_CHECK = 2L; // 待审核

	public static final Long STATUS_PASS = 3L; // 审核通过

	public static final Long STATUS_NOTPASS = 4L; // 审核未通过
	
	
	/**资源上传者类型**/
	/**
	 * 管理员录入
	 */
	final public static Integer AUTHORTYPE_ADMIN_INPUT = 1;
	
	/**
	 * 管理员导入
	 */
	final public static Integer AUTHORTYPE_ADMIN_IMPORT = 2;
	
	/**
	 * 会员录入
	 */
	final public static Integer AUTHORTYPE_MEMBER_INPUT = 3;
	
	/**论文同步过来**/
	final public static Integer AUTHORTYPE_PAPER = 4;
	
	/**采集系统导入**/
	final public static Integer AUTHORTYPE_GATHER = 5;
	
	
	/*************************************************************
	 * 修改 2011-07-25 
	 * author 王林
	 * 将加第二自定义分类和第三自定义分类外键ID	 *
	 */
	
	/** 自定义分类二ID */
	private Long rsdaRdtyTwo;
	
	/** 自定义分类三ID */
	private Long rsdaRdtyThree;
	
	/**源文大小*/
	private Long rsdaFileSize;
	
	private String rsdaKeyword;
	
	/**临时存放资源的核心元数据信息*/	
	private String rsdaDetailData;
	
	//资源题名首字母
	private String rsdaTitleEn;
	
	/**
	 * 资源页码和检索类型
	 */
	private String rsdaPage;
	
	/**
	 * 源文类型
	 * 0：传统文件与swf预览方式
	 * 1：带目录结构的（老藏文文献网资源管理方式）
	 * 2：图文对照方式（古籍图文对照方式）
	 * 3：纯图片预览文件（也支持全文检索）
	 * 4：html预览方式
	 */
	private Integer rsdaSourceType;
	
	
	public String getRsdaTitleEn() {
		return rsdaTitleEn;
	}

	public void setRsdaTitleEn(String rsdaTitleEn) {
		this.rsdaTitleEn = rsdaTitleEn;
	}

	public String getRsdaKeyword() {
		return rsdaKeyword;
	}

	public void setRsdaKeyword(String rsdaKeyword) {
		this.rsdaKeyword = rsdaKeyword;
	}

	public ResSourceData(long id, String imageUrl, String title, long grade, long looknum){
		this.rsdaId = id;
		this.rsdaBigimgUrl = imageUrl;
		this.rsdaTitle = title;
		this.rsdaGrade = grade;
		this.rsdaLooknum = looknum;
	}
	
	public ResSourceData(long id, String imageUrl, String title, long grade, long looknum, long rltyId ){
		this.rsdaId = id;
		this.rsdaBigimgUrl = imageUrl;
		this.rsdaTitle = title;
		this.rsdaGrade = grade;
		this.rsdaLooknum = looknum;
		this.rsdaRltyId = rltyId;
	}
	
	public ResSourceData(long id, String imageUrl, String title, long grade, long looknum, long rltyId, String rsdaDescription, Date rsdaBuilddate, ResLiterType literType, String rsdaCreator, String rsdaPublisher ){
		this.rsdaId = id;
		this.rsdaBigimgUrl = imageUrl;
		this.rsdaTitle = title;
		this.rsdaGrade = grade;
		this.rsdaLooknum = looknum;
		this.rsdaRltyId = rltyId;
		this.rsdaDescription = rsdaDescription;
		this.rsdaBuilddate = rsdaBuilddate;
		this.resLiterType = literType;
		this.rsdaCreator = rsdaCreator;
		this.rsdaPublisher = rsdaPublisher;
	}
	
	public ResSourceData(long id, String imageUrl, String title, long grade, long looknum, long rltyId, String rsdaDescription, Date rsdaBuilddate, ResLiterType literType, String rsdaCreator, String rsdaPublisher,String rsdaKeyword, String rsdaFileType){
		this.rsdaId = id;
		this.rsdaBigimgUrl = imageUrl;
		this.rsdaTitle = title;
		this.rsdaGrade = grade;
		this.rsdaLooknum = looknum;
		this.rsdaRltyId = rltyId;
		this.rsdaDescription = rsdaDescription;
		this.rsdaBuilddate = rsdaBuilddate;
		this.resLiterType = literType;
		this.rsdaCreator = rsdaCreator;
		this.rsdaPublisher = rsdaPublisher;
		this.rsdaKeyword = rsdaKeyword;
		this.rsdaFileType = rsdaFileType;
	}
	
	public String getRsdaFileName() {
		return rsdaFileName;
	}

	public void setRsdaFileName(String rsdaFileName) {
		this.rsdaFileName = rsdaFileName;
	}

	public String getRsdaFileUrl() {
		return rsdaFileUrl;
	}

	public void setRsdaFileUrl(String rsdaFileUrl) {
		this.rsdaFileUrl = rsdaFileUrl;
	}

	public Long getRsdaIsuse() {
		return rsdaIsuse;
	}

	public void setRsdaIsuse(Long rsdaIsuse) {
		this.rsdaIsuse = rsdaIsuse;
	}

	// Constructors
	public ResSourceData() {}

	
	public Long getRsdaId() {
		return rsdaId;
	}

	public void setRsdaId(Long rsdaId) {
		this.rsdaId = rsdaId;
	}

	public ResDynamicType getResDynamicType() {
		return resDynamicType;
	}

	public void setResDynamicType(ResDynamicType resDynamicType) {
		this.resDynamicType = resDynamicType;
	}

	public Long getRsdaRdtyId() {
		return rsdaRdtyId;
	}

	public void setRsdaRdtyId(Long rsdaRdtyId) {
		this.rsdaRdtyId = rsdaRdtyId;
	}

	public Long getRsdaRmstId() {
		return rsdaRmstId;
	}

	public void setRsdaRmstId(Long rsdaRmstId) {
		this.rsdaRmstId = rsdaRmstId;
	}

	public ResMetaStore getResMetaStore() {
		return resMetaStore;
	}

	public void setResMetaStore(ResMetaStore resMetaStore) {
		this.resMetaStore = resMetaStore;
	}

	public SysManager getSysManager() {
		return sysManager;
	}

	public void setSysManager(SysManager sysManager) {
		this.sysManager = sysManager;
	}

	public Long getRsdaSmanId() {
		return rsdaSmanId;
	}

	public void setRsdaSmanId(Long rsdaSmanId) {
		this.rsdaSmanId = rsdaSmanId;
	}

	public ResMissionInfo getResMissionInfo() {
		return resMissionInfo;
	}

	public void setResMissionInfo(ResMissionInfo resMissionInfo) {
		this.resMissionInfo = resMissionInfo;
	}

	public Long getRsdaRminId() {
		return rsdaRminId;
	}

	public void setRsdaRminId(Long rsdaRminId) {
		this.rsdaRminId = rsdaRminId;
	}

	public ResSubjectType getResSubjectType() {
		return resSubjectType;
	}

	public void setResSubjectType(ResSubjectType resSubjectType) {
		this.resSubjectType = resSubjectType;
	}

	public Long getRsdaRstyId() {
		return rsdaRstyId;
	}

	public void setRsdaRstyId(Long rsdaRstyId) {
//		this.resSubjectTypeId=this.rsdaRstyId;
//		if(this.rsdaRstyId == null )this.resSubjectTypeId=0L;
		this.rsdaRstyId = rsdaRstyId;
	}

	public ResLiterType getResLiterType() {
		return resLiterType;
	}

	public void setResLiterType(ResLiterType resLiterType) {
		this.resLiterType = resLiterType;
	}

	public Long getRsdaRltyId() {
		return rsdaRltyId;
	}

	public void setRsdaRltyId(Long rsdaRltyId) {
		this.rsdaRltyId = rsdaRltyId;
	}

	public Date getRsdaBuilddate() {
		return rsdaBuilddate;
	}

	public void setRsdaBuilddate(Date rsdaBuilddate) {
		this.rsdaBuilddate = rsdaBuilddate;
	}

	public Long getRsdaStatus() {
		return rsdaStatus;
	}

	public void setRsdaStatus(Long rsdaStatus) {
		this.rsdaStatus = rsdaStatus;
	}

	
	public Long getRsdaGrade() {
		return rsdaGrade;
	}

	public void setRsdaGrade(Long rsdaGrade) {
		this.rsdaGrade = rsdaGrade;
	}

	public Long getRsdaCmtstus() {
		return rsdaCmtstus;
	}

	public void setRsdaCmtstus(Long rsdaCmtstus) {
		this.rsdaCmtstus = rsdaCmtstus;
	}

	public Long getRsdaRcmd() {
		return rsdaRcmd;
	}

	public void setRsdaRcmd(Long rsdaRcmd) {
		this.rsdaRcmd = rsdaRcmd;
	}

	public Long getRsdaLooknum() {
		return rsdaLooknum;
	}

	public void setRsdaLooknum(Long rsdaLooknum) {
		this.rsdaLooknum = rsdaLooknum;
	}

	public String getRsdaTitle() {
		return rsdaTitle;
	}

	public void setRsdaTitle(String rsdaTitle) {
		this.rsdaTitle = rsdaTitle;
	}

	
	public String getRsdaSubject() {
		return rsdaSubject;
	}

	public void setRsdaSubject(String rsdaSubject) {
		this.rsdaSubject = rsdaSubject;
	}

	
	public String getRsdaDescription() {
		return rsdaDescription;
	}

	public void setRsdaDescription(String rsdaDescription) {
		this.rsdaDescription = rsdaDescription;
	}

	
	public String getRsdaSource() {
		return rsdaSource;
	}

	public void setRsdaSource(String rsdaSource) {
		this.rsdaSource = rsdaSource;
	}

	
	public String getRsdaLanguage() {
		return rsdaLanguage;
	}

	public void setRsdaLanguage(String rsdaLanguage) {
		this.rsdaLanguage = rsdaLanguage;
	}

	
	public String getRsdaRelation() {
		return rsdaRelation;
	}

	public void setRsdaRelation(String rsdaRelation) {
		this.rsdaRelation = rsdaRelation;
	}
	
	
	public String getRsdaCoverage() {
		return rsdaCoverage;
	}

	public void setRsdaCoverage(String rsdaCoverage) {
		this.rsdaCoverage = rsdaCoverage;
	}

	
	public String getRsdaCreator() {
		return rsdaCreator;
	}

	public void setRsdaCreator(String rsdaCreator) {
		this.rsdaCreator = rsdaCreator;
	}

	
	public String getRsdaPublisher() {
		return rsdaPublisher;
	}

	public void setRsdaPublisher(String rsdaPublisher) {
		this.rsdaPublisher = rsdaPublisher;
	}

	
	public String getRsdaContributor() {
		return rsdaContributor;
	}

	public void setRsdaContributor(String rsdaContributor) {
		this.rsdaContributor = rsdaContributor;
	}

	
	public String getRsdaRights() {
		return rsdaRights;
	}

	public void setRsdaRights(String rsdaRights) {
		this.rsdaRights = rsdaRights;
	}

	
	public String getRsdaDate() {
		return rsdaDate;
	}

	public void setRsdaDate(String rsdaDate) {
		this.rsdaDate = rsdaDate;
	}

	
	public String getRsdaType() {
		return rsdaType;
	}

	public void setRsdaType(String rsdaType) {
		this.rsdaType = rsdaType;
	}

	
	public String getRsdaFormat() {
		return rsdaFormat;
	}

	public void setRsdaFormat(String rsdaFormat) {
		this.rsdaFormat = rsdaFormat;
	}

	
	public String getRsdaIdentifier() {
		return rsdaIdentifier;
	}

	public void setRsdaIdentifier(String rsdaIdentifier) {
		this.rsdaIdentifier = rsdaIdentifier;
	}

	public String getRsdaFileLocalName() {
		return rsdaFileLocalName;
	}

	public void setRsdaFileLocalName(String rsdaFileLocalName) {
		this.rsdaFileLocalName = rsdaFileLocalName;
	}

	public String getRsdaBigimgLocalName() {
		return rsdaBigimgLocalName;
	}

	public void setRsdaBigimgLocalName(String rsdaBigimgLocalName) {
		this.rsdaBigimgLocalName = rsdaBigimgLocalName;
	}

	public String getRsdaBigimgUrl() {
		return rsdaBigimgUrl;
	}

	public void setRsdaBigimgUrl(String rsdaBigimgUrl) {
		this.rsdaBigimgUrl = rsdaBigimgUrl;
	}

	public String getRsdaBigimgName() {
		return rsdaBigimgName;
	}

	public void setRsdaBigimgName(String rsdaBigimgName) {
		this.rsdaBigimgName = rsdaBigimgName;
	}

	public String getRsdaFileSwfUrl() {
		return rsdaFileSwfUrl;
	}

	public void setRsdaFileSwfUrl(String rsdaFileSwfUrl) {
		this.rsdaFileSwfUrl = rsdaFileSwfUrl;
	}
	
	public String getFindResultStr() {
		return findResultStr;
	}

	public void setFindResultStr(String findResultStr) {
		this.findResultStr = findResultStr;
	}

	public String getSubRsdsaTitleTable() {
		if(this.rsdaTitle.length() > 14)
			return ServiceUtil.subString(this.rsdaTitle, 14, "");
		return this.rsdaTitle;
	}

	public String getSubRsdsaTitleList() {
		if(this.rsdaTitle.length() > 25)
			return ServiceUtil.subString(this.rsdaTitle, 25, "");
		return this.rsdaTitle;
	}

	public String getSubRsdsaTitleDetail() {
		if(this.rsdaTitle.length() > 25)
			return ServiceUtil.subString(this.rsdaTitle, 25, "");
		return this.rsdaTitle;
	}

//	public Long getResSubjectTypeId() {
//		return resSubjectTypeId;
//	}
//
//	public void setResSubjectTypeId(Long resSubjectTypeId) {
//		this.resSubjectTypeId = resSubjectTypeId;
//	}
	
	public String getRsdaFileAddress() {
		return rsdaFileAddress;
	}

	public void setRsdaFileAddress(String rsdaFileAddress) {
		this.rsdaFileAddress = rsdaFileAddress;
	}

	public Integer getRsdaAuthortype() {
		return rsdaAuthortype;
	}

	public void setRsdaAuthortype(Integer rsdaAuthortype) {
		this.rsdaAuthortype = rsdaAuthortype;
	}
	
	public Long getRsdaRihiId() {
		return rsdaRihiId;
	}

	public void setRsdaRihiId(Long rsdaRihiId) {
		this.rsdaRihiId = rsdaRihiId;
	}
	
	public String getSubRsdaTitle(){
		if(this.rsdaTitle.trim().length() > 15)
			return ServiceUtil.subString(this.rsdaTitle, 15, "");
		else
			return this.rsdaTitle;
	}
	
	public String getSubRsdaTitle2(){
		if(this.rsdaTitle.trim().length() > 28)
			return ServiceUtil.subString(this.rsdaTitle, 28, "");
		else
			return this.rsdaTitle;
	}
	
	public String getSubRsdaDescription(){
		if(this.rsdaDescription == null)
			return "";
		if(this.rsdaDescription.trim().length() > 90)
			return ServiceUtil.subString(this.rsdaDescription, 90, "...");
		else
			return this.rsdaDescription;
	}

	public Long getRsdaUserId() {
		return rsdaUserId;
	}

	public void setRsdaUserId(Long rsdaUserId) {
		this.rsdaUserId = rsdaUserId;
	}

	public String getRsdaFileType() {
		return rsdaFileType;
	}

	public void setRsdaFileType(String rsdaFileType) {
		this.rsdaFileType = rsdaFileType;
	}

	public Long getRsdaRmstStatus() {
		return rsdaRmstStatus;
	}

	public void setRsdaRmstStatus(Long rsdaRmstStatus) {
		this.rsdaRmstStatus = rsdaRmstStatus;
	}

	public Long getRsdaRminStatus() {
		return rsdaRminStatus;
	}

	public void setRsdaRminStatus(Long rsdaRminStatus) {
		this.rsdaRminStatus = rsdaRminStatus;
	}

	public String getRsdaPhotoImage() {
		return rsdaPhotoImage;
	}

	public void setRsdaPhotoImage(String rsdaPhotoImage) {
		this.rsdaPhotoImage = rsdaPhotoImage;
	}

	public String getAdvanceResultStr() {
		return advanceResultStr;
	}

	public void setAdvanceResultStr(String advanceResultStr) {
		this.advanceResultStr = advanceResultStr;
	}

	public Long getRsdaRssuId() {
		return rsdaRssuId;
	}

	public void setRsdaRssuId(Long rsdaRssuId) {
		this.rsdaRssuId = rsdaRssuId;
	}

	public Long getRsdaRdtyTwo() {
		return rsdaRdtyTwo;
	}

	public void setRsdaRdtyTwo(Long rsdaRdtyTwo) {
		this.rsdaRdtyTwo = rsdaRdtyTwo;
	}

	public Long getRsdaRdtyThree() {
		return rsdaRdtyThree;
	}

	public void setRsdaRdtyThree(Long rsdaRdtyThree) {
		this.rsdaRdtyThree = rsdaRdtyThree;
	}

	public Long getRsdaFileSize() {
		return rsdaFileSize;
	}

	public void setRsdaFileSize(Long rsdaFileSize) {
		this.rsdaFileSize = rsdaFileSize;
	}
	public Long getRsdaYear() {
		return rsdaYear;
	}

	public void setRsdaYear(Long rsdaYear) {
		this.rsdaYear = rsdaYear;
	}

	public String getRsdaDetailData() {
		return rsdaDetailData;
	}

	public void setRsdaDetailData(String rsdaDetailData) {
		this.rsdaDetailData = rsdaDetailData;
	}

	public String getRsdaPage() {
		return rsdaPage;
	}

	public void setRsdaPage(String rsdaPage) {
		this.rsdaPage = rsdaPage;
	}

	public Integer getRsdaSourceType() {
		return rsdaSourceType;
	}

	public void setRsdaSourceType(Integer rsdaSourceType) {
		this.rsdaSourceType = rsdaSourceType;
	}


//	public Long getResMetaStoreStatus() {
//		return resMetaStoreStatus;
//	}
//
//	public void setResMetaStoreStatus(Long resMetaStoreStatus) {
//		this.resMetaStoreStatus = this.resMetaStore.getRmstStatus();
//	}
	
}