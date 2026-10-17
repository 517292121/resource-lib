package cn.com.unionlib.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;

/**
 * 
 * @description 读取word的内容
 * 
 * <p>能够读取到word 2003、2007、2010。doc、docx文件格式的内容</p>
 * 
 * <p>如果你的word文档使用 wps程序</p>
  
   <p>保存过，会出现以下错误，只需要再用 word程序 打开保存一下就可以解决</p>
   
   <p>org.textmining.text.extraction.FastSavedException: Fast-saved files are unsupported at this time</p>
 *
 * <p>使用到的jar包</p>
 * 
 * poi-3.9-20121203.jar <br>
 * poi-ooxml-3.9-20121203.jar <br>
 * poi-ooxml-schemas-3.9-20121203.jar <br>
 * poi-scratchpad-3.9-20121203.jar <br>
 * xmlbeans-2.3.0.jar <br>
 * poi-3.5-beta6-20090622.jar <br>
 * poi-scratchpad-3.5-beta6-20090622.jar <br>
 * dom4j-1.6.1.jar <br>
 * 
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Jun 25, 20153:56:34 PM
 *
 */
public class ReaderWord {

	/**
	 *
	 * @author MaChao
	 * @date Jun 25, 2015
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(readerDoc("G:/testfile/中国劳模专题数据库建设方案.docx"));
	}
	

	/**
	 * 读取word文件的内容入口
	 * 
	 * 首先通过poi读取word 2003 格式的内容，如果发生异常，再读取2007以上版本的内容
	 *
	 * @author MaChao
	 * @date Jun 25, 2015
	 *
	 * @param docFile
	 * @return
	 */
	public static String readerDoc(String filePath){
		File docFile = new File(filePath);
		if (docFile.exists() == false) {
			System.out.println("文件“" + filePath + "”不存在");
			return null;
		}
		
		String text = null;
		try {
			InputStream is = new FileInputStream(docFile);
			
			WordExtractor ex = new WordExtractor(is);//is是WORD文件的InputStream 
			
			text = ex.getText().trim();
			
			System.gc();
			return text;
		} catch (FileNotFoundException e) {
			System.out.println("文件：" + docFile.getPath() + " 不存在");
		} catch (Exception e) {
			try {
				text = readdoc07(docFile.getPath());
			} catch (Exception ex) {
				System.out.println("文件“"+docFile.getPath()+"”获取失败：" + e.getMessage());
			}
		}
		return text;
	}
	
	/**
	 * 读取 word 2007 以上版本文件 .docx格式
	 *
	 * @author MaChao
	 * @date Jun 25, 2015
	 *
	 * @param filePath
	 * @return
	 * @throws IOException 
	 * @throws OpenXML4JException 
	 * @throws XmlException 
	 */
	public static String readdoc07(String filePath) {
		try {
			OPCPackage opcPackage = POIXMLDocument.openPackage(filePath);
			
			POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
			
			String text = extractor.getText().trim();
			
			System.gc();
			return text;
		} catch (Exception e) {
			
//			e.printStackTrace();
			return null;
		}
	}	
	
}
