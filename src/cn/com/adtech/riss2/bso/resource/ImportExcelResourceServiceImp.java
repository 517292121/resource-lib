package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.web.bind.ServletRequestUtils;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.ExcelUtil;
import cn.com.adtech.riss2.core.LanguageUtil;
import cn.com.adtech.riss2.core.PinYin2Abbreviation;
import cn.com.adtech.riss2.core.ServiceUtil;
import cn.com.adtech.riss2.core.TempletConfig;
import cn.com.adtech.riss2.core.file.MyFileUtil;
import cn.com.adtech.riss2.domain.resource.ResDynamicType;
import cn.com.adtech.riss2.domain.resource.ResExtendData;
import cn.com.adtech.riss2.domain.resource.ResImportHistory;
import cn.com.adtech.riss2.domain.resource.ResImportTemplet;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.domain.resource.ResSourceLanguage;
import cn.com.adtech.riss2.domain.resource.ResSourceSubject;

import com.jbetter.hibernate.IdGenerator;

public class ImportExcelResourceServiceImp extends ImportExcelBasicServiceImp implements IImportExcleResourceService{
	
	public int importNumber = 0;
	private String returnMessage = "";
	private static String VIEW_REGEX = ".*\\.(?i)(swf|jpg|flv|mp4|mp3)";
	private final static int BATCH_SIZE = 500;
	

	
	public List<Object> importResource(File file, HttpServletRequest request,
			ResImportHistory importHistory, ResSourceData basicData) {
		
		FileExtMap = null;
		subjectTempMap = new HashMap<String, Long>();
		
		/**
		 * 源文类型
		 * 0：传统文件与swf预览方式
		 * 1：图片带章节目录、图文对照、子页检索、图片切片整合插件
		 * 2：名编辑方式
		 */
		int fileType = ServletRequestUtils.getIntParameter(request, "resFileType", 0);
		
		List<Object> resultList = new LinkedList<Object>();
		Long rihiId = null;
		if(importHistory != null){
			//保存导入批次记录
			importHistory.setRmhiAddress(getUrl(importHistory.getRmhiFilename(), importHistory.getRmhiLocalFilename()));
			Serializable s = getHibernateTemplate().save(importHistory);
			if(s == null){
				resultList.add("false");
				return resultList;
			}
			rihiId = Long.parseLong(s.toString());
		}
		int _importNumber = 0;
		//准备基础数据
		Long rminId = basicData.getRsdaRminId(); //作业ID
		Long rmstId = basicData.getRsdaRmstId(); //特色库ID
		Long rltyId = basicData.getRsdaRltyId(); //资源类型ID
		Long smanId = basicData.getRsdaSmanId(); //创建者ID
		Long rsdaCmtstatu = basicData.getRsdaCmtstus(); 
		Date date = new Date();
		
		String elementColumn, elementData = null, sql;
		int eleColumnIndex;
		
		//获取作业第一语种
		Long languageId = getLanguageId(rminId);
		
		//该作业下的资源主题目
		List<ResSourceSubject> sourceSubjectList = this.initSourceSubject(rminId);
		boolean isAdd = false;
		int checkSubject = ServletRequestUtils.getIntParameter(request, "checkSubject", 0);
		if(checkSubject > 0){
			//是否将不存在的主题自动添加到作业下，如果是则不做主题验证。
			isAdd = true;
			//如果不将不存在的主题保存，则要验证主题是否存在
		}else{
			isAdd = false;
		}
		
		//是否转换文件名
		int convertFilename = ServletRequestUtils.getIntParameter(request, "convertfileName", 0);
		
		//第四种图片预览格式导入时，临时存储所有资源文件目录，导入完成后统一执行切片
		List<String> imgDirList = new ArrayList<String>();
		
		//定义对象
		Transaction tx = null;
		Session session = null;
		jxl.Workbook rwb = null;
		ResExtendData reda = null;
		FileInputStream fis = null;
		ResSourceLanguage rsl = null;
		ResSourceData resource = null;
		StringBuffer messageBuffer = new StringBuffer();
		ResMetaStore metaStore = basicData.getResMetaStore();
		try {
			initData(metaStore, basicData.getResMissionInfo().getRminMissname(), fileType);
			initTypeTemplet(rmstId, rltyId);
			
			String _fileFolder = storeFolder;
			//学科分类缓存集合
			Map<String, Long> _subjectMap = this.subjectMap;
			
			//中图法分类集合
			List<String> _ztfList = this.ztfList;
			//文献编号集合
			//List<String> _wxNumberList = this.wxNumberList;
			
 			//自定义分类集合
//			Map<String, Long> _zdyMap = this.zdyMap;
			List<ResDynamicType> _zdyList = this.zdyList;
			
			//文件名集合
			List<String> _filenameList = this.filenameList;
			
			List<ResImportTemplet> _typeTempletList = this.templetTypeList;
			
//			Map<Long, String> eleMap = importTempletService.getTempletElement(rmstId, rltyId);
			List<ResImportTemplet> templetList = importTempletService.getTempletElementList(rmstId, rltyId);
			fis = new FileInputStream(file);
//			jxl.Workbook rwb = Workbook.getWorkbook(is);
			/**
			 * 遇到一些特殊字符，比如说海外的一些西语重音如á、é、í、ó和ú等在节目单导入之后显示不出来，出现乱码
			 * 改成以下方式，指定编码方式解析
			 */
			WorkbookSettings setting=new WorkbookSettings();
			setting.setEncoding("iso-8859-1"); 
			rwb = Workbook.getWorkbook(fis, setting);

			//获取第一张Sheet表
			Sheet rs = (Sheet) rwb.getSheet(0);
			int rows = rs.getRows();
//			int columns = rs.getColumns();
			long sourceId, elementId, myid ;
			String filename = null;
			
			//根目录文件，定义在外面避免循环获取（用户第二种图片预览格式导入）
			String imgRoot = ServletRequestUtils.getStringParameter(request, "imgRootDir", "");
			//资源主地址，定义在外面避免循环获取（用户第三种html预览格式导入）
			String rootUrl = ServletRequestUtils.getStringParameter(request, "fileRootUrl", "");
			//资源html文件名（用户第三种html预览格式导入）
			String indexHtml = ServletRequestUtils.getStringParameter(request, "indexHtml", "");
			
			int beginRow = ServletRequestUtils.getIntParameter(request, "beginRow", 2) - 1;
			String fileColumn = getTypeColumn(ResImportTemplet.RSDA_FILENAME_ID, templetTypeList);
			int fileColumnIndex = 0;
			if(CommonUtil.isNotNull(fileColumn)){
				fileColumnIndex = ExcelUtil.getColumnIndex(fileColumn);
			}
			
			// 打开新的连接
			session = this.getServiceHibernateTemplate().getSessionFactory().openSession();
			// 开起事务管理
			tx = session.beginTransaction();
			for(int i=beginRow; i<rows; i++){
				if(fileColumnIndex > 0){
					filename = rs.getCell(fileColumnIndex-1, i).getContents().trim();
				}else{
					filename = null;
				}
				resource = new ResSourceData();
				resource.setRsdaRminId(rminId);
				resource.setRsdaRmstId(rmstId);
				resource.setRsdaRltyId(rltyId);
				resource.setRsdaSmanId(smanId);
				resource.setRsdaRihiId(rihiId);
				resource.setRsdaRminStatus(basicData.getResMissionInfo().getRminStatus());
				resource.setRsdaRmstStatus(metaStore.getRmstStatus());
				resource.setRsdaCmtstus(rsdaCmtstatu); // 资源评论开关
				resource.setRsdaRmstStatus(metaStore.getRmstStatus());
				resource.setRsdaAuthortype(basicData.getRsdaAuthortype());
				resource.setRsdaSourceType(fileType);
				
				resource = parseBasicData(resource, date); //封装资源基本信息
				resource = parseTypesData(resource, rs, i, _subjectMap, _ztfList, _zdyList, _typeTempletList); //封装资源分类信息
				resource = parseElementData(resource, templetList, rs, i); //封装资源元数据信息
				//资源文件名
				String fname = filename == null ? resource.getRsdaTitle() : filename;

				if(fileType == 0){
					//资源为文件格式时，进行传统文件解析操作
					resource = parseResourceFile(resource, filename, _filenameList, _fileFolder,convertFilename, request);//配置资源文件
				} else if (fileType == 1){
					if(CommonUtil.isNotNull(imgRoot) && CommonUtil.isNotNull(fname)){
						//图片预览格式，与图文对照相同，只需要保存资源路径
						resource.setRsdaFileUrl(imgRoot+"/"+fname);
						
						//存储资源目录，导入后统一切片
						imgDirList.add(resource.getRsdaFileUrl());
					}
				} else if (fileType == 2){
					//html预览格式，保存html完成地址
					resource.setRsdaFileUrl(rootUrl + "/" + fname + "/" + indexHtml);
				}
				//解析资源所关联的主题
				Long rssuId = getSourceSubjectId(sourceSubjectList, rs, i, isAdd, rminId);
				resource.setRsdaRssuId(rssuId);
				
				session.save(resource);
				sourceId = resource.getRsdaId();

				// 保存资源对应语种记录
				rsl = new ResSourceLanguage();
				rsl.setResLanguageId(languageId);
				rsl.setResSourceId(sourceId);
				rsl.setRslaDate(new Date().toLocaleString());
				rsl.setRslaStatus(2L);
				rsl.setRslaUserId(smanId);
				rsl.setRslaIsfirst(true);
				rsl.setRslaAuthorName(MyRequestUtil.getSmanName(request));
				rsl.setRslaUsername(MyRequestUtil.getLoginUserName(request));
				session.save(rsl);
				
				//保存元素数据
				for(int j=0,js=templetList.size(); j<js; j++){
					elementId = templetList.get(j).getRiteRepoId();
					elementColumn = templetList.get(j).getRiteColumn();
					if(elementColumn != null && elementColumn.length() > 0){
						eleColumnIndex = ExcelUtil.getColumnIndex(elementColumn) - 1;
						try {
							elementData = rs.getCell(eleColumnIndex, i).getContents().trim();
						} catch (Exception e) {}
						
						// 解决ORA-01400: 无法将 NULL 插入 (RES_EXTEND_DATA.REDA_ID)异常问题
						if(StringUtils.isBlank(elementData)){
							continue;
						}
						
						/*
						 * 此方法截取太慢
						 * if(ServiceUtil.getStringRealLength(elementData) > 2999){
							elementData = ServiceUtil.getStringForTruncation(elementData, 2997);
							messageBuffer.append("<li>第" + (i+beginRow-1) + " 条数据，元素" + templetList.get(j).getElementName() + " 长度大于3000，超过3000个字符的已被截取</li>");
						}*/
						if(elementData.length() > 800){
							elementData = elementData.substring(0, 796)+"...";
							//messageBuffer.append("<li>第" + (i+beginRow-1) + " 条数据，元素" + templetList.get(j).getElementName() + " 长度大于3000，超过3000个字符的已被截取</li>");
						}
						
						reda = new ResExtendData();
						reda.setRedaRepoId(elementId);
						reda.setRedaRsdaId(sourceId);
						reda.setRedaLanguage(languageId);
						reda.setRedaData(elementData);
						session.save(reda);
					}
				}
				
				if(i%BATCH_SIZE == 0){
					session.flush();
					session.clear();
				}
				_importNumber++;
				System.out.println("已完成：" + (i+1) + "/" + rows);
			}
			session.flush();
			session.clear();
			tx.commit();
			resultList.add("true");
		} catch (Exception e) {
			e.printStackTrace();
			if(tx != null){
				tx.rollback();
			}
			if(importHistory != null){
				delete(importHistory);
			}
			_importNumber = 0;
			resultList.add("false");
		} finally{
			if(session != null){
				session.close();
			}
			if(rwb != null){
				rwb.close();
			}
			if(fis != null){
				try {
					fis.close();
				} catch (IOException e) {}
			}
			FileExtMap = null;
		}
		resultList.add(messageBuffer.toString());
		returnMessage += messageBuffer.toString();
		messageBuffer = null;
		this.importNumber = _importNumber;
		
		//将导入数量同步到作业资源总量里面
		if(_importNumber > 0 && rminId > 0L){
			String hql = "update ResMissionInfo rmin set rmin.rminResCount=rmin.rminResCount+" + _importNumber + " where rmin.rminId=?";
			getHibernateTemplate().bulkUpdate(hql, rminId);
		}
		
		//如果为图片格式，导入完成后做切片处理
		if (fileType == 1){
			imgSplitForImp(imgDirList);
		}
		return resultList;
	}
	
	private void imgSplitForImp(final List<String> imgDirList){
		if(imgDirList == null || imgDirList.size() < 1){
			return;
		}
		new Thread(){
			@Override
			public void run() {
				super.run();
				for(String dir : imgDirList){
					System.out.println(dir);
					//新开一个纯程对目录中的图片创建缩略图和切片
					MyFileUtil.createImgCover(dir);
				}
			}
		}.start();
	}
	
	public Long getLanguageId(Long rminId){
		if(rminId == null){
			return 1L;  //如果作业为空，则默认为简体中文(1)，目前主要针对导入采集数据
		}
		String hql = "select rmin.rminLanugage from ResMissionInfo rmin where rmin.rminId=" + rminId;
		List list =getHibernateTemplate().find(hql);
		if (list == null || list.size() < 1){
			return 1L; //默认为简体中文
		}else{
			Long id = Long.parseLong(list.get(0).toString());
			list = null;
			return id;
		}
	}
	
	//获取资源库语种类型
	private String getLibLanguage(Long rmstId){
		String hql = "select rmst.rmstLanguage from ResMetaStore rmst where rmst.rmstId=" + rmstId;
		String lan = getSingleData(hql);
		return lan;
	}
	
	/**封装资源基本信息**/
	public ResSourceData parseBasicData(ResSourceData source, Date date){
		source.setRsdaBuilddate(date);
		source.setRsdaStatus(2L); // 状态，默认为2
		source.setRsdaGrade(3L); // 评分，默认为3
		source.setRsdaRcmd(0L); // 是否为推荐资源，默认为0(不是)
		source.setRsdaIsuse(1L); // 是否可用，默认为1（可用）
		source.setRsdaLooknum(1L); // 浏览次数
//		source.setRsdaAuthortype(ResSourceData.AUTHORTYPE_ADMIN_IMPORT); //资源来源类型
		return source;
	}
	
	/**根据分类映射ID获取模板中设置的列对应的列*/
	private String getTypeColumn(long repoid, List<ResImportTemplet> templetTypeList){
		if(templetTypeList == null || templetTypeList.size() < 1){
			return null;
		}
		ResImportTemplet templet = null;
		String column = null;
		for(int i=0,s=templetTypeList.size(); i<s; i++){
			templet = templetTypeList.get(i);
			if(templet.getRiteRepoId() == repoid){
				column = templet.getRiteColumn();
				break;
			}
		}
		return column;
	}
	
	/** 封装资源三个分类信息* */
	public ResSourceData parseTypesData(ResSourceData resource,
			Sheet sheet, int rows,
			Map<String, Long> _subjectMap, List<String> _ztfList,
			List<ResDynamicType> _zdyList, List<ResImportTemplet> _templetTypeList) {
		
//		if(columns < 4){
//			return resource;
//		}
		String rstyName = "", rdtyName="", rmtyName="", excelColumn;
		Long rstyId, rdtyId;
		int columnIndex = 0;
		//设置学科分类
		excelColumn = getTypeColumn(ResImportTemplet.RSDA_RSTY_ID, templetTypeList);
		if(CommonUtil.isNotNull(excelColumn)){
			columnIndex = ExcelUtil.getColumnIndex(excelColumn);
			rstyName = sheet.getCell(columnIndex-1, rows).getContents().trim();
			rstyId = getSubjectTypeId(rstyName, _subjectMap);
			resource.setRsdaRstyId(rstyId);
		}
		
		//设置中图法分类
		excelColumn = getTypeColumn(ResImportTemplet.RSDA_MIDDLENAME_ID, templetTypeList);
		if(CommonUtil.isNotNull(excelColumn)){
			columnIndex = ExcelUtil.getColumnIndex(excelColumn);
			rmtyName = sheet.getCell(columnIndex-1, rows).getContents().trim();
			if(rmtyName != null){
				rmtyName = rmtyName.toUpperCase();
			}
		}
		
		//设置自定义分类一
		excelColumn = getTypeColumn(ResImportTemplet.RSDA_RDTY_ONE_ID, templetTypeList);
		if(CommonUtil.isNotNull(excelColumn)){
			columnIndex = ExcelUtil.getColumnIndex(excelColumn);
			rdtyName = sheet.getCell(columnIndex-1, rows).getContents().trim();
			rdtyId = getZdyType(rdtyName, _zdyList, 1);
			resource.setRsdaRdtyId(rdtyId);
		}
		
		//设置自定义分类二
		excelColumn = getTypeColumn(ResImportTemplet.RSDA_RDTY_TWO_ID, templetTypeList);
		if(CommonUtil.isNotNull(excelColumn)){
			columnIndex = ExcelUtil.getColumnIndex(excelColumn);
			rdtyName = sheet.getCell(columnIndex-1, rows).getContents().trim();
			Long rdtyId2 = getZdyType(rdtyName, _zdyList, 2);
			resource.setRsdaRdtyTwo(rdtyId2);
		}
		
		//设置自定义分类三
		excelColumn = getTypeColumn(ResImportTemplet.RSDA_RDTY_THREE_ID, templetTypeList);
		if(CommonUtil.isNotNull(excelColumn)){
			columnIndex = ExcelUtil.getColumnIndex(excelColumn);
			rdtyName = sheet.getCell(columnIndex-1, rows).getContents().trim();
			Long rdtyId3 = getZdyType(rdtyName, _zdyList, 3);
			resource.setRsdaRdtyThree(rdtyId3);
		}
		return resource;
	}
	
	
	
	//解析资源主题ID
	private Long getSourceSubjectId(List<ResSourceSubject> subjectList, Sheet sheet, int rows, boolean isAdd, Long rminId) {
		Long id = null;
		
		String excelColumn = getTypeColumn(ResImportTemplet.RSDA_RSSU_ID, templetTypeList);
		if(CommonUtil.isNotNull(excelColumn)){
			int columnIndex = ExcelUtil.getColumnIndex(excelColumn);
			String subjectName = sheet.getCell(columnIndex-1, rows).getContents().trim();
			if(CommonUtil.isNotNull(subjectName)){
				id = getSourceSubjectId(subjectName, subjectList);
				if(id == null && isAdd){
					if(subjectTempMap.containsKey(subjectName)){
						id = subjectTempMap.get(subjectName);
					}else{
						id = saveSourceSubject(rminId, subjectName);
						subjectTempMap.put(subjectName, id);
					}
				}
			}
		}
		return id;
	}
	
	private Long saveSourceSubject(Long rminId, String subjectName){
		ResSourceSubject sourceSubject = new ResSourceSubject();
		sourceSubject.setRssuName(subjectName);
		sourceSubject.setRssuRminId(rminId);
		try {
			Serializable s = getHibernateTemplate().save(sourceSubject);
			if(s != null){
				return (Long) s;
			}else{
				return null;
			}
		} catch (Exception e) {
			logger.error(e);
			return null;
		} 
	}
	
	/**封装资源十五个核心元素基本信息 增加了一个关键词**/
	public ResSourceData parseElementData(ResSourceData resource, List<ResImportTemplet> templetList, Sheet sheet, int rows){
		ResImportTemplet element = null;
		String eleColumn = "";
		String elename = "";
		int cellIndex;
		String celldata = "";
		for(int i = 0, s = templetList.size(); i < s; i++) {
			element = templetList.get(i);
			elename = element.getElementName().toLowerCase();
			eleColumn = element.getRiteColumn();
			cellIndex = ExcelUtil.getColumnIndex(eleColumn) -1;
			if(cellIndex < 0){
				celldata = "";
			}else{
				celldata = sheet.getCell(cellIndex, rows).getContents().trim();
				if(celldata.length() > 500){
					celldata = celldata.substring(0, 450)+"...";
				}
				/*if(ServiceUtil.getStringRealLength(celldata) > 3199){
					celldata = ServiceUtil.getStringForTruncation(celldata, 3195) + "...";
				}*/
			}
			if(elename.equals("title")) {
				//同步资源题名首字母
				String en = PinYin2Abbreviation.cn2py(celldata);
				resource.setRsdaTitleEn(en);
				
				resource.setRsdaTitle(celldata);
			}
			if(elename.equals("creator")) {
				resource.setRsdaCreator(celldata);
			}
			if(elename.equals("subject")) {
				resource.setRsdaSubject(celldata);
			}
			if(elename.equals("description")) {
				resource.setRsdaDescription(celldata);
			}
			if(elename.equals("publisher")) {
				resource.setRsdaPublisher(celldata);
			}
			if(elename.equals("contributor")) {
				resource.setRsdaContributor(celldata);
			}
			if(elename.equals("created")) {
				if(resource.getRsdaDate() == null){
					resource.setRsdaDate(celldata);
				}
			}
			if(elename.equals("date")){
				resource.setRsdaDate(celldata);
			}
			if(elename.equals("type")) {
				resource.setRsdaType(celldata);
			}
			if(elename.equals("format")) {
				resource.setRsdaFormat(celldata);
			}
			if(elename.equals("identifier")) {
				resource.setRsdaIdentifier(celldata);
			}
			if(elename.equals("source")) {
				resource.setRsdaSource(celldata);
			}
			if(elename.equals("language")) {
				resource.setRsdaLanguage(celldata);
			}
			if(elename.equals("relation")) {
				resource.setRsdaRelation(celldata);
			}
			if(elename.equals("coverage")) {
				resource.setRsdaCoverage(celldata);
			}
			if(elename.equals("rights")) {
				resource.setRsdaRights(celldata);
			}
			if(elename.equals("keyword")) {
				resource.setRsdaKeyword(celldata);
			}
		}
		return resource;
	}
	
	
	/**封装资源的文件及其路径
	 * 
	 * 资源文件命名规则
	 *  封面
	 *	  文件名：     题名 + _jpg.
	 *	
	 *	源文件
	 *	  原文件名：   题名 + _data_s.
	 *	  转换文件名： 题名 + _data_c.
	 *	
	 *	预览文件：
	 *	  原文件名：   题名 + _pre_s.
	 *	  转换文件名： 题名 + _pre_c.
	 * 
	 * **/
	public ResSourceData parseResourceFile(ResSourceData resource,
			String filename, List<String> _filenameList, String _fileFolder,
			int convertFilename, HttpServletRequest request) {
		String title = "";
		String sourceFilename;
		String sourceSwfname;
		String sourceImage;
		String extName;
		String preFolder = "";
		
		boolean ignoreSoufile = ServletRequestUtils.getIntParameter(request, "exitSourcefile", 0) == 1 ? true : false;
		boolean ignoreImgfile = ServletRequestUtils.getIntParameter(request, "exitFaceimg", 0) == 1 ? true : false;
		String souFormat = ServletRequestUtils.getStringParameter(request, "sourceFormat", "");
		String vieFormat = ServletRequestUtils.getStringParameter(request, "viewFormat", "");
		
		if(filename != null && filename.trim().length() > 0){
			title = filename.trim();
		}else{
			title = resource.getRsdaTitle();
		}
		sourceFilename = title + "_data_s";
		sourceSwfname = title + "_data_c";
		sourceImage = title + "_jpg.jpg";
		preFolder = _fileFolder + "/";
		
		// word文件在转换时需要将文件名中的“替换为『，”替换为』，否则会转换不成功。hasChangeFileName参数目前仅表示如果源文件为doc文件，则要转换源文件生成文件swf。
		Boolean hasChangeFileName = false;
		//资源源文件原文件
		extName = containFilename(sourceFilename, _filenameList);
		String codeName = "";
		String fileFolder = "";
		//如果需要转换文件名
		if(convertFilename == 1){
			codeName = getMathFilename();
			fileFolder = SysConfigInfo.RES_DIR + storeFolder + "/";
		}
		if(extName != null && extName.length() > 0){
			// 根据文件路径获取文件大小
			String fileUrl = SysConfigInfo.FILE_ROOT_DIR + preFolder + sourceFilename + "." + extName;
			File file = new File(fileUrl);
			if(file.exists()){
				resource.setRsdaFileSize(file.length());
			}
			if(extName.toLowerCase().contains("doc")){
				sourceFilename = sourceFilename.replace("“", "『").replace("”", "』");
				hasChangeFileName = true;
			}
			sourceFilename += "." + extName;
			resource.setRsdaFileName(title + "." + extName);
			if(convertFilename == 1){
				boolean b = fileRename(fileFolder + sourceFilename, codeName);
				if(b){
					resource.setRsdaFileLocalName(preFolder + codeName + "." + extName);
				}else{
					resource.setRsdaFileLocalName(preFolder + sourceFilename);
				}
			}else{
				resource.setRsdaFileLocalName(preFolder + sourceFilename);
			}
			resource.setRsdaFileUrl(getUrl(resource.getRsdaFileLocalName(), sourceFilename));
			resource.setRsdaFileType(extName.toLowerCase());
		}else{
			if(ignoreSoufile){
				resource.setRsdaFileName(title + "." + souFormat);
				resource.setRsdaFileLocalName(preFolder + sourceFilename + "." + souFormat);
				resource.setRsdaFileUrl(getUrl(resource.getRsdaFileLocalName(), sourceFilename));
				resource.setRsdaFileType(souFormat.toLowerCase());
			}
		}
		//源文件转换文件
		extName = containFilename(sourceSwfname, _filenameList);
		if(extName != null && extName.length() > 0){
			if(hasChangeFileName){
				sourceSwfname = sourceSwfname.replace("“", "『").replace("”", "』");
			}
			sourceSwfname += "." + extName;
			String name = getMathFilename();
			if(convertFilename == 1){
				boolean b = fileRename(fileFolder + sourceSwfname, name);
				if(b){
					resource.setRsdaFileSwfUrl(getUrl(preFolder + name + "." + extName, sourceSwfname));
				}else{
					resource.setRsdaFileSwfUrl(getUrl(preFolder + sourceSwfname, sourceSwfname));
				}
			}else{
				resource.setRsdaFileSwfUrl(getUrl(preFolder + sourceSwfname, sourceSwfname));
			}
			
			//如果没有原文件
			if(CommonUtil.isNotNull(resource.getRsdaFileName()) == false){
				if(extName.equals("flv") || extName.equals("mp3")){
					resource.setRsdaFileName(title + "." + extName);
					if(convertFilename == 1){
						resource.setRsdaFileLocalName(preFolder + name);
					}else{
						resource.setRsdaFileLocalName(preFolder + sourceSwfname);
					}
					
					resource.setRsdaFileUrl(getUrl(resource.getRsdaFileSwfUrl(), sourceSwfname));
					resource.setRsdaFileType(extName.toLowerCase());
				}
				
			}
		}else{
			if(ignoreSoufile){
				resource.setRsdaFileSwfUrl(getUrl(preFolder + sourceSwfname + "." + vieFormat, sourceSwfname));
			}
		}
		
		//封面
		if(_filenameList.contains(sourceImage)){
			resource.setRsdaBigimgName(title + ".jpg");	
			if(convertFilename == 1){
				String codename = getMathFilename();
				boolean b = fileRename(fileFolder + sourceImage, codename);
				if(b){
					resource.setRsdaBigimgLocalName(preFolder + codename + ".jpg");
				}else{
					resource.setRsdaBigimgLocalName(preFolder + sourceImage);
				}
			}else{
				resource.setRsdaBigimgLocalName(preFolder + sourceImage);
			}
			resource.setRsdaBigimgUrl(getUrl(resource.getRsdaBigimgLocalName(), sourceImage));
		}else{
			if(ignoreImgfile){
				resource.setRsdaBigimgName(title + ".jpg");
				resource.setRsdaBigimgLocalName(preFolder + sourceImage);
				resource.setRsdaBigimgUrl(getUrl(resource.getRsdaBigimgLocalName(), sourceImage));
			}
		}
		return resource;
	}
	
	//保存资源附件
	private void saveSourceFile(String filename, Long rsdaId){
		Map<String, String> fileMap = parseAttachmentFile(filename);
		if(fileMap == null || fileMap.size() < 1){
			return;
		}
		
		ResSourceFile sfile = null;
		List<ResSourceFile> fileList = new ArrayList<ResSourceFile>();
		
		int index = 1;
		for(Map.Entry<String, String> mm : fileMap.entrySet()) {
			sfile = new ResSourceFile();
			
			sfile.setResFileName(mm.getKey());
			sfile.setResFileAccessType("DOWNLOAD");  //权限，默认为最高权限
			sfile.setResLocalPath(storeFolder + "/" + filename + "/" + mm.getKey());
			sfile.setResFileRemark(null);
			sfile.setResIndex(index);
			sfile.setResFilePath(getUrl(sfile.getResLocalPath(), mm.getKey()));
			sfile.setResRsdaId(rsdaId);
			if(CommonUtil.isNotNull(mm.getValue())){
				sfile.setResViewPath(getUrl(storeFolder + "/" + filename + "/" + mm.getValue(), mm.getKey()));
			}
			
			fileList.add(sfile);
            index++;
            save(sfile);
		}
	}
	
	public Map<String, String> parseAttachmentFile(String filename) {
		File dir = new File(SysConfigInfo.RES_DIR + "/" + storeFolder + "/" + filename);
		if(!dir.exists()){
			return null;
		}
		if(!dir.isDirectory()){
			return null;
		}
		//附件源文件，根据扩展名识别其中几种格式的文件
		Map<String, String> sourceMap = new HashMap<String, String>();
		
		File[] fileArr = dir.listFiles();
		List<File> sourceList = Arrays.asList(fileArr);
		List<File> viewList = new ArrayList<File>();
		
		String fname = null;
		for(File file : sourceList){
			fname = file.getName();
			if(fname.indexOf(".") > 0){
				if(!fname.matches(VIEW_REGEX)){
					sourceMap.put(fname.substring(0, fname.lastIndexOf(".")), fname);
				}else{
					viewList.add(file);
				}
			}
		}
		sourceList = null;
		
		Map<String, String> fileMap = new HashMap<String, String>();
		for(File file : viewList){
			fname = file.getName();
			String name = fname.substring(0, fname.lastIndexOf("."));
			if(sourceMap.containsKey(name)){
				String f = sourceMap.get(name);
				fileMap.put(f, fname);
				sourceMap.remove(name);
			}else{
				fileMap.put(fname, null);
			}
		}
		
		Collection<String> others = sourceMap.values();
		for(String s : others){
			fileMap.put(s, null);
		}
		
		return fileMap;
	}
	
	/**验证分类信息**/
	public boolean checkTypes(HttpServletRequest request, ResMissionInfo mission, File file){
		StringBuffer messageBuffer = new StringBuffer();
		
		int fileType = ServletRequestUtils.getIntParameter(request, "resFileType", 0);
		
		String excelColumn = "";
		int columnIndex = 0 ;
		boolean result = true;
		try {
			InputStream is = new FileInputStream(file);
			jxl.Workbook rwb = Workbook.getWorkbook(is);

			Long rstyId, rdtyId, rssuId;
			String rstyName = "", rdtyName="", rmtyName="";
			//获取第一张Sheet表
			Sheet rs = (Sheet) rwb.getSheet(0);
			int rows = rs.getRows();
			int columns = rs.getColumns();
			
			int templetCols = importTempletService.getTempletNumber(mission.getRminRmstId(), mission.getRminRltyId());
			if(columns < templetCols){
				messageBuffer.append("文件中的列数低于模板中设置的列！");
				returnMessage = messageBuffer.toString();
				return false;
			}
			
			initData(mission.getResMetaStore(), mission.getRminMissname(), fileType);
			initTypeTemplet(mission.getRminRmstId(), mission.getRminRltyId());
			
			List<ResSourceSubject> sourceSubject = null;
			boolean isCheckSubject = true;
			int checkSubject = ServletRequestUtils.getIntParameter(request, "checkSubject", 0);
			if(checkSubject > 0){
				//是否将不存在的主题自动添加到作业下，如果是则不做主题验证。
				isCheckSubject = false;
			}else{
				//如果不将不存在的主题保存，则要验证主题是否存在
				sourceSubject = this.initSourceSubject(mission.getRminId());
			}
			
			//学科分类缓存集合
			Map<String, Long> _subjectMap = this.initSubjectType();
			
			//中图法分类集合
			List<String> _ztfList = this.initZTFtype();
			
				//自定义分类集合
			List<ResDynamicType> _zdyList = this.initZdyType(mission.getRminRmstId());
			
			int beginRow = ServletRequestUtils.getIntParameter(request, "beginRow", 3) - 1;
			
			
			if(rows <= beginRow){
				messageBuffer.append("文件中数据总行数低于起始行数！");
				returnMessage = messageBuffer.toString();
				return false;
			}
//			if(columns < 4){
//				messageBuffer.append("该数据文件不是有效的文件！");
//				returnMessage = messageBuffer.toString();
//				return false;
//			}
			for(int i=beginRow; i<rows; i++){
				
				//学科分类
				excelColumn = getTypeColumn(ResImportTemplet.RSDA_RSTY_ID, templetTypeList);
				if(CommonUtil.isNotNull(excelColumn)){
					columnIndex = ExcelUtil.getColumnIndex(excelColumn);
					rstyName = rs.getCell(columnIndex - 1, i).getContents().trim();
					rstyId = getSubjectTypeId(rstyName, _subjectMap);
					if(rstyId == null && rstyName.length() > 0){
						messageBuffer.append("<li>第：" + (i+beginRow-1) + " 行数据学科分类不存在！</li>");
						result = false;
					}
				}
				
				//中图法分类
				excelColumn = getTypeColumn(ResImportTemplet.RSDA_MIDDLENAME_ID, templetTypeList);
				if(CommonUtil.isNotNull(excelColumn)){
					columnIndex = ExcelUtil.getColumnIndex(excelColumn);
					rmtyName = rs.getCell(columnIndex - 1, i).getContents().trim();
					if(rmtyName != null){
						rmtyName = rmtyName.toUpperCase();
					}
					if(_ztfList.contains(rmtyName) == false && rmtyName.length() > 0){
						messageBuffer.append("<li>第：" + (i+beginRow-1) + " 行数据中图法分类不存在！</li>");
						result = false;
					}
				}
				
				//自定义分类一
				excelColumn = getTypeColumn(ResImportTemplet.RSDA_RDTY_ONE_ID, templetTypeList);
				if(CommonUtil.isNotNull(excelColumn)){
					columnIndex = ExcelUtil.getColumnIndex(excelColumn);
					rdtyName = rs.getCell(columnIndex - 1, i).getContents().trim();
					rdtyId = getZdyType(rdtyName, _zdyList, 1);
					if(rdtyId == null && rdtyName.length() > 0){
						messageBuffer.append("<li>第：" + (i+beginRow-1) + " 行数据"+ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE)+"不存在！</li>");
						result = false;
					}
				}
				
				//自定义分类二
				excelColumn = getTypeColumn(ResImportTemplet.RSDA_RDTY_TWO_ID, templetTypeList);
				if(CommonUtil.isNotNull(excelColumn)){
					columnIndex = ExcelUtil.getColumnIndex(excelColumn);
					String rdtyName2 = rs.getCell(columnIndex - 1, i).getContents().trim();
					rdtyId = getZdyType(rdtyName2, _zdyList, 2);
					if(rdtyId == null && rdtyName2.length() > 0){
						messageBuffer.append("<li>第：" + (i+beginRow-1) + " 行数据"+ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO)+"不存在！</li>");
						result = false;
					}
				}
				
				//自定义分类三
				excelColumn = getTypeColumn(ResImportTemplet.RSDA_RDTY_THREE_ID, templetTypeList);
				if(CommonUtil.isNotNull(excelColumn)){
					columnIndex = ExcelUtil.getColumnIndex(excelColumn);
					String rdtyName3 = rs.getCell(columnIndex - 1, i).getContents().trim();
					rdtyId = getZdyType(rdtyName3, _zdyList, 3);
					if(rdtyId == null && rdtyName3.length() > 0){
						messageBuffer.append("<li>第：" + (i+beginRow-1) + " 行数据"+ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE)+"不存在！</li>");
						result = false;
					}
				}
				
				//主题
				if(isCheckSubject){
					excelColumn = getTypeColumn(ResImportTemplet.RSDA_RSSU_ID, templetTypeList);
					if(CommonUtil.isNotNull(excelColumn)){
						columnIndex = ExcelUtil.getColumnIndex(excelColumn);
						String subjectName = rs.getCell(columnIndex - 1, i).getContents().trim();
						if(CommonUtil.isNotNull(subjectName)){
							rssuId = getSourceSubjectId(subjectName, sourceSubject);
							if(rssuId == null ){
								messageBuffer.append("<li>第：" + (i+beginRow-1) + " 行数据中资源主题不存在！</li>");
								result = false;
							}
						}
					}
				}
			}
			rwb.close();
		} catch (Exception e) {
			e.printStackTrace();
			messageBuffer.append("<li>在效验资源分类时发生错误："+e.getMessage()+"</li>");
			result = false;
		}
		if(!result){
			String filename = SysConfigInfo.IMP_EXCEL_DIR + file.getName();
			this.uploadService.removeFile(filename);
		}
		returnMessage = messageBuffer.toString();
		messageBuffer = null;
		return result;
	}

	/**
	 * 验证是否设置了模板
	 * @param rmstId 特色库ID
	 * @param rltyId 资源类型ID
	 * @return
	 */
	public boolean checkTemplet(long rmstId, long rltyId){
		List<ResImportTemplet> templetList = importTempletService.getTempletElementList(rmstId, rltyId);
		if(templetList == null || templetList.size()<1){
			return false;
		}else{
			return true;
		}
	}
	
	/**验证必备元素**/
	public boolean checkMustElement(HttpServletRequest request, File file, ResMissionInfo mission){
		boolean result = true;
		StringBuffer messageBuffer = new StringBuffer();
		long rmstId = mission.getRminRmstId();
		long rltyId = mission.getRminRltyId();
		List<ResImportTemplet> templetList = importTempletService.getTempletElementList(rmstId, rltyId);
		
		try {
			InputStream is = new FileInputStream(file);
			jxl.Workbook rwb = Workbook.getWorkbook(is);
			int eleColumnIndex;
			String elementColumn;
			String elementData;

			//获取第一张Sheet表
			Sheet rs = (Sheet) rwb.getSheet(0);
			int rows = rs.getRows();
			ResImportTemplet element = null;
			int beginRow = ServletRequestUtils.getIntParameter(request, "beginRow", 3) - 1;
			for(int i=beginRow; i<rows; i++){
				for(int j=0,js=templetList.size(); j<js; j++){
					element = templetList.get(j);
					elementColumn = element.getRiteColumn();
					if(elementColumn != null && elementColumn.length() > 0){
						eleColumnIndex = ExcelUtil.getColumnIndex(elementColumn) - 1;
						try {
							elementData = rs.getCell(eleColumnIndex, i).getContents().trim();
						} catch (Exception e) {
							elementData = "";
						}
//						if(ServiceUtil.getStringRealLength(elementData.length() > 1000){
//							messageBuffer.append("<li>第" + (i+beginRow-1) + " 条数据，元素'" + templetList.get(j).getElementTitle() + "(" + templetList.get(j).getElementName() + ")' 长度大于1000，保存失败</li>");
//							result = false;
//						}
						if(element.getRepoRestrict().equals("M") && (elementData == null || elementData.length() < 1)){
							messageBuffer.append("<li>第" + (i+beginRow-1) + " 条数据，元素'" + templetList.get(j).getElementTitle() + "(" + templetList.get(j).getElementName() + ")' 为必备元素，但未找到数据</li>");
							result = false;
						}
						elementData = "";
					}
				}
			}
			rwb.close();
		}catch (Exception e) {
			e.printStackTrace();
			messageBuffer.append("<li>在效验元素值时发生错误："+e.getMessage()+"</li>");
			result = false;
		}
		if(!result){
//			String filename = SysConfigInfo.IMP_EXCEL_DIR + file.getName();
//			this.uploadService.removeFile(filename);
		}
		returnMessage += messageBuffer.toString();
		messageBuffer = null;
		return result;
	}

	public int getImportNumber() {
		return this.importNumber;
	}

	public String getMessage() {
		return returnMessage;
	}
}

















