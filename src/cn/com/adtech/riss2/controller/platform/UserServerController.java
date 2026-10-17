package cn.com.adtech.riss2.controller.platform;

import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.adtech.riss2.bso.ISysUserPlatformService;
import cn.com.adtech.riss2.controller.platform.local.LocalecUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;
import cn.com.adtech.riss2.core.PasswordAuthenticator;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.SmsSend;
import cn.com.adtech.riss2.domain.SysEmailActivation;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.mail.IMailService;

import com.jbetter.hibernate.IdGenerator;


/**
 * 
 * 前端注解版本控制器基本处理
 *
 * @author MaChao
 *
 * @date：Jan 2, 2018
 */
@Controller
public class UserServerController {
	
	@Resource(name="sysUserPlatformService")
	private ISysUserPlatformService userPlatformService;
	
	@Resource(name="mailService")
	private IMailService mailService;
	
	//进入用户中心
	@RequestMapping(value="/member/login")
	public String login() {
		return "platform/pc/login";
	}
	
	//退出登录
	@RequestMapping(value="/member/exit")
	public void loginOut(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		Map<String, Object> map = new HashMap<String, Object>();
		PrintWriter out = response.getWriter();
		try {
			request.getSession().setAttribute("sysUserinfo", null);
			map.put("success", "true");
			String json =  JSONObject.fromObject(map).toString();
			out.print(json);
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("msg", e.getMessage());
			String json =  JSONObject.fromObject(map).toString();
			out.print(json);
		}
		out.flush();
		out.close();
	}
	

	//进入用户中心
	@RequestMapping(value="/member/center")
	public String usercenter(HttpServletRequest request, HttpServletResponse response) {
		SysUserinfo userinfo = (SysUserinfo)ServiceUtil.getCurrentUserinfo(request);
		if(userinfo == null) {
			String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			return "redirect:" + basePath + "/index";
		}
		return "platform/userCenter/usercenterIndex";
	}
	
	
	/********用户注册处理****************************/
	//进入用户中心
	@RequestMapping(value="/register")
	public String register(Model model, HttpServletRequest request) {
		SysUserinfo user = (SysUserinfo) request.getSession().getAttribute("sysUserinfo");
		//获取国际化
		LocalecUtil localecUtil=new LocalecUtil();
		if(user != null) { // 已经登录的则直接进入会员空间消息订阅
			return "redirect:" + request.getContextPath() + "/member/center";
		}else {
			Map<String, Object> map = new LinkedHashMap<String, Object>();
			map.put("title",localecUtil.get("user.registration"));
			model.addAllAttributes(map);
			return "platform/pc/register";
		}
	}
	//服务条款
	@RequestMapping(value="/reg/server")
	public String regServer() {
		return "platform/pc/regServer";
	}
	
	//验证手机号码及发送验证短信
	@RequestMapping(value="/reg/email/save")
	public void emailResSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		//获取国际化
		LocalecUtil localecUtil=new LocalecUtil();
		
		String loginName = ServletRequestUtils.getStringParameter(request, "loginName", "");
		String loginPwd = ServletRequestUtils.getStringParameter(request, "loginPwd", "");
		String email = ServletRequestUtils.getStringParameter(request, "email", "");
		String imgCode = ServletRequestUtils.getStringParameter(request, "imgCode", "");
		
		try {
			if(userPlatformService.getUserinfoByLoginName(loginName) != null){
				throw new ErrorMessageException(localecUtil.get("user.number.registered"));
			}
			String sysImgCode = request.getSession().getAttribute("REGIMGCODE").toString().toUpperCase();
			if(!imgCode.equals(sysImgCode)){
				throw new ErrorMessageException(localecUtil.get("user.graphical.code.wrong"));
			}
			
			SysUserinfo userinfo = new SysUserinfo();
			userinfo.setSuinLoginName(loginName);
			try {
				userinfo.setSuinPassword(PasswordAuthenticator.createPassword(loginPwd));
			}catch(Exception e) {
				throw new ErrorMessageException(localecUtil.get("user.password.wrong"));
			}
			
			if(userPlatformService.getUserinfoBySuinEmail(email) != null){
				throw new ErrorMessageException(localecUtil.get("user.mailbox.binding.number"));
			}
			
			userinfo.setSuinEmail(email);
			
			// 设置默认样式
			Long skinId = 2l;
			try {
				skinId = Long.parseLong(ConstantsObj.getSysProperty(MmSettingConstant.getRiss2DefaultSkin()));
			}catch(Exception ex) {
				skinId = 2l;
			}
			userinfo.setSuinName("-");
			userinfo.setSuinSstyId(skinId);
			userinfo.setSysUserType(userPlatformService.getDefaultSysUserType());
			userinfo.setSuinIsStop(false);
			userinfo.setSuinAuditMode(new Short("1"));
			userinfo.setSuinState(Short.valueOf("0"));
			userinfo.setSuinJoinDate(new Date());
			
			userPlatformService.save(userinfo);
			// ------ 邮件审核 begin -----------------------------------------------------------------
			String type = ConstantsObj.getSysProperty(MmSettingConstant.getUserRegisterCheckType());
			if("2".equals(type)){
				// 保存用户审核方式
				userinfo.setSuinAuditMode(Short.valueOf("2"));
				try {
					// 域名从数据库中取值
					//String www = ConstantsObj.getSysProperty(MmSettingConstant.getRiss2Website());
					//拼接访问地址
					String path = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
					Long key = IdGenerator.generateId();
					Long suinId = userinfo.getSuinId();
					
					String emailUrl = path + "checkRegisterMailUrl.htm?uid="+CommonUtil.encode(Long.toString(suinId))+"&key="+CommonUtil.encode(Long.toString(key));
					
					// 构造邮件信息
					StringBuilder txt = new StringBuilder();
					txt.append("<p>").append(userinfo.getSuinLoginName()).append("，你好！</p>")
					   .append("<p>"+localecUtil.get("user.congratulations.successful.registration")+"</p>")
					   .append("<p>"+localecUtil.get("user.click.activation")+"：</p>")
					   .append("<p>").append(emailUrl).append("</p>")
					   .append(localecUtil.get("user.click.not.clicking"))
					   .append("<p>"+localecUtil.get("user.lognore.this.mail")+"</p>")
					   .append("<p>"+ConstantsObj.FORMAT_DATE.format(new Date())+"</p>")
					   .append("<p>（"+localecUtil.get("user.please.do.not.reply")+"）</p>");
					
					// 发送邮件
					mailService.initParams(email, localecUtil.get("user.platform.user.activation"), txt.toString());
					mailService.sendMail();
					
					// 保存邮件激活信息
					SysEmailActivation sysEmailActivation = new SysEmailActivation();
					sysEmailActivation.setSeacSuinId(suinId);
					sysEmailActivation.setSeacKey(key);
					this.userPlatformService.save(sysEmailActivation);
					
					map.put("success", "true");
					map.put("result", localecUtil.get("user.confirmation.letter.mailbox")+"“"+email+"”,"+localecUtil.get("user.link.completion.registration"));
				}catch(Exception e) {
					e.printStackTrace();
					map.put("success", "true");
					map.put("result", localecUtil.get("user.mil.fail"));
					userinfo.setSuinAuditMode(Short.valueOf("1"));
				}
			}else{
				userinfo.setSuinAuditMode(Short.valueOf("1"));
				map.put("success", "true");
				map.put("result", localecUtil.get("user.success.examine"));
			}
			
			userPlatformService.save(userinfo);
		} catch (ErrorMessageException e){
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg", localecUtil.get("repeat.error")+"：" + e.getMessage());
		}
		String json = JSONObject.fromObject(map).toString();
		out.print(json);
		out.flush();
		out.close();
	}
	
	
	
	
	//获取图形验证码
	@RequestMapping(value="/reg/imgcode")
	public void getImgCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		try {
			String imgcode = request.getSession().getAttribute("REGIMGCODE").toString().toUpperCase();
			out.print(imgcode);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
		out.flush();
		out.close();
	}
	//获取短信验证码
	@RequestMapping(value="/reg/smscode")
	public void getSmsCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		try {
			String smscode = request.getSession().getAttribute("REGSMSCODE").toString();
			out.print(smscode);
		} catch (Exception e) {
			e.printStackTrace();
			out.print("");
		}
		out.flush();
		out.close();
	}
	
	//验证手机号码及发送验证短信
	@RequestMapping(value="/reg/sendsms/{mobileNum}")
	public void sendSmsCode	(@PathVariable String mobileNum, HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		//获取国际化
		LocalecUtil localecUtil=new LocalecUtil();
		try {
			
			if(userPlatformService.getUserinfoByLoginName(mobileNum) != null){
				throw new ErrorMessageException(localecUtil.get("user.phone.register"));
			}
			String code = (int)((Math.random()*9+1)*100000) + "";
			String sms = localecUtil.get("user.sms.title")+code+localecUtil.get("user.broblem");
			request.getSession().setAttribute("REGSMSCODE", code);
			SmsSend.sendsms(mobileNum, sms);
//			System.out.println("验证码：" + code);
			map.put("success", "true");
		} catch (ErrorMessageException e){
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg",localecUtil.get("repeat.error")+"：" + e.getMessage());
		}
		String json = JSONObject.fromObject(map).toString();
		out.print(json);
		out.flush();
		out.close();
	}
	
	
	//验证手机号码及发送验证短信
	@RequestMapping(value="/reg/mobile/save")
	public void mobileResSave(HttpServletRequest request, HttpServletResponse response) throws Exception {
		response.setContentType("text/plain;charset=utf-8");
		response.setHeader("Cache-Control", "no-cache");
		PrintWriter out = response.getWriter();
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		//获取国际化
		LocalecUtil localecUtil=new LocalecUtil();
		
		String mobileNum = ServletRequestUtils.getStringParameter(request, "telVal", "");
		String loginPwd = ServletRequestUtils.getStringParameter(request, "pwdval", "");
		String imgCode = ServletRequestUtils.getStringParameter(request, "imgCode", "").toUpperCase();
		String smsCode = ServletRequestUtils.getStringParameter(request, "smsCode", "");
		
		try {
			if(mobileNum.length() != 11){
				throw new ErrorMessageException(localecUtil.get("user.pnhoe.number.error"));
			}
			if(userPlatformService.getUserinfoByLoginName(mobileNum) != null){
				throw new ErrorMessageException(localecUtil.get("user.phone.repeat.register"));
			}
			String sysImgCode = request.getSession().getAttribute("REGIMGCODE").toString().toUpperCase();
			if(!imgCode.equals(sysImgCode)){
				throw new ErrorMessageException(localecUtil.get("user.graphical.code"));
			}
			HttpSession session = request.getSession();
			if(session.getAttribute("REGSMSCODE") == null || 
					!CommonUtil.isNotNull(session.getAttribute("REGSMSCODE").toString())){
				throw new ErrorMessageException(localecUtil.get("user.obtain.sms.code"));
			}
			
			String sysSmsCode = session.getAttribute("REGSMSCODE").toString();
			if(!smsCode.equals(sysSmsCode)){
				throw new ErrorMessageException(localecUtil.get("user.input.sms.code.error"));
			}
			
			SysUserinfo userinfo = new SysUserinfo();
			userinfo.setSuinLoginName(mobileNum);
			try {
				userinfo.setSuinPassword(PasswordAuthenticator.createPassword(loginPwd));
			}catch(Exception e) {
				throw new ErrorMessageException(localecUtil.get("user.passwd.error"));
			}
			userinfo.setSuinMobileTelephone(mobileNum);
			
			// 设置默认样式
			Long skinId = 2l;
			try {
				skinId = Long.parseLong(ConstantsObj.getSysProperty(MmSettingConstant.getRiss2DefaultSkin()));
			}catch(Exception ex) {
				skinId = 2l;
			}
			userinfo.setSuinName("-");
			userinfo.setSuinSstyId(skinId);
			userinfo.setSysUserType(userPlatformService.getDefaultSysUserType());
			userinfo.setSuinIsStop(false);
			userinfo.setSuinState(Short.valueOf("1")); //短信注册默认为审核通过
			userinfo.setSuinAuditMode(new Short("1"));
			userinfo.setSuinJoinDate(new Date());
			userinfo.setSuinLastvisitDate(new Date());
			userPlatformService.save(userinfo);
			
			session.setAttribute("sysUserinfo", userinfo);
			session.setAttribute("REGSMSCODE", "");
			map.put("success", "true");
		} catch (ErrorMessageException e){
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg", e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			map.put("success", "false");
			map.put("errmsg", localecUtil.get("repeat.error")+"：" + e.getMessage());
		}
		String json = JSONObject.fromObject(map).toString();
		out.print(json);
		out.flush();
		out.close();
	}
}



























