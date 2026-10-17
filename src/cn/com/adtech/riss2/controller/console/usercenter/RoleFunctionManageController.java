package cn.com.adtech.riss2.controller.console.usercenter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.bso.ISysRoleService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysCatalog;
import cn.com.adtech.riss2.domain.SysFunction;
import cn.com.adtech.riss2.domain.SysRole;

/**
 * 后台管理员管理控制器
 * 
 * @author MaChao
 */
@SuppressWarnings("unchecked")
public class RoleFunctionManageController extends MultiActionController {
	private ICommonService commonService;

	private ISysRoleService roleService;

	public ICommonService getCommonService() {
		return commonService;
	}

	public void setCommonService(ICommonService commonService) {
		this.commonService = commonService;
	}

	public ISysRoleService getRoleService() {
		return roleService;
	}

	public void setRoleService(ISysRoleService roleService) {
		this.roleService = roleService;
	}

	public ModelAndView initPage(HttpServletRequest request, HttpServletResponse response,
			PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String roleId = request.getParameter("roleId");
		request.setAttribute("roleId", roleId);
		SysRole sr = this.roleService.getRoleById(Long.parseLong(roleId));
		request.setAttribute("roleName", sr.getSrolName());
		return new ModelAndView("usercenter/roleFunctionManage");
	}

	/**
	 * 构造树
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView createTree(HttpServletRequest request, HttpServletResponse response,
			PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		StringBuilder sb = new StringBuilder();
		String id = request.getParameter("nodeId");
		List<SysCatalog> catagories = commonService.find("from SysCatalog");
		sb.append("[");
		if (id == null || id.equals("") || id.equals("0")) {
			for (SysCatalog sc : catagories) {
				if (sc.getParentCatalog() == null)
					sb.append(createTreeJsonStr(sc.getScatName(), sc.getScatId(), false));
			}
		}
		if (id != null && !id.equals("")) {
			List<SysCatalog> childrenCatalogs = commonService
					.find("from SysCatalog sc where sc.parentCatalog.scatId=" + Long.parseLong(id));
			if (childrenCatalogs != null && childrenCatalogs.size() > 0) {
				for (SysCatalog sc : childrenCatalogs) {
					sb.append(createTreeJsonStr(sc.getScatName(), sc.getScatId(), false));
				}
			}
			List<SysFunction> funcs = commonService
					.find("from SysFunction sf where sf.parentCatalog.scatId=" + Long.parseLong(id));
			if (funcs != null && funcs.size() > 0) {
				for (SysFunction sf : funcs) {
					sb.append(createTreeJsonStr(sf.getSfunName(), sf.getSfunId(), true));
				}
			}
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]");
		request.setAttribute("id", id);
		response.getWriter().write(sb.toString());
		response.getWriter().flush();
		System.out.println(sb.toString());
		return null;
	}

	private String createTreeJsonStr(String text, Long id, boolean isLeaf) {
		StringBuilder sb = new StringBuilder();
		sb.append("{ text: '").append(text).append("', leaf: ").append(isLeaf).append(", id: ")
				.append(id).append("},");
		return sb.toString();
	}

	/**
	 * 获得对应的catalog下面的functions
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getAllFunctionsByCatalog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String id = request.getParameter("id");
		Wrapper wrapper = this.roleService.getAllFunctionsByCataLogId(Long.parseLong(id), null);
		List list = (List) wrapper.getResult();
		String result = CommonUtil.createJsonTable(list, new String[] { "sfunId", "sfunName",
				"sfunDesc", "sfunUrl", "sfunIndex", "sfunCode", "sfunIsHidden" }, wrapper
				.getMessage());
		response.getWriter().write(result);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 获得对应的角色下面的functions
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getAllFunctionsByRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String id = request.getParameter("roleId");
		Wrapper wrapper = this.roleService.getAllFunctionsByRoleId(Long.parseLong(id), null);
		List list = (List) wrapper.getResult();
		String result = CommonUtil.createJsonTable(list, new String[] { "sfunId", "sfunName",
				"sfunDesc", "sfunUrl", "sfunIndex", "sfunCode", "sfunIsHidden" }, wrapper
				.getMessage());
		response.getWriter().write(result);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 给角色增加权限
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addFunctionsToRole(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String roleId = request.getParameter("roleId");
		String[] functionsId = request.getParameter("functionsId").split(",");
		Long[] funcsId = null;
		if (roleId != null && !roleId.equals("") && functionsId != null && functionsId.length > 0) {
			funcsId = new Long[functionsId.length];
			for (int i = 0; i < functionsId.length; i++) {
				funcsId[i] = Long.parseLong(functionsId[i]);
			}
		}
		this.roleService.saveFunctionsToRole(funcsId, Long.parseLong(roleId));
		response.getWriter().write("添加权限成功");
		response.getWriter().flush();
		return null;
	}

	/**
	 * 给角色删除权限
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteFunctionsFromRole(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String roleId = request.getParameter("roleId");
		String[] functionsId = request.getParameter("functionsId").split(",");
		Long[] funcsId = null;
		if (roleId != null && !roleId.equals("") && functionsId != null && functionsId.length > 0) {
			funcsId = new Long[functionsId.length];
			for (int i = 0; i < functionsId.length; i++) {
				funcsId[i] = Long.parseLong(functionsId[i]);
			}
		}
		this.roleService.deleteFunctionsFromRole(funcsId, Long.parseLong(roleId));
		response.getWriter().write("删除权限成功");
		response.getWriter().flush();
		return null;
	}
}
