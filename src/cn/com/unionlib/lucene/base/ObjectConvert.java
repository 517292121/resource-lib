package cn.com.unionlib.lucene.base;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.facet.index.FacetFields;
import org.apache.lucene.facet.taxonomy.CategoryPath;
import org.apache.lucene.facet.taxonomy.directory.DirectoryTaxonomyWriter;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;

import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.bean.ResPageBean;
import cn.com.unionlib.lucene.factory.AnalyzerFactory;
import cn.com.unionlib.lucene.factory.GroupConnFactory;

import com.jbetter.hibernate.IdGenerator;
import com.opensymphony.oscache.util.StringUtil;

/**
 * 
 * 对象转换
 *
 * @author MaChao
 *
 * @date：Jan 29, 2018
 */
public class ObjectConvert {

	/**
	 * 将DataBean转换为索引Document对象
	 * 
	 * StringField 不分词 TextField 分词
	 * Field.Store.YES 存储
	 * Field.Store.NO  不存储
	 * @author MaChao
	 * @date Nov 01, 2018
	 */
	public final static Document dataToDocument(ResMainBean dataBean)throws Exception {	
		//聚类集合
		List<CategoryPath> categories = new ArrayList<CategoryPath>();
		
		Document d = new Document();
		//id
		String id = String.valueOf(dataBean.getId());
		d.add(new StringField("id", id, Store.YES));
		
		//标题-分词-存储	
		TextField titleField = new TextField("title", CommonUtil.nullToBlank(dataBean.getTitle()), Store.YES);
		titleField.setBoost(2F);
		d.add(titleField);
		
		//全文内容，需要分词，但不做存储(正文内容太大)》》正文做为文本文件已存放到硬盘
		String srcText = CommonUtil.nullToBlank(dataBean.getContent());
		d.add(new TextField("content", srcText, Store.NO));
		//将源文写入硬盘
		String textPath = "";
		if(CommonUtil.isNotNull(srcText)){
			textPath = IoCommonUtil.writeToTxt(srcText, id+".txt");
		}
		
		//创建者
		String creators = dataBean.getCreator();
		addMulitGroup("creator", creators, categories, d);
		
		//出版者
		String publishers = dataBean.getPublisher();
		addMulitGroup("publisher", publishers, categories, d);
		
		//关键词
		String keyword = dataBean.getKeyword();
		addMulitGroup("keyword", keyword, categories, d);
		
		//日期
		String dateStr = dataBean.getDate();
		if(!StringUtil.isEmpty(dateStr)){
			if(dateStr.length()>4){
				dateStr =  dateStr.substring(0, 4);
			}
			d.add(new StringField("date", dateStr, Store.YES));
			categories.add(new CategoryPath("date", dateStr));			
		}	
		
		// 资源类型
		String resType = dataBean.getResType();
		d.add(new StringField("resType", CommonUtil.nullToBlank(resType), Store.YES));
		if(!StringUtil.isEmpty(resType)){
			categories.add(new CategoryPath("resType", resType));
		}
		
		// 学科分类
		String subjectClass = dataBean.getSubjectClass();
		d.add(new StringField("subjectClass", CommonUtil.nullToBlank(subjectClass), Store.YES));
		if(!StringUtil.isEmpty(subjectClass)){
			categories.add(new CategoryPath("subjectClass", subjectClass));
		}
		
		// 特色库
		String resLib = dataBean.getResLibrary();
		d.add(new StringField("resLibrary", CommonUtil.nullToBlank(resLib), Store.YES));
		if(!StringUtil.isEmpty(resLib)){
			categories.add(new CategoryPath("resLibrary", resLib));
		}
		
		//数据类型
		String dataType = CommonUtil.isNotNull(dataBean.getDataType()) ? dataBean.getDataType() : "未知";
		d.add(new StringField("dataType", dataType, Store.YES));
		categories.add(new CategoryPath("dataType", dataType));
		

		//封面
		d.add(new StringField("coverUrl", CommonUtil.nullToBlank(dataBean.getCoverUrl()), Store.YES));
		
		//文本存放路径
		d.add(new StringField("textPath", textPath, Store.YES));
		
		//资源本地存放路径
		d.add(new StringField("srcPath", CommonUtil.nullToBlank(dataBean.getSrcPath()), Store.YES));
		
		//语种
		d.add(new StringField("language", CommonUtil.nullToBlank(dataBean.getLanguage()), Store.YES));
		
		
		//如果需要聚类的字段为0，则不进行聚类存储。否则会报错NoSuchElementException
		if(categories.size() < 1){
			return d;
		}
		//获取聚类写索引
		DirectoryTaxonomyWriter taxoWriter = GroupConnFactory.instance().getTaxonomyWriter();
		//保存聚类索引
		FacetFields facetFields = new FacetFields(taxoWriter);
		facetFields.addFields(d, categories);
		return d;
	}
	
	private static void addMulitGroup(String fieldname, String value, List<CategoryPath> categories, Document d){
		if(value == null){
			return;
		}
		value = value.replaceAll(" |，|；|,|;|/", ",");
		String[] values = value.split(",");
		for (String s : values) {	
			if(StringUtil.isEmpty(s)){
				continue;
			}
			categories.add(new CategoryPath(fieldname, s));
			d.add(new StringField(fieldname, s, Store.YES));
		}
	}
	
	/**
	 * 将索引Document对象转换为DataBean
	 * @param doc
	 * @param query
	 * @param remarkLen
	 * @param isFilter
	 * @return
	 */
	public static ResMainBean documentToData(Document doc, Query query){
		String title = doc.get("title");
		String textPath = doc.get("textPath");
		String txt = "";
		if(CommonUtil.isNotNull(textPath)){
			txt = IoCommonUtil.readTxt(textPath);
		}
		
		ResMainBean b = new ResMainBean();
		b.setId(Long.parseLong(doc.get("id")));
		b.setTitle(getHighlighterStr(query, "title", title, 120));
		if(CommonUtil.isNotNull(txt)){
			b.setContent(getHighlighterStr(query, "content", txt, 160));
		}
		
		b.setCreator(mergeArr(doc.getValues("creator")));
		b.setPublisher(mergeArr(doc.getValues("publisher")));
		b.setKeyword(mergeArr(doc.getValues("keyword")));
		
		b.setDate(doc.get("date"));
		b.setCoverUrl(doc.get("coverUrl"));
		b.setSubjectClass(doc.get("subjectClass"));
		b.setResLibrary(doc.get("resLibrary"));
		b.setResType(doc.get("resType"));
		b.setDataType(doc.get("dataType"));
		b.setSrcPath(doc.get("srcPath"));
		b.setLanguage(doc.get("language"));
		b.setTextPath(textPath);
		return b;
	}
	private static String mergeArr(String[] arr){
		if(arr == null || arr.length < 1){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(int i=0,s=arr.length; i<s; i++){
			sb.append(arr[i]);
			if(i < s-1){
				sb.append("，");
			}
		}
		String str = sb.toString();
		sb = null;
		return str;
	}
	
	/**
	 * 高亮处理
	 */
	private static String getHighlighterStr(Query bQuery, String fieldName, String content, int remarkLen){
		if(CommonUtil.isNotNull(content) == false){
			return "";
		}
		Analyzer analyzer = AnalyzerFactory.instance().getComAnalyzer();
		Formatter formatter = new SimpleHTMLFormatter("<em>", "</em>");
		
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(bQuery));
		Fragmenter fragmenter = new SimpleFragmenter(remarkLen);
		highlighter.setTextFragmenter(fragmenter);
		
		String str = null;
		try {
			str = highlighter.getBestFragment(analyzer, fieldName, content);
		} catch (IOException e) {
			System.out.println("高亮IOException异常字符：" + fieldName + "  content：" + content);
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			System.out.println("高亮InvalidTokenOffsetsException异常字符：" + fieldName + "  content：" + content);
			e.printStackTrace();
		}
		
		//如果内容中没有包含任何关键词，则高亮后的结果为空；此时直接截取前面一段内容
		if(str == null || str.length() < 1){
			if(content != null && content.length() > 0){
				if(remarkLen > content.length()){
					remarkLen = content.length();
				}
				str = content.substring(0, remarkLen);
				
			}
		}
		return str;
	}
	
	
	//子页数据对象转换为索引对象
	final public static Document dataToPageDoc(ResPageBean dataBean)throws Exception {	
		Document d = new Document();
		//id
		String id = dataBean.getId() == null ? IdGenerator.generateId()+"" : String.valueOf(dataBean.getId());
		d.add(new StringField("id", id, Store.YES));
		
		//资源id
		String resid = dataBean.getResId() == null ? "0" : String.valueOf(dataBean.getResId());
		d.add(new StringField("resid", resid, Store.YES));
		
		//页面标题\名称
		String title = CommonUtil.nullToBlank(dataBean.getTitle());
		d.add(new StringField("title", title, Store.YES));
		
			
		//全文内容，需要分词和存储。主要是目录是excel中提取，分页时也要展示检索内容。因此直接存储提取时更方便
		String srcText = CommonUtil.nullToBlank(dataBean.getContent());
		d.add(new TextField("content", srcText, Store.YES));
		
		//页码，存储不分词
		String pagenum = String.valueOf(dataBean.getPageNum());
		d.add(new StringField("pagenum", pagenum, Store.YES));
		//子页类型不分词
		String pageType = String.valueOf(dataBean.getPageType());
		d.add(new StringField("pageType", pageType, Store.YES));
		
		return d;
	}
	
	//子页索引对象转换为数据对象
	final public static ResPageBean docToPagedata(Document doc, Query query, boolean isHighRed)throws Exception {	
		String txt = CommonUtil.nullToBlank(doc.get("content"));
		
		ResPageBean bean = new ResPageBean();
		bean.setId(Long.parseLong(doc.get("id")));
		bean.setResId(Long.parseLong(doc.get("resid")));
		bean.setTitle(doc.get("title"));
		bean.setPageType(doc.get("pageType") == null ? 1 : Integer.parseInt(doc.get("pageType")));
		bean.setPageNum(doc.get("pagenum") == null ? 0 : Integer.parseInt(doc.get("pagenum")));
		
		if(CommonUtil.isNotNull(txt)){
			if(isHighRed){
				bean.setContent(getHighlighterStr(query, "content", txt, 40));
			}else{
				bean.setContent(txt);
			}
		}
		return bean;
	}
}
