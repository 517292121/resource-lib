package cn.com.adtech.riss2.controller.console.resource.build;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IImportExcleResourceService;
import cn.com.adtech.riss2.bso.resource.IImportHistoryService;
import cn.com.adtech.riss2.bso.resource.IMissionInfoManagementService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.resource.ResImportHistory;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.framework.session.Pagination;

public class ImportExcelController extends MultiActionController{
	
	private long beginTimes;
	private long uploadEndTime;
	private long checkedEndtime;
	private long importEndTime;
	private String message = "";
	private IMissionInfoManagementService missionService = null;
	private IUploadService uploadService = null;
	private IImportExcleResourceService importService = null;
	private IImportHistoryService importHistoryService;
	
	public void setImportHistoryService(IImportHistoryService importHistoryService) {
		this.importHistoryService = importHistoryService;
	}
	public void setImportService(IImportExcleResourceService importService) {
		this.importService = importService;
	}
	public void setUploadService(IUploadService uploadService) {
		this.uploadService = uploadService;
	}
	public void setMissionService(IMissionInfoManagementService missionService) {
		this.missionService = missionService;
	}

	public ModelAndView doImport(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0L);
		String missionName = missionService.getMissionNameById(rminId);
		
		request.setAttribute("missionName", missionName);
		request.setAttribute("rminId", rminId);
		return new ModelAndView("resource/build/importResource");
	}
	
	private String getErrorMessage(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("<span style='color:red'><h2>错误提示：</h2><hr>");
		buffer.append("<span style='font-size:12px;'>" + message + "<br>");
		buffer.append("<br><a href='javascript:window.history.go(-1);'><<返回</a></span></span>");
		String result = buffer.toString();
		buffer = null;
		
		return result;
	}
	
	
	public void imp(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		//特色库ID
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0);
		//资源类型ID
		long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0); 
		//资源源文存放目录
		String resourceFolder = ServletRequestUtils.getStringParameter(request, "folder","");
		//excel文件名称，不带.xls
		String xlsName = ServletRequestUtils.getStringParameter(request, "xls","");
		
		//验证特色库ID
		if(rmstId < 1){
			out.print("导入失败！未知所属导入特色库。");
			out.flush();
			return;
		}
		
		//验证资源类型ID
		if(rltyId < 1){
			out.print("导入失败！未知导入资源所属资源类型。");
			out.flush();
			return;
		}
		
		//验证资源源文存放目录
		if(CommonUtil.isNotNull(resourceFolder) == false){
			out.print("导入失败！未指定资源源文目录文件夹。");
			out.flush();
			return;
		}
		
		//验证excel文件
		if(CommonUtil.isNotNull(xlsName) == false){
			out.print("导入失败！未指定的excle文件名。");
			out.flush();
			return;
		}
		
		//资源文件总目录
		String resourceRoot = SysConfigInfo.FILE_ROOT_DIR;
		//采集资源存放根目录
		String gatherResourceFolder = SysConfigInfo.GATHER_RES_DIR;
		//采集资源导入的Excle文件目录,在gatherResourceFolder下
		String gatherExcleFolder = SysConfigInfo.GATHER_RES_EXCEL_DIR;
		File xlsFile = new File(resourceRoot + "/" + gatherExcleFolder + "/" + xlsName + ".xls");
		
		//验证Excel文件是否存在
		if(xlsFile.exists() == false){
			out.print("导入失败！excle文件'"+xlsName+".xls'在服务器“"+xlsFile.getParent()+"”中不存在。");
			out.flush();
			return;
		}
		
		
		//验证Excel文件是否有效
		if(xlsFile.length() < 1 || xlsFile.canRead() == false){
			out.print("导入失败！excle文件'"+xlsName+".xls'为空或无法读取。");
			out.flush();
			return;
		}
		 
		ResMetaStore metaStore = (ResMetaStore)importService.get(ResMetaStore.class, rmstId);
		ResLiterType literType = (ResLiterType)importService.get(ResLiterType.class, rltyId);
		//验证特色库有效性
		if(metaStore == null){
			out.print("导入失败！所导入的特色库不存在。");
			out.flush();
			return;
		}
		//验证资源类型有效性
		if(literType == null){
			out.print("导入失败！所导入的资源类型不存在。");
			out.flush();
			return;
		}
		
		metaStore.setRmstFolder(gatherResourceFolder);
		
		//定义虚拟作业，主要是为了提供之类导入时需要从作业中得到的参数
		ResMissionInfo mission = new ResMissionInfo();
		mission.setRminId(null);  //采集资源作业ID默认为10010
		mission.setRminRmstId(rmstId);
		mission.setRminRltyId(rltyId);
		mission.setResMetaStore(metaStore);
		mission.setRminMissname(resourceFolder); //将以前用作业名称作业文件夹设置为手动传递文件夹名称
		
		//验证特色库对应的资源类型是否已设置模板
		boolean templetCheckedResult = this.importService.checkTemplet(rmstId, rltyId);
		if(templetCheckedResult == false){
			out.print("导入失败！特色库'"+metaStore.getRmstName()+"'中的'"+literType.getRltyName()+"'资源类型未设置Excle导入模板。");
			out.flush();
			return;
		}
		
		//验证分类
		boolean typesCheckResult = this.importService.checkTypes(request,mission,xlsFile);
		if(typesCheckResult == false){
			out.print(importService.getMessage());
			out.flush();
			return;
		}
		
		//封装基本信息
		ResSourceData basicData = new ResSourceData();
		basicData.setRsdaRminId(null);
		basicData.setRsdaRmstId(mission.getRminRmstId());
		basicData.setRsdaRltyId(mission.getRminRltyId());
		if(basicData.getRsdaRltyId() == null || basicData.getRsdaRltyId() < 1){
			Long gatherRltyId = ServletRequestUtils.getLongParameter(request, "rltyId");
			basicData.setRsdaRltyId(gatherRltyId);
		}
		try {
			SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
			basicData.setRsdaSmanId(sysManager.getSmanId());
		}catch(Exception e) {
			basicData.setRsdaSmanId(null);
		}
		basicData.setRsdaCmtstus(mission.getResMetaStore().getRmstCmtstus());
		basicData.setResMetaStore(mission.getResMetaStore());
		basicData.setResMissionInfo(mission);
		basicData.setRsdaAuthortype(ResSourceData.AUTHORTYPE_GATHER);
		
		List<Object> resultList = this.importService.importResource(xlsFile, request, null, basicData);
		
		//jsondata 如果返回的是"true"则成功导入，否则便是错误信息
		String jsondata = resultList.get(0).toString();
		out.print(jsondata);
		out.flush();
	}

	public ModelAndView importResourceData(HttpServletRequest request, HttpServletResponse response) throws Exception{
		message = "";
		beginTimes = System.currentTimeMillis(); //开始执行时间
		long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0L);
		ResMissionInfo mission = missionService.getResMissionInfoById(rminId); //获取作业对象
		
		//自定义分类根据分类名称或是分类号匹配，为true表示用户分类名称，否则用分类号。默认为分类名称
		int impGroupType = ServletRequestUtils.getIntParameter(request, "impGroupType", 0);
		boolean igt = impGroupType == 0 ? true : false; 
		this.importService.setImpDynamicName(igt);
		
		boolean b = checkform(request, mission.getResMetaStore());
		if(b == false){ //验证表单基本信息
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print(getErrorMessage());
			out.flush();
			return null;
		}
		
		boolean templetCheckedResult = this.importService.checkTemplet(mission.getRminRmstId(), mission.getRminRltyId());
		if(templetCheckedResult == false){
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			message = "请正确设置该作业所属特色库对应资源类型的Excel模板...";
			out.print(getErrorMessage());
			out.flush();
			return null;
		}
		
		//封装导入历史对象
		int checkMust = MyRequestUtil.getIntParameter(request, "checkMust");
		ResImportHistory rih = new ResImportHistory();
		rih.setRmhiRminId(rminId);
		rih.setRmhiAuthor(MyRequestUtil.getLoginUserId(request));
		rih.setRmhiCheckMust(checkMust);
		rih.setRmhiCheckType(0);
		rih.setRmhiFilefolder(mission.getRminMissname());
		rih.setRmhiImportDate(new Date());
		File file = uploadResourceFile(request, rih);
		uploadEndTime = System.currentTimeMillis(); //文件上传结束时间
		
		boolean typesCheckResult = this.importService.checkTypes(request,mission,file);
		if(typesCheckResult == false){
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
			out.print("<span style='font-size:12px;'>" + importService.getMessage() + "<br>");
			out.print("<br><a href='javascript:window.history.go(-1);'><<返回</a></span></span>");
			out.flush();
			return null;
		}
		if(checkMust < 1){
			boolean checkMustResult = this.importService.checkMustElement(request, file, mission);
			if(checkMustResult == false){
				response.setContentType("text/html; charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
				out.print("<span style='font-size:12px;'>" + importService.getMessage() + "<br>");
				out.print("<br><a href='javascript:window.history.go(-1);'><<返回</a></span></span>");
				out.flush();
				return null;
			}
		}
		checkedEndtime = System.currentTimeMillis(); //校验完成时间
		ResSourceData basicData = new ResSourceData();
		basicData.setRsdaRminId(rminId);
		basicData.setRsdaRmstId(mission.getRminRmstId());
		basicData.setRsdaRltyId(mission.getRminRltyId());
		basicData.setRsdaSmanId(MyRequestUtil.getLoginUserId(request));
		basicData.setRsdaCmtstus(mission.getResMetaStore().getRmstCmtstus());
		basicData.setResMetaStore(mission.getResMetaStore());
		basicData.setResMissionInfo(mission);
		basicData.setRsdaAuthortype(ResSourceData.AUTHORTYPE_ADMIN_IMPORT);
		
		//TODO 验证模板是否正确设置
		
		List<Object> resultList = this.importService.importResource(file, request, rih, basicData);
		importEndTime = System.currentTimeMillis(); //导入完成时间
		
		StringBuffer resultMessage = new StringBuffer();
		if(resultList.get(0).equals("true")){
			resultMessage.append("导入成功,共导入："+importService.getImportNumber()+" 条资源<hr>");
			resultMessage.append(this.importService.getMessage() + "<br>");
		}else{
			if(file.exists()){
				file.delete();
			}
			resultMessage.append("导入失败<hr>");
		}
		resultMessage.append("上传文件用时：" + ((uploadEndTime - beginTimes) / 1000.000) + "秒<br>");
		resultMessage.append("数据验证用时：" + ((checkedEndtime - uploadEndTime) / 1000.000) + "秒<br>");
		resultMessage.append("保存数据用时：" + ((importEndTime - checkedEndtime) / 1000.000) + "秒<br>");
		resultMessage.append("总共用时：<span style='color:red'>" + ((importEndTime - beginTimes) / 1000.000) + "</span>秒");
		
		request.setAttribute("result", resultMessage.toString());
		return doImport(request, response);
	}
	 
	/**验证表单基本信息**/
	public boolean checkform(HttpServletRequest request, ResMetaStore store){
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile("excelfile");
		if(file == null || file.getSize() <= 0 || !ServiceUtil.isNotNull(file.getOriginalFilename())) {
			message = "<li>excel文件不能为空!</li>";
			return false;
		}else{
			return true;
		}
	}
	
	
	/**上传EXCEL数据文件**/
	public File uploadResourceFile(HttpServletRequest request, ResImportHistory rih){
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile("excelfile");
		String fileFolder = SysConfigInfo.IMP_EXCEL_DIR;
		String filename = file.getOriginalFilename();
		rih.setRmhiLocalFilename(filename);
		try {
			String localname = filename.substring(filename.lastIndexOf(".")); // 扩展名
			localname = System.currentTimeMillis() + localname;
			uploadService.saveFile(fileFolder, localname, file.getBytes());
			String filepath = SysConfigInfo.FILE_ROOT_DIR + fileFolder + localname;
			
			rih.setRmhiFilename(fileFolder + localname);
			File localfile = new File(filepath);
			return localfile;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**************************************导入记录相关**************************************/
	/**进入导入记录界面**/
	public ModelAndView initImportHistoryList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		long rminId = MyRequestUtil.getLongParameter(request, "rminId");
//		String missionName = missionService.getMissionNameById(rminId);
		ResMissionInfo mission = missionService.getResMissionInfoById(rminId);
		String address = SysConfigInfo.FILE_URL_PATH;
		
		request.setAttribute("rminId", rminId);
		request.setAttribute("mission", mission);
		request.setAttribute("address", address);
		return new ModelAndView("resource/build/importHistory");
	}
	
	/**构造记录记录的JSON数据**/
	public void importHistoryList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long rminId = MyRequestUtil.getLongParameter(request, "rminId");

		String sort = ServletRequestUtils.getStringParameter(request, "sort", "rmhiImportDate"); //排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");  //排序方式
		if(sort.equals("sourceNum")){
			sort = "col_7_0_";
		}
		
		int start = ServletRequestUtils.getIntParameter(request, "start", 0); // 起始记录
		int limit = ServletRequestUtils.getIntParameter(request, "limit", 20); // 每页记录数
		int currentPage = start / limit;
		// 添加分页器
		Pagination pagination = new Pagination();
		// 设置当前页
		pagination.setCurrentPage(currentPage);
		// 设置每页记录数
		pagination.setPageResults(limit);
		
		List<ResImportHistory> list = importHistoryService.getImportHistoryList(rminId, pagination, dir, sort);
		String[] keyArr = new String[]{"rmhiId", "rmhiCheckMust", "rmhiFilefolder", "rmhiLocalFilename", "rmhiFilename", "rmhiAddress", "rmhiImportDate", "sourceNum"};
		String jsondata = CommonUtil.createJsonTable(list, keyArr, pagination.getTotalResults()+"");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	public ModelAndView initImportResourceList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rminId = MyRequestUtil.getLongParameter(request, "rminId");
		long rihiId = MyRequestUtil.getLongParameter(request, "rihiId");
		ResImportHistory importHistory = (ResImportHistory) importHistoryService.get(ResImportHistory.class, rihiId);
		ResMissionInfo mission = missionService.getResMissionInfoById(rminId);
		request.setAttribute("rihiId", rihiId);
		request.setAttribute("rminId", rminId);
		request.setAttribute("mission", mission);
		request.setAttribute("importHistory", importHistory);
		return new ModelAndView("resource/build/importResourceList");
	}

	
	public void importResourceList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long rihiId = MyRequestUtil.getLongParameter(request, "rihiId");
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		String sort = ServletRequestUtils.getStringParameter(request, "sort", "RSDA_BUILDDATE");
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");
		String jsondata = importHistoryService.getResourceByImportHistory(rihiId, begin, pageNum, sort, dir);
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	public void deleteHistory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		Long rmhiId = ServletRequestUtils.getLongParameter(request, "rmhiId",0L);
		try {
			importHistoryService.deleteImportHistory(rmhiId);
			out.print("1");
		}catch(Exception e) {
			e.printStackTrace();
			out.print("错误：" + e.getMessage());
		}
		out.flush();
	}
	
	public void deleteHistoryAndResource(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		Long rmhiId = ServletRequestUtils.getLongParameter(request, "rmhiId",0L);
		try {
			importHistoryService.deleteImportHistoryAndResource(rmhiId);
			out.print("1");
		}catch(Exception e) {
			e.printStackTrace();
			out.print("错误：" + e.getMessage());
		}
		out.flush();
	}
	
	/**
	 * 进入资源导入介绍说明页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView impdesc(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0L);
		request.setAttribute("rminId", rminId);
		return new ModelAndView("resource/build/impDescription");
	}
}
