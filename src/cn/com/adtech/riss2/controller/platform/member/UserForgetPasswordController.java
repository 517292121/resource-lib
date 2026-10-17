package cn.com.adtech.riss2.controller.platform.member;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.PasswordAuthenticator;
import cn.com.adtech.riss2.domain.SysForgetPassword;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.mail.IMailService;
import cn.com.adtech.riss2.upload.IUploadService;

import com.jbetter.hibernate.IdGenerator;

/**
 * 忘记密码
 * 
 * @author MaChao
 * @date 2010-12-20
 */
public class UserForgetPasswordController extends MultiActionController {
	private ISysUserPlatformService sysUserPlatformService;
	
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

	public ISysUserPlatformService getSysUserPlatformService() {
		return sysUserPlatformService;
	}

	public void setSysUserPlatformService(ISysUserPlatformService sysUserPlatformService) {
		this.sysUserPlatformService = sysUserPlatformService;
	}
	
	/**
	 * 忘记密码
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao 
	 * @date 2010-12-20
	 */
	public ModelAndView initForgetPasswordView(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return new ModelAndView("platform/userForgetPassword");
	}
	
	/**
	 * 忘记密码 - 第一步：选择密码找回方式;1:邮箱找回,2:密保找回；
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-20
	 */
	public ModelAndView forgetPasswordStep1(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String step = ServletRequestUtils.getStringParameter(request, "step","1");
		String resuturnPage = "platform/userGetPasswordByEmailStep1";
		if(step.equals("1")){
			resuturnPage = "platform/userGetPasswordByEmailStep1";
		}else{
			resuturnPage = "platform/userGetPasswordByQuestionStep1";
		}
		return new ModelAndView(resuturnPage);
	}
	
	/**
	 * 1：通过邮箱找回密码 - 检测用户信息是否正确
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-20
	 */
	public ModelAndView step1CheckUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String suinLoginName = ServletRequestUtils.getStringParameter(request, "suinLoginName","");
		String suinEmail = ServletRequestUtils.getStringParameter(request, "suinEmail","");
		try {
			/**
			 * 实现国际化验证,Spring会根据session中的locale对象，自动调用属性文件
			 */
			ResourceBundle rb = CommonUtil.getResourceBundle("messages",request);
			// 检测用户信息是否正确 
			if(!CommonUtil.isNotNull(suinLoginName)){
				map.put("suinLoginNameErro", rb.getString("forgetpassword.form.loginname.null"));
				throw new ErrorMessageException("用户名不能为空！");
			}
			if(suinLoginName.trim().length() > 26){
				map.put("suinLoginNameErro", rb.getString("forgetpassword.form.loginname.large"));
				throw new ErrorMessageException("用户名不能超过26个字符！");
			}
			if(!CommonUtil.isNotNull(suinEmail)){
				map.put("suinEmailErro", rb.getString("forgetpassword.form.email.null"));
				throw new ErrorMessageException("保密邮箱不能为空！");
			}
			if(suinEmail.trim().length() > 80){
				map.put("suinEmailErro", rb.getString("forgetpassword.form.email.large"));
				throw new ErrorMessageException("邮箱不能超过80个字符！");
			}
			SysUserinfo sysUserinfo = this.sysUserPlatformService.getUserinfoByLoginName(suinLoginName);
			if(sysUserinfo == null){
				map.put("suinLoginNameErro", rb.getString("forgetpassword.form.loginname.notfound"));
				throw new ErrorMessageException("用户名不存在！");
			}
			if(!sysUserinfo.getSuinEmail().equals(suinEmail)){
				map.put("suinEmailErro", rb.getString("forgetpassword.form.email.error2"));
				throw new ErrorMessageException("保密邮箱错误！");
			}
			// 将重置密码URL发送到用户邮箱里
			StringBuilder txt = new StringBuilder();
			
			
			// 域名从数据库中取值
			String www = ConstantsObj.getSysProperty(MmSettingConstant.getRiss2Website());
			Long suinId = sysUserinfo.getSuinId();
			Long key = IdGenerator.generateId();
			
			// 保存找回密码信息
			SysForgetPassword sysForgetPassword = this.sysUserPlatformService.getSysForgetPassword(suinId);
			if(sysForgetPassword != null){
				sysForgetPassword.setSfpaDate(getAddDate(ConstantsObj.FORGET_PASSWORD_MAIL_DAYS));
				key = sysForgetPassword.getSfpaKey();
			}else{
				sysForgetPassword = new SysForgetPassword();
				sysForgetPassword.setSfpaKey(key);
				sysForgetPassword.setSfpaSuinId(suinId);
				sysForgetPassword.setSfpaDate(getAddDate(ConstantsObj.FORGET_PASSWORD_MAIL_DAYS));
			}
			this.sysUserPlatformService.save(sysForgetPassword);
			
			
			String emailUrl = "http://"+www+"/checkMailUrl.htm?uid="+localUploadService.encode(suinId.toString())+"&key="+localUploadService.encode(key.toString());
			
			txt.append("<p>亲爱的：").append(sysUserinfo.getSuinEmail()).append("</p>")
			   .append("<p>欢迎使用RISS2会员找回密码功能。请点击以下链接重置您的密码（链接"+ConstantsObj.FORGET_PASSWORD_MAIL_DAYS+"天内有效） ：</p>")
			   .append("<p>"+emailUrl+"</p>")
			   .append("(如果您无法点击这个链接，请将此链接复制到浏览器地址栏后访问)")
			   .append("<p>如果您没有申请找回密码，请忽略此邮件。</p>")
			   .append("<p>RISS2自检资源平台</p>")
			   .append("<p>"+ConstantsObj.FORMAT_DATE.format(new Date())+"</p>")
			   .append("<p>（本邮件由系统自动发出，请勿回复。）</p>");
			
			try {
				mailService.initParams(sysUserinfo.getSuinEmail(), "找回密码", txt.toString());
				mailService.sendMail();
			}catch(ErrorMessageException e) {
				map.put("sendErro", rb.getString("forgetpassword.sendmail.error"));
				throw new ErrorMessageException("请求找回密码过程中，邮件发送失败。请与管理员联系！");
			}
			
			
			// 返回提示信息
			request.getSession().setAttribute("email", suinEmail);
			response.sendRedirect(request.getContextPath()+"/forgetPassword.htm?action=showMsg&type="+localUploadService.encode("1"));
			return null;
		}catch(ErrorMessageException e) {
			map.put("suinLoginName", suinLoginName);
			map.put("suinEmail", suinEmail);
			return new ModelAndView("platform/userGetPasswordByEmailStep1",map);
		}
	}
	
	/**
	 * 2：通过密保问题找回密码 - 检测用户信息是否正确
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-21
	 */
	public ModelAndView step2CheckUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String suinLoginName = ServletRequestUtils.getStringParameter(request, "suinLoginName","");
		try {
			/**
			 * 实现国际化验证,Spring会根据session中的locale对象，自动调用属性文件
			 */
			ResourceBundle rb = CommonUtil.getResourceBundle("messages",request);
			
			// 检测用户信息是否正确 
			if(!CommonUtil.isNotNull(suinLoginName))
				throw new ErrorMessageException(rb.getString("forgetpassword.form.loginname.null"));
			if(suinLoginName.trim().length() > 26)
				throw new ErrorMessageException(rb.getString("forgetpassword.form.loginname.large"));
			SysUserinfo sysUserinfo = this.sysUserPlatformService.getUserinfoByLoginName(suinLoginName);
			if(sysUserinfo == null)
				throw new ErrorMessageException(rb.getString("forgetpassword.form.loginname.notfound"));
		
			// 保存用户信息
			map.put("question", sysUserinfo.getSuinQuestion());
			map.put("suinId", sysUserinfo.getSuinId());
			
		}catch(ErrorMessageException e) {
			map.put("suinLoginName", suinLoginName);
			map.put("suinLoginNameErro", e.getMessage());
			return new ModelAndView("platform/userGetPasswordByQuestionStep1",map);
		}
		return new ModelAndView("platform/userGetPasswordByQuestionStep2",map);
	}
	
	/**
	 * 2：通过密保问题找回密码 - 检测密保信息是否正确
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-21
	 */
	public ModelAndView step2CheckAnswer(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		Long suinId = ServletRequestUtils.getLongParameter(request, "suinId",0);
		String suinAnswer = ServletRequestUtils.getStringParameter(request, "suinAnswer","");
		String question = ServletRequestUtils.getStringParameter(request, "question","");
		try {
			/**
			 * 实现国际化验证,Spring会根据session中的locale对象，自动调用属性文件
			 */
			ResourceBundle rb = CommonUtil.getResourceBundle("messages",request);
			
			// 检测用户信息是否正确 
			if(!CommonUtil.isNotNull(suinAnswer))
				throw new ErrorMessageException(rb.getString("usercenter.userinfo.suinAnswer.null"));
			if(suinAnswer.trim().length() > 26)
				throw new ErrorMessageException(rb.getString("usercenter.userinfo.suinAnswer.erro"));
			
			SysUserinfo sysUserinfo = (SysUserinfo)this.sysUserPlatformService.get(SysUserinfo.class, suinId);
			if(sysUserinfo == null){
				response.sendRedirect(request.getContextPath()+"/forgetPassword.htm?action=showMsg&type="+localUploadService.encode("3"));
				return null;
			}
			if(!suinAnswer.equals(sysUserinfo.getSuinAnswer()))
				throw new ErrorMessageException(rb.getString("forgetpassword.form.answer.error"));
			
			// 保存用户相关信息
			map.put("suinId", suinId);
			// 保存找回密码标识，防止恶意操作
			map.put("key", this.localUploadService.encode(suinId.toString()));
		}catch(ErrorMessageException e) {
			map.put("suinId", suinId);
			map.put("question", question);
			map.put("suinAnswer", suinAnswer);
			map.put("suinAnswerErro", e.getMessage());
			return new ModelAndView("platform/userGetPasswordByQuestionStep2",map);
		}
		return new ModelAndView("platform/userResetPassword",map);
	}
	
	/**
	 * 显示提示信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-21
	 */
	public ModelAndView showMsg(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/**
		 * 实现国际化验证,Spring会根据session中的locale对象，自动调用属性文件
		 */
		ResourceBundle rb = CommonUtil.getResourceBundle("messages",request);
		Map<String,Object> map = new HashMap<String, Object>();
		String type = ServletRequestUtils.getStringParameter(request, "type","");
		String email = (String)request.getSession().getAttribute("email");
		type = localUploadService.decode(type);
		String msg = "";
		if("1".equals(type) && CommonUtil.isNotNull(email)){
			msg = rb.getString("forgetpassword.tips.info1_1")+"（"+handlerMail(email)+"）"+rb.getString("forgetpassword.tips.info1_2");
			request.getSession().setAttribute("email", null);
		}else if("2".equals(type)){
			msg = rb.getString("forgetpassword.tips.info2");
		}else if("3".equals(type)){
			msg = rb.getString("forgetpassword.tips.info3");
		}else if("4".equals(type)){
			msg = rb.getString("forgetpassword.tips.info4");
		}else if("5".equals(type)){
			msg = rb.getString("mailActivation.send.erro");
		}else if("6".equals(type)){
			msg = rb.getString("mailActivation.manager.check");
		}else if("7".equals(type)){
			String registerMail = (String)request.getSession().getAttribute("email");
			msg = rb.getString("mailActivation.send.ok_1")+registerMail+rb.getString("mailActivation.send.ok_2");
			request.getSession().setAttribute("email", null);
		}else if("8".equals(type)){
			//msg = rb.getString("mailActivation.ok");
			msg = "恭喜你！你的帐号已经激活成功啦，快来<a href='javascript:openLogin();'>登录</a>吧！";
		}
		else{
			response.sendRedirect(request.getContextPath() + "/index.htm");
			return null;
		}
		map.put("msg", msg);
		return new ModelAndView("platform/pc/userGetPasswordTips",map);
	}
	
	/**
	 * 保存重置密码信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author MaChao
	 * @date 2010-12-21
	 */
	public ModelAndView resetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Map<String,Object> map = new HashMap<String, Object>();
		String key = ServletRequestUtils.getStringParameter(request, "key","");// 找回密码标识
		Long suinId = ServletRequestUtils.getLongParameter(request, "suinId",0);// 用户ID
		String suinPassword = ServletRequestUtils.getStringParameter(request, "pwd",""); // 新密码
		try {
			/**
			 * 实现国际化验证,Spring会根据session中的locale对象，自动调用属性文件
			 */
			ResourceBundle rb = CommonUtil.getResourceBundle("messages",request);
			
			// 校验用户
			SysUserinfo sysUserinfo = (SysUserinfo)this.sysUserPlatformService.get(SysUserinfo.class, suinId);
			if(sysUserinfo == null){
				response.sendRedirect(request.getContextPath()+"/forgetPassword.htm?action=showMsg&type="+localUploadService.encode("4"));
				return null;
			}
			// 校验是否为合法的用户请求
			Long tempKey = Long.parseLong(this.localUploadService.decode(key));
			if(!CommonUtil.isNotNull(key) || !tempKey.equals(suinId)){
				response.sendRedirect(request.getContextPath()+"/forgetPassword.htm?action=showMsg&type="+localUploadService.encode("3"));
				return null;
			}
			// 校验密码输入框
			if(!CommonUtil.isNotNull(suinPassword))
				throw new ErrorMessageException(rb.getString("forgetpassword.form.password.null"));
			if(suinPassword.trim().length() > 40)
				throw new ErrorMessageException(rb.getString("forgetpassword.form.password.large"));
			// 重置密码
			sysUserinfo.setSuinPassword(PasswordAuthenticator.createPassword(suinPassword));
			this.sysUserPlatformService.save(sysUserinfo);
			
			response.sendRedirect(request.getContextPath()+"/forgetPassword.htm?action=showMsg&type="+localUploadService.encode("2"));
			return null;
		}catch(ErrorMessageException e) {
			map.put("error", e.getMessage());
			map.put("key", this.localUploadService.encode(suinId.toString()));
			map.put("suinId", suinId);
			return new ModelAndView("platform/userResetPassword",map);
		}
	}
	
	/**
	 * 处理邮箱,以:zd****@126.com该形式显示
	 * @param email
	 * @return
	 */
	private String handlerMail(String email){
		if(!CommonUtil.isNotNull(email)){
			return "";
		}
		String[] mails = email.split("\\@");
		if(mails.length < 2){
			return "";
		}
		String showMail = "";
		if(mails[0].length()> 2){
			showMail = mails[0].substring(0, 3)+"******@"+mails[1];
		}else{
			showMail = email;
		}
		return showMail;
	}
	
	/**
	 * 处理日期：两天后的日期
	 * 
	 * @param days
	 * @return
	 */
	private Date getAddDate(int days){
		Calendar C = Calendar.getInstance();// 得到当前时间 
		C.add(C.DATE, days);
		return C.getTime(); 
	}
}
