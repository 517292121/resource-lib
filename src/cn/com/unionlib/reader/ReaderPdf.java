package cn.com.unionlib.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.pdfbox.pdfparser.PDFParser;
import org.pdfbox.pdmodel.PDDocument;
import org.pdfbox.util.PDFTextStripper;

import com.spire.pdf.PdfDocument;
import com.spire.pdf.PdfPageBase;
import com.spire.pdf.widget.PdfPageCollection;

import cn.com.adtech.riss2.core.CommonUtil;

/**
 * 
 * @description 读取pdf文档；目前仍有一部分pdf无法读取，还没找到完整的解决方案
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Jun 26, 20153:18:20 PM
 *
 */
public class ReaderPdf {
	
	private final static String ENCODING = "UTF-8";
	
	/**
	 * 此方法读取pdf效率高，但部分文件无法读取，当读取失败时调用另一个方法读取
	 *
	 * @author MaChao
	 * @date Jun 13, 2015
	 *
	 * @param filePath
	 * @return
	 */
	public static String readerPdf(String filePath){
		File file = new File(filePath);
		if (file.exists() == false) {
			System.out.println("文件“" + filePath + "”不存在");
			return null;
		}
		
		File txt = null;
		try {
			txt = pdf2Txt(filePath);
		} catch(IllegalArgumentException e) {
			System.out.println("xpdf读取失败, 使用默认方式读取...");
		}
		
		FileInputStream fis = null;
		PDDocument pdd = null;
		try {
			if(txt == null) {
				fis = new FileInputStream(file);
				PDFParser p = new PDFParser(fis);
				p.parse();     
				pdd = p.getPDDocument();     
				PDFTextStripper ts = new PDFTextStripper(); 
				
				String ct = ts.getText(pdd).trim();
				// 持久化到txt
				txt = new File(file.getParent(), FilenameUtils.getBaseName(file.getName())+".txt");
				FileUtils.writeStringToFile(file, ct, ENCODING);
			}
			
			return FileUtils.readFileToString(txt, ENCODING);
		} catch (Exception e) {
			System.out.println(filePath + "：读取出错");
			return readerPdf2(filePath);
		} finally {
			if(fis != null) {
				try {
					fis.close();
					fis = null;
				} catch (IOException e) {
					e.printStackTrace();
				}  
			}
			if(pdd != null) {
				try {
					pdd.close();
					pdd = null;
				} catch (IOException e) {
					e.printStackTrace();
				}     
			}
			
		}   
	}
	
	/**
	 * 该方法读取稳定、但是效率低，且容易造成内存溢出；因此当上面方法无法读取时再调用此方法
	 *
	 * @author MaChao
	 * @date Jun 13, 2015
	 *
	 * @param filePath
	 * @return
	 */
	public static String readerPdf2(String filePath){
		File file = new File(filePath);
		if (file.exists() == false) {
			System.out.println("文件“" + filePath + "”不存在");
			return null;
		}
		
		long len = file.length();
		if(len > (1024*1024*50)){
			//超过10M的PDF文件一般很难读取，需要占用大量内存，视为双层PDF或超大文件。放弃文字提取
			//如因业务需要可做调
			System.out.println("超过50M的PDF文件，放弃文字读取！");
			return null;
		}
		
		PdfDocument doc = null;
		try {
			// 创建PdfDocument实例
			doc = new PdfDocument();
			// 加载PDF文件
			doc.loadFromFile(filePath);
			StringBuilder sb = new StringBuilder();
			PdfPageBase page;
			
			System.out.print("读取中");
			// 遍历PDF页面，获取文本
			PdfPageCollection colls = doc.getPages();
			int pages = colls.getCount();
			for (int i = 0; i < pages; i++) {
				page = colls.get(i);
				sb.append(page.extractText(true));
				System.err.print(".");
			}
			return sb.toString();
		} catch (Exception e) {
			return null;
		} finally {
			if(doc != null) {
				doc.close();
				doc = null;
			}
		}
	}
	/**
	 * 获取pdf每页内容
	 * @author MaChao
	 * @date 2019-11-12
	 * @param filePath
	 * @return
	 */
	public static List<String> readerPdfs(String filePath){
		File file = new File(filePath);
		if (file.exists() == false) {
			System.out.println("文件“" + filePath + "”不存在");
			return null;
		}
		
		long len = file.length();
		if(len > (1024*1024*50)){
			//超过10M的PDF文件一般很难读取，需要占用大量内存，视为双层PDF或超大文件。放弃文字提取
			//如因业务需要可做调
			System.out.println("超过50M的PDF文件，放弃文字读取！");
			return null;
		}
		
		PdfDocument doc = null;
		try {
			// 创建PdfDocument实例
			doc = new PdfDocument();

			// 加载PDF文件
			doc.loadFromFile(filePath);

			List<String> result=new ArrayList<String>();
			
			PdfPageBase page;
			
			System.out.print("读取中");
			// 遍历PDF页面，获取文本
			PdfPageCollection colls = doc.getPages();
			int pages = colls.getCount();
			for (int i = 0; i < pages; i++) {
				page = colls.get(i);
				try {
					result.add(page.extractText(true));
				} catch (Exception e) {
					// TODO: handle exception
				}
				System.err.print(".");
			}
			return result;
		} catch (Exception e) {
			return Collections.EMPTY_LIST;
		} finally {
			if(doc != null) {
				doc.close();
				doc = null;
			}
		}
		
	}
	
	private static String PATH_TO_XPDF_TEXT =  CommonUtil.getProperties("/config.properties", "xpdf.pdftotext");
    /**
     * PDF转TXT，TXT与PDF同名(此方法读取速度最快)
     * @param path	pdf的物理路径
     * @return
     */
	public static File pdf2Txt(String path) throws IllegalArgumentException{
		if(StringUtils.isBlank(PATH_TO_XPDF_TEXT)) {
			throw new IllegalArgumentException("xpdf未配置!");
		}
    	if(StringUtils.isBlank(path)) {
    		throw new IllegalArgumentException("path不能为空");
    	}
    	
    	File pdf = new File(path);
    	if(pdf == null || !pdf.exists() || !pdf.isFile()) {
    		throw new IllegalArgumentException("pdf文件不存在!");
    	}
    	
    	Process p = null;
        try {
        	// 使用命令执行
            p = Runtime.getRuntime().exec(new String[]{PATH_TO_XPDF_TEXT, "-layout", path});
            // 等待当前进程结束
            p.waitFor();
            // 获取当前进程最终的结果
            int status = p.exitValue();
            
            if(status == 1 || status == 3) {
            	throw new IllegalArgumentException("PDF打开失败!");
            }
            if(status == 2) {
            	throw new IllegalArgumentException("创建TXT权限不足，请检查用户权限!");
            }
            if(status == 99) {
            	throw new IllegalArgumentException("PDF读取发生未知错误!");
            }
            
            File txt = new File(pdf.getParent(), FilenameUtils.getBaseName(pdf.getName())+".txt");
            if(txt == null || !txt.exists() || !txt.isFile()) {
            	throw new IllegalArgumentException("PDF转TXT失败，未知错误!");
            }
            return txt;
        } catch (Exception e) {
            e.printStackTrace();
        	throw new IllegalArgumentException("系统错误!");
        } finally {
        	if(p != null) {
        		p.destroy();
        	}
        }
    }
	
	public static void main(String[] args) throws IOException {
		long currTime = System.currentTimeMillis();
		String txt = readerPdf("D:/ResourceFile/公用测试数据/pdf/10616_20180315_赖斌.pdf");
		System.out.println(txt);
		System.out.println("耗时: "+(System.currentTimeMillis() - currTime));
	}
}
