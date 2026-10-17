package cn.com.adtech.riss2.core.file;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ImgHandleUtil;

/**
 * 获取文件大小
 * 
 * @description 
 *
 * @author MaChao
 *
 * @date Oct 31, 2016 - 3:55:39 PM
 */
public class MyFileUtil {
	
	public static String getFileSize(String Path) {
		return getFileSize(new File(Path));
	}

	/**
	 * 获取文件大小并返回
	 *
	 * @author MaChao
	 * @date Oct 31, 2016
	 * @time 4:16:14 PM
	 */
	public static String getFileSize(File file) {
		String size = "";
		if (file.exists() && file.isFile()) {
			long fileS = file.length();
			return formatFileSize(fileS);
		} else if (file.exists() && file.isDirectory()) {
			size = "";
		} else {
			size = "0BT";
		}
		return size;
	}
	
	public static String formatFileSize(long fileS){
		String size = "";
		DecimalFormat df = new DecimalFormat("#.00");
		long kb = 1024;
		long mb = kb*1024;
		long gb = mb*1024;
		if (fileS < kb) {
			size = df.format((double) fileS) + "BT";
		} else if (fileS < mb) {
			size = df.format((double) fileS / kb) + "KB";
		} else if (fileS < gb) {
			size = df.format((double) fileS / mb) + "MB";
		} else {
			size = df.format((double) fileS / gb) + "GB";
		}
		return size;
	}
	
	/**
	 * 根据文件目录返回与目录文件夹名称相同的txt文本文件
	 * 
	 * @param dirpath 目录
	 * @return 如 dirpath=“D:/resource/book/201808002”，则返回如：“D:/resource/book/201808002/201808002.txt” 
	 * 	       未找到则返回null
	 */
	public static File getSrctxtFileByDirname(String dirpath){
		if(!CommonUtil.isNotNull(dirpath)){
			return null;
		}
		File dirFile = new File(dirpath);
		if(dirFile.isDirectory() == false){
			return null;
		}
		
		String filename = dirFile.getName();
		//几种主流文本格式依次查找
		File file = new File(dirpath, filename + ".txt");
		if(file.exists()){
			return file;
		}
		return null;
	}
	
	/**
	 * 获取目录中所有txt文件，但不包含与文件夹名称相同的txt文件
	 * @param dirpath
	 * @return
	 */
	public static List<File> getDirTxtFile(String dirpath){
		if(!CommonUtil.isNotNull(dirpath)){
			return null;
		}
		File dirFile = new File(dirpath);
		if(dirFile.isDirectory() == false){
			return null;
		}
		
		File[] fileArr = dirFile.listFiles();
		if(fileArr == null || fileArr.length < 1){
			return null;
		}
		
		List<File> fileList = new ArrayList<File>();
		String dirname = dirFile.getName();
		for(File f : fileArr){
			if(f.isDirectory()){
				continue;
			}
			String s = f.getAbsolutePath();
			if(s.indexOf(".") < 0){
				continue;
			}
			
			String filename = f.getName();
			filename = filename.substring(0, filename.lastIndexOf("."));
			String fmt = s.substring(s.lastIndexOf(".")+1).toLowerCase();
			if(fmt.equals("txt") && !filename.equals(dirname)){
				fileList.add(f);
			}
		}
		return fileList;
	}
	
	/**
	 * 新开一个线程对目录中的图片进行处理，包括压缩缩略图，分片切图
	 * @param dirPath
	 */
	public synchronized static void imgSplit(final String dirPath){
		new Thread(){
			@Override
			public void run() {
				super.run();
				createImgCover(dirPath);
			}
		}.start();
	}
	
	
	public static void createImgCover(String dirPath){
		System.out.println(dirPath + " 图片处理中..");
		if(!CommonUtil.isNotNull(dirPath)){
			return;
		}
		File imgDir = new File(dirPath);
		 if(!imgDir.exists() || !imgDir.isDirectory()){
			 return;
		 }
		 File[] fileArr = imgDir.listFiles();
		 if(fileArr == null || fileArr.length < 1){
			 return;
		 }
		 
		 String filename = null;  //原图名称
		 String fmt = null; 
		 File coverFile = null;
		 File xmlFile = null;
		 boolean isCover = false;
		 for(File f : fileArr){
			 if(f.isDirectory()){
				 continue;
			 }
			 filename = f.getName();
			 String fname = filename.substring(0, filename.lastIndexOf("."));
			 if(fname.endsWith("_small")){ //跳过缩略图
				 continue;
			 }
			 if(fname.contains("cover")){ //跳过封面
				 isCover = true;
				 continue;
			 }
			 //文件格式
			 fmt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			 if(fmt.equals("xml")){ //跳过切片的xml配置文件
				 continue;
			 }
			 if(fmt.equals("txt")){ //跳过txt源文
				 continue;
			 }
			 
			 if(fmt.equals("jpg") || fmt.equals("jpeg") || fmt.equals("png") || fmt.equals("gif") || fmt.equals("bmp")){
				 
				 //判断封面是否生成，如果未生成则创建（一般第一次访问），已生成则无须再创建
				 coverFile = new File(f.getParent(), fname + "_small." + fmt);
				 if(!coverFile.exists()){
					 ImgHandleUtil.pictureHandle(f.getAbsolutePath(), 72, 50, true);
				 }
				 
				 //根据xml是否存在判断是否已对图片进行切片
				 xmlFile = new File(f.getParent(), fname + ".xml");
				 if(!xmlFile.exists()){
					 try {
						DeepZoomUtil.processImageFile(f, new File(f.getParent()));
					} catch (Exception e) {
						System.out.println("图片“"+f.getAbsolutePath()+"”切片失败");
						e.printStackTrace();
					}
				 }
			 }
		 }
		 
		 //如果目录中没有封面文件，则根据第一个图片自动切取封面
		 if(isCover){
			 return;
		 }
		 for(File f : fileArr){
			 if(f.isDirectory()){
				 continue;
			 }
			 filename = f.getName();
			 String fname = filename.substring(0, filename.lastIndexOf("."));
			 if(fname.endsWith("_small")){ //跳过缩略图
				 continue;
			 }
		
			 //文件格式
			 fmt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			 if(fmt.equals("jpg") || fmt.equals("jpeg") || fmt.equals("png") || fmt.equals("gif") || fmt.equals("bmp")){
				 ImgHandleUtil.pictureHandle(f.getAbsolutePath(), "cover.jpg", 180, 250, true);
				 break;
			 }
		 } 
		 System.out.println(dirPath + " 图片处理完成--------------");
	}
	
	public static File getXlsByDirname(String dirpath){
		if(!CommonUtil.isNotNull(dirpath)){
			return null;
		}
		File dirFile = new File(dirpath);
		if(dirFile.isDirectory() == false){
			return null;
		}
		
		String filename = dirFile.getName();
		File file = new File(dirpath, filename + ".xls");
		if(file.exists()){
			return file;
		}
		file = new File(dirpath, filename + ".xlsx");
		if(file.exists()){
			return file;
		}
		return null;
	}
	
	/**
	 * 根据目录获取所有页面名称并排序
	 * @param dirPath
	 * @return
	 */
	public static List<String> getPageNameList(String dirPath){
		if(!CommonUtil.isNotNull(dirPath)){
			return null;
		}
		File imgDir = new File(dirPath);
		 if(!imgDir.exists() || !imgDir.isDirectory()){
			 return null;
		 }
		 File[] fileArr = imgDir.listFiles();
		 if(fileArr == null || fileArr.length < 1){
			 return null;
		 }
		 
		 String filename = null;  //原图名称
		 String fmt = null; 
		 List<String> pageList = new LinkedList<String>();
		 for(File f : fileArr){
			 if(f.isDirectory()){
				 continue;
			 }
			 filename = f.getName();
			 String fname = filename.substring(0, filename.lastIndexOf("."));
			 if(fname.endsWith("_small")){ //跳过缩略图
				 continue;
			 }
			 if(fname.contains("cover")){ //跳过封面
				 continue;
			 }
			 //文件格式
			 fmt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
			 if(fmt.equals("xml")){ //跳过切片的xml配置文件
				 continue;
			 }
			 if(fmt.equals("txt")){ //跳过txt源文
				 continue;
			 }
			 
			 if(fmt.equals("jpg") || fmt.equals("jpeg") || fmt.equals("png") || fmt.equals("gif") || fmt.equals("bmp")){
				 pageList.add(fname);
			 }
		 }
		 
		//按文件名排序
		Collections.sort(pageList, new Comparator<String>() {
			public int compare(String f1, String f2) {
                return f1.compareTo(f2);
            }
		});
		 
		 return pageList;
	}
	
	public static void main(String[] args){
//		List<File> list = getDirTxtFile("D:/ResourceFile/baseDir/imgtxt/book01/dazangjin");
//		for(File f : list){
//			String fname = f.getName();
//			fname = fname.substring(0, fname.lastIndexOf("."));
//			System.out.println(fname);
//			System.out.println(f.getAbsolutePath());
//		}
		
		List<String> pageList = getPageNameList("E:/测试资料/temp/干部管理");
		
		System.out.println(pageList.indexOf("B00003_00"));
		
		for(int i=0,s=pageList.size(); i<s; i++){
			System.out.println((i+1) + "：" + pageList.get(i));
		}
	}
}
