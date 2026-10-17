package cn.com.adtech.riss2.mail;




/**
 * 邮件服务接口
 * @author MaChao
 * @date 2010-12-20
 */
public interface IMailService {

	/**
	 * 初始化参数配置
	 */
	public void initParams(String to,String subject,String content);
	/**
	 * 发送文本邮件
	 * @throws Exception 
	 */
	public void sendMail() throws Exception;
}
