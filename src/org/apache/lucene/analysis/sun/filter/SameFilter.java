package org.apache.lucene.analysis.sun.filter;

import java.io.IOException;
import java.util.Stack;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.sun.dic.SameDictionary;
import org.apache.lucene.analysis.sun.util.LanguageUtil.CharType;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.apache.lucene.util.AttributeSource;


public class SameFilter extends TokenFilter{

	//同义词典
	private SameDictionary sameDic = null;
	
	//Token的基本属性
	private CharTermAttribute termAtt;
	private PositionIncrementAttribute position;
	private OffsetAttribute offsetAtt;
	private TypeAttribute type;
	
	//记录全局信息变量 
	private AttributeSource.State current;
	private Stack<String> sames;
	private CharType sameType;
	private String tibetanStr = CharType.TIBETAN.toString();
	private String chineseStr = CharType.CHINESE.toString();
	
	public SameFilter(TokenStream input) {
		super(input);
		
		sameDic = SameDictionary.getSingleton();
		this.termAtt = addAttribute(CharTermAttribute.class);
	    this.position = addAttribute(PositionIncrementAttribute.class);
	    this.offsetAtt = addAttribute(OffsetAttribute.class);
	    this.type = addAttribute(TypeAttribute.class);
	    
	    sames = new Stack<String>();
	}

	@Override
	public boolean incrementToken() throws IOException {
		if(sames.size() > 0){
			//将元素出栈，并且获取这个同义词
			String str = sames.pop();
			//还原状态
			restoreState(current);
			this.termAtt.setEmpty();
			this.termAtt.append(str);
			//设置位置0
			this.position.setPositionIncrement(0);
			
			//设置语种类型
			this.type.setType(sameType.toString());
			
			//设置下标
			int start = offsetAtt.startOffset();
			int end = start + termAtt.length();
			if(end > offsetAtt.endOffset()){
				end = offsetAtt.endOffset();
			}
			this.offsetAtt.setOffset(start, end);
			
			return true;
		}
		
		if(!this.input.incrementToken()) {
			return false;
		}
		
		if(pushTibetSames(termAtt.buffer(), termAtt.length())) {
			//如果有同义词将当前状态先保存
			current = captureState();
		}
		return true;
	}
	
	/**
	 * 获取藏文的同义词并注入到栈中
	 *
	 * @author wanglin
	 * @date Oct 18, 2016
	 *
	 * @param s
	 * @return
	 */
	public boolean pushTibetSames(char[] charArr, int len){
		String[] words = null;
		String t = this.type.type();
		if(t.equals(tibetanStr)){
			words = sameDic.getTibetSames(charArr, len);
			this.sameType = CharType.CHINESE; //藏文对应的都是中文
		}else if (t.equals(chineseStr)){
			words = sameDic.getChineseSames(charArr, len);
			this.sameType = CharType.TIBETAN; //中文对应的都是藏文
		}else{
			//如果当前词语语种既不为藏文又不为中文，则不作同义词处理
			return false;
		}
		
		if(words == null || words.length < 1){
			return false;
		}
		for(int i=0,ss=words.length; i<ss; i++){
			this.sames.push(words[i]);
		}
		return true;
	}

}



