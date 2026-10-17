package cn.com.adtech.riss2.controller.platform;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.platform.IResourceCommentsService;
import cn.com.adtech.riss2.bso.sc.IFavoritesService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.json.DateJsonValueProcessor;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResSourceComt;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

/**
 * 
 * 资源详细页面控制器
 *
 * @author MaChao
 *
 * @date：Jan 8, 2018
 */
@Controller
public class ResDetailController extends ResCommonController{

	//资源评论服务对象
	@Resource(name="resourceCommentsService")
	protected IResourceCommentsService resCommentsService;
	
	//资源收藏服务对象
	@Resource(name="favoritesService")
	protected IFavoritesService favoritesService;
	
	
	
	//进入资源详情页面（PC版）
	@RequestMapping(value="/res/{rsdaId}")
	public String resDetail(@PathVariable String rsdaId, Model model, HttpServletRequest request) {
		String error = getResDetail(rsdaId, model, request);
		if(CommonUtil.isNotNull(error)){
			model.addAttribute("errormsg", error);
			return "platform/pc/error";
		}else{
			//PC版权详细页面分为传统标准版与新简洁版，只需返回不同的页面即可展示不同效果
			return "platform/pc/resDetail-sim";
			//return "platform/pc/resDetail-com";
		}
	}
	/** Six real, published research records per page; no design placeholder content. */
	@RequestMapping(value="/res/research/{rsdaId}", method=org.springframework.web.bind.annotation.RequestMethod.GET)
	public void relatedResearch(@PathVariable Long rsdaId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("application/json;charset=UTF-8");
		response.setHeader("Cache-Control", "no-store");
		Map<String, Object> result = new LinkedHashMap<String, Object>();
		if (rsdaId < 1L) {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			result.put("success", false);
			response.getWriter().write(JSONObject.fromObject(result).toString());
			return;
		}
		Pagination pagination = new Pagination();
		pagination.setCurrentPage(Math.max(0, Math.min(10000, ServletRequestUtils.getIntParameter(request, "pn", 0))));
		pagination.setPageResults(6);
		try {
			List<Object[]> rows = resourceSearchService.getRelatedResearch(pagination, rsdaId);
			List<Map<String, Object>> items = new ArrayList<Map<String, Object>>();
			for (Object[] row : rows) {
				Map<String, Object> item = new LinkedHashMap<String, Object>();
				item.put("id", CommonUtil.idToString(String.valueOf(row[0])));
				item.put("title", row[1]);
				item.put("creator", row[2]);
				item.put("date", row[3]);
				items.add(item);
			}
			result.put("success", true);
			result.put("items", items);
			result.put("totalLine", pagination.getTotalResults());
			result.put("totalPage", pagination.getTotalPages());
			result.put("currentPage", pagination.getCurrentPage());
		} catch (Exception e) {
			org.apache.log4j.Logger.getLogger(ResDetailController.class).error("相关研究加载失败", e);
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			result.put("success", false);
		}
		response.getWriter().write(JSONObject.fromObject(result).toString());
	}

	//进入资源详细页面（WAP版）
	@RequestMapping(value="/wap/resdetail/{rsdaId}")
	public String resWapDetail(@PathVariable String rsdaId, Model model, HttpServletRequest request) {
		String error = getResDetail(rsdaId, model, request);
		if(CommonUtil.isNotNull(error)){
			model.addAttribute("errormsg", error);
			return "platform/wap/error";
		}else{
			return "platform/wap/resDetail";
		}
	}
	
	/**
	 * 获取资源元数据JSON
	 * @param rmstId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/res/meta/{rsdaId}")
	public void getResMetadata(@PathVariable Long rsdaId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			List<Object[]> dataList = this.resourceSearchService.getMetaData(rsdaId);
			String json = "";
			if(dataList != null && dataList.size() > 0){
				map.put("dataList", dataList);
				json = JSONObject.fromObject(map).toString();
			}
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 加入收藏
	 * @param rsdaId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/res/favorite")
	public void saveFavorite(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
			if(rsdaId < 1L){
				throw new ErrorMessageException("参数错误，收藏失败！");
			}
			SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
			if(user == null) {
				throw new ErrorMessageException("您没有登录或登录已过期，请先登录!!!");
			}
			this.favoritesService.addFavorite(user.getSuinId(), rsdaId);
			map.put("success", "true");
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg", "操作失败：" + e.getMessage());
		}
		String json = JSONObject.fromObject(map).toString();
		out.print(json);
		out.flush();
		out.close();
	}
	
	
	/**
	 * 获取资源同一主题下其它资源
	 * @param rsdaId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/res/related/{rssuId}")
	public void getResRelated(@PathVariable Long rssuId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			List<Object[]> dataList = this.resourceSearchService.getResRelated(rssuId, rsdaId);
			String json = "";
			if(dataList != null && dataList.size() > 0){
				ResCommonController.parseCoverList(request, dataList, 2, 3, 4);
				
				map.put("dataList", dataList);
				json = JSONObject.fromObject(map).toString();
			}
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 获取资源评论
	 * @param rsdaId
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/res/comments/{rsdaId}")
	public void getResComments(@PathVariable Long rsdaId, HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		int page = ServletRequestUtils.getIntParameter(request, "page", 0);
		Pagination p = new Pagination();
		p.setCurrentPage(page);
		p.setPageResults(10);
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:mm"));
		try {
			Long userId = MyRequestUtil.getMemberId(request);
			List<Object[]> dataList = this.resCommentsService.getCommentsList(rsdaId, userId, p);
			String json = "";
			if(dataList != null && dataList.size() > 0){
				map.put("totalnum", p.getTotalResults());
				map.put("dataList", dataList);
				json = JSONObject.fromObject(map, config).toString();
			}
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 保存评论
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/res/comments/save")
	public void saveResComments(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
		String comtContent = ServletRequestUtils.getStringParameter(request, "content", "");
		try {
			SysUserinfo sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
			ResSourceData rsda = (ResSourceData) this.resCommentsService.get(ResSourceData.class, rsdaId);
			if(rsda==null){
				throw new ErrorMessageException("无效的资源！");
			}
			if(sysUserinfo == null) {
				throw new ErrorMessageException("nologin");
			}
			if(!ServiceUtil.isNotNull(comtContent)) {
				throw new ErrorMessageException("评论内容不能为空！");
			}
			if(comtContent.length() > 300) {
				throw new ErrorMessageException("评论内容不能超过300个字符！");
			}
			ResSourceComt resSourceComt = new ResSourceComt();
			resSourceComt.setRscoRsdaId(rsdaId);
			resSourceComt.setRscoContent(comtContent);
			resSourceComt.setRscoGrade((short)0);
			resSourceComt.setRscoSuinId(sysUserinfo.getSuinId());
			resSourceComt.setRscoDate(new Date());
			
			this.resCommentsService.save(resSourceComt);
			map.put("success", "true");	
		} catch (ErrorMessageException e){
			e.printStackTrace();
			map.put("success", "false");
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("msg", "错误：" + e.getMessage());
		}
		
		String json = JSONObject.fromObject(map).toString();
		out.print(json);
		out.flush();
		out.close();
	}
	
	/**
	 * 获取资源附件
	 * @param rsdaId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/res/attfile/{rsdaId}")
	public void getAttachFile(@PathVariable Long rsdaId, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		PrintWriter out = response.getWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Object[]> dataList = this.resourceSearchService.getAttachFile(rsdaId);
			String json = "";
			if(dataList != null && dataList.size() > 0){
				map.put("totalnum", dataList.size());
				map.put("dataList", dataList);
				json = JSONObject.fromObject(map).toString();
			}
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
		out.flush();
		out.close();
	}
	/**
	 * 附件详细页面
	 * @param rsdaId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/res/attdetail/{rsfiId}")
	public String arrachFileDetail(@PathVariable Long rsfiId, Model model) throws IOException {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			ResSourceFile resFile = (ResSourceFile) this.resourceSearchService.get(ResSourceFile.class, rsfiId);
			
			if(CommonUtil.isNotNull(resFile.getResLocalPath())){
				File srcFile = new File(SysConfigInfo.FILE_ROOT_DIR + resFile.getResLocalPath());
				if(srcFile.exists()){
					map.put("srcExist", true);
				}
			}
			
			map.put("resFile", resFile);
			model.addAllAttributes(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "platform/pc/resFileDetail";
	}
	

	//资源附件预览
	@RequestMapping(value="/res/attview/{rsfiId}")
	public String resArrFileView(@PathVariable Long rsfiId, Model model) {
		try {
			ResSourceFile resFile = (ResSourceFile) this.resourceSearchService.get(ResSourceFile.class, rsfiId);
			if(resFile == null){
				throw new ErrorMessageException("该文件信息未找到！");
			}
			String viewPath = resFile.getResViewPath();
			if(!CommonUtil.isNotNull(viewPath)){
				throw new ErrorMessageException("该文件没有可预览文件！");
			}
			String localpath = CommonUtil.parseUrlName(viewPath);
			File file = new File(SysConfigInfo.FILE_ROOT_DIR + localpath);
			if(!file.exists()){
				throw new ErrorMessageException("对不起，该资源预览文件不存在或已被删除，请下载预览！"); 
			}
			
			String type = "";
			String extname = localpath.substring(localpath.lastIndexOf(".") + 1).toLowerCase();
			if(extname.equals("tif")){ //tif。暂时无法转换的图片格式
				type = "photo";
			}else{
				type = ServiceUtil.getFileTypeBySpread(extname); //获取文件载体类型
			}
			
			model.addAttribute("browseUrl", viewPath);
			model.addAttribute("resType", type);
			model.addAttribute("title", resFile.getResFileName());
		} catch (ErrorMessageException e) {
			e.printStackTrace();
			model.addAttribute("filemsg", e.getMessage());
		}catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("filemsg", "访问出错：" + e.getMessage());
		}
		return "platform/pc/filePreview";
	}
	
	
	/**
	 * 获取相关资源（按相同资源库与资源类型获取）
	 * @param rsdaId
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/res/similar")
	public void getSimilarRes(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Long rsdaId = ServletRequestUtils.getLongParameter(request, "rsdaId", 0L);
		Long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L);
		Long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L);
		
		PrintWriter out = response.getWriter();
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			List<Object[]> dataList = this.resourceSearchService.getSimilarRes(rsdaId, rmstId, rltyId);
			String json = "";
			if(dataList != null && dataList.size() > 0){
				ResCommonController.parseCoverList(request, dataList, 3, 4, 5);
				map.put("totalnum", dataList.size());
				map.put("dataList", dataList);
				json = JSONObject.fromObject(map).toString();
			}
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
		out.flush();
		out.close();
	}
}
