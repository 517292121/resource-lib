package cn.com.unionlib.lucene.base;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharSet;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.file.EncodingDetect;

/**
 * 
 * 系统中文本文件读取，存储，目录等处理
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date May 31, 20162:13:49 PM
 *
 */
public class IoCommonUtil {
	
	//子目录文件夹起始编号，暂定为四位数；
	//由于是数字累加，所以即便超过四位数，依然可以继续扩大
	final public static int FOLDER_BEGIN_NO = 1001;
	
	//每个文件夹最大存放文件个数
	public static int FOLDER_MAX_FILE = 1200;
	
	//统一的文本编码
	final public static String TXT_FILE_CODE = "UNICODELITTLE";
	
	
	
	/**
	 * 自动获取网页文本存放路径
	 * 路径格式为text.folder /yyyy/yyyyMMdd/编码，如G:/wis/text/2016/20160613/1005
	 * 系统自动根据当前日期封装对应的路径，再计算出相应的编号目录
	 * 如果路径存在，则直接返回路径，不存在先创建目录后返回路径
	 *
	 * @author MaChao
	 * @date Jun 13, 2016
	 *
	 * @return
	 */
	public static String getTextDir(){
		String dir = createDir(SysConfigInfo.INDEX_TXT_DIR, true);
		return dir;
	}

	
	
	private static String createDir(String root, boolean isYear){
		//获取当前年月日
		String nowStr = getNowStr(isYear);
		if(!root.endsWith("/") && !root.endsWith("\\")){
			root = root + "/";
		}
		
		//根据配置的根目录加上当前年月日，得到当天的目录
		String folder = root + nowStr;
		File f = new File(folder);
		
		//如果当天目录不存在或者当天的目录中没有任何子目录，则创建一个当天加一个起始编号的子目录
		if(f.exists() == false 
				|| f.list() == null 
				|| f.list().length < 1){
			f = new File(folder + "/" + FOLDER_BEGIN_NO);
			f.mkdirs();
			return f.getAbsolutePath();
		}
		
		//遍历所有子目录得到最大的子目录
		File[] files = f.listFiles();
		int max = FOLDER_BEGIN_NO;
		for(File file : files){
			if(file.isDirectory() == false){
				continue;
			}
			String foldername = file.getName();
			try {
				int temp = Integer.parseInt(foldername);
				if(temp > max){
					max = temp;
				}
			} catch (Exception e) {}
		}
		
		//如果最大的目录中文件个数已经达到目录最大文件存放个数
		//则在最大目录上加一重新建一个文件夹
		f = new File(folder + "/" + max);
		if(f.list() != null && f.list().length >= FOLDER_MAX_FILE){
			f = new File(folder + "/" + (max+1));
			f.mkdirs();
		}
		files = null;
		return f.getAbsolutePath();
	}
	
	/**
	 * 获取当年下当天的目录格式
	 * yyyy/yyyyMMdd格式的字符串功能
	 *
	 * @author MaChao
	 * @date Jun 8, 2016
	 *
	 * @return demo：2016/20160513
	 */
	public static String getNowStr(boolean isYear){
		Calendar now = Calendar.getInstance();  
        int y = now.get(Calendar.YEAR);  
        int m = now.get(Calendar.MONTH) + 1;  
        int d = now.get(Calendar.DAY_OF_MONTH);  
        
        String mstr = (m<10 ? "0" : "") + m;
        String dstr = (d<10 ? "0" : "") + d;
        String datestr = y + mstr + dstr;
        if(isYear){
        	datestr = y + "/" + datestr;
        }
        return datestr;
	}
	
	/**
	 * 读取txt文件内容
	 *
	 * @author MaChao
	 * @date May 31, 2016
	 *
	 * @param txtPath
	 * @return
	 */
	public static String readTxt(String txtPath){
		File f = new File(txtPath);
		if(f.exists() == false){
			return "";
		}
		try {
			String fileCode = getCharset(txtPath);
			InputStreamReader read = null;
			read = new InputStreamReader(new FileInputStream(f), fileCode);
			BufferedReader reader = new BufferedReader(read);
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while((line = reader.readLine()) != null){
				buffer.append(line).append("\n");
			}
			read.close();
			reader.close();
			
			line = buffer.toString().trim();
			buffer = null;
			return line;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("文件读取失败");
			return "";
		}
	}
	
	/**
	 * 写入文本
	 * @param txt
	 * @param fileName
	 * @return
	 */
	public static synchronized String writeToTxt(String txt, String fileName) {
		String dir = getTextDir();
		File file = new File(dir + "/" + fileName);
		try {
			FileUtils.writeStringToFile(file, txt, TXT_FILE_CODE);
			String filePath = file.getAbsolutePath();
			file = null;
			return filePath;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 更新文件内容
	 * @param txt
	 * @param fileName
	 * @return
	 */
	public static synchronized void updateFileTxt(String filePath, String txt) {
		File file = new File(filePath);
		try {
			FileUtils.writeStringToFile(file, txt, TXT_FILE_CODE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除文件
	 * @param fileUrl
	 */
	public static void deleteFile(String fileUrl){
		if(!CommonUtil.isNotNull(fileUrl)){
			return;
		}
		File f = new File(fileUrl);
		if(f.exists()){
			f.delete();
		}
	}
	
	
	/**
	 * 写入txt文件内容<br>
	 * 如果文件已存在，则覆盖之前文件的内容<br>
	 * 如果文件目录不存在，则自动创建文件目录
	 * 
	 *
	 * @author MaChao
	 * @date May 31, 2016
	 *
	 * @param txtPath
	 * @param content
	 * @return
	 */
	private static boolean writeTxt(String txtPath, String content, String charset){
		File file = new File(txtPath);
		File folder = new File(file.getParent());
		if(folder.exists() == false){
			try {
				boolean b = folder.mkdirs();
				if(b == false){
					System.out.println("错误：文件创建失败：" + folder.getPath());
					return false;
				}
			} catch (Exception e) {
				System.out.println("错误：文件\""+folder+"\"创建异常 " + e.getMessage());
				return false;
			}
		}
		
		try {
			OutputStreamWriter writer = null;
			if(CommonUtil.isNotNull(charset)){
				writer = new OutputStreamWriter(new FileOutputStream(file), charset);
			}else{
				writer = new OutputStreamWriter(new FileOutputStream(file));
			}
			BufferedWriter bw = new BufferedWriter(writer);
			bw.write(content);
			bw.close();
			writer.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	//获取txt文件编码，以实现读取任意编码的文件内容
	private static String getCharset(String txtPath) throws IOException {
//		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(txtPath));
//		int p = (bin.read() << 8) + bin.read();
//		String code = null;
//		switch (p) {
//		case 0xefbb:
//			code = "UTF-8";
//			break;
//		case 0xfffe:
//			code = "Unicode";
//			break;
//		case 0xfeff:
//			code = "UTF-16BE";
//			break;
//		default:
//			code = "GBK";
//		}
//		return code;
		
		String code = EncodingDetect.getJavaEncode(txtPath);
		return code;
	}
}





















