package cn.com.adtech.riss2.controller.console.count;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.count.ISysCountService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysManager;

/**
 * 后台站点访问量统计
 * 
 * @author MaChao
 * @date 2011-1-12
 */
public class AccessCounterController extends MultiActionController {
	private ISysCountService countService;

	public ISysCountService getCountService() {
		return countService;
	}

	public void setCountService(ISysCountService countService) {
		this.countService = countService;
	}

	public ModelAndView initView(HttpServletRequest request, HttpServletResponse response) {
		return new ModelAndView("count/accessCounter");
	}

	/**
	 * 获得站点访问量统计列表
	 * 
	 * @author MaChao
	 * @date 2011-1-12
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	public void getAccessCounterList(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("txt/plain;charset=utf-8");
		PrintWriter out = response.getWriter();
		SysManager sysManager = (SysManager)ServiceUtil.getCurrentUser(request);
		if(sysManager == null) {
			return;
		}
		Date date = new Date();
		int beginYear = ServletRequestUtils.getIntParameter(request, "beginYear", 0);
		int endYear = ServletRequestUtils.getIntParameter(request, "endYear", 0);
		
		List<Object[]> list = countService.getAccessCounterList(beginYear, endYear);
		String result = "[";
//		for(int i = 0, j = 0; endYear >= beginYear; endYear--, j++) {
//			if(j != 0) {
//				result += ",";
//			}
//			if(i >= list.size() || !(endYear + "").equals(list.get(i)[0].toString())) {
//				result += "{'year':'" + endYear + "','accesscount':'0'}";
//			}else {
//				Object[] objects = list.get(i++);
//				result += "{'year':'" + objects[0] + "','accesscount':'" + objects[1] + "'}";
//			}
//		}
//		result += ",";
		Object[] o = null;
		for(int i=0,s=list.size(); i<s; i++){
			o = list.get(i);
			result += "{'year':'" + o[0] + "','accesscount':'" + o[1] + "'}";
			if(i < s-1){
				result += ",";
			}
		}
		result += "]";
		out.write(result);
		out.flush();
	}
}
