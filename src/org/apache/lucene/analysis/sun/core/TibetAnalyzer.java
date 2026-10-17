package org.apache.lucene.analysis.sun.core;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.sun.filter.ChilderFilter;
import org.apache.lucene.analysis.sun.filter.SameFilter;
import org.apache.lucene.analysis.sun.filter.TibetFilter;
import org.apache.lucene.util.Version;

/**
 * 
 * 阳光藏文分词器
 *
 * @author wanglin
 * 
 * @version 1.0
 *
 * @date 2016.09
 *
 */
public final class TibetAnalyzer extends Analyzer {
	
	//是否采用简单分词效果
	private boolean isSimple = false;
	
	//是否切分同义词
	private boolean isSame = false;
	
	//只否只切分出是词语的词
	private boolean isMust = false;
	
	public TibetAnalyzer(){
		this.isSimple = false;
		this.isSame = false;
		this.isMust = false;
	}
	public TibetAnalyzer(boolean isSimple){
		this.isSimple = isSimple;
		this.isSame = false;
		this.isMust = false;
	}
	public TibetAnalyzer(boolean isSimple, boolean issame){
		this.isSimple = isSimple;
		this.isSame = issame;
		this.isMust = false;
	}
	public TibetAnalyzer(boolean isSimple, boolean issame, boolean isMust){
		this.isSimple = isSimple;
		this.isSame = issame;
		this.isMust = isMust;
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		Tokenizer t = null;
		
		//判断需要创建哪种分词器
		if(isSimple){
			t = new SunSimpleTokenizer(reader, isMust);
		}else{
			t = new SunFineTokenizer(reader);
		}
		
		//基础过滤
		TokenStream ts = new TibetFilter(t);
		
		//统计转换成小写过滤
		ts = new LowerCaseFilter(Version.LUCENE_46, ts);
		
		if(isSimple){
			//过滤子词元，保留最大的词元
			ts = new ChilderFilter(ts);
		}
		
		//添加同义词过滤器
		if(isSame){
			ts = new SameFilter(ts);
		}
		
		TokenStreamComponents tsc = new TokenStreamComponents(t, ts);
		return tsc;
	}
	
	
	
	public boolean isSimple() {
		return isSimple;
	}
	public void setSimple(boolean isSimple) {
		this.isSimple = isSimple;
	}
	public boolean isSame() {
		return isSame;
	}
	public void setSame(boolean isSame) {
		this.isSame = isSame;
	}
	public boolean isMust() {
		return isMust;
	}
	public void setMust(boolean isMust) {
		this.isMust = isMust;
	}

}
