package cn.com.adtech.riss2.controller.console.usercenter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ISysRoleService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysRole;
import cn.com.adtech.riss2.exception.ErrorMessageException;

@SuppressWarnings("unchecked")
public class RoleManageController extends MultiActionController {
	private ISysRoleService roleService;

	public ISysRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(ISysRoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * 页面初始化方法，根据type的不同转向不同的页面 主要是做页面的转向工作
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView roleManagement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("/usercenter/" + (request.getParameter("type") == null ? "roleManagement" : request.getParameter("type")));
	}

	/**
	 * 根据条件查询所有的角色并分页
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initAllRoles(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		Wrapper wrapper = this.roleService.getAllRolesBySearchCondition(ServletRequestUtils.getStringParameter(request, "condition"), page);
		List list = (List)wrapper.getResult();
		String result = CommonUtil.createJsonTable(list, new String[] {"srolId", "srolName", "srolCode", "srolDesc", "srolIsSysRole"},
				wrapper.getMessage());
		String s = result.replaceAll("\"srolIsSysRole\":true", "\"srolIsSysRole\":\"是\"").replaceAll("\"srolIsSysRole\":false",
				"\"srolIsSysRole\":\"不是\"");
		response.getWriter().write(s);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 添加新的角色
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addRole(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		SysRole role = new SysRole();
		role.setSrolDesc(request.getParameter("srolDesc"));
		role.setSrolName(request.getParameter("srolName"));
		role.setSrolCode(request.getParameter("srolCode"));
		role.setSrolIsSysRole(false);
		String msg = "";
		try {
			this.roleService.saveRole(role);
			msg = "{success: true, msg:'增加角色成功!!'}";
		}catch(ErrorMessageException e) {
			msg = "{success: false, msg: '" + e.getMessage() + "'}";
		}
		response.getWriter().write(msg);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 删除角色
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteRole(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		SysRole role = this.roleService.getRoleById(Long.parseLong(request.getParameter("roleId")));
		String msg = "";
		try {
			this.roleService.deleteRole(role);
			msg = "删除角色成功";
		}catch(ErrorMessageException e) {
			msg = e.getMessage();
		}
		response.getWriter().write(msg);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 更新角色信息
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView modifyRole(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		SysRole role = this.roleService.getRoleById(Long.parseLong(request.getParameter("roleId")));
		role.setSrolDesc(request.getParameter("srolDesc"));
		role.setSrolName(request.getParameter("srolName"));
		role.setSrolCode(request.getParameter("srolCode"));
		String msg = "";
		try {
			this.roleService.updateRole(role);
			msg = "{success: true, msg: '更新角色成功!!'}";
		}catch(ErrorMessageException e) {
			msg = "{success: false, msg: '" + e.getMessage() + "'}";
		}
		response.getWriter().write(msg);
		response.getWriter().flush();
		return null;
	}
}