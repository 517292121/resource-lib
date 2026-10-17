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
import cn.com.adtech.riss2.core.json.DateJsonValueProcessor;
import cn.com.adtech.riss2.core.CommonUtil;

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
	
	//进入用户中心 
	@RequestMapping(value="/index")
	public String index(HttpServletRequest request) {
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

	/**
	 * Spring Boot does not apply the old web.xml welcome-file rule when the
	 * application is run from an executable WAR. Keep the original index.jsp
	 * device detection behavior for requests to the root URL.
	 */
	@RequestMapping(value="/")
	public String root(HttpServletRequest request) {
		boolean isPhone = CommonUtil.judgeIsPhone(request);
		request.getSession().setAttribute("CLIENTTYPE", isPhone ? "WAP" : "PC");
		return isPhone ? "redirect:/mobile" : "redirect:/index";
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
