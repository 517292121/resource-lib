package cn.com.adtech.riss2.controller.platform.member.infosubscription;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.infosubscription.IInfoSubscriptionService;
import cn.com.adtech.riss2.bso.resource.IResourceDataService;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.infosubscription.InfoSubscription;
import cn.com.adtech.riss2.domain.infosubscription.IsIgnoreResSource;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

import com.jbetter.framework.session.Pagination;

/**
 * 会员消息订阅关键词管理
 * 
 * @author MaChao
 * 
 */
public class InfoSubscriptionController extends MultiActionController {
	// 分页条数
	//	private static final int PAGE_SIZE = 2;

	private IInfoSubscriptionService infoSubscriptionService;
	
	private IResourceDataService resourceDataService;

	public ModelAndView iniKeyWordManage(HttpServletRequest request, HttpServletResponse response,
			Pagination pagination) throws Exception {
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		Map<String, Object> param = new HashMap<String, Object>();
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		} else {
			int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
			pagination.setCurrentPage(currentPage);
			int PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
			pagination.setPageResults(PAGE_SIZE);
			List<InfoSubscription> result = this.infoSubscriptionService.getAllInfoSubscriptionByUserId(user.getSuinId(), pagination);
			param.put("result", result);
			param.put("pagination", pagination);
			param.put("currentPage", currentPage);
			param.put("pageUrl", "/usercenter/infoSubscription.htm?action=iniKeyWordManage&display=lock");
			param.put("display", ServletRequestUtils.getStringParameter(request, "display"));
		}
		return new ModelAndView("platform/userCenter/userCenterInfoSubscriptionKeyWordManager", param);
	}
	
	/**
	 * 添加关键字
	 * @param request
	 * @param response
	 * @param pagination
	 * @throws Exception
	 */
	public void addKeyWordManage(HttpServletRequest request, HttpServletResponse response,
			Pagination pagination) throws Exception {
		String keyWords = ServletRequestUtils.getStringParameter(request, "keyWords");
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return;
		} else {
			if(keyWords != null && !keyWords.trim().equals("")) {
				String[] words = keyWords.split(",|，");
				InfoSubscription[] infos = new InfoSubscription[words.length];
				for (int i = 0; i < words.length; i++) {
					if(words[i] != null && !words[i].trim().equals("")) {
						InfoSubscription infoSubscription = new InfoSubscription();
						infoSubscription.setIssDate(new Date());
						infoSubscription.setIssUserId(user.getSuinId());
						infoSubscription.setIssKeyWord(words[i].trim());
						infos[i] = infoSubscription;
					}
				}
				this.infoSubscriptionService.addInfoSubscriptions(infos);
			}
			request.getRequestDispatcher("/usercenter/infoSubscription.htm?action=showUpdate").forward(request, response);
		}
	}
	
	/**
	 * 删除关键字
	 * @param request
	 * @param response
	 * @param pagination
	 * @throws Exception
	 */
	public void deleteKeyWords(HttpServletRequest request, HttpServletResponse response,
			Pagination pagination) throws Exception {
		String ids = ServletRequestUtils.getStringParameter(request, "ids");
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return;
		} else {		
			if(ids != null && !ids.trim().equals("")) {
				String[] idsStr = ids.split(",");
				Long[] infoIds = new Long[idsStr.length];
				for(int i = 0; i < idsStr.length; i++) {
					infoIds[i] = Long.parseLong(idsStr[i]);
				}
				this.infoSubscriptionService.deleteInfoSubscriptions(infoIds);
			}
			request.getRequestDispatcher("/usercenter/infoSubscription.htm?action=iniKeyWordManage").forward(request, response);
		}
	}
	
	/**
	 * 查看有关关键字的更新
	 * @param request
	 * @param response
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public ModelAndView showUpdate(HttpServletRequest request, HttpServletResponse response,
			Pagination pagination) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		} else {
			int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
			pagination.setCurrentPage(currentPage);
			int PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
			pagination.setPageResults(PAGE_SIZE);
			List<InfoSubscription> result = this.infoSubscriptionService.getAllInfoSubscriptionByUserId(user.getSuinId(), null);
			String[] keyWords = new String[result.size()];
			if(keyWords != null && result.size() > 0) {
				for (int i = 0; i < result.size(); i++) {
					InfoSubscription info = result.get(i);
					keyWords[i] = info.getIssKeyWord();
				}
			}
			List<ResSourceData> list = this.resourceDataService.getResSourceDatasByKeyWords(user.getSuinId(), keyWords, pagination);
			param.put("result", list);
			param.put("pagination", pagination);
			param.put("currentPage", currentPage);
			param.put("pageUrl", "/usercenter/infoSubscription.htm?action=showUpdate&display=lock");
			param.put("display", ServletRequestUtils.getStringParameter(request, "display"));
		}
		return new ModelAndView("platform/userCenter/userCenterInfoSubscriptionUpdate", param);
	}
	
	/**
	 * 忽略资源
	 * @param request
	 * @param response
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public void ignoreResource(HttpServletRequest request, HttpServletResponse response,
			Pagination pagination) throws Exception {
		String ignoreIds = ServletRequestUtils.getStringParameter(request, "ignoreIds");
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return;
		} else {		
			if(ignoreIds != null && !ignoreIds.trim().equals("")) {
				String[] idsStr = ignoreIds.split(",");
				IsIgnoreResSource[] isIgnoreResources = new IsIgnoreResSource[idsStr.length];
				for(int i = 0; i < idsStr.length; i++) {
					if(idsStr[i] != null && !idsStr[i].trim().equals("")) {
						IsIgnoreResSource isr = new IsIgnoreResSource();
						isr.setIsInRsDate(new Date());
						isr.setIsInRsSourceId(Long.parseLong(idsStr[i]));
						isr.setIsInRsUserId(user.getSuinId());
						isIgnoreResources[i] = isr;
					}
				}
				this.infoSubscriptionService.addIgoreResources(isIgnoreResources);
			}
			request.getRequestDispatcher("/usercenter/infoSubscription.htm?action=showUpdate").forward(request, response);
		}
	}

	/**
	 * 查看忽略的资源
	 * @param request
	 * @param response
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initIgnoredResource(HttpServletRequest request, HttpServletResponse response,
			Pagination pagination) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		} else {
			int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
			pagination.setCurrentPage(currentPage);
			int PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
			pagination.setPageResults(PAGE_SIZE);
			List<IsIgnoreResSource> result = null;
			try{
				result = this.infoSubscriptionService.getAllIgnoreResourcesByUserId(user.getSuinId(), pagination);
			}catch (Exception e) {
				e.printStackTrace();
			}
			param.put("result", result);
			param.put("pagination", pagination);
			param.put("currentPage", currentPage);
			param.put("pageUrl", "/usercenter/infoSubscription.htm?action=initIgnoredResource&display=lock");
			param.put("display", ServletRequestUtils.getStringParameter(request, "display"));
		}
		return new ModelAndView("platform/userCenter/userCenterInfoSubscriptionIgnoreManage", param);
	}
	
	/**
	 * 恢复资源
	 * @param request
	 * @param response
	 * @param pagination
	 * @return
	 * @throws Exception
	 */
	public void restoreIgnore(HttpServletRequest request, HttpServletResponse response,
			Pagination pagination) throws Exception {
		String ignoreIds = ServletRequestUtils.getStringParameter(request, "ignoreIds");
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return;
		} else {		
			if(ignoreIds != null && !ignoreIds.trim().equals("")) {
				String[] idsStr = ignoreIds.split(",");
				Long[] ignoredResourcesIds = new Long[idsStr.length];
				for(int i = 0; i < idsStr.length; i++) {
					ignoredResourcesIds[i] = Long.parseLong(idsStr[i]);
				}
				this.infoSubscriptionService.deleteIgnoreResources(ignoredResourcesIds);
			}
			request.getRequestDispatcher("/usercenter/infoSubscription.htm?action=initIgnoredResource").forward(request, response);
		}
	}
	
	public IInfoSubscriptionService getInfoSubscriptionService() {
		return infoSubscriptionService;
	}

	public void setInfoSubscriptionService(IInfoSubscriptionService infoSubscriptionService) {
		this.infoSubscriptionService = infoSubscriptionService;
	}

	public IResourceDataService getResourceDataService() {
		return resourceDataService;
	}

	public void setResourceDataService(IResourceDataService resourceDataService) {
		this.resourceDataService = resourceDataService;
	}

}
