package cn.com.adtech.riss2.calisSchema;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;

import sun.misc.BASE64Decoder;

public class SchemaGeneral extends Schema{
	 

	public SchemaGeneral(String fileName) {
		super(fileName);
	}

	@Override
	public void body(Object object) {
		 List<MetaDataEntity> list = ( List<MetaDataEntity>)object;
		 Element root = metadata.addElement(calis+type+":"+type,"http://www.calis.edu.cn/metadata_ns/1.0/"+type+"/");
		 root.addNamespace("xsi","http://www.w3.org/2001/XMLSchema-instance");
		 root.addAttribute(qname,"http://www.calis.edu.cn/metadata_ns/oai/"+type+"/ http://www.calis.edu.cn/metadata_ns/oai/"+type+"/"+type+".xsd");
		 for(MetaDataEntity dataEntity : list){
			 List<MetaDataEntity> childlist = dataEntity.getList();
				 Element element = root.addElement(calis+type+":"+dataEntity.getName());
				 element.setText(dataEntity.getValue());
				 if(null!=childlist&&childlist.size()>0){
				 for(MetaDataEntity childdata : childlist){
					 if(null!=childdata.getName()&&null!=childdata.getValue()){
						 Element elementchild = element.addElement(calis+type+"-"+childdata.getName());
						 elementchild.setText(childdata.getValue());
					 }
				 }
			 } 
		 }
		 
	}
	
	@Override
	protected String setCalis_Identifier() {
		return "oai:"+library+".calis.edu.cn:"+type+"/ID"+resId;
	}

	/**
	 * ID 文件路径 
	 */
	@Override
	public void about(Object object) {
		Map<String , Object> objList = (Map<String , Object>)object;
		String basepath =  objList.get("basePath") == null ? "": objList.get("basePath").toString();
		String filepath = objList.get("rsdaFileLocalName") == null ? "": objList.get("rsdaFileLocalName").toString();
		String filename = objList.get("rsdaFileName") == null ? "": objList.get("rsdaFileName").toString() ;
		String filetype = objList.get("rsdaFileType") == null ? "": objList.get("rsdaFileType").toString() ;
		String webpath = objList.get("webpath") == null ? "": objList.get("webpath").toString() ;
		
		File file = new File(basepath+filepath);
		
		this.about = record.addElement("about");
		
		Element METS = about.addElement("METS:mets","http://www.loc.gov/METS/");
			METS.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			METS.addNamespace("xlink", "http://www.w3.org/TR/xlink");
			METS.addAttribute(qname, "http://www.loc.gov/METS/ http://www.loc.gov/standards/mets/version13/mets.xsd");
			METS.addAttribute("LABEL", CalisMetsUtil.getLable(resId));
			METS.addAttribute("OBJID", CalisMetsUtil.getObjId(library,type,resId));
			METS.addAttribute("PROFILE", CalisMetsUtil.getProFile(resId));
			
			//Mets包所对应的数字对象的时间戳
			METS.addElement("METS:metsHdr").addAttribute("LASTMODDATE", CalisMetsUtil.getLastModDate());

		Element mets_fileSec = METS.addElement("METS:fileSec");
		
		Element mets_fileGrp = mets_fileSec.addElement("METS:fileGrp");
		
		Element mets_file = mets_fileGrp.addElement("METS:file");
			mets_file.addAttribute("MIMETYPE", CalisMetsUtil.getMIMETYPE(filetype));
			mets_file.addAttribute("ID", resId);
			mets_file.addAttribute("SIZE", String.valueOf(file.length()));
			mets_file.addAttribute("USE", filename );

			//FLocat可用于存储文件对象的CALIS-OID和URL，它是一个可重复元素
				Element mets_FLocat = mets_file.addElement("METS:FLocat");
			//判断是否有原文，如有就生成。没有，就用链接的方式。 由于平台中资源紧紧只对应一个源文件，所以每次只执行一种
			if(file.isFile()){
					mets_FLocat.addAttribute("LOCTYPE", "OTHER");
					mets_FLocat.addAttribute("OTHERLOCTYPE", "CALISOID");
					mets_FLocat.addAttribute("xlink:href", CalisMetsUtil.getObjId(library,type,resId));
				//加入编码后的文件节点
				Element mets_FContent = mets_file.addElement("METS:FContent");
				Element mets_binData = mets_FContent.addElement("METS:binData");
					try {
						mets_binData.setText(CalisMetsUtil.getFileContent(file));
					} catch (IOException e) {
						e.printStackTrace();
					}
			}else{
					mets_FLocat.addAttribute("LOCTYPE", "URL");
					mets_FLocat.addAttribute("xlink:href", webpath+"platform/search/detailView.htm?rsdaId="+resId);
			}
		
		//定义mets的结构
		Element mets_structMap = METS.addElement("METS:structMap");
			mets_structMap.addAttribute("TYPE", "single");
		Element mets_div = mets_structMap.addElement("METS:div");
			mets_div.addAttribute("LABEL", CalisMetsUtil.getLable(resId));
			mets_div.addAttribute("ORDER", "1");
			mets_div.addAttribute("TYPE", "obj");
	}
}
