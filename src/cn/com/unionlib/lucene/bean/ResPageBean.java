package cn.com.unionlib.lucene.bean;

/**
 * 
 * 资源子页面索引数据存储对象
 *
 * @author MaChao
 *
 * @date：Aug 13, 2018
 */
public class ResPageBean {

	//数据ID
	private Long id;
	
	//所属资源ID
	private Long resId;
	
	//标题\名称
	private String title;
	
	//检索内容
	private String content;

	//标题编译后（资源检索时临时使用）
	private String titleEncode;
	
	//页码，从1开始，不分封面目录页等
	private int pageNum;
	
	//子页类型  1.pdf  2.图文
	private int pageType;

	
	public int getPageType() {
		return pageType;
	}

	public void setPageType(int pageType) {
		this.pageType = pageType;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getResId() {
		return resId;
	}

	public void setResId(Long resId) {
		this.resId = resId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitleEncode() {
		return titleEncode;
	}

	public void setTitleEncode(String titleEncode) {
		this.titleEncode = titleEncode;
	}

}
