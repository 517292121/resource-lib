package cn.com.adtech.riss2.bso.resource;

import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResImportTemplet;

public interface IImportTempletService extends ICommonService{

	/**
	 * 保存模板元素
	 * @param rmstId特色库ID
	 * @param rltyId 资源类型ID
	 * @param templetElements
	 * @return
	 */
	public boolean saveImportTempletList(Long rmstId,Long rltyId,List<ResImportTemplet> templetElements);
	
	
	/**
	 * 获取资源除了基本信息之外的包括（中图法分类，学科分类，各种文件）相对应的Excel列
	 * @param rmstId
	 * @param rltyId
	 * @return
	 */
	public Map<Long, String> getOtherElementData(Long rmstId, Long rltyId);
	
	/**
	 * 获取模板中设置的元素对应关系
	 * @param rmstId 特色库ID
	 * @param rltyId 资源类型ID
	 * return Map<Long, String> long：元素ID、String：对应的Excel列名
	 * @return
	 */
	public Map<Long, String> getTempletElement(Long rmstId, Long rltyId);
	
	/**
	 * 获取模板中的元素，主要封装了元素ID，对应的列，元素名称
	 * @param rmstId
	 * @param rltyId
	 * @return
	 */
	public List<ResImportTemplet> getTempletElementList(Long rmstId, Long rltyId);
	
	/**
	 * 获取模板中一共设置了多少列
	 * @param rmstId
	 * @param rltyId
	 * @return
	 */
	public int getTempletNumber(long rmstId, long rltyId);
}
