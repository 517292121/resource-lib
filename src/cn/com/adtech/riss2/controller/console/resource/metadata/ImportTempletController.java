/***************************************************************************************
 	*	 
 	* 对数据批量导入Excel模板设置控制器
 	*
 	* @see (importTempletEdit.jsp、resourceEdit.jsp、IMetadataElementPoolService.java)
 	* 
 *
 * 开发公司: 重庆亚德科技研发中心
 * 
 * 版权:本文件版权归属重庆亚德科技研发中心
 * 
 	* @author MaChao
 	* 
 	* @since 该文件所支持的浏览器：IE、火狐
 	*
 	* @date  (开发日期): 2010-09-29
 	* 
 	* @version 1.2
 	* 
 *
 * 最后更新日期: 2010-11-23
 *
 * 更改人: 王林
 * 
 * 更改内容:
 *   
 * 复审人:   
 * 
 * 复审时间： 
 * 
 * 复审结果:
 *
 **************************************************************************************/ 
package cn.com.adtech.riss2.controller.console.resource.metadata;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import cn.com.adtech.riss2.bso.resource.IImportTempletService;
import cn.com.adtech.riss2.bso.resource.IMetadataElementPoolService;
import cn.com.adtech.riss2.controller.MyRequestUtil;
import cn.com.adtech.riss2.core.ConstantsObj;
import cn.com.adtech.riss2.core.TempletConfig;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResImportTemplet;

public class ImportTempletController extends MultiActionController{

	protected IMetadataElementPoolService elementPool = null;
	private IImportTempletService importTempletService = null;
	private String message = "";
	
	public void setImportTempletService(IImportTempletService importTempletService) {
		this.importTempletService = importTempletService;
	}
	public void setElementPool(IMetadataElementPoolService elementPool) {
		this.elementPool = elementPool;
	}

	/**
	 * 进入导入模板设置界面
	 */
	public ModelAndView init(HttpServletRequest request, HttpServletResponse response) throws Exception {
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L); //特色库ID
		long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L); //资源类型ID
		List<ResElementPool> elementList = elementPool.getTempletElement(rmstId, rltyId);
		
		//获取分类的模板设置
		Map<String, String> templetMap = elementPool.getTempletElementByGroup(rmstId, rltyId);
		
		String sql = "select RMST_NAME from RES_META_STORE where RMST_ID=" + rmstId;
		String hql = "select rmst.rmstName,rmst.rmstGroupOne,rmst.rmstGroupTwo,rmst.rmstGroupThree from ResMetaStore rmst where rmst.rmstId=" + rmstId; 
		List<Object[]> list = (List<Object[]>)elementPool.find(hql);
		Object[] rmstData =  list.get(0);
		request.setAttribute("rmstName", rmstData[0]);
		
		sql = "select RLTY_NAME from RES_LITER_TYPE where RLTY_ID=" + rltyId;
		request.setAttribute("rltyName", elementPool.getFirstData(sql));
		request.setAttribute("elementList", elementList);
		request.setAttribute("rmstId", rmstId);
		request.setAttribute("rltyId", rltyId);
		request.setAttribute("rmstData", rmstData);
		return new ModelAndView("resource/metadata/importTempletEdit",templetMap);
	}
	
	/**
	 * 保存导入模板
	 */
	public ModelAndView saveTemplet(HttpServletRequest request, HttpServletResponse response) throws Exception {
		message = "";
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L); //特色库ID
		long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L); //资源类型ID
		List<ResElementPool> elementList = elementPool.getElementByTemplet(rmstId, rltyId);
		boolean b = checkForm(request, elementList);
		if(b == false){
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.print("<span style='color:red'><h2>错误提示：</h2><hr>");
			out.print("<span style='font-size:12px;'>" + message + "<br>");
			out.print("<br><a href='javascript:window.history.go(-1);'><<返回重新编辑</a></span></span>");
			return null;
		}
		List<ResImportTemplet> templetElementList = parseDataList(request, elementList);
		b = importTempletService.saveImportTempletList(rmstId,rltyId,templetElementList);
		for (int i = 0; i < templetElementList.size(); i++) {
			ResImportTemplet templet = templetElementList.get(i);
			System.out.println(templet.getRiteRepoId() + "  " + templet.getRiteColumn());
		}
		if(b) {
			request.setAttribute("result", "<script>showMsg('设置成功');</script>");
		}else {
			request.setAttribute("result", "<script>alert('对不起，保存失败！');</script>");
		}
		return init(request, response);
	}
	
	/**
	 * 验证表单
	 * @param request
	 * @param elementList
	 * @return
	 */
	public boolean checkForm(HttpServletRequest request, List<ResElementPool> elementList){
		if(elementList == null || elementList.size() < 1){
			message = "<li>未找到相关元素！</li>";
			return false;
		}
		boolean b = true;
		String columnName = "";
		ResElementPool element = null;
		List<ElementTemp> checkTempList = new LinkedList<ElementTemp>();
		ElementTemp elementTemp = null;
		
		//循环验证所有元素
		for(int i=0,s=elementList.size(); i<s; i++){
			element = elementList.get(i);
			columnName = ServletRequestUtils.getStringParameter(request, "" + element.getRepoId(),"");
			columnName = columnName.trim().toUpperCase();
			if(element.getRepoRestrict() != null && element.getRepoRestrict().toUpperCase().equals("M") && columnName.length() < 1){
				message += "<li>元素：\""+element.getRepoTitle()+"\"为必备，但未设置对应的列</li>";
				b = false;
			}else{
				elementTemp = new ElementTemp();
				elementTemp.setElementId(element.getRepoId());
				elementTemp.setElementName(element.getRepoTitle());
				elementTemp.setExcelColumn(columnName);
				checkTempList.add(elementTemp);				
			}
		}
		
		// 分类改为直接追回到模板最后。文件名已更新改用资源的题名作为文件名，无须再手动设置 //
		
		//学科分类
		if(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RSTY_ID,"").trim().length() > 0){
			elementTemp = new ElementTemp();
			elementTemp.setElementId(ResImportTemplet.RSDA_RSTY_ID);
			elementTemp.setElementName("学科分类");
			elementTemp.setExcelColumn(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RSTY_ID,"").trim());
			checkTempList.add(elementTemp);	
		}
		
		//中图法分类
		if(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_MIDDLENAME,"").trim().length() > 0){
			elementTemp = new ElementTemp();
			elementTemp.setElementId(ResImportTemplet.RSDA_MIDDLENAME_ID);
			elementTemp.setElementName("中图法分类");
			elementTemp.setExcelColumn(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_MIDDLENAME,"").trim());
			checkTempList.add(elementTemp);	
		}
		
		//自定义分类一
		if(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RDTY_ONE,"").trim().length() > 0){
			elementTemp = new ElementTemp();
			elementTemp.setElementId(ResImportTemplet.RSDA_RDTY_ONE_ID);
			elementTemp.setElementName(ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_ONE));
			elementTemp.setExcelColumn(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RDTY_ONE,"").trim());
			checkTempList.add(elementTemp);	
		}
		
		//自定义分类二
		if(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RDTY_TWO,"").trim().length() > 0){
			elementTemp = new ElementTemp();
			elementTemp.setElementId(ResImportTemplet.RSDA_RDTY_TWO_ID);
			elementTemp.setElementName(ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_TWO));
			elementTemp.setExcelColumn(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RDTY_TWO,"").trim());
			checkTempList.add(elementTemp);	
		}
		
		//自定义分类三
		if(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RDTY_THREE,"").trim().length() > 0){
			elementTemp = new ElementTemp();
			elementTemp.setElementId(ResImportTemplet.RSDA_RDTY_THREE_ID);
			elementTemp.setElementName(ConstantsObj.getSysProperty(TempletConfig.RSDA_RDTY_THREE));
			elementTemp.setExcelColumn(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RDTY_THREE,"").trim());
			checkTempList.add(elementTemp);	
		}
		
		//文件名
		if(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_FILENAME,"").trim().length() > 0){
			elementTemp = new ElementTemp();
			elementTemp.setElementId(ResImportTemplet.RSDA_FILENAME_ID);
			elementTemp.setElementName("文件名");
			elementTemp.setExcelColumn(ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_FILENAME,"").trim());
			checkTempList.add(elementTemp);	
		}
		
		//效验设置的列是否有重复
		if(checkSame(checkTempList) == false){
			b = false;
		}
		return b;
	}
	
	/**检查各元素设置的Excel有没有重复**/
	private boolean checkSame(List<ElementTemp> elementTempList){
		boolean b = true;
		if(elementTempList == null || elementTempList.size() < 1){
			return b;
		}
		String parentElementName = "",parentColumnName = "",childElementName = "",childColumnName = "";
		Long parentElementId,childElementId;
		List<String> checks = new LinkedList<String>();
		for(int i=0,s=elementTempList.size(); i<s; i++){
			parentElementName = elementTempList.get(i).getElementName().toUpperCase();
			parentColumnName = elementTempList.get(i).getExcelColumn().toUpperCase();
			parentElementId = elementTempList.get(i).getElementId();
			for(int j=0; j<s; j++){
				childElementId = elementTempList.get(j).getElementId();
				if(i == j){
					continue;
				}
				if(checks.contains(parentElementId + "-" + childElementId) || checks.contains(childElementId + "-" + parentElementId)){
					continue;
				}
				childElementName = elementTempList.get(j).getElementName().toUpperCase();
				childColumnName = elementTempList.get(j).getExcelColumn().toUpperCase();
				if(childColumnName != null && childColumnName.length() > 0 && childColumnName.equals(parentColumnName)){
					message += "<li>元素‘"+parentElementName+"’与元素‘"+childElementName+"’所设置的列相冲突</li>";
					checks.add(parentElementId + "-" + childElementId);
					b = false;
				}
			}
		}
		return b;
	}
	
	/**封装设置的模板元素到集合**/
	private List<ResImportTemplet> parseDataList(HttpServletRequest request, List<ResElementPool> elementList){
		long rmstId = ServletRequestUtils.getLongParameter(request, "rmstId", 0L); //特色库ID
		long rltyId = ServletRequestUtils.getLongParameter(request, "rltyId", 0L); //资源类型ID
		List<ResImportTemplet> templetElementList = new LinkedList<ResImportTemplet>();
		ResImportTemplet templetElement = null;
		ResElementPool element = null;
		String columnName = "";
		
		//封元素设置
		for(int i=0,s=elementList.size(); i<s; i++){
			element = elementList.get(i);
			columnName = ServletRequestUtils.getStringParameter(request, "" + element.getRepoId(), "").toUpperCase();
			templetElement = new ResImportTemplet(rmstId,rltyId,element.getRepoId(), columnName);
			templetElementList.add(templetElement);
		}
		
		//学科分类
		String rstyColumn = ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RSTY_ID,"").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.RSDA_RSTY_ID,rstyColumn);
		templetElementList.add(templetElement);
		
		//中图法分类
		String rmtyColumn = ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_MIDDLENAME,"").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.RSDA_MIDDLENAME_ID,rmtyColumn);
		templetElementList.add(templetElement);
		
		//自定义分类一
		String rdtyColumn1 = ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RDTY_ONE,"").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.RSDA_RDTY_ONE_ID,rdtyColumn1);
		templetElementList.add(templetElement);
		
		//自定义分类二
		String rdtyColumn2 = ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RDTY_TWO,"").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.RSDA_RDTY_TWO_ID,rdtyColumn2);
		templetElementList.add(templetElement);
		
		//自定义分类三
		String rdtyColumn3 = ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_RDTY_THREE,"").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.RSDA_RDTY_THREE_ID,rdtyColumn3);
		templetElementList.add(templetElement);
		
		//文件名
		String rsdaFilename = ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_FILENAME,"").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.RSDA_FILENAME_ID,rsdaFilename);
		templetElementList.add(templetElement);
		
		//资源主题
		String rsdaSourceSubject = ServletRequestUtils.getStringParameter(request, TempletConfig.RSDA_SOURCE_SUBJECT,"").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.RSDA_RSSU_ID,rsdaSourceSubject);
		templetElementList.add(templetElement);
		/**
		//封面文件
		String imageColumn = ServletRequestUtils.getStringParameter(request, "imagefile","").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.IMAGE_FILE_ID,imageColumn);
		templetElementList.add(templetElement);
		
		//预览源文件
		String viewSourceColumn = ServletRequestUtils.getStringParameter(request, "viewfile","").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.VIEW_SOURCE_FILE_ID,viewSourceColumn);
		templetElementList.add(templetElement);
		
		//预览文件转换文件
		String convertViewColumn = ServletRequestUtils.getStringParameter(request, "convertViewfile","").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.VIEW_CONVERT_FILE_ID,convertViewColumn);
		templetElementList.add(templetElement);
		
		//源文件
		String sourcefileColumn = ServletRequestUtils.getStringParameter(request, "sourcefile","").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.SOURCE_SOURCE_FILE_ID,sourcefileColumn);
		templetElementList.add(templetElement);
		
		//源文件转换文件
		String convertSourcefileColumn = ServletRequestUtils.getStringParameter(request, "convertSourcefile","").trim().toUpperCase();
		templetElement = new ResImportTemplet(rmstId,rltyId,ResImportTemplet.SOURCE_CONVERT_FILE_ID,convertSourcefileColumn);
		templetElementList.add(templetElement);
		**/
		
		
		return templetElementList;
	}
}


class ElementTemp{
	private Long elementId;
	private String elementName;
	private String excelColumn;
	public Long getElementId() {
		return elementId;
	}
	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}
	public String getElementName() {
		return elementName;
	}
	public void setElementName(String elementName) {
		this.elementName = elementName;
	}
	public String getExcelColumn() {
		return excelColumn;
	}
	public void setExcelColumn(String excelColumn) {
		this.excelColumn = excelColumn;
	}
}
