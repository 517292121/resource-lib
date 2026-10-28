package cn.com.adtech.riss2.controller.platform;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bean.CatalogBean;
import cn.com.adtech.riss2.bean.ImgViewBean;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.MyFileCatalogUtil;
import cn.com.adtech.riss2.core.ParseUrl;
import cn.com.adtech.riss2.core.file.FileDownloadFactory;
import cn.com.adtech.riss2.core.file.MyFileUtil;
import cn.com.adtech.riss2.core.file.ResourceDirectoryFiles;
import cn.com.adtech.riss2.core.file.SpreadsheetPreview;
import cn.com.adtech.riss2.core.file.WordToPdf;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.unionlib.lucene.base.AnalyzerUtil;
import cn.com.unionlib.lucene.base.IoCommonUtil;
import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.lucene.bean.ResPageBean;
import cn.com.unionlib.lucene.dao.ILuceneSearchService;

/**
 * 
 * 资源在线预览控制
 *
 * @author MaChao
 *
 * @date：Aug 9, 2018
 */
@Controller
public class ResBrowseController extends ResCommonController{
	
	@Resource(name="luceneSearchService")
	private ILuceneSearchService luceneSearchService;

	//进入资源独立预览页面（PC版）
	@RequestMapping(value="/res/browse/{rsdaId}")
	public String resBrowsePc(@PathVariable String rsdaId, Model model, HttpServletRequest request) {
		
		String error = getResDetail(rsdaId, model, request);
		if(CommonUtil.isNotNull(error)){
			model.addAttribute("errormsg", error);
			return "platform/pc/error";
		}
		try {
			Map<String, Object> map = model.asMap();
			ResSourceData resource = (ResSourceData) map.get("resource");
			if (cn.com.adtech.riss2.core.file.TextFilePreview.supports(resource)) {
				if (!(map.get("userRight") instanceof Number) || ((Number)map.get("userRight")).intValue()<4) {
					request.setAttribute("message","您没有该资源的阅读权限！");
					return "platform/pc/browse/error";
				}
				try { model.addAllAttributes(cn.com.adtech.riss2.core.file.TextFilePreview.readerModel(resource)); }
				catch (Exception previewError) {
					org.apache.commons.logging.LogFactory.getLog(getClass()).warn("TXT preview failed for resource "+resource.getRsdaId(),previewError);
					request.setAttribute("message","文本文件暂时无法预览，请检查源文件是否存在、编码是否正确及文件是否超过10MB。");
					return "platform/pc/browse/error";
				}
				return "platform/pc/browse/textDirectory";
			}
			if ((resource.getRsdaSourceType()==null || resource.getRsdaSourceType()==0) && SpreadsheetPreview.supports(resource)) {
				if (!(map.get("userRight") instanceof Number) || ((Number)map.get("userRight")).intValue()<4) {
					request.setAttribute("message","您没有该资源的阅读权限！");
					return "platform/pc/browse/error";
				}
				try {
					model.addAttribute("excel",SpreadsheetPreview.read(SpreadsheetPreview.resourceFile(resource),
							ServletRequestUtils.getIntParameter(request,"sheet",-1),ServletRequestUtils.getIntParameter(request,"page",1),
							ServletRequestUtils.getIntParameter(request,"columns",1)));
				} catch (Exception previewError) {
					org.apache.commons.logging.LogFactory.getLog(getClass()).warn("Excel preview failed for resource "+resource.getRsdaId(),previewError);
					model.addAttribute("excelError","该 Excel 文件暂时无法预览，请检查文件是否完整、加密或超出预览容量。");
				}
				return "platform/pc/browse/spreadsheet";
			}
			if(resource.getRsdaSourceType() == null || resource.getRsdaSourceType() == 0){
				String pdfurl = null;
				if(CommonUtil.isNotNull(resource.getRsdaFileType())){
					String format = resource.getRsdaFileType().toLowerCase();
					String swf = resource.getRsdaFileSwfUrl();
					if(CommonUtil.isNotNull(swf) && swf.startsWith("/FileDownload")){
						//如果有swf文件，则采用传统模式预览
						pdfurl = null;
					}else{
						if(format.equals("pdf")){
							pdfurl = resource.getRsdaFileLocalName();
						}
						if(format.equals("doc") || format.equals("docx")){
							if(!CommonUtil.isNotNull(swf)){
								//word转换为pdf，采用pdf.js预览
								String wordadd = resource.getRsdaFileLocalName();
								String pdfUrl = WordToPdf.conversionInit(SysConfigInfo.FILE_ROOT_DIR, wordadd);
								
								resource.setRsdaFileSwfUrl(pdfUrl);
								
								this.resourceSearchService.save(resource);	//word转成pdf后将pdf路径保存到数据库
							}
							pdfurl = resource.getRsdaFileSwfUrl();
						}
					}
				}
				if(CommonUtil.isNotNull(pdfurl)){
					String key = ServletRequestUtils.getStringParameter(request, "key","");
					int page = ServletRequestUtils.getIntParameter(request, "page",0);
					if(page==0){
						page = ServletRequestUtils.getIntParameter(request, "p",0);
					}
					boolean bright = ServletRequestUtils.getBooleanParameter(request, "bright",false);
					request.setAttribute("key", key);	// pdf的关键词
					request.setAttribute("page", page > 0 ? page : "");	// pdf跳转的页码
					request.setAttribute("bright", bright == true ? true : false);	// 是否高亮关键词
					request.setAttribute("title", resource.getRsdaTitle());
					
					String fileAccessUrl = pdfurl + "&T" + FileDownloadFactory.getInstance().getTime();
					fileAccessUrl = CommonUtil.encode(fileAccessUrl);
					fileAccessUrl = fileAccessUrl.replace("=", "%3D");
					request.setAttribute("fileUrl", fileAccessUrl);
					return "common/pdfviewer";
				}
				return "platform/pc/browse/fileview";
								
			} 
			else if(resource.getRsdaSourceType() == 1){
				return imgBrowser(resource, model, request);
			}
			else if(resource.getRsdaSourceType() == 2){
				String htmlUrl = resource.getRsdaFileUrl();
				if(!CommonUtil.isNotNull(htmlUrl)){
					throw new ErrorMessageException("该资源暂无可预览文件！");
				}
				boolean isConn = ParseUrl.urlIsConnect(htmlUrl);
				if(isConn == false){
					throw new ErrorMessageException("对不起，该资源暂时不可预览！");
				}
				return "platform/pc/browse/htmlview";
			}
			else{
				model.addAttribute("errormsg", "该资源文件已删除!");
				return "platform/pc/error";
			}
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			request.setAttribute("message", e.getMessage());
			return "platform/pc/browse/error";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "请求错误：" + e.getMessage());
			return "platform/pc/browse/error";
		}
	}
	
	private String imgBrowser(ResSourceData resource, Model model, HttpServletRequest request) throws UnsupportedEncodingException {
		String path = resource.getRsdaFileUrl();
		try {
			if(!CommonUtil.isNotNull(path)){
				throw new ErrorMessageException("该资源没有可预览文件！");
			}
			
			File imgDir = new File(path);
			if(!imgDir.exists() || !imgDir.isDirectory()){
				throw new ErrorMessageException("该资源预览文件目录无效！");
			}
			if (useTextDirectoryReader(path, request)) {
				return textDirectoryBrowser(resource, request);
			}
			
			File[] fileArr = imgDir.listFiles();
			if(fileArr == null || fileArr.length < 1){
				throw new ErrorMessageException("该资源预览文件为空！");
			}
			
			//获取是否有目录
			File catalogFile = MyFileUtil.getXlsByDirname(path);
			if(catalogFile == null){
				request.setAttribute("isCatalog", false);
			}else{
				request.setAttribute("isCatalog", true);
			}
			
			//判断页面是否有源文txt
			boolean haveSrcTxt = false;
			String dirname = imgDir.getName();
			for(File f : fileArr){
				if(f.isDirectory()){
					continue;
				}
				String filename = f.getName();
				if(filename.indexOf(".") < 0){
					continue;
				}
				
				String fname = filename.substring(0, filename.lastIndexOf("."));
				String fmt = filename.substring(filename.lastIndexOf(".")+1).toLowerCase();
				if(fmt.equals("txt") && !fname.equals(dirname)){
					haveSrcTxt = true;
					break;
				}
			}
			request.setAttribute("haveSrcTxt", haveSrcTxt);
			
			//判断页面是否有源文pdf
			boolean haveSrcPdf = false;
			File pdf = new File(imgDir.getPath(), imgDir.getName()+".pdf");
			//如果小写的.pdf不存在时则查询大写的后缀为PDF
			if(!pdf.exists()){
				pdf = new File(imgDir.getPath(), imgDir.getName()+".PDF");
			}
			if(pdf.exists()){
				haveSrcPdf = true;
				request.setAttribute("pdfPath", CommonUtil.encode(pdf.getAbsolutePath()));
			}
			request.setAttribute("haveSrcPdf", haveSrcPdf);
			
			try {
				path = CommonUtil.encode(path);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			//关键词
			String key = ServletRequestUtils.getStringParameter(request, "key", "");
			if (CommonUtil.isNotNull(key)) {
				int match = ServletRequestUtils.getIntParameter(request, "match", 0);
				if (match == 1) {
					// 精确检索时不做分词高亮
					request.setAttribute("keys", key);
				}else{
					// 非精确检索时才做分词高亮
					List<String> keyList = AnalyzerUtil.displayToken(key);
					String keys = "";
					for (int i = 0, s = keyList.size(); i < s; i++) {
						keys += keyList.get(i);
						if (i < s - 1) {
							keys += "、";
						}
					}
					request.setAttribute("keys", keys);
				}
			}
			
			//当前页
			String currname = ServletRequestUtils.getStringParameter(request, "p", "");
			if(CommonUtil.isNotNull(currname)){
				//有指定选中图片时，用指定的图片做为默认显示页
				String currXml = resource.getRsdaFileUrl() + "/" + currname + ".xml";
				File currXmlFile = new File(currXml);
				if(currXmlFile.exists()){
					request.setAttribute("currxml", URLEncoder.encode(currXmlFile.getAbsolutePath(), "utf-8"));	
					request.setAttribute("currname", currname);	
				}
			}else{
				//没有指定选中图片时，用第一张图片作为默认显示
				String filename = null;
				String fmt = null;
				for (File f : fileArr) {
					if (f.isDirectory()) {
						continue;
					}
					filename = f.getName();
					String fname = filename.substring(0, filename.lastIndexOf("."));
					// 过滤filters
					if (fname.endsWith("_small")) {
						continue;
					}
					// 过滤封面
					if (fname.toLowerCase().equals("cover")) {
						continue;
					}
					fmt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
					if (fmt.equals("xml")) { // 跳过切片的xml配置文件
						continue;
					}
					if (fmt.equals("txt")) { // 跳过txt源文
						continue;
					}
					if (fmt.equals("jpg") || fmt.equals("jpeg") || fmt.equals("png") || fmt.equals("gif") || fmt.equals("bmp")) {
						String firstXml = URLEncoder.encode(f.getParent() + "\\" + fname + ".xml", "utf-8");
						request.setAttribute("currxml", firstXml);
						request.setAttribute("currname", fname);
						break;
					}
				}
			}
			
			request.setAttribute("imgDir", path);
			return "platform/pc/browse/imgBrowse";
			
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			request.setAttribute("message", e.getMessage());
			return "platform/pc/browse/error";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "请求错误：" + e.getMessage());
			return "platform/pc/browse/error";
		}
	}
	

	//进入数据检索子页面
	@RequestMapping(value="/res/pagesearch/{rsdaId}")
	public String inPageSearch(@PathVariable Long rsdaId, HttpServletRequest request) throws UnsupportedEncodingException {
		String hql = "select rsda.rsdaFileUrl from ResSourceData rsda where rsda.rsdaId=" + rsdaId;
		String imgDir = this.resourceSearchService.getSingleData(hql);
		boolean isSrc = false;
		if(CommonUtil.isNotNull(imgDir)){
			List<File> txtList = MyFileUtil.getDirTxtFile(imgDir);
			if(txtList != null && txtList.size() > 1){
				isSrc = true;
			}
			txtList = null;
			
			String lastChar = imgDir.substring(imgDir.length()-1);
			if(!lastChar.equals("/") && !lastChar.equals("\\")){
				imgDir += "/";
			}
			imgDir = URLEncoder.encode(imgDir, "utf-8");
		}
		request.setAttribute("havetxt", isSrc);
		request.setAttribute("rsdaId", rsdaId);
		request.setAttribute("imgDir", imgDir);
		return "platform/pc/browse/pageSearch";
	}
	
	//lucene检索资源子页
	@RequestMapping(value="/lucene/pageSearch")
	public void lucenePageSearchJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
//		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		Map<String, Object> map = new HashMap<String, Object>();
		Map<String, String> params = new HashMap<String, String>();
		
		//分页设置
		int currentPage = ServletRequestUtils.getIntParameter(request, "pn", 1);
		int size = 200;  //将页面数量设置大一些,直接一页展示完
		PageBean page = new PageBean();
		page.setCurrentPage(currentPage);
		page.setPageSize(size);
		try {
			String resId = ServletRequestUtils.getStringParameter(request, "resId", "");
			String k = ServletRequestUtils.getStringParameter(request, "key", "");
			String match = ServletRequestUtils.getStringParameter(request, "match", "0");
			params.put("match", match);
			
			
			if(CommonUtil.isNotNull(k)){
				if(match.equals("1")){
					map.put("keys", k);
				}else{
					String keys = "";
					List<String> keyList = AnalyzerUtil.displayToken(k);
					for(int i=0,s=keyList.size(); i<s; i++){
						keys += keyList.get(i);
						if(i < s-1){
							keys += "、";
						}
					}
					
					map.put("keys", keys);
				}
			}
			
			if(!CommonUtil.isNotNull(resId)){
				throw new ErrorMessageException("未知资源！");
			}
			params.put("resId", resId);
			if(CommonUtil.isNotNull(k)){
				params.put("key", k);
			}
			
			List<ResPageBean> dataList = this.luceneSearchService.pageSearch(page, params);
			
			//对标题（文件名）进行编码，用于图片预览时检索结果后对资源定位
			int isEncode = ServletRequestUtils.getIntParameter(request, "encode", 0);
			if(isEncode == 1 && dataList != null){
				for(ResPageBean p : dataList){
					p.setTitleEncode(URLEncoder.encode(p.getTitle(), "utf-8"));
				}
			}
			
			map.put("success", true);
			map.put("currentPage", page.getCurrentPage());
			map.put("totalPage", page.getCountPage());
			map.put("totalLine", page.getCountRows());
			map.put("pageSize", size);
			map.put("dataList", dataList);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
			System.out.println("JSON:" + json);
			out.flush();
			out.close();
		}
	}
	
	@RequestMapping(value="/imgview/json")
	public void imgListJson(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		
		long begin = System.currentTimeMillis();
		String json = "";
		File jsonFile = null;
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String path = ServletRequestUtils.getStringParameter(request, "imgDir", "");
			if (!CommonUtil.isNotNull(path)) {
				throw new ErrorMessageException("文件路径为空");
			}
			path = CommonUtil.decode(path);
			File imgDir = new File(path);
			if (!imgDir.exists() || !imgDir.isDirectory()) {
				throw new ErrorMessageException("该资源文件路径无效");
			}
			
			jsonFile = new File(path, "config.json");
			if(jsonFile.exists()){
				json = IoCommonUtil.readTxt(jsonFile.getAbsolutePath());
				return;
			}
			
			File[] fileArr = imgDir.listFiles();
			if (fileArr == null || fileArr.length < 1) {
				throw new ErrorMessageException("该资源没有相关图片");
			}
	
			// 对目录中的图片创建缩略图
			boolean split = ServletRequestUtils.getBooleanParameter(request, "split", false);
			if(split){
				MyFileUtil.imgSplit(path);
			}
	
			String filename = null; // 原图名称
			String covername = null; // 封面（缩略图）名称
			String fmt = null;
			File coverFile = null;
			List<ImgViewBean> imgList = new ArrayList<ImgViewBean>();
			ImgViewBean img = null;
			for (File f : fileArr) {
				if (f.isDirectory()) {
					continue;
				}
				filename = f.getName();
				String fname = filename.substring(0, filename.lastIndexOf("."));
				// 过滤filters
				if (fname.endsWith("_small")) {
					continue;
				}
				// 过滤封面
				if (fname.toLowerCase().equals("cover")) {
					continue;
				}
				fmt = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
				if (fmt.equals("xml")) { // 跳过切片的xml配置文件
					continue;
				}
				if (fmt.equals("txt")) { // 跳过txt源文
					continue;
				}
				if (fmt.equals("jpg") || fmt.equals("jpeg") || fmt.equals("png") || fmt.equals("gif") || fmt.equals("bmp")) {
					covername = fname + "_small." + fmt;
					coverFile = new File(f.getParent(), covername);
					
					img = new ImgViewBean();
					img.setSmallpath(URLEncoder.encode(coverFile.getAbsolutePath(), "utf-8"));
					img.setXmlpath(URLEncoder.encode(f.getParent() + "\\" + fname + ".xml", "utf-8"));
					img.setFilename(fname);
					imgList.add(img);
				}
			}
			map.put("success", true);
			map.put("imgList", imgList);
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			if(!CommonUtil.isNotNull(json)){
				json = JSONObject.fromObject(map).toString();
				if((Boolean) map.get("success")){
					FileUtils.writeStringToFile(jsonFile, json, IoCommonUtil.TXT_FILE_CODE);
				}
			}
			
			long end = System.currentTimeMillis();
			System.out.println("异步加载用时：" + (end-begin));
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	//获取原文内容
	@RequestMapping(value="/page/srctxt")
	public void getSrctxt(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
//		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();

		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String path = ServletRequestUtils.getStringParameter(request, "txtpath", "");
			if (!CommonUtil.isNotNull(path)) {
				throw new ErrorMessageException("没有原文件");
			}
			File txtFile = new File(path);
			if (!txtFile.exists()) {
				throw new ErrorMessageException("本页文本正在加工中...");
			}
			
			String srctxt = IoCommonUtil.readTxt(txtFile.getAbsolutePath());
			srctxt = CommonUtil.hangleTextArea(srctxt);
			
			String[] lines = srctxt.split("<br/>");
			StringBuffer sb = new StringBuffer();
			for(String line : lines) {
				sb.append("<p>").append(line).append("</p>");
			}
			srctxt = sb.toString();
			sb = null;
			
			map.put("txt", srctxt);
		} catch (ErrorMessageException e) {
			map.put("txt", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("txt", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	
	//进入资源目录
	@RequestMapping(value="/res/catalog/{dir}")
	public String inCatalog(@PathVariable String dir, Model model, HttpServletRequest request) {
		
		try {
			if(!CommonUtil.isNotNull(dir)){
				throw new ErrorMessageException("未知资源目录！");
			}
			String dirpath = CommonUtil.decode(dir);
			File catalogfile = MyFileUtil.getXlsByDirname(dirpath);
			if(catalogfile == null){
				throw new ErrorMessageException("该资源没有章节目录！");
			}
			
			List<CatalogBean> catalogList = MyFileCatalogUtil.getXlsCatalog(catalogfile);
			request.setAttribute("catalogList", catalogList);
			
			String keys = ServletRequestUtils.getStringParameter(request, "key", "");
			if(CommonUtil.isNotNull(keys)){
				String[] keyArr = keys.split("、");
				for(String s : keyArr){
					for(CatalogBean b : catalogList){
						b.setTitle(b.getTitle().replaceAll(s, "<em>"+s+"</em>"));
					}
				}
			}
			
			String lastChar = dirpath.substring(dirpath.length()-1);
			if(!lastChar.equals("/") && !lastChar.equals("\\")){
				dirpath += "/";
			}
			String codedir = URLEncoder.encode(dirpath, "utf-8");
			request.setAttribute("imgDir", codedir);
			
			return "platform/pc/browse/catalog";
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			request.setAttribute("message", e.getMessage());
			return "platform/pc/browse/error";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "请求错误：" + e.getMessage());
			return "platform/pc/browse/error";
		}
	}
	
	//进入资源独立预览页面（wap版）
	@RequestMapping(value="/wap/res/browse/{rsdaId}")
	public String resBrowseWap(@PathVariable String rsdaId, Model model, HttpServletRequest request) {
		
		String error = getResDetail(rsdaId, model, request);
		if(CommonUtil.isNotNull(error)){
			model.addAttribute("errormsg", error);
			return "platform/wap/error";
		}
		try {
			Map<String, Object> map = model.asMap();
			ResSourceData resource = (ResSourceData) map.get("resource");
			if(resource.getRsdaSourceType() == null || resource.getRsdaSourceType() == 0){
				String pdfurl = null;
				if(CommonUtil.isNotNull(resource.getRsdaFileType())){
					String format = resource.getRsdaFileType().toLowerCase();
					String swf = resource.getRsdaFileSwfUrl();
					if(CommonUtil.isNotNull(swf) && swf.startsWith("/FileDownload")){
						//如果有swf文件，则采用传统模式预览
						pdfurl = null;
					}else{
						if(format.equals("pdf")){
							pdfurl = resource.getRsdaFileLocalName();
						}
						if(format.equals("doc") || format.equals("docx")){
							if(!CommonUtil.isNotNull(swf)){
								//word转换为pdf，采用pdf.js预览
								String wordadd = resource.getRsdaFileLocalName();
								String pdfUrl = WordToPdf.conversionInit(SysConfigInfo.FILE_ROOT_DIR, wordadd);
								
								resource.setRsdaFileSwfUrl(pdfUrl);
								
								this.resourceSearchService.save(resource);	//word转成pdf后将pdf路径保存到数据库
							}
							pdfurl = resource.getRsdaFileSwfUrl();
						}
					}
				}
				if(CommonUtil.isNotNull(pdfurl)){
					request.setAttribute("title", resource.getRsdaTitle());
					
					String fileAccessUrl = SysConfigInfo.FILE_ROOT_DIR + pdfurl;
					fileAccessUrl = CommonUtil.encode(fileAccessUrl);
					fileAccessUrl = fileAccessUrl.replace("=", "%3D");
					request.setAttribute("fileUrl", fileAccessUrl);
					return "common/pdfviewer";
				}
				
				return "platform/pc/browse/fileview";
			} 
			else if(resource.getRsdaSourceType() == 1){
				return imgBrowserWap(resource, model, request);
			}
			else if(resource.getRsdaSourceType() == 2){
				String htmlUrl = resource.getRsdaFileUrl();
				if(!CommonUtil.isNotNull(htmlUrl)){
					throw new ErrorMessageException("该资源暂无可预览文件！");
				}
				boolean isConn = ParseUrl.urlIsConnect(htmlUrl);
				if(isConn == false){
					throw new ErrorMessageException("对不起，该资源暂时不可预览！");
				}
				return "platform/pc/browse/htmlview";
			}
			else{
				model.addAttribute("errormsg", "该资源文件已删除!");
				return "platform/pc/error";
			}
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			model.addAttribute("errormsg", e.getMessage());
			return "platform/pc/error";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errormsg", "访问出错：" + e.getMessage());
			return "platform/pc/error";
		}
	}
	
	/**
	 * 根据目录地址读取当前地址下封装了详细信息的切片信息的同名json文件
	 * @param request
	 * @param response
	 * @author MaChao
	 * @throws IOException 
	 * @date 2018-09-19
	 */
	@RequestMapping(value="/res/getTileSources")
	public void getTileSources(HttpServletRequest request,HttpServletResponse response) throws IOException{
		String path = ServletRequestUtils.getStringParameter(request, "path", "");
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		Map<String,Object> map = new HashMap<String,Object>();
		String message = "";
		try{
			File file = new File(path);
			if(!file.exists() || !file.isDirectory()){
				throw new Exception("文件路径不存在或不是文件夹！");
			}
			//List<String> list = SectionImgUtils.getSmallFiles(file);
			//map.put("list", list);
			File jsonTxt = new File(file.getPath()+"/"+file.getName()+".json");
			if(!file.exists()){
				throw new Exception("切片的详细数据不存在");
			}
			map.put("tileSources", reader(jsonTxt.getPath()));
			map.put("success", true);
		}catch(Exception e){
			map.put("success", false);
			map.put("message", e.getMessage());
		}
		out.print(JSONObject.fromObject(map));
		out.flush();
		out.close();
	}
	
	//根据文件路径读取资源内容
	public static List<Object> reader(String filePath) {
        try {
            File file = new File(filePath);
            StringBuffer centent =new StringBuffer("");
            List<Object> list = new ArrayList<Object>();
            if (file.isFile() && file.exists()) {
                InputStreamReader read = new InputStreamReader(new FileInputStream(file), "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(read);
                String lineTxt ="";
                while ((lineTxt=bufferedReader.readLine())!= null) {
                	centent.append(lineTxt);
                }
            }
            Object obj;
            String[] str = centent.toString().split(";");
            List<Object> lists = new ArrayList<Object>();
            for(String s : str){
            	obj = JSONObject.fromObject(s);
            	lists.add(obj);
            }
            return lists;
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find the file specified!");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Error reading file content!");
            e.printStackTrace();
        }
        return null;
    }
	
	private boolean useTextDirectoryReader(String path, HttpServletRequest request) {
		List<File> texts = ResourceDirectoryFiles.textFiles(path);
		if (texts.isEmpty()) return false;
		List<String> imagePages = MyFileUtil.getPageNameList(path);
		if (imagePages == null || imagePages.isEmpty()) return true;
		String requested = cn.com.adtech.riss2.annotation.TextAnchor.chapterParameter(request, "p");
		if (!imagePages.contains(requested)) {
			for (File text : texts) if (ResourceDirectoryFiles.relativeTitle(new File(path), text).equals(requested)) return true;
		}
		return false;
	}

	private String textDirectoryBrowser(ResSourceData resource, HttpServletRequest request) {
		File root = new File(resource.getRsdaFileUrl());
		List<File> files = ResourceDirectoryFiles.textFiles(root.getPath());
		String requested = cn.com.adtech.riss2.annotation.TextAnchor.chapterParameter(request, "p");
		List<Map<String, String>> chapters = new ArrayList<Map<String, String>>();
		int current = 0;
		for (int i = 0; i < files.size(); i++) {
			String title = ResourceDirectoryFiles.relativeTitle(root, files.get(i));
			Map<String, String> chapter = new HashMap<String, String>();
			chapter.put("path", title);
			chapter.put("title", title);
			chapters.add(chapter);
			if (title.equals(requested) || (title + ".txt").equals(requested)) current = i;
		}
		if (files.isEmpty()) {
			request.setAttribute("message", "该资源没有可阅读的文本章节。");
			return "platform/pc/browse/error";
		}
		try {
			// The browser needs every chapter to calculate one accurate, responsive book-wide page count.
			for (int i = 0; i < files.size(); i++) {
				String text = ResourceDirectoryFiles.readText(files.get(i));
				chapters.get(i).put("text", text);
				chapters.get(i).put("hash", cn.com.adtech.riss2.annotation.TextAnchor.hash(text));
			}
			request.setAttribute("chapterText", chapters.get(current).get("text"));
		} catch (IOException e) {
			request.setAttribute("message", "资源中的文本章节暂时无法读取，请联系管理员。");
			return "platform/pc/browse/error";
		}
		request.setAttribute("textChapters", chapters);
		request.setAttribute("currentChapter", current);
		request.setAttribute("chapterTitle", chapters.get(current).get("title"));
		return "platform/pc/browse/textDirectory";
	}

	private String imgBrowserWap(ResSourceData resource, Model model, HttpServletRequest request) {
		 String path = resource.getRsdaFileUrl();
		 if(!CommonUtil.isNotNull(path)){
			 return "platform/pc/browse/error";
		 }
		 File imgDir = new File(path);
		 if(!imgDir.exists() || !imgDir.isDirectory()){
			 request.setAttribute("message", "该资源没有可预览文件！");
			 return "platform/pc/browse/error";
		 }
		 File[] fileArr = imgDir.listFiles();
		 if (useTextDirectoryReader(path, request)) {
			 return textDirectoryBrowser(resource, request);
		 }
		 if(fileArr == null || fileArr.length < 1){
			 request.setAttribute("message", "该资源没有可预览文件！");
			 return "platform/pc/browse/imgtxt";
		 }
		 request.setAttribute("imgDir", path);	
		 return "platform/wap/imgBrowse";
	}
	
	/**
	 * pdf在线预览
	 * @param request
	 * @return
	 * 
	 * @author MaChao
	 * @date		2019-03-14
	 * @Version		1.0
	 */
	@RequestMapping(value = "/res/browse/pdf")
	public String pdfPreview(HttpServletRequest request){
		String path = ServletRequestUtils.getStringParameter(request, "file" ,"").trim();
		String key = ServletRequestUtils.getStringParameter(request, "key" ,"").trim();
		String title = ServletRequestUtils.getStringParameter(request, "title" ,"").trim();
		int page = ServletRequestUtils.getIntParameter(request, "page",0);
		boolean bright = ServletRequestUtils.getBooleanParameter(request, "bright", true);
		try {
			request.setAttribute("title", title);
			request.setAttribute("key", key);
			request.setAttribute("page", page > 0 ? page : "");	// pdf跳转的页码
			request.setAttribute("bright", bright);	// 是否高亮关键词
			
			//此处必须要解码，因为之前编码不能直接一步到位（编码一步到位之后出现解码失败），所以此处获取到地址之后需要解码重新编码
			path = CommonUtil.decode(path);
			path = CommonUtil.encode(path);
			path = path.replace("=", "%3D");
			request.setAttribute("fileUrl", path);
			return "common/pdfviewer";
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			request.setAttribute("message", e.getMessage());
			return "platform/pc/browse/error";
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "请求错误：" + e.getMessage());
			return "platform/pc/browse/error";
		}
	}
	
}
