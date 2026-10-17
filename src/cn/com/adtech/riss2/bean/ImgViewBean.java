package cn.com.adtech.riss2.bean;

/**
 * 
 * 图片预览数据对象
 *
 * @author MaChao
 *
 * @date：Sep 6, 2018
 */
public class ImgViewBean {

	//缩略图路径
	private String smallpath;
	
	//切片后的xml路径
	private String xmlpath;
	
	//文件原始名称(未转码)
	private String filename;
	
	public String getSmallpath() {
		return smallpath;
	}

	public void setSmallpath(String smallpath) {
		this.smallpath = smallpath;
	}

	public String getXmlpath() {
		return xmlpath;
	}

	public void setXmlpath(String xmlpath) {
		this.xmlpath = xmlpath;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
}
