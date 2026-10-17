package cn.com.adtech.riss2.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.upload.LocalUploadService;

import com.jbetter.hibernate.IdGenerator;

public class FileUploadController  extends MultiActionController{

	LocalUploadService uploadService = new LocalUploadService();
	public String uploadFile(HttpServletRequest request, String fileName) throws IOException{
		String filename = "";
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile(fileName);
		if(file == null || file.getSize() <= 0 || !CommonUtil.isNotNull(file.getOriginalFilename())) {
			return "";
		}
		filename = file.getOriginalFilename();
		String localFileName = "";
		String extName = "";
		
		Long n = (System.currentTimeMillis() + IdGenerator.generateId());
		System.out.println("随机数：" + n);
		if(filename.indexOf(".") > 0) { // 执行上传操作
			extName = filename.substring(filename.lastIndexOf(".") + 1); // 扩展名
			localFileName = n + "." + extName;
		}else{
			localFileName = n + "";
		}
		InputStream inputStream = file.getInputStream();
		
		String fileFolder = uploadService.getFolderByYearMonth();
		uploadService.saveFile(fileFolder, localFileName, inputStream);
		
		return fileFolder + localFileName;
	}
	
	public void deleteFile(HttpServletRequest request, String fileLocalPath){
		if(CommonUtil.isNotNull(fileLocalPath) == false){
			return;
		}
		String filepath = request.getRealPath("/") + "/" + fileLocalPath;
		File file = new File(filepath);
		if(file.exists()){
			file.delete();
		}
	}
	

	/**
	 * 获取错误html信息
	 * @param errorInfo
	 * @return
	 */
	protected String getErrorMessage(String errorInfo){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<span style='color:red'><h2>错误提示：</h2><hr>");
		buffer.append("<span style='font-size:12px;'>" + errorInfo + "<br>");
		buffer.append("<br>点击<a href='javascript:window.history.go(-1);' style='font-family:宋体;'><<返回</a> 重新操作</span></span>");
		errorInfo = buffer.toString();
		buffer = null;
		
		return errorInfo;
	}
}
