package cn.com.unionlib.parser.core.bean;


/**
 * 
 * @description 分页对象bean
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Jun 30, 201511:32:53 AM
 *
 */
public class PageBean {

	/** 当前页数，默认为 1 */
	private int currentPage = 1;
	
	/** 每页数量，默认为 20 */
	private int pageSize = 20;
	
	/** 结果总页数 ***/
	private int countPage = 0;
	
	/** 结果总记录数 ***/
	private int countRows = 0;
	

	
	
	/**实例化时设置当前页数***/
	public PageBean(int currentPage){
		this.currentPage = currentPage;
	} 
	
	/**实例化时设置当前页数和每页记录数***/
	public PageBean(int currentPage, int pageSize){
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getCountPage() {
		return countPage;
	}

	public void setCountPage(int countPage) {
		this.countPage = countPage;
	}

	public int getCountRows() {
		return countRows;
	}

	public void setCountRows(int countRows) {
		this.countRows = countRows;
		if(countRows>0){
			this.countPage = (countRows-1)/pageSize+1;
		}
	}

	
}
