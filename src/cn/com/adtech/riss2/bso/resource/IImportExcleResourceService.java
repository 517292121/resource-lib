package cn.com.adtech.riss2.bso.resource;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResImportHistory;
import cn.com.adtech.riss2.domain.resource.ResMissionInfo;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

public interface IImportExcleResourceService extends ICommonService {

	public List<Object> importResource(File file, HttpServletRequest request,
			ResImportHistory importHistory, ResSourceData basicData);
	
	public String[] getFileNames(String folder, Long rfilid);

	
	public boolean checkMustElement(HttpServletRequest request, File file, ResMissionInfo mission);
	
	public boolean checkTypes(HttpServletRequest request, ResMissionInfo mission,File file);
	
	/**
	 * 获取导入信息
	 * @return
	 */
	public String getMessage();
	
	/**
	 * 获取成功导入的资源数量
	 * @return
	 */
	public int getImportNumber();
	
	/**
	 * 验证是否设置了模板
	 * @param rmstId 特色库ID
	 * @param rltyId 资源类型ID
	 * @return
	 */
	public boolean checkTemplet(long rmstId, long rltyId);
	
	/**
	 * 设置自定义分类匹配类型
	 * @param impDynamicName
	 */
	public void setImpDynamicName(boolean impDynamicName);
}
