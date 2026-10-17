package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FilenameUtils;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.bso.sc.IResFilesystemService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResImportTemplet;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceSubject;
import cn.com.adtech.riss2.upload.IUploadService;

public class ImportExcelBasicServiceImp extends CommonServiceImp{

	//学科分类缓存集合
	protected Map<String, Long> subjectMap = new HashMap<String, Long>();
	
	//中图法分类集合
	protected List<String> ztfList = new ArrayList<String>();
	
	//自定义分类集合
	protected List<ResDynamicType> zdyList = new LinkedList<ResDynamicType>();
	
	/**主题集合*/
	protected List<ResSourceSubject> resourceSubjectList = new LinkedList<ResSourceSubject>();
	
	/**用于临时保存已新增的资源主题，以免相同的主题重复新增到作业下*/
	protected Map<String, Long> subjectTempMap = new HashMap<String, Long>();
	
	/**模板中的分类和文件设置*/
	protected List<ResImportTemplet> templetTypeList = new LinkedList<ResImportTemplet>();
	
	private ResDynamicType dynamicType = null;
	
	private ResSourceSubject sourceSubject = null;
	
	protected String storeFolder = "";
	
	/**自定义分类根据分类名称或是分类号匹配，为true表示用户分类名称，否则用分类号。默认为分类名称*/
	protected boolean impDynamicName = true;
	public void setImpDynamicName(boolean impDynamicName) {
		this.impDynamicName = impDynamicName;
	}
	
	protected IImportTempletService importTempletService = null;
	protected IUploadService localUploadService; //本地文件服务器
	protected IUploadService remoteUploadService; //远程文件服务器
	protected IUploadService uploadService = null;
	protected IResFilesystemService fileService = null; //
	protected List<String> filenameList = null;
	
	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}
	public void setRemoteUploadService(IUploadService remoteUploadService) {
		this.remoteUploadService = remoteUploadService;
	}
	public void setFileService(IResFilesystemService fileService) {
		this.fileService = fileService;
	}
	public void setImportTempletService(IImportTempletService importTempletService) {
		this.importTempletService = importTempletService;
	}
	
	/**初始化学科分类**/
	public Map<String, Long>  initSubjectType(){
		subjectMap = new HashMap<String, Long>();
		String hql = "select rst.rstyId, rst.rstyName from ResSubjectType rst";
		List<Object[]> list = getHibernateTemplate().find(hql);
		if(list == null || list.size() < 1){
			return new HashMap<String, Long>();
		}
		Object[] obj;
		for(int i=0,s=list.size(); i<s; i++){
			obj = list.get(i);
			try {
				subjectMap.put(obj[1].toString(), Long.parseLong(obj[0].toString()));
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}
		return subjectMap;
	}
	
	/**初始化中图法分类**/
	public List<String> initZTFtype(){
		ztfList = new ArrayList<String>();
		String hql = "select rmt.bztyCode from ResMiddleType rmt";
		ztfList = getHibernateTemplate().find(hql);
		return ztfList;
	}
	
	/**初始化自定义分类**/
	public List<ResDynamicType> initZdyType(long rmstId){
		String hql = "from ResDynamicType rdt where rdt.rmstId=" + rmstId;
		zdyList = getHibernateTemplate().find(hql);
		return zdyList;
	}
	
	/**根据作业ID初始化资源主题*/
	public List<ResSourceSubject> initSourceSubject(Long rminId){
		String hql = "from ResSourceSubject where ";
		if(rminId == null){
			hql += "rssuRminId is null";
		}else{
			hql += "rssuRminId=" + rminId;
		}
		resourceSubjectList = getHibernateTemplate().find(hql);
		return resourceSubjectList;
	}
	
	/**
	 * 初始化模板中的分类和文件设置
	 * @param rmstId
	 * @param rltyId
	 */
	protected void initTypeTemplet(long rmstId, long rltyId){
		String hql = "from ResImportTemplet where riteRepoId<10 and resRmstId=" + rmstId + " and resRltyId=" + rltyId;
		this.templetTypeList = getHibernateTemplate().find(hql);
	}
	
	
	
	protected void initData(ResMetaStore store, String fileFolder, int fileType) {
		initSubjectType();
		initZTFtype();
		initZdyType(store.getRmstId());
		Long rfilid = store.getRmstRfilId();
		
		//只有传统文件格式的资源才会用到文件名集合，因此如果是其它三种格式的则不需要初始化
		if(fileType > 0){
			return;
		}
		
		storeFolder = SysConfigInfo.RES_DIR + store.getRmstFolder() + "/" + fileFolder;
		filenameList = new LinkedList<String>();
		String[] nameArr = getFileNames(storeFolder, rfilid);
		if(nameArr != null && nameArr.length > 0){
			//感觉文件名无须做验证，暂时注释验证
//			if(fileType == 1){
//				for(int i=0; i<nameArr.length; i++){
//					if(nameArr[i].indexOf(".") > 0){
//						logger.error("文件：" + nameArr[i] + " 不是有效文件夹，未被正确导入！");
//						continue;
//					}
//					filenameList.add(nameArr[i]);
//				}
//			}else{
//				for(int i=0; i<nameArr.length; i++){
//					if(nameArr[i].indexOf(".") < 0){
//						logger.error("文件：" + nameArr[i] + " 不是有效文件，未被正确导入！");
//						continue;
//					}
//					filenameList.add(nameArr[i]);
//				}
//			}
			
			for(int i=0; i<nameArr.length; i++){
				filenameList.add(nameArr[i]);
			}
		}
		
	}
	
	public String[] getFileNames(String folder, Long rfilid){
		IUploadService _uploadService = initUploadService(rfilid);
		filenameList = new LinkedList<String>();
		String[] nameArr = _uploadService.getFilenameArr(folder);
		return nameArr;
	}
	
	private IUploadService initUploadService(Long rfilid){
		if(rfilid == null || rfilid.equals(0L)){
			uploadService = localUploadService;
		}else{
			uploadService = remoteUploadService;
			ResFilesystem fileSystem = this.fileService.getFilesystem(rfilid);
			uploadService.setFileServiceBean(fileSystem);
		}
		return uploadService;
	}
	
	/**获取学科分类ID**/
	public Long getSubjectTypeId(String subjectName, Map<String, Long> _subjectMap){
		if(subjectName == null || subjectName.length() < 1){
			return null;
		}
		subjectName = subjectName.trim();
		if(_subjectMap.containsKey(subjectName)){
			return _subjectMap.get(subjectName);
		}else{
			return null;
		}
	}
	
	/**获取自定义分类ID**/
	public Long getZdyType(String zdyname, List<ResDynamicType> _zdyList, int rdtyType){
		if(zdyname == null || zdyname.length() < 1){
			return null;
		}
		if(_zdyList == null || _zdyList.size() < 1){
			return null;
		}
		zdyname = zdyname.trim();
		Long id = null;
		
		for(int i=0,s=_zdyList.size(); i<s; i++){
			dynamicType = _zdyList.get(i);
			//根据分类名称匹配
			if(impDynamicName){
				if(dynamicType.getRdtyName() != null && dynamicType.getRdtyName().trim().equals(zdyname) && dynamicType.getRdtyType() == rdtyType){
					id = dynamicType.getRdtyId();
					break;
				}
			}else{ //根据分类号匹配
				if(dynamicType.getRdtyCode() != null && dynamicType.getRdtyCode().trim().equals(zdyname) && dynamicType.getRdtyType() == rdtyType){
					id = dynamicType.getRdtyId();
					break;
				}
			}
		}
		return id;
	}
	
	public Long getSourceSubjectId(String subjectName, List<ResSourceSubject> subjectList){
//		if(CommonUtil.isNotNull(subjectName) == false){
//			return null;
//		}
		if(subjectList == null || subjectList.size() < 1){
			return null;
		}
		subjectName = subjectName.trim();
		Long id = null;
		
		for(int i=0,s=subjectList.size(); i<s; i++){
			sourceSubject = subjectList.get(i);
			if(sourceSubject.getRssuName().equals(subjectName)){
				id = sourceSubject.getRssuId();
				break;
			}
		}
		return id;
	}
	
	/**文件路径编码**/
	public String getUrl(String pathAndLocalFileName, String viewFileName){
		try {
			return "/FileDownload?downloadPath=" + uploadService.encode(pathAndLocalFileName) + "&fileName="
			+ uploadService.encode(viewFileName);
		} catch (Exception e) {
			return "";
		}
	}
	
	
	private String filePrename;
	private String fileExtname;
	private String fileAllname;
	
	
public static Map<String, String> FileExtMap = null;
	
	/**
	 * 根据文件名找相关的资源文件
	 * @param titlename 对应的文件名
	 * @return 对应的文件扩展名。如果没找到则返回null
	 */
	protected String containFilename(String filename, List<String> _filenameList) {
		if(FileExtMap == null){
			FileExtMap = new HashMap<String, String>();
			
			for(int i=0,s=_filenameList.size(); i<s; i++){
				String fname = _filenameList.get(i);
				
				String baseName = FilenameUtils.getBaseName(fname).toLowerCase();
				String extName = FilenameUtils.getExtension(fname).toLowerCase();
				FileExtMap.put(baseName, extName);
			}
		}
		filename = filename.toLowerCase();
		return FileExtMap.get(filename);
	}
	/**
	 * 根据文件名找相关的资源文件
	 * @param titlename 对应的文件名
	 * @return 对应的文件扩展名。如果没找到则返回null
	 */
	/*protected String containFilename(String filename, List<String> _filenameList) {
		for(int i=0,s=_filenameList.size(); i<s; i++){
			fileAllname = _filenameList.get(i);
			fileExtname = fileAllname.substring(fileAllname.lastIndexOf(".") + 1);
			if(fileExtname.toLowerCase().contains("doc")){
				filename = filename.replace("“", "『").replace("”", "』");
			}
			filePrename = fileAllname.substring(0,fileAllname.lastIndexOf("."));
			if(filePrename.equals(filename)){
				return fileExtname;
			}
		}
		return null;
	}*/
	

	/**
	 * 生成随机数
	 * @return
	 */
	private String randomChar(int count){
		Random dd = new Random();
		String a = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuffer aa = new StringBuffer();
		for(int j=0;j<count;j++){
			int i = dd.nextInt(a.length());
			aa.append(a.charAt(i));
		}
		return aa.toString();
	}
	
	/**
	 * 获取随机生成的文件名
	 *
	 * @author MaChao
	 * @date 2013-12-6
	 *
	 * @return
	 */
	protected String getMathFilename() {
		String filename = randomChar(20) + System.currentTimeMillis();
		return filename;
	}
	
	/**
	 * 修改文件名
	 *
	 * @author MaChao
	 * @date 2013-12-6
	 *
	 * @param sourceFilePath 待修改的文件路径
	 * @param newFilename 新的文件名
	 * @return
	 */
	protected static boolean fileRename(String sourceFilePath, String newFilename) {
		File file = new File(sourceFilePath);
		if(file.exists()){
			String oldName = file.getName();
			if(oldName.indexOf(".") >= 0){
				String extName = oldName.substring(oldName.lastIndexOf("."));
				newFilename = newFilename + extName;
			}
			String fileFolder = file.getParent();
			File destFile = new File(fileFolder + "//" + newFilename);
			
			boolean b = file.renameTo(destFile);
			return b;
		}else{
			System.out.println("文件“"+sourceFilePath+"”不存在！");
			return false;
		}
	}
	
}























