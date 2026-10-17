package cn.com.adtech.riss2.core.file;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import javax.imageio.ImageIO;

/**
 * Deep Zoom Converter
 * 
 * @author MaChao
 *
 */
public class DeepZoomUtil {
    static final String xmlHeader = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
    static final String schemaName = "http://schemas.microsoft.com/deepzoom/2009";

    static Boolean deleteExisting = true;
    static String tileFormat = "jpg";

    // settings
    static int tileSize = 256;
    static int tileOverlap = 1;
    static Boolean verboseMode = false;
    static Boolean debugMode = false;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            processImageFile(new File("C:/Users/Administrator/Desktop/0J7A4119.JPG"), new File("C:/Users/Administrator/Desktop/"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the given image file, producing its Deep Zoom output files
     * in a subdirectory of the given output directory.
     * @param inFile the file containing the image
     * @param outputDir the output directory
     */
    public static synchronized String processImageFile(File inFile, File outputDir) throws IOException {
        if (verboseMode)
             System.out.printf("Processing image file: %s\n", inFile);

        //判断图片方向有无旋转
        ImageRotate.rotateImage(inFile);
        
        String fileName = inFile.getName();
        String nameWithoutExtension = fileName.substring(0, fileName.lastIndexOf('.'));
        String pathWithoutExtension = outputDir + File.separator + nameWithoutExtension;

        BufferedImage image = loadImage(inFile);

        int originalWidth = image.getWidth();
        int originalHeight = image.getHeight();

        double maxDim = Math.max(originalWidth, originalHeight);

        int nLevels = (int)Math.ceil(Math.log(maxDim) / Math.log(2));

        if (debugMode){
        	System.out.printf("nLevels=%d\n", nLevels);
        }

        // Delete any existing output files and folders for this image

        File descriptor = new File(pathWithoutExtension + ".xml");
        if (descriptor.exists()) {
            if (deleteExisting){
            	deleteFile(descriptor);
            }else{
            	throw new IOException("File already exists in output dir: " + descriptor);
            }
        }

        File imgDir = new File(pathWithoutExtension);
        if (imgDir.exists()) {
            if (deleteExisting) {
                if (debugMode)
                    System.out.printf("Deleting directory: %s\n", imgDir);
                deleteDir(imgDir);
            } else
                throw new IOException("Image directory already exists in output dir: " + imgDir);
        }

        imgDir = createDir(outputDir, nameWithoutExtension.concat("_files"));

        double width = originalWidth;
        double height = originalHeight;

        for (int level = nLevels; level >= 0; level--) {
            int nCols = (int)Math.ceil(width / tileSize);
            int nRows = (int)Math.ceil(height / tileSize);
            if (debugMode)
                System.out.printf("level=%d w/h=%f/%f cols/rows=%d/%d\n",
                                   level, width, height, nCols, nRows);
            
            File dir = createDir(imgDir, Integer.toString(level));
            for (int col = 0; col < nCols; col++) {
                for (int row = 0; row < nRows; row++) {
                    BufferedImage tile = getTile(image, row, col);
                    saveImage(tile, dir + File.separator + col + '_' + row);
                }
            }

            // Scale down image for next level
            width = Math.ceil(width / 2);
            height = Math.ceil(height / 2);
            if (width > 10 && height > 10) {
                // resize in stages to improve quality
                image = resizeImage(image, width * 1.66, height * 1.66);
                image = resizeImage(image, width * 1.33, height * 1.33);
            }
            image = resizeImage(image, width, height);
        }

        saveImageDescriptor(originalWidth, originalHeight, descriptor);
        saveImageDescriptor(originalWidth, originalHeight, new File(outputDir.getPath()+"/"+outputDir.getName() + ".json"),imgDir.getPath());
        return File.separator + nameWithoutExtension;
    }


    /**
     * Delete a file
     * @param path the path of the directory to be deleted
     */
    private static void deleteFile(File file) throws IOException {
         if (!file.delete())
             throw new IOException("Failed to delete file: " + file);
    }

    /**
     * Recursively deletes a directory
     * @param path the path of the directory to be deleted
     */
    private static void deleteDir(File dir) throws IOException {
        if (!dir.isDirectory())
            deleteFile(dir);
        else {
            for (File file : dir.listFiles()) {
               if (file.isDirectory())
                   deleteDir(file);
               else
                   deleteFile(file);
            }
            if (!dir.delete())
                throw new IOException("Failed to delete directory: " + dir);
        }
    }

    /**
     * Creates a directory
     * @param parent the parent directory for the new directory
     * @param name the new directory name
     */
    private static File createDir(File parent, String name) throws IOException {
        assert(parent.isDirectory());
        File result = new File(parent + File.separator + name);
        if (!result.mkdir())
           throw new IOException("Unable to create directory: " + result);
        return result;
    }

    /**
     * Loads image from file
     * @param file the file containing the image
     */
    private static BufferedImage loadImage(File file) throws IOException {
        BufferedImage result = null;
        try {
        	//设置图片的缓存区
        	ImageIO.setCacheDirectory(new File(file.getParent()));
        	//读取图片
            result = ImageIO.read(file);
        } catch (Exception e) {
        	e.printStackTrace();
            throw new IOException("Cannot read image file: " + file);
        }
        return result;
    }

    /**
     * Gets an image containing the tile at the given row and column
     * for the given image.
     * @param img - the input image from whihc the tile is taken
     * @param row - the tile's row (i.e. y) index
     * @param col - the tile's column (i.e. x) index
     */
    private static BufferedImage getTile(BufferedImage img, int row, int col) {
        int x = col * tileSize - (col == 0 ? 0 : tileOverlap);
        int y = row * tileSize - (row == 0 ? 0 : tileOverlap);
        int w = tileSize + (col == 0 ? 1 : 2) * tileOverlap;
        int h = tileSize + (row == 0 ? 1 : 2) * tileOverlap;

        if (x + w > img.getWidth()){
        	w = img.getWidth() - x;
        }
        if (y + h > img.getHeight()){
        	h = img.getHeight() - y;
        }

        if (debugMode){
            System.out.printf("getTile: row=%d, col=%d, x=%d, y=%d, w=%d, h=%d\n",
                              row, col, x, y, w, h);
        }
        
        assert(w > 0);
        assert(h > 0);

        BufferedImage result = new BufferedImage(w, h, img.getType());
        Graphics2D g = result.createGraphics();
        g.drawImage(img, 0, 0, w, h, x, y, x+w, y+h, null);

        return result;
    }

    /**
     * Returns resized image
     * NB - useful reference on high quality image resizing can be found here:
     *   http://today.java.net/pub/a/today/2007/04/03/perils-of-image-getscaledinstance.html
     * @param width the required width
     * @param height the frequired height
     * @param img the image to be resized
     */
    private static BufferedImage resizeImage(BufferedImage img, double width, double height) {
        int w = (int)width;
        int h = (int)height;
        BufferedImage result = new BufferedImage(w, h, img.getType());
        Graphics2D g = result.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                           RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g.drawImage(img, 0, 0, w, h, 0, 0, img.getWidth(), img.getHeight(), null);
        return result;
    }

    /**
     * Saves image to the given file
     * @param img the image to be saved
     * @param path the path of the file to which it is saved (less the extension)
     */
    private static void saveImage(BufferedImage img, String path) throws IOException {
        File outputFile = new File(path + "." + tileFormat);
        try {
            ImageIO.write(img, tileFormat, outputFile);
        } catch (IOException e) {
            throw new IOException("Unable to save image file: " + outputFile);
        }
    }

    /**
     * Write image descriptor XML file
     * @param width image width
     * @param height image height
     * @param file the file to which it is saved
     */
    private static void saveImageDescriptor(int width, int height, File file) throws IOException {
        StringBuilder sb = new StringBuilder(256);
        sb.append(xmlHeader);
        sb.append("<Image TileSize=\"");
        sb.append(tileSize);
        sb.append("\" Overlap=\"");
        sb.append(tileOverlap);
        sb.append("\" Format=\"");
        sb.append(tileFormat);
        sb.append("\" ServerFormat=\"Default\" xmlns=\"");
        sb.append(schemaName);
        sb.append("\">");
        sb.append("<Size Width=\"");
        sb.append(width);
        sb.append("\" Height=\"");
        sb.append(height);
        sb.append("\" />");
        sb.append("</Image>");
        saveText(sb.toString().getBytes("UTF-8"), file);
    }

    /**
     * Saves strings as text to the given file
     * @param bytes the image to be saved
     * @param file the file to which it is saved
     */
    private static synchronized void saveText(byte[] bytes, File file) throws IOException {
        try {
            //输出�?
            FileOutputStream fos = new FileOutputStream(file,true);
            //从输出流中创建写通道
            FileChannel writeChannel = fos.getChannel();
            //将既有数组作为buffer内存空间
            ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
            //将buffer写到磁盘
            writeChannel.write(byteBuffer);
            
            writeChannel.close();
        } catch (IOException e) {
            throw new IOException("Unable to write to text file: " + file);
        }
    }
    
    /**
     * Write image descriptor XML file
     * @param width image width
     * @param height image height
     * @param file the file to which it is saved
     * @param path 制作的存放要求格式的图片文件夹的路径
     */
    private static void saveImageDescriptor(int width, int height, File file,String path) throws IOException {
        StringBuilder sb = new StringBuilder(256);
        if(file.exists() && file.length() > 0) {  
        	sb.append(";");
        }
        sb.append("{");
        sb.append("Image:{");
        sb.append("xmlns:").append("\"").append(schemaName).append("\",");
        sb.append("Url:").append("\"").append(pathReplaceAll(path)).append("/\",");
        sb.append("Overlap:").append("\"").append(tileOverlap).append("\",");
        sb.append("TileSize:").append("\"").append(tileSize).append("\",");
        sb.append("Format:").append("\"").append(tileFormat).append("\",");
        sb.append("Size :{");
        sb.append("Height:").append("\"").append(height).append("\",");
        sb.append("Width:").append("\"").append(width).append("\"");
        sb.append("}");
        sb.append("}");
        sb.append("}");
        //换行，具有良好的跨平台性
        //sb.append(System.getProperty("line.separator"));
        saveText(sb.toString().getBytes("UTF-8"), file);
    }
    
    /**
	 * 将路径里面的\\替换成/
	 * @param path
	 * @return
	 */
	public static String pathReplaceAll(String path){
		//System.out.println(path);
		//System.out.println(path.replaceAll("\\\\", "/"));
		return path.replaceAll("\\\\", "/");
	}
}
