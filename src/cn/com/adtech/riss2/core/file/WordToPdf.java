package cn.com.adtech.riss2.core.file;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;

/**
 * 论文源文转换转换工具类
 * @author MaChao
 * @date 	2019-04-23
 * @version	1.0
 *
 */
public class WordToPdf {
	
	public final static String licenseStr = "<License>\n" +
									        "  <Data>\n" +
									        "    <Products>\n" +
									        "      <Product>Aspose.Total for Java</Product>\n" +
									        "      <Product>Aspose.Words for Java</Product>\n" +
									        "    </Products>\n" +
									        "    <EditionType>Enterprise</EditionType>\n" +
									        "    <SubscriptionExpiry>20991231</SubscriptionExpiry>\n" +
									        "    <LicenseExpiry>20991231</LicenseExpiry>\n" +
									        "    <SerialNumber>8bfe198c-7f0c-4ef8-8ff0-acc3237bf0d7</SerialNumber>\n" +
									        "  </Data>\n" +
									        "  <Signature>sNLLKGMUdF0r8O1kKilWAGdgfs2BvJb/2Xp8p5iuDVfZXmhppo+d0Ran1P9TKdjV4ABwAgKXxJ3jcQTqE/2IRfqwnPf8itN8aFZlV3TJPYeD3yWE7IT55Gz6EijUpC7aKeoohTb4w2fpox58wWoF3SNp6sK6jDfiAUGEHYJ9pjU=</Signature>\n" +
									        "</License>";

	public static boolean getLicense() {
       boolean result = false;
       try {
           InputStream is = new ByteArrayInputStream(licenseStr.getBytes("UTF-8"));

       		License aposeLic = new License();
       		aposeLic.setLicense(is);
       		result = true;
       } catch (Exception e) {
           e.printStackTrace();
           System.out.println("转换jar验证失败...");
       }
       return result;
   }
	
	/**
	 * 源文转换开始
	 * @param folder		文件保存文件夹
	 * @param Address		文件名
	 * @return
	 * 
	 * @author MaChao
	 * @date		2019-04-23
	 * @version		1.0
	 */
	public static synchronized String conversionInit(String folder, String name) {
		long old = System.currentTimeMillis();
		//截取文件后缀
		String s = suffix(name);
		if("pdf".equals(s)){	//跳过pdf文件，避免重复转换
			return name;
		}
		if("doc".equals(s) && "docx".equals(s)){
			throw new ErrorMessageException("系统只支持pdf,doc,docx源文，请重新上传");
		}
        if (!getLicense()) {          // 验证License 若不验证则转化出的pdf文档会有水印产生
        	throw new ErrorMessageException("转换工具验证失败，不能正常转换，请上传pdf");
        }
        String out = replaceName(name);
        FileOutputStream os = null;
        Document doc = null;
        try {  
            File file = new File(folder+out);  
            os = new FileOutputStream(file);
            doc = new Document(folder+name);                   
            doc.save(os, SaveFormat.PDF);//全面支持DOC, DOCX, OOXML, RTF HTML, OpenDocument, PDF, EPUB, XPS, SWF 相互转换
            long now = System.currentTimeMillis();
            System.out.println("共耗时：" + ((now - old) / 1000.0) + "秒");  //转化用时
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally{
        	if(os != null){
        		try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				os = null;
        	}
        	doc = null;
        }
        return out;
    }
	
	//截取后缀名
    public static String suffix(String name){
    	if(name == null || name.isEmpty() || name.indexOf(".") == -1){
    		return "";
    	}
    	String suffix = name.substring(name.lastIndexOf(".")+1, name.length()).toLowerCase();
    	return suffix;
    }
    
    //更改后缀名
    public static String replaceName(String name){
    	if(name == null || name.isEmpty() || name.indexOf(".") == -1){
    		return null;
    	}
    	String suffix = name.substring(0, name.lastIndexOf(".")+1)+"pdf";
    	return suffix;
    }
    
    public static void main(String[] args){
//    	File f = new File("D:/resbook/yxg/1WORD版17本");
//    	File[] fs = f.listFiles();
//    	long a = System.currentTimeMillis();
//    	for(int i=0; i<fs.length; i++){
//    		conversionInit("", fs[i].getAbsolutePath());
//    		System.out.println(i);
//    	}
//    	long b = System.currentTimeMillis();
//    	System.out.println("用时：" + (b-a));
    	
    	String s = conversionInit("D:/test/word2pdf/", "44.docx");
    	System.out.println(s);
    }
    
}
