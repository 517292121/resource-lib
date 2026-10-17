package cn.com.adtech.riss2.mail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.mail.util.ByteArrayDataSource;

import com.sun.mail.util.LineInputStream;

import cn.com.adtech.riss2.exception.ErrorMessageException;

/*
 * 用JAVA程序发邮件 1. 下载，jaf-1_1-fr.zip和javamail-1_4.zip 解压缩会生成：activation.jar和mail.jar 在www.zhoujianboy.ys168.com或者www.sun.com可以下载 2.
 * 打开Jcreator->Configure->Options->JDK Profiles->在右边选择JDK，然后->edit ->Add->Add Archive..->分别添加：activation.jar和mail.jar二个包。 3.
 * 现在你可以正常使用javax.activation和javax.mail了。 Title: 使用javamail发送邮件 Description: 演示如何使用javamail包发送电子邮件。这个实例可发送多附件
 */
public class SendMail extends Thread{
	String to = ""; // 收件人

	String from = ""; // 发件人

	String host = ""; // smtp主机

	String username = "";

	String password = "";

	String filename = ""; // 附件文件名

	String subject = ""; // 邮件主题

	String content = ""; // 邮件正文

	Vector file = new Vector(); // 附件文件集合

	/**
	 * <br/> 方法说明：默认构造器 <br/> 输入参数： <br/> 返回类型：
	 */
	public SendMail() {}

	/**
	 * <br/> 方法说明：构造器，提供直接的参数传入 <br/> 输入参数： <br/> 返回类型：
	 */
	public SendMail(String to, String from, String smtpServer, String username, String password, String subject, String content) {
		this.to = to;
		this.from = from;
		this.host = smtpServer;
		this.username = username;
		this.password = password;
		this.subject = subject;
		this.content = content;
	}

	/**
	 * <br/> 方法说明：设置邮件服务器地址 <br/> 输入参数：String host 邮件服务器地址名称 <br/> 返回类型：
	 */
	public void setHost(String host) {
		this.host = host;
	}

	/**
	 * <br/> 方法说明：设置登录服务器校验密码 <br/> 输入参数： <br/> 返回类型：
	 */
	public void setPassWord(String pwd) {
		this.password = pwd;
	}

	/**
	 * <br/> 方法说明：设置登录服务器校验用户 <br/> 输入参数： <br/> 返回类型：
	 */
	public void setUserName(String usn) {
		this.username = usn;
	}

	/**
	 * <br/> 方法说明：设置邮件发送目的邮箱 <br/> 输入参数： <br/> 返回类型：
	 */
	public void setTo(String to) {
		this.to = to;
	}

	/**
	 * <br/> 方法说明：设置邮件发送源邮箱 <br/> 输入参数： <br/> 返回类型：
	 */
	public void setFrom(String from) {
		this.from = from;
	}

	/**
	 * <br/> 方法说明：设置邮件主题 <br/> 输入参数： <br/> 返回类型：
	 */
	public void setSubject(String subject) {
		this.subject = subject;
	}

	/**
	 * <br/> 方法说明：设置邮件内容 <br/> 输入参数： <br/> 返回类型：
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * <br/> 方法说明：把主题转换为中文 <br/> 输入参数：String strText <br/> 返回类型：
	 */
	public String transferChinese(String strText) {
		try {
			strText = MimeUtility.encodeText(new String(strText.getBytes(), "GB2312"), "GB2312", "B");
		}catch(Exception e) {
			e.printStackTrace();
		}
		return strText;
	}

	/**
	 * <br/> 方法说明：往附件组合中添加附件 <br/> 输入参数： <br/> 返回类型：
	 */
	public void attachfile(String fname) {
		file.addElement(fname);
	}

	/**
	 * <br/> 方法说明：发送邮件 <br/> 输入参数： <br/> 返回类型：boolean 成功为true，反之为false
	 * 
	 * @throws IOException
	 */
	
	public void run(){

		// 构造mail session
		Properties props = System.getProperties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "25");
		Session session = Session.getDefaultInstance(props, new Authenticator(){
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});
		try {
			// 构造MimeMessage 并设定基本的值
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(from));
//			System.out.println(from);
			InternetAddress[] address = {new InternetAddress(to)};
			msg.setRecipients(Message.RecipientType.TO, address);
			subject = transferChinese(subject);
			msg.setSubject(subject);
			// 构造Multipart
			Multipart mp = new MimeMultipart();
			// 向Multipart添加正文
			MimeBodyPart mbpContent = new MimeBodyPart();
			mbpContent.setText(content);
			// 向MimeMessage添加（Multipart代表正文）
			mp.addBodyPart(mbpContent);
			// 向Multipart添加附件
			/*
			 * Enumeration efile = file.elements(); while (efile.hasMoreElements()) { MimeBodyPart mbpFile = new MimeBodyPart(); filename =
			 * efile.nextElement().toString(); FileDataSource fds = new FileDataSource(filename); mbpFile.setDataHandler(new
			 * DataHandler(fds)); mbpFile.setFileName(fds.getName()); //向MimeMessage添加（Multipart代表附件） mp.addBodyPart(mbpFile); }
			 * file.removeAllElements();
			 */
			// 向Multipart添加MimeMessage
			msg.setContent(mp);
			msg.setSentDate(new Date());
			// 发送HTML
			collect(msg);
			// 发送邮件
			Transport.send(msg);
		}catch(Exception mex) {
			mex.printStackTrace();
			throw new ErrorMessageException("邮件发送失败。请与管理员联系！");
		}
    }

	public void collect(Message msg) throws MessagingException, IOException {
		String subject = msg.getSubject();
		StringBuffer sb = new StringBuffer();
		sb.append("<HTML>\n");
		sb.append("<HEAD>\n");
		sb.append("<TITLE>\n");
		sb.append(subject + "\n");
		sb.append("</TITLE>\n");
		sb.append("</HEAD>\n");
		sb.append("<BODY>\n");
		sb.append(this.content + "\n\n");
		sb.append("</BODY>\n");
		sb.append("</HTML>\n");
		msg.setDataHandler(new DataHandler(new ByteArrayDataSource(sb.toString(), "text/html")));
	}

	/**
	 * <br/> 方法说明：主方法，用于测试 <br/> 输入参数： <br/> 返回类型：
	 * 
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		StringBuilder txt = new StringBuilder();
		txt.append("<p>亲爱的：zdtwyjp@126.com： </p>")
		   .append("<p>欢迎使用新浪会员找回密码功能。请点击以下链接重置您的密码（链接48小时内有效） ：</p>")
		   .append("<p>http://login.sina.com.cn/member/getpwd/getpwd3.php?r=cd02a4ee9d981df7c2946b487753c2df</p>")
		   .append("<p>如果您没有申请找回密码，请忽略此邮件。</p>")
		   .append("<p>RISS2自检资源平台</p>")
		   .append("<p>2010-12-3</p>")
		   .append("<p>（本邮件由系统自动发出，请勿回复。）</p>");
		
		
		SendMail sendmail = new SendMail();
		
//		sendmail.setHost("mail.adtech.com.cn");
//		sendmail.setUserName("lujiangnan");
//		sendmail.setPassWord("lujiangnan");
//		sendmail.setFrom("lujiangnan@adtech.com.cn");
		
//		sendmail.setHost("smtp.126.com");
//		sendmail.setUserName("wangying8981@126.com");
//		sendmail.setPassWord("1happy_888");
//		sendmail.setFrom("wangying8981@126.com");
		
		sendmail.setHost("smtp.sina.com");
		sendmail.setUserName("ltkjserver@sina.com");
		sendmail.setPassWord("ltkj87098318");
		sendmail.setFrom("ltkjserver@sina.com");
		
		sendmail.setTo("286239880@qq.com");
		sendmail.setSubject("邮件发送测试");
		sendmail.setContent(txt.toString());
		sendmail.run();
	}
} // end
