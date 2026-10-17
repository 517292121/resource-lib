package cn.com.unionlib.lucene.bean;

import java.util.List;
import java.util.Map;

/**
 * 
 * 索引检索结果
 *
 * @author MaChao
 *
 * @date：Jan 31, 2018
 */
public class SearchResultBean {

	/**
	 * 数据列表结果集
	 */
	private List<Object[]> dataList = null;
	
	/**
	 * 聚类结果集
	 */
	private Map<String, List<Object[]>> groupMap = null;

	
	public List<Object[]> getDataList() {
		return dataList;
	}

	public void setDataList(List<Object[]> dataList) {
		this.dataList = dataList;
	}

	public Map<String, List<Object[]>> getGroupMap() {
		return groupMap;
	}

	public void setGroupMap(Map<String, List<Object[]>> groupMap) {
		this.groupMap = groupMap;
	}

	
}
