package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

import cn.com.adtech.riss2.bso.CommonServiceImp;
import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.core.CommonUtil;
import cn.com.adtech.riss2.domain.resource.ResLiterType;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.domain.resource.ResSourceData;
import cn.com.adtech.riss2.domain.resource.ResSourceFile;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

import com.jbetter.framework.session.Pagination;

public class ExportResourceServiceImp extends CommonServiceImp implements IExportResourceService{
	
	//导出时附件备份目录
	private static String attDir = null;
	private static String rootPath = "";
	public ExportResourceServiceImp(){
//		try {
//			if(!CommonUtil.isNotNull(attDir)){
//				attDir = ToolUnit.getProperties("/config.properties", "exp.attach.dir");
//			}
//			if(!CommonUtil.isNotNull(rootPath)){
//				rootPath = ToolUnit.getProperties("/config.properties", "downloadPath");
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		rootPath = SysConfigInfo.RES_DIR;
		
		if(CommonUtil.isNotNull(attDir)){
			File f = new File(attDir);
			if(!f.exists()){
				f.mkdirs();
			}
		}
	}
	
	
	private String parseFromHql(Long rmstId, Long rltyId, Map<String, Object> map, 
			String sort, String dir, List<Object> params){
		
		StringBuffer sb = new StringBuffer();
		sb.append(" from ResSourceData rsda");
		sb.append(" where rsda.rsdaStatus=").append(ResSourceData.STATUS_PASS);
		
		if(rmstId != null && rmstId > 0){
			sb.append(" and rsda.rsdaRmstId = ?");
			params.add(rmstId);
		}
		if(rltyId != null && rltyId > 0){
			sb.append(" and rsda.rsdaRltyId = ?");
			params.add(rltyId);
		}
		if(map.containsKey("rsdaTitle")){
			sb.append(" and rsda.rsdaTitle like ?");
			params.add("%" + map.get("rsdaTitle") + "%");
		}
		if(map.containsKey("beginDate")){
			sb.append(" and rsda.rsdaBuilddate >= ?");
			params.add(map.get("beginDate"));
		}
		if(map.containsKey("endDate")){
			sb.append(" and rsda.rsdaBuilddate <= ?");
			params.add(map.get("endDate"));
		}
		sb.append(" order by rsda.").append(sort).append(" ").append(dir);
		
		String hql = sb.toString();
		sb = null;
		return hql;
	}
	
	/**
	 * 获取资源列表
	 *
	 * @author MaChao
	 * @date Nov 4, 2016
	 *
	 * @param p  分页对象
	 * @param rmstId  特色库ID 
	 * @param rltyId  资源类型ID
	 * @param map  其它检索参数
	 * @param sort
	 * @param dir
	 * @return
	 */
	@Override
	public List<Object[]> getResourceList(Pagination p, Long rmstId,
			Long rltyId, Map<String, Object> map, String sort, String dir) {
		StringBuffer sb = new StringBuffer();
		sb.append("select rsda.rsdaId,");
		sb.append("rsda.rsdaTitle,");
		sb.append("rsda.resMetaStore.rmstName,");
		sb.append("rsda.resLiterType.rltyName,");
		sb.append("rsda.rsdaBuilddate");
		
		List<Object> params = new ArrayList<Object>();
		String from = parseFromHql(rmstId, rltyId, map, sort, dir, params);
		sb.append(from);
		String hql = sb.toString();
		
		List<Object[]> list = null;
		if(params.size() > 0){
			Object[] arr = params.toArray();
			params = null;
			list = getPageHibernateTemplate().find(p, hql, arr);
		}else{
			list = getPageHibernateTemplate().find(p, hql);
		}
		
		return list;
	}
	
	/**
	 * 获取所有资源ID集合
	 *
	 * @author MaChao
	 * @date Nov 7, 2016
	 *
	 * @param rmstId
	 * @param rltyId
	 * @param map
	 * @param sort
	 * @param dir
	 * @return
	 */
	private List<Object[]> getResList(Long rmstId,
			Long rltyId, Map<String, Object> map, String sort, String dir){
		
		StringBuffer sb = new StringBuffer();
		sb.append("select rsda.rsdaId,");
		sb.append("rsda.rsdaRstyId,");
		sb.append("rsda.rsdaRdtyId,");
		sb.append("rsda.rsdaRdtyTwo,");
		sb.append("rsda.rsdaRdtyThree,");
		sb.append("rsda.rsdaFileLocalName ");
		
		List<Object> params = new ArrayList<Object>();
		String from = parseFromHql(rmstId, rltyId, map, sort, dir, params);
		sb.append(from);
		String hql = sb.toString();
		
		List<Object[]> list = null;
		if(params.size() > 0){
			Object[] arr = params.toArray();
			params = null;
			list = getHibernateTemplate().find(hql, arr);
		}else{
			list = getHibernateTemplate().find(hql);
		}
		
//		if(list == null || list.size() < 1){
//			return null;
//		}
		
		//将检索结果的对象数组转换为Long数组，方便后面直接使用
//		List<Long[]> result = new ArrayList<Long[]>();
//		Long[] temp = null;
//		for(int i=0,s=list.size(); i<s; i++){
//			Object[] arr = list.get(i);
//			temp = new Long[]{
//						(Long)arr[0], 
//						arr[1] == null ? null : (Long)arr[1],
//						arr[2] == null ? null : (Long)arr[2],
//						arr[3] == null ? null : (Long)arr[3],
//						arr[4] == null ? null : (Long)arr[4]};
//			result.add(temp);
//		}
//		list = null;
		return list;
	}
	
	private List<Object[]> getElementTemplet(Long rmstId, Long rltyId){
		String hql = "select rtme.rtmeRepoId," +
				"rtme.resElementPool.repoTitle," +
				"rtme.resElementPool.repoParent," +
				"rtme.resElementPool.repoType " +
				"from ResTempletMeta rtme where rtme.rtmeRsinId=? and rtme.rtmeRltyId=? " +
				"order by rtme.resElementPool.repoIndex ASC";
		
		
		Long[] values = {rmstId, rltyId};
		List<Object[]> list = getHibernateTemplate().find(hql, values);
		
		if(list == null || list.size() < 1){
			return null;
		}
		
		//对元数据的顺序按资源录入时的格式重新组合
		List<Object[]> result = new ArrayList<Object[]>();
		Object[] temp = null;
		for(int i=0,s=list.size(); i<s; i++){
			temp = list.get(i);
			if((Long) temp[3] == 1L){ //第一次遍历15个核心元数据
				result.add(temp);
				for(int j=0; j<s; j++){
					if(list.get(j)[2].equals(temp[0])){
						result.add(list.get(j));
					}
				}
			}
		}
		for(int i=0,s=list.size(); i<s; i++){
			temp = list.get(i);
			if((Long) temp[3] == 2L){ //第二次遍历自定义元数据
				result.add(temp);
				for(int j=0; j<s; j++){
					if(list.get(j)[2].equals(temp[0])){
						result.add(list.get(j));
					}
				}
			}
		}
		return result;
	}

	/**
	 * 资源导出
	 *
	 * @author MaChao
	 * @date Nov 7, 2016
	 *
	 * @param outStream
	 * @param map
	 * @throws IOException 
	 */
	@Override
	public void exportToExcel(OutputStream outStream, Map<String, Object> map) throws IOException {
		Long rmstId = (Long) map.get("rmstId"); //特色库ID  *必须
		Long rltyId = (Long) map.get("rltyId"); //资源类型ID  *必须
		String sort = map.get("sort").toString();
		String dir = map.get("dir").toString();
		
		List<Object[]> reslist = getResList(rmstId, rltyId, map, sort, dir);
		List<Object[]> elelist = getElementTemplet(rmstId, rltyId);
		if(reslist == null || reslist.size() < 1){
			return;
		}
		if(elelist == null || elelist.size() < 1){
			return;
		}
		int rows = reslist.size(); //总行数》资源总数
		int cols = elelist.size(); //总列数》元数据模板个数
		
		//特色库对象
		ResMetaStore metaStore = (ResMetaStore) get(ResMetaStore.class, rmstId);
		//资源类型对象
		ResLiterType literType = (ResLiterType) get(ResLiterType.class, rltyId);
		
		//表格创建开始
		HSSFWorkbook workbook = new HSSFWorkbook(); // 创建对Excel工作簿文件的引用
		HSSFCell cell = null;
		HSSFSheet sheet = workbook.createSheet("工作簿1"); // 创建第一张工作表，本方法只支持一张工作表
	
		// 第一行，显示大标题或者是提示信息
		HSSFRow infoRow = sheet.createRow(0);
		infoRow.setHeight((short)0x280);// 设置行高
		HSSFFont infoFont = workbook.createFont();// 设置字体
		infoFont.setBoldweight((short)1000);// 设置字体类型
		infoFont.setFontHeightInPoints((short)20);// 设置字体大小
		HSSFCellStyle infoStyle = workbook.createCellStyle();// 添加字体样式
		infoStyle.setFont(infoFont);
		infoStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 设置对齐方式
		// 设置单元格边框
		infoStyle.setBorderLeft((short)1);
		infoStyle.setBorderRight((short)1);
		infoStyle.setBorderTop((short)1);
		infoStyle.setBorderBottom((short)1);
		
		for(int i = 0; i < cols; i++) {
			cell = infoRow.createCell(i);
			cell.setCellStyle(infoStyle);
			if(i == 0){
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				
				HSSFRichTextString hstr = new HSSFRichTextString(metaStore.getRmstName() + "——" + literType.getRltyName());
				cell.setCellValue(hstr);
			}
		}
		
		//合并第一行标题列，加的8是三个自定义分类加学科分类的名称及分类号
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, cols + 9 - 1));
		
		//第二行，添加标题
		HSSFRow titleRow = sheet.createRow(1);
		titleRow.setHeight((short) 0x180);// 设置行高
		HSSFFont titlefont = workbook.createFont();// 设置字体
		titlefont.setColor(HSSFFont.COLOR_NORMAL);// 设置字体颜色
		titlefont.setBoldweight((short)1000);// 设置字体类型
		
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFont(titlefont);// 添加字体样式
		titleStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 设置对齐方式
		// 设置单元格边框
		titleStyle.setBorderBottom((short)1);
		titleStyle.setBorderLeft((short)1);
		titleStyle.setBorderRight((short)1);
		titleStyle.setBorderTop((short)1);
		
		for (int i = 0; i < cols; i++) {
			cell = titleRow.createCell(i);
			cell.setCellStyle(titleStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(ToolUnit.NullToBlank(elelist.get(i)[1].toString())));
		}
		
		//添加学科分类及三种自定义分类及分类号
		cell = titleRow.createCell(cols);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString("学科分类"));
		cell = titleRow.createCell(cols+1);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString("学科分类号"));
		
		cell = titleRow.createCell(cols+2);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(ToolUnit.NullToBlank(metaStore.getRmstGroupOne())));
		cell = titleRow.createCell(cols+3);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(ToolUnit.NullToBlank(metaStore.getRmstGroupOne() + "分类号")));
		
		cell = titleRow.createCell(cols+4);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(ToolUnit.NullToBlank(metaStore.getRmstGroupTwo())));
		cell = titleRow.createCell(cols+5);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(ToolUnit.NullToBlank(metaStore.getRmstGroupTwo() + "分类号")));
		
		cell = titleRow.createCell(cols+6);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(ToolUnit.NullToBlank(metaStore.getRmstGroupThree())));
		cell = titleRow.createCell(cols+7);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString(ToolUnit.NullToBlank(metaStore.getRmstGroupThree() + "分类号")));
		
		cell = titleRow.createCell(cols+8);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		cell.setCellValue(new HSSFRichTextString("物理文件名"));
		
		
		//第三行开始——列表内容
		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 设置对齐方式
		// 设置单元格边框
		contentStyle.setBorderBottom((short)1);
		contentStyle.setBorderLeft((short)1);
		contentStyle.setBorderRight((short)1);
		contentStyle.setBorderTop((short)1);
		
		// 第二行开始，遍历内容
		Long rsdaId = null;
		Long repoId = null;
		String data = null;
		String[] groups = null;
		Object[] resInfo = null;
		Map<Long, String> eleMap = null;
		for (int i = 0; i < rows; i++) {
			HSSFRow row = sheet.createRow(i+2); //加上第一行和第二行的标题行
			row.setHeight((short) 0x160);
			
			resInfo = reslist.get(i);
			rsdaId = (Long) resInfo[0];
			
			//通过map形式，一次查出当前资源所有元数据值，就不用每个元数据查询一次数据库
			eleMap = getElementVal(rsdaId);  
			for (int j = 0; j < cols; j++) {
				repoId = (Long) elelist.get(j)[0];
				data = eleMap.containsKey(repoId) ? eleMap.get(repoId) : "";
				
				cell = row.createCell(j);
				cell.setCellStyle(contentStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(data));
			}
			
			//添加学科分类
			groups = getSubject(resInfo[1] == null ? null : (Long) resInfo[1]);
			cell = row.createCell(cols);
			cell.setCellStyle(contentStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(groups == null ? "" : groups[0]));
			cell = row.createCell(cols + 1);
			cell.setCellStyle(contentStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(groups == null ? "" : groups[1]));
			
			
			//添加自定义分类一
			groups = getGroupInfo(resInfo[2] == null ? null : (Long) resInfo[2]);
			cell = row.createCell(cols + 2);
			cell.setCellStyle(contentStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(groups == null ? "" : groups[0]));
			cell = row.createCell(cols + 3);
			cell.setCellStyle(contentStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(groups == null ? "" : groups[1]));
			
			//添加自定义分类二
			groups = getGroupInfo(resInfo[3] == null ? null : (Long) resInfo[3]);
			cell = row.createCell(cols + 4);
			cell.setCellStyle(contentStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(groups == null ? "" : groups[0]));
			cell = row.createCell(cols + 5);
			cell.setCellStyle(contentStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(groups == null ? "" : groups[1]));
			
			//添加自定义分类三
			groups = getGroupInfo(resInfo[4] == null ? null : (Long) resInfo[4]);
			cell = row.createCell(cols + 6);
			cell.setCellStyle(contentStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(groups == null ? "" : groups[0]));
			cell = row.createCell(cols + 7);
			cell.setCellStyle(contentStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(new HSSFRichTextString(groups == null ? "" : groups[1]));
			
			//添加物理文件名
			cell = row.createCell(cols + 8);
			cell.setCellStyle(contentStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			if(resInfo[5] != null && resInfo[5].toString().length() > 1){
				String s = resInfo[5].toString();
				s = s.substring(s.lastIndexOf("/") + 1);
				s = s.substring(0, s.lastIndexOf("."));
				
				s = s.replace("_data_s", "");
				s = s.replace("_data", "");
				cell.setCellValue(new HSSFRichTextString(s));
				
				//移动资源附件
				if(CommonUtil.isNotNull(attDir)){
					copyAttachment(s, rsdaId);
				}
			}
			
		}
		
		workbook.write(outStream);
	}
	
	private void copyAttachment(String folder, Long rsdaId){
		String hql = "from ResSourceFile sf where sf.resRsdaId=?";
		List<ResSourceFile> filelist = getHibernateTemplate().find(hql, rsdaId);
		if(filelist == null || filelist.size() < 1){
			return;
		}
		String dir = attDir + folder;
		File f = new File(dir);
		f.mkdirs();
		
		ResSourceFile sf = null;
		File srcFile = null;
		
		File destFile = new File(attDir + folder);
		for(int i=0,s=filelist.size(); i<s; i++){
			sf = filelist.get(i);
			srcFile = new File(rootPath + sf.getResLocalPath());
			
			try {
				//移动源文件
				FileUtils.copyFileToDirectory(srcFile, destFile, true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			try {
				String filename = sf.getResLocalPath();
				filename = filename.substring(0, filename.lastIndexOf("."));
				
				//移动预览文件，由于表中没记录预览文件路径，因此只有几种格式都试着移动
				FileUtils.copyFileToDirectory(new File(rootPath + filename + ".swf"), destFile, true);
				FileUtils.copyFileToDirectory(new File(rootPath + filename + ".jpg"), destFile, true);
				FileUtils.copyFileToDirectory(new File(rootPath + filename + ".mp3"), destFile, true);
				FileUtils.copyFileToDirectory(new File(rootPath + filename + ".flv"), destFile, true);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 获取元数据值
	 *
	 * @author MaChao
	 * @date Nov 7, 2016
	 *
	 * @param rsdaId
	 * @param repoId
	 * @return
	 */
	private String getElementVal(Long rsdaId, Long repoId){
		String hql = "select reda.redaData from ResExtendData reda where reda.redaRsdaId=? and reda.redaRepoId=?";
		Long[] values = {rsdaId, repoId};
		List<String> list = getHibernateTemplate().find(hql, values);
		if(list == null || list.size() < 1 || list.get(0) == null){
			return "";
		}
		String data = list.get(0);
		list = null;
		return data;
	}
	
	private Map<Long, String> getElementVal(Long rsdaId){
		Map<Long, String> map = new HashMap<Long, String>();
		String hql = "select reda.redaRepoId, reda.redaData from ResExtendData reda where reda.redaRsdaId=?";
		List<Object[]> list = getHibernateTemplate().find(hql, rsdaId);
		if(list == null || list.size() < 1 || list.get(0) == null){
			return map;
		}
		
		Object[] arr = null;
		for(int i=0,s=list.size(); i<s; i++){
			arr = list.get(i);
			map.put((Long) arr[0], arr[1] == null ? "" : arr[1].toString());
		}
		list = null;
		return map;
	}
	
	/**
	 * 获取资源学科信息
	 *
	 * @author MaChao
	 * @date Nov 7, 2016
	 *
	 * @param rstyId
	 * @return
	 */
	private String[] getSubject(Long rstyId){
		if(rstyId == null || rstyId < 1L){
			return null;
		}
		String hql = "select rsty.rstyName, rsty.rstyCode from ResSubjectType rsty where rsty.rstyId=?";
		List<Object[]> list = getHibernateTemplate().find(hql, rstyId);
		if(list == null || list.size() < 1 || list.get(0) == null){
			return null;
		}
		Object[] s = list.get(0);
		String[] arr = {s[0] == null ? "" : s[0].toString(), s[1] == null ? "" : s[1].toString()};
		list = null;
		return arr;
	}
	
	/**
	 * 获取自定义分类信息
	 *
	 * @author MaChao
	 * @date Nov 7, 2016
	 *
	 * @param gid 分类id
	 * @return
	 */
	public String[] getGroupInfo(Long gid){
		if(gid == null || gid < 1L){
			return null;
		}
		String hql = "select g.rdtyName, g.rdtyCode from ResDynamicType g where g.rdtyId=?";
		List<Object[]> list = getHibernateTemplate().find(hql, gid);
		if(list == null || list.size() < 1 || list.get(0) == null){
			return null;
		}
		Object[] s = list.get(0);
		String[] arr = {s[0] == null ? "" : s[0].toString(), s[1] == null ? "" : s[1].toString()};
		list = null;
		return arr;
	}
}
