package org.apache.lucene.analysis.sun.filter;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.sun.dic.Dictionary;
import org.apache.lucene.analysis.sun.util.LanguageUtil;
import org.apache.lucene.analysis.sun.util.LanguageUtil.CharType;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;

public class TibetFilter extends TokenFilter{

	//定义禁用词集合
	private Dictionary dic = null;
	private CharTermAttribute termAtt;
	private PositionIncrementAttribute position;
	private TypeAttribute type;
	private int plen = 0; //记录词语与上一个词语之前的间隔
	
	private String otherStr = CharType.OTHER.toString();
	  
	public TibetFilter(TokenStream input) {
		super(input);
		
		this.dic = Dictionary.getSingleton();
	    this.termAtt = addAttribute(CharTermAttribute.class);
	    this.position = addAttribute(PositionIncrementAttribute.class);
	    this.type = addAttribute(TypeAttribute.class);
	}

	@Override
	public boolean incrementToken() throws IOException {
		plen = 1;
		while (this.input.incrementToken()) {
			if(termAtt.length() < 1){
				continue;
			} 
			else if(termAtt.length() == 1 && stopChar(termAtt.charAt(0))){
				continue;
			}
			else if(dic.matchInStopDict(termAtt.buffer(), termAtt.length())){
				plen++;
			}
			else if(type.type().equals(otherStr)){
				plen++;
			}
			else{
				position.setPositionIncrement(plen);
				return true;
			}
		}
		return false;
	}
	
	private boolean stopChar(char c){
		if(LanguageUtil.isSplit(c)){
			return true;
		}
		//注意：以下字符与空格不相等，因此要单独判断一下
		if(c == ' '){
			return true;
		}
		return false;
	}

}
