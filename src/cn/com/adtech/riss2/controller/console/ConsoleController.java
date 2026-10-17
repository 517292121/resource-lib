package cn.com.adtech.riss2.controller.console;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import cn.com.adtech.riss2.core.CurrentUserSession;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysCatalog;
import cn.com.adtech.riss2.domain.SysManager;

/**
 * 后台首页控制器
 * 
 * @author MaChao
 */
@SuppressWarnings("unchecked")
public class ConsoleController implements Controller {
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysManager sysUserinfo = ServiceUtil.getCurrentUser(request);
		Map<String, Object> model = new HashMap<String, Object>();
		StringBuilder items = new StringBuilder();
		StringBuilder catalogDiv = new StringBuilder();
		if(sysUserinfo != null) {
			CurrentUserSession userSession = CurrentUserSession.findUserSession(request);
			List<SysCatalog> catalogList = userSession.getAccessableSysCatalogs();
			if(catalogList != null && catalogList.size() > 0) {
				for(SysCatalog sysCatalog : catalogList) {
					catalogDiv.append("<div id='root").append(sysCatalog.getScatId()).append("'></div>");
					items.append("{contentEl: 'root").append(sysCatalog.getScatId()).append("',");
					items.append("title:'").append(sysCatalog.getScatName()).append("',");
					items.append("border:false,iconCls:'nav'},");
				}
			}
			model.put("catalogList", catalogList);
		}else {
			return new ModelAndView("../../pages/login");
		}
		if(items.length() > 1) {
			model.put("items", items.substring(0, items.length() - 1));
			model.put("catalogDiv", catalogDiv.toString());
		}
		return new ModelAndView("main/main", model);
	}
}
