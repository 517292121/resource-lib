package cn.com.adtech.riss2.core;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.domain.SysManager;
import cn.com.adtech.riss2.domain.SysUserinfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.jdbcms.ToolUnit;


public class ServiceUtil {
	/**
	 * 获得当前登录的用户对象
	 * 
	 * @param request
	 * @return
	 */
	public static SysManager getCurrentUser(HttpServletRequest request) {
		SysManager user = null;
		CurrentUserSession userSession = CurrentUserSession.findUserSession(request);
		if(userSession != null) {
			user = userSession.getCurrentUser();
		}
		return user;
	}

	public static SysUserinfo getCurrentUserinfo(HttpServletRequest request) {
		SysUserinfo sysUserinfo = null;
		if(request.getSession().getAttribute("sysUserinfo") instanceof SysUserinfo) {
			sysUserinfo = (SysUserinfo)request.getSession().getAttribute("sysUserinfo");
		}else {
			request.getSession().setAttribute("sysUserinfo", sysUserinfo);
		}
		return sysUserinfo;
	}

	public static boolean isNotNull(String value) {
		return value != null && !"".equals(value) && !"null".equals(value.toLowerCase());
	}

	public static void deleteCookie(String cookieName, String cookiePath, HttpServletResponse response) {
		Cookie cookie = new Cookie(cookieName, "");
		cookie.setPath(cookiePath);
		cookie.setMaxAge(-1);
		response.addCookie(cookie);
	}

	public static String checkCurrentUser(HttpServletRequest request, HttpServletResponse response, ICommonService info)
			throws ServletException, IOException {
		String url = request.getRequestURI() + "?" + (request.getQueryString() == null ? "" : request.getQueryString());
		SysManager sysUserinfo = ServiceUtil.getCurrentUser(request);
		if(sysUserinfo == null) {
			request.setAttribute("url", request.getRequestURI() + "?" + (request.getQueryString() == null ? "" : request.getQueryString()));
			request.getRequestDispatcher("/console/index.htm").forward(request, response);
			return null;
		}else {
			// if (sysUserinfo != null
			// && sysUserinfo.getSysUserType().getSutyId().equals(
			// SysUserType.USER_TYPE_WORKER)) {
			if(sysUserinfo != null) {
				CurrentUserSession userSession = CurrentUserSession.findUserSession(request);
				Long id = info.getSysFunction(url);
				boolean flag = userSession.isHasFunction(id);
				if(flag == false) {
					// 没ID的时候跳到登录页面
					request.setAttribute("url", request.getRequestURI() + "?"
							+ (request.getQueryString() == null ? "" : request.getQueryString()));
					request.getRequestDispatcher("/console/index.htm").forward(request, response);
					return null;
				}
			}
		}
		return null;
	}

	/**
	 * 获取UTF-8格式数据库下,输入中英文混合字符串的真实长度(中文字符占3位)
	 * 
	 * @author MaChao
	 * @date 2008-8-13
	 */
	public static int getStringRealLength(String str) {
		int result = 0;
		if(ServiceUtil.isNotNull(str)) {
			int blen = str.getBytes().length; // 中文占2位时的总长度
			int len = str.length(); // 中文占1位时的总长度
			if(blen > len) {
				result = blen + (blen - len); // (blen - len)即为中文字符数
			}else {
				result = len;
			}
		}
		return result;
	}

	/**
	 * 截断字符串
	 * 
	 * @author MaChao
	 * @date 2009-7-10
	 */
	public static String getStringForTruncation(String str, int length) {
		if(isNotNull(str)) {
			while(getStringRealLength(str) > length) {
				str = str.substring(0, str.length() - 1);
			}
		}
		return str;
	}

	/**
	 * 返回固定长度(真实长度)的字符串，超长的以...结尾。 (主要用于页面显示)
	 * 
	 * @author MaChao
	 * @date 2010-10-15
	 * @param str
	 * @param length
	 * @return
	 */
	public static String getStringForFixedRealLength(String str, int length) {
		if(getStringRealLength(str) <= length) {
			return str;
		}
		return getStringForTruncation(str, length - 2) + "...";
	}

	/**
	 * 截取中英文混合字符串
	 * 
	 * @param text
	 * @param length
	 * @param endWith
	 * @return
	 * @author MaChao
	 * @date 2010-10-26
	 */
	public static String subString(String text, int length, String endWith) {
		int textLength = text.length();
		int byteLength = 0;
		StringBuffer returnStr = new StringBuffer();
		for(int i = 0; i < textLength && byteLength < length * 2; i++) {
			String str_i = text.substring(i, i + 1);
			if(str_i.getBytes().length == 1) {// 英文
				byteLength++;
			}else {// 中文
				byteLength += 2;
			}
			returnStr.append(str_i);
		}
		try {
			if(byteLength < text.getBytes("GBK").length) {// getBytes("GBK")每个汉字长2，getBytes("UTF-8")每个汉字长度为3
				returnStr.append(endWith);
			}
		}catch(UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return returnStr.toString();
	}

	/**
	 * 通过扩展名获得文件类型[大类型]
	 * 
	 * @author MaChao
	 * @date 2010-10-29
	 * @param fileName
	 * @return["video":视频;"audio":音频;"document":文档;"photo":图像;]
	 */
	public static String getFileTypeBySpread(String fileName) {
		if(!ServiceUtil.isNotNull(fileName)) {
			return "";
		}
		String spread = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		// 视频
		if("avi".equals(spread)) {
			return "video";
		}
		if("asf".equals(spread)) {
			return "video";
		}
		if("dat".equals(spread)) {
			return "video";
		}
		if("mpeg".equals(spread)) {
			return "video";
		}
		if("rmvb".equals(spread)) {
			return "video";
		}
		if("mp4".equals(spread)) {
			return "video";
		}
		if("3gp".equals(spread)) {
			return "video";
		}
		if("rm".equals(spread)) {
			return "video";
		}
		if("flv".equals(spread)) {
			return "video";
		}
		if("mov".equals(spread)) {
			return "video";
		}
		// 音频
		if("wma".equals(spread)) {
			return "audio";
		}
		if("mp3".equals(spread)) {
			return "audio";
		}
		if("amr".equals(spread)) {
			return "audio";
		}
		if("wav".equals(spread)) {
			return "audio";
		}
		// 文档
		if("doc".equals(spread)) {
			return "document";
		}
		if("docx".equals(spread)) {
			return "document";
		}
		if("pdf".equals(spread)) {
			return "document";
		}
		if("ppt".equals(spread)) {
			return "document";
		}
		if("xls".equals(spread)) {
			return "document";
		}
		if("txt".equals(spread)) {
			return "document";
		}
		if("swf".equals(spread)) {
			return "document";
		}
		// 图像
		if("jpg".equals(spread)) {
			return "photo";
		}
		if("bmp".equals(spread)) {
			return "photo";
		}
		if("png".equals(spread)) {
			return "photo";
		}
		if("gif".equals(spread)) {
			return "photo";
		}
		if("jpeg".equals(spread)) {
			return "photo";
		}
//		if("tif".equals(spread)) {
//			return "photo";
//		}
		return "";
	}
	
	/**
	 * 构造缩略图
	 * 
	 * @param pic
	 * @return
	 * @throws IOException
	 * @author MaChao
	 * @date 2010-10-13
	 */
	public static byte[] createThumbPic(int new_w, int new_h, byte[] pic) throws Exception {
		ByteArrayInputStream in = new ByteArrayInputStream(pic);
		Image src;
		byte[] thumb = null;
		src = javax.imageio.ImageIO.read(in);
		// int old_w = src.getWidth(null); // 得到源图宽
		// int old_h = src.getHeight(null);
		// double ratio = (double)old_w / (double)old_h;
		// int new_w = 60;
		// int new_h = 60;
		try {
			BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, new_w, new_h, null); // 绘制缩小后的图
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.write(tag, "jpg", out);
			thumb = out.toByteArray();
			out.close();
		}catch(Exception e) {
			thumb = pic;
			e.printStackTrace();
		}
		return thumb;
	}
	/**
	 * 通过扩展名获得文件类型[小类型]
	 * 
	 * @author MaChao
	 * @date 2010-11-24
	 * @param fileName
	 * @return[如:"word"、"pdf"、"avi"、"rmvb"]
	 */
	public static String getIconTypeBySpread(String fileName) {
		if(!ServiceUtil.isNotNull(fileName)) {
			return "";
		}
		String spread = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
		// 文档
		if(spread.startsWith("doc")) {
			return "word";
		}
		if(spread.startsWith("wps")) {
			return "word";
		}
		if(spread.startsWith("pdf")) {
			return "pdf";
		}
		if(spread.startsWith("ppt")) {
			return "ppt";
		}
		if(spread.startsWith("pot")) {
			return "ppt";
		}
		if(spread.startsWith("pps")) {
			return "ppt";
		}
		if(spread.startsWith("rtf")) {
			return "txt";
		}
		if(spread.startsWith("txt")) {
			return "txt";
		}
		// 图片
		if(spread.startsWith("bmp")) {
			return "jpg";
		}
		if(spread.startsWith("jpeg")) {
			return "jpg";
		}
		if(spread.startsWith("png")) {
			return "jpg";
		}
		if(spread.startsWith("jpg")) {
			return "jpg";
		}
		if(spread.startsWith("gif")) {
			return "gif";
		}
		// 音频
		if(spread.startsWith("mp3")) {
			return "mp3";
		}
		if(spread.startsWith("wav")) {
			return "wav";
		}
		// 视频
		if(spread.startsWith("flv")) {
			return "flv";
		}
		if(spread.startsWith("fla")) {
			return "flv";
		}
		if(spread.startsWith("mov")) {
			return "mov";
		}
		if(spread.startsWith("avi")) {
			return "avi";
		}
		// 其它
		if(spread.startsWith("htm")) {
			return "html";
		}
		if(spread.startsWith("xml")) {
			return "html";
		}
		return "";
	}
	
	
	/**
	 * 获取封面
	 * @param resSource
	 * @param request
	 * @author MaChao
	 * @return
	 */
	public static String getImgUrl(ResSourceData resSource,
			HttpServletRequest request) {
		String imgUrl = resSource.getRsdaBigimgUrl();
		if (imgUrl != null) {
			imgUrl = imgUrl.replaceAll("\\n", "");
		}
		if (!ServiceUtil.isNotNull(imgUrl)) {
			return "";
		}
		imgUrl = SysConfigInfo.FILE_URL_PATH + imgUrl;
		return imgUrl;
	}
	
	/**
	 * 获得用户Locale
	 * @param request
	 * @return
	 */
	public static ResourceBundle getUserLocale(HttpServletRequest request) {
		Object val = request.getSession().getAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME);
		Locale locale = null;
		if(val != null){
			locale = (Locale)val;
		}else{
			locale = new Locale("zh", "CN");  
		}
		return ResourceBundle.getBundle("messages",locale);
	}
	
	public static void main(String[] args){
		System.out.println(ToolUnit.getStringRealLengthByGbk("你是我一ddq生最爱的人kk"));
		System.out.println(getStringRealLength("你是我一生最爱的人"));
		System.out.println(ToolUnit.subStringByGbk("你是我一ddq生最爱的人kk", 20));
	}
}
