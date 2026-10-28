package cn.com.adtech.riss2.cache;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.fileservice.FileServiceSettings;

/**
 * 
 * 系统参数配置对象，在启动项目时即全部加载到该对象；使用时便不用每次再从配置文件中获取
 *
 * @author MaChao
 *
 * @date：Dec 28, 2017
 */
public class SysConfigInfo {


	//----config.properties相关配置缓存----//
	public static String JDBC_DRIVER = "";
	public static String JDBC_URL = "";
	public static String JDBC_USERNAME = "";
	public static String JDBC_PASSWORD = "";
	public static String DATABASE_DIALECT = "";
	public static String DB_BACKUP_DIR = "";//数据库备份路径
	
	
	/** 项目文件总体存放根路径 **/
	public static String FILE_ROOT_DIR = "";
	
	/** fileService项目本地webService访问路径 ***/
	public static String WEBSERVICE_PATH = "";
	
	/** fileService项目外部访问路径 ***/
	public static String FILE_URL_PATH = "";
	
	
	/**
	 * 初始化基本配置参数，随项目启动时执行
	 */
	private static String contextPath = "";

	public static void initConfig(String servletContextPath) {
		contextPath = servletContextPath == null ? "" : servletContextPath;
		initConfig();
	}

	public static void initConfig(){
		JDBC_DRIVER = CommonUtil.getProperties("/config.properties", "jdbc.driverClassName");
		JDBC_URL = CommonUtil.getProperties("/config.properties", "jdbc.url");
		JDBC_USERNAME = CommonUtil.getProperties("/config.properties", "jdbc.username");
		JDBC_PASSWORD = CommonUtil.getProperties("/config.properties", "jdbc.password");
		DATABASE_DIALECT = CommonUtil.getProperties("/config.properties", "jdbc.database.dialect");
		WEBSERVICE_PATH = CommonUtil.getProperties("/config.properties", "webServicePath");
		FILE_URL_PATH = CommonUtil.getProperties("/config.properties", "fileUrlPath");
		if (FileServiceSettings.embedded()) {
			// Same-origin URLs follow the main application's port/domain and context path.
			WEBSERVICE_PATH = contextPath + "/fileService";
			FILE_URL_PATH = contextPath + "/fileService";
		}
		
		FILE_ROOT_DIR = FileServiceSettings.property("file.dir", "");
		String lastChar = FILE_ROOT_DIR.substring(FILE_ROOT_DIR.length() - 1);
		if(!lastChar.equals("/") && !lastChar.equals("\\")){
			FILE_ROOT_DIR += "/";
		}
		INDEX_MAIN_DIR = FILE_ROOT_DIR + "index/main";
		INDEX_PAGE_DIR = FILE_ROOT_DIR + "index/page";
		INDEX_GROUP_DIR = FILE_ROOT_DIR + "index/group";
		INDEX_TXT_DIR = FILE_ROOT_DIR + "index/txt";
		
		DB_BACKUP_DIR = FILE_ROOT_DIR + "databasebackup/";
	} 
	
	
	
	//---------其它之前config.properties中的配置。全部改为不再配置，在此处直接定义--------//
	/** 资源（按库）存放目录 **/
	public static String RES_DIR = "resource/";
	
	/** 批量导入资源的EXCLE表格存放目录 ***/
	public static String IMP_EXCEL_DIR = "impExcel/";
	
	
	/** 特色库封面存放路径 ***/
	public static String STORE_FACE_DIR = "storeimg/";
	
	/** 数据库备份存放目录 ***/
	public static String DATA_BAK_DIR = "databak/";
	
	/** 信息发布文件存放路径 ***/
	public static String INFORMATION_DIR = "information/";
	
	//// 其它相关文件存放地址 ***/
	public static String OTHER_DIR = "other/";
	
	/** 主索引文件存放路径 ***/
	public static String INDEX_MAIN_DIR = "";
	/** 子页索引存储路径 ***/
	public static String INDEX_PAGE_DIR = "";
	/** 聚类索引存放目录 ***/
	public static String INDEX_GROUP_DIR = "";
	/** 索引全文存放目录 ***/
	public static String INDEX_TXT_DIR = "";
	
	
	/** 采集资源批量存放根目录 （该功能几乎不用） ***/
	public static String GATHER_RES_DIR = "gather/";
	/** 采集资源导入的Excle目录 ***/
	public static String GATHER_RES_EXCEL_DIR = "gather/excel/";
}
