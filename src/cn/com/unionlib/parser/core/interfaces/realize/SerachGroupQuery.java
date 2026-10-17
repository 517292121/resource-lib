package cn.com.unionlib.parser.core.interfaces.realize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.grouping.GroupDocs;
import org.apache.lucene.search.grouping.GroupingSearch;
import org.apache.lucene.search.grouping.TopGroups;
import org.apache.lucene.util.BytesRef;

import cn.com.unionlib.parser.core.exception.ParserException;
import cn.com.unionlib.parser.core.interfaces.SerachGroup;

/**
 * 聚类查询
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-13
 */
public class SerachGroupQuery implements SerachGroup {
	
	
	/**
	 * 聚类查询
	 * @Description: TODO
	 * @param @param query			查询条件
	 * @param @param filter			过滤条件
	 * @param @param groupField		聚类字段
	 * @param @param sortFields		排序(暂无使用,如需要请自行实现GroupSerach)
	 * @param @param docsLimit		一个字段返回多少条记录
	 * @param @param groupOffset	起始位置(类似分页 一般设为 0 )
	 * @param @param groupLimit     结束位置(一页返回多少条)
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public Map<String, Map<String, Integer>> group(Query query, Filter filter,
			String[] groupField,SortField[] sortFields,int docsLimit,int groupOffset,int groupLimit) {
		
		Map<String, Map<String, Integer>> groupMap = new HashMap<String, Map<String,Integer>>();
		// 统计显示多少组
		TopGroups<BytesRef> result = null;
		// 进行分组设置
		GroupingSearch groupingSearch = null;
		//聚类数
		int  groupSize= 0;
		//聚类值
		String groupValue;
		//值与数关联
		Map<String, Integer> valueMap= null;
		if(groupOffset<0){
			throw new ParserException("groupOffset 必须 >=0 当前为:"+groupOffset);
		}
		if(groupLimit<1){
			throw new ParserException("groupLimit 必须 >=1 当前"+groupLimit);
		}
		try {
			for (int c = 0; c < groupField.length; c++) {	
				// 进行分组设置
				groupingSearch = new GroupingSearch(groupField[c]);
				//设置缓存
				groupingSearch.setCachingInMB(5.0, true);
				groupingSearch.setAllGroups(true);
				if(docsLimit==0){
					docsLimit = 100;
				}
				groupingSearch.setGroupDocsLimit(docsLimit);
				if(filter!=null){
					result = groupingSearch.search(indexSearcher,filter,query, groupOffset,groupLimit );
				}else{
					result = groupingSearch.search(indexSearcher,query, groupOffset,groupLimit);
				}
				// 如果能查询出数据则进行字段统计
				if (result.groups.length > 0) {
					for (GroupDocs<BytesRef> groupDocs : result.groups){
						//统计结果不为空才统计
						groupSize  = groupDocs.totalHits;
						if(groupSize>0 && groupDocs.groupValue!=null){
							groupValue = groupDocs.groupValue.utf8ToString();
							if(!"".equals(groupValue)){		
								if(!groupMap.containsKey(groupField[c])){								
									valueMap = new TreeMap<String, Integer>();
									groupMap.put(groupField[c], valueMap);
								}else{
									valueMap = groupMap.get(groupField[c]);
								}
								valueMap.put(groupValue, groupSize);
							}	
						}
					}
				}
			}	
		} catch (Exception e) {
			System.out.println("查询失败!"+e.getMessage());
		}
		//排序
		SortMap(groupMap);
		
		return groupMap;
	}
	
	/**
	 * 排序
	 * @author MaChao
	 * @date 2017-4-13
	 */
	protected void SortMap(Map<String, Map<String, Integer>> groupMap){
		List<Map.Entry<String,Integer>> list = null;
		
		//排序
		for(Map.Entry<String, Map<String, Integer>> entry : groupMap.entrySet()){
			 list = new ArrayList<Map.Entry<String,Integer>>(entry.getValue().entrySet());
			 Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
					public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
						return o2.getValue().compareTo(o1.getValue());
					}
			});
			Map<String, Integer> vaMap = new LinkedHashMap<String, Integer>();
			for(Map.Entry<String, Integer> map : list){		
				vaMap.put(map.getKey(), map.getValue());
			}
			groupMap.put(entry.getKey(), vaMap);
		}

	}

	/**
	 * 索引查询器
	 */
	private IndexSearcher indexSearcher ;

	public void setIndexSearcher(IndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}
	
}
