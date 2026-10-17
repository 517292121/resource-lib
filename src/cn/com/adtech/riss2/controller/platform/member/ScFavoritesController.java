package cn.com.adtech.riss2.controller.platform.member;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.sc.IFavoritesService;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.sc.ScFavorites;
import cn.com.adtech.riss2.exception.ErrorMessageException;

import com.jbetter.framework.session.Pagination;

/**
 * 收藏控制器
 * @author MaChao
 *
 */
public class ScFavoritesController extends MultiActionController {
	private IFavoritesService favoritesService;
	
	public IFavoritesService getFavoritesService() {
		return favoritesService;
	}

	public void setFavoritesService(IFavoritesService favoritesService) {
		this.favoritesService = favoritesService;
	}
	
	/**
	 * 初始化会员中心--我的收藏
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-7
	 */
	public ModelAndView initScFavoriteView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		pagination.setPageResults(15);
		pagination.setCurrentPage(currentPage);
		
		// 添加检索条件
		String resourceType = ServletRequestUtils.getStringParameter(request, "resourceType","0");
		String search = ServletRequestUtils.getStringParameter(request, "search","");
		Short rltyType = 0;
		try {
			rltyType = Short.parseShort(resourceType);
		}catch(Exception e) {
			rltyType = 0;
		}
		
		// 保存查询条件为分页提供数据
		List<Object[]> list = null;
		try{
			list = this.favoritesService.getFavoritesList(userinfo.getSuinId(), rltyType, search, pagination);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		map.put("favoriteList", list);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/user/favorite.htm?action=initScFavoriteView");
		map.put("search", search);
		map.put("resourceType", rltyType);
		return new ModelAndView("platform/userCenter/usercenterFavorite", map);
	}
	
	/**
	 * 添加收藏
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void addFavorite(HttpServletRequest request, HttpServletResponse response) 
		throws Exception {
		String retv = "";
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		if(user == null) {
			retv = "您没有登录，请先登录!!!";
		} else {
			Long sourceId = ServletRequestUtils.getLongParameter(request, "sourceId");
			if(sourceId == null) {
				throw new RuntimeException("资源为空!!!!");
			}
			try {
				favoritesService.addFavorite(user.getSuinId(), sourceId);
				retv = "收藏资源成功!!!";
			} catch (ErrorMessageException e) {
				retv = e.getMessage();
			}
		}
		response.getWriter().write(retv);
		response.getWriter().flush();
	}
	
	/**
	 * 删除收藏信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-7
	 */
	public ModelAndView delScFavorite(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
			if(userinfo == null) {
				response.sendRedirect(request.getContextPath() + "/index.htm");
				return null;
			}
			Long scfId = ServletRequestUtils.getLongParameter(request, "scfId", 0);
			ScFavorites scFavorites = (ScFavorites)this.favoritesService.get(ScFavorites.class, scfId);
			if(scFavorites != null && userinfo.getSuinId().equals(scFavorites.getScfUserId())){
				this.favoritesService.delete(scFavorites);
			}
			response.sendRedirect(request.getContextPath() + "/user/favorite.htm?action=initScFavoriteView");
		}catch(ErrorMessageException e) {
			response.sendRedirect(request.getContextPath() + "/user/favorite.htm?action=initScFavoriteView");
		}
		return null;
	}
	
	/**
	 * 初始化我的评论
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-7
	 */
	public ModelAndView initMyComtView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		int currentPage = ServletRequestUtils.getIntParameter(request, "currentPage", 0);
		Pagination pagination = new Pagination();
		int PAGE_SIZE = Integer.parseInt(ConstantsObj.getSysProperty(MmSettingConstant.getDefaultPageSize()));
		pagination.setPageResults(PAGE_SIZE);
		pagination.setCurrentPage(currentPage);
		
		// 添加检索条件
		String resourceType = ServletRequestUtils.getStringParameter(request, "resourceType","0");
		String search = ServletRequestUtils.getStringParameter(request, "search","");
		Short rltyType = 0;
		try {
			rltyType = Short.parseShort(resourceType);
		}catch(Exception e) {
			rltyType = 0;
		}
		
		
		// 保存查询条件为分页提供数据
		List list = this.favoritesService.getMySourceComtList(userinfo.getSuinId(), pagination,rltyType,search);
		map.put("comtList", list);
		map.put("pagination", pagination);
		map.put("currentPage", currentPage);
		map.put("pageUrl", "/user/favorite.htm?action=initMyComtView");
		map.put("search", search);
		map.put("resourceType", rltyType);
		return new ModelAndView("platform/userCenter/usercenterComt", map);
	}
}
