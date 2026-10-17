package cn.com.adtech.riss2.bso.resource;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import cn.com.adtech.riss2.bso.ICommonService;
import cn.com.adtech.riss2.domain.resource.ResElementPool;
import cn.com.adtech.riss2.domain.resource.ResImportTemplet;
import cn.com.adtech.riss2.domain.resource.ResSourceData;

public interface IMetadataElementPoolService extends ICommonService{

	public List<ResElementPool> getElementList(long rsty);
	
	public Serializable saveElement(ResElementPool element);
	
	/**
	 * 模拟特色库和资源类型(模板)获取不包含的元素
	 * @param store 特色库ID
	 * @param sourceType 资源类型
	 * @param eleType 元素类型
	 * @return
	 */
	public List<ResElementPool> getElementByUnTemplet(long store, long sourceType, int eleType);
	
	/**
	 * 模拟特色库和资源类型(模板)获取包含的元素
	 * @param store 特色库ID
	 * @param sourceType 资源类型
	 * @param eleType 元素类型
	 * @return
	 */
	public List<ResElementPool> getElementByTemplet(long store, long sourceType);
	
	public List<ResElementPool> getTempletElement(long store, long sourceType);
	
	/**
	 * 获取模板中除元素外，其它分类和文件名的对应对象
	 * @param store
	 * @param sourceType
	 * @return
	 */
	public Map<String, String> getTempletElementByGroup(long store, long sourceType);
	
	/**
	 * 获取模板中除元素外，其它分类和文件名的对应对象
	 * @param store
	 * @param sourceType
	 * @return
	 */
	public List<ResImportTemplet> getTempletByGroup(long store, long sourceType);
	
	
	
	/**
	 * 根据ID获取元素
	 */
	public ResElementPool getElementById(long repoId);
	
	public boolean updateElement(ResElementPool element);
	
	/**
	 * 查询出模板中拥有的元素ID
	 */
	public List<Long> getElementIdByTemplet(long store, long sourceType);
	
	
	/**
	 * 保存资源
	 * @param resource
	 * @return 保存后的ID
	 */
	public long saveResource(ResSourceData resource);
	
	
	/**
	 * 修改资源
	 * @param resource
	 * @return
	 */
	public boolean updateResource(ResSourceData resource);
	
}













