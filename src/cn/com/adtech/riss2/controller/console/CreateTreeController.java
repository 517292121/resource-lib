package cn.com.adtech.riss2.controller.console;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.core.CurrentUserSession;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;
/**
 * 获得后台功能树json
 * 
 * @author MaChao
 *O
 */
public class CreateTreeController extends MultiActionController{
	private static Logger logger = Logger.getLogger(CreateTreeController.class);
	
	public void childCategory(HttpServletRequest request,HttpServletResponse response){
		SysManager sysUserinfo = ServiceUtil.getCurrentUser(request);
		if(sysUserinfo!=null){
			long catalogId = ServletRequestUtils.getLongParameter(request, "id", 0);
			CurrentUserSession userSession = CurrentUserSession.findUserSession(request);
			String json = userSession.getJsonFunctionTree(catalogId);
			if(userSession.isManager() && json != null && !json.trim().isEmpty()) {
				try {
					net.sf.json.JSONArray nodes=net.sf.json.JSONArray.fromObject(json);
					appendAnnotationMenu(nodes);
					json=nodes.toString();
				} catch(Exception error) { logger.warn("无法补充批注管理菜单",error); }
			}
			
			if(logger.isDebugEnabled())
				logger.debug(json);
			
			response.setContentType("txt/plain;charset=utf-8");
			PrintWriter writer=null;
			try {
				writer = response.getWriter();
				writer.write(json);
				writer.flush();
				writer.close();
			} catch (IOException e) {
				logger.error("获得输出对象失败！",e);				
			}
		}
	}

	/** Reuse the administrator's existing resource-maintenance folder without changing role assignments. */
	private void appendAnnotationMenu(net.sf.json.JSONArray nodes) {
		for(Object value:nodes) {
			net.sf.json.JSONObject node=(net.sf.json.JSONObject)value;
			if(!node.containsKey("children"))continue;
			net.sf.json.JSONArray children=node.getJSONArray("children");
			if("资源维护".equals(node.optString("text"))||"cat306".equals(node.optString("id"))) {
				boolean exists=false;
				for(Object child:children) if("/console/annotations".equals(((net.sf.json.JSONObject)child).optString("url")))exists=true;
				if(!exists) {
					net.sf.json.JSONObject annotation=new net.sf.json.JSONObject();
					annotation.put("id","textAnnotations");annotation.put("text","批注管理");annotation.put("leaf",true);annotation.put("url","/console/annotations");children.add(annotation);
				}
			} else appendAnnotationMenu(children);
		}
	}
}
