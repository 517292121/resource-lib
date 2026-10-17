package cn.com.adtech.riss2.controller.console;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;
import org.springframework.web.servlet.mvc.multiaction.NoSuchRequestHandlingMethodException;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.platform.ProgressController;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.json.JsonUtil;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.unionlib.lucene.bean.PageBean;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.dao.IAllDataIndexService;
import cn.com.unionlib.lucene.dao.ILuceneDaoService;
import cn.com.unionlib.lucene.dao.ILuceneSearchService;
import cn.com.unionlib.lucene.dao.LuceneDaoServiceImp;
import cn.com.adtech.riss2.controller.platform.ProgressController;

import com.jbetter.framework.session.Pagination;

/**
 * 
 * 后台索引管理面板
 *
 * @author MaChao
 *
 * @date：Feb 6, 2018
 */
public class IndexManagerController extends MultiActionController{
	
	public final static String INDEX_CREATE_PROGRESS_KEY = "INDEX_CREATE_PROGRESS_KEY"; 
	
	private IAllDataIndexService dataIndexService;
	public void setDataIndexService(IAllDataIndexService dataIndexService) {
		this.dataIndexService = dataIndexService;
	}
	
	
	private ILuceneSearchService luceneSearchService;
	public void setLuceneSearchService(ILuceneSearchService luceneSearchService) {
		this.luceneSearchService = luceneSearchService;
	}

	@Override
	protected ModelAndView handleNoSuchRequestHandlingMethod(
			NoSuchRequestHandlingMethodException ex,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return new ModelAndView("lucene/indexManager");
	}
	
	/**
	 * 获取索引目录中文件个数
	 *
	 * @author MaChao
	 * @date Jul 13, 2016
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getIndexFiles(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String result = "";
		String indexDir = SysConfigInfo.INDEX_MAIN_DIR;
		try {
			File f = new File(indexDir);
			if(f.exists() == false){
				result = "索引目录“"+indexDir+"”还未创建";
			}else{
				result = f.list().length + " 个文件";
			}
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 获取索引目录占用空间大小
	 *
	 * @author MaChao
	 * @date Jul 13, 2016
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getIndexSize(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		String result = "";
		String indexDir = SysConfigInfo.INDEX_MAIN_DIR;
		try {
			File f = new File(indexDir);
			if(f.exists()){
				long len = FileUtils.sizeOfDirectory(f);
				result = convertFileSize(len);
			}
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
		out.flush();
		out.close();
	}
	private String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;
 
        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }
	
	/**
	 * 获取资源数量
	 *
	 * @author MaChao
	 * @date Jul 15, 2016
	 *
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getResNum(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		try {
			String result = this.dataIndexService.getResCounts();
			System.out.println("RESULT:" + result);
			out.print(result);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("fail");
		}
		out.flush();
		out.close();
	}
	
	
	/**
	 * 重建文件资源索引
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void reResIndex(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		try {
			this.dataIndexService.saveResToIndex(request);
			ProgressController.sendSucess(INDEX_CREATE_PROGRESS_KEY);
			out.print("success");
		} catch (Exception e) {
			e.printStackTrace();
			out.print("fail");
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 重建文本资源索引
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void reTextIndex(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		try {
			this.dataIndexService.saveTextResToIndex();
			ProgressController.sendSucess(INDEX_CREATE_PROGRESS_KEY);
			out.print("success");
		} catch (Exception e) {
			e.printStackTrace();
			out.print("fail");
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 重建信息资讯索引
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void reInfoIndex(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		try {
			this.dataIndexService.saveInfoToIndex();
			ProgressController.sendSucess(INDEX_CREATE_PROGRESS_KEY);
			out.print("success");
		} catch (Exception e) {
			e.printStackTrace();
			out.print(e.getMessage() );
		}
		out.flush();
		out.close();
	}
	
	/**
	 * 重建所有数据到索引
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void reAllIndex(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		try {
			this.dataIndexService.saveAlldataToIndex(request);
			ProgressController.sendSucess(INDEX_CREATE_PROGRESS_KEY);
			out.print("success");
		} catch (Exception e) {
			e.printStackTrace();
			out.print(e.getMessage() );
		}
		out.flush();
		out.close();
	}
	
	
	/*************-------数据管理相关--------------********/
	//进入资源列表
	public ModelAndView resourcelist(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String dataType = ServletRequestUtils.getStringParameter(request, "dt", "");
		
		String groupConfig = "";
		if(dataType.equals(ResMainBean.DATA_TYPE_RES)){
			groupConfig = "{field:'resType', title: '资源类型', width: 120},";
		}
		if(dataType.equals(ResMainBean.DATA_TYPE_TEXT)){
			groupConfig = "{field:'resLibrary', title: '资源库', width: 140},";
		}
		if(dataType.equals(ResMainBean.DATA_TYPE_INFO)){
			groupConfig = "{field:'subject', title: '所属分类', width: 140},";
		}
		
		request.setAttribute("dataType", dataType);
		request.setAttribute("groupConfig", groupConfig);
		return new ModelAndView("lucene/resourcelist");
	}
	
	public void listJson(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		String json = "";
		
		
		//分页设置
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0) + 1;
		PageBean page = new PageBean();
		page.setCurrentPage(currentPage);
		page.setPageSize(14);
		
		//获取参数
		Map<String, String> params = new HashMap<String, String>();
		String attr = ServletRequestUtils.getStringParameter(request, "attr", "");
		String key = ServletRequestUtils.getStringParameter(request, "key", "");
		String dataType = ServletRequestUtils.getStringParameter(request, "dataType", "");
		String match = ServletRequestUtils.getStringParameter(request, "match", "0");	//匹配方式，默认模糊匹配(0)
		String lan = ServletRequestUtils.getStringParameter(request, "lan", "");
		if(CommonUtil.isNotNull(attr)){
			params.put("attr", attr);
		}
		if(CommonUtil.isNotNull(key)){
			params.put("key", key);
		}
		if(CommonUtil.isNotNull(dataType)){
			params.put("dataType", dataType);
		}
		params.put("language", lan);
		params.put("match", match);
		
		try {
			List<Object[]> dataList = this.luceneSearchService.luceneSearch(page, params);
			String[] keyArr = { "id", "title", "content", "dataType",
					"resType", "creator", "publisher", "keyword", "date",
					"coverUrl", "resLibrary", "subject", "language" };
			
			Pagination pagination = new Pagination();
			pagination.setCurrentPage(currentPage-1);
			pagination.setPageResults(page.getPageSize());
			pagination.setTotalPages(page.getCountPage());
			pagination.setTotalResults(page.getCountRows());
			
			json = JsonUtil.pageListToJson(pagination, dataList, keyArr, null);
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			json = JsonUtil.parseErrorJson(e.getMessage());
			out.print(json);
		} finally {
			out.flush();
			out.close();
		}
	}
	
	public void delindex(HttpServletRequest request,HttpServletResponse response) throws Exception{
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Map<String, Object> map = new HashMap<String, Object>();
		String ids = ServletRequestUtils.getStringParameter(request, "ids", "");
		try {
			ILuceneDaoService luceneService = new LuceneDaoServiceImp();
			
			String[] idArr = ids.split(",");
			for(String id : idArr){
				luceneService.deleteDatabean(id);
			}
			
			map.put("success", true);
			map.put("msg", "删除成功");
		} catch (ErrorMessageException e) {
			map.put("success", false);
			map.put("msg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "删除失败!");
		} finally {
			JSONObject json = JSONObject.fromObject(map);
			PrintWriter out = response.getWriter();
			out.print(json.toString());
			out.flush();
			out.close();
		}
	}
	
	//进入资源列表
	public ModelAndView resdetail(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long resId = ServletRequestUtils.getLongParameter(request, "id", 0L);
		try {
			ResMainBean resMain = this.luceneSearchService.getResDetail(resId);
			
			String txt = CommonUtil.hangleTextArea(resMain.getContent());
			resMain.setContent(txt);
			
			request.setAttribute("res", resMain);
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("message", "索引查询错误：" + e.getMessage());
		}
		return new ModelAndView("lucene/resdetail");
	}
	
	/**
	 * 删除无效数据
	 * @param response
	 * @throws IOException
	 */
	public void deleteInvalidIndex(HttpServletRequest request, HttpServletResponse response) {
		try{
			this.dataIndexService.deleteInvalidIndex();
			ProgressController.sendSucess(INDEX_CREATE_PROGRESS_KEY);
		} catch(IOException e) {
			ProgressController.sendError(INDEX_CREATE_PROGRESS_KEY, "删除无效数据失败!");
		}
	}
}
