package cn.com.unionlib.reader;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 
 * @description 读取文档内容
 * 
 * <p>支持doc、docx、xls、xlsx、pdf、txt文件内容读取<br>
 * 
 * 但有部分doc和pdf内容读取会失败<br>
 * 
 * 入口函数：readerFile(String filePath)
 * 
 * <p>
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Jun 26, 20155:30:28 PM
 *
 */
public class CommonReader {

	/**
	 *
	 * @author MaChao
	 * @date Jun 26, 2015
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		String text = readerFile("E:\\ResourceFile\\baseDir\\resource\\1521513591106\\20180320_1005\\1522390590530_data.pdf");
		System.out.println(text);
	}
	
	public static String readerFile(String filePath){
		if(filePath == null || filePath.length() < 1){
			return null;
		}
		String extname = filePath.substring(filePath.lastIndexOf(".") + 1);
		extname = extname.toLowerCase();
		
		String text = "";
		
		if(extname.equals("doc") || extname.equals("docx")){
			text = ReaderWord.readerDoc(filePath);
		}
		else if (extname.equals("pdf")){
			String txtPath = filePath.replace(".pdf", ".txt");
		 	File file = new File(txtPath);
			if(file.exists()){
				text = readerTxt(txtPath);
			}else{
				System.out.println("文件“" + txtPath + "”不存在");
				text = ReaderPdf.readerPdf(filePath);
			}
		}
		else if (extname.equals("txt")){
			text = readerTxt(filePath);
		} else {
			System.out.println("文件“"+filePath+"” 未知文档格式类型，无法读取！");
		}
		return text;
	}

	/**
	 * 读取文本文件(主要针对.txt)
	 *
	 * @author MaChao
	 * @date Jun 26, 2015
	 *
	 * @param filepath
	 * @return
	 */
	public static String readerTxt(String filepath){
		File file = new File(filepath);
		if(file.exists() == false){
			System.out.println("文件“" + filepath + "”不存在");
			return null;
		}
		String line = null;
		StringBuffer buffer = new StringBuffer();
		try {
			//获取编码格式，不同编码文件要用不同的编码读取
			String encode = getCharset(filepath);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file),encode);
			BufferedReader br = new BufferedReader(isr);
			line = br.readLine();
			while(line != null){
				buffer.append(line + "\n");
				line = br.readLine();
			}
			line = buffer.toString();
			buffer = null;
			isr.close();
			br.close();
			return line;
		} catch (FileNotFoundException e) {
			System.out.println("文件“" + filepath + "”不存在");
			return null;
		} catch (IOException e) {
			System.out.println("文件“"+filepath+"”读取失败：" + e.getMessage());
			return null;
		}
	}
	
	/**
	 * 获取txt文件的编码格式
	 *
	 * @author MaChao
	 * @date Jun 26, 2015
	 *
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private static String getCharset(String fileName) throws IOException{  
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(fileName));    
        int p = (bin.read() << 8) + bin.read();    
          
        String code = null;    
          
        switch (p) {    
            case 0xefbb:    
                code = "UTF-8";    
                break;    
            case 0xfffe:    
                code = "Unicode";    
                break;    
            case 0xfeff:    
                code = "UTF-16BE";    
                break;    
            default:    
                code = "GBK";    
        }    
        return code;  
}
}
