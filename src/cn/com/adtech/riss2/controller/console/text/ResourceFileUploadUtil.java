package cn.com.adtech.riss2.controller.console.text;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.upload.LocalUploadService;

import com.jbetter.hibernate.IdGenerator;

public class ResourceFileUploadUtil {

	private LocalUploadService uploadService = new LocalUploadService();
	public String[] uploadfile(HttpServletRequest request, String folder, String fileName) throws Exception{
		//0：物理路径   1：访问路径  2：文件名称  3：文件格式
		String[] arr = new String[4];
		String filename = "";
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile(fileName);
		if(file == null || file.getSize() <= 0 || !CommonUtil.isNotNull(file.getOriginalFilename())) {
			return null;
		}
		filename = file.getOriginalFilename();
		arr[2] = filename;
		String localFileName = "";
		String extName = "";
		
		Long n = (System.currentTimeMillis() + IdGenerator.generateId());
		System.out.println("随机数：" + n);
		if(filename.indexOf(".") > 0) { // 执行上传操作
			extName = filename.substring(filename.lastIndexOf(".") + 1); // 扩展名
			localFileName = n + "." + extName;
			arr[3] = extName;
		}else{
			localFileName = n + "";
			arr[3] = "";
		}
		InputStream inputStream = file.getInputStream();
		
		String filepath = SysConfigInfo.FILE_ROOT_DIR;
		if(CommonUtil.isNotNull(folder) == false){
			folder = uploadService.getFolderByYearMonth();
		}
//		String fileFolder = uploadService.getYearMonth();
		uploadService.saveFile(folder, localFileName, inputStream);
		
		String localpath = filepath + folder + "/" + localFileName;
		arr[0] = localpath;
		
		String fileUrl = CommonUtil.getUrl(folder + "/" + localFileName, filename);
		arr[1] = fileUrl;
		return arr;
	}
	
	public String uploadfiles(String filePath) throws Exception{
        String filename = "";
        File file = new File(filePath);
        if(file == null || !file.exists()) {
            return null;
        }
        filename = file.getName();
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
        InputStream inputStream = new FileInputStream(file);
        
        String fileFolder = SysConfigInfo.IMP_EXCEL_DIR;
        if(CommonUtil.isNotNull(fileFolder) == false){
            fileFolder = uploadService.getFolderByYearMonth();
        }
        uploadService.saveFile(fileFolder, localFileName, inputStream);
        
        String fileUrl = CommonUtil.getUrl(fileFolder + "/" + localFileName, filename);
        return fileUrl;
    }
	
	/**
	 * 
	 *
	 * @author MaChao
	 * @date Nov 18, 2015
	 *
	 * @param filePath
	 */
	public void deleteFile(String filePath){
		if(!CommonUtil.isNotNull(filePath)){
			return;
		}
		File file = new File(filePath);
		if(file.exists()){
			file.delete();
		}
	}
}
