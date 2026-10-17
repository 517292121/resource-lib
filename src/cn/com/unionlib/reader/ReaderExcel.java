package cn.com.unionlib.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.parsers.DocumentBuilderFactory;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * @description 读取excel的内容
 * 
 * <p>能够读取到excle 2003、2007、2010。xls、xlsx文件格式的内容</p>
 *
 * @author MaChao
 * 
 * @version 1.0
 *
 * @date Jun 26, 201511:19:59 AM
 *
 */
public class ReaderExcel {
 
    /**
     * @param args
     */
    public static void main(String[] args) {
//        String fileUrl = "G:/testxls";
//		File folder = new File(fileUrl);
//		File[] fileList = folder.listFiles();
//		
//		String text = null;
//		for(int i=0,s=fileList.length; i<s; i++){
//			text = readerXls(fileList[i].getPath());
//			if(text == null){
//				System.out.println("文件：" + fileList[i].getPath() + " 读取内容失败");
//			}
//		}
    	
    	String text = readerXlsx("E:/lta0011.xlsx");
//    	String text = readerXlsx("E:/mybook.xlsx");
        System.out.println(text); 
    }
    

	/**
	 * 读取excle文件内容
	 * 默认按照excle2003(.xls)读取，如果读取失败则再按2007以上(.xlsx)版本读取
	 *
	 * @author MaChao
	 * @date Jun 13, 2015
	 *
	 * @param filePath
	 * @return
	 */
	public static String readerXls(String filePath) {
		File file = new File(filePath);
		if (file.exists() == false) {
			System.out.println("文件“" + filePath + "”不存在");
			return null;
		}

		//构建Workbook对象, 只读Workbook对象  
		//直接从本地文件创建Workbook  
		//从输入流创建Workbook  

		FileInputStream fis;
		String text = null;
		try {
			fis = new FileInputStream(file);
			StringBuilder sb = new StringBuilder();
			jxl.Workbook rwb = Workbook.getWorkbook(fis);
			//一旦创建了Workbook，我们就可以通过它来访问  
			//Excel Sheet的数组集合(术语：工作表)，  
			//也可以调用getsheet方法获取指定的工资表  
			Sheet[] sheet = rwb.getSheets();
			for (int i = 0; i < sheet.length; i++) {
				Sheet rs = rwb.getSheet(i);
				for (int j = 0; j < rs.getRows(); j++) {
					Cell[] cells = rs.getRow(j);
					for (int k = 0; k < cells.length; k++)
						sb.append(cells[k].getContents());
				}
			}
			fis.close();
			text = sb.toString();
			sb = null;
			System.gc();
		} catch (Exception e) {
			//如果读取失败，则按xlsx格式再读取
			text = readerXlsx(filePath);
		}
		return text;
	}
    
	/**
	 * 读取excle 2007 (.xlsx) 以上版本的文件
	 *
	 * @author MaChao
	 * @date Jun 26, 2015
	 *
	 * @param filePath
	 * @return
	 */
    public static String readerXlsx(String filePath){
    	// 解压Book1.xlsx
        ZipFile xlsxFile;
        try {
        	StringBuffer buffer = new StringBuffer();
            xlsxFile = new ZipFile(new File(filePath));
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
 
            // 先读取sharedStrings.xml这个文件备用
            ZipEntry sharedStringXML = xlsxFile.getEntry("xl/sharedStrings.xml");
            InputStream sharedStringXMLIS = xlsxFile.getInputStream(sharedStringXML);
            Document sharedString = dbf.newDocumentBuilder().parse(sharedStringXMLIS);
            NodeList str = sharedString.getElementsByTagName("t");
            String sharedStrings[] = new String[str.getLength()];
            for (int n = 0; n < str.getLength(); n++) {
                Element element = (Element) str.item(n);
                sharedStrings[n] = element.getTextContent();
            }
            // 找到解压文件夹里的workbook.xml,此文件中包含了这张工作表中有几个sheet
            ZipEntry workbookXML = xlsxFile.getEntry("xl/workbook.xml");
            InputStream workbookXMLIS = xlsxFile.getInputStream(workbookXML);
            Document doc = dbf.newDocumentBuilder().parse(workbookXMLIS);
            // 获取一共有几个sheet
            NodeList nl = doc.getElementsByTagName("sheet");
 
            for (int i = 0; i < nl.getLength(); i++) {
                Element element = (Element) nl.item(i);// 将node转化为element，用来得到每个节点的属性
                // 接着就要到解压文件夹里找到对应的name值的xml文件，比如在workbook.xml中有<sheet name="Sheet1"
                // sheetId="1" r:id="rId1" /> 节点
                // 那么就可以在解压文件夹里的xl/worksheets下找到sheet1.xml,这个xml文件夹里就是包含的表格的内容
                ZipEntry sheetXML = xlsxFile.getEntry("xl/worksheets/" + element.getAttribute("name").toLowerCase() + ".xml");
                InputStream sheetXMLIS = null;
                try {
                	sheetXMLIS = xlsxFile.getInputStream(sheetXML);
				} catch (Exception e) {
					e.printStackTrace();
//					continue;
				}
                
                Document sheetdoc = dbf.newDocumentBuilder().parse(sheetXMLIS);
                NodeList rowdata = sheetdoc.getElementsByTagName("row");
                for (int j = 0; j < rowdata.getLength(); j++) {
                    // 得到每个行
                    // 行的格式：
                    /*
                     * <row r="1" spans="1:3">r表示第一行,spans表示有几列 <c r="A1"
                     * t="s">//r表示该列的列表
                     * ，t="s"个人认为是表示这个单元格的内容可以在sharedStrings.xml这个文件里找到，对应的节点
                     * 下标就是v节点的值，即0，若没有t属性，则v的值就是该单元格的内容 <v>0</v> </c> <c r="B1"
                     * t="s"> <v>1</v> </c> <c r="C1" t="s"> <v>2</v> </c> </row>
                     */
                    Element row = (Element) rowdata.item(j);
                    // 根据行得到每个行中的列
                    NodeList columndata = row.getElementsByTagName("c");
                    for (int k = 0; k < columndata.getLength(); k++) {
                        Element column = (Element) columndata.item(k);
                        NodeList values = column.getElementsByTagName("v");
                        Element value = (Element) values.item(0);
                        if (column.getAttribute("t") != null
                                & column.getAttribute("t").equals("s")) {
                            // 如果是共享字符串则在sharedstring.xml里查找该列的值
                            buffer.append(sharedStrings[Integer.parseInt(value.getTextContent())]);
                        } else {
                            if (value != null) {
                                buffer.append(value.getTextContent());
                            }
                        }
                    }
                }
            }
            
            System.gc();
            String text = buffer.toString();
            buffer = null;
            if(text == null || text.length() < 1){
            	return null;
            }
            return text;
        } catch (Exception e) {
        	e.printStackTrace();
        	System.out.println("文件“"+filePath+"”读取失败：" + e.getMessage());
            return null;
        }
    }
 
}
