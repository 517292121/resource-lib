package cn.com.adtech.riss2.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import cn.com.adtech.riss2.cache.SysConfigInfo;
import cn.com.adtech.riss2.domain.ResFilesystem;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResImportTemplet;
import cn.com.adtech.riss2.domain.resource.ResMetaStore;
import cn.com.adtech.riss2.exception.ErrorMessageException;
import cn.com.adtech.riss2.jdbcms.ToolUnit;

public class ExcelUtil extends ExcelTool {

	private String fileName = "";

	/**
	 * 工作表名称
	 */
	private String sheetName = "";

	private String filePath = "";

	public String getFilePath() {
		return filePath;
	}

	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public ExcelUtil() {

	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public ExcelUtil(String filename) {
		this.fileName = filename;
	}

	/**
	 * 导出元数据Excel模板
	 * 
	 * @param headArr
	 * @param outStream
	 */
	public void createExcelTemplet(String[] headArr, OutputStream outStream) {
		if (headArr == null || headArr.length < 1) {
			return;
		}
		HSSFWorkbook workbook = new HSSFWorkbook(); // 创建对Excel工作簿文件的引用

		if (sheetName == null || sheetName.length() < 1) {
			sheetName = "工作簿1";
		}
		HSSFSheet sheet = workbook.createSheet(sheetName); // 创建第一张工作表，本方法只支持一张工作表

		HSSFRow row = sheet.createRow(0); // 创建第一行

		HSSFCell cell = null;
		for (int i = 0; i < headArr.length; i++) {
			cell = row.createCell(i);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(ToolUnit.NullToBlank(headArr[i]));
		}
		try {
			workbook.write(outStream);
		} catch (FileNotFoundException e) {
			System.out.println("发生FileNotFoundException异常");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("发生IOException异常");
			e.printStackTrace();
		}

	}

	/**
	 * 导出元数据Excel模板
	 * 
	 * @param headArr
	 * @param outStream
	 */
	public void createExcelTemplet(List<ResElementPool> elementList,
			OutputStream outStream, ResMetaStore store, ResFilesystem fileSystem, String rltyName, List<ResImportTemplet> templetList) {
		if (elementList == null || elementList.size() < 1) {
			return;
		}
		HSSFWorkbook workbook = new HSSFWorkbook(); // 创建对Excel工作簿文件的引用
		HSSFCell cell = null;

		if (sheetName == null || sheetName.length() < 1) {
			sheetName = "工作簿1";
		}
		HSSFSheet sheet = workbook.createSheet(sheetName); // 创建第一张工作表，本方法只支持一张工作表

		HSSFRow titleRow = sheet.createRow(0);
		titleRow.setHeight((short) 0x160);
		HSSFFont titlefont = workbook.createFont();
		titlefont.setColor(HSSFFont.COLOR_NORMAL);
		
		HSSFCellStyle titleStyle = workbook.createCellStyle();
		titleStyle.setFont(titlefont);
		titleStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER); 
		cell = titleRow.createCell(0);
		cell.setCellStyle(titleStyle);
		cell.setCellType(HSSFCell.CELL_TYPE_STRING);
		
		String title = "【" + store.getRmstName() + "--" + rltyName + "】";
		if(fileSystem == null){
			title += "  导入数据前，请将文件拷贝到本地服务器:";
			title += SysConfigInfo.RES_DIR;
			title += store.getRmstFolder(); 
			title += "下相关作业文件夹中！";
		}else{
			title += "  导入数据前，请将文件拷贝到服务器:";
			title += fileSystem.getRfilName() + "(" + fileSystem.getRfilFtpHost() + ")";
			title += " 的" + store.getRmstFolder();
			title += "下相关作业文件夹中！";
		}
		cell.setCellValue(title);

		HSSFRow row = sheet.createRow(1); // 创建第一行
		row.setHeight((short) 0x160);

		ResElementPool templetElement = null;
		int cells;
		String titleValue;
		String titleName;
		int maxCell = 0;
		for (int i = 0, s = elementList.size(); i < s; i++) {
			templetElement = elementList.get(i);
			cells = ExcelTool.getColumnIndex(templetElement.getExcelColumn());
			if(cells > maxCell){
				maxCell = cells;
			}
			if (cells > 0) {
				cell = row.createCell(cells - 1);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				titleValue = ToolUnit
						.NullToBlank(templetElement.getRepoTitle());
				titleName = ToolUnit.NullToBlank(templetElement.getRepoName());
				if (titleName.length() > 0) {
					titleValue += "(" + titleName + ")";
				}
				if(templetElement.getRepoRestrict() != null && templetElement.getRepoRestrict().equals("M")){
					titleValue = "*" + titleValue;
				}
				cell.setCellValue(titleValue);
			}
		}
		
		if(templetList != null && templetList.size() > 0){
			ResImportTemplet templet = null;
			int columnIndex = 0;
			for(int i=0,s=templetList.size(); i<s; i++){
				templet = templetList.get(i);
				if (CommonUtil.isNotNull(templet.getRiteColumn())){
					columnIndex = ExcelTool.getColumnIndex(templet.getRiteColumn());
					if(columnIndex > 0){
						cell = row.createCell(columnIndex - 1);
						cell.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell.setCellValue(TempletConfig.getGroupElementName(templet.getRiteRepoId()));
					}
				}
			}
		}
		try {
			workbook.write(outStream);
		} catch (FileNotFoundException e) {
			System.out.println("发生FileNotFoundException异常");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("发生IOException异常");
			e.printStackTrace();
		}

	}
	
	/**
	 * 制作导出模板
	 * @param outStream 输出流
	 * @param list 输出的对象结果集
	 * @param titles 标题列表
	 * @param startIndex 开始位置
	 * @param columnLength 显示列长度
	 * @param lastContent 相关提示信息
	 * @author MaChao
	 * @date 2011-1-24
	 */
	public void exportToExcel(OutputStream outStream,List<Object[]> list,String[] titles,int startIndex,int columnLength,String lastContent) { 

		if (list == null || list.size() < 1 || titles == null || titles.length < 1 || columnLength <= 0) {
			return;
		}
		if(startIndex < 0){
			startIndex = 0;
		}
		Object[] temp = list.get(0);
		if(temp.length < columnLength){
			throw new ErrorMessageException("指定显示的列数过大！");
		}
		// 标题列长度
		int columnNumber = titles.length;
		
		HSSFWorkbook workbook = new HSSFWorkbook(); // 创建对Excel工作簿文件的引用
		HSSFCell cell = null;

		if (sheetName == null || sheetName.length() < 1) {
			sheetName = "工作簿1";
		}
		int rowNumber = 0;
		
		HSSFSheet sheet = workbook.createSheet(sheetName); // 创建第一张工作表，本方法只支持一张工作表
		// 设置单元格宽度
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 5000);
		
//		Region region = new Region(0,(short)0,0,(short)(titles.length-1));
//		sheet.addMergedRegion(region);
		
		// 第一行，显示大标题或者是提示信息
		HSSFRow infoRow = sheet.createRow(rowNumber);
		rowNumber++;
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
		
		for(int i = 0; i < columnNumber; i++) {
			cell = infoRow.createCell(i);
			cell.setCellStyle(infoStyle);
			if(i == 0){
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(sheetName);
			}
		}

		HSSFRow titleRow = sheet.createRow(rowNumber);
		rowNumber++;
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
		
		// 第二行，添加标题内容
		for (int i = 0; i < columnNumber; i++) {
			cell = titleRow.createCell(i);
			cell.setCellStyle(titleStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(ToolUnit.NullToBlank(titles[i]));
		}
		
		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 设置对齐方式
		// 设置单元格边框
		contentStyle.setBorderBottom((short)1);
		contentStyle.setBorderLeft((short)1);
		contentStyle.setBorderRight((short)1);
		contentStyle.setBorderTop((short)1);
		
		// 第三行开始，遍历内容
		for (int i = 0, s = list.size(); i < s; i++) {
			HSSFRow row = sheet.createRow(rowNumber); // 创建第一行
			rowNumber++;
			row.setHeight((short) 0x160);
			Object[] obj = list.get(i);
			for (int j = startIndex; j < columnLength + startIndex ; j++) {
				cell = row.createCell(j-startIndex);
				cell.setCellStyle(contentStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(obj[j] == null ? "" : obj[j].toString());
			}
		}
		
		if(ServiceUtil.isNotNull(lastContent)){
			// 最后一行，该行主要用于显示总记录数
//			Region lastRegion = new Region(rowNumber,(short)0,rowNumber,(short)(titles.length-1));
//			sheet.addMergedRegion(lastRegion);
			HSSFRow lastRow = sheet.createRow(rowNumber);
			lastRow.setHeight((short) 0x180);// 设置行高
			HSSFFont lastFont = workbook.createFont();// 设置字体
			lastFont.setColor(HSSFFont.COLOR_NORMAL);// 设置字体颜色
			lastFont.setBoldweight((short)1000);// 设置字体类型
			HSSFCellStyle lastStyle = workbook.createCellStyle();
			lastStyle.setFont(lastFont);// 添加字体样式
			lastStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 设置对齐方式
			// 设置单元格边框
			lastStyle.setBorderBottom((short)1);
			lastStyle.setBorderLeft((short)1);
			lastStyle.setBorderRight((short)1);
			lastStyle.setBorderTop((short)1);
			
			for(int i = 0; i < columnNumber; i++) {
				cell = lastRow.createCell(i);
				cell.setCellStyle(lastStyle);
				if(i == 0){
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell.setCellValue(lastContent);
				}
			}
		}
		
		
		
		try {
			workbook.write(outStream);
		} catch (FileNotFoundException e) {
			System.out.println("发生FileNotFoundException异常");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("发生IOException异常");
			e.printStackTrace();
		}
	} 
	
	/**
	 * 导出Excel列表数据
	 * @param outStream
	 * @param list
	 * @param titles
	 * @param lastContent
	 */
	public void exportListToExcel(OutputStream outStream,List<Object[]> list,String[] titles,String lastContent) { 

		if (list == null || list.size() < 1 || titles == null || titles.length < 1) {
			return;
		}
		HSSFWorkbook workbook = new HSSFWorkbook(); // 创建对Excel工作簿文件的引用
		HSSFCell cell = null;

		if (sheetName == null || sheetName.length() < 1) {
			sheetName = "工作簿1";
		}
		int rowNumber = 0;
		
		HSSFSheet sheet = workbook.createSheet(sheetName); // 创建第一张工作表，本方法只支持一张工作表
		// 设置单元格宽度
		sheet.setColumnWidth(0, 7000);
		sheet.setColumnWidth(1, 5000);
		sheet.setColumnWidth(2, 5000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 5000);
		
//		Region region = new Region(0,(short)0,0,(short)(titles.length-1));
//		sheet.addMergedRegion(region);
		
		// 第一行，显示大标题或者是提示信息
		HSSFRow infoRow = sheet.createRow(rowNumber);
		rowNumber++;
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
		
		for(int i = 0; i < titles.length; i++) {
			cell = infoRow.createCell(i);
			cell.setCellStyle(infoStyle);
			if(i == 0){
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(sheetName);
			}
		}

		HSSFRow titleRow = sheet.createRow(rowNumber);
		rowNumber++;
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
		
		// 第二行，添加标题内容
		for (int i = 0; i < titles.length; i++) {
			cell = titleRow.createCell(i);
			cell.setCellStyle(titleStyle);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(ToolUnit.NullToBlank(titles[i]));
		}
		
		HSSFCellStyle contentStyle = workbook.createCellStyle();
		contentStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 设置对齐方式
		// 设置单元格边框
		contentStyle.setBorderBottom((short)1);
		contentStyle.setBorderLeft((short)1);
		contentStyle.setBorderRight((short)1);
		contentStyle.setBorderTop((short)1);
		
		// 第三行开始，遍历内容
		for (int i = 0, s = list.size(); i < s; i++) {
			HSSFRow row = sheet.createRow(rowNumber); // 创建第一行
			rowNumber++;
			row.setHeight((short) 0x160);
			Object[] obj = list.get(i);
			for (int j = 0; j < obj.length; j++) {
				cell = row.createCell(j);
				cell.setCellStyle(contentStyle);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(obj[j] == null ? "" : obj[j].toString());
			}
		}
		
		// 最后一行，该行主要用于显示总记录数
		
//		Region lastRegion = new Region(rowNumber,(short)0,rowNumber,(short)(titles.length-1));
//		sheet.addMergedRegion(lastRegion);
		HSSFRow lastRow = sheet.createRow(rowNumber);
		lastRow.setHeight((short) 0x180);// 设置行高
		HSSFFont lastFont = workbook.createFont();// 设置字体
		lastFont.setColor(HSSFFont.COLOR_NORMAL);// 设置字体颜色
		lastFont.setBoldweight((short)1000);// 设置字体类型
		HSSFCellStyle lastStyle = workbook.createCellStyle();
		lastStyle.setFont(lastFont);// 添加字体样式
		lastStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT); // 设置对齐方式
		// 设置单元格边框
		lastStyle.setBorderBottom((short)1);
		lastStyle.setBorderLeft((short)1);
		lastStyle.setBorderRight((short)1);
		lastStyle.setBorderTop((short)1);
		
		
		for(int i = 0; i < titles.length; i++) {
			cell = lastRow.createCell(i);
			cell.setCellStyle(lastStyle);
			if(i == 0){
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(lastContent);
			}
		}
		
		try {
			workbook.write(outStream);
		} catch (FileNotFoundException e) {
			System.out.println("发生FileNotFoundException异常");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("发生IOException异常");
			e.printStackTrace();
		}
	} 
	
	/**
	 * 导出Excel列表数据
	 * 
	 * @param outStream
	 * @param list
	 * @param titles
	 *
	 * @author MaChao
	 * @date 2018-8-21
	 */
	public void exportListToExcel(OutputStream outStream,List<Object[]> list,String[] titles) { 
		if (list == null || list.size() < 1 || titles == null || titles.length < 1) {
			return;
		}
		
		HSSFWorkbook workbook = new HSSFWorkbook(); // 创建对Excel工作簿文件的引用
		HSSFCell cell = null;
		if (sheetName == null || sheetName.length() < 1) {
			sheetName = "sheet1";
		}
		
		HSSFSheet sheet = workbook.createSheet(sheetName); // 创建第一张工作表，本方法只支持一张工作表
		
		// 标题行
		HSSFRow row = sheet.createRow(0);
		row.setHeight((short) 0x200);// 设置行高
		// 设置单元格样式
		HSSFFont titlefont = workbook.createFont();// 设置字体
		titlefont.setColor(HSSFFont.COLOR_NORMAL);// 设置字体颜色
		titlefont.setBoldweight((short)1000);// 设置字体类型
		HSSFCellStyle style = workbook.createCellStyle();
		style.setFont(titlefont);// 添加字体样式
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 设置对齐方式
		style.setBorderBottom((short)1);// 设置单元格边框
		style.setBorderLeft((short)1);
		style.setBorderRight((short)1);
		style.setBorderTop((short)1);
		// 填充标题
		for (int i = 0; i < titles.length; i++) {
			sheet.setColumnWidth(i, 5000);//设置单元格宽度
			cell = row.createCell(i);
			cell.setCellStyle(style);
			cell.setCellType(HSSFCell.CELL_TYPE_STRING);
			cell.setCellValue(ToolUnit.NullToBlank(titles[i]));
		}
		
		// 内容行
		style = workbook.createCellStyle();
		style.setAlignment(HSSFCellStyle.ALIGN_LEFT); // 设置对齐方式
		style.setBorderBottom((short)1);// 设置单元格边框
		style.setBorderLeft((short)1);
		style.setBorderRight((short)1);
		style.setBorderTop((short)1);
		// 填充内容
		int rowNumber = 1;
		Object[] obj = null;
		for (int i = 0, s = list.size(); i < s; i++) {
			row = sheet.createRow(rowNumber);
			row.setHeight((short) 0x180);
			obj = list.get(i);
			for (int j = 0; j < obj.length; j++) {
				cell = row.createCell(j);
				cell.setCellStyle(style);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(obj[j] == null ? "" : obj[j].toString());
			}
			rowNumber++;
		}
		
		try {
			workbook.write(outStream);
		} catch (FileNotFoundException e) {
			System.out.println("发生FileNotFoundException异常");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("发生IOException异常");
			e.printStackTrace();
		}
	} 

}
