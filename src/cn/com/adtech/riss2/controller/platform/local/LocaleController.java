package cn.com.adtech.riss2.controller.platform.local;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.mvc.Controller;

import cn.com.adtech.riss2.cache.PlatformCacheFactory;

/**
 * 实现国际化功能
 * 
 * @author MaChao
 * @date 2010-12-13
 */
public class LocaleController implements Controller {
	
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String language = ServletRequestUtils.getStringParameter(request, "locale", "zh_CN");
		
		String from = ServletRequestUtils.getStringParameter(request, "from",request.getContextPath() + "/index.htm");
		Locale locale = null;
		if(language == null || language.equals("zh_CN")) {// 中文
			locale = new Locale("zh", "CN");
		}else if(language.equals("en_US")) {// 英文
			locale = new Locale("en", "US");
		}else if(language.equals("zw_XZ")) { // 藏文
			locale = new Locale("zw", "XZ");
		}else {
			locale = new Locale("zh", "CN");  
		}
		request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME, locale);
		language = locale.getLanguage();
		request.getSession().setAttribute("language", language);
		response.sendRedirect(code_url(from));
		
		PlatformCacheFactory.instance().setCurrLan(language);
		return null;
	}
	
	/**
	 * 将url进行编码（针对整个url进行编码）
	 * @param url	需要编码的url
	 * @throws Exception 
	 */
	public static String code_url(String url) throws Exception{
		if(url == null || "".equals(url) || url.trim().length() < 1 || !url.contains("?") || (!url.contains("&") && !url.contains("splicing;"))){
			return filterChinese(url);
		}
		//url的请求路径
		String url_path = filterChinese(url.substring(0,url.indexOf("?")+1));
		//url的请求参数
		String url_params = url.substring(url.indexOf("?")+1,url.length());
		
		url_params = url_params.replaceAll("splicing;", "&");
		//将其携带的请求参数截取为数组
		String[] url_array = url_params.split("&");
		if(url_array !=null && url_array.length < 1){
			return url;
		}

		//编码后的url
		StringBuilder str = new StringBuilder(url_path);
		String params = "";
		String data = "";
		for(int i = 0;i < url_array.length;i++){
			params = url_array[i];
			data = params.substring(params.indexOf("=")+1, params.length());
			//解码
			data = URLDecoder.decode(data,"UTF-8");
			//编码
			data = URLEncoder.encode(data,"UTF-8");
			if(i > 0){
				str.append("&");
			}
			str.append(params.substring(0, params.indexOf("=")+1)).append(data);
		}
		url = str.toString();
		
		return url;
	}
	
	
	/**
	 * 过滤掉中文（针对请求路径中的中文进行编码）
	 * 
	 * @param str
	 *            待过滤中文的字符串
	 * @return 过滤掉中文后字符串
	 */
	public static String filterChinese(String str) {
		// 用于返回结果
		String result = str;
		boolean flag = isContainChinese(str);
		if (flag) {// 包含中文
			// 用于拼接过滤中文后的字符
			StringBuffer sb = new StringBuffer();
			// 用于校验是否为中文
			boolean flag2 = false;
			// 用于临时存储单字符
			char chinese = 0;
			// 5.去除掉文件名中的中文
			// 将字符串转换成char[]
			char[] charArray = str.toCharArray();
			// 过滤到中文及中文字符
			for (int i = 0; i < charArray.length; i++) {
				chinese = charArray[i];
				flag2 = isChinese(chinese);
				if (!flag2) {// 不是中日韩文字及标点符号
					sb.append(chinese);
					continue;
				}
				try {
					sb.append(URLEncoder.encode(chinese + "", "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			result = sb.toString();
		}
		return result;
	}

	/**
	 * 判断字符串中是否包含中文
	 * 
	 * @param str
	 *            待校验字符串
	 * @return 是否为中文
	 * @warn 不能校验是否为中文标点符号
	 */
	public static boolean isContainChinese(String str) {
		Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
		Matcher m = p.matcher(str);
		if (m.find()) {
			return true;
		}
		return false;
	}

	/**
	 * 判定输入的是否是汉字
	 * 
	 * @param c
	 *            被校验的字符
	 * @return true代表是汉字
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}
	
	
	public static void main(String[] args) throws Exception {
		String url = "http://192.168.1.168/RISS-ZW//res/type/2";
		System.out.println(url.substring(0,url.indexOf("?")+1));
	}
	
}
