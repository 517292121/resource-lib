package cn.com.adtech.riss2.controller.platform.member;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.AbstractController;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.PasswordAuthenticator;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.domain.SysEmailActivation;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.mail.IMailService;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.hibernate.IdGenerator;

/**
 * 会员注册--数据提交
 * 
 * @author MaChao
 * @date 2010-9-19
 */
public class MemberRegisterSubmitController extends AbstractController {
	private IMailService mailService;
	
	private IUploadService localUploadService;
	
	public IUploadService getLocalUploadService() {
		return localUploadService;
	}

	public void setLocalUploadService(IUploadService localUploadService) {
		this.localUploadService = localUploadService;
	}

	public IMailService getMailService() {
		return mailService;
	}

	public void setMailService(IMailService mailService) {
		this.mailService = mailService;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String loginName = ServletRequestUtils.getStringParameter(request, "loginName", "");
//		String password = ServletRequestUtils.getStringParameter(request, "password", "");
		String question = ServletRequestUtils.getStringParameter(request, "question", "");
		String answer = ServletRequestUtils.getStringParameter(request, "answer", "");
		String hint = ServletRequestUtils.getStringParameter(request, "hint", "");
		String email = ServletRequestUtils.getStringParameter(request, "email", "");
		String name = ServletRequestUtils.getStringParameter(request, "name", "");
		String sex = ServletRequestUtils.getStringParameter(request, "sex", "3");
		String birthday = ServletRequestUtils.getStringParameter(request, "birthday", "");
		String password = ServletRequestUtils.getStringParameter(request, "password_md5", "");
		String mobileTelephone = ServletRequestUtils.getStringParameter(request, "mobileTelephone", "");
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
		Map result = new HashedMap();
		try {
			if(sysUserPlatformService.getUserinfoByLoginName(loginName) != null) {
				throw new ErrorMessageException(rb.getString("register.msg.error"));
			}
			SysUserinfo userinfo = new SysUserinfo();
			userinfo.setSuinLoginName(loginName);
			try {
				userinfo.setSuinPassword(PasswordAuthenticator.createPassword(password));
			}catch(Exception e) {
				throw new ErrorMessageException(rb.getString("register.password.erro2"));
			}
			userinfo.setSuinQuestion(question);
			userinfo.setSuinAnswer(answer);
			userinfo.setSuinHint(hint);
			userinfo.setSuinEmail(email);
			if(!ServiceUtil.isNotNull(name)){
				throw new ErrorMessageException(rb.getString("register.msg.name.info"));
			}else {
				userinfo.setSuinName(name);
			}
			try {
				userinfo.setSuinSex(Short.valueOf(sex));
			}catch(Exception e) {
				throw new ErrorMessageException(rb.getString("register.sex"));
			}
			if(ServiceUtil.isNotNull(birthday)) {
				try {
					userinfo.setSuinBirthday(ConstantsObj.FORMAT_DATE.parse(birthday));
				}catch(ParseException e) {
					throw new ErrorMessageException(rb.getString("register.birthday.msg"));
				}
			}
			
//			if(SysUserinfo.SEX_MALE.equals(userinfo.getSuinSex())){
//				userinfo.setSuinImageUrl(ConstantsObj.SUIN_IMAGE_MALE);
//			}else if(SysUserinfo.SEX_FEMALE.equals(userinfo.getSuinSex())){
//				userinfo.setSuinImageUrl(ConstantsObj.SUIN_IMAGE_FEMALE);
//			}else {
//				userinfo.setSuinImageUrl(ConstantsObj.SUIN_IMAGE_UNKNOWN);
//			}
			
			userinfo.setSuinMobileTelephone(mobileTelephone);
			// 设置默认样式
			Long skinId = 2l;
			try {
				skinId = Long.parseLong(ConstantsObj.getSysProperty(MmSettingConstant.getRiss2DefaultSkin()));
			}catch(Exception ex) {
				skinId = 2l;
			}
			userinfo.setSuinSstyId(skinId);
			// 设置用户类型
			userinfo.setSysUserType(sysUserPlatformService.getDefaultSysUserType());
			// 默认设置
			userinfo.setSuinIsStop(false);
			userinfo.setSuinState(Short.valueOf("0"));
			userinfo.setSuinAuditMode(new Short("1"));
			userinfo.setSuinJoinDate(new Date());
			try {
				sysUserPlatformService.save(userinfo);
			}catch(Exception e) {
				e.printStackTrace();
				throw new ErrorMessageException(rb.getString("register.submit.error"));
			}
			
			// ------ 邮件审核 begin -----------------------------------------------------------------
			String type = ConstantsObj.getSysProperty(MmSettingConstant.getUserRegisterCheckType());
			if("2".equals(type)){
				// 保存用户审核方式
				userinfo.setSuinAuditMode(Short.valueOf("2"));
				sysUserPlatformService.save(userinfo);
				try {
					// 域名从数据库中取值
					String www = ConstantsObj.getSysProperty(MmSettingConstant.getRiss2Website());
					Long key = IdGenerator.generateId();
					Long suinId = userinfo.getSuinId();
					String emailUrl = "http://"+www+"/checkRegisterMailUrl.htm?uid="+localUploadService.encode(suinId.toString())+"&key="+localUploadService.encode(key.toString());
					
					// 构造邮件信息
					StringBuilder txt = new StringBuilder();
					txt.append("<p>").append(userinfo.getSuinLoginName()).append("，你好！</p>")
					   .append("<p>恭喜你成功注册本资源库平台！</p>")
					   .append("<p>请点击下面的链接，完成激活操作：</p>")
					   .append("<p>").append(emailUrl).append("</p>")
					   .append("如果该链接不能点击，请将链接复制到浏览器地址栏中访问，即可成功激活你的用户！")
					   .append("<p>如果您没有注册帐户，请忽略此邮件。</p>")
					   .append("<p>"+ConstantsObj.FORMAT_DATE.format(new Date())+"</p>")
					   .append("<p>（本邮件由系统自动发出，请勿回复。）</p>");
					
					// 发送邮件
					mailService.initParams(email, "平台用户激活", txt.toString());
					mailService.sendMail();
					
					// 保存邮件激活信息
					SysEmailActivation sysEmailActivation = new SysEmailActivation();
					sysEmailActivation.setSeacSuinId(suinId);
					sysEmailActivation.setSeacKey(key);
					this.sysUserPlatformService.save(sysEmailActivation);
					
					// 页面跳转
					request.getSession().setAttribute("email", email);
					response.sendRedirect(request.getContextPath()+"/forgetPassword.htm?action=showMsg&type="+localUploadService.encode("7"));
					return null;
					
				}catch(Exception e) {
					e.printStackTrace();
					try {
						response.sendRedirect(request.getContextPath()+"/forgetPassword.htm?action=showMsg&type="+localUploadService.encode("5"));
						return null;
					}catch(Exception e1) {
						return null;
					}
				}
			}else{
				userinfo.setSuinAuditMode(Short.valueOf("1"));
				sysUserPlatformService.save(userinfo);
				// 页面跳转
				try {
					request.getSession().setAttribute("email", email);
					response.sendRedirect(request.getContextPath()+"/forgetPassword.htm?action=showMsg&type="+localUploadService.encode("6"));
					return null;
				}catch(Exception e) {
					return null;
				}
			}
			
			// ------ 邮件审核 end -----------------------------------------------------------------
			
//			request.setAttribute("dontAutoReturn", "1");
		}catch(ErrorMessageException e) {
			e.printStackTrace();
			result.put("loginName", loginName);
			result.put("question", question);
			result.put("answer", answer);
			result.put("hint", hint);
			result.put("email", email);
			result.put("name", name);
			result.put("sex", sex);
			result.put("birthday", birthday);
			result.put("mobileTelephone", mobileTelephone);
			result.put("message", e.getMessage());
			return new ModelAndView("platform/register", result);
		}
	}

	private ISysUserPlatformService sysUserPlatformService;

	public ISysUserPlatformService getSysUserPlatformService() {
		return sysUserPlatformService;
	}

	public void setSysUserPlatformService(ISysUserPlatformService sysUserPlatformService) {
		this.sysUserPlatformService = sysUserPlatformService;
	}
}
