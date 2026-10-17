package org.apache.lucene.analysis.sun;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.sun.core.TibetAnalyzer;
import org.apache.lucene.analysis.sun.dic.SameDictionary;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;
import org.apache.lucene.analysis.tokenattributes.TypeAttribute;
import org.wltea.analyzer.lucene.IKAnalyzer;


public class TestAnalyzer {

	/**
	 * @author wanglin
	 * @date Sep 19, 2016
	 *
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		TestAnalyzer ta = new TestAnalyzer();
		String txt = "པར་དབང་ཡོད་ཚད་མཆོད་མེ་བོད་ཀྱི་རྩོམ་རིག་དྲ་བར་དབང་བས་ཆོག་མཆན་མ་ཐོབ་པར་གཞན་གྱིས་བརྒྱུད་འགོད་བྱེད་མི་ཆོག" +
				"版权所有： © 2005-2015 tibetcm.com All rights reserved. Powered by: tibetitw.com " +
				"E-mail:[email protected]   QQ:492779202";

		TibetAnalyzer a = new TibetAnalyzer(false, false);
		Analyzer ik = new IKAnalyzer(false);
		
//		txt = "བོད་ལྗོངས་སློབ་གྲྭ་ཆེན་མོ";
//		txt = "ཡིག་ཀ་ཐོག་དགོན་བསྒྱུར་བྱ་དགོས་པའི་ནང་དོན་འདིར་བླུགས་རོགས";
//		txt = FileUtils.readFileToString(new File("G:/wisZw/text/2016/20161013/1021/1351476329385018.txt"), "UNICODELITTLE");
//		char[] c = txt.toCharArray();
//		Dictionary dic = Dictionary.getSingleton();
//		System.out.println(dic.matchInTibetDict(txt.toCharArray()));
//		ta.displayToken(a, "བོད་ལྗོངས་སློབ་གྲྭ");
		displayToken(ik, txt);
//		ta.displayToken(ik, "西藏大学图书馆");
//		ta.isExitsDic(txt);
//		ta.displayToken(ik, "的我共不为试");
//		System.out.println("切分完成：" + c.length);
		
//		ta.testHigh("联图科技入", a);
		
		
//		String str = "ཚན་རིག"; 
//		Dictionary dic = Dictionary.getSingleton();
//		DictionaryForList dfl = DictionaryForList.getSingleton();
//		System.out.println(dic.matchInMainDict(str.toCharArray()));
//		System.out.println(dfl.matchInMainDict(str));
//		ta.margeDic();
		
//		String word = "成都市";
//		ta.samedic(word.toCharArray() , "zh");
	}
	
	
	private void samedic(char[] arr, String lanType){
		SameDictionary dic = SameDictionary.getSingleton();
		String[] sames = null;
		if(lanType.equals("zw")){
			sames = dic.getTibetSames(arr);
		}else if (lanType.equals("zh")){
			sames = dic.getChineseSames(arr);
		}
		
		if(sames == null || sames.length < 1){
			System.out.println("未找到同义词");
			return;
		}
		for(String s : sames){
			System.out.println(s);
		}
	}

	public static int displayToken(Analyzer a, String txt) throws IOException{
		TokenStream ts = null;
		StringBuffer sb = new StringBuffer();
		sb.append(txt + "\r\n\r\n");
		int i = 0;
		try {
			ts = a.tokenStream("words", txt);
			
			//位置增量的属性，存储语汇单元之间的距离
			PositionIncrementAttribute pia = ts.addAttribute(PositionIncrementAttribute.class);
			//每个语汇单元的位置偏移量
			OffsetAttribute oa = ts.addAttribute(OffsetAttribute.class);
			//存储每一个语汇单元的信息（分词单元信息）
			CharTermAttribute cta = ts.addAttribute(CharTermAttribute.class);
			//使用的分词器的类型信息
			TypeAttribute ta = ts.addAttribute(TypeAttribute.class);
			
//			ts.reset();
			while(ts.incrementToken()){
				System.out.print((i+1) + "：    position：" + pia.getPositionIncrement()+"    token：'" + cta+"'  legth："+cta.length()+"  offset：["+oa.startOffset()+"-"+oa.endOffset()+"]-->"+ta.type()+"\n");
				sb.append((i+1) + "：  " + pia.getPositionIncrement()+"    '" + cta+"'    "+cta.length()+"    offset：["+oa.startOffset()+"-"+oa.endOffset()+"]-->"+ta.type()+"\r\n");
				i++;
			}
			
			File f = new File("D:/splitWord.txt");
			FileUtils.writeStringToFile(f, sb.toString(), "UNICODELITTLE");
			System.out.println("\n切分完成，分词结果已保存至：D:/splitWordfont.txt   字符总数：" + txt.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
		return i;
	}
	

	//分词器效率测试
	private void testSpeed(Analyzer a) throws IOException{
		File fileDir = new File("G:/testfile/zwweb/");
		File[] fileArr = fileDir.listFiles();
		String txt = null; 
		long aa = System.currentTimeMillis();
		int i = 1;
		for(File f : fileArr){
			txt = FileUtils.readFileToString(f, "UNICODELITTLE");
			int count = displayToken(a, txt);
//			System.out.println("第：" + i++ + " 个文本共分出词语个数：" + count );
		}
		long bb = System.currentTimeMillis();
		System.out.println("分词器：" + a + " 切分完用时：(" + (bb - aa) + ") 毫秒");
	}
	
	/**
	 * 合并两个词典中的词语到一个词典，同时支队
	 *
	 * @author wanglin
	 * @date Sep 30, 2016
	 *
	 */
	private void margeDic(){
		String dicA = "G:/zwdic3.txt";
		String dicB = "G:/main2016.dic";
		String destPath = "G:/main2016v3.dic";
		File fileA = new File(dicA);
		File fileB = new File(dicB);
		File destFile = new File(destPath);
		String charset = "UNICODELITTLE";
		
		List<String> tempList = new ArrayList<String>();
		StringBuffer sb = new StringBuffer();
		InputStream is = null;
		try {
			is = new FileInputStream(fileA);
			BufferedReader br = new BufferedReader(new InputStreamReader(is, charset),512);
			String line = null;
			int anum = 0;
			while((line = br.readLine()) != null){
				if(tempList.contains(line) == false){
					sb.append(line).append("\r\n");
					tempList.add(line);
				}
				anum++;
			}
			
			is = new FileInputStream(fileB);
			br = new BufferedReader(new InputStreamReader(is, charset),512);
			int bnum = 0;
			while((line = br.readLine()) != null){
				if(tempList.contains(line) == false){
					sb.append(line).append("\r\n");
					tempList.add(line);
				}
				bnum++;
			}
			
			String result = sb.toString();
			sb = null;
			FileUtils.writeStringToFile(destFile, result, charset);
			System.out.println("合并完成。文件A词语数：" + anum + "  文件B词语数：" + bnum + "   合并结果词语数：" + tempList.size());
			tempList = null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(is != null){
				try {
					is.close();
					is = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * 判断词语是否在词典中存在
	 *
	 * @author wanglin
	 * @date Oct 18, 2016
	 *
	 * @param s
	 */
	private void isExitsDic(String s){
		InputStream is = this.getClass().getResourceAsStream("dic/tibetan.dic");
		if(is == null){
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
		}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UNICODELITTLE"),512);
			String theWord = null;
			String result = "词典加载完成，未找到该词语";
			do {
				theWord = br.readLine();
				if (theWord != null && theWord.equals(s)) {
					result = "该词语存在";
					break;
				}
			} while (theWord != null);
			System.out.println(result);
		} catch (IOException ioe) {
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
			ioe.printStackTrace();
		}
	}
	
	
	private void dicNum(String dicname){
		InputStream is = this.getClass().getResourceAsStream(dicname);
		if(is == null){
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
		}
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is,"UNICODELITTLE"),512);
			String theWord = null;
			int i = 0;
			do {
				theWord = br.readLine();
				if (theWord != null) {
					i++;
				}
			} while (theWord != null);
			System.out.println("共有：" + i + "  个词语");
		} catch (IOException ioe) {
			System.out.println("词典加载出错，请检索词典路径及文件是否正确：");
			ioe.printStackTrace();
		}
	}
}
