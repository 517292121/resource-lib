package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.cache.IPlatformCacheService;
import cn.com.adtech.riss2.bso.resource.IMetadataManagementService;
import cn.com.adtech.riss2.bso.resource.IResDynamicTypeService;
import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.bso.sc.IResFilesystemService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ImgHandleUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResMetaPerm;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResStoreLiter;
import cn.com.adtech.riss2.domain.resource.ResTempletMeta;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;

@SuppressWarnings("unchecked")
public class DefineStoreController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(DefineStoreController.class);

	private IMetadataManagementService metadataManagementService;
	private IResourceDataService resourceService = null;
	private IResDynamicTypeService dynamicTypeService = null;
	public void setDynamicTypeService(IResDynamicTypeService dynamicTypeService) {
		this.dynamicTypeService = dynamicTypeService;
	}

	private IUploadService localUploadService;
	private IUploadService remoteUploadService;
	private IResFilesystemService fileService;
	private IPlatformCacheService cacheService;
	public void setCacheService(IPlatformCacheService cacheService) {
		this.cacheService = cacheService;
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
	public IMetadataManagementService getMetadataManagementService() {
		return metadataManagementService;
	}
	public void setMetadataManagementService(IMetadataManagementService metadataManagementService) {
		this.metadataManagementService = metadataManagementService;
	}
	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}
	private IUploadService uploadService = null;
	/**
	 * 初始化特色库页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-8-31
	 */
	public ModelAndView initDefineStoreView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/metadata/defineStore");
	}

	/**
	 * 初始化新增特色库窗口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-15
	 */
	public ModelAndView initAddMetaStore(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		map = initRltySysManData(map, null);
		String url = "/console/data/resource/defineStore.htm?action=saveAddMetaStore";
		map.put("url", url);
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) {
			response.sendRedirect(request.getContextPath() + "/console/index.htm");
			return null;
		}
		map.put("currentSmanId", sysManager.getSmanId());
		// 初始化文件服务器
		map.put("serverList", fileService.getAllFileServiceOption(-1L));
		map.put("rmstGroupOne", DynamicTypeController.getDynamicTypeByCode(1));
		map.put("rmstGroupTwo", DynamicTypeController.getDynamicTypeByCode(2));
		map.put("rmstGroupThree", DynamicTypeController.getDynamicTypeByCode(3));
		return new ModelAndView("resource/metadata/addMetaStore", map);
	}

	/**
	 * 初始化编辑特色库窗口
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-16
	 */
	public ModelAndView initEditMetaStore(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);
		if(rmstId > 0) {
			map = initRltySysManData(map, rmstId);
			ResMetaStore rms = (ResMetaStore)this.metadataManagementService.get(ResMetaStore.class, rmstId);
			if(rms != null) {
				List rltyIds = this.metadataManagementService.getRltyIdsList(rmstId);
				List sysManIds = this.metadataManagementService.getSysManIdsList(rmstId);
				map.put("rltyIds", rltyIds);
				map.put("smanIds", sysManIds);
				map.put("resMetaStore", rms);
				String url = "/console/data/resource/defineStore.htm?action=editAddMetaStore";
				map.put("url", url);
				// 初始化文件服务器
				Long tempId = rms.getRmstRfilId();
				map.put("serverList", fileService.getAllFileServiceOption(tempId == null ? -1 : tempId));
				
				map.put("rmstGroupOne", rms.getRmstGroupOne());
				map.put("rmstGroupTwo", rms.getRmstGroupTwo());
				map.put("rmstGroupThree", rms.getRmstGroupThree());
			}
		}
		return new ModelAndView("resource/metadata/addMetaStore", map);
	}

	/**
	 * 编辑特色库
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-16
	 */
	public ModelAndView editAddMetaStore(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] rltyIds = null;
		ResMetaStore rms = null;
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return null;
			}
			Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);
			String rmstName = ServletRequestUtils.getStringParameter(request, "rmstName", "").trim(); // 特色库名称
			String rmstCompany = ServletRequestUtils.getStringParameter(request, "rmstCompany", "").trim(); // 建设单位
			Long rmstStatus = ServletRequestUtils.getLongParameter(request, "rmstStatus", 1); // 特色库状态
			String rmstRemark = ServletRequestUtils.getStringParameter(request, "rmstRemark", "").trim(); // 描述
			Long rmstCmtstus = ServletRequestUtils.getLongParameter(request, "rmstCmtstus", 1);// 特色库评论开关
			Long rmstRfilId = ServletRequestUtils.getLongParameter(request, "fileServiceId", 0); //文件服务器ID
			Long rmstIsUploadResource = ServletRequestUtils.getLongParameter(request, "rmstIsUploadResource",1);// 用户上传资源开关
			String rmstLogoBg = ServletRequestUtils.getStringParameter(request, "rmstLogoBg","");//特色库图片背景色
			Integer rmstResourceAuidt = ServletRequestUtils.getIntParameter(request, "resourceAuditStatu",2); //资源提交默认审核状态
			String rmstLanguage = ServletRequestUtils.getStringParameter(request, "rmstLanguage", "zh");
			
			if(rmstRfilId == 0)
				rmstRfilId = null;
			rltyIds = request.getParameterValues("rltyId");
			String url = "/console/data/resource/defineStore.htm?action=editAddMetaStore";
			map.put("url", url);
			if(!CommonUtil.isNotNull(rmstCompany))
				throw new ErrorMessageException("特色库建设单位不能为空！");
			if(rltyIds.length < 1)
				throw new ErrorMessageException("资源类型不能为空！");
			if(!CommonUtil.isNotNull(rmstName))
				throw new ErrorMessageException("特色库名称不能为空！");
			// 初始化资源类型和管理员
			if(rmstId <= 0)
				throw new ErrorMessageException("特色库ID不存在！");
			rms = (ResMetaStore)this.metadataManagementService.get(ResMetaStore.class, rmstId);
			if(rms == null)
				throw new ErrorMessageException("特色库不存在！");
			// 更新信息
			Long tempFileId = rms.getRmstRfilId();
			rms.setRmstName(rmstName);
			rms.setRmstCompany(rmstCompany);
			rms.setRmstRemark(rmstRemark);
			rms.setRmstStatus(rmstStatus);
			rms.setRmstRfilId(rmstRfilId);
			rms.setRmstIsUploadResource(rmstIsUploadResource);
			rms.setRmstIndexSort(ServletRequestUtils.getLongParameter(request, "rmstIndexNo", 0));
			
			//三种自定义分类名称设置
			rms.setRmstGroupOne(ServletRequestUtils.getStringParameter(request, "rmstGroupOne",""));
			rms.setRmstGroupTwo(ServletRequestUtils.getStringParameter(request, "rmstGroupTwo",""));
			rms.setRmstGroupThree(ServletRequestUtils.getStringParameter(request, "rmstGroupThree",""));
			rms.setRmstResourceAudit(rmstResourceAuidt);
			rms.setRmstLanguage(rmstLanguage);
			
			//上传特色库logo
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
			MultipartFile file = multiRequest.getFile("rmstLogo");
			String fielName = file.getOriginalFilename();
			if(fielName.indexOf(".")>0){
				String rmstLogos = upLoadLogo(request,file,rms.getRmstFolder());
				rms.setRmstLogo(rmstLogos);
			}
			rms.setRmstLogoBg(rmstLogoBg);
			
			if(!CommonUtil.isNotNull(rmstName))
				throw new ErrorMessageException("特色库名称不能为空！");
			// 初始化资源类型
			map = initRltySysManData(map, null);
			List resLiterTypeList = this.metadataManagementService.getResLiterTypeObejctList();
			map.put("resLiterTypeList", resLiterTypeList);
			// 服务器端验证权限类型是否为空
			int temp = 0;
			int manager_count = 0;
			List<SysManager> sysManagerList1 = this.metadataManagementService.getSysManagerObjectList();
			for(SysManager sma : sysManagerList1) {
				String[] smanRights = request.getParameterValues("riss2" + sma.getSmanId());
				if(smanRights != null && smanRights.length > 0) {
					temp++;
					// 初始化管理员
					sma.setRightsNumber(addRightsNum(smanRights));
					// 验证是否勾选了管理员
					for(String temp_ : smanRights) {
						Long rmpeType = Long.parseLong(temp_);
						if(ResMetaPerm.RMST_MANAGER.equals(rmpeType))
							manager_count++;
					}
				}
			}
			map.put("sysManagerList", sysManagerList1);
			if(temp == 0)
				throw new ErrorMessageException("权限类型不能为空！");
			if(manager_count == 0)
				throw new ErrorMessageException("特色库至少得指定一个管理员！");
			ResMetaStore tempRms = metadataManagementService.isResMetaStoreRmstNameExit(rmstName);
			if(tempRms != null && !tempRms.getRmstId().equals(rmstId))
				throw new ErrorMessageException("该特色库已经存在！");
			// ----------- 校验评论开关:begin ---------------------
			rms.setRmstCmtstus(rmstCmtstus);
			List rmstCmtStusList = this.metadataManagementService.getResSourceData(rmstId, 1L);
			if(rmstCmtstus == 0) {
				String tempIds = listToIds(rmstCmtStusList);
				if(tempIds.length() > 0) {
					boolean result = this.metadataManagementService.updateResSourceDataCmtStusByBatch(tempIds, 0L);
					if(!result)
						throw new ErrorMessageException("更新评论开关过程中出现异常情况！");
				}
			}
				
			// 当特色库评论开关被开启时，将该特色库下所有资源的评论开关也开启；
			if(rmstCmtstus == 1) {
				List rmstCmtStusList2 = this.metadataManagementService.getResSourceData(rmstId, 0L);
				String tempIds2 = listToIds(rmstCmtStusList2);
				if(tempIds2.length() > 0) {
					boolean result2 = this.metadataManagementService.updateResSourceDataCmtStusByBatch(tempIds2, 1L);
					if(!result2)
						throw new ErrorMessageException("更新评论开关过程中出现异常情况！");
				}
			}
			// ----------- 校验评论开关:end ---------------------
			// 更新数据库
			this.metadataManagementService.save(rms);
			
			// 修改特色库目录
			if(tempFileId!=null && rmstRfilId==null || tempFileId==null && rmstRfilId!=null ||tempFileId!=null && rmstRfilId!=null&&!tempFileId.equals(rmstRfilId)){
				uploadService = getUploadServiceById(rmstRfilId);
				uploadService.createDir("/" + rms.getRmstFolder());
			}
			
			// 更新资源类型
			List<Long> resStoreLiterList = this.metadataManagementService.getResStoreLiterList(rmstId);
			ResStoreLiter rsl = null;
			// 添加新增类型
			for(String ltid : rltyIds) {
				Long rltyId = Long.parseLong(ltid);
				if(resStoreLiterList.contains(rltyId))
					continue;
				else {
					rsl = new ResStoreLiter();
					rsl.setRmstId(rmstId);
					rsl.setRltyId(rltyId);
					this.metadataManagementService.save(rsl);
					// 添加新增资源类型元数据模板元素
					saveResTempletMeta(rltyId, rmstId);
				}
			}
			// 删除被取消的类型
			for(Long rsliter : resStoreLiterList) {
				if(checkIdIsInList(rltyIds, rsliter))
					continue;
				else {
					ResStoreLiter rsLiter = this.metadataManagementService.getResStoreLiter(rmstId, rsliter);
					if(rsLiter == null)
						throw new ErrorMessageException("更新特色库过程中出现异常情况！");
					this.metadataManagementService.delete(rsLiter);
					// 批量删除被取消类型的模板元素
					this.metadataManagementService.deleteResTempletMetaByBatch(rmstId, rsliter);
				}
			}
			// ----------------------------- 更新管理员 begin --------------------------------
			List<SysManager> sysManagerList = this.metadataManagementService.getSysManagerObjectList();
			for(SysManager sma : sysManagerList) {
				String[] smanRights = request.getParameterValues("riss2" + sma.getSmanId());
				List<Long> tempRightsList = this.metadataManagementService.getResMetaPermList(rmstId, null, sma.getSmanId(), 4L);
				if(smanRights != null && smanRights.length > 0) {
					// 添加新增特色库管理员
					ResMetaPerm rmp = null;
					for(String rightsId : smanRights) {
						Long tempRightsId = Long.parseLong(rightsId);
						if(tempRightsList.contains(tempRightsId))
							continue;
						else {
							rmp = new ResMetaPerm();
							rmp.setSmanId(sma.getSmanId());
							rmp.setRmstId(rmstId);
							rmp.setRmpeBuilddate(new Date());
							if(tempRightsId == 1)
								rmp.setRmpeType(ResMetaPerm.RMST_MANAGER);
							else if(tempRightsId == 2)
								rmp.setRmpeType(ResMetaPerm.RMST_DATAPROCESS);
							else
								rmp.setRmpeType(ResMetaPerm.RMST_DATACHECK);
							this.metadataManagementService.save(rmp);
						}
					}
					// 删除被取消的权限
					for(Long hasRigthsId : tempRightsList) {
						if(checkIdIsInList(smanRights, hasRigthsId))
							continue;
						else {
							List list = this.metadataManagementService.getResMetaPermList(rmstId, hasRigthsId, sma.getSmanId(), 3L);
							if(list.size() == 0)
								throw new ErrorMessageException("更新特色库过程中出现异常情况！");
							this.metadataManagementService.delete((ResMetaPerm)list.get(0));
						}
					}
				}else {
					// 删除被取消的权限
					for(Long hasRigthsId : tempRightsList) {
						List list = this.metadataManagementService.getResMetaPermList(rmstId, hasRigthsId, sma.getSmanId(), 3L);
						if(list.size() > 0)
							this.metadataManagementService.delete((ResMetaPerm)list.get(0));
					}
				}
			}
			
			//保存完成后更新缓存
			this.cacheService.updateMetastoreList();
			// ----------------------------- 更新管理员 end --------------------------------
			map.put("msg", "更新特色库成功！");
			map.put("edit", true);
		}catch(ErrorMessageException e) {
			map.put("rltyIds", arrayToList(rltyIds));
			map.put("resMetaStore", rms);
			map.put("error", e.getMessage());
		}catch(Exception ex) {
			ex.printStackTrace();
			map.put("error", "系统忙，请稍后访问！");
		}
		return new ModelAndView("resource/metadata/addMetaStore", map);
	}
	
	/*
	 * 上传特色库logo
	 */
	private String upLoadLogo(HttpServletRequest request,MultipartFile file, String localFileName){
		//System.out.println("123");
		String rmstLogo = "";
		
		
		String fileName = file.getOriginalFilename();
		String extName = "";
		InputStream inputStream;
		String metaStoreLogoFolder = SysConfigInfo.STORE_FACE_DIR;
		try {
			inputStream = file.getInputStream();
			if(ServiceUtil.isNotNull(fileName) && fileName.indexOf(".") > 0) { // 执行上传操作
				extName = fileName.substring(fileName.lastIndexOf(".") + 1); // 扩展名
				localFileName = localFileName + "." + extName;
				localUploadService.saveFile(metaStoreLogoFolder, localFileName, inputStream);
				
				ImgHandleUtil.pictureHandle(metaStoreLogoFolder+localFileName, 245, 300, false);
				rmstLogo = CommonUtil.getUrl(metaStoreLogoFolder+"/"+localFileName,localFileName);
			}
		} catch (IOException e) {
			rmstLogo = "";
		} catch (Exception e) {
			rmstLogo = "";
		}
		return rmstLogo;
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

	/**
	 * 定义特色库
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-16
	 */
	public ModelAndView saveAddMetaStore(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String[] rltyIds = null;
		ResMetaStore rms = null;
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return null;
			}
			String rmstName = ServletRequestUtils.getStringParameter(request, "rmstName", "").trim(); // 特色库名称
			String rmstCompany = ServletRequestUtils.getStringParameter(request, "rmstCompany", "").trim(); // 建设单位
			Long rmstStatus = ServletRequestUtils.getLongParameter(request, "rmstStatus", 1); // 特色库状态
			String rmstRemark = ServletRequestUtils.getStringParameter(request, "rmstRemark", "").trim(); // 描述
			Long rmstCmtstus = ServletRequestUtils.getLongParameter(request, "rmstCmtstus", 1);// 特色库评论开关
			Long rmstRfilId = ServletRequestUtils.getLongParameter(request, "fileServiceId", 0); //文件服务器ID
			Long rmstIsUploadResource = ServletRequestUtils.getLongParameter(request, "rmstIsUploadResource",1);// 用户上传资源开关
			Integer rmstResourceAuidt = ServletRequestUtils.getIntParameter(request, "resourceAuditStatu",2); //资源提交默认审核状态
			String rmstLogoBg = ServletRequestUtils.getStringParameter(request, "rmstLogoBg","");//特色库图片背景色
			String rmstLanguage = ServletRequestUtils.getStringParameter(request, "rmstLanguage", "zh");
			
			
			if(rmstRfilId == 0)
				rmstRfilId = null;
			rltyIds = request.getParameterValues("rltyId");
			String url = "/console/data/resource/defineStore.htm?action=saveAddMetaStore";
			map.put("url", url);
			if(!CommonUtil.isNotNull(rmstCompany))
				throw new ErrorMessageException("特色库建设单位不能为空！");
			if(rltyIds.length < 1)
				throw new ErrorMessageException("资源类型不能为空！");
			if(!CommonUtil.isNotNull(rmstName))
				throw new ErrorMessageException("特色库名称不能为空！");
			rms = new ResMetaStore();
			rms.setRmstName(rmstName);
			rms.setRmstCompany(rmstCompany);
			rms.setRmstRemark(rmstRemark);
			rms.setRmstStatus(rmstStatus);
			rms.setRmstIndexSort(ServletRequestUtils.getLongParameter(request, "rmstIndexNo",1));
			
			//三种自定义分类名称设置
			rms.setRmstGroupOne(ServletRequestUtils.getStringParameter(request, "rmstGroupOne",""));
			rms.setRmstGroupTwo(ServletRequestUtils.getStringParameter(request, "rmstGroupTwo",""));
			rms.setRmstGroupThree(ServletRequestUtils.getStringParameter(request, "rmstGroupThree",""));
			
			// 保存特色库评论开关值
			rms.setRmstCmtstus(rmstCmtstus);
			rms.setRmstRfilId(rmstRfilId);
			// 保存用户上传资源开关值
			rms.setRmstIsUploadResource(rmstIsUploadResource);
			rms.setRmstResourceAudit(rmstResourceAuidt);
			rms.setRmstLanguage(rmstLanguage);
			
			// 初始化资源类型
			map = initRltySysManData(map, null);
			List resLiterTypeList = this.metadataManagementService.getResLiterTypeObejctList();
			map.put("resLiterTypeList", resLiterTypeList);
			// 服务器端验证权限类型是否为空
			int temp = 0;
			int manager_count = 0;
			List<SysManager> sysManagerList1 = this.metadataManagementService.getSysManagerObjectList();
			for(SysManager sma : sysManagerList1) {
				String[] smanRights = request.getParameterValues("riss2" + sma.getSmanId());
				if(smanRights != null && smanRights.length > 0) {
					temp++;
					// 初始化管理员
					sma.setRightsNumber(addRightsNum(smanRights));
					// 验证是否勾选了管理员
					for(String temp_ : smanRights) {
						Long rmpeType = Long.parseLong(temp_);
						if(ResMetaPerm.RMST_MANAGER.equals(rmpeType))
							manager_count++;
					}
				}
			}
			map.put("sysManagerList", sysManagerList1);
			if(temp == 0)
				throw new ErrorMessageException("权限类型不能为空！");
			if(manager_count == 0)
				throw new ErrorMessageException("特色库至少得指定一个管理员！");
			if(metadataManagementService.isResMetaStoreRmstNameExit(rmstName) != null)
				throw new ErrorMessageException("该特色库已经存在！");
			// 保存特色库
			Date date = new Date();
			rms.setRmstBuilddate(date);
			rms.setSmanId(sysManager.getSmanId());
			rms.setRmstFolder(date.getTime() + "");
			
			//上传特色库logo
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
			MultipartFile file = multiRequest.getFile("rmstLogo");
			
			String rmstLogo = upLoadLogo(request,file,rms.getRmstFolder());
			rms.setRmstLogo(rmstLogo);
			// 特色库图片背景色
			if(CommonUtil.isNotNull(rmstLogoBg)){
				rms.setRmstLogoBg(rmstLogoBg);
			}
			
//			this.metadataManagementService.save(rms);
			// 增加特色库的同时给用户增加RSS订阅
			// author puzhijun
			// date 2010-12-23
			final String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
			this.metadataManagementService.addMetaStore(basePath, rms);
			// 创建特色库目录
			uploadService = getUploadServiceById(rmstRfilId);
			uploadService.createDir("/" + SysConfigInfo.RES_DIR + rms.getRmstFolder());
			// 保存特色库资源类型
			ResStoreLiter rsl = null;
			for(String ltid : rltyIds) {
				Long rltyId = Long.parseLong(ltid);
				rsl = new ResStoreLiter();
				rsl.setRmstId(rms.getRmstId());
				rsl.setRltyId(rltyId);
				this.metadataManagementService.save(rsl);
				// 保存元数据模板元素
				saveResTempletMeta(rltyId, rms.getRmstId());
			}
			// ----------------------- 保存权限 begin -------------------------------------------
			List<SysManager> sysManagerList = this.metadataManagementService.getSysManagerObjectList();
			for(SysManager sma : sysManagerList) {
				String[] smanRights = request.getParameterValues("riss2" + sma.getSmanId());
				if(smanRights != null && smanRights.length > 0) {
					ResMetaPerm rmp = null;
					for(int i = 0; i < smanRights.length; i++) {
						Long rightId = Long.parseLong(smanRights[i]);
						rmp = new ResMetaPerm();
						rmp.setSmanId(sma.getSmanId());
						rmp.setRmstId(rms.getRmstId());
						rmp.setRmpeBuilddate(date);
						if(rightId == 1)
							rmp.setRmpeType(ResMetaPerm.RMST_MANAGER);
						else if(rightId == 2)
							rmp.setRmpeType(ResMetaPerm.RMST_DATAPROCESS);
						else
							rmp.setRmpeType(ResMetaPerm.RMST_DATACHECK);
						this.metadataManagementService.save(rmp);
					}
				}
			}
			
			//保存完成后更新缓存
			this.cacheService.updateMetastoreList();
			// ----------------------- 保存权限 over -------------------------------------------
			map.put("msg", "特色库定义成功！");
			map.put("save", true);
		}catch(ErrorMessageException e) {
			// 构造初始化信息
			map.put("resMetaStore", rms);
			map.put("rltyIds", arrayToList(rltyIds));
			map.put("error", e.getMessage());
		}catch(Exception ex) {
			ex.printStackTrace();
			map.put("error", "系统忙，请稍后访问！");
		}
		return new ModelAndView("resource/metadata/addMetaStore", map);
	}

	/**
	 * 获取得特色库列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getResMetaStoreList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int rmpType = ServletRequestUtils.getIntParameter(request, "rmpType", 1);
		long userid = MyRequestUtil.getLoginUserId(request);
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		Map<String, Object> conditions = new HashMap<String, Object>();
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rmstBuilddate");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		conditions.put("dir", dir);
		List list = this.metadataManagementService.getResMetaStoreList(pagination, conditions, rmpType, userid);
		String result = CommonUtil.createJsonTable(list, new String[] {"rmstId", "smanName", "rmstName", "rmstCompany", "rmstBuilddate",
				"rmstStatus","rmstFolder","rmstIndexSort","rmstGroupOne","rmstGroupTwo","rmstGroupThree", "rmstLanguage"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	/**
	 * 获取特色库信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-2
	 */
	public void getResMetaStore(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return;
			}
			Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0); // 特色库状态
			if(rmstId <= 0)
				throw new ErrorMessageException("特色库ID不能为空！");
			ResMetaStore rms = (ResMetaStore)this.metadataManagementService.get(ResMetaStore.class, rmstId);
			if(rms == null)
				throw new ErrorMessageException("特色库不存在！");
			List resStoreLiterList = this.metadataManagementService.getResStoreLiterList(rmstId);
			List resMetaPermList = this.metadataManagementService.getResMetaPermList(rmstId, null, null, new Long(1));
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("resStoreLiterIds", changeListToString(resStoreLiterList));
			result.put("resMetaPermIds", changeListToString(resMetaPermList));
			result.put("rmstId", rms.getRmstId());
			result.put("rmstName", rms.getRmstName());
			result.put("rmstCompany", rms.getRmstCompany());
			result.put("rmstRemark", rms.getRmstRemark());
			result.put("rmstStatus", rms.getRmstStatus());
			result.put("success", true);
			String json = JSONObject.fromObject(result).toString();
			out.write(json);
		}catch(ErrorMessageException e) {
			logger.error(e.getMessage());
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}

	/**
	 * 删除特色库
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-6
	 */
	public void delResMetaStore(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return;
			}
			Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);
			if(rmstId <= 0)
				throw new ErrorMessageException("特色库ID不能为空！");
			ResMetaStore rms = (ResMetaStore)this.metadataManagementService.get(ResMetaStore.class, rmstId);
			if(rms == null)
				throw new ErrorMessageException("特色库不存在！");
			// 连同用户的RSS订阅资源一起删除
			final String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
			boolean result = this.metadataManagementService.deleteResMetaStore(basePath, rmstId);
//			boolean result = this.metadataManagementService.deleteResMetaStore(rmstId);
			if(result) {
				uploadService = getUploadServiceById(rms.getRmstRfilId());
				uploadService.deleteFileOrDir("/" + SysConfigInfo.RES_DIR + rms.getRmstFolder());
				
				//保存完成后更新缓存
				this.cacheService.updateMetastoreList();
				out.write("{success:true,msg:'特色库删除成功！'}");
			}else
				out.write("{success:false,msg:'该特色库下已经有数据了，不允许删除！'}");
		}catch(ErrorMessageException e) {
			logger.error("删除特色库失败", e);
			out.write("{success:false,msg:'" + e.getMessage() + "'}");
		}catch(Exception ex) {
			logger.error("删除特色库失败", ex);
			out.write("{success:false,msg:'系统忙，请稍后访问！'}");
		}
		out.flush();
	}

	/**
	 * 构造树
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-6
	 */
	public void createRMSTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		StringBuilder json = new StringBuilder();
		try {
			Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);
			String nodeId = ServletRequestUtils.getStringParameter(request, "node");
			ResMetaStore rms = (ResMetaStore)this.metadataManagementService.get(ResMetaStore.class, rmstId);
			if(rms != null) {
				if(nodeId == null || nodeId.length() == 0 || nodeId.equals("tge-root")) {
					nodeId = "0";
				}
				json.append("[");
				if("0".equals(nodeId))
					json.append(createJsonTree(rmstId, new Long(1), null));
				else
					json.append(createJsonTree(rmstId, new Long(2), Long.parseLong(nodeId)));
				json.append("]");
				out.write(json.toString());
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		out.flush();
	}

	/**
	 * 处理自定义分类:增、删、改
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-28
	 */
	public void handlerRMSTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		try {
			Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);
			String data = ServletRequestUtils.getStringParameter(request, "data", "");
			String method = ServletRequestUtils.getStringParameter(request, "method", "");
			TempObject tempObject = (TempObject)CommonUtil.jsonToObject(data, TempObject.class);
			ResDynamicType rdt = null;
			if(method.equalsIgnoreCase("add")) {
				// 保存特色库自定义分类根目录
				rdt = new ResDynamicType();
				rdt.setRdtyName(tempObject.getCity());
				rdt.setRmstId(rmstId);
				if("tge-root".equals(tempObject.getParentId())) {
					rdt.setRdtyGrade(1L);
					rdt.setRdtyPid(null);
				}else {
					Long pid = Long.parseLong(tempObject.getParentId());
					if(pid != null) {
						ResDynamicType temp = (ResDynamicType)this.metadataManagementService.get(ResDynamicType.class, pid);
						if(temp != null) {
							rdt.setRdtyPid(pid);
							rdt.setRdtyGrade(temp.getRdtyGrade() + 1);
						}
					}
				}
				this.metadataManagementService.save(rdt);
				// 更新页面上的ID值
				out.print(rdt.getRdtyId());
			}else if(method.equalsIgnoreCase("update")) {
				ResDynamicType temp = (ResDynamicType)this.metadataManagementService.get(ResDynamicType.class, tempObject.getId());
				if(temp != null) {
					temp.setRdtyName(tempObject.getCity());
					this.metadataManagementService.save(temp);
				}
			}else if(method.equalsIgnoreCase("delete")) {
				ResDynamicType temp = (ResDynamicType)this.metadataManagementService.get(ResDynamicType.class, tempObject.getId());
				if(temp != null)
					this.metadataManagementService.delete(temp);
			}
		}catch(Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
		}
		out.flush();
	}

	/**
	 * 初始化自定义分类
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-25
	 */
	public ModelAndView initDefineGroupView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);  //特色库ID
		int rdtyType = ServletRequestUtils.getIntParameter(request, "rdtyType", 0);
		map.put("rmstId", rmstId);
		map.put("rdtyType", rdtyType);
		return new ModelAndView("resource/metadata/autoGroupframe", map);
//		if(rdtyType == 3){
//			return new ModelAndView("resource/metadata/autoGroupframe", map);
//		}
//		if(rmstId > 0) {
//			List rdtGrade1 = this.metadataManagementService.getResDynamicTypeList(null, rmstId, null, 1L, 0, null, rdtyType);
//			List rdtGrade2 = this.metadataManagementService.getResDynamicTypeList(null, rmstId, null, 2L, 0, null, rdtyType);
//			List rdtGrade3 = this.metadataManagementService.getResDynamicTypeList(null, rmstId, null, 3L, 0, null, rdtyType);
//			map.put("rdtGrade1", rdtGrade1);
//			map.put("rdtGrade2", rdtGrade2);
//			map.put("rdtGrade3", rdtGrade3);
//		}
//		if(rdtyType == 1){
//			map.put("rdtyTypeName", ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE));
//		}
//		if(rdtyType == 2){
//			map.put("rdtyTypeName", ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO));
//		}
//		if(rdtyType == 3){
//			map.put("rdtyTypeName", ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE));
//		}
//		return new ModelAndView("resource/metadata/initDefineGroup", map);
	}

	private String changeListToString(List list) {
		if(list == null || list.size() < 1)
			return "";
		String result = "";
		for(int i = 0; i < list.size(); i++) {
			result += list.get(i);
			if(i != list.size() - 1)
				result += "|";
		}
		return result;
	}

	private boolean checkIdIsInList(String[] str, Long id) {
		if(str.length < 1 || id == null)
			throw new ErrorMessageException("更新特色库过程中出现异常情况！");
		for(String temp : str) {
			Long tempId = Long.parseLong(temp);
			if(tempId.equals(id))
				return true;
		}
		return false;
	}

	// 保存元数据模板元素
	private void saveResTempletMeta(Long rltyId, Long rmstId) {
		ResTempletMeta rtme = null;
		// 获取当前资源类型对应的元素
		List<Long> repoIdList = this.metadataManagementService.getResElementPoolList(rltyId);
		for(Long repoId : repoIdList) {
			rtme = new ResTempletMeta();
			rtme.setRtmeRsinId(rmstId);
			rtme.setRtmeRltyId(rltyId);
			rtme.setRtmeRepoId(repoId);
			this.metadataManagementService.save(rtme);
		}
	}

	// 递归构造树
	private String createJsonTree(Long rmstId, Long rdtyGrade, Long rdtyPid) {
		StringBuilder json = new StringBuilder();
		List list = null;
		if(rdtyGrade == 1)
			list = this.metadataManagementService.getResDynamicTypeList(null, rmstId, null, null, new Long(1));
		else {
			ResDynamicType rdt = (ResDynamicType)this.metadataManagementService.get(ResDynamicType.class, rdtyPid);
			if(rdt != null) {
				list = this.metadataManagementService.getResDynamicTypeList(null, rmstId, rdtyPid, null, rdt.getRdtyGrade() + 1);
			}
		}
		for(int i = 0; i < list.size(); i++) {
			json.append("{city: '").append(((Object[])list.get(i))[1]).append("',id:").append(((Object[])list.get(i))[0]);
			Object[] obj = (Object[])list.get(i);
			List tempList = this.metadataManagementService.getResDynamicTypeList(null, rmstId, (Long)obj[0], null, (Long)obj[2] + 1);
			if(tempList.size() > 0)
				json.append(",leaf: false");
			else
				json.append(",leaf: true");
			// expanded：true 加载所有的子节点
			json.append(",expanded: true,checked:false");
			json.append("}");
			if(i != list.size() - 1) {
				json.append(",");
			}
		}
		return json.toString();
	}

	private List arrayToList(String[] arrays) {
		if(arrays.length < 1)
			return null;
		List list = new ArrayList();
		for(String arr : arrays) {
			list.add(Long.parseLong(arr));
		}
		return list;
	}

	private Map initRltySysManData(Map<String, Object> map, Long rmstId) {
		List resLiterTypeList = this.metadataManagementService.getResLiterTypeObejctList();
		List<SysManager> sysManagerList = this.metadataManagementService.getSysManagerObjectList();
		map.put("resLiterTypeList", resLiterTypeList);
		if(rmstId != null) {
			for(SysManager sma : sysManagerList) {
				List<ResMetaPerm> rmpList = this.metadataManagementService.getResMetaPermList(rmstId, null, sma.getSmanId(), 2L);
				sma.setRightsNumber(addRightsNum(rmpList));
			}
		}
		map.put("sysManagerList", sysManagerList);
		return map;
	}

	/**
	 * 根据权限类型获取用户所拥有的权限值:
	 * 
	 * @param list
	 * @return
	 */
	private Long addRightsNum(List<ResMetaPerm> list) {
		Long result = 0L;
		if(list.size() == 0)
			return result;
		for(ResMetaPerm rmp : list) {
			if(ResMetaPerm.RMST_MANAGER.equals(rmp.getRmpeType()))
				result += 1;
			else if(ResMetaPerm.RMST_DATAPROCESS.equals(rmp.getRmpeType()))
				result += 2;
			else
				result += 4;
		}
		return result;
	}

	private Long addRightsNum(String[] args) {
		Long result = 0L;
		if(args.length == 0)
			return result;
		for(String temp : args) {
			Long rmpeType = Long.parseLong(temp);
			if(ResMetaPerm.RMST_MANAGER.equals(rmpeType))
				result += 1;
			else if(ResMetaPerm.RMST_DATAPROCESS.equals(rmpeType))
				result += 2;
			else
				result += 4;
		}
		return result;
	}

	private String listToIds(List list) {
		String result = "";
		if(list == null)
			return result;
		for(int i = 0, t = list.size(); i < t; i++) {
			result += list.get(i).toString();
			if(i < t - 1)
				result += ",";
		}
		return result;
	}
	
	
	/**
	 * 文件服务器下拉选项Json
	 * 王林  2010-11-10
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getFileServiceOptionJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		String result = fileService.getAllFileServiceOptionJson();
		out.write(result);
		out.flush();
	}
	
	public IUploadService getUploadServiceById(Long rfilId){
		if(rfilId == null || rfilId.equals(0)){
			return localUploadService;
		}else{
			return remoteUploadService;
		}
	}
	
	public ModelAndView groupTree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId");
		int rdtyType = ServletRequestUtils.getIntParameter(request, "rdtyType", 0);
		List<ResDynamicType> groupList = this.dynamicTypeService.getDynamicTypeList(rmstId, rdtyType);
		ResMetaStore metaStore = (ResMetaStore) this.dynamicTypeService.get(ResMetaStore.class, rmstId);
		
		String groupName = "";
		if(rdtyType == 1){
			groupName = metaStore.getRmstGroupOne();
		} else if (rdtyType == 2) {
			groupName = metaStore.getRmstGroupTwo();
		} else if (rdtyType == 3) {
			groupName = metaStore.getRmstGroupThree();
		}
		request.setAttribute("groupList", groupList);
		request.setAttribute("rmstId", rmstId);
		request.setAttribute("groupName", groupName);
		request.setAttribute("rdtyType", rdtyType);
		return new ModelAndView("resource/metadata/groupTree");
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView groupInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long rdtyId = ServletRequestUtils.getLongParameter(request, "rdtyId",0);
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId",0);
		int rdtyType = ServletRequestUtils.getIntParameter(request, "rdtyType", 0);
		if(rmstId == null || rmstId < 1){
			request.setAttribute("error", "特色库ID无效！");
			return new ModelAndView("resource/metadata/groupInfo");
		}
		String options = "";
		ResDynamicType dynamic = null;
		if(rdtyId == null || rdtyId < 1){
			options = dynamicTypeService.getDynamicOptions(rmstId, 0L, rdtyType);
		}else{
			dynamic = (ResDynamicType) resourceService.get(ResDynamicType.class, rdtyId);
			options = dynamicTypeService.getDynamicOptions(rmstId, dynamic.getRdtyPid() == null ? 0 : dynamic.getRdtyPid(), rdtyType);
		}
		
		if(CommonUtil.isNotNull(options)){
			options = options.replace("请选择...", "无");
		}else{
			options = "<option value='0'>无</option>";
		}
		
		request.setAttribute("options", options);
		request.setAttribute("dynamic", dynamic);
		request.setAttribute("rmstId", rmstId);
		request.setAttribute("rdtyType", rdtyType);
		return new ModelAndView("resource/metadata/groupInfo");
	}
	
	
}
