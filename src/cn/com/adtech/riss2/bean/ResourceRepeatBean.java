package cn.com.adtech.riss2.bean;

import java.util.List;

import cn.com.adtech.riss2.domain.resource.ResSourceData;

/**
 * 资源查重参数BEAN
 * 
 * @author MaChao
 * 
 * @date 2011-08-04
 *
 */ 
public class ResourceRepeatBean {

	/***********************查重条件**********************/
	/**是否查重标题*/
	private boolean checkTitle;
	
	/**是否查重出版者*/
	private boolean checkPublisher;
	
	/**是否查重源文大小*/
	private boolean checkFilesize;
	
	/**是否查重源文文件类型*/
	private boolean checkFiletype;
	
	//主题
	private boolean checkSubject;
	
	//描述
	private boolean checkDescription;
	
	//来源
	private boolean checkSource;
	
	//语种
	private boolean checkLanguage;
	
	//关联
	private boolean checkRelation;
	
	//覆盖范围
	private boolean checkCoverage;
	
	//创建者
	private boolean checkCreator;
	
	//其它责任者
	private boolean checkContributor;
	
	//权限
	private boolean checkRights;
	
	//日期
	private boolean checkDate;
	
	//类型
	private boolean checkType;
	
	//格式
	private boolean checkFormat;
	
	//标识符
	private boolean checkIdentifier;
	
	/**********************其它相关参数**********************/
	/**查重的资源范围(资源状态)*/
	private Long rsdaStatus;
	
	/**作业ID*/
	private Long rminId;
	
	/**待查重的资源集合*/
	private List<ResSourceData> repeatResourceList;
	
	/**等查重的资源ID数组*/
	private Long[] rsdaIdArr;
	
	//是否查重所有列
	private boolean repeatAll;

	
	
	public boolean isCheckTitle() {
		return checkTitle;
	}

	public void setCheckTitle(boolean checkTitle) {
		this.checkTitle = checkTitle;
	}

	public boolean isCheckPublisher() {
		return checkPublisher;
	}

	public void setCheckPublisher(boolean checkPublisher) {
		this.checkPublisher = checkPublisher;
	}

	public boolean isCheckFilesize() {
		return checkFilesize;
	}

	public void setCheckFilesize(boolean checkFilesize) {
		this.checkFilesize = checkFilesize;
	}

	public boolean isCheckFiletype() {
		return checkFiletype;
	}

	public void setCheckFiletype(boolean checkFiletype) {
		this.checkFiletype = checkFiletype;
	}

	public Long getRsdaStatus() {
		return rsdaStatus;
	}

	public void setRsdaStatus(Long rsdaStatus) {
		this.rsdaStatus = rsdaStatus;
	}

	public Long getRminId() {
		return rminId;
	}

	public void setRminId(Long rminId) {
		this.rminId = rminId;
	}

	public List<ResSourceData> getRepeatResourceList() {
		return repeatResourceList;
	}

	public void setRepeatResourceList(List<ResSourceData> repeatResourceList) {
		this.repeatResourceList = repeatResourceList;
	}

	public Long[] getRsdaIdArr() {
		return rsdaIdArr;
	}

	public void setRsdaIdArr(Long[] rsdaIdArr) {
		this.rsdaIdArr = rsdaIdArr;
	}

	public boolean isCheckSubject() {
		return checkSubject;
	}

	public void setCheckSubject(boolean checkSubject) {
		this.checkSubject = checkSubject;
	}

	public boolean isCheckDescription() {
		return checkDescription;
	}

	public void setCheckDescription(boolean checkDescription) {
		this.checkDescription = checkDescription;
	}

	public boolean isCheckSource() {
		return checkSource;
	}

	public void setCheckSource(boolean checkSource) {
		this.checkSource = checkSource;
	}

	public boolean isCheckLanguage() {
		return checkLanguage;
	}

	public void setCheckLanguage(boolean checkLanguage) {
		this.checkLanguage = checkLanguage;
	}

	public boolean isCheckRelation() {
		return checkRelation;
	}

	public void setCheckRelation(boolean checkRelation) {
		this.checkRelation = checkRelation;
	}

	public boolean isCheckCoverage() {
		return checkCoverage;
	}

	public void setCheckCoverage(boolean checkCoverage) {
		this.checkCoverage = checkCoverage;
	}

	public boolean isCheckCreator() {
		return checkCreator;
	}

	public void setCheckCreator(boolean checkCreator) {
		this.checkCreator = checkCreator;
	}

	public boolean isCheckContributor() {
		return checkContributor;
	}

	public void setCheckContributor(boolean checkContributor) {
		this.checkContributor = checkContributor;
	}

	public boolean isCheckRights() {
		return checkRights;
	}

	public void setCheckRights(boolean checkRights) {
		this.checkRights = checkRights;
	}

	public boolean isCheckDate() {
		return checkDate;
	}

	public void setCheckDate(boolean checkDate) {
		this.checkDate = checkDate;
	}

	public boolean isCheckType() {
		return checkType;
	}

	public void setCheckType(boolean checkType) {
		this.checkType = checkType;
	}

	public boolean isCheckFormat() {
		return checkFormat;
	}

	public void setCheckFormat(boolean checkFormat) {
		this.checkFormat = checkFormat;
	}

	public boolean isCheckIdentifier() {
		return checkIdentifier;
	}

	public void setCheckIdentifier(boolean checkIdentifier) {
		this.checkIdentifier = checkIdentifier;
	}

	public boolean isRepeatAll() {
		return repeatAll;
	}

	public void setRepeatAll(boolean repeatAll) {
		this.repeatAll = repeatAll;
	}
}
