package cn.com.adtech.riss2.controller.console.sc;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import cn.com.adtech.riss2.bso.sc.IScService;
import cn.com.adtech.riss2.controller.common.AbsFormController;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.sc.ScIssue;
import cn.com.adtech.riss2.domain.sc.ScIssueType;

/**
 * 文献互助_发布文献求助信息
 * 
 * @author MaChao
 * @date 2010-11-2
 */
public class CreateScIssueController extends AbsFormController {
	@SuppressWarnings("unchecked")
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		// 获取登录用户实时数据
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo != null) {
			request.setAttribute("userinfo", userinfo);
		}
		// 获取求助分类
		List scIssueTypeList = ((IScService)getCommomService()).getScIssueTypeList();
		request.setAttribute("scIssueTypeList", scIssueTypeList);
		ScIssue scIssue = (ScIssue)makeObject("scisId", ScIssue.class, request);
		return scIssue;
	}

	@Override
	protected void validate(HttpServletRequest request, Object obj, Errors errors) {
		ScIssue scIssue = (ScIssue)obj;
		/**
		 * 实现国际化验证,Spring会根据session中的locale对象，自动调用属性文件
		 */
		Object val = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		Locale locale = null;
		if(val != null){
			locale = (Locale)val;
		}else{
			locale = new Locale("zh", "CN");  
		}
		ResourceBundle rb = ResourceBundle.getBundle("messages",locale);
		
		// 校验求助标题
		String scisTitle = scIssue.getScisTitle();
		if(!ServiceUtil.isNotNull(scisTitle)) {
			errors.rejectValue("scisTitle", null, rb.getString("usercenter.issue.create.error.title"));
		}else if(scisTitle.length() > 200) {
			errors.rejectValue("scisTitle", null, rb.getString("usercenter.issue.create.error.title2"));
		}
		// 校验求助内容
		String scisContent = scIssue.getScisContent();
		if(scisContent.length() > 8000) {
			errors.rejectValue("scisContent", null, rb.getString("usercenter.issue.create.error.content"));
		}
		// 校验求助分类
		Long scisScitId = null;
		try {
			String temp = ServletRequestUtils.getStringParameter(request, "scisScitId");
			scisScitId = Long.parseLong(temp);
		}catch(Exception e) {
			scisScitId = null;
		}
		
		if(scisScitId == null || scisScitId == -1) {
			errors.rejectValue("scisScitId", null, rb.getString("usercenter.issue.create.error.type"));
		}
		
	}

	@Override
	protected ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object obj, BindException errors)
			throws Exception {
		// 获取登录用户ID
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		// 保存求助信息
		ScIssue scIssue = (ScIssue)obj;
		String message = "";
		scIssue.setScisSuinId(userinfo.getSuinId());
		scIssue.setScisCreationTime(new Date());
		scIssue.setScisState(ScIssue.UN_SOLVE);
		scIssue.setScisIsuse(ScIssue.CAN_USE);
		scIssue.setScisReplyNumber(0L);
		((IScService)getCommomService()).save(scIssue);
		// 更新分类总数
		ScIssueType sit = (ScIssueType)((IScService)getCommomService()).get(ScIssueType.class, scIssue.getScisScitId());
		if(sit != null) {
			sit.setScitNumber(sit.getScitNumber() + 1);
			((IScService)getCommomService()).save(sit);
		}
		message = "保存成功！";
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("message", message);
		map.put("scIssue", scIssue);
		response.sendRedirect(request.getContextPath() + "/platform/scIssue.htm?action=initPlatformMyIssueView");
		return new ModelAndView("platform/userCenter/usercenterMyIssue", map);
	}

	@Override
	protected void supportData(HttpServletRequest request) {}
}
