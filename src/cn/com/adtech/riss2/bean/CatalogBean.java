package cn.com.adtech.riss2.bean;

/**
 * 
 * 章节目录对象
 *
 * @author MaChao
 *
 * @date：Oct 30, 2018
 */
public class CatalogBean {

	private String title;   //章节题名

	private String pageName;  //对应的页面（图片）名称
	
	private String pageNameCode; //编码后的页面名称
	

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPageNameCode() {
		return pageNameCode;
	}

	public void setPageNameCode(String pageNameCode) {
		this.pageNameCode = pageNameCode;
	}
	
}
