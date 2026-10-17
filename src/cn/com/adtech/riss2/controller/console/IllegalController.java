package cn.com.adtech.riss2.controller.console;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.AbstractController;

/**
 * 非法访问跳转控制器
 * @author MaChao
 * @date 2010-8-31
 */
@SuppressWarnings("unchecked")
public class IllegalController extends AbstractController {
	private String pageView;

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
		StringBuffer message = new StringBuffer("无效的非法操作，<br \\>请点击&nbsp;<a href=\"");
		message.append(request.getContextPath());
		message.append("/console/index.htm\">这里</a>&nbsp;返回主页。");
		Map model = new HashMap();
		model.put("message", message.toString());
		return new ModelAndView(getPageView(), model);
	}

	public String getPageView() {
		return pageView;
	}

	public void setPageView(String pageView) {
		this.pageView = pageView;
	}
}
