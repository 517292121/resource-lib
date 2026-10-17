package cn.com.adtech.riss2.filter;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import cn.com.adtech.riss2.core.CommonUtil;

/**
 * 
 * 对平台中所有URL参数进行特殊字符过滤，防止XSS攻击
 * 
 * 该工具类的处理会将所有script、src类型的注入脚本直接转换为空，如果需要转换为其它内容可自行修改replaceXSS方法
 *
 * @author MaChao
 *
 * @date：Jun 7, 2018
 */
public class XsslHttpServletRequestWrapper extends HttpServletRequestWrapper {

	HttpServletRequest xssRequest = null;

	public XsslHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		xssRequest = request;
	}

	@Override
	public String getParameter(String name) {
		String value = super.getParameter(replaceXSS(name));
		if (value != null) {
			value = replaceXSS(value);
		}
		return value;
	}

	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(replaceXSS(name));
		if (values != null && values.length > 0) {
			for (int i = 0; i < values.length; i++) {
				values[i] = replaceXSS(values[i]);
			}
		}
		return values;
	}

	@Override
	public String getHeader(String name) {

		String value = super.getHeader(replaceXSS(name));
		if (value != null) {
			value = replaceXSS(value);
		}
		return value;
	}

	/** 
	 * 去除待带script、src的语句，转义替换后的value值 
	 */
	public static String replaceXSS(String value) {
		if(!CommonUtil.isNotNull(value)){
			return "";
		}
		try {
			value = value.replace("+", "%2B"); // '+' replace to '%2B'
			value = URLDecoder.decode(value, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		// Avoid null characters
		value = value.replaceAll("\0", "");

		// Avoid anything between script tags  
		Pattern scriptPattern = Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE);
		value = scriptPattern.matcher(value).replaceAll("");

		// Avoid anything in a src='...' type of e­xpression  
		scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE	| Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");

		scriptPattern = Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE	| Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");

		// Remove any lonesome </script> tag  
		scriptPattern = Pattern.compile("</script>",Pattern.CASE_INSENSITIVE);
		value = scriptPattern.matcher(value).replaceAll("");

		// Remove any lonesome <script ...> tag  
		scriptPattern = Pattern.compile("<script(.*?)>",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE| Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");

		// Avoid eval(...) e­xpressions  
		scriptPattern = Pattern.compile("eval\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");

		// Avoid e­xpression(...) e­xpressions  
		scriptPattern = Pattern.compile("e­xpression\\((.*?)\\)",Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");

		// Avoid javascript:... e­xpressions  
		scriptPattern = Pattern.compile("javascript:",Pattern.CASE_INSENSITIVE);
		value = scriptPattern.matcher(value).replaceAll("");
		// Avoid alert:... e­xpressions  
		scriptPattern = Pattern.compile("alert", Pattern.CASE_INSENSITIVE);
		value = scriptPattern.matcher(value).replaceAll("");
		// Avoid onload= e­xpressions  
		scriptPattern = Pattern.compile("onload(.*?)=",	Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);
		value = scriptPattern.matcher(value).replaceAll("");
		scriptPattern = Pattern.compile("vbscript[\r\n| | ]*:[\r\n| | ]*",Pattern.CASE_INSENSITIVE);
		value = scriptPattern.matcher(value).replaceAll("");
		
		//将特殊字符进行转义
		value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\"", "&quot;").replaceAll("&", "&amp;");
		
		return value;
	}
}
