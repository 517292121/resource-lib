package cn.com.adtech.riss2.controller.platform.local;

import java.util.Locale;
import java.util.ResourceBundle;

import org.springframework.context.i18n.LocaleContextHolder;

public class LocalecUtil {

	private ResourceBundle chRb ;
	//方法块创建类的时候执行一次
	{
		Locale locale = LocaleContextHolder.getLocale();
		//获取当前系统使用的语言
		String language=locale.toString();
		Locale locale1 = null;
		//根据当前的系统语言现在相应的文件
		if(language == null || language.equals("zh_CN")) {// 中文
			locale1 = new Locale("zh", "");
		}else if(language.equals("en_US")) {// 英文
			locale1 = new Locale("en", "");
		}else if(language.equals("zw_XZ")) { // 藏文
			locale1 = new Locale("zw", "");
		}else {
			locale1 = new Locale("zh", "");  
		}
		chRb = ResourceBundle.getBundle("messages", locale1); // 绑定中文环境与数据文件，获取当前语言风格下的数据
	}
	
	public String get(String key){
		if(chRb==null){
			return "系统错误";
		}
		return chRb.getString(key);
	}
}
