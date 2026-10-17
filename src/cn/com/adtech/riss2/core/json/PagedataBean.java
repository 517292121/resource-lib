package cn.com.adtech.riss2.core.json;

import java.util.List;
import java.util.Map;

/**
 * 
 * JSON数据转换列表对象
 *
 * @author MaChao
 *
 * @date：Jun 19, 2018
 */
public class PagedataBean {

	//请求结果
	private boolean success = true;
	
	//当前页数
	private int currtPage;
	
	//总页数
	private int totalPage;
	
	//总行数
	private int totalRows;
	
	//显示条数
	private int pageResults;
	
	public int getPageResults() {
		return pageResults;
	}

	public void setPageResults(int pageResults) {
		this.pageResults = pageResults;
	}

	//数据列表对象
	private List<Map<String, Object>> dataList = null;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getCurrtPage() {
		return currtPage;
	}

	public void setCurrtPage(int currtPage) {
		this.currtPage = currtPage;
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public int getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(int totalRows) {
		this.totalRows = totalRows;
	}

	public List<Map<String, Object>> getDataList() {
		return dataList;
	}

	public void setDataList(List<Map<String, Object>> dataList) {
		this.dataList = dataList;
	}
	
	
}
