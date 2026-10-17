package cn.com.adtech.riss2.controller.console.sc;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.sc.IScService;

@SuppressWarnings("unchecked")
public class ScFaqTypeController extends MultiActionController {
	private static final Logger logger = Logger.getLogger(ScFaqTypeController.class);

	private IScService scServices;

	public IScService getScServices() {
		return scServices;
	}

	public void setScServices(IScService scServices) {
		this.scServices = scServices;
	}

	/** 
	 * FAQ库分类
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-11-4
	 */
	public ModelAndView initScFaqTypeView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List list = this.scServices.getScFaqTypeList(null, null, null, null, null,null);
		request.setAttribute("scFaqTypeLlist", list);
		return new ModelAndView("sc/scFaqType");
	}
}
