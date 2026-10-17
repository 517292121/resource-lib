package cn.com.adtech.riss2.calisSchema;

import java.util.List;

import org.dom4j.Element;

public class ETD extends SchemaGeneral{

	public ETD(String fileName) {
		super(fileName);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void body(Object object) {
		//之后加载。
		initCalis_Header();
		initCalis_Metadata();
		
		 List<MetaDataEntity> list = ( List<MetaDataEntity>)object;
		 Element root = metadata.addElement(calis+type+"_V2:"+type,"http://www.calis.edu.cn/metadata_ns/2.0/"+type+"/");
		 root.addAttribute("xmlns:xsi","http://www.w3.org/2001/XMLSchema-instance");
		 root.addAttribute(qname,"http://www.calis.edu.cn/metadata_ns/2.0/"+type+"/ http://www.calis.edu.cn/metadata_ns/2.0/"+type+"/"+type+"v2.xsd");
		 for(MetaDataEntity dataEntity : list){
			 List<MetaDataEntity> childlist = dataEntity.getList();
			 	if(dataEntity.getName().toLowerCase().equals("type")){
			 		continue;
			 	}
			 	
			 	if(dataEntity.getName().toLowerCase().equals("identifier")){
			 		if(null == dataEntity.getValue() || dataEntity.getValue().trim().equals("")){
			 			dataEntity.setValue("null");
			 		}
			 	}
			 	if(dataEntity.getName().toLowerCase().equals("contributors")){
			 		System.out.println();
			 	}
				 Element element = root.addElement(calis+type+"_V2:"+dataEntity.getName().toLowerCase());
				 element.setText((dataEntity.getValue()==null||dataEntity.getValue().trim().equals(""))?"无":dataEntity.getValue());
				 if(null!=childlist&&childlist.size()>0){
				 for(MetaDataEntity childdata : childlist){
						 //根据CALIS后提出只收割15个核心元素，及几个特殊子集元素修改
						 if(childdata.getName().toLowerCase().equals("disciplinelist")||childdata.getName().toLowerCase().equals("discipline")){
							 //学科
							 Element elementchild = root.addElement(calis+type+"_V2:subject");
							 elementchild.addAttribute("scheme", "disciplineList");
							 elementchild.setText(childdata.getValue());
							 continue;
						 }else if(childdata.getName().toLowerCase().equals("keyword")){
							 //如果SUBJECT为空，就用keyword代替
							 if(element.getText().trim().equals("")){
								 element.setText(childdata.getValue());
							 }
							//keyword
							 Element elementchild = root.addElement(calis+type+"_V2:subject");
							 elementchild.addAttribute("scheme", "keyword");
							 elementchild.setText(childdata.getValue());
							 continue;
						 }else if(childdata.getName().toLowerCase().equals("rightsstatement")){
							 //权限声明
							 if(element.getText().trim().equals("")){
								 element.setText(childdata.getValue());
							 } 
							 Element elementchild = element.addElement(calis+type+"_V2:rightsStatement");
							 elementchild.setText(childdata.getValue());
							 continue;
						 }else if(childdata.getName().toLowerCase().equals("securitylclassfication")){
							 //保密级别
							 if(element.getText().trim().equals("")){
								 element.setText(childdata.getValue());
							 } 
							 Element elementchild = element.addElement(calis+type+"_V2:securitylClassfication");
							 elementchild.setText(childdata.getValue());
							 continue;
						 }else if(childdata.getName().toLowerCase().equals("defensedate")){
							 Element elementchild = element.addElement(calis+type+"_V2:defenseDate");
							 elementchild.setText((dataEntity.getValue()==null||dataEntity.getValue().trim().equals(""))?"无":dataEntity.getValue());
							 continue;
						 }else if(childdata.getName().toLowerCase().equals("datesubmitted")){
							 Element elementchild = element.addElement(calis+type+"_V2:dateSubmitted");
							 elementchild.setText((dataEntity.getValue()==null||dataEntity.getValue().trim().equals(""))?"无":dataEntity.getValue());
							 continue;
						 }
							 Element elementchild = element.addElement(calis+type+"_V2:"+childdata.getName().toLowerCase());
							 elementchild.setText((childdata.getValue()==null||childdata.getValue().trim().equals(""))?"无":childdata.getValue());
				 }
			 } 
		 }
		 Element element = root.addElement(calis+type+"_V2:type");
		 element.setText(rltyname);
	}
}
