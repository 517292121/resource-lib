package cn.com.adtech.riss2.core;

/**
 *
 * 说明：多语种基本处理方法
 *
 * 创建时间：2020-4-7 下午4:50:07
 *
 * 创建者：王林
 *
 */
public class LanguageUtil {

	/**
	 * 将语种编码（zh、zw...）转换为中文名称（中文、藏文...）
	 * @param code
	 * @return
	 */
	public static String codeToTxt(String code){
		String txt = null;
		if(!CommonUtil.isNotNull(code)){
			txt = "";
		}else if(code.equals("zh") || code.equals("中文")){
			txt = "中文";
		}else if(code.equals("zw") || code.equals("藏文")){
			txt = "藏文";
		}else if(code.equals("mw") || code.equals("蒙文")){
			txt = "蒙文";
		}else if(code.equals("en") || code.equals("英文")){
			txt = "英文";
		}else{
			txt = code;
		}
		
		return txt;
	}
	
	/**
	 * 将语种中文名称（中文、藏文...）转换为编码（zh、zw...）
	 * @param code
	 * @return
	 */
	public static String txtToCode(String txt){
		String code = null;
		if(!CommonUtil.isNotNull(txt)){
			code = "";
		}else if(txt.equals("zh") || txt.equals("中文")){
			code = "zh";
		}else if(txt.equals("zw") || txt.equals("藏文")){
			code = "zw";
		}else if(txt.equals("mw") || txt.equals("蒙文")){
			code = "mw";
		}else if(txt.equals("en") || txt.equals("英文")){
			code = "en";
		}else{
			code = txt;
		}
		
		return code;
	}
	
	public static String subStr(String string, int len) {
        //用于计算字符串的中英文字符长度的数量
		int length = 0;
		for (int i = 0; i < string.length(); i++) {
            //如果unicode编码大于255，则证明不是英文，长度加2
			length = (int) (string.charAt(i)) > 255 ? length + 2 : length+1;
			if (length > len) {
				return string.substring(0,i);
			}
		}
		return string;
	}
	
	public static String substring(String str, int saveLen){
		if(str == null){
			return "";
		}
		int len = str.getBytes().length;
		if(len < saveLen){
			return str;
		}
		
		while(len > saveLen){
			str = str.substring(0, str.length()-1);
			len = str.getBytes().length;
		}
		return str;
	}
	
	
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String str = "སི་ཁྲོན་མི་རིགས་ཨུ་ལྷན་གྱིས་བློ་མཐུན་འགྱུར་མེད་རྡོ་རྗེ་ལ་“སི་ཁྲོན་ཞིང་ཆེན་མི་རིགས་མཐུན་སྒྲིལ་ཡར་ཐོན་གྱི་མི་སྒེར་གྱི་མིག་དཔེའི”མཚན་སྙན་གནང་བའི་མཛད་སྒོ་དང་སྔོན་ཐོན་བྱས་རྗེས་ཀྱི་སྙན་ཞུ་ཞུ་བའི་ཚོགས་འདུ་འཚོགས་པ།.....ངེད་གསར་འགོད་པས་ཐོ་འགོད་བྱས་པ་དང་པར་བླངས།";
//		str = "你为什么这样对我，啊..........why";
		str = "དུས་ཚིགས་དང་པོར་ཀྲུང་གོའི་དཔལ་འབྱོར་དུས་མཚུངས་ལས་7.7%འཕར་བ།.......ངེད་རྩོམ་སྒྲིག་པུའུ་ཡིས་སྒྲིག་སྒྱུར་བྱས།";
		int len = str.getBytes().length;
		
		
		System.out.println(str.length());
		System.out.println(len);
		
		String s2 = substring(str, 256);
		System.out.println(s2);
		System.out.println(s2.getBytes().length);
	}
}
