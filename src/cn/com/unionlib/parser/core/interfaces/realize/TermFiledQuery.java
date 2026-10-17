package cn.com.unionlib.parser.core.interfaces.realize;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

import cn.com.unionlib.parser.core.bean.Serach;

/**
 * 短语查询
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-12
 */
public class TermFiledQuery implements cn.com.unionlib.parser.core.interfaces.TermQuery{

	/**
	 * 单个短语
	 * @author MaChao
	 * @date 2017-4-11
	 */
	public Query bulidTermQuery(String coulmn,String keyWord,Occur occur) {
		Query query  = null;
		if(occur==null){
		    query = new TermQuery(new Term(coulmn,keyWord));
		}else{
			BooleanQuery b = new BooleanQuery();
			b.add(new TermQuery(new Term(coulmn,keyWord)),occur);
			query = b;
		}
		return query;
	}
	
	/**
	 * 多个短语
	 * @author MaChao
	 * @date 2017-4-12
	 */
	public Query bulidTermQuery(List<Serach> serachList){
		BooleanQuery query = new BooleanQuery();
		if(serachList==null || serachList.size()<1){
			throw new NullPointerException("字段与值不能为空");
		}
		Map<String, BooleanQuery> map = new HashMap<String, BooleanQuery>();
		Map<String, Occur> occurMap = new HashMap<String, Occur>();
		BooleanQuery b = null;
		for(Serach serach : serachList){
			//同字段属于一个检索片段
			if(map.containsKey(serach.getCoulmn())){
				b = map.get(serach.getCoulmn());
				b.add(bulidTermQuery(serach.getCoulmn(), serach.getKeyWord(), null),serach.getOccur());
				occurMap.remove(serach.getCoulmn());
			}else{
				b =  new BooleanQuery();
				b.add(bulidTermQuery(serach.getCoulmn(), serach.getKeyWord(), null),serach.getOccur());
				map.put(serach.getCoulmn(),b);
				//多字段用must关联 单字段取本身
				occurMap.put(serach.getCoulmn(),serach.getOccur());
			}
		}
		Occur occur = null;
		for(Map.Entry<String, BooleanQuery> entry : map.entrySet()){
			occur = occurMap.get(entry.getKey());
			if(occur==null){
				occur = Occur.MUST;
			}
			query.add(entry.getValue(), occur);
		}
		return query;
	}
	
	
	
	
}
