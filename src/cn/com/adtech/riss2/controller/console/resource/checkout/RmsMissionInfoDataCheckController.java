package cn.com.adtech.riss2.controller.console.resource.checkout;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import cn.com.adtech.riss2.bso.resource.IResMissionInfoCheckService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.jdbcms.ToolUnit;
import cn.com.adtech.riss2.upload.IUploadService;
import cn.com.adtech.riss2.wsClient.FileConvertServiceUtil;

import com.jbetter.framework.session.Pagination;

import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.controller.console.resource.build.ResourceFileLoad;

@SuppressWarnings("unchecked")
public class RmsMissionInfoDataCheckController extends ResourceFileLoad {
	private static final Logger logger = Logger.getLogger(RmsMissionInfoDataCheckController.class);

	private IResMissionInfoCheckService resMissionInfoCheckService;

	public IResMissionInfoCheckService getResMissionInfoCheckService() {
		return resMissionInfoCheckService;
	}

	public void setResMissionInfoCheckService(IResMissionInfoCheckService resMissionInfoCheckService) {
		this.resMissionInfoCheckService = resMissionInfoCheckService;
	}

	/**
	 * 初始化作业资源校验页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-13
	 */
	public ModelAndView initRmsMissionInfoDataCheckView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		Long userId = ServletRequestUtils.getLongParameter(request, "userid", 0);
		int pagecount = Integer.valueOf(ServletRequestUtils.getStringParameter(request, "pagecount", "0"));
		String  searchtitle = ServletRequestUtils.getStringParameter(request, "titleshow",""); 
		
	
		String sql = "select RMIN_STATUS from RES_MISSION_INFO where RMIN_ID = " + rminId;
		String rminStatu = resMissionInfoCheckService.getFirstData(sql);
		if(userId == 0){
			userId = resMissionInfoCheckService.getUserId(rminId, "rminCheckuser");
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rminId", rminId);
		map.put("userId", userId);
		map.put("pagecount", pagecount);
		request.setAttribute("searchtitle", searchtitle);
		map.put("rminStatu", rminStatu);
		return new ModelAndView("resource/checkout/rmsMissionInfoDataCheck", map);
	}

	/**
	 * 获取资源信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-9-15
	 */
	public ModelAndView initResSourceDataDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0);
		Long nextId = ServletRequestUtils.getLongParameter(request, "nextId", 0);
		int pagecount = ServletRequestUtils.getIntParameter(request, "pagecount",1);
		String  titleshow = ServletRequestUtils.getStringParameter(request, "titleshow",""); 
		pagecount = (pagecount-1)*20;
		if(nextId > 0){
			rsdaId = nextId;
		}
		String reqType = ServletRequestUtils.getStringParameter(request, "reqType", "");
		if(rsdaId > 0) {
			ResSourceData rsd = (ResSourceData)this.resMissionInfoCheckService.get(ResSourceData.class, rsdaId);
			if(rsd != null) {
				List list = this.resMissionInfoCheckService.getResSourceDataDetailInfo(rsdaId, null, null, null,null);
				result.put("rsdList", list);
				result.put("rsd", rsd);
//				// 获取学科下拉options选项列表
				String subjectOptions = resourceService.getSubjectOptions(ToolUnit.NullTo0(rsd.getRsdaRstyId()), 0, "1"); // 学科选项
				result.put("subjectOptions", subjectOptions);
				
//				//主题下拉选项
				long selectId = rsd.getRsdaRssuId()==null ? -1 : rsd.getRsdaRssuId();
				String sourceSubjectOptions = resourceService.getSourceSubjectOptions(rsd.getRsdaRminId(),selectId);
				request.setAttribute("sourceSubjectOptions", sourceSubjectOptions);

				String dynamicTypeOptions = dynamicTypeService.getDynamicOptions(rsd.getRsdaRmstId(), rsd.getRsdaRdtyId() == null ? 0
						: rsd.getRsdaRdtyId(), 1);  //分类一
				String dynamicTypeOptions2 = dynamicTypeService.getDynamicOptions(rsd.getRsdaRmstId(), rsd.getRsdaRdtyTwo() == null ? 0
						: rsd.getRsdaRdtyTwo(), 2);  //分类二
				//String dynamicTypeOptions3 = resourceService.getDynamicTypeOptions(rsd.getRsdaRmstId(), rsd.getRsdaRdtyThree() == null ? 0
						//: rsd.getRsdaRdtyThree(), 3);  //分类三
				String dynamicTypeOptions3 = dynamicTypeService.getDynamicOptions(rsd.getRsdaRmstId(), rsd.getRsdaRdtyThree() == null ? 0
						: rsd.getRsdaRdtyThree(), 3);
				request.setAttribute("dynamicTypeOptions", dynamicTypeOptions);
				request.setAttribute("dynamicTypeOptions2", dynamicTypeOptions2);
				request.setAttribute("dynamicTypeOptions3", dynamicTypeOptions3);
				
				// 获取所有的元素信息
				Long languageId = sldao.getSourceFirstLanguage(rsdaId);
				List<ResElementPool> EleDataList = resourceService.getResourceAllData(rsd.getRsdaRmstId(), rsd.getRsdaRltyId(), languageId,
						rsdaId);
				request.setAttribute("EleDataList", EleDataList);
				
				// 获取语种
				ResLanguageType language = lanService.getResLanguage(languageId);
				request.setAttribute("language", language);
				request.setAttribute("languageId", languageId);
			
			}
		}
		result.put("reqType", reqType);
		result.put("pagecount", pagecount);
		result.put("titleshow", titleshow);
		return new ModelAndView("resource/checkout/resSourceDataDetail", result);
	}

	private ResSourceLanguage getRSL(Long rsdaId, Long rltyId) {
		List list2 = this.resMissionInfoCheckService.find("from ResSourceLanguage rsl where rsl.resSourceId = " + rsdaId
				+ " and rsl.resLanguageId = " + rltyId);
		if(list2.size() > 0)
			return (ResSourceLanguage)list2.get(0);
		return null;
	}

	/**
	 * 获取资源列表
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */

	public void getResMissionInfoDataList(HttpServletRequest request, HttpServletResponse response) throws Exception {
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
		// 查询条件一：状态
		Long rsdaStatus = ServletRequestUtils.getLongParameter(request, "rsdaStatus_combox", 0);
	    //查询条件二：题名
		String searchtitle = ServletRequestUtils.getStringParameter(request, "searchtitle","");
		conditions.put("rslaStatus", rsdaStatus);
		conditions.put("searchtitle", searchtitle);
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		conditions.put("rminId", rminId);
		// 排序字段
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rslaDate");
		conditions.put("sort", sort);
		// 排序方式
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		conditions.put("dir", dir);
		List list = this.resMissionInfoCheckService.getResSourceDataList(pagination, conditions);
		String result = CommonUtil.createJsonTable(list, new String[] {"rsdaId", "rsdaTitle", "rltyName", "smanName",
				"rsdaBuilddate", "rsdaStatus", "rltyId", "rsdaCreator", "rsdaPublisher"}, pagination.getTotalResults() + "");
		out.write(result);
		out.flush();
	}

	public ModelAndView saveRsdaStatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			if(sysManager == null) {
				response.sendRedirect(request.getContextPath() + "/console/index.htm");
				return null;
			}
			Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0);
			Long rsdaStatus = ServletRequestUtils.getLongParameter(request, "rsdaStatus", 0);
			//跳转获得检索词
			String searchtitle = ServletRequestUtils.getStringParameter(request, "searchtitle","");
			if(rsdaId == 0)
				throw new ErrorMessageException("资源Id不能为空！");
			ResSourceData rsd = (ResSourceData)this.resMissionInfoCheckService.get(ResSourceData.class, rsdaId);
			if(rsd == null)
				throw new ErrorMessageException("该资源不存在！");
			if(rsdaStatus == 0)
				throw new ErrorMessageException("校验结果不正确！");
			if(rsdaStatus == 3){
				if(!saveSourceData(request,response)){
					return initRmsMissionInfoDataCheckView(request,response);
				}
			}
			rsd = null;
		    rsd = (ResSourceData)this.resMissionInfoCheckService.get(ResSourceData.class, rsdaId);
			//修改资源表
			rsd.setRsdaStatus(rsdaStatus);
			this.resMissionInfoCheckService.save(rsd);
			//修改语种表
//			this.resMissionInfoCheckService.updateResSourceLanguageStatus(rsdaId, 1, rsdaStatus);
			//添加审核人登录名和姓名，该字段为统计工作量而设计
			ResSourceLanguage resSourceLanguage = this.resMissionInfoCheckService.getResSourceLanguage(rsdaId, 1);
			resSourceLanguage.setRslCheckLoginname(sysManager.getSmanLoginName());
			resSourceLanguage.setRslCheckSuinname(sysManager.getSmanName());
			resSourceLanguage.setRslaStatus(rsdaStatus);
			this.resMissionInfoCheckService.save(resSourceLanguage);
			
			// 构造初始化信息
			// 返回提示信息
			request.setAttribute("result", "<script>showSuccessMessage('" + request.getContextPath() + "/','2','保存成功');</script>");
			// 通过并审核下一条资源
			Long type = ServletRequestUtils.getLongParameter(request, "type", 0);
			long userid;
			if(rsd.getRsdaUserId() != null && rsd.getRsdaUserId() > 0){
				userid = -1;
			}else{
				userid = sysManager.getSmanId();
			}
			if(type == 3){
				// 获取下一条资源:存在则跳转到详情页面，不存在则回到审核页面;
				List nextRsd = this.resMissionInfoCheckService.getNextRSD(rsd.getRsdaRminId(), rsd.getRsdaAuthortype(),searchtitle);
				if(nextRsd.size()>0){
					Object[] temp = (Object[])nextRsd.get(0);
					Long nextRsdaId = (Long)temp[0];
					Long nextRltyId = (Long)temp[1];
					String url = "/console/data/rmsMissionInfoDataCheck.htm?action=initResSourceDataDetail&rsdaId="+nextRsdaId+"&rltyId="+nextRltyId+"&userid=" + userid + "&nextId=" + nextRsdaId+"&titleshow="+searchtitle;
					request.getRequestDispatcher(url).forward(request,response);
					return null;
				}	
			}
			if(rsd.getRsdaUserId() != null && rsd.getRsdaUserId() > 0){
				request.getRequestDispatcher("/console/data/memberResource.htm?action=initMemberResourceList").forward(request,response);
			}else{
				request.getRequestDispatcher("/console/data/rmsMissionInfoDataCheck.htm?action=initRmsMissionInfoDataCheckView&titleshow="+searchtitle+"&rminId="+rsd.getRsdaRminId()+"&userid=" + userid).forward(request,response);
			}
			return null;
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			result.put("msg", e.getMessage());
			return new ModelAndView("resource/checkout/resSourceDataDetail", result);
		}catch(Exception ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
		return new ModelAndView("resource/checkout/resSourceDataDetail", result);
}
	protected String errorMsg = "";
	public Boolean saveSourceData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		errorMsg = "";
		long sourceId = MyRequestUtil.getLongParameter(request, "sid");
		long store = MyRequestUtil.getLongParameter(request, "store"); // 特色库ID
		long sourceType = MyRequestUtil.getLongParameter(request, "stype"); // 资源类型ID
		Long rsdaRssuId = ServletRequestUtils.getLongParameter(request, "rsdaRssuId",0L);
		int reqType = MyRequestUtil.getIntParameter(request, "reqType");
		List<ResElementPool> elementList = elementPool.getElementByTemplet(store, sourceType);
		if(reqType == 3) { // 资源预览
			long rsty_id = ServletRequestUtils.getLongParameter(request, "rsty_id", 0L); // 学科分类
			long rdty_id = ServletRequestUtils.getLongParameter(request, "rdty_id", 0L); // 学科分类
			String rstyName = "";
			String rdtyName = "";
			if(rsty_id > 0){
				String sql = "select RSTY_NAME from RES_SUBJECT_TYPE where RSTY_ID=" + rsty_id;
				rstyName = resourceService.getFirstData(sql);
			}if(rdty_id > 0){
				String sql = "select RDTY_NAME from RES_DYNAMIC_TYPE where RDTY_ID=" + rdty_id;
				rdtyName = resourceService.getFirstData(sql);
			}
			
			request.setAttribute("elementList", elementList);
			request.setAttribute("requestObj", request);
			request.setAttribute("rstyName", rstyName);
			request.setAttribute("rdtyName", rdtyName);
		}
		boolean checkResult = checkForm(elementList, request); // 表单服务端验证
		if(checkResult == false) {
			request.setAttribute("errorMsg", errorMsg);
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
			out.print("<span style='font-size:12px;'>" + errorMsg + "<br>");
			out.print("<br><a href='javascript:window.history.go(-1);'>返回重新编辑</a></span></span>");
			return false;
		}
		ResSourceData resource = resourceService.getResourceMetaInfo(sourceId);
		if(rsdaRssuId>0){
			resource.setRsdaRssuId(rsdaRssuId);
		}else{
			resource.setRsdaRssuId(null);
		}
		
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
		String filePath = storeFolder + "/" + folder + "/";
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
		resource = ParseResourceInfo(request, resource);
		resource = ParseResourceMetadata(resource, request, elementList);
		checkFile(tmpResource, resource, request); // 检查之前和现在的文件变化情况
		tmpResource = null;
		boolean b = resourceService.updateResource(resource, elementList, request);
		if(b) {
			errorMsg = "<script>showSuccessMessage('" + request.getContextPath() + "/');</script>";
			request.setAttribute("result", errorMsg);
		}else {
			request.setAttribute("result", "<script>alert('对不起，修改失败!');</script>");
		}
		request.setAttribute("reqType", MyRequestUtil.getIntParameter(request, "reqType"));
	    mustGenerateImg = false;
		return true;
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
	public IUploadService getUploadServiceById(Long rfilId) {
		if(rfilId == null || rfilId.equals(0)) {
			return localUploadService;
		}else {
			return remoteUploadService;
		}
	}

	// 封装资源。但不包括：状态、评分、评论开关状态、是否为推荐资源、点击量、审核评论
	public ResSourceData ParseResourceInfo(HttpServletRequest request, ResSourceData resource) {
		long store = MyRequestUtil.getLongParameter(request, "store"); // 特色库ID
		long sourceType = MyRequestUtil.getLongParameter(request, "stype"); // 资源类型ID
		long rmin_id = MyRequestUtil.getLongParameter(request, "rmin_id"); // 作业ID
		String rmty_name = MyRequestUtil.getStringParameter(request, "rmty_id"); // 中图法分类
		long sman_id = MyRequestUtil.getLongParameter(request, "userid"); // 获取当前登录用户ID
		
		int inputType = ServletRequestUtils.getIntParameter(request, "inputType", ResSourceData.AUTHORTYPE_ADMIN_INPUT);
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
		return resource;
	}
	
	public ResSourceData ParseResourceMetadata(ResSourceData resource, HttpServletRequest request, List<ResElementPool> elelist) {
		ResElementPool element = null;
		String eleid = "";
		String elename = "";
		// Date date = null;
		for(int i = 0, s = elelist.size(); i < s; i++) {
			element = elelist.get(i);
			elename = element.getRepoName().toLowerCase();
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
				String rsdaDate = MyRequestUtil.getStringParameter(request, eleid);
				resource.setRsdaDate(rsdaDate);
				if(rsdaDate.length()>=4){
					rsdaDate = rsdaDate.substring(0, 4);
					Long year = null;
					try{
						year = Long.parseLong(rsdaDate);
					}catch (Exception e) {
						year = null;
					}
					resource.setRsdaYear(year);
				}
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
}
