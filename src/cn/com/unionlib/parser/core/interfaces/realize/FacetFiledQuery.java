package cn.com.unionlib.parser.core.interfaces.realize;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.facet.params.FacetSearchParams;
import org.apache.lucene.facet.search.CountFacetRequest;
import org.apache.lucene.facet.search.FacetResult;
import org.apache.lucene.facet.search.FacetResultNode;
import org.apache.lucene.facet.search.FacetsCollector;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.TaxonomyReader;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;

import cn.com.unionlib.lucene.factory.GroupConnFactory;
import cn.com.unionlib.parser.core.interfaces.FacetQuery;

/**
 * 聚类查询
 * @author MaChao
 * @date 2017-9-4
 */
public class FacetFiledQuery implements FacetQuery {

	/**
	 * 获取聚类结果
	 * @author MaChao
	 * @date 2017-9-4
	 */
	@Override
	public Map<String, Map<String, Integer>> group(Query query, Filter filter,
			String[] groupField, int docsLimit) {
		//保证返回不为null
		Map<String, Map<String, Integer>> facetMap = new HashMap<String, Map<String,Integer>>();
		TaxonomyReader reader = GroupConnFactory.instance().getTaxonomyReader();
		if(docsLimit==0){
			docsLimit = 100;
		}
		//生成聚类字段和返回数
		CountFacetRequest[] facetRequests = new CountFacetRequest[groupField.length];
		for(int i = 0,size = groupField.length;i< size;i++){
			facetRequests[i] = new CountFacetRequest(new CategoryPath(groupField[i]), docsLimit);		
		}
		FacetSearchParams facetSearchParams = new FacetSearchParams(facetRequests);
		
		//开始搜集
		FacetsCollector facetsCollector = FacetsCollector.create(facetSearchParams, 
				indexSearcher.getIndexReader(), reader);
		
		//搜集完毕开始查询
		try{
			if(filter!=null){
				indexSearcher.search(query, filter,facetsCollector);
			}else{
				indexSearcher.search(query,facetsCollector);
			}
			
			List<FacetResult> res = facetsCollector.getFacetResults();
			String filedName = null;
		    Map<String, Integer> map = null;
			if(res!=null && res.size()>0){
				for (FacetResult r : res) {
					if(r.getFacetResultNode().subResults.size()>0){
						filedName = r.getFacetResultNode().label.toString();						
						if(facetMap.containsKey(filedName)){
							map = facetMap.get(filedName);
						}else{
							map = new LinkedHashMap<String, Integer>();		
							facetMap.put(filedName, map);
						}			
						for (FacetResultNode n : r.getFacetResultNode().subResults) {
							map.put(n.label.components[1], (int)n.value);
						}
					}
				}
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return facetMap;
	}

	
	/**
	 * 索引查询器
	 */
	private IndexSearcher indexSearcher ;

	public void setIndexSearcher(IndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}
}
