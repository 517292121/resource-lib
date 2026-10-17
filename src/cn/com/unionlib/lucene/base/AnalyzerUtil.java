package cn.com.unionlib.lucene.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.unionlib.lucene.factory.AnalyzerFactory;

public class AnalyzerUtil {
	
	public static void main(String[] args){
		List<String> list = displayToken("再见了青春");
		for(String s : list){
			System.out.println(s);
		}
	}

	public static List<String> displayToken(String txt) {
		if(!CommonUtil.isNotNull(txt)){
			return null;
		}
		Analyzer a = AnalyzerFactory.instance().getSmartAnalyzer();
		TokenStream ts = null;
		List<String> keylist = new ArrayList<String>();
		try {
			ts = a.tokenStream("words", txt);
			
			//存储每一个语汇单元的信息（分词单元信息）
			CharTermAttribute cta = ts.addAttribute(CharTermAttribute.class);
			
			ts.reset();
			while(ts.incrementToken()){
				keylist.add(cta.toString());
			}
			return keylist;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
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
	}
}
