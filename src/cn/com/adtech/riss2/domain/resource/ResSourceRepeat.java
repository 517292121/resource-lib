package cn.com.adtech.riss2.domain.resource;

/**
 * 资源查重临时对象
 * 
 * @author MaChao
 * 
 * @date 2011-08-023
 */
public class ResSourceRepeat implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long rsreId;
	private Long rsreRminId; //作业ID
	private Long rsreRsdaId; //资源ID
	private String rsreRsdaTitle;  //资源题名
	
	//下面是各个属性的重复数
	private Long rsreTitle;
	private Long rsrePublisher;
	private Long rsreFileSize;
	private Long rsreFileType;
	
	private Long rsreSubject;
	private Long rsreDescription;
	private Long rsreSource;
	private Long rsreLanguage;
	private Long rsreRelation;
	private Long rsreCoverage;
	private Long rsreCreator;
	private Long rsreContributor;
	private Long rsreRights;
	private Long rsreDate;
	private Long rsreType;
	private Long rsreFormat;
	private Long rsreIdentifier;
	
	private Long rsreRepeatAll;

	// Constructors

	/** default constructor */
	public ResSourceRepeat() {
	}

	// Property accessors

	public Long getRsreId() {
		return this.rsreId;
	}

	public void setRsreId(Long rsreId) {
		this.rsreId = rsreId;
	}

	public Long getRsreRminId() {
		return this.rsreRminId;
	}

	public void setRsreRminId(Long rsreRminId) {
		this.rsreRminId = rsreRminId;
	}

	public Long getRsreRsdaId() {
		return this.rsreRsdaId;
	}

	public void setRsreRsdaId(Long rsreRsdaId) {
		this.rsreRsdaId = rsreRsdaId;
	}

	public String getRsreRsdaTitle() {
		return this.rsreRsdaTitle;
	}

	public void setRsreRsdaTitle(String rsreRsdaTitle) {
		this.rsreRsdaTitle = rsreRsdaTitle;
	}

	public Long getRsreTitle() {
		return this.rsreTitle;
	}

	public void setRsreTitle(Long rsreTitle) {
		this.rsreTitle = rsreTitle;
	}

	public Long getRsrePublisher() {
		return this.rsrePublisher;
	}

	public void setRsrePublisher(Long rsrePublisher) {
		this.rsrePublisher = rsrePublisher;
	}

	public Long getRsreFileSize() {
		return this.rsreFileSize;
	}

	public void setRsreFileSize(Long rsreFileSize) {
		this.rsreFileSize = rsreFileSize;
	}

	public Long getRsreFileType() {
		return this.rsreFileType;
	}

	public void setRsreFileType(Long rsreFileType) {
		this.rsreFileType = rsreFileType;
	}

	public Long getRsreSubject() {
		return this.rsreSubject;
	}

	public void setRsreSubject(Long rsreSubject) {
		this.rsreSubject = rsreSubject;
	}

	public Long getRsreDescription() {
		return this.rsreDescription;
	}

	public void setRsreDescription(Long rsreDescription) {
		this.rsreDescription = rsreDescription;
	}

	public Long getRsreSource() {
		return this.rsreSource;
	}

	public void setRsreSource(Long rsreSource) {
		this.rsreSource = rsreSource;
	}

	public Long getRsreLanguage() {
		return this.rsreLanguage;
	}

	public void setRsreLanguage(Long rsreLanguage) {
		this.rsreLanguage = rsreLanguage;
	}

	public Long getRsreRelation() {
		return this.rsreRelation;
	}

	public void setRsreRelation(Long rsreRelation) {
		this.rsreRelation = rsreRelation;
	}

	public Long getRsreCoverage() {
		return this.rsreCoverage;
	}

	public void setRsreCoverage(Long rsreCoverage) {
		this.rsreCoverage = rsreCoverage;
	}

	public Long getRsreCreator() {
		return this.rsreCreator;
	}

	public void setRsreCreator(Long rsreCreator) {
		this.rsreCreator = rsreCreator;
	}

	public Long getRsreContributor() {
		return this.rsreContributor;
	}

	public void setRsreContributor(Long rsreContributor) {
		this.rsreContributor = rsreContributor;
	}

	public Long getRsreRights() {
		return this.rsreRights;
	}

	public void setRsreRights(Long rsreRights) {
		this.rsreRights = rsreRights;
	}

	public Long getRsreDate() {
		return this.rsreDate;
	}

	public void setRsreDate(Long rsreDate) {
		this.rsreDate = rsreDate;
	}

	public Long getRsreType() {
		return this.rsreType;
	}

	public void setRsreType(Long rsreType) {
		this.rsreType = rsreType;
	}

	public Long getRsreFormat() {
		return this.rsreFormat;
	}

	public void setRsreFormat(Long rsreFormat) {
		this.rsreFormat = rsreFormat;
	}

	public Long getRsreIdentifier() {
		return this.rsreIdentifier;
	}

	public void setRsreIdentifier(Long rsreIdentifier) {
		this.rsreIdentifier = rsreIdentifier;
	}

	public Long getRsreRepeatAll() {
		return this.rsreRepeatAll;
	}

	public void setRsreRepeatAll(Long rsreRepeatAll) {
		this.rsreRepeatAll = rsreRepeatAll;
	}

}
