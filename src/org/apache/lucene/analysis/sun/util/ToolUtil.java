package org.apache.lucene.analysis.sun.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.commons.io.FileUtils;

public class ToolUtil {

	/**
	 *
	 * @author wanglin
	 * @date Oct 17, 2016
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		ToolUtil tu = new ToolUtil();
//		tu.cc();
		
		List<String> tibetDic = tu.getTibetList();
		System.out.println(tibetDic.contains("ཁྲུན་ཏུའ"));
	}
	
	public void cc(){
		File tibetDicFile = new File("G:/tibetan-same.dic");
		List<String> tibetDic = getTibetList();
		try {
			InputStream is = new FileInputStream(tibetDicFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UNICODELITTLE"),512);
			String theWord = null;
			String tibetWord = null;
			String[] arr = null;
			StringBuffer sb = new StringBuffer();
			int i = 0;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					arr = theWord.split("：");
					if(arr[1].equals("春秋")){
						System.out.println(i);
					}
					tibetWord = arr[0];
					if(tibetDic.contains(tibetWord) == false){
						sb.append(theWord).append("\r\n");
						i++;
					}
				}
			} while (theWord != null);
			
			System.out.println("共有：" + i + " 个词语不存在");
			System.out.println(sb.toString());
		} catch (IOException ioe) {
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
			ioe.printStackTrace();
		}
	}
	
	
	public void bb(){
		File tibetDicFile = new File("G:/same3.txt");
		try {
			InputStream is = new FileInputStream(tibetDicFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UNICODELITTLE"),512);
			String theWord = null;
			String tibetWord = null;
			String zhWord = null;
			StringBuffer sb = new StringBuffer();
			StringBuffer failSb = new StringBuffer();
			int a = 0, b=0;
			String[] arr = null;
			char[] charArr = null;
			List<Character> fuhao = new ArrayList<Character>();
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					arr = theWord.split("：");
					tibetWord = arr[0];
					zhWord = arr[1];
					
					zhWord = zhWord.replace("，", "；").trim();
					sb.append(tibetWord).append("：").append(zhWord).append("\r\n");
				}
			} while (theWord != null);
			
			
			System.out.println("a：" + a + "   b：" + b);
//			FileUtils.writeStringToFile(new File("G:/same3.txt"), sb.toString(), "UNICODELITTLE");
//			FileUtils.writeStringToFile(new File("G:/same4.txt"), failSb.toString(), "UNICODELITTLE");
		} catch (IOException ioe) {
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
			ioe.printStackTrace();
		}
	}
	
	
	public void aa(){
		File tibetDicFile = new File("G:/zw2zh.txt");
		try {
			InputStream is = new FileInputStream(tibetDicFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UNICODELITTLE"),512);
			String theWord = null;
			String tibetWord = null;
			String zhWord = null;
			StringBuffer sb = new StringBuffer();
			StringBuffer failSb = new StringBuffer();
			int a = 0, b=0;
			String[] arr = null;
			char[] charArr = null;
			List<Character> fuhao = new ArrayList<Character>();
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					arr = theWord.split("：");
					tibetWord = arr[0];
					zhWord = arr[1];
					
					boolean fh = false;
					charArr = zhWord.toCharArray();
					for(char c : charArr){
						if(!LanguageUtil.isChinese(c) && !isSplit(c)){
							fh = true;							
						}
					}
					if(fh){
						a++;
						failSb.append(theWord).append("\r\n");
					}else{
						sb.append(theWord).append("\r\n");
						b++;
					}
				}
			} while (theWord != null);
			
			
			System.out.println("a：" + a + "   b：" + b);
			FileUtils.writeStringToFile(new File("G:/same3.txt"), sb.toString(), "UNICODELITTLE");
			FileUtils.writeStringToFile(new File("G:/same4.txt"), failSb.toString(), "UNICODELITTLE");
		} catch (IOException ioe) {
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
			ioe.printStackTrace();
		}
	}
	
	private boolean isSplit(char c){
		if(c == '，' || c == '、' || c == '；' || c == '。'){
			return true;
		}
		return false;
	}
	
	private int getChineseIndex(String str){
		char[] arr = str.toCharArray();
		int s = arr.length;
		for(int i=0; i<s; i++){
			if(LanguageUtil.isChinese(arr[i])){
				return i;
			}
		}
		return 0;
	}
	
	public List<String> getTibetList(){
		File tibetDicFile = new File("G:/tibetan.dic");
		List<String> tibetList = new ArrayList<String>();
		try {
			InputStream is = new FileInputStream(tibetDicFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UNICODELITTLE"),512);
			String theWord = null;
			do {
				theWord = br.readLine();
				if (theWord != null && !"".equals(theWord.trim())) {
					tibetList.add(theWord.trim());
				}
			} while (theWord != null);
			
			return tibetList;
		} catch (IOException ioe) {
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
			ioe.printStackTrace();
			return null;
		}
	}

}
