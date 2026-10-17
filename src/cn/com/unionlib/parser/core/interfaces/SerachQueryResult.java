package cn.com.unionlib.parser.core.interfaces;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;

import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.parser.util.DocumentToBean;
/**
 * 基础查询
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-13
 */
public class SerachQueryResult implements SerachQuery{
	

	/**
	 * 查询数据
	 * 无过滤条件
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public List<?> getDocList(Query query,Sort sort,PageBean pageBean,Class clas) {
		return getDocList(query, null, sort, pageBean, clas);
	}

	/**
	 * 查询数据
	 * 有过滤条件
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public List<?> getDocList(Query query,Filter filter,Sort sort, PageBean pageBean,Class clas) {
		if(docToBean==null){
			throw new NullPointerException("Document转换实体工具为空,请检查参数!");
		}
		ScoreDoc lastSd = getLastScoreDoc(indexSearcher, query, filter, pageBean,sort);
		List<Object> dataList  = null;
		try{
			TopDocs tds = getTopDocs(indexSearcher, lastSd, query, filter, pageBean.getPageSize(), sort);
			//总数
			pageBean.setCountRows(tds.totalHits);
			//结果集
			ScoreDoc[] sc = tds.scoreDocs;
			dataList = new ArrayList();
			for(ScoreDoc scoreDoc : sc){
				Document doc = indexSearcher.doc(scoreDoc.doc);
				dataList.add(docToBean.get(doc, clas,query));
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
		return dataList;
	}
	
	/**
	 * 获取数据
	 * @author MaChao
	 * @date 2017-4-13
	 */
	protected TopDocs getTopDocs(IndexSearcher searcher,ScoreDoc lastSd,Query query,Filter filter,int pageSize,Sort sort)throws IOException{
		TopDocs tds = null;
		if(filter==null){
			if(sort==null){
				 tds = searcher.searchAfter(lastSd, query,pageSize);
			}else{
				 tds = searcher.searchAfter(lastSd, query,pageSize,sort);
			}
		}else{
			if(sort==null){
				tds = searcher.searchAfter(lastSd, query,filter, pageSize);
			}else{
				tds = searcher.searchAfter(lastSd, query,filter, pageSize,sort);
			}
		}
		return tds;
	}
	
	
	/**
	 * 获取上一页
	 * @author MaChao
	 * @date 2017-4-13
	 */
	protected ScoreDoc getLastScoreDoc(IndexSearcher searcher,Query query,Filter filter ,PageBean page,Sort sort){
		try{
			if (page.getCurrentPage() == 1){
				return null;
			}
			//获取上一页的数量
			int num = (page.getCurrentPage()-1) * page.getPageSize();	
			TopDocs tds = null;
			if(filter==null){
				if(sort!=null){
					tds = searcher.search(query,num,sort);
				}else{
					tds = searcher.search(query,num);
				}
			}else{
				//检索结果检索则加上筛选过滤器
				if(sort!=null){
					tds = searcher.search(query,filter,num,sort);
				}else{
					tds = searcher.search(query,filter,num);
				}
			}
			if(tds.totalHits<1){
				return null;
			}
			return tds.scoreDocs[num - 1];
		}catch(IOException e){
				e.printStackTrace();
		}
		 return null;
	}

	/**
	 * 设置解析doc转bean
	 */
	private DocumentToBean docToBean;
	
	/**
	 * 索引查询器
	 */
	private IndexSearcher indexSearcher;
	
	public void setDocToBean(DocumentToBean docToBean) {
		this.docToBean = docToBean;
	}
	public void setIndexSearcher(IndexSearcher indexSearcher) {
		this.indexSearcher = indexSearcher;
	}
	
}
