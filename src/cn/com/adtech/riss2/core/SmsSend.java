package cn.com.adtech.riss2.core;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class SmsSend {
	
	//短信接口地址
	final private static String url       = "http://api.qirui.com:7891/mt";
	
	//apiKey和APISecret
	final private static String apiKey    = "1064260010";
	final private static String apiSecret = "6aeb5694bb02097f690e";
	
	//短信模板格式
	/**
	private String SMS_JCXT_ERROR = "【联图科技】联图检测系统提示您：您的数据库平台“中国科学引文数据库(CSCD)”于2018-03-08 14:47检测时发现不能访问，请即时处理";
	private String SMS_REG_CODE = "【联图科技】您手机注册的验证码为：{1}，如有问题请拨打客服电话：028-87098318";
	**/
	
	public static void main(String[] args) throws Exception {
		String code = (int)((Math.random()*9+1)*1000) + "";
    }
	
	/**
	 * 短信发送
	 * @param mobile 接收短信手机号码，多个号码用“;”分割，如“18603079607;18080117780”
	 * @param smsMsg 发送短信内容，一条内容为67个字符，超过67个按两条扣费
	 * @throws Exception
	 */
	public static void sendsms(String mobile, String smsMsg) throws Exception{
        StringBuilder sb = new StringBuilder(2000);
        sb.append(url);
        sb.append("?dc=15");
        sb.append("&sm=").append(URLEncoder.encode(smsMsg, "utf8"));
        sb.append("&da=").append(mobile);
        sb.append("&un=").append(apiKey);
        sb.append("&pw=").append(apiSecret); 
        sb.append("&tf=3&rd=1&rf=2");   //短信内容编码为 urlencode+utf8
        
        String request = sb.toString();
		
		CloseableHttpClient client = HttpClients.createDefault();

    	HttpGet httpGet = new HttpGet(request);
		
		CloseableHttpResponse response = client.execute(httpGet);

		String respStr = null;
    
  		HttpEntity entity = response.getEntity();
        if(entity != null) {
              respStr = EntityUtils.toString(entity, "UTF-8");
        }
        System.out.println(respStr);
	}
}
