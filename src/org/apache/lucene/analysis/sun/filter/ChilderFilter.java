package org.apache.lucene.analysis.sun.filter;

import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.util.AttributeSource;

/**
 * 
 * 过滤子词语
 *
 * @author wanglin
 * 
 * @version 1.0
 *
 * @date Oct 31, 20163:03:09 PM
 *
 */
public class ChilderFilter extends TokenFilter{

	//记录全局信息变量 
	private AttributeSource.State record = null; //记录前一个
	private AttributeSource.State before = null; //记录后一个
	private AttributeSource.State tempState = null; //后一个交给前一个后，接受当前临时记录
	private OffsetAttribute offsetAtt = null;
	private int startIndex = -1; //记录前一个开始下标
	private int currIndex = 0; //记录后一个开始下标
	
	
	public ChilderFilter(TokenStream input) {
		super(input);
		this.offsetAtt = addAttribute(OffsetAttribute.class);
	}
	
	
	@Override
	public boolean incrementToken() throws IOException {
		boolean next = input.incrementToken();
		if(next == false){
			if (currIndex > startIndex){
				restoreState(before);
				
				//能进入这一步，说明已经最后一个词，返回后强行改变一下参数使下次不能成立即可
				currIndex = startIndex - 1;
				return true;
			}
			return false;
		}
		
		//如果是开始下标小于0，说明是第一次进来；则将第一个token的起始下标赋给它
		if(startIndex < 0){
			this.startIndex = offsetAtt.startOffset();
		}else{
			this.startIndex = currIndex;
		}
		this.currIndex = offsetAtt.startOffset();
		if(currIndex > startIndex){
			this.record = before;
			this.tempState = captureState();
			
			restoreState(before);
			
			this.before = tempState;
			return true;
		}
			
		while (next) {
			if (currIndex == startIndex) {
				record = captureState();
				next = input.incrementToken();
				if(next){
					currIndex = offsetAtt.startOffset();
				}else{
					currIndex = startIndex - 1;
				}
			} else {
				before = captureState();
				restoreState(record);
				return true;
			}
		}
		
		restoreState(record);
		return true;
	}


	@Override
	public void reset() throws IOException {
		super.reset();
		
		this.record = null;
		this.before = null;
		this.tempState = null;
		this.startIndex = 0;
		this.currIndex = 0;
	}

}
