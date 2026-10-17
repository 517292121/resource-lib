package cn.com.adtech.riss2.mail;

import java.io.IOException;

import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.MmSettingConstant;



/**
 * 邮件服务
 * @author MaChao
 * @date 2010-12-20
 */
public class MailServiceImpl implements IMailService{

	String to = ""; // 收件人

	String from = ""; // 发件人

	String host = ""; // smtp主机

	String username = "";

	String password = "";

//	String filename = ""; // 附件文件名

	String subject = ""; // 邮件主题

	String content = ""; // 邮件正文
	
	
	/**
	 * 初始化参数
	 */
	public void initParams(String to,String subject,String content){
//		this.host = ConstantsObj.getSysProperty(MmSettingConstant.getSendMailHost());
//		this.from = ConstantsObj.getSysProperty(MmSettingConstant.getSendMailFromAddress());
//		this.username = ConstantsObj.getSysProperty(MmSettingConstant.getSendMailFromUsername());
//		this.password = ConstantsObj.getSysProperty(MmSettingConstant.getSendMailFromPassword());
		
		this.host = "smtp.sina.com";
		this.from = "ltkjserver@sina.com";
		this.username = "ltkjserver";
		this.password = "ltkj87098318";
		this.to = to;
		this.subject = subject;
		this.content = content;
	}

	/**
	 * 发送文本邮件
	 * @throws IOException 
	 */
	public void sendMail() throws IOException{
		SendMail sendEmail = new SendMail();
		sendEmail.setHost(host);
		sendEmail.setUserName(username);
		sendEmail.setPassWord(password);
		sendEmail.setFrom(from);
		sendEmail.setTo(to);
		sendEmail.setSubject(subject);
		sendEmail.setContent(content);
		sendEmail.run();
	}
}
