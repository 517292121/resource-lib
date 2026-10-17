package cn.com.adtech.riss2.calisSchema;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;


public abstract class Schema{
	/**
	 * 创建的文档对象
	 */
	private Document document = null;  //创建文档
	protected final String calis = "CALIS_";
	protected Namespace xmlns = DocumentHelper.createNamespace("","http://www.calis.edu.cn/metadata_ns/oai/record/");
	protected Namespace xsi = DocumentHelper.createNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
	protected QName qname = DocumentHelper.createQName("schemaLocation", xsi);
	
	/**
	 * 编码
	 */
	protected OutputFormat format = null;
	/**
	 * 路径
	 */
	private String path = "d:\\";

	/**
	 * 文件名
	 */
	protected final String fileName;
	
	/**
	 * 最外部节点record
	 */
	protected Element record;
	
	/**
	 * 内容外部节点metadata
	 */
	protected Element metadata;
	
	/**
	 * about外部节点
	 */
	protected Element about;
	
	/**
	 * 元素类型type
	 */
	protected String type = "";
	
	/**
	 * 馆号
	 */
	protected String library = "";
	
	/**
	 * 资源ID
	 */
	protected String resId = "";
	
	/**
	 * 类型名称
	 */
	protected String rltyname = "其他";
	
	public Schema(String fileName){
		document = DocumentHelper.createDocument();
		document.setXMLEncoding("UTF-8");
		this.fileName = fileName;
		format = OutputFormat.createPrettyPrint();
		format.setEncoding("UTF-8");
		initCalis_Record();
//		initCalis_Header();
//		initCalis_Metadata();
	}
	/**
	 * 加载最外部节点
	 * @return record
	 */
	protected  void initCalis_Record(){
		Element record =document.addElement("record",xmlns.getURI()); 
		record.add(xsi);
		record.addAttribute(qname,"http://www.calis.edu.cn/metadata_ns/oai/record/ http://www.calis.edu.cn/metadata_ns/oai/record/record.xsd");
		this.record = record;
	}
	
	/**
	 * 加载头部
	 */
	protected void initCalis_Header(){
		Element header = record.addElement("header");
		Element identifier =  header.addElement("identifier");
		identifier.setText(setCalis_Identifier());
		Element datestamp = header.addElement("datestamp");
		datestamp.setText(setCalis_Datestamp());
	}
	
	/**
	 * 头部的Identifier
	 */
	protected String setCalis_Identifier(){
		return resId;
	}
	
	/**
	 * 头部的Datestamp
	 */
	protected String setCalis_Datestamp(){
		return setCalis_Time();
	}
	
	protected String setCalis_Time(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("hh:mm:ss");
		Date date = new Date();
		return simpleDateFormat.format(date)+"T"+simpleDateFormat2.format(date)+"Z";
	}
	
	/**
	 * 内容部分外部节点
	 */
	protected void initCalis_Metadata(){
		this.metadata = record.addElement("metadata");
	}
	/**
	 * 内容部分
	 */
	public abstract void body(Object object);
	
	/**
	 * 原文about
	 */
	public abstract void about(Object object);
	 
	/**
	 * 名字规则
	 */
	public String reNameToXML(String str){
		return str.replaceAll(":", "%3A").replaceAll("/", "%2F");
	}
	
	/**
	 * 生成XML
	 * @return Boolean
	 */
	public Boolean createXML() {
		try {
			File file =  new File(path);
			if(!file.isDirectory()){
				file.mkdirs();
			}
			XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(path +reNameToXML("oai:"+library+".edu.cn:"+type+"-RISS2/"+fileName +"@"+ setCalis_Time() ) + ".oai.xml"), format); 
			xmlWriter.write(document); // 写入文件中
			xmlWriter.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	/**
	 * 读取XML
	 * 
	 * @param fileName
	 * @return
	 */
	public static Document XMLReader(String fileName){
		File inputXml=new File(fileName);     
        SAXReader saxReader = new SAXReader();  
        Document document = null;
		try {
			document = saxReader.read(inputXml);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return document;
	} 
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getLibrary() {
		return library;
	}
	public void setLibrary(String library) {
		this.library = library;
	} 
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getRltyname() {
		return rltyname;
	}
	public void setRltyname(String rltyname) {
		this.rltyname = rltyname;
	}
	
}
