package cn.com.adtech.riss2.core.file;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;

/**
 * 判断图像方向是否被旋转过，如果旋转过则自动纠正回来
 * 注：由于大部分扫描图像没有Exif信息，因此无法判断是否被旋转过
 * 
 * @author MaChao
 * 
 * @date 2022.03.17
 *
 */
public class ImageRotate {

	public static void main(String[] args) {
	    String imgPath = "D:\\test\\rotate\\sun\\1.jpg";
	    long a = System.currentTimeMillis();
	    rotateImage(new File(imgPath));
	    long b = System.currentTimeMillis();
	    
	    System.out.println("处理完成，用时：" + (b-a));
	}

	public static void rotateImage(File imgFile) {
        try {
            Metadata metadata = ImageMetadataReader.readMetadata(imgFile);
            StringBuilder description = new StringBuilder();
            
            
            for(Directory directory : metadata.getDirectories()) {
            	for(Tag tag : directory.getTags()) {
            		if (tag.getTagType() == ExifIFD0Directory.TAG_ORIENTATION) {
                        description.append(tag.getDescription().replaceAll(" ", ""));
                    }
            	}
            }
            if (description.length() > 0) {
                int rotateIndex = description.indexOf("Rotate");
                int cwIndex = description.indexOf("CW");
                if (rotateIndex >= 0 && cwIndex > 0 && rotateIndex < cwIndex) {
                    int angel = Integer.valueOf(description.substring(rotateIndex + 6, cwIndex));
                    System.out.println("============图片方向纠正，顺时针旋转{"+angel+"}°,图片路径：{"+imgFile.getAbsolutePath()+"}===========");
                    BufferedImage oldImage = ImageIO.read(imgFile);
                    BufferedImage newImage = Rotate(oldImage, angel);
                    ImageIO.write(newImage, "jpg", imgFile);
                    newImage.getGraphics().dispose();
                    oldImage.getGraphics().dispose();
                }
            }
        } catch (ImageProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

	}
	
	
	/**
     * 对图片进行旋转
     *
     * @param src   被旋转图片
     * @param angel 旋转角度
     * @return 旋转后的图片
     */
    private static BufferedImage Rotate(Image src, int angel) {
        int srcWidth = src.getWidth(null);
        int srcHeight = src.getHeight(null);
        // 计算旋转后图片的尺寸
        Rectangle rect_des = CalcRotatedSize(new Rectangle(new Dimension(srcWidth, srcHeight)), angel);
        BufferedImage res = null;
        res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = res.createGraphics();
        // 进行转换
        g2.translate((rect_des.width - srcWidth) / 2, (rect_des.height - srcHeight) / 2);
        g2.rotate(Math.toRadians(angel), srcWidth / 2, srcHeight / 2);

        g2.drawImage(src, null, null);
        return res;
    }

    /**
     * 计算旋转后的图片
     *
     * @param src   被旋转的图片
     * @param angel 旋转角度
     * @return 旋转后的图片
     */
    private static Rectangle CalcRotatedSize(Rectangle src, int angel) {
        // 如果旋转的角度大于90度做相应的转换
        if (angel >= 90) {
            if (angel / 90 % 2 == 1) {
                int temp = src.height;
                src.height = src.width;
                src.width = temp;
            }
            angel = angel % 90;
        }

        double r = Math.sqrt(src.height * src.height + src.width * src.width) / 2;
        double len = 2 * Math.sin(Math.toRadians(angel) / 2) * r;
        double angelAlpha = (Math.PI - Math.toRadians(angel)) / 2;
        double angelDeltaWidth = Math.atan((double) src.height / src.width);
        double angelDeltaHeight = Math.atan((double) src.width / src.height);

        int lenDeltaWidth = (int) (len * Math.cos(Math.PI - angelAlpha
            - angelDeltaWidth));
        int lenDeltaHeight = (int) (len * Math.cos(Math.PI - angelAlpha
            - angelDeltaHeight));
        int desWidth = src.width + lenDeltaWidth * 2;
        int desHeight = src.height + lenDeltaHeight * 2;
        return new Rectangle(new Dimension(desWidth, desHeight));
    }
}
