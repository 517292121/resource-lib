package cn.com.adtech.riss2.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

public class CommonUtil {
	private static final Logger logger = Logger.getLogger(CommonUtil.class);
	
	final private static List<String> CHARLIST = new ArrayList<String>();
	static{
		CHARLIST.add("z");
		CHARLIST.add("K");
		CHARLIST.add("U");
		CHARLIST.add("O");
		CHARLIST.add("J");
		CHARLIST.add("M");
		CHARLIST.add("w");
		CHARLIST.add("A");
		CHARLIST.add("s");
		CHARLIST.add("E");
	}

	public static boolean isNotNull(String value) {
		return value != null && !"".equals(value) && !"null".equals(value.toLowerCase());
	}
	
	public static String nullToBlank(Object o){
		return null == o ? "" : o.toString();
	}

	/**
	 * 在下拉选项框中添加自定义选项
	 * 
	 * @param jsondata
	 *            json格式字符串
	 * @return
	 * @author MaChao
	 * @date 2010-9-10
	 */
	public static String createStoreSelectHasNull(String jsondata, String value, String text) {
		String result = "[['" + value + "','" + text + "']";
		if(jsondata.length() > 2){
			result += ",";
			result += jsondata.substring(1);
		}
		return result;
	}

	/**
	 * 创建TABLE JSON报文
	 * 
	 * @author MaChao
	 * @date 2009-7-15
	 * @version 1.0
	 * @param list
	 *            List 重构对象以后的参数对象列表
	 * @param strings
	 *            String[] 需要构造的JSON报文KEY
	 * @param total
	 *            String 页面总条数，可以为空
	 * @return String 构造好的JSON报文字符串
	 */
	public static String createJsonTable(List list, String[] keys, String totalPages) {
		try {
			if(keys == null || keys.length == 0) {
				throw new ErrorMessageException("需要构造的JSON报文KEY为空！");
			}
			List<Map> dealList = new ArrayList<Map>();
			for(int i = 0, size = list.size(); i < size; i++) {
				Object[] row = (Object[])list.get(i);
				if(row.length != keys.length) {
					throw new ErrorMessageException("数据对象中的列数量与需要构造的JSON报文KEY数量不相等！");
				}
				Map<String, Object> obj2Map = new HashMap<String, Object>();
				for(int j = 0; j < row.length; j++) {
					if(row[j] instanceof Date) {
						Calendar calendar = Calendar.getInstance();
						calendar.setTime((Date)row[j]);
						if(calendar.get(Calendar.HOUR_OF_DAY) == 0 && calendar.get(Calendar.MINUTE) == 0
								&& calendar.get(Calendar.SECOND) == 0) {
							row[j] = ConstantsObj.FORMAT_DATE.format(row[j]);
						}else {
							row[j] = ConstantsObj.FORMAT_DATE_TIME.format(row[j]);
						}
					}
					obj2Map.put(keys[j], row[j]);
				}
				dealList.add(obj2Map);
			}
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("totalProperty", totalPages);
			result.put("root", dealList);
			String json = JSONObject.fromObject(result).toString();
			if(logger.isInfoEnabled()) {
				logger.info(json);
			}
			return json;
		}catch(Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * josn - > Object
	 * @param jsonString
	 * @param clazz
	 * @return
	 * @author MaChao
	 * @date 2010-9-28
	 */
	public static Object jsonToObject(String jsonString, Class clazz) {
		JSONObject jsonObject = null;
		try {
			jsonObject = JSONObject.fromObject(jsonString);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return JSONObject.toBean(jsonObject, clazz);
	}
	
	public static String createOptionsList(List list) {
		if(list.size()==0)return "[]";
		StringBuilder result = new StringBuilder();
		result.append("[");
		for(int i = 0; i < list.size(); i++) {
			Object[] obj = (Object[])list.get(i);
			result.append("['").append(obj[0]).append("','").append(obj[1]).append("']");
			if(i != list.size() - 1)
				result.append(",");
		}
		result.append("]");
		return result.toString();
	}
	
	// 处理TextArea的换行符
	public static String hangleTextArea(String in) {
		StringBuffer out = new StringBuffer();
		for(int i = 0; in != null && i < in.length(); i++) {
			char c = in.charAt(i);
			if(c == '\'')
				out.append("&#039;");
			else if(c == '\"')
				out.append("&#034;");
			else if(c == '<')
				out.append("&lt;");
			else if(c == '>')
				out.append("&gt;");
			else if(c == '&')
				out.append("&amp;");
			else if(c == ' ')
				out.append("&nbsp;");
			else if(c == '\n')
				out.append("<br/>");
			else
				out.append(c);
		}
		return out.toString();
	}
	
	/**
	 * 获取绑定对象，主要是为了实现国际化
	 * 
	 * @param basename 资源文件名字
	 * @param request
	 * @return 
	 */
	public static ResourceBundle getResourceBundle(String basename,HttpServletRequest request){
		Object val = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		Locale locale = null;
		if(val != null){
			locale = (Locale)val;
		}else{
			locale = new Locale("zh", "CN");  
		}
		ResourceBundle rb = ResourceBundle.getBundle(basename,locale);
		return rb;
	}

    public static String subString(String text, int len){
        if(isNotNull(text) == false){
            return "";
        }
        if(text.length() < len){
            return text;
        }
        return text.substring(0, len);
    }
    
    private static int getUtf8Len(String str){
    	try {
			int len = str.getBytes("UTF-8").length;
			return len;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return str.length();
		}
    	
    } 
    
    public static String subUtf8Str(String str, int saveLen){
		if(str == null){
			return "";
		}
		int len = getUtf8Len(str);
		if(len < saveLen){
			return str;
		}
		
		while(len > saveLen){
			str = str.substring(0, str.length()-1);
			len = getUtf8Len(str);
		}
		return str;
	}
    
    final public static String getProperties(String file_path, String key) {
		Properties prop = new Properties();
		try {
			InputStream input = null;
			if ("/config.properties".equals(file_path) || "config.properties".equals(file_path)) {
				Path[] externalConfigPaths = new Path[] {
						Paths.get("MCProject", "config", "config.properties"),
						Paths.get("config", "config.properties")
				};
				for (Path configPath : externalConfigPaths) {
					if (Files.isRegularFile(configPath)) {
						input = Files.newInputStream(configPath);
						break;
					}
				}
			}
			if (input == null) {
				input = ToolUnit.class.getResourceAsStream(file_path);
			}
			if (input == null) {
				logger.error("配置文件不存在: " + file_path);
				return "";
			}
			try (InputStream is = input; InputStreamReader isr = new InputStreamReader(is, "UTF-8")) {
				prop.load(isr);
				return prop.getProperty(key);
			}
		}catch(IOException e) {
			logger.error("读取配置失败: " + file_path, e);
			return "";
		}
	}
    
    /**
	 * 编码
	 * 
	 * @param str
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encode(String str) throws UnsupportedEncodingException {
		if(str == null)
			return "";
		Base64 base64 = new Base64();
		str = java.net.URLEncoder.encode(str, "utf-8");
		return new String(base64.encode(str.getBytes()));
	}

	/**
	 * 解码
	 * 
	 * @param str
	 * @return
	 * @throws IOException
	 */
	public static String decode(String str) throws IOException {
		if(str == null)
			return "";
		Base64 base64 = new Base64();
		str = new String(base64.decode(str.getBytes()));
		return java.net.URLDecoder.decode(str, "utf-8");
	}
	

	/**
	 * 编译地址
	 * @param localpath
	 * @param viewFileName
	 * @return
	 * @throws Exception
	 */
	public static String getUrl(String localpath, String viewFileName) throws Exception {
		return "/FileDownload?downloadPath=" + encode(localpath) + "&fileName="	+ encode(viewFileName);
	}
	
	public static String parseUrlName(String codeUrl){
		String pVal = UrlParser.getUrlParameter(codeUrl, "downloadPath");
		if(!CommonUtil.isNotNull(pVal)){
			return "";
		}
		String localname = "";
		try {
			localname = decode(pVal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localname;
	}
	
	/**
	 * 将数字ID转换为字符串
	 * @param idStr
	 * @return
	 */
	public static String idToString(String idStr){
		if(!CommonUtil.isNotNull(idStr)){
			return "";
		}
		char[] charArr = idStr.toCharArray();
		StringBuffer sb = new StringBuffer();
		for(char c : charArr){
			if(Character.isDigit(c)){
				sb.append(CHARLIST.get(Integer.valueOf(String.valueOf(c))));
			}else{
				sb.append(c);
			}
		}
		String str = sb.toString();
		sb = null;
		return str;
	}
	
	/**
	 * 将字符串ID转换为long
	 * @param idStr
	 * @return
	 */
	public static Long strToId(String idStr){
		if(!CommonUtil.isNotNull(idStr)){
			return null;
		}
		try {
			char[] charArr = idStr.toCharArray();
			StringBuffer sb = new StringBuffer();
			String s = null;
			for(char c : charArr){
				s = String.valueOf(c);
				if(CHARLIST.contains(s)){
					sb.append(CHARLIST.indexOf(s));
				}else{
					sb.append(s);
				}
			}
			String str = sb.toString();
			sb = null;
			return Long.parseLong(str);			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 批量转换数据列表ID为字符类型，ID必须为对一个对象
	 * @param dataList
	 */
	public static void parserId(List<Object[]> dataList){
		if(dataList == null || dataList.size() < 1){
			return;
		}
		for(Object[] data : dataList){
			data[0] = CommonUtil.idToString(data[0].toString());
		}
	}
	
	/**
	* 验证是否是手机访问
	* @param request
	* @return
	*/
	public static boolean judgeIsPhone(HttpServletRequest request) {
		String User_Agent = request.getHeader("User-Agent");
		if (User_Agent.contains("Android") || User_Agent.contains("iPhone")) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前语种
	 * 
	 * @author MaChao
	 * @date 2018-11-6
	 * @return
	 */
	public static String getLanguage(HttpServletRequest request) {
		// 获取当前系统使用的语言
		Locale locale = LocaleContextHolder.getLocale();
		String language = "zh";	//默认语种

		if (locale == null) {
			return language;
		}
		language = locale.toString();

		if ("zh".equals(language) || "zh_CN".equals(language)) {
			return "zh";
		}

		if ("zw_XZ".equals(language) || "zw".equals(language)) {
			return "zw";
		}

		if ("en_US".equals(language) || "en".equals(language)) {
			return "en";
		}

		return language;
	}

	public static void main(String[] ars) throws IOException{
		String ss = encode("D:/ResourceFile/myy/resource/myytsqwsjklib/20181123_1005/my00066/my00066.pdf");
		System.out.println(ss);
		String dd = decode(ss);
		System.out.println(dd);
		
		String s = "RCUzQSU1Q1Jlc291cmNlRmlsZSU1Q215eSU1Q3Jlc291cmNlJTVDbXl5dHNxd3Nqa2xpYiU1QzIwMTgxMTIzXzEwMDUlNUNteTAwMDY2JTVDbXkwMDA2Ni5wZGY";
		System.out.println(decode(s));
	}
}
