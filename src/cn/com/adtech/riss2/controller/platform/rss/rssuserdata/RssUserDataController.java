package cn.com.adtech.riss2.controller.platform.rss.rssuserdata;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.rss.IRssUserDataService;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.rss.RssUserData;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

/**
 * 会员的RSS信息操作
 * @author MaChao
 *
 */
public class RssUserDataController extends MultiActionController {
	// 分页条数
//	private static final int PAGE_SIZE = ConstantsObj.DEFAULT_PAGE_SIZE;
	private static final int PAGE_SIZE = 20;
	
	private IRssUserDataService rssUserDataService;
	
	public ModelAndView initUserRssData(HttpServletRequest request, HttpServletResponse response, 
			Pagination pagination) throws Exception {
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		Map<String, Object> param = new HashMap<String, Object>();
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		} else {
			int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
			pagination.setCurrentPage(currentPage);
			pagination.setPageResults(PAGE_SIZE);
			List<RssUserData> result = this.rssUserDataService.getAllRssUserDataByUserId(user.getSuinId(), pagination);
			param.put("result", result);
			param.put("pagination", pagination);
			param.put("currentPage", currentPage);
			param.put("pageUrl", "/usercenter/rssUserData.htm?action=initUserRssData&display=lock");
			param.put("display", ServletRequestUtils.getStringParameter(request, "display"));
		}
		return new ModelAndView("platform/userCenter/userCenterRssUserDataManage", param);
	}

	public void addRssData(HttpServletRequest request, HttpServletResponse response, 
			RssUserData rss) throws Exception {
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return;
		} else {
			try {
				int display = Integer.parseInt(request.getParameter("rssDataDisplayNum1"));
				if(display <= 0){
					throw new ErrorMessageException("显示条数必须大于0");
				}
				int order = Integer.parseInt(request.getParameter("rssDataOrder1"));
				if(order < 0){
					throw new ErrorMessageException("排序号必须大于0");
				}
				rss.setRssDataDisplayNum(display);
				rss.setRssDataOrder(order);
			} catch (NumberFormatException e) {
				request.setAttribute("error", "yes");
				request.setAttribute("rss", rss);
				request.getRequestDispatcher("/usercenter/rssUserData.htm?action=initUserRssData").forward(request, response);
				return;
			} catch (ErrorMessageException e) {
				request.setAttribute("error", "yes");
				request.setAttribute("rss", rss);
				request.getRequestDispatcher("/usercenter/rssUserData.htm?action=initUserRssData").forward(request, response);
				return;
			}
			Date addDate = new Date();
			rss.setRssDataName((rss.getRssDataName() == null || rss.getRssDataName().trim().equals("")) ? 
					rss.getRssDataSourceUrl() : rss.getRssDataName());
			rss.setRssDataAddDate(addDate);
			rss.setRssDataLastModifyDate(addDate);
			rss.setRssDataUserId(user.getSuinId());
			this.rssUserDataService.addRssUserData(rss);
			request.getRequestDispatcher("/usercenter/rssUserData.htm?action=initUserRssData").forward(request, response);
		}
	}
	
	public void delRssData(HttpServletRequest request, HttpServletResponse response, 
			RssUserData rss) throws Exception {
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		String ids = ServletRequestUtils.getStringParameter(request, "ids");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return;
		} else {
			if(ids != null && !ids.trim().equals("")) {
				String[] idsStr = ids.split(",");
				Long[] infoIds = new Long[idsStr.length];
				for(int i = 0; i < idsStr.length; i++) {
					if(idsStr[i] != null && !idsStr[i].trim().equals("")) {
						infoIds[i] = Long.parseLong(idsStr[i]);
					}
				}
				this.rssUserDataService.deleteRssUserDatas(infoIds);
			}
			request.getRequestDispatcher("/usercenter/rssUserData.htm?action=initUserRssData").forward(request, response);
		}
	}
	
	public void getRssDataById(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		Long id = ServletRequestUtils.getLongParameter(request, "id");
		RssUserData rss = (RssUserData) this.rssUserDataService.get(RssUserData.class, id);
		StringBuilder sb = new StringBuilder();
		sb.append("{").append("\"id\": ").append("\"").append(rss.getRssDataId()).
		append("\", " ).append("\"rssDataName\": ").append("\"").append(rss.getRssDataName()).
		append("\", " ).append("\"rssDataSourceUrl\": ").append("\"").append(rss.getRssDataSourceUrl()).
		append("\", " ).append("\"rssDataOrder\": ").append("\"").append(rss.getRssDataOrder()).
		append("\", " ).append("\"rssDataDisplayNum\": ").append("\"")
			.append( rss.getRssDataDisplayNum()).append("\"}" );
		response.getWriter().write(sb.toString());
		response.getWriter().flush();
	}
	
	public void updateRssData(HttpServletRequest request, HttpServletResponse response, 
			RssUserData rss) throws Exception {
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return;
		} else {
			try {
				int display = Integer.parseInt(request.getParameter("rssDataDisplayNum1"));
				if(display <= 0){
					throw new ErrorMessageException("显示条数必须大于0");
				}
				int order = Integer.parseInt(request.getParameter("rssDataOrder1"));
				if(order < 0){
					throw new ErrorMessageException("排序号必须大于0");
				}
				rss.setRssDataDisplayNum(display);
				rss.setRssDataOrder(order);
			} catch (NumberFormatException e) {
				request.setAttribute("error", "yes");
				request.setAttribute("rss", rss);
				request.getRequestDispatcher("/usercenter/rssUserData.htm?action=initUserRssData").forward(request, response);
				return;
			} catch (ErrorMessageException e) {
				request.setAttribute("error", "yes");
				request.setAttribute("rss", rss);
				request.getRequestDispatcher("/usercenter/rssUserData.htm?action=initUserRssData").forward(request, response);
				return;
			}
			rss.setRssDataUserId(user.getSuinId());
			this.rssUserDataService.updateRssUserData(rss);
		}
		request.getRequestDispatcher("/usercenter/rssUserData.htm?action=initUserRssData").forward(request, response);
	}
	
	public IRssUserDataService getRssUserDataService() {
		return rssUserDataService;
	}

	public void setRssUserDataService(IRssUserDataService rssUserDataService) {
		this.rssUserDataService = rssUserDataService;
	}

}
