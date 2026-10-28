package cn.com.adtech.riss2.controller.console.resource.build;

import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IMetadataElementPoolService;
import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.bso.resource.ISourceLanguageService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.file.WordToPdf;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResLanguageType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

public class ResourceController extends MultiActionController {

	private IResourceDataService resourceService = null;
	private ISourceLanguageService sourceLanguageService = null;
	private IMetadataElementPoolService elementPool = null;
	
	public void setSourceLanguageService(ISourceLanguageService sourceLanguageService) {
		this.sourceLanguageService = sourceLanguageService;
	}
	public void setResourceService(IResourceDataService resourceService) {
		this.resourceService = resourceService;
	}
	public void setElementPool(IMetadataElementPoolService elementPool) {
		this.elementPool = elementPool;
	}

	/**
	 * 进入资源列表
	 */
	public ModelAndView resourcelist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return new ModelAndView("resource/build/resourcelist");
	}

	/**
	 * 删除资源
	 */
	public void deleteResource(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		long sourceId = MyRequestUtil.getLongParameter(request, "sourceId");
		long languageId = MyRequestUtil.getLongParameter(request, "languageId");
		boolean b = resourceService.deleteResource(sourceId, languageId,
				request);

		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		if (b) {
			out.write("{success:true,msg:'资源删除成功！'}");
		} else {
			out.write("{success:true,msg:'对不起，删除失败！'}");
		}
		out.flush();
	}

	/**
	 * 翻译的资源列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void interpretSourceList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		long rmin_id = MyRequestUtil.getLongParameter(request, "RSDA_RMIN_ID");
		long language = ServletRequestUtils.getLongParameter(request, "lanId", 0);
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		String title = ServletRequestUtils.getStringParameter(request, "title","").trim();
		int statu = ServletRequestUtils.getIntParameter(request, "statu", 0);

		String sort = ServletRequestUtils.getStringParameter(request, "sort", "RSLA_DATE"); //排序字段
		String dir = ServletRequestUtils.getStringParameter(request, "dir", "DESC");  //排序方式
		resourceService.setParams(" and rsd.RSDA_STATUS=3 ");
		String jsondata = resourceService.getResourceListJsonByMission(rmin_id,language, begin, pageNum, sort, dir, title, statu);

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}

	/**
	 * 翻译的资源语种列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public ModelAndView interpretSourceLanguageList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		long sourceId = MyRequestUtil.getLongParameter(request, "sourceId");
		long rminId = ServletRequestUtils.getLongParameter(request, "rminId", 0);
		if(rminId == 0){
			rminId = resourceService.getSourceRminId(sourceId);
		}
		request.setAttribute("sourceId", sourceId);
		request.setAttribute("rminId", rminId);
		request.setAttribute("rminStatu", ServletRequestUtils.getIntParameter(request, "rminStatu",0));
		return new ModelAndView("resource/build/interpretReousrceLanguageList");
	}

	/**
	 * 翻译的资源语种列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void interpretSourceLanguageListJson(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long sourceId = MyRequestUtil.getLongParameter(request, "sourceId");
		int begin = MyRequestUtil.getIntParameter(request, "start");
		int pageNum = MyRequestUtil.getIntParameter(request, "limit");
		String type = MyRequestUtil.getStringParameter(request, "type");
		
		int statu = MyRequestUtil.getIntParameter(request, "statu");
		String where = "";
		if(statu > 0){
			where = " and rsl.RSLA_STATUS=3 and rsl.RSLA_ISFIRST=0 ";
		}
		if("1".equals(type))
			where = " and rsl.RSLA_STATUS !=3 and rsl.RSLA_ISFIRST=0";

		String jsondata = sourceLanguageService.getSourceLanguageListJson(
				sourceId, begin, pageNum,where);

		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}

	/**
	 * 删除资源的具体语种
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void deleteSourceByLanguage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long sourceId = MyRequestUtil.getLongParameter(request, "sourceId");
		long languageId = MyRequestUtil.getLongParameter(request, "languageId");

		boolean b = resourceService.deleteResourceByLanguage(sourceId, languageId);
		String jsondata = "";
		if (b) {
			jsondata = "1";
		} else {
			jsondata = "0";
		}
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.print(jsondata);
		out.flush();
	}
	
	/**
	 * 修改资源其它语信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView interpertSourceEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int reqType = ServletRequestUtils.getIntParameter(request, "reqType", 1);
		long sourceId = MyRequestUtil.getLongParameter(request, "sid");
		long languageId = ServletRequestUtils.getLongParameter(request, "languageId", 0);
		if(languageId == 0){
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<h2>错误提示：</h2><hr><span style='font-size:12px;color:red'>该资源语种不明确。</span>");
			out.print("<a href='javascript:window.history.go(-1)' style='font-size:12px'>返回</a>");
			out.flush();
			return null;
		}
		ResSourceData source = resourceService.getResourceMetaInfo(sourceId);
		
		//获取语种选项options列表
        String passLanguageOption = sourceLanguageService.getSourceLanguageOption(sourceId, 4, languageId); //已审核通过的语种
        
        ResLanguageType language = sourceLanguageService.getLanguage(languageId);
		List<ResElementPool> EleDataList = resourceService.getResourceAllData(source.getRsdaRmstId(), source.getRsdaRltyId(), languageId, sourceId);   
		
		request.setAttribute("passLanguage", passLanguageOption);
		request.setAttribute("resource", source);
		request.setAttribute("EleDataList", EleDataList);
		request.setAttribute("reqType", reqType);
		request.setAttribute("languageId", languageId);
		request.setAttribute("languageOption", language);
		
		
		return new ModelAndView("resource/build/interpretEdit");
	}
	
	/**
	 * 保存修改的资源其它语
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView saveInterpretEdit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String errorMsg = "";
		
		int reqType = ServletRequestUtils.getIntParameter(request, "reqType", 1);
		long newLanguage = ServletRequestUtils.getLongParameter(request, "newLanguage", 0);
		long sourceId = MyRequestUtil.getLongParameter(request, "sourceId");
		long languageId = ServletRequestUtils.getLongParameter(request, "languageId", 0);
		long store = MyRequestUtil.getLongParameter(request, "store");  //特色库ID
		long sourceType = MyRequestUtil.getLongParameter(request, "stype");  //资源类型ID
		List<ResElementPool> elementList = elementPool.getElementByTemplet(store, sourceType);
		
		String sql = "";
		String eleid = "";
		String data;
		boolean b ; 
		
		List<String> sqlList = new LinkedList<String>();
		//通过资源的模板，循环更新所有元素值
		for(int i=0,s=elementList.size(); i<s; i++){
			eleid = elementList.get(i).getRepoId().toString();
			data = MyRequestUtil.getStringParameter(request, eleid);
			sql = "update RES_EXTEND_DATA set REDA_DATA='"+data+"'";
			if(languageId != newLanguage){
				sql += " ,REDA_LANGUAGE=" + newLanguage;
			}
			sql += " where REDA_RSDA_ID="+sourceId+" and REDA_REPO_ID="+eleid+" and REDA_LANGUAGE=" +languageId ;
			sqlList.add(sql);
		}
		b = this.resourceService.executeMultiSql(sqlList);
		if(b == false){
			errorMsg += "语种修改失败！";
		}
		
		if(newLanguage != languageId){
			boolean res = sourceLanguageService.updateSourceLanguage(sourceId, languageId, newLanguage);
			if(res == false){
				errorMsg += "语种修改失败！";
			}
		}
		
		if(errorMsg.length() < 2){
			errorMsg = "<script>showSuccessMessage('"+request.getContextPath()+"/');</script>";
		}else{
			errorMsg = "<script>alert('"+errorMsg+"');</script>";
		}
		request.setAttribute("result", errorMsg);
		
		request.setAttribute("rminId", ServletRequestUtils.getLongParameter(request, "rminId",0));
		
		if(reqType == 1){
			return new ModelAndView("resource/build/interpretResourceList");
		}else{
			return new ModelAndView("resource/manager/sourceLanguageList");
		}
		
	}
	
	/**
	 * 资源预览
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView sourceView(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int fileType = ServletRequestUtils.getIntParameter(request, "type", 0);
		long sourceId = ServletRequestUtils.getLongParameter(request, "sourceId", 0L);
		ResSourceData resource = resourceService.getResourceMetaInfo(sourceId);
		if (fileType==3) {
			Object session=request.getSession().getAttribute("userSession");
			if (!(session instanceof cn.com.adtech.riss2.core.CurrentUserSession)
					|| !((cn.com.adtech.riss2.core.CurrentUserSession)session).isManager()
					|| ServiceUtil.getCurrentUser(request)==null) {
				response.sendError(403,"请先登录后台"); return null;
			}
			if (resource==null) { response.sendError(404,"源文件所属资源不存在"); return null; }
			if (cn.com.adtech.riss2.core.file.TextFilePreview.supports(resource)) {
				ModelAndView view=new ModelAndView("platform/pc/browse/textDirectory");
				view.addObject("resource",resource);view.addObject("title",resource.getRsdaTitle());view.addObject("consolePreview",true);
				view.addObject("idcode",CommonUtil.idToString(String.valueOf(sourceId)));
				try { view.addAllObjects(cn.com.adtech.riss2.core.file.TextFilePreview.readerModel(resource)); }
				catch (Exception previewError) {
					logger.warn("TXT preview failed for resource "+sourceId,previewError);
					view=new ModelAndView("platform/pc/browse/error");
					view.addObject("message","文本文件暂时无法预览，请检查源文件是否存在、编码是否正确及文件是否超过10MB。");
				}
				return view;
			}
			if (cn.com.adtech.riss2.core.file.SpreadsheetPreview.supports(resource)) {
				ModelAndView view=new ModelAndView("platform/pc/browse/spreadsheet");
				view.addObject("resource",resource);view.addObject("title",resource.getRsdaTitle());view.addObject("consolePreview",true);
				view.addObject("consoleSourceId",sourceId);
				try { view.addObject("excel",cn.com.adtech.riss2.core.file.SpreadsheetPreview.read(cn.com.adtech.riss2.core.file.SpreadsheetPreview.resourceFile(resource),
						ServletRequestUtils.getIntParameter(request,"sheet",-1),ServletRequestUtils.getIntParameter(request,"page",1),ServletRequestUtils.getIntParameter(request,"columns",1))); }
				catch (Exception previewError) {
					logger.warn("Excel preview failed for resource "+sourceId,previewError);
					view.addObject("excelError","该 Excel 文件暂时无法预览，请检查文件是否完整、加密或超出预览容量。");
				}
				return view;
			}
		}
		String sourceType = "";
		String fileUrl = "";
		String fileName = "";
		String fileAddress = "";
		String sourceTitle="";
		if(fileType == 1){  //封面那个文件
			fileUrl = resource.getRsdaBigimgUrl() + "&amp;isOnLine=true";
			fileName = resource.getRsdaBigimgName();
			sourceType = ServiceUtil.getFileTypeBySpread(fileName);
		}else if(fileType == 3){ //源文件
			fileUrl = resource.getRsdaFileSwfUrl();
			fileName = resource.getRsdaFileName();
			String format = resource.getRsdaFileType();
			
			String swf = resource.getRsdaFileSwfUrl();
			if(CommonUtil.isNotNull(format) && (!CommonUtil.isNotNull(swf) || !swf.startsWith("/FileDownload"))){
				format = format.toLowerCase();
				
				if(format.equals("pdf")){
					String fileAccessUrl = SysConfigInfo.FILE_ROOT_DIR + resource.getRsdaFileLocalName();
					fileAccessUrl = CommonUtil.encode(fileAccessUrl);
					fileAccessUrl = fileAccessUrl.replace("=", "%3D");
					
					request.setAttribute("fileUrl", fileAccessUrl);
					request.setAttribute("title", resource.getRsdaTitle());
					return new ModelAndView("common/pdfviewer");
				}
				if(format.equals("doc") || format.equals("docx")){
					if(!CommonUtil.isNotNull(resource.getRsdaFileSwfUrl())){
						//word转换为pdf，采用pdf.js预览
						String wordadd = resource.getRsdaFileLocalName();
						String pdfUrl = WordToPdf.conversionInit(SysConfigInfo.FILE_ROOT_DIR, wordadd);
						
						resource.setRsdaFileSwfUrl(pdfUrl);
					}
					
					if(!CommonUtil.isNotNull(resource.getRsdaFileSwfUrl())){
						request.setAttribute("filemsg", "该资源暂无可预览文件！");
						return new ModelAndView("platform/pc/resBrowse");
					}
					
					String viewUrl = resource.getRsdaFileSwfUrl();
					if(!viewUrl.startsWith("/FileDownload")){
						//如果预览文件路径为FileDownload开头，说明是swf预览模式。还是用传统swf预览
						String fileAccessUrl = SysConfigInfo.FILE_ROOT_DIR + viewUrl;
						fileAccessUrl = CommonUtil.encode(fileAccessUrl);
						fileAccessUrl = fileAccessUrl.replace("=", "%3D");
						
						request.setAttribute("fileUrl", fileAccessUrl);
						request.setAttribute("title", resource.getRsdaTitle());
						return new ModelAndView("common/pdfviewer");
					}
				}
			}
			
			
			sourceType = ServiceUtil.getFileTypeBySpread(fileName);
			if(sourceType.equals("photo") && fileUrl == null){
				fileUrl = resource.getRsdaFileUrl();
			}
			fileUrl += "&amp;isOnLine=true";
			if(sourceType.equals("video")){
				fileUrl = fileUrl.split("&")[0];
			}
		}
		//预览特色库封面
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		if(rmstId > 0){
			ResMetaStore rmst = (ResMetaStore)this.resourceService.get(ResMetaStore.class, rmstId);
			sourceType ="photo";
			fileUrl = rmst.getRmstLogo();
			fileAddress = SysConfigInfo.FILE_URL_PATH;
			sourceTitle = rmst.getRmstName();
			fileName ="logo";
		}else{
			fileAddress = SysConfigInfo.FILE_URL_PATH;
			sourceTitle = resource.getRsdaTitle();
		}
		request.setAttribute("sourceType", sourceType);
		request.setAttribute("fileUrl", fileUrl);
		request.setAttribute("fileAddress", fileAddress);
		request.setAttribute("fileName", fileName);
		request.setAttribute("sourceTitle", sourceTitle);
		
				
		return new ModelAndView("resource/build/FileView");
	}
}














