package org.apache.lucene.analysis.sun.dic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 
 * 词库单态对象
 *
 * @author wanglin
 * 
 * @version 1.0
 *
 * @date Sep 19, 20162:42:05 PM
 *
 */
public class Dictionary {

	private static Dictionary singleton;
	
	final private String stopFilename = "stop.dic";
	private final String tibetanFilename = "tibetan.dic";
	private final String chineseFilename = "chinese.dic";
	
	/**
	 * 藏文词典
	 */
	private DictTree tibetDict = null;
	
	/**
	 * 中文词典
	 */
	private DictTree chinaDict = null;
	
	/**
	 * 禁用词典集合对象
	 */
	private DictTree stopDict = null;
	
	/**
	 * 藏文词典中最大词语字符长度
	 */
	private int maxTibetWordChar = 0;
	
	/**
	 * 中文词典中最大词语字符长度
	 */
	private int maxChinaWordChar = 0;
	
	
	private Dictionary(){
		loadTibetDict();
		loadChineseDict();
		loadStopDict();
	}
	
	public static Dictionary getSingleton(){
		if(singleton == null){
			singleton = new Dictionary();
		}
		return singleton;
	}
	
	/**
	 * 获取主词典
	 *
	 * @author wanglin
	 * @date Sep 19, 2016
	 *
	 * @return
	 */
	public DictTree getTibetDict(){
		return this.tibetDict;
	}
	
	/**
	 * 添加一个词到词典
	 *
	 * @author wanglin
	 * @date Sep 19, 2016
	 *
	 * @param word
	 */
	public void addTibetDic(char[] charArr){
		if(this.tibetDict != null){
			this.tibetDict.fillWord(charArr);
			
			//TODO 同时更新词典文件
		}
	}
	
	/**
	 * 加载藏文词典
	 *
	 * @author wanglin
	 * @throws IOException 
	 * @date Sep 19, 2016
	 *
	 */
	public void loadTibetDict(){
		this.tibetDict = loadDictTree("tibetan", tibetanFilename);
	}
	
	/**
	 * 加载中文词典
	 *
	 * @author wanglin
	 * @date Oct 11, 2016
	 *
	 */
	public void loadChineseDict(){
		this.chinaDict = loadDictTree("chinese", chineseFilename);
	}
	
	/**
	 * 加载禁用词典
	 *
	 * @author wanglin
	 * @date Sep 23, 2016
	 *
	 */
	public void loadStopDict(){
		this.stopDict = loadDictTree("stop", stopFilename);
	}
	
	private DictTree loadDictTree(String dictName, String filename){
		long aa = System.currentTimeMillis();
		
		DictTree dict = new DictTree('0');
		InputStream is = this.getClass().getResourceAsStream(filename);
		if(is == null){
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
		}
		
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UNICODELITTLE"),512);
			String theWord = null;
			char[] arr = null;
			int i = 0;
			int maxWordChar = 0;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					arr = theWord.toCharArray();
					dict.fillWord(arr);
					
					//取出最大字符长度
					if(arr.length > maxWordChar){
						maxWordChar = arr.length;
					}
					i++;
				}
			} while (theWord != null);
			
			if(dictName.equals("tibetan")){
				this.maxTibetWordChar = maxWordChar;
			}
			if(dictName.equals("chinese")){
				this.maxChinaWordChar = maxWordChar;
			}
			
			long bb = System.currentTimeMillis();
//			System.out.println("词典加载完成，共加载：" + i + " 个词到词典，用时：" + (bb-aa) + " 毫秒");
		} catch (IOException ioe) {
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
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
	 * 内置默认停用词
	 *
	 * @author wanglin
	 * @date Sep 23, 2016
	 *
	 */
	private void defStopDict(){
		this.stopDict = new DictTree('0');
		//TODO 自定义添加停用词
		String[] stopWords = {"and", "are", "as", "at", "be", "is", "brt"};
		for(String s : stopWords){
			this.stopDict.fillWord(s.toCharArray());
		}
	}
	
	/**
	 * 验证词库中是否包含指定的词语
	 *
	 * @author wanglin
	 * @date Sep 19, 2016
	 *
	 * @param word
	 * @return
	 */
	public boolean matchInTibetDict(char[] charArr){
		return tibetDict.match(charArr, charArr.length);
	}
	public boolean matchInTibetDict(char[] charArr, int charLen){
		return tibetDict.match(charArr, charLen);
	}
	
	/**
	 * 验证是否为中文词语
	 *
	 * @author wanglin
	 * @date Oct 11, 2016
	 *
	 * @param charArr
	 * @param charLen
	 * @return
	 */
	public boolean matchInChinaDict(char[] charArr, int charLen){
		return chinaDict.match(charArr, charLen);
	}
	
	
	/**
	 * 验证是否为停用词
	 *
	 * @author wanglin
	 * @date Sep 23, 2016
	 *
	 * @param word
	 * @return
	 */
	public boolean matchInStopDict(char[] charArr){
		return stopDict.match(charArr, charArr.length);
	}
	public boolean matchInStopDict(char[] charArr, int charLen){
		return stopDict.match(charArr, charLen);
	}
	
	/**
	 * 获取藏文词典中最大词语字符长度
	 *
	 * @author wanglin
	 * @date Sep 26, 2016
	 *
	 * @return
	 */
	public int getMaxTibetanWordChar(){
		return this.maxTibetWordChar + 1;
	}
	
	/**
	 * 获取中文词典中最大词语字符长度
	 *
	 * @author wanglin
	 * @date Oct 17, 2016
	 *
	 * @return
	 */
	public int getMaxChineseWordChar(){
		return this.maxChinaWordChar + 1;
	}
}

