package cn.com.adtech.riss2.upload;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.com.adtech.riss2.domain.ResFilesystem;

/**
 * 文件操作公共接口
 * 
 * @author MaChao
 */
public interface IUploadService {
	/**
	 * 保存文件
	 * 
	 * @param path
	 *            文件的路径
	 * @param fileName
	 *            文件名
	 * @param mediaFile
	 *            文件二进制数组
	 */
	public void saveFile(String path, String fileName, byte[] mediaFile) throws Exception;

	/**
	 * 移动文件
	 * 
	 * @param newPath
	 * @param oldPath
	 * @param newFileName
	 * @param oldFileName
	 */
	public void moveFile(String newPath, String oldPath, String newFileName, String oldFileName) throws Exception;

	/**
	 * 删除文件
	 * 
	 * @param path
	 *            文件的路径
	 * @param fileName
	 *            文件名
	 * @param stream
	 *            文件流
	 */
	public void deleteFile(String path, String fileName) throws Exception;

	/**
	 * 删除文件或文件夹
	 * 
	 * @param path
	 * @return 返回删除情况 true--成功，false--失败
	 * @author MaChao
	 */
	boolean deleteFileOrDir(String path);

	/**
	 * 创建目录
	 * 
	 * @param path
	 * @return
	 * @author MaChao
	 */
	boolean createDir(String path);

	/**
	 * 在父目录下创建子目录
	 * 
	 * @param parentPath
	 * @param childPath
	 * @return
	 * @author MaChao
	 */
	String createDir(String parentPath, String childPath);

	/**
	 * 获得制定目录下的子目录，并存放到xml文件中。
	 * 
	 * @param path
	 * @param root
	 * @param doc
	 * @author MaChao
	 */
	void getFolders(String path, Node root, Document doc);

	/**
	 * 保存文件，当有重名文件时，另存为新的名字。
	 * 
	 * @param path
	 * @param uplFile
	 * @param fileName
	 * @return 返回保存的文件名。
	 * @throws Exception
	 * @author MaChao
	 */
	String saveFile(String path, FileItem uplFile, String fileName) throws Exception;

	/**
	 * 创建文件存放的文件夹，以当天为一个文件夹
	 * 
	 * @return
	 */
	public String createFolder();

	/**
	 * 编码
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	String encode(String str) throws Exception;

	/**
	 * 解码
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	String decode(String str) throws Exception;

	void removeFile(String filename);

	/**
	 * 设置文件系统对象，远程上传必设
	 * 
	 * @param fileServcieBean
	 */
	public void setFileServiceBean(ResFilesystem fileServcieBean);
	
	/**
	 * 一次删除多个文件
	 * @param fileList
	 */
	public void removeFileList(List<String> fileList);
	
	public String[] getFilenameArr(String filepath);
	
	public void saveFile(String path, String fileName, InputStream inputStream)throws Exception;
}
