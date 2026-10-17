package cn.com.adtech.riss2.controller.console.resource.build;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.ILanguageTypeService;
import cn.com.adtech.riss2.bso.resource.IMemberResourceService;
import cn.com.adtech.riss2.bso.resource.IMetadataElementPoolService;
import cn.com.adtech.riss2.bso.resource.IMissionInfoManagementService;
import cn.com.adtech.riss2.bso.resource.IResDynamicTypeService;
import cn.com.adtech.riss2.bso.resource.IResMissionInfoCheckService;
import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.bso.resource.ISourceLanguageService;
import cn.com.adtech.riss2.bso.sc.IResFilesystemService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ImageScaleTool;
import cn.com.adtech.riss2.core.ImgHandleUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.file.WordToPdf;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.upload.IUploadService;
import cn.com.adtech.riss2.upload.LocalUploadService;
import cn.com.adtech.riss2.wsClient.FileConvertServiceUtil;

import com.jbetter.hibernate.IdGenerator;

/**
 * 为资源上传，更新专门提取的类
 * 
 * @author MaChao
 * @date 2010-11-18
 */
public class ResourceFileLoad extends MultiActionController {
	protected IMetadataElementPoolService elementPool = null;

	protected IResourceDataService resourceService = null;

	protected IMissionInfoManagementService missionService = null;

	protected ISourceLanguageService sldao = null;

	protected ILanguageTypeService lanService = null;

	protected IResFilesystemService fileService = null;

	protected IUploadService localUploadService;

	protected IUploadService remoteUploadService;
	
	protected IMemberResourceService memberResourceService = null;
	
	protected IResMissionInfoCheckService resourceCheckedService = null;
	
	protected IResDynamicTypeService dynamicTypeService = null;
	
	// 图片类型的资源，如果没有新增或修改封面，则生成封面
	protected Boolean mustGenerateImg = false;

	public void setMissionService(IMissionInfoManagementService missionService) {
		this.missionService = missionService;
	}

	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}

	public void setElementPool(IMetadataElementPoolService elementPool) {
		this.elementPool = elementPool;
	}

	public void setSldao(ISourceLanguageService sldao) {
		this.sldao = sldao;
	}

	public void setLanService(ILanguageTypeService lanService) {
		this.lanService = lanService;
	}

	public void setFileService(IResFilesystemService fileService) {
		this.fileService = fileService;
	}

	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}

	public void setRemoteUploadService(IUploadService remoteUploadService) {
		this.remoteUploadService = remoteUploadService;
	}
	
	public void setDynamicTypeService(IResDynamicTypeService dynamicTypeService) {
		this.dynamicTypeService = dynamicTypeService;
	}

	protected IUploadService uploadService = null;

	protected String errorMsg = "";
	
	LocalUploadService localUpload = new LocalUploadService();

	/**
	 * 根据源文件或者预览文件获取封面
	 * 
	 * @param resource
	 * @param filePath
	 * @param localFileNamePre
	 * @param request
	 * @return true表示需要自动生成封面
	 * @throws Exception
	 */
	private Boolean getSourceImage(ResSourceData resource, String filePath, String localFileNamePre, HttpServletRequest request)
			throws Exception {
		// 是否需要自动生成封面(默认不)
		Boolean hasMust = false;
		String localFileName = "";
		String fileName = "";
		MultipartFile file = null;
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		if(hasLoadFile("loadfile", request)) {// 根据源文件获取资源封面，目前只针对视频和图片
			file = multiRequest.getFile("loadfile");
			fileName = file.getOriginalFilename();
		}else if(hasLoadFile("viewfile", request)) {// 根据预览文件获取资源封面，目前只针对视频
			file = multiRequest.getFile("viewfile");
			fileName = file.getOriginalFilename();
		}
		if(ServiceUtil.isNotNull(fileName) && fileName.indexOf(".") > 0) {
			// 源文件是图片，则立即生成不再需要另外生成
			if(ConstantsObj.FileConvertName.TYPE_PHOTO.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
				localFileName = localFileNamePre + ConstantsObj.FileConvertName.NAME_IMG + "."
						+ ConstantsObj.FileConvertName.EXT_JPEG_NAME;
				
				
				uploadService.saveFile(filePath, localFileName, file.getInputStream());
				
				String fileNamePath = filePath + localFileName;
				//绝对路径
				String p = SysConfigInfo.FILE_ROOT_DIR + fileNamePath; 
				//此处是根据文档的封面进行剪切的。可以优化为不通的资源载体剪切为不同的尺寸大小
				ImgHandleUtil.pictureHandle(p, 180, 250, false);
				
				resource.setRsdaBigimgName(changeSuffix(fileName, ConstantsObj.FileConvertName.EXT_JPEG_NAME));
				resource.setRsdaBigimgLocalName(fileNamePath);
				resource.setRsdaBigimgUrl(getUrl(fileNamePath, changeSuffix(fileName, ConstantsObj.FileConvertName.EXT_JPEG_NAME)));
				return false;
			}
			// 源文件是视频，则需要生成
			if(ConstantsObj.FileConvertName.TYPE_VIDEO.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
				localFileName = localFileNamePre + ConstantsObj.FileConvertName.NAME_IMG + "."
						+ ConstantsObj.FileConvertName.EXT_JPEG_NAME;
				String fileNamePath = filePath + localFileName;
				resource.setRsdaBigimgName(changeSuffix(fileName, ConstantsObj.FileConvertName.EXT_JPEG_NAME));
				resource.setRsdaBigimgLocalName(fileNamePath);
				resource.setRsdaBigimgUrl(getUrl(fileNamePath, changeSuffix(fileName, ConstantsObj.FileConvertName.EXT_JPEG_NAME)));
				return true;
			}
			//如果是PDF文件，则获取第一页作为封面
//			if(fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase().equals("pdf")){
//				localFileName = localFileNamePre + ConstantsObj.FileConvertName.NAME_IMG + ".jpg";
//				String fileNamePath = filePath + localFileName;
//				resource.setRsdaBigimgName(localFileName);
//				resource.setRsdaBigimgLocalName(fileNamePath);
//				resource.setRsdaBigimgUrl(getUrl(fileNamePath, fileName));
//				return true;
//			}
		}
		return hasMust;
	}

	/**
	 * 上传封面处理
	 * 
	 * @param resource
	 * @param filePath
	 * @param localFileNamePre
	 * @param request
	 * @return true表示需要执行文件转换
	 * @throws Exception
	 */
	public Boolean loadImageFile(ResSourceData resource, String filePath, String localFileNamePre, HttpServletRequest request)
			throws Exception {
		// 对应属性置空
		resource.setRsdaBigimgName(null);
		resource.setRsdaBigimgUrl(null);
		resource.setRsdaBigimgLocalName(null);
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		String fileName = "";
		String localFileName = "";
		MultipartFile file = multiRequest.getFile("bigimg");
		if(!hasLoadFile("bigimg", request)) { // 如果没有上传封面文件，则通过其它两个文件获取
			return getSourceImage(resource, filePath, localFileNamePre, request);
		}
		fileName = file.getOriginalFilename();
		String extName = "";
		if(ServiceUtil.isNotNull(fileName) && fileName.indexOf(".") > 0) { // 执行上传操作
			extName = ConstantsObj.FileConvertName.EXT_JPEG_NAME; // 扩展名
			localFileName = localFileNamePre + ConstantsObj.FileConvertName.NAME_IMG + "." + extName;
			uploadService.saveFile(filePath, localFileName, file.getInputStream());
			
			//绝对路径
			String p = SysConfigInfo.FILE_ROOT_DIR + filePath + localFileName; 
			//此处是根据文档的封面进行剪切的。可以优化为不通的资源载体剪切为不同的尺寸大小
			ImgHandleUtil.pictureHandle(p, 180, 250, false);
			
		}
		String fileNamePath = filePath + localFileName;
		resource.setRsdaBigimgName(changeSuffix(fileName, ConstantsObj.FileConvertName.EXT_JPEG_NAME));
		resource.setRsdaBigimgLocalName(fileNamePath);
		resource.setRsdaBigimgUrl(getUrl(fileNamePath, resource.getRsdaBigimgName()));
		return false;
	}

	/**
	 * 上传源文件
	 * 
	 * @param resource
	 * @param filePath
	 * @param localFileNamePre
	 * @param request
	 * @return true表示需要执行文件转换
	 * @throws Exception
	 */
	public Boolean loadSourceFile(ResSourceData resource, String filePath, String localFileNamePre, HttpServletRequest request)
			throws Exception {
		// 是否需要执行文件转换(默认不)
		Boolean hasMust = false;
		// 对应属性置空
		resource.setRsdaFileName(null);
		resource.setRsdaFileUrl(null);
		resource.setRsdaFileLocalName(null);
		resource.setRsdaFileSwfUrl(null);
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile("loadfile");
		if(!hasLoadFile("loadfile", request)) {
			resource.setRsdaFileType("");
			resource.setRsdaFileSize(0L);
			return hasMust;
		}
		String fileName = file.getOriginalFilename();
		String localFileName = "";
		String extName = "";
		if(ServiceUtil.isNotNull(fileName) && fileName.indexOf(".") > 0) { // 执行上传操作
			extName = fileName.substring(fileName.lastIndexOf(".") + 1); // 扩展名
			extName = extName.toLowerCase();
			 
			localFileName = localFileNamePre + ConstantsObj.FileConvertName.NAME_DATA + "." + extName;
			//uploadService.saveFile(filePath, localFileName, file.getBytes());
			uploadService.saveFile(filePath, localFileName, file.getInputStream());
		}
		resource.setRsdaFileType(extName);
		resource.setRsdaFileSize(file.getSize()); 
		String fileNamePath = filePath + localFileName;
		if(ServiceUtil.isNotNull(fileName)){
			resource.setRsdaFileName(fileName);
			resource.setRsdaFileLocalName(fileNamePath);
			resource.setRsdaFileUrl(getUrl(fileNamePath, fileName));
		}
		if(ConstantsObj.FileConvertName.TYPE_PHOTO.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
			resource.setRsdaFileSwfUrl(getUrl(fileNamePath, fileName));
			return false;
		}else if(ConstantsObj.FileConvertName.TYPE_AUDIO.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
			resource.setRsdaFileSwfUrl(getUrl(changeSuffix(fileNamePath, ConstantsObj.FileConvertName.EXT_MP3_NAME), changeSuffix(fileName,
					ConstantsObj.FileConvertName.EXT_MP3_NAME)));
			if(extName.equals(ConstantsObj.FileConvertName.EXT_MP3_NAME)) {
				return false;
			}else {
				return true;
			}
		}else if(ConstantsObj.FileConvertName.TYPE_VIDEO.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
			resource.setRsdaFileSwfUrl(getUrl(changeSuffix(fileNamePath, ConstantsObj.FileConvertName.EXT_FLV_NAME), changeSuffix(fileName,
					ConstantsObj.FileConvertName.EXT_FLV_NAME)));
			if(extName.equals(ConstantsObj.FileConvertName.EXT_FLV_NAME) || extName.equals("mp4")) {
				resource.setRsdaFileSwfUrl(getUrl(changeSuffix(fileNamePath, extName), changeSuffix(fileName, extName)));
				return false;
			}else {
				return true;
			}
		}else if(ConstantsObj.FileConvertName.TYPE_DOCUMENT.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
			//pdf采用pdf.js预览，不做swf转换
			if(extName.equals("pdf")){
				return false;
				
			}else if (extName.equals("doc") || extName.equals("docx")){
				//word转换为pdf，采用pdf.js预览
				String wordadd = resource.getRsdaFileLocalName();
				String pdfUrl = WordToPdf.conversionInit(SysConfigInfo.FILE_ROOT_DIR, wordadd);
				
				resource.setRsdaFileSwfUrl(pdfUrl);
			}else{
				resource.setRsdaFileSwfUrl(getUrl(changeSuffix(fileNamePath, ConstantsObj.FileConvertName.EXT_SWF_NAME), changeSuffix(fileName,
						ConstantsObj.FileConvertName.EXT_SWF_NAME)));
				if(extName.equals(ConstantsObj.FileConvertName.EXT_SWF_NAME)) {
					return false;
				}else {
					return true;
				}
			}
		}
		return hasMust;
	}
	
	/**
	 * 上传资源的附件
	 * @param resource 
	 * @param filePath 
	 * @param localFileNamePre 
	 * @param request 
	 */
	public void loadAttachmentFile(ResSourceData resource, String filePath, String localFileNamePre, HttpServletRequest request,ResFilesystem fileService)
		throws Exception {
		
		boolean flag = false;
		int fileNumber = (int)MyRequestUtil.getLongParameter(request, "fileNumber");
		ResSourceFile rsfile = null;
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = null;
		for(int i=1;i<=fileNumber;i++){
			rsfile = new ResSourceFile();
			int resIndex = MyRequestUtil.getIntParameter(request, "annexNum"+i);
			String resFileRemark = MyRequestUtil.getStringParameter(request, "annexRemark"+i);
			file = multiRequest.getFile("annexFile"+i);
			if(!hasLoadFile("annexFile"+i, request)) {
				continue;
			}
			
			String fileName = file.getOriginalFilename();
			String localFileName = "";
			String extName = "";
			if(ServiceUtil.isNotNull(fileName) && fileName.indexOf(".") > 0) { // 执行上传操作
				extName = fileName.substring(fileName.lastIndexOf(".") + 1); // 扩展名
				localFileName = System.currentTimeMillis() + "" + ConstantsObj.FileConvertName.NAME_DATA + "." + extName;
				uploadService.saveFile(filePath, localFileName, file.getInputStream());
			}
			String fileNamePath = filePath + localFileName;
			if(ServiceUtil.isNotNull(fileName)){
				rsfile.setResLocalPath(fileNamePath);
				rsfile.setResFileName(fileName);
				rsfile.setResFilePath(getUrl(fileNamePath, fileName));
				rsfile.setResRsdaId(resource.getRsdaId());
				rsfile.setResFileRemark(resFileRemark);
				rsfile.setResIndex(resIndex);
			}
			
			if(ConstantsObj.FileConvertName.TYPE_PHOTO.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
				rsfile.setResViewPath(getUrl(fileNamePath, fileName));
				flag = false;
			}else if(ConstantsObj.FileConvertName.TYPE_AUDIO.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
				rsfile.setResViewPath(getUrl(changeSuffix(fileNamePath, ConstantsObj.FileConvertName.EXT_MP3_NAME), changeSuffix(fileName,
						ConstantsObj.FileConvertName.EXT_MP3_NAME)));
				if(extName.toLowerCase().equals(ConstantsObj.FileConvertName.EXT_MP3_NAME)) {
					flag = false;
				}else {
					flag = true;
				}
			}else if(ConstantsObj.FileConvertName.TYPE_VIDEO.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
				rsfile.setResViewPath(getUrl(changeSuffix(fileNamePath, ConstantsObj.FileConvertName.EXT_FLV_NAME), changeSuffix(fileName,
						ConstantsObj.FileConvertName.EXT_FLV_NAME)));
				if(extName.toLowerCase().equals(ConstantsObj.FileConvertName.EXT_FLV_NAME)) {
					flag = false;
				}else {
					flag = true;
				}
			}else if(ConstantsObj.FileConvertName.TYPE_DOCUMENT.equals(ServiceUtil.getFileTypeBySpread(fileName))) {
				rsfile.setResViewPath(getUrl(changeSuffix(fileNamePath, ConstantsObj.FileConvertName.EXT_SWF_NAME), changeSuffix(fileName,
						ConstantsObj.FileConvertName.EXT_SWF_NAME)));
				if(extName.toLowerCase().equals(ConstantsObj.FileConvertName.EXT_SWF_NAME)) {
					flag = false;
				}else {
					flag = true;
				}
			}
			//保存附件
			Long rsfiId = missionService.saveAttachmentFile(rsfile);
			if(rsfiId<1){
				continue;
			}
			if(flag){
				String fileLocalName = rsfile.getResLocalPath();
				if(ConstantsObj.FileConvertName.TYPE_AUDIO.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
					FileConvertServiceUtil.audioConvert(fileLocalName, changeSuffix(fileLocalName, ConstantsObj.FileConvertName.EXT_MP3_NAME),
							fileService.getRfilAddress());
				}
				if(ConstantsObj.FileConvertName.TYPE_VIDEO.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
					FileConvertServiceUtil.videoConvert(fileLocalName, changeSuffix(fileLocalName, ConstantsObj.FileConvertName.EXT_FLV_NAME),
							"", fileService.getRfilAddress());
				}
				if(ConstantsObj.FileConvertName.TYPE_DOCUMENT.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
					FileConvertServiceUtil.documentConvert(fileLocalName,
							changeSuffix(fileLocalName, ConstantsObj.FileConvertName.EXT_SWF_NAME), fileService.getRfilAddress());
				}
			}
		}
	}
	

	/**
	 * 上传资源的附件
	 */
	public void upLoadFile(String filePath, String localFileName, InputStream inputStream){
		try {
			uploadService.saveFile(filePath, localFileName, inputStream);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
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
		
		String fileFolder = localUpload.getFolderByYearMonth();
		localUpload.saveFile(fileFolder, localFileName, inputStream);
		return fileFolder + localFileName;
	}

	/**
	 * 转换后缀
	 * 
	 * @author MaChao
	 * @param fileName
	 *            [原文件名]
	 * @param suffix
	 *            [后缀，如swf]
	 * @return
	 */
	protected String changeSuffix(String fileName, String suffix) {
		if(ServiceUtil.isNotNull(fileName)) {
			if(fileName.lastIndexOf(".") != -1) {
				return fileName.substring(0, fileName.lastIndexOf(".") + 1) + suffix;
			}
		}
		return fileName;
	}

	/**
	 * 组装url
	 * 
	 * @param pathAndLocalFileName
	 * @param viewFileName
	 * @return
	 * @throws Exception
	 */
	protected String getUrl(String pathAndLocalFileName, String viewFileName) throws Exception {
		return "/FileDownload?downloadPath=" + uploadService.encode(pathAndLocalFileName) + "&fileName="
				+ uploadService.encode(viewFileName);
	}

	/**
	 * 判断是否上传了文件
	 * 
	 * @param type[值为三者之一：bigimg,viewfile,loadfile]
	 * @param request
	 * @return[true表示有文件上传]
	 */
	protected Boolean hasLoadFile(String type, HttpServletRequest request) {
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile(type);
		if(file == null || file.getSize() <= 0 || !ServiceUtil.isNotNull(file.getOriginalFilename())) {
			return false;
		}
		return true;
	}
	
	public void setMemberResourceService(
			IMemberResourceService memberResourceService) {
		this.memberResourceService = memberResourceService;
	}

	public Boolean getMustGenerateImg() {
		return mustGenerateImg;
	}

	public void setMustGenerateImg(Boolean mustGenerateImg) {
		this.mustGenerateImg = mustGenerateImg;
	}

	public IUploadService getUploadService() {
		return uploadService;
	}

	public void setUploadService(IUploadService uploadService) {
		this.uploadService = uploadService;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public IMetadataElementPoolService getElementPool() {
		return elementPool;
	}

	public IResourceDataService getResourceService() {
		return resourceService;
	}

	public IMissionInfoManagementService getMissionService() {
		return missionService;
	}

	public ISourceLanguageService getSldao() {
		return sldao;
	}

	public ILanguageTypeService getLanService() {
		return lanService;
	}

	public IResFilesystemService getFileService() {
		return fileService;
	}

	public IUploadService getLocalUploadService() {
		return localUploadService;
	}

	public IUploadService getRemoteUploadService() {
		return remoteUploadService;
	}

	public IMemberResourceService getMemberResourceService() {
		return memberResourceService;
	}

	public void setResourceCheckedService(
			IResMissionInfoCheckService resourceCheckedService) {
		this.resourceCheckedService = resourceCheckedService;
	}
}
