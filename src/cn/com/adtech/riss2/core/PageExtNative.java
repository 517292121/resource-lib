package cn.com.adtech.riss2.core;

import java.util.HashMap;
import java.util.Map;

/**
 * EXT分页控制器
 * 封装Ext前台TABLE分页排序查询条件
 * 
 * @author MaChao
 * @date 2009-8-10
 * @version 1.0
 */
public class PageExtNative 
{
	
	// 起始页
	private int start;
	
	// 页面大小
	private int limit;
	
	// 排序字段
	private String sort;
	
	// 升序还是降序
	private String dir;
	
	// 查询条件
	private Map<String, String> mSearch;
	
	public PageExtNative(Map<String, String> mSearch)
	{
		this.mSearch = mSearch;
	}
	
	public PageExtNative()
	{
		this.mSearch = new HashMap<String, String>();
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getDir() {
		return dir;
	}

	public void setDir(String dir) {
		this.dir = dir;
	}

	public Map<String, String> getMSearch() {
		return mSearch;
	}

	public void setMSearch(Map<String, String> search) {
		mSearch = search;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
