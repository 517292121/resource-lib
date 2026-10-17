package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IImportTempletService;
import cn.com.adtech.riss2.bso.resource.IMetadataElementPoolService;
import cn.com.adtech.riss2.bso.resource.IMetadataTempletManagementService;
import cn.com.adtech.riss2.core.ExcelUtil;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResImportTemplet;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;

public class ExcelController extends MultiActionController {
	
	private IMetadataTempletManagementService metadataTempletService = null;
	protected IMetadataElementPoolService elementPool = null;
	private IImportTempletService importTempletService = null;

	public void setImportTempletService(IImportTempletService importTempletService) {
		this.importTempletService = importTempletService;
	}
	public IMetadataTempletManagementService getMetadataTempletService() {
		return metadataTempletService;
	}
	public void setMetadataTempletService(IMetadataTempletManagementService metadataTempletService) {
		this.metadataTempletService = metadataTempletService;
	}
	public void setElementPool(IMetadataElementPoolService elementPool) {
		this.elementPool = elementPool;
	}

	public ModelAndView exportExcelTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long store_id = ServletRequestUtils.getLongParameter(request, "storeId", 0);
		long sourceType = ServletRequestUtils.getLongParameter(request, "sourceType", 0);
		String rmstName = ServletRequestUtils.getStringParameter(request, "rmstName", "");
		String rltyName = ServletRequestUtils.getStringParameter(request, "rltyName", "");
		String[] eleNames = metadataTempletService.getTempletAllElementName(store_id, sourceType);
		if(eleNames == null) {
			request.setAttribute("result", "<script>alert('该模板没有任务元素，文件创建失败!');</script>");
			return new ModelAndView("resource/metadata/TempletSelectRetype");
		}else {
			ExcelUtil excelUtil = new ExcelUtil();
			excelUtil.setSheetName("录入模板");
			String fileName = rmstName + " - " + rltyName + " - 录入模板";
			// 处理不同浏览器对文件名的解析问题
			String agent = (String)request.getHeader("USER-AGENT");
			if(agent != null && agent.indexOf("MSIE") == -1) { // FF
				fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
				fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
			}else { // IE
				fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
			}
			response.setContentType("application/x-msdownload; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
			OutputStream outStream = null;
			try {
				outStream = response.getOutputStream();
				excelUtil.createExcelTemplet(eleNames, outStream);
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
			return null;
		}
	}
	
	
	public ModelAndView exportTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long store_id = ServletRequestUtils.getLongParameter(request, "storeId", 0);
		long sourceType = ServletRequestUtils.getLongParameter(request, "sourceType", 0);
		
		//获取模板中除元素外，其它分类和文件名的对应对象
		List<ResImportTemplet> templetList = elementPool.getTempletByGroup(store_id, sourceType);
		
		String rmstName = ServletRequestUtils.getStringParameter(request, "rmstName", "");
		String rltyName = ServletRequestUtils.getStringParameter(request, "rltyName", "");
		ResMetaStore store = (ResMetaStore) importTempletService.get(ResMetaStore.class, store_id);
		List<ResElementPool> elementList = elementPool.getTempletElement(store_id, sourceType);
		//Map<Long, String> map = importTempletService.getOtherElementData(store_id, sourceType);
		//elementList = parseOtherElement(elementList, map);
		if(elementList == null || elementList.size() < 1) {
			request.setAttribute("result", "<script>alert('该模板没有任何元素，文件创建失败!');</script>");
			return new ModelAndView("resource/metadata/TempletSelectRetype");
		}else {
			ExcelUtil excelUtil = new ExcelUtil();
			excelUtil.setSheetName("录入模板");
			String fileName = rmstName + " - " + rltyName + " - 录入模板";
			// 处理不同浏览器对文件名的解析问题
			String agent = (String)request.getHeader("USER-AGENT");
			if(agent != null && agent.indexOf("MSIE") == -1) { // FF
				fileName = new String(fileName.getBytes("ISO-8859-1"), "UTF-8");
				fileName = "=?UTF-8?B?" + (new String(Base64.encodeBase64(fileName.getBytes("UTF-8")))) + "?=";
			}else { // IE
				fileName = new String(fileName.getBytes("GBK"), "ISO-8859-1");
			}
			response.setContentType("application/x-msdownload; charset=utf-8");
			response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".xls");
			OutputStream outStream = null;
			try {
				ResFilesystem fileSystem = null;
				if(store.getRmstRfilId() != null && store.getRmstRfilId() > 0L){
					fileSystem = (ResFilesystem) importTempletService.get(ResFilesystem.class, store.getRmstRfilId());
				}
				outStream = response.getOutputStream();
				excelUtil.createExcelTemplet(elementList, outStream, store, fileSystem, rltyName, templetList);
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
			return null;
		}
	}
	
}