package org.apache.lucene.analysis.sun.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * 获取词语语种
 * @description TODO
 *
 * @author wanglin
 * 
 * @version 1.0
 *
 * @date Apr 27, 20162:32:52 PM
 *
 */
public class LanguageUtil {
    public static boolean isEnglish(String charaString) {
        return charaString.matches("^[a-zA-Z]*");
    }
 
    public static boolean isChinese(String str){
        boolean temp = false;
        Pattern p=Pattern.compile("[\u4e00-\u9fa5]"); 
        Matcher m=p.matcher(str); 
        if(m.find()){ 
            temp =  true;
        }
        return temp;
    }
    
    public static boolean isChinese(char c){
    	if(checkType(c) == CharType.CHINESE){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public static boolean isTibet(char c){
    	if(checkType(c) == CharType.TIBETAN){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    /**
     * 判断字符语言。<br>
     * 这里只是简单实现了一个做个例子；没有经过任何算法提高效率及判断多种语言处理；
     * 建议参数其它分词器通过的语言判断方式。采用Unicode编码判断获取各种语言类型
     *
     * @author wanglin
     * @date Apr 27, 2016
     *
     * @param str
     * @return
     */
    public static String getWordText(String str){
    	if(str == null){
    		return "";
    	}
    	if(isEnglish(str)){
    		return "ENGLISH";
    	}
    	if(isChinese(str)){
    		return "CN_WORD";
    	}
    	return "TIBETEN";
    }
    
    
    
    public enum CharType{   
        NUMBER, //2字节数字１２３４   
        ENGLISH, //gb2312中的，例如:ＡＢＣ，2字节字符同时包含 1字节能表示的 basic latin and latin-1 OTHER,// 其他字符
        TIBETAN,
        CHINESE,//中文字  
        OTHER;
    }  
    /** 
    * 判断输入char类型变量的字符类型 
    * @param c char类型变量 
    * @return CharType 字符类型 
    */  
    public static CharType checkType(char c) {
		CharType ct = null;

		//中文，编码区间0x4e00-0x9fbb                
		if ((c >= 0x4e00) && (c <= 0x9fbb)) {
			ct = CharType.CHINESE;
		}
		//Halfwidth and Fullwidth Forms， 编码区间0xff00-0xffef   
		else if ((c >= 0xff00) && (c <= 0xffef)) { //        2字节英文字   
			if (((c >= 0xff21) && (c <= 0xff3a)) || ((c >= 0xff41) && (c <= 0xff5a))) {
				ct = CharType.ENGLISH;
			} else if ((c >= 0xff10) && (c <= 0xff19)) {
				ct = CharType.NUMBER; //2字节数字 
			} else {
				ct = CharType.OTHER; //其他字符，可以认为是标点符号  
			}
		}

		//basic latin，编码区间 0000-007f                 
		else if ((c >= 0x0021) && (c <= 0x007e)) { //1字节数字 if (( c >= 0x0030 )&&(
			if ((c >= 0x0030) && (c <= 0x0039)) {
				ct = CharType.NUMBER; //1字节字符    
			} else if (((c >= 0x0041) && (c <= 0x005a)) || ((c >= 0x0061) && (c <= 0x007a))) {
				ct = CharType.ENGLISH;
			} else {
				ct = CharType.OTHER;
			}
		}
		//其他字符，可以认为是标点符号 else ct = CharType.DELIMITER; }  
		//latin-1，编码区间0080-00ff                
		else if ((c >= 0x00a1) && (c <= 0x00ff)) {
			if ((c >= 0x00c0) && (c <= 0x00ff)) {
				ct = CharType.ENGLISH;
			} else {
				ct = CharType.OTHER;
			}
		}
		
		//藏文编码
		else if (c >= 0x0F00 && c <= 0x0FFF){
			return CharType.TIBETAN;
		}
		else{
			ct = CharType.OTHER;
		}
		return ct;
	} 
    
    /**
	 * 判断是否为藏文分割符
	 *
	 * @author wanglin
	 * @date Sep 19, 2016
	 *
	 * @param c
	 * @return
	 */
	public static boolean isSplit(char c){
		if(c == '་' || 
				c == '།' || 
				c == ' '){
			return true;
		}
		return false;
	}
   
}
