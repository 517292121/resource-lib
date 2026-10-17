package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IResDynamicTypeService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ExcelUtil;
import cn.com.adtech.riss2.core.FileNameParsingUtil;
import cn.com.adtech.riss2.core.TempletConfig;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.upload.IUploadService;

public class DynamicTypeController extends MultiActionController {

	private IResDynamicTypeService dynamicService = null;
	private IUploadService uploadService = null;
	public void setDynamicService(IResDynamicTypeService dynamicService) {
		this.dynamicService = dynamicService;
	}
	public void setUploadService(IUploadService uploadService) {
		this.uploadService = uploadService;
	}
	

	public static String getDynamicTypeByCode(int rdtyType){
		if(rdtyType == 1){
			return ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE);
		}
		if(rdtyType == 2){
			return ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO);
		}
		if(rdtyType == 3){
			return ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE);
		}
		return "";
	}
	
	/**
	 * 导出自定义分类
	 * @param request
	 * @param response
	 * @param page
	 * @throws Exception
	 */
	public void export(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		int rdtyType = ServletRequestUtils.getIntParameter(request, "rdtyType", 0);
		if(rmstId > 1){
			request.setAttribute("error", "未知特色库!");
		} 
		
		List<Object[]> list = this.dynamicService.getDynamicType(rmstId, rdtyType);
		String fileName = getDynamicTypeByCode(rdtyType);
		ExcelUtil excelUtil = new ExcelUtil();
		
		// 处理不同浏览器对文件名的解析问题
		FileNameParsingUtil.processFileName(request, fileName);
		response.setContentType("application/x-msdownload; charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename="+fileName+".xls");
		
		OutputStream outStream = null;
		try {
			String[] titles = new String[]{"分类名称","藏文名称","英文名称","分类号","上级分类号","排序号","级数"};
			outStream = response.getOutputStream(); 
			excelUtil.exportListToExcel(outStream, list, titles);
			outStream.flush();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			if(outStream != null) {
				try {
					outStream.close();
				}catch(IOException e) {
					e.printStackTrace();
				}
				outStream = null;
			}
		}
	}

	
	/**
	 * 进入导入模板设置界面
	 */
	public ModelAndView initImport(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L); //特色库ID
		int rdtyType = ServletRequestUtils.getIntParameter(request, "rdtyType", 0); //资源类型ID
		if(rmstId < 1){
			request.setAttribute("error", "所属特色库无效!");
			return new ModelAndView("resource/metadata/importGroup");
		}
		ResMetaStore store = (ResMetaStore) this.dynamicService.get(ResMetaStore.class, rmstId);
		
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("rmstId", rmstId);
		map.put("rdtyType", rdtyType);
		map.put("store", store);
		map.put("groupname", getDynamicTypeByCode(rdtyType));
		return new ModelAndView("resource/metadata/importGroup", map);
	}
	
	
	/**
	 * 批量导入分类
	 */
	public ModelAndView importGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L); //特色库ID
		int rdtyType = ServletRequestUtils.getIntParameter(request, "rdtyType", 0); //资源类型ID
		int beginRows = ServletRequestUtils.getIntParameter(request, "beginRow", 2); //起始行
		if(rmstId < 1){
			request.setAttribute("result", "所属特色库无效!");
			return initImport(request, response);
		}
		File xlsFile = uploadImportFile(request);
		if(xlsFile == null){
			request.setAttribute("result", "导入失败，文件上传出错!");
			return initImport(request, response);
		}
		String message = this.dynamicService.checkGroupExcel(xlsFile);
		if(CommonUtil.isNotNull(message)){
			request.setAttribute("result", message);
			return initImport(request, response);
		}
		message = this.dynamicService.checkGroupData(xlsFile, beginRows, rmstId, rdtyType);
		
		if(CommonUtil.isNotNull(message)){
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
			out.print("<span style='font-size:12px;'>" + message + "<br>");
			out.print("<br><a href='javascript:window.history.go(-1);'><<返回</a></span></span>");
			out.flush();
			return null;
		}
		
		message = this.dynamicService.saveImportExcel(xlsFile, request);
		if(CommonUtil.isNotNull(message)){
			request.setAttribute("result", message);
			return initImport(request, response); 
		}
		request.setAttribute("result", "导入成功！");
		return initImport(request, response);
	}
	
	
	/**上传EXCEL数据文件**/
	public File uploadImportFile(HttpServletRequest request){
		MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
		MultipartFile file = multiRequest.getFile("excelfile");
		String fileFolder = "dynamicExcel/";
		String filename = file.getOriginalFilename();
		try {
			String localname = filename.substring(filename.lastIndexOf(".")); // 扩展名
			localname = System.currentTimeMillis() + localname;
			uploadService.saveFile(fileFolder, localname, file.getBytes());
			String filepath = SysConfigInfo.FILE_ROOT_DIR + fileFolder + localname;
			
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
}
