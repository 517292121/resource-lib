package org.apache.lucene.analysis.sun.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * @description 同义词典管理
 *
 * @author wanglin
 * 
 * @version 1.0
 *
 * @date Oct 18, 201610:02:04 AM
 *
 */
public class SameDictionary {
	
	private static SameDictionary singleton;

	/**藏文的中文同义词语***/
	private DictTree tibetSames = null;
	/**中文的藏文同义词词*****/
	private DictTree chinaSames = null;
	
	final private static String tibetSameName = "tibetan-same.dic";
	final private static String chinaSameName = "chinese-same.dic";
	
	public SameDictionary(){
		this.tibetSames = loadSame(tibetSameName);
		this.chinaSames = loadSame(chinaSameName);
	}
	
	public static SameDictionary getSingleton(){
		if(singleton == null){
			singleton = new SameDictionary();
		}
		return singleton;
	}
	
	
	private DictTree loadSame(String filename){
		long aa = System.currentTimeMillis();
		
		DictTree dict = new DictTree('0');
		InputStream is = this.getClass().getResourceAsStream(filename);
		if(is == null){
			System.out.println("词典“"+filename+"”词典加载出错，请检索词典路径及文件是否正确：");
		}
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UNICODELITTLE"),512);
			String theWord = null;
			char[] arr = null;
			int i = 0;
			String sourceWord = null;
			String sameWord = null;
			String[] temp = null;
			String[] sames = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					temp = theWord.split("：");
					sourceWord = temp[0];
					sameWord = temp[1];
					sames = sameWord.split("；");
					
					arr = sourceWord.toCharArray();
					dict.fillWord(arr, sames);
					
					i++;
				}
			} while (theWord != null);
			
			long bb = System.currentTimeMillis();
			System.out.println("词典加载完成，共加载：" + i + " 个词到词典，用时：" + (bb-aa) + " 毫秒");
		} catch (IOException ioe) {
			System.out.println("词典“"+filename+"”词典加载出错，请检索词典路径及文件是否正确：");
			ioe.printStackTrace();
		}finally{
			try {
				if(is != null){
                    is.close();
                    is = null;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return dict;
	}
	
	/**
	 * 获取藏文对应的中文词语
	 *
	 * @author wanglin
	 * @date Oct 18, 2016
	 *
	 * @param word
	 * @return
	 */
	public String[] getTibetSames(String word){
		return tibetSames.getSames(word.toCharArray(), word.length());
	}
	public String[] getTibetSames(char[] charArr){
		return tibetSames.getSames(charArr, charArr.length);
	}
	public String[] getTibetSames(char[] charArr, int end){
		return tibetSames.getSames(charArr, end);
	}
	
	
	/**
	 * 获取中文对应的藏文词语
	 *
	 * @author wanglin
	 * @date Oct 18, 2016
	 *
	 * @param word
	 * @return
	 */
	public String[] getChineseSames(String word){
		return chinaSames.getSames(word.toCharArray(), word.length());
	}
	public String[] getChineseSames(char[] charArr){
		return chinaSames.getSames(charArr, charArr.length);
	}
	public String[] getChineseSames(char[] charArr, int end){
		return chinaSames.getSames(charArr, end);
	}
}
