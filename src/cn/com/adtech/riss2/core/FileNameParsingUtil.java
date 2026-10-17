package cn.com.adtech.riss2.core;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

public class FileNameParsingUtil {
	/**
     * 处理不同浏览器对文件名的解析问题
     * 
     * @Title: processFileName
     * 
     * @Description: ie,chrom,firfox下处理文件名显示乱码
     */
    public static String processFileName(HttpServletRequest request, String fileNames) {
        String codedfilename = null;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent && -1 != agent.indexOf("Trident")) {
            	// ie
            	codedfilename = URLEncoder.encode(fileNames, "UTF8");
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {
            	//火狐,chrome等
                codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
            }else{
            	//其他浏览器 
            	codedfilename = URLEncoder.encode(fileNames, "UTF8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codedfilename;
    }
}
