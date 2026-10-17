package cn.com.unionlib.lucene.dao;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.read.biff.BiffException;

import org.jsoup.Jsoup;

import cn.com.adtech.riss2.bean.CatalogBean;
import cn.com.adtech.riss2.bean.ImgViewBean;
import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.MyFileCatalogUtil;
import cn.com.adtech.riss2.core.file.MyFileUtil;
import cn.com.adtech.riss2.domain.IdsInformation;
import cn.com.adtech.riss2.domain.text.ResTextResource;
import cn.com.unionlib.lucene.base.IoCommonUtil;
import cn.com.unionlib.lucene.bean.ResMainBean;
import cn.com.unionlib.lucene.bean.ResPageBean;
import cn.com.unionlib.reader.CommonReader;
import cn.com.unionlib.reader.ReaderPdf;

import com.jbetter.hibernate.IdGenerator;

/**
 * 
 * 数据创建索引公共处理封装
 *
 * @author MaChao
 *
 * @date：Feb 6, 2018
 */
public class IndexCommonService extends CommonServiceImp{

	/**每批次创建多少条资源**/
	protected static int BATCH_SIZE = 20;
	
	/**为了提高效果，只查询出了资源需要创建成索引的字段，如果需要添加字段根据情况扩展即可；**/
	protected String getSearchColumn(){
		StringBuffer buffer = new StringBuffer();
		buffer.append("rsda.rsdaId,");		//资源ID
		buffer.append("rsda.rsdaTitle,");			//题名
		buffer.append("rsda.rsdaCreator,");			//创建者
		buffer.append("rsda.rsdaPublisher,");		//出版者
		buffer.append("rsda.rsdaKeyword,");			//关键词
		buffer.append("rsda.rsdaDate,");			//日期
		buffer.append("rsda.resLiterType.rltyName,");			//资源类型id
		buffer.append("rsda.resMetaStore.rmstName,");			//学科分类id
		buffer.append("rsty.rstyName,");
		buffer.append("rsda.rsdaBigimgUrl,");		//封面
		buffer.append("rsda.rsdaFileLocalName,");		//源文存放路径
		buffer.append("rsda.rsdaSourceType,");
		buffer.append("rsda.rsdaFileUrl,");
		
		buffer.append("rsda.rsdaLanguage");  //语种
		String columns = buffer.toString();
		buffer = null;
		return columns;
	}
	

	/**
	 * 将资源转化为索引数据对象
	 * @param resource
	 * @return
	 */
	protected ResMainBean resourceToDatabean(Object[] resource) {
		if (resource == null) {
			return null;
		}
		try {
			Long rsdaId = (Long) resource[0];
			
			//获取资源预览模式
			int viewType = resource[11] == null ? 0 : (Integer) resource[11]; 
			
			String srcFile = resource[10] == null ? "" : resource[10].toString();
			String srcDir = resource[12] == null ? "" : resource[12].toString();
			
			//解析资源全文（主索引）
			ResMainBean bean = parseSrcFile(rsdaId, viewType, srcFile, srcDir);
			
			//封装基本元数据
			bean.setTitle(CommonUtil.nullToBlank(resource[1]));
			bean.setCreator(CommonUtil.nullToBlank(resource[2]));
			bean.setPublisher(CommonUtil.nullToBlank(resource[3]));
			bean.setKeyword(CommonUtil.nullToBlank(resource[4]));
			bean.setDate(CommonUtil.nullToBlank(resource[5]));
			bean.setResType(CommonUtil.nullToBlank(resource[6]));
			bean.setResLibrary(CommonUtil.nullToBlank(resource[7]));
			bean.setSubjectClass(CommonUtil.nullToBlank(resource[8]));
			bean.setCoverUrl(CommonUtil.nullToBlank(resource[9]));
			bean.setDataType(ResMainBean.DATA_TYPE_RES);
			
			bean.setLanguage(getFormatLan(CommonUtil.nullToBlank(resource[13])));
			return bean;
		} catch (Exception e) {
			System.out.println("封装对象为lucene对象出错：" + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
	
	//获取格式化后的语种
	protected String getFormatLan(String lanStr){
		if(!CommonUtil.isNotNull(lanStr)){
			return "none";
		}
		lanStr = lanStr.toLowerCase();
		if(lanStr.equals("zh") || lanStr.equals("中文")){
			return "zh";
		}
		if(lanStr.equals("zw") || lanStr.equals("藏文")){
			return "zw";
		}
		if(lanStr.equals("mw") || lanStr.equals("蒙文")){
			return "mw";
		}
		if(lanStr.equals("en") || lanStr.equals("英文")){
			return "en";
		}
		return lanStr;
	}
	
	protected ResMainBean parseSrcFile(Long rsdaId, int viewType, String srcFile, String srcDir){
		String sourceTxt = getAllEleData(rsdaId); //全文
		ResMainBean bean = new ResMainBean();
		//传统全文预览方式
		if(viewType == 0){
			String resDir = SysConfigInfo.FILE_ROOT_DIR;
			File file = new File(resDir + srcFile);
			if (file.exists()) {
				String srcVal = CommonReader.readerFile(file.getPath());
				sourceTxt = sourceTxt + srcVal;
				bean.setSrcPath(file.getAbsolutePath());
				
				String extname = srcFile.substring(srcFile.lastIndexOf(".") + 1);
				extname = extname.toLowerCase();
				if(extname.equals("pdf")){
					List<ResPageBean> pageList = parsePdfPageList(rsdaId,file.getPath());
					bean.setPageList(pageList);
				}
			}
		}
				
		//纯图片预览文件：根据图片目录找到与文件夹名称相同的文件作为全文
		else if (viewType == 1 && srcDir != null){
			String pdfurl="";
			srcDir=srcDir.replaceAll("\\\\","/");
			if(srcDir.endsWith("/")){
				pdfurl=srcDir+srcDir.substring(srcDir.lastIndexOf("/",srcDir.lastIndexOf("/")-1)+1,srcDir.lastIndexOf("/"))+".pdf";
			}else{
				pdfurl=srcDir+srcDir.substring(srcDir.lastIndexOf("/"))+".pdf";
			}
			File file = new File(pdfurl);
			List<ResPageBean> pageList2 = new ArrayList<ResPageBean>();
			if(file.exists()){
				pageList2 = parsePdfPageList(rsdaId, pdfurl);
			}
			
			//如果没有源文全文，则按分页进行解析
			List<ResPageBean> pageList = parsePageList(rsdaId, srcDir);
			pageList.addAll(pageList2);
			bean.setPageList(pageList);
			bean.setSrcPath(srcDir);
			
			File srctxtFile = MyFileUtil.getSrctxtFileByDirname(srcDir);
			if(srctxtFile != null){
				String srcVal = IoCommonUtil.readTxt(srctxtFile.getAbsolutePath());
				sourceTxt += srcVal;
			}else{
				//全文合并分页的内容
				sourceTxt += pageTxtMerge(pageList);
			}
		}
		
		//viewType == 2： html格式和图片预览格式不做索引处理
		
		bean.setId(rsdaId);
		bean.setContent(sourceTxt);
		return bean;
	}
	/**
	 * 封装pdf的子页数据
	 * @author MaChao
	 * @date 2019-11-12
	 * @param rsdaId
	 * @param filePath
	 * @return
	 */
	private List<ResPageBean> parsePdfPageList(Long rsdaId,String filePath){
		//子页集合
		List<ResPageBean> pageList = new ArrayList<ResPageBean>();
		ResPageBean page = null;
		//pdf每页内容
		List<String> pageListContent=ReaderPdf.readerPdfs(filePath);
		
		if(pageListContent!=null&&pageListContent.size()>0L){
			for(int i=0;i<pageListContent.size();i++){
				String txt = Jsoup.parse(pageListContent.get(i)).text();
				//封装子页
				page = new ResPageBean();
				page.setId(IdGenerator.generateId());
				page.setResId(rsdaId);
				page.setContent(txt);
				page.setTitle((i+1)+"");
				page.setPageNum(i+1);
				page.setPageType(1);
				pageList.add(page);
			}
		}
		return pageList;
	}
	//封装子页数据
	private List<ResPageBean> parsePageList(Long rsdaId, String srcDir){
		//子页集合
		List<ResPageBean> pageList = new ArrayList<ResPageBean>();
		ResPageBean page = null;
		File f = null;
		
		//根据每页的全文创建索引
		List<File> txtList = MyFileUtil.getDirTxtFile(srcDir);
		
		//获取所有页面名称
		List<String> pageNameList = MyFileUtil.getPageNameList(srcDir);
		
		if(txtList != null && txtList.size() > 0){
			for(int i=0,s=txtList.size(); i<s; i++){
				f = txtList.get(i);
				String fname = f.getName();
				fname = fname.substring(0, fname.lastIndexOf("."));
				if(pageNameList.contains(fname) == false){
					continue;
				}
				
				String txt = IoCommonUtil.readTxt(f.getAbsolutePath());
				if(!CommonUtil.isNotNull(txt)){
					continue;
				}
				
				//根据该文件在所有页面中出现的顺序作为页码
				int pagenum = pageNameList.indexOf(fname);
				
				txt = Jsoup.parse(txt).text();
				
				//封装子页
				page = new ResPageBean();
				page.setId(IdGenerator.generateId());
				page.setResId(rsdaId);
				page.setContent(txt);
				page.setTitle(fname);
				page.setPageNum(pagenum+1);
				page.setPageType(2);
				pageList.add(page);
			}
		}
		
		//根据章节目录创建索引
		File xlsfile = MyFileUtil.getXlsByDirname(srcDir);
		if(xlsfile != null){
			try {
				List<CatalogBean> catalogList = MyFileCatalogUtil.getXlsCatalog(xlsfile);
				if(catalogList == null || catalogList.size() < 1){
					return pageList;
				}
				
				CatalogBean bean = null;
				for(int i=0,s=catalogList.size(); i<s; i++){
					bean = catalogList.get(i);
					if(!CommonUtil.isNotNull(bean.getTitle())){
						continue;
					}
					
					String pagename = bean.getPageName();
					if(pageNameList.contains(pagename) == false){
						continue;
					}
					
					//根据该文件在所有页面中出现的顺序作为页码
					int pagenum = pageNameList.indexOf(pagename);
					
					page = new ResPageBean();
					page.setId(IdGenerator.generateId());
					page.setResId(rsdaId);
					page.setContent(bean.getTitle());
					page.setTitle(pagename);
					page.setPageNum(pagenum + 1);
					page.setPageType(2);
					pageList.add(page);
				}
			} catch (BiffException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return pageList;
	}
	
	//合并分同的内容为一个大内容
	private String pageTxtMerge(List<ResPageBean> pageList){
		if(pageList == null || pageList.size() < 1){
			return "";
		}
		StringBuffer sb = new StringBuffer();
		for(ResPageBean page : pageList){
			sb.append(page.getContent());
		}
		String txt = sb.toString();
		sb = null;
		return txt;
	}
	
	
	/**
	 * 获取资源所有元数据值
	 *
	 * @author MaChao
	 * @date Oct 28, 2016
	 *
	 * @return
	 */
	protected String getAllEleData(Long rsdaId){
		String hql = "select reda.resElementPool.repoTitle, reda.redaData, reda.resElementPool.repoName from ResExtendData reda where reda.redaRsdaId=?";
		List<Object[]> list = getHibernateTemplate().find(hql, rsdaId);
		StringBuffer sb = new StringBuffer();
		for(Object[] arr : list){
			if(arr != null && arr.length == 3 && arr[1] != null && arr[1].toString().length() > 0){
				if(arr[2] != null && arr[2].toString().toLowerCase().equals("title")){
					continue;
				}
//				if(arr[0] != null){
//					sb.append(arr[0].toString()).append("：");
//				}
				sb.append(arr[1].toString()).append("；");
			}
		}
		String data = sb.toString();
		sb = null;
		return data;
	}
	
	/**
	 * 文本资源转化为索引数据对象
	 * @param res
	 * @return
	 */
	public ResMainBean textResToDatabean(ResTextResource res){
    	StringBuffer sb = new StringBuffer();
    	if(CommonUtil.isNotNull(res.getRtreCol1())){
    		sb.append("，"+res.getRtreCol1());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol2())){
    		sb.append("，"+res.getRtreCol2());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol3())){
    		sb.append("，"+res.getRtreCol3());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol4())){
    		sb.append("，"+res.getRtreCol4());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol5())){
    		sb.append("，"+res.getRtreCol5());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol6())){
    		sb.append("："+res.getRtreCol6());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol7())){
    		sb.append("，"+res.getRtreCol7());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol8())){
    		sb.append("，"+res.getRtreCol8());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol9())){
    		sb.append("，"+res.getRtreCol9());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol10())){
    		sb.append("，"+res.getRtreCol10());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol11())){
    		sb.append("，"+res.getRtreCol11());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol12())){
    		sb.append("，"+res.getRtreCol12());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol13())){
    		sb.append("，" + Jsoup.parse(res.getRtreCol13()).text());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol14())){
    		sb.append("，" + Jsoup.parse(res.getRtreCol14()).text());
    	}
    	if(CommonUtil.isNotNull(res.getRtreCol15())){
    		sb.append("，" + Jsoup.parse(res.getRtreCol15()).text());
    	}
    	String content = sb.toString();
    	sb = null;
    	
    	ResMainBean bean = new ResMainBean();
		bean.setId(res.getRtreId());
		bean.setTitle(CommonUtil.nullToBlank(res.getRtreTitle()));
		bean.setCreator("");
		bean.setPublisher("");
		bean.setKeyword("");
		bean.setDate("");
		bean.setResType("");
		if(res.getResTextLibrary() != null){
			bean.setResLibrary(res.getResTextLibrary().getRtliName());
		}else{
			bean.setResLibrary("");
		}
		
		if(res.getResLibraryClassify() != null && res.getResLibraryClassify().getRlclName() != null){
			bean.setSubjectClass(res.getResLibraryClassify().getRlclName());
		} else if (res.getRtreRlclId() != null && res.getRtreRlclId() > 0L){
			String hql = "select rlcl.rlclName from ResLibraryClassify rlcl where rlcl.rlclId=" + res.getRtreRlclId();
			String classname = getSingleData(hql);
			bean.setSubjectClass(classname);
		}else{
			bean.setSubjectClass("");
		}
		bean.setCoverUrl(res.getRtreCover());
		bean.setContent(content);
		bean.setDataType(ResMainBean.DATA_TYPE_TEXT);
		
		//文本库默认未设置语种
		bean.setLanguage(res.getRtreLanguage());
		return bean;
    }
	
	
	/**
	 * 将信息对象转换为索引数据对象
	 * @param info
	 * @return 
	 */
	public ResMainBean infoToDatabean(IdsInformation info){
		String content = Jsoup.parse(CommonUtil.nullToBlank(info.getIdsiContent())).text();
    	String classname = null;
    	if(info.getIdsInformationType() != null && info.getIdsInformationType().getIdstName() != null){
    		classname = info.getIdsInformationType().getIdstName();
    	}else if(info.getIdsiIdstId() != null && info.getIdsiIdstId() > 0L){
    		String hql = "select idst.idstName from IdsInformationType idst where idst.idstId=" + info.getIdsiIdstId();
    		classname = getSingleData(hql);
    	}else{
    		classname = "";
    	}
    	
    	ResMainBean bean = new ResMainBean();
		bean.setId(info.getIdsiId());
		bean.setTitle(CommonUtil.nullToBlank(info.getIdsiTitle()));
		bean.setCreator("");
		bean.setPublisher("");
		bean.setKeyword("");
		bean.setDate("");
		bean.setResType("");
		bean.setResLibrary("");
		bean.setSubjectClass(classname);
		bean.setCoverUrl(info.getIdsiImgUrl());
		bean.setContent(content);
		bean.setDataType(ResMainBean.DATA_TYPE_INFO);
		
		//信息默认未设置语种，如果有需要可以根据指定字段进行设置
		bean.setLanguage(info.getIdsiLanguage());
		return bean;
	}
}
