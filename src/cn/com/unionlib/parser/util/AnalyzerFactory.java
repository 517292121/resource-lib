package cn.com.unionlib.parser.util;

import org.apache.lucene.analysis.Analyzer;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * 单列分词器工厂
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-12
 */
public class AnalyzerFactory {
	//本身
	private static AnalyzerFactory factory= null;

	//细粒切分分词器
	private Analyzer fineAnalyzer = null;
	
	//智能切分分词器
	private Analyzer smartAnalyzer = null; 
	
	private AnalyzerFactory(){};
	
	public static AnalyzerFactory getInstance(){
		if(factory==null){
			synchronized (AnalyzerFactory.class) {
				factory = new AnalyzerFactory();
			}
		}
		return factory;
	}
	
	
	/**
	 * 颗粒分词
	 * @author MaChao
	 * @date 2017-4-12
	 */
	public Analyzer getFineAnalyzer(){
		if(fineAnalyzer == null){
			fineAnalyzer = new IKAnalyzer(false);
		}
		return fineAnalyzer;
	} 
	
	/**
	 * 智能分词
	 * @author MaChao
	 * @date 2017-4-12
	 */
	public Analyzer getSmartAnalyzer(){
		if(smartAnalyzer == null){
			smartAnalyzer = new IKAnalyzer(true);
		}
		return smartAnalyzer;
	}
}
