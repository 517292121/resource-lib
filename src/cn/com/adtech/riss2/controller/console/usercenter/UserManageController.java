package cn.com.adtech.riss2.controller.console.usercenter;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ISysUserTypeService;
import cn.com.adtech.riss2.bso.ISysUserinfoService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.PasswordAuthenticator;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysUserType;
import cn.com.adtech.riss2.domain.SysUserinfo;

@SuppressWarnings("unchecked")
public class UserManageController extends MultiActionController {
	private ISysUserinfoService userinfoService;

	private ISysUserTypeService userTypeService;

	public ISysUserinfoService getUserinfoService() {
		return userinfoService;
	}

	public void setUserinfoService(ISysUserinfoService userinfoService) {
		this.userinfoService = userinfoService;
	}

	public ISysUserTypeService getUserTypeService() {
		return userTypeService;
	}

	public void setUserTypeService(ISysUserTypeService userTypeService) {
		this.userTypeService = userTypeService;
	}

	/**
	 * 页面初始化方法，根据type的不同转向不同的页面 主要是做页面的转向工作(会员信息)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView userManagement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// return new ModelAndView("/usercenter/" + (request.getParameter("type") == null ? "userManagement" :
		// request.getParameter("type")));
		return new ModelAndView("/usercenter/userManagement");
	}

	/**
	 * 页面初始化方法(会员审核)
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView auditUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("/usercenter/auditUser");
	}

	/**
	 * 根据条件查询所有的会员并分页
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initAllUsers(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String suinIsStop = ServletRequestUtils.getStringParameter(request, "suinIsStop");
		Wrapper wrapper = null;
		if(suinIsStop != null && !suinIsStop.trim().equals("") && (suinIsStop.trim().equals("true") || suinIsStop.trim().equals("false"))) {
			wrapper = this.userinfoService.getAllUsersBySearchCondition(ServletRequestUtils.getStringParameter(request, "condition"),
					Boolean.parseBoolean(suinIsStop), page);
		}else {
			wrapper = this.userinfoService.getAllUsersBySearchCondition(ServletRequestUtils.getStringParameter(request, "condition"), null,
					page);
		}
		List list = (List)wrapper.getResult();
		String result = CommonUtil.createJsonTable(list, new String[] {"suinId", "suinLoginName", "suinPassword", "suinName", "suinSex",
				"suinBirthday", "suinEmail", "suinIsStop", "suinJoinDate", "suinLastvisitDate", "suinMobileTelephone", "suinState",
				"suinAuditMode", "suinQuestion", "suinAnswer", "suinHint", "sutyId", "sutyName"}, wrapper.getMessage());
		String s = result.replace("\"suinIsStop\":false", "\"suinIsStop\":\"启用\"").replaceAll("\"suinIsStop\":true",
				"\"suinIsStop\":\"禁用\"");
		response.getWriter().write(s);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 删除会员并分页
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteUsers(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String params = request.getParameter("userIds");
		String[] pars = params.split(" ");
		Long[] userIds = new Long[pars.length];
		for(int i = 0; i < pars.length; i++) {
			userIds[i] = Long.parseLong(pars[i]);
		}
		if(this.userinfoService.getIssu(userIds)){
			this.userinfoService.deleteUsers(userIds);
			response.getWriter().write("删除成功！");
		}else
			response.getWriter().write("该用户已做关联，请勿删除！");
		response.getWriter().flush();
		return null;
	}

	/**
	 * 重置会员密码
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView resetUsersPassword(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String params = request.getParameter("userIds");
		String newPassword = request.getParameter("newSuinPwd");
		String[] pars = params.split(" ");
		Long[] userIds = new Long[pars.length];
		for(int i = 0; i < pars.length; i++) {
			userIds[i] = Long.parseLong(pars[i]);
		}
		if(newPassword != null) {
			this.userinfoService.updateUsersPassword(userIds, PasswordAuthenticator.createPassword(newPassword));
		}
		response.getWriter().write("{success: true, msg: '密码重置成功'}");
		response.getWriter().flush();
		return null;
	}

	/**
	 * 启用，禁用会员
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView modifyUsersStopState(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String params = request.getParameter("userIds");
		String[] pars = params.split(" ");
		Long[] userIds = new Long[pars.length];
		for(int i = 0; i < pars.length; i++) {
			userIds[i] = Long.parseLong(pars[i]);
		}
		this.userinfoService.updateUsersState(userIds);
		response.getWriter().write("启用/禁用会员成功");
		response.getWriter().flush();
		return null;
	}

	/**
	 * 根据条件查询所有的会员并分页
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initAllNotAuditUsers(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		String condition = ServletRequestUtils.getStringParameter(request, "condition", "");
		String userState = ServletRequestUtils.getStringParameter(request, "suinState");
		Wrapper wrapper = null;
		// if(userState != null && !userState.trim().equals("") &&
		// (userState.trim().equals("0") || userState.trim().equals("-1"))) {
		wrapper = this.userinfoService.getAllNotAuditedUsersBySearchCondition(condition, userState, page);
		// } else {
		// wrapper = this.userinfoService.getAllNotAuditedUsersBySearchCondition(condition, null, page);
		// }
		// wrapper = this.userinfoService.getAllNotAuditedUsersBySearchCondition(condition, "selectUserState", page);
		List list = (List)wrapper.getResult();
		String result = CommonUtil.createJsonTable(list, new String[] {"suinId", "suinLoginName", "suinPassword", "suinName", "suinSex",
				"suinBirthday", "suinEmail", "suinIsStop", "suinJoinDate", "suinLastvisitDate", "suinMobileTelephone", "suinState",
				"suinAuditMode", "suinAuditMode,suinQuestion", "suinAnswer", "suinHint", "sutyId", "sutyName"}, wrapper.getMessage());
		result = result.replace("\"suinAuditMode\":1", "\"suinAuditMode\":\"人工\"").replaceAll("\"suinAuditMode\":2",
				"\"suinAuditMode\":\"邮件\"").replaceAll("\"suinState\":0", "\"suinState\":\"待审核\"").replaceAll("\"suinState\":-1",
				"\"suinState\":\"未通过\"");
		result = result.replace("\"suinSex\":1", "\"suinSex\":\"男\"").replaceAll("\"suinSex\":2", "\"suinSex\":\"女\"").replaceAll(
				"\"suinSex\":3", "\"suinSex\":\"未知\"");
		response.getWriter().write(result);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 审核会员
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView auditUsers(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		String result = "";
		Short state = Short.parseShort(request.getParameter("suinState"));
		Long userTypeId = ServletRequestUtils.getLongParameter(request, "sutyName", 0l);
		Long userId = ServletRequestUtils.getLongParameter(request, "userId", 0l);
		SysUserinfo sui = (SysUserinfo)userinfoService.get(SysUserinfo.class, userId);
		SysUserType sysUserType = (SysUserType)userinfoService.get(SysUserType.class, userTypeId);
		if(sysUserType != null) {
			sui.setSuinState(state);
			sui.setSysUserType(sysUserType);
			userinfoService.save(sui);
			result = "{success: true, msg: '审核成功！'}";
		}else {
			result = "{success: false, msg: '审核失败！'}";
		}
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().write(result);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 查询出所有的用户类型
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void getAllUserTypes(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Wrapper wrapper = this.userTypeService.getAllUserTypesBySearchCondition(null, null);
		String userIdsStr = ServletRequestUtils.getStringParameter(request, "userIds");
		String[] userIds = userIdsStr.split(" ");
		SysUserinfo user = null;
		if(userIds.length == 1) {
			user = (SysUserinfo)this.userinfoService.get(SysUserinfo.class, Long.parseLong(userIds[0]));
		}
		List list = (List)wrapper.getResult();
		StringBuilder sb = new StringBuilder();
		String ret = "";
		if(list != null) {
			for(int i = 0; i < list.size(); i++) {
				Object[] sut = (Object[])list.get(i);
				sb.append("{ xtype: 'radio', boxLabel: '").append(sut[1]).append("'");
				if(user != null && user.getSysUserType().getSutyId().longValue() == ((Long)sut[0]).longValue()) {
					sb.append(", checked: true");
				}else {
					sb.append(", checked: ").append(sut[5]);
				}
				sb.append(", inputValue: '").append(sut[0]).append("'");
				sb.append(", name:'userType'},");
			}
			ret = sb.substring(0, sb.lastIndexOf(",")).toString();
		}
		response.getWriter().write(ret);
	}

	/**
	 * 修改用户类型
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	public void modifyUserType(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String params = request.getParameter("userIds");
		String[] pars = params.split(" ");
		Long[] userIds = new Long[pars.length];
		for(int i = 0; i < pars.length; i++) {
			userIds[i] = Long.parseLong(pars[i]);
		}
		Long userTypeId = ServletRequestUtils.getLongParameter(request, "userTypeId");
		this.userinfoService.updateUsersTypes(userIds, userTypeId);
	}
}