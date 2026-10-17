package cn.com.adtech.riss2.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * URL路径处理
 * @author MaChao
 *
 */
public class ParseUrl {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String aa = getHtml("http://192.168.1.104:9090/spms/login.jsp?loginname=root&loginpwd=root");
//		System.out.println(aa);
		
//		test();
		
		String url = "http://192.168.1.156:8008/htmlfile/松赞干布时期佛教传入吐蕃的原因/index.html";
		System.out.println(urlIsConnect(url));
	}
	
	public static void test(){
		String data = "id=10011&&username=王林&&studentNo=500223";
		String[] arr = data.split("&&");
		System.out.println(arr.length);
		for(int i=0; i<arr.length; i++){
			System.out.println(i + ": " + arr[i]);
		}
	}

	/**
	 * 将指定的url路径解析为源代码
	 * @param u:要解析的url路径
	 * @return 解析之后的源代码字符串
	 */
	final public static String getHtml(String u){
		if(u==null || u.length()<2){
			return null;
		}
		String line = "";
		URL url = null;
		InputStream is = null;
		BufferedReader br = null;
		StringBuffer buffer = new StringBuffer();
		try {
			url = new URL(u);
			is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is, "UTF-8");
			br = new BufferedReader(isr);
			while(true){
				line = br.readLine();
				if(line==null){
					break;
				}else{
					buffer.append(line+"\n");
				}
			}
		} catch (MalformedURLException e) {
			System.out.println("连接路径:"+u+"时发生异常:"+e.getMessage());
			line = "404";
		} catch (IOException e) {
			System.out.println("读取路径:"+u+"时发生异常:"+e.getMessage());
			line = "500";
		}
		br = null;
		is = null;
		url = null;
		line = buffer.toString();
		buffer = null;
		return line;
	}
	
	public static synchronized boolean urlIsConnect(String urlStr) {  
		if (urlStr == null || urlStr.length() <= 0) {
			return false;
		}
		urlStr = setUrlForChn(urlStr); //地址中的中文进行转码
		int counts = 0;
		URL url = null;
		HttpURLConnection con = null;
		int state = 0;
		boolean b = false;
		while (counts < 5) {
			counts++;
			try {
				url = new URL(urlStr);
				con = (HttpURLConnection) url.openConnection();
				state = con.getResponseCode();
				if (state == 200) {
					b = true;
					break;
				}
			} catch (Exception ex) {
				System.out.println("URL不可用，连接第 " + counts + " 次");
				urlStr = null;
				continue;
			}
		}
		return b;
	}  
	
	/**
     * 对含有中文的字符串进行Unicode编码
     * \ue400 \u9fa5 Unicode表中的汉字的头和尾
     */
    public static String setUrlForChn(String url){
        String regEx = "[\u4e00-\u9fa5]";
        Pattern p = Pattern.compile(regEx);//p的值为[一-龥]
        Matcher m = p.matcher(url);
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            try {
				m.appendReplacement(sb, URLEncoder.encode(m.group(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
