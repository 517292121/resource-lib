package cn.com.adtech.riss2.upload;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import cn.com.adtech.riss2.core.Riss2Ftp;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.exception.ErrorMessageException;

public class RemoteUploadService implements IUploadService {
	private Riss2Ftp riss2Ftp;

	private ResFilesystem fileServiceBean;

	public void setRiss2Ftp(Riss2Ftp riss2Ftp) {
		this.riss2Ftp = riss2Ftp;
	}

	public void setFileServiceBean(ResFilesystem fileServcieBean) {
		this.fileServiceBean = fileServcieBean;
	}

	public void saveFile(String path, String fileName, byte[] mediaFile) throws Exception {
		// ftp客户端		FTPClient ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
		try {
			// 上传
			ByteArrayInputStream stream = new ByteArrayInputStream(mediaFile);
			riss2Ftp.upload(ftp, stream, path, fileName);
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
	}

	public void moveFile(String newPath, String oldPath, String newFileName, String oldFileName) throws Exception {
		// ftp客户端		FTPClient ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
		// 得到远程文献流		try {
			InputStream stream = riss2Ftp.getRemoteStream(ftp, oldPath, oldFileName);
			// 移动文献
			riss2Ftp.upload(ftp, stream, newPath, newFileName);
			// 删除文献
			riss2Ftp.delete(ftp, oldPath, oldFileName);
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
	}

	public void deleteFile(String path, String fileName) throws Exception {
		// ftp客户端
		FTPClient ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
		try {
			// 删除
			riss2Ftp.delete(ftp, path, fileName);
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
	}

	public boolean createDir(String path) {
		boolean result = true;
		// ftp客户端
		FTPClient ftp = null;
		// 得到远程文献流		try {
			ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
			riss2Ftp.makeDirectory(ftp, path);
			ftp.changeToParentDirectory();
		}catch(Exception e) {
			e.printStackTrace();
			result = false;
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
		return result;
	}

	public String createDir(String parentPath, String childPath) {
		// ftp客户端		FTPClient ftp = null;
		String retValue = "110";
		// 得到远程文献流		try {
			ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
			boolean isRepeat = false;
			FTPFile[] ftpFileList = ftp.listFiles(parentPath);
			if(ftpFileList != null) {
				for(int i = 0; i < ftpFileList.length; ++i) {
					if(!(".".equals(ftpFileList[i].getName()) || "..".equals(ftpFileList[i].getName()))) {// 有些ftp要返回.和..
						if(ftpFileList[i].isDirectory()) {
							if(ftpFileList[i].getName().equals(childPath)) {
								isRepeat = true;
								break;
							}
						}
					}
				}
			}
			if(isRepeat) {
				retValue = "101";
			}else {
				try {
					boolean dirCreated = ftp.makeDirectory(parentPath + childPath);
					if(dirCreated)
						retValue = "0";
					else
						retValue = "102";
				}catch(SecurityException sex) {
					retValue = "103";
				}
			}
		}catch(Exception e) {
			throw new ErrorMessageException("ftp连接失败！", e);
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
		return retValue;
	}

	public boolean deleteFileOrDir(String path) {
		boolean result = false;
		// ftp客户端		FTPClient ftp = null;
		// 得到远程文献流		try {
			ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
			if(path.indexOf(".") != -1) {// 当包含.时，认为是删除文件				result = ftp.deleteFile(path);
			}else {
				result = deleteFolder(ftp, path);
			}
		}catch(Exception e) {
			throw new ErrorMessageException("ftp连接失败！", e);
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
		return result;
	}

	public void getFiles(String path, Node root, Document doc, String downLoadPath) throws DOMException, UnsupportedEncodingException {
		// ftp客户端		FTPClient ftp = null;
		// 得到远程文献流		try {
			ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
			Element files = doc.createElement("Files");
			root.appendChild(files);
			FTPFile[] ftpFileList = ftp.listFiles(path);
			if(ftpFileList != null) {
				for(int i = 0; i < ftpFileList.length; ++i) {
					if(!(".".equals(ftpFileList[i].getName()) || "..".equals(ftpFileList[i].getName()))) {// 有些ftp要返回.和..
						if(ftpFileList[i].isFile()) {
							Element myEl = doc.createElement("File");
							myEl.setAttribute("name", ftpFileList[i].getName());
							myEl.setAttribute("size", "" + ftpFileList[i].getSize() / 1024);
							myEl.setAttribute("url", downLoadPath + Riss2Ftp.encode(path + ftpFileList[i].getName()) + "&fileName="
									+ Riss2Ftp.encode(ftpFileList[i].getName()));
							files.appendChild(myEl);
						}
					}
				}
			}
		}catch(Exception e) {
			throw new ErrorMessageException("ftp连接失败！", e);
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
	}

	public void getFolders(String path, Node root, Document doc) {
		// ftp客户端		FTPClient ftp = null;
		// 得到远程文献流		try {
			ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
			if(!ftp.changeWorkingDirectory(path)) {
				riss2Ftp.makeDirectory(ftp, path);
			}
			Element folders = doc.createElement("Folders");
			root.appendChild(folders);
			FTPFile[] ftpFileList = ftp.listFiles(path);
			if(ftpFileList != null) {
				for(int i = 0; i < ftpFileList.length; ++i) {
					if(!(".".equals(ftpFileList[i].getName()) || "..".equals(ftpFileList[i].getName()))) {// 有些ftp要返回.和..
						if(ftpFileList[i].isDirectory()) {
							Element myEl = doc.createElement("Folder");
							myEl.setAttribute("name", ftpFileList[i].getName());
							folders.appendChild(myEl);
						}
					}
				}
			}
		}catch(Exception e) {
			throw new ErrorMessageException("ftp连接失败！", e);
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
	}

	public String saveFile(String path, FileItem uplFile, String fileName) throws Exception {
		// ftp客户端		FTPClient ftp = null;
		// 得到远程文献流		try {
			ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
			if(!ftp.changeWorkingDirectory(path)) {
				ftp.makeDirectory(path);
			}
			String nameWithoutExt = getNameWithoutExtension(fileName);
			String ext = getExtension(fileName);
			String[] names = ftp.listNames(path);
			if(names != null) {
				int counter = 1;
				for(int i = 0; i < names.length; i++) {
					if(names[i].endsWith("/" + fileName) || names[i].equals(fileName)) {// 有的返回的是全路径名，有的ftp只返回文件名
						fileName = nameWithoutExt + "(" + counter + ")" + "." + ext;
						counter++;
						i = 0;
					}
				}
			}
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			ftp.storeFile(path + "/" + fileName, uplFile.getInputStream());
		}catch(Exception e) {
			throw new ErrorMessageException("ftp连接失败！", e);
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
		return fileName;
	}

	/**
	 * 删除指定文件夹及其的全部内容
	 * 
	 * @param ftp
	 * @param path
	 *            目录路径
	 * @return true--删除成功 ,false--删除失败
	 * @throws IOException
	 */
	private static boolean deleteFolder(FTPClient ftp, String path) throws IOException {
		boolean result = true;
		if(path != null) {
			FTPFile[] ftpFile = ftp.listFiles(path);
			if(ftpFile != null) {
				for(int i = 0; i < ftpFile.length; i++) {
					if(!(".".equals(ftpFile[i].getName()) || "..".equals(ftpFile[i].getName()))) {// 有些ftp要返回.和..
						if(ftpFile[i].isDirectory()) {
							deleteFolder(ftp, path + ftpFile[i].getName() + "/");
							ftp.removeDirectory(path + ftpFile[i].getName());
						}else {
							ftp.deleteFile(path + ftpFile[i].getName());
						}
					}
				}
			}
			// 因为目录不为空时，不能删除，所以只需要返回外层删除结果。			result = ftp.removeDirectory(path);
		}else {
			result = false;
		}
		return result;
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

	public String decode(String str) throws Exception {
		if(str == null)
			return "";
		Base64 base64 = new Base64();
		str = new String(base64.decode(str.getBytes()));
		return java.net.URLDecoder.decode(str, "utf-8");
	}

	public String encode(String str) throws Exception {
		if(str == null)
			return "";
		Base64 base64 = new Base64();
		str = java.net.URLEncoder.encode(str, "utf-8");
		return new String(base64.encode(str.getBytes()));
	}

	public void removeFile(String filename) {
		if(filename == null || filename.length() < 2){
			return;
		}
		// ftp客户端
		FTPClient ftp = null;
		try {
			ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
		} catch (Exception e) {
			System.out.println("文件:" + filename + " 删除失败!FTP创建失败");
			e.printStackTrace();
			return;
		}
		String filePath = filename.substring(0,filename.lastIndexOf("/") + 1);
		filename = filename.substring(filename.lastIndexOf("/") + 1);
		try {
			// 删除
			riss2Ftp.delete(ftp, filePath, filename);
			riss2Ftp.disconnectFtpServer(ftp);
		} catch (Exception e) {
			System.out.println("文件:" + filename + " 删除失败!");
			e.printStackTrace();
		}
	}


	@Override
	public void removeFileList(List<String> fileList) {
		if(fileList == null || fileList.size() < 1){
			return;			
		}
		// ftp客户端
		FTPClient ftp = null;
		try {
			ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
			String filename = "";
			String filePath = "";
			for(int i=0,s=fileList.size(); i<s; i++){
				filename = fileList.get(i);
				if(filename != null && filename.length() > 1){
					filePath = filename.substring(0,filename.lastIndexOf("/") + 1);
					filename = filename.substring(filename.lastIndexOf("/") + 1);
					try {
						riss2Ftp.delete(ftp, filePath, filename); // 删除
					} catch (Exception e) {
						System.out.println("文件：" + filename + " 删除失败");
					}
				}
			}
			riss2Ftp.disconnectFtpServer(ftp); //关闭客户端连接
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String[] getFilenameArr(String filepath) {
		FTPClient ftp = null;
		try {
			ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
			riss2Ftp.makeDirectory(ftp, filepath);
			String[] fileArr = ftp.listNames();
			riss2Ftp.disconnectFtpServer(ftp);
			return fileArr;
		} catch (Exception e) {
			System.out.println("获取FTP文件名文件名数组发生异常：" + e.getMessage());
			return null;
		}
	}

	@Override
	public void saveFile(String path, String fileName, InputStream inputStream) throws Exception {
		// ftp客户端
		FTPClient ftp = riss2Ftp.connectFtpServer(this.fileServiceBean);
		try {
			// 上传
			riss2Ftp.upload(ftp, inputStream, path, fileName);
		}finally {
			riss2Ftp.disconnectFtpServer(ftp);
		}
	}

}