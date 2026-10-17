package cn.com.adtech.riss2.core;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.MmSetting;
import cn.com.adtech.riss2.domain.SysTouristBrowseCache;

import com.jbetter.util.ProductManager;

@SuppressWarnings("serial")
public final class ConstantsObj implements Serializable {
	/** 列表默认每页显示条数 */
	public static final int DEFAULT_PAGE_SIZE = 20;
	
	/** 判断字符串是否包含字母的正则表达式 */
	public static final String FIND_IS_CONTAIN_LETTER_REGEX = "^.*[a-zA-Z].*$";

	/** 日期格式化串 */
	public static final String DATE_PATTERN = "yyyy-MM-dd";

	/** 日期格式化 */
	public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(DATE_PATTERN);

	/** 日期时间格式化串 */
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

	/** 日期时间格式化 */
	public static final SimpleDateFormat FORMAT_DATE_TIME = new SimpleDateFormat(DATE_TIME_PATTERN);
	
	/** 验证电子邮箱的正则表达式(允许空串) */
	public static final String FIND_IS_E_MAIL_REGEX = "/^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$|^\\s{0}$/";

	final public static String noAuthor = "<span style='color:red'>无</span>";

	/** 列表分页显示文字 */
	public static final String PAGING_TOOLBAR_DISPLAY_MSG = "当前显示第&nbsp;{0}&nbsp;到&nbsp;{1}&nbsp;条,共&nbsp;{2}&nbsp;条";

	public static final String L_MESSAGE = "Sorry,your license is invalid.Please contact with provider(023-86171104)!!";

	private static String strLimitDate = "";

	/** 信息列表显示默认条数 */
//	public static final int IDS_DEFAULT_COUNT = 5;

	/** 文献求助默认条数 */
//	public static final int SCISSUE_DEFAULT_COUNT = 10;

	/** 文献求助附件存放位置 */
	public static final String ISSUE_ATTACHEMENT = "reshelp";
	
	/** 用户头像存放位置 */
	public static final String SUIN_IMAGE = "userface";
	
	/** 用户头像限制文件类型 */
	public static final String SUIN_IMAGE_TYPE = "JPG,GIF,BMP,PNG,JPEG";
	
	/** 用户头像大小(M) */
	public static final int SUIN_IMAGE_SIZE = 2;
	
	/** 资源评价用户默认头像(男) */
	public static final String SUIN_IMAGE_MALE = "/images/platform/user_face_s.gif";
	
	/** 资源评价用户默认头像(女) */
	public static final String SUIN_IMAGE_FEMALE = "/images/platform/user_face_s.gif";
	
	/** 资源评价用户默认头像(未知) */
	public static final String SUIN_IMAGE_UNKNOWN = "/images/platform/user_face_s.gif";

	/** 文献求助默认为20M */
//	public static final Long ISSUE_SIZE = 1024 * 1024 * 20L;
	
	/** 元素分割字符串 */
	final public static String ELEMENT_SPLIT = "$$@@";

	final public static String ELEMENT_DATA_SPLIT = "::!!";

	/** 资源建设中元素框的最高像素,如果设为0则不限制. 最好为14的倍数加8 * */
	final public static int TEXTAREA_MAX_HEIGHT = 218;

	/** 配置文件路径 * */
	final public static String CONFIG_PATH = "/config.properties";
	
	/** 浏览历史显示条数 * */
	final public static int BROWSE_HISTORY_LENGTH = 10;
	
	/**会员上次资源的默认语种ID**/
	final public static long MEMBER_RESOURCE_DEFAULT_LANGUAGE = 1;
	
	/** 忘记密码发送邮件有效时间（天） */
	final public static int FORGET_PASSWORD_MAIL_DAYS = 2;
	
	/** 文件转换命名相关属性类 */
	public static class FileConvertName {
		/** 资源封面文件名命名规则，文件名加_jpg */
		final public static String NAME_IMG = "_jpg";

		/** 预览文件名命名规则，文件名加_pre */
		final public static String NAME_PRE = "_pre";

		/** 源文件名命名规则，文件名加_data */
		final public static String NAME_DATA = "_data";

		/** 生成的图片文件的扩展名 */
		final public static String EXT_JPEG_NAME = "jpeg";

		/** 生成的文档文件(swf)的扩展名 */
		final public static String EXT_SWF_NAME = "swf";

		/** 生成的音频文件(mp3)的扩展名 */
		final public static String EXT_MP3_NAME = "mp3";

		/** 生成的视频文件(flv)的扩展名 */
		final public static String EXT_FLV_NAME = "flv";

		/** 显示类型_图片 */
		final public static String TYPE_PHOTO = "photo";

		/** 显示类型_音频 */
		final public static String TYPE_AUDIO = "audio";

		/** 显示类型_视频 */
		final public static String TYPE_VIDEO = "video";

		/** 显示类型_文档 */
		final public static String TYPE_DOCUMENT = "document";

		/** 从源视频文件生成的预览文件的时间长度(s) */
		final public static String VIEW_FILE_TIME = "120";

		/** 从视频文凭生成封面的截图时间点 */
		final public static String PHOTO_CUT_TIME = "00:00:05";
	}

	public static String getStrLimitDate() {
		return strLimitDate;
	}

	// private static final int PPACKAGENAME = ProductManager.PRODUCT_PACKAGE_ALL;
	// private static final int PVERSIONNAME = ProductManager.PRODUCT_VERSION_PROFESSIONAL;
	protected static final String L_TRIAL_MESSAGE = "Sorry，your software has been out of probationary period.Please contact with provider(023-86171104)!!";

	// ant编译时将自动替换/*@*/PVERSIONNAME/*@*/。Eclipse调试时请更改PVERSIONNAME值
	// private static int productVersionName = /* @ */PVERSIONNAME/* @ */;
	// 调试试用版
	private static int productVersionName = ProductManager.PRODUCT_VERSION_TRIAL;

	// ant编译时将自动替换/*@*/PPACKAGENAME/*@*/。Eclipse调试时请更改PPACKAGENAME值
	// private static int productPackageName = /* @ */PPACKAGENAME/* @ */;
	public static void setStrLimitDate(String strLimitDate) {
		ConstantsObj.strLimitDate = strLimitDate;
	}

	public static int getProductVersion() {
		return productVersionName;
	}

	/**
	 * 获得配置文件属性
	 * 
	 * @author MaChao
	 */
	public static class ConfigurationUtil {
		// 密钥
		public static String KEY_GENERATOR = "Key_Generator";

		private static ConfigurationFactory factory = new ConfigurationFactory("/WEB-INF/classes/config.xml");

		private static Configuration config = null;

		public static int getInt(String name) {
			try {
				config = factory.getConfiguration();
			}catch(ConfigurationException e1) {
				e1.printStackTrace();
			}
			return config.getInt(name);
		}

		public static String getString(String name) {
			try {
				config = factory.getConfiguration();
			}catch(ConfigurationException e1) {
				e1.printStackTrace();
			}
			return config.getString(name);
		}
	}

	/** 系统属性集 */
	private static Properties properties = new Properties();

	/**
	 * 初始化系统属性
	 * 
	 * @param commonService
	 */
	@SuppressWarnings("unchecked")
	public static void initSysProperties(ICommonService commonService) {
		// 初始化系统属性
		properties.clear();
		List list = commonService.getSupportData("MmSetting", "msetId");
		for(int i = 0; i < list.size(); i++) {
			MmSetting mmSet = (MmSetting)list.get(i);
			properties.setProperty(mmSet.getMsetTitle(), ServiceUtil.isNotNull(mmSet.getMsetValue()) ? mmSet.getMsetValue() : "");
		}
	}

	/**
	 * 设置系统属性
	 * 
	 * @param name
	 * @param value
	 */
	public static void setSysProperty(String key, String value) {
		properties.remove(key);
		properties.setProperty(key, ServiceUtil.isNotNull(value) ? value : "");
	}

	/**
	 * 通过key获得系统属性
	 * 
	 * @param key
	 * @return
	 */
	public static String getSysProperty(String key) {
		return properties.getProperty(key);
	}
	
	/**
	 * 获取皮肤代码
	 * @return
	 */
	public static String getSkinCode(){
		String code = getSysProperty("riss2_default_skin");
		if(code.equals("1")){
			return "def";
		} else if(code.equals("2")){
			return "blue";
		} else if(code.equals("3")){
			return "red";
		} else {
			return "def";
		}
	}

	/**
	 * 系统设置KEY
	 * 
	 * @author MaChao
	 */
	public static class SysSettingKey {
		final public static String SOURCE_LOCATION_OPTIONS = "source_location_options"; // 资源馆藏位置选项

		final public static String RESOURCE_UPLOAD_SOURCEFILE_MAXSIZE = "resource_upload_sourcefile_maxsize"; // 允许的源文件上传最大值

		final public static String RESOURCE_UPLOAD_VIEWFILE_MAXSIZE = "resource_upload_viewfile_maxsize"; // 允许的预览文件上传最大值

		final public static String RESOURCE_UPLOAD_PHOTOFILE_MAXSIZE = "resource_upload_photofile_maxsize"; // 允许的预览文件上传最大值
		
		final public static String RESOURCE_DOWN_MAXSIZE = "resource_down_maxsize"; // 允许用户每天下载资源的次数

	}
	
	/**
	 * 游客访问视频记录缓存
	 * 
	 * @author MaChao
	 * @date 2010-12-31
	 */
	public static class SysTouristBrowseHistoryCache{
		/** 允许缓存的游客数量 */
		final public static int TOURIST_SIZE = 5000;
		/**
		 * 用于缓存信息
		 */
		public static Map<String, Long> map = new HashMap<String,Long>();
		
		
		/**
		 * 初始化游客缓存信息
		 * 
		 * @param commonService
		 */
		@SuppressWarnings("unchecked")
		public static void initSysTouristBrowseHistoryCache(ICommonService commonService) {
			// 初始化系统属性
			map.clear();
			// 获取系统游客缓存信息
			List list = commonService.getSysTouristBrowseCacheList();
			for(int i = 0; i < list.size(); i++) {
				SysTouristBrowseCache sysTouristBrowseCache = (SysTouristBrowseCache)list.get(i);
				Long value = sysTouristBrowseCache.getStbhcValue();
				map.put(sysTouristBrowseCache.getStbhcKey(), 0L);
			}
		}

		
		public static void setSysTouristBrowseHistoryCache(String key, Long value) {
			if(map.containsKey(key)){
				map.remove(key);
			}
			map.put(key, value == null ? 0 : value);
		}

		public static Long getSysTouristBrowseHistoryCache(String key) {
			return map.get(key);
		}
	}
	
	/**
	 * 资源相关属性长度截取
	 * @author MaChao
	 *
	 */
	public static class ResourceLength{
		public static final Integer CREATER_LENGTH = 10; 
		public static final Integer PUBLISHER_LENGTH = 15; 
		public static final Integer DESCRIPTION_LENGTH = 100; 
	}
	
	// 限制TAG标签的长度
	public static final int TAG_LENGTH = 4;
	
}
