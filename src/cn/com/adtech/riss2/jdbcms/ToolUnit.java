package cn.com.adtech.riss2.jdbcms;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ServiceUtil;

@SuppressWarnings("unchecked")
public class ToolUnit {
	public ToolUnit() {}

	/**
	 * 设置小数保留位数 如：numberFormat(2.3455, 2) return 2.35
	 */
	final public static String numberFormat(double data, int num) {
		NumberFormat myformat = NumberFormat.getNumberInstance();
		myformat.setMaximumFractionDigits(num);
		return myformat.format(data);
	}

//	final public static String getProperties(String file_path, String key) {
//		Properties prop = new Properties();
//		try {
//			prop.load(ToolUnit.class.getResourceAsStream(file_path));
//			return prop.getProperty(key);
//		}catch(IOException e) {
//			e.printStackTrace();
//			return "";
//		}
//	}

	final public static String InterceptStr(String str, int num) {
		if(str == null || str.length() < 1) {
			return "";
		}
		if(str.length() <= num) {
			return str;
		}
		str = str.substring(0, num);
		str += "...";
		return str;
	}

	final public static String ConvertHtmlToString(String content) {
		if(content == null || content.length() < 1) {
			return "";
		}
		content = content.replaceAll("<", "&lt;").replace(">", "&gt;");
		return content;
	}

	/**
	 * 将集合中的值转换成以','分割的字符串
	 * 
	 * @return
	 */
	final public static String ConvertListToString(List list) {
		if(null == list || list.size() < 1) {
			return "";
		}
		String result = "";
		for(int i = 0, s = list.size(); i < s; i++) {
			result += list.get(i).toString() + ",";
		}
		result = result.substring(0, result.length() - 1);
		return result;
	}

	final public static Date StringToDate(String datestr, String format) {
		String f = "yyyy-MM-dd HH:mm:ss";
		if(null != format && format.length() > 8) {
			f = format;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(f);
		try {
			Date date = sdf.parse(datestr);
			return date;
		}catch(ParseException e) {
			System.out.println("日期转换异常：" + e.getMessage());
			return null;
		}finally {
			sdf = null;
		}
	}

	final public static String NullToBlank(String str) {
		if(null == str) {
			return "";
		}
		return str;
	}

	final public static long NullTo0(Long data) {
		if(data!=null) {
			return data;
		}else {
			return 0;
		}
		
	}

	/**
	 * 分割出文件的名称
	 * 
	 * @param filepath
	 * @return
	 */
	final public static String getFileName(String filepath) {
		if(filepath == null || filepath.length() < 1) {
			return "";
		}
		File file = new File(filepath);
		filepath = file.getName();
		file = null;
		return filepath;
	}

	final public static long StrToLong(String data, long defaultData) {
		try {
			long a = Long.parseLong(data);
			return a;
		}catch(Exception e) {
			return defaultData;
		}
	}

	/**
	 * 转义json中的特殊字符
	 * 
	 * @param s
	 * @return
	 */
	final public static String stringToJson(String s) {
		if(s == null || s.length() < 1){
			return "";
		}
		String retv = s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\'", "\\\'").
			replace("/", "\\/").replace("\b", "\\b").replace("\f", "\\f").
			replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
//		StringBuffer sb = new StringBuffer();
//		for(int i = 0; i < s.length(); i++) {
//			char c = s.charAt(i);
//			switch(c) {
//			case '\"':
//				sb.append("\\\"");
//				break;
//			case '\\':
//				sb.append("\\\\");
//				break;
//			case '/':
//				sb.append("\\/");
//				break;
//			case '\b':
//				sb.append("\\b");
//				break;
//			case '\f':
//				sb.append("\\f");
//				break;
//			case '\n':
//				sb.append("\\n");
//				break;
//			case '\r':
//				sb.append("\\r");
//				break;
//			case '\t':
//				sb.append("\\t");
//				break;
//			default:
//				sb.append(c);
//			}
//		}
//		return sb.toString();
		return retv;
	}
	
	final public static String stringToSQL(String s) {
//		if(s == null || s.length() < 1) {
//			return "";
//		}
//		StringBuffer sb = new StringBuffer();
//		for(int i = 0; i < s.length(); i++) {
//			char c = s.charAt(i);
//			switch(c) {
//			case '\"':
//				sb.append("\\\"");
//				break;
//			case '\\':
//				sb.append("\\\\");
//				break;
//			case '/':
//				sb.append("\\/");
//				break;
//			default:
//				sb.append(c);
//			}
//		}
		return s;
	}
	
	final public static String jsonToString(String s) {
		s = s.replace("\\\"", "\"").replace("\\\\", "\\").replace("\\/", "/")
				.replace("\\b", "\b").replace("\\f", "\f").replace("\\r", "\r")
				.replace("\\t", "\t");
		return s;
	}
	
	final public static String stringToJsondata(String s) {
		if(s == null || s.length() < 1) {
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			switch(c) {
			case '\b':
				sb.append("");
				break;
			case '\f':
				sb.append("");
				break;
			case '\n':
				sb.append("");
				break;
			case '\r':
				sb.append("");
				break;
			case '\t':
				sb.append("");
				break;
			default:
				sb.append(c);
			}
		}
		return sb.toString();
	}

	final public static boolean checkDateTime(String date) {
		if(date == null || date.length() < 2) {
			return false;
		}
		String a = date.substring(date.length() - 2, date.length());
		if(a.equals(".0")) {
			return true;
		}else {
			return false;
		}
	}

	final public static String nullToDefaultStr(String data, String defaultStr) {
		if(null == data) {
			return defaultStr;
		}else {
			return data;
		}
	}
	
	final public static String objectToStr(Object o, String defaultStr){
		if(o == null){
			return defaultStr;
		}
		try {
			return o.toString();
		} catch (Exception e) {
			return defaultStr;
		}
	}
	/**
	 * 过滤SQL注入
	 * @param Str_SQL
	 * @return
	 */
	final public static String sqlFilter(String Str_SQL){
		String str = "~\"~'~and~exec~insert~select~delete~update~count~*~chr~mid~master~truncate~char~declare";
		String[] str_rep = str.split("~");
		for(String one:str_rep){
			Str_SQL= Str_SQL.replace(one, "");
		}
		return Str_SQL;
	} 
	
	/**
	 * 获取GBK格式数据库下,输入中英文混合字符串的真实长度(中文字符占2位)
	 * 
	 * @author MaChao
	 * @date 2008-8-13
	 */
	public static int getStringRealLengthByGbk(String str) {
		int result = 0;
		if(ServiceUtil.isNotNull(str)) {
			int blen = 0;
			try {
				blen = str.getBytes("GBK").length;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} // 中文占2位时的总长度
			int len = str.length(); // 中文占1位时的总长度
			if(blen > len) {
				result = len + (blen - len); // (blen - len)即为中文字符数
			}else {
				result = len;
			}
		}
		return result;
	}
	
	/**
	 * 根据gbk编码截取字符串,即一可汉字按两个长度计算
	 * @param str
	 * @param len
	 * @return
	 */
	public static String subStringByGbk(String str, int len){
		if(ServiceUtil.isNotNull(str)){
			while(getStringRealLengthByGbk(str) > len) {
				str = str.substring(0, str.length() - 1);
			}
		}
		return str;
	}
	
	public static String subString(String str, int len){
		if(CommonUtil.isNotNull(str) == false){
			return "";
		}
		if(str.length() <= len){
			return str;
		}
		return str.substring(0, len) + "...";
	}
}
