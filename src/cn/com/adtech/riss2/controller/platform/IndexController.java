package cn.com.adtech.riss2.controller.platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bean.ResourceForTypeBean;
import cn.com.adtech.riss2.bso.platform.IIndexDataService;
import cn.com.adtech.riss2.annotation.TextAnnotationRepository;
import cn.com.adtech.riss2.core.json.DateJsonValueProcessor;
import cn.com.unionlib.lucene.dao.IAllDataIndexService;

/**
 * 
 * PC版前台主页（专属）控制器
 *
 * @author MaChao
 *
 * @date：Jan 2, 2018
 */
@Controller
public class IndexController {
	
	@Resource(name="indexDataService")
	private IIndexDataService indexDataService;
	@Resource(name="alldataIndexService")
	private IAllDataIndexService indexService;
	@Resource(name="textAnnotationRepository")
	private TextAnnotationRepository annotations;
	
	//进入用户中心 
	@RequestMapping(value="/index")
	public String index(HttpServletRequest request) {
		loadHomeStatistics(request);
		List<ResourceForTypeBean> cmdlist = this.indexDataService.getRcmdResource();
		if(cmdlist != null && cmdlist.size() > 0){
			for(ResourceForTypeBean obj : cmdlist){
				List<Object[]> reslist = obj.getResList();
				try {
					ResCommonController.parseCoverList(request, reslist, 2, 3, 4);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
		}
		request.setAttribute("cmdreslist", cmdlist);
		return "platform/pc/index";
	}

	private void loadHomeStatistics(HttpServletRequest request) {
		try {
			Map<String, Object> totals = indexDataService.getHomeStatistics();
			request.setAttribute("homeDocumentCount", totals.get("resourceTotal"));
			request.setAttribute("homeVisitCount", totals.get("visitTotal"));
		} catch (Exception ex) {
			org.apache.log4j.Logger.getLogger(IndexController.class).error("Unable to load homepage catalogue totals", ex);
		}
		try {
			request.setAttribute("homePageCount", Long.valueOf(indexService.getResCounts().split(",")[3]));
		} catch (Exception ex) {
			org.apache.log4j.Logger.getLogger(IndexController.class).error("Unable to load homepage page total", ex);
		}
		try {
			request.setAttribute("homeAnnotationCount", annotations.countPublished());
		} catch (Exception ex) {
			org.apache.log4j.Logger.getLogger(IndexController.class).error("Unable to load homepage annotation total", ex);
		}
	}

	/**
	 * 根路径入口：暂时关闭设备识别，手机与电脑统一进入 PC 首页。
	 */
	@RequestMapping(value="/")
	public String root(HttpServletRequest request) {
		// boolean isPhone = CommonUtil.judgeIsPhone(request);
		// request.getSession().setAttribute("CLIENTTYPE", isPhone ? "WAP" : "PC");
		// return isPhone ? "redirect:/mobile" : "redirect:/index";
		request.getSession().setAttribute("CLIENTTYPE", "PC");
		return "redirect:/index";
	}
	
	//异步获取最新资源
	@RequestMapping(value="/index/resList/{sort}")
	public void getResList(@PathVariable String sort, HttpServletResponse response) throws IOException{
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		JsonConfig config = new JsonConfig();
		config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor("yyyy-MM-dd HH:ss"));
		try {
			List<Object[]> resList = this.indexDataService.getIndexResource(sort, 8);
			map.put("success", true);
			map.put("dataList", resList);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map, config).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	//异步获取热门检索词
	@RequestMapping(value="/index/hotsearch")
	public void hotsearch(HttpServletResponse response) throws IOException{
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			List<String> resList = this.indexDataService.getHotSearchKey(7);
			map.put("success", true);
			map.put("dataList", resList);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
	
	//异步获取信息词义
	@RequestMapping(value="/index/info")
	public void infoList(HttpServletResponse response) throws IOException{
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			List<Object[]> infoList = this.indexDataService.getInfoList(3);
			map.put("success", true);
			map.put("dataList", infoList);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", false);
			map.put("msg", "错误：" + e.getMessage());
		} finally {
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
			out.flush();
			out.close();
		}
	}
}
