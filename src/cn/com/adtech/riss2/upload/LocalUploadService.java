package cn.com.adtech.riss2.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.domain.ResFilesystem;

public class LocalUploadService implements IUploadService {

	@SuppressWarnings("unused")
	private ResFilesystem fileServiceBean;

	public void setFileServiceBean(ResFilesystem fileServcieBean) {
		this.fileServiceBean = fileServcieBean;
	}

	public void saveFile(String path, String fileName, byte[] mediaFile) {
		File filePath = new File(SysConfigInfo.FILE_ROOT_DIR + path);
		if(!filePath.exists()) {
			filePath.mkdirs();
		}
		File file = new File(filePath, fileName);
		System.out.println("-------------- 文件开始上传-------------------------------");
		FileOutputStream output;
		try {
			output = new FileOutputStream(file);
			output.write(mediaFile);
			output.flush();
			output.close();
		}catch(FileNotFoundException e) {
			System.out.println(file.getPath() + "  文件未找到");
			e.printStackTrace();
		}catch(IOException e) {
			System.out.println("  IO异常");
			e.printStackTrace();
		}
		System.out.println("-------------- 文件上传完毕-------------------------------");
	}

	public void saveFile(String path, String fileName, InputStream inputStream) {
		File filePath = new File(SysConfigInfo.FILE_ROOT_DIR + path);
		if(!filePath.exists()) {
			filePath.mkdirs();
		}
		File file = new File(filePath, fileName);
		System.out.println("-------------- 文件开始上传-------------------------------");
		FileOutputStream output;
		try {
			output = new FileOutputStream(file);
			byte[] buffer = new byte[1024*1024];
			int length = 0;
			while((length = inputStream.read(buffer)) > 0) {
				output.write(buffer, 0, length);
				output.flush();
			}
			output.close();
		}catch(FileNotFoundException e) {
			System.out.println(file.getPath() + "  文件未找到");
			e.printStackTrace();
		}catch(IOException e) {
			System.out.println("  IO异常");
			e.printStackTrace();
		}
		System.out.println("-------------- 文件上传完毕-------------------------------");
	}

	public void moveFile(String newPath, String oldPath, String newFileName, String oldFileName) throws Exception {
		File fromFilePath = new File(SysConfigInfo.FILE_ROOT_DIR + oldPath);
		File toFilePath = new File(SysConfigInfo.FILE_ROOT_DIR + newPath);
		if(!toFilePath.exists()) {
			toFilePath.mkdirs();
		}
		File fromPicFile = new File(fromFilePath, oldFileName);
		File toPicFile = new File(toFilePath, newFileName);
		if(fromPicFile.exists()) {
			FileInputStream in = new FileInputStream(fromPicFile);
			FileOutputStream output = new FileOutputStream(toPicFile);
			byte[] buffer = new byte[1024];
			int len = 0;
			while((len = in.read(buffer)) > 0) {
				output.write(buffer, 0, len);
			}
			output.flush();
			output.close();
			in.close();
			fromPicFile.delete();
		}
	}

	public void deleteFile(String path, String fileName) throws Exception {
		File file = new File(SysConfigInfo.FILE_ROOT_DIR + path, fileName);
		if(file.exists()) {
			if(file.delete())
				System.out.println("文件删除成功！");
		}else
			System.out.println("文件不存在！");
	}

	public boolean deleteFileOrDir(String path) {
		boolean result = false;
		File file = new File(SysConfigInfo.FILE_ROOT_DIR + path);
		if(file.isFile()) {
			result = file.delete();
		}else {
			result = deleteFolder(file);
		}
		return result;
	}

	/**
	 * 删除指定文件夹及其的全部内容
	 * 
	 * @param path
	 *            目录对象
	 * @return true--删除成功 ,false--删除失败
	 * @author MaChao
	 */
	private boolean deleteFolder(File path) {
		boolean result = true;
		if(path != null && path.isDirectory()) {
			File[] fileList = path.listFiles();
			for(int i = 0; i < fileList.length; i++) {
				if(fileList[i].isDirectory()) {
					deleteFolder(fileList[i]);
				}
				fileList[i].delete();
			}
			// 因为目录不为空时，不能删除，所以只需要返回外层删除结果。
			result = path.delete();
		}else {
			result = false;
		}
		return result;
	}

	public boolean createDir(String path) {
		boolean result = false;
		File newFolder = new File(SysConfigInfo.FILE_ROOT_DIR + path);
		if(!newFolder.exists()) {
			result = newFolder.mkdirs();
		}
		return result;
	}

	public String createDir(String parentPath, String childPath) {
		String retValue = "110";
		File newFolder = new File(SysConfigInfo.FILE_ROOT_DIR + parentPath + childPath);
		if(newFolder.exists()) {
			retValue = "101";
		}else {
			try {
				boolean dirCreated = newFolder.mkdirs();
				if(dirCreated)
					retValue = "0";
				else
					retValue = "102";
			}catch(SecurityException sex) {
				retValue = "103";
			}
		}
		return retValue;
	}

	public void getFolders(String path, Node root, Document doc) {
		File dir = new File(SysConfigInfo.FILE_ROOT_DIR + path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		Element folders = doc.createElement("Folders");
		root.appendChild(folders);
		File[] fileList = dir.listFiles();
		for(int i = 0; i < fileList.length; ++i) {
			if(fileList[i].isDirectory()) {
				Element myEl = doc.createElement("Folder");
				myEl.setAttribute("name", fileList[i].getName());
				folders.appendChild(myEl);
			}
		}
	}

	public String saveFile(String path, FileItem uplFile, String fileName) throws Exception {
		File dir = new File(SysConfigInfo.FILE_ROOT_DIR + path);
		if(!dir.exists()) {
			dir.mkdirs();
		}
		String nameWithoutExt = getNameWithoutExtension(fileName);
		String ext = getExtension(fileName);
		File pathToSave = new File(dir, fileName);
		int counter = 1;
		while(pathToSave.exists()) {
			fileName = nameWithoutExt + "(" + counter + ")" + "." + ext;
			pathToSave = new File(dir, fileName);
			counter++;
		}
		uplFile.write(pathToSave);
		return fileName;
	}

	private static String getNameWithoutExtension(String fileName) {
		return fileName.substring(0, fileName.lastIndexOf("."));
	}

	private String getExtension(String fileName) {
		return fileName.substring(fileName.lastIndexOf(".") + 1);
	}

	public String createFolder() {
		int y, m;
		Calendar cal = Calendar.getInstance();
		y = cal.get(Calendar.YEAR);
		m = (cal.get(Calendar.MONTH) + 1);
		cal = null;
		String folder = y + "-" + m;
		return folder;
	}

	/**
	 * 编码
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public String encode(String str) throws UnsupportedEncodingException {
		if(str == null)
			return "";
		Base64 base64 = new Base64();
		str = java.net.URLEncoder.encode(str, "utf-8");
		return new String(base64.encode(str.getBytes()));
	}

	/**
	 * 解码
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public String decode(String str) throws IOException {
		if(str == null)
			return "";
		Base64 base64 = new Base64();
		str = new String(base64.decode(str.getBytes()));
		return java.net.URLDecoder.decode(str, "utf-8");
	}

	@Override
	public void removeFile(String filename) {
		if(null == filename || filename.length() < 3) {
			return;
		}
		String lastChar = filename.substring(filename.length() - 1);
		if(lastChar.equals("/")) {
			lastChar = "";
		}else {
			lastChar = "/";
		}
		File file = new File(SysConfigInfo.FILE_ROOT_DIR + lastChar + filename);
		if(file.exists()) {
			file.delete();
		}
	}

	public void removeFileList(List<String> fileList) {
		if(fileList == null || fileList.size() < 1) {
			return;
		}
		for(int i = 0, s = fileList.size(); i < s; i++) {
			removeFile(fileList.get(i));
		}
	}

	public String[] getFilenameArr(String filepath) {
		File file = new File(SysConfigInfo.FILE_ROOT_DIR + filepath);
		if(file == null) {
			return null;
		}
		if(file.exists() == false) {
			System.out.println("文件夹:" + SysConfigInfo.FILE_ROOT_DIR + filepath + " 未找到");
			return null;
		}
		String[] filenames = file.list();
		return filenames;
	}
	
	public String getFolderByYearMonth() {
		int y, m, d;
		Calendar cal = Calendar.getInstance();
		y = cal.get(Calendar.YEAR);
		m = (cal.get(Calendar.MONTH) + 1);
		d = cal.get(Calendar.DAY_OF_MONTH);
		cal = null;
		String folder = "uploadfile/" + y + "/" + m + "/" + d + "/";
		
		return folder;
	}
}
