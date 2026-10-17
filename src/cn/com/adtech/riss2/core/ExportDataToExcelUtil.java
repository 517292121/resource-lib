package cn.com.adtech.riss2.core;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.adtech.export.ExportExcel;
import com.adtech.export.config.Align;
import com.adtech.export.config.CellColConfig;
import com.adtech.export.config.ColHeaderColConfig;
import com.adtech.export.config.ColHeaderConfig;
import com.adtech.export.config.ColHeaderRowConfig;
import com.adtech.export.config.FooterConfig;
import com.adtech.export.config.TableConfig;
import com.adtech.export.config.TitleConfig;

/**
 * @copyright adtech
 * 类说明:导出表格数据到excel
 * @author MaChao
 * @version 创建时间：2011-1-13
 */

public class ExportDataToExcelUtil {

	/**
	 * 说明：统计表格构造
	 *@param
	 *@return
	 *@author MaChao
	 *@date 2011-1-13
	 */
	public static TableConfig buildTableConfigForExcelUseState(String title,
			String code, String top, String footer) {
		// 标题
		TitleConfig titleConfig = new TitleConfig();
		titleConfig.setAlign("center");
		titleConfig.setContent(top);
		titleConfig.setBackgroundGray(false);
		titleConfig.setFontBold(true);
		titleConfig.setFontPointSize(10);
		titleConfig.setHeight(10);
		titleConfig.setBorder(false);
		// 表格
		TableConfig tableConfig = new TableConfig();
		tableConfig.setTitle(titleConfig);
		ColHeaderConfig colHeaderConfig = new ColHeaderConfig();
		List<ColHeaderRowConfig> rows = new ArrayList<ColHeaderRowConfig>();
		ColHeaderRowConfig rowConfig = new ColHeaderRowConfig();
		List<ColHeaderColConfig> rowConfigCols = new ArrayList<ColHeaderColConfig>();
		String[] titles = title.trim().split("\\,");
		String[] codes = code.trim().split("\\,");
		List<CellColConfig> colConfigList = new ArrayList<CellColConfig>();
		for (int i = 0; i < titles.length; i++) {
			// 列头
			ColHeaderColConfig colConfig = new ColHeaderColConfig();
			colConfig.setContent(titles[i]);
			colConfig.setAlign(Align.CENTER);
			colConfig.setFontBold(true);
			rowConfigCols.add(colConfig);
			// 单元格
			CellColConfig cellColConfig = new CellColConfig();
			cellColConfig.setPosition((short) (i + 1));
			cellColConfig.setAlign(Align.CENTER);
			cellColConfig.setWidth(50);
			cellColConfig.setColHeader(titles[i]);
			cellColConfig.setFieldName(codes[i]);
			colConfigList.add(cellColConfig);
		}
		rowConfig.setCols(rowConfigCols);
		rows.add(rowConfig);
		colHeaderConfig.setRows(rows);
		tableConfig.setColHeader(colHeaderConfig);
		tableConfig.setCellCols(colConfigList);
		tableConfig.setLockedWidth(true);
		FooterConfig foot = new FooterConfig();
		foot.setContent(footer);
		foot.setAlign("left");
		foot.setFontPointSize(8);
		foot.setFontBold(true);
		tableConfig.setFooter(foot);
		return tableConfig;
	}
	

      
	/*
	 * @param List<javaBean>
	 */
	public static void writeExcel(List objList, TableConfig tableConfig,
			HttpServletResponse response) throws IOException {
		ExportExcel exportExcel = new ExportExcel();
		exportExcel.setSheetName("导出");
		OutputStream output = response.getOutputStream();
		BufferedOutputStream out = new BufferedOutputStream(output);
		int result = exportExcel.exportExcel(objList, tableConfig, out);

	}
}

