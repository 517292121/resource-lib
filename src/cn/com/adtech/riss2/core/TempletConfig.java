package cn.com.adtech.riss2.core;

import cn.com.adtech.riss2.domain.resource.ResImportTemplet;

/**
 * 模板相关常量配置
 * 
 * @author MaChao
 * 
 * @date 2011-07-25
 *
 */
public class TempletConfig {
	
	
	/***************************定义资源批量导入模板中固定列对应的KEY***************************/
	
	/**自定义分类一*/
	final public static String RSDA_RDTY_ONE = "rsda_rdty_one";
	
	/**自定义分类二**/
	final public static String RSDA_RDTY_TWO = "rsda_rdty_two";
	
	/**自定义分类三**/
	final public static String RSDA_RDTY_THREE = "rsda_rdty_three";
	
	
	/**学科分类*/
	final public static String RSDA_RSTY_ID = "rsda_rsty_id";
	
	/**中图法分类*/
	final public static String RSDA_MIDDLENAME = "rsda_middlename";
	
	
	/**文件名称*/
	final public static String RSDA_FILENAME = "rsda_filename";
	
	
	/**资源主题*/
	final public static String RSDA_SOURCE_SUBJECT = "rsda_source_subject";
	
	/**资源存储目录*/
	final public static String RESOURCE_ROOT_DIR = "resource_root_dir";
	
	
	
	
	final public static String getGroupElementName(long eleId){
		if(eleId == ResImportTemplet.RSDA_RSTY_ID){
			return "学科分类";
		}
		if(eleId == ResImportTemplet.RSDA_MIDDLENAME_ID){
			return "中图法分类";
		}
		if(eleId == ResImportTemplet.RSDA_RDTY_ONE_ID){
			return ConstantsObj.getSysProperty(RSDA_RDTY_ONE);
		}
		if(eleId == ResImportTemplet.RSDA_RDTY_TWO_ID){
			return ConstantsObj.getSysProperty(RSDA_RDTY_TWO);
		}
		if(eleId == ResImportTemplet.RSDA_RDTY_THREE_ID){
			return ConstantsObj.getSysProperty(RSDA_RDTY_THREE);
		}
		if(eleId == ResImportTemplet.RSDA_FILENAME_ID){
			return "文件名";
		}
		if(eleId == ResImportTemplet.RSDA_RSSU_ID){
			return "所属主题";
		}
		return "";
	}

}
