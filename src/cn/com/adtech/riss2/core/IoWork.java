package cn.com.adtech.riss2.core;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class IoWork {
	
	public static boolean WriteFile(String content, String filepath){
		File file = new File(filepath);
		if(file.exists() == false){
			try {
				boolean b = file.createNewFile();
				if(b == false){
					System.out.println("�����ļ�����ʧ��");
					return false;
				}
			} catch (IOException e) {
				System.out.println("�����ļ�\""+filepath+"\"�����쳣 " + e.getMessage());
				return false;
			}
		}
		
		try {
			FileWriter fw = new FileWriter(file);
			fw.write(content);
			fw.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("�����ļ�д���쳣 " + e.getMessage());
			return false;
		}
	}
	
	
	public static String ReaderFile(String filepath){
		String line = null;
		File file = new File(filepath);
		if(file.exists() == false){
			System.out.println("�ļ�δ�ҵ�");
			return "�ļ�δ�ҵ�";
		}
		StringBuffer buffer = new StringBuffer();
		try {
			InputStreamReader isr = new InputStreamReader(new FileInputStream(file),"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			line = br.readLine();
			while(line != null){
				buffer.append(line + "\n");
				line = br.readLine();
			}
			line = buffer.toString();
			buffer = null;
			return line;
		} catch (FileNotFoundException e) {
			return "�����ļ������쳣��";
		} catch (IOException e) {
			return "�����ļ������쳣��";
		}
	}

	/**
	 * 获取文件编码格式
	 * @author MaChao
	 * @date 2017-4-4
	 */
	public static String getCharset(File file) throws IOException{      
        BufferedInputStream bin = new BufferedInputStream(new FileInputStream(file));    
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
