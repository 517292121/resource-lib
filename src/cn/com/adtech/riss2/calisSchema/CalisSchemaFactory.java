package cn.com.adtech.riss2.calisSchema;

public class CalisSchemaFactory {
	public static Schema getCalis_schema(String type,String fileName){
	   Schema schema = null;
	   
	    if(null == type || type.trim().equals("")){
			schema = new SchemaGeneral(fileName);
	    }else if(type.trim().equals("JNArt")){
			schema = new JNArt(fileName);
		}else if(type.trim().equals("ETD")){
			schema = new ETD(fileName);
		}else if(type.trim().equals("ebook")){
			schema = new Ebook(fileName);
		}else{
			schema = new SchemaGeneral(fileName);
		}
		return schema;
	}
	
	public static enum Schema_Type{
		Schema_General,JNArt
	}
}
