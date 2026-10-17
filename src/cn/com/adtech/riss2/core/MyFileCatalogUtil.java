package cn.com.adtech.riss2.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import cn.com.adtech.riss2.bean.CatalogBean;
import cn.com.unionlib.lucene.bean.ResPageBean;

/**
 * 资源目录封装工具
 *
 * @author MaChao
 *
 * @date 2018-8-21 下午4:18:10 
 *
 */
public class MyFileCatalogUtil {

	public static List<ResPageBean> RES_PAGE_BEAN_lIST;
	
	//获取切片插件的目录
	public static List<CatalogBean> getXlsCatalog(File xlsfile) throws BiffException, IOException{
		// 获取xls文件信息
		int beginRow = 1;
		InputStream is = new FileInputStream(xlsfile);
		jxl.Workbook rwb = Workbook.getWorkbook(is);
		Sheet rs = (Sheet) rwb.getSheet(0);
		int rows = rs.getRows();
		
		// 将目录内容封装为子页集合对象
		List<CatalogBean> list = new ArrayList<CatalogBean>();
		CatalogBean catalog = null;
		String catalogname = null; //章节名称
		String imgname = null; //对应页面图片名称
		for (int i = beginRow; i < rows; i++) {
			// 目录名称
			catalogname = rs.getCell(0, i).getContents().trim();
			if(!CommonUtil.isNotNull(catalogname)){
				continue;
			}
			
			catalog = new CatalogBean();
			catalog.setTitle(catalogname);
			
			// 图片文件名
			imgname = rs.getCell(1, i).getContents().trim();
			catalog.setPageName(imgname);
			
			//转码后的文件名
			catalog.setPageNameCode(URLEncoder.encode(imgname, "utf-8"));
			list.add(catalog);
		}
		return list;
	}
	
	public static void fileSort(File[] fileArr){
		Arrays.sort(fileArr, new StringComparator());
	}
}


//此类实现Comparable接口  
class StringComparator implements Comparator<File>{  
     @Override  
     public int compare(File f1, File f2) {  
         if(getNum(f1)<getNum(f2)){
             return -1;  
         }else if(getNum(f1)>getNum(f2)){  
             return 1;   
         }else{
        	 return 0;  
         }
     }  
       
     public int getNum(File file){  
    	 String fileName = file.getName();
    	 StringBuffer sb = new StringBuffer();  
         for(int i=0;i<fileName.length();i++){  
             if(Character.isDigit(fileName.charAt(i))){ 
                 sb.append(fileName.charAt(i));  
             }else{ 
            	 break;  
             }
         }  
	     if(sb.toString().isEmpty()){
	         return 0;  
	     }else{
	         return Integer.parseInt(sb.toString());  
	     }  
     }  
}  
