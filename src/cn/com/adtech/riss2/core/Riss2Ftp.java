package cn.com.adtech.riss2.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.exception.ErrorMessageException;

/**
 * 使用commons的net包进行ftp连接<br>
 * 相关包：commons-net-1.4.1.jar,commons-io-1.4.jar
 * 
 * @author MaChao
 * @create 2010-10-21
 */
public class Riss2Ftp {

	public Riss2Ftp() {}

	/**
	 * 连接ftp服务器,返回ftp客户端
	 * 
	 * @param ftpHostName
	 *            ftp服务器地址
	 * @param ftpUserName
	 *            登录名
	 * @param ftpPassword
	 *            登录密码
	 */
	public FTPClient connectFtpServer(ResFilesystem fileService) throws Exception {
		if(fileService == null){
			throw new Exception("文件系统实体ResFilesystem为空，FTP服务器连接失败！");
		}
		
		// 创建ftp客户端对象
		FTPClient ftp = new FTPClient();
		ftp.setControlEncoding("GBK");
		try {
			// 链接到ftp服务器
			ftp.connect(fileService.getRfilFtpHost());
			System.out.println("连接到ftp服务器：" + fileService.getRfilFtpHost() + " 成功..开始登录");
			// 登录.用户名 密码
			if(ftp.login(fileService.getRfilFtpUsername(), fileService.getRfilFtpPassword())) {
				System.out.println("登录成功.");
				// 返回值
				int reply = ftp.getReplyCode();
				if((!FTPReply.isPositiveCompletion(reply))) {
					ftp.disconnect();
					throw new Exception("ftp连接失败！");
				}
			}else
				System.out.println("登录失败.");
		}catch(Exception e) {
			e.printStackTrace();
			throw new Exception("ftp连接失败！", e);
		}
		ftp.setFileType(FTP.BINARY_FILE_TYPE);
		return ftp;
	}

	/**
	 * 文件上传
	 * 
	 * @param ftp
	 *            ftp客户端
	 * @param localPath
	 *            本地文件完整路径
	 * @param remotePath
	 *            ftp上传路径，请以"/"分割路径
	 * @param remoteFile
	 *            文件名
	 */
	public void upload(FTPClient ftp, InputStream localIn, String remotePath, String remoteFile) throws Exception {
		try {
			makeDirectory(ftp, remotePath);
			boolean result = ftp.storeFile(remoteFile, localIn);
			if(!result) {
				throw new ErrorMessageException("文件上传失败!");
			}
			System.out.println("文件上传成功！");
		}catch(Exception ex) {
			throw new ErrorMessageException("文件上传失败!", ex);
		}finally {
			localIn.close();
		}
	}

	/**
	 * 进入目录
	 * 
	 * @param ftp
	 * @param remotePath
	 * @throws Exception
	 */
	public void makeDirectory(FTPClient ftp, String remotePath) throws Exception {
		// 设置目录
		String[] dirArray = remotePath.split("/");
		if(dirArray.length > 0) {
			try {
				// ftp.changeToParentDirectory();
				ftp.changeWorkingDirectory("/");
				for(int k = 0; k < dirArray.length; k++) {
					if(dirArray[k] != null && !dirArray[k].equals(""))
						if(!ftp.changeWorkingDirectory(dirArray[k])) {
							ftp.makeDirectory(dirArray[k]);
							ftp.changeWorkingDirectory(dirArray[k]);
						}
				}
			}catch(Exception e) {
				throw new Exception("变更目录失败!", e);
			}
		}
	}

	/**
	 * 在父目录下创建子目录及其必须的父目录
	 * 
	 * @param ftp
	 *            ftp连接
	 * @param parentDir
	 *            父目录
	 * @param childDir
	 *            子目录
	 * @return 创建成功返回true，失败返回false
	 * @throws IOException
	 * @author MaChao
	 */
	public static boolean createDir(FTPClient ftp, String parentDir, String childDir) throws IOException {
		boolean result = false;
		StringBuffer sbParentDir = new StringBuffer(parentDir);
		if(!sbParentDir.toString().endsWith("/"))
			sbParentDir.append("/");
		if(!ftp.changeWorkingDirectory(sbParentDir.toString() + childDir)) {// 当目录不存在时，才创建目录。
			String[] dirArray = childDir.split("/");
			for(int i = 0; i < dirArray.length; i++) {
				if(dirArray[i] != null && dirArray[i].trim().length() > 0) {
					result = ftp.makeDirectory(sbParentDir.toString() + dirArray[i]);
					sbParentDir.append(dirArray[i]).append("/");
				}
			}
		}
		return result;
	}

	/**
	 * 文件上传
	 * 
	 * @param ftp
	 *            ftp客户端
	 * @param localPath
	 *            本地文件完整路径
	 * @param remotePath
	 *            ftp上传路径，请以"/"分割路径
	 * @param remoteFile
	 *            文件名
	 */
	public void delete(FTPClient ftp, String remotePath, String remoteFile) throws Exception {
		makeDirectory(ftp, remotePath);
		try {
			boolean result = ftp.deleteFile(remoteFile);
			if(!result) {
				throw new Exception("文件删除失败!");
			}
			System.out.println("文件删除成功！");
		}catch(Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 获得远端文件的流,用于移动文件
	 * 
	 * @param ftp
	 * @param remotePath
	 *            ftp服务器文件的路径
	 * @param fileName
	 *            ftp服务器文件的名字
	 * @return InputStream
	 * @throws Exception
	 */
	public InputStream getRemoteStream(FTPClient ftp, String remotePath, String fileName) throws Exception {
		makeDirectory(ftp, remotePath);
		InputStream stream = ftp.retrieveFileStream(fileName);
		if(!ftp.completePendingCommand()) {
			throw new ErrorMessageException("文件移动失败！");
		}
		return stream;
	}

	/**
	 * 关闭连接
	 * 
	 * @param ftp
	 * @author MaChao
	 */
	public void disconnectFtpServer(FTPClient ftp) {
		if(ftp != null) {
			try {
				ftp.logout();
				ftp.disconnect();
				System.out.println("关闭ftp连接成功！");
			}catch(Exception ex) {
				// throw new ErrorMessageException("关闭ftp连接失败！",ex);
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 编码
	 * 
	 * @param str
	 * @return
	 * @author MaChao
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String str) throws UnsupportedEncodingException {
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
	public static String decode(String str) throws IOException {
		if(str == null)
			return "";
		Base64 base64 = new Base64();
		str = new String(base64.decode(str.getBytes()));
		return java.net.URLDecoder.decode(str, "utf-8");
	}
}
