package cn.com.unionlib.lucene.bean;

import java.util.List;

/**
 * 
 * @description 数据对象实体bean
 * 
 * @author MaChao
 * 
 * @version 1.0
 * 
 * @date Jun 23, 201510:31:02 AM
 * 
 */
public class ResMainBean {
	
	private Long id;
	
	//题名
	private String title; 			 

	///////----------索引基本核心元数据--------/
	// 创建者
	private String creator; 		

	// 出版者
	private String publisher;		
	
	// 关键词
	private String keyword;
	
	//显示日期
	private String date;
	
	
	///////----------索引封面路径--------/
	// 封面路径
	private String coverUrl; 		

	///////----------索引资源分类，只做按库、资源类型、学科三个全局分组--------/
	// 学科分类
	private String subjectClass; 		
	
	//所属特色库
	private String resLibrary;
	
	// 资源类型
	private String resType;            

	
	///////----------（所有元数据+全文） --------/
	private String content; 	
	
	
	// 数据类型 RES:资源   TEXT:文档资源  INFO:资讯
	private String dataType;
	
	final public static String DATA_TYPE_RES = "资源";
	final public static String DATA_TYPE_TEXT = "文本";
	final public static String DATA_TYPE_INFO = "资讯";
	
	
	//源文本存放路径
	private String textPath;
	
	
	//资源具体每一页数据集合（用于图文对照、目录树预览格式资源，临时使用不做存储）
	private List<ResPageBean> pageList;
	
	//源文路径
	private String srcPath;

	//语种  中文：zh   藏文：zw  蒙文：mw  英文：en    默认（未知）：none
	private String language;
	
	public ResMainBean() {
		super();
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCoverUrl() {
		return coverUrl;
	}

	public void setCoverUrl(String coverUrl) {
		this.coverUrl = coverUrl;
	}

	public String getSubjectClass() {
		return subjectClass;
	}

	public void setSubjectClass(String subjectClass) {
		this.subjectClass = subjectClass;
	}

	public String getResLibrary() {
		return resLibrary;
	}

	public void setResLibrary(String resLibrary) {
		this.resLibrary = resLibrary;
	}

	public String getResType() {
		return resType;
	}

	public void setResType(String resType) {
		this.resType = resType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getTextPath() {
		return textPath;
	}

	public void setTextPath(String textPath) {
		this.textPath = textPath;
	}

	public List<ResPageBean> getPageList() {
		return pageList;
	}

	public void setPageList(List<ResPageBean> pageList) {
		this.pageList = pageList;
	}

	public String getSrcPath() {
		return srcPath;
	}

	public void setSrcPath(String srcPath) {
		this.srcPath = srcPath;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
}
