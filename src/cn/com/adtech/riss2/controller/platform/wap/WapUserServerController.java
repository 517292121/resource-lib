package cn.com.adtech.riss2.controller.platform.wap;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * WAP版前台主页（专属）控制器
 *
 * @author MaChao
 *
 * @date：Jan 2, 2018
 */
@Controller
public class WapUserServerController {
	
	//进入用户登录
	@RequestMapping(value="/wap/login")
	public String inLogin(Model model, HttpServletRequest request) {
		String from = ServletRequestUtils.getStringParameter(request, "from",request.getContextPath() + "/");
		
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("from", from);
		model.addAllAttributes(map);
		return "platform/wap/login";
	}
	
	//进入用户注册
	@RequestMapping(value="/wap/register")
	public String inRegister() {
		return "platform/wap/register";
	}
}
