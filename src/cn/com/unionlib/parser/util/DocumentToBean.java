package cn.com.unionlib.parser.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import cn.com.unionlib.parser.core.interfaces.ParseValue;

/**
 * doc转bean
 * @Description: TODO
 * @author MaChao
 * @date 2017-4-12
 */
public class DocumentToBean {
	
	
	public DocumentToBean() {
	
	}

	/**
	 * 构造函数
	 */
	public DocumentToBean(ParseValue parseValue) {
		this.parseValue = parseValue;
	}
	
	/**
	 * doc批量转实体
	 * @author MaChao
	 * @date 2017-4-13
	 */
	public List<?> get(List<Document> docList,Class clas,Query query){
		List<Object> list = new ArrayList<Object>();
		for(Document doc: docList){
			  list.add(get(doc, clas,query));
		}
		return list;
	}
	
	
	/**
	 * doc转实体
	 * @author MaChao
	 * @date 2017-4-12
	 */
	public Object get(Document doc,Class clas,Query query){
		Object o = null;
		if(clas==null){
			throw new NullPointerException("实体类不能为空!");
		}
		if(doc==null){
			return o;
		}	
		try {
			//创建实例
			o =clas.newInstance();
			Object value = null;
			Object parserValue = null;
			//获取所有字段
			Field[] fields = clas.getDeclaredFields();
			String filedType = "";
			String filedName = "";
			for (Field f : fields) {
				//设置可访问私有变量
				f.setAccessible(true);
				//根据字段名获取doc值
				filedName = f.getName();
				if(filedName.equals("creator")|| filedName.equals("subject") 
						|| filedName.equals("keyword")){
					value = doc.getValues(filedName);
				}else{
					value = doc.get(f.getName());
				}
				//获取字段类型
				filedType = f.getType().toString();			
				if(parseValue==null){	
					setvalue(o,f,filedType,value,query);					
				}else{
					try{
						//解析不为空,优先执行自定义接口
						parserValue =  parseValue.ParseObject(filedType, value);
						if(parserValue==null){
							parserValue = value;
						}
						setvalue(o,f,filedType,value,query);						
					}catch (Exception e) {
						System.out.println(value+" 自定义转换错误!");
					}		
				}
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		return o;
	}

	/**
	 * 高亮处理
	 */
	protected  String getHighlighterStr(Query bQuery, String fieldName, String content,int subsize){
		if(null==content && "".equals(content)){
			return "";
		}
		Formatter formatter = new SimpleHTMLFormatter("<span style='color: red'>", "</span>");
		
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(bQuery));
		Fragmenter fragmenter = new SimpleFragmenter(subsize);
		highlighter.setTextFragmenter(fragmenter);
		Analyzer analyzer = AnalyzerFactory.getInstance().getFineAnalyzer();
		String str  = "";
		try{
			str = highlighter.getBestFragment(analyzer, fieldName, content);
			if(str==null){
				str = content.substring(0,content.length()>subsize ? subsize:content.length());
			}
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("高亮失败!");
		}
		return str;
	}
	/**
	 * 处理字段值
	 * @author MaChao
	 * @date 2017-4-13
	 */
	protected Field setvalue(Object o ,Field field,String filedType,Object value,Query query) {
		try{
			if(value==null){
				return null;
			}
			if(value instanceof String[]){
				String[] strs = (String[]) value;
				if(strs.length==0){
					return field;
				}
				List<String> list = new ArrayList<String>();
				String str = null;
				for(String s : strs){
					str = getHighlighterStr(query, field.getName(), s, 200);
					list.add(str);
				}
				field.set(o,list);
			}else if (filedType.endsWith("String")) {
				field.set(o,getHighlighterStr(query, field.getName(), value.toString(), 200));
			}else if(filedType.endsWith("Long")|| filedType.endsWith("long")){
				field.set(o, parseLong(value));
			}
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return field;
	}
	
	
	/**
	 * 转换为long
	 * @author MaChao
	 * @date 2017-4-13
	 */
	protected Long parseLong(Object value){
		Long l = 0L;
		try{
			if(value!=null){
				l = Long.parseLong(value.toString());
			}
		}catch (Exception e) {
			System.out.println(value+"转换Long错误");
		}
		return l;
	}
	
	/**
	 * 转换为Integer
	 * @author MaChao
	 * @date 2017-4-13
	 */
	protected Integer parseInteger(Object value){
		Integer integer = null;
		try{
			if(value!=null){
				integer = Integer.parseInt(value.toString());
			}
		}catch (Exception e) {
			System.out.println(value+":转换Integer错误");
		}
		return integer;
	}
	
	/**
	 * 转换为Integer
	 * @author MaChao
	 * @date 2017-4-13
	 */
	protected int parseint(Object value){
		int i = 0;
		try{
			if(value!=null){
				i = Integer.parseInt(value.toString());
			}
		}catch (Exception e) {
			System.out.println(value+":转换int错误");
		}
		return i;
	}
	
	/**
	 * 转换接口
	 */
	private ParseValue parseValue;

	public ParseValue getParseValue() {
		return parseValue;
	}
	public void setParseValue(ParseValue parseValue) {
		this.parseValue = parseValue;
	}

}
