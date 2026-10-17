package cn.com.adtech.riss2.controller.console.sc;

import java.util.Date;
import java.util.List;
import java.util.Locale;
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
import cn.com.adtech.riss2.domain.sc.ScConsultation;
import cn.com.adtech.riss2.domain.sc.ScConsultationType;

/**
 * 参考咨询
 * 
 * @author MaChao
 * @date 2010-11-15
 */
public class CreateScConsultationController extends AbsFormController {
	@SuppressWarnings("unchecked")
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		// 获取登录用户实时数据
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo != null) {
			request.setAttribute("userinfo", userinfo);
		}
		List scConsultationTypeList = ((IScService)getCommomService()).getScConsultationType(null, null, null);
		request.setAttribute("scConsultationTypeList", scConsultationTypeList);
		ScConsultation scConsultation = (ScConsultation)makeObject("sctyId", ScConsultation.class, request);
		return scConsultation;
	}

	@Override
	protected void validate(HttpServletRequest request, Object obj, Errors errors) {
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
		
		ScConsultation sc = (ScConsultation)obj;
		String sccoTitle = sc.getSccoTitle();
		// 校验标题
		if(!ServiceUtil.isNotNull(sccoTitle)) {
			errors.rejectValue("sccoTitle", null, rb.getString("usercenter.zixun.erro.title"));
		}else if(sccoTitle.length() > 300) {
			errors.rejectValue("sccoTitle", null, rb.getString("usercenter.zixun.erro.title2"));
		}
		String sccoContent = sc.getSccoContent();
		// 校验咨询内容
		if(sccoContent.length() > 8000) {
			errors.rejectValue("sccoContent", null, rb.getString("usercenter.zixun.erro.des"));
		}
		// 校验咨询分类
		Long sctyId = null;
		try {
			String temp = ServletRequestUtils.getStringParameter(request, "sctyId");
			sctyId = Long.parseLong(temp);
		}catch(Exception e) {
			sctyId = null;
		}
		
		if(sctyId == null || sctyId == -1) {
			errors.rejectValue("sctyId", null, rb.getString("usercenter.zixun.erro.type"));
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
		// 保存咨询信息
		ScConsultation sc = (ScConsultation)obj;
		
		sc.setSuinId(userinfo.getSuinId());
		sc.setSccoCreationTime(new Date());
		sc.setSccoState(ScConsultation.UN_RESPONSE);
		((IScService)getCommomService()).save(sc);
		// 更新分类总数
		ScConsultationType sct = (ScConsultationType)((IScService)getCommomService()).get(ScConsultationType.class, sc.getSctyId());
		if(sct != null) {
			sct.setSctyNumber(sct.getSctyIndex() + 1);
			((IScService)getCommomService()).save(sct);
		}
		response.sendRedirect(request.getContextPath() + "/platform/scConsultation.htm?action=myConsultationView");
		return new ModelAndView("platform/userCenter/usercenterMyConsultation");
	}

	@Override
	protected void supportData(HttpServletRequest request) {}
}
