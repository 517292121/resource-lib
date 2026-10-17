package cn.com.adtech.riss2.controller.platform;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import net.sf.json.JSONObject;


/**
 * 进度条控制器
 * @author MaChao
 * @date	2021-07-26
 * @version	1.0
 *
 */
@Controller
public class ProgressController {
	
	/**
	 * 获取进度
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value="/progress/get")
	public void getResList(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		String key = ServletRequestUtils.getStringParameter(request, "key", "").trim();
		Map<String, Object> map = getProgressMap(key);
		if(map == null || map.isEmpty()) {
			map = new HashMap<String, Object>();
			map.put("c", -1);
		}
		
		try{
			PrintWriter out = response.getWriter();
			String json = JSONObject.fromObject(map).toString();
			out.print(json);
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新进度条
	 * @param key		进度条session中存储的key
	 * @param no		当前进度
	 * @param total		总进度
	 * @param msg		提示信息
	 */
	public static void updateProgress(String key, long no, long total, String msg) {
		Map<String, Object> map = getProgressMap(key);
		map = map != null ? map : new HashMap<String, Object>();
		
		map.put("c", 1);
		map.put("n", no);
		map.put("s", msg);
		map.put("t", total);
		setProgressMap(key, map);
	}
	
	/**
	 * 数据完成
	 * @param key		进度条session中存储的key
	 */
	public static void sendSucess(String key) {
		Map<String, Object> map = getProgressMap(key);
		if(map == null || map.isEmpty()) {
			return;
		}
		map.put("c", 2);
		setProgressMap(key, map);
	}
	
	/**
	 * 数据处理失败
	 * @param key		进度条session中存储的key
	 * @param msg		错误信息
	 */
	public static void sendError(String key, String msg) {
		Map<String, Object> map = getProgressMap(key);
		map = map != null ? map : new HashMap<String, Object>();
		
		map.put("c", 3);
		map.put("s", msg);
		setProgressMap(key, map);
	}

	private static Map<String, Object> getProgressMap(String key) {
		// 通过上下文获取request
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        if(request == null) {
        	return null;
        }
        // 通过request获取session
        HttpSession session = request.getSession();
        
        // 从session中获取进度Map
		Map<String, Object> progressMap = (Map<String, Object>) session.getAttribute(key);
		if(progressMap == null) {
			return null;
		}
		return progressMap;
	}
	
	private static void setProgressMap(String key, Map<String, Object> progressMap) {
		// 通过上下文获取request
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
		if(request == null) {
			return;
		}
		// 通过request获取session
		HttpSession session = request.getSession();
		if(session == null) {
			return;
		}
		session.setAttribute(key, progressMap);
	}
}
