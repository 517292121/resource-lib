package cn.com.unionlib.lucene.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

/**
 * 
 * 分词
 *
 * @author MaChao
 *
 * @date：Feb 2, 2018
 */
public class SegmentWord {

	final public static List<String> displayToken(String txt, Analyzer a) {
		TokenStream ts = null;
		List<String> wordList = new ArrayList<String>();
		try {
			ts = a.tokenStream("words", txt);
			
			//存储每一个语汇单元的信息（分词单元信息）
			CharTermAttribute cta = ts.addAttribute(CharTermAttribute.class);
			
			ts.reset();
			while(ts.incrementToken()){
				wordList.add(cta.toString());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			//释放TokenStream的所有资源
			if(ts != null){
		      try {
				ts.close();
		      } catch (IOException e) {
				e.printStackTrace();
		      }
			}
	    }
		return wordList;
	}
}
