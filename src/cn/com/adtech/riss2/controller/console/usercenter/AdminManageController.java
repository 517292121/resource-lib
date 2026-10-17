package cn.com.adtech.riss2.controller.console.usercenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ISysManagerService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.PasswordAuthenticator;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysRole;
import cn.com.adtech.riss2.domain.SysUserRole;
import cn.com.adtech.riss2.exception.ErrorMessageException;

/**
 * 后台管理员管理控制器
 * 
 * @author MaChao
 */
@SuppressWarnings("unchecked")
public class AdminManageController extends MultiActionController {
	private ISysManagerService sysManagerService;

	public ISysManagerService getSysManagerService() {
		return sysManagerService;
	}

	public void setSysManagerService(ISysManagerService sysManagerService) {
		this.sysManagerService = sysManagerService;
	}

	/**
	 * 页面初始化方法，根据type的不同转向不同的页面 主要是做页面的转向工作
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView adminManagement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(ServletRequestUtils.getStringParameter(request, "type") != null
				&& ServletRequestUtils.getStringParameter(request, "type").equals("editManager")
				&& ServletRequestUtils.getStringParameter(request, "userId") != null) {
			Map resultMap = sysManagerService.getManagerInfoById(ServletRequestUtils.getLongParameter(request, "userId"));
			List<SysUserRole> managerRoles = (List<SysUserRole>)resultMap.get("managerRoles");
			Map<Long, String> map = new HashMap<Long, String>();
			for(int i = 0; i < managerRoles.size(); i++) {
				map.put(managerRoles.get(i).getRole().getSrolId(), "role");
			}
			List roles = this.sysManagerService.getAllRoles();
			StringBuilder sb = new StringBuilder();
			// 构造Ext checkbox
			for(int i = 0; i < roles.size(); i++) {
				SysRole s = (SysRole)roles.get(i);
				sb.append("{ boxLabel: '").append(s.getSrolName()).append("'").append(",name: 'srolIds', id: '").append(s.getSrolId())
						.append("'").append(",inputValue: '").append(s.getSrolId()).append("'").append(",xtype: 'checkbox' ");
				if(map.containsKey(s.getSrolId()))
					sb.append(",checked: true ");
				sb.append("},");
			}
			sb.deleteCharAt(sb.length() - 1);
			request.setAttribute("resultMap", resultMap);
			request.setAttribute("result", sb.toString());
		}
		if(ServletRequestUtils.getStringParameter(request, "type") != null
				&& ServletRequestUtils.getStringParameter(request, "type").equals("addManager")) {
			makeChckBoxes(request, response, null);
		}
		return new ModelAndView("/usercenter/" + (request.getParameter("type") == null ? "adminManagement" : request.getParameter("type")));
	}

	/**
	 * 根据条件查询所有的管理员并分页
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initAllManagers(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		Wrapper wrapper = this.sysManagerService.getAllManagersBySearchCondition(ServletRequestUtils.getStringParameter(request,
				"condition"), page);
		List list = (List)wrapper.getResult();
		String result = CommonUtil.createJsonTable(list, new String[] {"smanId", "smanName", "smanLoginName", "smanEmail", "smanTel",
				"smanRemark", "smanIsStop", "roles"}, wrapper.getMessage());
		response.getWriter().write(result);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 添加管理员
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public void addManager(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		SysManager manager = new SysManager();
		constructManagerByRequest(request, manager);
		SysRole[] roles = getSysRoles(request, "srolIds");
		String msg = "";
		try {
			sysManagerService.saveManager(manager, roles);
			msg = "{success: true, msg: '添加成功!!!'}";
		}catch(ErrorMessageException e) {
			msg = "{success: false, msg:'" + e.getMessage() + "'}";
		}
		response.getWriter().write(msg);
		response.getWriter().flush();
	}

	/**
	 * 更新管理员基本信息
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public void modifyManagerInfo(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		SysManager manager = (SysManager)(((List<SysManager>)sysManagerService.getManagerInfoById(
				ServletRequestUtils.getLongParameter(request, "smanId")).get("manager")).get(0));
		constructManagerByRequest(request, manager);
		SysRole[] roles = getSysRoles(request, "srolIds");
		String msg = "";
		try {
			this.sysManagerService.updateManagerInfo(manager, roles);
			msg = "{success: true, msg: '更新成功!!!'}";
		}catch(ErrorMessageException e) {
			msg = "{success: false, msg:'" + e.getMessage() + "'}";
		}
		response.getWriter().write(msg);
		response.getWriter().flush();
	}

	/**
	 * 删除管理员
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteManager(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		SysManager manager = (SysManager)this.sysManagerService.get(SysManager.class, ServletRequestUtils.getLongParameter(request,
				"userId"));
		this.sysManagerService.deleteSysManager(manager);
		response.getWriter().write("删除成功!!!");
		response.getWriter().flush();
		return null;
	}
	/**
	 * 停用/启用管理员
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public void modifyAdminStatus(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
//		SysManager manager = (SysManager)this.sysManagerService.get(SysManager.class, ServletRequestUtils.getLongParameter(request,
//		"userId"));
//		this.sysManagerService.deleteSysManager(manager);
		
		Long managerId = ServletRequestUtils.getLongParameter(request, "userId");
		
		boolean b = this.sysManagerService.isStop(managerId);
		if(b){
			this.sysManagerService.updateSysManagerStatus(managerId);
			response.getWriter().write("修改管理员状态成功!!!");
		}else{
			response.getWriter().write("请先移交该管理员创建的数据库!");
		}
		
		response.getWriter().flush();
	}

	/**
	 * 修改管理员密码
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView modifyManagerPwd(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		SysManager manager = (SysManager)this.sysManagerService.get(SysManager.class, ServletRequestUtils.getLongParameter(request,
				"userId"));
		manager.setSmanPwd(PasswordAuthenticator.createPassword(ServletRequestUtils.getStringParameter(request, "newPassword", "")));
		List<SysRole> roles = this.sysManagerService.getSysRoleByManagerId(manager.getSmanId());
		SysRole[] rolesArray = new SysRole[roles.size()];
		this.sysManagerService.updateManagerInfo(manager, roles.toArray(rolesArray));
		response.getWriter().write("{success: true, msg: '密码更改成功!!!'}");
		response.getWriter().flush();
		return null;
	}

	// 构造页面上角色组的checkbox显示字符串
	private List makeChckBoxes(HttpServletRequest request, HttpServletResponse response, PageExtNative page) {
		Wrapper wrapper = this.sysManagerService.getAllRoles(page);
		List list = (List)wrapper.getResult();
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < list.size(); i++) {
			Object[] j = (Object[])list.get(i);
			sb.append("{ boxLabel: '").append(j[1]).append("'").append(",name: 'srolIds', id: '").append(j[0]).append("'").append(
					",inputValue: '").append(j[0]).append("'").append(",xtype: 'checkbox' },");
		}
		sb.deleteCharAt(sb.length() - 1);
		request.setAttribute("result", sb.toString());
		return list;
	}

	// 填充sysManager各个属性
	private SysManager constructManagerByRequest(HttpServletRequest request, SysManager manager) throws Exception {
		manager.setSmanEmail(ServletRequestUtils.getStringParameter(request, "smanEmail"));
		manager.setSmanLoginName(ServletRequestUtils.getStringParameter(request, "smanLoginName"));
		manager.setSmanName(ServletRequestUtils.getStringParameter(request, "smanName"));
		manager.setSmanTel(ServletRequestUtils.getStringParameter(request, "smanTel"));
		manager.setSmanRemark(ServletRequestUtils.getStringParameter(request, "smanRemark"));
		manager.setSmanType(1);
		manager.setSmanIsStop(false);
		if(ServletRequestUtils.getStringParameter(request, "smanPwd1") != null) {
			String pwd = PasswordAuthenticator.createPassword(ServletRequestUtils.getStringParameter(request, "smanPwd1"));
			manager.setSmanPwd(pwd);
		}
		return manager;
	}

	// 根据客户端获取权限组
	private SysRole[] getSysRoles(HttpServletRequest request, String param) {
		SysRole[] roles = null;
		String[] ids = request.getParameterValues(param);
		if(ids != null) {
			roles = new SysRole[ids.length];
			for(int i = 0; i < ids.length; i++) {
				SysRole role = (SysRole)sysManagerService.get(SysRole.class, Long.parseLong(ids[i]));
				roles[i] = role;
			}
		}
		return roles;
	}
}
