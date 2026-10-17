package cn.com.adtech.riss2.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import cn.com.adtech.riss2.cache.SysConfigInfo;

import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.Thumbnails.Builder;
import net.coobird.thumbnailator.geometry.Positions;

/**
 * 
 * 根据图片源文压缩生成一个文件名_small.jpg的封面小图
 *
 * @author MaChao
 *
 * @date：Mar 28, 2017
 */
public class ImgHandleUtil {
	
	
	/**
	 * 压缩至指定图片尺寸（例如：横130高120），保持图片不变形，多余部分裁剪掉(这个是引的网友的代码) 
	 * 更多操作参考：http://blog.csdn.net/chenleixing/article/details/44685817
	 */
	public static String pictureHandle(String filepath, int w, int h, boolean srcKeep){
		return pictureHandle(filepath, null, w, h, srcKeep); 
	}
	
	public static String pictureHandle(String filepath, String outName, int w, int h, boolean srcKeep){
		File srcfile = new File(filepath);
		if(!srcfile.exists()){
			srcfile = new File(SysConfigInfo.FILE_ROOT_DIR + filepath);
			if(!srcfile.exists()){
				System.out.println("文件“"+filepath+"”不存在！");
				return null;
			}
		}
		
		
		String srcName = srcfile.getName();
		String extName = srcName.substring(srcName.lastIndexOf(".") + 1);
		
		String destName = srcName;
		if(srcKeep){
			destName = srcName.substring(0, srcName.lastIndexOf("."));
			if(CommonUtil.isNotNull(outName)){
				destName = outName;
			}else{
				destName = destName + "_small." + extName;
			}
		}
		
		File tofile = new File(srcfile.getParent(), destName);
		try {
			BufferedImage image = ImageIO.read(srcfile);
			Builder<BufferedImage> builder = null;  
			
			int imageWidth = image.getWidth();  
			int imageHeitht = image.getHeight();  
			if(imageWidth < w && imageHeitht < h){
				return srcfile.getPath();
				
			}
			
			if ((float)w / h != (float)imageWidth / imageHeitht) {  
				if (imageWidth > imageHeitht) {  
					image = Thumbnails.of(srcfile).height(h).asBufferedImage();  
				} else {  
					image = Thumbnails.of(srcfile).width(w).asBufferedImage();  
				}  
				builder = Thumbnails.of(image).sourceRegion(Positions.CENTER, w, h).size(w, h);  
			} else {  
				builder = Thumbnails.of(image).size(w, h);  
			}  
			builder.outputFormat(extName).toFile(tofile); 
			return tofile.getPath();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}  
	}
	
	
}
