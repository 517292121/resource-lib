package org.apache.lucene.analysis.sun.dic;

import java.util.HashMap;
import java.util.Map;

public class DictTree {

	/**
	 *
	 * @author wanglin
	 * @date Sep 28, 2016
	 *
	 * @param args
	 */
	public static void main(String[] args) {

	}
	
	/** 当前节点上的字符内容 */
	private Character nodeChar = null;
	
	/** 从根节点到当前节点是否为一个词 */
	private boolean isWord = false;
	
	private Map<Character, DictTree> childrenMap = null;
	
	private String[] sameWords = null;
	
	public DictTree(Character c){
		this.nodeChar = c;
		this.childrenMap = new HashMap<Character, DictTree>();
	}
	
	
	/**
	 * 词段匹配
	 *
	 * @author wanglin
	 * @date Sep 29, 2016
	 *
	 * @param charArr
	 * @return
	 */
	public boolean match(char[] charArr, int end){
		Character c = Character.valueOf(charArr[0]);
		DictTree dt = childrenMap.get(c);
		if(dt == null){
			return false;
		}
		if(end < 2){
			return dt.isWord;
		}
		boolean b = false;
		DictTree child = null;
		for(int i=1; i<end; i++){
			child = dt.childrenMap.get(charArr[i]);
			if(child == null){
				b = false;
				break;
			}
			
			dt = child;
			b = child.isWord;
		}
		return b;
	}
	
	/**
	 * 获取同义词
	 *
	 * @author wanglin
	 * @date Oct 18, 2016
	 *
	 * @param charArr
	 * @param end
	 * @return
	 */
	public String[] getSames(char[] charArr, int end){
		Character c = Character.valueOf(charArr[0]);
		DictTree dt = childrenMap.get(c);
		if(dt == null){
			return null;
		}
		if(end < 2){
			return dt.sameWords;
		}
		String[] sames = null;
		DictTree child = null;
		for(int i=1; i<end; i++){
			child = dt.childrenMap.get(charArr[i]);
			if(child == null){
				sames = null;
				break;
			}
			
			dt = child;
			sames = child.sameWords;
		}
		return sames;
	}
	
	/**
	 * 添加词段
	 *
	 * @author wanglin
	 * @date Sep 29, 2016
	 *
	 * @param charArr
	 * @param samwWords 同义词组
	 */
	public void fillWord(char[] charArr, String[] sameWords){
		Character c = Character.valueOf(charArr[0]);
		DictTree dt = childrenMap.get(c);
		
		if(dt == null){
			dt = new DictTree(c);
			childrenMap.put(c, dt);
		}
		int s = charArr.length;
		if(s < 2){
			dt.isWord = true;
			dt.sameWords = sameWords;
			return;
		}
		
		DictTree child = null;
		for(int i=1; i<s; i++){
			c = Character.valueOf(charArr[i]);
			child = dt.childrenMap.get(c);
			
			if(child == null){
				child = new DictTree(c);
				dt.childrenMap.put(c, child);
			}
			dt = child;
			
			if(i == (s-1)){
				dt.sameWords = sameWords;
				child.isWord = true;
			}
		}
	}
	
	/**
	 * 添加词段
	 *
	 * @author wanglin
	 * @date Sep 29, 2016
	 *
	 * @param charArr
	 */
	public void fillWord(char[] charArr){
		fillWord(charArr, null);
	}
	
	
}















