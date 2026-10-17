package cn.com.adtech.riss2.calisSchema;

import java.util.List;

import org.dom4j.Element;

public class Ebook extends SchemaGeneral{

	public Ebook(String fileName) {
		super(fileName);
	}
	@Override
	public void body(Object object) {
		//之后加载。
		initCalis_Header();
		initCalis_Metadata();
		
		 List<MetaDataEntity> list = ( List<MetaDataEntity>)object;
		 Element root = metadata.addElement(calis+type+":"+type,"http://www.calis.edu.cn/metadata_ns/1.0/"+type+"/");
		 root.addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		 root.addAttribute(qname,"http://www.calis.edu.cn/metadata_ns/1.0/"+type+"/ http://www.calis.edu.cn/metadata_ns/1.0/"+type+"/"+type+".xsd");
		 for(MetaDataEntity dataEntity : list){
			 List<MetaDataEntity> childlist = dataEntity.getList();
			 	if(dataEntity.getName().toLowerCase().equals("type")||dataEntity.getName().toLowerCase().equals("resourcetype")){
			 		continue;
			 	}
			 	Element element = root.addElement(calis+type+":"+dataEntity.getName().toLowerCase());
					    element.setText(dataEntity.getValue()+"  ");
				 if(null!=childlist&&childlist.size()>0){
				 for(MetaDataEntity childdata : childlist){
						 //根据CALIS后提出只收割15个核心元素，及几个特殊子集元素修改
						 if(childdata.getName().toLowerCase().equals("disciplinelist")||childdata.getName().toLowerCase().equals("discipline")){
							 //学科
							 Element elementchild = root.addElement(calis+type+":subject");
							 elementchild.addAttribute("scheme", "disciplineList");
							 elementchild.setText(childdata.getValue());
							 continue;
						 }else if(childdata.getName().toLowerCase().equals("keyword")){
							 //如果SUBJECT为空，就用keyword代替
							 if(element.getText().trim().equals("")){
								 element.setText(childdata.getValue());
							 }
							//keyword
							 Element elementchild = root.addElement(calis+type+":subject");
							 elementchild.addAttribute("scheme", "keyword");
							 elementchild.setText(childdata.getValue());
							 continue;
						 }else if(childdata.getName().toLowerCase().equals("rightsstatement")){
							 //权限声明
							 Element elementchild = root.addElement(calis+type+":rightManagement");
							 if(childdata.getValue()!=null&&!childdata.getValue().equals(""))
								 elementchild.setText(childdata.getValue());
							 else
								 elementchild.setText("无");
							 continue;
						 }else if(childdata.getName().toLowerCase().equals("securitylclassfication")){
							 //保密级别
							 if(element.getText().trim().equals("")){
								 element.setText(childdata.getValue());
							 } 
						 }
							 Element elementchild = element.addElement(calis+type+":"+childdata.getName().toLowerCase());
							 elementchild.setText(childdata.getValue());
				 } 
				 if(element.getName().equals("rights")){
					 root.remove(element);
				 }	 
			 }
		 }
		 Element element = root.addElement(calis+type+":resourceType");
		 element.setText(rltyname);
	}
}
