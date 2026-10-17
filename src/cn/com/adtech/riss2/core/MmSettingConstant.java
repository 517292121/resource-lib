package cn.com.adtech.riss2.core;

/**
 * 系统参数表(MM_SETTINGS)中常量值设定。
 * 
 * @author MaChao
 * @date 2010-12-15
 */
public class MmSettingConstant {
	/**
	 * 是属于哪个部分的系统设置参数. <br>
	 * 1.系统参数设置 <br>
	 * 2.网站参数设置 <br>
	 * 4.积分参数设置 <br>
	 */
	public static Short getSysSettingType() {
		return new Short((short)1);
	}

	public static Short getWebSettingType() {
		return new Short((short)2);
	}
	
	public static Short getAsspointType() {
		return new Short((short)4);
	}

	/**
	 * 系统参数名称(MSET_NAME)字段的值
	 */
	// ********1.整个系统参数设置***** begin
	/**
	 * 邮件发送服务器(smtp)
	 */
	public static String getSendMailHost() {
		return "sendmail_host";
	}
	/**
	 * 发信人邮件地址
	 */
	public static String getSendMailFromAddress() {
		return "sendmail_from_address";
	}

	/**
	 * 发信人邮件登录用户名
	 */
	public static String getSendMailFromUsername() {
		return "sendmail_from_username";
	}

	/**
	 * 发信人邮件登录密码
	 */
	public static String getSendMailFromPassword() {
		return "sendmail_from_password";
	}
	
	/**
	 * 会员审核方式:1.邮件审核；2.管理员审核；
	 */
	public static String getUserRegisterCheckType(){
		return "user_register_check_type";
	}
	
	/**
	 * 网站域名
	 */
	 public static String getRiss2Website(){
		 return "riss2_website";
	 }
	 
	 /**
	  * 中文网站版权
	  */
	 public static String getWebSiteFooter4zh(){
		 return "web_site_footer_zh";
	 }
	 
	 /**
	  * 英文网站版权
	  */
	 public static String getWebSiteFooter4en(){
		 return "web_site_footer_en";
	 }
	 
	 /**
	  * 藏文网站版权
	  */
	 public static String getWebSiteFooter4zw(){
		 return "web_site_footer_zw";
	 }
	 
	 /**
	  * 敏感词
	  */
	 public static String getsensitiveWords(){
		 return "sensitive_words";
	 }
	 
	 /**
	  * 热门资源设置：1、最近一个月；3、最近三个月；6、最近半年；
	  */
	 public static String getRiss2IndexHotResource(){
		 return "riss2_index_hotresource";
	 }
	 
	 /**
	  * 允许会员上传的资源类型
	  */
	 public static String getUserUploadResourceType(){
		 return "user_upload_resource_type";
	 }
	 
	 /**
	  * 游客最多浏览的视频数
	  */
	 public static String getUnLoginUserCanBrowseVideoNumber(){
		 return "unlogin_user_can_browse_video_number";
	 }
	 
	 /**
	  * 允许会员上传的图片类型
	  * @return
	  */
	 public static String getAllowUserUploadFileTypePicture(){
		 return "allow_user_upload_file_type_picture";
	 }
	 
	 /**
	  * 允许会员上传的视频类型
	  * @return
	  */
	 public static String getAllowUserUploadFileTypeVideo(){
		 return "allow_user_upload_file_type_video";
	 }
	 
	 /**
	  * 允许会员上传的音频类型
	  * @return
	  */
	 public static String getAllowUserUploadFileTypeAudieo(){
		 return "allow_user_upload_file_type_audieo";
	 }
	 
	 /*
	  * 允许会员上传的文档类型
	  */
	 public static String getAllowUserUploadFileTypeDocument(){
		 return "allow_user_upload_file_type_document";
	 }
	 
	 
	 

	// ********1.整个系统参数设置***** end
	// ********2.网站参数设置*****begin
	/**
	 * 网站显示信息条数
	 */
	public static String getDefaultPageSize() {
		return "default_page_size";
	}

	/**
	 * 是否允许用户上传资源
	 * 
	 * @return
	 */
	public static String getUserUploadResourceIsOpen() {
		return "user_upload_resource_is_open";
	}
	
	/**
	 * 求助回复默认大小
	 */
	public static String getDefaultIssueAttachementSize(){
		return "default_issue_attachement_size";
	}
	
	/**
	 * 获取默认皮肤
	 */
	public static String getRiss2DefaultSkin(){
		return "riss2_default_skin";
	}
	
	
	// ********2.网站参数设置***** end

	/**会员积分设置begin**/
	
	/**
	 * 登录积分
	 */
	public static String getAsspoint_Login(){
		return "asspoint_login";
	}
	/**
	 * 加工资源积分
	 */
	public static String getAsspoint_machining_Res(){
		return "asspoint_machining_res";
	}
	/**
	 * 留言积分
	 */
	public static String getAsspoint_machining_message(){
		return "asspoint_machining_message";
	} 
	
	
	
	/**会员积分设置end**/
	
	/**图书馆号**/
	public static String getRiss_library(){
		return "riss_library";
	} 
}
