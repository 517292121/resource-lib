package cn.com.adtech.riss2.controller.console.resource.build;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.com.adtech.riss2.bso.cache.IPlatformCacheService;
import cn.com.adtech.riss2.bso.resource.IResourceLuceneService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.controller.platform.ResCommonController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.LanguageUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.file.MyFileUtil;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.domain.resource.ResSourceSubject;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.jdbcms.ToolUnit;
import cn.com.adtech.riss2.jsonrpc.ResourceRpc;
import cn.com.adtech.riss2.upload.IUploadService;
import cn.com.adtech.riss2.wsClient.FileConvertServiceUtil;

import com.jbetter.framework.session.Pagination;

public class InputSingleController extends ResourceFileLoad {
	// ///////////////////////////////////////////////////资源录入相关
	
	private IResourceLuceneService resourceLuceneService = null;
	public void setResourceLuceneService(
			IResourceLuceneService resourceLuceneService) {
		this.resourceLuceneService = resourceLuceneService;
	}
	
	private IPlatformCacheService cacheService;
	public void setCacheService(IPlatformCacheService cacheService) {
		this.cacheService = cacheService;
	}
	
	/**
	 * 初始化资源录入界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	private int pageshow = 0;
	public ModelAndView initData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long store = MyRequestUtil.getLongParameter(request, "store");
		long sourceType = MyRequestUtil.getLongParameter(request, "stype");
		long rmin_id = MyRequestUtil.getLongParameter(request, "rmin_id");
		ResMissionInfo mission = missionService.getResMissionInfoById(rmin_id);
		if(sourceType == 0 && store == 0) { // 如果没有传资源类型ID和特色库ID过来，则根据作业再去查询一次
			store = mission.getRminRmstId();
			sourceType = mission.getRminRltyId();
		}
		List<ResElementPool> elementList = elementPool.getElementByTemplet(store, sourceType);
		ResLanguageType language = lanService.getResLanguage(mission.getRminLanugage());
		String missName = missionService.getMissionNameById(rmin_id);
		String subjectOptions = resourceService.getSubjectOptions(-1L, 0L, "1"); // 学科选项
//		String dynamicTypeOptions = resourceService.getDynamicTypeOptions(store, -1, 1); //自定义分类一
//		String dynamicTypeOptions2 = resourceService.getDynamicTypeOptions(store, -1, 2); //自定义分类一
//		String dynamicTypeOptions3 = resourceService.getDynamicTypeOptions(store, -1, 3); //自定义分类一
		
		String dynamicTypeOptions = resourceService.getDynamicTypeOptions(store, -1, 1); //自定义分类一
		String dynamicTypeOptions2 = resourceService.getDynamicTypeOptions(store, -1, 2); //自定义分类一
		String dynamicTypeOptions3 = dynamicTypeService.getDynamicOptions(store, 0L, 3);
		
		//主题下拉选项
		String sourceSubjectOptions = resourceService.getSourceSubjectOptions(rmin_id,-1);
		
		//会员类型下拉
		String sysUserTypeOptions = resourceService.getSourceRightsOptions(-1);
		
		String hql = "select rmst.rmstGroupOne,rmst.rmstGroupTwo,rmst.rmstGroupThree from ResMetaStore rmst where rmst.rmstId=" + store; 
		List<Object[]> list = (List<Object[]>)elementPool.find(hql);
		Object[] groupData =  list.get(0);
		
		//String fileServiceOption = fileService.getAllFileServiceOption(0L);
		
		String libLan = "";
		if(mission != null && mission.getResMetaStore() != null && mission.getResMetaStore().getRmstLanguage() != null){
			String lan = mission.getResMetaStore().getRmstLanguage();
			libLan = LanguageUtil.codeToTxt(lan);
		}
		
		request.setAttribute("elementlist", elementList);
		request.setAttribute("store", store);
		request.setAttribute("groupData", groupData);
		request.setAttribute("libLan", libLan);
		request.setAttribute("stype", sourceType);
		request.setAttribute("rmin_id", rmin_id);
		request.setAttribute("language", language);
		request.setAttribute("subjectOptions", subjectOptions);
		request.setAttribute("dynamicTypeOptions", dynamicTypeOptions);
		request.setAttribute("dynamicTypeOptions2", dynamicTypeOptions2);
		request.setAttribute("dynamicTypeOptions3", dynamicTypeOptions3);
		request.setAttribute("missName", missName);
		request.setAttribute("sourceSubjectOptions", sourceSubjectOptions);
		request.setAttribute("sysUserTypeOptions", sysUserTypeOptions);
		//request.setAttribute("fileServiceOption", fileServiceOption);
		return new ModelAndView("/resource/build/input_single");
	}
	
	/**
	 * 初始化主题界面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initSubject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rmin_id = MyRequestUtil.getLongParameter(request, "rmin_id");
		request.setAttribute("rminId",rmin_id);
		return new ModelAndView("/resource/build/resourceSubject");
	}
	
	/**
	 * 获取主题分页数据
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	public void subjectListJsonByMission(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		
		//排序相关
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rssu_Id");// 排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");// 排序方式
		
		// 查询条件
		long rssuRminId = ServletRequestUtils.getLongParameter(request, "rminId",0);
		
		//获取结果数据
		String jsondata = resourceService.getResourceSubjectJson(start,limit, rssuRminId, sort, dir);
		
		//打印结果数据
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 删除主题
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void deleteSubject(HttpServletRequest request,HttpServletResponse response) throws IOException{
		long rssuId = ServletRequestUtils.getLongParameter(request, "rssuId", 0);
		
		
		boolean b = resourceService.deleteSubject(rssuId);
		
		PrintWriter out = response.getWriter();
		if(!b){
			out.print("0");
		}else{
			out.print("1");
		}
		out.flush();
	}

	/**
	 * 新增或修改主题
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void saveOrUpdateSubject(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long rssuId =ServletRequestUtils.getLongParameter(request, "rssuId", 0); 
		long rssuRminId = ServletRequestUtils.getLongParameter(request, "rminId",0);
		String rssuName = ServletRequestUtils.getStringParameter(request, "rssuName","");
		String rssuRemark = ServletRequestUtils.getStringParameter(request, "rssuRemark","");
		ResSourceSubject rssu = new ResSourceSubject();
		rssu.setRssuId(rssuId);
		rssu.setRssuName(rssuName);
		rssu.setRssuRemark(rssuRemark);
		rssu.setRssuRminId(rssuRminId);
		
		
		boolean b = resourceService.saveOrUpdateSubject(rssu);
		
		//打印结果数据
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		if(b){
			out.print("{success:true,msg:'" + rssuName + " 保存成功!'}");
		}else{
			out.print("{success:false,msg:'" + rssuName + " 保存失败!'}");
		}
		out.flush();
	}

	/**
	 * 保存资源
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void saveResourceData(HttpServletRequest request, HttpServletResponse response, Pagination pagination) throws Exception {
		PrintWriter out = response.getWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		
		errorMsg = "";
		long store = MyRequestUtil.getLongParameter(request, "store"); // 特色库ID
		long sourceType = MyRequestUtil.getLongParameter(request, "stype"); // 资源类型ID
		long rminId = MyRequestUtil.getLongParameter(request, "rmin_id"); // 作业ID
		int reqType = MyRequestUtil.getIntParameter(request, "req");
		long memberId = ServletRequestUtils.getLongParameter(request, "memberId", 0L); //会员ID
		Long rsdaRssuId = ServletRequestUtils.getLongParameter(request, "rsdaRssuId",0L);
		try {
			ResMissionInfo mission = missionService.getResMissionInfoById(rminId);
			ResMetaStore rmst = null;
			if(mission == null){
				mission = new ResMissionInfo();
				rmst = (ResMetaStore) resourceService.get(ResMetaStore.class, store);
			}else{
				rmst = mission.getResMetaStore();
			}
			if(rmst == null){
				throw new ErrorMessageException("数据资源库不存在或已被删除");
			}
			// 获取该资源所属模板的元素集合
			List<ResElementPool> elementList = elementPool.getElementByTemplet(store, sourceType);
			if(reqType == 3) { //如果是预览资源
//				long rsty_id = ServletRequestUtils.getLongParameter(request, "rsty_id", 0L); // 学科分类
//				long rdty_id = ServletRequestUtils.getLongParameter(request, "rdty_id", 0L); // 学科分类
//				String rstyName = "";
//				String rdtyName = "";
//				if(rsty_id > 0){
//					String sql = "select RSTY_NAME from RES_SUBJECT_TYPE where RSTY_ID=" + rsty_id;
//					rstyName = resourceService.getFirstData(sql);
//				}if(rdty_id > 0){
//					String sql = "select RDTY_NAME from RES_DYNAMIC_TYPE where RDTY_ID=" + rdty_id;
//					rdtyName = resourceService.getFirstData(sql);
//				}
//	//			request.setAttribute("elementList", elementList);
//	//			request.setAttribute("requestObj", request);
//	//			request.setAttribute("rstyName", rstyName);
//	//			request.setAttribute("rdtyName", rdtyName);
//	//			return new ModelAndView("/resource/build/resourcePreview");
//				response.sendRedirect(request.getContextPath()+"/resource/build/resourcePreview");
			}else {
				boolean checkResult = checkForm(elementList, request);
				
				if(checkResult == false) { // 如果校验失败，显示出相关错误信息，并停止保存操作
//					request.setAttribute("errorMsg", errorMsg);
//					response.setContentType("text/html; charset=UTF-8");
//					PrintWriter out = response.getWriter();
//					out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
//					out.print("<span style='font-size:12px;'>" + errorMsg + "<br>");
//					out.print("<br><a href='javascript:window.history.go(-1);'>返回重新编辑</a></span></span>");
//					return null;
					throw new ErrorMessageException(errorMsg);
				}else{
					String storeFolder = resourceService.getStoreFolder(store); // 获取特色库文件存放盘符
					// 封装基本信息
					ResSourceData resource = new ResSourceData();
					resource = ParseResourceInfo(request, resource);
					resource.setRsdaStatus(2L); // 状态，默认为2
					resource.setRsdaGrade(3L); // 评分，默认为3
					resource.setRsdaCmtstus(rmst.getRmstCmtstus()); // 新建资源时，资源评论开关与特色库保持一致；
					resource.setRsdaRcmd(0L); // 是否为推荐资源，默认为0(不是)
					resource.setRsdaIsuse(1L); // 是否可用，默认为1（可用）
					resource.setRsdaLooknum(1L); // 浏览次数
					resource.setRsdaRminStatus(mission.getRminStatus());
					resource.setRsdaRmstStatus(rmst.getRmstStatus());
					resource.setRsdaBuilddate(new Date());
					if(rsdaRssuId<1){
						resource.setRsdaRssuId(null);
					}else{
						resource.setRsdaRssuId(rsdaRssuId);
					}
					if(memberId < 1){
						resource.setRsdaUserId(null);
						resource.setRsdaAuthortype(ResSourceData.AUTHORTYPE_ADMIN_INPUT);
					}else{
						resource.setRsdaUserId(memberId);
						resource.setRsdaAuthortype(ResSourceData.AUTHORTYPE_MEMBER_INPUT);
					}
					
					// 封装元素信息
					resource = ParseResourceMetadata(resource, rmst.getRmstLanguage(), request, elementList);
					
					// ---根据文件类型做不同的文件处理
					int fileType = ServletRequestUtils.getIntParameter(request, "resFileType", 0);
					if(fileType == 0){
						ResFilesystem fileService = this.fileService.getFilesystem(rmst.getRmstRfilId()); // 文件服务器对象
						if(fileService == null) {
							fileService = new ResFilesystem();
						}
						uploadService = getUploadServiceById(rmst.getRmstRfilId());
						uploadService.setFileServiceBean(fileService);
						
						// 文件相对路径
						if(mission.getRminMissname() == null || mission.getRminMissname().length() < 1){
							mission.setRminMissname(uploadService.createFolder());
						}
						String filePath = SysConfigInfo.RES_DIR + storeFolder + "/" + mission.getRminMissname() + "/";
						// 当前使用的物理文件名的前部的定义
						String localFileNamePre = System.currentTimeMillis() + "";
						// 如果资源封面的状态发生了变化，则重新上传封面
						// 当前使用的物理文件名的前部的定义
						Boolean hasMustImg = loadImageFile(resource, filePath, localFileNamePre, request);
//						Boolean hasMustView = loadViewFile(resource, filePath, localFileNamePre, request);	//由于基本不会使用预览文件，暂时取消该文件的处理
						Boolean hasMustSource = loadSourceFile(resource, filePath, localFileNamePre, request);
						// 调用远程方法执行转换
						if(hasMustSource) {
							String fileLocalName = resource.getRsdaFileLocalName();
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
//						if(hasMustView) {
//							if(hasLoadFile("viewfile", request)) {// 如果上传了预览文件则生成
//								String fileLocalName = resource.getRsdaViewfileLocalName();
//								if(ConstantsObj.FileConvertName.TYPE_AUDIO.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
//									FileConvertServiceUtil.audioConvert(fileLocalName, changeSuffix(fileLocalName,
//											ConstantsObj.FileConvertName.EXT_MP3_NAME), fileService.getRfilAddress());
//								}
//								if(ConstantsObj.FileConvertName.TYPE_DOCUMENT.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
//									FileConvertServiceUtil.documentConvert(fileLocalName, changeSuffix(fileLocalName,
//											ConstantsObj.FileConvertName.EXT_SWF_NAME), fileService.getRfilAddress());
//								}
//								if(ConstantsObj.FileConvertName.TYPE_VIDEO.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
//									FileConvertServiceUtil.videoConvert(fileLocalName, changeSuffix(fileLocalName,
//											ConstantsObj.FileConvertName.EXT_FLV_NAME), "", fileService.getRfilAddress());
//								}
//							}else {// 如果视频文件没上传预览文件则用源文件生成
//								String fileLocalName = resource.getRsdaFileLocalName();
//								if(ConstantsObj.FileConvertName.TYPE_VIDEO.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
//									String inputFileName = fileLocalName.substring(0, fileLocalName.lastIndexOf("_"))
//									+ ConstantsObj.FileConvertName.NAME_DATA + "." + ConstantsObj.FileConvertName.EXT_FLV_NAME;
//									String outputFileName = fileLocalName.substring(0, fileLocalName.lastIndexOf("_"))
//									+ ConstantsObj.FileConvertName.NAME_PRE + "." + ConstantsObj.FileConvertName.EXT_FLV_NAME;
//									FileConvertServiceUtil.videoConvert(inputFileName, outputFileName, ConstantsObj.FileConvertName.VIEW_FILE_TIME,
//											fileService.getRfilAddress());
//								}
//							}
//						}
						if(hasMustImg) {// 只有视频文件才可生成封面，如果没有源文件则采用预览文件生成封面
							String fileLocalName = "";
							if(hasLoadFile("loadfile", request)) {
								fileLocalName = resource.getRsdaFileLocalName();
							}
							if(ConstantsObj.FileConvertName.TYPE_VIDEO.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
								String outputFileName = fileLocalName.substring(0, fileLocalName.lastIndexOf("_")) + ConstantsObj.FileConvertName.NAME_IMG
								+ "." + ConstantsObj.FileConvertName.EXT_JPEG_NAME;
								FileConvertServiceUtil.photoConvertFromVideo(fileLocalName, outputFileName, ConstantsObj.FileConvertName.PHOTO_CUT_TIME,
										fileService.getRfilAddress());
							}
						}
					}else if(fileType == 1){
						String imgDir = ServletRequestUtils.getStringParameter(request, "imgDir", "");
						resource.setRsdaFileUrl(imgDir);

						//对目录中的图片创建缩略图和切片
						MyFileUtil.imgSplit(imgDir);
					}else if(fileType == 2){
						String htmlDir = ServletRequestUtils.getStringParameter(request, "htmlDir", "");
						resource.setRsdaFileUrl(htmlDir);
					}
					long sourceid = this.missionService.saveResource(resource, elementList, request);
					
					if(sourceid > 0) {
						map.put("success", true);
						map.put("msg", "保存成功");
					}else {
						map.put("success", false);
						map.put("msg", "保存失败");
					}
				}
			}
		
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		}finally{
			mustGenerateImg = false;
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}

	// //////////////////////////////////////////////////////////////资源列表相关
	/**
	 * 进入某作业下资源列表
	 */
	public ModelAndView missionResourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rminId = MyRequestUtil.getLongParameter(request, "rmin_id");
		int pagecount = ServletRequestUtils.getIntParameter(request, "pagecount", 0);
		String  searchtitle= ServletRequestUtils.getStringParameter(request, "searchtitle","");
		ResMissionInfo mission = missionService.getResMissionInfoById(rminId);
		
		request.setAttribute("rminId", rminId);
		request.setAttribute("mission", mission);
		request.setAttribute("pagecount", pagecount);
		request.setAttribute("searchtitle", searchtitle);
		return new ModelAndView("resource/build/missionResourceList");
	}

	/**
	 * 获取资源列表JSON数据
	 */
	public ModelAndView resourceListJson(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long store = MyRequestUtil.getLongParameter(request, "RSDA_RMST_ID");
		long sourceType = MyRequestUtil.getLongParameter(request, "RSDA_RLTY_ID");
		long rmin_id = MyRequestUtil.getLongParameter(request, "RSDA_RMIN_ID");
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		long userid = MyRequestUtil.getLoginUserId(request);
		String jsondata = resourceService.getResourceListJson(userid, store, sourceType, rmin_id, begin, pageNum);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		return null;
	}

	/**
	 * 获取某作业下资源列表JSON数据
	 */
	public ModelAndView resourceListJsonByMission(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rmin_id = MyRequestUtil.getLongParameter(request, "RSDA_RMIN_ID");
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		String rsdaTitle = ServletRequestUtils.getStringParameter(request, "rsdaTitle", "");
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "RSDA_BUILDDATE");
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "ASC");
		String param = "";
		if(rsdaTitle.trim().length() > 0){
			param = " and rsd.RSDA_TITLE like '%"+rsdaTitle+"%'";
		}
		resourceService.setParams(param);
		String jsondata = resourceService.getResourceListJsonByMission(rmin_id, begin, pageNum, sort, dir);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
		return null;
	}

	// /////////////////////////////////////////////////////////资源修改相关
	/**
	 * 进入资源列表
	 */
	public ModelAndView doResourceEdit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long sourceId = MyRequestUtil.getLongParameter(request, "sid");
		long languageId = ServletRequestUtils.getLongParameter(request, "lanId", 0);
		int pagecount = ServletRequestUtils.getIntParameter(request, "pagecount", 1);
		if (pageshow!=0) {
			pagecount = pageshow;
			pageshow = 0;
		}
		pagecount = (pagecount-1)*20;
		if(languageId == 0) {
			languageId = sldao.getSourceFirstLanguage(sourceId);
		}
		String searchtitle =  ServletRequestUtils.getStringParameter(request, "searchtitle","");
		int reqType = MyRequestUtil.getIntParameter(request, "reqType");
		// 获取资源基本信息
		ResSourceData source = resourceService.getResourceMetaInfo(sourceId);
		// 获取所有的元素信息
		List<ResElementPool> EleDataList = resourceService.getResourceAllData(source.getRsdaRmstId(), source.getRsdaRltyId(), languageId,
				sourceId);
		// 获取语种
		ResLanguageType language = lanService.getResLanguage(languageId);
		// 获取学科下拉options选项列表
		String subjectOptions = resourceService.getSubjectOptions(ToolUnit.NullTo0(source.getRsdaRstyId()), 0, "1"); // 学科选项
		// 获取自定义分类下拉options选项列表
		String dynamicTypeOptions = resourceService.getDynamicTypeOptions(source.getRsdaRmstId(), source.getRsdaRdtyId() == null ? 0
				: source.getRsdaRdtyId(), 1);  //分类一
		String dynamicTypeOptions2 = resourceService.getDynamicTypeOptions(source.getRsdaRmstId(), source.getRsdaRdtyTwo() == null ? 0
				: source.getRsdaRdtyTwo(), 2);  //分类二
//		String dynamicTypeOptions3 = resourceService.getDynamicTypeOptions(source.getRsdaRmstId(), source.getRsdaRdtyThree() == null ? 0
//				: source.getRsdaRdtyThree(), 3);  //分类三
		
		String dynamicTypeOptions3 = dynamicTypeService.getDynamicOptions(source.getRsdaRmstId(), source.getRsdaRdtyThree() == null ? 0
				: source.getRsdaRdtyThree(), 3);  //分类三
		
		
		//主题下拉选项
		long selectId = source.getRsdaRssuId()==null ? -1 : source.getRsdaRssuId();
		@SuppressWarnings("unused")
		long rsdaRminId = source.getRsdaRminId()==null ? -1 : source.getRsdaRminId();
		
		String sourceSubjectOptions ="";
		if(reqType!=5){
			if(source.getRsdaRminId() != null && source.getRsdaRminId()>0){
				sourceSubjectOptions = resourceService.getSourceSubjectOptions(source.getRsdaRminId(),selectId);
			}
		}
		
		String resLan = "";
		if(CommonUtil.isNotNull(source.getRsdaLanguage())){
			String lanCode = source.getRsdaLanguage();
			resLan = LanguageUtil.codeToTxt(lanCode);
		}
		
//		String rights = source.getRsdaRights();
		
		//会员类型下拉
//		long rsdaRights = -1;
		
//		if(rights !=null){
//			rsdaRights = Long.parseLong(source.getRsdaRights());
//		}
//		String sysUserTypeOptions = resourceService.getSourceRightsOptions(rsdaRights);
		
//		request.setAttribute("sysUserTypeOptions", sysUserTypeOptions);
		request.setAttribute("sourceSubjectOptions", sourceSubjectOptions);
		request.setAttribute("languageId", languageId);
		request.setAttribute("language", language);
		request.setAttribute("subjectOptions", subjectOptions);
		request.setAttribute("dynamicTypeOptions", dynamicTypeOptions);
		request.setAttribute("dynamicTypeOptions2", dynamicTypeOptions2);
		request.setAttribute("dynamicTypeOptions3", dynamicTypeOptions3);
		request.setAttribute("resource", source);
		request.setAttribute("EleDataList", EleDataList);
		request.setAttribute("reqType", reqType);
		request.setAttribute("rminId", source.getRsdaRminId());
		request.setAttribute("pagecount", pagecount);
		request.setAttribute("searchtitle", searchtitle);
		request.setAttribute("resLan", resLan);
		if(reqType == 5){
			return new ModelAndView("platform/userCenter/resourceEdit");
		}else{
			//记录列表页面里面的检索参数
			Long searchRmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
			Long searchRltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
			request.setAttribute("searchRmstId", searchRmstId);
			request.setAttribute("searchRltyId", searchRltyId);
		    return new ModelAndView("resource/build/resourceEdit");
		}
	}

	/**
	 * 修改资源
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public void updateResourceData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		
		errorMsg = "";
		long sourceId = MyRequestUtil.getLongParameter(request, "sid");
		long store = MyRequestUtil.getLongParameter(request, "store"); // 特色库ID
		long sourceType = MyRequestUtil.getLongParameter(request, "stype"); // 资源类型ID
		 pageshow = MyRequestUtil.getIntParameter(request, "pagecount");
			if (pageshow!=0) {
				pageshow = (pageshow/20)+1;
			}else {
				pageshow = 1;	
			}
		Long rsdaRssuId = ServletRequestUtils.getLongParameter(request, "rsdaRssuId",0L);
		
		try {
			int reqType = MyRequestUtil.getIntParameter(request, "reqType");
			List<ResElementPool> elementList = elementPool.getElementByTemplet(store, sourceType);
			if(reqType == 3) { // 资源预览
//				long rsty_id = ServletRequestUtils.getLongParameter(request, "rsty_id", 0L); // 学科分类
//				long rdty_id = ServletRequestUtils.getLongParameter(request, "rdty_id", 0L); // 学科分类
//				String rstyName = "";
//				String rdtyName = "";
//				if(rsty_id > 0){
//					String sql = "select RSTY_NAME from RES_SUBJECT_TYPE where RSTY_ID=" + rsty_id;
//					rstyName = resourceService.getFirstData(sql);
//				}if(rdty_id > 0){
//					String sql = "select RDTY_NAME from RES_DYNAMIC_TYPE where RDTY_ID=" + rdty_id;
//					rdtyName = resourceService.getFirstData(sql);
//				}
//				
//				request.setAttribute("elementList", elementList);
//				request.setAttribute("requestObj", request);
//				request.setAttribute("rstyName", rstyName);
//				request.setAttribute("rdtyName", rdtyName);
//				response.sendRedirect(request.getContextPath()+"/resource/build/resourcePreview");
			}else{
				boolean checkResult = checkForm(elementList, request); // 表单服务端验证
				if(checkResult == false) {
//					request.setAttribute("errorMsg", errorMsg);
//					response.setContentType("text/html; charset=UTF-8");
//					PrintWriter out = response.getWriter();
//					out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
//					out.print("<span style='font-size:12px;'>" + errorMsg + "<br>");
//					out.print("<br><a href='javascript:window.history.go(-1);'>返回重新编辑</a></span></span>");
//					return null;
					throw new ErrorMessageException(errorMsg);
				}else{
					ResSourceData resource = resourceService.getResourceMetaInfo(sourceId);
					if(rsdaRssuId>0){
						resource.setRsdaRssuId(rsdaRssuId);
					}else{
						resource.setRsdaRssuId(null);
					}
					
					// ---根据文件类型做不同的文件处理
					int fileType = ServletRequestUtils.getIntParameter(request, "resFileType", 0);
					if(fileType == 0){
						
						// 获取特色库文件存放盘符
						String storeFolder = null;
						if(resource.getResMetaStore() != null) {
							storeFolder = resource.getResMetaStore().getRmstFolder();
						}else {
							storeFolder = resourceService.getStoreFolder(store);
						}
						ResSourceData tmpResource = new ResSourceData(); // 创建一个资源对象记录资源原先的文件名，用作修改后的文件名进行比较。
						tmpResource.setRsdaBigimgName(resource.getRsdaBigimgName());
						tmpResource.setRsdaFileName(resource.getRsdaFileName());
						tmpResource.setRsdaFileLocalName(resource.getRsdaFileLocalName());
						tmpResource.setRsdaBigimgLocalName(resource.getRsdaBigimgLocalName());
						Long fileServiceId = resource.getResMetaStore().getRmstRfilId(); // 文件服务器ID
						ResFilesystem fileServcieBean = fileService.getFilesystem(fileServiceId); // 获取文件服务实体对象
						if(fileServcieBean == null) {
							fileServcieBean = new ResFilesystem();
						}
						uploadService = getUploadServiceById(fileServiceId);
						uploadService.setFileServiceBean(fileServcieBean);
						// 文件相对路径
						String folder = "";
						if(resource.getResMissionInfo() == null){
							folder = uploadService.createFolder();
						}else{
							folder = resource.getResMissionInfo().getRminMissname();
						}
						String filePath = SysConfigInfo.RES_DIR + storeFolder + "/" + folder + "/";
						// 如果资源封面的状态发生了变化，则重新上传封面
						// 当前使用的物理文件名的前部的定义
						String localFileNamePre = System.currentTimeMillis() + "";
						Boolean hasMustImg = false;
						Boolean hasMustSource = false;
						if(MyRequestUtil.getIntParameter(request, "bigimg_statu") > 0) {
							hasMustImg = loadImageFile(resource, filePath, localFileNamePre, request);
						}
						// 如果源文件的状态发生了变化，则重新上传源文件
						if(MyRequestUtil.getIntParameter(request, "loadfile_statu") > 0) {
							hasMustSource = loadSourceFile(resource, filePath, localFileNamePre, request);
						}
						// 调用远程方法执行转换
						if(hasMustSource) {
							String fileLocalName = resource.getRsdaFileLocalName();
							if(ConstantsObj.FileConvertName.TYPE_AUDIO.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
								FileConvertServiceUtil.audioConvert(fileLocalName, changeSuffix(fileLocalName, ConstantsObj.FileConvertName.EXT_MP3_NAME),
										fileServcieBean.getRfilAddress());
							}
							if(ConstantsObj.FileConvertName.TYPE_VIDEO.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
								FileConvertServiceUtil.videoConvert(fileLocalName, changeSuffix(fileLocalName, ConstantsObj.FileConvertName.EXT_FLV_NAME),
										"", fileServcieBean.getRfilAddress());
							}
							if(ConstantsObj.FileConvertName.TYPE_DOCUMENT.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
								FileConvertServiceUtil.documentConvert(fileLocalName,
										changeSuffix(fileLocalName, ConstantsObj.FileConvertName.EXT_SWF_NAME), fileServcieBean.getRfilAddress());
							}
						}
						if(hasMustImg) {// 只有视频文件才可生成封面，如果没有源文件则采用预览文件生成封面
							String fileLocalName = "";
							if(hasLoadFile("loadfile", request)) {
								fileLocalName = resource.getRsdaFileLocalName();
							}
							if(ConstantsObj.FileConvertName.TYPE_VIDEO.equals(ServiceUtil.getFileTypeBySpread(fileLocalName))) {
								String outputFileName = fileLocalName.substring(0, fileLocalName.lastIndexOf("_")) + ConstantsObj.FileConvertName.NAME_IMG
										+ "." + ConstantsObj.FileConvertName.EXT_JPEG_NAME;
								FileConvertServiceUtil.photoConvertFromVideo(fileLocalName, outputFileName, ConstantsObj.FileConvertName.PHOTO_CUT_TIME,
										fileServcieBean.getRfilAddress());
							}
						}
						checkFile(tmpResource, resource, request); // 检查之前和现在的文件变化情况
						tmpResource = null;
					}else if(fileType == 1){
						String imgDir = ServletRequestUtils.getStringParameter(request, "imgDir", "");
						resource.setRsdaFileUrl(imgDir);
					}else if(fileType == 2){
						String htmlDir = ServletRequestUtils.getStringParameter(request, "htmlDir", "");
						resource.setRsdaFileUrl(htmlDir);
					}
					resource = ParseResourceInfo(request, resource);
					
					String lan = resource.getResMetaStore().getRmstLanguage();
					resource = ParseResourceMetadata(resource, lan, request, elementList);
					
					boolean b = resourceService.updateResource(resource, elementList, request);
					if(b) {
						if(resource.getRsdaStatus().equals(ResSourceData.STATUS_PASS)
								&& resource.getRsdaIsuse().equals(ResSourceData.ISUSE_USEABLENESS)
								&& resource.getRsdaRmstStatus().equals(ResMetaStore.STATUS_USEABLENESS)
								&& resource.getRsdaRminStatus().equals(ResMissionInfo.RMINSTATUS_COMMIT_PASS)){
							
							//同时更新到lucene索引中（但必须是审核通过后的可用资源）
							resource = ResCommonController.parseResCover(request, resource);
							this.resourceLuceneService.updateResourceToIndex(resource);
//							this.cacheService.updateResforType();	//已采用时时查询
						}
//						
//						errorMsg = "<script>Msg.success('保存成功！');</script>";
//						request.setAttribute("result", errorMsg);
						map.put("success", true);
						map.put("msg", "保存成功");
					}else {
//						request.setAttribute("result", "<script>alert('对不起，修改失败!');</script>");
						map.put("success", false);
						map.put("msg", "保存失败");
					}
					request.setAttribute("reqType", MyRequestUtil.getIntParameter(request, "reqType"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		}finally{
			mustGenerateImg = false;
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	/**
	 *  进入修改附件列表
	 **/
	public ModelAndView initResSourceFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//资源列表的检索条件参数
		Long resId = MyRequestUtil.getLongParameter(request, "resId");	
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		int beginRow = ServletRequestUtils.getIntParameter(request, "begin", 0);
		beginRow = (beginRow-1)*20;
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmst_id", 0L);
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rlty_id", 0L);
		
		//页面请求来源方式
		String pageSource = ServletRequestUtils.getStringParameter(request, "pageSource", "");
		
		ResSourceData sourceData = (ResSourceData)missionService.get(ResSourceData.class, resId);
		List<ResSourceFile> list = missionService.getResSourceFileToResSourceId(resId);
		request.setAttribute("resId", resId);
		if(null!=sourceData){
			request.setAttribute("storeId", sourceData.getResMetaStore().getRmstId());
			request.setAttribute("rminid", sourceData.getRsdaRminId());
			request.setAttribute("rsdaTitle", sourceData.getRsdaTitle());
		}
		
		request.setAttribute("list", list);
		request.setAttribute("rminId", sourceData.getRsdaRminId());
		request.setAttribute("key", key);
		request.setAttribute("beginRow", beginRow);
		
		request.setAttribute("rmst_id", rmstId);
		request.setAttribute("rlty_id", rltyId);
		request.setAttribute("pageSource", pageSource);
		return new ModelAndView("resource/build/resourceFiles");
	}
	
	/**
	 *  修改附件列表基本信息
	 **/
	public void updateResSourceFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		Long resFileId = MyRequestUtil.getLongParameter(request, "resFileId");
		int uptype = MyRequestUtil.getIntParameter(request, "uptype");
		ResSourceFile resSourceFile= (ResSourceFile)missionService.get(ResSourceFile.class, resFileId);
		boolean b =true; 
		try {
			//修改说明
			if(uptype==1){
				String resFileRemark = MyRequestUtil.getStringParameter(request, "resFileRemark");
				resSourceFile.setResFileRemark(resFileRemark); 
				missionService.save(resSourceFile);
			}
			//修改序号
			else if (uptype==2){
				int index = MyRequestUtil.getIntParameter(request, "index"); 
				missionService.updateResSourceFileIndex(resFileId, index);
			}else{
				b=false;
			}
		} catch (Exception e){
			e.printStackTrace();
				b=false;
		}
		if(b){
			out.print("修改成功");
		}else {
			out.print("修改失败");
		}
		out.flush();
	}
	
	/**
	 *  修改-新增附件	
	 **/
	public void saveResFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		Long resSourceId = MyRequestUtil.getLongParameter(request, "RESSOURCEId");
		String remark = MyRequestUtil.getStringParameter(request, "remark");
		remark = CommonUtil.hangleTextArea(remark);
		
		String accessType = ServletRequestUtils.getStringParameter(request, "accessType", "");
		
		try {
		/**上传附件**/
		long store = MyRequestUtil.getLongParameter(request, "storeId"); // 特色库ID
		long rminId = MyRequestUtil.getLongParameter(request, "rminid"); // 作业ID
		ResMissionInfo mission = missionService.getResMissionInfoById(rminId);
		ResSourceData resource = (ResSourceData)missionService.get(ResSourceData.class, resSourceId);
		ResMetaStore rmst = null;
		if(mission == null){
			mission = new ResMissionInfo();
			rmst = (ResMetaStore) resourceService.get(ResMetaStore.class, store);
		}else{
			rmst = mission.getResMetaStore();
		}
		if(rmst == null){
			return ;
		}
		
		ResFilesystem fileService = this.fileService.getFilesystem(rmst.getRmstRfilId()); // 文件服务器对象
		if(fileService == null) {
			fileService = new ResFilesystem();
		}
		uploadService = getUploadServiceById(rmst.getRmstRfilId());
		uploadService.setFileServiceBean(fileService);
		
		String storeFolder = null;
		if(resource.getResMetaStore() != null) {
			storeFolder = resource.getResMetaStore().getRmstFolder();
		}else {
			storeFolder = resourceService.getStoreFolder(store);
		}
		String filePath = SysConfigInfo.RES_DIR + storeFolder + "/" + mission.getRminMissname() + "/"; 


		boolean flag = false;
		ResSourceFile rsfile = new ResSourceFile(); 
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = null;
		file = multiRequest.getFile("resFile");
		if(!hasLoadFile("resFile", request)) {
			out.print("{success:false,msg:'没有上传文件!'}"); 
			out.flush();
			return;
		}
		
		String fileName = file.getOriginalFilename();
		String localFileName = "";
		String extName = "";
		if(ServiceUtil.isNotNull(fileName) && fileName.indexOf(".") > 0) { // 执行上传操作
			extName = fileName.substring(fileName.lastIndexOf(".") + 1); // 扩展名
			localFileName = System.currentTimeMillis() + "" + ConstantsObj.FileConvertName.NAME_DATA + "." + extName;
			InputStream inputStream =  file.getInputStream();
			 upLoadFile(filePath, localFileName, inputStream);
		}
		String fileNamePath = filePath + localFileName;
		if(ServiceUtil.isNotNull(fileName)){
			rsfile.setResLocalPath(fileNamePath);
			rsfile.setResFileName(fileName);
			rsfile.setResFilePath(getUrl(fileNamePath, fileName));
			rsfile.setResRsdaId(resource.getRsdaId());
			rsfile.setResFileRemark(remark);
			rsfile.setResIndex(missionService.getFileIndex(resSourceId)+1);
			rsfile.setResFileAccessType(accessType);
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
			rsfile.setResViewPath(getUrl(changeSuffix(fileNamePath, extName), changeSuffix(fileName, extName)));
			if(extName.toLowerCase().equals(ConstantsObj.FileConvertName.EXT_FLV_NAME) || extName.toLowerCase().equals("mp4")) {
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
			out.print("{success:false,msg:'上传失败!'}"); 
			out.flush();
			return;
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
			out.print("{success:true,msg:'上传成功!'}"); 
		} catch (Exception e) {
			e.printStackTrace();
			out.print("{success:false,msg:'上传或转换发生错误!'}"); 
		}
		out.flush();
		
	}
	/**
	 *  删除附件
	 **/
	public void deleteResSourceFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		Long resFileId = MyRequestUtil.getLongParameter(request, "resFileId"); 
		boolean b = missionService.deleteResSourceFile(resFileId);
		if(b){
			out.print("删除成功");
		}else {
			out.print("删除失败");
		}
		out.flush();
	}
	
	/**
	 * 附件预览
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView sourceFileView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long sourceFileId = ServletRequestUtils.getLongParameter(request, "sourceFileId", 0L);
		ResSourceFile sourceFile = (ResSourceFile)missionService.get(ResSourceFile.class,sourceFileId);
		String sourceType = "";
		String fileUrl = "";
		String fileName = ""; 
			fileUrl = sourceFile.getResViewPath() + "&amp;isOnLine=true";
			fileName = ToolUnit.getFileName(ToolUnit.NullToBlank(sourceFile.getResLocalPath()));
			sourceType = ServiceUtil.getFileTypeBySpread(fileName);
		String fileAddress = SysConfigInfo.FILE_URL_PATH;
		request.setAttribute("sourceType", sourceType);
		request.setAttribute("fileUrl", fileUrl);
		request.setAttribute("fileAddress", fileAddress);
		request.setAttribute("fileName", fileName);
		request.setAttribute("sourceTitle", sourceFile.getResFileName());
				
		return new ModelAndView("resource/build/FileView");
	}
	
	

	// ///////////////////////////////////////////////////////多语种录入相关
	// 进入其它语录入作业选择页面
	public ModelAndView doInterpretMission(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/build/interpretMissionList");
	}

	/**
	 * 进入某作业下资源列表
	 */
	public ModelAndView interpretResourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rminId = MyRequestUtil.getLongParameter(request, "rmin_id");
		long languageId = ServletRequestUtils.getLongParameter(request, "lanId", 0);
		ResLanguageType language = lanService.getResLanguage(languageId);
		ResMissionInfo mission = missionService.getResMissionInfoById(rminId);
		String rminName = mission.getRminMissname();
		request.setAttribute("rminId", rminId);
		request.setAttribute("rminName", rminName);
		request.setAttribute("rminStatu", mission.getRminStatus());
		request.setAttribute("language", language);
		return new ModelAndView("resource/build/interpretResourceList");
	}

	// 进入其它语录入已翻译页
	public ModelAndView InterpretSourceInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long sourceId = MyRequestUtil.getLongParameter(request, "sourceId");
		long languageId = MyRequestUtil.getLongParameter(request, "languageId");
		ResourceRpc sourcedao = new ResourceRpc();
		request.setAttribute("sourceId", sourceId);
		request.setAttribute("languageId", languageId);
		String options = sldao.getSourceLanguageOption(sourceId, 1, languageId);
		ResSourceData source = resourceService.getResourceMetaInfo(sourceId);
		String data = sourcedao.getSourceInfo(sourceId, languageId, source.getRsdaRstyId(), source.getRsdaRltyId());
		request.setAttribute("languageOptions", options);
		request.setAttribute("sourceData", data);
		return new ModelAndView("resource/build/sourceInfo");
	}

	// 进入其它语录入翻译页面
	public ModelAndView InterpretSourceForm(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long sourceId = MyRequestUtil.getLongParameter(request, "sourceId");
		long languageId = MyRequestUtil.getLongParameter(request, "languageId");
		int reqType = 0;
		long missionFirstLanguageId = sldao.getSourceFirstLanguage(sourceId);
		if(languageId == 0) {
			languageId = sldao.getSourceFirstLanguage(sourceId);
			reqType = 1;
		}
		ResSourceData source = resourceService.getResourceMetaInfo(sourceId);
		List<ResElementPool> elelist = resourceService.getResourceAllData(source.getRsdaRmstId(), source.getRsdaRltyId(),
				missionFirstLanguageId, sourceId);
		request.setAttribute("sourceId", sourceId);
		request.setAttribute("languageId", languageId);
		request.setAttribute("source", source);
		request.setAttribute("elelist", elelist);
		ResLanguageType language = lanService.getResLanguage(languageId);
		String passLanguageOption = sldao.getSourceLanguageOption(sourceId, 4, languageId); // 已审核通过的语种
		request.setAttribute("lname", passLanguageOption);
		request.setAttribute("language", language);
		request.setAttribute("reqType", reqType);
		return new ModelAndView("resource/build/interpretForm");
	}

	// 保存录入的其它语信息
	public ModelAndView SaveInterpret(HttpServletRequest request, HttpServletResponse response) throws Exception {
		errorMsg = "";
		long store = MyRequestUtil.getLongParameter(request, "store"); // 特色库ID
		long sourceType = MyRequestUtil.getLongParameter(request, "stype"); // 资源类型ID
		long sourceId = MyRequestUtil.getLongParameter(request, "sourceId");
		int reqType = ServletRequestUtils.getIntParameter(request, "reqType", 1);
		// 获取该资源所有的元素
		List<ResElementPool> elementList = elementPool.getElementByTemplet(store, sourceType);
		boolean checkResult = InterpretCheckForm(elementList, request);
		if(checkResult == false) {
			request.setAttribute("errorMsg", errorMsg);
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
			out.print("<span style='font-size:12px;'>" + errorMsg + "<br>");
			out.print("<br><a href='javascript:window.history.go(-1);'>返回重新编辑</a></span></span>");
			return null;
		}
		Serializable s = sldao.saveSourceLanguage(request, elementList);
		if(s != null) {
			errorMsg = "<script>showSuccessMessage('" + request.getContextPath() + "/');</script>";
			request.setAttribute("result", errorMsg);
			request.setAttribute("sourceId", sourceId);
			request.setAttribute("rminId", MyRequestUtil.getLongParameter(request, "rminId"));
			if(reqType == 2){ //保存并继续
				long languageId = MyRequestUtil.getLongParameter(request, "languageId");
				long rminId = MyRequestUtil.getLongParameter(request, "rmin_id");
				int rminStatu = MyRequestUtil.getIntParameter(request, "rminStatu");
				long next_rsdaId = sldao.getNextResourceId(rminId, languageId);
				if(next_rsdaId > 0){
					String url = request.getContextPath() + "/console/data/inputSingle.htm?action=InterpretSourceForm&sourceId="+next_rsdaId+"&languageId="+languageId+"&rminStatu="+rminStatu;
					//request.getRequestDispatcher().forward(request,response);
					response.sendRedirect(url);
					return null;
				}else{
					return interpretResourceList(request, response);
				}
			}else{
				return interpretResourceList(request, response);
			}
		}else {
			errorMsg = "<script>alert('" + errorMsg + "');</script>";
			request.setAttribute("result", errorMsg);
			return InterpretSourceForm(request, response);
		}
	}
	
	/**
	 * 用户资源审核
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView memberResourceChecked(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "sid", 0);
		Long rsdaStatus = ServletRequestUtils.getLongParameter(request, "rsdaStatus", 0);
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return null;
			}
			if(rsdaId == 0)
				throw new ErrorMessageException("资源Id不能为空！");
			ResSourceData rsd = resourceService.getResourceMetaInfo(rsdaId);
			if(rsd == null)
				throw new ErrorMessageException("该资源不存在！");
			if(rsdaStatus == 0)
				throw new ErrorMessageException("校验结果不正确！");
			//修改资源表
			rsd.setRsdaStatus(rsdaStatus);
			
			boolean b = this.resourceCheckedService.updateResourceChecked(rsd, rsdaStatus);
			String resultMsg = "保存失败!";
			if(b){
				resultMsg = "保存成功!";
				int changStatu = ServletRequestUtils.getIntParameter(request, "isChang", 1);
				if(changStatu > 0){
					updateResourceData(request, response);
				}
			}
			// 返回提示信息 
			request.setAttribute("result", "<script>showSuccessMessage('" + request.getContextPath() + "/','2','"+resultMsg+"');</script>");
			
			// 通过并审核下一条资源
			Long type = ServletRequestUtils.getLongParameter(request, "type", 0);
			if(type == 3){
				// 获取下一条资源:存在则跳转到详情页面，不存在则回到审核页面;
				List<Object> nextRsd = this.resourceCheckedService.getNextRSD(0L, rsd.getRsdaAuthortype(),"");
				if(nextRsd.size()>0){
					Object[] temp = (Object[])nextRsd.get(0);
					Long nextRsdaId = (Long)temp[0];
					request.getRequestDispatcher("/console/data/memberResource.htm?action=initChecked&rsdaid="+nextRsdaId).forward(request,response);
					return null;
				}
			}
			request.getRequestDispatcher("/console/data/memberResource.htm?action=initMemberResourceList").forward(request,response);
			return null;
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			result.put("msg", e.getMessage());
			request.getRequestDispatcher("/console/data/memberResource.htm?action=initChecked&rsdaid="+rsdaId).forward(request,response);
			return null;
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
			request.getRequestDispatcher("/console/data/memberResource.htm?action=initChecked&rsdaid="+rsdaId).forward(request,response);
			return null;
		}
	}

	// ////////////////////////////////////////////////////////资源的基本封装和操作
	// 封装资源。但不包括：状态、评分、评论开关状态、是否为推荐资源、点击量、审核评论
	public ResSourceData ParseResourceInfo(HttpServletRequest request, ResSourceData resource) {
		long store = MyRequestUtil.getLongParameter(request, "store"); // 特色库ID
		long sourceType = MyRequestUtil.getLongParameter(request, "stype"); // 资源类型ID
		long rmin_id = MyRequestUtil.getLongParameter(request, "rmin_id"); // 作业ID
		String rmty_name = MyRequestUtil.getStringParameter(request, "rmty_id"); // 中图法分类
		long sman_id = MyRequestUtil.getLongParameter(request, "userid"); // 获取当前登录用户ID
		
		int inputType = ServletRequestUtils.getIntParameter(request, "inputType", ResSourceData.AUTHORTYPE_ADMIN_INPUT);
		int fileType = ServletRequestUtils.getIntParameter(request, "resFileType", 0);
		Long rsty_id = null; // 学科法分类
		Long rdty_id = null; //自定义分类一
		Long rdty_id_two = null; //自定义分类二
		Long rdty_id_three = null; //自定义分类三
		try {
			if(request.getParameter("rsty_id") != null && request.getParameter("rsty_id").length() > 0) {
				rsty_id = ServletRequestUtils.getLongParameter(request, "rsty_id");
			}
			if(request.getParameter("rdty_id") != null && request.getParameter("rdty_id").length() > 0) {
				rdty_id = ServletRequestUtils.getLongParameter(request, "rdty_id");
			}
			if(request.getParameter("rdty_id_two") != null && request.getParameter("rdty_id_two").length() > 0) {
				rdty_id_two = ServletRequestUtils.getLongParameter(request, "rdty_id_two");
			}
			if(request.getParameter("rdty_id_three") != null && request.getParameter("rdty_id_three").length() > 0) {
				rdty_id_three = ServletRequestUtils.getLongParameter(request, "rdty_id_three");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		resource.setRsdaRltyId(sourceType);
		resource.setRsdaRmstId(store);
		resource.setRsdaRminId(rmin_id < 1 ? null : rmin_id);
		resource.setRsdaRstyId(rsty_id);
		resource.setRsdaSmanId(sman_id < 1 ? null : sman_id);
		resource.setRsdaAuthortype(inputType); // 资源来源类型
		resource.setRsdaRdtyId(rdty_id); //自定义分类一
		resource.setRsdaRdtyTwo(rdty_id_two);  //自定义分类二
		resource.setRsdaRdtyThree(rdty_id_three); //自定义分类三
		resource.setRsdaSourceType(fileType);
		return resource;
	}

	//封装资源十五个核心元素基本信息 增加了一个关键词
	public ResSourceData ParseResourceMetadata(ResSourceData resource, String lan, HttpServletRequest request, List<ResElementPool> elelist) {
		ResElementPool element = null;
		String eleid = "";
		String elename = "";
		// Date date = null;
		for(int i = 0, s = elelist.size(); i < s; i++) {
			element = elelist.get(i);
			elename = ToolUnit.NullToBlank(element.getRepoName()).toLowerCase();
			eleid = element.getRepoId().toString();
			if(elename.equals("title")) {
				resource.setRsdaTitle(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("creator")) {
				resource.setRsdaCreator(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("subject")) {
				resource.setRsdaSubject(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("description")) {
				resource.setRsdaDescription(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("publisher")) {
				resource.setRsdaPublisher(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("contributor")) {
				resource.setRsdaContributor(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("created")) {
				if(resource.getRsdaDate() == null){
					resource.setRsdaDate(MyRequestUtil.getStringParameter(request, eleid));
				}
			}
			if(elename.equals("date")){
				resource.setRsdaDate(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("type")) {
				resource.setRsdaType(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("format")) {
				resource.setRsdaFormat(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("identifier")) {
				resource.setRsdaIdentifier(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("source")) {
				resource.setRsdaSource(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("language")) {
				resource.setRsdaLanguage(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("relation")) {
				resource.setRsdaRelation(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("coverage")) {
				resource.setRsdaCoverage(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("rights")) {
				resource.setRsdaRights(MyRequestUtil.getStringParameter(request, eleid));
			}
			if(elename.equals("keyword")) {
				resource.setRsdaKeyword(MyRequestUtil.getStringParameter(request, eleid));
			}
		}
		
		//根据资源库的语种设备资源所属语种
		if(CommonUtil.isNotNull(lan)){
			String lanTxt = LanguageUtil.codeToTxt(lan);
			resource.setRsdaLanguage(lanTxt);
		}
		return resource;
	}

	// 检查修改之前的四个文件，如果发生了变化则删除之前的文件
	public void checkFile(ResSourceData old, ResSourceData now, HttpServletRequest request) {
		if(null != old.getRsdaBigimgLocalName() && old.getRsdaBigimgLocalName().equals(now.getRsdaBigimgLocalName()) == false) {
			this.uploadService.removeFile(old.getRsdaBigimgLocalName());
			String comName = old.getRsdaBigimgLocalName();
			if(ServiceUtil.isNotNull(comName)) {
				comName = comName.substring(0, comName.lastIndexOf("_") + 1);
				uploadService.removeFile(comName + "jpg.jpeg");
			}
		}
		if(null != old.getRsdaFileLocalName() && old.getRsdaFileLocalName().equals(now.getRsdaFileLocalName()) == false) {
			this.uploadService.removeFile(old.getRsdaFileLocalName());
			String comName = old.getRsdaFileLocalName();
			if(ServiceUtil.isNotNull(comName)) {
				comName = comName.substring(0, comName.lastIndexOf("_") + 1);
				uploadService.removeFile(comName + "data.flv");
				uploadService.removeFile(comName + "data.swf");
				uploadService.removeFile(comName + "data.mp3");
			}
		}
	}

	/**
	 * 表单服务器端验证
	 * 
	 * @return
	 */
	private boolean checkForm(List<ResElementPool> elementList, HttpServletRequest request) {
		boolean result = true;
		// 验证登录状态
		if(MyRequestUtil.getLongParameter(request, "userid") < 1 && MyRequestUtil.getMemberId(request) < 1 && MyRequestUtil.getLoginUserId(request) < 1) {
			errorMsg += "<li>用户未登录或登录过期 </li>";
			result = false;
		}
		// 验证必填元素
		if(checkElementData(elementList, request) == false) {
			result = false;
		}
		
		int fileType = ServletRequestUtils.getIntParameter(request, "resFileType", 0);
		//只有源文为文件模式才做文件验证
		if(fileType > 0){
			return true;
		}
		
		// 验证上传文件
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile("loadfile");
		int sourceSize = Integer.parseInt(ConstantsObj.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE));
		int viewSize = Integer.parseInt(ConstantsObj.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_VIEWFILE_MAXSIZE));
		int photoceSize = Integer.parseInt(ConstantsObj.getSysProperty(ConstantsObj.SysSettingKey.RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE));
		if(file != null && file.getSize() > 1024 * 1024 * sourceSize) {
			errorMsg += "<li>源文件大小不能超过" + sourceSize + "M</li>";
		}
		file = multiRequest.getFile("bigimg");
		if(file != null) {
			String extName = file.getOriginalFilename();
			extName = extName.substring(extName.lastIndexOf(".") + 1).toLowerCase();
			if(extName != null && extName.length() > 0 && !extName.equals("jpg") && !extName.equals("bmp")
					&& !extName.equals("png") && !extName.equals("jpeg") && !extName.equals("gif")) {
				errorMsg += "<li>封面图片文件格式不正确！</li>";
				result = false;
			}
			if(file.getSize() > 1024 * 1024 * photoceSize) {
				errorMsg += "<li>封面图片文件大小不能超过" + photoceSize + "M</li>";
				result = false;
			}
		}
		file = multiRequest.getFile("viewfile");
		if(file != null && file.getSize() > 1024 * 1024 * viewSize) {
			errorMsg += "<li>预览文件大小不能超过" + viewSize + "M</li>";
			result = false;
		}
		file = null;
		multiRequest = null;
		return result;
	}

	/**
	 * 其它语录入表单服务器端验证
	 * 
	 * @return
	 */
	private boolean InterpretCheckForm(List<ResElementPool> elementList, HttpServletRequest request) {
		boolean result = true;
		// 验证登录状态
		if(MyRequestUtil.getLongParameter(request, "userid") < 1) {
			errorMsg += "<li>用户未登录或登录过期 </li>";
			result = false;
		}
		long sourceId = MyRequestUtil.getLongParameter(request, "sourceId");
		if(sourceId == 0) {
			errorMsg += "<li>翻译的资源未找到，可能是已被删除 </li>";
			result = false;
		}
		if(MyRequestUtil.getLongParameter(request, "language_id") < 1) {
			errorMsg += "<li>翻译的新语种不明确，请选择！</li>";
			result = false;
		}
		// 验证必填元素
		if(checkElementData(elementList, request) == false) {
			result = false;
		}
		return result;
	}

	/**
	 * 验证必填元素
	 */
	private boolean checkElementData(List<ResElementPool> elementList, HttpServletRequest request) {
		boolean result = true;
		if(elementList != null && elementList.size() > 0) {
			ResElementPool element = null;
			String eledata = "";
			for(int i = 0, s = elementList.size(); i < s; i++) {
				element = elementList.get(i);
				if(element.getRepoRestrict() != null && element.getRepoRestrict().toUpperCase().equals("M")) {
					eledata = MyRequestUtil.getStringParameter(request, element.getRepoId().toString());
					if(eledata.trim().length() < 1) {
						errorMsg += "<li>元素:" + element.getRepoTitle() + " 为必填，不能为空！</li>";
						result = false;
					}
				}
			}
		}
		return result;
	}

	public IUploadService getUploadServiceById(Long rfilId) {
		if(rfilId == null || rfilId.equals(0)) {
			return localUploadService;
		}else {
			return remoteUploadService;
		}
	}

	
}