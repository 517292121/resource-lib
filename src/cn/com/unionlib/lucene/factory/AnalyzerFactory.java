package cn.com.unionlib.lucene.factory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.sun.core.TibetAnalyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;


/**
 * 
 * @description 分词器工厂
 * 由于保存索引和检索的时候都需要对文本内容进行分词
 * 且用的分词器必须保持一致
 * 因此将分词器用单态模式工厂创建
 * 如果需要更换分词器，平台只需要修改getNewAnalyzer创建的分词器对象即可
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Jun 19, 20153:03:25 PM
 *
 */
public class AnalyzerFactory {

	//自身实例对象
	private static AnalyzerFactory self = null;
	
	//普通分词器全局对象（完整分词）
	private Analyzer comAnalyzer = null;
	
	//智能分词器 ， 不切分同义词
	private Analyzer smartAnalyzer = null;
	
	//智能加同义词分词
	private Analyzer smartSameAnalyzer = null;
	
	/**
	 * 定义私有函数防止实例化
	 */
	private AnalyzerFactory(){
		
	}
	
	public static AnalyzerFactory instance(){
		if(self == null){
			self = new AnalyzerFactory();
		}
		return self;
	} 
	
	/**
	 * 获取新建分词器，整个系统所有分词器仅在此处创建
	 *
	 * @author MaChao
	 * @date Oct 17, 2016
	 *
	 * @param b 是否为简单切分
	 * @return
	 */
	public Analyzer getNewAnalyzer(boolean isSmart, boolean isSame){
		//中文分词
//		Analyzer a = new IKAnalyzer(b);
		
		//藏文分词
		Analyzer a = new TibetAnalyzer(isSmart, isSame);
		return a;
	}
	
	public Analyzer getComAnalyzer(){
		if(comAnalyzer == null){
			comAnalyzer = getNewAnalyzer(false, false);
		}
		return comAnalyzer;
	} 
	
	public Analyzer getSmartAnalyzer(){
		if(smartAnalyzer == null){
			smartAnalyzer = getNewAnalyzer(true, false);
		}
		return smartAnalyzer;
	} 
	public Analyzer getSmartSameAnalyzer(){
		if(smartSameAnalyzer == null){
			smartSameAnalyzer = getNewAnalyzer(true, true);
		}
		return smartSameAnalyzer;
	} 
	
	public void refreshAnalyzer(){
		this.comAnalyzer = getNewAnalyzer(false, false);
		this.smartAnalyzer = getNewAnalyzer(true, false);
		this.smartSameAnalyzer = getNewAnalyzer(true, true);
	}
}
