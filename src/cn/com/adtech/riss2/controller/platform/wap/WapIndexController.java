package cn.com.adtech.riss2.controller.platform.wap;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bean.ResourceForTypeBean;
import cn.com.adtech.riss2.cache.PlatformCacheFactory;
import cn.com.adtech.riss2.controller.platform.ResCommonController;

/**
 * 
 * WAP版前台主页（专属）控制器
 *
 * @author MaChao
 *
 * @date：Jan 2, 2018
 */
@Controller
public class WapIndexController {

	//进入用户中心
	@RequestMapping(value="/mobile")
	public String mobileIndex() {
		return "platform/wap/index";
	}
	
	//异步获取信息词义
	@RequestMapping(value="/wap/cmdres")
	public void getCmdRes(HttpServletRequest request, HttpServletResponse response) throws IOException{
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		response.setHeader("Access-Control-Allow-Origin", "*");
		
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		List<ResourceForTypeBean> cmdlist = PlatformCacheFactory.instance().getResForTypeList();
		try {
			ResourceForTypeBean resbean = null;
			for(int i=0,s=cmdlist.size(); i<s; i++){
				resbean = cmdlist.get(i);
				ResCommonController.parseCoverList(request, resbean.getResList(), 2, 3, 4);
				map.put("dname", resbean.getRltyName());
				map.put("d" + resbean.getRltyId(), resbean.getResList());
			}
			map.put("success", true);
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
