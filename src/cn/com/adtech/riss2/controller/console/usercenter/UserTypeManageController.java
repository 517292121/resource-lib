package cn.com.adtech.riss2.controller.console.usercenter;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ISysUserTypeService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.PageExtNative;
import cn.com.adtech.riss2.core.Wrapper;
import cn.com.adtech.riss2.domain.SysUserType;
import cn.com.adtech.riss2.exception.ErrorMessageException;

@SuppressWarnings("unchecked")
public class UserTypeManageController extends MultiActionController {
	private ISysUserTypeService userTypeService;

	public ISysUserTypeService getUserTypeService() {
		return userTypeService;
	}

	public void setUserTypeService(ISysUserTypeService userTypeService) {
		this.userTypeService = userTypeService;
	}

	/**
	 * 页面初始化方法，根据type的不同转向不同的页面 主要是做页面的转向工作
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ModelAndView userTypeManagement(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("/usercenter/"
				+ (request.getParameter("type") == null ? "userTypeManagement" : request.getParameter("type")));
	}

	/**
	 * 根据条件查询所有的会员类型并分页
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView initAllUserTypes(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		Wrapper wrapper = this.userTypeService.getAllUserTypesBySearchCondition(ServletRequestUtils
				.getStringParameter(request, "condition"), page);
		List list = (List)wrapper.getResult();
		String result = CommonUtil.createJsonTable(list, new String[] {"sutyId", "sutyName", "sutyDoc", "sutyPic", "sutyVideo",
				"sutyDefault","sutyPurview"}, wrapper.getMessage());
		String s = result.replace("\"sutyDoc\":0", "\"sutyDoc\":\"不可浏览\"").replaceAll("\"sutyDoc\":1", "\"sutyDoc\":\"可浏览预览版\"")
				.replaceAll("\"sutyDoc\":2", "\"sutyDoc\":\"可浏览全文\"").replaceAll("\"sutyDoc\":3", "\"sutyDoc\":\"可下载\"").replaceAll(
						"\"sutyPic\":0", "\"sutyPic\":\"不可浏览\"").replaceAll("\"sutyPic\":1", "\"sutyPic\":\"可浏览缩略图\"").replaceAll(
						"\"sutyPic\":2", "\"sutyPic\":\"可浏览原图\"").replaceAll("\"sutyVideo\":0", "\"sutyVideo\":\"不可观看\"").replaceAll(
						"\"sutyVideo\":1", "\"sutyVideo\":\"可观看预览版\"").replaceAll("\"sutyVideo\":2", "\"sutyVideo\":\"可观看高清且下载\"").replaceAll(
						"\"sutyDefault\":true", "\"sutyDefault\":\"是\"").replaceAll("\"sutyDefault\":false", "\"sutyDefault\":\"否\"");
		response.getWriter().write(s);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 修改会员类型
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView modifyUserType(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		Long typeId = Long.parseLong(request.getParameter("userTypeId"));
		SysUserType type = new SysUserType();
		type.setSutyName(ServletRequestUtils.getStringParameter(request, "sutyName"));
		type.setSutyDefault(ServletRequestUtils.getBooleanParameter(request, "sutyDefault"));
		type.setSutyDoc(Short.parseShort(ServletRequestUtils.getStringParameter(request, "sutyDoc","3")));
		type.setSutyPic(Short.parseShort(ServletRequestUtils.getStringParameter(request, "sutyPic","2")));
		type.setSutyVideo(Short.parseShort(ServletRequestUtils.getStringParameter(request, "sutyVideo","2")));
		int purview = ServletRequestUtils.getIntParameter(request, "sutyPurview", SysUserType.PURIVEW_DOWNLOAD);
		type.setSutyPurview(purview);
		String info = "";
		try {
			this.userTypeService.updateUserType(type, typeId);
			info = "{success: true, msg: '更新成功'}";
		}catch(ErrorMessageException e) {
			info = "{success: false, msg: '" + e.getMessage() + "'}";
		}
		response.getWriter().write(info);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 添加会员类型
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView addUserType(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		SysUserType type = new SysUserType();
		type.setSutyName(ServletRequestUtils.getStringParameter(request, "sutyName"));
		type.setSutyDefault(ServletRequestUtils.getBooleanParameter(request, "sutyDefault"));
		type.setSutyDoc(Short.parseShort(ServletRequestUtils.getStringParameter(request, "sutyDoc","3")));
		type.setSutyPic(Short.parseShort(ServletRequestUtils.getStringParameter(request, "sutyPic","2")));
		type.setSutyVideo(Short.parseShort(ServletRequestUtils.getStringParameter(request, "sutyVideo","2")));
		int purview = ServletRequestUtils.getIntParameter(request, "sutyPurview", SysUserType.PURIVEW_DOWNLOAD);
		type.setSutyPurview(purview);
		String info = "";
		try {
			this.userTypeService.saveSysUserType(type);
			info = "{success: true, msg: '添加用户类型成功'}";
		}catch(ErrorMessageException e) {
			info = "{success: false, msg: '" + e.getMessage() + "'}";
		}
		response.getWriter().write(info);
		response.getWriter().flush();
		return null;
	}

	/**
	 * 删除会员类型
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView deleteUserType(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		try {
			this.userTypeService.deleteSysUserType(Long.parseLong(request.getParameter("typeId")));
		}catch(ErrorMessageException e) {
			response.getWriter().write(e.getMessage());
			response.getWriter().flush();
		}
		return null;
	}

	/**
	 * 根据id查找用户类型
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	public ModelAndView getUserTypeById(HttpServletRequest request, HttpServletResponse response, PageExtNative page) throws Exception {
		response.setContentType("text/html; charset=UTF-8");
		SysUserType userType = this.userTypeService.getSysUserTypeById(ServletRequestUtils.getLongParameter(request, "userTypeId"));
		response.getWriter().write(buildExtRadioStr(userType));
		response.getWriter().flush();
		return null;
	}

	// 组拼对应的用户类型的对资源的各种操作权限
	private String buildExtRadioStr(SysUserType userType) {
		StringBuilder sb = new StringBuilder();
//		sb.append("[{xtype:'textfield',fieldLabel:'类型名称',name:'sutyName'").append(
//				",anchor:'90%',allowBlank:false,blankText:'该项不能为空',value:'").append(userType.getSutyName()).append("'},{").append(
//				"xtype:'panel',layout:'column',fieldLabel:'文档操作',isFormField:true").append(
//				",items:[{xtype:'radio',boxLabel:'不可浏览',name:'sutyDoc'").append(",inputValue:0,").append("checked:").append(
//				userType.getSutyDoc() == 0 ? true : false).append("}").append(
//				",{xtype:'radio',boxLabel:'可浏览预览版',name:'sutyDoc',inputValue:1").append(",checked:").append(
//				userType.getSutyDoc() == 1 ? true : false).append("}").append(
//				",{xtype:'radio',boxLabel:'可浏览全文',name:'sutyDoc',inputValue:2").append(",checked:").append(
//				userType.getSutyDoc() == 2 ? true : false).append("}").append(",{xtype:'radio',boxLabel:'可下载',name:'sutyDoc',inputValue:3")
//				.append(",checked:").append(userType.getSutyDoc() == 3 ? true : false).append("}]}");
//		sb.append(",{xtype:'panel',layout:'column',fieldLabel:'图片操作',isFormField:true,items:[").append(
//				"{xtype:'radio',boxLabel:'不可浏览',name:'sutyPic',inputValue:0,").append("checked:").append(
//				userType.getSutyPic() == 0 ? true : false).append("}").append(
//				",{xtype:'radio',boxLabel:'可浏览缩略图',name:'sutyPic',inputValue:1,").append("checked:").append(
//				userType.getSutyPic() == 1 ? true : false).append("}").append(
//				",{xtype:'radio',boxLabel:'可浏览原图',name:'sutyPic',inputValue:2,").append("checked:").append(
//				userType.getSutyPic() == 2 ? true : false).append("}]}");
//		sb.append(",{xtype:'panel',layout:'column',fieldLabel:'视频操作',isFormField:true,items:[").append(
//				"{xtype:'radio',boxLabel:'不可观看',name:'sutyVideo',inputValue:0,").append("checked:").append(
//				userType.getSutyVideo() == 0 ? true : false).append("}").append(
//				",{xtype:'radio',boxLabel:'可观看预览版',name:'sutyVideo',inputValue:1,").append("checked:").append(
//				userType.getSutyVideo() == 1 ? true : false).append("}").append(
//				",{xtype:'radio',boxLabel:'可观看高清且下载',name:'sutyVideo',inputValue:2,").append("checked:").append(
//				userType.getSutyVideo() == 2 ? true : false).append("}]}");
//		sb.append(",{xtype:'panel',layout:'column',fieldLabel:'默认',isFormField:true,items:[").append(
//				"{xtype:'radio',boxLabel:'是',name:'sutyDefault',inputValue:true,").append("checked:").append(userType.getSutyDefault())
//				.append("}").append(",{xtype:'radio',boxLabel:'否',name:'sutyDefault',inputValue:false,").append("checked:").append(
//						userType.getSutyDefault() == false ? "true" : "false").append("}]}]");
		sb.append("[{xtype:'textfield',fieldLabel:'类型名称',name:'sutyName'").append(
		",anchor:'90%',allowBlank:false,blankText:'该项不能为空',value:'").append(userType.getSutyName()).append("'},{").append(
		"xtype:'panel',fieldLabel:'权限设置',isFormField:true").append(
		",items:[{xtype:'radio',boxLabel:'不可访问',name:'sutyPurview'").append(",inputValue:1,").append("checked:").append(
		userType.getSutyPurview() == 1 ? true : false).append("},").append("{xtype:'radio',boxLabel:'不可浏览',name:'sutyPurview'").
		append(",inputValue:2,").append("checked:").append(userType.getSutyPurview() == 2 ? true : false).append("}") .append(
		",{xtype:'radio',boxLabel:'可浏览预览版',name:'sutyPurview',inputValue:3").append(",checked:").append(
		userType.getSutyPurview() == 3 ? true : false).append("}").append(
		",{xtype:'radio',boxLabel:'可浏览全文',name:'sutyPurview',inputValue:4").append(",checked:").append(
		userType.getSutyPurview() == 4 ? true : false).append("}").append(",{xtype:'radio',boxLabel:'可下载',name:'sutyPurview',inputValue:5")
		.append(",checked:").append(userType.getSutyPurview() == 5 ? true : false).append("}]}");
sb.append(",{xtype:'panel',layout:'column',fieldLabel:'默认',isFormField:true,items:[").append(
		"{xtype:'radio',boxLabel:'是',name:'sutyDefault',inputValue:true,").append("checked:").append(userType.getSutyDefault())
		.append("}").append(",{xtype:'radio',boxLabel:'否',name:'sutyDefault',inputValue:false,").append("checked:").append(
				userType.getSutyDefault() == false ? "true" : "false").append("}]}]");
		return sb.toString();
	}

	public ModelAndView getUserTypeList(HttpServletRequest request, HttpServletResponse response) {
		List list = userTypeService.getUserTypeList();
		String result = CommonUtil.createJsonTable(list, new String[] {"sutyId", "sutyName"}, list.size() + "");
		response.setContentType("text/html; charset=UTF-8");
		try {
			response.getWriter().write(result);
			response.getWriter().flush();
		}catch(IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}